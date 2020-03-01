package
{
	/**
	 * @author zzy
	 * @date Jun 29, 2012
	 */
	import flash.events.MouseEvent;
	
	import mx.controls.Alert;
	import mx.controls.Label;
	import mx.controls.LinkButton;
	import mx.events.DynamicEvent;
	
	public class UrlLinkRenderer extends LinkButton
	{
		private var newUrlLink:Label;
		private var orderByFilter:String;
		[Bindable]
		private var _linkButtonLabel:String = "" ;
		[Bindable]
		private var _rowObject:Object = new Object();
		public function UrlLinkRenderer()
		{
			super();
			this.setStyle("textDecoration","underline");
			this.setStyle("textAlign","left");
			this.addEventListener(MouseEvent.CLICK,linkButtonClickHandler);
		}
		
		/**
		 * Override the set method for the data property. function also sets the data for the
		 * container instance and sets member variables newId, newLabel, and newCheckBox.selected value
		 * 
		 */
		override public function set data(value:Object):void
		{
			super.data = value;
			
			// Make sure there is data
			if (value != null) {
				
				//Alert.show("test");
				var str:String = "";
				for (var i:Object in value) {
					str += i + " || " + value[i] + "\n";
				}
				
				this._rowObject = value ;
				this.label = value[_linkButtonLabel];
				Alert.show("setData  " + str);
			}
		}
		
		public function set linkButtonLabel(value:String):void {
			_linkButtonLabel = value ;
		}
		public function get linkButtonLabel():String {
			return _linkButtonLabel ;
		}
		
		private function linkButtonClickHandler(e:MouseEvent):void {
			var event:LinkButtonDynamicEvent = new LinkButtonDynamicEvent("DataGridLinkButtonClickEvent",_rowObject);
			dispatchEvent(event);
		}
	}
}