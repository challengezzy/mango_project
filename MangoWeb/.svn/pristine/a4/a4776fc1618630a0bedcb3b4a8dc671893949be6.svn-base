package smartx.bam.flex.modules.report.utils
{

	
	import flash.events.MouseEvent;
	
	import mx.controls.DateField;
	import mx.events.CalendarLayoutChangeEvent;
	import mx.formatters.DateFormatter;
	
	import smartx.bam.flex.utils.BAMUtil;
	
	public class ReportDateTimeField extends ReportComponent
	{
		public static const FIRST_DAY_OF_THIS_MONTH:String = "{fisrtdayofthismonth}";
		public static const YESTERDAY:String = "{yesterday}";
		public static const TODAY:String = "{today}";
		
//		private var dateTimeInput:DateTimeField = new DateTimeField();
		private var dateTimeInput:DateField = new DateField();
		private var dateFormatter:DateFormatter = new DateFormatter();
		private var formatString:String = "YYYY-MM-DD JJ:NN:SS";
		
		public function ReportDateTimeField(labelName:String,keyName:String,params:Object,isMandatory:Boolean,showLabel:Boolean=true)
		{
			super(labelName,keyName,dateTimeInput,params,isMandatory,"",showLabel);
			dateTimeInput.dayNames=["日","一","二","三","四","五","六"]; 
			dateTimeInput.monthNames=["一","二","三","四","五","六","七","八","九","十","十一","十二"]; 
			dateTimeInput.setStyle("borderStyle","solid");
			dateTimeInput.showToday = true;
			dateTimeInput.setStyle("todayColor","red");
//			dateTimeInput.showTime = false;
			dateTimeInput.formatString = "YYYY-MM-DD";
			dateFormatter.formatString = formatString;
//			dateTimeInput.selectedDateTime = null;
			dateTimeInput.selectedDate = null;
			dateTimeInput.width = 170;
			dateTimeInput.addEventListener(CalendarLayoutChangeEvent.CHANGE,dataChange);
		}
		
//		public override function get realValue():Object{
//			if(dateTimeInput.selectedDateTime!=null)
//				return dateFormatter.format(dateTimeInput.selectedDateTime);
//			return null;
//		}
		
		public override function get stringValue():String{
			if(dateTimeInput.selectedDate!=null)
				return dateFormatter.format(dateTimeInput.selectedDate);
			return null;
		}
		
		
		public override function clearContent(event:MouseEvent):void{
			dateTimeInput.selectedDate = null;
			dataChange(null);
		}
		
		private function dataChange(event:CalendarLayoutChangeEvent):void{
			dispatchEvent(new ReportEvent(ReportEvent.REAL_VALUE_CHANGE));
		}
		
		public override function setValue(value:Object,showValue:Object,isDefault:Boolean=false):void{
			var tempValue:String = String(value);
			if(!BAMUtil.isEmpty(tempValue)){
				var nowTime:Date = new Date();
				if(tempValue.toLowerCase() == ReportDateTimeField.TODAY){
					dateTimeInput.selectedDate = nowTime;
				}else if(tempValue.toLowerCase() == ReportDateTimeField.YESTERDAY){
					var now_millisecond:Number = nowTime.getTime();
					var twenty_four_hour:Number = 24*60*60*1000;
					var yesterday_millisecond:Number = now_millisecond-twenty_four_hour;
					var yesterdayTime:Date = new Date(yesterday_millisecond);
					dateTimeInput.selectedDate = yesterdayTime;
				}else if(tempValue.toLowerCase() == ReportDateTimeField.FIRST_DAY_OF_THIS_MONTH){
					var year:Number = nowTime.getFullYear();
					var month:Number = nowTime.getMonth();
					var firtDayTime:Date = new Date(year,month,1);
					dateTimeInput.selectedDate = firtDayTime;
				}else{
					var newDate:Date = new Date(Date.parse(tempValue));
					dateTimeInput.selectedDate = newDate;
				}
			}
			if(isDefault){
				this.params[this.keyName] = this.stringValue;
				dispatchEvent(new ReportEvent(ReportEvent.SET_DEFAULT_VALUE));
			}
			dataChange(null);
		}
	}
}