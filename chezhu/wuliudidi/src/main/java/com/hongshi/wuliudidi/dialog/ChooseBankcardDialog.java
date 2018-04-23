package com.hongshi.wuliudidi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.BindNewBankcardActivity;
import com.hongshi.wuliudidi.adapter.BankcardListAdapter;
import com.hongshi.wuliudidi.model.BankcardModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abc on 2016/4/11.
 */
public class ChooseBankcardDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private ListView bankcardListView;
    private List<BankcardModel> list = new ArrayList<>();
    private ImageView leftArrowImage;
    private RelativeLayout useNewCardLayout;
    private BankcardListAdapter bankcardListAdapter;
    private ChooseBankcardCallBack callBack;

    public ChooseBankcardDialog(Context context, int theme, List<BankcardModel> list, ChooseBankcardCallBack callBack) {
        super(context, theme);
        this.mContext = context;
        this.list = list;
        this.callBack = callBack;
        init();
    }

    private void init(){
        setContentView(R.layout.choose_bankcard_dialog);

        leftArrowImage = (ImageView) findViewById(R.id.left_arrow_image);
        bankcardListView = (ListView) findViewById(R.id.choose_card_listview);
        useNewCardLayout = (RelativeLayout) findViewById(R.id.use_new_card);

        leftArrowImage.setOnClickListener(this);
        useNewCardLayout.setOnClickListener(this);

        bankcardListAdapter = new BankcardListAdapter(mContext, list);
        bankcardListView.setAdapter(bankcardListAdapter);
        bankcardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                callBack.getResult(list.get(i));
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.left_arrow_image:
                dismiss();
                break;
            case R.id.use_new_card:
                Intent intent = new Intent(mContext, BindNewBankcardActivity.class);
                mContext.startActivity(intent);
                dismiss();
                break;
            default:
                break;
        }
    }


    public static interface ChooseBankcardCallBack {
        public void getResult(BankcardModel bankcard);
    }
}
