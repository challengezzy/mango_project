/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


遮罩界面基类。


*/

package hlib
{
	import flash.geom.Rectangle;
	
	public class MaskComponentBase extends ComponentBase
	{
		public function MaskComponentBase()
		{
			super();
		}
		protected override function SuperResize():void
		{
			var rect:Rectangle = scrollRect;
			if(!rect)
				rect = new Rectangle();
			rect.width = Math.max(0, width);
			rect.height = Math.max(0, height);
			
			scrollRect = rect;

			super.SuperResize();
		}
	}
}