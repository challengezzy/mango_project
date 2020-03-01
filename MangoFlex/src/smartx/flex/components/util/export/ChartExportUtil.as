package smartx.flex.components.util.export
{
	/**
	 * @author zzy
	 * @图片导出工具类
	 * @date Aug 5, 2011
	 */
	import flash.display.BitmapData;  
	import flash.display.DisplayObject;  
	import flash.display.DisplayObjectContainer; 
	import flash.geom.Matrix;  
	import flash.geom.Point;  
	import flash.net.FileReference; 
	import flash.utils.ByteArray;  
	import mx.controls.Alert;  
	import mx.graphics.codec.PNGEncoder;  
	
	public class ChartExportUtil{  
		
		//导出图片
		public static function exportChart(displayObj:DisplayObject,fileName:String):void{  
			var dt:DisplayObject = displayObj;  
			var bd:BitmapData = getBitMapDataSimple(dt);     
			var fr:Object = new FileReference();  
			
			if(fr.hasOwnProperty("save")){  
				var encoder:PNGEncoder = new PNGEncoder();  
				var data:ByteArray = encoder.encode(bd);  
				fr.save(data, fileName + '.png');  
			}else{  
				Alert.show("当前flash player版本不支持此功能,请安装10.0.0以上版本！","提示");  
			}  
		}   
		
		//获得对象的BitMapData		
		public static function getBitMapData(displayObj:DisplayObject,container:DisplayObjectContainer):BitmapData{  
			
			var bmpData:BitmapData = new BitmapData(displayObj.width,displayObj.height,true,0x00ffffff);  
			
			var bounds:Object = displayObj.getBounds(displayObj);			
			//var bounds2:Object = container.getBounds(container);  
			
			var matrix:Matrix = displayObj.transform.matrix.clone();			
			var point0:Point = container.globalToLocal(displayObj.localToGlobal(new Point(bounds.x,bounds.y)));  
			var point1:Point = container.globalToLocal(displayObj.localToGlobal(new Point(bounds.x,bounds.y+bounds.height)));  
			var point2:Point = container.globalToLocal(displayObj.localToGlobal(new Point(bounds.x+bounds.width,bounds.y)));  
			var point3:Point = container.globalToLocal(displayObj.localToGlobal(new Point(bounds.x+bounds.width,bounds.y+bounds.height)));  
			var point:Point = point0;  	
			
			if(point.x>point1.x)
				(point.x=point1.x);
			if(point.x>point2.x)
				(point.x=point2.x); 
			if(point.x>point3.x)
				(point.x=point3.x); 
			
			if(point.y>point1.y)
				(point.y=point1.y); 
			if(point.y>point2.y)
				(point.y=point2.y);			
			if(point.y>point3.y)
				(point.y=point3.y);  
			
			matrix.tx =  point.x - displayObj.x;
			matrix.ty =  point.y - displayObj.y;  
			bmpData.draw(displayObj,matrix);  
			
			return bmpData;
		}  
		
		//获得对象的BitMapData		
		public static function getBitMapDataSimple(displayObj:DisplayObject):BitmapData{  
			var bmpData:BitmapData = new BitmapData(displayObj.width,displayObj.height,true,0x00ffffff);  
			 
			bmpData.draw(displayObj);  
			
			return bmpData;
		}  
		
	}  

}