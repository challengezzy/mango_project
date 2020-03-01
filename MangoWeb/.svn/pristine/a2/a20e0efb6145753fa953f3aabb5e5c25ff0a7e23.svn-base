package smartx.bam.flex.modules.businessscenario
{
	import mx.managers.PopUpManager;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	
	public class TempleteRuleCreateButtonListener implements ListButtonListener
	{
		private var panel:BillListPanel = null;
		
		private var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		public function TempleteRuleCreateButtonListener()
		{
		}
		
		public function buttonClick(listPanel:BillListPanel):void
		{
			
			panel = listPanel;
			var crp:CreateRulePanel = new CreateRulePanel();
			crp.endpoint = endpoint;
			crp.showSystemButton = false;
			crp.isEdit = false;
			crp.onQuery = onQuery;
			
			PopUpManager.addPopUp(crp,desktop,true);
			PopUpManager.centerPopUp(crp);
			
		}
		
		public function onQuery():void{
			if(panel != null ){
				panel.query(false,true,true);
			}
		}
	}
}