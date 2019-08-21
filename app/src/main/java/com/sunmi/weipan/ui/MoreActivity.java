package com.sunmi.weipan.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;

import com.sunmi.weipan.BaseActivity;
import com.sunmi.weipan.R;
import com.sunmi.weipan.fragment.BackgroundManagerFragment;
import com.sunmi.weipan.fragment.GoodsManagerFragment;
import com.sunmi.weipan.fragment.PayModeSettingFragment;
import com.sunmi.weipan.utils.ScreenManager;

public class MoreActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout ivBack;
    public ScreenManager screenManager = null;
    public Display[] displays;

    public GoodsManagerFragment goodsManagerFragment;
    private PayModeSettingFragment payModeSettingFragment;
    private BackgroundManagerFragment backgroundManagerFragment;
    private FrameLayout fl_2, fl_3, fl_5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        initView();
        initAction();
        initData();
    }

    private void initView() {
        ivBack = findViewById(R.id.iv_back);
        goodsManagerFragment = new GoodsManagerFragment();
        payModeSettingFragment = new PayModeSettingFragment();
        backgroundManagerFragment = new BackgroundManagerFragment();
        fl_2 = findViewById(R.id.fl_2);
        fl_3 = findViewById(R.id.fl_3);
        fl_5 = findViewById(R.id.fl_5);
        addContent(goodsManagerFragment, false);
        checkState(0);
    }

    private void initAction() {
        ivBack.setOnClickListener(this);
        fl_2.setOnClickListener(this);
        fl_3.setOnClickListener(this);
        fl_5.setOnClickListener(this);
    }

    private void initData() {
        screenManager = ScreenManager.getInstance();
        screenManager.init(this);
        displays = screenManager.getDisplays();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_2:
                checkState(1);
                replaceContent(goodsManagerFragment, false);
                break;
            case R.id.fl_3:
                checkState(2);
                replaceContent(payModeSettingFragment, false);
                break;
            case R.id.fl_5:
                checkState(4);
                Bundle bundle = new Bundle();
                bundle.putString("id", System.currentTimeMillis() + "");
                backgroundManagerFragment.setArguments(bundle);
                this.replaceContent(backgroundManagerFragment, false);
                break;
            case R.id.iv_back:
                setResult(1);
                finish();
                break;
        }
    }


    private void checkState(int index) {
        fl_2.setBackgroundColor(Color.TRANSPARENT);
        fl_3.setBackgroundColor(Color.TRANSPARENT);
        fl_5.setBackgroundColor(Color.TRANSPARENT);
        switch (index) {
            case 1:
                fl_2.setBackgroundColor(Color.parseColor("#44ffffff"));
                break;
            case 2:
                fl_3.setBackgroundColor(Color.parseColor("#44ffffff"));
                break;
            case 4:
                fl_5.setBackgroundColor(Color.parseColor("#44ffffff"));
                break;
        }
    }


}
