package smartx.bam.flex.modules.alertmessage
{
	import mx.controls.Alert;
	import mx.core.IFlexDisplayObject;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	
	public class AlertMsgDelButtonListener implements ListButtonListener
	{	
		public function AlertMsgDelButtonListener()
		{
		}
		
		public function buttonClick(listPanel:BillListPanel):void
		{
			var item:* = listPanel.getSelectedRowValues();
			
			if(item == null){
				Alert.show("请先选择一条数据!","提示");
			}else{
				listPanel.isShowAlert = false;
				listPanel.deleteRow();
			}
		}
	}
}