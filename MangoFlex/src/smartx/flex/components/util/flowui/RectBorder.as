package smartx.flex.components.util.flowui
{
	import flash.display.Shape;

	public class RectBorder extends Shape
	{
		private var _thickness: Number;
		private var _color: uint;
		private var _width: uint;
		private var _height: uint;
		private var _dotColor: uint;
		private var _dotWidth: uint;
		private var _dotHeight: uint;
		
		public function get thickness(): Number{
			return _thickness;
		}
		
		public function set thickness(value: Number): void{
			_thickness = value;
		}
		
		public function get color(): uint{
			return _color;
		}
		
		public function set color(value: uint): void{
			_color = value;
		}
		
		public function get rectWidth(): uint{
			return _width;
		}
		
		public function set rectWidth(value: uint): void{
			_width = value;
		}
		
		public function get rectHeight(): uint{
			return _height;
		}
		
		public function set rectHeight(value: uint): void{
			_height = value;
		}
		
		public function get dotColor(): uint{
			return _dotColor;
		}
		
		public function set dotColor(value: uint): void{
			_dotColor = value;
		}
		
		public function get dotWidth(): uint{
			return _dotWidth;
		}
		
		public function set dotWidth(value: uint): void{
			_dotWidth = value;
		}
		
		public function get dotHeight(): uint{
			return _dotHeight;
		}
		
		public function set dotHeight(value: uint): void{
			_dotHeight = value;
		}
		
		public function RectBorder(thickness: Number = 0, color: uint = 0, 
			width: uint = 0, height: uint = 0, dotColor: uint = 0,
			dotWidth: uint = 0, dotHeight: uint = 0)
		{
			super();
			this._color = color;
			this._thickness = thickness;	
			this._height = height;
			this._width = width;
			this._dotColor = dotColor;
			this._dotWidth = dotWidth;
			this._dotHeight = dotHeight;
			
			this.draw();
		}
		
		private function draw(): void{
			//设置样式
			this.graphics.lineStyle(thickness, color);
			//画边框
			this.graphics.drawRect(0, 0, rectWidth, rectHeight);
			//四个角分别画四个黑点
			this.graphics.lineStyle(thickness, dotColor);
			this.graphics.beginFill(dotColor);
			this.graphics.drawRect(0, 0, dotWidth, dotHeight);
			this.graphics.drawRect(rectWidth - dotWidth, 0, dotWidth, dotHeight);
			this.graphics.drawRect(0, rectHeight - dotHeight, dotWidth, dotHeight);
			this.graphics.drawRect(rectWidth - dotWidth, rectHeight - dotHeight, dotWidth, dotHeight);
			this.graphics.endFill();
		}
	}
}