package smartx.flex.components.event
{
	import flash.events.Event;

	public class BasicEvent extends Event
	{
		public static const LOGIN_SUCCESSFUL:String = "loginSuccessful";
		public static const LOGIN_FAILED:String = "loginFailed";
		public static const RELOGIN:String = "relogin";
		public static const CLOSE:String = "close";
		public static const LOADMENU_SUCCESSFUL:String = "loadMenuSuccessful";
		public static const LOADDATA_SUCCESSFUL:String = "loadDataSuccessful";
		public static const OPENMENU_SUCCESSFUL:String = "openMenuSuccessful";
		public var menuId:String;
		public var dataValue:Object;
		public function BasicEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false,menuId:String=null,dataValue:Object=null)
		{
			super(type, bubbles, cancelable);
			this.menuId = menuId;
			this.dataValue = dataValue;
		}
		
	}
}