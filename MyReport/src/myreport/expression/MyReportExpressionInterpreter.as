/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：

表达式解析器。


*/
package myreport.expression
{
 
	import hlib.Expression;
	import hlib.ExpressionInterpreter;
	import hlib.MethodExpression;
	import hlib.StringUtil;
	
	internal class MyReportExpressionInterpreter extends ExpressionInterpreter
	{
 
		private static var _SupportedMyReportMethods:Array = GetSupportedMyReportMethods();
		private static var _SupportedMyReportStringMethods:Array = [MethodExpression.STRING_METHOD];
		internal static var MyReportInstance:MyReportExpressionInterpreter = new MyReportExpressionInterpreter();
		
		private static function GetSupportedMyReportMethods():Array
		{
			var methods:Array = new Array();
			for(var name:String in MyReportMethodExpression.SupportedMyReportMethods)
			{
				methods.push(name);
			}
			return methods;
		}
		
		public function MyReportExpressionInterpreter()
		{
			super();
			_HandleError = false;
		}
		
		protected override function get SupportedMethods():Array
		{
			return _SupportedMyReportMethods;
			
		}
		protected override function get SupportedStringMethods():Array
		{
			return _SupportedMyReportStringMethods;
		}
		
		protected override function ValidateOperator(operator:String, exp:String, start:int):Boolean
		{
			if(operator == "!")
			{
				var key:String;
				key = "Parameters!";
				if(start>=key.length-1)
				{
					if(exp.substr(start-key.length+1, key.length) == key)
						return false;
				}
				key = "Fields!";
				if(start>=key.length-1)
				{
					if(exp.substr(start-key.length+1, key.length) == key)
						return false;
				}
			}
 
			return true;
		}
		
		protected override function CreateMethodExpression(method:String, arguments:Array):Expression
		{
			var exp:Expression = new MyReportMethodExpression(method, arguments);
			return exp;
		}
 
		protected override function CreateValueExpression(expression:String):Expression
		{
			return new ValueExpression(expression);
 
		}
	}
}