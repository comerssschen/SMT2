package com.sunmi.weipan.bean;

/**
 * 作者：create by comersss on 2018/11/16 09:34
 * 邮箱：904359289@qq.com
 * 请求带金额的二维码请求参数
 */
public class ArgCreateQRCode {
    private String cash_id;
    private String total_fee;
    private String client;
    private String remark;
    private String pay_type;

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getCash_id() {
        return cash_id;
    }

    public void setCash_id(String cash_id) {
        this.cash_id = cash_id;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
