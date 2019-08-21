package com.sunmi.weipan.bean;

/**
 * 作者：create by comersss on 2018/11/29 09:24
 * 邮箱：904359289@qq.com
 * 支付相关的返回结果
 */
public class ResultBasePay {


    /**
     * Format : json
     * Version : 1.0
     * IsError : false
     * Code : pay_success
     * Msg : 支付成功
     * Data : {"total_fee":0.03,"discount_fee":0.0,"receipt_fee":0.03,"pay_type":2,"pay_time":"2018-11-29T09:22:39","out_trade_no":"00026607100117233181129092238181","merchant_no":"MYBKJHZF2018112909223894056179","transaction_id":"4200000234201811293546805536","buyer_id":"oO157v17Ptyt-Q5MYL0mX3VfGrck","state":10,"error":"","result":"0"}
     */

    private String Format;
    private String Version;
    private boolean IsError;
    private String Code;
    private String Msg;
    private String Data;

    public String getFormat() {
        return Format;
    }

    public void setFormat(String Format) {
        this.Format = Format;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }

    public boolean getIsError() {
        return IsError;
    }

    public void setIsError(boolean IsError) {
        this.IsError = IsError;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public String getData() {
        return Data;
    }

    public void setData(String Data) {
        this.Data = Data;
    }
}
