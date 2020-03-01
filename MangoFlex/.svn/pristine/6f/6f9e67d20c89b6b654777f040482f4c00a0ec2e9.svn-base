package smartx.flex.components.vo
{
	import smartx.flex.components.util.TempletDataUtil;

	[RemoteClass(alias="smartx.framework.metadata.vo.RefItemVO")]
	public class SimpleRefItemVO implements ItemVO
	{
		public var id:String; // 主键
    	public var code:String; // 编码
    	public var name:String; // 名称
		
		public var hashVO:Object = null;
    	
		public function SimpleRefItemVO()
		{
		}
		
				
		public function toString():String{
			return name;
		}
		
		public function toSqlString():String{
			return TempletDataUtil.convertSQLValue(id);
		}

	}
}