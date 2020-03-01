package smartx.flex.components.util.script
{
	import flash.events.Event;
	
	public class ScriptEvent extends Event
	{
		public static const OUTPUT_VALUE:String = "outputValue";
		
		public var outputValue:Object = null;
		
		public function ScriptEvent(type:String, outputValue:Object=null, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type,bubbles,cancelable);
			this.outputValue = outputValue;
		}

	}
}