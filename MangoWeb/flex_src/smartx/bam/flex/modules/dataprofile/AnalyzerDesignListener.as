package smartx.bam.flex.modules.dataprofile
{
	import mx.managers.PopUpManager;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.ifc.CardButtonListener;
	import smartx.flex.components.util.MemoryUtil;

	/**
	 * @author zzy
	 * @date Aug 31, 2011
	 * 分析集的剖析任务设计
	 */
	public class AnalyzerDesignListener implements CardButtonListener
	{
		private var thisCardPanel:BillCardPanel;
		
		private var designer:AnalyzerSetDesigner;
		
		private var cardValue:Object;
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		public function buttonClick(cardPanel:BillCardPanel):void
		{
			thisCardPanel = cardPanel;
			cardValue = thisCardPanel.getDataValue();
			var content:String = cardValue["CONTENT"];
			
			designer = new AnalyzerSetDesigner();
			designer.confirmFunc = desingOK;
			designer.datasourceName = cardValue["DATASOURCENAME"]
			
			if(content != null)
				designer.contentXml = new XML(content)
			
			PopUpManager.addPopUp(designer,desktop,true);
			PopUpManager.centerPopUp(designer);
		}
		
		private function desingOK():void{
			if(designer.getContentXml() != null)
				cardValue["CONTENT"] = designer.getContentXml();
			else
				cardValue["CONTENT"] = "";
			
			PopUpManager.removePopUp(designer);
			designer.confirmFunc = null;
			designer = null;
			MemoryUtil.forceGC();
		}
		
	}
}