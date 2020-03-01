package smartx.flex.components.core.mtchart.event
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	
	/**
	 * sky zhangzz
	 **/
	public class MTChartEvent extends Event
	{
		public static const REFRESH_DATA:String = "refreshData";
		
		public static const STOP_SUCCESSFUL:String = "stopSuccessful";
		
		public static const START_SUCCESSFUL:String = "startSuccessful";
		
		public static const RELATION_SELECTED:String = "relationSelected"; 
		
		public static const RELATION_REFRESH:String = "relationRefresh";
		
		public static const EDIT:String = "edit";
		
		public static const INIT_COMPLETE:String = "initComplete";
		
		public static const DETAIL:String = "detail";
		
		public static const SET_VARIABLE:String = "setVariable";
		
		public var id:String;
		
		public var chartType:String;
		
		public var mtcode:String;
		
		public var dataValues:ArrayCollection;
		
		public function MTChartEvent(type:String,id:String=null,mtcode:String=null,chartType:String=null,dataValues:ArrayCollection=null,bubbles:Boolean=false,cancelable:Boolean=false)
		{
			//TODO: implement function
			super(type, bubbles, cancelable);
			this.id = id;
			this.chartType = chartType;
			this.mtcode = mtcode;
			this.dataValues = dataValues;
		}
	}
}