package smartx.bam.flex.modules.gis
{
	import flash.events.Event;
	
	import mx.events.FlexEvent;
	
	import org.openscales.core.Map;
	import org.openscales.core.events.I18NEvent;
	import org.openscales.fx.FxMap;
	import org.openscales.geometry.basetypes.Pixel;
	
	import smartx.bam.flex.modules.gis.event.GisEvent;
	import smartx.bam.flex.modules.gis.ifc.IWidgetControl;
	import smartx.bam.flex.modules.gis.ifc.IWidgetTemplate;
	import smartx.bam.flex.modules.gis.vo.WidgetStates;
	
	import spark.components.Group;
	
	[Event(name = "widgetStateChanged", type = "smartx.bam.flex.modules.gis.event.GisEvent")]
	public class WidgetControl extends Group implements IWidgetControl
	{
		protected var _map:Map = null;
		protected var _fxMap:FxMap = null;
		protected var _active:Boolean = false;
		
		protected var _isInitialized:Boolean = false;
		
		private var _widgetState:String;
		
		private var _widgetTemplate:IWidgetTemplate;
		
		private var _widgetIcon:String = "smartx/flex/bam/assets/image/i_widget.png";
		
		private var _widgetTitle:String = "";
		
		[Bindable]
		private var _widgetId:Number;
		
		private var _contentXml:XML;
		
		private var _initialWidth:Number = 0;
		
		private var _initialHeight:Number = 0;
		
		private var _isDraggable:Boolean = true;
		
		private var _isResizeable:Boolean = true;
		
		private var _widgetPreload:String;
		
		public function WidgetControl()
		{
			super();
			this.addEventListener(FlexEvent.CREATION_COMPLETE, onCreationComplete); 
		}
		
		protected function onCreationComplete(event:Event):void {
			this._isInitialized = true;
			
			if((this.map) && (this.active == false)) {  
				this.active = true;
			}
			
			var childrenNum:int = this.numChildren;
			for (var i:int=0;i<childrenNum;i++)
			{
				var child:Object = this.getChildAt(i);
				if (child is IWidgetTemplate)
				{
					_widgetTemplate = child as IWidgetTemplate;
					
					_widgetTemplate.baseWidget = this;
					
					if (_widgetState)
					{
						_widgetTemplate.widgetState = _widgetState;
					}
					
					if (_widgetPreload == WidgetStates.WIDGET_MINIMIZED)
					{
						_widgetTemplate.widgetState = WidgetStates.WIDGET_MINIMIZED;
					}
				}
			}
			/*if (_waitForCreationComplete) // by default wait for creationComplete before loading the config
			{
				loadConfigXML();
			}*/
		}
		
		public function set preload(value:String):void
		{
			_widgetPreload = value;
		}
		
		
		private function setWidgetTemplateControl():void
		{
			var childrenNum:int = this.numChildren;
			for (var i:int=0;i<childrenNum;i++)
			{
				var child:Object = this.getChildAt(i);
				if (child is IWidgetTemplate)
				{
					_widgetTemplate = child as IWidgetTemplate;
					_widgetTemplate.resizable = isResizeable;
					_widgetTemplate.draggable = isDraggable;
				}
			}
		}
		
		public function set widgetId(value:Number):void
		{
			this._widgetId = value;
		}
		
		public function get widgetId():Number
		{
			return this._widgetId;
		}
		
		public function set widgetTitle(value:String):void
		{
			this._widgetTitle = value;
		}
		
		public function get widgetTitle():String
		{
			return this._widgetTitle;
		}
		
		public function set widgetIcon(value:String):void
		{
			this._widgetIcon = value;
		}
		
		public function get widgetIcon():String
		{
			return this._widgetIcon;
		}
		
		public function getState():String
		{
			return _widgetState;
		}
		
		public function setState(value:String):void
		{
			_widgetState = value;
			if (_widgetTemplate)
			{
				_widgetTemplate.widgetState = value;
			}
			notifyStateChanged(value);
		}
		
		private function notifyStateChanged(widgetState:String):void
		{
			var data:Object =
				{
					id: this._widgetId,
					state: widgetState
				};
			_fxMap.dispatchEvent(new GisEvent(GisEvent.WIDGET_STATE_CHANGED, data));
		}
		
		public function get initialWidth():Number
		{
			return _initialWidth;
		}
		
		public function set initialWidth(value:Number):void
		{
			_initialWidth = value;
		}
		
		public function get initialHeight():Number
		{
			return _initialHeight;
		}
		
		public function set initialHeight(value:Number):void
		{
			_initialHeight = value;
		}
		
		[Bindable]
		public function get contentXml():XML
		{
			return _contentXml;
		}
		
		public function set contentXml(value:XML):void
		{
			_contentXml = value;
		}
		
		[Bindable(event="propertyChange")]
		public function get map():Map
		{
			return this._map;
		}
		
		public function set map(value:Map):void
		{
			this._map = value;
			// Activate the control only if this control has already thrown an Event.COMPLETE
			if(this._isInitialized) {
				this.active = true;
			}
			// Dispatch an event to allow binding for the map of this Control
			dispatchEvent(new Event("propertyChange"));
		}
		
		public function get active():Boolean
		{
			return this._active;
		}
		
		public function set active(value:Boolean):void
		{
			this._active = value;
		}
		
		public function draw():void
		{
		}
		
		public function destroy():void
		{
			this._map = null;
			this._fxMap = null;
		}
		
		public function set position(px:Pixel):void
		{
			if (px != null) {
				this.x = px.x;
				this.y = px.y;
			}
		}
		
		public function get position():Pixel
		{
			return new Pixel(this.x, this.y);
		}
		
		
		public function get fxMap():FxMap
		{
			return this._fxMap;
		}
		
		public function set fxMap(value:FxMap):void
		{
			this._fxMap = value;
			this.fxMap.addEventListener(FlexEvent.CREATION_COMPLETE, onFxMapCreationComplete);
		}
		
		/**
		 * Flex Map wrapper initialization
		 */
		protected function onFxMapCreationComplete(event:Event):void {
			this.map = this._fxMap.map;
		}
		
		public function get isDraggable():Boolean{
			return _isDraggable;
		}
		
		public function set isDraggable(value:Boolean):void{
			_isDraggable = value;
			setWidgetTemplateControl();
		}
		
		public function get isResizeable():Boolean{
			return _isResizeable;
		}
		
		public function set isResizeable(value:Boolean):void{
			_isResizeable = value;
			setWidgetTemplateControl();
		}
		
		public function onMapLanguageChange(event:I18NEvent):void{
			
		}
		
	}
}