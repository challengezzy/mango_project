/*
Copyright (c) 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


XML字符串生成


*/
package hlib
{
	public class XmlWriter
	{
		public var NEW_LINE:String = "\r\n";
		private var _Data:String;
		public function XmlWriter() 
		{
			_Data = "";
		}
		public function toString():String
		{
			return _Data;
		}
		public function Write(value:String):XmlWriter
		{
			_Data+=value;
			return this;
		}
		public function WriteLine(value:String):XmlWriter
		{
			return Write(value).NewLine();
		}
		public function NewLine():XmlWriter
		{
			return Write(NEW_LINE);
		}
		public function Tab():XmlWriter
		{
			return Write("\t");
		}
		public function Space():XmlWriter
		{
			return Write(" ");
		}

		/**
		 * <name>
		 */ 
		public function Begin(name:String):XmlWriter
		{
			return Write("<"+name+">");
		}
		/**
		 * <name
		 */ 
		public function BeginEle(name:String):XmlWriter
		{
			return Write("<"+name);
		}
		/**
		 * >
		 */ 
		public function EndEle():XmlWriter
		{
			return Write(">");
		}
		/**
		 * name值不为空时</name>，否则/>
		 */ 
		public function End(name:String = null):XmlWriter
		{
			if(name)
				return Write("</"+name+">");
			return Write("/>");
		}
		/**
		 * 写文本节点，Escape XML关键字
		 */ 
		public function Text(value:*):XmlWriter
		{
			return Write(StringUtil.EscapeXML(value));
		}
		
		public function CData(value:*):XmlWriter
		{
			return Write("<![CDATA[").Write(value).Write("]]>");
		}
		/**
		 *  name="value"，前面有空格
		 */ 
		public function Attr(name:String, value:*=""):XmlWriter
		{
			return Write(" "+name+"=\""+StringUtil.EscapeXML(value)+"\"");
		}
		/**
		 * 写入多个属性，names和values数量必须相同
		 *  name="value"，前面有空格
		 */ 
		public function Attrs(names:Array, values:Array):XmlWriter
		{
			for(var i:int=0;i<names.length;i++)
			{
				Attr(names[i], values[i]);
			}
			return this;
		}
		/**
		 * <name>value<name>
		 */ 
		public function Ele(name:String, value:*=""):XmlWriter
		{
			return Write("<"+name+">"+StringUtil.EscapeXML(value)+"</"+name+">");
		}
		
		/**
		 * 写入多个元素
		 * <name>value<name>
		 */ 
		public function Eles(names:Array, values:Array):XmlWriter
		{
			for(var i:int=0;i<names.length;i++)
			{
				Ele(names[i], values[i]);
			}
			
			return this;
		}
		/**
		 * 写带1个属性的元素
		 */ 
		public function EleAttr(eleName:String, eleValue:*, attrName:String, attrValue:*):XmlWriter
		{
			BeginEle(eleName).Attr(attrName, attrValue).EndEle()
				.Text(eleValue)
				.End(eleName);
			return this;
		}
		/**
		 * 写带多个属性的元素
		 */ 
		public function EleAttrs(eleName:String, eleValue:*, attrNames:Array, attrValues:Array):XmlWriter
		{
			BeginEle(eleName);
			for(var i:int=0;i<attrNames.length;i++)
			{
				Attr(attrNames[i], attrValues[i]);
			}
			EndEle()
			.Text(eleValue)
				.End(eleName);
			return this;
		}
		public function WriteArray(value:Array):XmlWriter
		{
			for each(var item:Object in value)
			{
				Write(item.toString());
			}
			
			return this;
		}
		/**
		 * value有值时输出name
		 * @param value: 支持Array, Object
		 */ 
		public function WriteObject(value:Object, name:String=""):XmlWriter
		{
			var result:String = "";
			
			if(value is Array)
			{
				for each(var item:Object in value)
				{
					result += item.toString();
				}
			}
			else
				result += value.toString();
			
			if(result && name)
				Begin(name).Write(result).End(name);
			else
				Write(result);
			 return this;
		}
	}
}