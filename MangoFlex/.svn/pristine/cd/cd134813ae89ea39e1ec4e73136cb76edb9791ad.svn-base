package smartx.flex.components.core.cep
{
	import mx.collections.ArrayCollection;

	public class StreamEvent
	{
		public var fieldList:ArrayCollection = new ArrayCollection();
		
		public function findFieldByName(name:String):StreamEventField{
			for each(var field:StreamEventField in fieldList){
				if(field.name == name)
					return field;
			}
			return null;
		}
		
		public function findFieldIndexByName(name:String):int{
			for(var i:int=0;i<fieldList.length;i++){
				var field:StreamEventField = fieldList.getItemAt(i) as StreamEventField;
				if(field.name == name)
					return i;
			}
			return -1;
		}
		
		public function removeFieldByName(name:String):void{
			var i:int = findFieldIndexByName(name);
			if(i > 0)
				fieldList.removeItemAt(i);
		}
	}
}