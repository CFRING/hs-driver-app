package com.hongshi.wuliudidi;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommonRes {
	public static int time = 0;
	public static List<String> taskIdList = new ArrayList<String>();
	public static final int frontCardPhoto_id=0x7f201591;//个人身份证正面
	public static final int backCardPhoto_id=0x7f201592;//反面
	public static final int drivingPhoto_id=0x7f201593;//驾驶证
	
	public static final int c_frontCardPhoto_id=0x7f201594;//企业认证身份证前面
	public static final int c_backCardPhoto_id=0x7f201595;//反面
	public static final int c_businessPhoto_id=0x7f201596;//营业执照
	public static final int c_transportationPhoto_id=0x7f201597;//道路运输经营许可证
	
	/**
	 * Handler传message用
	 */
	public static final int CAMERA=5001;//拍照
	public static final int GALLERY=5002;//相册
	public static final int MAN=5003;//男
	public static final int WOMAN=5004;//女
	public static final int DELETE_TRUCK=5005;//删除一辆货车
	public static final int DELETE_TEAM_MEMBER=5006;//删除一个车队员
	public static final int DELETE_DRIVER=5007;//删除一个司机
	public static final int RENEW_UPLOAD_PHOTO = 5008;//重新拍到货单据
	public static final int DELETE_BANKCARD = 5010;//删除一张银行卡
	
	public static int roleType=-1;//角色类型；1-普通司机，2-司机队长，3，司机队员
	public static int organizationType=-1;//判断是企业司机或者是个人司机，1-企业，2，个人
	public static int enterpriseType = 1;//企业
	public static int personType = 2;//个人
	public static boolean hasIdentityAuth = false;//是否有实名认证
	public static boolean hasDrivingLicenceAuth = false;//是否有驾驶证认证
	public static boolean hasEnterpriseAuth = false;//是否有企业法人认证
	public static boolean hasBusinessAuth = false;//是否有营业执照
	public static boolean hasTransportationPermissionAuth = false;//是否有道路运输经营许可证
	public static boolean hasAcct = false;//是否已缴纳运输保证金
	public static final int NEWMESSAGE = 10000;//有新消息
	public static final int TASKMESSAGE = 10001;//任务有新消息
	public static final int SYSTEMMESSAGE = 10002;//系统新消息
	public static final int AUCTIONMESSAGE = 10003;//竞价新消息
	public static final int INVITEMESSAGE = 10004;//邀请新消息
	public static final int TRANSITMESSAGE = 10005;//运输任务新消息
	public static final int PLANMESSAGE = 10006;//运输计划新消息
	public static final int STOPTHREAD = 10007;//停止线程
	public static final int STARTTHREAD = 10008;//开启线程
	public static final int GetAllAreaSuccess = 10009;//获取全部区域成功
	public static final int GetAllAreaFailed = 10010;//获取全部区域失败
	public static final int ReacquireAllArea = 10011;//重新获取全部区域
	public static final int CANCELAUCTION = 1000;//取消竞价
	public static final int PONDERATIONUPLOAD = 1001;//上传过磅单
	public static final int CONTINUESCAN = 1002;//继续扫描
	
	public static final int ARRIVETAG  = 2;//货物送达现在标记
	public static final int PICTAG  = 1;//调用上传图片框标记
	public static final int SEXTAG  = 0;//选择性别标记
	
	public static final int TYPE_BOTTOM  = 0;//dialog底部弹出标记
	public static final int TYPE_CENTER  = 1;//居中显示
	
	public static final String RefreshTruck = "action.addTruck";//添加车辆以后发的刷新广播
	public static final String TeamMemberModify = "action.teamMemberModify";//队员信息修改后发的刷新广播
	public static final String DriverModify = "action.driverModify";//司机信息变动后发的刷新广播
	public static final String RefreshWinBid = "action.refresh.winBid";//刷新托运信息界面的广播
	public static final String NewTask = "action.task.plan";//新增派车单所发的广播
	public static final String ACTIONPHOTOPATH = "action.photo.path";//获取图片path的广播
	public static final String RefreshUserInfo = "action.userInfo";//重新获取账号基本信息
	public static final String RefreshGoodsList = "action.refresh.goodsList";//刷新货源列表
	public static final String RefreshData = "action.refresh";//重新获取货源列表、任务列表、消息列表
	public static final String RefreshMyWalletData = "action.refresh_my_wallet_data";//重新获取钱包详情列表
	public static final String RefreshGoodsListWithStartAddr = "action.refresh.with_start_Address";//根据筛选的地址刷新货源列表
	public static final String RefreshGoodsListWithEndAddr = "action.refresh.with_end_Address";//根据筛选的地址刷新货源列表
	public static final String RefreshGoodsListWithStartCorp = "action.refresh.with_Start_Corp";//根据筛选的出发地公司刷新货源列表
	public static final String RefreshGoodsListWithEndCorp = "action.refresh.with_End_Corp";//根据筛选的目的地公司刷新货源列表
	public static final String RefreshGoodsListWithGoodsType = "action.refresh.with_goods_type";//根据筛选的货品类型刷新货源列表
	public static final String GetAuctionTruckList = "action.auction.truckList";//接单详情页面获取竞价车辆列表
	public static final String GetRecGoodsSourceAndFinishOrder = "action.auction.driver_home";//司机端刷新首页成交动态及推荐货源
	public static final String RefreshUserPhoto = "action.driver.refresh.user.photo";//刷新司机用户头像
	public static final String CometStart = "action.comet.start";//开始接收comet消息
	public static final String CometStartForDriver = "action.comet.start_for_driver";//司机开始接收comet消息
	public static final String CometStopForDriver = "action.comet.stop_for_driver";//司机停止接收comet消息
	public static final String CometStop = "action.comet.stop";//停止接收comet消息
	public static final String GetALlMessage = "action.message.all";//接收所有后台消息
	public static final String NewMessage= "action.message.from_news";//收到新的消息
	public static final String MessageFromSystem = "action.message.from_system";//收到系统消息
	public static final String MessageFromAuction = "action.message.from_auction";//收到竞价消息
	public static final String MessageFromTransit = "action.message.from_transit";//收到任务消息
	public static final String DriverTask = "action.driver_task";//司机任务列表刷新
	public static final String MessageFromInvite = "action.message.from_invite";//收到车队邀请消息
	public static final String ClickSystemMessage = "action.click_system";//点击消息fragment的系统消息
	public static final String ClickAuctionMessage = "action.click_auction";//点击消息fragment的竞价消息
	public static final String ClickInviteMessage = "action.click_invite";//点击消息fragment的车队邀请消息
	public static final String ClickTransitMessage = "action.click_transit";//点击消息fragment的任务消息
	public static final String UploadReceiptFinished = "action.upload_receipt.finished";//派车单签收单据上传完毕
	public static final String UploadTakeOrdersCaFinished = "action.upload_receipt.finished";//提货凭证上传完毕
	public static final String UploadPhoto = "action.upload_photo";//上传头像时发送的广播
	public static final String BindNewBankcard = "action.bind_new_bankcard";//新添加银行卡时发送的广播
	
	
	public static final int TYPE_TRUCK_NORMAL = 1;//正常选择车牌号头部两位
	public static final int TYPE_DRIVER_LICENCE = 5;//选择驾照类型
	public static final int TYPE_ALL_TRUCK_TYPE = 3;//选择车型
	public static final int TYPE_TRUCK_NONGYONG = 1002;//农用车type，传给选择车牌号头部的对话框
	
	public static final int TYPE_FAREN_SHENFENZHENG_ZHENGMIAN = 10000;//法人身份证正面
	public static final int TYPE_FAREN_SHENFENZHENG_FANMIAN = 10001;//法人身份证反面
	public static final int TYPE_FAREN_YINGYEZHIZHAO = 10002;//法人营业执照
	public static final int TYPE_FAREN_XUKEZHENG = 10003;//法人道路许可证
	public static final int TYPE_SHIMING_ZHENGMIAN = 10004;//实名认证正面
	public static final int TYPE_SHIMING_FANMIAN = 10005;//实名认证反面
	public static final int TYPE_JIASHIZHENG = 10006;//驾驶证
	public static final int TYPE_HUIDAN = 1007;//派车单货物送达回单
	public static final int TYPE_UPLOAD_HUIDAN = 2000;//上传回单标记
	public static final int TYPE_CALLBACK_PCD_ID = 2001;//任务adapter中返回派车单id标记
//	public static File tempFile = new File("/sdcard/wuliudidi/"+"wuliudidi_photo"+".jpg");
	public static File tempFile = new File(Environment.getExternalStorageDirectory()+"/wuliudidi_photo"+".jpg");
	public static final String Evaluate_Refresh = "action.evaluete.refresh";
	
	public static enum PhotoTag{
		//车辆认证页面的 车头照、车尾照、车头行驶证、车尾行驶证、道路运输证正面、道路运输证反面
		truckHeadPhoto, truckTailPhoto, truckHeadLicencePhoto, truckTailLicencePhoto,
		transiLicenceFrontPhoto, transiLicenceBackPhoto;
	}
	//配置变量
	public static boolean TRUCK = false;
	
	public static String UserId;

	//货源竞拍类型
	public static final int BID_PRICE = 1;//报价
	public static final int FIXED_PRICE = 2;//一口价
	public static final int LONG_TRANSPORT = 3;//接单运输

	//推送通知跳转
	public static final String CZ_AUCTION_DETAIL =  "CZ_AUCTION_DETAIL";//竞价单详情
	public static final String CZ_CONSIGN_DETAIL =  "CZ_CONSIGN_DETAIL";//接单记录的托运信息
	public static final String CZ_ALL_CONSIGN =  "CZ_ALL_CONSIGN";//我的接单记录
	public static final String CZ_ALL_TASK =  "CZ_ALL_TASK";//任务列表
	public static final String CZ_INDEX = "CZ_INDEX";//首页推荐货源

	//自动获取验证码
	public static final int SMS_PASSWORD = 900001;
}
