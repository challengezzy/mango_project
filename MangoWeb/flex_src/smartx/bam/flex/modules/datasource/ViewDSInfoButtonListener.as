package smartx.bam.flex.modules.datasource
{
	import mx.collections.ArrayCollection;
	import mx.managers.PopUpManager;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.vo.MetadataTemplet;
	
	public class ViewDSInfoButtonListener implements ListButtonListener
	{	
		private var endpoint:String = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		
		private var host:String = "http://127.0.0.1:8080/DBExplorer";
		
		public function ViewDSInfoButtonListener()
		{
			
			if(endpoint != null){
				var tempAddress:String = endpoint.split("/messagebroker/amf")[0];
				var index:int = tempAddress.lastIndexOf("/");
				var ipAddress:String = tempAddress.substr(0,index);
				host = ipAddress+"/DBExplorer";
			}
			
			var metadataTemplet:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet("MT_SYSTEMCONFIG");
			
			if(metadataTemplet != null){
				var temp:XML = metadataTemplet.contentXML;
				
				var tempHost:String = String(temp.address);
				
				if(tempHost != ""){
					host = tempHost;
				}
			}
		}
		
		public function buttonClick(listPanel:BillListPanel):void
		{
			var selectedItem:Object = listPanel.getSelectedRowValue();
			if(selectedItem != null){
				var chs:DataBaseChooserPanel = new DataBaseChooserPanel();
				chs.selectedItem = selectedItem;
				chs.address = host;
				
				PopUpManager.addPopUp(chs,listPanel.root,true);
				PopUpManager.centerPopUp(chs);
				
			}else{
				SmartXMessage.show("需要选中一条记录!");
			}
		}
	}
}