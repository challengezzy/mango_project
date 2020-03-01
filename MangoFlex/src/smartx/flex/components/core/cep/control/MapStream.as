package smartx.flex.components.core.cep.control
{
	import mx.collections.ArrayCollection;
	
	import smartx.flex.components.assets.AssetsFileLib;
	import smartx.flex.components.core.cep.AbstractStreamControlPropertyEditor;
	import smartx.flex.components.core.cep.CEPConst;
	import smartx.flex.components.core.cep.CEPUtils;
	import smartx.flex.components.core.cep.IStreamControl;
	import smartx.flex.components.core.cep.StreamEvent;
	import smartx.flex.components.core.cep.StreamEventField;
	import smartx.flex.components.core.cep.StreamInstance;
	import smartx.flex.components.core.cep.ValidateResult;

	public class MapStream implements IStreamControl
	{
		private var icon:Class = AssetsFileLib.palette_mapIcon;
		
		private var mapStreamPropertyEditor:MapStreamPropertyEditor;
		
		public function MapStream()
		{
		}
		
		public function getIcon():Class
		{
			return icon;
		}
		
		public function getName():String
		{
			return CEPConst.NAME_STREAMCONTROL_MAP;
		}
		
		public function getDisplayName():String
		{
			return "计算";
		}
		
		public function getType():String
		{
			return CEPConst.TYPE_STREAMCONTROL_OPERATOR;
		}
		
		public function getDescription():String
		{
			return "基于输入事件进行表达式计算以产生输出事件";
		}
		
		public function getPropertyEditorClassName():String
		{
			return "smartx.flex.components.core.cep.control.MapStreamPropertyEditor";
		}
		
		public function printOutputStreamEvent(instance:StreamInstance):StreamEvent
		{
			var outputEvent:StreamEvent = new StreamEvent();
			//复制input的事件
			if(instance.inputStreamList.length > 0){
				var inputStream:StreamInstance = StreamInstance(instance.inputStreamList.getItemAt(0));
				var inputEvent:StreamEvent = inputStream.getOutputStreamEvent();
				outputEvent.fieldList.addAll(inputEvent.fieldList);
			} 
			//处理本环节产生的field
			for each(var fieldXML:XML in instance.defineXML.settings.additionalExpression.field){
				var action:String = fieldXML.action;
				var name:String = fieldXML.name;
				var expression:String = fieldXML.expression;
				var field:StreamEventField = null;
				if(action == "Remove"){
					outputEvent.removeFieldByName(name);
				}
				else if(action == "Add"){
					field = new StreamEventField();
					field.name = name;
					field.type = "*";//todo无法读取表达式的返回类型
					outputEvent.fieldList.addItem(field);
				}
			}
			return outputEvent;
		}
		
		public function compile(instance:StreamInstance):String
		{
			var resultepl:String = CEPUtils.getModuleEPLs(instance);
			if(instance.inputStreamList.length > 0){
				var inputStream:StreamInstance = StreamInstance(instance.inputStreamList.getItemAt(0));
				var inputEvent:StreamEvent = inputStream.getOutputStreamEvent();
				var epl:String = "select ";
				var outputObjects:ArrayCollection = new ArrayCollection();
				
				for each(var inputEventField:StreamEventField in inputEvent.fieldList){
					var tempObject:Object = new Object();
					tempObject["expression"] = inputEventField.name;
					tempObject["name"] = inputEventField.name;
					outputObjects.addItem(tempObject);
				} 
				
				for each(var fieldXML:XML in instance.defineXML.settings.additionalExpression.field){
					var action:String = fieldXML.action;
					var name:String = fieldXML.name;
					var expression:String = fieldXML.expression;
					var field:StreamEventField = null;
					if(action == "Remove"){
						for(var i:int=0;i<outputObjects.length;i++){
							var removeObject:Object = outputObjects.getItemAt(i);
							if(removeObject["name"] == name){
								outputObjects.removeItemAt(i);
								break;
							}
						}
					}
					else if(action == "Add"){
						var addObject:Object = new Object();
						addObject["expression"] = expression;
						addObject["name"] = name;
						outputObjects.addItem(addObject);
					}
				}

				var isFirst:Boolean = true;
				for each(var object:Object in outputObjects){
					if(!isFirst)
						epl += " , ";
					else
						isFirst = false;
					epl += object["expression"]+" as "+object["name"];
				}
				epl += " from "+inputStream.name;
				resultepl += "insert into "+instance.name+" "+epl+";\n";
				if(instance.isNamedWindow)
					resultepl += CEPUtils.getEPLForCreateNamedWindow(instance);
			}
			return resultepl;
		}
		
		public function validate(instance:StreamInstance):ValidateResult{
			var result:ValidateResult = new ValidateResult();
			if(instance.inputStreamList.length != 1){
				result.isSuccessful = false;
				result.errorInfo = "\"计算\"控件的输入事件流有且仅有一个";
			}
			return result;
		}
	}
}