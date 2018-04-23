package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.view.DiDiTitleView;

import net.tsz.afinal.http.AjaxParams;

/**
 * @author Created by huiyuan on 2017/9/13.
 */

public class SupplierEvaluateActivity extends Activity implements View.OnClickListener{

    private DiDiTitleView title_view;
    private ImageView star1,star2,star3,star4,star5;
    private TextView input_char_count,evaluation_text;
    private EditText input_content;
    private Button submit;
    private RelativeLayout content_edit_container;

    private final String evaluateUrl = GloableParams.HOST + "carrier/qrcodepay/orderEvaluation/save.do?";
//    private final String evaluateUrl = "http://192.168.158.33:8080/gwcz/carrier/qrcodepay/orderEvaluation/save.do?";
    private int starCount = 0;
    private String payeeUid = "";
    private String orderId = "";
    private byte stars = 0;
    private String content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payeeUid = getIntent().getStringExtra("payeeUid");
        orderId = getIntent().getStringExtra("orderId");
        stars = getIntent().getByteExtra("stars",stars);
        content = getIntent().getStringExtra("content");

        setContentView(R.layout.supplier_evaluate_activity);
        initViews();
    }

    private void initViews(){
        title_view = (DiDiTitleView) findViewById(R.id.title_view);
        title_view.setBack(this);
        title_view.setTitle("评价");
        star1 = (ImageView) findViewById(R.id.star1);
        star2 = (ImageView) findViewById(R.id.star2);
        star3 = (ImageView) findViewById(R.id.star3);
        star4 = (ImageView) findViewById(R.id.star4);
        star5 = (ImageView) findViewById(R.id.star5);
        input_char_count = (TextView) findViewById(R.id.input_char_count);
        evaluation_text = (TextView) findViewById(R.id.evaluation_text);
        input_content = (EditText) findViewById(R.id.input_content);
        submit = (Button) findViewById(R.id.submit);
        content_edit_container = (RelativeLayout) findViewById(R.id.content_edit_container);

        input_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputString = s.toString();

                if (inputString.length() > 140) {
                    Toast.makeText(SupplierEvaluateActivity.this,"最多输入140个字符!",Toast.LENGTH_LONG).show();
                    return;
                }
                input_char_count.setText("" + inputString.length());
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(starCount == 0){
                    Toast.makeText(SupplierEvaluateActivity.this,"请选择星星数量!",Toast.LENGTH_LONG).show();
                    return;
                }
                submitEvaluateContent();
            }
        });

        if(stars > 0){
            switch (stars){
                case 1:
                    star1.setBackgroundResource(R.drawable.star_pressed_supplier);
                    break;
                case 2:
                    star1.setBackgroundResource(R.drawable.star_pressed_supplier);
                    star2.setBackgroundResource(R.drawable.star_pressed_supplier);
                    break;
                case 3:
                    star1.setBackgroundResource(R.drawable.star_pressed_supplier);
                    star2.setBackgroundResource(R.drawable.star_pressed_supplier);
                    star3.setBackgroundResource(R.drawable.star_pressed_supplier);
                    break;
                case 4:
                    star1.setBackgroundResource(R.drawable.star_pressed_supplier);
                    star2.setBackgroundResource(R.drawable.star_pressed_supplier);
                    star3.setBackgroundResource(R.drawable.star_pressed_supplier);
                    star4.setBackgroundResource(R.drawable.star_pressed_supplier);
                    break;
                case 5:
                    star1.setBackgroundResource(R.drawable.star_pressed_supplier);
                    star2.setBackgroundResource(R.drawable.star_pressed_supplier);
                    star3.setBackgroundResource(R.drawable.star_pressed_supplier);
                    star4.setBackgroundResource(R.drawable.star_pressed_supplier);
                    star5.setBackgroundResource(R.drawable.star_pressed_supplier);
                    break;
                default:
                    break;
            }

            if(content != null && !"".equals(content)){
                evaluation_text.setText(content);
                evaluation_text.setVisibility(View.VISIBLE);
            }else {
                evaluation_text.setVisibility(View.GONE);
            }
            submit.setVisibility(View.GONE);
            content_edit_container.setVisibility(View.GONE);
        }else {
            star1.setOnClickListener(this);
            star2.setOnClickListener(this);
            star3.setOnClickListener(this);
            star4.setOnClickListener(this);
            star5.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.star1:
//                if(starCount == 1){
//                    starCount = 0;
//                    star1.setBackgroundResource(R.drawable.stars_empty);
//                    star2.setBackgroundResource(R.drawable.stars_empty);
//                    star3.setBackgroundResource(R.drawable.stars_empty);
//                    star4.setBackgroundResource(R.drawable.stars_empty);
//                    star5.setBackgroundResource(R.drawable.stars_empty);
//                }else {
                    starCount = 1;
                    star1.setBackgroundResource(R.drawable.star_pressed_supplier);
                    star2.setBackgroundResource(R.drawable.stars_empty);
                    star3.setBackgroundResource(R.drawable.stars_empty);
                    star4.setBackgroundResource(R.drawable.stars_empty);
                    star5.setBackgroundResource(R.drawable.stars_empty);
//                }
                break;
            case R.id.star2:
                starCount = 2;
                star1.setBackgroundResource(R.drawable.star_pressed_supplier);
                star2.setBackgroundResource(R.drawable.star_pressed_supplier);
                star3.setBackgroundResource(R.drawable.stars_empty);
                star4.setBackgroundResource(R.drawable.stars_empty);
                star5.setBackgroundResource(R.drawable.stars_empty);
                break;
            case R.id.star3:
                starCount = 3;
                star1.setBackgroundResource(R.drawable.star_pressed_supplier);
                star2.setBackgroundResource(R.drawable.star_pressed_supplier);
                star3.setBackgroundResource(R.drawable.star_pressed_supplier);
                star4.setBackgroundResource(R.drawable.stars_empty);
                star5.setBackgroundResource(R.drawable.stars_empty);
                break;
            case R.id.star4:
                starCount = 4;
                star1.setBackgroundResource(R.drawable.star_pressed_supplier);
                star2.setBackgroundResource(R.drawable.star_pressed_supplier);
                star3.setBackgroundResource(R.drawable.star_pressed_supplier);
                star4.setBackgroundResource(R.drawable.star_pressed_supplier);
                star5.setBackgroundResource(R.drawable.stars_empty);
                break;
            case R.id.star5:
                starCount = 5;
                star1.setBackgroundResource(R.drawable.star_pressed_supplier);
                star2.setBackgroundResource(R.drawable.star_pressed_supplier);
                star3.setBackgroundResource(R.drawable.star_pressed_supplier);
                star4.setBackgroundResource(R.drawable.star_pressed_supplier);
                star5.setBackgroundResource(R.drawable.star_pressed_supplier);
                break;
            default:
                break;
        }
    }

    private void submitEvaluateContent(){
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("payeeUid",payeeUid);
        ajaxParams.put("orderId",orderId);
        ajaxParams.put("star",starCount + "");
        ajaxParams.put("content",input_content.getText().toString());

        DidiApp.getHttpManager().sessionPost(SupplierEvaluateActivity.this, evaluateUrl, ajaxParams,
                new ChildAfinalHttpCallBack() {
                    @Override
                    public void data(String t) {
                        Toast.makeText(SupplierEvaluateActivity.this,"评价成功!",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.putExtra("stars","" + starCount);
                        setResult(8001,intent);

                        submit.setVisibility(View.GONE);
                        content_edit_container.setVisibility(View.GONE);

                        if(!"".equals(input_content.getText().toString())){
                            evaluation_text.setText(input_content.getText().toString());
                            evaluation_text.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(String errCode, String errMsg, Boolean errSerious) {

                    }
                });
    }
}
