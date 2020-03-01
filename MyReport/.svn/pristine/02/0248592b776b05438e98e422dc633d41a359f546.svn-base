/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


异步报表页面生成类


*/
package myreport.report.async
{
	import hlib.MsgUtil;
	
	import mx.collections.ArrayCollection;
	
	import myreport.data.report.ReportSettings;
	import myreport.report.ReportItem;
	import myreport.report.ReportPage;
	import myreport.report.ReportPageNumber;
	import myreport.report.ReportRender2;
	import myreport.util.AsyncJob;
	
	public class AsyncPageRenderJob extends AsyncJob
	{
		private var _Settings:ReportSettings;
		private var _Contents:Array;
		private var _Pages:Array;
		private var _TableData:ArrayCollection;
 
		private var _Current:int = 0;
		private var _Total:int = 0;
		private var _Init:Boolean = false;
		private var _Finished:Boolean = false;
		
		protected override function Disposing():void
		{
			_Settings = null;
			_Contents = null;
			_Pages = null;
			_TableData = null;
			super.Disposing();
		}
		
		public function AsyncPageRenderJob(settings:ReportSettings, contents:Array, pages:Array, tableData:ArrayCollection)
		{
			super();
			_Settings = settings;
			_Contents = contents;
			_Total = contents.length;
			_Pages = pages;
			_TableData = tableData;
			
		}
		public override function get Finished():Boolean
		{
			return _Init && _Finished && _Current >= _Total;
		}
		
		protected override function OnExecute():void
		{
			ReportProgress("正在生成页面...", _Current, _Total);
			var page:ReportPage;
			
			if(!_Init || _Current<_Total)
			{
				_Init = true;
				page = ReportRender2.CreatePage(_Settings, _Pages.length);
				_Pages.push(page);
				ReportRender2.BeginRenderPage(page, _Settings);
 
				if(page.Overflow)
					throw new Error("页面内容溢出，增加页面高度才能正常显示。");
				//Render Content
				for (; _Current < _Contents.length; _Current++)
				{
					var item:ReportItem = _Contents[_Current];
					if(!page.CheckOverflow(item.height))
					{
						page.AddContent(item);
						if(!_Settings.CanGrow && item.PageBreak)
						{
							_Current++;
							break;
						}
					}
					else
						break;
				}
				page.EndRender();
				
				if (_Pages.length > ReportRender2.MAX_PAGE)
				{
					MsgUtil.ShowInfo("报表引擎支持的页数上限是" + ReportRender2.MAX_PAGE + "页，" + ReportRender2.MAX_PAGE + "页之后的页面已被忽略。");
					_Current = _Total;
				}
				return;
			}
			
			//刷新页码
			if(_Settings.ShowPageNumber)
			{
				for each(page in _Pages)
				{
					if(page.PageNumber)
					{
						ReportPageNumber(page.PageNumber).Render(page.Index+1, _Pages.length);
					}
				}
			}
			_Settings.TableData = _TableData;
			_Finished = true;
			//下一个Job
			Complete();
		}
	}
}