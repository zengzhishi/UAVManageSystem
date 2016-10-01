package com.zlion.service;

import com.zlion.model.BlockApplication;
import com.zlion.model.Location;
import com.zlion.model.Uav;
import com.zlion.repository.BlockApplicationRepository;
import com.zlion.repository.LocationRepository;
import com.zlion.repository.UavRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.sun.org.apache.xpath.internal.operations.String;

/**
 * Created by zzs on 2016/9/17.
 */
@Service
public class UavService {

    private UavRepository uavRepository;
    private LocationRepository locationRepository;
    private BlockApplicationRepository blockApplicationRepository;

    private int countvalue;
    private final SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd hh") ;
    private final int ForwardDateNum = 1;


    @Autowired
    public UavService(UavRepository uavRepository, LocationRepository locationRepository, BlockApplicationRepository blockApplicationRepository) {
        this.uavRepository = uavRepository;
        this.locationRepository = locationRepository;
        this.blockApplicationRepository = blockApplicationRepository;
    }

    @Transactional
    public List<Location> getLocations(String uuid){
        Long uavId = uavRepository.findByUuid(uuid).getId();
        return locationRepository.findByUavId(uavId);
    }

    @Transactional
    public List<Location> getLocationsByTime(String uuid, Date starttime, Date endtime, int page, int rows){
        Long uavId = uavRepository.findByUuid(uuid).getId();
        List<Location> locationList = locationRepository.findByUavIdAndTimeBetween(uavId,starttime,endtime);
        countvalue=locationList.size();
//      此处进行按时间排序
        sortClass sort = new sortClass();//建立排序规则
        Collections.sort(locationList,sort);//按照从小到大排序
        Collections.reverse(locationList);//反转顺序

        int fromindex = (page-1)*rows+1;
        int toindex = fromindex+rows-1;
        locationList.subList(fromindex,toindex);
        return  locationList;
    }

    @Transactional
    public int getCountvalue(){
        return countvalue;
    }

    @Transactional
    public boolean addLocation(String uuid, double latitude, double longitude, double height){
        try{
            Uav uav = uavRepository.findByUuid(uuid);
            if (uav != null){
                Location location = new Location(uav.getId(), new Date(), latitude, longitude, height);
                locationRepository.save(location);
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }


    public Uav getUavDetail(String uuid){
        Uav uav = uavRepository.findByUuid(uuid);
        return uav;
    }

    public Long getUavBelong(String uuid){
        Uav uav = uavRepository.findByUuid(uuid);
        return uav.getUser_id();
    }

    public void delUav(Long userId, String uuid) {
        Uav uav = uavRepository.findByUuid(uuid);
        uavRepository.delete(uav);
    }

    /**
     * Get Location application state
     *
     * @param geohash   Geohash code for location
     * @param strBeginDate
     * @param strEndDate
     * @return {
     *
     * }
     */
    public Map<String, Object> getBlockApplyState(String geohash, String strBeginDate, String strEndDate){

        Map<String, Object> result = new HashMap<String, Object>();
        Date beginDate, endDate;

        //转化时间为Data类型
        try{
            beginDate = sim.parse(strBeginDate);
            endDate = sim.parse(strEndDate);
        }catch(ParseException e){
            e.printStackTrace();
            result.put("state", false);
            result.put("msg", "Parse error!");
            return result;
        }

        //判断是否有申请
        List<BlockApplication> applyList = blockApplicationRepository.qureyByGeohashAndTimeBetween(geohash, beginDate, endDate);
        if (applyList == null){
            result.put("state", true);
            result.put("data", null);
        }
        else{
            result.put("state", false);
            result.put("msg", "Location has been applied");
            result.put("data", applyList);
        }

        return result;
    }

    public Map<String, Object> getBlockApplyState(String geohash){

        Date date = new Date();
        String strBeginDate = sim.format(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        String strEndDate = sim.format(cal.getTime());

        return getBlockApplyState(geohash, strBeginDate, strEndDate);
    }


}

class sortClass implements Comparator{
    @Override
    public int compare(Object arg0,Object arg1){
        Location location0 = (Location)arg0;
        Location location1 = (Location)arg1;
        int flag = location0.getDate().compareTo(location1.getDate());
        return  flag;
    }
}
