package smartx.flex.components.util
{
	import flash.net.LocalConnection;

	public class MemoryUtil
	{
		public static function forceGC():void{
			try {
				
				trace("force a GC");
				
				new LocalConnection().connect("foo");
				
				new LocalConnection().connect("foo");
				
			} catch (e:Error) {
				
			}
		}
	}
}