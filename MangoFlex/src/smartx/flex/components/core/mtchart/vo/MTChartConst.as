package smartx.flex.components.core.mtchart.vo
{
	/**
	 * @author zzy
	 * @date Jun 13, 2011
	 */
	public class MTChartConst
	{
		
		//OLAP 图类型
		public static const OLAP_VIEWTYPE_GRID_NAME:String = "表格";//ViewType.GRID
		public static const OLAP_VIEWTYPE_CHARTS_NAME:String = "图形"; //ViewType.CHARTS
		public static const OLAP_VIEWTYPE_GRIDANDCHART_NAME:String = "表格和图形";//ViewType.GRIDANDCHART

		public static const OLAP_CHARTTYPE_BAR_NAME:String = "柱状图"; //ChartType.BAR
		public static const OLAP_CHARTTYPE_LINE_NAME:String = "折线图"; //ChartType.LINE
		public static const OLAP_CHARTTYPE_PIE_NAME:String = "饼图"; //ChartType.PIE
		public static const OLAP_CHARTTYPE_SCATTER_NAME:String = "分布图";//ChartType.SCATTER
		
		//OLAP图表 聚合函数类型
		public static const OLAP_MEASURE_SUM:String = "Sum";
		public static const OLAP_MEASURE_COUNT:String = "Count";
		public static const OLAP_MEASURE_AVERAGE:String = "Average";
		public static const OLAP_MEASURE_MAX:String = "Max";
		public static const OLAP_MEASURE_MIN:String = "Min";
		public static const OLAP_MEASURE_PRODUCT:String = "Product";
		public static const OLAP_MEASURE_PERCENT:String = "Percent";
		
		
		
		public static const OLAP_MEASURE_SUM_NAME:String = "求和";
		public static const OLAP_MEASURE_COUNT_NAME:String = "计数";
		public static const OLAP_MEASURE_AVERAGE_NAME:String = "求平均";
		public static const OLAP_MEASURE_MAX_NAME:String = "最大值";
		public static const OLAP_MEASURE_MIN_NAME:String = "最小值";
		public static const OLAP_MEASURE_PRODUCT_NAME:String = "乘积";
		public static const OLAP_MEASURE_PERCENT_NAME:String = "百分比";
		
		public static const OLAP_MEASURE_FLAG_COLUMN:String = "测量值";
		
		//仪表盘对象变量SHAREOBJ
		public static const SHAREOBJ_DBO_VARIABLE:String = "shareObjDboVariabel";
		
		//生成SQL变量
		public static const GROUPBY_PREFIX:String = " GROUP BY ";
		public static const ORDERBY_PREFIX:String = " ORDER BY ";
		public static const STARTWITH_PREFIX:String = " START WITH ";
		public static const WHERE_PREFIX:String = " WHERE 1=1 AND ";
		public static const ROWNUM_LIMIT:String = " AND ROWNUM <=";
		
		public static const EXPOPTION_PNG:String = "图片(PNG格式)";
		public static const EXPOPTION_CURDATA_CSV:String = "当前页数据(CSV)";
		public static const EXPOPTION_ALLDATA_CSV:String = "全部数据(CSV)";
		
		//MTCHARTPANEL菜单关键字
		public static const MTCHART_KEY_REFRESH:String = "refresh";
		public static const MTCHART_KEY_EDIT:String = "edit";
		public static const MTCHART_KEY_DETAIL:String = "detail";
		public static const MTCHART_KEY_SETVAR:String = "setVar";
		public static const MTCHART_KEY_START:String = "start";
		public static const MTCHART_KEY_STOP:String = "stop";
		public static const MTCHART_KEY_EXPORT:String = "export";
		public static const MTCHART_KEY_PRINT:String = "print";
		
	}
}