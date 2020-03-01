package smartx.bam.flex.modules.businessscenario
{
	import flash.events.Event;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.styletemplate.StyleTemplate03;
	import smartx.flex.components.styletemplate.ifc.EventListenerIFC;
	
	public class ScenarioItemChangeEventListener implements EventListenerIFC
	{
		private var debugMode:Boolean = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_GLOBAL_DEBUGMODE) as Boolean;
		
		private var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		public function ScenarioItemChangeEventListener()
		{
		}
		
		public function handler(event:Event, obj:Object):void
		{
			var treePanel:StyleTemplate03 = obj as StyleTemplate03;
			
			var item:* = treePanel.treePanel.tree.selectedItem;
			
			if( item !=null && item["TYPE"]=="1" && treePanel.isEditMode == true){
				
				var scenarionId:String = String(item["ID"]).replace("S","");
				
				var childPanel:BusinessScenarion = new  BusinessScenarion();
				childPanel.endpoint = endpoint;
				childPanel.scenarioId = scenarionId;
				childPanel.debugMode = debugMode;
				childPanel.percentHeight = 100;
				childPanel.percentWidth = 100;
				
				treePanel.setExtendCanvasContent(childPanel,60);
				
			}
			
		}
	}
}