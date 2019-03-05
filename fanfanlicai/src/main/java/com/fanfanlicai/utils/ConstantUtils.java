package com.fanfanlicai.utils;

public class ConstantUtils {

	/**
	 * 调试接口
	 */
	
	public static int updateflag=0;  // 2 设置中心设置提现密码     1密码输错之后的忘记密码
	public static int loginflag=0;	 //  0表示正常登录     1表示从活动页面登录
	public static int shouyiflag=0;  //  0显示饭盒累计收益      1显示饭碗累计收益    2显示饭桶累计收益
	public static int touziflag=0;  //   1表示从第二个投资界面操作完后跳到继续停留在第二投资界面并刷新数据      2表示从我的界面操作完(提现充值)返回后依然停留在我的界面
	public static int fanheorfanwanorfantongflag=0;  //  从首页跳到第二页投资界面后     0显示饭盒投资界面      1显示饭碗投资界面    2显示饭桶投资界面
	public static int yitouxiangmuflag=0;  //  从第二页的饭碗投资界面的饭碗待收收益和饭碗资产点进去后     0显示饭盒已投项目界面      1显示饭碗已投项目界面     2显示饭桶已投项目界面

	// 测试地址
	public static final String BASE_URL = "https://testhf.fanfanjf.com:6843/app.onigiri/app/v2";
	public static final String BASE_WEIXIN_URL = "https://testhf.fanfanjf.com/customer.onigiri";

	// 正式地址
	//public static final String BASE_URL = "https://app.fanfanjf.com/app/v2";
	//public static final String BASE_WEIXIN_URL = "https://weixin.fanfanjf.com/customer.onigiri";

	//1.刷新token
	public static final String REFRESHTOKEN_URL = BASE_URL + "/token/refresh";
	//2.新手机号注册
	public static final String REGISTER_URL = BASE_URL + "/user/register";
	//3.密码登录  
	public static final String MIMA_LOGIN_URL = BASE_URL + "/user/login";
	//4.验证码登录 
	public static final String YANZHENGMA_LOGIN_URL = BASE_URL + "/user/loginByVerifyCode";
	//5.短信或语音
	public static final String SMS_URL = BASE_URL + "/sms/send";
	//6.首页（饭盒）
	public static final String INDEX_URL = BASE_URL + "/index/index";
	//7.用户资金和个人信息
	public static final String MY_ACCOUNT_URL = BASE_URL + "/account/detail";
	//8.邀请奖励
	public static final String MY_INVITE_REWARD_URL = BASE_URL + "/invite/reward";
	//9.邀请详情
	public static final String MY_INVITE_DETAIL_URL = BASE_URL + "/invite/detail";
	//10.图形验证码
	public static final String CAPTCHA_URL = BASE_URL + "/user/captcha";

	//13.投资的项目
	public static final String LOANS_URL = BASE_URL + "/account/loans";
	//14.获取uuid
	public static final String GETUUID_URL = BASE_URL + "/account/uuid";

	//18.银行卡列表和限额
	public static final String BANKLIST_URL = BASE_URL + "/index/banklist";
	//19.提现实名认证
	public static final String VERIFYIDNO_URL = BASE_URL + "/cash/verifyIdNo";
	//20.重置或设置提现密码
	public static final String SETCASHPWD_URL = BASE_URL + "/cash/setCashPwd";
	//21.退出登录
	public static final String LOGOUT_URL = BASE_URL + "/user/logout";
	//22.资金流水
	public static final String TRANSLIST_URL = BASE_URL + "/account/transList";
	//25.发现页
	public static final String FIND_URL = BASE_URL + "/advertise/list";
	//26.公告
	public static final String LIST_URL = BASE_URL + "/article/list";
	//27.公告详情
	public static final String LIST_DETAIL_URL = BASE_URL + "/article/detail";
	//28.找回登录密码
	public static final String FINDPWD_URL = BASE_URL + "/user/findPwd";
	//30.设置地址和邮编
	public static final String SETADDRESS_URL = BASE_URL + "/user/setAddress";
	//33.验证手机号码是否已存在
	public static final String VERIFYPHONE_URL = BASE_URL + "/user/verifyPhone";
	//34.验证身份证号是否已存在
	public static final String VERIFYUSERIDNO_URL = BASE_URL + "/user/verifyUserIdNo";
	//35.查看合同
	public static final String CONTRACT_URL = BASE_URL + "/account/contract";
	//36.检查更新
	public static final String CHECKUPDATE_URL = BASE_URL + "/index/checkUpdate";
	//37.首页轮播图 
	public static final String GETADVERTISELIST_URL = BASE_URL + "/advertise/getAdvertiseList";
	
