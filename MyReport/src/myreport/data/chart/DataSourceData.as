/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


数据源。


*/
package myreport.data.chart
{
	import flash.utils.Dictionary;
	
	import hlib.CloneUtil;
	import hlib.DisposeUtil;
	import hlib.ItemData;
	import hlib.XmlWriter;

	public final class DataSourceData extends ItemData
	{
		public var Serieses:Array = new Array();
		override protected function Disposing():void
		{
			DisposeUtil.Dispose(Serieses);
			Serieses = null;
			super.Disposing();
		}
		
		public function DataSourceData(xml:XML = null)
		{
			super();
			FromXML(xml);
		}
		override public function Clone():*
		{
			var clone:DataSourceData = new DataSourceData();
			clone.Serieses = CloneUtil.Clone(Serieses);
			return clone;
		}
		override public function FromXML(xml:XML):void
		{
			if (!xml)
				return;
			if(xml.Serieses.length())
			{
				for each(var item:XML in xml.Serieses.Series)
				{
					var series:SeriesData = new SeriesData(item);
					Serieses.push(series);
				}
			}
		}
		override public function ToXML():String
		{
			var xml:XmlWriter = new XmlWriter();
			xml.Begin("DataSource");
			xml.WriteObject(Serieses, "Serieses");
			xml.End("DataSource");
			return xml.toString();
		}

	}
}