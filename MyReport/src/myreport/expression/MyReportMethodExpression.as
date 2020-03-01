/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


函数调用表达式

*/

package myreport.expression
{
	
	import hlib.DisposeUtil;
	import hlib.Expression;
	import hlib.MathUtil;
	import hlib.MethodExpression;
	import hlib.Parser;
	import hlib.StringUtil;
	
	import mx.collections.ArrayCollection;
	
	import myreport.data.report.ReportSettings;
	import myreport.data.report.TableColumnSetting;
	import myreport.render.RenderUtil;
	import myreport.report.ReportRender2;
	
	internal class MyReportMethodExpression extends MethodExpression
	{
		internal static var SupportedMyReportMethods:Object = GetSupportedMyReportMethods();
		
		private static function GetSupportedMyReportMethods():Object
		{
			var methods:Object = {};
			for(var key:String in SupportedMethods)
			{
				methods[key] = SupportedMethods[key];
			}
			//【杂项函数】
			AddMethod(methods, "Not", Not);//取非：Not(exp)
			AddMethod(methods, "RowCount", RowCount);//总行数：RowCount()
			AddMethod(methods, "RowIndex", RowIndex);//行索引：RowIndex()
			AddMethod(methods, "RowNumber", RowNumber);//行号：RowNumber()
			AddMethod(methods, "GroupIndex", GroupIndex);//
			AddMethod(methods, "GroupNumber", GroupNumber);//
			AddMethod(methods, "GroupRowIndex", GroupRowIndex);//分组行索引：GroupRowIndex()
			AddMethod(methods, "GroupRowNumber", GroupRowNumber);//分组行号：GroupRowNumber()
			AddMethod(methods, "GroupStartIndex", GroupStartIndex);//分组开始索引：GroupStartIndex()
			AddMethod(methods, "GroupEndIndex", GroupEndIndex);//分组结束索引：GroupEndIndex()
			AddMethod(methods, "Val", Val);//当前值：Val()
			AddMethod(methods, "Current", Val);//兼容代码
			AddMethod(methods, "ColumnIndex", ColumnIndex);
			AddMethod(methods, "DynamicField", DynamicField);
			AddMethod(methods, "DynamicText", DynamicText);
			AddMethod(methods, "DynamicValue", DynamicValue);
			
			//【控件显示函数】
			AddMethod(methods, "ToImage", ToImage);//显示图形：ToImage(exp)
			AddMethod(methods, "ToCode128B", ToCode128B);//显示一维码(Code128B)：ToCode128B(exp)
			AddMethod(methods, "ToEAN8", ToEAN8);//显示一维码(EAN8)：ToEAN8(exp)
			AddMethod(methods, "ToEAN13", ToEAN13);//显示一维码(EAN13)：ToEAN13(exp)
			AddMethod(methods, "ToReport", ToReport);//显示子报表：ToReport(style, table, parameters)
			//【统计函数】
			AddMethod(methods, "Sum", Sum);//累加：Sum(field, start, end)
			AddMethod(methods, "SumIf", SumIf);//条件累加：SumIf(field, cond, start, end)
			AddMethod(methods, "Get", Get);
			AddMethod(methods, "Avg", Avg);//求平均：Avg(field, start, end)
			AddMethod(methods, "AvgIf", AvgIf);//	条件求平均：AvgIf(field, cond, start, end)
			AddMethod(methods, "CountIf", CountIf);//条件计数：CountIf(cond, start, end)
			AddMethod(methods, "Max", Max);//最大值：Max(field, start, end)
			AddMethod(methods, "Min", Min);//最小值：Min(field, start, end)
			
			//【数据源函数】
			AddMethod(methods, "Series", Series);//创建序列：Series(field)
			AddMethod(methods, "GetValue", GetValue);
			
			return methods;
		}
		
		/**
		 * arguments：函数参数，存储Expression对象
		 */ 
		public function MyReportMethodExpression(method:String, arguments:Array)
		{
			super(method, arguments);
		}
		
		public override function Execute(context:*):*
		{
			var func:Function = SupportedMyReportMethods[_Method];//通过函数表查询处理方法
			return func(context, _Arguments);
		}
		
		/*
		【杂项函数】
		取非：Not(exp)
		总行数：RowCount()
		行索引：RowIndex()
		行号：RowNumber()
		分组行索引：GroupRowIndex()
		分组行号：GroupRowNumber()
		分组开始索引：GroupStartIndex()
		分组结束索引：GroupEndIndex()
		当前值：Current()/Val()
		*/
		private static function Not(context:ExpressionContext, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("Not函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			var result:Boolean = !Parser.ParseBoolean(exp.Execute(context));
			
			return Number(result);
		}		
		private static function RowCount(context:ExpressionContext, args:Array):Object
		{
			if(context.Table)
				return context.Table.length;
			return 0;
		}
		private static function RowIndex(context:ExpressionContext, args:Array):Object
		{
			if(isNaN(context.RowIndex)) 
				return 0;
			return context.RowIndex;
		}
		private static function RowNumber(context:ExpressionContext, args:Array):Object
		{
			return Number(RowIndex(context, args)) + 1;
		}
		private static function GroupIndex(context:ExpressionContext, args:Array):Object
		{
			return context.GroupIndex;
		}
		private static function GroupNumber(context:ExpressionContext, args:Array):Object
		{
			return context.GroupIndex + 1;
		}
		private static function GroupRowIndex(context:ExpressionContext, args:Array):Object
		{
			if(isNaN(context.GroupRowIndex)) 
				return 0;
			return context.GroupRowIndex;
		}
		private static function GroupRowNumber(context:ExpressionContext, args:Array):Object
		{
			return Number(GroupRowIndex(context, args)) + 1;
		}		
		private static function GroupStartIndex(context:ExpressionContext, args:Array):Object
		{
			if(isNaN(context.GroupStartIndex)) 
				return 0;
			return context.GroupStartIndex;
		}
		private static function GroupEndIndex(context:ExpressionContext, args:Array):Object
		{
			if(isNaN(context.GroupEndIndex)) 
				return 0;
			return context.GroupEndIndex;
		}
		private static function Val(context:ExpressionContext, args:Array):Object
		{
			return context.Current;
		}
		
		private static function ColumnIndex(context:ExpressionContext, args:Array):Object
		{
			return context.ColumnIndex;
		}
		private static function DynamicField(context:ExpressionContext, args:Array):Object
		{
			if(context.Settings && context.ColumnIndex<context.Settings.TableColumnSettings.length)
			{
				var col:TableColumnSetting = context.Settings.TableColumnSettings[context.ColumnIndex];
				return col.Field;
			}
			return "";
		}
		private static function DynamicText(context:ExpressionContext, args:Array):Object
		{
			if(context.Settings && context.ColumnIndex<context.Settings.TableColumnSettings.length)
			{
				var col:TableColumnSetting = context.Settings.TableColumnSettings[context.ColumnIndex];
				if(col.Text)
					return col.Text;
				return col.Field;
			}
			return "";
		}
		private static function DynamicValue(context:ExpressionContext, args:Array):Object
		{
			var rowIndex:int = context.RowIndex;
			if(context.Settings && context.ColumnIndex<context.Settings.TableColumnSettings.length)
			{
				var col:TableColumnSetting = context.Settings.TableColumnSettings[context.ColumnIndex];
				if(rowIndex>=0 && rowIndex<=context.Table.length)
				{
					var row:Object = context.Table[rowIndex];
					if(row && row.hasOwnProperty(col.Field))
						return row[col.Field];
				}
			}
			return "";
		}
		/*
		【控件显示函数】
		*/
		private static function ToImage(context:ExpressionContext, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("ToImage函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			return RenderUtil.CreateImage(exp.Execute(context));
		}
		private static function ToCode128B(context:ExpressionContext, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("ToCode128B函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			return RenderUtil.CreateBarCode128B(exp.Execute(context));
		}
		private static function ToEAN8(context:ExpressionContext, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("ToEAN8函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			return RenderUtil.CreateBarEan8(exp.Execute(context));
		}		
		private static function ToEAN13(context:ExpressionContext, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("ToEAN13函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			return RenderUtil.CreateBarEan13(exp.Execute(context));
		}
		private static function ToReport(context:ExpressionContext, args:Array):Object
		{
			var table:ArrayCollection;
			var parameters:Object;
			var style:ReportSettings;
			
			if(args.length>0)
			{
				var value:Object = Expression(args[0]).Execute(context);
				if(value is ReportSettings)
					style = value as ReportSettings;
				else
					style = new ReportSettings(new XML(value));
			}
			if(args.length>1)
				table =  Expression(args[1]).Execute(context) as ArrayCollection;
			if(args.length>2)
				parameters = Expression(args[2]).Execute(context);
			
			if(!style)
				style = new ReportSettings(null);
			
			style.CanGrow = true;
			style.TableData = table;
			style.ParameterData = parameters;
			
			var pages:Array = ReportRender2.RenderPages(style); 
			if(pages.length)
			{
				//trace("subreport:", pages[0].width, pages[0].height);
				return pages[0];
			}
			
			return null;
		}
		
		
		/*
		【统计函数】
		累加：Sum(field, start, end)
		条件累加：SumIf(field, cond, start, end)
		求平均：Avg(field, start, end)
		条件求平均：AvgIf(field, cond, start, end)
		条件计数：CountIf(cond, start, end)
		最大值：Max(field, start, end)
		最小值：Min(field, start, end)
		*/
		private static function Sum(context:ExpressionContext, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("Sum函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			var table:ArrayCollection = context.Table;
			var result:Number = 0;
			if(table)
			{
				var start:int = 0;
				var end:int = table.length - 1;
				
				if(args.length > 1)
				{
					var from:Number = Number(Expression(args[1]).Execute(context));
					if(!isNaN(from))
						start = from;
				}
				if(args.length > 2)
				{
					var to:Number = Number(Expression(args[2]).Execute(context));
					if(!isNaN(to))
						end = Math.min(end, to);
				}
				var rowContext:ExpressionContext = context.Clone();
				for (; start <= end; start++)
				{
					rowContext.RowIndex = start;
					var value:Number = Number(exp.Execute(rowContext));
					result += value;
				}
				rowContext.Dispose();
			}
			return result;
		}
		
		private static function SumIf(context:ExpressionContext, args:Array):Object
		{
			if(args.length < 2)
				throw new Error("SumIf函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			var table:ArrayCollection = context.Table;
			var result:Number = 0;
			if(table)
			{
				var start:int = 0;
				var end:int = table.length - 1;
				
				if(args.length > 2)
				{
					var from:Number = Number(Expression(args[2]).Execute(context));
					if(!isNaN(from))
						start = from;
				}
				if(args.length > 3)
				{
					var to:Number = Number(Expression(args[3]).Execute(context));
					if(!isNaN(to))
						end = Math.min(end, to);
				}				
				var cond:Expression = args[1];
				var condContext:ExpressionContext = context.Clone();
				var rowContext:ExpressionContext = context.Clone();
				for (; start <= end; start++)
				{
					rowContext.RowIndex = start;
					var value:Number = Number(exp.Execute(rowContext));
					condContext.RowIndex = start;
					condContext.Current = value;
					if(Parser.ParseBoolean(cond.Execute(condContext)))
					{
						result += value;
					}
				}
				condContext.Dispose();
				rowContext.Dispose();
			}
			return result;
		}
		
		private static function Get(context:ExpressionContext, args:Array):Object
		{
			if(args.length < 2)
				throw new Error("Get函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			var table:ArrayCollection = context.Table;
			var result:Object = null;
			if(table)
			{
				var start:int = 0;
				var end:int = table.length - 1;
				
				if(args.length > 2)
				{
					var from:Number = Number(Expression(args[2]).Execute(context));
					if(!isNaN(from))
						start = from;
				}
				if(args.length > 3)
				{
					var to:Number = Number(Expression(args[3]).Execute(context));
					if(!isNaN(to))
						end = Math.min(end, to);
				}				
				var cond:Expression = args[1];
				var condContext:ExpressionContext = context.Clone();
				var rowContext:ExpressionContext = context.Clone();
				for (; start <= end; start++)
				{
					rowContext.RowIndex = start;
					var value:Object = exp.Execute(rowContext);
					condContext.RowIndex = start;
					condContext.Current = value;
					if(Parser.ParseBoolean(cond.Execute(condContext)))
					{
						result = value;
						break;
					}
				}
				condContext.Dispose();
				rowContext.Dispose();
			}
			return result;
		}
		
		private static function Avg(context:ExpressionContext, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("Avg函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			var table:ArrayCollection = context.Table;
			var total:int = 0;
			var result:Number = 0;
			if(table)
			{
				var start:int = 0;
				var end:int = table.length - 1;
				
				if(args.length > 1)
				{
					var from:Number = Number(Expression(args[1]).Execute(context));
					if(!isNaN(from))
						start = from;
				}
				if(args.length > 2)
				{
					var to:Number = Number(Expression(args[2]).Execute(context));
					if(!isNaN(to))
						end = Math.min(end, to);
				}					
				var rowContext:ExpressionContext = context.Clone();
				for (; start <= end; start++)
				{
					rowContext.RowIndex = start;
					var value:Number = Number(exp.Execute(rowContext));
					result += value;
					total ++;
				}
				rowContext.Dispose();
			}
			if(total)
				return result / total;
			else
				return 0;
			
		}
		
		private static function AvgIf(context:ExpressionContext, args:Array):Object
		{
			if(args.length < 2)
				throw new Error("AvgIf函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			var table:ArrayCollection = context.Table;
			var total:int = 0;
			var result:Number = 0;
			if(table)
			{
				var start:int = 0;
				var end:int = table.length - 1;
				
				if(args.length > 2)
				{
					var from:Number = Number(Expression(args[2]).Execute(context));
					if(!isNaN(from))
						start = from;
				}
				if(args.length > 3)
				{
					var to:Number = Number(Expression(args[3]).Execute(context));
					if(!isNaN(to))
						end = Math.min(end, to);
				}					
				
				var cond:Expression = args[1];
				var condContext:ExpressionContext = context.Clone();
				var rowContext:ExpressionContext = context.Clone();
				for (; start <= end; start++)
				{
					rowContext.RowIndex = start;
					var value:Number = Number(exp.Execute(rowContext));
					condContext.RowIndex = start;
					condContext.Current = value;
					if(Parser.ParseBoolean(cond.Execute(condContext)))
					{
						result += value;
						total ++;
					}
				}
				condContext.Dispose();
				rowContext.Dispose();
			}
			
			if(total)
				return result / total;
			else
				return 0;
			
		}
		
		private static function CountIf(context:ExpressionContext, args:Array):Object
		{
			if(args.length < 1)
				throw new Error("CountIf函数缺少参数，当前参数长度：" + args.length + "。");
			
			var table:ArrayCollection = context.Table;
			var start:int = 0;
			var end:int = table.length - 1;
			
			if(args.length > 1)
			{
				var from:Number = Number(Expression(args[1]).Execute(context));
				if(!isNaN(from))
					start = from;
			}
			if(args.length > 2)
			{
				var to:Number = Number(Expression(args[2]).Execute(context));
				if(!isNaN(to))
					end = Math.min(end, to);
			}
			var count:int = 0;
			if(table)
			{
				var cond:Expression = args[0];
				var condContext:ExpressionContext = context.Clone();
				for (; start <= end; start++)
				{
					condContext.RowIndex = start;
					if(Parser.ParseBoolean(cond.Execute(condContext)))
					{
						count ++;
					}
				}
				condContext.Dispose();
			}
			
			return count;
		}
		
		private static function Max(context:ExpressionContext, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("Max函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			var table:ArrayCollection = context.Table;
			var result:Number;
			if(table)
			{
				var start:int = 0;
				var end:int = table.length - 1;
				
				if(args.length > 1)
				{
					var from:Number = Number(Expression(args[1]).Execute(context));
					if(!isNaN(from))
						start = from;
				}
				if(args.length > 2)
				{
					var to:Number = Number(Expression(args[2]).Execute(context));
					if(!isNaN(to))
						end = Math.min(end, to);
				}					
				var rowContext:ExpressionContext = context.Clone();
				for (; start <= end; start++)
				{
					rowContext.RowIndex = start;
					var value:Number = Number(exp.Execute(rowContext));
					if(!isNaN(value))
					{
						if(isNaN(result))
							result = value;
						else
							result = Math.max(result, value);
					}
				}
				rowContext.Dispose();
			}
			
			return result;
			
		}
		
		private static function Min(context:ExpressionContext, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("Min函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			
			var table:ArrayCollection = context.Table;
			var result:Number;
			if(table)
			{
				var start:int = 0;
				var end:int = table.length - 1;
				
				if(args.length > 1)
				{
					var from:Number = Number(Expression(args[1]).Execute(context));
					if(!isNaN(from))
						start = from;
				}
				if(args.length > 2)
				{
					var to:Number = Number(Expression(args[2]).Execute(context));
					if(!isNaN(to))
						end = Math.min(end, to);
				}					
				var rowContext:ExpressionContext = context.Clone();
				for (; start <= end; start++)
				{
					rowContext.RowIndex = start;
					var value:Number = Number(exp.Execute(rowContext));
					if(!isNaN(value))
					{
						if(isNaN(result))
							result = value;
						else
							result = Math.min(result, value);
					}
				}
				rowContext.Dispose();
			}
			
			return result;
			
		}
		/*
		【数据源函数】
		*/
		private static function Series(context:ExpressionContext, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("Series函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			
			var table:ArrayCollection = context.Table;
			var values:Array = new Array();
			if(table)
			{
				var start:int = 0;
				var end:int = table.length - 1;				
				var rowContext:ExpressionContext = context.Clone();
				for (; start <= end; start++)
				{
					rowContext.RowIndex = start;
					var value:Object = exp.Execute(rowContext);
					values.push(value);
				}
				rowContext.Dispose();
			}
			
			return values;
			
		}
		private static function GetValue(context:ExpressionContext, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("GetValue函数缺少参数，当前参数长度：" + args.length + "。");
			var field:String = String(Expression(args[0]).Execute(context));
			var rowIndex:int = context.RowIndex;
			if(args.length>1)
				rowIndex = int(Expression(args[1]).Execute(context));
			
			if(rowIndex>=0 && rowIndex<=context.Table.length)
			{
				var row:Object = context.Table[rowIndex];
				if(row && row.hasOwnProperty(field))
					return row[field];
			}
			return "";
		}
	}
}