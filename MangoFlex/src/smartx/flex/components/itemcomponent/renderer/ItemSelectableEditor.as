package smartx.flex.components.itemcomponent.renderer
{
	
	import mx.controls.Text;
	
	/**
	 * 可选择的列表渲染
	 * */
	public class ItemSelectableEditor extends Text
	{
		
		public var itemKey:String;
		
		public function ItemSelectableEditor()
		{
			super();
			this.selectable = true;
		}
		
		override public function set data(value:Object):void
		{
			super.data = value;
			
			this.text = value[itemKey];
		}
		
		public function get realValue():Object{
			return this.text;
		}
		
		public function get stringValue():String{
			return this.text;
		}
		
		override public function get data():Object{
			return this.text;
		}
		
	}
}