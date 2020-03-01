/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


值处理表达式，支持调用：
【值绑定】
绑定表格数据：	Fields!列名称.Value
绑定参数数据：	Parameters!参数名称.Value
常量字符串：		常量名称

*/
package myreport.expression
{
	import hlib.Expression;
	import hlib.StringUtil;
	
	import mx.collections.ArrayCollection;

	internal class ValueExpression extends Expression
	{
	 
		private var _Value:String;
		public function ValueExpression(value:String)
		{
			super();
			_Value = value;
		}
 
		public override function Execute(context:*):*
		{
			var ec:ExpressionContext = context;
 
			var value:String = "";
			
			if(StringUtil.StartsAndEndsWidth(_Value, "Fields!", ".Value"))
			{
				value = StringUtil.Trim(StringUtil.SliceWdith(_Value, "Fields!", ".Value"));
		 
				var table:ArrayCollection = ec.Table;
				var rowIndex:Number = ec.RowIndex;
				if(isNaN(rowIndex))
					rowIndex = 0;
				if(table && rowIndex < table.length)
				{
					var row:Object = table[rowIndex];
					if(row && row.hasOwnProperty(value))
						return row[value];
				}
				return null;
			}
			else if(StringUtil.StartsAndEndsWidth(_Value, "Parameters!", ".Value"))
			{
				value = StringUtil.Trim(StringUtil.SliceWdith(_Value, "Parameters!", ".Value"));
				var parameters:Object = ec.Parameters;
				if(parameters && parameters.hasOwnProperty(value))
					return parameters[value];
				return null;
			}
 
			return _Value;
		}
		public function toString():String
		{
			return _Value;
		}
	}
}