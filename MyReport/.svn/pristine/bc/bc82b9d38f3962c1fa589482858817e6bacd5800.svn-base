/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


工具栏。

*/
package myreport.util
{
	import hlib.DrawUtil;
	
	import mx.containers.HBox;
	public class ToolBar extends HBox
	{
		public function ToolBar()
		{
			super();
			setStyle("verticalAlign", "middle");
		    verticalScrollPolicy = "off";
			horizontalScrollPolicy = "off";
		}
		protected override function updateDisplayList(w:Number, h:Number):void
		{
			super.updateDisplayList(w, h);
			graphics.clear();
			graphics.lineStyle(1,0x696969);
			DrawUtil.DrawBackgrouand(graphics, 0, 0, w-1, h, [0xFFFFFF,0xCCCCCC], [1,1], "linear");
		}
	}
}