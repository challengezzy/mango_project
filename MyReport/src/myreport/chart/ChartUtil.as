/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


图表工具类，能够自动生成颜色，文本标签等

*/
package myreport.chart
{
 
	import flash.text.TextFormat;
	
	import hlib.MathUtil;
	import hlib.TextBase;
	
	import myreport.data.report.Define;
 
	public final class ChartUtil
	{
		private static var _DefaultColors:Array = [
			0xFF0000,0x0099CC,0x006F00,0xCCCC00,0xFF66CC,
			0x996600,0x669966,0x7C7CB4,0xFF9933,0x9900FF,
			0x999999,0x99FFCC,0xCCCCFF,0x669900,0x0099FF,
			0x1941A5
		];
 
		private static function RandColor(color:uint):uint
		{
			var r:uint = Math.floor(color/65536);
			var g:uint = Math.floor((color-r*65536)/256);
			var b:uint = color-r*65536-g*256;
			var intensityRequired:Number = Math.random();
			var rgbIndex:int = MathUtil.RandInt(0, 3);
			switch(rgbIndex)
			{
				case 0:
					r*=intensityRequired;
					break;
				case 1:
					g*=intensityRequired;
					break;
				case 2:
					b*=intensityRequired;
					break;
			}
			var color:uint = r << 16 | g << 8 | b;
			return color;
		}
		internal static function GetColor(index:int = 0):int
		{
			if(index<_DefaultColors.length)
				return _DefaultColors[index];
			var color:uint = _DefaultColors[MathUtil.RandInt(0, _DefaultColors.length)];
			return RandColor(color);
		}
		internal static function CreateText(text:Object, fontSize:Number, fontBold:Boolean,  
												 textColor:uint = 0, width:Number = NaN,
												 textAlign:String = "left"):TextBase
		{
			return TextBase.CreateText(text, CreateTextFormat(fontBold, fontSize, textAlign, textColor), width);
		}
		
		private static function CreateTextFormat(fontBold:Boolean, fontSize:Number, 
												 textAlign:String, textColor:uint):TextFormat
		{
			var tf:TextFormat = new TextFormat();
			tf.align = textAlign;
			tf.bold = fontBold;
			tf.size = fontSize;
			tf.font = "Simsun";
			tf.color = textColor;
			return tf;
		}
		
		public static function CreateChart(type:String):ChartBase
		{
			switch(type)
			{
				case Define.CONTROL_TYPE_COLUMN_3D:
					return new Column3D();
				case Define.CONTROL_TYPE_PIE_3D:
					return new Pie3D();
				case Define.CONTROL_TYPE_LINE_3D:
					return new Line3D();
				case Define.CONTROL_TYPE_COLUMN_3D_MS:
					return new Column3DMS();
				case Define.CONTROL_TYPE_COLUMN_3D_S:
					return new Column3DS();	
				case Define.CONTROL_TYPE_LINE_3D_MS:
					return new Line3DMS();
			}
			
			return null;
		}
			
	}
}