	//38.我的银行卡
	public static final String BANKCARD_URL = BASE_URL + "/account/bankcard";
	//39.饭碗投资记录
	public static final String FW_INVEST_URL = BASE_URL + "/account/fwInvest/list";
	//40.饭盒投资界面
	public static final String FHINVESTINFO_URL = BASE_URL + "/invest/fhInvestInfo";
	//41.饭碗投资界面
	public static final String FWINVESTINFO_URL = BASE_URL + "/invest/fwInvestInfo";
	//42.饭盒赎回校验
	public static final String FHVERIFYINFO_URL = BASE_URL + "/rec/fhVerifyInfo";
	//43.饭盒赎回提交
	public static final String FHRECSUBMIT_URL = BASE_URL + "/rec/fhRecSubmit";
	//买入
	//44.买入（第一步，点击买入按钮）
	public static final String GOTOINVEST_URL = BASE_URL + "/invest/gotoInvest";
	//46.确认支付(买入第三步)
	public static final String SUBMITINVEST_URL = BASE_URL + "/invest/submitInvest";
	//48.大额预约
	public static final String BOOK_URL = BASE_URL + "/invest/book";
	//49.修改登录密码
	public static final String UPDATEPWD_URL = BASE_URL + "/user/updatePwd";
	//50.加息票
	public static final String RATECOUPON_URL = BASE_URL + "/activity/rateCoupon/list";
	//51.打饭记录
	public static final String SIGNINLIST_URL = BASE_URL + "/activity/signIn/list";
	//52.打饭签到
	public static final String SIGNINSUBMIT_URL = BASE_URL + "/activity/signIn/submit";

	//54.使用饭盒加息票
	public static final String USEFHRATECOUPON_URL = BASE_URL + "/activity/useFhRateCoupon";
	//55.提现票
	public static final String TICKET_URL = BASE_URL + "/cash/ticket/list";
	//55.红包
	public static final String REDBAG_URL = BASE_URL + "/activity/redbag/list";
	//56.饭桶投资记录
	public static final String FT_INVEST_URL = BASE_URL + "/account/ftInvest/list";
	//57.饭桶投资界面
	public static final String FTINVESTINFO_URL = BASE_URL + "/invest/ftInvestInfo";
	//58.饭桶赎回手续费
	public static final String QUERYFTRECFEE_URL = BASE_URL + "/rec/queryFtRecFee";
	//59.饭桶赎回至卡或账户余额
	public static final String FTRECSUBMIT_URL = BASE_URL + "/rec/ftRecSubmit";

	//61.饭桶预约校验
	public static final String FTBOOKVERIFYINFO_URL = BASE_URL + "/invest/ftBookVerifyInfo";
	//62.饭桶买入第1步
	public static final String FTINVESTSTEP1_URL = BASE_URL + "/invest/ftInvestStep1";
	//63.饭桶买入第2步
	public static final String FTINVESTSTEP2_URL = BASE_URL + "/invest/ftInvestStep2";
	//63.签到打饭抽奖
	public static final String SIGNDRAWOPER_URL = BASE_URL + "/activity/signIn/signDrawOper";
	//64.会员体系
	public static final String GETALLMEMBEREQUITY_URL = BASE_URL + "/member/getAllMemberEquity";
	//65.当前会员权益
	public static final String GETMEMBEREQUITY_URL = BASE_URL + "/member/getMemberEquity";
	//66.设置公告为已读
	public static final String SETHAVEREADOPER_URL = BASE_URL + "/article/setHaveReadOper";
	//66.站内信
	public static final String STATIONLETTER_LIST_URL = BASE_URL + "/stationLetter/list";
	//66.查看站内信详情
	public static final String STATIONLETTER_DETAIL_URL = BASE_URL + "/stationLetter/detail";
	//66.设置站内信为已读
	public static final String STATIONLETTER_SETHAVEREADOPER_URL = BASE_URL + "/stationLetter/setHaveReadOper";
	//67.修改用户的邮箱
	public static final String UPDATEUSEREMAIL_URL = BASE_URL + "/user/updateUserEmail";
	//67.验证邮箱是否已存在
	public static final String ISEMAILEXIST_URL = BASE_URL + "/user/isEmailExist";
	//68.优先购列表
	public static final String PREFERTICKET_LIST = BASE_URL + "/activity/preferTicket/list";
	//69.优先购兑换码
	public static final String PREFERTICKET_EXCHANGE = BASE_URL + "/activity/preferTicket/exchange";

