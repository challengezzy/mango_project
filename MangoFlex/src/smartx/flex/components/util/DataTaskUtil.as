package smartx.flex.components.util
{
	import com.adobe.utils.StringUtil;
	
	import mx.collections.ArrayCollection;
	
	import smartx.flex.components.util.Hashtable;

	/**
	 * @author zzy
	 * @date Sep 1, 2011
	 */
	public class DataTaskUtil
	{
		
		public static function paramExract(expression:String):ArrayCollection{
			var isParseError:Boolean = false;
			var paramArr:ArrayCollection = new ArrayCollection();
			var lastIndex:int = 0;
			var leftBracketIdx:int = expression.indexOf("{",lastIndex);
			var rightBracketIdx:int = expression.indexOf("}",lastIndex);
			
			while(!isParseError && (leftBracketIdx > -1 || rightBracketIdx > -1) )
			{	
				if(leftBracketIdx < 0 || rightBracketIdx < leftBracketIdx){
					throw Error("字符串[" + expression + "]提取参数失败, { 和 } 不匹配！");
				}
				
				var param:String = expression.substring(leftBracketIdx+1,rightBracketIdx);
				
				lastIndex = rightBracketIdx + 1;
				leftBracketIdx = expression.indexOf("{",lastIndex);
				rightBracketIdx = expression.indexOf("}",lastIndex);
				
				var isParamExist:Boolean = false;
				for each(var temp:String in paramArr ){
					if(param == temp){				
						isParamExist = true;
						break;
					}
				}
				if( !isParamExist)
					paramArr.addItem(param);
			}
			
			return paramArr;				
		}
		
		/**
		 * 数据任务变量解析
		 * */
		public static function parseVariable(variableStr:String,paramMap:Object):String{
			var isDataError:Boolean = false;			
			variableStr = StringUtil.trim(variableStr);
			var lastIndex:int = 0;
			var leftBracketIdx:int = variableStr.indexOf("{",lastIndex);
			var rightBracketIdx:int = variableStr.indexOf("}",lastIndex);
			while(leftBracketIdx > -1 && rightBracketIdx > -1 && !isDataError){				
				var param:String = variableStr.substring(leftBracketIdx+1,rightBracketIdx);
				var paramValue:String = paramMap[param];
				
				if(paramValue == null)
					throw Error("变量["+ param +"]解析失败，未找到对应值!");
				
				variableStr = variableStr.replace("{".concat(param).concat("}"),paramValue);				

				//lastIndex = rightBracketIdx + 1;
				leftBracketIdx = variableStr.indexOf("{");
				rightBracketIdx = variableStr.indexOf("}");
			}
			return variableStr;
		}
		
		
	}
	
}