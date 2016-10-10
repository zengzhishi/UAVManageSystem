package com.zlion;

import com.alibaba.fastjson.JSON;
import com.zlion.service.UavService;
import com.zlion.util.GeohashUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UavManageSystemApplicationTests {

	@Autowired
	private UavService uavService;

	@Test
	public void contextLoads() {

//		String uuids = "[23244,123]";
//		String uuids = "";
//		List<String> uuidList = JSON.parseArray(uuids, String.class);
//		List<Long> uavIdList = new ArrayList<Long>();
//		try {
//			uavIdList = uavService.transferStringToLong(uuidList);
//		}catch (NullPointerException e){
//			e.printStackTrace();
//		}
//		System.out.println(uavIdList);
		String startDate = "2016-01-1 10";
		String endDate = "2016-01-5 10";
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd hh");
		try{
			Date begin = sim.parse(startDate);
			Date end = sim.parse(endDate);
			if (begin.getTime() > end.getTime()){
				System.out.println("时间前的数值大");
			}else{
				System.out.println("时间前的数值小");
			}
		}catch (ParseException e){
			e.printStackTrace();
		}

//		double latitude = 119.123;
//		double longitude = 32.35556;
//		String geohash = GeohashUtil.encode(latitude, longitude, 6);

//		Map<String, Object> msg = uavService.getBlockApplyState(geohash, startDate, endDate);
//		if ((boolean)msg.get("state")){
////				uavService.addBlockApply(geohash, startDate, endDate, false, uavIdList);
//		}
//		else{
//			System.out.println(msg.get("msg"));
//		}



	}

}
