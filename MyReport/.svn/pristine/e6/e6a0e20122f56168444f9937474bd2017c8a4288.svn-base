/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


路径处理工具类。


*/

package hlib
{
	public final class PathUtil
	{
		/**This code produces output similar to the following:
		 * HasExtension('myfile.ext') returns True
		 * HasExtension('mydir\myfile') returns False
		 * HasExtension('C:\mydir.ext\') returns False
		 */
		public static function HasExtension(path:String):Boolean
		{
			var index:int = path.lastIndexOf(".");
			if (index < 0)
				return false;
			var ext:String = path.substring(index);
			if (ext.lastIndexOf("\\") >= 0)
				return false;
			else
				return true;
		}
		
		/**This code produces output similar to the following:
		 * GetExtension('C:\mydir.old\myfile.ext') returns '.ext'
		 * GetExtension('C:\mydir.old\') returns ''
		 */
		public static function GetExtension(path:String):String
		{
			var index:int = path.lastIndexOf(".");
			if (index < 0)
				return "";
			var ext:String = path.substring(index);
			if (ext.lastIndexOf("\\") >= 0)
				return "";
			else
				return ext;
		}
		
		/**This code produces output similar to the following:
		 * ChangeExtension(C:\mydir\myfile.com.extension, '.old') returns 'C:\mydir\myfile.com.old'
		 * ChangeExtension(C:\mydir\myfile.com.extension, '') returns 'C:\mydir\myfile.com'
		 * ChangeExtension(C:\mydir\, '.old') returns 'C:\mydir\.old'
		 */
		public static function ChangeExtension(path:String, extension:String):String
		{
			var folder:String = GetDirectoryName(path);
			if(folder)
				return Combine(GetDirectoryName(path) , GetFileNameWithoutExtension(path) + extension);
			return GetFileNameWithoutExtension(path) + extension;
		}
		/**This code produces output similar to the following:
		 * ChangeExtension(C:\mydir\myfile.com.extension, 'file') returns 'C:\mydir\file'
		 * ChangeExtension(C:\mydir\file, '') returns 'C:\mydir\'
		 * ChangeExtension(C:\mydir\, 'file') returns 'C:\mydir\file'
		 */
		public static function ChangeFileName(path:String, fileName:String):String
		{
			return Combine(GetDirectoryName(path) , fileName);
		}
		
		/**This code produces output similar to the following:
		 * GetDirectoryName('C:\mydir\myfile.ext') returns 'C:\mydir'
		 * GetDirectoryName('C:\mydir\') returns 'C:\mydir'
		 * GetDirectoryName('C:\') returns ''
		 * GetDirectoryName('C:') returns ''
		 */
		public static function GetDirectoryName(path:String):String
		{
			var str:String = "";
			var index:int = path.lastIndexOf("\\");
			if (index < 0)
			{
				str = "";
			}			
			str = path.substring(0, index);
			return str;
		}

		
		/**This code produces output similar to the following:
		 * GetFileName('C:\mydir\myfile.ext') returns 'myfile.ext'
		 * GetFileName('C:\mydir\') returns ''
		 * GetFileName('C:') returns ''
		 */
		public static function GetFileName(path:String):String
		{
			var fileName:String = "";
			var index:int = path.lastIndexOf("\\");
			fileName = path.substring(index + 1);
			return fileName;
		}
		
		/**This code produces output similar to the following:
		 * GetFileNameWithoutExtension('C:\mydir\myfile.ext') returns 'myfile'
		 * GetFileName('C:\mydir\') returns ''
		 */
		public static function GetFileNameWithoutExtension(path:String):String
		{
			
			var fileName:String = GetFileName(path);
			var index:int = fileName.lastIndexOf(".");
			if (index >= 0)
				fileName = fileName.substring(0, index);
			return fileName;
		}
		
		/**This code produces output similar to the following:
		 * GetPathRoot('\mydir\') returns '\'
		 * GetPathRoot('myfile.ext') returns ''
		 * GetPathRoot('C:\mydir\myfile.ext') returns 'C:\'
		 */
		public static function GetPathRoot(path:String):String
		{
			var index:int = path.indexOf("\\");
			var str:String = path.substring(0, index + 1);
			return str;
		}
		
		/**This code produces output similar to the following:
		 * GetChildPath('\mydir\') returns 'mydir\'
		 * GetChildPath('myfile.ext') returns ''
		 * GetChildPath('C:\mydir\myfile.ext') returns 'mydir\myfile.ext'
		 */
		public static function GetChildPath(path:String):String
		{
			var index:int = path.indexOf("\\");
			var str:String = path.substring(index + 1);
			return str;
		}
		
		
		/**This code produces output similar to the following:
		 * IsPathRooted('C:\mydir\myfile.ext') returns True
		 * IsPathRooted('\\myPc\mydir\myfile') returns True
		 * IsPathRooted('mydir\sudir\') returns False
		 */
		public static function IsPathRooted(path:String):Boolean
		{
			var result:Boolean = false;
			if (path.indexOf("\\\\") == 0)
				result = true;
			else if (path.indexOf(":\\") == 1)
				result = true;
			return result;
		}
		
		/**This code produces output similar to the following:
		 * "c:\test" "123" ->"c:\test\123"
		 * "c:\test" "\123" ->"c:\test\123"
		 * "c:\test\" "\123" ->"c:\test\123"
		 * "c:\test\" "123" ->"c:\test\123"
		 */
		public static function Combine(path1:String, path2:String):String
		{
			if (path1.charAt(path1.length - 1) == "\\" && path2.charAt(0) == "\\")
				return path1 + path2.substring(1);
			else if (path1.charAt(path1.length - 1) == "\\" && path2.charAt(0) != "\\" || path1.charAt(path1.length - 1) != "\\" && path2.charAt(0) == "\\")
				return path1 + path2;
			else if (path1.charAt(path1.length - 1) != "\\" && path2.charAt(0) != "\\")
				return path1 + "\\" + path2;
			return null;
		}
		
		/**This code produces output similar to the following:
		 * ToOpposite('Project\Demo1\Asset') returns 'Project/Demo1/Asset'
		 * ToOpposite('c:\Project\Demo1\Asset\text.xml', 'c:\Project\') returns 'Demo1/Asset'
		 */
		public static function ToOpposite(path:String, root:String = null):String
		{
			if (root == null || root.length == 0 || root.length > path.length)
				return path.replace(/\\/g, "/");
			var result:String = path.substring(root.length).replace(/\\/g, "/");
			if (result.length > 0 && result.charAt(0) == "/")
				result = result.substring(1);
			return result;
		}
		
		/**This code produces output similar to the following:
		 * ToLocal('Project/Demo1/Asset') returns 'Project\Demo1\Asset'
		 */
		public static function ToLocal(path:String, root:String = null):String
		{
			var reg:RegExp = new RegExp("/", "g");
			if (root == null)
				return path.replace(reg, "\\");
			if (path == null || path.length == 0)
				return "";
			return Combine(root, path.replace(reg, "\\"));
		}
		
		/**比较路径是否相等
		 */
		public static function Compare(path1:String, path2:String, ignoreCase:Boolean = true):Boolean
		{
			if(path1 == path2)
				return true;
			if(ignoreCase)
				return Combine(path1, "d").toLowerCase() == Combine(path2, "d").toLowerCase();
			else
				return Combine(path1, "d") == Combine(path2, "d");
		}
		
		/**
		 * 合并Url地址
		 * "/test" "123" ->"/test/123"
		 * "/test" "/123" ->"/test/123"
		 * "/test/" "/123" ->"/test/123"
		 * "/test/" "123" ->"/test/123"
		 */
		public static function CombineURL(path1:String, path2:String):String
		{
			if (path1.charAt(path1.length - 1) == "/" && path2.charAt(0) == "/")
				return path1 + path2.substring(1);
			else if (path1.charAt(path1.length - 1) == "/" && path2.charAt(0) != "/" || path1.charAt(path1.length - 1) != "/" && path2.charAt(0) == "/")
				return path1 + path2;
			else if (path1.charAt(path1.length - 1) != "/" && path2.charAt(0) != "/")
				return path1 + "/" + path2;
			return null;
		}
		
		/**
		 * 获取网络路径服务器
		 * 
		 */ 
		public static function GetUrlHost(url:String):String
		{
			if(!url)
				return "";
			var start:int = url.indexOf("://");
			if(start>0)
			{
				start += 3;
				var end:int = url.indexOf("/", start);
				if(end>start)
					return url.substring(0, end);
			}
			return url;
		}
	}
}