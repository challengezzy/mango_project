/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


程序集信息。


*/

package myreport
{
 
	public final class Assembly
	{
		public static const SHOW_WATERMARK:Boolean = false;
		
		public static const NAME:String = "MyReport报表引擎";
		public static const VERSION:String = "2.6.4.8";
		public static const AUTHOR:String = "Hunk Cai";
		public static const BLOG:String = "http://blog.csdn.net/hunkcai";
		public static const COPYRIGHT:String = "Copyright 2010 - 2012 Hunk.Cai All Rights Reserved.";

		
		public static function Trace():String
		{
			return "【" + NAME + "】\r\n" + "版本:" + VERSION + "\r\n" + "作者:" + AUTHOR + "\r\n" + "博客:" + BLOG + "\r\n" + "版权声明:" + COPYRIGHT;
		}
 
	 
	}
}