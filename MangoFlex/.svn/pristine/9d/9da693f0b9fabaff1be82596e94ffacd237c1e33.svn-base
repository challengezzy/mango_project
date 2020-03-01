package smartx.flex.components.core.cep.control
{
	import smartx.flex.components.assets.AssetsFileLib;
	import smartx.flex.components.core.cep.CEPConst;
	import smartx.flex.components.core.cep.CEPUtils;
	import smartx.flex.components.core.cep.IStreamControl;
	import smartx.flex.components.core.cep.StreamEvent;
	import smartx.flex.components.core.cep.StreamEventField;
	import smartx.flex.components.core.cep.StreamInstance;
	import smartx.flex.components.core.cep.ValidateResult;
	
	public class InputStream implements IStreamControl
	{
		private var icon:Class = AssetsFileLib.palette_inputIcon;
		
		private var inputStreamPropertyEditor:InputStreamPropertyEditor;
		
		public function InputStream()
		{
		}
		
		public function getIcon():Class
		{
			return icon;
		}
		
		public function getName():String
		{
			return CEPConst.NAME_STREAMCONTROL_INPUTSTREAM;
		}
		
		public function getDisplayName():String
		{
			return "输入";
		}
		
		public function getType():String
		{
			return CEPConst.TYPE_STREAMCONTROL_STREAM;
		}
		
		public function getDescription():String
		{
			return "将外部事件流输入应用的接入点";
		}
		
		public function getPropertyEditorClassName():String
		{
			return "smartx.flex.components.core.cep.control.InputStreamPropertyEditor";
		}
		
		public function printOutputStreamEvent(instance:StreamInstance):StreamEvent
		{
			var defineXML:XML = instance.defineXML;
			var outputEvent:StreamEvent = new StreamEvent();
			for each(var fieldXML:XML in defineXML.settings.schema.field){
				var field:StreamEventField = new StreamEventField();
				field.name = fieldXML.name;
				field.type = fieldXML.type;
				field.description = fieldXML.description;
				outputEvent.fieldList.addItem(field);
			}
			return outputEvent;
		}
		
		public function compile(instance:StreamInstance):String
		{
			var epl:String = CEPUtils.getModuleEPLs(instance);
			epl += "create schema "+instance.name+" as (";
			var outputEvent:StreamEvent = printOutputStreamEvent(instance);
			var isFirst:Boolean = true;
			for each(var field:StreamEventField in outputEvent.fieldList){
				if(!isFirst)
					epl += " , ";
				else
					isFirst = false;
				epl += field.name+" "+field.type;
			}
			epl += ");\n";
			if(instance.isNamedWindow)
				epl += CEPUtils.getEPLForCreateNamedWindow(instance);
			return epl;
		}
		
		public function validate(instance:StreamInstance):ValidateResult{
			var result:ValidateResult = new ValidateResult();
			if(instance.inputStreamList.length > 0){
				result.isSuccessful = false;
				result.errorInfo = "\"输入\"控件本身不能再包含输入事件流";
			}
			return result;
		}
	}
}