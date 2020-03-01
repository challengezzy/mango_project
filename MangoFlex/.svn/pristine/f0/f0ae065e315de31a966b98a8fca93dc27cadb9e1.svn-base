package smartx.flex.components.core.workflow.task
{
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;
	
	import smartx.flex.components.vo.workflow.TaskAssign;
	
	public class TaskRule
	{
		private var _taskFilterList:ArrayCollection = new ArrayCollection();
		private var _taskHandler:TaskHandler;
		public function TaskRule(tfl:ArrayCollection,th:TaskHandler){
			if(th == null){
				throw new Error("必须指定taskHandler");
			}
			this._taskFilterList = tfl;
			this._taskHandler = th;
		}
		
		public function get taskFilterList():ArrayCollection{
			return _taskFilterList;
		}
		
		public function get taskHandler():TaskHandler{
			return _taskHandler;
		}
		
		public function getHandleUI(taskAssign:TaskAssign):UIComponent{
			for each(var taskFilter:TaskFilter in _taskFilterList){
				if(taskFilter.check(taskAssign)){
					return _taskHandler.getHandlerUI(taskAssign);
				}
			}
			return null;
		}
		
		public function getBatchHandleUI(taskAssignList:ArrayCollection):UIComponent{
			if(taskAssignList == null || taskAssignList.length==0)
				throw new Error("任务不能为空");
			for each(var taskAssign:TaskAssign in taskAssignList){
				var matched:Boolean = false;
				for each(var taskFilter:TaskFilter in _taskFilterList){
					if(taskFilter.check(taskAssign)){
						matched=true;
						break;
					}
				}
				if(!matched){//该taskassign不匹配任何过滤器，无法批量处理
					return null;
				}
			}
			var uicomponent:UIComponent =  _taskHandler.getBatchHandlerUI(taskAssignList);
			return uicomponent;
		}
		
		public function getDetailUI(taskAssign:TaskAssign):UIComponent{
			for each(var taskFilter:TaskFilter in _taskFilterList){
				if(taskFilter.check(taskAssign)){
					return _taskHandler.getDetailUI(taskAssign);
				}
			}
			return null;
		}
	}
}