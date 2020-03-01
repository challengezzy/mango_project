package smartx.flex.components.util.script
{
	import smartx.flex.components.util.Random;
	
	public class ScriptExecutorFactory
	{
		private static var executorPool:Object = new Object();//执行器的池
		
		public function ScriptExecutorFactory(){
			throw new Error("ScriptExecutorFactory不能实例化");
		}
		
		public static function createNewExecutor():ScriptExecutor{
			var newId:String = Random.bit(8);
			while(executorPool[newId] != null){//如果有重复就重取
				newId = Random.bit(8);
			}
			var executor:ScriptExecutor = new ScriptExecutor(newId);
			executorPool[newId] = executor;
			return executor;
		}
		
		public static function getExecutor(id:String):ScriptExecutor{
			return executorPool[id];
		}
		
		public static function removeExecutor(id:String):void{
			executorPool[id] = null;
		}
	}
}