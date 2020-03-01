package smartx.bam.flex.modules.dqc.listener
{
	import com.events.FCEvent;
	
	import flash.events.MouseEvent;
	
	import mx.charts.events.ChartEvent;
	import mx.charts.events.ChartItemEvent;
	import mx.controls.AdvancedDataGrid;
	import mx.controls.Alert;
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
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
	
	public class AdvDataGridTestClickListener implements MTChartClickListener
	{
		private var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT)) as String;
		
		private var debugMode:Boolean =  ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_GLOBAL_DEBUGMODE) as Boolean;
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var formService:RemoteObject;
		
		private var statisticsId:String;
		private var column:String;
		private var value:String;
		private var detailTable:String;
		
		public function AdvDataGridTestClickListener()
		{
			formService = new RemoteObject(GlobalConst.SERVICE_FORM);
			formService.endpoint = endpoint;
			
			formService.getSimpleHashVoArrayByDS.addEventListener(ResultEvent.RESULT,getSimpleHashVoArrayByDSHandler);
			formService.getSimpleHashVoArrayByDS.addEventListener(FaultEvent.FAULT,function(event:FaultEvent):void{
				OperatingTipUtil.endOperat();
				SmartXMessage.show("无法获取数据!",SmartXMessage.MESSAGE_ERROR,event.fault.faultString);
			});
		}
		
		public function itemClick(event:ChartItemEvent, chart:MTChartPanel):void{
		}
		
		public function click(event:ChartEvent, chart:MTChartPanel):void{
		}
		
		public function fcItemClick(event:FCEvent, chart:MTChartPanel):void{
		}
		
		public function fcClick(event:MouseEvent, chart:MTChartPanel):void{
		}
		
		public function listItemClick(event:ListEvent, chart:MTChartPanel):void{
			//SmartXMessage.show("listItemClick");
			var adg:AdvancedDataGrid = event.currentTarget as AdvancedDataGrid;
			//SmartXMessage.show("listItemClick," + adg.selectedCells[0]["rowIndex"] + ",columnIndex" + adg.selectedCells[0]["columnIndex"]);
			//取到列表的行、表，进而判断出该行、列的数据，并处理
			
			var rowIndex:int = adg.selectedCells[0]["rowIndex"];
			var columnIndex:int = adg.selectedCells[0]["columnIndex"];
			var column:AdvancedDataGridColumn = adg.columns[columnIndex];
			
			var columnField:String = column.dataField;
			var headText:String = column.headerText;
			
			//以下数据内容取法针对于FLAT数据是有效的，对于Hierarchical数据是不正确
			var rowData:Object = adg.dataProvider[rowIndex];
			var cellData:String = rowData[columnField];
			
			SmartXMessage.show("你点击的单元格索引为：("+rowIndex+","+columnIndex+"),列表头为:"+headText+"("+columnField+"),数据为："+cellData);
			
		}
		
		private function getSimpleHashVoArrayByDSHandler(event:ResultEvent):void{
			OperatingTipUtil.endOperat();
		}
		
	}
}