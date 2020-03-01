package smartx.flex.components.core.cep
{
	import mx.collections.ArrayCollection;

	public class StreamInstance
	{
		//public var type:IStreamControl;
		private var _defineXML:XML;
		private var _inputStreamList:ArrayCollection;
		
		public function StreamInstance(defineXML:XML, inputStreamList:ArrayCollection){
			this._defineXML = defineXML;
			this._inputStreamList = inputStreamList;
		}
		
		public function get defineXML():XML{
			return _defineXML;
		}

		public function get inputStreamList():ArrayCollection{
			return _inputStreamList;
		}
		
		public function get type():IStreamControl{
			return StreamControlFactory.getStreamControl(defineXML.@type);
		}
		
		public function get code():String{
			return defineXML.@code;
		}
		
		public function get name():String{
			return defineXML.@name;
		}
		
		public function get x():String{
			return defineXML.@x;
		}
		
		public function get y():String{
			return defineXML.@y;
		}
		
		public function get description():String{
			return defineXML.description;
		}
		
		public function get isNamedWindow():Boolean{
			var s:String = defineXML.isNamedWindow;
			if(s == "true")
				return true;
			else
				return false;
		}
		
		public function get windowName():String{
			return defineXML.windowName;
		}
		
		public function get windowType():String{
			return defineXML.windowType;
		}
		
		public function get windowSize():String{
			return defineXML.windowSize;
		}
		
		public function getOutputStreamEvent():StreamEvent{
			return type.printOutputStreamEvent(this);
		}
		
		public function compile():String{
			return type.compile(this);
		}
		
		public function validate():ValidateResult{
			return type.validate(this);
		}
	}
}