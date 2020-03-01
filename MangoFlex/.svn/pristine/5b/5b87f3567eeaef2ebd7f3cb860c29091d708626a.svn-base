package smartx.flex.components.util
{
	import flash.events.Event;
	import flash.events.EventDispatcher;

	public class EventBus extends EventDispatcher
	{
		private static var _eventBus:EventBus;
		
		private static var lock:Boolean = false;
		
		public function EventBus() 
		{
			if(!lock)
				throw new Error( "eventBus只能被定义一个！");
			initialize();
		}
		
		private function initialize():void{
			//做一些初始化的事情
		}
		
		public static function getInstance():EventBus{
			if(_eventBus == null){
				lock = true;
				_eventBus = new EventBus();
				lock = false;
			}
			return _eventBus
		}
		
		public function dispatch(type:String):Boolean{
			return dispatchEvent(new Event(type));
		}
	}
}