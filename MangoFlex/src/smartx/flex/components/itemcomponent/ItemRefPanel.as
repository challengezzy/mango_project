package smartx.flex.components.itemcomponent
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.containers.TitleWindow;
	import mx.controls.Alert;
	import mx.controls.DataGrid;
	import mx.core.Application;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	import mx.utils.ObjectUtil;
	
	import qs.utils.StringUtils;
	
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.event.StyleTemplateEvent;
	import smartx.flex.components.event.UIComponentEvent;
	import smartx.flex.components.itemcomponent.ext.MTCardForRefPanel;
	import smartx.flex.components.itemcomponent.ext.RefDialog;
	import smartx.flex.components.itemcomponent.ext.RefInput;
	import smartx.flex.components.itemcomponent.ext.RefMultiDialog;
	import smartx.flex.components.itemcomponent.ext.RefTreeDialog;
	import smartx.flex.components.styletemplate.MTStyleTemplate02;
	import smartx.flex.components.styletemplate.StyleTemplate02;
	import smartx.flex.components.util.CompareUtil;
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.util.StringUtils;
	import smartx.flex.components.util.script.ScriptEvent;
	import smartx.flex.components.util.script.ScriptExecutor;
	import smartx.flex.components.util.script.ScriptExecutorFactory;
	import smartx.flex.components.vo.ItemVO;
	import smartx.flex.components.vo.MetadataTemplet;
	import smartx.flex.components.vo.SimpleRefItemVO;
	import smartx.flex.components.vo.TempletItemVO;

	//参照框，暂不支持任何客户端变量转换{}
	public class ItemRefPanel extends ItemUIComponent
	{
		public static const KEY_BUTTONOFNEW:String = "buttonOfNew";
		public static const KEY_MTOFNEW:String = "MTOfNew";
		public static const KEY_IDOFNEW:String = "IDOfNew";
		public static const KEY_NAMEOFNEW:String = "NameOfNew";
		
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
	    private var novaRefDialog:RefDialog;
	    private var novaMultiRefDialog:RefMultiDialog;
	    private var novaRefTreeDialog:RefTreeDialog;
		
		//add by caohenghui for SMARTX-73 --start
		private var temp_str_realsql:String;
		//add by caohenghui for SMARTX-73 --end
	    
	    public var returnVO:SimpleRefItemVO;
		
		public var returnVOArr:ArrayCollection=null;
        
	    private var isInited:Boolean = false;
	    
	    private var dataValue:Object = new Object();
		
		public var parameterMap:Object = new Object();
		
		private var isCondition:Boolean=false;
		
		private var refdesc:String;
		
		public var buttonOfNew:Boolean = false;
		public var mtOfNew:String;
		public var idOfNew:String="ID";
		public var nameOfNew:String="NAME";
		public var condition_itemtype:String="";
		private var newWindow:TitleWindow = new TitleWindow();
		
		public function ItemRefPanel(templetItemVO:TempletItemVO, destination:String, endpoint:String=null, isCondition:Boolean=false,showLabel:Boolean=true,dataValue:Object=null)
		{
			super(templetItemVO, refInputField, showLabel);
			this.isCondition=isCondition;
			condition_itemtype=templetItemVO.conditionItemType;
			if(destination == null)
				throw new Error("参数destination不能为null");
			this.formService = new RemoteObject(destination);
			if(endpoint!=null)
				formService.endpoint = endpoint;
			refInputField.refButton.addEventListener(MouseEvent.CLICK,onButtonClicked);
			refInputField.newButton.addEventListener(MouseEvent.CLICK,onNewButtonClicked);
			//获取参照描述信息
			formService.getRefDescTypeAndSQL.addEventListener(ResultEvent.RESULT,getRefDescTypeAndSQLHandler);
			formService.getRefDescTypeAndSQL.addEventListener(FaultEvent.FAULT,faultHandler);
			formService.getRefDescExtendParameterMap.addEventListener(ResultEvent.RESULT,getRefDescExtendParameterMapHandler);
			formService.getRefDescExtendParameterMap.addEventListener(FaultEvent.FAULT,faultHandler);
			formService.generateRefItemVO.addEventListener(ResultEvent.RESULT,generateRefItemVOHandler);
			formService.generateRefItemVO.addEventListener(FaultEvent.FAULT,faultHandler);
			reInit(dataValue,isCondition);
			//refInputField.width = 160;
		}
		
		private function faultHandler(event:FaultEvent):void{   
			Alert.show(event.fault.faultString, 'Error');
		}
		
		//重新初始化
		public function reInit(dataValue:Object=null,isCondition:Boolean=false,newrefdesc:String=null):void{
			isInited = false;
			if(dataValue != null)
				this.dataValue = dataValue;
			refdesc=null;
			if(newrefdesc!=null)
				refdesc=newrefdesc;
			if(refdesc==null && isCondition)
				refdesc = templetItemVO.conditionRefDesc;
			if(refdesc == null){
				if(templetItemVO.clientrefdesc != null && templetItemVO.clientrefdesc != ""){
					refdesc = templetItemVO.clientrefdesc;
					//只解析参照客户端sql
		            var executor:ScriptExecutor = ScriptExecutorFactory.createNewExecutor();
				    executor.registorVarMap(dataValue);//将当前记录的字段值都作为脚本执行器的环境变量
				    //这里有个小trick，如果脚本中已还有输出语句，则认为是用户自己控制输出，否则认为这个公式只是一个表达式，我们帮他在外面套一层输出
				    if(refdesc.indexOf("sysOutput")>=0){
				    	executor.scriptText = refdesc;
				    	executor.compile();
				    }
				    else{
				    	executor.scriptText = "sysOutput("+refdesc+")";
				    	try{
				    		executor.compile();
				    	}
				    	catch(error:Error){
				    		//如果编译通不过，加上引号试试
				    		executor.scriptText = "sysOutput(\""+refdesc+"\")";
				    		executor.compile();
				    	}
				    }
				    executor.addEventListener(ScriptEvent.OUTPUT_VALUE,function(event:ScriptEvent):void{
				        var returnValue:String = String(event.outputValue);
				        refdesc = returnValue;
				        formService.getRefDescTypeAndSQL(refdesc);
				    });
				    executor.execute();
				    return;
				}
				else
					refdesc = templetItemVO.refdesc;
			}
			if(refdesc == null)
				return;
			
			formService.getRefDescTypeAndSQL(refdesc);
			
		}
		
		private function getRefDescTypeAndSQLHandler(event:ResultEvent):void{
			var str_refdefines:Array = event.result as Array;
			str_type = str_refdefines[0]; // 类型
            str_realsql = str_refdefines[1]; //
			temp_str_realsql = str_refdefines[1];
            str_parentfieldname = str_refdefines[2]; //
            str_pkfieldname = str_refdefines[3]; //
            str_table = str_refdefines[4];
            str_table_fk = str_refdefines[5];
            str_datasourcename = str_refdefines[6]; //取得数据源,暂时不支持从客户端变量取
            loadall = str_refdefines[7];
			formService.getRefDescExtendParameterMap(refdesc);
		}
		
		private function getRefDescExtendParameterMapHandler(event:ResultEvent):void{
			parameterMap = event.result as Object;
			var buttonOfNewStr:String = parameterMap[KEY_BUTTONOFNEW];
			if(buttonOfNewStr == "true"){
				buttonOfNew = true;
				refInputField.buttonOfNew = true;
			}
			mtOfNew = parameterMap[KEY_MTOFNEW];
			if(parameterMap[KEY_IDOFNEW] != null)
				idOfNew = parameterMap[KEY_IDOFNEW];
			if(parameterMap[KEY_NAMEOFNEW] != null)
				nameOfNew = parameterMap[KEY_NAMEOFNEW];
			isInited = true;
			dispatchEvent(new Event("initComplete"));
		}
		
		private function onButtonClicked(event:MouseEvent):void{
			if(!isInited)
				return;
			
			var condition_str:String = getUnionCondition();
			if(condition_str != null && condition_str != ""){
				this.str_realsql = "select * from ("+str_realsql+") where "+condition_str;
			}
			
			if(str_type == "TABLE"){//暂时只支持table
				if(condition_itemtype=="多选参照" &&isCondition){
					novaMultiRefDialog = new RefMultiDialog();
					novaMultiRefDialog.title = "选择: "+templetItemVO.itemname;
					novaMultiRefDialog.init(this);
					//novaRefDialog.owner = this.refInputField;
					novaMultiRefDialog.addEventListener(CloseEvent.CLOSE,onNovaMultiRefDialogClosed);
					PopUpManager.addPopUp(novaMultiRefDialog,this.owner.root,true);
					PopUpManager.centerPopUp(novaMultiRefDialog);
				}else{
					novaRefDialog = new RefDialog();
					novaRefDialog.title = "选择: "+templetItemVO.itemname;
					novaRefDialog.init(this);
					//novaRefDialog.owner = this.refInputField;
					novaRefDialog.addEventListener(CloseEvent.CLOSE,onNovaRefDialogClosed);
					PopUpManager.addPopUp(novaRefDialog,this.owner.root,true);
					PopUpManager.centerPopUp(novaRefDialog);
				}
			}else if(str_type == "TREE"){//20091203 开始支持tree
				novaRefTreeDialog = new RefTreeDialog();
				novaRefTreeDialog.init(this);
				novaRefTreeDialog.title = "选择: "+templetItemVO.itemname;
				//novaRefTreeDialog.owner = this.refInputField;
				novaRefTreeDialog.addEventListener(CloseEvent.CLOSE, onNovaRefTreeDialogClosed);
				PopUpManager.addPopUp(novaRefTreeDialog,this.owner.root,true);
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
					dataarray.getItemAt(dataGrid.selectedIndex)[templetItemVO.itemkey] = returnVO;
					dataGrid.invalidateList();
				}
				dispatchEvent(new UIComponentEvent(UIComponentEvent.REAL_VALUE_CHANGE));
			}
			this.str_realsql = this.temp_str_realsql;
		}
		
		private function onNovaMultiRefDialogClosed(event:CloseEvent):void{
			if(novaMultiRefDialog != null && novaMultiRefDialog.closeByConfirm&&novaMultiRefDialog.seletedItemsArray.length>0){
				refInputField.refInput.text="";
				returnVOArr=new ArrayCollection();
				var tempStr:String="";
				for each(var item:Object in novaMultiRefDialog.seletedItemsArray){
					var arr:Array=item as Array;
					returnVO = new SimpleRefItemVO();
					returnVO.id = item.id;
					returnVO.code = item.code;
					returnVO.name = item.name;
					returnVOArr.addItem(returnVO);
					tempStr += returnVO.name+",";
				}
				refInputField.refInput.text=tempStr.substring(0,tempStr.length-1);
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
					for each(var tempItem:Object in novaMultiRefDialog.seletedItemsArray){
						returnVO = new SimpleRefItemVO();
						returnVO.id = tempItem.id;
						returnVO.code = tempItem.code;
						returnVO.name = tempItem.name;
						dataarray.getItemAt(dataGrid.selectedIndex)[templetItemVO.itemkey] = returnVO;
					}
					
					dataGrid.invalidateList();
				}
				dispatchEvent(new UIComponentEvent(UIComponentEvent.REAL_VALUE_CHANGE));
			}
			this.str_realsql = this.temp_str_realsql;
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
					dataarray.getItemAt(dataGrid.selectedIndex)[templetItemVO.itemkey] = returnVO;
					dataGrid.invalidateList();
				}
				dispatchEvent(new UIComponentEvent(UIComponentEvent.REAL_VALUE_CHANGE));
			}
			this.str_realsql = this.temp_str_realsql;
		}
		
		protected override function clearContent(event:MouseEvent):void{
			refInputField.refInput.text = null;
			returnVO = null;
			returnVOArr=null;
			dispatchEvent(new UIComponentEvent(UIComponentEvent.REAL_VALUE_CHANGE));
		}
		
		public override function getQueryConditon(isPreciseQuery:Boolean=false):String{
			if(returnVOArr!=null&&returnVOArr.length>0){
//				var tempVO:SimpleRefItemVO=returnVOArr[0] as SimpleRefItemVO;
				var  tempStr:String=" and ("+templetItemVO.itemkey+"=ANY(";
				for each(var temp:Object in returnVOArr){
					var tempIV:SimpleRefItemVO=temp as SimpleRefItemVO;
					tempStr += "'"+tempIV.id+"',";
				}
				tempStr=tempStr.substr(0,tempStr.length-1);
				tempStr += "))";
				return tempStr;
			}else if(returnVO != null)
				return " and ("+templetItemVO.itemkey+" = '"+returnVO.id+"') ";
			return "";
		}

		public override function get realValue():Object{
			return returnVO;
		}
		
		public override function get stringValue():String{
			if(returnVO != null)
				return returnVO.id;
			return null;
		}
   		 // Define the getter method.
    	public override function set data(value:Object):void {
        	super.data = value;
        	if(value == null)
        		return;
        	returnVO = value[templetItemVO.itemkey];
        	if(returnVO != null)
        		refInputField.refInput.text = value[templetItemVO.itemkey].toString();
        	else 
				refInputField.refInput.text = null;
        }
		
		public function setValueByItemId(itemId:String):void{
			if(!isInited)
				throw new Error("不能在未初始化完成的参照上设置值");
			formService.generateRefItemVO(refdesc,itemId);
			
		}
		
		public function generateRefItemVOHandler(event:ResultEvent):void{
			var vo:SimpleRefItemVO = event.result as SimpleRefItemVO;
			if(vo != null){
				returnVO = vo;
				refInputField.refInput.text = vo.toString();
				dispatchEvent(new UIComponentEvent(UIComponentEvent.REAL_VALUE_CHANGE));
			}
			
		}
		
		public override function set editable(editable:Boolean):void{
			this._editable = editable;
			//clearButton.setVisible(_editable);
			refInputField.refButton.enabled = _editable;
		}
		
		public override function getSortFunction(fieldName:String):Function{
			return function(obj1:Object,obj2:Object):int{
				if(obj1[fieldName] != null && obj2[fieldName] == null)
					return -1;
				if(obj1[fieldName] == null && obj2[fieldName] != null)
					return 1;
				if(obj1[fieldName] is ItemVO && obj2[fieldName] is ItemVO){
					if(CompareUtil.hashTotalColumn(obj1)){
						if(column){
							return (column.sortDescending?-1:1)*1;
						}else{
							return 1;
						}
					}else if(CompareUtil.hashTotalColumn(obj2)){
						if(column){
							return (column.sortDescending?-1:1)*-1;
						}else{
							return -1;
						}
					}else{
						return ObjectUtil.stringCompare(CompareUtil.getFirstPinYin(String(ItemVO(obj1[fieldName]))),CompareUtil.getFirstPinYin(String(ItemVO(obj2[fieldName]))),true);
					}
				}
				return 0;
			}
		}
		
		private function onNewButtonClicked(event:MouseEvent):void{
			if(!isInited)
				return;
			if(mtOfNew == null)
				return;
			var mt:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet(mtOfNew);
			if(mt == null)
				return;
			var panel:MTCardForRefPanel = new MTCardForRefPanel();
			panel.endpoint = this.formService.endpoint;
			panel.metadataTemplet = mt;
			panel.percentHeight = 100;
			panel.percentWidth = 100;
			newWindow = new TitleWindow();
			newWindow.width = 800;
			newWindow.height = 450;
			newWindow.showCloseButton = true;
			newWindow.addEventListener(CloseEvent.CLOSE,function(e:CloseEvent):void{
				PopUpManager.removePopUp(newWindow);
			});
			newWindow.addChild(panel);
			PopUpManager.addPopUp(newWindow,this.root,true);
			PopUpManager.centerPopUp(newWindow);
			panel.addEventListener("cancel",function(e:Event):void{
					PopUpManager.removePopUp(newWindow);
				});
		
			panel.addEventListener("confirm",function(e:Event):void{
					PopUpManager.removePopUp(newWindow);
					var cardPanel:MTCardForRefPanel = e.target as MTCardForRefPanel;
					if(cardPanel == null)
						return;
					returnVO = new SimpleRefItemVO();
					var dataValue:* = cardPanel.getDataValue();
					returnVO.id = dataValue[idOfNew];
					returnVO.code = dataValue[nameOfNew];
					returnVO.name = dataValue[nameOfNew];
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
						dataarray.getItemAt(dataGrid.selectedIndex)[templetItemVO.itemkey] = returnVO;
						dataGrid.invalidateList();
					}
					dispatchEvent(new UIComponentEvent(UIComponentEvent.REAL_VALUE_CHANGE));
				});
		}
		
		private function getUnionCondition():String{
			var condition_str:String = "";
			var unionCondition_Str:String = templetItemVO.extattr05;
			if(unionCondition_Str != null && unionCondition_Str != ""){
				if(controlsArray != null && controlsArray.length>0){
					var tempCol:ArrayCollection = new ArrayCollection();
					for each(var itemComponent:ItemUIComponent in controlsArray){
						var key:String = itemComponent.templetItemVO.itemkey;
						tempCol.addItem("{"+key+"}");
						var value:String = itemComponent.stringValue;
						if(value != null && value != ""){
							unionCondition_Str = qs.utils.StringUtils.replaceAll(unionCondition_Str,"{"+key+"}",value);
						}
					}
					var conditionArray:Array = qs.utils.StringUtils.replaceAll(unionCondition_Str," and "," AND ").split(" AND ");
					if(conditionArray != null && conditionArray.length>0){
						
						for each(var temp_str:String in conditionArray){
							if(!isContain(tempCol,temp_str)){
								if(condition_str == "" ){
									condition_str = temp_str;
								}else{
									condition_str += " AND "+temp_str;
								}
							}
						}
					}
				}
			}
			return condition_str;
		}
		
		private function isContain(col:ArrayCollection,str:String):Boolean{
			var flag:Boolean = false;
			if(col != null && str != null && str != ""){
				for each(var temp_str:String in col){
					if(str.indexOf(temp_str)>=0){
						flag = true;
						break;
					}
				}
			}
			return flag;
		}
		
	}
}