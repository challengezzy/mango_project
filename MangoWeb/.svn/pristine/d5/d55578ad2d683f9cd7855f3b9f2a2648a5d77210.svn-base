package smartx.bam.flex.modules.businessview.listener
{
	import mx.controls.Alert;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.core.BillTreePanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	
	/**
	 * sky zhangzz
	 **/
	public class BvRedeplyTreeButtonListerner extends BvTreeButtonListener
	{
		private var status:String;
		
		public function BvRedeplyTreeButtonListerner()
		{
			super();
			remoteObj.redeployBusinessView.addEventListener(ResultEvent.RESULT,redeployHandler);
			remoteObj.redeployBusinessView.addEventListener(FaultEvent.FAULT,redeployFaultHandler);
		}
		
		override public function buttonClick(btp:BillTreePanel):void{
			billTreePanel = btp;
			var data:Object = billCardPanel.getDataValue();
			if(data != null){
				redeplyBv(data,btp);
			}else
				Alert.show("请选择一条记录！","warning");
		}
		
		public function redeplyBv(data:Object,btp:BillTreePanel):void{
			if(billTreePanel == null)
				billTreePanel = btp;
			if(data["CODE"]== null || data["CODE"] == ""){
				Alert.show("业务视图为空","error");
				return;
			}
			bvCode = data["CODE"];
			status = data["STATUS"].id;
			moduleName = data["STREAMMODULENAME"];
			startOperat();
			remoteObj.redeployBusinessView(data["PROVIDERNAME"],moduleName);
		}
		
		private function redeployHandler(event:ResultEvent):void{
			if(status == "1"){
				remoteObj.startBusinessView(moduleName);	
			}else{
				Alert.show("重新发布业务视图["+bvCode+"]成功！");
				endOperat();
				queryBvCard();
			}
		}
		
		private function redeployFaultHandler(event:FaultEvent):void{
			endOperat();
			Alert.show("重新发布业务视图["+bvCode+"]失败！\n 错误："+event.fault.faultString, 'Error');
		}
	}
}