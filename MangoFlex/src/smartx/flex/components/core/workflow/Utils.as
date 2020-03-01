package smartx.flex.components.core.workflow
{
	import flash.geom.Point;
	
	import smartx.flex.components.vo.workflow.Activity;
	import smartx.flex.components.vo.workflow.Process;
	import smartx.flex.components.vo.workflow.TaskAssignRule;
	import smartx.flex.components.vo.workflow.Transition;
	import smartx.flex.components.util.flowui.FlowIcon;
	
	public class Utils
	{
		public function Utils()
		{
		}
		
		/**
		 * 产生一个指定范围的随机数
		*/
		public static function random(min: int, max: int): int{
			return int((max - min) * Math.random() + 1 + min);
		}
		
		/**
		 * 	生成一个随机字符串,长度由参数指定
		*/
		public static function randomString(n: int): String{
			var i: int = 0;
			var str: String = "";
			while(i < n){
				str += random(0, 9);
				i ++;
			}
			return str;
		}
		
		public static function nodeHTML(node: String): String{
			return "<b style='color:blue;'>" + node + "</b>";
		}
		
		public static function attributeHTML(attribute: String): String{
			return "<i style='color:red;'>" + attribute + "</i>";
		}
		
		public static function space(n: int): String{
			var i: int = 0;
			var str: String = "";
			while(i < n){
				str += "&nbsp;";
				i ++;
			}
			return str;
		}
		
		public static function xmlDeleteNode(xmlToDelete:XML):Boolean
	    {
	     	var cn:XMLList = XMLList(xmlToDelete.parent()).children();
	     
	     	for ( var i:Number = 0 ; i < cn.length() ; i++ )
	     	{
	      		if ( cn[i] == xmlToDelete ) 
	      		{
	      			delete cn[i];      
	       			return true;
	      		}
	     	}    
    
		    return false;
		    
		}
		
		public static function getArrowLineAxis(fromIcon:FlowIcon, toIcon:FlowIcon):Array{
			var fromx:int;
			var fromy:int;
			var tox:int;
			var toy:int;
			if(fromIcon.x + fromIcon.iconWidth <= toIcon.x){
				fromx = fromIcon.x + fromIcon.iconWidth;
				tox = toIcon.x;
			}
			else if(fromIcon.x >= toIcon.x + toIcon.iconWidth){
				fromx = fromIcon.x;
				tox = toIcon.x + toIcon.iconWidth;
			}
			else{
				fromx = fromIcon.x + fromIcon.iconWidth/2;
				tox = toIcon.x + toIcon.iconWidth/2;
			}
			
			if(fromIcon.y + fromIcon.iconHeight <= toIcon.y){
				fromy = fromIcon.y + fromIcon.iconHeight;
				toy = toIcon.y;
			}
			else if(fromIcon.y >= toIcon.y + toIcon.iconHeight){
				fromy = fromIcon.y;
				toy = toIcon.y + toIcon.iconHeight;
			}
			else{
				fromy = fromIcon.y + fromIcon.iconHeight/2;
				toy = toIcon.y + toIcon.iconHeight/2;
			}
			var result:Array = new Array(2);
			result[0] = new Point(fromx,fromy);
			result[1] = new Point(tox,toy);
			return result;
		}
		
		private static function handleNullXMLValue(v:String):String{
			return v == null?"":v;
		}
		
		public static function getNextProcessCode():String{
			var prefix:String = "WFP";
			var now:Date = new Date();
			return prefix+now.fullYear+"-"+(now.month+1)+"-"+now.date+"."+randomString(4);
		}
		
		//获取下一个转移id
		public static function getNextTransitionCode(xml:XML):String{
			var i:int=0;
			var prefix:String = "WFT";
			while(i < Infinity){
				var newCode:String = (prefix+i);
				var exists:Boolean = false;
				for each(var t:XML in xml.transitions.transition){
					if(t.code == newCode){
						exists = true;
						break;
					}
				}
				if(exists){
					i++;
					continue;
				}	
				else
					return newCode;
			}
			return prefix+randomString(4);//不可能走到这里
		}
		
		//获取下一个环节id
		public static function getNextActivityCode(xml:XML):String{
			var i:int=0;
			var prefix:String = "WFA";
			while(i < Infinity){
				var newCode:String = (prefix+i);
				var exists:Boolean = false;
				for each(var t:XML in xml.activities.activity){
					if(t.code == newCode){
						exists = true;
						break;
					}
				}
				if(exists){
					i++;
					continue;
				}	
				else
					return newCode;
			}
			return prefix+randomString(4);//不可能走到这里
		}
		
		//是否已存在该转移
		public static function transitionExists(xml:XML,fromCode:String,toCode:String):Boolean{
			for each(var t:XML in xml.transitions.transition){
				if(t.fromActivityCode == fromCode && t.toActivityCode == toCode)
					return true;
			}
			return false;
		}
		
		//将数据库中读取的process转换为xml格式
		public static function processToXML(process:Process):XML{
			var processXML:XML = 
			<process>
				<activities/>
				<transitions/>
			</process>;
			processXML.@code = handleNullXMLValue(process.code);
			processXML.@name = handleNullXMLValue(process.name);
			for each(var a:Activity in process.activityCollection){
				var activityXML:XML =
				<activity>
				  <code></code> 
				  <wfname></wfname> 
				  <uiname></uiname> 
				  <x></x> 
				  <y></y> 
				  <afterInterceptorClassName /> 
				  <beforeInterceptorClassName /> 
				  <performMode></performMode> 
				  <splitType></splitType> 
				  <joinType></joinType> 
				  <joinCondition />
				  <taskAssignRules/> 
				  <description /> 
				</activity>
				;
				activityXML.code = handleNullXMLValue(a.code);
				activityXML.wfname = handleNullXMLValue(a.wfname);
				activityXML.uiname = handleNullXMLValue(a.uiname);
				activityXML.x = a.x;
				activityXML.y = a.y;
				activityXML.afterInterceptorClassName = handleNullXMLValue(a.afterInterceptorClassName);
				activityXML.beforeInterceptorClassName = handleNullXMLValue(a.beforeInterceptorClassName);
				activityXML.performMode = handleNullXMLValue(a.performMode);
				activityXML.splitType = handleNullXMLValue(a.splitType);
				activityXML.joinType = handleNullXMLValue(a.joinType);
				activityXML.joinCondition = handleNullXMLValue(a.joinCondition);
				activityXML.description = handleNullXMLValue(a.description);
				//处理扩展属性
				for(var key:String in a.extendProperties){
					var value:String = a.extendProperties[key];
					var extPropertiesXML:XML =
					<property/>;
					extPropertiesXML.@key = key;
					extPropertiesXML.@value = value;
					activityXML.appendChild(extPropertiesXML);
				}
				for each(var tar:TaskAssignRule in a.taskAssignRuleList){
					var tarXML:XML =
					<taskAssignRule>
						<taskAssignObject/>						
					</taskAssignRule>;
					tarXML.@executorClassName = tar.executorClassName;
					tarXML.taskAssignObject.@id = tar.object.id;
					tarXML.taskAssignObject.@name = tar.object.name;
					if(tar.taskAssignRuleTemplate != null){
						tarXML.@templateName = tar.taskAssignRuleTemplate.name;
					}
					activityXML.taskAssignRules.appendChild(tarXML);
				}
				processXML.activities.appendChild(activityXML);
			}
			for each(var t:Transition in process.transitionCollection){
				var transitionXML:XML = 
				<transition>
				  <code></code> 
				  <wfname></wfname> 
				  <uiname></uiname> 
				  <fromActivityCode></fromActivityCode> 
				  <toActivityCode></toActivityCode> 
				  <condition /> 
				</transition>;
				transitionXML.code = handleNullXMLValue(t.code);
				transitionXML.wfname = handleNullXMLValue(t.wfname);
				transitionXML.uiname = handleNullXMLValue(t.uiname);
				transitionXML.fromActivityCode = handleNullXMLValue(t.fromActivity.code);
				transitionXML.toActivityCode = handleNullXMLValue(t.toActivity.code);
				transitionXML.condition = handleNullXMLValue(t.condition);
				processXML.transitions.appendChild(transitionXML);
			}
			return processXML;
		}
	}
}