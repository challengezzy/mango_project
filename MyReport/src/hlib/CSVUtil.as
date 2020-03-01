/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


CSV格式处理工具栏


*/
package hlib
{
	import flash.utils.Dictionary;

	public final class CSVUtil
	{
		/**
		 * 是否包含CSV关键字符",
		 */
		public static function HasCSV(value:String):Boolean
		{
			if(!value)
				return false;
			for(var i:int=0; i<value.length; i++)
			{
				var char:String = value.charAt(i);
				switch(char)
				{
					case "\"":
					case ",":
						return true;
				}
			}
			return false;
		}
		/**
		 * 替换"字符
		 */
		public static function EscapeCSV(value:String, check:Boolean = true):String
		{
			if (check && !HasCSV(value))
			{
				return value;
			}
			return value.replace(/\"/g,"\"\"");
		}
		
		public static function ReadCells(line:String, start:int = 0):Array
		{
			var cells:Array = new Array();
			var index:Object = {start:start};
			while(index.start<line.length)
			{
				var value:String = NextCell(line, index);
				cells.push(value);
			}
			
			return cells;
		}
		
		/**
		 * @param index:{start:int}
		 */ 
		public static function NextCell(line:String, index:Object):String
		{
			var cell:String = "";
			var end:int;
			if(index.start<line.length)
			{
				end = IndexOfCell(line, index.start);
				if(end<0)
				{
					cell = line.substring(index.start);
					index.start = line.length;
				}
				else
				{
					cell = line.substring(index.start, end);
					index.start = end+1;
				}
			}
			if(cell)
			{
				if(cell.charAt(0) == "\"" && cell.charAt(cell.length-1) == "\"")
					cell = cell.substring(1, cell.length-1);
			}
			if(cell)
				return StringUtil.Replace(cell, "\"\"", "\"");
			return cell;
		}
		
		private static function IndexOfCell(line:String, start:int):int
		{
			
			var count:int = 0;//引号数量
			var length:int = line.length;
			for(var i:int=start; i<length; i++)
			{
				var char:String = line.charAt(i);
				if(char == "\"")
					count ++;
				else if(char == "," && count%2==0)
				{
					return i;
				}
			}
			return -1;
		}
		/**
		 * CSV的第一行是列头文本，第二行开始是数据
		 * @return: {Columns:Array, Table:Array}
		 */ 
		public static function CSVToArray(text:String):Object
		{
			var columns:Array = new Array();
			var table:Array = new Array();
			
			if(!text)
				return {Columns:columns, Table:table};
			
			var start:int, end:int;			
			var len:int = text.length;
			var line:String;
			start = end = 0;
			var readHeader:Boolean = false;
			while(start < len)
			{
				end = text.indexOf("\r\n", start);
				if(end > start)
				{
					line = text.substring(start, end);
					start = end + "\r\n".length;
				}
				else
				{
					line = text.substring(start);
					start = len;
				}
				if(line)
				{
					if(!readHeader)
					{
						readHeader = true;
						columns = ReadCells(line);
//						trace(columns);
					}
					else
					{
						var cells:Array = ReadCells(line);
//						trace(cells);
						var obj:Dictionary = new Dictionary();
						for(var c:int=0;c<columns.length;c++)
						{
							var name:String = columns[c];
							if(c < cells.length)
								obj[name] = cells[c];
						}
						table.push(obj);
					}
				}
			}
			
			return {Columns:columns, Table:table};
		}
	}
}