package
{
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.util.StringUtils;
	import smartx.flex.components.util.script.ScriptEvent;
	import smartx.flex.components.util.script.ScriptExecutor;
	import smartx.flex.components.util.script.ScriptExecutorFactory;
	
	public class ScriptUtil
	{
		public function ScriptUtil()
		{
			throw new Error("ScriptUtil不能实例化");
		}
		
		public static function output(value:Object,executorId:String):void{
			var executor:ScriptExecutor = ScriptExecutorFactory.getExecutor(executorId);
			if(executor != null){
				var event:ScriptEvent = new ScriptEvent(ScriptEvent.OUTPUT_VALUE,value);
				executor.dispatchEvent(event);
			}
		}
		
		public static function end(executorId:String):void{
			ScriptExecutorFactory.removeExecutor(executorId);
		}
		
		public static function getVar(name:String,executorId:String):Object{
			var executor:ScriptExecutor = ScriptExecutorFactory.getExecutor(executorId);
			if(executor != null){
				return executor.getVar(name);
			}
			return null;
		}
		public static function getClientVar(name:String):Object{
			return ClientEnviorment.getInstance().getVar(name);
		}
		
		public static function getCurrentTime():String{
			var nowStr:String = StringUtils.convertDateToString(new Date(),1);
			
			return nowStr;
		}

	}
}