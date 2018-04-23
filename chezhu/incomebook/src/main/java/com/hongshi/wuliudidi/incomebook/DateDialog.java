package com.hongshi.wuliudidi.incomebook;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateDialog extends Dialog implements OnWheelChangedListener{

    private final String TAG = "DateDialog";
    private Context mContext;
    private SetDateCallBack mDateCallBack;
    private WheelView mYearView,mMonthView,mDayView,mHourView,mMinuteView;
    private TextView mDate;
    private final String[] years = {"2015","2016","2017","2018","2019","2020","2021","2022","2023","2024","2025","2026","2027","2028","2029","2030","2031","2032","2033","2034","2035","2036","2037","2038"
            ,"2039","2040","2041","2042","2043","2044","2045","2046","2047","2048","2049","2050","2051","2052","2053","2054","2055","2056","2057","2058","2059","2060","2061","2062","2063","2064"
            ,"2065","2066","2067","2068","2069","2070"};
    private final String[] months = {"01","02","03","04","05","06","07","08","09","10","11","12"};
    private final String[] days28 = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28"};
    private final String[] days29 = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29"};
    private final String[] days30 = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"};
    private final String[] days31 = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
    private String[] minute ;
    private String[] hours;
    private String yearStr = "";
    private String monthStr = "";
    private String dayStr = "";
    private String hourStr = "";
    private String minuteStr = "";
    private String dateType = "";
    private String name = "";
    private TextView mCancel,mSure,mDialogName;
    public static final String Entire = "Year_Month_Day_Hour_Minute";
    public static final String YearMonthDay = "Year_Month_Day";
    public static final String YearMonth = "Year_Month";
    public static final String Year = "Year";
    public DateDialog(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    protected DateDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
        init();
    }

    public DateDialog(Context context, int theme, SetDateCallBack mDateCallBack, String dateType, String name) {
        super(context, theme);
        this.mContext = context;
        this.mDateCallBack = mDateCallBack;
        this.dateType = dateType;
        this.name = name;
        init();
    }
    private void init() {
        setContentView(R.layout.date_view_dialog);
        hours = mContext.getResources().getStringArray(R.array.hours);
        minute = mContext.getResources().getStringArray(R.array.minute);

        mYearView = (WheelView) findViewById(R.id.year);
        mMonthView = (WheelView) findViewById(R.id.month);
        mDayView = (WheelView) findViewById(R.id.day);
        mHourView = (WheelView) findViewById(R.id.hour);
        mMinuteView = (WheelView) findViewById(R.id.minute);
        mDate = (TextView) findViewById(R.id.date);
        mDialogName = (TextView) findViewById(R.id.dialog_name);
        mCancel = (TextView) findViewById(R.id.cancel);

        if(dateType.equals(YearMonthDay)){
            mYearView.setDrawShadows(false);
            mMonthView.setDrawShadows(false);
            mDayView.setDrawShadows(false);
            mHourView.setVisibility(View.GONE);
            mMinuteView.setVisibility(View.GONE);
        }else if(dateType.equals(YearMonth)){
            mYearView.setDrawShadows(false);
            mMonthView.setDrawShadows(false);;
            mDayView.setVisibility(View.GONE);
            mHourView.setVisibility(View.GONE);
            mMinuteView.setVisibility(View.GONE);
        }else if(dateType.equals(Year)){
            mYearView.setDrawShadows(false);
            mMonthView.setVisibility(View.GONE);
            mDayView.setVisibility(View.GONE);
            mHourView.setVisibility(View.GONE);
            mMinuteView.setVisibility(View.GONE);
        }
        mDialogName.setText(name);
        mSure = (TextView) findViewById(R.id.sure);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mDate.getText().toString();
                SimpleDateFormat sdf;
                if(dateType.equals(Entire)){
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    hourStr = hours[mHourView.getCurrentItem()];//TODO
                    try {
                        long millionSeconds = sdf.parse(str).getTime();
                        mDateCallBack.date(millionSeconds);
                        dismiss();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else if (dateType.equals(YearMonthDay)) {
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                    hourStr = hours[mHourView.getCurrentItem()];
                    try {
                        long millionSeconds = sdf.parse(str).getTime();
                        mDateCallBack.date(millionSeconds);
                        dismiss();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else if(dateType.equals(YearMonth)){
                    sdf = new SimpleDateFormat("yyyy-MM");
                    try {
                        long millionSeconds = sdf.parse(str).getTime();
                        mDateCallBack.date(millionSeconds);
                        dismiss();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else if (dateType.equals(Year)){
                    sdf = new SimpleDateFormat("yyyy");
                    try {
                        long millionSeconds = sdf.parse(str).getTime();
                        mDateCallBack.date(millionSeconds);
                        dismiss();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Calendar c = Calendar.getInstance();
        yearStr = String.valueOf(c.get(Calendar.YEAR));
        monthStr = String.valueOf(c.get(Calendar.MONTH)+1);
        dayStr = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        hourStr = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        minuteStr = String.valueOf(c.get(Calendar.MINUTE));
        if(monthStr.length()==1){
            monthStr = "0" + monthStr;
        }
        if(dayStr.length()==1){
            dayStr = "0" + dayStr;
        }
        for(int i = 0;i <= 60; i += 5){
            if (Integer.valueOf(minuteStr) > i && Integer.valueOf(minuteStr) <i+5){
                minuteStr = String.valueOf(i + 5);
                if (minuteStr.equals("60")){
                    minuteStr = "00";
                    hourStr = String.valueOf(Integer.valueOf(hourStr) + 1);
                }
                break;
            }
        }
        if(hourStr.length()==1){
            hourStr = "0" +hourStr;
        }
        if(minuteStr.length()==1){
            minuteStr = "0" +minuteStr;
        }
        setUpListener();
        this.setCanceledOnTouchOutside(true);
    }

    private void setUpListener() {
        // ���change�¼�
        mYearView.addChangingListener(this);
        mMonthView.addChangingListener(this);
        mDayView.addChangingListener(this);
        mHourView.addChangingListener(this);
        mMinuteView.addChangingListener(this);
        mYearView.setVisibleItems(3);
        mMonthView.setVisibleItems(3);
        mDayView.setVisibleItems(3);
        mHourView.setVisibleItems(3);
        mMinuteView.setVisibleItems(3);

        mYearView.setCyclic(true);
        mMonthView.setCyclic(true);
        mDayView.setCyclic(true);
//		mHourView.setCyclic(true);
//		mMinuteView.setCyclic(true);
        updateYears(true);
        updateMonth(true);
        updateDays(true);
        updateHour(true);
        updateMinute(true);

        if(dateType.equals(Entire)){
            mDate.setText(yearStr + "-" + monthStr + "-" + dayStr + " " + hourStr + ":" + minuteStr);
        }else if(dateType.equals(YearMonthDay)){
            mDate.setText(yearStr+"-"+monthStr+"-"+dayStr);
        }else if(dateType.equals(YearMonth)){
            mDate.setText(yearStr+"-"+monthStr);
        }else if(dateType.equals(Year)){
            mDate.setText(yearStr);
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mYearView) {
            yearStr = years[mYearView.getCurrentItem()];
            updateYears(false);
        } else if (wheel == mMonthView) {
            monthStr = months[mMonthView.getCurrentItem()];
            updateMonth(false);
        } else if (wheel == mDayView) {
            updateDays(false);
            dayStr = days31[mDayView.getCurrentItem()];
        }else if (wheel == mHourView) {
            updateHour(false);
            hourStr = hours[mHourView.getCurrentItem()];
        }else if (wheel == mMinuteView) {
            updateMinute(false);
            minuteStr = minute[mMinuteView.getCurrentItem()];
        }

        if(dateType.equals(Entire)){
            mDate.setText(yearStr + "-" + monthStr + "-" + dayStr + " " + hourStr + ":" + minuteStr);
        }else if(dateType.equals(YearMonthDay)){
            mDate.setText(yearStr+"-"+monthStr+"-"+dayStr);
        }else if(dateType.equals(YearMonth)){
            mDate.setText(yearStr+"-"+monthStr);
        }else if(dateType.equals(Year)){
            mDate.setText(yearStr);
        }
    }
    private void updateYears(boolean isFirst) {
        DateArrayAdapter mDateArrayAdapter = new DateArrayAdapter(mContext, years, mYearView.getCurrentItem());
        mYearView.setViewAdapter(mDateArrayAdapter);
        if(isFirst){
            for(int i= 0;i<years.length;i++){
                if(yearStr.equals(years[i])){
                    mYearView.setCurrentItem(i);
                }
            }
        }else {
            updateDays(false);
        }
    }
    private void updateMonth(boolean isFirst) {
        mMonthView.setViewAdapter(new DateArrayAdapter(mContext,months,mMonthView.getCurrentItem()));
        if(isFirst){
            for(int i= 0;i<months.length;i++){
                if(monthStr.equals(months[i])){
                    mMonthView.setCurrentItem(i);
                }
            }
        }else {
            updateDays(false);
        }
    }
    private void updateDays(boolean isFirst) {
        String[] days;
        int currentItem = mDayView.getCurrentItem();
        if (!isRunYear() && isFebruary()){
            days = days28;
            if (currentItem > 27){
                currentItem = 27;
                mDayView.setCurrentItem(currentItem);
            }
        }else if (isRunYear() && isFebruary()){
            days = days29;
            if (currentItem > 28){
                currentItem = 28;
                mDayView.setCurrentItem(currentItem);
            }
        }else if (isHasThirtyDays()){
            days = days30;
            if (currentItem > 29){
                currentItem = 29;
                mDayView.setCurrentItem(currentItem);
            }
        }else {
            days = days31;
        }
        mDayView.setViewAdapter(new DateArrayAdapter(mContext, days,currentItem));
        if(isFirst){
            for(int i= 0;i<days.length;i++){
                if(dayStr.equals(days[i])){
                    mDayView.setCurrentItem(i);
                }
            }
        }
    }
    private void updateHour(boolean isFirst) {
        mHourView.setViewAdapter(new DateArrayAdapter(mContext, hours,mHourView.getCurrentItem()));
        if(isFirst){
            for(int i= 0;i<hours.length;i++){
                if(hourStr.equals(hours[i])){
                    mHourView.setCurrentItem(i);
                }
            }
        }
    }
    private void updateMinute(boolean isFirst) {
        mMinuteView.setViewAdapter(new DateArrayAdapter(mContext,minute,mMinuteView.getCurrentItem()));
        if(isFirst){
            for(int i= 0;i<minute.length;i++){
                if(minuteStr.equals(minute[i])){
                    mMinuteView.setCurrentItem(i);
                }
            }
        }
    }
    //�ǲ�������
    private Boolean isRunYear(){
        return (Integer.valueOf(yearStr) % 4 == 0) && (Integer.valueOf(yearStr) % 100 != 0) || (Integer.valueOf(yearStr) % 400 == 0);
    }
    //�ǲ��Ƕ���
    private Boolean isFebruary(){
        return monthStr.equals("02");
    }
    //�ǲ���30����·�
    private Boolean isHasThirtyDays(){
        return monthStr.equals("04") || monthStr.equals("06") || monthStr.equals("09") || monthStr.equals("11");

    }
}
