package smartx.flex.components.core.mtchart
{
	import com.flx.external.FlashInterface;
	import com.fusioncharts.components.FusionCharts;
	
	import flash.display.Loader;
	import flash.net.LocalConnection;
	
	import mx.controls.Image;
	import mx.core.FlexGlobals;
	
	public class FusionChartsWrapper extends FusionCharts
	{
		public function FusionChartsWrapper()
		{
			super();
		}
		
		public function destroy():void{
			var loader:Loader;
			var loaderid:String;
			for(var i:int=0;i<this.numChildren;i++){
				var child:* = this.getChildAt(i);
				if(child is Loader){
					loader = child as Loader;
					loaderid = loader.name.toString();
					FlashInterface.call(loaderid + ".chart.destroy");
					//loader.unloadAndStop(true);
					
				}
				else if(child is Image){
					var image:Image = child as Image;
					image.source = null;
				}
			}
			if(loader)
				this.removeChild(loader);
			//FlashInterface.publish(FlexGlobals.topLevelApplication.parent, false);

			try {
				
				trace("force a GC");
				
				new LocalConnection().connect("foo");
				
				new LocalConnection().connect("foo");
				
			} catch (e:Error) {

			}
		}
	}
}