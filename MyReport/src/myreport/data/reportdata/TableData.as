/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表数据——表格数据。


*/

package myreport.data.reportdata
{
	
	import hlib.CloneUtil;
	import hlib.DisposeUtil;
	import hlib.LoadCounter;
	
	internal final class TableData extends ItemData
	{
		/** item=>Array */
		public var Rows:Array = new Array();
		
		override protected function Disposing():void
		{
			DisposeUtil.Dispose(Rows);
			Rows = null;
			super.Disposing();
		}
		
		public function TableData(counter:LoadCounter, xml:XML = null)
		{
			super();
			Counter = counter;
			FromXML(xml);
		}
		
		override public function FromXML(xml:XML):void
		{
			if (!xml)
				return;
			
			for each(var rowXML:XML in xml.Row)
			{
				var row:Array = new Array();
				for each(var setXML:XML in rowXML.Set)
				{
					var set:SetData = new SetData(Counter, setXML);
					row.push(set);
				}
				Rows.push(row);
			}
		}
		override public function Clone():*
		{
			var clone:TableData = new TableData(Counter);
			clone.Rows = CloneUtil.Clone(Rows);
			return clone;
		}
		
	}
}