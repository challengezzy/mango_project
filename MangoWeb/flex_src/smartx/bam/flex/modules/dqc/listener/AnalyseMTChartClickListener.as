package smartx.bam.flex.modules.dqc.listener
{
	import com.events.FCEvent;
	
	import flash.events.MouseEvent;
	
	import mx.charts.events.ChartEvent;
	import mx.charts.events.ChartItemEvent;
	import mx.events.CloseEvent;
	import mx.events.ListEvent;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import net.brandonmeyer.containers.SuperPanel;
	
	import smartx.bam.flex.modules.common.variableControl.vo.VariableVo;
	import smartx.bam.flex.modules.entitymodel.QueryDatagridWindow;
	import smartx.bam.flex.modules.entitymodel.utils.EntityModelListPanel;
	import smartx.bam.flex.modules.entitymodel.utils.EntityUtil;
	import smartx.bam.flex.utils.BAMUtil;
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.mtchart.MTChartPanel;
	import smartx.flex.components.core.mtchart.listener.MTChartClickListener;
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.util.OperatingTipUtil;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.vo.MetadataTemplet;
	
	public class AnalyseMTChartClickListener implements MTChartClickListener
	{
		
		private var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT)) as String;
		
		private var debugMode:Boolean =  ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_GLOBAL_DEBUGMODE) as Boolean;
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var bamService:RemoteObject;
		
		private var ruleCode:String;
		private var entityCode:String;
		private var modelCode:String;
		private var datasource:String;
		
		public function AnalyseMTChartClickListener()
		{
			bamService = new RemoteObject(BAMConst.BAM_Service);
			bamService.endpoint = endpoint;
			
			bamService.generateDisplayEntitySqlContainConditionByMtCode.addEventListener(ResultEvent.RESULT,generateEntitySqlHandler);
			bamService.generateDisplayEntitySqlContainConditionByMtCode.addEventListener(FaultEvent.FAULT,function(event:FaultEvent):void{
				OperatingTipUtil.endOperat();
				SmartXMessage.show("生成SQL错误!",SmartXMessage.MESSAGE_ERROR,event.fault.faultString);
			});
		}
		
		public function itemClick(event:ChartItemEvent, chart:MTChartPanel):void
		{
		}
		
		public function click(event:ChartEvent, chart:MTChartPanel):void
		{
		}
		
		public function fcItemClick(event:FCEvent, chart:MTChartPanel):void
		{
			var info:String = event.param as String;
			if(!BAMUtil.isEmpty(info)){
				var params:Array = info.split(";");
				this.modelCode = params[0];
				this.entityCode = params[1];
				this.ruleCode = params[2];
				
				var column:String = params[3];
				var value:String = params[4];
				this.datasource = params[5];
				var conditionStr:String ="";
				if(column != null && column != ""){
					if(value != null && value != "" && value != "-1" && value != VariableVo.IGNORE_VALUE){
						conditionStr = " where "+column+"='"+value+"'";
					}
				}
				
				OperatingTipUtil.startOperat("正在分析....");
				bamService.generateDisplayEntitySqlContainConditionByMtCode(BAMConst.ENTITY_MT_PREFIX+modelCode+"_"+entityCode,BAMConst.ENTITY_MODEL_MT_PREFIX+modelCode,BAMConst.ENTITY_MT_RULE_PREFEX+this.ruleCode,conditionStr,null);
			}
		}
		
		public function fcClick(event:MouseEvent, chart:MTChartPanel):void
		{
		}
		
		public function listItemClick(event:ListEvent, chart:MTChartPanel):void
		{
		}
		
		private function generateEntitySqlHandler(event:ResultEvent):void{
			OperatingTipUtil.endOperat();
			
			var sql:String = event.result as String;
			
			//				var groupcolumn:String = String(this.currentRuleXml.@groupcolumn);
			//				if(groupcolumn != null && groupcolumn != ""){
			//					var groupcolumnValue:String = this.paramsMap[groupcolumn];
			//					if(groupcolumnValue != null && groupcolumnValue != "" && groupcolumnValue != "-1" && groupcolumnValue != VariableVo.IGNORE_VALUE){
			//						sql = "select * from ("+sql+") where "+groupcolumn+"='"+groupcolumnValue+"'";
			//					}
			//				}
			
			var entityMtCode:String = BAMConst.ENTITY_MT_PREFIX+modelCode+"_"+entityCode;
			var entityTemplete:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet(entityMtCode);
			var tempEnityXml:XML = null;
			if(entityTemplete!=null){
				tempEnityXml = entityTemplete.contentXML;
			}
			
			var modelMtCode:String = BAMConst.ENTITY_MODEL_MT_PREFIX+modelCode;
			var templete:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet(modelMtCode);
			var tempModelXml:XML = null;
			var datasource:String = null;
			if(templete!=null){
				tempModelXml = templete.contentXML;
				datasource = String(tempModelXml.datasource);
			}
			
			var queryDatagridWindow:QueryDatagridWindow = new QueryDatagridWindow(); 
			queryDatagridWindow.attributesXml = tempEnityXml.@type=="virtual"?tempEnityXml.attributes[0]:EntityUtil.displayAttributeWrap(tempEnityXml,tempModelXml);EntityUtil.displayAttributeWrap(tempEnityXml,tempModelXml);
			queryDatagridWindow.sharedObjectCode = ruleCode;
			queryDatagridWindow.sharedObjectSpaceName = modelCode.concat("_").concat(entityCode);
			queryDatagridWindow.sql = sql;
			queryDatagridWindow.dataSource = datasource;
			queryDatagridWindow.debugMode = debugMode;
			queryDatagridWindow.modelCode = modelCode;
			queryDatagridWindow.entityCode = entityCode;
			queryDatagridWindow.ruleCode = ruleCode;
			
			PopUpManager.addPopUp(queryDatagridWindow,desktop,true);
			
			PopUpManager.centerPopUp(queryDatagridWindow);
			
			queryDatagridWindow.refresh();
			
//			var entityModelListPanel:EntityModelListPanel = new EntityModelListPanel;
//			entityModelListPanel.sharedObjectSpaceName = modelCode.concat("_").concat(entityCode);
//			entityModelListPanel.sharedObjectCode = ruleCode;
//			entityModelListPanel.debugMode = debugMode;
//			entityModelListPanel.attributesXml = tempEnityXml.@type=="virtual"?tempEnityXml.attributes[0]:EntityUtil.displayAttributeWrap(tempEnityXml,tempModelXml);EntityUtil.displayAttributeWrap(tempEnityXml,tempModelXml);
//			entityModelListPanel.isQueryAfterCompleted = true;
//			entityModelListPanel.sql = sql;
//			entityModelListPanel.pageSize = 33;
//			entityModelListPanel.dataSource = datasource;
//			
//			var superPanel:SuperPanel = new SuperPanel();
//			superPanel.width = 950;
//			superPanel.height = 550;
//			superPanel.allowClose = true;
//			superPanel.allowDrag = true;
//			superPanel.allowMaximize = true;
//			superPanel.allowResize = true;
//			
//			superPanel.addChild(entityModelListPanel);
//			
//			superPanel.addEventListener(CloseEvent.CLOSE,function(e:CloseEvent):void{
//				PopUpManager.removePopUp(superPanel);
//			});
//			
//			PopUpManager.addPopUp(superPanel,desktop,true);
//			PopUpManager.centerPopUp(superPanel);
			
		}
		
		private function getDisplayAttributes(xml:XML):XML{
			
			var attributesXml:XML = new XML("<attributes />");
			
			if(xml != null){
				for each(var displayXml:XML in xml.displayAttributes.attribute){
					var columnName:String = String(displayXml.@name);
					for each(var attXml:XML in xml.attribute){
						var tempColumn:String = String(attXml.@name);
						if(!BAMUtil.isEmpty(tempColumn)&&tempColumn==columnName){
							attributesXml.appendChild(attXml.copy());
							break;
						}
					}
				}
			}
			
			return attributesXml;
		}
	}
}