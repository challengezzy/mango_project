package smartx.flex.components.util
{
	import flash.display.*;
	import flash.events.*;
	import flash.geom.*;
	import flash.net.*;
	import flash.system.*;
	import flash.utils.*;
	
	import mx.containers.accordionClasses.*;
	import mx.controls.tabBarClasses.*;
	import mx.core.*;
	import mx.core.BitmapAsset;

	public class IconUtility extends BitmapAsset
	{
		public function IconUtility()
		{
			super();
			addEventListener(Event.ADDED_TO_STAGE, addedHandler);
			addEventListener(Event.REMOVED_FROM_STAGE,removeHandler);
			return;
		}
		
		private function removeHandler(event:Event):void{
			bitmapData.dispose();
			bitmapData = null;
		}
		
		public static function destroy(target:Object):void{
			var tmp:* = dictionary[target];
			if(tmp != null && tmp.source is Loader){
				Loader(tmp.source).unloadAndStop();
			}
			delete dictionary[target];
		}
		
		private function getData(object:Object):void
		{
			var loc1:*=null;
			var loc2:*=null;
			var loc3:*=null;
			loc1 = dictionary[object];
			if (loc1) 
			{
				loc2 = loc1.source;
				if (loc1.width > 0 && loc1.height > 0) 
				{
					bitmapData = new BitmapData(loc1.width, loc1.height, true, 16777215);
				}
				if (loc2 is Loader) 
				{
					loc3 = loc2 as Loader;
					if (!loc3.content) 
					{
						loc3.contentLoaderInfo.addEventListener(Event.COMPLETE, completeHandler, false, 0, true);
					}
					else 
					{
						displayLoader(loc3);
					}
				}
			}
			return;
		}
		
		private function addedHandler(event:flash.events.Event):void
		{
			var loc1:*=null;
			var loc2:*=null;
			if (parent) 
			{
				if (parent is AccordionHeader) 
				{
					loc1 = parent as AccordionHeader;
					getData(loc1.data);
				}
				else if (parent is Tab) 
				{
					loc2 = parent as Tab;
					getData(loc2.data);
				}
				else 
				{
					getData(parent);
				}
			}
			return;
		}
		
		private function displayLoader(loader:flash.display.Loader):void
		{
			var loc1:*=null;
			if (!bitmapData) 
			{
				bitmapData = new BitmapData(loader.content.width, loader.content.height, true, 16777215);
			}
			bitmapData.draw(loader, new Matrix(bitmapData.width / loader.width, 0, 0, bitmapData.height / loader.height, 0, 0));
			if (parent is UIComponent) 
			{
				loc1 = parent as UIComponent;
				loc1.invalidateSize();
			}
			return;
		}
		
		private function completeHandler(event:flash.events.Event):void
		{
			if (event && event.target && event.target is LoaderInfo) 
			{
				displayLoader(event.target.loader as Loader);
			}
			return;
		}
		
		public static function getClass(target:mx.core.UIComponent, source:String, width:Number=NaN, height:Number=NaN):Class
		{
			var loc1:*=null;
			if (!dictionary) 
			{
				dictionary = new Dictionary(false);
			}
			loc1 = new Loader();
			loc1.load(new URLRequest(source as String), new LoaderContext(true));
			dictionary[target] = {"source":loc1, "width":width, "height":height};
			return IconUtility;
		}
		
		private static var dictionary:flash.utils.Dictionary;
	}
}