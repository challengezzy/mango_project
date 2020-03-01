package myreport.data.report
{
	
	import hlib.DisposeUtil;
	import hlib.Hash;
	
	import myreport.data.chart.ChartData;
	import myreport.data.chart.Column3DData;
	import myreport.data.chart.Column3DMSData;
	import myreport.data.chart.Column3DSData;
	import myreport.data.chart.Line3DData;
	import myreport.data.chart.Line3DMSData;
	import myreport.data.chart.Pie3DData;
	
	public class ControlSetting extends ItemSetting
	{
		public var Type:String = "";
		private var _Styles:Hash = new Hash();//样式字典
		public var Chart:ChartData;
		override protected function Disposing():void
		{
			DisposeUtil.Dispose(_Styles);
			_Styles = null;
			if(Chart)
			{
				Chart.Dispose();
				Chart = null;
			}
			super.Disposing();
		}
		
		public function ControlSetting(xml:XML = null)
		{
			super();
			FromXML(xml);
		}
		
		//======================控件样式=========================
		public function ClearStyles():void
		{
			_Styles.Clear();
		}
		public function HasStyle(name:String):Boolean
		{
			return _Styles.ContainsKey(name);
		}
		public function get StyleNames():Array
		{
			return _Styles.Keys;
		}
		public function GetStyle(name:String, defaultValue:* = undefined):*
		{
			if(!HasStyle(name))
				return defaultValue;
			var value:* = _Styles.Get(name);
			if(name.charAt(0) == "b")
				return ReadBoolean(value);
			return value;
		}
		public function SetStyle(name:String, value:*):void
		{
			_Styles.Set(name, value);
		}
		
		//================IClone====================
		override public function Clone():*
		{
			var clone:ControlSetting = new ControlSetting();
			clone.Type = Type;
			clone._Styles = _Styles.Clone();
			if(Chart)
				clone.Chart = Chart.Clone();
			return clone;
		}
		
		override public function FromXML(xml:XML):void
		{
			if (!xml)
				return;
			Type = xml.@type;
			var child:XML;
			if(xml.Set.length())
			{
				for each(child in xml.Set)
				{
					var key:String = child.@name;
					var value:* = String(child.@value);
					if(!value)
						value = String(child.text());
					
					if(key.charAt(0) == "c")
						value = ReadColor(value);
					else if(key.charAt(0) == "b")
						value = ReadBoolean(value);
					_Styles.Set(key, value);
				}
			}
			Chart = null;
			if(xml.Chart.length())
			{
				var chartType:String = xml.Chart.@type;
				if(chartType == Define.CONTROL_TYPE_PIE_3D)
					Chart = new Pie3DData();
				else if(chartType == Define.CONTROL_TYPE_COLUMN_3D)
					Chart = new Column3DData();
				else if(chartType == Define.CONTROL_TYPE_LINE_3D)
					Chart = new Line3DData();
				else if(chartType == Define.CONTROL_TYPE_COLUMN_3D_MS)
					Chart = new Column3DMSData();
				else if(chartType == Define.CONTROL_TYPE_COLUMN_3D_S)
					Chart = new Column3DSData();
				else if(chartType == Define.CONTROL_TYPE_LINE_3D_MS)
					Chart = new Line3DMSData();
				if(Chart)
					Chart.FromXML(XML(xml.Chart));
			}
		}
		
		override public function ToXML():String
		{
			var result:String = "";
			if(Type)
			{
				result += "<Control type=\"" + Type + "\">";
				
				var names:Array = _Styles.Keys;
				for each(var name:String in names)
				{
					var value:* = _Styles.Get(name);
					if(name.charAt(0) == "c")
						value =  ToHexColor(value);
					
					result += "<Set name=\"" + name + "\">" + EscapeXML(value) + "</Set>";
				}
				if(Chart)
					result += Chart.ToXML();
				result += "</Control>";
			}
			return result;
		}
	}
}