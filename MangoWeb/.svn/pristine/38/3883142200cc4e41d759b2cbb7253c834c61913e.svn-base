package smartx.bam.flex.utils
{
	import flash.display.DisplayObject;
	import flash.events.MouseEvent;
	
	import mx.controls.Alert;
	import mx.managers.PopUpManager;
	
	import smartx.bam.flex.modules.dashboard.DashboardLayoutPanel;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.util.MemoryUtil;

	public class DBLayoutManagerUtil
	{
		private var dataValue:Object;
		
		private var bcp:BillCardPanel;
		
		private var popupParent:DisplayObject;
		
		private var layoutManager:DashboardLayoutPanel;
		
		public var doCodes:Array;
		
		public function DBLayoutManagerUtil(popupParent:DisplayObject,bcp:BillCardPanel){
			this.popupParent = popupParent;
			this.bcp = bcp;
		}
		
		public function advClick(event:MouseEvent):void{
			if(bcp != null){
				dataValue = bcp.getDataValue();
				var content:String = dataValue["CONTENT"];
				var type:Object = dataValue["TYPE"];
				layoutManager = new DashboardLayoutPanel();
				layoutManager.contentXml = new XML(content);
				layoutManager.confirmFunc = confirmBtnClick;
				if(doCodes)
					layoutManager.doCodes = doCodes;
				PopUpManager.addPopUp(layoutManager,popupParent,true);
				PopUpManager.centerPopUp(layoutManager);
				
			}else
				Alert.show("打开布局管理时发生错误！","error");
		}
		
		
		private function confirmBtnClick():void{
			dataValue["CONTENT"] = layoutManager.getContentXml();
			bcp.setDataValue(dataValue);
			PopUpManager.removePopUp(layoutManager);
			layoutManager.confirmFunc = null;
			layoutManager = null;
			MemoryUtil.forceGC();
		}
		
	}
}