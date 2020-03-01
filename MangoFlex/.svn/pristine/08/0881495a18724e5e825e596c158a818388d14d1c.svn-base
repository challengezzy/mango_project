package smartx.flex.components.shortcut
{
	import mx.collections.ArrayCollection;
	
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.vo.MetadataTemplet;

	/**
	 * @author zzy
	 * @date Aug 23, 2011
	 */
	public class ShortcutKeyUtil
	{
		private var defaultKeyXml:XML = <root>
				<shortcutKey catalog="dashboardObject" command="edit" shift="true" ctrl="false" alt="false">e</shortcutKey>
				<shortcutKey catalog="dashboardObject" command="detail" shift="true" ctrl="false" alt="false">d</shortcutKey>
				<shortcutKey catalog="dashboardObject" command="refresh" shift="true" ctrl="false" alt="false">r</shortcutKey>
				<shortcutKey catalog="dashboardObject" command="setVar" shift="true" ctrl="false" alt="false">s</shortcutKey>
				<shortcutKey catalog="dashboardObject" command="start" shift="true" ctrl="false" alt="false">q</shortcutKey>
				<shortcutKey catalog="dashboardObject" command="stop" shift="true" ctrl="false" alt="false">t</shortcutKey>
				<shortcutKey catalog="dashboardObject" command="export" shift="true" ctrl="false" alt="false">x</shortcutKey>
				<shortcutKey catalog="dashboardObject" command="print" shift="true" ctrl="false" alt="false">p</shortcutKey>
			</root> ;
		
		private static var keyUtil:ShortcutKeyUtil = new ShortcutKeyUtil();
		
		private var metaTempletCode:String = "ShortCutKeyDesign";//快捷键元数据编码
		private var shortcutKeys:ArrayCollection = new ArrayCollection();
		
		public function ShortcutKeyUtil()
		{
			if(keyUtil)
				throw new Error("ShortcutKeyUtil不能被重复实例化！");			
			var metadataTemplet:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet(metaTempletCode);
			var keySetXml:XML;
			if(metadataTemplet != null)
				keySetXml = new XML(metadataTemplet.content);
			else
				keySetXml = defaultKeyXml;
			
			var temp:XML;
			for each(temp in keySetXml.shortcutKey){
				//<shortcutKey catalog="dashboardObject" command="edit" shift="false" ctrl="false" alt="true">e</shortcutKey>
				var cmd:ShortcutCommand = new ShortcutCommand();
				cmd.catalog = temp.@catalog;
				cmd.charCode = temp.toString().toUpperCase();
				cmd.command = temp.@command;
				cmd.ctrl = 	temp.@ctrl == "true"?true:false;
				cmd.shift = temp.@shift == "true"?true:false;
				cmd.alt = 	temp.@alt == "true"?true:false;
				
				shortcutKeys.addItem(cmd);
			}

		}
		
		public static function getInstance():ShortcutKeyUtil{
			if(keyUtil == null)
				keyUtil = new ShortcutKeyUtil();
			return keyUtil;
		}
		
		/**
		 * 根据按键对应的目录和键值查找对应的命令
		 * */
		public function getShortcutCmd(_catalog:String,_keyCode:int, _ctrl:Boolean=false,
									   _alt:Boolean=false,_shift:Boolean=false):String
		{
			var keyCmd:String = null;
			var charCode:String = numToChar(_keyCode).toUpperCase(); 
			
			for each(var temp:ShortcutCommand in shortcutKeys){
				if(temp.catalog == _catalog && temp.charCode == charCode 
					&& temp.ctrl == _ctrl && temp.shift == _shift && temp.alt == _alt){
					keyCmd = temp.command;
					return keyCmd;
				}
			}
			
			return keyCmd;
		}
		
		//键值转换成子母
		private function numToChar(num:int):String {
			if (num > 47 && num < 58) {
				var strNums:String = "0123456789";
				return strNums.charAt(num - 48);
			} else if (num > 64 && num < 91) {
				var strCaps:String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
				return strCaps.charAt(num - 65);
			} else if (num > 96 && num < 123) {
				var strLow:String = "abcdefghijklmnopqrstuvwxyz";
				return strLow.charAt(num - 97);
			} else {
				return num.toString();
			}
		}
	}
}