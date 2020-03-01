/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


表达式引擎参数。


*/
package myreport.expression
{
	import hlib.IClone;
	import hlib.IDispose;
	
	import mx.collections.ArrayCollection;
	
	import myreport.data.report.ReportSettings;

	public class ExpressionContext implements IClone, IDispose
	{

		public var Table:ArrayCollection;
		public var Parameters:Object;
		
		public var Settings:ReportSettings;
		public var ColumnIndex:int;
		
		public var RowIndex:int;
		public var GroupRowIndex:int;
		public var GroupStartIndex:int;
		public var GroupEndIndex:int;	
		public var GroupIndex:int;
		public var Current:Object;
		
		public function ExpressionContext()
		{
		}
		
		public function get CurrentRow():Object
		{
			var rowIndex:int = RowIndex;
			if(isNaN(rowIndex))
				rowIndex = 0;
			if(Table && rowIndex < Table.length)
			{
				return Table[rowIndex];
			}
			return null;
		}
		
		public function Clone():*
		{
			var clone:ExpressionContext = new ExpressionContext();
			clone.Settings = Settings;
			clone.Table = Table;
			clone.Parameters = Parameters;
			clone.Current = Current;
			clone.ColumnIndex = ColumnIndex;
			clone.RowIndex = RowIndex;
			clone.GroupRowIndex = GroupRowIndex;
			clone.GroupStartIndex = GroupStartIndex;
			clone.GroupEndIndex = GroupEndIndex;
			return clone;
		}
		
		//================IDispose====================
		private var _Disposed:Boolean = false;	
		public function Dispose():void
		{
			if(_Disposed) return;
			_Disposed = true;

			Table = null;
			Parameters = null;
			Current = null;
		}
	}
}