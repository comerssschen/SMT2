package com.sunmi.weipan.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.google.gson.Gson;
import com.sunmi.extprinterservice.ExtPrinterService;
import com.sunmi.payment.PaymentService;
import com.sunmi.weipan.bean.Config;
import com.sunmi.weipan.bean.PayResultBean;
import com.sunmi.weipan.bean.Request;
import com.sunmi.weipan.BaseActivity;
import com.sunmi.weipan.R;
import com.sunmi.weipan.utils.SucessEvent;
import com.sunmi.weipan.adapter.GoodsAdapter;
import com.sunmi.weipan.adapter.SusceeAdapter;
import com.sunmi.weipan.bean.GoodsCode;
import com.sunmi.weipan.bean.GvBeans;
import com.sunmi.weipan.bean.MenusBean;
import com.sunmi.weipan.fragment.GoodsManagerFragment;
import com.sunmi.weipan.fragment.PayModeSettingFragment;
import com.sunmi.weipan.utils.ResourcesUtils;
import com.sunmi.weipan.utils.ScreenManager;
import com.sunmi.weipan.utils.SharePreferenceUtil;
import com.sunmi.weipan.view.CustomDialog;
import com.sunmi.weipan.view.PhotoPopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private PhotoPopupWindow mPhotoPopupWindow;
    private String PayMoney;
    private final String TAG = "MainActivity";
    private ListView lvMenus;
    private SusceeAdapter menusAdapter;
    private ArrayList<MenusBean> menus = new ArrayList<>();
    private RecyclerView reDrink;
    private RecyclerView re_snacks;
    private RecyclerView re_others;
    private FrameLayout flShoppingCar;
    private GoodsAdapter drinkAdapter;
    private GoodsAdapter snackAdapter;
    private GoodsAdapter othersAdapter;
    private List<GvBeans> mDrinksBean;
    private List<GvBeans> mSnacksBean;
    private List<GvBeans> mOthers;
    private TextView tvPrice;
    private TextView btnClear;
    private RelativeLayout rtlEmptyShopcar, rl_no_goods;
    private LinearLayout llyShopcar, ll_drinks, ll_snacks, ll_others, main_ll_pay;
    private ImageView ivCar;
    private TextView tvCar, tvCarMoeny;
    private Button btnPay;//去付款
    private BottomSheetLayout bottomSheetLayout;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private Button btnMore;//更多功能
    private ScreenManager screenManager = ScreenManager.getInstance();
    public static boolean isVertical = false;
    private CustomDialog customDialog;
    private LinearLayout llK1ShoppingCar;
    private RelativeLayout rlCar;
    private boolean isRealDeal;
    private int totalCount = 0;
    private EditText etScan;
    public static KPrinterPresenter kPrinterPresenter;
    private ExtPrinterService extPrinterService = null;//k1 打印服务
    private ResultReceiver resultReceiver;
    private Gson gson = new Gson();

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Subscribe
    public void onEvent(SucessEvent event) {
        kPrinterPresenter.print(menus, "55555");
        totalCount = 0;
        if (!isVertical) {
            llyShopcar.setVisibility(View.GONE);
            rtlEmptyShopcar.setVisibility(View.VISIBLE);
            tvPrice.setText(ResourcesUtils.getString(MainActivity.this, R.string.units_money_units) + "0.00");
            menus.clear();
            menusAdapter.update(menus);
        } else {
            menus.clear();
            tvCarMoeny.setText("");
            tvCar.setText("");
            tvCar.setVisibility(View.GONE);
            ivCar.setImageResource(R.drawable.car_gray);
            bottomSheetLayout.dismissSheet();
            btnPay.setBackgroundColor(Color.parseColor("#999999"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(MainActivity.this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;// 屏幕宽度
        int height = dm.heightPixels;// 屏幕宽度
        Log.i("test", "width" + width);
        Log.i("test", "height" + height);
        Log.i("test", dm.densityDpi + " hhh " + dm.density);
        isVertical = height > width;
        menus.clear();
        initView();
        initData();
        initAction();
    }


    @Override
    protected void onStart() {
        super.onStart();
        int goodsMode = (int) SharePreferenceUtil.getParam(this, GoodsManagerFragment.GOODSMODE_KEY, GoodsManagerFragment.defaultGoodsMode);
        switch (goodsMode) {
            case 0:
                ll_snacks.setVisibility(View.GONE);
                ll_drinks.setVisibility(View.GONE);
                rl_no_goods.setVisibility(View.VISIBLE);
                break;
            case GoodsManagerFragment.Goods_1 | GoodsManagerFragment.Goods_2:
                ll_drinks.setVisibility(View.VISIBLE);
                ll_snacks.setVisibility(View.VISIBLE);
                rl_no_goods.setVisibility(View.GONE);
                break;
            case GoodsManagerFragment.Goods_3 | GoodsManagerFragment.Goods_4:
                ll_snacks.setVisibility(View.GONE);
                ll_drinks.setVisibility(View.GONE);
                rl_no_goods.setVisibility(View.GONE);
                break;
            case GoodsManagerFragment.Goods_1 | GoodsManagerFragment.Goods_2 + GoodsManagerFragment.Goods_3 | GoodsManagerFragment.Goods_4:
                ll_drinks.setVisibility(View.VISIBLE);
                ll_snacks.setVisibility(View.VISIBLE);
                rl_no_goods.setVisibility(View.GONE);

                break;
        }
        ll_others.setVisibility(mOthers.isEmpty() ? View.GONE : View.VISIBLE);
        othersAdapter.notifyDataSetChanged();
        drinkAdapter.notifyDataSetChanged();
        snackAdapter.notifyDataSetChanged();
    }

    private Handler handler = new Handler();

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun = new Runnable() {

        @Override
        public void run() {
            scanResult(etScan.getText().toString().replace(" ", "").replace("\n", ""));
            etScan.setText("");
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (!ObjectUtils.isEmpty(etScan)) {
            etScan.requestFocus();
        }
    }

    private void registerResultReceiver() {
        resultReceiver = new ResultReceiver(new ResultReceiver.ResultCallback() {
            @Override
            public void callback(String result) {
                Log.i("test", result);
                if (ObjectUtils.isEmpty(result)) {
                    ToastUtils.showShort("网络连接失败，请重试");
                    return;
                }
                PayResultBean response = gson.fromJson(result, PayResultBean.class);
                switch (response.getResultCode()) {
                    case "T00"://交易成功
                        kPrinterPresenter.print(menus, "支付宝扫脸");
                        Intent intent = new Intent(MainActivity.this, SucessActivity.class);
                        intent.putExtra("menus", (Serializable) result);
                        intent.putExtra("count", totalCount);
                        startActivity(intent);
                        break;
                    default:
                        // 交易失败
                        break;
                }
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ResultReceiver.RESPONSE_ACTION);
        registerReceiver(resultReceiver, intentFilter);
    }

    private ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            extPrinterService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            extPrinterService = ExtPrinterService.Stub.asInterface(service);
            kPrinterPresenter = new KPrinterPresenter(MainActivity.this, extPrinterService);
        }
    };

    private void initView() {
        Intent intent = new Intent();
        intent.setPackage("com.sunmi.extprinterservice");
        intent.setAction("com.sunmi.extprinterservice.PrinterService");
        bindService(intent, connService, Context.BIND_AUTO_CREATE);
        registerResultReceiver();

        etScan = findViewById(R.id.et_scan);
//        etScan.setFocusable(true);
//        etScan.setFocusableInTouchMode(true);
        etScan.requestFocus();
//        etScan.setShowSoftInputOnFocus(false);
        etScan.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (delayRun != null) {
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler.removeCallbacks(delayRun);
                }
                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                if (!TextUtils.isEmpty(etScan.getText())) {
                    handler.postDelayed(delayRun, 100);
                }
            }
        });

        TextView view = findViewById(R.id.app_name);
        view.setVisibility(isVertical ? View.INVISIBLE : View.VISIBLE);
        lvMenus = findViewById(R.id.lv_menus);
        tvPrice = findViewById(R.id.main_tv_price);
        btnClear = findViewById(R.id.main_btn_clear);
        llyShopcar = findViewById(R.id.lly_shopcar);
        rtlEmptyShopcar = findViewById(R.id.rtl_empty_shopcar);
        flShoppingCar = findViewById(R.id.fl_shopping_car);
        main_ll_pay = findViewById(R.id.main_ll_pay);
        btnMore = findViewById(R.id.main_btn_more);
        reDrink = findViewById(R.id.gv_drinks);
        re_snacks = findViewById(R.id.gv_snacks);
        re_others = findViewById(R.id.gv_others);
        ll_drinks = findViewById(R.id.ll_drinks);
        ll_snacks = findViewById(R.id.ll_snacks);
        ll_others = findViewById(R.id.ll_others);
        rl_no_goods = findViewById(R.id.rl_no_goods);
        bottomSheetLayout = findViewById(R.id.bottomSheetLayout);
        ivCar = findViewById(R.id.iv_car);

        btnPay = findViewById(R.id.main_k1_btn_pay);
        tvCarMoeny = findViewById(R.id.tv_car_money);
        tvCar = findViewById(R.id.tv_car_num);

        llK1ShoppingCar = findViewById(R.id.ll_k1_shopping_car);
        rlCar = findViewById(R.id.main_btn_car);
        if (isVertical) {
            llK1ShoppingCar.setVisibility(View.VISIBLE);
            flShoppingCar.setVisibility(View.GONE);
        } else {
            llK1ShoppingCar.setVisibility(View.GONE);
            flShoppingCar.setVisibility(View.VISIBLE);
            llyShopcar.setVisibility(View.GONE);
            rtlEmptyShopcar.setVisibility(View.VISIBLE);
        }


    }

    private void scanResult(String code) {
        Log.i("test", "code = " + code);
        if (GoodsCode.getInstance().getGood().containsKey(code)) {
            GvBeans mOther = GoodsCode.getInstance().getGood().get(code);
            MenusBean bean = new MenusBean();
            bean.setId("" + (menus.size() + 1));
            bean.setMoney(mOther.getPrice());
            bean.setName(mOther.getName());
            bean.setCode(mOther.getCode());
            bean.setUnit(mOther.getUnit());
            bean.setUnitPrice(mOther.getPrice());
            bean.setCount(1);
            addMenus(bean);
            buildMenuJson(menus);
        }
//        for (GvBeans mOther : mOthers) {
//            if (ObjectUtils.equals(mOther.getCode(), code)) {
//                MenusBean bean = new MenusBean();
//                bean.setId("" + (menus.size() + 1));
//                bean.setMoney(mOther.getPrice());
//                bean.setName(mOther.getName());
//                bean.setCode(mOther.getCode());
//                bean.setUnit(mOther.getUnit());
//                bean.setUnitPrice(mOther.getPrice());
//                bean.setCount(1);
//                addMenus(bean);
//                buildMenuJson(menus);
//            }
//        }
    }

    private void initAction() {
        othersAdapter.setOnItemClickListener(new GoodsAdapter.OnItemClickListener() {

            private int pos;

            @Override
            public void onItemClick(View view, int position) {
                MenusBean bean = new MenusBean();
                bean.setId("" + (menus.size() + 1));
                bean.setMoney(mOthers.get(position).getPrice());
                bean.setName(mOthers.get(position).getName());
                bean.setCode(mOthers.get(position).getCode());
                bean.setUnit(mOthers.get(position).getUnit());
                bean.setUnitPrice(mOthers.get(position).getPrice());
                bean.setCount(1);
                addMenus(bean);
                buildMenuJson(menus);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Log.i("test", "position 1111 = " + position);
                pos = position;
                if (customDialog == null) {
                    customDialog = new CustomDialog(MainActivity.this);
                }
                customDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.tv_confirm:
                                Log.i("test", "position 2222 = " + pos);
                                GvBeans gvBeans = mOthers.get(pos);
                                GoodsCode.getInstance().deleteGoods(gvBeans.getCode());
                                othersAdapter.notifyDataSetChanged();
                                customDialog.dismiss();
                                Toast.makeText(MainActivity.this, "已删除", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.tv_cancel:
                                customDialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });

                if (!customDialog.isShowing()) {
                    customDialog.show();
                    TextView tv_content = customDialog.findViewById(R.id.tv_content);
                    tv_content.setText("确认要删除吗");
                }
            }
        });
        drinkAdapter.setOnItemClickListener(new GoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MenusBean bean = new MenusBean();
                bean.setId("" + (menus.size() + 1));
                bean.setMoney(mDrinksBean.get(position).getPrice());
                bean.setName(mDrinksBean.get(position).getName());
                bean.setCode(mDrinksBean.get(position).getCode());
                bean.setUnit(mDrinksBean.get(position).getUnit());
                bean.setUnitPrice(mDrinksBean.get(position).getPrice());
                bean.setCount(1);
                addMenus(bean);
                buildMenuJson(menus);
            }

        });
        snackAdapter.setOnItemClickListener(new GoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MenusBean bean = new MenusBean();
                bean.setId("" + (menus.size() + 1));
                bean.setMoney(mSnacksBean.get(position).getPrice());
                bean.setName(mSnacksBean.get(position).getName());
                bean.setCode(mSnacksBean.get(position).getCode());
                bean.setUnit(mSnacksBean.get(position).getUnit());
                bean.setUnitPrice(mSnacksBean.get(position).getPrice());
                bean.setCount(1);
                addMenus(bean);
                buildMenuJson(menus);
            }

        });

        btnClear.setOnClickListener(this);
        btnMore.setOnClickListener(this);
        main_ll_pay.setOnClickListener(this);
        btnPay.setOnClickListener(this);
        rlCar.setOnClickListener(this);
    }

    public void addMenus(MenusBean bean) {
        boolean isExist = false;
        for (MenusBean menu : menus) {
            if (ObjectUtils.equals(menu.getCode(), bean.getCode())) {
                isExist = true;
                menu.setCount(menu.getCount() + 1);
                menu.setMoney(ResourcesUtils.getString(R.string.units_money) + decimalFormat.format((Float.parseFloat(menu.getMoney().substring(1)) + Float.parseFloat(bean.getMoney().substring(1)))));
            }
        }
        if (!isExist) {
            menus.add(bean);
        }
    }

    private void initData() {
        screenManager.init(this);
        Display[] displays = screenManager.getDisplays();
        Log.e(TAG, "屏幕数量" + displays.length);
        for (int i = 0; i < displays.length; i++) {
            Log.e(TAG, "屏幕" + displays[i]);
        }
        mDrinksBean = GoodsCode.getInstance().getDrinks();
        mSnacksBean = GoodsCode.getInstance().getSnacks();
        mOthers = GoodsCode.getInstance().getOthers();

        drinkAdapter = new GoodsAdapter(mDrinksBean, 1);
        reDrink.setLayoutManager(new GridLayoutManager(this, 4));
        reDrink.setAdapter(drinkAdapter);

        snackAdapter = new GoodsAdapter(mSnacksBean, 3);
        re_snacks.setLayoutManager(new GridLayoutManager(this, 4));
        re_snacks.setAdapter(snackAdapter);

        othersAdapter = new GoodsAdapter(mOthers, 0);
        re_others.setLayoutManager(new GridLayoutManager(this, 4));
        re_others.setAdapter(othersAdapter);
        menus.clear();
        tvPrice.setText(ResourcesUtils.getString(this, R.string.units_money_units) + "0.00");
        menusAdapter = new SusceeAdapter(this, menus);
        lvMenus.setAdapter(menusAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_clear:
                totalCount = 0;
                if (isVertical) {
                    menus.clear();
                    tvCarMoeny.setText("");
                    tvCar.setText("");
                    tvCar.setVisibility(View.GONE);
                    ivCar.setImageResource(R.drawable.car_gray);
                    bottomSheetLayout.dismissSheet();
                    btnPay.setBackgroundColor(Color.parseColor("#999999"));
                } else {
                    llyShopcar.setVisibility(View.GONE);
                    rtlEmptyShopcar.setVisibility(View.VISIBLE);
                    menus.clear();
                    tvPrice.setText(ResourcesUtils.getString(this, R.string.units_money_units) + "0.00");
                    menusAdapter.update(menus);
                }
                break;
            case R.id.main_btn_more:
                Intent intent = new Intent(MainActivity.this, MoreActivity.class);
                startActivity(intent);
                break;
            case R.id.main_ll_pay:
                isRealDeal = (boolean) SharePreferenceUtil.getParam(MainActivity.this, PayModeSettingFragment.IS_REAL_DEAL, PayModeSettingFragment.default_isRealDeal);
                if (isRealDeal) {
                    PayMoney = tvPrice.getText().toString().substring(1);
                } else {
                    PayMoney = "0.01";
                }
                showPayPopWindow();
                break;
            case R.id.main_k1_btn_pay:
                isRealDeal = (boolean) SharePreferenceUtil.getParam(MainActivity.this, PayModeSettingFragment.IS_REAL_DEAL, PayModeSettingFragment.default_isRealDeal);
                if (isRealDeal) {
                    PayMoney = tvPrice.getText().toString().substring(1);
                } else {
                    PayMoney = "0.01";
                }
                showPayPopWindow();
                break;
            case R.id.main_btn_car:
                if (menus.size() > 0) {
                    if (!bottomSheetLayout.isSheetShowing()) {
                        bottomSheetLayout.showWithSheetView(createBottomSheetView());
                    } else {
                        bottomSheetLayout.dismissSheet();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void showPayPopWindow() {
        mPhotoPopupWindow = new PhotoPopupWindow(MainActivity.this, "共" + totalCount + "件商品", "￥" + PayMoney);
        mPhotoPopupWindow.setPopListener(new PhotoPopupWindow.PopLitener() {
            @Override
            public void onClosed() {
                mPhotoPopupWindow.dismiss();
            }

            @Override
            public void onPart1() {
                Request request = new Request();
                // 应用类型
                request.appType = "51";
                // 应用包名
                request.appId = getPackageName();
                // 交易类型
                request.transType = "00";
                // 交易金额
                request.amount = Float.valueOf(Float.valueOf(PayMoney) * 100).longValue();
                // Saas软件订单号
//                request.orderId = "123346546465";
                // 商品信息
//                request.orderInfo = "商品信息";
                // 支付码
//                request.payCode = "17682310719";
                Config config = new Config();
                // 交易过程中是否显示UI界面
                config.processDisplay = true;
//                // 是否展示交易结果页
                config.resultDisplay = false;
//                // 是否打印小票
                config.printTicket = false;
//                // 指定签购单上的退款订单号类型
//                config.printIdType = "指定签购单上的退款订单号类型";
//                // 备注
//                config.remarks = "备注";
                request.config = config;
                String jsonStr = gson.toJson(request);
                Log.i("test", jsonStr);
                PaymentService.getInstance().callPayment(jsonStr);

                mPhotoPopupWindow.dismiss();
                mPhotoPopupWindow.dismiss();
            }

            @Override
            public void onPart2() {
                Intent intent = new Intent(MainActivity.this, ReceiptCodeActivity.class);
                intent.putExtra("menus", (Serializable) menus);
                intent.putExtra("paymoney", PayMoney);
                intent.putExtra("count", totalCount);
                intent.putExtra("type", "1");
                startActivity(intent);
                mPhotoPopupWindow.dismiss();
            }

            @Override
            public void onPart3() {
                Intent intent = new Intent(MainActivity.this, ReceiptCodeActivity.class);
                intent.putExtra("menus", (Serializable) menus);
                intent.putExtra("paymoney", PayMoney);
                intent.putExtra("count", totalCount);
                intent.putExtra("type", "2");
                startActivity(intent);
                mPhotoPopupWindow.dismiss();
            }
        });
        mPhotoPopupWindow.showAtLocation(getWindow().getDecorView(),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    private View createBottomSheetView() {
        View bottomSheet = LayoutInflater.from(this).inflate(R.layout.sheet_layout, bottomSheetLayout, false);
        lvMenus = bottomSheet.findViewById(R.id.lv_menus);
        TextView tvPrice = bottomSheet.findViewById(R.id.main_tv_price);
        TextView btnClear = bottomSheet.findViewById(R.id.main_btn_clear);
        btnClear.setOnClickListener(this);
        menusAdapter = new SusceeAdapter(this, menus);
        lvMenus.setAdapter(menusAdapter);
        lvMenus.setSelection(menusAdapter.getCount() - 1);
        tvPrice.setText(tvCarMoeny.getText().toString());
        return bottomSheet;
    }

    private void buildMenuJson(List<MenusBean> menus) {
        totalCount++;
        float price = 0.00f;
        for (MenusBean bean1 : menus) {
            price = price + Float.parseFloat(bean1.getMoney().substring(1));
        }
        tvPrice.setText(ResourcesUtils.getString(MainActivity.this, R.string.units_money_units) + decimalFormat.format(price));
        menusAdapter.update(menus);
        // 购物车有东西
        if (isVertical) {
            tvCarMoeny.setText(ResourcesUtils.getString(R.string.units_money_units) + price);
            tvCar.setText(menus.size() + "");
            tvCar.setVisibility(View.VISIBLE);
            ivCar.setImageResource(R.drawable.car_white);
            btnPay.setBackgroundColor(Color.parseColor("#FC5436"));
            if (bottomSheetLayout.isSheetShowing()) {
                menusAdapter.notifyDataSetChanged();
                lvMenus.setSelection(menusAdapter.getCount() - 1);
                TextView tvPrice = bottomSheetLayout.findViewById(R.id.main_tv_price);
                tvPrice.setText(tvCarMoeny.getText().toString());
            }
        } else {
            llyShopcar.setVisibility(View.VISIBLE);
            rtlEmptyShopcar.setVisibility(View.GONE);
        }
    }

    //退出时的时间
    private long mExitTime;

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
