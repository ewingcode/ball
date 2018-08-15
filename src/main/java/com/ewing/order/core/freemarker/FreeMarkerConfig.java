package com.ewing.order.core.freemarker;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import freemarker.template.TemplateException;

@Configuration
public class FreeMarkerConfig {

	@Autowired
	protected freemarker.template.Configuration configuration;
	@Autowired
	protected org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver resolver;
	@Autowired
	protected org.springframework.web.servlet.view.InternalResourceViewResolver springResolver;

	@PostConstruct
	public void setSharedVariable() {

		try {
			configuration.setSetting("template_update_delay", "1");
			configuration.setSetting("default_encoding", "UTF-8");
		} catch (TemplateException e) {
			e.printStackTrace();
		}

	//	springResolver.setSuffix(".jsp");
		springResolver.setOrder(1);

		resolver.setSuffix(".html"); // 解析后缀为html的</span>
		resolver.setCache(false);
		resolver.setRequestContextAttribute("request");
		resolver.setOrder(0);

	}
}
