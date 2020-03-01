package hlib
{
	import flash.utils.Dictionary;

	internal final class OperatorUtil
	{
		//定义运算符优先级
		public static var OperatorsPriority:Dictionary = CreateOperatorsPriority();
		
		private static function CreateOperatorsPriority():Dictionary
		{
			//初始化运算符合和优先级
			var dict:Dictionary = new Dictionary();
			
			dict["-N"] = -1;//负数
			dict["+N"] = -1;//正数
			dict["!"] = -1;//取非
			
			dict["^"] = -2;//幂运算
			
			dict["*"] = -3;
			dict["/"] = -3;
			dict["%"] = -3;//取余
			
			dict["+"] = -4;
			dict["-"] = -4;
			
			dict["<"] = -5;
			dict["<="] = -5;
			dict[">"] = -5;
			dict[">="] = -5;
			
			dict["=="] = -6;
			dict["!="] = -6;
			
			dict["&&"] = -7;
			
			dict["||"] = -8;
			
			return dict;
		}
		public static function InterpretOperator(exp:String, start:int):String
		{
			if(!exp) 
				return null;
			var op:String = exp.substr(start, 2);
			switch(op)
			{
				case "==":
				case "!=":
				case ">=":
				case "<=":
				case "&&":
				case "||":
					return op;
			}
			
			op = exp.substr(start, 1);
			
			switch(op)
			{
				case "(":
				case ")":
				case "+":
				case "-":
				case "*":
				case "/":
				case "%":
				case "^":
				case ">":
				case "<":
				case "!":
				case "\""://字符串定义
				case "'"://字符串定义
					return op;
			}
			return null;
		}
		public static function InterpretStringOperator(exp:String, start:int):String
		{
			if(!exp) 
				return null;
			var op:String = exp.substr(start, 1);
			switch(op)
			{
				case "\""://字符串定义
				case "'"://字符串定义
					return op;
			}
			return null;
		}
		public static function IsSingleOperator(op:String):Boolean
		{
			return op == "-N" || op == "+N" || op == "!";
		}
		public static function AdjustOperator(op:String, hasLeft:Boolean):String
		{
			if (!hasLeft)
			{
				if (op == "-")
					return "-N";
				else if (op == "+")
					return "+N";
			}
 
			return StringUtil.Trim(op).toLowerCase();
		}
	}
}