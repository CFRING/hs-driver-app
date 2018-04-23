package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.WebViewWithTitleActivity;
import com.hongshi.wuliudidi.model.BannerModel;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huiyuan
 * @version 1.0
 * @created 2017/11/29 19:36
 * @title BannerAdapter
 * @description 广告适配器
 * @changeRecord：2017/11/29 19:36 modify by
 */
public class BannerAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    List<BannerModel> bannerModels = new ArrayList<>();
    private int size;
	private FinalBitmap finalBitmap;
    private Bitmap bitmap;
    private String title = "";
    private String adUrl = "";
    private String bannerSource = "";

    public BannerAdapter(Context context, List<BannerModel> bannerModelList,String bannerSource) {
        mContext = context;
        this.bannerModels = bannerModelList;
        this.bannerSource = bannerSource;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        finalBitmap = FinalBitmap.create(context);
		size = bannerModels.size();
        if("home_page".equals(bannerSource)){
            bitmap  = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.home_three);
        }else {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.advert_place_holder_150);
        }
    }
    @Override
    public int getCount() {
        // 返回最大值来实现循环
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.image_item, null);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.imgView);

        if(size > 0){
            finalBitmap.display(image,bannerModels.get(position % bannerModels.size()).getAdPic(),bitmap);
            title = bannerModels.get(position % bannerModels.size()).getTitle();
            adUrl = bannerModels.get(position % bannerModels.size()).getAdUrl();
        }else {
                image.setImageBitmap(bitmap);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,WebViewWithTitleActivity.class);
                Bundle bundle = new Bundle();
                if (title != null && !"".equals(title)){
                    bundle.putString("title", title);
                }else {
                    bundle.putString("title", "活动说明");
                }
                if(adUrl != null && !"".equals(adUrl)){
                    bundle.putString("url", adUrl);
                }else {
                    return;
                }
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
}
