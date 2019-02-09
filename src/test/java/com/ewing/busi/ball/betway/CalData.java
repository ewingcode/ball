package com.ewing.busi.ball.betway;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ewing.order.Door;
import com.ewing.order.ball.dto.BetInfoDto;
import com.ewing.order.ball.event.InRateCache;
import com.ewing.order.ball.util.CalUtil;
import com.ewing.order.busi.ball.dao.BetRollInfoDao;
import com.ewing.order.busi.ball.ddl.BetInfo;
import com.ewing.order.busi.ball.ddl.BetRollInfo;
import com.ewing.order.busi.ball.service.BetRollInfoService;
import com.ewing.order.core.jpa.BaseDao;
import com.ewing.order.util.BeanCopy;
import com.ewing.order.util.DataFormat;
import com.ewing.order.util.GsonUtil;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 *
 * @author tansonlam
 * @create 2018年8月24日
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Door.class)
public class CalData {
	private static Logger log = LoggerFactory.getLogger(CalData.class);
	@Resource
	private BaseDao baseDao;
	@Resource
	private BetRollInfoService betRollInfoService;
	@Resource
	private BetRollInfoDao betRollInfoDao;
	Integer rollSmall = 0;
	Integer rollBig = 0;
	Integer todaySmall = 0;
	Integer todayBig = 0;
	DecimalFormat fnum = new DecimalFormat("##0.00");
	DecimalFormat fnum2 = new DecimalFormat("##0.0000");
	DecimalFormat fnum3 = new DecimalFormat("##0.000");
	int avg = 0;
	float scoreEachSec = 0;
	float ratio_rou = 0;
	boolean showRollDetail = false;
	boolean showQuartzScore = false;
	public static Map<String, List<BetRollInfo>> rollMap = Maps.newConcurrentMap();
	ExecutorService buyWayExecutor = Executors.newFixedThreadPool(3);
	ExecutorService rollQueryExecutor = Executors.newFixedThreadPool(10);
	private void printCost(String name, long start) {
		log.info(name + ",cost:" + (System.currentTimeMillis() - start));
	}

	public List<BetRollInfo> findRollInfo(Integer gid) {
		return baseDao.find("select * from bet_roll_info where gid ='" + gid + "' and se_now='Q4'",
				BetRollInfo.class);
	}
 
