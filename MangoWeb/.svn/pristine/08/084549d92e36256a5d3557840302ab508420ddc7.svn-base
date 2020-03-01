package smartx.flex.modules
{
	import mx.controls.Label;
	import mx.controls.Text;
	
	public class DataGridLabel extends Label
	{
		public var itemKey:String;
		
		public function DataGridLabel()
		{
			super();
			this.selectable = true;
		}
		
		/**
		 * Override the set method for the data property. function also sets the data for the
		 * container instance and sets member variables newId, newLabel, and newCheckBox.selected value
		 * 
		 */
		override public function set data(value:Object):void
		{
			super.data = value;
			
			this.text = value[itemKey];
		}
		
		override public function get data():Object{
			return this.text;
		}
	}
}