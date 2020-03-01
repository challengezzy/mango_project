/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表数据加载类。


*/

package myreport.data.reportdata
{
 
	import flash.events.Event;
	import flash.events.ProgressEvent;
	import flash.utils.Dictionary;
	
	import hlib.DisposeUtil;
	import hlib.EventDispatcherBase;
	import hlib.IDispose;
	import hlib.LoadCounter;
	import hlib.UrlLoader;
	
	import mx.collections.ArrayCollection;
	
	public final class ReportDataLoader extends EventDispatcherBase implements IDispose
	{
		private var _Counter:LoadCounter;
 		private var _Data:ReportData;
		public var Parameters:Object;
		public var Table:ArrayCollection;
		//================IDispose====================
		protected override function Disposing():void
		{
			_Counter.Dispose();
			_Counter = null;
			
			Parameters = null;
			Table = null;
			super.Disposing();
		}
 
		public function ReportDataLoader()
		{
			super(null);
			_Counter = new LoadCounter();
			_Counter.addEventListener(Event.COMPLETE, OnLoadComplete);
			_Counter.addEventListener(ProgressEvent.PROGRESS, OnProgressEvent);
		}
	
		public function Load(url:String):void
		{
			_Counter.Reset();
			var loader:UrlLoader = new UrlLoader(url);
			loader.addEventListener(Event.COMPLETE, function(e:Event):void
			{
				_Data = new ReportData(_Counter, new XML(loader.Data));
				_Counter.CommitTotal();
				
			});
			loader.Load();
		}
		private function OnProgressEvent(e:ProgressEvent):void
		{
			e.stopPropagation();
			trace(_Counter.CurrentLoaded + "/" + _Counter.TotalLoaded);
			dispatchEvent(e.clone());
		}
 
		private function OnLoadComplete(e:Event):void
		{
			e.stopPropagation();
			Parameters = new Dictionary();
			Table = new ArrayCollection();
			var set:SetData;
			for each(set in _Data.Parameters)
			{
				Parameters[set.Name] = GetValue(set);
			}
			for each(var row:Array in _Data.Table.Rows)
			{
				var newRow:Dictionary = new Dictionary();
				for each(set in row)
				{
					newRow[set.Name] = GetValue(set);
				}
 
				Table.addItem(newRow);
			}
			_Data.Dispose();
			
			dispatchEvent(e.clone());
		}
		private function GetValue(setData:SetData):*
		{
		 	if(setData.Type.toLowerCase() == "table")
				return GetTable(setData.Value);
			else if(setData.Type.toLowerCase() == "params")
				return GetParams(setData.Value);
			return setData.Value;
		}
		private function GetParams(params:Dictionary):*
		{
			var dict:Dictionary = new Dictionary();
			for(var key:String in params)
			{
				dict[key] = GetValue(params[key]);
			}
			return dict;
		}
		
		private function GetTable(tableData:TableData):ArrayCollection
		{
			var table:ArrayCollection = new ArrayCollection();
			var set:SetData;
			for each(var row:Array in tableData.Rows)
			{
				var newRow:Dictionary = new Dictionary();
				for each(set in row)
				{
					newRow[set.Name] = GetValue(set);
				}
				
				table.addItem(newRow);
			}
			return table;
		}
  
		

	}
}