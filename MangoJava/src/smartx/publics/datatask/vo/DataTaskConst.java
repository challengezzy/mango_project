package smartx.publics.datatask.vo;

/**
 *@author zzy
 *@date Feb 24, 2012
 **/
public class DataTaskConst {
	
	/**
	 * 各参数键-值对区分字符串
	 */
	public static String PARAM_SPLIT = "<||>";//<||>  由于|是特殊字符，需要进行转义
	
	
	/**
	 * 数据任务键-值表示形式
	 */
	public static String PARAM_KEYVALUE = ":<=";

	public static String PARAM_SPLIT_S = "<\\|\\|>";//解析时使用
	public static String PARAM_KEYVALUE_S = ":<=";//解析时使用
	
	public static String PARAM_RUN_SUBSEQUENT = "PARAM_RUN_SUBSEQUENT";
	
	public static String TASKSTATUS_INIT = "0";//初始化
	public static String TASKSTATUS_RUNNING = "1";//正在执行
	public static String TASKSTATUS_COMPLETED = "9";//正常完成
	public static String TASKSTATUS_STOPED = "10";//非正常结束
	
}
