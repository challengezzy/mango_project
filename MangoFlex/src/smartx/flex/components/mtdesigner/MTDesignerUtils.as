package smartx.flex.components.mtdesigner
{
	import flash.utils.getDefinitionByName;
	
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.MetadataTemplet;

	public class MTDesignerUtils
	{
		public static function findMTDesigner(mttypeCode:String):MTDesigner{
			var systemConfig:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet(GlobalConst.MTCODE_SYSTEMCONFIG);
			var systemConfigXml:XML = systemConfig.contentXML;
			var uiclass:String = systemConfigXml.mtdesigners.designer.(@mttype == mttypeCode).@uiclass;
			var mtdesigner:MTDesigner;
			if(uiclass == null || uiclass == "")
				mtdesigner = new MTDesigner();
			else{
				var mtDesignerClass:Class = getDefinitionByName(uiclass) as Class;
				mtdesigner = new mtDesignerClass() as MTDesigner;
			}
			return mtdesigner;
		}
	}
}