package smartx.bam.flex.modules.datasource
{
	import flash.utils.getDefinitionByName;
	
	import mx.controls.Alert;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.ifc.CardButtonListener;
	import smartx.flex.components.vo.ItemVO;
	import smartx.flex.components.vo.SimpleComboxItemVO;
	import smartx.flex.components.vo.SimpleRefItemVO;
	import smartx.flex.components.vo.TempletVO;
	
	/**
	 * sky zhangzz
	 **/
	public class RelationDBAdvButtonListener implements CardButtonListener
	{
		private var remoteObj:RemoteObject;
		private var adaptorClass:String;
		private var billCardPanel:BillCardPanel;
		private var dataValue:Object;
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		public function RelationDBAdvButtonListener()
		{
			
		}
		
		private function confirmBtnClick():void{

		}
		
		public function buttonClick(bcp:BillCardPanel):void
		{
			billCardPanel = bcp;
			
			if(billCardPanel != null){
				
				var dataValue:Object = bcp.getDataValue();
				var refVO:SimpleComboxItemVO = dataValue["TYPE"] as SimpleComboxItemVO;
				
				if(refVO != null ){
					
					var config:String = dataValue["CONFIGURATION"] as String;
					
					var dsType:String = refVO.id;
					
					var dbap:DataBaseAdvPanel = new DataBaseAdvPanel();
					dbap.dsType = dsType;
					dbap.config = config;
					dbap.bcp = bcp;
					
					PopUpManager.addPopUp(dbap,desktop,true);
					PopUpManager.centerPopUp(dbap);
					
				}else{
					
					Alert.show("请先选择数据源类型!","提示");
					
				}
				

			}	
		}
	}
}