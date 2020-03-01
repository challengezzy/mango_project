/*
Copyright (c) 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


哈希数据结构


*/

package hlib
{
	import flash.utils.Dictionary;

	public class Hash implements IDispose, IClone
	{
		public var Keys:Array = new Array();
		private var _Dict:Dictionary = new Dictionary();
 
		private var _Disposed:Boolean = false;	
		protected final function get Disposed():Boolean
		{
			return _Disposed;
		}
		protected function Disposing():void
		{
			
		}
		public final function Dispose():void
		{
			if(_Disposed) return;
			_Disposed = true;
			Disposing();
			DisposeUtil.Dispose(Keys);
			DisposeUtil.Dispose(_Dict);
			Keys = null;
			_Dict = null;
		}
		
		public function Hash()
		{
		}
		
		public function Clone():*
		{
			var clone:Hash = new Hash();
			clone.Keys = CloneUtil.Clone(Keys);
			clone._Dict = CloneUtil.Clone(_Dict);
			return clone;
		}
		
		public function Clear():void
		{
			DisposeUtil.Clear(Keys);
			DisposeUtil.Clear(_Dict);
		}
		
		public function Get(key:*):*
		{
			return _Dict[key];
		}
		
		public function Set(key:*, value:*):*
		{
			if(key === undefined || key === null)
				throw new Error("key不能为空。");
			var index:int = Keys.indexOf(key);
			if(index<0)
				Keys.push(key);
			var old:* = _Dict[key];
			_Dict[key] = value;
			return old;
		}
		public function ContainsKey(key:*):Boolean
		{
			return Keys.indexOf(key)>=0;
		}
		public function ContainsValue(value:*):Boolean
		{
			for each(var key:* in Keys)
			{
				if(_Dict[key] == value)
					return true;
			}
			return false;
		}
		public function Remove(key:*):*
		{
			var index:int = Keys.indexOf(key);
			var val:*;
			if(index>=0)
			{
				Keys.splice(index, 1);
				val = _Dict[key];
				delete _Dict[key];
			}
			return val;
		}
		/**
		 * @param keys: 为null时设置src的所有属性
		 */ 
		public function Fill(src:Hash, keys:Array=null):void
		{
			if(!keys)
			{
				keys = src.Keys
			}
			
			for each(var key:* in keys)
			{
				if(src.ContainsKey(key))
					Set(key, src.Get(key));
			}
		}
		/**
		 * @param keys: 为null时比较src的所有属性
		 */ 
		public function Equal(src:Hash, keys:Array = null):Boolean
		{
			if(!keys)
			{
				keys = src.Keys
			}	
			
			for each(var key:* in keys)
			{
				if(!ContainsKey(key) && !src.ContainsKey(key))
					continue;
				if(!ContainsKey(key) || !src.ContainsKey(key))
					return false;
				if(Get(key) != src.Get(key))
					return false;
			}
			
			return true;
		}
		
		public function DistinctKeys(src:Hash):Array
		{
			if(!src)
				return Keys;
			
			var keys:Array = new Array();
			for each(var key:String in src.Keys)
			{
				if(!ContainsKey(key) || Get(key)!= src.Get(key))
				{
					keys.push(key);
				}
			}
			return keys;
		}
	}
}