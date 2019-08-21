package com.sunmi.weipan.bean;

/**
 * 作者：create by comersss on 2018/11/16 09:42
 * 邮箱：904359289@qq.com
 * 查询订单支付状态请求参数
 */
public class ArgQueryOrderState {
    private String cash_id;
    private String out_trade_no;
    private String client;

    public String getCash_id() {
        return cash_id;
    }

    public void setCash_id(String cash_id) {
        this.cash_id = cash_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
