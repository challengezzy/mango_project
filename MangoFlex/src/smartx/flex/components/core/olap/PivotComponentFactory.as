package smartx.flex.components.core.olap
{
	import com.flexmonster.pivot.FlexPivotComponent;
	import com.flexmonster.pivot.model.report.vo.ReportValueObject;
	
	import mx.collections.ArrayCollection;
	
	import smartx.flex.components.util.Hashtable;

	/**
	 * 由于FlexPivotComponent控件的创建内存代价较大，采用少数实例的方式
	 * @author zzy
	 * @date Aug 11, 2011
	 */
	public class PivotComponentFactory
	{
		private static var pivotFactory:PivotComponentFactory = new PivotComponentFactory();
		
		private static var swfFilePath:String = "olap/";
		private static var localSettingsURL:String = "local.chinese.xml";
		
		//存储FlexPivotComponent的对象池
		private static var pivotArray:ArrayCollection = new ArrayCollection(); 
		
		public function PivotComponentFactory()
		{
			if(pivotFactory)
				throw new Error("PivotComponentFactory不能被实例化！");
		}
		
		public static function getInstance():PivotComponentFactory{
			if(pivotFactory == null)
				pivotFactory = new PivotComponentFactory();
			
			return pivotFactory;
		}
		
		/**
		 *获取一个 FlexPivotComponent对象
		 **/
		public static function getPivotComponent():FlexPivotComponent{
			var pivot:FlexPivotComponent;
			if(pivotArray.length < 1 ){
				//对象池中的对象已使用完毕,重新创建pivot对象，默认创建3个
				pivot = new FlexPivotComponent();
				pivot.percentHeight = 100;
				pivot.percentWidth = 100;
				pivot.componentFolder = swfFilePath;
				
//				var reportObj:ReportValueObject;
//				if(reportObj == null)
//					reportObj = new ReportValueObject();
//				
//				reportObj.localSettingsUrl = localSettingsURL;
//				pivot.setReport(reportObj);
				//pivot.swfFilePath = swfFilePath;
				//pivot.localSettingsURL = localSettingsURL;
					
				pivotArray.addItem(pivot);
			}
			
			pivot = pivotArray.getItemAt(0) as FlexPivotComponent;
			pivotArray.removeItemAt(pivotArray.getItemIndex(pivot));//删除已使用的对象
			
			return pivot;
		}
		
		/**
		 * 是否FlexPivotComponent对象，返还给内存
		 **/
		public static function releasePivot(pivot:FlexPivotComponent):void{
			//使用后放回
			pivot.clear();
			pivotArray.addItemAt(pivot,0);
		}
	}
}