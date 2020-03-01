/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


序列。


*/
package myreport.data.chart
{
	import hlib.CloneUtil;
	import hlib.DisposeUtil;
	import hlib.ItemData;
	import hlib.XmlWriter;

	public final class SeriesData extends ItemData
	{
		public var Name:String = "系列";
		public var Values:Array = new Array();
		override protected function Disposing():void
		{
 			DisposeUtil.Dispose(Values);
			Values = null;
			super.Disposing();
		}
		
		public function SeriesData(xml:XML = null)
		{
			super();
			FromXML(xml);
		}
		override public function Clone():*
		{
			var clone:SeriesData = new SeriesData();
			clone.Name = Name;
			clone.Values = CloneUtil.Clone(Values);
			return clone;
		}
		override public function FromXML(xml:XML):void
		{
			if (!xml)
				return;
			Name = xml.@name;
			for each(var item:XML in xml.Value)
			{
				Values.push(String(item));
			}
		}
		override public function ToXML():String
		{
			var xml:XmlWriter = new XmlWriter();
			xml.BeginEle("Series");
			xml.Attr("name", Name);
			xml.EndEle();
			for each(var value:String in Values)
			{
				xml.Ele("Value", value);
			}
			
			xml.End("Series");
			return xml.toString();
		}

	}
}