	//存管相关
	//1.查询绑卡或签约未签约状态
	public static final String QUERYBINDCARANDSIGNSTATUS_URL = BASE_URL + "/bindcard/queryBindCarAndSignStatus";
	//2.绑卡
	public static final String BINDCARDANDREG_URL = BASE_URL + "/bindcard/bindCardAndReg";
	//3.省
	public static final String LISTPROVINCE_URL = BASE_URL + "/areacode/listProvince";
	//4.市
	public static final String LISTCITY_URL = BASE_URL + "/areacode/listCity";
	//7.签约
	public static final String SIGNCARD_URL = BASE_URL + "/bindcard/signCard";
	//8.换卡状态
	public static final String QUERYAPPLYSTATUS_URL = BASE_URL + "/bindcard/queryApplyStatus";
	//10.上传图片
	public static final String IMAGEFILEUPLOAD_URL = BASE_URL + "/bindcard/imageFileUpload";
	//11.上传图片
	public static final String APPLYCHANGECARD_URL = BASE_URL + "/bindcard/applyChangeCard";
	//12.第一步校验、第二步查询存管绑卡信息
	public static final String RECHARGESTEPONE_URL = BASE_URL + "/recharge/rechargeStepOne";
	//13.第一步校验、第二步查询存管绑卡信息
	public static final String RECHARGESTEPFINAL_URL = BASE_URL + "/recharge/rechargeStepFinal";
	//14.1.获取提现用户信息(提现第一步)（同之前）
	public static final String CASHVERIFYINFO_URL = BASE_URL + "/cash/cashVerifyInfo";
	//14.2.计算提现手续费（提现第二步）
	public static final String QUERYCASHFEE_URL = BASE_URL + "/cash/queryCashFee";
	//14.3.提现（提现第三步）
	public static final String SUBMITCASH_URL = BASE_URL + "/cash/submitCash";
	//16.赎回进度查询
	public static final String GETEARNINGSLIST_URL = BASE_URL + "/transDetail/getEarningsList";
	//17.查询平台平均赎回时间字符串
	public static final String QUERYPLATFORMRECAVGTIMESTR_URL = BASE_URL + "/transDetail/queryPlatformRecAvgTimeStr";
	//18.获取用户开户的信息（信息回显）
	public static final String GETUSERACCOUNTINFO_URL = BASE_URL + "/user/getUserAccountInfo";

	//20.出借详情
	public static final String GETDIDETAILINFO_URL = BASE_URL + "/account/fwLoans";
	//75.饭碗投资记录还款进度
	public static final String GETFWRECORDREPAY_URL = BASE_URL + "/account/fwRecordRepayInfo";
	//76.标的还款列表
	public static final String GETBIDDETAILINFO_URL = BASE_URL + "/account/fwRecordRepayList";
	//投资首页 饭碗买入 获取 项目介绍 计息方式等信息
	public static final String GETFWPRODUCTINFOD_URL = BASE_URL + "/fwProductInfoRest/queryFwProductInfoDetil";
	//直投产品-标的组成列表
	public static final String GETPACKAGEINFOLIST_URL = BASE_URL + "/fwProductInfoRest/ztPackageInfoList";
	//饭碗产品-标的组成列表
	public static final String GETFWPACKAGEINFOLIST_URL = BASE_URL + "/fwProductInfoRest/fwPackageInfoList";
	//自动授权-直投产品
	public static final String SETAUTOAUTHORIZED_URL = BASE_URL + "/user/setAutoAuthorized";
	//直投买入第一步
	public static final String GOTOZTINVEST_URL = BASE_URL + "/ztInvest/gotoZtInvest";
	//直投产品买入第二步
	public static final String SUBMITZTINVEST_URL = BASE_URL + "/ztInvest/submitZtInvest";
	//标的组成(基本信息+图片资料)
	public static final String BORROWDISCLOSUREINFO_URL = BASE_URL + "/fwProductInfoRest/borrowDisclosureInfo";

