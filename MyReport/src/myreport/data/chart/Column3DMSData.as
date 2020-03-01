package myreport.data.chart
{
	import hlib.XmlWriter;

	public class Column3DMSData extends Column3DData
	{
		public function Column3DMSData()
		{
			super();
			Type = "Column3DMS";
		}
		
		public override function Clone():*
		{
			var clone:Column3DMSData = new Column3DMSData();
			CloneTo(clone);
			return clone;
		}
	}
}