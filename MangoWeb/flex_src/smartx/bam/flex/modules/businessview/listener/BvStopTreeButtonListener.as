package smartx.bam.flex.modules.businessview.listener
{
	import mx.controls.Alert;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.core.BillTreePanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	
	/**
	 * sky zhangzz
	 **/
	public class BvStopTreeButtonListener extends BvTreeButtonListener
	{
		
		public function BvStopTreeButtonListener()
		{
			super();
			remoteObj.stopBusinessView.addEventListener(ResultEvent.RESULT,stopHandler);
			remoteObj.stopBusinessView.addEventListener(FaultEvent.FAULT,stopFaultHandler);
		}
		
		override public function buttonClick(btp:BillTreePanel):void{
			billTreePanel = btp;
			var data:Object = btp.tree.selectedItem;
			stopBv(data);
		}
		
		public function stopBv(data:Object):void{
			if(data != null){
				if(data["CODE"]== null || data["CODE"] == ""){
					Alert.show("业务视图为空","error");
					return;
				}
				bvCode = data["CODE"];
				startOperat();
				remoteObj.stopBusinessView(bvCode);
			}else
				Alert.show("请选择一条记录！","warning");
		}
		
		private function stopHandler(event:ResultEvent):void{
			endOperat();
			Alert.show("停止业务视图["+bvCode+"]成功！");
			queryBvCard();
		}
		
		private function stopFaultHandler(event:FaultEvent):void{
			endOperat();
			Alert.show("停止业务视图["+bvCode+"]失败！\n 错误："+event.fault.faultString, 'Error');
		}
	}
}