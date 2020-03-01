package smartx.flex.components.core.mtchart
{
	import com.ammap.AmMap;
	
	import mx.events.FlexEvent;
	
	public class AmMapWrapper extends AmMap
	{
		public function AmMapWrapper()
		{
			super();
			this.addEventListener(FlexEvent.CREATION_COMPLETE,removeAmLink);
		}
		
		private function removeAmLink(event:FlexEvent):void{
			this.removeChild(_amchartsLink);
			removeEventListener(FlexEvent.CREATION_COMPLETE,removeAmLink);
		}
	}
}