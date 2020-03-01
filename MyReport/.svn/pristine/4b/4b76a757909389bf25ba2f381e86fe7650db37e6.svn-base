/*
Copyright (c) 2010 - 2010, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


该类是克隆对象的工具类。


*/

package hlib
{
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.IList;

	public final class CloneUtil
	{
		public static function Clone(value:Object):*
		{
			if (value is IList)
				return CloneIList(value as IList);
			else if (value is Array)
				return CloneArray(value as Array);
			else if(value is Dictionary)
				return CloneDictionary(value as Dictionary);
			else if (value is IClone)
				return IClone(value).Clone();
			else
				return value;
		}

		private static function CloneIList(value:IList):IList
		{
			var result:IList;
			if(value is ArrayCollection)
				result = new ArrayCollection();
			else
				result = new ArrayList();
			for (var i:int = 0; i < value.length; i++)
			{
				result.addItem(CloneObject(value[i]));		
			}
			return result;
		}

		private static function CloneArray(value:Array):Array
		{
			var result:Array = new Array();
			for (var i:int = 0; i < value.length; i++)
			{
				result.push(CloneObject(value[i]));
			}
			return result;
		}

		private static function CloneDictionary(value:Dictionary):Dictionary
		{
			var result:Dictionary = new Dictionary();
			for(var key:* in value)
			{
				result[key] = CloneObject(value[key]);
			}
			return result;
		}
		
		public static function CloneObject(value:Object):*
		{
			if (value is IClone)
				return IClone(value).Clone();
			else if (value is IList)
				return CloneIList(value as IList);
			else if (value is Array)
				return CloneArray(value as Array);
			else if(value is Dictionary)
				return CloneDictionary(value as Dictionary);
			else
				return value;
		}
	}
}