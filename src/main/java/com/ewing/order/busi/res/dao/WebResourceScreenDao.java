package com.ewing.order.busi.res.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ewing.order.busi.res.ddl.WebResourceScreenshot;
import com.ewing.order.common.contant.IsEff;
import com.ewing.order.core.jpa.BaseDao;
import com.ewing.order.util.SqlUtil;

/**
 * 资源截图
 * 
 * @author tansonlam
 * @createDate 2016年2月4日
 * 
 */
@Component
public class WebResourceScreenDao {

	@Resource
	private BaseDao baseDao;

	public List<WebResourceScreenshot> getResScreenshot(List<Integer> resourceIds) {
		return baseDao.find("resource_id in (" + SqlUtil.array2InCondition(resourceIds) + ") and iseff='"
				+ IsEff.EFFECTIVE + "' order by rank", WebResourceScreenshot.class);
	}

	public List<WebResourceScreenshot> getResScreenshot(Integer resourceId) {
		return baseDao.find("resource_id =  " + resourceId + " and iseff='" + IsEff.EFFECTIVE + "' order by rank",
				WebResourceScreenshot.class);
	}

}
