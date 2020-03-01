/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


图表图形分组

*/
package myreport.chart
{
	import flash.display.DisplayObject;

	internal class ChartGroup
	{
		public var XAxisLabel:DisplayObject;
		/** 柱状图堆叠式 */
		public var MaxLabel:DisplayObject;
		/** 柱状图堆叠式 */
		public var MinLabel:DisplayObject;
		/** 图像 , item=>ChartShape */
		public var ChartShapes:Array = new Array();
		public var Name:String;
		/**
		 * item => ChartGroupItem
		 */ 
		public var Items:Array = new Array();
		/** 图例值 */
		public var Labels:Array;

		public function ChartGroup()
		{
		}
	}
}