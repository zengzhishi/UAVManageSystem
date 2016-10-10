package com.zlion.controller;

import com.alibaba.fastjson.JSON;
import com.zlion.model.BlockApplication;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.*;

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
     * @api {get} /uav/locations 分页查询无人机的位置信息,按照时间和无人机的uuid来查询
     * @apiName Locations for uav
     * @apiGroup Uav
     * @apiVersion 0.3.0
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
            if (!(request.getParameter("page").equals("")||request.getParameter("page")==null)
                    && !(request.getParameter("rows")==null||request.getParameter("rows").equals(""))){
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
    /**
     *
     * @api {post} /uav/location/add Add Location of uav(只记录无回应)
     * @apiName Locations for uav
     * @apiGroup Uav
     * @apiVersion 0.3.0
     *
     * @apiParam {Number} height Height of uav
     * @apiParam {Number} latitude Latitude of uav.
     * @apiParam {Number} longitude Longitude of uav.
     *
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

        try{
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
        }catch (ParseException e){
            e.printStackTrace();
            jsonRender = jsonRender.multiError();
            jsonRender.put("Msg", "Date Format Error!");
        }


        return jsonRender;
    }

    /**
     * 无人机的区块申请
     */
    /**
     *
     *
     * @api {post} /uav/block/apply 无人机的区块申请
     * @apiName Apply block for uavs
     * @apiGroup Uav
     * @apiVersion 0.3.0
     *
     * @apiParam {Strings} uuidList UUID list for uavs, 这里的string不用双引号,直接填写, exampe:[23244,123].
     * @apiParam {String} beginTime Locations from Date (Date Time Format: yyyy-MM-dd hh).
     * @apiParam {String} endTime Locations to Date (Date Time Format: yyyy-MM-dd hh).(结束时间,优先级大于持续时间)
     * @apiParam {Number} lastingTime Lasting time for you apply.(持续时间,持续时间和结束时间都可以为空,会选择默认时间1天)
     * @apiParam {String} geohash Geohash code for the block you apply(区块geohash编码,可以为空,但是经纬度和geohash不能同时为空,geohash优先级大于经纬度）
     * @apiParam {String} latitude Latitude of location of apply block.
     * @apiParam {String} longitude Longitude of location of apply block.
     *
     * @apiSource {Number} Code Return code of state
     * @apiSource {String} Msg Msg of state
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 100 OK
     *     {
     *       "Code": 100,
     *       "Msg": "ok"
     *     }
     *
     * @apiError ArgumentException Some apply Uavs' uuids are not in system! / Start Date Can't be empty! / Date Format Error! / Block or location can't be empty.(多种报错原因)
     * @apiError AuthException Action need auth.
     * @apiError RepeatedException Location has been applied.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 102 ArgumentException
     *     {
     *       //argumentException 有多种类型的参数错误，具体错误原因以Msg为准
     *       "Code": 102,
     *       "Msg": "Some apply Uavs' uuids are not in system!"
     *     }
     *
     *     HTTP/1.1 101 AuthException
     *     {
     *       "Code": 101,
     *       "Msg": "Action need auth"
     *     }
     *
     *     HTTP/1.1 106 RepeatedException
     *     {
     *       "Code": 106,
     *       "Msg": "Location has been applied"
     *     }
     */
    @ResponseBody
    @RequestMapping(value = "/block/apply", method = RequestMethod.POST)
    public Result addApplyList(HttpServletRequest request, HttpSession session){
        Result jsonRender = new Result();

        List<String> uuidList = JSON.parseArray(request.getParameter("uuidList"), String.class);
        List<Long> uavIdList = new ArrayList<Long>();

        if (uuidList != null && !"".equals(uuidList)){
            try {
                uavIdList = uavService.transferStringToLong(uuidList);
            }catch (NullPointerException e){
                jsonRender.argError();
                jsonRender.put("Msg", "Some apply Uavs' uuids are not in system!");
                return jsonRender;
            }
        }

        String geohash = request.getParameter("geohash");

        String strStartDate = request.getParameter("beginTime");
        String strEndDate = request.getParameter("endTime");

        //判断时间数据
        if (strStartDate==null || "".equals(strEndDate)){
            jsonRender = jsonRender.argError();
            jsonRender.put("Msg", "Start Date Can't be empty!");
            return jsonRender;
        }
        //默认时间结束时间为空的时候就使用默认的持续时间
        try{
            if (strEndDate == null || "".equals(strEndDate)){
                if (request.getParameter("lastingTime") == null || "".equals(request.getParameter("lastingTime"))){
                    strEndDate = uavService.getEndDateByLasting(strStartDate);
                }
                else{
                    int lastingTime = Integer.parseInt(request.getParameter("lastingTime"));
                    strEndDate = uavService.getEndDateByLasting(strStartDate, lastingTime);
                }
            }
        }catch (ParseException e){
            e.printStackTrace();
            jsonRender = jsonRender.argError();
            jsonRender.put("Msg", "Date Format Error!");
            return jsonRender;
        }

        //转化geohash code
        if (geohash == null || "".equals(geohash)){
            if ((null==request.getParameter("latitude") || "".equals(request.getParameter("latitude")))
                    || (null==request.getParameter("latitude") || "".equals(request.getParameter("latitude")))){

                jsonRender = jsonRender.argError();
                jsonRender.put("Msg", "Block or location can't be empty");
                return jsonRender;
            }
            else{
                double latitude = Double.parseDouble(request.getParameter("latitude"));
                double longitude = Double.parseDouble(request.getParameter("longitude"));
                geohash = GeohashUtil.encode(latitude, longitude, 6);
            }
        }


        //查询区块申请信息并添加申请
        try{
                Map<String, Object> msg = uavService.getBlockApplyState(geohash, strStartDate, strEndDate);
            if ((boolean)msg.get("state")){
                    if (session.getAttribute("adminId") != null){
                        //admin的区块申请操作
                        uavService.addBlockApply(geohash, strStartDate, strEndDate, true, uavIdList);
                    }
                    else if (session.getAttribute("authId") != null){
                        //普通用户的申请操作
                        uavService.addBlockApply(geohash, strStartDate, strEndDate, false, uavIdList);
                    }
                    else{
                        jsonRender = jsonRender.needAuth();
                        jsonRender.put("Msg", "Action need auth");
                    }

            }
            else{
                jsonRender = jsonRender.multiError();
                jsonRender.put("Msg", msg.get("msg"));
            }
        }catch (ParseException e){
            e.printStackTrace();
            jsonRender = jsonRender.argError();
            jsonRender.put("Msg", "Date Format Error!");
        }


        return jsonRender;
    }


    /**
     *
     *
     * @api {get} /uav/blocks Show block apply list
     * @apiName Block applications
     * @apiGroup Uav
     * @apiVersion 0.3.1
     *
     * @apiParam {String} beginTime Locations from Date (Date Time Format: yyyy-MM-dd hh).
     * @apiParam {String} endTime Locations to Date (Date Time Format: yyyy-MM-dd hh).
     * @apiParam {String} geohash Geohash code for the block you apply(区块geohash编码,可以为空,但是经纬度和geohash不能同时为空,geohash优先级大于经纬度）
     * @apiParam {String} latitude Latitude of location of apply block.
     * @apiParam {String} longitude Longitude of location of apply block.
     *
     * @apiSource {Number} Code Return code of state
     * @apiSource {String} Msg Msg of state
     * @apiSource {[block,..]} Data One page data for locations
     * @apiSource {Number} Counts Total number of elements for locations
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *       "Code": 200,
     *       "Msg": "ok"
     *     }
     *
     * @apiError ArgumentException Time arguments can't be empty! / Date Format Error! / Block or location can't be empty.(多种报错原因)
     * @apiError AuthException Action need auth.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 102 ArgumentException
     *     {
     *       //argumentException 有多种类型的参数错误，具体错误原因以Msg为准
     *       "Code": 102,
     *       "Msg": "Some apply Uavs' uuids are not in system!"
     *     }
     *
     *     HTTP/1.1 101 AuthException
     *     {
     *       "Code": 101,
     *       "Msg": "Action need auth"
     *     }
     */
    @ResponseBody
    @RequestMapping(value = "/blocks", method = RequestMethod.GET)
    public Result getBlockApply(HttpServletRequest request, HttpSession session){
        Result jsonRender = new Result();

        if (session.getAttribute("authId")==null && session.getAttribute("adminId")==null){
            jsonRender = jsonRender.needAuth();
            jsonRender.put("Msg", "Action need auth");
            return jsonRender;
        }

        String strBeginTime = request.getParameter("beginTime");
        String strEndTime = request.getParameter("endTime");
        if ((strBeginTime.equals("")||strBeginTime==null) &&(strEndTime.equals("")||strEndTime==null)){
            jsonRender = jsonRender.argError();
            jsonRender.put("Msg", "Time arguments can't be empty!");
            return jsonRender;
        }

        int page = 1, rows = 10;
        //分页的基本参数，根据需要自己设置需要的参数把
        if (!(request.getParameter("page").equals("")||request.getParameter("page")==null)
                && !(request.getParameter("rows")==null||request.getParameter("rows").equals(""))){
            page = Integer.parseInt(request.getParameter("page"));
            rows = Integer.parseInt(request.getParameter("rows"));
        }

        String geohash = request.getParameter("geohash");
        //转化geohash code
        if (geohash == null || "".equals(geohash)){
            if ((null==request.getParameter("latitude") || "".equals(request.getParameter("latitude")))
                    || (null==request.getParameter("latitude") || "".equals(request.getParameter("latitude")))){
                jsonRender.argError();
                jsonRender.put("Msg", "Block or location can't be empty");
                return jsonRender;
            }
            else{
                double latitude = Double.parseDouble(request.getParameter("latitude"));
                double longitude = Double.parseDouble(request.getParameter("longitude"));
                geohash = GeohashUtil.encode(latitude, longitude, 6);
            }
        }
        try{
            List<BlockApplication> applyList = uavService.getBlockApplyState(geohash, strBeginTime, strEndTime, page, rows);
            jsonRender = jsonRender.okForList();
            jsonRender.put("Data", applyList);
            jsonRender.put("Counts", uavService.getCountvalue());
        }catch (ParseException e){
            e.printStackTrace();
            jsonRender = jsonRender.argError();
            jsonRender.put("Msg", "Date Format Error!");
        }
        return jsonRender;
    }

}
