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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UavManageSystemApplicationTests {

	@Autowired
	private UavService uavService;

	@Test
	public void contextLoads() {

		String uuids = "[23244,123]";

		List<String> uuidList = JSON.parseArray(uuids, String.class);
		List<Long> uavIdList = new ArrayList<Long>();
		try {
			uavIdList = uavService.transferStringToLong(uuidList);
		}catch (NullPointerException e){
			e.printStackTrace();
		}
		String startDate = "2016-01-1 10";
		String endDate = "2016-01-5 10";
		double latitude = 119.123;
		double longitude = 32.35556;
		String geohash = GeohashUtil.encode(latitude, longitude, 6);

//		Map<String, Object> msg = uavService.getBlockApplyState(geohash, startDate, endDate);
//		if ((boolean)msg.get("state")){
////				uavService.addBlockApply(geohash, startDate, endDate, false, uavIdList);
//		}
//		else{
//			System.out.println(msg.get("msg"));
//		}



	}

}
