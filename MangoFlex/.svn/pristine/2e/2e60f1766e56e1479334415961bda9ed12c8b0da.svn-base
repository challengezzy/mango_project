package smartx.flex.components.core.mtchart
{
	
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	
	import smartx.flex.components.util.Hashtable;

	/**
	 * 流程定义数据转换处理，可以个性化定制流程图数据
	 * */
	public class ProcessXmlDataConvert implements IEventDispatcher
	{
		private var eventDispatcher:EventDispatcher;
		
		public function ProcessXmlDataConvert(){
			eventDispatcher = new EventDispatcher(this);
		}
		
		/**
		 * 数据转换
		 **/
		public function convertXmlData(paras:Hashtable, dataStr:String):void{
			
		}
		
		/**
		 * @private
		 */
		public function addEventListener(type:String, listener:Function,
										 useCapture:Boolean = false, priority:int = 0, useWeakReference:Boolean = false):void
		{
			eventDispatcher.addEventListener(type, listener, useCapture, priority, useWeakReference);
		}
		
		/**
		 * @private
		 */
		public function dispatchEvent(event:Event):Boolean
		{
			return eventDispatcher.dispatchEvent(event);
		}
		
		/**
		 * @private
		 */
		public function removeEventListener(type:String, listener:Function, useCapture:Boolean = false):void
		{
			eventDispatcher.removeEventListener(type, listener, useCapture);
		}
		
		/**
		 * @private
		 */
		public function hasEventListener(type:String):Boolean
		{
			return eventDispatcher.hasEventListener(type);
		}
		
		/**
		 * @private
		 */
		public function willTrigger(type:String):Boolean
		{
			return eventDispatcher.willTrigger(type);
		}
		
	}
}