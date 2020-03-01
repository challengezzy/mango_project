package smartx.flex.modules.basic.system.help.listeners
{
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.controls.Button;
	import mx.managers.PopUpManager;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.StyleTemplate03;
	import smartx.flex.components.styletemplate.ifc.EventListenerIFC;
	import smartx.flex.modules.basic.system.help.HelpFileUploadPanel;
	
	public class UploadFileBtnListener implements EventListenerIFC
	{
		private var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		private var treePanel:StyleTemplate03;
		
		private var bcp:BillCardPanel;
		
		private var dataValue:Object;
		
		private var designBtn:Button;
		
		public function UploadFileBtnListener()
		{
		}
		
		public function handler(event:Event, obj:Object):void
		{
			treePanel = obj as StyleTemplate03;
			if(treePanel.treePanel.tree.selectedItem == null ){
				return;
			}
			var dis:DisplayObject;
			if(treePanel.cardPanelBox.getChildren().length > 0)
				dis = treePanel.cardPanelBox.getChildAt(0);
			if(dis && dis is BillCardPanel){
				bcp = dis as BillCardPanel;
				dataValue = bcp.getDataValue();
				if(designBtn==null || (designBtn != null && !treePanel.buttonBar.contains(designBtn)))
					treePanel.buttonBar.addChild(createBtn(bcp));
			}
				
		}
		
		private function createBtn(bcp:BillCardPanel):Button{
			
			designBtn = new Button();
			designBtn.label = "上传文件";
			designBtn.toolTip = "上传图片或FLV文件";
//			designBtn.setStyle("icon",AssetsFileLib.uploadIcon);
			
			designBtn.addEventListener(MouseEvent.CLICK,function(event:MouseEvent):void{
				
				var hfup:HelpFileUploadPanel = new HelpFileUploadPanel();
				hfup.dataValue = dataValue;
				hfup.bcp = bcp;
				
				PopUpManager.addPopUp(hfup,treePanel.root,true);
				PopUpManager.centerPopUp(hfup);
				
			});
			return designBtn;
		}
	}
}