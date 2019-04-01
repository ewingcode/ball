package com.ewing.order.busi.ball.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

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
		return render("win");
	}
	
	@GetMapping("/windetail.op")
	public ModelAndView windetail() {
		return render("windetail");
	}
	
	@GetMapping("/betdetail.op")
	public ModelAndView betdetail() {
		return render("betdetail");
	}

}
