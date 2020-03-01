package smartx.flex.components.vo
{
	import mx.collections.ArrayCollection;
	
	[RemoteClass(alias="smartx.framework.metadata.vo.Pub_Templet_1VO")]
	public class TempletVO{
		public var id:String;
		public var templetcode:String;                            //模板编码
	    public var templetname:String;                            //模板名称
	    public var tablename:String;                              //取数的表名
	    public var datasourcename:String;                         //数据源名称,如果为空,则从默认数据源取数!!
	    public var dataconstraint:String;                         //数据过滤条件
	    public var pkname:String;                                 //主键名称
	    public var pksequencename:String;                         //序列对应列名
	    public var ordersetting:String;                           //默认检索排序设置
	    public var savedtablename:String;                         //保存数据的表名
	    public var cardcustpanel:String;                          //卡片的自定义面板
	    public var listcustpanel:String;                          //列表的自定义面板
	    public var itemKeys:Array;                             //
	    public var realViewColumns:Array;                      //真正的视图的列名,如果它匹配上itemKeys中的一项,就给itemKeys这项赋值
	    public var realSavedTableColumns:Array;                //真正的保存数据库的列名,它肯定是realViewColumns的子集
	    public var realSavedTableHaveColumns:Array;            //
	    public var itemVos:Array;
	    
	    public var itemGroups:ArrayCollection;
	    public var directItemVOs:ArrayCollection;
	}
	
}
