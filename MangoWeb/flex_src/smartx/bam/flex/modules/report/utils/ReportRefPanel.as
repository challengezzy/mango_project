package smartx.bam.flex.modules.report.utils
{
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.controls.DataGrid;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	
	import smartx.bam.flex.utils.BAMUtil;
	import smartx.flex.components.itemcomponent.ext.RefInput;
	import smartx.flex.components.vo.SimpleRefItemVO;
	import smartx.flex.components.vo.TableDataStruct;
	
	public class ReportRefPanel extends ReportComponent
	{
		
		private var refInputField:RefInput = new RefInput();
		public var formService:RemoteObject;
		public var str_type:String;
		public var str_realsql:String;
		public var str_parentfieldname:String;
		public var str_pkfieldname:String; //
		public var str_datasourcename:String //数据源名字    
		public var str_table:String;//树表参照表SQL
		public var str_table_fk:String;//树表参照表外键
		public var loadall:String;
		private var novaRefDialog:ReportRefDialog;
		private var novaRefTreeDialog:ReportRefTreeDialog;
		
		public var returnVO:SimpleRefItemVO;
		
		private var isInited:Boolean = false;
		
		private var dataValue:Object = new Object();
		public function ReportRefPanel(labelName:String,keyName:String, endpoint:String,destination:String,params:Object,isMandatory:Boolean,fetchSQL:String="", isCondition:Boolean=false,showLabel:Boolean=true,dataValue:Object=null)
		{
			super(labelName,keyName,refInputField,params,isMandatory,fetchSQL,showLabel);
			
			this.formService = new RemoteObject(destination);
			formService.endpoint = endpoint;
			
			refInputField.refButton.addEventListener(MouseEvent.CLICK,onButtonClicked);
			//获取参照描述信息
			formService.getRefDescTypeAndSQL.addEventListener(ResultEvent.RESULT,getRefDescTypeAndSQLHandler);
			formService.getRefDescTypeAndSQL.addEventListener(FaultEvent.FAULT,faultHandler);
//			reInit(dataValue,isCondition);
			this.refresh();
			//refInputField.width = 160;
		}
		
		private function faultHandler(event:FaultEvent):void{   
			Alert.show(event.fault.faultString, 'Error');
		}
		
		//重新初始化
		public function reInit(dataValue:Object=null,isCondition:Boolean=false):void{
			var refdesc:String = BAMUtil.expression(fetchSQL,params);
			if(refdesc == null)
				return;
			formService.getRefDescTypeAndSQL(refdesc);
			
		}
		
		private function getRefDescTypeAndSQLHandler(event:ResultEvent):void{
			var str_refdefines:Array = event.result as Array;
			str_type = str_refdefines[0]; // 类型
			str_realsql = str_refdefines[1]; //
			str_parentfieldname = str_refdefines[2]; //
			str_pkfieldname = str_refdefines[3]; //
			str_table = str_refdefines[4];
			str_table_fk = str_refdefines[5];
			str_datasourcename = str_refdefines[6]; //取得数据源,暂时不支持从客户端变量取
			loadall = str_refdefines[7];
			isInited = true;
			
			dispatchEvent(new ReportEvent(ReportEvent.INIT_COMPLETE));
		}
		
		private function onButtonClicked(event:MouseEvent):void{
			if(!isInited)
				return;
			if(str_type == "TABLE"){//暂时只支持table
				novaRefDialog = new ReportRefDialog();
				novaRefDialog.title = "选择: "+labelName;
				novaRefDialog.init(this);
				//novaRefDialog.owner = this.refInputField;
				novaRefDialog.addEventListener(CloseEvent.CLOSE, onNovaRefDialogClosed);
				PopUpManager.addPopUp(novaRefDialog,this.owner,true);
				PopUpManager.centerPopUp(novaRefDialog);
				
			}
			else if(str_type == "TREE"){//20091203 开始支持tree
				novaRefTreeDialog = new ReportRefTreeDialog();
				novaRefTreeDialog.init(this);
				novaRefTreeDialog.title = "选择: "+labelName;
				//novaRefTreeDialog.owner = this.refInputField;
				novaRefTreeDialog.addEventListener(CloseEvent.CLOSE, onNovaRefTreeDialogClosed);
				PopUpManager.addPopUp(novaRefTreeDialog,this.owner,true);
				PopUpManager.centerPopUp(novaRefTreeDialog);
			}
		}
		
		private function onNovaRefDialogClosed(event:CloseEvent):void{
			if(novaRefDialog != null && novaRefDialog.closeByConfirm){
				returnVO = new SimpleRefItemVO();
				returnVO.id = novaRefDialog.refPK;
				returnVO.code = novaRefDialog.refCode;
				returnVO.name = novaRefDialog.refName;
				refInputField.refInput.text = returnVO.name;
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
					dataarray.getItemAt(dataGrid.selectedIndex)[keyName] = returnVO;
					dataGrid.invalidateList();
				}
				dispatchEvent(new ReportEvent(ReportEvent.REAL_VALUE_CHANGE));
			}
		}
		
		private function onNovaRefTreeDialogClosed(event:CloseEvent):void{
			if(novaRefTreeDialog != null && novaRefTreeDialog.closeByConfirm){
				returnVO = new SimpleRefItemVO();
				returnVO.id = novaRefTreeDialog.refPK;
				returnVO.code = novaRefTreeDialog.refCode;
				returnVO.name = novaRefTreeDialog.refName;
				refInputField.refInput.text = returnVO.name;
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
					dataarray.getItemAt(dataGrid.selectedIndex)[keyName] = returnVO;
					dataGrid.invalidateList();
				}
				dispatchEvent(new ReportEvent(ReportEvent.REAL_VALUE_CHANGE));
			}
		}
		
		public override function clearContent(event:MouseEvent):void{
			refInputField.refInput.text = null;
			returnVO = null;
			dispatchEvent(new ReportEvent(ReportEvent.REAL_VALUE_CHANGE));
		}
		
		public override function get stringValue():String{
			if(returnVO != null)
				return returnVO.id;
			return null;
		}
		
		public override function setValue(value:Object,showValue:Object,isDefault:Boolean=false):void{
			var tempValue:String = String(value);
			if(tempValue.indexOf("{") >= 0 )//默认值有参数且没有被替换，则不设置默认值 zzy
				return;
				
			if(returnVO == null ){
				returnVO = new SimpleRefItemVO();
			}
			
			returnVO.id = tempValue;
			returnVO.code = tempValue;
			returnVO.name = showValue as String;
			refInputField.refInput.text = returnVO.name;
			if(isDefault){
				this.params[this.keyName] = tempValue;
//				formService.getTableDataStructByDS.addEventListener(ResultEvent.RESULT,getTableDataStructByDSHandler);
//				formService.getTableDataStructByDS.addEventListener(FaultEvent.FAULT,faultHandler);
//				var sql:String = "select * from ("+ str_realsql+") where rownum <= 1000";//限制1000条
//				formService.getTableDataStructByDS(str_datasourcename,sql);
				
				dispatchEvent(new ReportEvent(ReportEvent.SET_DEFAULT_VALUE));
			}
			dispatchEvent(new ReportEvent(ReportEvent.REAL_VALUE_CHANGE));
		}
		
		private function getTableDataStructByDSHandler(event:ResultEvent):void{
			var tableDataStruct:TableDataStruct = event.result as TableDataStruct;
			var tableBody:Array = tableDataStruct.table_body;
			
			if(tableBody.length > 0){
				returnVO.id = tableBody[0][0] as String;
				returnVO.code = tableBody[0][1] as String;
				returnVO.name = tableBody[0][2] as String;
				refInputField.refInput.text = returnVO.name;
			}
		}
		
		
		
		public override function refresh(variable:String=null,value:String=null):void{
			if(!BAMUtil.isEmpty(fetchSQL)){
				this.reInit();
			}
		}
		
	}
}