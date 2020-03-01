package smartx.flex.components.itemcomponent
{
	
	import com.adobe.flex.extras.controls.AutoComplete;
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.controls.DataGrid;
	import mx.core.UIComponent;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.flex.components.event.UIComponentEvent;
	import smartx.flex.components.util.StringUtils;
	import smartx.flex.components.util.TempletDataUtil;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.SimpleHashVO;
	import smartx.flex.components.vo.TempletItemVO;

	public class ItemAutoCompleteField extends ItemUIComponent
	{
		private var textInput:AutoComplete = new AutoComplete();
		private var searching:Boolean = false;
		private var formService:RemoteObject;
		
		public function ItemAutoCompleteField(templetItemVO:TempletItemVO, endpoint:String=null,showLabel:Boolean=true)
		{
			super(templetItemVO, textInput, showLabel);
			formService = new RemoteObject(GlobalConst.SERVICE_FORM);
			if(endpoint != null)
				formService.endpoint = endpoint;
			formService.getSimpleHashVoArrayByDS.addEventListener(ResultEvent.RESULT,getSimpleHashVoArrayByDSHandler);
			formService.getSimpleHashVoArrayByDS.addEventListener(FaultEvent.FAULT,faultHandler);
			textInput.setStyle("borderStyle","solid");
			textInput.addEventListener("typedTextChange",dataChange);
			textInput.labelField = "name";
			textInput.dataProvider = new ArrayCollection();
			//textInput.prompt = templetItemVO.
		}
		
		private function faultHandler(event:FaultEvent):void{   
			Alert.show(event.fault.faultString, 'Error');
		}
		
		public override function getQueryConditon(isPreciseQuery:Boolean=false):String{
			if(textInput.text != null && textInput.text != ""){
				if(isPreciseQuery)
					return " and ("+templetItemVO.itemkey+" = '"+TempletDataUtil.convertSQLValue(textInput.text)+"') ";
				else
					return " and ("+templetItemVO.itemkey+" like '%"+TempletDataUtil.convertSQLValue(textInput.text)+"%') ";
			}
			return "";
		}
		
		public override function get realValue():Object{
			if(textInput.text != "")
				return textInput.text;
			return null;
		}
		
		public override function get stringValue():String{
			return textInput.text;
		}
		
		protected override function clearContent(event:MouseEvent):void{
			textInput.text = null;
			dataChange(null);
		}
		
		public override function set editable(editable:Boolean):void{
			this._editable = editable;
			textInput.enabled =_editable;
		}
		
		public override function set data(value:Object):void {
			super.data = value;
			if(value!=null)
				textInput.text = value[templetItemVO.itemkey];
			else
				textInput.text = null;
		}
		
		private function dataChange(event:Event):void{
			/*
			todo by xuzhilin
			以下是为了满足billlistpanel的列表编辑功能所采取的恶心写法
			由于参照采用popup弹出窗口的方式进行数据编辑，在popup窗口弹出后，会失去对本Panel的焦点，从而触发datagrid的itemeditend事件。
			那么无论在popup中选择了什么数据，都无法自动传回datagird。（因为datagird只在itemeditend时调用getRealValue）
			该问题从目前网上的资料看，无解。
			以下代码是一种变通，即如果父窗体是datagrid的话，popup确认后主动去更新datagrid的dataprovider。
			这显然是不合理的写法，儿子还能去操作老爹的数据？认错老爹就更惨了！（是datagrid就是亲爹？）
			*/
			if(owner is DataGrid){
				var dataGrid:DataGrid = owner as DataGrid;
				var dataarray:ArrayCollection = dataGrid.dataProvider as ArrayCollection;
				dataarray.getItemAt(dataGrid.selectedIndex)[templetItemVO.itemkey] = textInput.text;
				dataGrid.invalidateList();
			}
			dispatchEvent(new UIComponentEvent(UIComponentEvent.REAL_VALUE_CHANGE));
			refreshAutoCompleteDataProvider();
		}
		
		private function refreshAutoCompleteDataProvider():void{
			if(searching)//已经在搜索的话，直接返回
				return;
			if(textInput.text == null || textInput.text == "" 
				|| textInput.text.length == 0)//还没键入任何字符，也直接跳过
				return;
			var sql:String = "select * from (select distinct "+templetItemVO.itemkey+" name from "+templetItemVO.pub_Templet_1VO.tablename
			+" where "+templetItemVO.itemkey+" like '"+TempletDataUtil.convertSQLValue(textInput.typedText)+"%') where rownum<=100";
			formService.getSimpleHashVoArrayByDS(templetItemVO.pub_Templet_1VO.datasourcename,sql);
			searching = true;
		}
		
		private function getSimpleHashVoArrayByDSHandler(event:ResultEvent):void{
			var result:Array = event.result as Array;
			if(result != null){
				var newDataProvider:ArrayCollection = new ArrayCollection();
				for each(var vo:SimpleHashVO in result){
					var obj:Object = new Object();
					obj.name = vo.dataMap["name"];
					newDataProvider.addItem(obj);
				}
				var dp:ArrayCollection = textInput.dataProvider as ArrayCollection;
				dp.removeAll();
				dp.addAll(newDataProvider);
				textInput.typedText = textInput.typedText;
			}
			
			searching = false;
		}
	}
}