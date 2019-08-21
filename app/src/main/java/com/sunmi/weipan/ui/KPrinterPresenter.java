package com.sunmi.weipan.ui;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.sunmi.extprinterservice.ExtPrinterService;
import com.sunmi.weipan.R;
import com.sunmi.weipan.bean.MenusBean;
import com.sunmi.weipan.fragment.PayModeSettingFragment;
import com.sunmi.weipan.utils.ResourcesUtils;
import com.sunmi.weipan.utils.SharePreferenceUtil;
import com.sunmi.weipan.utils.Utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by zhicheng.liu on 2018/4/4
 * address :liuzhicheng@sunmi.com
 * description :
 */

public class KPrinterPresenter {
    private Context context;
    private static final String TAG = "KPrinterPresenter";
    private ExtPrinterService mPrinter;
    String unic = "GBK";
    private String PayMoney;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public KPrinterPresenter(Context context, ExtPrinterService printerService) {
        this.context = context;
        this.mPrinter = printerService;
    }

    public void print(ArrayList<MenusBean> menus, String payType) {
        int fontsizeTitle = 1;
        int fontsizeContent = 0;
        int fontsizeFoot = 1;
        String divide = "************************************************" + "";
        String divide2 = "-----------------------------------------------" + "";
        try {
            if (mPrinter.getPrinterStatus() != 0) {
                return;
            }
            mPrinter.lineWrap(1);
            int width = divide2.length() * 5 / 12;
            String goods = formatTitle(width);
            mPrinter.setAlignMode(1);
            mPrinter.setFontZoom(fontsizeTitle, fontsizeTitle);
            mPrinter.sendRawData(boldOn());
            mPrinter.printText("杭州微盘每日付收款明细");
            mPrinter.flush();
            mPrinter.setAlignMode(0);
            mPrinter.setFontZoom(fontsizeContent, fontsizeContent);
            mPrinter.sendRawData(boldOff());
            mPrinter.printText(divide);
            mPrinter.printText("订单编号：" + SystemClock.uptimeMillis() + "");
            mPrinter.flush();
            mPrinter.printText("下单时间：" + formatData(new Date()) + "");
            mPrinter.flush();
            mPrinter.printText("支付方式：" + payType);
            mPrinter.flush();
            mPrinter.printText(divide);
            mPrinter.flush();
            mPrinter.printText(goods + "");
            mPrinter.flush();
            mPrinter.printText(divide2);
            mPrinter.flush();
            printGoods(menus, fontsizeContent, divide2, width);
            mPrinter.printText(divide);
            mPrinter.flush();
//            mPrinter.printQrCode("https://sunmi.com/", 8, 0);
            mPrinter.lineWrap(1);
            mPrinter.setFontZoom(fontsizeContent, fontsizeContent);
            mPrinter.printText("感谢使用微盘智慧收银！");
            mPrinter.flush();
            mPrinter.lineWrap(4);
            mPrinter.cutPaper(0, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatTitle(int width) {
        Log.e("@@@@@", width + "=======");

        String[] title = {
                "商品名称",
                "数量",
                "单位",
                "金额（元）",
        };
        StringBuffer sb = new StringBuffer();
        int blank1 = width - String_length(title[0]);
        int blank2 = width - String_length(title[1]);
        int blank3 = width - String_length(title[2] + title[1]);

        sb.append(title[0]);
        sb.append(addblank(blank1));

        sb.append(title[1]);
        sb.append(addblank(1));

        sb.append(title[2]);
        sb.append(addblank(blank3 - 1));

        sb.append(title[3]);

//        int w1 = width / 3;
//        int w2 = width / 3 + 2;
//        String str = String.format("%-" + w1 + "s%-" + w2 + "s%s", title[0], title[1], title[2]);
        return sb.toString();
    }

    private void printNewline(String str, int width) throws Exception {
        List<String> strings = Utils.getStrList(str, width);
        for (String string : strings) {
            mPrinter.printText(string);
            mPrinter.flush();
        }
    }

    private void printGoods(ArrayList<MenusBean> menus, int fontsizeContent, String divide2, int width) throws Exception {
        int blank1;
        int blank2;
        float price = 0.00f;
        for (MenusBean menu : menus) {
            price = price + Float.parseFloat(menu.getMoney().substring(1));
        }

        boolean isRealDeal = (boolean) SharePreferenceUtil.getParam(context, PayModeSettingFragment.IS_REAL_DEAL, PayModeSettingFragment.default_isRealDeal);
        if (isRealDeal) {
            PayMoney = "" + decimalFormat.format(price);
        } else {
            PayMoney = "0.01";
        }
        int maxNameWidth = isZh() ? (width - 2) / 2 : (width - 2);
        StringBuffer sb = new StringBuffer();
        for (MenusBean listBean : menus) {
            sb.setLength(0);
            String name = listBean.getName();
            String name1 = name.length() > maxNameWidth ? name.substring(0, maxNameWidth) : "";

            blank1 = width - String_length(name.length() > maxNameWidth ? name1 : name) + 1;
            blank2 = width - String_length(listBean.getCount() + listBean.getUnit());

            sb.append(name.length() > maxNameWidth ? name1 : name);
            sb.append(addblank(blank1));

            sb.append(listBean.getCount());
            sb.append(addblank(4));

            sb.append(listBean.getUnit());
            sb.append(addblank(blank2 - 4));

            sb.append(listBean.getMoney().replace(ResourcesUtils.getString(context, R.string.units_money), ""));
            mPrinter.printText(sb.toString() + "");
            mPrinter.flush();
            if (name.length() > maxNameWidth) {
                printNewline(name.substring(maxNameWidth), maxNameWidth);
            }

        }
        mPrinter.printText(divide2);
        mPrinter.flush();

        String total = "累计金额：";
        String real = "实际收款：";

        sb.setLength(0);
        blank1 = width * 2 - String_length(total);
        blank2 = width * 2 - String_length(real);
        sb.append(total);
        sb.append(addblank(blank1));
        sb.append(decimalFormat.format(price));
        mPrinter.printText(sb.toString() + "");
        mPrinter.flush();

        sb.setLength(0);
        sb.append(real);
        sb.append(addblank(blank2));
        sb.append(PayMoney);
        mPrinter.printText(sb.toString() + "");
        mPrinter.flush();
        sb.setLength(0);
    }

    private String formatData(Date nowTime) {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return time.format(nowTime);
    }

    private String addblank(int count) {
        String st = "";
        if (count < 0) {
            count = 0;
        }
        for (int i = 0; i < count; i++) {
            st = st + " ";
        }
        return st;
    }

    private static final byte ESC = 0x1B;// Escape

    /**
     * 字体加粗
     */
    private byte[] boldOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0xF;
        return result;
    }

    /**
     * 取消字体加粗
     */
    private byte[] boldOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0;
        return result;
    }

    private boolean isZh() {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

    private byte[] mCmd = new byte[24];

    public synchronized int setCharSize(int hsize, int vsize) {
        int Width = 0;
        if (hsize == 0) {
            Width = 0;
        }
        if (hsize == 1) {
            Width = 16;
        }
        if (hsize == 2) {
            Width = 32;
        }
        if (hsize == 3) {
            Width = 48;
        }
        if (hsize == 4) {
            Width = 64;
        }
        if (hsize == 5) {
            Width = 80;
        }
        if (hsize == 6) {
            Width = 96;
        }

        if (hsize == 7) {
            Width = 112;
        }

        if (Width <= 0) {
            Width = 0;
        }

        if (Width >= 112) {
            Width = 112;
        }

        if (vsize <= 0) {
            vsize = 0;
        }

        if (vsize >= 7) {
            vsize = 7;
        }

        int Mul = Width + vsize;
        this.mCmd[0] = 29;
        this.mCmd[1] = 33;
        this.mCmd[2] = (byte) Mul;

        return /*this.mPrinter.writeIO(this.mCmd, 0, 3, 2000)*/1;
    }


    private int String_length(String rawString) {
        return rawString.replaceAll("[\\u4e00-\\u9fa5]", "SH").length();
    }
}
