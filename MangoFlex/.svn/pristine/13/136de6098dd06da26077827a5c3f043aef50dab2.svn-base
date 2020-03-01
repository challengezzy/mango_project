package smartx.flex.components.vo
{
	[RemoteClass(alias="smartx.publics.metadata.vo.MetadataTemplet")]
	public class MetadataTemplet
	{
		public var id:Number;
		public var name:String;
		public var code:String;
		public var owner:String;
		public var scope:String;
		public var content:String;
		public var type:int;
		public var typeCode:String;
		public var versionCode:String;
		
		public function MetadataTemplet(){
			
		}
		
		public function get contentXML():XML{
			return XML(content);
		}
		
		public function set contentXML(xml:XML):void{
			content = xml.toXMLString();
		}
	}
}