package com.ewing.order.weixin.dto;

import java.util.Map;

import org.nutz.json.Json;
import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * 封装微信api返回结果, 输出实体类
 * 
 * @author 凡梦星尘(elkan1788@gmail.com)
 * @since 2.0
 */
public class ApiResult {

    private static final Log log = Logs.get();

//    private static ConfigReaderUtils _cr;
//
//    static {
//        try {
//            _cr = new ConfigReaderUtils(ResourceUtils.getURL("classpath:config/properties/ErrorCode.properties").getFile());
//        } catch (FileNotFoundException e) {
//            log.error(e.getMessage(), e);
//        }
//       
//    }

    private Map<String, Object> content;
    private String json;
    private Integer errCode;
    private String errMsg;
    private String errCNMsg;

    @SuppressWarnings("unchecked")
    public ApiResult(String json) {
        this.json = json;
        this.content = Json.fromJson(Map.class, json);
        this.errCode = (Integer) this.content.get("errcode");
        this.errMsg = (String) this.content.get("errmsg");
        this.errCNMsg = this.errCode == null ? "请求成功." : ErrorCode.SYSTEM_ERROR.getMsg();

        if (log.isInfoEnabled()) {
            log.infof("Wechat api result: %s", json);
            log.infof("%s", this.getErrCNMsg());
        }
    }

    public static ApiResult create(String json) {
        return new ApiResult(json);
    }

    public Object get(String key) {
        return content.get(key);
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public String getJson() {
        return json;
    }

    public Integer getErrCode() {
        return this.errCode;
    }

    public String getErrMsg() {
        return this.errMsg == null ? "Unknow Error!" : this.errMsg;
    }

    public String getErrCNMsg() {
        return this.errCNMsg == null ? "未知错误!" : this.errCNMsg;
    }

    public boolean isSuccess() {
        return (this.errCode == null || this.errCode.intValue() == 0);
    }

}
