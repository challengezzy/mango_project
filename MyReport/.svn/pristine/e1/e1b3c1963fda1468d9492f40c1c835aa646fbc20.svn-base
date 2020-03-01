/*
Copyright (c), Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


集合处理的工具类。


*/
package hlib
{
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	
	public final class CollectionUtil
	{
		/**
		 * dict键值是数字的字典类型，删除某个键值时要进行地址搬移
		 * @param index: 删除的索引
		 * @param max: 删除前的总长度
		 * @return: 返回删除的对象
		 */ 
		public static function Splice(dict:Dictionary, index:int, max:int):*
		{
			var remove:* = dict[index];
			for(var i:int=index;i<max;i++)
			{
				dict[i] = dict[i+1];
			}
			delete dict[max];
			return remove;
		}
		
		/**
		 * 对table进行分组排序
		 * @param group: 分组属性
		 * @param order: 删除前的总长度
		 * @return: 返回分组排序
		 */ 
		public static function GroupAndOrderTable(table:ArrayCollection, group:String, order:String):ArrayCollection
		{
			var sortTable:ArrayCollection;
			if(group || order)
			{
				sortTable = new ArrayCollection(table.source);
				var sort:Sort = new Sort();
				var sortFields:Array = new Array();
				if(group)
					sortFields.push(new SortField(group));
				if(order)
					sortFields.push(new SortField(order));
				sort.fields = sortFields;
				sortTable.sort = sort;
				sortTable.refresh();
			}
			return sortTable;
		}
 
		/**
		 * 把数组src填充到数组dest
		 * @return： 返回填充后的dest
		 */ 
		public static function AddArrayRange(dest:Array, src:Array):Array
		{
			if(src)
			{
				for each(var item:Object in src)
				{
					dest.push(item);
				}
			}
			return dest;
		}
	}
}