package myreport.report.async
{
	import hlib.CollectionUtil;
	
	import mx.collections.ArrayCollection;
	
	import myreport.data.report.ReportSettings;
	import myreport.data.report.TableGroupSetting;
	import myreport.report.ReportRender2;
	import myreport.util.AsyncJob;

	public final class AsyncContentRenderJob extends AsyncJob
	{
		private var _Pages:Array;
		private var _Settings:ReportSettings;
		private var _TableData:ArrayCollection;
		
		private var _Contents:Array = new Array();
		private var _Init:Boolean = false;
		private var _Finished:Boolean = false;
		private var _Current:int = 0;
		private var _Total:int = 0;
		private var group:TableGroupSetting;
		private var groupStart:int = 0;
		private var groupEnd:int = 0;
		private var groupIndex:int = 0;
		private var generateGroupHeader:Boolean = false;
		private var generateGroupFooter:Boolean = false;
		
		protected override function Disposing():void
		{
			_Pages = null;
			_Settings = null;
			_TableData = null;
			group = null;
			super.Disposing();
		}
		
		public function AsyncContentRenderJob(settings:ReportSettings, pages:Array)
		{
			super();
			_Settings = settings;
			_Pages = pages;
			ReportRender2.ExecuteColSpan(settings);
			ReportRender2.RenderColumns(_Settings);
			
			_Settings.SetUnit("px");
			_Settings.RefreshAllLayout();
			_TableData = _Settings.TableData;//临时缓存表数据，有可能执行分组排序
			group = _Settings.Group;
			
			ReportRender2.GroupTable(_Settings);
 
			if(_Settings.TableData)
				_Total = _Settings.TableData.length;
		}
		
		public override function get Finished():Boolean
		{
			return _Init && _Finished && _Current >= _Total;
		}
		
		protected override function OnExecute():void
		{
			ReportProgress("正在生成报表主体...", _Current, _Total);
			if(!_Init)//初始化阶段
			{
				_Init = true;
				
				if(!_Settings.TableHeaderRepeat)
				{
					ReportRender2.FillTableHeaderOrFooter(_Contents, _Settings, true);
				}
				
				group = _Settings.Group;
				if(group)
				{
					ReportRender2.ValidateGroup(_Settings, group);
					generateGroupHeader = true;
				}
				
				return;
			}
			
			if(_Settings.TableData && _Current < _Total)//生成主体阶段
			{
				//GroupHeaders
				if(generateGroupHeader)
				{
					groupStart = _Current;
					groupEnd = ReportRender2.GetGroupEndIndex(_Settings, group, _Current);
					ReportRender2.FillTableGroupHeaderOrFooter(_Contents, _Settings, _Current, groupIndex, groupStart, groupEnd, true);
					generateGroupHeader = false;
				}
				
				//TableDetails
				ReportRender2.FillTableDetail(_Contents, _Settings, _Settings.TableDetailSettings, _Current, groupIndex, groupStart, groupEnd);
				
				//GroupFooters
				generateGroupFooter = group && _Current == groupEnd;
				if(generateGroupFooter)
				{
					ReportRender2.FillTableGroupHeaderOrFooter(_Contents, _Settings, _Current, groupIndex, groupStart, groupEnd, false);
					generateGroupFooter = false;
					generateGroupHeader = true;
					groupIndex++;
				}
				
				_Current++
				return;
			}
			
			//结束阶段
			if(!_Settings.TableFooterRepeat)
			{
				ReportRender2.FillTableHeaderOrFooter(_Contents, _Settings, false);
			}
			
			if(!_Settings.PageFooterRepeat)
			{
				ReportRender2.FillPageHeaderOrFooter(_Contents, _Settings, false);
			}
			_Finished = true;
			//下一个Job
			var job:AsyncPageRenderJob = new AsyncPageRenderJob(_Settings, _Contents, _Pages, _TableData);
			Worker.AddJob(job);
		}
	}
}