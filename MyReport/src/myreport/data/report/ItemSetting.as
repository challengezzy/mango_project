/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表样式——报表样式基类。


*/
package myreport.data.report
{
	import hlib.DisposeUtil;
	import hlib.IClone;
	import hlib.IDispose;
	import hlib.Parser;
	import hlib.StringUtil;

	public class ItemSetting implements IDispose, IClone
	{
		public function ItemSetting()
		{
		}

		public function FromXML(xml:XML):void
		{
		}

		public function ToXML():String
		{
			return "";
		}

		//================IDispose====================
		private var _Disposed:Boolean = false;
		protected function get Disposed():Boolean
		{
			return _Disposed;
		}
		protected function Disposing():void
		{
		}

		public function Dispose():void
		{
			if (_Disposed)
				return;
			_Disposed = true;
			Disposing();
		}
		//================IClone====================
		public function Clone():*
		{
			var clone:ItemSetting = new ItemSetting();
			return clone;
		}
		//=================static======================
		private static function CreateItemSetting(xml:XML):ItemSetting
		{
			var type:String = xml.@type;
			if(!type)//兼容xsi命名空间
			{
				var qn:QName = new QName("http://www.w3.org/2001/XMLSchema-instance", "type");
				type = xml.attribute(qn);
			}
			switch (type)
			{
				case "ConditionStyleSetting":
					return new ConditionStyleSetting(xml);
				case "TableCellSetting":
					return new TableCellSetting(xml);
				case "TableColumnSetting":
					return new TableColumnSetting(xml);
				case "TableGroupSetting":
					return new TableGroupSetting(xml);
				case "TableRowSetting":
					return new TableRowSetting(xml);
				case "CaptionRowSetting":
					return new CaptionRowSetting(xml);
				case "CaptionCellSetting":
					return new CaptionCellSetting(xml);	
				case "SubReportRowSetting":
					return new SubReportRowSetting(xml);
			}

			return null;
		}
		internal static function FillItemSettings(list:Array, parent:XML, name:String):void
		{
			if(!parent)
				return;
			var node:XMLList = parent.elements(name);
			if(node.length())
			{
				var xml:XML = XML(node);
				for each (var item:XML in xml.ItemSetting)
				{
					list.push(CreateItemSetting(item));
				}
			}
		}
		internal static function GetItemSettingsXML(list:Array, name:String):String
		{
			var result:String = "";
			
			var child:String = "";
			for each (var item:ItemSetting in list)
			{
				child += item.ToXML();
			}
			if(child)
			{
				result += "<" + name + ">";
				result += child;
				result += "</" + name + ">";
			}
			return result;
		}
		//value = 11pt
		internal static function ReadFontSize(value:String):Number
		{
			return parseFloat(value);
		}
		
		internal static function ReadBoolean(value:String):Boolean
		{
			return Parser.ParseBoolean(value);
		}
		internal static function ReadColor(value:String):uint
		{
			return Parser.ParseColor(value);
		}
		internal static function ToHexColor(color:uint):String
		{
			return Parser.ToHexColor(color);
		}
		internal static function EscapeXML(value:String):String
		{
			if(StringUtil.HasXmlChar(value))
				return StringUtil.EscapeXML(value);
			return value;
		}

	}
}