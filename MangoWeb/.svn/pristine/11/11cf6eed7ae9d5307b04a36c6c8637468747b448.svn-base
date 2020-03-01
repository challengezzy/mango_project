package smartx.bam.flex.modules.queryview
{
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Button;
	import mx.core.IFlexDisplayObject;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.assets.AssetsFileLib;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.event.BillCardPanelEvent;
	import smartx.flex.components.styletemplate.StyleTemplate03;
	import smartx.flex.components.styletemplate.ifc.EventListenerIFC;
	import smartx.flex.components.util.LoadingWindow;
	
	public class QueryViewItemChangeListener implements EventListenerIFC
	{
		private var debugMode:Boolean = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_GLOBAL_DEBUGMODE) as Boolean;
		
		private var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var bcp:BillCardPanel;
		
		private var isQv:Boolean = false;
		
		private var dataValue:Object;
		
		[Bindable]
		private var queryBtn:Button;
		
		private var treePanel:StyleTemplate03;
		
		public function QueryViewItemChangeListener()
		{
		}
		
		public function handler(event:Event, obj:Object):void
		{
			var tree:StyleTemplate03 = obj as StyleTemplate03;
			
			treePanel = tree;
			
			if(treePanel.treePanel.tree.selectedItem == null )
				return;
			var disObj:DisplayObject;
			if(treePanel.cardPanelBox.getChildren().length > 0)
				disObj = treePanel.cardPanelBox.getChildAt(0);
			if(disObj && disObj is BillCardPanel){
				bcp = disObj as BillCardPanel;
				dataValue = bcp.getDataValue();
				if(bcp.templetCode == "T_V_BAM_QUERYVIEW"){
					bcp.addEventListener(BillCardPanelEvent.SAVESUCCESSFUL,cardPanelSaveSuccessful);
					if(queryBtn==null || (queryBtn != null && !treePanel.buttonBar.contains(queryBtn)))
						treePanel.buttonBar.addChild(createQueryBtn(bcp));
					isQv = true;
				}else{
					if(queryBtn && treePanel.buttonBar.contains(queryBtn))
						treePanel.buttonBar.removeChild(queryBtn);
					isQv = false;
				}
			}
		}
		
		private function createQueryBtn(bcp:BillCardPanel):Button{
			queryBtn = new Button();
			queryBtn.label = "查询结果集";
			queryBtn.toolTip = "查询结果集";
			queryBtn.setStyle("icon",AssetsFileLib.queryResultIcon);
			
			var listener:QueryButtonListener = new QueryButtonListener();
			queryBtn.addEventListener(MouseEvent.CLICK,function(event:MouseEvent):void{
				listener.buttonClick(bcp);
			});
			return queryBtn;
		}
		
		private function cardPanelSaveSuccessful(event:BillCardPanelEvent):void{
			var obj:Object = bcp.getDataValue();
			if(parseInt(obj[bcp.getTempletVO().pkname]) > 0){
				var item:Object = treePanel.treePanel.tree.selectedItem;
				var querySql:String = bcp.getTempletVO().pkname+"='"+obj[bcp.getTempletVO().pkname]+"'"
				bcp.refresh(querySql);
				//bamService.queryAllDependBv(obj["CODE"]);//查询是否有关联的依赖项
			}
			if(bcp != null)
				bcp.removeEventListener(BillCardPanelEvent.SAVESUCCESSFUL,cardPanelSaveSuccessful);
		}

		
	}
}