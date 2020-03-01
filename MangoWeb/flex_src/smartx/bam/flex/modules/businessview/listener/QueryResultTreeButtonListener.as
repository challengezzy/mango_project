package smartx.bam.flex.modules.businessview.listener
{
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.bam.flex.modules.businessview.BvResultWindow;
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.core.BillTreePanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.SimpleHashVO;
	import smartx.flex.components.vo.TableDataStruct;
	
	/**
	 * sky zhangzz
	 **/
	public class QueryResultTreeButtonListener extends BvTreeButtonListener
	{
		private var bvResultWindow:BvResultWindow;
		
		private var columns:Array;
		
		private var queryColumns:Array;
		
		private var dataProvider:ArrayCollection;
		
		private var windowName:String;
		
		public function QueryResultTreeButtonListener(){
			super();
		}
		
		override public function buttonClick(btp:BillTreePanel):void{
			billTreePanel = btp;
			var data:Object = billCardPanel.getDataValue();
			if(data){
				if(data["STATUS"].id == "1"){
					windowName = data["STREAMWINDOWNAME"];
					startOperat();
					remoteObj.getTableStructByName(cepDatasource,windowName);
				}else{
					Alert.show("该业务视图状态错误，无法查询结果集！");					
				}
			}else
				Alert.show("请选择一条记录！");
		}
		
		override protected function getTableStructByNameHandler(event:ResultEvent):void{
			endOperat();
			columns = new Array();
			queryColumns = new Array();
			
			var tableStruct:TableDataStruct = event.result as TableDataStruct;
			for each(var header:String in tableStruct.table_header){
				var dataGridColumn:DataGridColumn = new DataGridColumn(header);
				dataGridColumn.dataField = header.toLowerCase();
				columns.push(dataGridColumn);
				queryColumns.push(dataGridColumn.dataField);
			}
			if(columns.length > 0){
				startOperat();
				//smartxFormRpc.getSimpleHashVoArrayByDS(cepDatasource,"select * from ".concat(windowName).concat(" limit 1000"));
				bvResultWindow = new BvResultWindow();
				bvResultWindow.columns = columns;
				bvResultWindow.queryColumns = queryColumns;
				bvResultWindow.sql = "select * from ".concat(windowName).concat(" limit 1000");
				bvResultWindow.datasource = cepDatasource;
				PopUpManager.addPopUp(bvResultWindow,desktop,true);
				PopUpManager.centerPopUp(bvResultWindow);
				bvResultWindow.refresh();
			}else{
				Alert.show("该视图没有数据！");
			}
			endOperat();
		}
		
	}
}