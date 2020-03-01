package smartx.bam.flex.modules.entitymodel.utils
{
	import com.adobe.utils.StringUtil;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.controls.dataGridClasses.DataGridColumn;
	
	import smartx.bam.flex.utils.BAMUtil;
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.util.Hashtable;
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.util.StringUtils;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.MetadataTemplet;

	public class EntityUtil
	{
		
		public function EntityUtil()
		{
		}
		
		
		public function getSqlWithOneCondition(entityXml:XML,ruleXml:XML=null,addCondition:Boolean=false):String{
			var sqlStr:String = "";
			var type:String = String(entityXml.mappingInfo.@type);
			var tableName:String = "";
			if(type.toLowerCase() == "table"){
				tableName = String(entityXml.mappingInfo.tableName);
			}else{
				var tempView:String = String(entityXml.mappingInfo.queryView);
				if(tempView != null ){
					var array:Array = tempView.split("@@");
					if(array != null && array.length ==2){
						tableName = array[1] as String;
					}
				}
			}
			
			var columnsAliasStr:String ="";
			var columnsStr:String = "";
			for each(var xml:XML in entityXml.mappingInfo.attributeMapping.map){
				var attributeName:String = xml.@attributeName;
				var columnName:String = xml.@columnName;
				
				if(columnsAliasStr == ""){
					columnsAliasStr = columnName+" "+ attributeName;
				}else{
					columnsAliasStr = columnsAliasStr +","+columnName+" "+ attributeName;
				}
				
				if(columnsStr == ""){
					columnsStr = columnName;
				}else{
					columnsStr = columnsStr +","+columnName;
				}
				
			}
			
			if(columnsAliasStr == ""){
				columnsAliasStr = " * ";
			}
			if(columnsStr == "" ){
				columnsStr = " * ";
			}
			
			var tempStr:String = "SELECT "+columnsStr+" FROM ("+tableName+") WHERE 1=1 ";
			
			if(addCondition){
				var conditionStr:String = getConditon(ruleXml);
				if(conditionStr != "" ){
					tempStr = tempStr + " "+ conditionStr
				}
			}
			
			sqlStr = "SELECT "+columnsAliasStr+" FROM ("+tempStr+")";
			
			return sqlStr;
		}
		
		private function getConditon(rule:XML):String{
			var tempSql:String = "";
			var andSql:String = "";
			var orSql:String = "";
			
			if( rule != null){
				for each(var item:XML in rule.item){
					var enabled:String = item.@enabled;
					if(enabled != null && enabled == "true"){
						
						var type:String = item.@type;
						var column:String = item.@column;
						var logicalOperator:String = item.@logicalOperator;
						var relationalOperator:String = item.@relationalOperator;
						var value:String = item.@value;
						
						var valueStr:String = dealValue(value,relationalOperator);
						
						if(logicalOperator == "AND"){
							var andTemp:String ="";
							andTemp = column+" "+relationalOperator+" "+valueStr;
							if(type == "stringLength"){
								andTemp = "length("+column+")"+" "+relationalOperator+" "+valueStr;
							}
							if(andSql == ""){
								andSql = andTemp+"";
							}else{
								if(andTemp !="")
									andSql = andSql + " AND "+andTemp;
							}
							
						}else if(logicalOperator == "OR"){
							var orTemp:String ="";
							orTemp = column+" "+relationalOperator+" "+valueStr;
							if(type == "stringLength"){
								orTemp = "length("+column+")"+" "+relationalOperator+" "+valueStr;
							}
							if(orSql == ""){
								orSql = orTemp+"";
							}else{
								if(orTemp !="")
									orSql = orSql + " OR "+orTemp;
							}
						}
						
					}
				}
			}
			if(andSql !="" && orSql != ""){
				tempSql = "AND "+andSql+" OR "+orSql;
			}else if(andSql =="" && orSql != ""){
				tempSql = "OR "+orSql;
			}else if(andSql !="" && orSql == ""){
				tempSql = "AND "+andSql;
			}
			
			return tempSql;
		}
		
		private function dealValue(value:String,relationalOperator:String):String{
			var realValue:String ="";
			if(value != null && value != ""){
				var valueArr:Array = value.split(",");
				for each(var vl:String in valueArr){
					if(realValue == ""){
						realValue = "'"+vl+"'";
					}else{
						realValue = realValue+","+"'"+vl+"'";
					}
				}
			}
			
			if(relationalOperator == "IN" || relationalOperator == "NOT IN"){
				realValue = " ("+realValue+")";
			}else if(relationalOperator =="LIKE"){
				realValue = "'%"+value+"%'";
			}else if(relationalOperator =="IS" || relationalOperator =="IS NOT"){
				realValue = value;
			}
			return realValue;
		}
		
		public function getColumns(entityXml:XML):Array{
			
			var columnsCol:Array = new Array();
			
			for each( var attributeItem:XML in entityXml.attributes.attribute){
				var column:DataGridColumn = new DataGridColumn();
				var name:String = String(attributeItem.@name);
				var type:String = String(attributeItem.@type);
				var label:String = String(attributeItem.@label);
				var isDisplay:String = String(attributeItem.@isDisplay);
				var refDesc:String = String(attributeItem.@dictionaryRef);
				column.headerText = label;
				if(name != null ){
					name = name.toLowerCase();
				}
				column.dataField = name;
				column.width = 200;
//				for each( var mappingItem:XML in entityXml.mappingInfo.attributeMapping.map){
//					var attributeName:String = String(mappingItem.@attributeName);
//					var columnName:String = String(mappingItem.@columnName);
//					if(attributeName != "" && attributeName != null && attributeName == name){
//						column.dataField = name;
//					}
//				}
				if(isDisplay != null && isDisplay.toLowerCase() == "true"){
					columnsCol.push(column);
				}
			}
			
			return columnsCol;
		}
		
		private function getAllCondition(entityXml:XML):String{
			
			var tempSql:String = "";
			var andSql:String = "";
			var orSql:String = "";
			
			if( entityXml != null){
				for each(var rule:XML in  entityXml.rules.rule){
					for each(var item:XML in rule.item){
						var enabled:String = item.@enabled;
						if(enabled != null && enabled == "true"){
							
							var type:String = item.@type;
							var column:String = item.@column;
							var logicalOperator:String = item.@logicalOperator;
							var relationalOperator:String = item.@relationalOperator;
							var value:String = item.@value;
							
							var valueStr:String = dealValue(value,relationalOperator);
							
							if(logicalOperator == "AND"){
								var andTemp:String ="";
								andTemp = column+" "+relationalOperator+" "+valueStr;
								if(type == "stringLength"){
									andTemp = "length("+column+")"+" "+relationalOperator+" "+valueStr;
								}
								if(andSql == ""){
									andSql = andTemp+"";
								}else{
									if(andTemp !="")
										andSql = andSql + " AND "+andTemp;
								}
								
							}else if(logicalOperator == "OR"){
								var orTemp:String ="";
								orTemp = column+" "+relationalOperator+" "+valueStr;
								if(type == "stringLength"){
									orTemp = "length("+column+")"+" "+relationalOperator+" "+valueStr;
								}
								if(orSql == ""){
									orSql = orTemp+"";
								}else{
									if(orTemp !="")
										orSql = orSql + " OR "+orTemp;
								}
							}
							
						}
					}
				}
			}
			if(andSql !="" && orSql != ""){
				tempSql = "AND "+andSql+" OR "+orSql;
			}else if(andSql =="" && orSql != ""){
				tempSql = "OR "+orSql;
			}else if(andSql !="" && orSql == ""){
				tempSql = "AND "+andSql;
			}
			
			return tempSql;
		}
		
		public function getSqlWithAllCondition(entityXml:XML):String{
			var sqlStr:String = "";
			var type:String = String(entityXml.mappingInfo.@type);
			var tableName:String = "";
			if(type.toLowerCase() == "table"){
				tableName = String(entityXml.mappingInfo.tableName);
			}else{
				var tempView:String = String(entityXml.mappingInfo.queryView);
				if(tempView != null ){
					var array:Array = tempView.split("@@");
					if(array != null && array.length ==2){
						tableName = array[1] as String;
					}
				}
			}
			
			var columnsAliasStr:String ="";
			var columnsStr:String = "";
			for each(var xml:XML in entityXml.mappingInfo.attributeMapping.map){
				var attributeName:String = xml.@attributeName;
				var columnName:String = xml.@columnName;
				
				if(columnsAliasStr == ""){
					columnsAliasStr = attributeName+" "+columnName;
				}else{
					columnsAliasStr = columnsAliasStr +","+attributeName+" "+columnName;
				}
				
				if(columnsStr == ""){
					columnsStr = columnName;
				}else{
					columnsStr = columnsStr +","+columnName;
				}
				
			}
			
			if(columnsAliasStr == ""){
				columnsAliasStr = " * ";
			}
			if(columnsStr == "" ){
				columnsStr = " * ";
			}
			
			var tempStr:String = "SELECT "+columnsStr+" FROM "+tableName+" WHERE 1=1 ";
			
			var conditionStr:String = getAllCondition(entityXml);
			if(conditionStr != "" ){
				tempStr = tempStr + " "+ conditionStr
			}
			
			sqlStr = "SELECT "+columnsAliasStr+" FROM ("+tempStr+")";
			
			return sqlStr;
		}
		
		/**
		 * 解析过滤条件（只支持客户端环境变量的解析）
		 */
		public static function parseFilter(filter:String):String{
			filter = StringUtil.trim(filter);
			var lastIndex:int = 0;
			while(filter.indexOf("{",lastIndex) > -1 && filter.indexOf("}",lastIndex) > -1){
				var params:String = filter.substring(filter.indexOf("{",lastIndex)+1,filter.indexOf("}",lastIndex));
				var clientVal:Object = ClientEnviorment.getInstance().getVar(params);//客户端环境变量
				
				if(clientVal){
					if(!isNaN(Number(clientVal)))
						filter = filter.replace("{".concat(params).concat("}"),clientVal);
					else
						filter = filter.replace("{".concat(params).concat("}"),"'".concat(clientVal).concat("'"));
				}else
					return "";//参数解析失败
				
				lastIndex = filter.indexOf("{",lastIndex);
			}
			return filter;
		}
		
		/**
		 * 解析同义词属性
		 */
		public static function parseEntitySynonyms(dataProvider:ArrayCollection,attributes:XML):ArrayCollection{
			var synonymsMt:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet(GlobalConst.MTCODE_SYNONYMS);
			if(synonymsMt == null)
				throw new Error("解析绑定同义词字段错误 ,同义词元数据模板未找到！");
			var synonymsMtXml:XML = synonymsMt.contentXML;
			for each(var att:XML in attributes.attribute){
				if(String(att.@category) == BAMConst.ENTITY_ATT_CATEGORY_DICT){
					var name:String = String(att.@name).toLowerCase();
					if(synonymsMtXml.synonyms.(@code==String(att.@dictionaryRefCode)).length() == 0)
						continue;
					var synonyms:XML = synonymsMtXml.synonyms.(@code==String(att.@dictionaryRefCode))[0] ;
					for each(var data:Object in dataProvider){
						for each(var variable:XML in synonyms.variable){
							if(String(variable.@value) == String(data[name]))
								data[name] = String(variable.@name);
						}
					}
				}
			}
			return dataProvider;
		}
		
		/**
		 * 解析同义词字段,同义词在OLAP图中将重新
		 */
		public static function getDictColumnStr(attribute:XML):String{
			var columnStr:String = "";
			var synonymsMt:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet(GlobalConst.MTCODE_SYNONYMS);
			if(synonymsMt == null)
				throw new Error("解析绑定同义词字段错误 ,同义词元数据模板未找到！");
			var synonymsMtXml:XML = synonymsMt.contentXML;
			var name:String = String(attribute.@name).toLowerCase();
			if(String(attribute.@category) == BAMConst.ENTITY_ATT_CATEGORY_DICT){
				var refCode:String = String(attribute.@dictionaryRefCode);
				for each(var synonymsXml:XML in synonymsMtXml.synonyms){
					var tempCode:String = String(synonymsXml.@code);
					if(!BAMUtil.isEmpty(refCode)&&tempCode==refCode){
						for each(var variable:XML in synonymsXml.variable){
							var value:String = String(variable.@value);
							var label:String = String(variable.@name);
							if(BAMUtil.isEmpty(columnStr)){
								columnStr = "'"+value+"','"+label+"'";
							}else{
								columnStr = columnStr+",'"+value+"','"+label+"'";
							}
						}
						break;
					}
				}
			}
			if(!BAMUtil.isEmpty(columnStr)){
				columnStr = " decode("+name+","+columnStr+","+name+"||'') "+name+" ";
			}else{
				columnStr = name;
			}
			return columnStr;
		}
		
		/**
		 * 解析实体中的日期属性
		 */ 
		public static function parseEntityDateAtt(dataProvider:ArrayCollection,attributes:XML):ArrayCollection{
			for each(var att:XML in attributes.attribute){
				if(String(att.@category) == BAMConst.ENTITY_ATT_CATEGORY_NORMAL && String(att.@type) == 'DATE'){
					var name:String = String(att.@name).toLowerCase();
					for each(var data:Object in dataProvider){
						if(data[name] is Date)
							data[name] = StringUtils.convertDateToString(data[name],1);
					}
				}
			}
			return dataProvider;
		}
		
		/**
		 * 获取该实体的所有父实体属性 
		 * @param isDisplay 是否获取显示属性
		 */
		public static function getInheritAttributes(entityXml:XML,entitiesXml:XML,isDisplay:Boolean=false):ArrayCollection{
			var allInheritAtt:ArrayCollection = new ArrayCollection();
			var allInheritAttMap:Hashtable = new Hashtable();
			var allInheritEntity:ArrayCollection = getAllInheritEntity(entityXml,entitiesXml);
			for each(var entity:XML in allInheritEntity){
				var attributes:XMLList = isDisplay?entity.attributes.displayAttributes.attribute:entity.attributes.attribute;
				for each(var att:XML in attributes){//将继承属性做一个封装，加上所属的实体编码
					if(allInheritAttMap.containsKey(String(att.@name)))
						continue;
					var attCopy:XML = att.copy();
					attCopy.@[BAMConst.ENTITY_DIS_ATT_PARENT_ENTITYCODE] = entity.@code;
					allInheritAtt.addItem(attCopy);
					allInheritAttMap.add(String(attCopy.@name),attCopy);
				}
			}
			return allInheritAtt;
		}
		
		/**
		 * 获取所有继承关系
		 **/ 
		public static function getAllInheritRelation(entityXml:XML,entitiesXml:XML):ArrayCollection{
			var allInheritRelation:ArrayCollection = new ArrayCollection();
			var allInheritEntity:ArrayCollection = getAllInheritEntity(entityXml,entitiesXml);
			for each(var inheritEntity:XML in allInheritEntity){
				for each(var realtion:XML in inheritEntity.relations.relation){
					allInheritRelation.addItem(realtion);
				}
			}
			return allInheritRelation;
		}
		
		/**
		 * 获取所有继承实体 (非抽象实体)
		 */
		public static function getAllInheritEntity(entityXml:XML,entitiesXml:XML):ArrayCollection{
			var allInheritEntity:ArrayCollection = new ArrayCollection();
			recursiveAllInheritEntity(String(entityXml.@parentEntityCode),entitiesXml,allInheritEntity);
			allInheritEntity = new ArrayCollection(allInheritEntity.toArray().reverse());//倒序排列
			return allInheritEntity;
		}
		
		/**
		 * 递归所有的实体
		 */ 
		private static function recursiveAllInheritEntity(parentEntityCode:String,entitiesXml:XML,allInheritEntity:ArrayCollection):void{
			var entitiesList:XMLList = entitiesXml.entities.entity.(@code==parentEntityCode);
			if(entitiesList.length() > 0 && String(entitiesList[0].@isAbstract) == "false"){
				allInheritEntity.addItem(entitiesList[0]);
				recursiveAllInheritEntity(String(entitiesList.@parentEntityCode),entitiesXml,allInheritEntity);
			}
		}
		
		/**
		 * 封装显示属性 
		 */
		public static function displayAttributeWrap(entityXml:XML,entities:XML):XML{
			if(entityXml.attributes.length() == 0)
				return new XML();
			var displayAttXml:XML = <root />;
			
			var wrapEntities:XML = <root><entities /></root>;
			for each(var e:XML in entities.entities.entity){
				if(String(e.@mtcode) != null && String(e.@mtcode) != ""){
					var mt:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet(String(e.@mtcode));
					mt == null?wrapEntities.entities.appendChild(e):wrapEntities.entities.appendChild(mt.contentXML);
				}else
					wrapEntities.entities.appendChild(e);
			}
			
			var allInheritAtt:ArrayCollection = getInheritAttributes(entityXml,wrapEntities);
			for each(var parentAtt:XML in allInheritAtt){
				var parentAttTemp:XML = parentAtt.copy();
				displayAttXml.appendChild(parentAttTemp);
			}
			for each(var att:XML in entityXml.attributes.attribute){
				var attTemp:XML = att.copy();
				displayAttXml.appendChild(attTemp);
			}
			var displayAttributesWrap:XML = <root />;
			displayAttributesWrap.@defaultLayout = String(entityXml.attributes.displayAttributes.@defaultLayout);
			var displayAtts:XMLList = entityXml.attributes.displayAttributes.attribute;
			for each(var displayAtt:XML in displayAtts){
				var displayAttsXml:XMLList = displayAttXml.attribute.(@name == displayAtt.@name);
				if(displayAttsXml.length() > 0){
					displayAttsXml[0].@label = displayAtt.@label;
					displayAttsXml[0].@isQuickQuery = displayAtt.@isQuickQuery;
					//如果显示属性中包含了实体引用的LABEL字段，则使用LABEL字段做显示
					if(String(displayAttsXml[0].@[BAMConst.ENTITY_ATT_ENTITY_DISPLAY]) != ""){
						displayAttsXml[0].@name = displayAttsXml[0].@[BAMConst.ENTITY_ATT_ENTITY_DISPLAY];
					}
					displayAttributesWrap.appendChild(displayAttsXml[0]);
				}
			}
			if(entityXml.attributes.displayAttributes.groupLayoutInfo.length()>0){
				displayAttributesWrap.appendChild(entityXml.attributes.displayAttributes.groupLayoutInfo.copy());
			}
			
			return displayAttributesWrap;
		}
		
		/**
		 * 获取新的实体名称
		 **/ 
		public static function getNewEntityCode(entitySrc:XML,entities:XMLList):void{
			for each(var entity:XML in entities){
				if(String(entity.@code) == String(entitySrc.@code)){
					entitySrc.@code = String(entitySrc.@code).concat("_1");
					getNewEntityCode(entitySrc,entities);
				}
			}
		}
		
		/**
		 * 获取所有属性中（包括继承属性）的实体引用的实体
		 **/ 
		public static function getAllRefEntities(allAttributes:ArrayCollection,entireEntityXml:XML):ArrayCollection{
			var allRefEntities:ArrayCollection = new ArrayCollection();
			//如果当前所有属性中（包括继承属性）有实体引用的，还需要将被引用的实体和其子实体封装在XML中
			if(allAttributes.length > 0){
				for each(var refEntityAtt:XML in allAttributes){
					if(String(refEntityAtt.@category) != BAMConst.ENTITY_ATT_CATEGORY_ENTITY)
						continue;
					
					var refEntityCode:String = String(refEntityAtt.@refEntity);
					var refEntity:XML = entireEntityXml.entities.entity.(@code == refEntityCode).length() == 0?null
											:entireEntityXml.entities.entity.(@code == refEntityCode)[0];
					if(refEntity == null)
						continue;
					
					var refEntities:ArrayCollection = EntityUtil.getAllInheritEntity(refEntity,entireEntityXml);
					refEntities.addItem(refEntity);
					
					allRefEntities.addAll(refEntities);
				}
			}
			
			return allRefEntities;
		}
		
	}
}