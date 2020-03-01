/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表项基类。


*/
package myreport.report
{
	import hlib.Parser;
	import hlib.SpriteBase;
	
	import myreport.data.report.ReportSettings;
 
	public class ReportItem extends SpriteBase
	{
		internal static const TYPE_OTHER:int = 0;
		internal static const TYPE_TABLE_HEADER:int = 1;
		internal static const TYPE_TABLE_FOOTER:int = 2;
		internal static const TYPE_TABLE_CONTENT:int = 3;
		internal static const TYPE_PAGE_HEADER:int = 4;
		internal static const TYPE_PAGE_FOOTER:int = 5;
		

		internal var Type:int = 0;
		/**用于区分分组*/
		internal var GroupIndex:int = -1;
		
		internal var SubReportBegin:Boolean = false;
		internal var SubReportEnd:Boolean = false;
		
		public var PageBreak:Boolean = false;
		
		private var _Settings:ReportSettings;
		
		protected override function Disposing():void
		{
			_Settings = null;
			super.Disposing();
		}
		
		public function ReportItem(settings:ReportSettings)
		{
			super();
			_Settings = settings;
		}
		public function get Settings():ReportSettings
		{
			return _Settings;
		}

	}
}