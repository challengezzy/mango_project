package smartx.bam.flex.modules.analyzeview
{
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.controls.Button;
	import mx.managers.PopUpManager;
	
	import smartx.bam.flex.modules.analyzeview.designer.AnalyzerDesignerPanel;
	import smartx.bam.flex.modules.analyzeview.designer.DesignAnalyzeviewPanel;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.StyleTemplate03;
	import smartx.flex.components.styletemplate.ifc.EventListenerIFC;
	import smartx.flex.components.util.IconUtility;
	
	public class AnalyzeviewItemChangeListener implements EventListenerIFC
	{
		private var debugMode:Boolean = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_GLOBAL_DEBUGMODE) as Boolean;
		
		private var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var bcp:BillCardPanel;
		
		private var dataValue:Object;
		
		private var designBtn:Button;
		
		private var isQv:Boolean = false;
		
		private var treePanel:StyleTemplate03;
		
		public function AnalyzeviewItemChangeListener()
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
				if(bcp.templetCode == "T_V_BAM_ANALYZEVIEW"){
					if(designBtn==null || (designBtn != null && !treePanel.buttonBar.contains(designBtn)))
						treePanel.buttonBar.addChild(createBtn(bcp));
					isQv = true;
				}else{
					if(designBtn && treePanel.buttonBar.contains(designBtn))
						treePanel.buttonBar.removeChild(designBtn);
					isQv = false;
				}
			}
		}
		
		private function createBtn(bcp:BillCardPanel):Button{
			
			designBtn = new Button();
			designBtn.label = "高级";
			designBtn.toolTip = "分析视图元数据高级编辑器";
			var icon:Class = IconUtility.getClass(designBtn,"smartx/flex/modules/assets/images/design.png",16,16);
			designBtn.setStyle("icon",icon);
			
			designBtn.addEventListener(MouseEvent.CLICK,function(event:MouseEvent):void{

				var dap:AnalyzerDesignerPanel = new AnalyzerDesignerPanel();
				dap.dataValue = bcp.getDataValue();
				dap.bcp = bcp;
				
				PopUpManager.addPopUp(dap,desktop,true);
				PopUpManager.centerPopUp(dap);
			});
			return designBtn;
		}
		
	}
}