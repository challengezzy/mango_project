package smartx.flex.components.core.cep
{
	import mx.collections.ArrayCollection;
	
	import smartx.flex.components.core.cep.control.InputStream;
	import smartx.flex.components.core.cep.control.MapStream;

	public class StreamControlFactory
	{
		private static var streamControlPool:Object = new Object();
		
		private static var temp:StreamControlFactory = new StreamControlFactory();//保证静态初始化时构造函数被调用
		
		public function StreamControlFactory(){
			registorStreamControl(new MapStream());
			registorStreamControl(new InputStream()); 
		}
		
		public static function registorStreamControl(streamControl:IStreamControl):void{
			streamControlPool[streamControl.getName()] = streamControl;
		}
		
		public static function getStreamControl(name:String):IStreamControl{
			return streamControlPool[name];
		}
		
		public static function getStreamControlByType(type:String):ArrayCollection{
			var result:ArrayCollection = new ArrayCollection();
			for(var name:String in streamControlPool){
				var control:IStreamControl = getStreamControl(name);
				if(control.getType() == type)
					result.addItem(control);
			}
			return result;
		}
	}
}