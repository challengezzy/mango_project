package smartx.bam.flex.modules.dqc.listener
{
	import com.events.FCEvent;
	
	import flash.events.MouseEvent;
	
	import mx.charts.events.ChartEvent;
	import mx.charts.events.ChartItemEvent;
	import mx.controls.Alert;
	import mx.events.ListEvent;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.bam.flex.modules.common.variableControl.vo.VariableVo;
	import smartx.bam.flex.modules.entitymodel.QueryDatagridWindow;
	import smartx.bam.flex.utils.BAMUtil;
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.mtchart.MTChartPanel;
	import smartx.flex.components.core.mtchart.listener.MTChartClickListener;
	import smartx.flex.components.util.OperatingTipUtil;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.SimpleHashVO;
	
	public class HisAnalyseMTChartClickListener implements MTChartClickListener
	{
		
		private var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT)) as String;
		
		private var debugMode:Boolean =  ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_GLOBAL_DEBUGMODE) as Boolean;
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var formService:RemoteObject;
		
		private var statisticsId:String;
		private var column:String;
		private var value:String;
		private var detailTable:String;
		
		public function HisAnalyseMTChartClickListener()
		{
			formService = new RemoteObject(GlobalConst.SERVICE_FORM);
			formService.endpoint = endpoint;
			
			formService.getSimpleHashVoArrayByDS.addEventListener(ResultEvent.RESULT,getSimpleHashVoArrayByDSHandler);
			formService.getSimpleHashVoArrayByDS.addEventListener(FaultEvent.FAULT,function(event:FaultEvent):void{
				OperatingTipUtil.endOperat();
				SmartXMessage.show("无法获取数据!",SmartXMessage.MESSAGE_ERROR,event.fault.faultString);
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
				if(params != null && params.length ==4){
					this.statisticsId = params[0];
					this.column = params[1];
					this.value = params[2];
					this.detailTable = params[3];
					
					OperatingTipUtil.startOperat("正在分析....");
					formService.getSimpleHashVoArrayByDS(BAMConst.DATASOURCE_SMARTDQ,"select m.mtcontent content,s.entitymodelcode modelcode,s.entitycode,s.entitycode,s.rulecode from dq_entity_statistics s,dq_entity_statistics_map m where s.mapid=m.id and s.id="+statisticsId);
				}
			}
		}
		
		public function fcClick(event:MouseEvent, chart:MTChartPanel):void
		{
		}
		
		public function listItemClick(event:ListEvent, chart:MTChartPanel):void
		{
		}
		
		private function getSimpleHashVoArrayByDSHandler(event:ResultEvent):void{
			OperatingTipUtil.endOperat();
			var spArr:Array = event.result as Array;
			if(spArr != null ){
				var sp:SimpleHashVO = spArr[0] as SimpleHashVO;
				var content:String = sp.dataMap["content"] as String;
				
				var attrXml:XML =  new XML(content);
				var sql:String = "select * from "+this.detailTable+" where DQ_STATISTICSID='"+statisticsId+"'";
				
				if(!BAMUtil.isEmpty(column) && (!BAMUtil.isEmpty(value) && value != '全部' && value != 'ALL')&&value != VariableVo.IGNORE_VALUE){
					sql = sql + " AND "+column+"='"+value+"'";
				}
				
				var queryDatagridWindow:QueryDatagridWindow = new QueryDatagridWindow(); 
				queryDatagridWindow.attributesXml = getDisplayAttributes(attrXml);
				queryDatagridWindow.sharedObjectCode = detailTable;
				queryDatagridWindow.sharedObjectSpaceName = "HIS_"+BAMConst.KEYNAME_SHAREDOBJECT_ENTITY_MODEL_FILTERINFO;
				queryDatagridWindow.sql = sql;
				queryDatagridWindow.dataSource = BAMConst.DATASOURCE_SMARTDQ;
				queryDatagridWindow.debugMode = debugMode;
				queryDatagridWindow.modelCode = sp.dataMap["modelcode"] as String;
				queryDatagridWindow.entityCode = sp.dataMap["entitycode"] as String;
				queryDatagridWindow.ruleCode = sp.dataMap["rulecode"] as String;
				
				PopUpManager.addPopUp(queryDatagridWindow,desktop,true);
				
				PopUpManager.centerPopUp(queryDatagridWindow);
				
				queryDatagridWindow.refresh();
				
			}
		}
		
		private function getDisplayAttributes(xml:XML):XML{
			
			var attributesXml:XML = new XML("<attributes />");
			
			if(xml != null){
				for each(var displayXml:XML in xml.displayAttributes.attribute){
					attributesXml.appendChild(displayXml.copy());
				}
			}
			
			return attributesXml;
		}
	}
}