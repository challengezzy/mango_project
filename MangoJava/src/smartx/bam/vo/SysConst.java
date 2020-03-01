/**
 * 
 */
package smartx.bam.vo;

/**
 * @author caohenghui
 * 
 */
public class SysConst {

	// 告警类型状态
	public static final int BAM_ALERT_LOWERED = 0; // 普通状态
	public static final int BAM_ALERT_RAISED = 1; // 激活状态
	public static final int BAM_ALERT_ACKNOWLEDGED = 2; // 确认状态

	// 规则类型
	public static final int BAM_RULE_SENDEVERYTIME = 0; // 每次发送
	public static final int BAM_RULE_SENDONCE = 1; // 只发送一次
	public static final int BAM_RULE_RESET = 2; // 重置

	// module 状态
	public static final int PUB_CEP_STREAMMODULE_STATUS_STOP = 0;// 停止
	public static final int PUB_CEP_STREAMMODULE_STATUS_START = 1;// 启动
	public static final int PUB_CEP_STREAMMODULE_STATUS_EXCEPTION = 2;// 异常

	// ESPER数据源方式
	public static final int BAM_RELATIONALDATASOURCE_TYPE_DATASOURCE = 0;// 数据源
	public static final int BAM_RELATIONALDATASOURCE_TYPE_DATASOURCEFACTORY = 1;// DBCP
	public static final int BAM_RELATIONALDATASOURCE_TYPE_DRIVERMANAGER = 2; // JDBC

	// 告警传送方式
	public static final int BAM_SUBSCIBER_DELIVERYTYPE_NORMAL = 0;// 普通
	public static final int BAM_SUBSCIBER_DELIVERYTYPE_EMAIL = 1; // 邮件
	public static final int BAM_SUBSCIBER_DELIVERYTYPE_ALL = 2; // 全部

	/** 仪表盘对象类型 */
	public static final int PIECHART = 0;// 饼图
	public static final int COMBINATIONCHART = 1;// 混合图
	public static final int DISTRIBUTIONCHART = 2;// 分布图
	public static final int PIVOTCOMBINATIONCHART = 3;// 透视混合图
	public static final int GEOGRAPHYCHART = 4;// 地理图
	public static final int INDICATOR = 5;// 指示器
	public static final int TABLE = 6;// 列表
	public static final int TREE_LIST = 7;// 树形列表

	public static final String RELATION_DS_PREFIX = "datasource_relation_";

	public static final String MAPKEY_ALERT = "Alert";
	public static final String MAPKEY_TASK = "Task";

	public static final String AVDATASOURCETYPE_QUERYVIEW = "QueryView";
	public static final String AVDATASOURCETYPE_BUSINESSVIEW = "BusinessView";

	public static final String AVANALYZERTYPE_STRINGANALYZER = "StringAnalyzer";
	public static final String AVANALYZERTYPE_NUMBERANALYZER = "NumberAnalyzer";
	public static final String AVANALYZERTYPE_BOOLEANANALYZER = "BooleanAnalyzer";
	public static final String AVANALYZERTYPE_DATEANALYZER = "DateAnalyzer";
	public static final String AVANALYZERTYPE_DATEGAPANALYZER = "DateGapAnalyzer";
	public static final String AVANALYZERTYPE_VALUEANALYZER = "ValueAnalyzer";
	public static final String AVANALYZERTYPE_MATCHANALYZER = "MatchAnalyzer";
	public static final String AVANALYZERTYPE_WEEKDAYANALYZER = "WeekDayAnalyzer";

	public static final String ANALYZER_CODE = "AnalyzerCode";
	public static final String ANALYZER_GROUP = "ANALYZER";

	public static final String ENTITY_GROUP = "ENTITY";
	
	public static int STARTED = 1;//启动
	public static int STOPED = 0;//停止
	
	//元数据前缀
	public static final String DB_METADATA_PREFIX = "MT_LAYOUT_";
	public static final String DO_METADATA_PREFIX = "MT_DO_";
	public static final String EM_METADATA_PREFIX = "MT_EM_";//领域实体元数据前缀
	public static final String EN_METADATA_PREFIX = "MT_EN_";//实体元数据前缀
	public static final String EN_RULE_METADATA_PREFIX = "MT_EM_RULE_";//实体规则元数据前缀
	
	//关系型数据源状态
	public static final int RELATION_DATASOURCE_STATUS_VALID = 1;//启用
	public static final int RELATION_DATASOURCE_STATUS_UNVALID = 0;//停用
	
	//客户端环境变量
	public static final String PARA_CEP_SERVICE = "CEP_SERVICE";//是否启动CEP服务
}
