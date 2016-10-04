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

    /*
    查看无人机的位置信息,需要做分页,需要实现按照时间来查询    暂时无法测试
     */
    @ResponseBody
    @RequestMapping(value = "/locations", method = RequestMethod.GET)
    public Result uavLocations(HttpServletRequest request, HttpSession session){
        Result jsonRender = new Result();
        jsonRender = jsonRender.okForList();

//        //分页的基本参数，根据需要自己设置需要的参数把
//        int page = Integer.parseInt(request.getParameter("page"));
//        int rows = Integer.parseInt(request.getParameter("rows"));
        String strBeginTime = request.getParameter("beginTime");
        String strEndTime = request.getParameter("endTime");

        int page = 1, rows = 10;
//        long startTimeStamp = new Date().getTime();
//        long endTimeStamp = new Date().getTime();
//        //这里用的是时间戳,需要做转化
//        try{
//            startTimeStamp = Long.parseLong(request.getParameter("timeStart"));
//            endTimeStamp = Long.parseLong(request.getParameter("timeEnd"));
//        }catch (NumberFormatException e){
//            e.printStackTrace();
//        }


        String uuid = request.getParameter("uuid");


//        Date startTime = new Date(startTimeStamp*1000L);
//        Date endTime = new Date(endTimeStamp*1000L);
        List<Location> locations = uavService.getLocationsByTime(uuid, strBeginTime, strEndTime);
//        List<Location> locationlist = uavService.getLocationsByTime(uuid,startTime,endTime,page,rows);
//
//        jsonRender.put("Date",locationlist);
//        jsonRender.put("Lenth",uavService.getCountvalue());
//        jsonRender.put("startDate", startTime.toString());
//        jsonRender.put("endDate", endTime.toString());

        jsonRender.put("data", locations);
        return jsonRender;
    }

    /*
    无人机通过该接口添加无人的位置记录
     */
    @ResponseBody
    @RequestMapping(value = "/location/add", method = RequestMethod.POST)
    public void addUavLocation(HttpServletRequest request, HttpSession session){

        String uuid = request.getParameter("uuid");
        double latitude = Double.parseDouble(request.getParameter("latitude"));
        double longitude = Double.parseDouble(request.getParameter("longitude"));
        double height = Double.parseDouble(request.getParameter("height"));

        uavService.addLocation(uuid,latitude,longitude,height);
    }

    /*
    geohash的位置编码，用来判断用法的，先不用测试
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
    @RequestMapping(value = "/location/apply", method = RequestMethod.GET)
    public Result getApplyList(HttpServletRequest request, HttpSession session){

        Result jsonRender = new Result();



        return jsonRender;
    }

}
