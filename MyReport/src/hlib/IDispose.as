/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


释放资源的接口

调用该方法能够释放对象内的所有引用以便垃圾回收。

该方法应该能够重复调用。


*/

package hlib
{
	/*
	 * 释放资源接口
	 */
	public interface IDispose
	{
		function Dispose():void;
	}
}