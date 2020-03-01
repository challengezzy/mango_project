package myreport.design.chart
{
	import hlib.ComponentBase;
	import hlib.LayoutUtil;
	import hlib.Parser;
	import hlib.TextBase;
	
	import mx.controls.listClasses.IListItemRenderer;
 
	public class ColorItemRender extends ComponentBase implements IListItemRenderer
	{
		private var _Label:TextBase;
		private var _Data:Object;
		private var _Color:uint;
		public function ColorItemRender()
		{
			super();
			_Label = TextBase.CreateText("");
			addChild(_Label);
			_Label.x = 36;
			width = 300;
		}
		protected override function OnResize():void
		{
			graphics.clear();
			if(height>=4)
			{
				graphics.lineStyle(1);
				graphics.beginFill(_Color);
				graphics.drawRect(2,2,32,height-4);
				graphics.endFill();
			}
			
			hlib.LayoutUtil.VerticalAlign(_Label, height, "middle");
		}
		
		public function get data():Object
		{
			return _Data;
		}
		
		public function set data(value:Object):void
		{
			if(_Data == value)
				return;
			_Data = value;
			
			if(_Data && _Data.hasOwnProperty("Value"))
				_Color = _Data.Value;
			else
				_Color = uint(_Data);
			
			_Label.text = hlib.Parser.ToHexColor(_Color);
			OnResize();
 
		}
	}
}