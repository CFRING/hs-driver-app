package com.hongshi.wuliudidi.utils;

import java.util.LinkedHashSet;
import java.util.Set;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class JpushUtil {

	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;
	public static Context mContext;
	
	public void initJpush(Context mContext,String tag,String alias){
		this.mContext = mContext;
		setTag(tag);
		setAlias(alias);
	}
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				JPushInterface.setAliasAndTags(mContext,
						(String) msg.obj, null, mAliasCallback);
				break;

			case MSG_SET_TAGS:
				JPushInterface.setAliasAndTags(mContext, null,
						(Set<String>) msg.obj, mTagsCallback);
				break;
			default:
			}
		}
	};
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
            case 0:
                logs = "Set tag and alias success";
                break;
                
            case 6002:
                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                if (Util.isConnected(mContext)) {
                	mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                } else {
                }
                break;
            
            default:
                logs = "Failed with errorCode = " + code;
            }
            
        }
	    
	};
	
    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
            case 0:
                logs = "Set tag and alias success";
                break;
                
            case 6002:
                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                if (Util.isConnected(mContext)) {
                	mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                } else {
                }
                break;
            
            default:
                logs = "Failed with errorCode = " + code;
            }
            
        }
        
    };
    public void setTag(String tag) {
//		String tag = "user";
		// ","隔开的多个 转换成 Set
		String[] sArray = tag.split(",");
		Set<String> tagSet = new LinkedHashSet<String>();
		for (String sTagItme : sArray) {
			tagSet.add(sTagItme);
		}

		// 调用JPush API设置Tag
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

	}
	public void setAlias(String alias) {
		// 调用JPush API设置Alias
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}
}
