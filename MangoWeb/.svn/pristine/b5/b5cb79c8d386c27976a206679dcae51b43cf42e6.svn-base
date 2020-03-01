package smartx.bam.flex.modules.businessscenario
{
	import mx.controls.Alert;
	
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	
	public class TempleteRuleDelButtonListener implements ListButtonListener
	{
		public function TempleteRuleDelButtonListener()
		{
		}
		
		public function buttonClick(listPanel:BillListPanel):void
		{
			var item:* = listPanel.getSelectedRowValue();
			if(item == null){
				Alert.show("请先选择一条数据!","提示");
			}else{
				listPanel.isShowAlert = false;
				listPanel.deleteRow();
			}
		}
	}
}