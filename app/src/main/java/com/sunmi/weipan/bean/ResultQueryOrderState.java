package com.sunmi.weipan.bean;

/**
 * 作者：create by comersss on 2018/11/16 09:43
 * 邮箱：904359289@qq.com
 * 查询订单支付状态返回参数
 */
public class ResultQueryOrderState {

    /**
     * out_trade_no : 自定义单号
     * merchant_no : 商户单号
     * transaction_id : 第三方订单号（微信或支付宝单号）
     * state : 订单状态 1支付中 7退款中 8退款成功 9支付失败 10支付成功
     * pay_type : 支付类型 1微信 2支付宝 3银联刷卡
     * total_fee : 订单总金额
     * discount_fee : 优惠金额
     * receipt_fee : 实收金额
     * net_income : 净收入
     * pay_time : 支付时间
     * buyer_id : 消费者标识（微信openid 支付宝buyer_logon_id）
     * merchant_id : 商户编号
     * merchant_name : 商户全称
     * stores_id : 门店编号
     * stores_name : 门店名称
     * cash_id : 收银员编号
     * cash_name : 收银员名称
     * remark : 备注
     */

    private String out_trade_no;
    private String merchant_no;
    private String transaction_id;
    private String state;
    private String pay_type;
    private String total_fee;
    private String discount_fee;
    private String receipt_fee;
    private String net_income;
    private String pay_time;
    private String buyer_id;
    private String merchant_id;
    private String merchant_name;
    private String stores_id;
    private String stores_name;
    private String cash_id;
    private String cash_name;
    private String remark;

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getMerchant_no() {
        return merchant_no;
    }

    public void setMerchant_no(String merchant_no) {
        this.merchant_no = merchant_no;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getDiscount_fee() {
        return discount_fee;
    }

    public void setDiscount_fee(String discount_fee) {
        this.discount_fee = discount_fee;
    }

    public String getReceipt_fee() {
        return receipt_fee;
    }

    public void setReceipt_fee(String receipt_fee) {
        this.receipt_fee = receipt_fee;
    }

    public String getNet_income() {
        return net_income;
    }

    public void setNet_income(String net_income) {
        this.net_income = net_income;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getStores_id() {
        return stores_id;
    }

    public void setStores_id(String stores_id) {
        this.stores_id = stores_id;
    }

    public String getStores_name() {
        return stores_name;
    }

    public void setStores_name(String stores_name) {
        this.stores_name = stores_name;
    }

    public String getCash_id() {
        return cash_id;
    }

    public void setCash_id(String cash_id) {
        this.cash_id = cash_id;
    }

    public String getCash_name() {
        return cash_name;
    }

    public void setCash_name(String cash_name) {
        this.cash_name = cash_name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
