package smartx.flex.components.itemcomponent.renderer
{
	import spark.components.CheckBox;
	
	public class TriStateCheckBox extends CheckBox
	{
		public function TriStateCheckBox()
		{
			super();
		}
		
		private var _partial:Boolean;
		
		public function get partial():Boolean
		{
			return _partial;
		}
		
		public function set partial(value:Boolean):void
		{
			_partial = value;
			invalidateSkinState();
		}
		
//		override protected function getCurrentSkinState():String
//		{
//			var state:String = super.getCurrentSkinState();
//			if (partial)
//				return "partial_" + state;
//			return state;
//		}
	}
}