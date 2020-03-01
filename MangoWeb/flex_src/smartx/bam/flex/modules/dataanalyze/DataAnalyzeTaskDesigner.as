package smartx.bam.flex.modules.dataanalyze
{
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	
	import net.brandonmeyer.containers.SuperPanel;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.MTStyleTemplate02;
	import smartx.flex.components.styletemplate.MTStyleTemplate04;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.vo.MetadataTemplet;
	
	public class DataAnalyzeTaskDesigner implements ListButtonListener
	{
		private var endpoint:String = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var billistPanel:BillListPanel;
		
		private var mtCode:String = "MT_PUB_DATATASK_DATAANALYZE";
		
		public function DataAnalyzeTaskDesigner()
		{
		}
		
		public function buttonClick(listPanel:BillListPanel):void
		{
			this.billistPanel = listPanel;
			
			var mtTemplet:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet(mtCode);
			if(mtTemplet != null ){
				var mTStyleTemplate02:MTStyleTemplate02 = new MTStyleTemplate02();
				mTStyleTemplate02.endpoint = endpoint;
				mTStyleTemplate02.metadataTemplet = mtTemplet;
				
				var superPanel:SuperPanel = new SuperPanel();
				superPanel.title = "数据分析任务管理";
				superPanel.width = 800;
				superPanel.height = 500;
				superPanel.setStyle("paddingBottom",3);
				superPanel.setStyle("paddingTop",5);
				superPanel.setStyle("paddingLeft",3);
				superPanel.setStyle("paddingRight",3);
				superPanel.allowClose = true;
				superPanel.allowDrag = true;
				superPanel.allowMaximize = true;
				superPanel.allowResize = true;
				superPanel.addEventListener(CloseEvent.CLOSE,function(event:CloseEvent):void{
					PopUpManager.removePopUp(superPanel);
				});
				
				superPanel.addChild(mTStyleTemplate02);
				
				PopUpManager.addPopUp(superPanel,desktop,true);
				PopUpManager.centerPopUp(superPanel);
			}
			
			
		}
	}
}