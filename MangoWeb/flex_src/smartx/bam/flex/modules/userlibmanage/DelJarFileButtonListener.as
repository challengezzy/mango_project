package smartx.bam.flex.modules.userlibmanage
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
	public class DelJarFileButtonListener implements ListButtonListener
	{
		private var adaptorClass:String;
		private var billListPanel:BillListPanel;
		private var bamService:RemoteObject;
		private var destination:String = GlobalConst.SERVICE_FORM;
		private var endpoint:String  = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		private var userName:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_LOGIN_USER_LOGINNAME));
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var item:Object;
		
		public function DelJarFileButtonListener()
		{
			bamService = new RemoteObject("smartXBAMService");
			
			if(endpoint != null){
				bamService.endpoint = endpoint;
			}
			
			bamService.deleteJarFile.addEventListener(ResultEvent.RESULT,deleteJarFileHandler);
			bamService.deleteJarFile.addEventListener(FaultEvent.FAULT,faultHandler);
			
		}
		
		public function buttonClick(blp:BillListPanel):void
		{
			this.billListPanel = blp;
			
			 item = blp.getSelectedRowValue();
			
			if(item == null){
				
				Alert.show("请先选择一条记录!","提示");
				
			}else{
				
				ConfirmUtil.confirm("确定要删除吗?",desktop,confirmHandler);
				
			}
			
		}
		
		private function faultHandler(event:FaultEvent):void{   
			Alert.show(event.fault.faultString, 'Error');
		}
		
		private function confirmHandler(event:CloseEvent):void{
			
			if(event.detail == Alert.YES){
				
				var id:String = item["ID"] as String;
				var path:String = item["PATH"] as String;
				
				bamService.deleteJarFile(id,path);
			}
		}
		
		private function deleteJarFileHandler(event:ResultEvent):void{
			Alert.show("删除成功!");
			billListPanel.query(false,true,true);
		}

	}
}