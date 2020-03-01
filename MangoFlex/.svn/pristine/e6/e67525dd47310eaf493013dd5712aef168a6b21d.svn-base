package smartx.flex.components.util.flowui
{
	import flash.geom.Point;
	import flash.text.TextField;
	import flash.text.TextFieldAutoSize;
	
	import mx.core.UIComponent;

	public class ArrowLine extends UIComponent
	{
       //箭头的大小
        private var _radius:int=6;
        private var _fromPoint:Point;
        private var _toPoint:Point;
        //线性的颜色
        private var _lineColor:uint=0x000000;
        
        private var _selectedLineColor:uint= 0xFF0000;
        //是否需要画箭头
        private var _needArrow:Boolean=true;
        
        public var identity:String;
        
        private var _selected:Boolean = false;
        
        private var _label:String;
        
        private var labelField:TextField = new TextField()
        
        public function ArrowLine(identity:String, fromPoint:Point, toPoint:Point, label:String="",lineColor:uint = 0x000000, selectedLineColor:uint = 0xFF0000, 
        radius:int = 6, needArrow:Boolean = true,selected:Boolean = false){
        	this.identity = identity;
        	this._fromPoint = fromPoint;
        	this._toPoint = toPoint;
        	this._lineColor = lineColor;
        	this._selectedLineColor = selectedLineColor;
        	this._radius = radius;
        	this._needArrow = needArrow;
        	this._selected = selected;
        	this._label = label;
        	
        	labelField.autoSize = TextFieldAutoSize.LEFT;
        	if(_label!=null)
        		labelField.text = _label;
        	this.addChild(labelField);
        	
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
			labelField.text = _label;
			draw();
		}
		
		public function set needArrow(value: Boolean): void{
			this._needArrow = value;
			draw();
		}
		
		public function get needArrow(): Boolean{
			return _needArrow;
		}
		
		
		
		
		public function get radius(): int{
			return _radius;
		}
		
		public function set radius(value: int): void{
			this._lineColor = value;
			draw();
		}
		
		public function get lineColor(): uint{
			return _lineColor;
		}
		
		public function set lineColor(value: uint): void{
			this._lineColor = value;
			draw();
		}
		
		public function get selectedLineColor(): uint{
			return _selectedLineColor;
		}
		
		public function set selectedLineColor(value: uint): void{
			this._selectedLineColor = value;
			draw();
		}
		
		
		public function set fromPoint(value: Point): void{
			this._fromPoint = value;
			draw();
		}
		
		public function get fromPoint(): Point{
			return _fromPoint;
		}
		
		public function set toPoint(value: Point): void{
			this._toPoint = value;
			draw();
		}
		
		public function get toPoint(): Point{
			return _toPoint;
		}
		
        
        private function getAngle():int
        {
            var  tmpx:int=_toPoint.x-_fromPoint.x ;
            var tmpy:int=_fromPoint.y -_toPoint.y ;
            var angle:int= Math.atan2(tmpy,tmpx)*(180/Math.PI);
            return angle;
        }
        
        //绘制线
        private function draw():void
        {
            this.graphics.clear();
            var tempLineColor:uint;
            if(selected)
            	tempLineColor = _selectedLineColor;
            else
            	tempLineColor = _lineColor;
            	
            this.graphics.lineStyle(1,tempLineColor,1);
            this.graphics.moveTo(_fromPoint.x,_fromPoint.y);
            this.graphics.lineTo(_toPoint.x,_toPoint.y);
            
			labelField.x = _fromPoint.x + (_toPoint.x - _fromPoint.x)/2;
			labelField.y = _fromPoint.y + (_toPoint.y - _fromPoint.y)/2;
            
            if(needArrow)
            {
                var angle:int =getAngle();
                var centerX:int=_toPoint.x-radius * Math.cos(angle *(Math.PI/180)) ;
                var centerY:int=_toPoint.y+radius * Math.sin(angle *(Math.PI/180)) ;
                var topX:int=_toPoint.x ;
                var topY:int=_toPoint.y  ;
                
                var leftX:int=centerX + _radius * Math.cos((angle +120) *(Math.PI/180))  ;
                var leftY:int=centerY - _radius * Math.sin((angle +120) *(Math.PI/180))  ;
                
                var rightX:int=centerX + _radius * Math.cos((angle +240) *(Math.PI/180))  ;
                var rightY:int=centerY - _radius * Math.sin((angle +240) *(Math.PI/180))  ;
                
                this.graphics.beginFill(tempLineColor,1);
                
                this.graphics.lineStyle(1,tempLineColor,1);
                
                this.graphics.moveTo(topX,topY);
                this.graphics.lineTo(leftX,leftY);
                
                this.graphics.lineTo(centerX,centerY);
                
                this.graphics.lineTo(rightX,rightY);
                this.graphics.lineTo(topX,topY);
                
                this.graphics.endFill();
            }
        }

	}
}

