package com.ewing.order.ball.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.ball.bill.DailyBillResp;
import com.ewing.order.ball.bill.HistoryBillResp;
import com.ewing.order.ball.bill.TodayBillResp;
import com.ewing.order.ball.bk.bet.BetResp;
import com.ewing.order.ball.bk.bet.BkPreOrderViewResp;
import com.ewing.order.ball.bk.game.BkGameListResp;
import com.ewing.order.ball.bk.game.BkRollGameListResp;
import com.ewing.order.ball.ft.game.FtGameListResp;
import com.ewing.order.ball.ft.game.FtRollGameListResp;
import com.ewing.order.ball.league.LeagueResp;
import com.ewing.order.ball.leaguelist.LeagueListResp;
import com.ewing.order.ball.login.LoginResp;
import com.ewing.order.ball.login.MemberResp;
import com.ewing.order.common.exception.BusiException;
import com.ewing.order.common.prop.BallmatchProp;
import com.ewing.order.util.HttpUtils;

/**
 *
 * @author tansonlam
 * @create 2018年7月20日
 */
public class RequestTool {
	private static Logger log = LoggerFactory.getLogger(RequestTool.class);
	private final static String ballDomain = BallmatchProp.url;

	private final static String userAgent = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/53";

	public static class ErrorCode {
		public static final String doubleLogin = "error@doubleLogin";
	}

	private static String httpRequest(String url, String method, Map<String, String> params,
			Map<String, String> headerAttribute) {
		String resp = HttpUtils.request(url, "POST", params, getHeaders());
		if (resp != null) {
			if (resp.indexOf(ErrorCode.doubleLogin) > -1) {
				throw new BusiException(ErrorCode.doubleLogin);
			}
		}
		return resp;
	}
	
	private static String httpRequest(String url, String method, String params,
			Map<String, String> headerAttribute) {
		String resp = HttpUtils.request2(url, "POST", params, getHeaders(),null);
		if (resp != null) {
			if (resp.indexOf(ErrorCode.doubleLogin) > -1) {
				throw new BusiException(ErrorCode.doubleLogin);
			}
		}
		return resp;
	}

