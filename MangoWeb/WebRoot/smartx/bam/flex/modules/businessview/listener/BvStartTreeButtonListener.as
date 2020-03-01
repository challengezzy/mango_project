package smartx.bam.flex.modules.businessview.listener
{
	import mx.controls.Alert;
	
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.core.BillTreePanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	
	/**
	 * sky zhangzz
	 **/
	public class BvStartTreeButtonListener extends BvTreeButtonListener
	{
		public function BvStartTreeButtonListener(){
			super();
		}
		
		override public function buttonClick(btp:BillTreePanel):void{
			billTreePanel = btp
			var data:Object = btp.tree.selectedItem;
			startBv(data);
		}
		
		public function startBv(data:Object):void{
			if(data != null){
				if(data["CODE"]== null || data["CODE"] == ""){
					Alert.show("业务视图为空","error");
					return;
				}
				bvCode = data["CODE"];
				moduleName = data["STREAMMODULENAME"];
				dependModules.removeAll();
				startOperat();
				remoteObj.startBusinessView(moduleName);
			}else
				Alert.show("记录为空！","warning");
		}
	}
}