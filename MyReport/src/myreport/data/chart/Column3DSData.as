package myreport.data.chart
{
	import hlib.XmlWriter;

	public class Column3DSData extends Column3DData
	{
		public function Column3DSData()
		{
			super();
			Type = "Column3DS";
		}
		
		public override function Clone():*
		{
			var clone:Column3DSData = new Column3DSData();
			CloneTo(clone);
			return clone;
		}
	}
}