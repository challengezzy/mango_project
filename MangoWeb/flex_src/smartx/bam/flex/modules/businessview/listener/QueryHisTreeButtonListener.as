package smartx.bam.flex.modules.businessview.listener
{
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	
	import smartx.bam.flex.modules.businessview.BvResultWindow;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.core.BillTreePanel;
	import smartx.flex.components.vo.SimpleHashVO;
	import smartx.flex.components.vo.TableDataStruct;

	public class QueryHisTreeButtonListener extends BvTreeButtonListener
	{
		private var tableName:String;
		
		private const BV_PERFIX:String = "bv_";
		
		private var columns:Array;
		
		private var queryColumns:Array;
		
		private var dataProvider:ArrayCollection;
		
		private var bvResultWindow:BvResultWindow;
		
		public function QueryHisTreeButtonListener(){
			super();
		}
		
		override public function buttonClick(btp:BillTreePanel):void{
			billTreePanel = btp;
			var data:Object = btp.tree.selectedItem;
			if(data){
				tableName = BV_PERFIX.concat(data["STREAMWINDOWNAME"]);
				startOperat();
				remoteObj.getTableStructByName(null,tableName);
			}else
				Alert.show("请选择一条记录！");
		}
		
		override protected function getTableStructByNameHandler(event:ResultEvent):void{
			columns = new Array();
			var tableStruct:TableDataStruct = event.result as TableDataStruct;
			for each(var header:String in tableStruct.table_header){
				var dataGridColumn:DataGridColumn = new DataGridColumn(header);
				dataGridColumn.dataField = header.toLowerCase();
				columns.push(dataGridColumn);
				queryColumns.push(dataGridColumn.dataField);
			}
			if(columns.length > 0){
				bvResultWindow = new BvResultWindow();
				bvResultWindow.columns = columns;
				bvResultWindow.queryColumns = queryColumns;
				bvResultWindow.sql ="select * from ".concat(tableName);
				PopUpManager.addPopUp(bvResultWindow,desktop,true);
				PopUpManager.centerPopUp(bvResultWindow);
				bvResultWindow.refresh();
			}else{
				Alert.show("该视图没有历史数据！");
			}
			endOperat();	
		}
		
		override protected function getTableStructByNameFaultHandler(event:FaultEvent):void{
			endOperat();
			Alert.show("该视图没有历史数据！");
		}
	}
}