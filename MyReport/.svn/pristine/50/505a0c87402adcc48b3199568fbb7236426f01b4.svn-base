/*
Copyright (c), Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：

*/
package myreport.util
{
	import flash.events.ContextMenuEvent;
	import flash.ui.ContextMenu;
	import flash.ui.ContextMenuItem;
	import flash.utils.Dictionary;
	
	import hlib.DisposeUtil;
	import hlib.IDispose;
 
	public final class ContextMenuExtension implements IDispose
	{
		private var _Menu:ContextMenu;
		private var _MenuShowFunc:Function;
		private var _MenuItemClickFunc:Function;
		private var _Items:Dictionary = new Dictionary();
		//================IDispose====================
		protected var _Disposed:Boolean = false;
		
		public function Dispose():void
		{
			if (_Disposed)
				return;
			_Disposed = true;
			//trace("ContextMenuExtension dispose...");
			var items:Array = _Menu.customItems;
			_Menu.customItems = null;
			for each(var item:ContextMenuItem in items)
			{
				item.removeEventListener(ContextMenuEvent.MENU_ITEM_SELECT, OnContextMenuItemClick);
			}
			hlib.DisposeUtil.Clear(items);
			_Menu.removeEventListener(ContextMenuEvent.MENU_SELECT, OnContextMenu);
			_Menu = null;
			_MenuShowFunc = null;
			_MenuItemClickFunc = null;

			hlib.DisposeUtil.Clear(_Items);
			_Items = null;
		}
		
		/**
		 * @param items: "-"字符表示分隔符号
		 * @param menuShowFunc: function():void
		 * @param menuItemClickFunc: function(item:ContextMenuItem):void
		 */ 
		public function ContextMenuExtension(items:Array, menuShowFunc:Function, menuItemClickFunc:Function):void
		{
			_Menu = new ContextMenu();
			_MenuShowFunc = menuShowFunc;
			_MenuItemClickFunc = menuItemClickFunc;
			_Menu.addEventListener(ContextMenuEvent.MENU_SELECT, OnContextMenu);
			_Menu.hideBuiltInItems();
			_Menu.builtInItems.print = false;
			
			
			var item:ContextMenuItem;
			var separatorBefore:Boolean = false;
			for each(var name:String in items)
			{
				if(name == "-")
				{
					separatorBefore = true;
				}
				else
				{
					item = new ContextMenuItem(name, separatorBefore);
					separatorBefore = false;
					item.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, OnContextMenuItemClick);
					_Menu.customItems.push(item);
					_Items[name] = item;
				}
			}
		}
		public function get Memu():ContextMenu
		{
			return _Menu;
		}
		public function GetItem(name:String):ContextMenuItem
		{
			return _Items[name];
		}
		private function OnContextMenu(e:ContextMenuEvent):void
		{
	 		if(_MenuShowFunc != null)
				_MenuShowFunc();
		}
		
		
		private function OnContextMenuItemClick(e:ContextMenuEvent):void
		{
			if(_MenuItemClickFunc != null)
			{
				var item:ContextMenuItem = e.currentTarget as ContextMenuItem;
				if(item)
					_MenuItemClickFunc(item);
			}
		}
	}
}