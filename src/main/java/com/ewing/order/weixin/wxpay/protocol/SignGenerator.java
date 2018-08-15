package com.ewing.order.weixin.wxpay.protocol;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;

import com.ewing.order.common.prop.WeixinProp;
import com.ewing.order.util.MD5Util;
import com.google.common.base.Charsets;

/**
 * 微信支付接口的签名方法<br/>
 * 
 * <pre>
 * 举例: 
 * 假设传送的参数如下：
 * appid： wxd930ea5d5a258f4f
 * mch_id： 10000100
 * device_info： 1000
 * body： test
 * nonce_str： ibuaiVcKdpRxkhJA<br/>
 * <b>注意：</b>
 * 微信支付API接口协议中包含字段nonce_str，主要保证签名不可预测。我们推荐生成随机数算法如下：调用随机数函数生成，将得到的值转换为字符串。
 * 
 * 第一步：对参数按照key=value的格式，并按照参数名ASCII字典序排序如下： 
 * stringA="appid=wxd930ea5d5a258f4f&body=test&device_info=1000&mch_id=10000100&nonce_str=ibuaiVcKdpRxkhJA";
 * 
 * 第二步：拼接API密钥：
 * stringSignTemp="stringA&key=192006250b4c09247ec02edce69f6a2d" 
 * sign=MD5(stringSignTemp).toUpperCase()="9A0A8659F005D6984697E2CA0A9CF3B7"
 * 最终得到最终发送的数据： 
 * <xml> 
 * <appid>wxd930ea5d5a258f4f</appid> 
 * <mch_id>10000100</mch_id> 
 * <device_info>1000<device_info> 
 * <body>test</body> 
 * <nonce_str>ibuaiVcKdpRxkhJA</nonce_str> 
 * <sign>9A0A8659F005D6984697E2CA0A9CF3B7</sign> 
 * <xml>
 * </pre>
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年1月23日
 *
 */
public class SignGenerator {

    public static String createSign(SortedMap<String, Object> params){
      return createSign(params, WeixinProp.payKey);
    }
    
    public static String createSign(SortedMap<String, Object> packageParams, String key) {
      StringBuffer sb = new StringBuffer();
      Set<Entry<String, Object>> es = packageParams.entrySet();
      Iterator<Entry<String, Object>> it = es.iterator();
      while (it.hasNext()) {
          Map.Entry<String, Object> entry = it.next();
          String k = (String) entry.getKey();
          String v = "" + entry.getValue();
          if (null != v && !"".equals(v) && !"sign".equals(k)
                  && !"key".equals(k)) {
              sb.append(k + "=" + v + "&");
          }
      }
      sb.append("key=" + key);
      System.out.println("md5 sb:" + sb);
      String sign = MD5Util.MD5Encode(sb.toString(), Charsets.UTF_8.name())
              .toUpperCase();
      System.out.println("packge签名:" + sign);
      return sign;

  }

}
