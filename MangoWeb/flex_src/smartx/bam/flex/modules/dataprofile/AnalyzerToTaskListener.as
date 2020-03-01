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
	 */
	public class AnalyzerToTaskListener implements CardButtonListener
	{
		private var thisCardPanel:BillCardPanel;
		
		private var taskDesigner:AnalyzerSetTaskWindow;
		
		private var cardValue:Object;
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		public function buttonClick(cardPanel:BillCardPanel):void
		{
			thisCardPanel = cardPanel;
			cardValue = thisCardPanel.getDataValue();
			
			var analySetId:String = cardValue["ID"];
			var analySetDs:String = cardValue["DATASOURCENAME"];
			var analySetCode:String = cardValue["CODE"];
			
			taskDesigner = new AnalyzerSetTaskWindow();
			taskDesigner.analyzerSetId = analySetId;
			taskDesigner.analyzerCode = analySetCode;
			taskDesigner.datasourceName = analySetDs;
			
			PopUpManager.addPopUp(taskDesigner,desktop,true);
			PopUpManager.centerPopUp(taskDesigner);
		}
		
		private function desingOK():void{
//			if(taskDesigner.getContentXml() != null)
//				cardValue["CONTENT"] = taskDesigner.getContentXml();
//			else
//				cardValue["CONTENT"] = "";
//			
//			PopUpManager.removePopUp(taskDesigner);
//			taskDesigner.confirmFunc = null;
//			taskDesigner = null;
			MemoryUtil.forceGC();
		}
		
	}
}