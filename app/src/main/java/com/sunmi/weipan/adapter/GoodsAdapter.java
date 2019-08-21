package com.sunmi.weipan.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunmi.weipan.R;
import com.sunmi.weipan.bean.GvBeans;

import java.io.File;
import java.util.List;


public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHold> {
    private List<GvBeans> list;

    private GoodsAdapter.OnItemClickListener listener;
    private int mFlag;

    private int selectPosition = -1;

    public GoodsAdapter(List<GvBeans> gvBeans, int flag) {
        this.list = gvBeans;
        this.mFlag = flag;
    }

    public int getItemCount() {
        return this.list.size();
    }

    public void onBindViewHolder(final ViewHold hold, final int position) {
        if (TextUtils.isEmpty(list.get(position).getImgUrl())) {
            GvBeans gvBeans = list.get(position);
            hold.ivPhoto.setImageResource(gvBeans.getImgId());
        } else {
            hold.ivPhoto.setImageBitmap(getDiskBitmap(list.get(position).getImgUrl()));
        }
        hold.tvName.setText(list.get(position).getName());
        hold.tvPrice.setText(list.get(position).getPrice());
        hold.tvUnit.setText("/" + list.get(position).getUnit());
        if (mFlag == 2) {
            if (selectPosition == position) {
                hold.ivBorder.setSelected(true);
            } else {
                hold.ivBorder.setSelected(false);
            }
        }

        if (this.listener != null) {
            hold.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View var1) {
                    GoodsAdapter.this.listener.onItemClick(var1, position);
                }
            });
            hold.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    GoodsAdapter.this.listener.onItemLongClick(view, position);
                    return true;
                }
            });
        }

    }

    public ViewHold onCreateViewHolder(ViewGroup var1, int var2) {
        return new ViewHold(LayoutInflater.from(var1.getContext()).inflate(R.layout.gv_menus_layout, var1, false)) {
        };
    }

    public void setOnItemClickListener(OnItemClickListener var1) {
        this.listener = var1;
    }

    /**
     * 加载本地图片
     * http://bbs.3gstdy.com
     *
     * @param pathString
     * @return
     */
    private Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }


        return bitmap;
    }

    public static class OnItemClickListener {
        public void onItemClick(View view, int position) {
        }

        public void onItemLongClick(View view, int position) {

        }
    }


    public static class ViewHold extends RecyclerView.ViewHolder {
        public ImageView ivPhoto;
        public TextView tvName;
        public TextView tvPrice;
        public TextView tvUnit;
        public ImageView ivCar;
        public ImageView ivBorder;

        public ViewHold(View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvUnit = itemView.findViewById(R.id.tv_unit);
            ivCar = itemView.findViewById(R.id.iv_car);
            ivBorder = itemView.findViewById(R.id.iv_border);
        }
    }

}

