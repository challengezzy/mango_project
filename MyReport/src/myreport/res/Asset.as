/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


嵌入资源。


*/
package myreport.res
{
	import mx.core.ByteArrayAsset;

	public final class Asset
	{
		public static function LoadXML(assetClass:Class):XML
		{
			var bytes:ByteArrayAsset = new 	assetClass() as ByteArrayAsset;
			return new XML(bytes.readUTFBytes(bytes.length));
		}
		
		[Embed(source="PrintHS.png")]
		[Bindable]
		public static var ICON_PRINT:Class;
		[Embed(source="Help.png")]
		[Bindable]
		public static var ICON_HELP:Class;

		[Embed(source="MoveFirstHS.png")]
		[Bindable]
		public static var ICON_FIRST:Class;
		[Embed(source="MovePreviousHS.png")]
		[Bindable]
		public static var ICON_PREV:Class;
		[Embed(source="MoveNextHS.png")]
		[Bindable]
		public static var ICON_NEXT:Class;
		[Embed(source="MoveLastHS.png")]
		[Bindable]
		public static var ICON_LAST:Class;

		[Embed(source="Delete16.png")]
		[Bindable]
		public static var ICON_CLOSE:Class;
		
		[Embed(source="cur/SizeAll21.png")]
		public static var ICON_SIZE_ALL:Class;
		[Embed(source="cur/VDividerCursor21.png")]
		public static var ICON_V_DIVIDER:Class;
		[Embed(source="cur/HDividerCursor21.png")]
		public static var ICON_H_DIVIDER:Class;
		
		[Embed(source="openHS.png")]
		[Bindable]
		public static var ICON_OPEN:Class;
		[Embed(source="RefreshDocViewHS.png")]
		[Bindable]
		public static var ICON_REFRESH:Class;
		[Embed(source="SaveHS.png")]
		[Bindable]
		public static var ICON_SAVE:Class;
		[Embed(source="PropertiesHS.png")]
		[Bindable]
		public static var ICON_PROPERTIES:Class;
		[Embed(source="EditInformationHS.png")]
		[Bindable]
		public static var ICON_DESIGN:Class;
		[Embed(source="PrintPreviewHS.png")]
		[Bindable]
		public static var ICON_PREVIEW:Class;
		[Embed(source="FunctionHS.png")]
		[Bindable]
		public static var ICON_FUNCTION:Class;
		
		[Embed(source="Delete12.png")]
		[Bindable]
		public static var ICON_DELETE12:Class;
		[Embed(source="Delete16.png")]
		[Bindable]
		public static var ICON_DELETE16:Class;
		[Embed(source="Add12.png")]
		[Bindable]
		public static var ICON_ADD12:Class;
		[Embed(source="Add16.png")]
		[Bindable]
		public static var ICON_ADD16:Class;
		
		[Embed(source="edit.png")]
		[Bindable]
		public static var ICON_EDIT16:Class;
		
		[Embed(source="text_rich_colored.png")]
		[Bindable]
		public static var ICON_CONDITION_STYLE:Class;
		
		[Embed(source="Filter16.png")]
		[Bindable]
		public static var ICON_FILTER:Class;
 
		[Embed(source="OperationAndFunction.xml", mimeType="application/octet-stream")]
		[Bindable]
		public static var XML_OPERATION_AND_FUNCTION:Class;
		
		[Embed(source="File_PDF16.png")]
		[Bindable]
		public static var ICON_FILE_PDF16:Class;
		
		[Embed(source="Excel.png")]
		[Bindable]
		public static var ICON_FILE_XLS16:Class;
	}
}