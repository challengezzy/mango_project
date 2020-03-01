package smartx.bam.flex.modules.entitymodel.utils
{
	import com.adobe.utils.StringUtil;
	
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	
	import smartx.bam.flex.vo.BAMConst;

	/**
	 * GIS应用的工具类 
	 */
	public class GisUtil
	{
		public function GisUtil(){}
		
		/**
		 * 图层预览
		 * @param geoserverUrl
		 * @param workspace
		 */ 
		public static function layerPreview(geoserverUrl:String,workspace:String,layerName:String
											,minx:String,maxx:String,miny:String,maxy:String,srsid:String):void{
			var previewUrl:String = geoserverUrl+"/"+workspace+"/wms?service=WMS&version=1.1.0&request=GetMap&layers="
									+ workspace+":"+layerName+"&styles=&bbox="+minx+","+miny+","+maxx+","+maxy+"&width=704&height=530&srs="
									+ srsid+"&format=application/openlayers";
			trace("图层预览Url..."+previewUrl);
			navigateToURL(new URLRequest(previewUrl),"_blank");
		}
		
		/**
		 * 图层样式编辑
		 * @param geoserverUrl
		 * @param workspace
		 * @param layerName
		 */ 
		public static function editStyler(geoserverUrl:String,workspace:String,layerName:String):void{
			var stylerUrl:String = geoserverUrl+"/www/styler/index.html?layer="+workspace+"%3A"+layerName;
			trace("图层样式编辑Url..."+stylerUrl);
			navigateToURL(new URLRequest(stylerUrl),"_blank");
		}
		
		/**
		 * 判断该图层是否属于当前领域实体模型或基本图层
		 * @param layerName
		 * @param entityModelCode
		 */ 
		public static function layerIsBelongEntityModel(layerName:String,entityModelCode:String):Boolean{
			var isOk:Boolean = false;
			if(StringUtil.beginsWith(layerName,BAMConst.ENTITYGIS_TABLENAME_PREFIX.toLowerCase())){
				var name:String = String(layerName).substring(BAMConst.ENTITYGIS_TABLENAME_PREFIX.length,String(layerName).length);
				if(StringUtil.beginsWith(name,entityModelCode.toLowerCase()))
					isOk = true;
			}else//基础图层
				isOk = true;
			return isOk;
		}
		
	}
}