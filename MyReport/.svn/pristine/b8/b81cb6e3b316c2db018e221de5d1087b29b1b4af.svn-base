package myreport.util
{
	import hlib.ComponentBase;
	import hlib.DrawUtil;
	import hlib.LayoutUtil;
	
	public final class ProgressBar extends ComponentBase
	{
		private var _Curent:Number = 0;
		private var _Total:Number = 0;
		public function ProgressBar()
		{
			super();
		}
		protected override function OnResize():void
		{
			Paint();
		}
		
		public function SetProgress(current:Number, total:Number):void
		{
			_Curent = current;
			_Total = total;
			Paint();
		}
		
		private function Paint():void
		{
			graphics.clear();
			if(!width || !height)
				return;
			var h:Number = Math.floor(height * 0.4);
			
			var w:Number = 0;
			if(_Total)
				w = width*_Curent/_Total;

			hlib.DrawUtil.DrawBackgrouand(graphics, 0, 0, width, h, [0xF3F3F3, 0xDADADA],1,"linear");
			hlib.DrawUtil.DrawBackgrouand(graphics, 0, h, width, height-h, [0xC9C9C9, 0xD5D5D5],1,"linear");
			
			hlib.DrawUtil.DrawBackgrouand(graphics, 0, 0, w, h, [0xCDFFCD, 0x9CEEAC],1,"linear");
			hlib.DrawUtil.DrawBackgrouand(graphics, 0, h, w, height-h, [0x04D42B, 0x5AEA6B],1,"linear");
			
			hlib.DrawUtil.DrawBorder(graphics, 0, 0, width, height, 0xB2B2B2, 0.8);
		}
	}
}