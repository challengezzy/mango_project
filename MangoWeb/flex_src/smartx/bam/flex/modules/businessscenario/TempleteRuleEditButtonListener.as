package smartx.bam.flex.modules.businessscenario
{
	import mx.controls.Alert;
	import mx.managers.PopUpManager;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	
	public class TempleteRuleEditButtonListener implements ListButtonListener
	{
		private var panel:BillListPanel = null;
		
		private var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		public function TempleteRuleEditButtonListener()
		{
		}
		
		public function buttonClick(listPanel:BillListPanel):void
		{
			
			panel = listPanel;
			
			var item:Object = listPanel.getSelectedRowValue();
			
			if(item == null){
				
				Alert.show("请先选择一条规则模板!","提示");
				
			}else{
				
				var templeteId:String = String(item["RULETEMPATEID"]);
				var businessruleId:String = String(item["ID"]);
				var besdesc:String = String(item["DESCRIPTION"]);
				
				var crp:CreateRulePanel = new CreateRulePanel();
				crp.endpoint = endpoint;
				crp.showSystemButton = false;
				crp.isEdit = true;
				crp.templeteId = templeteId;
				crp.businessruleId = businessruleId;
				crp.besdesc = besdesc;
				crp.onQuery = onQuery;
				
				PopUpManager.addPopUp(crp,desktop,true);
				PopUpManager.centerPopUp(crp);
				
			}
			
		}
		
		public function onQuery():void{
			if(panel != null ){
				panel.query(false,true,true);
			}
		}
	}
}