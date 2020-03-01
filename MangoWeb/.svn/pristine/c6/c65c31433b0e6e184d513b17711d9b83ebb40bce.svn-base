package smartx.bam.flex.modules.dqc.listener
{
	import flash.display.DisplayObject;
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	import mx.containers.VBox;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.core.UIComponent;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	import mx.utils.ObjectUtil;
	
	import smartx.bam.flex.modules.common.variableControl.vo.VariableVo;
	import smartx.bam.flex.modules.dqc.EntityOverviewPanel;
	import smartx.bam.flex.modules.dqc.EntityRelationListPanel;
	import smartx.bam.flex.modules.entitymodel.listener.EmTreeItemClickListener;
	import smartx.bam.flex.modules.entitymodel.utils.EntityModelListPanel;
	import smartx.bam.flex.modules.entitymodel.utils.EntityTempletUtil;
	import smartx.bam.flex.modules.entitymodel.utils.EntityUtil;
	import smartx.bam.flex.utils.BAMUtil;
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.chart.AdvListChart;
	import smartx.flex.components.util.LoadingWindow;
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.util.OperatingTipUtil;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.vo.MetadataTemplet;
	
	public class ChkAndDoEmTreeItemClickListener implements EmTreeItemClickListener
	{
		private var rpc:RemoteObject;
		
		private var endpoint:String = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		
		private var entityOverviewPanel:EntityOverviewPanel = new EntityOverviewPanel();
		
		private var entityXml:XML;
		
		private var entireEntityXml:XML;
		
		private var modelCode:String;
		
		private var localParent:UIComponent;
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var datasource:String;
		
		private var sql:String;
		
		public function ChkAndDoEmTreeItemClickListener(){
			rpc = new RemoteObject(BAMConst.BAM_Service);
			if(endpoint)
				rpc.endpoint = endpoint;
			rpc.generateDisplayEntitySqlContainCondition.addEventListener(ResultEvent.RESULT,generateEntitySqlHandler);
			rpc.generateDisplayEntitySqlContainCondition.addEventListener(FaultEvent.FAULT,function(event:FaultEvent):void{
				SmartXMessage.show("生成SQL错误!",SmartXMessage.MESSAGE_ERROR,event.fault.faultString);
			});
			
			rpc.generateEntitySqlOriginalData.addEventListener(ResultEvent.RESULT,generateEntitySqlOriginalDataHandler);
			rpc.generateEntitySqlOriginalData.addEventListener(FaultEvent.FAULT,function(event:FaultEvent):void{
				SmartXMessage.show("生成SQL错误!",SmartXMessage.MESSAGE_ERROR,event.fault.faultString);
			});
			
			rpc.getMetadataTempletContent.addEventListener(ResultEvent.RESULT,getMetadataTempletContentHandler);
			rpc.getMetadataTempletContent.addEventListener(FaultEvent.FAULT,function(event:FaultEvent):void{
				SmartXMessage.show("获取元数据时出错",SmartXMessage.MESSAGE_ERROR,"错误详情:"+event.fault.faultString);
				invokeMethodIndex++;
				if(invokeMethodSum == invokeMethodIndex)
					OperatingTipUtil.endOperat();
			});
		}
		
		private var invokeMethodSum:Number = 0;
		
		private var invokeMethodIndex:Number = 0;
		
		private var allEntityMetadata:XML = <root><entities /></root>;
		
		private function getMetadataTempletContentHandler(event:ResultEvent):void{
			var metadataTemplet:MetadataTemplet = event.result as MetadataTemplet;
			var currentEntityXml:XML = metadataTemplet.contentXML;
			allEntityMetadata.entities[0].appendChild(currentEntityXml);
			invokeMethodIndex++;
			if(invokeMethodIndex == invokeMethodSum){
				var allAttributes:ArrayCollection = new ArrayCollection();
				var allInheritAtts:ArrayCollection = EntityUtil.getInheritAttributes(currentEntityXml,allEntityMetadata);
				allAttributes.addAll(allInheritAtts);
				
				for each(var att:XML in currentEntityXml.attributes.attribute){
					allAttributes.addItem(att);
				}
				//获得所有的实体引用的实体
				var allRefEntities:ArrayCollection = EntityUtil.getAllRefEntities(allAttributes,entireEntityXml);
				for each(var refEntityXml:XML in allRefEntities){
					//判断在引用实体是否已存在
					if(allEntityMetadata.entities.entity.(@code == String(refEntityXml.@code)).length() > 0)
						continue;
					var entityMetadataTemplet:MetadataTemplet = MetadataTempletUtil.getInstance()
						.findMetadataTemplet(String(refEntityXml.@mtcode));
					if(entityMetadataTemplet && entityMetadataTemplet.contentXML)
						allEntityMetadata.entities.appendChild(entityMetadataTemplet.contentXML);
				}
				
				entireEntityXml = allEntityMetadata;
				entityXml = currentEntityXml;
				trace(allEntityMetadata.toXMLString());
				var condition:String = getFilter(currentEntityXml);
				if(!BAMUtil.isEmpty(condition)){
					condition = " where "+condition;
				}
				rpc.generateDisplayEntitySqlContainCondition(currentEntityXml.toXMLString(),allEntityMetadata.toXMLString(),null,condition);
				OperatingTipUtil.endOperat();
			}
		}
		
		/**
		 * 加载实体元数据（包括父实体）
		 **/ 
		private function loadEntityMetadata(entitiesArr:ArrayCollection):void{
			OperatingTipUtil.startOperat("加载实体中...",desktop);
			invokeMethodSum = entitiesArr.length;
			invokeMethodIndex = 0;
			delete allEntityMetadata.entities.*;
			for each(var entityXml:XML in entitiesArr){
				rpc.getMetadataTempletContent(String(entityXml.@mtcode));
			}
		}
		
		public function itemClick(item:Object, parent:UIComponent):void{
			if(item is XML){
				entityXml = XML(item);
				var entityModelMtCode:String = String(item.@entityModelMtCode);
				if(entityModelMtCode == "")
					return;//没有领域实体元数据的话，继续也没意义，直接回去吧
				
				entireEntityXml = MetadataTempletUtil.getInstance().findMetadataTemplet(entityModelMtCode).contentXML;
				entityXml.@parentEntityCode = entireEntityXml.entities.entity.(@code == String(entityXml.@code)).@parentEntityCode;
				modelCode = item.@entityModelCode;
			}else{
				entityXml = XML(item.content);
				entireEntityXml = XML(item.entireContent);
				modelCode = String(entireEntityXml.entityModelCode);
			}
			datasource = entireEntityXml.datasource;
			localParent = parent;
			
			if((String(entityXml.@type) == "entity" && String(entityXml.@isAbstract) == "false") 
				|| String(entityXml.@type) == "virtual"){//实体和虚拟实体
				if(String(entityXml.@mtcode) == "")
					rpc.generateEntitySql(entityXml.toXMLString(),entireEntityXml.toXMLString(),null);
				else{
					var entitiesArr:ArrayCollection = new ArrayCollection();
					entitiesArr.addAll(EntityUtil.getAllInheritEntity(entityXml,entireEntityXml));//加入当前实体所有的父实体
					entitiesArr.addItem(entityXml);//加入当前实体
					
					loadEntityMetadata(entitiesArr);
				}
			}else
				entityOverviewPanel.visible = false;
		}
		
		private function generateEntitySqlHandler(event:ResultEvent):void{
			
			sql = event.result as String;
			
			rpc.generateEntitySqlOriginalData(entityXml.toXMLString(),entireEntityXml.toXMLString(),null);
			
//			var vbox:VBox = new VBox();
//			vbox.percentHeight = 100;
//			vbox.percentWidth = 100;
//			
//			var relationList:EntityRelationListPanel = new EntityRelationListPanel();
//			relationList.modelXml = entireEntityXml;
//			relationList.modelCode = this.modelCode;
//			relationList.relationsXml = entityXml.relations[0];
//			relationList.datasource = this.datasource;
			
			
//			entityOverviewPanel = new EntityOverviewPanel();
////			entityOverviewPanel.maxHeight = 500;
//			if(entityOverviewPanel.endpoint == null)
//				entityOverviewPanel.endpoint = endpoint;
//			while(localParent.numChildren>0){
//				localParent.removeChildAt(0);
//			}
////			vbox.addChildAt(entityOverviewPanel,0);
////			vbox.addChildAt(relationList,1);
//			localParent.addChild(entityOverviewPanel);
//			entityOverviewPanel.visible = true;
//			var sharedObjectCode:String = String(entireEntityXml.entityModelCode)+"_"+String(entityXml.@code);
//			var displayAtts:XML = EntityUtil.displayAttributeWrap(entityXml,entireEntityXml);
//			entityOverviewPanel.attributesXml = String(entityXml.@type) == "virtual"?entityXml.attributes[0]:displayAtts;
//			var filedNameMap:Object = {};
//			for each(var att:XML in entityOverviewPanel.attributesXml.attribute){
//				filedNameMap[String(att.@name)] = String(att.@label);
//				if(String(att.@type) == "DATE"){//如果是DATE类型
//					filedNameMap[String(att.@name).concat("_YEAR")] = String(att.@label).concat("（年）");
//					filedNameMap[String(att.@name).concat("_MONTH")] = String(att.@label).concat("（月）");
//					filedNameMap[String(att.@name).concat("_DAY")] = String(att.@label).concat("（日）");
//				}
//			}
//			entityOverviewPanel.fieldNameMap = filedNameMap;
//			entityOverviewPanel.sharedObjectCode = sharedObjectCode;
//			//添加过滤条件
////			var filter:String = entityXml.filter;
//			var dimensionAttribute:String = entityXml.@dimensionAttribute;
////			var filter:String = this.getFilter(entityXml);
////			if(filter != null && filter != ""){
////				filter = EntityUtil.parseFilter(filter);
////				if(filter != "")
////					sql = sql.concat("and ").concat(dimensionAttribute).concat("=").concat(filter);
////				sql = sql.concat(filter);
////			}
//			entityOverviewPanel.sql = sql;
//			entityOverviewPanel.datasource = datasource;
//			entityOverviewPanel.olapTitle = String(entityXml.@name);
//			if(entityOverviewPanel.listPanel.isCreationComplete)
//				entityOverviewPanel.refresh();
//			//所有的继承实体
//			var allInheritEntity:ArrayCollection = EntityUtil.getAllInheritEntity(entityXml,entireEntityXml);
//			var templetCodeArr:Array = [];
//			//查找默认编辑方案的TEMPLETCODE(包括继承实体)
//			if(entityXml.editors.editor.length() > 0 && entityXml.editors.editor.(@isDefault=="true").length() > 0){
//				var isInvalidTempletCodeArr:Boolean = false;//是否是无效的实体编辑方案组
//				for each(var inheritEntity:XML in allInheritEntity){
//					if(inheritEntity.editors.editor.length() > 0 && inheritEntity.editors.editor.(@isDefault=="true").length() > 0)
//						templetCodeArr.push(String(inheritEntity.editors.editor.(@isDefault=="true")[0].@templetCode));
//					else{//如果继承属性没有默认的编辑方案则不允许对该实体进行编辑
//						isInvalidTempletCodeArr = true;
//						break;
//					}
//				}
//				if(!isInvalidTempletCodeArr){//如果有效则将当前实体的编辑方案添加进方案组，并生产元原模板
//					var currentTempletCode:String = entityXml.editors.editor.(@isDefault=="true")[0].@templetCode;
//					templetCodeArr.push(currentTempletCode);
//				}else
//					templetCodeArr = null;
//			}else
//				templetCodeArr = null;
//			entityOverviewPanel.templetCodeArr = templetCodeArr;
//			entityOverviewPanel.entityXml = entityXml;
//			entityOverviewPanel.contentXml = entireEntityXml;
//			entityOverviewPanel.modelCode = this.modelCode;
//			entityOverviewPanel.relationsXml = entityXml.relations[0];
//			
//			var deskTopFrame:DeskTopFrame = getDeskTopFrame(localParent) as DeskTopFrame;
//			if(deskTopFrame != null ){
//				deskTopFrame.verticalScrollPosition = 0;
//			}
		}
		
		private function generateEntitySqlOriginalDataHandler(event:ResultEvent):void{
			
			var countSQL:String = event.result as String;
			
			entityOverviewPanel = new EntityOverviewPanel();
			if(entityOverviewPanel.endpoint == null)
				entityOverviewPanel.endpoint = endpoint;
			while(localParent.numChildren>0){
				localParent.removeChildAt(0);
			}
			localParent.addChild(entityOverviewPanel);
			entityOverviewPanel.visible = true;
			var sharedObjectCode:String = String(entireEntityXml.entityModelCode)+"_"+String(entityXml.@code);
			var displayAtts:XML = EntityUtil.displayAttributeWrap(entityXml,entireEntityXml);
			entityOverviewPanel.attributesXml = String(entityXml.@type) == "virtual"?entityXml.attributes[0]:displayAtts;
			var filedNameMap:Object = {};
			for each(var att:XML in entityOverviewPanel.attributesXml.attribute){
				filedNameMap[String(att.@name)] = String(att.@label);
				if(String(att.@type) == "DATE"){//如果是DATE类型
					filedNameMap[String(att.@name).concat("_YEAR")] = String(att.@label).concat("（年）");
					filedNameMap[String(att.@name).concat("_MONTH")] = String(att.@label).concat("（月）");
					filedNameMap[String(att.@name).concat("_DAY")] = String(att.@label).concat("（日）");
				}
			}
			entityOverviewPanel.fieldNameMap = filedNameMap;
			entityOverviewPanel.sharedObjectCode = sharedObjectCode;
			//添加过滤条件
			var dimensionAttribute:String = entityXml.@dimensionAttribute;
			entityOverviewPanel.sql = sql;
			entityOverviewPanel.countSQL = countSQL;
			entityOverviewPanel.datasource = datasource;
			entityOverviewPanel.olapTitle = String(entityXml.@name);
			if(entityOverviewPanel.listPanel.isCreationComplete)
				entityOverviewPanel.refresh();
			//所有的继承实体
			var allInheritEntity:ArrayCollection = EntityUtil.getAllInheritEntity(entityXml,entireEntityXml);
			var templetCodeArr:Array = [];
			//查找默认编辑方案的TEMPLETCODE(包括继承实体)
			if(entityXml.editors.editor.length() > 0 && entityXml.editors.editor.(@isDefault=="true").length() > 0){
				var isInvalidTempletCodeArr:Boolean = false;//是否是无效的实体编辑方案组
				for each(var inheritEntity:XML in allInheritEntity){
					if(inheritEntity.editors.editor.length() > 0 && inheritEntity.editors.editor.(@isDefault=="true").length() > 0)
						templetCodeArr.push(String(inheritEntity.editors.editor.(@isDefault=="true")[0].@templetCode));
					else{//如果继承属性没有默认的编辑方案则不允许对该实体进行编辑
						isInvalidTempletCodeArr = true;
						break;
					}
				}
				if(!isInvalidTempletCodeArr){//如果有效则将当前实体的编辑方案添加进方案组，并生产元原模板
					var currentTempletCode:String = entityXml.editors.editor.(@isDefault=="true")[0].@templetCode;
					templetCodeArr.push(currentTempletCode);
				}else
					templetCodeArr = null;
			}else
				templetCodeArr = null;
			entityOverviewPanel.templetCodeArr = templetCodeArr;
			entityOverviewPanel.entityXml = entityXml;
			entityOverviewPanel.contentXml = entireEntityXml;
			entityOverviewPanel.modelCode = this.modelCode;
			entityOverviewPanel.relationsXml = entityXml.relations[0];
			
			var deskTopFrame:DeskTopFrame = getDeskTopFrame(localParent) as DeskTopFrame;
			if(deskTopFrame != null ){
				deskTopFrame.verticalScrollPosition = 0;
			}
		}
		
		private function getFilter(entityXml:XML):String{
			var filterStr:String = "";
			var valueMap:Object = new Object();
			if(entityXml != null ){
				for each(var itemXml:XML in entityXml.groupAttributes.groupAttribute){
					var name:String = String(itemXml.@name);
					var parameter:String = String(itemXml.@parameter);
					var parameterValue:String = "";
					if(!BAMUtil.isEmpty(parameter)){
						var tempValue:Object = ClientEnviorment.getInstance().getVar(parameter);
						if(tempValue != null){
							parameterValue = tempValue as String;
						}
					}
					valueMap[name] = parameterValue;
				}
				
				var objInfo:Object = ObjectUtil.getClassInfo(valueMap); 
				var fieldName:Array = objInfo["properties"] as Array; 
				for each(var q:QName in fieldName){ 
					var key:String = q.localName;
					var value:String = valueMap[key] as String;
					if(!BAMUtil.isEmpty(value)&& value != null && value != "" && value != "-1" && value.toLowerCase() != 'all' && value != "全部" && value != VariableVo.IGNORE_VALUE){
						if(BAMUtil.isEmpty(filterStr)){
							filterStr = key+"='"+value+"'";
						}else{
							filterStr = filterStr + " AND "+key+"='"+value+"'";
						}
					}
				}
				
			}
			return filterStr;
		}
		
		private function getDeskTopFrame(currentContener:DisplayObject):DisplayObject{
			var display:DisplayObject = null;
			var tempDisplay:DisplayObject = currentContener.parent;
			if(tempDisplay is DeskTopFrame){
				display = tempDisplay;
			}else{
				display = getDeskTopFrame(tempDisplay);
			}
			return  display;
		}
		
	}
}