package smartx.flex.components.core.mtchart.listener
{
	import com.events.FCEvent;
	
	import flash.events.MouseEvent;
	
	import mx.charts.events.ChartEvent;
	import mx.charts.events.ChartItemEvent;
	import mx.events.ListEvent;
	
	import smartx.flex.components.core.mtchart.MTChartPanel;

	/**
	 * author sky
	 * MTChart控件点击事件监听器
	 **/ 
	public interface MTChartClickListener
	{
		/**
		 * 普通图表ITEM点击事件
		 **/ 
		function itemClick(event:ChartItemEvent,chart:MTChartPanel):void;
		
		/**
		 * 普通图表点击事件
		 **/ 
		function click(event:ChartEvent,chart:MTChartPanel):void;
		
		/**
		 * F系图表ITEM点击事件
		 * 注：使用F系图表ITEM点击事件，数据结构中必须加入"link:'S-'" 列如：{name:'name',value:'sky',link:'S-sky'}
		 **/  
		function fcItemClick(event:FCEvent,chart:MTChartPanel):void;
		
		/**
		 * F系图表点击事件
		 **/ 
		function fcClick(event:MouseEvent,chart:MTChartPanel):void;
		
		/**
		 * 普通列表和高级列表的单击响应事件，处理方式的不同由实现类自己区别实体
		 * 变通列表只能定位到行
		 * 高级数据列表可以定位到单元格
		 **/
		function listItemClick(event:ListEvent,chart:MTChartPanel):void;
		
	}
}