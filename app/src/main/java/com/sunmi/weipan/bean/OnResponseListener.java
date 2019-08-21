package com.sunmi.weipan.bean;

/**
 * 作者：create by comersss on 2018/11/29 13:48
 * 邮箱：904359289@qq.com
 * 网络请求回调
 */
public abstract class OnResponseListener {
    public abstract void onResponse(String serverRetData);

    public abstract void onFail(String errMsg);
}
