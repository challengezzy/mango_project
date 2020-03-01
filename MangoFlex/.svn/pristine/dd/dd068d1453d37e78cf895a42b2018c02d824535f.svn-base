package smartx.flex.components.util.script
{
	import com.hurlant.eval.ByteLoader;
	import com.hurlant.eval.CompiledESC;
	import com.hurlant.eval.Evaluator;
	
	import flash.events.EventDispatcher;
	import flash.utils.ByteArray;
	
	public class ScriptExecutor extends EventDispatcher
	{
		private var _id:String;
		
		public var scriptText:String="";
		
		//public var isExecuted:Boolean = false;
		
		public var isCompiled:Boolean = false;
		
		private var esc:CompiledESC = new CompiledESC;
		private var evaluator:Evaluator = new Evaluator;
		
		private var variableMap:Object = new Object();
		
		public var enableOutputTrace:Boolean = true;//是否在debug模式打印output
		
		private var bytes:ByteArray;
		
		//所有脚本执行最后必须执行的脚本
		private var additionalScript:String 
		
		public function ScriptExecutor(id:String)
		{
			this._id = id;
			additionalScript = 
			  "\n function sysOutput(value:Object):void{" + 
			  "\n 	ScriptUtil.output(value,sysGetExecutorId());" + 
			  "\n }" + 
			  "\n function sysGetVar(name:String):Object{" + 
			  "\n 	return ScriptUtil.getVar(name,sysGetExecutorId());" + 
			  "\n }" +
			  "\n function getCurrentTime():String{" + 
			  "\n 	return ScriptUtil.getCurrentTime();" + 
			  "\n }" +
			  "\n function sysGetExecutorId():String{" + 
			  "\n 	return \""+_id+"\";"  +
			  "\n }" +
			  "\n function sysGetClientVar(name:String):Object{" + 
			  "\n 	return ScriptUtil.getClientVar(name);"  +
			  "\n }" +
			  "\n ScriptUtil.end(sysGetExecutorId());";
			if(enableOutputTrace)
				this.addEventListener(ScriptEvent.OUTPUT_VALUE,function(event:ScriptEvent):void{
					trace("Script Output:["+event.outputValue+"]");
				});
		}
		
		public function get id():String{
			return _id;
		}
		
		public function registorVar(name:String,value:Object):void{
			variableMap[name] = value;
		}
		//整个对象的所有属性都作为变量注册
		public function registorVarMap(varMap:Object):void{
			for(var name:String in varMap){
				variableMap[name] = varMap[name];
			} 
		}
		
		public function getVar(name:String):Object{
			return variableMap[name];
		}
		
		public function compile():void{
			var src:String = scriptText + additionalScript;
			bytes = esc.eval(src);
			if(bytes == null){
				isCompiled = false;
				throw new Error("编译失败");
			}
		}
		
		public function execute():void{
//			if(isExecuted)
//				throw new Error("无法重复执行脚本");
			if(!isCompiled){
				compile();
			}
			ByteLoader.loadBytes(bytes);
			//isExecuted = true;
		}

	}
}