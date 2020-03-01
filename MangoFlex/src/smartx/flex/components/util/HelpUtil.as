package smartx.flex.components.util
{
	import smartx.flex.components.basic.HelpPanel;
	import smartx.flex.components.basic.HelpWindow;

	public class HelpUtil
	{
		
		public static const SYS_MENU_HEAD:String = "SYS_MENU_";
		public static const SYS_LOGIN_HEAD:String = "SYS_LOGIN_";
		
		private static var helpPanel:HelpPanel;
		private static var helpWindow:HelpWindow;
		
		private static var instance:HelpUtil;
		
		public function HelpUtil()
		{
			
		}
		
		public static function getInstance():HelpUtil{
			if(instance == null ){
				instance = new HelpUtil();
				helpPanel = new HelpPanel();
				helpWindow = new HelpWindow();
			}
			return instance;
		}
		
		public function getHelpPanel():HelpPanel{
			return helpPanel;
		}
		
		public function getHelpWindow():HelpWindow{
			return helpWindow;
		}
		
		public function dispatchHelpEvent(type:String):void{
			HelpUtil.getInstance().getHelpWindow().dispatchHelpEvent(type);
		}
		
		public function changeEventStatus(flag:Boolean):void{
			HelpUtil.getInstance().getHelpWindow().isDispatchHelpEvent = flag;
		}
		
		public function getEventStatus():Boolean{
			return HelpUtil.getInstance().getHelpWindow().isDispatchHelpEvent;
		}
		
		public function refreshEvents():void{
			HelpUtil.getInstance().getHelpWindow().initData();
		}
	}
}