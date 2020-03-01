
package smartx.bam.flex.utils
{
	import flash.display.DisplayObject;
	import flash.events.MouseEvent;
	
	import mx.managers.PopUpManager;
	
	import smartx.bam.flex.modules.dashboardobject.designer.AdvDataGridDesigner;
	import smartx.bam.flex.modules.dashboardobject.designer.BaseDesigner;
	import smartx.bam.flex.modules.dashboardobject.designer.CardDesigner;
	import smartx.bam.flex.modules.dashboardobject.designer.GeneralFormDesigner;
	import smartx.bam.flex.modules.dashboardobject.designer.GeographyChartDesigner;
	import smartx.bam.flex.modules.dashboardobject.designer.IndicatorDesigner;
	import smartx.bam.flex.modules.dashboardobject.designer.ListChartDesigner;
	import smartx.bam.flex.modules.dashboardobject.designer.MultiSeriesChartDesigner;
	import smartx.bam.flex.modules.dashboardobject.designer.OlapDesigner;
	import smartx.bam.flex.modules.dashboardobject.designer.ProcessChartDesigner;
	import smartx.bam.flex.modules.dashboardobject.designer.SingleSeriesChartDesigner;
	import smartx.bam.flex.modules.dashboardobject.designer.TreeListChartDesigner;
	import smartx.bam.flex.modules.dashboardobject.designer.XYPlotChartDesigner;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.core.mtchart.vo.MTChartType;
	import smartx.flex.components.util.MemoryUtil;
	import smartx.flex.components.util.SmartXMessage;

	/**
	 * sky zhangzz
	 **/
	public class DODesignerUtil
	{
		private var popupParent:DisplayObject;
		
		private var dataValue:Object;
		
		private var bcp:BillCardPanel;
		
		public var qvCodes:Array;
		
		public var bvCodes:Array;
		
		private var generalFormDesigner:GeneralFormDesigner;
		
		public function DODesignerUtil(popupParent:DisplayObject,bcp:BillCardPanel){
			this.popupParent = popupParent;
			this.bcp = bcp;
		}
		
		public function advClick(event:MouseEvent):void{
			var designer:BaseDesigner = null;
			generalFormDesigner = new GeneralFormDesigner();
			if(bcp){
				dataValue = bcp.getDataValue();
				var content:String = dataValue["CONTENT"];
				var type:Object = dataValue["TYPE"];
				if(!type){
					SmartXMessage.show("没有该类型的图表!",SmartXMessage.MESSAGE_ERROR);
					return;
				}
				if(type.id == MTChartType.SINGLESERIESCHART){
					designer = new SingleSeriesChartDesigner();
					generalFormDesigner.title = "单组数据图属性设置";
				}else if(type.id == MTChartType.MULTISERIESCHART){
					designer = new MultiSeriesChartDesigner();
					generalFormDesigner.title = "多组数据图属性设置";
				}else if(type.id == MTChartType.XYPLOTCHART){
					designer = new XYPlotChartDesigner();
					generalFormDesigner.title = "坐标数据图属性设置";
				}else if(type.id == MTChartType.GEOGRAPHYCHART){
					designer = new GeographyChartDesigner();
					generalFormDesigner.title = "地理图属性设置";
				}else if(type.id == MTChartType.INDICATOR){
					designer = new IndicatorDesigner();
					generalFormDesigner.title = "指示器属性设置";
				}else if(type.id == MTChartType.PIVOTCOMBINATIONCHART){
					SmartXMessage.show("透视混合图高级属性");
				}else if(type.id == MTChartType.TABLE){
					designer = new ListChartDesigner();
					generalFormDesigner.title = "列表图属性设置";
				}else if(type.id == MTChartType.TREELIST){
					designer = new TreeListChartDesigner();
					generalFormDesigner.title = "树形列表图属性设置";
				}else if(type.id == MTChartType.CARD){
					designer = new CardDesigner();
					generalFormDesigner.title = "卡片图属性设置";
				}else if(type.id == MTChartType.OLAP){
					designer = new OlapDesigner();
					generalFormDesigner.title = "OLAP图属性设置";
				}else if(type.id == MTChartType.PROCESSCHART){
					designer = new ProcessChartDesigner();
					generalFormDesigner.title = "流程图属性设置";
				}else if(type.id == MTChartType.ADVTABLE){
					designer = new AdvDataGridDesigner();
					generalFormDesigner.title = "高级列表图属性设置";
				}else{
					SmartXMessage.show("没有该类型图表的高级属性!",SmartXMessage.MESSAGE_ERROR);
				}
				
				if(designer != null){
					generalFormDesigner.designer = designer;
					generalFormDesigner.confirmFunc = confirmBtnClick;
					if(qvCodes)
						generalFormDesigner.qvCodes = qvCodes;
					if(bvCodes)
						generalFormDesigner.bvCodes = bvCodes;
					generalFormDesigner.contentXml = content!=null?new XML(content):null;
					generalFormDesigner.endpoint = bcp.endpoint;
					
					PopUpManager.addPopUp(generalFormDesigner,popupParent,true);
					PopUpManager.centerPopUp(generalFormDesigner)
				}
			}else{
				SmartXMessage.show("打开高级属性发生错误！",SmartXMessage.MESSAGE_ERROR);
			}
		}
		
		private function confirmBtnClick():void{
			dataValue["CONTENT"] = generalFormDesigner.getContentXmlStr();
			bcp.setDataValue(dataValue);
			PopUpManager.removePopUp(generalFormDesigner);
			generalFormDesigner.confirmFunc = null;
			generalFormDesigner = null;
			MemoryUtil.forceGC();
		}
		
	}
}