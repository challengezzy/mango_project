package smartx.bam.flex.modules.datatask
{
	import mx.collections.ArrayCollection;
	import mx.containers.Form;
	import mx.containers.FormItem;
	import mx.controls.TextInput;
	import mx.core.UIComponent;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.DataTaskParamSetWindow;
	import smartx.flex.components.util.DataTaskUtil;
	import smartx.flex.components.util.Hashtable;
	import smartx.flex.components.util.MemoryUtil;
	import smartx.flex.components.util.OperatingTipUtil;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.util.StringUtils;
	
	public class StartAndRunTaskListener implements ListButtonListener
	{
		private var bamService:RemoteObject;
		private var endpoint:String  = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		private var listPanel:BillListPanel;
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var variableControlMap:Hashtable = new Hashtable();//动态变量控件MAP
		
		private var taskContent:String;//任务内容
		private var taskId:String ;
		private var taskName:String;
		
		private var paramSetWindow:DataTaskParamSetWindow;
		
		public function StartAndRunTaskListener()
		{
			bamService = new RemoteObject(BAMConst.BAM_Service);
			bamService.endpoint = endpoint;
			
			bamService.addAndStartTask.addEventListener(ResultEvent.RESULT,addAndStartTaskHandler);
			bamService.addAndStartTask.addEventListener(FaultEvent.FAULT,function(event:FaultEvent):void{
				OperatingTipUtil.endOperat();
				SmartXMessage.show("数据任务启动失败!",SmartXMessage.MESSAGE_ERROR,event.fault.faultString);
			});
			
		}
		
		public function buttonClick(listPanel:BillListPanel):void
		{
			this.listPanel = listPanel;
			if(listPanel.getSelectedRowValues().length != 1){
				SmartXMessage.show("请选择一个任务任务模板来启动！");
				return;
			}
			var selectObj:Object = listPanel.getSelectedRowValue();
			taskId = selectObj["ID"];
			taskName = selectObj["NAME"];
			taskContent = selectObj["CONTENT"];
			
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
				
				PopUpManager.addPopUp(paramSetWindow,desktop,true);
				PopUpManager.centerPopUp(paramSetWindow);
				paramSetWindow.controlBox.addChild( initVariableControl(params) );
				
			}else{
				//无变量直接执行
				bamService.addAndStartTask(taskId,taskName,taskContent,null);
			}
			
			MemoryUtil.forceGC();
		}
		
		private function paramSetOK():void{
			var paramStr:String = "";
			var paramMap:Object = new Object();
			
			var keyArray:Array = variableControlMap.getKeySet().sort();//进行排序，保证paramStr的结果一致
			for each(var key:String in keyArray){
				var ui:UIComponent = variableControlMap.find(key) as UIComponent;
				var value:String = "";
				if(ui is TextInput){
					var ti:TextInput = TextInput(ui);
					value = ti.text;
				}
				paramMap[key] = value;
				//paramMap.add(key,value);
				paramStr = paramStr + key + ":" + value + ";  ";
			}
			if(paramSetWindow.isPopUp)
				PopUpManager.removePopUp(paramSetWindow);			
			
			//进行变量解析
			try{
				taskContent = DataTaskUtil.parseVariable(taskContent,paramMap);
			}catch(e:Error){
				trace(e.message);
				SmartXMessage.show("变量解析错误！",SmartXMessage.MESSAGE_ERROR,e.message);
				return;
			}
			
			bamService.addAndStartTask(taskId,taskName,taskContent,paramMap);
			
			paramSetWindow.confirmFunc = null;
			paramSetWindow = null;
			MemoryUtil.forceGC();
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
		
		private function faultHandler(event:FaultEvent):void{
			OperatingTipUtil.endOperat();
			SmartXMessage.show("获取任务内容失败！");
		}
		
		private function addAndStartTaskHandler(event:ResultEvent):void{
			OperatingTipUtil.endOperat();
			var curTaskInsId:String = event.result as String;
			
			var viewWindow:DataTaskRunMonitorWindow = new DataTaskRunMonitorWindow();
			viewWindow.curTaskInsId = curTaskInsId;
			
			PopUpManager.addPopUp(viewWindow,desktop,false);
			PopUpManager.centerPopUp(viewWindow);
			
		}
		
	}
}