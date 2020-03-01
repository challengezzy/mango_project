package smartx.flex.components.util.flowui
{
	import flash.events.MouseEvent;
	import flash.text.TextField;
	import flash.text.TextFieldAutoSize;
	
	import mx.controls.Image;

	[Event(name=IconEvent.ICON_MOUSE_DOWN, type="events.IconEvent")]
	[Event(name=IconEvent.ICON_MOUSE_UP, type="events.IconEvent")]
	[Event(name=IconEvent.ICON_MOVE, type="events.IconEvent")]

	public class FlowIcon extends Image
	{
		private var _label: String; //图标上的文字说明
		private var _selected: Boolean; //图标是否被选中
		private var _identity: String; //图标唯一标识符
		private var _icon: Object; //图像数据
		public var iconWidth:int;
		public var iconHeight:int;
		
		private var text: TextField = new TextField();

		public function get icon(): Object{
			return _icon;
		}
		
		public function set icon(value: Object): void{
			this._icon = value;
			draw();
		}
		
		
		public function get identity(): String{
			return _identity;
		}
		
		public function set identity(value: String): void{
			this._identity = value;
			draw();
		}
		
		public function get selected(): Boolean{
			return _selected;
		}
		
		public function set selected(value: Boolean): void{
			this._selected = value;
			draw();
		}
		
		public function get label(): String{
			return _label;
		}
		
		public function set label(value: String): void{
			this._label = value;
			draw();
		}
		
		
		public function FlowIcon(icon: Object, iconWidth:int,iconHeight:int,name:String,label: String = "节点",
			selected : Boolean = false)
		{
			super();
			this._icon = icon;
			this._label = label;
			this._identity = name;
			this._selected = selected;
			this.iconHeight = iconHeight;
			this.iconWidth = iconWidth;
			
			this.source = icon;
			text.autoSize = TextFieldAutoSize.LEFT;
			text.x = -30;
			text.y = iconHeight + 3;
			text.width = iconWidth - 2*text.x;
			text.wordWrap = true;
			//text.border=true;
			this.addChild(text);
			
			this.draw();
			this.events();
		}
		
		//画图标
		private function draw(): void{
			this.source = this.icon;
			
			text.htmlText = "<p align='center'><b>" + this.label + "</b></p>";

			if(_selected){
				var hasRect:Boolean = false;
				for(var j: int = this.numChildren - 1; j >= 0 ; j --){
					if(this.getChildAt(j) is RectBorder){
						hasRect = true;
						break;
					}
				}
				if(!hasRect){
					var rect:RectBorder = new RectBorder(2, 0xFF0000, this.width, this.height, 0x000000, 2,2);
					this.addChild(rect);
				}
			}
			else{
				for(var k: int = this.numChildren - 1; k >= 0 ; k --){
					if(this.getChildAt(k) is RectBorder){
						this.removeChildAt(k);
					}
				}
			}
		}
		
		
		//事件绑定
		private function events(): void{
			this.addEventListener(MouseEvent.MOUSE_DOWN, onIconMouseDown);
			this.addEventListener(MouseEvent.MOUSE_UP, onIconMouseUp);
			this.addEventListener(MouseEvent.MOUSE_MOVE, onIconMove);
		}
		
		//触发鼠标按下事件
		private function onIconMouseDown(event: MouseEvent): void{
			var e: IconEvent= new IconEvent(IconEvent.ICON_MOUSE_DOWN);
			e.icon = this;
			this.dispatchEvent(e);			
		}
		
		//触发鼠标弹起事件
		private function onIconMouseUp(event: MouseEvent): void{
			var e: IconEvent= new IconEvent(IconEvent.ICON_MOUSE_UP);
			e.icon = this;
			this.dispatchEvent(e);			
		}
		
		//触发鼠标移动事件
		private function onIconMove(event: MouseEvent): void{
			var e: IconEvent= new IconEvent(IconEvent.ICON_MOVE);
			e.icon = this;
			this.dispatchEvent(e);	
		}
	}
}