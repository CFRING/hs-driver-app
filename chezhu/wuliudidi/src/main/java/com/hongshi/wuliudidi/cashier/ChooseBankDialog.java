package com.hongshi.wuliudidi.cashier;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.hongshi.wuliudidi.R;

import java.util.List;


/**
 * Created on 2016/4/26.
 */
public class ChooseBankDialog extends Dialog {

    private final String TAG = "ChooseBankDialog";

    /**
     * 选择对话框回调接口
     */
    public interface ChooseDialogCallBack {
        /**
         *
         * @param bankCard 选中的银行卡所有信息
         */
        public void chooseBank(TiXianModel.BankCard bankCard);
    }

    private Context mContext;
    private List<TiXianModel.BankCard> mList;
    private ChooseDialogCallBack mChooseDialogCallBack;

    private RelativeLayout mBackImg;
    private TextView mTitle;
    private ListView mListView;

    public ChooseBankDialog(Context context, int themeResId, List<TiXianModel.BankCard> list, ChooseDialogCallBack chooseDialogCallBack) {
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
        mListView.setAdapter(new com.hongshi.wuliudidi.cashier.ChooseBankAdapter(mContext, mList));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TiXianModel.BankCard bankCard = mList.get(position);
                mChooseDialogCallBack.chooseBank(bankCard);
                cancel();
            }
        });
    }

    public void setTitle(String title){
        mTitle.setText(title);
    }
}


