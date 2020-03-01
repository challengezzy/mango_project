/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


按钮皮肤。


*/
/*
   over/selected:
   --begin:fefc9f|feb42b
   --end:feb42b|fde373
   --border:a97601|fefdcb
 */
package myreport.util
{

	import flash.display.GradientType;
 
	import mx.core.UIComponent;
	import mx.skins.Border;
 
	public class ButtonSkin extends Border
	{

		public function ButtonSkin()
		{
			super();
		}

		override public function get measuredWidth():Number
		{
			return UIComponent.DEFAULT_MEASURED_MIN_WIDTH;
		}

		override public function get measuredHeight():Number
		{
			return UIComponent.DEFAULT_MEASURED_MIN_HEIGHT;
		}

		private var _CornerRadius:Number = 4;
		private var _OverFillColors_T:Array = [0xfefc9f, 0xfeb42b];
		private var _OverFillColors_B:Array = [0xfeb42b, 0xfde373];
		private var _OverBorderColor:uint = 0xa97601;
		private var _OverBorderColor_I:uint = 0xfefdcb;

		private var _DisabledBorderColor:uint = 0x878a8c;
		private var _DisabledBorderColor_I:uint = 0xffffff;
		private var _DisabledFillColors:Array = [0xe4eded, 0xeeeeee];
		
		override protected function updateDisplayList(w:Number, h:Number):void
		{
			super.updateDisplayList(w, h);

			var cr:Number = Math.max(0, _CornerRadius);
			var cr1:Number = Math.max(0, _CornerRadius - 1);
			var cr2:Number = Math.max(0, _CornerRadius - 2);

			graphics.clear();
			switch (name)
			{
				case "selectedUpSkin":
				case "selectedOverSkin":
				case "selectedDisabledSkin":
				case "overSkin":
				{
					//outer border
					drawRoundRect(0, 0, w, h, cr, _OverBorderColor, 1, null, GradientType.LINEAR, null, {x: 1, y: 1, w: w - 2, h: h - 2, r: cr1});
					//inner border
					drawRoundRect(1, 1, w - 2, h - 2, cr1, _OverBorderColor_I, 1, null, GradientType.LINEAR, null, {x: 2, y: 2, w: w - 4, h: h - 4, r: cr2});
					//fill top
					drawRoundRect(2, 2, w - 4, (h - 4) / 2, {tl: cr2, tr: cr2, bl: 0, br: 0}, _OverFillColors_T, 1, verticalGradientMatrix(2, 2, w - 4, (h - 4) / 2));
					//fill bottom
					drawRoundRect(2, 2 + (h - 4) / 2, w - 4, (h - 4) / 2, {tl: 0, tr: 0, bl: cr2, br: cr2}, _OverFillColors_B, 1, verticalGradientMatrix(2, 2 + (h - 4) / 2, w - 4, (h - 4) / 2));

					break;
				}
				case "disabledSkin":
				{
					//outer border
					drawRoundRect(0, 0, w, h, cr, _DisabledBorderColor, 1, null, GradientType.LINEAR, null, {x: 1, y: 1, w: w - 2, h: h - 2, r: cr1});
					//inner border
					drawRoundRect(1, 1, w - 2, h - 2, cr1, _DisabledBorderColor_I, 1, null, GradientType.LINEAR, null, {x: 2, y: 2, w: w - 4, h: h - 4, r: cr2});
					//fill top
					drawRoundRect(2, 2, w - 4, h - 4, {tl: cr2, tr: cr2, bl: cr2, br: cr2}, _DisabledFillColors, 1, verticalGradientMatrix(2, 2, w - 4, h - 4));

					break;
				}
			}
		}
	}
}
