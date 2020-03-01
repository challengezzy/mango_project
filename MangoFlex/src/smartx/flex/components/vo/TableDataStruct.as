package smartx.flex.components.vo
{
	[RemoteClass(alias="smartx.framework.metadata.vo.TableDataStruct")]
	public class TableDataStruct
	{
		public var fromtablename:String; //从哪一个取的数,只取第一列的,不支持从多个表取数,除非写视图!!

    	public var table_body_type:Array;

   		public var table_header:Array;

    	public var table_body:Array;//string[][]

		public function TableDataStruct()
		{
		}

	}
}