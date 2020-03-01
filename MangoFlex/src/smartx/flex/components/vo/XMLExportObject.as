package smartx.flex.components.vo
{
	[RemoteClass(alias="smartx.publics.form.vo.XMLExportObject")]
	public class XMLExportObject
	{
		public var tableName:String;
		public var pkName:String;
		public var fkName:String;
		public var visiblePkName:String;
		public var datasource:String;
		public var fetchSql:String;
		public var childObject:XMLExportObject;
		public var childObjects:Array;
		public var fkType:String;
		public var fkTable:String;
		public var idToParent:String;
		public var idToChild:String;
	}
}