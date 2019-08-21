package com.sunmi.weipan.utils;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.sunmi.weipan.bean.OnResponseListener;
import com.sunmi.weipan.bean.ResultBasePay;

/**
 * 作者：create by comersss on 2018/11/23 17:37
 * 邮箱：904359289@qq.com
 * 网络请求业务封装
 */
public class OkGoUtils {

    private static OkGoUtils okGoUtils;

    public static OkGoUtils getInstance() {
        if (okGoUtils == null)
            okGoUtils = new OkGoUtils();
        return okGoUtils;
    }


    /**
     * 不走网关的post请求
     *
     * @param bizData
     * @param method
     * @param onResponseListener
     */
    public void postNoGateWay(Activity context, String bizData, String method, final OnResponseListener onResponseListener) {
        OkGo.<String>post("http://dailypayapi.payweipan.com" + method)
                .tag(context)
                .upJson(bizData)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String json = response.body();
                            ResultBasePay result = new Gson().fromJson(json, ResultBasePay.class);
                            Log.i("test", "json = " + json);
                            if (!result.getIsError()) {
                                onResponseListener.onResponse(result.getData());
                            } else {
                                onResponseListener.onFail(result.getMsg());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            onResponseListener.onFail(e.toString());
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        onResponseListener.onFail(response.message());
                    }
                });
    }

}
