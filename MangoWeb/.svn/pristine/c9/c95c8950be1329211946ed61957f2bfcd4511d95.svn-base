package smartx.bam.flex.modules.businessview.listener
{
	import com.adobe.utils.StringUtil;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.core.IFlexDisplayObject;
	import mx.events.ListEvent;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.core.BillTreePanel;
	import smartx.flex.components.styletemplate.ifc.CardButtonListener;
	import smartx.flex.components.styletemplate.ifc.TreeButtonListener;
	import smartx.flex.components.util.LoadingWindow;
	import smartx.flex.components.vo.GlobalConst;
	
	/**
	 * sky zhangzz
	 **/
	public class BvTreeButtonListener implements TreeButtonListener
	{
		protected  var remoteObj:RemoteObject;
		
		protected  var billTreePanel:BillTreePanel;
		
		public var billCardPanel:BillCardPanel;
		
		protected var bvCode:String;
		
		protected var moduleName:String;
		
		public var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		protected var cepDatasource:String = GlobalConst.CEP_DATASOURCE_PREFIX.concat(GlobalConst.DEFAULTPROVIDERNAME_CEP);
		
		protected var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var operatingWindow:LoadingWindow = new LoadingWindow();
		
		protected var smartxFormRpc:RemoteObject;
		
		protected var dependModules:ArrayCollection = new ArrayCollection();
		
		public function BvTreeButtonListener()
		{
			remoteObj = new RemoteObject(BAMConst.BAM_Service);
			remoteObj.endpoint = endpoint;
			remoteObj.startBusinessView.addEventListener(ResultEvent.RESULT,startHandler);
			remoteObj.startBusinessView.addEventListener(FaultEvent.FAULT,startFaultHandler);
			remoteObj.getTableStructByName.addEventListener(ResultEvent.RESULT,getTableStructByNameHandler);
			remoteObj.getTableStructByName.addEventListener(FaultEvent.FAULT,getTableStructByNameFaultHandler);
			remoteObj.restartProvider.addEventListener(ResultEvent.RESULT,restartProviderHandler);
			remoteObj.restartProvider.addEventListener(FaultEvent.FAULT,faultHandler);
			
			smartxFormRpc = new RemoteObject(GlobalConst.SERVICE_FORM);
			smartxFormRpc.endpoint = endpoint;
			
		}
		
		protected function getTableStructByNameHandler(event:ResultEvent):void{};
		
		protected function restartProviderHandler(event:ResultEvent):void{};
		
		protected function getTableStructByNameFaultHandler(event:FaultEvent):void{
			endOperat();
			Alert.show("错误:\n"+event.fault.faultString,"error");
		};
		
		protected function faultHandler(event:FaultEvent):void{
			endOperat();
			Alert.show("错误:\n"+event.fault.faultString,"error");
		}
		
		public function buttonClick(treePanel:BillTreePanel):void{}
		
		protected function queryBvCard():void{
			if(billTreePanel)
				billTreePanel.tree.dispatchEvent(new ListEvent(ListEvent.ITEM_CLICK));
		}
		
		//启动依赖的视图
		protected function startDependBv():void{
			dependModules.addItem(moduleName);
			remoteObj.startBusinessView(moduleName);
		}
		
		protected function startHandler(event:ResultEvent):void{
			endOperat();
			if(dependModules.contains(moduleName)){
				dependModules.removeItemAt(dependModules.length-1);
				if(dependModules.length > 0){
					moduleName = String(dependModules.getItemAt(dependModules.length -1));
					remoteObj.startBusinessView(moduleName);
				}else
					Alert.show("启动业务视图["+bvCode+"]成功！");
			}else
				Alert.show("启动业务视图["+bvCode+"]成功！");
			queryBvCard();
		}
		
		protected function startFaultHandler(event:FaultEvent):void{
			endOperat();
			var faultStr:String = event.fault.faultString;
			if(faultStr.indexOf("for uses-declaration ") > 0){
				if(dependModules.length == 0)
					dependModules.addItem(moduleName);
				moduleName = StringUtil.replace(faultStr.substring(faultStr.indexOf("for uses-declaration ")+"for uses-declaration ".length,faultStr.length),"'","");
				startDependBv();
			}else
				Alert.show("启动业务视图["+bvCode+"]失败！\n 错误："+faultStr, 'Error');
		}
		
		protected function startOperat():void{
			operatingWindow.startOper("正在执行……",desktop);
		}
		
		protected function endOperat():void{
			operatingWindow.stopOper();
		}
		
	}
}