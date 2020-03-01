package smartx.flex.components.util.export
{
	import flash.net.FileReference;
	import flash.utils.ByteArray;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IViewCursor;
	import mx.controls.AdvancedDataGrid;
	import mx.controls.DataGrid;
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
	import mx.controls.dataGridClasses.DataGridColumn;
	
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.util.StringUtils;
	
	/**
	 * @author zzy
	 * @date Aug 12, 2011
	 */
	public class ListDataExportUtil
	{
		
		/**
		 * dataGrid数据直接导出成CSV文件，GBK格式
		 * 只有在响应用户事件（例如，在鼠标单击或按键事件的事件处理函数中）时才能成功调用此方法
		 * */
		public static function dataGridExport2CSV(dg:DataGrid,fileName:String):void{
			var fr:Object = new FileReference();  
			/**FileReference 的SAVE方法说明
			 *如果值为 String，则将其保存为 UTF-8 文本文件。 
			 *如果值为 XML，则会以 XML 格式将其写入到文本文件，并保留所有格式设置。 
			 *如果值为 ByteArray 对象，则会将其逐字写入到数据文件
			 * */
			//文件保存 ，鉴于String默认为UTF-8保存，转换成byte数组再保存
			if(fr.hasOwnProperty("save")){  
				var data:String = generateCsvData(dg);
				var bytes:ByteArray =new ByteArray();
				bytes.writeMultiByte(data,"gb2312");				
				
				fr.save(bytes, fileName + '.csv');  
			}else{  
				SmartXMessage.show("当前flash player版本不支持此功能,请安装10.0.0以上版本！");  
			}
		}
		
		public static function dataGridExport2CSVAdv(dg:AdvancedDataGrid,fileName:String):void{
			var fr:Object = new FileReference();  
			/**FileReference 的SAVE方法说明
			 *如果值为 String，则将其保存为 UTF-8 文本文件。 
			 *如果值为 XML，则会以 XML 格式将其写入到文本文件，并保留所有格式设置。 
			 *如果值为 ByteArray 对象，则会将其逐字写入到数据文件
			 * */
			//文件保存 ，鉴于String默认为UTF-8保存，转换成byte数组再保存
			if(fr.hasOwnProperty("save")){  
				var data:String = generateCsvDataAdv(dg);
				var bytes:ByteArray =new ByteArray();
				bytes.writeMultiByte(data,"gb2312");				
				
				fr.save(bytes, fileName + '.csv');  
			}else{  
				SmartXMessage.show("当前flash player版本不支持此功能,请安装10.0.0以上版本！");  
			}
		}
		
		/**
		 * dataGrid数据直接导出成CSV文件，UTF-8格式
		 * 只有在响应用户事件（例如，在鼠标单击或按键事件的事件处理函数中）时才能成功调用此方法
		 * */
		public static function dataGridExport2UTF8CSV(dg:DataGrid,fileName:String):void{
			var fr:Object = new FileReference();  

			if(fr.hasOwnProperty("save")){  
				var data:String = generateCsvData(dg);				
				fr.save(data, fileName + '.csv');  
			}else{  
				SmartXMessage.show("当前flash player版本不支持此功能,请安装10.0.0以上版本！");  
			}
		}
		
		public static function dataProviderExport2CSV(dataProvider:ArrayCollection,columns:Array,fileName:String):void{
			var fr:Object = new FileReference();  
			/**FileReference 的SAVE方法说明
			 *如果值为 String，则将其保存为 UTF-8 文本文件。 
			 *如果值为 XML，则会以 XML 格式将其写入到文本文件，并保留所有格式设置。 
			 *如果值为 ByteArray 对象，则会将其逐字写入到数据文件
			 * */
			//文件保存 ，鉴于String默认为UTF-8保存，转换成byte数组再保存
			if(fr.hasOwnProperty("save")){  
				var data:String = generateCsvDataBydataProvider(dataProvider,columns);
				var bytes:ByteArray =new ByteArray();
				bytes.writeMultiByte(data,"gb2312");				
				
				fr.save(bytes, fileName + '.csv');  
			}else{  
				SmartXMessage.show("当前flash player版本不支持此功能,请安装10.0.0以上版本！");  
			}
		}
		
		/**
		 *生成CSV格式文件 逗号"," 分割
		 * */
		public static function generateCsvData(dg:DataGrid, csvSeparator:String=",", lineSeparator:String="\n"):String
		{
			var data:String = "";
			var columns:Array = dg.columns;
			var columnCount:int = columns.length;
			var column:DataGridColumn;
			var header:String = "";
			var headerGenerated:Boolean = false;
			var dataProvider:Object = dg.dataProvider;
			var rowCount:int = dataProvider.length;
			var dp:Object = null;
			var cursor:IViewCursor = dataProvider.createCursor ();
			var j:int = 0;
			//loop through rows
			while (!cursor.afterLast)
			{
				var obj:Object = null;
				obj = cursor.current;
				//loop through all columns for the row
				for(var k:int = 0; k < columnCount; k++)
				{
					column = columns[k];
					//Exclude column data which is invisible (hidden)
					if(!column.visible)
					{
						continue;
					}
					
					//modify by zhangzy 20130107 对数据中的" 进行转义为""
					data += "\""+ StringUtils.StringReplaceAll(column.itemToLabel(obj),"\"","\"\"") + "\"";
					if(k < (columnCount -1))
					{
						data += csvSeparator;
					}
					//generate header of CSV, only if it's not genereted yet
					if (!headerGenerated)
					{
						header += "\"" + column.headerText + "\"";
						if (k < columnCount - 1)
						{
							header += csvSeparator;
						}
					}
				}
				headerGenerated = true;
				if (j < (rowCount - 1))
				{
					data += lineSeparator;
				}
				j++;
				cursor.moveNext ();
			}
			//set references to null:
			dataProvider = null;
			columns = null;
			column = null;
			
			return (header + "\r\n" + data);
		}
		
		/**
		 *生成CSV格式文件 逗号"," 分割
		 * */
		public static function generateCsvDataBydataProvider(dataProvider:ArrayCollection,columns:Array, csvSeparator:String=",", lineSeparator:String="\n"):String
		{
			var data:String = "";
			var columns:Array = columns;
			var columnCount:int = columns.length;
			var column:DataGridColumn;
			var header:String = "";
			var headerGenerated:Boolean = false;
//			var dataProvider:Object = dataProvider;
			var rowCount:int = dataProvider.length;
			var dp:Object = null;
			var cursor:IViewCursor = dataProvider.createCursor ();
			var j:int = 0;
			//loop through rows
			while (!cursor.afterLast)
			{
				var obj:Object = null;
				obj = cursor.current;
				//loop through all columns for the row
				for(var k:int = 0; k < columnCount; k++)
				{
					column = columns[k];
					//Exclude column data which is invisible (hidden)
					if(!column.visible)
					{
						continue;
					}
					//modify by zhangzy 20130107 对数据中的" 进行转义为""
					data += "\""+ StringUtils.StringReplaceAll(column.itemToLabel(obj),"\"","\"\"") + "\"";
					if(k < (columnCount -1))
					{
						data += csvSeparator;
					}
					//generate header of CSV, only if it's not genereted yet
					if (!headerGenerated)
					{
						header += "\"" + column.headerText + "\"";
						if (k < columnCount - 1)
						{
							header += csvSeparator;
						}
					}
				}
				headerGenerated = true;
				if (j < (rowCount - 1))
				{
					data += lineSeparator;
				}
				j++;
				cursor.moveNext ();
			}
			//set references to null:
			dataProvider = null;
			columns = null;
			column = null;
			
			return (header + "\r\n" + data);
		}
		
		/**
		 *生成CSV格式文件 逗号"," 分割
		 * */
		public static function generateCsvDataAdv(dg:AdvancedDataGrid, csvSeparator:String=",", lineSeparator:String="\n"):String
		{
			var data:String = "";
			var columns:Array = dg.columns;
			var columnCount:int = columns.length;
			var column:AdvancedDataGridColumn;
			var header:String = "";
			var headerGenerated:Boolean = false;
			var dataProvider:Object = dg.dataProvider;
			var rowCount:int = dataProvider.length;
			var dp:Object = null;
			var cursor:IViewCursor = dataProvider.createCursor ();
			var j:int = 0;
			//loop through rows
			while (!cursor.afterLast){
				var obj:Object = null;
				obj = cursor.current;
				//loop through all columns for the row
				for(var k:int = 0; k < columnCount; k++){
					column = columns[k];
					//Exclude column data which is invisible (hidden)
					if(!column.visible){
						continue;
					}
					//modify by zhangzy 20130107 对数据中的" 进行转义为""
					data += "\""+ StringUtils.StringReplaceAll(column.itemToLabel(obj),"\"","\"\"") + "\"";
					if(k < (columnCount -1)){
						data += csvSeparator;
					}
					//generate header of CSV, only if it's not genereted yet
					if (!headerGenerated){
						header += "\"" + column.headerText + "\"";
						if (k < columnCount - 1)
						{
							header += csvSeparator;
						}
					}
				}
				headerGenerated = true;
				if (j < (rowCount - 1)){
					data += lineSeparator;
				}
				j++;
				cursor.moveNext ();
			}
			//set references to null:
			dataProvider = null;
			columns = null;
			column = null;
			
			return (header + "\r\n" + data);
		}
	}
}