package io.insightchain.inbwallet.mvps.model.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

/**
 * Http请求返回的包装数据
 * Created by lijilong on 03/07.
 */

public class HttpResult<T> implements Serializable {
    private String result;
    private Integer code;
    private String message;
    private T data;
    private String etoken;
    private Timestamp serverTime = new Timestamp(System.currentTimeMillis());
    private String clientIP;
    private Map<String, String> locales;
    private Boolean isSuccessful;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getEtoken() {
        return etoken;
    }

    public void setEtoken(String etoken) {
        this.etoken = etoken;
    }

    public Timestamp getServerTime() {
        return serverTime;
    }

    public void setServerTime(Timestamp serverTime) {
        this.serverTime = serverTime;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public Map<String, String> getLocales() {
        return locales;
    }

    public void setLocales(Map<String, String> locales) {
        this.locales = locales;
    }

    public Boolean getSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(Boolean successful) {
        isSuccessful = successful;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "result='" + result + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", etoken='" + etoken + '\'' +
                ", serverTime=" + serverTime +
                ", clientIP='" + clientIP + '\'' +
                ", locales=" + locales +
                ", isSuccessful=" + isSuccessful +
                '}';
    }
}