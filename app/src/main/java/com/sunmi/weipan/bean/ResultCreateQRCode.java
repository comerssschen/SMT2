package com.sunmi.weipan.bean;

/**
 * 作者：create by comersss on 2018/11/16 09:36
 * 邮箱：904359289@qq.com
 * 请求带金额的二维码返回参数
 */
public class ResultCreateQRCode {


    /**
     * out_trade_no : &Vr^
     * qr_code_url : )Cv2l5
     */

    private String out_trade_no;
    private String qr_code_url;

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getQr_code_url() {
        return qr_code_url;
    }

    public void setQr_code_url(String qr_code_url) {
        this.qr_code_url = qr_code_url;
    }
}