	// @Test
	public void test(){
		Map<Integer,List<BuyWay>>  allResult = Maps.newHashMap();
		Date startDate = DataFormat.stringToDate("2019-01-01 00:00:00",DataFormat.DATETIME_FORMAT);
		Date endDate = DataFormat.stringToDate("2019-01-18 23:59:59",DataFormat.DATETIME_FORMAT);
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		while(startCal.before(endCal)){  
			Calendar tempEndCal = Calendar.getInstance();
			tempEndCal.setTime(startCal.getTime());
			tempEndCal.add(Calendar.MONTH, 1);
			if(tempEndCal.getTime().after(endDate))
				tempEndCal.setTime(endDate);
			else{ 
				tempEndCal.add(Calendar.SECOND, -1);
			}
			
			Map<Integer,List<BuyWay>> map1 =computeGame(DataFormat.DateToString(startCal.getTime(), DataFormat.DATETIME_FORMAT),DataFormat.DateToString(tempEndCal.getTime(), DataFormat.DATETIME_FORMAT)) ;
			String fileName = DataFormat.DateToString(startCal.getTime(), DataFormat.DATE_FORMAT2)+"-"+ DataFormat.DateToString(tempEndCal.getTime(), DataFormat.DATE_FORMAT2);
			File ballResultFile = new File("d:\\ballcal\\"+fileName); 
			//ballResultFile.deleteOnExit();
			try {
				FileUtils.writeStringToFile(ballResultFile, GsonUtil.getGson().toJson(new BallCalResult(map1)),"UTF-8");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			startCal.add(Calendar.MONTH, 1);  
			combineResult(allResult,map1);
		}
		 
		for(Integer desc : allResult.keySet()){
			List<BuyWay> buyWayList = allResult.get(desc);
			printResut(buyWayList);
		}
	}
	
	//@Test
	public void test2(){ 
		Map<Integer,List<BuyWay>>  allResult = Maps.newHashMap();
		Date startDate = DataFormat.stringToDate("2018-10-01 00:00:00",DataFormat.DATETIME_FORMAT);
		Date endDate = DataFormat.stringToDate("2019-01-13 23:59:59",DataFormat.DATETIME_FORMAT);
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		while(startCal.before(endCal)){  
			Calendar tempEndCal = Calendar.getInstance();
			tempEndCal.setTime(startCal.getTime());
			tempEndCal.add(Calendar.MONTH, 1);
			if(tempEndCal.getTime().after(endDate))
				tempEndCal.setTime(endDate);
			else{ 
				tempEndCal.add(Calendar.SECOND, -1);
			}
			
			Map<Integer,List<BuyWay>> map1 =computeGame2(DataFormat.DateToString(startCal.getTime(), DataFormat.DATETIME_FORMAT),DataFormat.DateToString(tempEndCal.getTime(), DataFormat.DATETIME_FORMAT)) ;
			String fileName = DataFormat.DateToString(startCal.getTime(), DataFormat.DATE_FORMAT2)+"-"+ DataFormat.DateToString(tempEndCal.getTime(), DataFormat.DATE_FORMAT2);
			File ballResultFile = new File("d:\\ballcal\\"+fileName); 
			//ballResultFile.deleteOnExit();
			try {
				FileUtils.writeStringToFile(ballResultFile, GsonUtil.getGson().toJson(new BallCalResult(map1)),"UTF-8");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			startCal.add(Calendar.MONTH, 1);  
			combineResult(allResult,map1);
		}
		 
		for(Integer desc : allResult.keySet()){
			List<BuyWay> buyWayList = allResult.get(desc);
			printResut(buyWayList);
		}
	}
	
	@Test
	public void test3(){ 
		 computeGame2("2019-01-01 00:00:00", "2019-01-21 23:59:59") ;
	}
	
	private void combineResult(Map<Integer,List<BuyWay>>  allResult ,Map<Integer,List<BuyWay>> newResult){
		Map<String,BuyWay> tempMap = Maps.newHashMap();
		for(Integer desc : newResult.keySet()){
			tempMap.clear();
			List<BuyWay> buyWayList = newResult.get(desc);
			for(BuyWay buyWay2 : buyWayList){
				tempMap.put(buyWay2.getId(), buyWay2);
			}
			List<BuyWay> existBuyWayList =  allResult.get(desc);
			if(existBuyWayList==null){
				allResult.put(desc, buyWayList);
			}else{
				Integer total = existBuyWayList.size();
				for(BuyWay existBuyWay : existBuyWayList){ 
					 
					//log.info("合并总数："+existBuyWayList.size()+",剩余："+(total--));
					existBuyWay.combine(tempMap.get(existBuyWay.getId())); 
					 
				}
			}
		} 
	}
	public List<BetInfoDto> loadGameInfo(String startTime,String endTime){
		long start = System.currentTimeMillis(); 
		Integer minGid = 0;
		List<BetInfo> entityList = baseDao
				.find("select * from bet_info where status=1 and gtype='BK' and create_time>='"
						+ startTime + "' and create_time<='" + endTime + "' "
						// +" and league like '%NBA%'"
						+ " and (league not like '%3X3%' and league not like '%美式足球%' and league not like '%篮网球%' and league not like '%测试%'  and league not like '%女子%')"
						// + " and ratio is not null and ratio_o is not null "
						// + " and gid in ('2762650')"
						+ " order by datetime desc ", BetInfo.class);
		List<Integer> gidList = Lists.transform(entityList, new Function<BetInfo, Integer>() {
			@Override
			public Integer apply(BetInfo betInfo) {
				return Integer.valueOf(betInfo.getGid());
			}
		});
		for (Integer gid : gidList) {
			if (minGid == 0 || minGid > gid) {
				minGid = gid;
			}
		}
		printCost("findBetInfo", start);
		start = System.currentTimeMillis();
		// 查询所有滚球，放在内存中
		List<BetRollInfo> allRollList = Lists
				.newArrayList();/*
								 * baseDao.
								 * find("select * from bet_roll_info where gid in ("
								 * + SqlUtil.array2InCondition(gidList) + ")",
								 * BetRollInfo.class);
								 */

		
		CountDownLatch countDownLatch = new CountDownLatch(gidList.size());
		for (Integer gid : gidList) {
			rollQueryExecutor.execute(new Runnable() {

				@Override
				public void run() {
					List<BetRollInfo> oneList = findRollInfo(gid);
					if (CollectionUtils.isNotEmpty(oneList)) {
						rollMap.put(gid.toString(), oneList);
					}
					countDownLatch.countDown();
					log.info("总赛事：" + gidList.size() + ",当前剩余数：" + countDownLatch.getCount());
				}
			});

		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		printCost("加载所有滚球赛事,滚球数据量：" + allRollList.size() + ",总时间", start);
	//	rollQueryExecutor.shutdownNow();
		// List<BetRollInfo> allRollList = baseDao.find("select * from
		// bet_roll_info where gid > "+minGid, BetRollInfo.class);
		printCost("findAllRollInfo", start);
		/*
		 * for (BetRollInfo betRollInfo : allRollList) { try { List<BetRollInfo>
		 * rollList = rollMap.get(betRollInfo.getGid()); if (rollList == null) {
		 * rollList = Lists.newArrayList(); rollMap.put(betRollInfo.getGid(),
		 * rollList); } rollList.add(betRollInfo); } catch (Exception e) {
		 * log.error(GsonUtil.getGson().toJson(betRollInfo),e); } }
		 */
		// 滚球按ID排序
		for (String gid : rollMap.keySet()) {
			List<BetRollInfo> rollList = rollMap.get(gid);
			Collections.sort(rollList, new Comparator<BetRollInfo>() {
				@Override
				public int compare(BetRollInfo o1, BetRollInfo o2) {
					try {

						return o1.getId().compareTo(o2.getId());
					} catch (NumberFormatException e) {
						log.error(e.getMessage(), e);
						return 0;
					}
				}

			});
		}
		printCost("findAllRollInfo2", start);
		start = System.currentTimeMillis();
		List<BetInfo> betInfoList = Lists.newArrayList();
		List<BetInfoDto> betInfoDtoList = Lists.newArrayList();
		for (BetInfo betInfo : entityList) {
			/*
			 * if (StringUtils.isEmpty(betInfo.getRatio()) ||
			 * StringUtils.isEmpty(betInfo.getRatio_o()) ||
			 * StringUtils.isEmpty(betInfo.getSc_total()) ||
			 * betInfo.getSc_total().equals("0")) { continue; }
			 */

			betInfoList.add(betInfo);
		}
		// 查询每场比赛最高和最低分

		/*
		 * List<RollGameCompute> maxMinList =
		 * betRollInfoDao.findMinAndMax(gidList); betInfoDtoList =
		 * BeanCopy.copy(entityList, BetInfoDto.class); for (BetInfoDto
		 * betInfoDto : betInfoDtoList) { for (RollGameCompute rollGameCompute :
		 * maxMinList) { if
		 * (betInfoDto.getGid().equals(rollGameCompute.getGid())) {
		 * betInfoDto.setMaxRatioR(rollGameCompute.getMaxRatioR());
		 * betInfoDto.setMinRatioR(rollGameCompute.getMinRatioR());
		 * betInfoDto.setMinRatioRou(rollGameCompute.getMinRatioRou());
		 * betInfoDto.setMaxRatioRou(rollGameCompute.getMaxRatioRou()); } } }
		 */
		printCost("fillMaxMinInfo start", start);
		betInfoDtoList = BeanCopy.copy(betInfoList, BetInfoDto.class);
		// betInfoDtoList = betRollInfoService.fillMaxMinInfo(betInfoList);
		printCost("fillMaxMinInfo end", start);
		for (BetInfoDto betInfo : betInfoDtoList) {

			/*
			 * if (betInfo.getMinRatioRou() == nutll) { continue; }
			 */
			if (showRollDetail) {
				try {
					printGame(betInfo);
					printRollDetail(betInfo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} /*
				 * else { log.info("查询滚球信息" + betInfo.getGid());
				 * List<BetRollInfo> list =
				 * betRollInfoDao.find(betInfo.getGid());
				 * rollMap.put(betInfo.getGid(), list); }
				 */

		}
		return betInfoDtoList;
	}
	public Map<Integer,List<BuyWay>> computeGame(String startTime,String endTime) {
		log.info("computeGame startTime:"+startTime+",endTime:"+endTime);
		Map<Integer,List<BuyWay>>  buyWayMap = Maps.newHashMap();
		long start = System.currentTimeMillis(); 
		
		List<BetInfoDto> betInfoDtoList = loadGameInfo(startTime,endTime);
 		printCost("batchBuy1 start", start);
 		List<BuyWay> buyWayList1 = batchBuy(betInfoDtoList, "Q4", true);
 		buyWayMap.put(1, buyWayList1);
 		printCost("batchBuy1 end", start);
 		InRateCache.clearAll();
 		List<BuyWay> buyWayList2 = batchBuy(betInfoDtoList, "Q4", false);
 		buyWayMap.put(0, buyWayList2);
 		printCost("batchBuy2 end", start);  
		InRateCache.clearAll();
		rollMap.clear();
		return buyWayMap;
	}
	
	public Map<Integer,List<BuyWay>> computeGame2(String startTime,String endTime) {
		log.info("computeGame startTime:"+startTime+",endTime:"+endTime);
		Map<Integer,List<BuyWay>>  buyWayMap = Maps.newHashMap();
	 
		
		List<BetInfoDto> betInfoDtoList = loadGameInfo(startTime,endTime); 
		 
		// CalData:649
		// -方案ID:3917,滚球买入AUTO，得分率差0.01416,得分率差比例:0.2,最大阀值比例:0.8,场数：71,出现次数:null,持续时间:120,
		// 剩余时间:null,是否反向买入:false,比率：64.79%,,赢场次:46,输场次25,下注:7100.0,金额:1221.9
		// 方案ID:r59954,滚球买入AUTO，得分率差0.019059999,
		//得分率差比例:0.2,最大阀值比例:1.2,场数：577,出现次数:11,持续时间:120,剩余时间:550,是否反向买入:true,
		// 是否全场得分：false,比率：57.02%,,赢场次:329,输场次248,下注:57700.0,金额:2457.7
		List<BuyWay> buyWayList5  = batchBuy2(betInfoDtoList,0.025f, 5, 50, "AUTO", "Q4", true,
				  1.2f, null, null,true);  
		List<BuyWay> buyWayList2  = batchBuy2(betInfoDtoList,null, 11, 120, "AUTO", "Q4", true,
		  1.2f, 550, 0.2f,false);  
		//z3482,滚球买入AUTO，得分率差0.03692,得分率差比例:0.4,最大阀值比例:0.8,场数：543,出现次数:null,持续时间:100,剩余时间:500,是否反向买入:false,是否全场得分：false,比率：56.91%,,赢场次:309,输场次234,下注:54300.0,金额:1907.7001
		 //z27261,滚球买入AUTO，得分率差0.0136400005,得分率差比例:0.2,最大阀值比例:0.8,场数：248,出现次数:7,持续时间:100,剩余时间:450,是否反向买入:false,是否全场得分：true,比率：58.87%,,赢场次:146,输场次102,下注:24800.0,金额:1880.0
		 List<BuyWay> buyWayList3  = batchBuy2(betInfoDtoList,null, null, 100, "AUTO", "Q4", false,
				  0.8f, 500, 0.4f,false); 
		 List<BuyWay> buyWayList4  = batchBuy2(betInfoDtoList,null, 7, 100, "AUTO", "Q4", true,
				  0.8f, 450, 0.2f,true); 
		 buyWayList2.addAll(buyWayList3);
		InRateCache.clearAll();
		rollMap.clear();
		return buyWayMap;
	}

	public List<BuyWay> batchBuy(List<BetInfoDto> betInfoDtoList, String seNow, Boolean desc) {
		Long start = System.currentTimeMillis();
		 
		List<BuyWay> buyWay2List = Lists.newArrayList();
		List<Callable<BuyWay>> callableList = Lists.newArrayList();
		List<Future<BuyWay>> allFutureList = Lists.newArrayList();
		Integer idInc = 0;
		  for (int j = 20; j <= 50; j += 5) {
	
		for (int i = 4; i <= 12; i++) {
			for (int z = 20; z <= 200; z += 20) {
				for (int k = 4; k <= 15; k += 2) {
					for (int l = 600; l >= 350; l -= 50) {
						for (int b = 0; b <= 200; b += 20) {
							for (int m = 0; m < 2; m++) {
								Float interval =j==20? null:j / (1000 * 1f);
								Integer minHighScoreTime = i == 4 ? null : i;
								Integer highScoreCostTime = z == 20 ? null : z;
								Float maxintervalPercent = k == 4 ? null : k / 10f;
								Integer leftTime = l == 600 ? null : l;
								Float intervalInPercent = b == 0 ? null : b / (100 * 1f);
								Boolean isAll = m == 0 ? true : false;
								idInc++;
								String id = desc?"r"+idInc:"z"+idInc;
//								BuyWay2 buyWay = new BuyWay2(interval, minHighScoreTime,
//										highScoreCostTime, "AUTO", seNow, desc, maxintervalPercent,
//										leftTime, intervalInPercent, isAll);
//								buyWay.compute(betInfoDtoList);
								// buyWay.printResult();
							//	buyWay2List.add(buyWay);
								callableList.add(new Callable<BuyWay>() {

											@Override
											public BuyWay call() throws Exception {
											
												BuyWay buyWay = new BuyWay(id,interval,
														minHighScoreTime, highScoreCostTime, "AUTO",
														seNow, desc, maxintervalPercent, leftTime,
														intervalInPercent, isAll);
												log.info("start compute for scheme :"+buyWay.getId());
												buyWay.compute(betInfoDtoList);
												log.info("end compute for scheme :"+buyWay.getId());
												return buyWay;
											}
										});
						 

							}
						}
					}
				}
			}
			}

		}
		Integer schemeTotal = callableList.size();
		log.info("方案总数："+callableList.size());
		try {
			allFutureList = buyWayExecutor.invokeAll(callableList);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(Future<BuyWay> future:allFutureList){
			try {
				log.info("方案总数："+callableList.size()+",剩余方案："+(schemeTotal--));
				BuyWay buyWay2 = future.get();
 			    buyWay2List.add(buyWay2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		printCost("batchBuy1 compute end", start);
		 
	
		return buyWay2List;
	}
	
	private void printResut(List<BuyWay> buyWay2List){
		   
		log.info("最赚钱的方案=====");
		buyWay2List.get(0).printResult();
		Collections.sort(buyWay2List, new Comparator<BuyWay>() {
			@Override
			public int compare(BuyWay o1, BuyWay o2) {
				return o2.sucRate().compareTo(o1.sucRate());
			}

		});
		log.info("最高比例赚钱的方案=====");
		buyWay2List.get(0).printResult();

		Collections.sort(buyWay2List, new Comparator<BuyWay>() {
			@Override
			public int compare(BuyWay o1, BuyWay o2) {
				return Float.valueOf(o2.smallWinMoney).compareTo(Float.valueOf(o1.smallWinMoney));
			}

		});
		log.info("最赚钱排序=====");
		for (BuyWay buyWay2 : buyWay2List) {
			buyWay2.shortPrintResult();
		} 
	}

	public List<BuyWay>  batchBuy2(List<BetInfoDto> betInfoDtoList, Float interval, Integer minHighScoreTime,
			Integer highScoreCostTime, String buySide, String seNow, Boolean desc,
			Float maxintervalPercent, Integer leftTime, Float intervalInPercent, Boolean isAll) {
		BuyWay bestMoenyBuyWay2 = null;
		BuyWay bestRateBuyWay2 = null;
		Integer loseNum = 0;
		Integer winNum = 0;
		Integer totalNum = 0;
		List<BuyWay> buyWay2List = Lists.newArrayList();

		BuyWay buyWay = new BuyWay(null,interval, minHighScoreTime, highScoreCostTime, "AUTO", seNow,
				desc, maxintervalPercent, leftTime, intervalInPercent, isAll);
		buyWay.compute(betInfoDtoList);
		buyWay.printResult();
		buyWay2List.add(buyWay);

		if (bestMoenyBuyWay2 == null || buyWay.smallWinMoney > bestMoenyBuyWay2.smallWinMoney)
			bestMoenyBuyWay2 = buyWay;
		if (bestRateBuyWay2 == null || buyWay.sucRate() > bestRateBuyWay2.sucRate())
			bestRateBuyWay2 = buyWay;
		if (buyWay.smallWinMoney > 0) {
			winNum++;
		} else {
			loseNum++;
		}
		totalNum++;

		log.info("最赚钱的方案=====");
		bestMoenyBuyWay2.printResult();
		log.info("最高比例赚钱的方案=====");
		bestRateBuyWay2.printResult();
		log.info("方案输赢比例,总数:" + totalNum + ",赢" + winNum + "输:" + loseNum);
		for (BuyWay buyWay2 : buyWay2List) {
			buyWay2.shortPrintResult();
		}
		return buyWay2List;
	}

	private static Integer idPool = 0;

	

	public void printRollDetail(BetInfoDto betInfo) {
		List<BetRollInfo> list = rollMap.get(betInfo.getGid());
		rollMap.put(betInfo.getGid(), list);
		for (BetRollInfo betRollInfo : list) {

			try {
				StringBuffer sb = new StringBuffer();
				sb.append("ID：").append(betRollInfo.getId());
				sb.append(",分数:" + betRollInfo.getRatio_rou_c()).append(",总分：")
						.append(StringUtils.leftPad(betRollInfo.getSc_total(), 3, "  ")).append(",")
						.append(betRollInfo.getSe_now()).append(",大回报:")
						.append(betRollInfo.getIor_ROUC()).append(",小回报:")
						.append(betRollInfo.getIor_ROUH())
						.append(" 节剩余（秒）:" + StringUtils.leftPad(betRollInfo.getT_count(), 3, " "))
						.append(",每场节-全场得分:")
						.append(fnum2.format(CalUtil.computeScoreSec4Quartz(betRollInfo)
								- CalUtil.computeScoreSec4Alltime(betRollInfo)))
						.append(" 每节进球（秒）："
								+ fnum2.format(CalUtil.computeScoreSec4Quartz(betRollInfo)))
						.append(" 全场进球（秒）："
								+ fnum2.format(CalUtil.computeScoreSec4Alltime(betRollInfo)))
						.append(",预计总分:").append(expectLeftScore(betRollInfo)).append(",预计总分2:")
						.append(expectLeftScore2(betRollInfo)).append(",预计总分3:")
						.append(expectLeftScore3(betRollInfo))
						.append(" [Q1]：" + betRollInfo.getSc_Q1_total())
						.append(" [Q2]：" + betRollInfo.getSc_Q2_total())
						.append(" [Q3]：" + betRollInfo.getSc_Q3_total())
						.append(" [Q4]：" + betRollInfo.getSc_Q4_total());
				if (betRollInfo.getRatio_rou_c() != null) {
					if (betRollInfo.getRatio_rou_c().equals(betInfo.getMaxRatioRou())) {
						sb.append("--[Max]");
					} else if (betRollInfo.getRatio_rou_c().equals(betInfo.getMinRatioRou())) {
						sb.append("--[Min]");
					}
				}

				log.info(sb.toString());
			} catch (Exception e) {
				log.error("rollinfo id:" + betRollInfo.getId(), e);
			}
		}
	}

	private void printGame(BetInfoDto betInfo) {
		if (StringUtils.isEmpty(betInfo.getRatio_o())) {
			betInfo.setRatio_o("小 0");
		}
		if (betInfo.getRatio_rou_c() == null) {
			betInfo.setRatio_rou_c(Float.valueOf(betInfo.getRatio_o().substring(1)));

		}

		StringBuffer sb = new StringBuffer();
		sb.append(betInfo.getGid());
		sb.append(" " + betInfo.getLeague());
		sb.append(" " + betInfo.getTeam_h()).append(" vs ").append(betInfo.getTeam_c())
				.append(" " + betInfo.getDatetime());
		avg = Math.round(Float.valueOf(betInfo.getRatio_o().substring(1)) / 4);
		scoreEachSec = avg / (600 * 1f);
		ratio_rou = Float.valueOf(betInfo.getRatio_o().substring(1));
		Float Q1float = (Integer.valueOf(betInfo.getSc_Q1_total()) - avg) / (avg * 1f) * 100f;
		Float Q2float = (Integer.valueOf(betInfo.getSc_Q1_total())
				+ Integer.valueOf(betInfo.getSc_Q2_total()) - 2 * avg) / (2 * avg * 1f) * 100f;
		Float Q3float = (Integer.valueOf(betInfo.getSc_Q1_total())
				+ Integer.valueOf(betInfo.getSc_Q2_total())
				+ Integer.valueOf(betInfo.getSc_Q3_total()) - 3 * avg) / (3 * avg * 1f) * 100f;

		todaySmall += betInfo.getRatio_rou_c() > Float.valueOf(betInfo.getSc_total()) ? 1 : 0;
		todayBig += betInfo.getRatio_rou_c() < Float.valueOf(betInfo.getSc_total()) ? 1 : 0;
		/*
		 * rollBig += Float.valueOf(betInfo.getMinRatioRou()) <
		 * Float.valueOf(betInfo.getSc_total()) ? 1 : 0; rollSmall +=
		 * Float.valueOf(betInfo.getMaxRatioRou()) >
		 * Float.valueOf(betInfo.getSc_total()) ? 1 : 0; Float
		 * minAndBeginPercent = Math .abs((betInfo.getRatio_rou_c() -
		 * Float.valueOf(betInfo.getMinRatioRou())) / betInfo.getRatio_rou_c());
		 * Float maxAndBeginPercent = Math .abs((betInfo.getRatio_rou_c() -
		 * Float.valueOf(betInfo.getMaxRatioRou())) / betInfo.getRatio_rou_c());
		 */

		String ratio_re = betInfo.getStrong().equals("H") ? betInfo.getRatio()
				: "-" + betInfo.getRatio();

		StringBuffer ratio_re_sb = new StringBuffer();
		ratio_re_sb.append(StringUtils.rightPad("让分：" + ratio_re, 11, " "));
		ratio_re_sb
				.append(StringUtils.rightPad(
						"分差： " + (numberPlus(betInfo.getSc_FT_H(), betInfo.getSc_OT_H())
								- numberPlus(betInfo.getSc_FT_A(), betInfo.getSc_OT_A())),
						11, " "));
		ratio_re_sb.append(StringUtils.rightPad("最小：" + betInfo.getMinRatioR(), 16, " "));
		ratio_re_sb.append(StringUtils.rightPad("最大：" + betInfo.getMaxRatioR(), 16, " "));
		ratio_re_sb.append(StringUtils.rightPad("主    ：" + betInfo.getIor_RH(), 14, " "));
		ratio_re_sb.append(StringUtils.rightPad("客    ：" + betInfo.getIor_RC(), 14, " "));

		StringBuffer ratio_rou_sb = new StringBuffer();
		ratio_rou_sb.append(
				StringUtils.rightPad(("大小：" + betInfo.getRatio_o().substring(1)).trim(), 11, " "));
		ratio_rou_sb.append(StringUtils.rightPad(" 总分：" + betInfo.getSc_total(), 11, " "));
		/*
		 * ratio_rou_sb.append(StringUtils.rightPad("最小：" +
		 * betInfo.getMinRatioRou() + "(" + fnum.format(minAndBeginPercent *
		 * 100) + "%)", 16, " "));
		 * ratio_rou_sb.append(StringUtils.rightPad("最大：" +
		 * betInfo.getMaxRatioRou() + "(" + fnum.format(maxAndBeginPercent *
		 * 100) + "%)", 16, " "));
		 */
		ratio_rou_sb.append(StringUtils.rightPad("大    ：" + betInfo.getIor_OUC(), 14, " "));
		ratio_rou_sb.append(StringUtils.rightPad("小    ：" + betInfo.getIor_OUH(), 14, " "));

		StringBuffer common_sb = new StringBuffer();
		common_sb.append(StringUtils
				.rightPad("主场：" + numberPlus(betInfo.getSc_FT_H(), betInfo.getSc_OT_H()), 11, " "));
		common_sb.append(StringUtils
				.rightPad("客场：" + numberPlus(betInfo.getSc_FT_A(), betInfo.getSc_OT_A()), 11, " "));
		StringBuffer quartz1_sb = new StringBuffer();
		StringBuffer quartz2_sb = new StringBuffer();
		StringBuffer quartz3_sb = new StringBuffer();
		StringBuffer quartz4_sb = new StringBuffer();
		if (showQuartzScore) {
			BetRollInfo Q1betRollInfo = betRollInfoDao.queryLastRollInfoByQuartz(betInfo.getGid(),
					"Q1");
			quartz1_sb.append(" 【Q1】" + betInfo.getSc_Q1_total())
					.append(" " + fnum.format(Q1float) + "%").append("(")
					.append(Q1betRollInfo != null && Q1betRollInfo.getRatio_rou_c() != null
							? Q1betRollInfo.getRatio_rou_c() : "")
					.append(")")
					.append(" 进球（秒）：" + fnum2.format(CalUtil.computeScoreSec4Quartz(Q1betRollInfo)))
					.append(" 全场进球（秒）："
							+ fnum2.format(CalUtil.computeScoreSec4Alltime(Q1betRollInfo)));
			BetRollInfo Q2betRollInfo = betRollInfoDao.queryLastRollInfoByQuartz(betInfo.getGid(),
					"Q2");
			quartz2_sb.append(" 【Q2】" + betInfo.getSc_Q2_total())
					.append(" " + fnum.format(Q2float) + "%").append("(")
					.append(Q2betRollInfo != null && Q2betRollInfo.getRatio_rou_c() != null
							? Q2betRollInfo.getRatio_rou_c() : "")
					.append(")")
					.append(" 进球（秒）：" + fnum2.format(CalUtil.computeScoreSec4Quartz(Q2betRollInfo)))
					.append(" 全场进球（秒）："
							+ fnum2.format(CalUtil.computeScoreSec4Alltime(Q2betRollInfo)));
			BetRollInfo Q3betRollInfo = betRollInfoDao.queryLastRollInfoByQuartz(betInfo.getGid(),
					"Q3");
			quartz3_sb.append(" 【Q3】" + betInfo.getSc_Q3_total())
					.append(" " + fnum.format(Q3float) + "%").append("(")
					.append(Q3betRollInfo != null && Q3betRollInfo.getRatio_rou_c() != null
							? Q3betRollInfo.getRatio_rou_c() : "")
					.append(")")
					.append(" 进球（秒）：" + fnum2.format(CalUtil.computeScoreSec4Quartz(Q3betRollInfo)))
					.append(" 全场进球（秒）："
							+ fnum2.format(CalUtil.computeScoreSec4Alltime(Q3betRollInfo)));
			BetRollInfo Q4betRollInfo = betRollInfoDao.queryLastRollInfoByQuartz(betInfo.getGid(),
					"Q4");
			quartz4_sb.append(" 【Q4】" + betInfo.getSc_Q4_total()).append(" ").append("(")
					.append(Q4betRollInfo != null && Q4betRollInfo.getRatio_rou_c() != null
							? Q4betRollInfo.getRatio_rou_c() : "")
					.append(")")
					.append(" 进球（秒）：" + fnum2.format(CalUtil.computeScoreSec4Quartz(Q4betRollInfo)))
					.append(" 全场进球（秒）："
							+ fnum2.format(CalUtil.computeScoreSec4Alltime(Q4betRollInfo)));
		}
		log.info(sb.toString());
		log.info(ratio_re_sb.toString());
		log.info(ratio_rou_sb.toString());
		log.info(common_sb.toString());
		log.info("每节比赛分数,  每节结束后总分-(平均分*N节)/平均分*N节,  每节最后时间滚球大小球");
		log.info(quartz1_sb.toString());
		log.info(quartz2_sb.toString());
		log.info(quartz3_sb.toString());
		log.info(quartz4_sb.toString());
		log.info("平均每节需要：" + avg);
		log.info("平均每秒进球数：" + fnum2.format(scoreEachSec));
		printMinRatioRou("大小球滚球时间节点 最小", betInfo.getGid(), betInfo.getMinRatioRou());
		printMinRatioRou("大小球滚球时间节点 最大", betInfo.getGid(), betInfo.getMaxRatioRou());
		log.info("\n");
	}

	private void printMinRatioRou(String title, String gid, Float ratioRou) {
		List<BetRollInfo> list = betRollInfoDao.find(gid, ratioRou);

		for (BetRollInfo betRollInfo : list) {
			StringBuffer sb = new StringBuffer();
			sb.append(title + " " + betRollInfo.getRatio_rou_c()).append(" 总分：")
					.append(StringUtils.leftPad(betRollInfo.getSc_total(), 3, "  ")).append(",")
					.append(",Q4得分-全场:")
					.append(fnum2.format(CalUtil.computeScoreSec4Quartz(betRollInfo)
							- CalUtil.computeScoreSec4Alltime(betRollInfo)))
					.append(",大回报:").append(betRollInfo.getIor_ROUC()).append(",小回报:")
					.append(betRollInfo.getIor_ROUH()).append(betRollInfo.getSe_now())
					.append(" 进球（秒）：" + fnum2.format(CalUtil.computeScoreSec4Quartz(betRollInfo)))
					.append(" 全场进球（秒）："
							+ fnum2.format(CalUtil.computeScoreSec4Alltime(betRollInfo)))
					.append(" 节剩余（秒）:" + StringUtils.leftPad(betRollInfo.getT_count(), 3, " "))
					.append(" 期望值:" + fnum.format(expectRatio(betRollInfo)))
					.append(" [Q1]：" + betRollInfo.getSc_Q1_total())
					.append(" [Q2]：" + betRollInfo.getSc_Q2_total())
					.append(" [Q3]：" + betRollInfo.getSc_Q3_total())
					.append(" [Q4]：" + betRollInfo.getSc_Q4_total());
			log.info(sb.toString());
		}
	}

	private Float expectRatio(BetRollInfo betRollInfo) {
		if (betRollInfo == null || StringUtils.isEmpty(betRollInfo.getT_count())
				|| betRollInfo.getRatio_rou_c() == null)
			return 0f;
		Integer costTime = 0;
		if (betRollInfo.getSe_now().equals("Q1")) {
			costTime = +600;
		} else if (betRollInfo.getSe_now().equals("Q2")) {
			costTime += 1200;
		} else if (betRollInfo.getSe_now().equals("Q3")) {
			costTime += 1800;
		} else if (betRollInfo.getSe_now().equals("Q4")) {
			costTime += 2400;
		}

		costTime = costTime - CalUtil.getFixTcount(betRollInfo.getT_count());
		float chabie = Integer.valueOf(betRollInfo.getSc_total()) - scoreEachSec * costTime;
		return betRollInfo.getRatio_rou_c() + chabie;
	}

	private Float expectRatio2(BetRollInfo betRollInfo) {
		if (betRollInfo == null || betRollInfo.getRatio_rou_c() == null)
			return 0f;
		Integer costTime = 0;
		if (betRollInfo.getSe_now().equals("Q1")) {
			costTime = +600;
		} else if (betRollInfo.getSe_now().equals("Q2")) {
			costTime += 1200;
		} else if (betRollInfo.getSe_now().equals("Q3")) {
			costTime += 1800;
		} else if (betRollInfo.getSe_now().equals("Q4")) {
			costTime += 2400;
		}

		costTime = costTime - CalUtil.getFixTcount(betRollInfo.getT_count());
		float chabie = Integer.valueOf(betRollInfo.getSc_total()) - scoreEachSec * costTime;
		return betRollInfo.getRatio_rou_c() + chabie;
	}

	public static Float expectLeftScore(BetRollInfo betRollInfo) {
		if (betRollInfo == null)
			return 0f;
		Integer each = CalUtil.getEachQuartz(betRollInfo);
		Integer costTime = 0;
		if (betRollInfo.getSe_now().equals("Q1")) {
			costTime = +each;
		} else if (betRollInfo.getSe_now().equals("Q2")) {
			costTime += each * 2;
		} else if (betRollInfo.getSe_now().equals("Q3")) {
			costTime += each * 3;
		} else if (betRollInfo.getSe_now().equals("Q4")) {
			costTime += each * 4;
		}

		costTime = CalUtil.getFixTcount(betRollInfo.getT_count());
		float total = Integer.valueOf(betRollInfo.getSc_total())
				+ CalUtil.computeScoreSec4Quartz(betRollInfo) * costTime;
		return total;
	}

	public static Float computeScoreSec4Alltime(BetRollInfo betRollInfo, Boolean isAll) {
		return isAll ? InRateCache.computeScoreSec4Alltime(betRollInfo)
				: InRateCache.computeScoreSecBefore4Q(betRollInfo);
	}

	public static Float expectLeftScore2(BetRollInfo betRollInfo) {
		if (betRollInfo == null)
			return 0f;
		Integer each = CalUtil.getEachQuartz(betRollInfo);
		Integer costTime = 0;
		if (betRollInfo.getSe_now().equals("Q1")) {
			costTime = each;
		} else if (betRollInfo.getSe_now().equals("Q2")) {
			costTime += each * 2;
		} else if (betRollInfo.getSe_now().equals("Q3")) {
			costTime += each * 3;
		} else if (betRollInfo.getSe_now().equals("Q4")) {
			costTime += each * 4;
		}

		costTime = CalUtil.getFixTcount(betRollInfo.getT_count());
		float total = Integer.valueOf(betRollInfo.getSc_total())
				+ CalUtil.computeScoreSec4Alltime(betRollInfo) * costTime;
		return total;
	}

	public static Float expectLeftScore3(BetRollInfo betRollInfo) {
		if (betRollInfo == null)
			return 0f;
		Float q4rate = CalUtil.computeScoreSec4Quartz(betRollInfo);
		Float allrate = CalUtil.computeScoreSec4Alltime(betRollInfo);
		Integer eachCount = CalUtil.getEachQuartz(betRollInfo);
		Integer leftTime = CalUtil.getFixTcount(betRollInfo.getT_count());
		float q4cost = eachCount - leftTime;
		Integer totalCost = eachCount * 4 - leftTime;
		float expectRate = allrate * ((eachCount * 3) / (totalCost * 1f))
				+ q4rate * (q4cost / (totalCost * 1f));

		float expectTotal = Integer.valueOf(betRollInfo.getSc_total()) + expectRate * leftTime;
		return expectTotal;
	}

	private Integer numberPlus(String number1, String number2) {
		return (StringUtils.isEmpty(number1) ? 0 : Integer.valueOf(number1))
				+ (StringUtils.isEmpty(number2) ? 0 : Integer.valueOf(number2));
	}

	private Float subtract(String numbers1, String numbers2) {
		Float num1 = StringUtils.isEmpty(numbers1) ? 0 : Float.valueOf(numbers1);
		Float num2 = StringUtils.isEmpty(numbers2) ? 0 : Float.valueOf(numbers2);
		return Math.abs(num2 - num1);
	}

	 
	public static void main(String[] args) {
		Date startDate = DataFormat.stringToDate("2018-10-01 00:00:00",DataFormat.DATETIME_FORMAT);
		Date endDate = DataFormat.stringToDate("2019-01-13 23:59:59",DataFormat.DATETIME_FORMAT);
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		while(startCal.before(endCal)){ 
			
			Calendar tempEndCal = Calendar.getInstance();
			tempEndCal.setTime(startCal.getTime());
			tempEndCal.add(Calendar.MONTH, 1);
			if(tempEndCal.getTime().after(endDate))
				tempEndCal.setTime(endDate);
			else{ 
				tempEndCal.add(Calendar.SECOND, -1);
			}
			
			String fileName = DataFormat.DateToString(startCal.getTime(), DataFormat.DATETIME_FORMAT)+"-"+ DataFormat.DateToString(tempEndCal.getTime(), DataFormat.DATETIME_FORMAT);
			System.out.println(fileName);
			
			startCal.add(Calendar.MONTH, 1);  
		}
	}
}
