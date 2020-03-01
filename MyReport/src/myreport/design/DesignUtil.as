/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


*/
package myreport.design
{
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.display.DisplayObject;
	import flash.display.DisplayObjectContainer;
	import flash.text.TextField;
	import flash.text.TextFieldAutoSize;
	import flash.text.TextFormat;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Image;
	import mx.core.IFlexDisplayObject;
	
	import myreport.data.chart.Column3DData;
	import myreport.data.chart.Column3DMSData;
	import myreport.data.chart.Column3DSData;
	import myreport.data.chart.Line3DData;
	import myreport.data.chart.Line3DMSData;
	import myreport.data.chart.Pie3DData;
	import myreport.data.report.*;
	import myreport.design.cellrender.BarCodeCellRender;
	import myreport.design.cellrender.ChartCellRender;
	import myreport.design.cellrender.CurrencyCellRender;
	import myreport.design.cellrender.DesignCellRenderBase;
	import myreport.design.cellrender.HeaderCellRender;
	import myreport.design.cellrender.HeaderCellRender2;
	import myreport.design.cellrender.TextCellRender;
	import myreport.design.chart.Column3DEditor;
	import myreport.design.chart.Column3DMSEditor;
	import myreport.design.chart.Column3DSEditor;
	import myreport.design.chart.Line3DEditor;
	import myreport.design.chart.Pie3DEditor;
	
	public final class DesignUtil
	{
		private static const TEXT_PADDING:Number = 1;
		private static var g_DragImage:Image = new Image();
		private static var _Enums:Dictionary = new Dictionary();
		
		public static function CreateBitmap(target:DisplayObject):Bitmap
		{
			if (target.width == 0 || target.height == 0)
				return null;
			var bitmap:Bitmap = new Bitmap();
			bitmap.bitmapData = new BitmapData(target.width, target.height);
			bitmap.bitmapData.draw(target);
			return bitmap;
		}
		
		public static function CreateDragImage(target:DisplayObject):IFlexDisplayObject
		{
			if (target == null)
				return null;
			if (g_DragImage != null)
			{
				if (g_DragImage.content is Bitmap)
				{
					Bitmap(g_DragImage.content).bitmapData.dispose();
				}
			}
			g_DragImage.x = 0;
			g_DragImage.y = 0;
			g_DragImage.source = CreateBitmap(target);
			return g_DragImage;
		}
		
		public static function CreateItem(data:Object, parent:Object, settings:ReportSettings):DisplayObject
		{
			//trace(data);
			var item:DisplayObject;
			if (data is TableColumnSetting)
			{
				item = new TableColumnItem(settings, data as TableColumnSetting);
			}
			else if (data is TableRowSetting)
			{
				item = new TableRowItem(settings, parent as Array, data as TableRowSetting);
			}
			else if (data is CaptionRowSetting)
			{
				item = new CaptionRowItem(settings, parent as Array, data as CaptionRowSetting);
			}
			else if(data is TableCellSetting)
			{
				item = new TableCellItem(settings, parent as TableRowSetting, data as TableCellSetting);
			}
			else if (data is CaptionCellSetting)
			{
				item = new CaptionCellItem(settings, parent as CaptionRowSetting, data as CaptionCellSetting);
			}
			else if (data is SubReportRowSetting)
			{
				item = new SubReportRowItem(settings, parent as Array, data as SubReportRowSetting);
			}
			return item;
		}
		
		public static function AddItems(items:Array, parent:Object, settings:ReportSettings, childPanel:DisplayObjectContainer):void
		{
			for each (var item:Object in items)
			{
				var child:DisplayObject = CreateItem(item, parent, settings);
				if (child != null)
				{
					childPanel.addChild(child);
				}
			}
		}
		public static function CreateControlEditor(setting:ControlSetting, name:String):*
		{
			var editor:ControlStyleEditorBase
			var c:String = name.charAt(0).toLowerCase();
			switch(c)
			{
				case "b":
					editor = new BooleanControlStyleEditor();
					break;
				case "e":
					editor = new EnumControlStyleEditor();
					editor.initialize();
					EnumControlStyleEditor(editor).Source = GetControlStyleEnum(name);
					break;
				default:
					editor = new StringControlStyleEditor();
					break;
			}
			
			editor.initialize();
			editor.SetData(setting, name);
			
			return editor;
		}
		

		
		public static function GetControlStyleEnum(name:String):ArrayCollection
		{
			var enum:ArrayCollection = _Enums[name] as ArrayCollection;
			if(!enum)
			{
				enum = new ArrayCollection();
				if(name == Define.CONTROL_BAR_CODE_TYPE)
				{
					enum.addItem("Code128B");
					enum.addItem("EAN13");
					enum.addItem("EAN8");
					enum.addItem("QR_CODE");
				}
				_Enums[name] = enum;
			}
			
			return enum;
		}

		public static function GetLabelName(name:String):String
		{
			switch(name)
			{
				case Define.CONTROL_CURRENCY_HEADER:
					return "金额标题";
				case Define.CONTROL_DIGIT:
					return "位数";
				case Define.CONTROL_BAR_CODE_TYPE:
					return "条形码类型";
				case Define.CONTROL_LEFT_LABEL:
					return "左标签";
				case Define.CONTROL_RIGHT_LABEL:
					return "右标签";
				case Define.CONTROL_MIDDLE_LABEL:
					return "中间标签";
				case Define.CONTROL_RIGHT_SCALE:
					return "右边线比例";
				case Define.CONTROL_BOTTOM_SCALE:
					return "底边线比例";
			}
			return name;
		}
		
		public static function GetDefaultControlSetting(name:String):ControlSetting
		{
			var setting:ControlSetting = new ControlSetting();
			setting.Type = name;
			if(name == Define.CONTROL_TYPE_CURRENCY)
			{
				setting.SetStyle(Define.CONTROL_CURRENCY_HEADER, false);
				setting.SetStyle(Define.CONTROL_DIGIT, 11);
			}
			else if(name == Define.CONTROL_TYPE_BAR_CODE)
			{
				setting.SetStyle(Define.CONTROL_BAR_CODE_TYPE, "Code128B");
			}
			else if(name == Define.CONTROL_TYPE_HEADER)
			{
				setting.SetStyle(Define.CONTROL_LEFT_LABEL, "");
				setting.SetStyle(Define.CONTROL_RIGHT_LABEL, "");
			}
			else if(name == Define.CONTROL_TYPE_HEADER2)
			{
				setting.SetStyle(Define.CONTROL_LEFT_LABEL, "");
				setting.SetStyle(Define.CONTROL_MIDDLE_LABEL, "");
				setting.SetStyle(Define.CONTROL_RIGHT_LABEL, "");
				setting.SetStyle(Define.CONTROL_RIGHT_SCALE, 0.6);
				setting.SetStyle(Define.CONTROL_BOTTOM_SCALE, 0.6);
			}
			else if(name == Define.CONTROL_TYPE_PIE_3D)
			{
				setting.Chart = new Pie3DData();
			}
			else if(name == Define.CONTROL_TYPE_COLUMN_3D)
			{
				setting.Chart = new Column3DData();
			}
			else if(name == Define.CONTROL_TYPE_LINE_3D)
			{
				setting.Chart = new Line3DData();
			}
			else if(name == Define.CONTROL_TYPE_COLUMN_3D_MS)
			{
				setting.Chart = new Column3DMSData();
			}
			else if(name == Define.CONTROL_TYPE_COLUMN_3D_S)
			{
				setting.Chart = new Column3DSData();
			}
			else if(name == Define.CONTROL_TYPE_LINE_3D_MS)
			{
				setting.Chart = new Line3DMSData();
			}
			return setting;
		}
		/**
		 * @param callback: function(data:*):void
		 */ 
		public static function TryEditChart(control:ControlSetting, settings:ReportSettings, callback:Function):Boolean
		{
			if(control.Type == Define.CONTROL_TYPE_PIE_3D)
			{
				myreport.design.chart.Pie3DEditor.Instance.Show(
					control.Chart, settings, callback);
				return true;
			}
			else if(control.Type == Define.CONTROL_TYPE_COLUMN_3D)
			{
				myreport.design.chart.Column3DEditor.Instance.Show(
					control.Chart, settings, callback);
				return true;
			}
			else if(control.Type == Define.CONTROL_TYPE_LINE_3D)
			{
				myreport.design.chart.Line3DEditor.Instance.Show(
					control.Chart, settings, callback);
				return true;
			}
			else if(control.Type == Define.CONTROL_TYPE_COLUMN_3D_MS)
			{
				myreport.design.chart.Column3DMSEditor.Instance.Show(
					control.Chart, settings, callback);
				return true;
			} 
			else if(control.Type == Define.CONTROL_TYPE_COLUMN_3D_S)
			{
				myreport.design.chart.Column3DSEditor.Instance.Show(
					control.Chart, settings, callback);
				return true;
			} 
			else if(control.Type == Define.CONTROL_TYPE_LINE_3D_MS)
			{
				myreport.design.chart.Line3DMSEditor.Instance.Show(
					control.Chart, settings, callback);
				return true;
			}
			return false;
		}
		
		public static function GetBorderStyleSource():ArrayCollection
		{
			var source:ArrayCollection = new ArrayCollection();
			source.addItem({data: Define.BORDER_STYLE_SOLID, label: "实线"});
			source.addItem({data: Define.BORDER_STYLE_DASH, label: "虚线"});
 
			return source;
		}
		
		public static function GetControlSource():ArrayCollection
		{
			var source:ArrayCollection = new ArrayCollection();
			source.addItem({data: Define.CONTROL_TYPE_NONE, label: "(无)"});
			source.addItem({data: Define.CONTROL_TYPE_CURRENCY, label: "金额"});
			source.addItem({data: Define.CONTROL_TYPE_BAR_CODE, label: "条形码"});
			source.addItem({data: Define.CONTROL_TYPE_HEADER, label: "表头分隔"});
			source.addItem({data: Define.CONTROL_TYPE_HEADER2, label: "表头分隔2"});
			source.addItem({data: Define.CONTROL_TYPE_PIE_3D, label: "饼图3D"});
			source.addItem({data: Define.CONTROL_TYPE_COLUMN_3D, label: "柱状图3D"});
			source.addItem({data: Define.CONTROL_TYPE_LINE_3D, label: "折线图3D"});
			source.addItem({data: Define.CONTROL_TYPE_COLUMN_3D_MS, label: "柱状图3D(多序列)"});
			source.addItem({data: Define.CONTROL_TYPE_COLUMN_3D_S, label: "柱状图3D(堆叠式)"});
			source.addItem({data: Define.CONTROL_TYPE_LINE_3D_MS, label: "折线图3D(多序列)"});
			return source;
		}
 
		public static function GetFormatSource():ArrayCollection
		{
			var format:ArrayCollection = new ArrayCollection();
			format.addItem({data: "", label: ""});
			format.addItem({data: "F2", label: "数字（2位小数）"});
			format.addItem({data: "0.##", label: "数字（最多2位小数）"});
			format.addItem({data: "0.####", label: "数字（最多4位小数）"});
			format.addItem({data: "F0", label: "整数"});
			format.addItem({data: "C2", label: "金额(￥0.00)"});
			format.addItem({data: "C#,#2", label: "千分符金额(￥1,234.00)"});
			format.addItem({data: "#,#2", label: "千分符数字(2位小数)"});
			format.addItem({data: "P0", label: "百分率(0%)"});
			format.addItem({data: "P2", label: "百分率(0.00%)"});
			format.addItem({data: "D", label: "日期（yyyy年MM月dd日）"});
			format.addItem({data: "yyyy-MM-dd", label: "日期（yyyy-MM-dd）"});
			format.addItem({data: "yyyy-MM-dd HH:mm:ss", label: "日期（yyyy-MM-dd HH:mm:ss）"});
			return format;
		}
		
		public static function GetPageNumberFormatSource():ArrayCollection
		{
 
			var format:ArrayCollection = new ArrayCollection();
			format.addItem({data: "{0}", label: "{0}"});
			format.addItem({data: "第{0}页", label: "第{0}页"});
			format.addItem({data: "【第{0}页】", label: "【第{0}页】"});
			format.addItem({data: "{0}/{1}", label: "{0}/{1}"});
			format.addItem({data: "{1}-{0}", label: "{1}-{0}"});
			format.addItem({data: "第{0}页 共{1}页", label: "第{0}页 共{1}页"});
			format.addItem({data: "【第{0}页 共{1}页】", label: "【第{0}页 共{1}页】"});
 
			return format;
		}
 
		public static function GetColumnSource(source:Object):ArrayCollection
		{
			var result:ArrayCollection = new ArrayCollection();
			if(source)
			{
				if(source is ReportSettings)
				{
					var settings:ReportSettings = ReportSettings(source);
					source = settings.TableData;
				}
 
				if(source && source.length > 0)
				{
					var obj:Object = source[0];
					for (var col:String in obj)
					{
						result.addItem(col);
					}
				}
			}

			return result;
		}
		
		public static function GetParameterSource(source:Object):ArrayCollection
		{
			var result:ArrayCollection = new ArrayCollection();
			if(source)
			{
				if(source is ReportSettings)
				{
					var settings:ReportSettings = ReportSettings(source);
					source = settings.ParameterData;
				}
				if(source)
				{
					for (var key:String in source)
					{
						result.addItem(key);
					}
				}
			}
			return result;
		}
	}
}