package smartx.flex.components.core.mtchart.utils
{
	import com.adobe.utils.StringUtil;
	import com.fusioncharts.components.FusionCharts;
	
	import flash.display.DisplayObject;
	import flash.display.Loader;
	import flash.net.LocalConnection;
	import flash.net.SharedObject;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Image;
	import mx.utils.ObjectUtil;
	
	import qs.utils.StringUtils;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.mtchart.MTChartPanel;
	import smartx.flex.components.core.mtchart.vo.MTChart;
	import smartx.flex.components.core.mtchart.vo.MTChartConst;
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.MetadataTemplet;

	/**
	 * sky zhangzz
	 **/
	public class MTChartUtil
	{
		public function MTChartUtil()
		{
		}
		
		/**
		 * 将元数据转换成MTCHART
		 **/
		public static function parseMetadataTemplet(metadata:XML):MTChart{
			var mc:MTChart = new MTChart();
			mc.datasource = metadata.datasource;
			mc.viewName = metadata.viewname;
			mc.chartName = metadata.chartname;
			mc.backupChartName = metadata.backupChartname;
			mc.refreshInterval = Number(metadata.refreshinterval);
			mc.filter = metadata.filter;
			mc.backupFilter = metadata.backupFilter;
			mc.groupby = metadata.groupby;
			mc.orderby = metadata.orderby;
			mc.isHorizontalLegend = metadata.isHorizontalLegend=="true"?true:false;
			mc.horizontalField = metadata.extend.horizontalField;
			mc.verticalField = metadata.extend.verticalField;
			
			mc.horizontalTitle =  metadata.extend.horizontalTitle;
			mc.verticalTitle = metadata.extend.verticalTitle;
			
			//混合图属性封装
			mc.chartType = metadata.charttype;
			mc.xAxiaName = metadata.extend.xaxianame;
			mc.pyAxiaName = metadata.extend.pyaxianame;
			mc.syAxiaName = metadata.extend.syaxianame;
			mc.lineMode = metadata.extend.linemode;
			mc.dataMode = metadata.extend.datamode;
			mc.yAxisMaxValue = metadata.extend.yaxismaxvalue;
			mc.yAxisMinValue = metadata.extend.yaxisminvalue;
			
			for each(var item:XML in metadata.extend.items.item){
				if(String(item.@dataField) != "" && item.@dataField != null)
					mc.dataFileds.push(item.@dataField);
				if(String(item.@nameField) != "" && item.@nameField != null)
					mc.nameFileds.push(item.@nameField);
				if(String(item.@labelField) != "" && item.@labelField != null)
					mc.labelFileds.push(item.@labelField);
				if(String(item.@color) != "" && item.@color != null)
					mc.colors.push(item.@color);
				if(String(item.@seriesNameField) != "" && item.@seriesNameField != null)
					mc.seriesNames.push(item.@seriesNameField);
				if(String(item.@type) != "" && item.@type != null)
					mc.types.push(item.@type);
				if(mc.dataMode == MTChart.COLUMN_MODE && (item.@bindSubYAxis=='Y' || item.@bindSubYAxis=='y'))
					mc.subSeriesNames.push(item.@seriesNameField);
				if(String(item.@linkField) != "" && item.@linkField != null)
					mc.linkFileds.push(item.@linkField);
				if(item.@xShowField != null && String(item.@xShowField) != "")
					mc.xShowFields.push(item.@xShowField);
			}
			
			mc.isShowLabel = metadata.extend.isShowLabel == "true"?true:false;
			mc.isShow3D = metadata.extend.isShow3D == "true"?true:false;
			
			if(mc.dataMode == MTChart.ROW_MODE && metadata.extend.subseriesnames.length()>0)
				mc.subSeriesNames = String(metadata.extend.subseriesnames).split(",");
			return mc;
		}
		
		/**
		 * 垃圾回收
		 * */
		public static function runGC():void{
			try {
				
				trace("force a GC");
				
				new LocalConnection().connect("foo");
				
				new LocalConnection().connect("foo");
				
			} catch (e:Error) {
				
				trace(e.message);
				
			}
		}
		
		public static function releaseResourceForFusionCharts(fusionChart:FusionCharts):void{
			for(var i:int=0;i<fusionChart.numChildren;i++){
				var child:* = fusionChart.getChildAt(i);
				if(child is Loader){
					var loader:Loader = child as Loader;
					loader.unloadAndStop(true);
				}
				else if(child is Image){
					var image:Image = child as Image;
					image.source = null;
				}
			}
			runGC();
//			fusionChart.FCFolder = null;
//			fusionChart.FCChartType = null;
//			fusionChart.FCRender();
		}
		
		/**
		 * 普通的变量解析，原始字符创形式为[abcded{var}xxx....]
		 **/
		public static function parseVariable(chartId:String,variableStr:String,dataValues:ArrayCollection,dboChartId:String,isDBPrior:Boolean=false):String{
			var isDataError:Boolean = false;
			
			variableStr = StringUtil.trim(variableStr);
			var varSoDbo:SharedObject = SharedObject.getLocal(MTChartConst.SHAREOBJ_DBO_VARIABLE,"/");//客户端仪表盘和仪表盘对象共享变量
			var dbId:String = dboChartId.replace("_".concat(chartId),"");
			
			var lastIndex:int = 0;
			while(variableStr.indexOf("{",lastIndex) > -1 && variableStr.indexOf("}",lastIndex) > -1 && !isDataError){				
				var params:String = variableStr.substring(variableStr.indexOf("{",lastIndex)+1,variableStr.indexOf("}",lastIndex));
				var clientVal:Object = ClientEnviorment.getInstance().getVar(params);//客户端环境变量
				var isVarExist:Boolean = false;//变量是否找到
				
				//变量解析顺序 ：参数数组(有索引)->参数数组(无索引)->仪表盘和仪表盘对象->客户端环境变量				
				//数组索引解析
				if(params.indexOf("[") > -1 && params.indexOf("]") > -1 && dataValues && dataValues.length > 0){
					var property:String = params.substring(0,params.indexOf("["));
					var indexStr:String = params.substring(params.indexOf("[")+1,params.indexOf("]"));
					
					//索引超出数组长度
					if( !isNaN(Number(indexStr)) && Number(indexStr) > -1 ){
						if( dataValues.length<=Number(indexStr)){//索引太大，取最后一个
							variableStr = variableStr.replace("{".concat(params).concat("}"),dataValues[dataValues.length-1][property]);
							isVarExist = true;
						}else if( dataValues[Number(indexStr)]){
							variableStr = variableStr.replace("{".concat(params).concat("}"),dataValues[Number(indexStr)][property]);
							isVarExist = true;
						}
					}else{
						isDataError = true;
						throw Error("参数{" + params + "}解析失败：" + variableStr);
					}
				}
				//如果没有索引就默认为0
				if( !isVarExist && dataValues && dataValues.length > 0 && dataValues[0][params] != undefined){
					variableStr = variableStr.replace("{".concat(params).concat("}"),dataValues[0][params]);
					isVarExist = true;
				}
				
				//根据仪表盘和仪表盘对象有优先级高低，进行解析替换
				if(!isVarExist && (varSoDbo.data[dboChartId] != undefined || varSoDbo.data[dbId] != undefined) ){
					var valSo1:Array;//第一优先级
					var valSo2:Array;// 第2优先级
					var value:String=null;
					
					//如果有一个为空，不需要判断优先级
					if( varSoDbo.data[dboChartId] == undefined ){
						valSo1 = varSoDbo.data[dbId] as Array;
					}else if(varSoDbo.data[dbId] == undefined){
						valSo1 = varSoDbo.data[dboChartId] as Array;
					}else{
						//如果仪表盘的优先级高
						if(isDBPrior){
							valSo1 = varSoDbo.data[dbId] as Array;
							valSo2 = varSoDbo.data[dboChartId] as Array;
						}else{
							valSo1 = varSoDbo.data[dboChartId] as Array;
							valSo2 = varSoDbo.data[dbId] as Array;
						}
					}
					//第一优先级搜索
					for each(var valSo:Object in valSo1){
						if(valSo.name == params){
							value = valSo.defaultValue;
							break;
						}
					}
					//第二优先级搜索
					if(value == null &&  valSo2 != null){
						for each(var valSoo:Object in valSo2){
							if(valSoo.name == params){
								value = valSoo.defaultValue;
								break;
							}
						}
					}
					
					if( value && value!=""){
						variableStr = variableStr.replace("{".concat(params).concat("}"),value);
						isVarExist = true;
					}
				}
				
				if( !isVarExist && clientVal ){
					variableStr = variableStr.replace("{".concat(params).concat("}"),clientVal);
					isVarExist = true;
				}
					
				if(!isVarExist){//参数解析失败
					isDataError = true;
					throw Error("参数{" + params + "}解析失败：" + variableStr);
				}
				lastIndex = variableStr.indexOf("{",lastIndex);
			}
			return variableStr;
		}
		
		/**
		 * SQL的Where子句变量解析，字符创形式为[where a={b} and ...] [group by c,{d},{e}...] [order by {f},g,...]
		 **/
		public static function parseFilterVariable(chartId:String,filter:String,dataValues:ArrayCollection,dboChartId:String,isDBPrior:Boolean=false):String{
			if(filter == null)
				return null;
			var isDataError:Boolean = false;
			
			filter = StringUtil.trim(filter);
			var varSoDbo:SharedObject = SharedObject.getLocal(MTChartConst.SHAREOBJ_DBO_VARIABLE,"/");
			var dbId:String = dboChartId.replace("_".concat(chartId),"");
			
			var lastIndex:int = 0;
			while(filter.indexOf("{",lastIndex) > -1 && filter.indexOf("}",lastIndex) > -1 && !isDataError){
				var isSubWhere:Boolean = true;//判断是否是group by,order by 后面的变量
				var params:String = filter.substring(filter.indexOf("{",lastIndex)+1,filter.indexOf("}",lastIndex));
				var clientVal:Object = ClientEnviorment.getInstance().getVar(params);//客户端环境变量
				var isVarExist:Boolean = false;//变量是否找到
				
				if((filter.indexOf(MTChartConst.GROUPBY_PREFIX) >=0 && filter.indexOf(MTChartConst.GROUPBY_PREFIX) < filter.indexOf("{",lastIndex)) 
					|| (filter.indexOf(MTChartConst.ORDERBY_PREFIX)>=0 && filter.indexOf(MTChartConst.ORDERBY_PREFIX) < filter.indexOf("{",lastIndex)))
					isSubWhere = false;
				
				//变量解析顺序 ：参数数组(有索引)->参数数组(无索引)->仪表盘和仪表盘对象->客户端环境变量
				if(params.indexOf("[") > -1 && params.indexOf("]") > -1 && dataValues && dataValues.length > 0){
					var property:String = params.substring(0,params.indexOf("["));
					var indexStr:String = params.substring(params.indexOf("[")+1,params.indexOf("]"));
					if( !isNaN(Number(indexStr)) && dataValues.length <= Number(indexStr)){
						isVarExist = true;
						if(!isNaN(dataValues[dataValues.length-1][property]) || !isSubWhere)
							filter = filter.replace("{".concat(params).concat("}"),dataValues[dataValues.length-1][property]);
						else
							filter = filter.replace("{".concat(params).concat("}"),"'".concat(dataValues[dataValues.length-1][property]).concat("'"));
					}else if(!isNaN(Number(indexStr)) && dataValues[Number(indexStr)]){
						isVarExist = true;
						if(!isNaN(dataValues[Number(indexStr)][property]) || !isSubWhere)
							filter = filter.replace("{".concat(params).concat("}"),dataValues[Number(indexStr)][property]);
						else
							filter = filter.replace("{".concat(params).concat("}"),"'".concat(dataValues[Number(indexStr)][property]).concat("'"));
					}else{
						isDataError = true;
						throw Error("参数{" + params + "}解析失败：" + filter);
					}
				}
				
				if( !isVarExist && dataValues && dataValues.length > 0 && dataValues[0][params] != undefined){//如果没有索引就默认为0
					isVarExist = true;
					if(!isNaN(dataValues[0][params]) || !isSubWhere)
						filter = filter.replace("{".concat(params).concat("}"),dataValues[0][params]);
					else
						filter = filter.replace("{".concat(params).concat("}"),"'".concat(dataValues[0][params]).concat("'"));
				}
				
				if( !isVarExist && (varSoDbo.data[dboChartId] != undefined || varSoDbo.data[dbId] != undefined) ){
					var valSo1:Array;//第一优先级
					var valSo2:Array;// 第2优先级
					var value:String=null;
					
					//如果有一个为空，不需要判断优先级
					if( varSoDbo.data[dboChartId] == undefined ){
						valSo1 = varSoDbo.data[dbId] as Array;
					}else if(varSoDbo.data[dbId] == undefined){
						valSo1 = varSoDbo.data[dboChartId] as Array;
					}else{
						//如果仪表盘的优先级高
						if(isDBPrior){
							valSo1 = varSoDbo.data[dbId] as Array;
							valSo2 = varSoDbo.data[dboChartId] as Array;
						}else{
							valSo1 = varSoDbo.data[dboChartId] as Array;
							valSo2 = varSoDbo.data[dbId] as Array;
						}
					}
					//第一优先级搜索
					for each(var valSo:Object in valSo1){
						if(valSo.name == params){
							value = valSo.defaultValue;
							break;
						}
					}
					//第二优先级搜索
					if(value == null &&  valSo2 != null){
						for each(var valSoo:Object in valSo2){
							if(valSoo.name == params){
								value = valSoo.defaultValue;
								break;
							}
						}
					}
					if( value && value!=""){
						isVarExist = true;
						if( !isNaN(Number(value)) || !isSubWhere )//值非空，并且
							filter = filter.replace("{".concat(params).concat("}"),value);
						else
							filter = filter.replace("{".concat(params).concat("}"),"'"+value+"'");
					}
				}
				
				if( !isVarExist && clientVal){
					isVarExist = true;
					if( (!isNaN(Number(clientVal)) || !isSubWhere))
						filter = filter.replace("{".concat(params).concat("}"),clientVal);
					else
						filter = filter.replace("{".concat(params).concat("}"),"'".concat(clientVal).concat("'"));
				}
				
				if( !isVarExist){
					isDataError = true;
					throw Error("参数{" + params + "}解析失败：" + filter);
				}
				
				lastIndex = filter.indexOf("{",lastIndex);
			}
			return filter;
		}
		
		//解析filter中的变量
		public static function parseFilter(id:String,dataValues:ArrayCollection,dboChartId:String,mtchart:MTChartPanel,isDBPriorVar:Boolean=true):String{
			var filter:String = "";
			var condition:String = "";
			var orderby:String = "";
			var groupby:String = "";	
			var backupCondition:String = "";
			if("" != StringUtil.trim(mtchart.mc.filter))
				condition = MTChartConst.WHERE_PREFIX + mtchart.mc.filter;	
			if("" != StringUtil.trim(mtchart.mc.backupFilter))
				backupCondition = MTChartConst.WHERE_PREFIX + mtchart.mc.backupFilter;	
			if("" != StringUtil.trim(mtchart.mc.orderby))
				orderby = MTChartConst.ORDERBY_PREFIX + mtchart.mc.orderby.replace("order by","");	
			if("" != StringUtil.trim(mtchart.mc.groupby))
				groupby = MTChartConst.GROUPBY_PREFIX + mtchart.mc.groupby.replace("group by","");
			
			filter = condition + groupby + orderby;
			if(filter != null && StringUtil.trim(filter) != ""){
				try{
					filter = MTChartUtil.parseFilterVariable(id,filter,dataValues,dboChartId,isDBPriorVar);
				}catch(e:Error){
					trace(e.message);
					//mtchart.title = "待初始化";
					//使用备用filter
					if(backupCondition != null && StringUtil.trim(backupCondition) != ""){
						filter = backupCondition + groupby + orderby;
						try{
							filter = MTChartUtil.parseFilterVariable(id,filter,dataValues,dboChartId,isDBPriorVar);
						}
						catch(e:Error){
							trace(e.message);
							throw new Error("dataError");
						}
					}
					else
						throw new Error("dataError");
				}
			}
			try{
				//标题动态解析
				mtchart.title = MTChartUtil.parseVariable(id,mtchart.originalTitle,dataValues,dboChartId,isDBPriorVar);
			}catch(e:Error){
				trace(e.message);
				if(mtchart.backupOriginalTitle != null && mtchart.backupOriginalTitle != ""){
					try{
						mtchart.title = MTChartUtil.parseVariable(id,mtchart.backupOriginalTitle,dataValues,dboChartId,isDBPriorVar);
					}
					catch(e:Error){
						trace(e.message);
						mtchart.title = "";
					}
				}else 
					mtchart.title = "";
				//mtchart.title = mtchart.originalTitle;
			}
			
			return filter;
		}
		
		//解析MTCHART中的绑定的同义词字段
		public static function parseSynonyms(dataProvider:ArrayCollection,contentXml:XML):ArrayCollection{
			if(contentXml.bindSynonymses.length() == 0)
				return dataProvider;
			var synonymsMt:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet(GlobalConst.MTCODE_SYNONYMS);
			if(synonymsMt == null)
				throw new Error("解析绑定同义词字段错误 ,同义词元数据模板未找到！");
			var sysnonymsMtXml:XML = synonymsMt.contentXML;
			for each(var bindSynonymsXml:XML in contentXml.bindSynonymses.bindSynonymse){
				var bindField:String = String(bindSynonymsXml.@bindField);
				var fieldName:String = String(bindSynonymsXml.@fieldName);
				var synonymsStr:String = String(bindSynonymsXml.@synonyms);
				if(bindField == "" || fieldName == "" || synonymsStr.split("@@@").length < 2)
					continue;
				var synonymsCode:String = synonymsStr.split("@@@")[1];
				if(sysnonymsMtXml.synonyms.(@code==synonymsCode).length() == 0)
					continue;
				for each(var data:Object in dataProvider){
					data[fieldName] = sysnonymsMtXml.synonyms.(@code==synonymsCode)[0].variable.(@value==data[bindField]).length() > 0?
						String(sysnonymsMtXml.synonyms.(@code==synonymsCode)[0].variable.(@value==data[bindField])[0].@name):data[bindField];
				}
			}
			return dataProvider;
		}
		
		public static function convertFormulaMacPars(str_formula:String):String{
			var temp_str_fomula:String = str_formula;
			var clientTemp:ClientEnviorment = ClientEnviorment.getInstance();
			if(clientTemp == null){
				return temp_str_fomula;
			}
			var map:Object = clientTemp.getVarMap();
			if(map == null){
				return temp_str_fomula;
			}
			var objInfo:Object = ObjectUtil.getClassInfo(map);
			var fieldNameArray:Array = objInfo["properties"] as Array;
			for each(var qName:QName in fieldNameArray){
				var tempValue:Object = map[qName.localName];
				if(tempValue==null){
					temp_str_fomula = StringUtils.replaceAll(temp_str_fomula,"{"+qName.localName+"}","null");
				}else{
					if(tempValue is String){
						temp_str_fomula = StringUtils.replaceAll(temp_str_fomula,"{"+qName.localName+"}","'"+tempValue.toString()+"'");
					}
				}
				
			}
			
			return temp_str_fomula;
		}
	}
}