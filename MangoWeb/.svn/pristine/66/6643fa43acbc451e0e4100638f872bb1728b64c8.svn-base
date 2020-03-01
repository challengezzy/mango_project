package smartx.bam.flex.modules.dashboardobject.designer
{
	import mx.collections.ArrayCollection;
	import mx.containers.Canvas;
	
	import smartx.flex.components.util.Hashtable;
	import smartx.flex.components.vo.TableDataStruct;
	
	public class BaseDesigner extends Canvas
	{
		[Bindable]
		public var contentXml:XML;
		//判断关联表是否发生了变化
		public var isTableChanged:Boolean = false;
		
		public var sql:String;
		
		public var tableDataStruct:TableDataStruct;
		
		//字段集合
		[Bindable]
		public var fieldList:ArrayCollection = new ArrayCollection();
		
		[Bindable]
		public var fieldTypeMap:Hashtable = new Hashtable();
		
		public function BaseDesigner(){
			super();
		}
		
		public function generalFormComplete():void{}
		
		public function showHandler():void{}
		
//		public function xmlConvertProperty():void{}
		
		public function propertyConvertXml():void{}
		
		public function initTableStructComplete():void{}
		
		public function confirmChecked():Boolean{return true;}
	}
}