/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


*/
package myreport.design
{
	import flash.events.Event;

	public class DesignEvent extends Event
	{
		public static const SAVE:String = "MyReportSave";
		public static const OPEN:String = "MyReportOpen";
		internal static const ITEM_MOUSE_DOWN:String = "MyReportItemMouseDown";
		
		internal static const SELECTION_CHANGED:String = "MyReportSelectionChanged";
 
		internal static const EDIT_PROPERTY:String = "MyReportEditProperty";
		internal static const EDIT_STYLE:String = "MyReportEdit";
		internal static const EDIT_CONTROL:String = "MyReportEditControl";
		internal static const EDIT_CONDITION_STYLE:String = "MyReportEditConditionStyle";

		internal static const DELETE:String = "MyReportDelete";
		internal static const ROW_DROP:String = "MyReportRowDrop";
		internal static const CELL_DROP:String = "MyReportCellDrop";
		internal static const ROW_SPAN_CHANGED:String = "MyReportRowSpanChanged";
		internal static const COL_SPAN_CHANGED:String = "MyReportColSpanChanged";
		internal static const HEIGHT_CHANGED:String = "MyReportHeightChanged";
		internal static const CELL_WIDTH_CHANGED:String = "MyReportCellWidthChanged";
		internal static const COLUMN_WIDTH_CHANGED:String = "MyReportColumnWidthChanged";
		internal static const COLUMN_ADDED:String = "MyReportColumnAdded";
		
		internal static const COLUMN_DELETED:String = "MyReportColumnDeleted";
		internal var ColumnIndex:uint = 0;
		
		internal var Target:Object;
		internal var Value:Object;
		internal var Append:Boolean = false;
		
		public function DesignEvent(type:String, bubbles:Boolean = false)
		{
			super(type, bubbles);
		}
		
		override public function clone():Event
		{
			var e:DesignEvent = new DesignEvent(type, bubbles);
			e.ColumnIndex = ColumnIndex;
			e.Target = Target;
			e.Value = Value;
			e.Append = Append;
			return e;
		}
	}
}