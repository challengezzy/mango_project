package smartx.flex.components.core.mtchart.utils
{
	import flash.utils.getDefinitionByName;
	
	import mx.core.UIComponent;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.mtchart.MTAdvDataGridPanel;
	import smartx.flex.components.core.mtchart.MTCardChartPanel;
	import smartx.flex.components.core.mtchart.MTChartPanel;
	import smartx.flex.components.core.mtchart.MTGeoMapChartPanel;
	import smartx.flex.components.core.mtchart.MTIndicatorPanel;
	import smartx.flex.components.core.mtchart.MTListChartPanel;
	import smartx.flex.components.core.mtchart.MTMultiSeriesChartPanel;
	import smartx.flex.components.core.mtchart.MTOLAPChartPanel;
	import smartx.flex.components.core.mtchart.MTProcessChartPanel;
	import smartx.flex.components.core.mtchart.MTSingleSeriesPanel;
	import smartx.flex.components.core.mtchart.MTTreeGridChartPanel;
	import smartx.flex.components.core.mtchart.MTXYPlotChartPanel;
	import smartx.flex.components.core.mtchart.event.MTChartEvent;
	import smartx.flex.components.core.mtchart.listener.MTChartClickListener;
	import smartx.flex.components.core.mtchart.vo.MTChartType;
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.vo.MetadataTemplet;
	
	/**
	 * sky zhangzz
	 **/
	public class MTChartFactory
	{
		private static var mtChartFactory:MTChartFactory = new MTChartFactory();
		
		public function MTChartFactory(){
			if(mtChartFactory)
				throw new Error("MTChartFactory不能被实例化！");
		}
		
		public static function getInstance():MTChartFactory{
			if(mtChartFactory == null)
				mtChartFactory = new MTChartFactory();
			return mtChartFactory;
		}
		
		/**
		 * 根据类型生成图表
		 **/ 
		public function getChart(type:int,mtcode:String,id:String,refreshListener:Function,endpoint:String=null,intervalTime:Number=0
										,stopListener:Function=null,startListener:Function=null,editListener:Function=null,detailListener:Function=null):MTChartPanel{
			if(mtcode == null){
				SmartXMessage.show("必须指定图表元数据模板编码(mtcode)");
				return null;
			}
			var xml:XML;
			var debugMode:Boolean;
			var metadataTemplet:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet(mtcode);
			if(metadataTemplet.contentXML.debugMode.length() == 0){
				//如没指定debugMode，根据客户端变量设置
				if(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_GLOBAL_DEBUGMODE) != null)
					debugMode = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_GLOBAL_DEBUGMODE);
				if(debugMode){
					xml = metadataTemplet.contentXML;
					xml.debugMode = "true";
					metadataTemplet.contentXML = xml;
				}
			}
			xml = metadataTemplet.contentXML;
			xml.mtcharttype = String(type);
			metadataTemplet.contentXML = xml;
			if(endpoint == null)
				endpoint = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
			var dboIntervalTime:Number = 0;
			if(metadataTemplet.contentXML.refreshinterval.length() > 0)
				dboIntervalTime = Number(metadataTemplet.contentXML.refreshinterval) * 1000;
			
			var chart:MTChartPanel;
			switch(type){
				case MTChartType.SINGLESERIESCHART:
					chart = new MTSingleSeriesPanel();
					break;
				case MTChartType.MULTISERIESCHART:
					chart = new  MTMultiSeriesChartPanel();
					break;
				case MTChartType.XYPLOTCHART:
					chart = new MTXYPlotChartPanel();
					break;
				case MTChartType.PIVOTCOMBINATIONCHART:
					break;
				case MTChartType.GEOGRAPHYCHART:
					chart = new MTGeoMapChartPanel();
					break;
				case MTChartType.INDICATOR:
					chart = new MTIndicatorPanel();
					break;
				case MTChartType.TABLE://列表图
					chart = new MTListChartPanel();
					break;
				case MTChartType.TREELIST:
					chart = new MTTreeGridChartPanel();
					break;
				case MTChartType.CARD:
					chart = new MTCardChartPanel();
					break;
				case MTChartType.OLAP:
					chart = new MTOLAPChartPanel();
					break;
				case MTChartType.PROCESSCHART:
					chart = new MTProcessChartPanel();
					break;
				case MTChartType.ADVTABLE:
					chart = new MTAdvDataGridPanel();
					break;
			}
			if(chart){
				chart.chartId = id;
				chart.endpoint = endpoint;
				chart.metadataTemplet = metadataTemplet;
				if(dboIntervalTime > 0)
					chart.intervalTime = dboIntervalTime;
				else if(dboIntervalTime == -1000 || intervalTime == -1000)
					chart.isAutoRefresh = false;
				else 
					chart.intervalTime = intervalTime;
				chart.addEventListener(MTChartEvent.REFRESH_DATA,refreshListener);
				if(stopListener != null)
					chart.addEventListener(MTChartEvent.STOP_SUCCESSFUL,stopListener);
				if(startListener != null)
					chart.addEventListener(MTChartEvent.START_SUCCESSFUL,startListener);
				if(editListener != null)
					chart.addEventListener(MTChartEvent.EDIT,editListener);
				if(detailListener != null)
					chart.addEventListener(MTChartEvent.DETAIL,detailListener);
				if(xml.chartClickListener.length() > 0){
					for each(var listenerXml:XML in xml.chartClickListener.listener){
						var classRefrence:Class = getDefinitionByName(listenerXml.toString()) as Class;
						var chartClickListener:MTChartClickListener = new classRefrence() as MTChartClickListener;
						chart.chartClickListeners.push(chartClickListener);
					}
				}
			}
			return chart;
		}
	}
}