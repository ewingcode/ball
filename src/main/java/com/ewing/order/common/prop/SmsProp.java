package com.ewing.order.common.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sms")
public class SmsProp {
	public static String accessKeyId;
	public static String accessKeySecret;
	public static String ballTipTemplateCode;
	public static String ballTipSignName;

	public static String getAccessKeyId() {
		return accessKeyId;
	}

	public static void setAccessKeyId(String accessKeyId) {
		SmsProp.accessKeyId = accessKeyId;
	}

	public static String getAccessKeySecret() {
		return accessKeySecret;
	}

	public static void setAccessKeySecret(String accessKeySecret) {
		SmsProp.accessKeySecret = accessKeySecret;
	}

	public static String getBallTipTemplateCode() {
		return ballTipTemplateCode;
	}

	public static void setBallTipTemplateCode(String ballTipTemplateCode) {
		SmsProp.ballTipTemplateCode = ballTipTemplateCode;
	}

	public static String getBallTipSignName() {
		return ballTipSignName;
	}

	public static void setBallTipSignName(String ballTipSignName) {
		SmsProp.ballTipSignName = ballTipSignName;
	}

}