	//天天饭局
	public static final String DAYDINNERINDEX_URL = BASE_WEIXIN_URL + "/dayDinnerActivity/intcpt-dayDinnerIndex.do";
	//信息披露
	public static final String PLATFORMDATA_URL = BASE_WEIXIN_URL + "/index/info.do";
	//饭饭金服协议微信页
	public static final String PROTOCOL_URL = BASE_WEIXIN_URL + "/userLR/goToServiceAgreement.do";
	//饭盒饭碗收益详情微信页
	public static final String FHRATE_URL = BASE_WEIXIN_URL + "/invest/intcpt-Fh_yieldRate.do";
	public static final String FWRATE_URL = BASE_WEIXIN_URL + "/invest/intcpt-Fw_yieldRate.do";
	public static final String FTRATE_URL = BASE_WEIXIN_URL + "/invest/intcpt-Ft_yieldRate.do";
	//测评微信页
	public static final String EVALUATION_URL = BASE_WEIXIN_URL + "/userSetting/intcpt-evaluation.do";
	//饭饭介绍微信页
	public static final String PLATFORMINTRO_URL = BASE_WEIXIN_URL + "/index/platformIntro.do";
	//帮助与反馈微信页
	public static final String HELP_URL = BASE_WEIXIN_URL + "/account/help.do";
	//饭桶答题微信页
	public static final String TOANSWERPAGE_URL = BASE_WEIXIN_URL + "/invest/toAnswerPage.do";

	//直投计算器
	public static final String ZTCALCULATOR_URL = BASE_WEIXIN_URL + "/ztProductInfoAction/intcpt-gainZtCalculator.do";
	//79.直投-项目介绍页面
	public static final String PROJECTINTROZT_URL = BASE_WEIXIN_URL + "/ztProductInfoAction/projectIntroZt.do";
	//80.直投-计息方式页面
	public static final String INTERESTRATEMETHODZT_URL = BASE_WEIXIN_URL + "/ztProductInfoAction/interestRateMethodZt.do";
	//81.老饭碗-项目介绍页面
	public static final String PROJECTINTROFW_URL = BASE_WEIXIN_URL + "/ztProductInfoAction/projectIntroFw.do";
	//82.老饭碗-计息方式页面
	public static final String INTERESTRATEMETHODFW_URL = BASE_WEIXIN_URL + "/ztProductInfoAction/interestRateMethodFw.do";
	//饭饭金服出借协议
	//public static final String INTERESTAGREEMENT_URL = BASE_WEIXIN_URL + "/ztProductInfoAction/investAgreement.do";
	//public static final String AGREEMENTLEND_URL = BASE_WEIXIN_URL + "/ztProductInfoAction/agreementlend.do";
	//饭饭金服债权转让协议
	//public static final String AGREEMENTORIGHT_URL = BASE_WEIXIN_URL + "/ztProductInfoAction/agreementOright3.do";
	//授权委托书
	public static final String AGREEMENTWARRANT_URL = BASE_WEIXIN_URL + "/ztProductInfoAction/trustAgreementFw.do";

	//73 协议模板 老饭碗
	public static final String DEBTCONSULTATIONFW_URL = BASE_WEIXIN_URL + "/ztProductInfoAction/debtConsultationFw.do";
	//73 协议模板 直投
	public static final String DEBTCONSULTATIONZT_URL = BASE_WEIXIN_URL + "/ztProductInfoAction/debtConsultationZt.do";
	//出借协议合同
	public static final String BORROWCONTRACTNEW_URL = BASE_WEIXIN_URL + "/borrowinfo/intcpt-borrowContractNew.do";
}