package com.hongshi.wuliudidi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.ChooseOilCardAdapter;
import com.hongshi.wuliudidi.model.OilCardModel;

import java.util.List;


/**
 * Created by huiyuan on 2017/8/16.
 */
public class ChooseOilCardDialog extends Dialog {

    /**
     * 选择对话框回调接口
     */
    public interface ChooseDialogCallBack {
        /**
         *
         * @param model 选中的油卡所有信息
         */
        public void oilCardChosen(OilCardModel model);
    }

    private Context mContext;
    private List<OilCardModel> mList;
    private ChooseDialogCallBack mChooseDialogCallBack;

    private RelativeLayout mBackImg;
    private TextView mTitle;
    private ListView mListView;

    public ChooseOilCardDialog(Context context, int themeResId, List<OilCardModel> list, ChooseDialogCallBack chooseDialogCallBack) {
        super(context, themeResId);
        mContext = context;
        mList = list;
        this.mChooseDialogCallBack = chooseDialogCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sdk_dialog_choose_card);

        mBackImg = (RelativeLayout) findViewById(R.id.jiantou_left);
        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        mTitle = (TextView) findViewById(R.id.choose_title);
        mListView = (ListView) findViewById(R.id.choose_list_view);
        mListView.setAdapter(new ChooseOilCardAdapter(mContext, mList));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OilCardModel oilCardModel = mList.get(position);
                if(mChooseDialogCallBack != null){
                    mChooseDialogCallBack.oilCardChosen(oilCardModel);
                }
                cancel();
            }
        });
    }

    public void setTitle(String title){
        mTitle.setText(title);
    }
}


