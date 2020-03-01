/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：

表达式引擎。


*/
package myreport.expression
{
	import hlib.ExpressionInterpreter;
	
	import mx.collections.ArrayCollection;
	
	public final class ExpressionEngine
	{
		public static function IsExpression(expression:String):Boolean
		{
			return hlib.ExpressionInterpreter.IsExpression(expression);
		}
		
		public static function Invoke(expression:String, context:ExpressionContext):Object
		{

			var result:Object = MyReportExpressionInterpreter.MyReportInstance.Invoke(expression, context);
			//MyReportExpressionInterpreter.MyReportInstance.Clear();

			return result;
		}
		
		public static function InvokeByData(expression:String, table:ArrayCollection, parameters:Object):Object
		{
			var context:ExpressionContext = new ExpressionContext();
			context.Table = table;
			context.Parameters = parameters;
			var result:Object = MyReportExpressionInterpreter.MyReportInstance.Invoke(expression, context);
			//MyReportExpressionInterpreter.MyReportInstance.Clear();
			
			return result;
		}
	}
}