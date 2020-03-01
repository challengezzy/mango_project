package smartx.bam.flex.modules.dqc.listener
{
	import mx.containers.TitleWindow;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillListItemPanel;
	import smartx.flex.components.core.chart.AdvListChart;
	import smartx.flex.components.styletemplate.ifc.ListItemButtonListenerIFC;
	
	public class RuleListClickListener implements ListItemButtonListenerIFC
	{
		
		private var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT)) as String;
		
		private var debugMode:Boolean =  ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_GLOBAL_DEBUGMODE) as String;
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var formService:RemoteObject;
		private var bamService:RemoteObject;
		
		private var listItemPanel:BillListItemPanel;
		
		private var queryColumns:Array = new Array();
		private var columns:Array = new Array();
		
		public function RuleListClickListener()
		{
			
			queryColumns.push("rulename");
			
			var dgColumn:DataGridColumn = new DataGridColumn();
			dgColumn.dataField = "rulename";
			dgColumn.headerText = "规则名称";
			dgColumn.editable = false;
			columns.push(dgColumn);
		}
		
		public function handler(dataValue:Object, billListItemPanel:BillListItemPanel):void
		{
			this.listItemPanel = billListItemPanel;
			var batchNo:String = dataValue["BATCHNO"];
			var taskName:String = dataValue["TASKNAME"];
			
			var sql:String = "select * from dq_entity_statistics t where t.batchno='"+batchNo+"' and t.taskname='"+taskName+"'";
			var datasource:String = listItemPanel.datasourceName;
			
			var ruleList:AdvListChart = new AdvListChart();
			ruleList.percentHeight = 100 ;
			ruleList.percentWidth = 100;
			ruleList.queryColumns = queryColumns;
			ruleList.columns=columns;
			ruleList.dataSource = datasource;
			ruleList.sql = sql;
			
			var titleWindow:TitleWindow = new TitleWindow();
			titleWindow.width = 400;
			titleWindow.height = 300;
			titleWindow.showCloseButton = true;
			titleWindow.addEventListener(CloseEvent.CLOSE,function(event:CloseEvent):void{
				PopUpManager.removePopUp(titleWindow);
			});
			titleWindow.addChild(ruleList);
			
			PopUpManager.addPopUp(titleWindow,desktop,true);
			PopUpManager.centerPopUp(titleWindow);
			
			ruleList.refresh();
		}
	}
}