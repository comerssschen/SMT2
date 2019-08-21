package com.sunmi.weipan.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * 作者：create by comersss on 2019/1/2 16:31
 * 邮箱：904359289@qq.com
 * 验证码倒计时
 */
public class CountDownHelper {
    // 倒计时timer
    private CountDownTimer countDownTimer;
    // 计时结束的回调接口
    private OnFinishListener listener;
    private TextView textView;
    private String tip = "";

    public CountDownHelper(final TextView button, int max, int interval) {
        this(button, max, interval, null);
    }

    /**
     * @param textView 需要显示倒计时的textView
     * @param max      需要进行倒计时的最大值,单位是秒
     * @param interval 倒计时的间隔，单位是秒
     */
    public CountDownHelper(final TextView textView, int max, int interval, final String tip) {
        this.textView = textView;
        this.tip = tip;
        countDownTimer = new CountDownTimer(max * 1000, interval * 1000 - 10) {
            @Override
            public void onTick(long time) {
                int second = (int) ((time + 15) / 1000);
                textView.setText("立即结束(" + second + "s)");
            }

            @Override
            public void onFinish() {
                textView.setText("立即结束");
                if (listener != null) {
                    listener.fin();
                }
            }
        };
    }

    public void start() {
        countDownTimer.start();
    }

    public void stop() {
        countDownTimer.cancel();
    }

    /**
     * 设置倒计时结束的监听器
     *
     * @param listener
     */
    public void setOnFinishListener(OnFinishListener listener) {
        this.listener = listener;
    }

    /**
     * 计时结束的回调接口
     */
    public interface OnFinishListener {
        void fin();
    }
}
