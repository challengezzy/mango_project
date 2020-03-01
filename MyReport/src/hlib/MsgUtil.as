/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


消息框显示工具类。


*/

package hlib
{
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	
	public final class MsgUtil
	{
		/**
		 * @param closeHandler = function():void
		 */ 
		public static function ShowInfo(message:String, closeHandler:Function = null):void
		{
			Alert.okLabel = "确定";
			Alert.show(message, "信息", Alert.OK, null, 
				function (e:CloseEvent):void
				{
					if(closeHandler!=null) closeHandler();
				});
		}
		/**
		 * @param okHandler = function():void
		 * @param cancelHandler = function():void
		 */
		public static function ShowQuestion(message:String, okHandler:Function, cancelHandler:Function = null):void
		{
			Alert.okLabel = "确定";
			Alert.cancelLabel="取消";
			Alert.show(message, "询问", Alert.OK|Alert.CANCEL, null,
				function (e:CloseEvent):void
				{
					if(e.detail == Alert.OK && okHandler!=null) 
						okHandler();
					else if(e.detail == Alert.CANCEL && cancelHandler!=null)
						cancelHandler();
				} 
			);
		}		
	}
}