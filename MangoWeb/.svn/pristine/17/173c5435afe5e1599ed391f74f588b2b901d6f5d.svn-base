package smartx.bam.flex.modules.gis.utils
{
	import mx.core.FlexGlobals;
	import mx.styles.CSSStyleDeclaration;
	import mx.styles.IStyleManager2;

	public class UIManager
	{
		public function UIManager(){}
		
		public static function setViewerStyle():void{
			var topLevelStyleManager:IStyleManager2 = FlexGlobals.topLevelApplication.styleManager;
			
			var textColor:uint = 0x000000;
			var styleAlpha:Number = 0.8;
			var backgroundColor:uint = 0x333333;
			var rolloverColor:uint = 0x101010;
			var selectionColor:uint = 0x000000;
			var titleColor:uint = 0xFFD700;
			
			
			//设置WIDGETTEMPLET样式
			var cssStyleDeclarationWT:CSSStyleDeclaration = new CSSStyleDeclaration("smartx.bam.flex.modules.gis.WidgetTemplate");
			
			cssStyleDeclarationWT.setStyle("backgroundColor", backgroundColor);
			//cssStyleDeclarationWT.setStyle("chromeColor", backgroundColor);
			cssStyleDeclarationWT.setStyle("color", textColor);
			//cssStyleDeclarationWT.setStyle("contentBackgroundColor", backgroundColor);
			//cssStyleDeclarationWT.setStyle("symbolColor", textColor);
			cssStyleDeclarationWT.setStyle("rollOverColor", rolloverColor);
			cssStyleDeclarationWT.setStyle("selectionColor", selectionColor);
			cssStyleDeclarationWT.setStyle("focusColor", titleColor);
			//cssStyleDeclarationWT.setStyle("accentColor", textColor);
			cssStyleDeclarationWT.setStyle("textSelectedColor", textColor);
			cssStyleDeclarationWT.setStyle("textRollOverColor", textColor);
			//cssStyleDeclarationWT.setStyle("contentBackgroundAlpha", styleAlpha);
			cssStyleDeclarationWT.setStyle("backgroundAlpha", styleAlpha);
			cssStyleDeclarationWT.setStyle("borderColor", selectionColor);
			cssStyleDeclarationWT.setStyle("borderThickness",1);
			
			topLevelStyleManager.setStyleDeclaration("smartx.bam.flex.modules.gis.WidgetTemplate", cssStyleDeclarationWT, false);
		}
	}
}