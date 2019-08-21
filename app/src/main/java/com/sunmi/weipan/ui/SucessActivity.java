package com.sunmi.weipan.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.sunmi.weipan.BaseActivity;
import com.sunmi.weipan.utils.CountDownHelper;
import com.sunmi.weipan.R;
import com.sunmi.weipan.utils.SucessEvent;
import com.sunmi.weipan.adapter.SusceeAdapter;
import com.sunmi.weipan.bean.MenusBean;
import com.sunmi.weipan.fragment.PayModeSettingFragment;
import com.sunmi.weipan.utils.SharePreferenceUtil;
import com.szsicod.print.escpos.PrinterAPI;
import com.szsicod.print.io.InterfaceAPI;
import com.szsicod.print.io.USBAPI;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：create by comersss on 2019/3/18 15:48
 * 邮箱：904359289@qq.com
 */
public class SucessActivity extends BaseActivity {
    @BindView(R.id.tv_total_count)
    TextView tvTotalCount;
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;
    @BindView(R.id.tv_bt_parm1)
    TextView tvBtParm1;
    @BindView(R.id.tv_bt_parm2)
    TextView tvBtParm2;
    @BindView(R.id.tv_time_tiger)
    TextView tvTimeTiger;
    @BindView(R.id.lv_menus)
    ListView lvMenus;
    private SusceeAdapter menusAdapter;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private InterfaceAPI io;
    public PrinterAPI mPrinter = PrinterAPI.getInstance();
    private String PayMoney;
    private int totalCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucess);
        ButterKnife.bind(this);
        totalCount = getIntent().getIntExtra("count", 0);
        ArrayList<MenusBean> menus = (ArrayList<MenusBean>) getIntent().getSerializableExtra("menus");
        Log.i("test", "menus = " + menus.toString());
        menusAdapter = new SusceeAdapter(this, menus);
        lvMenus.setAdapter(menusAdapter);
        float price = 0.00f;
        for (MenusBean bean1 : menus) {
            price = price + Float.parseFloat(bean1.getMoney().substring(1));
        }
        tvTotalMoney.setText("合计 ：￥" + decimalFormat.format(price));
        menusAdapter.update(menus);
        tvTotalCount.setText("共" + totalCount + "件商品");
        boolean isRealDeal = (boolean) SharePreferenceUtil.getParam(SucessActivity.this, PayModeSettingFragment.IS_REAL_DEAL, PayModeSettingFragment.default_isRealDeal);
        if (isRealDeal) {
            PayMoney = "" + price;
        } else {
            PayMoney = "0.01";
        }
        tvBtParm1.setText("实付 ￥" + PayMoney);
        EventBus.getDefault().post(new SucessEvent(""));
        CountDownHelper helper = new CountDownHelper(tvTimeTiger, 10, 1);
        helper.setOnFinishListener(new CountDownHelper.OnFinishListener() {
            @Override
            public void fin() {
                finish();
            }
        });
        helper.start();
//        print(menus);

    }

    @OnClick({R.id.tv_time_tiger})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_time_tiger:
                finish();
                break;
        }
    }

    private void print(final List<MenusBean> list) {
        if (io == null) {
            io = new USBAPI(SucessActivity.this);
        }
        if (PrinterAPI.SUCCESS == mPrinter.connect(io)) {//连接打印机
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mPrinter.setAlignMode(1);//0左对齐，1居中，2右对齐

                        mPrinter.printString("\n杭州微盘每日付收款明细");
                        mPrinter.printString("\n\n\n________________________________");
                        mPrinter.setAlignMode(0);
                        mPrinter.printString("\n销售单");
                        mPrinter.printString("\n--------------------------------\n");
                        printString("自助收银", "共" + totalCount + "件");
                        mPrinter.printString("--------------------------------");
                        mPrinter.printString("\n店员号：032  POS号32");
                        mPrinter.printString("\n" + TimeUtils.getNowString());
                        mPrinter.printString("\n--------------------------------\n");
                        printString("商品名称/数量/单位", "合计");
                        mPrinter.printString("--------------------------------\n");
                        float price = 0.00f;
                        for (MenusBean menu : list) {
                            printString(menu.getName() + " * " + menu.getCount() + " " + menu.getUnit(), menu.getMoney().replace("¥", "￥"));
                            price = price + Float.parseFloat(menu.getMoney().substring(1));
                        }
                        mPrinter.printString("________________________________");
                        printString("商品合计", "￥" + price);
                        printString("优惠金额", "-￥0.00");
                        printString("优惠券金额", "-￥0.00");
                        mPrinter.printString("\n");
                        printString("应付金额", "￥" + price);
                        printString("实际支付金额", "￥" + PayMoney);
                        mPrinter.printString("\n依法预付费卡、第三方卡、支付优惠的支付方式其消费金额不再重复开立发票");
                        mPrinter.printString("\n\n--------------------------------\n\n\n\n\n\n");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void printString(String left, String right) {
        int lLength = 0;
        int rLength = 0;
        try {
            lLength = left.getBytes("GBK").length;
            rLength = right.getBytes("GBK").length;
            mPrinter.setAlignMode(0);//0左对齐，1居中，2右对齐
            String nullString = "                                ";
            mPrinter.printString(left + nullString.substring(0, 32 - lLength - rLength) + right, "GBK", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("test", "lLength = " + lLength);
        Log.i("test", "rLength = " + rLength);
    }
}
