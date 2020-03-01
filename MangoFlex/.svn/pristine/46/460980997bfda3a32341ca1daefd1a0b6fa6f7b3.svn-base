package smartx.flex.components.core.mtchart.utils
{
	import com.adobe.utils.StringUtil;
	import com.hurlant.eval.ast.ThrowStmt;
	
	import flash.net.SharedObject;
	
	import mx.collections.ArrayCollection;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.mtchart.vo.MTChartConst;

	/**
	 * @author zzy
	 * @date Jul 20, 2011
	 */
	public class FilterParseUtil
	{
		
		public static function parseVariable_old(chartId:String,filter:String,dataValues:ArrayCollection,dboChartId:String,isDBPrior:Boolean=false):String{
			var isDataError:Boolean = false;
			filter = StringUtil.trim(filter);
			var varSoDbo:SharedObject = SharedObject.getLocal(MTChartConst.SHAREOBJ_DBO_VARIABLE,"/");
			var dbId:String = dboChartId.replace("_".concat(chartId),"");

			var lastIndex:int = 0;
			while(filter.indexOf("{",lastIndex) > -1 && filter.indexOf("}",lastIndex) > -1 && !isDataError){
				var isSubWhere:Boolean = true;//判断是否是group by,order by 后面的变量
				
				if((filter.indexOf(MTChartConst.GROUPBY_PREFIX) >=0 && filter.indexOf(MTChartConst.GROUPBY_PREFIX) < filter.indexOf("{",lastIndex)) 
					|| (filter.indexOf(MTChartConst.ORDERBY_PREFIX)>=0 && filter.indexOf(MTChartConst.ORDERBY_PREFIX) < filter.indexOf("{",lastIndex)))
					isSubWhere = false;
				
				var params:String = filter.substring(filter.indexOf("{",lastIndex)+1,filter.indexOf("}",lastIndex));
				if(params.indexOf("[") > -1 && params.indexOf("]") > -1 && dataValues && dataValues.length > 0){
					var property:String = params.substring(0,params.indexOf("["));
					var indexStr:String = params.substring(params.indexOf("[")+1,params.indexOf("]"));
					if(dataValues.length<=Number(indexStr)){
						if(!isNaN(dataValues[dataValues.length-1][property]) || !isSubWhere)
							filter = filter.replace("{".concat(params).concat("}"),dataValues[dataValues.length-1][property]);
						else
							filter = filter.replace("{".concat(params).concat("}"),"'".concat(dataValues[dataValues.length-1][property]).concat("'"));
					}else if(!isNaN(Number(indexStr)) && dataValues[Number(indexStr)]){
						if(!isNaN(dataValues[Number(indexStr)][property]) || !isSubWhere)
							filter = filter.replace("{".concat(params).concat("}"),dataValues[Number(indexStr)][property]);
						else
							filter = filter.replace("{".concat(params).concat("}"),"'".concat(dataValues[Number(indexStr)][property]).concat("'"));
					}else{
						throw Error("参数解析失败：" + filter);
						isDataError = true;
					}
				}else if(dataValues && dataValues.length > 0 && dataValues[0][params] != undefined){//如果没有索引就默认为0
					if(!isNaN(dataValues[0][params]) || !isSubWhere)
						filter = filter.replace("{".concat(params).concat("}"),dataValues[0][params]);
					else
						filter = filter.replace("{".concat(params).concat("}"),"'".concat(dataValues[0][params]).concat("'"));
				}else if(varSoDbo.data[dboChartId] != undefined || varSoDbo.data[dbId] != undefined){
					var valSos:Array;
					var value:String;
					//根据仪表盘和仪表盘对象有优先级高低，进行解析替换
					if(isDBPrior)//如果仪表盘的优先级高
						valSos = varSoDbo.data[dbId] as Array;
					else
						valSos = varSoDbo.data[dboChartId] as Array;
					
					for each(var valSo:Object in valSos){
						if(valSo.name == params)
							value = valSo.defaultValue;
					}
					if(value == null ){
						if(isDBPrior)
							valSos = varSoDbo.data[dboChartId] as Array;
						else
							valSos = varSoDbo.data[dbId] as Array;
						for each(var valSoo:Object in valSos){
							if(valSoo.name == params)
								value = valSoo.defaultValue;
						}
					}
					if( value  && ( !isNaN(Number(value)) || !isSubWhere ) )//值非空，并且
						filter = filter.replace("{".concat(params).concat("}"),value);
					else if(value && value != "")
						filter = filter.replace("{".concat(params).concat("}"),"'"+value+"'");
					else{
						throw Error("参数解析失败：" + filter);
						isDataError = true;
					}
				}else if(ClientEnviorment.getInstance().getVar(params)){
					var val:Object = ClientEnviorment.getInstance().getVar(params);
					if(val && (!isNaN(Number(val)) || !isSubWhere))
						filter = filter.replace("{".concat(params).concat("}"),val);
					else if(val)
						filter = filter.replace("{".concat(params).concat("}"),"'".concat(val).concat("'"));
					else{
						throw Error("参数解析失败：" + filter);
						isDataError = true;
					}
						
				}else{
					throw Error("参数解析失败：" + filter);
					isDataError = true;
				}
				
				lastIndex = filter.indexOf("{",lastIndex);
			}
			return filter;
		}
	}
}