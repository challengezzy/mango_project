package smartx.bam.flex.modules.dqc.listener
{
	import flash.display.DisplayObject;
	
	import mx.collections.ArrayCollection;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.bam.flex.modules.common.variableControl.vo.VariableVo;
	import smartx.bam.flex.modules.dqc.EntityRuleCheckPanel;
	import smartx.bam.flex.utils.BAMUtil;
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillListItemPanel;
	import smartx.flex.components.core.mtchart.MTMultiSeriesChartPanel;
	import smartx.flex.components.core.mtchart.event.MTChartEvent;
	import smartx.flex.components.styletemplate.ifc.ListItemButtonListenerIFC;
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.util.OperatingTipUtil;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.MetadataTemplet;
	import smartx.flex.components.vo.SimpleHashVO;
	
	public class GroupEntityRuleClickListener implements ListItemButtonListenerIFC
	{
		
		private var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT)) as String;
		
		private var debugMode:Boolean =  ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_GLOBAL_DEBUGMODE) as Boolean;
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var formService:RemoteObject;
		private var bamService:RemoteObject;
		
		private var chart:MTMultiSeriesChartPanel;
		
		private var listItemPanel:BillListItemPanel;
		
		[Bindable]
		private var ruleAnalyseCol:ArrayCollection = new ArrayCollection();
		
		private var taskName:String;
		
		public function GroupEntityRuleClickListener()
		{
			
			formService = new RemoteObject(GlobalConst.SERVICE_FORM);
			formService.endpoint = endpoint;
			
			bamService = new RemoteObject(BAMConst.BAM_Service);
			bamService.endpoint = endpoint;
			
			bamService.analyseEntityByBatchNo.addEventListener(ResultEvent.RESULT,analyseEntityByBatchNoHandler);
			bamService.analyseEntityByBatchNo.addEventListener(FaultEvent.FAULT,faultHander);
			
			formService.getSimpleHashVoArrayByDS.addEventListener(ResultEvent.RESULT,getSimpleHashVoArrayByDSHandler);
			formService.getSimpleHashVoArrayByDS.addEventListener(FaultEvent.FAULT,function(event:FaultEvent):void{
				OperatingTipUtil.endOperat();
				SmartXMessage.show("查询数据时出错!",SmartXMessage.MESSAGE_ERROR,event.fault.faultString);
			});
			
		}
		
		private function faultHander(event:FaultEvent):void{
			OperatingTipUtil.endOperat();
			SmartXMessage.show("获取数据出错",SmartXMessage.MESSAGE_ERROR,event.fault.faultDetail);
		}
		
		public function handler(dataValue:Object, billListItemPanel:BillListItemPanel):void
		{
			this.listItemPanel = billListItemPanel;
			var batchNo:String = dataValue["BATCHNO"];
			taskName = dataValue["TASKNAME"];
			
			formService.getSimpleHashVoArrayByDS(listItemPanel.datasourceName,"select * from dq_entity_statistics t where t.batchno='"+batchNo+"' and t.taskname='"+taskName+"'");
		}
		
		private function getSimpleHashVoArrayByDSHandler(event:ResultEvent):void{
			OperatingTipUtil.endOperat();
			var result:Array = event.result as Array;
			var rulesInfoArr:Array = new Array();
			var flag:Boolean =  true;
			for each(var obj:Object in result){
				
				var shv:SimpleHashVO = obj as SimpleHashVO;
				
				var item:Object = shv.dataMap;
				
				var ruleCode:String = item["rulecode"];
				var entityCode:String = item["entitycode"];
				var modelCode:String = item["entitymodelcode"];
				var batchNo:String = item["batchno"];
				
				var entityMtCode:String = BAMConst.ENTITY_MT_PREFIX+modelCode+"_"+entityCode;
				var entityTemplete:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet(entityMtCode);
				var paramsMap:Object = new Object();
				if(entityTemplete!=null){
					var tempEnityXml:XML = entityTemplete.contentXML;
					paramsMap = this.getParamsMap(tempEnityXml);
				}
				
				var ruleMtCode:String = BAMConst.ENTITY_MT_RULE_PREFEX+ruleCode;
				var ruleTemplete:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet(ruleMtCode);
				var tempRuleXml:XML;
				if(ruleTemplete!=null){
					tempRuleXml = ruleTemplete.contentXML;
				}
				var groupcolumn:String = String(tempRuleXml.@groupcolumn);
				var groupcolumnValue:String = paramsMap[groupcolumn];
				
				if(groupcolumnValue == VariableVo.IGNORE_VALUE  || groupcolumnValue == null || groupcolumnValue == "" || groupcolumnValue == VariableVo.IGNORE_VALUE|| groupcolumnValue == "ALL" || groupcolumnValue == "全部"|| groupcolumnValue == "-1"){
					
					var ruleObj:Object = new Object();
					ruleObj["ruleCode"] = ruleCode;
					ruleObj["entityCode"] = entityCode;
					ruleObj["modelCode"] = modelCode;
					ruleObj["batchNo"] = batchNo;
					ruleObj["taskName"] = taskName;
					
					rulesInfoArr.push(ruleObj);
				}else{
					flag = false;
					break;
				}
				
			}
			if(flag && rulesInfoArr.length>0){
				bamService.analyseEntityByBatchNo(rulesInfoArr);
			}else{
				SmartXMessage.show("无权限查看:分组总览情况!");
			}
			
		}
		
		private function analyseEntityByBatchNoHandler(event:ResultEvent):void{
			
			OperatingTipUtil.endOperat();
			
			var resultObj:Object  = event.result;
			var resultArr:Array = resultObj["SIMPLEHASHVOARRAY"];
			var dataProvider:ArrayCollection = new ArrayCollection();
			dataProvider.removeAll();
			ruleAnalyseCol.removeAll();
			var pickItem:Object = null;
			var maxValue:int = 0;
			for each(var spm:SimpleHashVO in resultArr){
				var item:Object = spm.dataMap;
				var tempValue:int = parseInt(item["totalcount"]);
				if(tempValue >= maxValue){
					maxValue = tempValue;
					pickItem = item;
				}
				dataProvider.addItem(spm.dataMap);
				ruleAnalyseCol.addItem(spm.dataMap);
			}
			
			var ruleAnalyseQueryColumns:Array = new Array();
			ruleAnalyseQueryColumns.push("batchno");
			ruleAnalyseQueryColumns.push("rulename");
			ruleAnalyseQueryColumns.push("columnlabel");
			
			var ruleAnalyseColumns:Array = new Array();
			var dgColumn:DataGridColumn = new DataGridColumn();
			dgColumn.dataField = "rulename";
			dgColumn.headerText = "规则名称";
			dgColumn.width = 255;
			ruleAnalyseColumns.push(dgColumn);
			
			var dgColumn2:DataGridColumn = new DataGridColumn();
			dgColumn2.dataField = "batchno";
			dgColumn2.headerText = "批次号";
			dgColumn2.width = 255;
			ruleAnalyseColumns.push(dgColumn2);
			
			var dgColumn4:DataGridColumn = new DataGridColumn();
			dgColumn4.dataField = "columnlabel";
			dgColumn4.headerText = "分组字段值";
			dgColumn4.width = 255;
			ruleAnalyseColumns.push(dgColumn4);
			
			var dgColumn3:DataGridColumn = new DataGridColumn();
			dgColumn3.dataField = "rowcount";
			dgColumn3.headerText = "数量";
			dgColumn3.width = 255;
			ruleAnalyseColumns.push(dgColumn3);
			
			var entityRuleCheckFrame:EntityRuleCheckPanel = this.getEntityRuleCheckPanel(listItemPanel);
			
			entityRuleCheckFrame.ruleAnalyseList.queryColumns = ruleAnalyseQueryColumns;
			entityRuleCheckFrame.ruleAnalyseList.columns=ruleAnalyseColumns;
			entityRuleCheckFrame.ruleAnalyseList.dataArray = ruleAnalyseCol;
			
			entityRuleCheckFrame.ruleAnalyseList.refresh();
			
			var contentXml:XML = <chart>
								  <charttype>MSColumn3D</charttype>
								  <rowsLimit/>
								  <datasource type="rdms" sql=""></datasource>
								  <chartname/>
								  <refreshinterval/>
								  <orderby/>
								  <filter/>
								  <bindSynonymses/>
								  <extend>
									<isShowLabel>true</isShowLabel>
									<xaxianame></xaxianame>
									<pyaxianame>记录数</pyaxianame>
									<syaxianame/>
									<subseriesnames></subseriesnames>
									<datamode>rowMode</datamode>
									<items>
									  <item dataField="rowcount" labelField="columnlabel" seriesNameField="" color="ffff00" linkField="info" nameField="rulename" type="Column"/>
									</items>
								  </extend>
								</chart>;
			
			contentXml.datasource = null;
			
			var templet:MetadataTemplet = new MetadataTemplet();
			templet.contentXML = contentXml;
			
			chart = new  MTMultiSeriesChartPanel();
			chart.isShowRightClickMenu = false;
			chart.chartId = "";
			chart.metadataTemplet = templet;
			chart.endpoint = endpoint;
			chart.percentHeight = 100;
			chart.percentWidth = 100;
			chart.dashboardCode = "";
			chart.isAutoRefresh = false;
			chart.chartClickListeners = [new GroupAnalyseMTChartClickListener()];
			
			var chartTitle:String = "["+taskName+"]分组总览";
			chart.title =  chartTitle;
			entityRuleCheckFrame.titleLabel.text = chartTitle;
			entityRuleCheckFrame.chartTitle = chartTitle;
			entityRuleCheckFrame.isVisibleLabel = false;
			
			chart.dataProvider= dataProvider;
			try{
				var childrenArr:Array = entityRuleCheckFrame.chartPanel.getChildren();
				if(childrenArr != null && childrenArr.length >0 ){
					entityRuleCheckFrame.chartPanel.removeAllChildren();
				}
			}catch(error:Error) {
				
			}
			
			entityRuleCheckFrame.chartPanel.addChild(chart);
			
			chart.addEventListener(MTChartEvent.INIT_COMPLETE,function(event:MTChartEvent):void{
				chart.refreshComplete();
				chart.hideChartButtonBox();
			});
			
			entityRuleCheckFrame.crrentAnalyseType = 2;
			entityRuleCheckFrame.chart = chart;
			entityRuleCheckFrame.mainStack.selectedChild = entityRuleCheckFrame.chartBox;
			
		}
		
		private function getEntityRuleCheckPanel(currentContener:DisplayObject):EntityRuleCheckPanel{
			var display:DisplayObject = null;
			var tempDisplay:DisplayObject = currentContener.parent;
			if(tempDisplay is EntityRuleCheckPanel){
				display = tempDisplay;
			}else{
				display = getEntityRuleCheckPanel(tempDisplay);
			}
			return  display as EntityRuleCheckPanel;
		}
		
		private function getParamsMap(entityXml:XML):Object{
			var paramsMap:Object = new Object();
			if(entityXml != null ){
				for each(var itemXml:XML in entityXml.groupAttributes.groupAttribute){
					var name:String = String(itemXml.@name);
					var parameter:String = String(itemXml.@parameter);
					var parameterValue:String = "";
					if(!BAMUtil.isEmpty(parameter)){
						var tempValue:Object = ClientEnviorment.getInstance().getVar(parameter);
						if(tempValue != null){
							parameterValue = tempValue as String;
						}
					}
					paramsMap[name] = parameterValue;
				}
				
			}
			return paramsMap;
		}
	}
}