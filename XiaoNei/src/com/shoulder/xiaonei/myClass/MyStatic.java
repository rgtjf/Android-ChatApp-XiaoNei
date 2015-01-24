package com.shoulder.xiaonei.myClass;

public class MyStatic {
	
	public final static int FINISHTHIS = 0;
    public final static int SUCCESSTHIS = 1;
    
    public final static int Girl = 0;
    public final static int Boy = 1;
    
    public final static int Status_Loading = 0;
    public final static int Status_Success = 1;
    public final static int Status_Fail = 2;

	public final static int MAIN_GOTOSECOND=2;//向二级界面跳转的code
	public final static int MAIN_GOTOSUBSCRIBE=3;//向订阅界面跳转的code
	public final static int TUCAOQIANGCOMMENTFABU =4;
    public final static int TUCAOQIANGFABU =5;
    public final static int TIAZAOFABU_TEXT=6;
    public final static int TIAOZAOFABU=7;
    public final static int TIAOZAOFABU_PHOTO=8;
    public final static int GOTOCHANGESCHOO=9;
    public final static int CHATTYPE_SHETUAN=10;
    public final static int CHATTYPE_HUODONG=11;
    public final static int CHATTYPE_SINGLE=12;
    public final static int CHATWHO_ME=13;//这条聊天是我（右边）还是别人（左边）
    public final static int CHATWHO_OTHERS=14;
    public final static int XListViewType_Chat=15;//聊天的下拉加载更多
    
    public final static int MSG_changeLayout = 0x000;
    public final static int MSG_buildCard = 0x901;
    
    public final static int DOUBLE_CLICK_TIME = 350; 
    
    public final static int MAXINT = 2147483647;
    public final static String MAXDATE = "2022/1/1%2012:12:12";
    public final static String MAXDATE_STANDARD = "2022/1/1 2012:12:12";
    
    public final static int CloseTime = 3000;
    public final static int GiveMeFive_FinishDelay = 6000; 
    public final static int GiveMeFive_Toast = 2700;
    public final static int FinishSearchTime = 5000;
    public final static long LoadingTime = 7000;
    public final static long LoadingPost = 3000;
    public final static long StartPageLoadingTime = 1500;
    public final static long TuCaoQiang_Comment_Fabu_SendMessageTime = 3000;
    public final static long Chat_TimeGap = 600000;//10min
    
    public final static String DB_Name = "sheyouDB";
    public final static String DB_tableName = "chatLast";
    public final static String DB_fieldObjectId = "objectId";
    public final static String DB_fieldHaveRead = "haveRead";
    public final static String DB_fieldLastMessage = "lastMessage";
    public final static int DB_HaveRead = 1;
    public final static int DB_HaveNotRead = 0;
    
    public final static String HTTPFAIL = "JIOJYIGY";
    
    public final static String Preferences_StartPage = "startPage";
    public final static String Preferences_StartPage_HasShowed = "hasShowed";
    
    public final static String KEYNAME_Avator = "avator";
    public final static String KEYNAME_ChatType = "type";
    public final static String KEYNAME_Content = "content";
    public final static String KEYNAME_From = "where_come_from";
    public final static String KEYNAME_GroupId = "groupId";
    public final static String KEYNAME_HuoDongName = "huodongName";
    public final static String KEYNAME_HuoDongId = "huodongId";
    public final static String KEYNAME_ObjectId = "objectId";
    public final static String KEYNAME_OrgId = "orgID";
    public final static String KEYNAME_OrgName = "orgName";
    public final static String KEYNAME_LeanCloudData = "com.avos.avoscloud.Data";
    public final static String KEYNAME_Push_Action = "action";
    public final static String KEYNAME_Push_Icon = "push_icon";
    public final static String KEYNAME_Push_Title = "push_title";
    public final static String KEYNAME_Push_Content = "push_content";
    public final static String KEYNAME_Push_NotificationId = "push_notification";
    public final static String KEYNAME_Register_Page_schoolId = "register_schoolId";
    public final static String KEYNAME_Register_Page_phoneNumber = "register_phoneNumber";
    public final static String KEYNAME_Register_Page_password = "register_password";
    public final static String KEYNAME_Register_Page_sex = "register_sex";
    public final static String KEYNAME_Register_Page_nickName = "register_nickName";
    public final static String KEYNAME_SingleName = "singleName";
    public final static String KEYNAME_SingleId = "singleId";
    public final static String KEYNAME_TitleText = "title_text";
    public final static String KEYNAME_Url = "uri";
    public final static String KEYNAME_WhereComeFrom = "whereComeFrom";
    
    public final static String KEYNAME_MESSAGE_NAME = "name";
    public final static String KEYNAME_MESSAGE_AVATAR = "image2";
    
    public final static String KEYNAME_GroupNameAndId = "groupNameAndId";
    public final static String LeanCloud_myUser = "myUser";
    public final static String LeanCloud_myUser_Name = "leanCloud_myUser_Name";
    public final static String LeanCloud_myUser_Installation = "leanCloud_myUser_Installation";
    public final static String LeanCloud_Installation_installationId = "installationId";
    public final static String LeanCloud_Push_Action_GuanZhu = "com.shoulder.GUAN_ZHU";
    public final static String LeanCloud_Push_Action_HuoDongTuiJian = "com.shouldr.HuoDongTuiJian";
    public final static int LeanCloud_Push_NotificationId_Guanzhu = 1000000;
    public final static int LeanCloud_Push_NotificationId_Chat = 2000000;
    public final static int LeanCloud_Push_NotificationId_HuoDongTuiJian = 3000000;
    
    //与ios聊天所需的冗余字段
    public final static String IOS_KEYNAME_GROUPID = "id";
    public final static String IOS_KEYNAME_MYNAME = "nickname";
    public final static String IOS_KEYNAME_MYPHONE = "phone";
    public final static String IOS_KEYNAME_MYURL = "image";
    
    
    
}
