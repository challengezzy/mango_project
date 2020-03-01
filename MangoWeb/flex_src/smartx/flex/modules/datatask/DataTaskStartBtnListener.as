package smartx.flex.modules.datatask
{
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.containers.Form;
	import mx.containers.FormItem;
	import mx.controls.Label;
	import mx.controls.TextInput;
	import mx.core.UIComponent;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.ifc.CardButtonListener;
	import smartx.flex.components.util.DataTaskParamSetWindow;
	import smartx.flex.components.util.DataTaskUtil;
	import smartx.flex.components.util.Hashtable;
	import smartx.flex.components.util.MemoryUtil;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.util.StringUtils;
	import smartx.flex.components.vo.GlobalConst;

	/**
	 * @author zzy
	 * @date Aug 31, 2011
	 */
	public class DataTaskStartBtnListener implements CardButtonListener
	{
		private var thisCardPanel:BillCardPanel;
		private var cardValue:Object;
		private var smartxService:RemoteObject;
		
		private var paramSetWindow:DataTaskParamSetWindow;
		
		private var variableControlMap:Hashtable = new Hashtable();//动态变量控件MAP
		
		private var paramMap:Hashtable = new Hashtable();
		
		private var taskContent:String;//任务内容
		
		public function DataTaskStartBtnListener()
		{
			smartxService = new RemoteObject(GlobalConst.SERVICE_FORM);
			smartxService.endpoint = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
			
			smartxService.addDataTask.addEventListener(ResultEvent.RESULT,function(event:ResultEvent):void{
				SmartXMessage.show("数据预处理任务【" + cardValue["NAME"] +"】启动成功！");
			});
			
			smartxService.addDataTask.addEventListener(FaultEvent.FAULT,function(event:FaultEvent):void{
				SmartXMessage.show("数据预处理任务启动失败！",SmartXMessage.MESSAGE_ERROR,event.fault.faultString);
			});
		}
		
		public function buttonClick(cardPanel:BillCardPanel):void
		{
			thisCardPanel = cardPanel;
			cardValue = thisCardPanel.getDataValue();			
			taskContent = cardValue["CONTENT"];
			if(taskContent == null || taskContent == ""){
				SmartXMessage.show("数据任务内容为空，不能执行！",SmartXMessage.MESSAGE_ERROR);
				return;
			}
			
			var params:ArrayCollection;
			try{
				params = DataTaskUtil.paramExract(taskContent);
			}catch(e:Error){
				trace(e.message);
				SmartXMessage.show("数据预处理任务变量设置错误",SmartXMessage.MESSAGE_ERROR,e.message);
				return;
			}
			
			if(params.length > 0){
				paramSetWindow = new DataTaskParamSetWindow();
				paramSetWindow.confirmFunc = paramSetOK;
				
				PopUpManager.addPopUp(paramSetWindow,cardPanel,true);
				PopUpManager.centerPopUp(paramSetWindow);
				paramSetWindow.controlBox.addChild( initVariableControl(params) );
					
			}else{
				//无变量直接执行
				var taskTitle:String = cardValue["NAME"] + "_" + StringUtils.hashCode(cardValue["CODE"] + "");
				smartxService.addDataTask(cardValue["ID"],taskTitle,taskContent,"");
			}
			
		}
		
		private function paramSetOK():void{
			var paramStr:String = "";
			paramMap.clear();
			for each(var key:String in variableControlMap.getKeySet()){
				var ui:UIComponent = variableControlMap.find(key) as UIComponent;
				var value:String = "";
				if(ui is TextInput){
					var ti:TextInput = TextInput(ui);
					value = ti.text;
				}
				paramMap.add(key,value);
				paramStr = paramStr + key + ":" + value + ";  ";
			}
			if(paramSetWindow.isPopUp)
				PopUpManager.removePopUp(paramSetWindow);			
			paramSetWindow.confirmFunc = null;
			paramSetWindow = null;
			MemoryUtil.forceGC();
			
			//进行变量解析
			try{
				taskContent = DataTaskUtil.parseVariable(taskContent,paramMap);
			}catch(e:Error){
				trace(e.message);
				SmartXMessage.show("变量解析错误！",SmartXMessage.MESSAGE_ERROR,e.message);
				return;
			}
			
			var taskTitle:String = cardValue["NAME"] + "_" + StringUtils.hashCode(cardValue["CODE"] + paramStr);			
			smartxService.addDataTask(cardValue["ID"],taskTitle,taskContent,paramStr);
			
		}
		
		private function initVariableControl(params:ArrayCollection):UIComponent{
			variableControlMap.clear();
			var controlForm:Form = new Form();
			controlForm.percentHeight = 100;
			controlForm.percentWidth = 100;
			for each(var paramKey:String in params){
				var item:FormItem = new FormItem();
				item.required = true;
				item.label = paramKey;
					
				var textInput:TextInput = new TextInput();
				//此处设置默认值，从环境变量读取
				var clientValue:String = ClientEnviorment.getInstance().getVar(paramKey) as String;
				textInput.text = clientValue;
				textInput.width = 100;
				item.addChild(textInput);
				
				variableControlMap.add(paramKey,textInput);
				controlForm.addChild(item);
			}
			return controlForm;
		}
	}
}