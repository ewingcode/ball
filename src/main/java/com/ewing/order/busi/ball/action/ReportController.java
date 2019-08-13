package com.ewing.order.busi.ball.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ewing.order.ball.util.CalUtil;
import com.ewing.order.core.web.base.BaseController;

/**
 *
 * @author tansonlam
 * @create 2019年1月29日 
 */

@Controller
public class ReportController extends BaseController { 

	@GetMapping("/win.op")
	public ModelAndView win() {
		Map<String,String> map =new HashMap<>();
		map.put("startDate", CalUtil.getShortStartDayOfWeek());
		return render("win",map);
	}
	
	@GetMapping("/windetail.op")
	public ModelAndView windetail() {
		Map<String,String> map =new HashMap<>();
		map.put("startDate", CalUtil.getShortStartDayOfWeek());
		return render("windetail",map);
	}
	
	@GetMapping("/betdetail.op")
	public ModelAndView betdetail() {
		Map<String,String> map =new HashMap<>();
		map.put("startDate", CalUtil.getShortStartDayOfWeek());
		return render("betdetail",map);
	}

}
