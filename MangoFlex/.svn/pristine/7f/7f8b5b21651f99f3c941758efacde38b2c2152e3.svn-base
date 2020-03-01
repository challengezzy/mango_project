package smartx.flex.components.util
{
	import flash.display.Sprite;
	
	import mx.controls.Alert;
	import mx.managers.PopUpManager;

	public class ConfirmUtil
	{
		public function ConfirmUtil()
		{
		}
		
		public static function confirm(text:String,parent:Sprite,confirmHandler:Function,iconClass:Class=null):void
		{
			//第一个参数是要显示的文本，
			//第二个参数是窗口的标题
			//第三个参数是按纽
			//第四个参数是父窗体
			//第五个参数是关闭后要执行的动作函数
			//第六个参数是图标
			//第七个参数是默认的按纽 
			var a:Alert = Alert.show(text, "确认", Alert.YES|Alert.NO, parent, confirmHandler, iconClass, Alert.NO);
//			a.setStyle("backgroundColor", 0xffffff);
//			a.setStyle("backgroundAlpha", 0.50);
//			a.setStyle("borderColor", 0xffffff);
//			a.setStyle("borderAlpha", 0.75);
//			a.setStyle("color", 0x000000);
			PopUpManager.centerPopUp(a);
		}
	}
}