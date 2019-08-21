package com.sunmi.weipan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.sunmi.weipan.R;
import com.sunmi.weipan.bean.MenusBean;
import com.sunmi.weipan.ui.MainActivity;

import java.util.List;

/**
 * 作者：create by comersss on 2019/3/18 16:44
 * 邮箱：904359289@qq.com
 */
public class SusceeAdapter extends BaseAdapter {

    private Context mContext;
    private List<MenusBean> mMenus;

    public SusceeAdapter(Context context, List<MenusBean> menus) {
        this.mContext = context;
        this.mMenus = menus;
    }

    @Override
    public int getCount() {
        return mMenus == null ? 0 : mMenus.size();
    }

    @Override
    public Object getItem(int position) {
        return mMenus == null ? null : mMenus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold hold = null;
        if (convertView == null) {
            hold = new ViewHold();
            if (mContext instanceof MainActivity) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.menus_presentation_items_layout, null);
            } else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.menus_succes_items_layout, null);
            }
            hold.tvName = convertView.findViewById(R.id.tvName);
            hold.tvMoney = convertView.findViewById(R.id.tvMoney);
            hold.tvcount = convertView.findViewById(R.id.tv_count);
            hold.tvunit = convertView.findViewById(R.id.tv_union);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }
        hold.tvName.setText(mMenus.get(position).getName());
        String unit = mMenus.get(position).getUnit();
        unit = ObjectUtils.isEmpty(unit) ? "-" : unit;
        hold.tvunit.setText(mMenus.get(position).getUnitPrice() + "/" + unit);
        hold.tvcount.setText("*" + mMenus.get(position).getCount());
        hold.tvMoney.setText(mMenus.get(position).getMoney());
        if (mContext instanceof MainActivity) {
            hold.tvunit.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHold {
        private TextView tvName;
        private TextView tvMoney;
        private TextView tvcount;
        private TextView tvunit;
    }

    public void update(List<MenusBean> menus) {
        this.mMenus = menus;
        notifyDataSetChanged();
    }
}
