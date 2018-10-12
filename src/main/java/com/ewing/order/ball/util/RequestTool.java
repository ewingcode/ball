package com.ewing.order.ball.util;

import java.util.HashMap;
import java.util.Map;

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

	private static Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("User-Agent", userAgent);
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
	 * 
	 */
	public static BetResp bkbet(String uid, String gid, String gtype, String golds, String wtype,
			String side, BkPreOrderViewResp bkPreOrderViewResp) {
		String url = ballDomain + "/bk/bk_bet.php";
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
		data.put("isRB", "N");
		String resp = httpRequest(url, "POST", data, getHeaders());
		log.info("betResp:" + resp);
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
