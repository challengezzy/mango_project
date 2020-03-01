/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


键盘值工具类

*/
package hlib
{
	import flash.ui.Keyboard;
	
	public final class KeyboardUtil
	{
		/**
		 * 是否输入键（会产生输入字符）
		 */ 
		public static function IsInputKey(keyCode:uint):Boolean
		{
			return (keyCode>=48&&keyCode<=57)||
				(keyCode>=65&&keyCode<=90)||
				(keyCode>=186&&keyCode<=192)||
				(keyCode>=219&&keyCode<=222);
		}
		/**
		 * 是否字母键
		 */ 
		public static function IsLetter(keyCode:uint):Boolean
		{
			return keyCode>=65&&keyCode<=90;
		}
		/**
		 * 是否数字键
		 */ 
		public static function IsNumber(keyCode:uint):Boolean
		{
			return keyCode>=48&&keyCode<=57;
		}
		public static function IsEnter(keyCode:uint):Boolean
		{
			return keyCode == Keyboard.ENTER;
		}
		public static function IsTab(keyCode:uint):Boolean
		{
			return keyCode == Keyboard.TAB;
		}
 
	}
}