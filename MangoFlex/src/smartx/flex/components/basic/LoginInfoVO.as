package smartx.flex.components.basic
{
	import mx.collections.ArrayCollection;
	
	[RemoteClass(alias="smartx.system.login.vo.LoginInfoVO")]
	public class LoginInfoVO
	{
		public var name:String;
		public var loginName:String;
		public var id:String;
		public var code:String;
		public var regionCode:String;
		public var totalLoginCount:int;
		public var userLoginCount:Number;
		public var loginTime:Date;
		public var isAdmin:Boolean;
		public var workPositionID:ArrayCollection;
		public var loginStatus:int;
		public var userParam:Object;
		public var dbReadWriteAuth:String;
		public var dispatchtype:String;
		public var isBlackUser:Boolean;
	    /**
	     * 普通用户登录失败
	     */
	    public  static const USER_ERROR_TYPE:int = 1;
	
	    /**
	     * 普通用户登录成功
	     */
	    public static const  USER_LOGINOK_TYPE:int = 2;
	
	    /**
	     * 管理员登录失败
	     */
	    public static const  ADMINUSER_ERROR_TYPE:int = 3;
	
	    /**
	     * 管理员登录成功
	     */
	    public static const  ADMINUSER_LOGINOK_TYPE:int = 4;
	
	    /**
	     * 用户帐户停用
	     */
	    public static const  USER_ACCOUTSTATUS_DISABLED:int = 5;
	
	    /**
	     * 用户帐户过期
	     */
	    public static const  USER_ACCOUT_EXPDATE:int = 6;
	
	    /**
	     * 用户密码过期
	     */
	    public static const  USER_PWD_EXPDATE:int = 7;
		
		/**
		 * 未授权的IP地址
		 */
		public static const  USER_IP_UNAUTHORIZED:int = 8;		
	}
}