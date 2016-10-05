package com.zlion.controller;

import com.zlion.model.Location;
import com.zlion.service.UavService;
import com.zlion.util.GeohashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzs on 2016/9/2.
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/uav")
public class UavController {

    private UavService uavService;

    @Autowired
    public UavController(UavService uavService) {
        this.uavService = uavService;
    }

    /**
     *
     *
     * @api {get} /uav/locations 分页查询无人机的位置信息,按照时间和无人机的uuid来查询
     * @apiName Locations for uav
     * @apiGroup Uav
     * @apiVersion 0.2.0
     *
     * @apiParam {String} uuid UUID of the uav.
     * @apiParam {String} beginTime Locations from Date (Date Time Format: yyyy-MM-dd hh).
     * @apiParam {String} endTime Locations to Date (Date Time Format: yyyy-MM-dd hh).
     * @apiParam {Number} page Present page index.
     * @apiParam {Number} rows Number of locations for one page.
     *
     * @apiSource {Number} Code Return code of state
     * @apiSource {String} Msg Msg of state
     * @apiSource {[Locations,..]} Data One page data for locations
     * @apiSource {Number} Counts Total number of elements for locations
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *       "Code": 200,
     *       "Msg": "ok"
     *     }
     *
     * @apiError AuthException User can't list this uav locations.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 101 AuthException
     *     {
     *       "Code": 101,
     *       "Msg": "User can't show locations of uav with uuid:{uuid}"
     *     }
     *
     */
    @ResponseBody
    @RequestMapping(value = "/locations", method = RequestMethod.GET)
    public Result uavLocations(HttpServletRequest request, HttpSession session){
        Result jsonRender = new Result();

        Long userId = (Long) session.getAttribute("authId");
        String uuid = request.getParameter("uuid");

        if (session.getAttribute("adminId") != null || uavService.checkUuidOwnerWithLoginUser(uuid, userId)){
            jsonRender = jsonRender.okForList();

            String strBeginTime = request.getParameter("beginTime");
            String strEndTime = request.getParameter("endTime");

            int page = 1, rows = 10;
            //分页的基本参数，根据需要自己设置需要的参数把
            if (!(request.getParameter("page").equals("")||request.getParameter("rows")==null)){
                page = Integer.parseInt(request.getParameter("page"));
                rows = Integer.parseInt(request.getParameter("rows"));
            }

            List<Location> locationlist = uavService.getLocationsByTime(uuid, strBeginTime, strEndTime, page, rows);

            jsonRender.put("Date",locationlist);
            jsonRender.put("Counts",uavService.getCountvalue());
        }
        else{
            jsonRender = jsonRender.needAuth();
            jsonRender.put("Msg", "User can't show locations of uav with uuid:" + uuid);
        }

        return jsonRender;
    }

    /*
    无人机通过该接口添加无人的位置记录
     */
    @ResponseBody
    @RequestMapping(value = "/location/add", method = RequestMethod.POST)
    public void addUavLocation(HttpServletRequest request){

        String uuid = request.getParameter("uuid");
        double latitude = Double.parseDouble(request.getParameter("latitude"));
        double longitude = Double.parseDouble(request.getParameter("longitude"));
        double height = Double.parseDouble(request.getParameter("height"));

        String geohash = GeohashUtil.encode(latitude, longitude, 6);

        uavService.addLocation(uuid,latitude,longitude,height);
    }

    /**
     * geohash的位置编码，用来判断用法的，先不用测试
     * @param request (latitude,longitude,beginDate,endDate)
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/location/encode", method = RequestMethod.GET)
    public Result getApplyLocation(HttpServletRequest request, HttpSession session){

//        String uuid = request.getParameter("uuid");
        double latitude = Double.parseDouble(request.getParameter("latitude"));
        double longitude = Double.parseDouble(request.getParameter("longitude"));
        String strBeginDate = request.getParameter("beginDate");
        String strEndDate = request.getParameter("endDate");

        String geohash = GeohashUtil.encode(latitude, longitude, 6);

        double [] locations = GeohashUtil.decode(geohash);
        double minLat = locations[0] - locations[2];
        double maxLat = locations[0] + locations[2];
        double minLng = locations[1] - locations[3];
        double maxLng = locations[1] + locations[3];

        Result jsonRender = new Result();

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("minLat", minLat);
        data.put("maxLat", maxLat);
        data.put("minLng", minLng);
        data.put("maxLng", maxLng);

        jsonRender.put("geohash", geohash);
        jsonRender.put("locations", data);

        if (strBeginDate != null && strEndDate != null){
            Map<String, Object> result = uavService.getBlockApplyState(geohash, strBeginDate, strEndDate);
        }
        else if (strBeginDate != null){

        }
        else if (strBeginDate == null && strEndDate != null){
            jsonRender = jsonRender.argError();
        }
        else{

        }

        return jsonRender;
    }

    @ResponseBody
    @RequestMapping(value = "/block/apply", method = RequestMethod.GET)
    public Result getApplyList(HttpServletRequest request, HttpSession session){

        Result jsonRender = new Result();



        return jsonRender;
    }

}
