package smartx.bam.flex.modules.businessscenario
{
	import com.adobe.utils.StringUtil;
	
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	import flash.utils.getDefinitionByName;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.ConfirmUtil;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.SimpleHashVO;
	import smartx.flex.components.vo.XMLExportObject;
	
	/**
	 * sky zhangzz
	 **/
	public class CreateRuleButtonListener implements ListButtonListener
	{
		private var formService:RemoteObject;
		private var adaptorClass:String;
		private var billListPanel:BillListPanel;
		private var destination:String = GlobalConst.SERVICE_FORM;
		private var endpoint:String  = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		private var userName:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_LOGIN_USER_LOGINNAME));
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		public function CreateRuleButtonListener()
		{
			formService = new RemoteObject(destination);
			
			if(endpoint != null){
				formService.endpoint = endpoint;
			}
		}
		
		public function buttonClick(blp:BillListPanel):void
		{
			this.billListPanel = blp;
			
			var item:Object = blp.getSelectedRowValue();
			
			if(item == null){
				
				Alert.show("请先选择一条规则模板!","提示");
				
			}else{
				
				var templeteId:String = String(item["ID"]);
				
				var crp:CreateRulePanel = new CreateRulePanel();
				crp.endpoint = endpoint;
				crp.showSystemButton = false;
				crp.templeteId = templeteId;
				crp.initQueryCondition = " ruletemplateid="+templeteId;
				
				PopUpManager.addPopUp(crp,desktop,true);
				PopUpManager.centerPopUp(crp);
			}
			
		}
		
		private function faultHandler(event:FaultEvent):void{   
			Alert.show(event.fault.faultString, 'Error');
		}
		
		private function getSimpleHashVoArrayByDSHandler(event:ResultEvent):void{
			var arrayCol:ArrayCollection = new ArrayCollection();
			var array:Array = event.result as Array;
			
			for each(var simpleVO:SimpleHashVO in array){
				arrayCol.addItem(simpleVO.dataMap);
			}
			
			
			
		}

	}
}