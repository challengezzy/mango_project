package smartx.bam.flex.modules.dqc.listener
{
	import flash.display.DisplayObject;
	
	import mx.collections.ArrayCollection;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
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
	
	public class HisEntityRuleClickListener implements ListItemButtonListenerIFC
	{
		[Bindable]
		private var dayNames:Array = ["日","一","二","三","四","五","六"];
		[Bindable]
		private var monthNames:Array =["一","二","三","四","五","六","七","八","九","十","十一","十二"];
		
		private var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT)) as String;
		
		private var debugMode:Boolean =  ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_GLOBAL_DEBUGMODE) as Boolean;
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var formService:RemoteObject;
		private var bamService:RemoteObject;
		
		private var chart:MTMultiSeriesChartPanel;
		
		private var ruleAnalyseCol:ArrayCollection = new ArrayCollection();
		
		private var listItemPanel:BillListItemPanel;
		
		private var taskName:String;
		
		public function HisEntityRuleClickListener()
		{
			
			formService = new RemoteObject(GlobalConst.SERVICE_FORM);
			formService.endpoint = endpoint;
			
			bamService = new RemoteObject(BAMConst.BAM_Service);
			bamService.endpoint = endpoint;
			
			formService.getSimpleHashVoArrayByDS.addEventListener(ResultEvent.RESULT,getSimpleHashVoArrayByDSHandler);
			formService.getSimpleHashVoArrayByDS.addEventListener(FaultEvent.FAULT,function(event:FaultEvent):void{
				OperatingTipUtil.endOperat();
				SmartXMessage.show("查询数据时出错!",SmartXMessage.MESSAGE_ERROR,event.fault.faultString);
			});
			
			bamService.analyseHisEntityByBatchNo.addEventListener(ResultEvent.RESULT,analyseHisEntityRuleHandler);
			bamService.analyseHisEntityByBatchNo.addEventListener(FaultEvent.FAULT,faultHander);
			
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
			
//			var titleWindow:TitleWindow = new TitleWindow();
//			titleWindow.addEventListener(CloseEvent.CLOSE,function(event:CloseEvent):void{
//				PopUpManager.removePopUp(titleWindow);
//			});
//			titleWindow.width = 450;
//			titleWindow.height = 150;
//			titleWindow.title = "选择日期";
//			titleWindow.showCloseButton = true;
//			
//			var vbox:VBox = new VBox();
//			vbox.percentHeight = 100;
//			vbox.percentWidth = 100;
//			vbox.setStyle("paddingBottom",5);
//			vbox.setStyle("paddingLeft",5);
//			vbox.setStyle("paddingRight",5);
//			vbox.setStyle("paddingTop",5);
//			
//			titleWindow.addChildAt(vbox,0);
//			
//			var timeHbox:HBox = new HBox();
//			timeHbox.percentWidth = 100;
//			
//			vbox.addChildAt(timeHbox,0);
//			
//			var startTimeLabel:Label = new Label();
//			startTimeLabel.text = "开始日期";
//			startTimeLabel.percentWidth = 10;
//			
//			timeHbox.addChildAt(startTimeLabel,0);
//			
//			var startTimeDC:DateField = new DateField();
//			startTimeDC.formatString="YYYY-MM-DD";
//			startTimeDC.selectedDate=getDate();
//			startTimeDC.showToday=true;
//			startTimeDC.dayNames= dayNames;
//			startTimeDC.monthNames=monthNames;
//			
//			timeHbox.addChildAt(startTimeDC,1);
//			
//			var endTimeLabel:Label = new Label();
//			endTimeLabel.text = "终止日期";
//			endTimeLabel.percentWidth = 10;
//			
//			timeHbox.addChildAt(endTimeLabel,2);
//			
//			var endTimeDC:DateField = new DateField();
//			endTimeDC.formatString="YYYY-MM-DD";
//			endTimeDC.selectedDate=getNextDate();
//			endTimeDC.showToday=true;
//			endTimeDC.dayNames= dayNames;
//			endTimeDC.monthNames=monthNames;
//			
//			timeHbox.addChildAt(endTimeDC,2);
//			
//			var countHbox:HBox = new HBox();
//			countHbox.percentWidth = 100;
//			
//			vbox.addChildAt(countHbox,1);
//			
//			var countLabel:Label = new Label();
//			countLabel.text = "显示数量";
//			countLabel.percentWidth = 10;
//			
//			countHbox.addChildAt(countLabel,0);
//			
//			var numStepper:NumericStepper = new NumericStepper();
//			numStepper.height = 20;
//			numStepper.width = 60;
//			numStepper.stepSize=1;
//			numStepper.value=5;
//			numStepper.minimum=1;
//			numStepper.maximum=100;
//			
//			countHbox.addChildAt(numStepper,1);
			
			formService.getSimpleHashVoArrayByDS(listItemPanel.datasourceName,"select * from dq_entity_statistics t where t.batchno='"+batchNo+"' and t.taskname='"+taskName+"'");
		}
		
		private function getSimpleHashVoArrayByDSHandler(event:ResultEvent):void{
			OperatingTipUtil.endOperat();
			var result:Array = event.result as Array;
			var rulesInfoArr:Array = new Array();
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
				
				var ruleObj:Object = new Object();
				ruleObj["ruleCode"] = ruleCode;
				ruleObj["entityCode"] = entityCode;
				ruleObj["modelCode"] = modelCode;
				ruleObj["groupColumnName"] = groupcolumn;
				ruleObj["groupColumnValue"] = groupcolumnValue;
				ruleObj["batchNo"] = batchNo;
				ruleObj["taskName"] = taskName;
				
				rulesInfoArr.push(ruleObj);
				
			}
			
			bamService.analyseHisEntityByBatchNo(rulesInfoArr);
		}
		
		private function analyseHisEntityRuleHandler(event:ResultEvent):void{
			
			OperatingTipUtil.endOperat();
			
			var resultObj:Object  = event.result;
			var resultArr:Array = resultObj["SIMPLEHASHVOARRAY"];
			var dataProvider:ArrayCollection = new ArrayCollection();
			dataProvider.removeAll();
			ruleAnalyseCol.removeAll();
			for each(var spm:SimpleHashVO in resultArr){
				dataProvider.addItem(spm.dataMap);
				ruleAnalyseCol.addItem(spm.dataMap);
			}
			
			var ruleAnalyseQueryColumns:Array = new Array();
			ruleAnalyseQueryColumns.push("rulename");
			ruleAnalyseQueryColumns.push("batchno");
			
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
			
			var dgColumn3:DataGridColumn = new DataGridColumn();
			dgColumn3.dataField = "rowcount";
			dgColumn3.headerText = "符合规则数量";
			dgColumn3.width = 255;
			ruleAnalyseColumns.push(dgColumn3);
			
			var dgColumn4:DataGridColumn = new DataGridColumn();
			dgColumn4.dataField = "totalcount";
			dgColumn4.width = 255;
			dgColumn4.headerText = "总数量";
			ruleAnalyseColumns.push(dgColumn4);
			
			var contentXml:XML = <chart>
								  <bindSynonymses/>
								  <charttype>MSColumn3D</charttype>
								  <rowsLimit/>
								  <datasource type="rdms" sql="">datasource_smartdq</datasource>
								  <chartname/>
								  <backupChartname/>
								  <refreshinterval/>
								  <orderby/>
								  <filter/>
								  <backupFilter/>
								  <extend>
									<isShowLabel>true</isShowLabel>
									<xaxianame/>
									<pyaxianame>记录数</pyaxianame>
									<syaxianame/>
									<subseriesnames/>
									<height/>
									<width/>
									<datamode>rowMode</datamode>
									<items>
									  <item dataField="rowcount" labelField="version" xShowField="batchno" seriesNameField="" color="" linkField="info" nameField="rulename" type="Column"/>
									</items>
								  </extend>
								</chart>;
			
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
			chart.chartClickListeners = [new HisAnalyseMTChartClickListener()];
			
			var chartTitle:String = "["+taskName+"]分析结果";
			chart.title =  chartTitle;
			
			var entityRuleCheckFrame:EntityRuleCheckPanel = this.getEntityRuleCheckPanel(listItemPanel);
			
			entityRuleCheckFrame.titleLabel.text = chartTitle;
			entityRuleCheckFrame.chartTitle = chartTitle;
			entityRuleCheckFrame.isVisibleLabel = false;
			entityRuleCheckFrame.crrentAnalyseType = 1;
			entityRuleCheckFrame.chart = chart;
			
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
			
			entityRuleCheckFrame.ruleAnalyseList.queryColumns = ruleAnalyseQueryColumns;
			entityRuleCheckFrame.ruleAnalyseList.columns=ruleAnalyseColumns;
			entityRuleCheckFrame.ruleAnalyseList.dataArray = ruleAnalyseCol;
			
			entityRuleCheckFrame.ruleAnalyseList.refresh();
			
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
		
		private function getDate():Date{
			var nowTime:Date = new Date();
			var now_millisecond:Number = nowTime.getTime();
			var twenty_four_hour:Number = 24*60*60*1000*5;
			var yesterday_millisecond:Number = now_millisecond-twenty_four_hour;
			var yesterdayTime:Date = new Date(yesterday_millisecond);
			return yesterdayTime;
		}
		
		private function getNextDate():Date{
			var nowTime:Date = new Date();
			var now_millisecond:Number = nowTime.getTime();
			var twenty_four_hour:Number = 24*60*60*1000;
			var yesterday_millisecond:Number = now_millisecond+twenty_four_hour;
			var tomorrowTime:Date = new Date(yesterday_millisecond);
			return tomorrowTime;
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