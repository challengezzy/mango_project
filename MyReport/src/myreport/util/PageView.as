/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


页面显示控件，支持Sprite。


*/
package myreport.util
{
	import flash.display.Sprite;
	import flash.filters.DropShadowFilter;
	
	import mx.core.UIComponent;
	
	import hlib.DisposeUtil;
	import hlib.IDispose;
 
	public class PageView extends UIComponent implements IDispose
	{
		private var _Shadow:DropShadowFilter;
		private var _Page:Object;
		public function get Page():Object
		{
			return _Page;
		}
		//================IDispose====================
		protected var _Disposed:Boolean = false;
		public function Dispose():void
		{
			if (_Disposed)
				return;
			_Disposed = true;
			
			var f:Array = filters;
			filters = null;
			DisposeUtil.Dispose(f);
			DisposeUtil.Dispose(this);
			_Shadow = null;
			_Page = null;
		}
		
		public function PageView(page:Sprite)
		{
			super();
			_Page = page;
			_Shadow = new DropShadowFilter(2, 90, 0, 0.8, 4, 4);

			addChild(page);
			width = page.width;
			height = page.height;
		}
 
		override protected function updateDisplayList(w:Number, h:Number):void
		{
			if(!w || !h)
				return;
			graphics.clear();
			graphics.beginFill(0xffffff);
			graphics.drawRect(0,0,w,h);
			graphics.endFill();
			var f:Array = filters;
			filters=[_Shadow];
			DisposeUtil.Dispose(f);
		}
	}
}