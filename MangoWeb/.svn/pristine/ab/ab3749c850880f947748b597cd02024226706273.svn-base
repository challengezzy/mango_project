package smartx.bam.flex.modules.businessview.listener
{
	import mx.controls.Alert;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.core.BillTreePanel;
	import smartx.flex.components.vo.GlobalConst;

	/**
	 * sky zhangzz
	 **/
	public class BvRestartProviderButtonListener extends BvTreeButtonListener
	{
		public function BvRestartProviderButtonListener(){
			super();
		}
		
		override public function buttonClick(btp:BillTreePanel):void{
			startOperat();
			remoteObj.restartProvider(GlobalConst.DEFAULTPROVIDERNAME_CEP);
		}
		
		override protected function restartProviderHandler(event:ResultEvent):void{
			endOperat();
			Alert.show("重启业务视图容器成功！");
		}
		
		override protected function faultHandler(event:FaultEvent):void{
			endOperat();
			Alert.show("重启业务视图容器失败！\n 错误："+event.fault.faultString, 'Error');
		}
	}
}