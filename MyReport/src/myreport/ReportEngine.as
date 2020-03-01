/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表引擎类。


*/
package myreport
{
	
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.display.Sprite;
	import flash.events.Event;
	import flash.geom.Rectangle;
	import flash.printing.PrintJob;
	import flash.printing.PrintJobOrientation;
	
	import hlib.DisposeUtil;
	import hlib.EventDispatcherBase;
	import hlib.MsgUtil;
	import hlib.StringUtil;
	import hlib.UrlLoader;
	import hlib.UrlLoaderManager;
	
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;
	
	import myreport.data.report.ReportSettings;
	import myreport.report.ReportPage;
	import myreport.report.ReportRender2;
	import myreport.report.async.AsyncContentRenderJob;
	import myreport.util.AsyncUtil;
	
	public final class ReportEngine
	{
		/**
		 * 设置报表引擎最大页数
		 */ 
		public static var MAX_PAGE:Number = 500;
		
		/**
		 * 设置报表引擎版本：2 或 1
		 */ 
		public static var RENDER_VERSION:uint = 2;
		private static var _Dispatcher:EventDispatcherBase;
		private static function GetDispatcher():EventDispatcherBase
		{
			if(!_Dispatcher)
			{
				_Dispatcher = new EventDispatcherBase();
			}
			return _Dispatcher;
		}
		
		public static function AddEventListener(type:String, listener:Function, useCapture:Boolean = false):void
		{
			GetDispatcher().addEventListener(type, listener, useCapture);
		}
		
		private static function DispatchEvent(event:Event):Boolean
		{
			return GetDispatcher().dispatchEvent(event);
		}
		
		public static function RemoveEventListener(type:String, listener:Function, useCapture:Boolean = false):void
		{
			GetDispatcher().removeEventListener(type, listener, useCapture);
		}
		
		public static function RemoveAllEvents():void
		{
			if(_Dispatcher)
				_Dispatcher.RemoveAllEvents();
		}
		
		private static function ReportPrint():void
		{
			var e:MyReportEvent = new MyReportEvent(MyReportEvent.PRINT);
			GetDispatcher().dispatchEvent(e);
		}
		private static function ScalePage(page:Sprite, print:PrintJob):void
		{
			var scaleX:Number = print.pageWidth/page.width;
			var scaleY:Number = print.pageHeight/page.height;
			page.scaleX = page.scaleY = scaleX<scaleY ? scaleX:scaleY;
		}
		private static function PrintPages(pages:Array, pageRange:String = null):void
		{
			if(!pages || pages.length == 0)
				return;
			
			var print:PrintJob;
			try
			{
				print = new PrintJob();
			}
			catch(e:Error)
			{
				MsgUtil.ShowInfo("当前有打印作业在进行，无法开始新的打印，请稍候再试。");
			}
			if (print.start())
			{
				var orientation:String = print.orientation;
				
				var begin:int = 0;
				var end:int = pages.length;
				if (pageRange != null)
				{
					var tokens:Array = pageRange.split("-");
					if (tokens.length > 0)
						begin = int(StringUtil.Trim(tokens[0])) - 1;
					if (tokens.length > 1)
						end = int(StringUtil.Trim(tokens[1]));
				}
				if (begin < 0)
					begin = 0;
				if (begin > pages.length - 1)
					begin = pages.length - 1;
				if (end <= begin)
					end = begin + 1;
				if (end > pages.length)
					end = pages.length;
				
				trace("[MyReport.ReportEngine]Print " + (begin + 1) + "-" + end + " pages.");
				
				var bitmaps:Array = new Array();
				for (; begin < end; begin++)
				{
					var page:ReportPage = pages[begin] as ReportPage;
					if(!page.Settings.AdjustPrintDirection)
					{
						if(page.Settings.FitToPrintSize)
						{
							ScalePage(page, print);
						}
						
						print.addPage(page, new Rectangle(0, 0, page.width, page.height));
					}
					else
					{
						var bitmap:Bitmap;
						var bmpData:BitmapData;
						var sprite:Sprite;
						if(
							(orientation == PrintJobOrientation.PORTRAIT && page.width>page.height) ||//横向
							(orientation == PrintJobOrientation.LANDSCAPE && page.height>page.width)//纵向
						)
						{
							
							bmpData = new BitmapData(page.width, page.height, true, 0xffffff);
							bmpData.draw(page,null,null,null,null,true);
							bitmap = new Bitmap(bmpData);
							bitmaps.push(bitmap);
							
							sprite = new Sprite();
							sprite.addChild(bitmap);
							sprite.rotation = -90;
							
							if(page.Settings.FitToPrintSize)
							{
								ScalePage(sprite, print);
							}
							
							print.addPage(sprite, new Rectangle(0, 0, page.width, page.height));
						}
						else
						{
							
							if(page.Settings.FitToPrintSize)
							{
								ScalePage(page, print);
							}
							print.addPage(page, new Rectangle(0, 0, page.width, page.height));
						}
					}
				}
				
				print.send();
				DisposeUtil.Dispose(pages);
				DisposeUtil.Dispose(bitmaps);
				ReportPrint();
			}
		}
		
		/**
		 * 筛选表格数据
		 * @param filter: 表格数据筛选表达式，为空时返回原表格数据
		 */ 
		public static function FilterTable(table:ArrayCollection, filter:String = null):ArrayCollection
		{
			return ReportRender2.FilterTable(table, filter);
		}
		
		public static function ToSettings(source:Object, table:ArrayCollection, parameters:Object):ReportSettings
		{
			var settings:ReportSettings
			if (source is XML || source is String)
			{
				settings = new ReportSettings(XML(source));
			}
			else if(source is ReportSettings)
			{
				settings = ReportSettings(source).Clone();
			}
			if(settings)
			{
				settings.TableData = table;
				settings.ParameterData = parameters;
			}
			return settings;
		}
		
		/**
		 * 生成页面
		 * 返回值: Array item => Sprite
		 * @param source: 支持XML对象、ReportSettings对象
		 */
		public static function GeneratePages(source:Object, table:ArrayCollection, parameters:Object):Array
		{
			var settings:ReportSettings = ToSettings(source, table, parameters);
			
			var pages:Array;
			
			ReportRender2.MAX_PAGE = MAX_PAGE;
			pages = ReportRender2.RenderPages(settings);	
			
			settings.Dispose();
			trace("[MyReport.ReportEngine]Generate " + pages.length + " pages.");
			return pages;
		}
		
		/**
		 * 打印
		 * @param source: 支持XML对象、ReportSettings对象
		 * @param pageRange: 页码范围，null时打印全部页码，格式：1-3
		 */ 
		public static function Print(source:Object, table:ArrayCollection, parameters:Object, pageRange:String = null):void
		{
			var pages:Array = GeneratePages(source, table, parameters);
			PrintPages(pages, pageRange);
		}		
		
		/**
		 * 打印2，支持多个报表同时打印
		 * 
		 * 参数sourceList，tableList，parametersList子项一一对应，并且长度必须一致，
		 * 内部会循环调用 sourceList[i], tableList[i], parametersList[i]
		 * 
		 * @param sourceList: 子项是XML对象、ReportSettings对象
		 * @param tableList: 子项是ArrayCollection对象
		 * @param parametersList: 子项是Object对象
		 * @param pageRange: 页码范围(多个报表总页数)，null时打印全部页码，格式：1-3
		 */
		public static function Print2(sourceList:Array, tableList:Array, parametersList:Array, pageRange:String = null):void
		{
			var total:Array = new Array();
			for(var i:int=0;i<sourceList.length;i++)
			{
				var pages:Array = GeneratePages(sourceList[i], tableList[i], parametersList[i]);
				for each(var page:* in pages)
				{
					total.push(page);
				}
			}
			
			PrintPages(total, pageRange);
		}
		
		/**
		 * 加载并打印
		 * 
		 * @param pageRange: 页码范围，null时打印全部页码，格式：1-3
		 */ 
		public static function LoadAndPrint(url:String, table:ArrayCollection, parameters:Object, pageRange:String = null):void
		{
			var loader:UrlLoader = new UrlLoader();
			loader.addEventListener(Event.COMPLETE, function(event:Event):void
			{
				Print(XML(loader.Data), table, parameters, pageRange);
				loader.Dispose();
			});
			loader.Load(url);
		}
		
		/**
		 * 加载并打印2，支持多个报表同时打印
		 * 
		 * 参数urlList，tableList，parametersList子项一一对应，并且长度必须一致，
		 * 内部会循环调用 urlList[i], tableList[i], parametersList[i]
		 * 
		 * @param urlList: 子项是String对象
		 * @param tableList: 子项是ArrayCollection对象
		 * @param parametersList: 子项是Object对象
		 * @param pageRange: 页码范围(多个报表总页数)，null时打印全部页码，格式：1-3
		 */ 
		public static function LoadAndPrint2(urlList:Array, tableList:Array, parametersList:Array, pageRange:String = null):void
		{
			var loaderMgr:UrlLoaderManager = new UrlLoaderManager();
			for each(var url:String in urlList)
			{
				loaderMgr.Load(url, url);
			}
			
			loaderMgr.addEventListener(Event.COMPLETE, function(event:Event):void
			{
				var sourceList:Array = new Array();
				for(var i:int=0;i<urlList.length;i++)
				{
					var url:String = urlList[i];
					sourceList[i] = new XML(loaderMgr.GetData(url));
				}
				
				Print2(sourceList, tableList, parametersList, pageRange);
				loaderMgr.Dispose();
			});
			loaderMgr.Commit();
		}
		//====================异步操作==============================
		/**
		 * 异步填充页面
		 * @param pages: 填充的页面列表
		 * @param callback: function(pages:Array):void, item => Sprite
		 */
		private static function FillPagesArync(settings:ReportSettings, pages:Array, callback:Function):void
		{
			var job:AsyncContentRenderJob = new AsyncContentRenderJob(settings, pages);
			AsyncUtil.RunJob(job,
				function():void
				{
					if(callback!=null)
					{
						callback(pages);
					}
					trace("[MyReport.ReportEngine]Generate " + pages.length + " pages.");
				});
		}
		/**
		 * 异步填充多个报表
		 * @param settingsList: item => ReportSettings，内部会清空settingsList
		 * @param pages: 填充的页面列表
		 * @param callback: function(pages:Array):void, item => Sprite
		 */
		private static function FillPagesAryncLoop(settingsList:Array, pages:Array, callback:Function):void
		{
			if(settingsList.length)
			{
				var settings:ReportSettings = settingsList.splice(0,1)[0];
				//trace(settingsList.length);
				FillPagesArync(settings, pages, function(pages2:Array):void
				{
					FillPagesAryncLoop(settingsList, pages2, callback);
				});
			}
			else
			{
				if(callback!=null)
				{
					callback(pages);
				}
			}
		}
		/**
		 * 异步生成页面
		 * @param source: 支持XML对象、ReportSettings对象
		 * @param callback: function(pages:Array):void, item => Sprite
		 */
		public static function GeneratePagesAsync(source:Object, table:ArrayCollection, parameters:Object, 
												  callback:Function):void
		{
			var settings:ReportSettings = ToSettings(source, table, parameters);
			
			ReportRender2.MAX_PAGE = MAX_PAGE;
			
			var pages:Array;
			pages = new Array();
			FillPagesArync(settings, pages, callback);
		}
		/**
		 * 异步填充多个报表页面
		 * @param settingsList: item => ReportSettings，内部会清空settingsList
		 * @param pagesList: 填充的页面列表
		 * @param callback: function(pagesList:Array):void, item => Array{item=>Sprite}
		 */
		private static function FillPagesAryncLoop2(settingsList:Array, pagesList:Array, callback:Function):void
		{
			if(settingsList.length)
			{
				var settings:ReportSettings = settingsList.splice(0,1)[0];
				var pages:Array = new Array();
				//trace(settingsList.length);
				FillPagesArync(settings, pages, function(pages2:Array):void
				{
					pagesList.push(pages2);
					FillPagesAryncLoop2(settingsList, pagesList, callback);
				});
			}
			else
			{
				if(callback!=null)
				{
					callback(pagesList);
				}
			}
		}
		/**
		 * 异步生成多个报表页面
		 * 参数sourceList，tableList，parametersList子项一一对应，并且长度必须一致，
		 * 内部会循环调用 sourceList[i], tableList[i], parametersList[i]
	 	 * @param callback: function(pagesList:Array):void, item => Array{item=>Sprite}
		 */
		public static function GeneratePagesAsync2(sourceList:Array, tableList:Array, parametersList:Array, 
												  callback:Function):void
		{
			var settings:ReportSettings;
			var settingsList:Array = new Array();
			for(var i:int=0;i<sourceList.length;i++)
			{
				settings = ToSettings(sourceList[i], tableList[i], parametersList[i]);
				settingsList.push(settings);
			}
			ReportRender2.MAX_PAGE = MAX_PAGE;
			
			var pagesList:Array;
			pagesList = new Array();
			
			FillPagesAryncLoop2(settingsList, pagesList, 
				function(pagesList:Array):void
				{
					if(callback!=null)
						callback(pagesList);
				}
			);
		}

		/**
		 * 异步生成页面打印
		 * @param source: 支持XML对象、ReportSettings对象
		 * @param pageRange: 页码范围，null时打印全部页码，格式：1-3
		 */ 
		public static function PrintAsync(source:Object, table:ArrayCollection, parameters:Object, pageRange:String = null):void
		{
			GeneratePagesAsync(source, table, parameters, 
				function(pages:Array):void
				{
					PrintPages(pages, pageRange);
				}
			);
		}
		
		/**
		 *  异步生成页面打印2，支持多个报表同时打印
		 * 
		 * 参数sourceList，tableList，parametersList子项一一对应，并且长度必须一致，
		 * 内部会循环调用 sourceList[i], tableList[i], parametersList[i]
		 * 
		 * @param sourceList: 子项是XML对象、ReportSettings对象
		 * @param tableList: 子项是ArrayCollection对象
		 * @param parametersList: 子项是Object对象
		 * @param pageRange: 页码范围(多个报表总页数)，null时打印全部页码，格式：1-3
		 */
		public static function PrintAsync2(sourceList:Array, tableList:Array, parametersList:Array, pageRange:String = null):void
		{
			var settings:ReportSettings;
			var settingsList:Array = new Array();
			for(var i:int=0;i<sourceList.length;i++)
			{
				settings = ToSettings(sourceList[i], tableList[i], parametersList[i]);
				settingsList.push(settings);
			}
			ReportRender2.MAX_PAGE = MAX_PAGE;
			
			var pages:Array;
			pages = new Array();
			
			FillPagesAryncLoop(settingsList, pages, 
				function(pages:Array):void
				{
					PrintPages(pages, pageRange);
				}
			);
		}
		
		/**
		 * 加载并异步生成页面打印
		 * 
		 * @param pageRange: 页码范围，null时打印全部页码，格式：1-3
		 */ 
		public static function LoadAndPrintAsync(url:String, table:ArrayCollection, parameters:Object, pageRange:String = null):void
		{
			var loader:UrlLoader = new UrlLoader();
			loader.addEventListener(Event.COMPLETE, function(event:Event):void
			{
				PrintAsync(XML(loader.Data), table, parameters, pageRange);
				loader.Dispose();
			});
			loader.Load(url);
		}
		
		/**
		 * 加载并异步生成页面打印2，支持多个报表同时打印
		 * 
		 * 参数urlList，tableList，parametersList子项一一对应，并且长度必须一致，
		 * 内部会循环调用 urlList[i], tableList[i], parametersList[i]
		 * 
		 * @param urlList: 子项是String对象
		 * @param tableList: 子项是ArrayCollection对象
		 * @param parametersList: 子项是Object对象
		 * @param pageRange: 页码范围(多个报表总页数)，null时打印全部页码，格式：1-3
		 */ 
		public static function LoadAndPrintAsync2(urlList:Array, tableList:Array, parametersList:Array, pageRange:String = null):void
		{
			var loaderMgr:UrlLoaderManager = new UrlLoaderManager();
			for each(var url:String in urlList)
			{
				loaderMgr.Load(url, url);
			}
			
			loaderMgr.addEventListener(Event.COMPLETE, function(event:Event):void
			{
				var sourceList:Array = new Array();
				for(var i:int=0;i<urlList.length;i++)
				{
					var url:String = urlList[i];
					sourceList[i] = new XML(loaderMgr.GetData(url));
				}
				PrintAsync2(sourceList, tableList, parametersList, pageRange);
				loaderMgr.Dispose();
			});
			loaderMgr.Commit();
		}
	}
}