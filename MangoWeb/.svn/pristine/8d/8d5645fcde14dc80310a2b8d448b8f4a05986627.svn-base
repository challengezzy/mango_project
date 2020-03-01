package smartx.flex.modules.datatask
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
	public class TaskDesignBtnListener implements CardButtonListener
	{
		private var thisCardPanel:BillCardPanel;
		
		private var taskDesigner:DataTaskMainDesigner;
		
		private var cardValue:Object;
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		public function TaskDesignBtnListener()
		{
		}
		
		public function buttonClick(cardPanel:BillCardPanel):void
		{
			thisCardPanel = cardPanel;
			cardValue = thisCardPanel.getDataValue();
			var content:String = cardValue["CONTENT"];
			
			taskDesigner = new DataTaskMainDesigner();
			taskDesigner.confirmFunc = desingOK;
			taskDesigner.dataTaskName = cardValue["NAME"];
			taskDesigner.dataTaskId = cardValue["ID"];
			taskDesigner.dataTaskMtCode = cardValue["MTCODE"];
			taskDesigner.endpoint = cardPanel.endpoint;
			if(content != null)
				taskDesigner.contentXml = new XML(content)
			
			PopUpManager.addPopUp(taskDesigner,desktop,true);
			PopUpManager.centerPopUp(taskDesigner);
		}
		
		private function desingOK():void{
			if(taskDesigner.mainDt.getContentXml() != null)
				cardValue["CONTENT"] = taskDesigner.mainDt.getContentXml();
			else
				cardValue["CONTENT"] = "";
			
			PopUpManager.removePopUp(taskDesigner);
			taskDesigner.confirmFunc = null;
			taskDesigner = null;
			MemoryUtil.forceGC();
		}
		
	}
}