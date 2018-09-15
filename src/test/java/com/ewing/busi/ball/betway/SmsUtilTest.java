package com.ewing.busi.ball.betway;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.ewing.order.Door;
import com.ewing.order.common.prop.SmsProp;
import com.ewing.order.util.SmsUtil;
import com.google.common.collect.Maps;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Door.class)
public class SmsUtilTest {
	@Test
	public void test() throws ClientException {
		Map<String, String> variables = Maps.newHashMap();
		variables.put("side", "大");
		variables.put("leauge", "联赛A");
		variables.put("host", "中国");
		variables.put("client", "澳洲");
		SendSmsResponse response = SmsUtil.sendSms(SmsProp.ballTipTemplateCode,
				SmsProp.ballTipSignName, "13826031714", variables);

		System.out.println("短信接口返回的数据----------------");
		System.out.println("Code=" + response.getCode());
		System.out.println("Message=" + response.getMessage());
		System.out.println("RequestId=" + response.getRequestId());
		System.out.println("BizId=" + response.getBizId());

		try {
			Thread.sleep(3000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 查明细
		if (response.getCode() != null && response.getCode().equals("OK")) {
			QuerySendDetailsResponse querySendDetailsResponse = SmsUtil
					.querySendDetails(response.getBizId());
			System.out.println("短信明细查询接口返回数据----------------");
			System.out.println("Code=" + querySendDetailsResponse.getCode());
			System.out.println("Message=" + querySendDetailsResponse.getMessage());
			int i = 0;
			for (QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse
					.getSmsSendDetailDTOs()) {
				System.out.println("SmsSendDetailDTO[" + i + "]:");
				System.out.println("Content=" + smsSendDetailDTO.getContent());
				System.out.println("ErrCode=" + smsSendDetailDTO.getErrCode());
				System.out.println("OutId=" + smsSendDetailDTO.getOutId());
				System.out.println("PhoneNum=" + smsSendDetailDTO.getPhoneNum());
				System.out.println("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());
				System.out.println("SendDate=" + smsSendDetailDTO.getSendDate());
				System.out.println("SendStatus=" + smsSendDetailDTO.getSendStatus());
				System.out.println("Template=" + smsSendDetailDTO.getTemplateCode());
			}
			System.out.println("TotalCount=" + querySendDetailsResponse.getTotalCount());
			System.out.println("RequestId=" + querySendDetailsResponse.getRequestId());
		}
	}
}
