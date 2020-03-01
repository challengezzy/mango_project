package smartx.flex.modules.menu
{
	
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.controls.Button;
	import mx.managers.PopUpManager;
	
	import smartx.flex.components.assets.AssetsFileLib;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.StyleTemplate03;
	import smartx.flex.components.styletemplate.ifc.EventListenerIFC;
	
	public class MenuItemChangeListener implements EventListenerIFC
	{
		private var debugMode:Boolean = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_GLOBAL_DEBUGMODE) as Boolean;
		
		private var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		private var bcp:BillCardPanel;
		
		private var dataValue:Object;
		
		private var designBtn:Button;
		
		private var isQv:Boolean = false;
		
		private var treePanel:StyleTemplate03;
		
		public function MenuItemChangeListener()
		{
		}
		
		public function handler(event:Event, obj:Object):void
		{
			treePanel = obj as StyleTemplate03;
			
			if(treePanel.treePanel.tree.selectedItem == null )
				return;
			var dis:DisplayObject;
			if(treePanel.cardPanelBox.getChildren().length > 0)
				dis = treePanel.cardPanelBox.getChildAt(0);
			if(dis && dis is BillCardPanel){
				bcp = dis as BillCardPanel;
				dataValue = bcp.getDataValue();
				if(designBtn==null || (designBtn != null && !treePanel.buttonBar.contains(designBtn))){
					treePanel.buttonBar.addChild(createBtn(bcp));
				}
			}
		}
		
		private function createBtn(bcp:BillCardPanel):Button{
			
			designBtn = new Button();
			designBtn.label = "菜单路径";
			designBtn.toolTip = "菜单路径高级编辑器";
			var icon:Class = AssetsFileLib.designIcon;
			designBtn.setStyle("icon",icon);
			
			designBtn.addEventListener(MouseEvent.CLICK,function(event:MouseEvent):void{
				
				var mudp:MenuURLDesignerPanel = new MenuURLDesignerPanel();
				mudp.dateValue = bcp.getDataValue();
				mudp.bcp = bcp;
				
				PopUpManager.addPopUp(mudp,treePanel,true);
				PopUpManager.centerPopUp(mudp);
				
			});
			return designBtn;
		}
	}
}