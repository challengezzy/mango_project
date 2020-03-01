package smartx.bam.flex.modules.entitymodel.entitytab
{
	import mx.collections.ArrayCollection;
	import mx.containers.VBox;
	import mx.controls.dataGridClasses.DataGridColumn;
	
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;

	public class AbstractTab extends VBox
	{
		[Bindable]
		public var contentXml:XML;
		[Bindable]
		public var selectedItem:Object;
		[Bindable]
		public var endpoint:String;
		[Bindable]
		public var debugMode:Boolean;
		
		public var isCreationComplete:Boolean = false;
		
		protected var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		public var insertMode:Boolean;
		[Bindable]
		protected var attributeTypeCboDp:ArrayCollection = new ArrayCollection([
			{name:"字符串",data:"VARCHAR2"},
			{name:"数字",data:"NUMBER"},
			{name:"日期",data:"DATE"},
			{name:"布尔",data:"BOOLEAN"},
			{name:"CLOB",data:"CLOB"}
		]);
		[Bindable]
		protected var categoryArr:ArrayCollection = new ArrayCollection([
			{name:"普通",data:BAMConst.ENTITY_ATT_CATEGORY_NORMAL},
			{name:"字典引用",data:BAMConst.ENTITY_ATT_CATEGORY_DICT},
			{name:"实体引用",data:BAMConst.ENTITY_ATT_CATEGORY_ENTITY}
		]);
		
		[Bindable]
		protected var sridDp:ArrayCollection = new ArrayCollection([
			{name:"4326",data:"EPSG:4326"}
		]);
		
		public function AbstractTab(){
			super();
		}
		
		protected function attTypeLabelFun(item:Object,column:DataGridColumn):String{
			for each(var attTypeData:Object in attributeTypeCboDp){
				if(attTypeData.data == item.@type)
					return attTypeData.name;
			}
			return "";
		}
		
		protected function categoryFun(item:Object,column:DataGridColumn):String{
			for each(var categoryData:Object in categoryArr){
				if(categoryData.data == item.@category)
					return categoryData.name;
			}
			return "";
		}
		
		protected function isNullableLabelFun(item:Object,column:DataGridColumn):String{
			if(item.@isNullable == "true")
				return "是";
			else
				return "否";
		}
	}
}