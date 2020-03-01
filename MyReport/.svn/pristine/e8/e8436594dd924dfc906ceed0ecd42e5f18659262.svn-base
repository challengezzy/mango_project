/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表数据。


*/

package myreport.data.reportdata
{
	import hlib.DisposeUtil;
	import hlib.LoadCounter;
	
	internal final class ReportData extends ItemData
	{
		/** item=>SetData */
		public var Parameters:Array = new Array();
		public var Table:TableData;
		
		override protected function Disposing():void
		{
			DisposeUtil.Dispose(Parameters);
			Table.Dispose();
			Parameters = null;
			Table = null;
			super.Disposing();
		}
		
		public function ReportData(counter:LoadCounter, xml:XML = null)
		{
			super();
 			Counter = counter;
			Table = new TableData(counter);
			FromXML(xml);
		}
		
		override public function FromXML(xml:XML):void
		{
			if (!xml)
				return;
 
			var item:XML;
			if(xml.Parameters.length())
			{
				for each(item in xml.Parameters.Set)
				{
					var set:SetData = new SetData(Counter, item);
					Parameters.push(set);
				}
			}
			
			if(xml.Table.length())
			{
				Table = new TableData(Counter, XML(xml.Table));
			}
		}
		

	}
}