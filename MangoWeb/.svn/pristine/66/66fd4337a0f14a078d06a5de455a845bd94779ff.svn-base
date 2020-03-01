package smartx.bam.flex.modules.entitymodel.listener
{
	import mx.managers.PopUpManager;
	
	import smartx.bam.flex.modules.entitymodel.DQEntityRuleTaskDesigner;
	import smartx.bam.flex.modules.entitymodel.DQTaskDesigner;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.vo.MetadataTemplet;
	
	public class DQEmRuleDesignTaskButtonListener implements ListButtonListener
	{
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var taskDesigner:DQEntityRuleTaskDesigner;
		
		public function DQEmRuleDesignTaskButtonListener()
		{
		}
		
		public function buttonClick(listPanel:BillListPanel):void
		{
			var item:Object = listPanel.getSelectedRowValue();
			taskDesigner = new DQEntityRuleTaskDesigner();
			taskDesigner.taskCategory = DQTaskDesigner.TASK_CATEGORY_RULECHECK;
			taskDesigner.subItemName = "规则";
			taskDesigner.initCondition = " TASKCATEGORY="+DQTaskDesigner.TASK_CATEGORY_RULECHECK;
			if(item != null ){
				var tempRule:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet(item["MTCODE"]);
				if(tempRule != null ){
					var ruleXml:XML = tempRule.contentXML;
					var taskCode:String = String(ruleXml.@taskCode);
					if(taskCode != null && taskCode != ""){
						taskDesigner.currentTaskCode = taskCode;
						taskDesigner.entityModelCode = item["ENTITYMODELCODE"];
						taskDesigner.entityCode = item["ENTITYCODE"];
						taskDesigner.ruleCode = item["CODE"];
					}
				}
			}
			
			PopUpManager.addPopUp(taskDesigner,desktop,true);
			PopUpManager.centerPopUp(taskDesigner);
			
		}
	}
}