	private static Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("User-Agent", userAgent); 
		headers.put("Content-type", "application/x-www-form-urlencoded");
		headers.put("Cookie", "OUTFOX_SEARCH_USER_ID_NCOO=946313589.0114888; _ga=GA1.1.1246637889.1533712119; _gid=GA1.1.650524102.1539590053; _gat=1");
		/*Content-type:application/x-www-form-urlencoded
		Cookie:OUTFOX_SEARCH_USER_ID_NCOO=946313589.0114888; _ga=GA1.1.1246637889.1533712119; _gid=GA1.1.650524102.1539590053; _gat=1
		Host:205.201.1.182
		Origin:http://205.201.1.182
		Referer:http://205.201.1.182/
*/	 	headers.put("Host", "205.201.1.182");
		headers.put("Origin", "http://205.201.1.182");
		headers.put("Referer", "http://205.201.1.182");  
		return headers;
	}

	/**
	 * 登陆接口
	 */
	public static LoginResp login(String user, String password) {
		if (StringUtils.isEmpty(password)) {
			throw new BusiException("密码不能为空！");
		}
		String url = ballDomain + "/app/login.php";
		Map<String, String> data = new HashMap<String, String>();

		data.put("username", user);
		data.put("password", password);
		data.put("langx", "zh-cn");
		data.put("app", "N");
		data.put("auto", "FFCHCF");
		data.put("blackbox", "");
		log.info("login request:" + data);
		String resp = httpRequest(url, "POST", data, getHeaders());
		if (HttpUtils.isErrorResp(resp))
			throw new BusiException("登陆出错！");
		return LoginResp.fromResp(resp);

	}

	/**
	 * 获取会员信息
	 */
	public static MemberResp memberSet(String uid) {
		String url = ballDomain + "/app/member/memSet.php";
		Map<String, String> data = new HashMap<String, String>();
		data.put("uid", uid);
		data.put("action", "GET");
		data.put("langx", "zh-cn");
		String resp = httpRequest(url, "POST", data, getHeaders());
		if (HttpUtils.isErrorResp(resp))
			throw new BusiException("获取会员信息出错！");
		return MemberResp.fromResp(resp);
	}

	/**
	 * 心跳
	 */
	public static void heartBeat(String uid) {
		String url = ballDomain + "/app/member/check_login_domain.php";
		Map<String, String> data = new HashMap<String, String>();
		data.put("code", "get_login_domain");
		data.put("uid", uid);
		String r = httpRequest(url, "POST", data, getHeaders());
		//log.info("heart resp:" + r);
	}

	/**
	 * 获取赛程总数
	 */
	public static LeagueResp getLeaguesCount(String uid) {
		String url = ballDomain + "/app/member/get_league_count.php";
		Map<String, String> data = new HashMap<String, String>();
		data.put("uid", uid);
		data.put("langx", "zh-cn");
		data.put("date", "ALL");
		data.put("sorttype", "league");
		data.put("ltype", "3");
		data.put("classname", "home");
		String resp = httpRequest(url, "POST", data, getHeaders());
		if (HttpUtils.isErrorResp(resp))
			throw new BusiException("获取赛程出错！");
		LeagueResp leagueResp = new LeagueResp();
		return leagueResp.fromResp(resp);
	}

	/**
	 * 获取指定类型的联赛列表
	 * 
	 * @param uid
	 * @param gtype
	 * @param showType
	 */
	public static LeagueListResp getLeagueList(String uid, String gtype, String showType) {
		String url = ballDomain + "/app/member/get_league_list.php";
		Map<String, String> data = new HashMap<String, String>();
		data.put("uid", uid);
		data.put("langx", "zh-cn");
		data.put("ltype", "3");
		data.put("gtype", gtype);
		data.put("showtype", showType);
		data.put("sorttype", "");
		data.put("date", "");
		data.put("isP", "");
		String resp = httpRequest(url, "POST", data, getHeaders());
		//log.info("getLeagueList:" + resp);
		if (HttpUtils.isErrorResp(resp))
			throw new BusiException("获取指定类型的联赛列表！");
		LeagueListResp leagueListResp = new LeagueListResp();

		return leagueListResp.fromResp(resp);

	}

	/**
	 * 获取指定联赛的投注项目
	 * 
	 * @param uid
	 * @param gtype
	 * @param showType
	 * @param leagueId
	 */
	public static BkGameListResp getBkGameList(String uid, String gtype, String showType,
			String leagueId) {
		String url = ballDomain + "/app/member/get_game_list.php";
		Map<String, String> data = new HashMap<String, String>();
		data.put("uid", uid);
		data.put("langx", "zh-cn");
		data.put("ltype", "3");
		data.put("gtype", gtype);
		data.put("showtype", showType);
		data.put("sorttype", "");
		data.put("lid", leagueId);
		data.put("date", "");
		data.put("isP", "");
		String resp = httpRequest(url, "POST", data, getHeaders());
		// log.info("getLeagueList:" + resp);
		if (HttpUtils.isErrorResp(resp))
			throw new BusiException("获取指定联赛的投注项目失败！");
		BkGameListResp gameListResp = new BkGameListResp();
		return gameListResp.fromResp(resp);
	}

	/**
	 * 获取指定联赛的投注项目
	 * 
	 * @param uid
	 * @param gtype
	 * @param showType
	 * @param leagueId
	 */
	public static BkRollGameListResp getBkRollGameList(String uid, String gtype, String showType,
			String leagueId) {
		String url = ballDomain + "/app/member/get_game_list.php";
		Map<String, String> data = new HashMap<String, String>();
		data.put("uid", uid);
		data.put("langx", "zh-cn");
		data.put("ltype", "3");
		data.put("gtype", gtype);
		data.put("showtype", showType);
		data.put("sorttype", "");
		data.put("lid", leagueId);
		data.put("date", "");
		data.put("isP", "");
		String resp = httpRequest(url, "POST", data, getHeaders());
		 //log.info("getBkRollGameList:" + resp);
		if (HttpUtils.isErrorResp(resp))
			throw new BusiException("获取指定联赛的投注项目失败！");
		BkRollGameListResp gameListResp = new BkRollGameListResp();
		return gameListResp.fromResp(resp);
	}

	/**
	 * 获取指定足球联赛的投注项目
	 * 
	 * @param uid
	 * @param gtype
	 * @param showType
	 * @param leagueId
	 */
	public static FtRollGameListResp getFtRollGameList(String uid, String gtype, String showType,
			String leagueId) {
		String url = ballDomain + "/app/member/get_game_list.php";
		Map<String, String> data = new HashMap<String, String>();
		data.put("uid", uid);
		data.put("langx", "zh-cn");
		data.put("ltype", "3");
		data.put("gtype", gtype);
		data.put("showtype", showType);
		data.put("sorttype", "");
		data.put("lid", leagueId);
		data.put("date", "");
		data.put("isP", "");
		String resp = httpRequest(url, "POST", data, getHeaders());
		if (HttpUtils.isErrorResp(resp))
			throw new BusiException("获取指定联赛的投注项目失败！");
		FtRollGameListResp gameListResp = new FtRollGameListResp();
		return gameListResp.fromResp(resp);
	}

	/**
	 * 获取指定足球联赛的投注项目
	 * 
	 * @param uid
	 * @param gtype
	 * @param showType
	 * @param leagueId
	 */
	public static FtGameListResp getFtGameList(String uid, String gtype, String showType,
			String leagueId) {
		String url = ballDomain + "/app/member/get_game_list.php";
		Map<String, String> data = new HashMap<String, String>();
		data.put("uid", uid);
		data.put("langx", "zh-cn");
		data.put("ltype", "3");
		data.put("gtype", gtype);
		data.put("showtype", showType);
		data.put("sorttype", "");
		data.put("lid", leagueId);
		data.put("date", "");
		data.put("isP", "");
		String resp = httpRequest(url, "POST", data, getHeaders());
		if (HttpUtils.isErrorResp(resp))
			throw new BusiException("获取指定联赛的投注项目失败！");
		FtGameListResp gameListResp = new FtGameListResp();
		return gameListResp.fromResp(resp);
	}

	/**
	 * 足球下注
	 * uid:wa9oqpjcbm19706631l235121
		langx:zh-cn
		odd_f_type:H
		golds:50
		gid:2632366
		gtype:BK
		wtype:ROU
		rtype:ROUC
		chose_team:c
		ioratio:0.8
		con:147
		ratio:100
		autoOdd:
		timestamp:1539736553768
		timestamp2:0f0673b570c20c96d77f465d42dfcf1d
		isRB:Y
	 */
	public static BetResp bkbet(String uid, String gid, String gtype, String golds, String wtype,
			String side, BkPreOrderViewResp bkPreOrderViewResp) {
		String url = ballDomain + "/bk/bk_bet.php";
		String timestamp = String.valueOf(System.currentTimeMillis());
		Map<String, String> data = new HashMap<String, String>();
		data.put("uid", uid);
		data.put("langx", "zh-cn");
		data.put("odd_f_type", "H");
		data.put("golds", golds);
		data.put("gid", gid);
		data.put("gtype", gtype);
		data.put("wtype", wtype);
		data.put("rtype", wtype + "" + side);
		data.put("chose_team", side.toLowerCase());
		data.put("ioratio", bkPreOrderViewResp.getIoratio());
		data.put("con", bkPreOrderViewResp.getCon());
		data.put("ratio", bkPreOrderViewResp.getRatio());
		data.put("autoOdd", "");
		data.put("timestamp", timestamp);
		data.put("timestamp2", bkPreOrderViewResp.getTs());
		data.put("isRB", "Y");
		StringBuffer params = new StringBuffer();
		params.append("uid="+ uid);
		params.append("&langx=zh-cn");
		params.append("&odd_f_type=H");
		params.append("&golds="+golds);
		params.append("&gid="+gid);
		params.append("&gtype="+gtype);
		params.append("&wtype="+wtype);
		params.append("&rtype="+wtype + "" + side);
		params.append("&chose_team="+side.toLowerCase());
		params.append("&ioratio="+bkPreOrderViewResp.getIoratio());
		params.append("&con="+bkPreOrderViewResp.getCon());
		params.append("&ratio="+bkPreOrderViewResp.getRatio());
		params.append("&autoOdd="+"");
		params.append("&timestamp="+timestamp);
		params.append("&timestamp2="+bkPreOrderViewResp.getTs());
		params.append("&isRB=Y");
		
		log.info("betRequest:" + data);
		String resp = httpRequest(url, "POST", params.toString(), getHeaders());
		log.info("betResp:" + resp);
		if (HttpUtils.isErrorResp(resp))
			throw new BusiException("下注失败！"); 
		BetResp ftBetResp = new BetResp();
		ftBetResp = ftBetResp.fromResp(resp);
		//返回是延迟下注
		int errMaxTime = 3;
		while(ftBetResp.getCode().equals("570")&& --errMaxTime>0){
			try {
				TimeUnit.SECONDS.sleep(4);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String ticketId =ftBetResp.getTicket_id();
			String active = ftBetResp.getActive();
			String timestamps2 = ftBetResp.getTimestamp();
			
			ftBetResp = delayBkbet(uid, gid, gtype, wtype,ticketId, active, side, golds, timestamp,timestamps2);
			 
		}
		
		if (!StringUtils.isEmpty(ftBetResp.getErrormsg())) {
			ftBetResp.setGid(gid);
			ftBetResp.setGtype(gtype);
			ftBetResp.setWtype(wtype);
			ftBetResp.setRtype(wtype + "" + side);
			ftBetResp.setIoratio(bkPreOrderViewResp.getIoratio());
			ftBetResp.setRatio(bkPreOrderViewResp.getRatio());
			ftBetResp.setGold(golds);
		}
		return ftBetResp;
	}
	
	public static Long add6Sec(long timestamp){ 
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		cal.add(Calendar.SECOND, 6);
		return cal.getTimeInMillis();
	}
	/**
	 * 足球下注
	 * 
	 */
	public static BetResp delayBkbet(String uid, String gid, String gtype, String wtype, String ticket_id, String active,
			String side, String golds,String timestamp,String timestamp2) {
		String url = ballDomain + "/bk/bk_bet.php";
		Map<String, String> data = new HashMap<String, String>(); 
//		 
//		data.put("uid", uid);
//		data.put("gtype", gtype);
//		data.put("gid", gid);
//		data.put("ticket_id", ticket_id);
//		data.put("active", "m_delay");
//		data.put("langx", "zh-cn"); 
//		data.put("timestamp", String.valueOf(add6Sec(Long.valueOf(timestamp))));
//		data.put("timestamp2", timestamp2);
//		data.put("wtype", wtype);
//		data.put("rtype", wtype + "" + side); 
//		log.info("betRequest:" + data);
		
		String params = "uid="+uid;
			params += "&gtype="+gtype;
			params += "&gid="+gid;
			params += "&ticket_id="+ticket_id;
			params +="&active=m_delay";
			params +="&langx=zh-cn";
			params +="&timestamp="+String.valueOf(System.currentTimeMillis());
			params +="&timestamp2="+timestamp2;
			params +="&wtype="+wtype;
			params +="&rtype="+wtype + "" + side;
		log.info("delayBkbet request:" + params);
		String resp = httpRequest(url, "POST", params, getHeaders());
		log.info("delayBkbet resp:" + resp);
		if (HttpUtils.isErrorResp(resp))
			throw new BusiException("下注失败！");
		BetResp ftBetResp = new BetResp();
		ftBetResp = ftBetResp.fromResp(resp);
		if (!StringUtils.isEmpty(ftBetResp.getErrormsg())) {
			ftBetResp.setGid(gid);
			ftBetResp.setGtype(gtype);
			ftBetResp.setWtype(wtype);
			ftBetResp.setRtype(wtype + "" + side);
			ftBetResp.setIoratio(ftBetResp.getIoratio());
			ftBetResp.setRatio(ftBetResp.getRatio());
			ftBetResp.setGold(golds);
		}
		return ftBetResp;
	}

	/**
	 * 足球下注
	 * 
	 * @param uid
	 * @param gid
	 * @param gtype
	 * @param golds
	 * @param wtype
	 * @param side
	 * @param bkPreOrderViewResp
	 */

	public static BetResp ftbet(String uid, String gid, String gtype, String golds, String wtype,
			String side, BkPreOrderViewResp bkPreOrderViewResp) {
		String url = ballDomain + "/ft/ft_bet.php";
		String isRB = wtype.startsWith("R") ? "Y" : "N";
		Map<String, String> data = new HashMap<String, String>();
		data.put("uid", uid);
		data.put("langx", "zh-cn");
		data.put("odd_f_type", "H");
		data.put("golds", golds);
		data.put("gid", gid);
		data.put("gtype", gtype);
		data.put("wtype", wtype);
		data.put("rtype", wtype + "" + side);
		data.put("chose_team", side.toLowerCase());
		data.put("ioratio", bkPreOrderViewResp.getIoratio());
		data.put("con", bkPreOrderViewResp.getCon());
		data.put("ratio", bkPreOrderViewResp.getRatio());
		data.put("autoOdd", "");
		data.put("timestamp", String.valueOf(System.currentTimeMillis()));
		data.put("timestamp2", bkPreOrderViewResp.getTs());
		data.put("isRB", isRB);
		data.put("imp", "N");
		data.put("ptype", "");
		log.info("ftbet request:" + data.toString());
		String resp = httpRequest(url, "POST", data, getHeaders());
		log.info("ftbet response:" + resp);
		if (HttpUtils.isErrorResp(resp))
			throw new BusiException("下注失败！");

		BetResp ftBetResp = new BetResp();
		ftBetResp = ftBetResp.fromResp(resp);
		if (!StringUtils.isEmpty(ftBetResp.getErrormsg())) {

			ftBetResp.setGid(gid);
			ftBetResp.setGtype(gtype);
			ftBetResp.setWtype(wtype);
			ftBetResp.setRtype(wtype + "" + side);
			ftBetResp.setIoratio(bkPreOrderViewResp.getIoratio());
			ftBetResp.setRatio(bkPreOrderViewResp.getRatio());
			ftBetResp.setGold(golds);
		}
		return ftBetResp;

	}

	/**
	 * 获取篮球投注前的信息
	 * 
	 * @param uid
	 * @param gid
	 * @param gtype
	 * @param wtype
	 * @param side
	 */
	public static BkPreOrderViewResp getFtPreOrderView(String uid, String gid, String gtype,
			String wtype, String side) {
		String url = ballDomain + "/ft/ft_order_view.php";
		Map<String, String> data = new HashMap<String, String>();
		data.put("uid", uid);
		data.put("langx", "zh-cn");
		data.put("odd_f_type", "H");
		data.put("gid", gid);
		data.put("gtype", gtype);
		data.put("wtype", wtype.toLowerCase());
		data.put("chose_team", side.toLowerCase());

		String resp = httpRequest(url, "POST", data, getHeaders());
		if (HttpUtils.isErrorResp(resp))
			throw new BusiException("获取指定比赛的投注信息！");
		BkPreOrderViewResp bkPreOrderViewResp = new BkPreOrderViewResp();
		return bkPreOrderViewResp.fromResp(resp);
	}

	/**
	 * 获取篮球投注前的信息
	 * 
	 * @param uid
	 * @param gid
	 * @param gtype
	 * @param wtype
	 * @param side
	 */
	public static BkPreOrderViewResp getbkPreOrderView(String uid, String gid, String gtype,
			String wtype, String side) {
		String url = ballDomain + "/bk/bk_order_view.php"; 
		StringBuffer sb = new StringBuffer();
		
		sb.append("uid="+uid);
		sb.append("&langx=zh-cn");
		sb.append("&odd_f_type=H");
		sb.append("&gid="+gid);
		sb.append("&gtype="+gtype);
		sb.append("&wtype="+wtype.toLowerCase());
		sb.append("&chose_team="+side.toLowerCase());
		//url和param加在一起，不然接口报错
		String resp = httpRequest(url+"?"+sb.toString(), "POST", "", getHeaders());
		if (HttpUtils.isErrorResp(resp))
			throw new BusiException("获取指定比赛的投注信息！");
		BkPreOrderViewResp bkPreOrderViewResp = new BkPreOrderViewResp();
		return bkPreOrderViewResp.fromResp(resp);
	}

	/**
	 * 获取指定日期的投注记录
	 * 
	 * @param uid
	 * @param date
	 */
	public static DailyBillResp getDailyBill(String uid, String date) {
		String url = ballDomain + "/app/member/get_history_view.php";
		Map<String, String> data = new HashMap<String, String>();
		data.put("uid", uid);
		data.put("langx", "zh-cn");
		data.put("LS", "g");
		data.put("today_gmt", date);
		data.put("gtype", "ALL");
		data.put("tmp_flag", "Y");

		String resp = httpRequest(url, "POST", data, getHeaders());
		if (HttpUtils.isErrorResp(resp))
			throw new BusiException("获取指定日期投注记录失败！");
		log.info("getHistoryView:" + resp);
		DailyBillResp billResp = new DailyBillResp();
		return billResp.fromResp(resp);
	}

	/**
	 * 获取今日投注记录
	 * 
	 * @param uid
	 * @param date
	 */
	public static TodayBillResp getTodayWagers(String uid) {
		String url = ballDomain + "/app/member/get_today_wagers.php";
		Map<String, String> data = new HashMap<String, String>();
		data.put("uid", uid);
		data.put("langx", "zh-cn");
		data.put("LS", "g");
		data.put("selGtype", "ALL");
		data.put("chk_cw", "N");
		String resp = httpRequest(url, "POST", data, getHeaders());
		if (HttpUtils.isErrorResp(resp))
			throw new BusiException("获取今日投注记录失败！");
		log.info("getTodayWagers:" + resp);
		TodayBillResp billResp = new TodayBillResp();
		return billResp.fromResp(resp);
	}

	/**
	 * 获取历史投注记录
	 * 
	 * @param uid
	 * @param date
	 */
	public static HistoryBillResp getHistoryData(String uid) {
		String url = ballDomain + "/app/member/get_history_data.php";
		Map<String, String> data = new HashMap<String, String>();
		data.put("uid", uid);
		data.put("langx", "zh-cn");
		data.put("gtype", "ALL");
		data.put("isAll", "N");
		String resp = httpRequest(url, "POST", data, getHeaders());
		log.info("getHistoryData:" + resp);
		if (HttpUtils.isErrorResp(resp))
			throw new BusiException("获取历史投注记录失败！");
		HistoryBillResp billResp = new HistoryBillResp();
		return billResp.fromResp(resp);
	}
}
