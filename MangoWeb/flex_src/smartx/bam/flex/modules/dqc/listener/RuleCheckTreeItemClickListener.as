package smartx.bam.flex.modules.dqc.listener
{
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;
	
	import smartx.bam.flex.modules.dqc.EntityRuleCheckPanel;
	import smartx.bam.flex.modules.entitymodel.listener.EmTreeItemClickListener;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.cep.CEPUtils;
	import smartx.flex.components.event.MetadataTempletUtilEvent;
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.util.OperatingTipUtil;
	import smartx.flex.components.vo.MetadataTemplet;
	
	public class RuleCheckTreeItemClickListener implements EmTreeItemClickListener
	{
		
		private var endpoint:String = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		
		private var entityXml:XML;
		
		private var modelXml:XML;
		
		private var localParent:UIComponent;
		
		private var entityRuleCheckPanel:EntityRuleCheckPanel;
		
		private var entitiesCodeCol:ArrayCollection = new ArrayCollection();
		
		private var entitiesMtCodeCol:ArrayCollection = new ArrayCollection();
		
		private var modelCode:String;
		
		private var currentEntityCode:String;
		
		private var currentEntityMtCode:String;
		
		public function RuleCheckTreeItemClickListener()
		{
			
		}
		public function itemClick(item:Object, parent:UIComponent):void
		{
			MetadataTempletUtil.getInstance().addEventListener(MetadataTempletUtilEvent.FLUSH_MTCODE_COMPLETE,mtFlushMtcodeCompleteHandler);
			MetadataTempletUtil.getInstance().addEventListener(MetadataTempletUtilEvent.FLUSH_MTCODE_FAILED,mtFlushMtcodeFailedHandler);
			
			entitiesCodeCol.removeAll();
			entitiesMtCodeCol.removeAll();
			
			entityXml = XML(item.content);
			modelXml = XML(item.entireContent);
			localParent = parent;
			
			currentEntityCode = String(entityXml.@code);
			
			currentEntityMtCode = String(entityXml.@mtcode);
			
			modelCode = String(modelXml.entityModelCode);
			
			getAllEntityMtCode(modelXml);
			
			getParentEntityCode(entityXml,modelXml);
			
			OperatingTipUtil.startOperat("正在初始化...",parent);
			MetadataTempletUtil.getInstance().flushMetadataTempletByMtCodeArray(entitiesMtCodeCol.toArray(),endpoint);
			
		}
		
		private function getAllEntityMtCode(modelXml:XML):void{
				for each(var tempEntityXml:XML in modelXml.entities.entity){
					var tempEntityMtCode:String = String(tempEntityXml.@mtcode);
					if(tempEntityMtCode != null || tempEntityMtCode != ""){
						entitiesMtCodeCol.addItem(tempEntityMtCode);
					}
					
				}
		}
		
		private function getParentEntityCode(curentEntityXml:XML,modelXml:XML):void{
			
			var selectedEntityCode:String = String(curentEntityXml.@code);
			entitiesCodeCol.addItem(selectedEntityCode);
			
			var parentEntityCode:String = String(curentEntityXml.@parentEntityCode);
			if(parentEntityCode != null && parentEntityCode != ""){
				for each(var tempEntityXml:XML in modelXml.entities.entity){
					var tempEntityCode:String = String(tempEntityXml.@code);
					if(parentEntityCode == tempEntityCode){
						getParentEntityCode(tempEntityXml,modelXml);
					}
				}
			}else{
				return;
			}
			
		}
		
		private function mtFlushMtcodeCompleteHandler(event:MetadataTempletUtilEvent):void{
			OperatingTipUtil.endOperat();
			showRuleList();
		}
		
		private function mtFlushMtcodeFailedHandler(event:MetadataTempletUtilEvent):void{
			OperatingTipUtil.endOperat();
			showRuleList();
		}
		
		private function showRuleList():void{
			
			var initCondition:String = "";
			
			var currentEntityXml:XML = null;
			var tempModelXml:XML = modelXml.copy();
			
			entityRuleCheckPanel = new EntityRuleCheckPanel();
			
//			var tempEntityXml:XML = null;
//			var templete:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet(currentEntityMtCode);
//			if(templete != null){
//				tempEntityXml = templete.contentXML;
//			}
			var entitiesXml:XML = tempModelXml.entities[0];
			if(entitiesXml != null ){
				CEPUtils.xmlDeleteNode(entitiesXml);
			}
			tempModelXml.appendChild(new XML("<entities />"));
			for each(var tempMtCode:String in entitiesMtCodeCol){
				
				var templete:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet(tempMtCode);
				if(templete != null){
					var tempEntityXml:XML = templete.contentXML;
					if(tempMtCode == this.currentEntityMtCode){
						currentEntityXml = tempEntityXml;
					}
					tempModelXml.entities.appendChild(tempEntityXml);
				}
			}
			
			
			
			try
			{
				if(localParent.getChildAt(0) != null ){
					localParent.removeChildAt(0);
				}
			} 
			catch(error:Error) 
			{
				trace(error);
			}
			
			if(String(entityXml.@isAbstract) == "false" 
				//|| String(entityXml.@type) == "virtual"
															){
				
				if(!localParent.contains(entityRuleCheckPanel)){
					localParent.addChildAt(entityRuleCheckPanel,0);
				}
				
//				entityRuleCheckPanel.entityXml = currentEntityXml;
//				entityRuleCheckPanel.modelXml = tempModelXml;
				entityRuleCheckPanel.initCondition = getInitCondition(entitiesCodeCol);
			}
			
		}
		
		private function getInitCondition(arrayCol:ArrayCollection):String{
			var initCondition:String = " ENTITYMODELCODE='"+this.modelCode+"' ";
			var entityCodes:String = "";
			for each(var entityCode:String in arrayCol){
				if(entityCodes == ""){
					entityCodes = "'"+entityCode+"'";
				}else{
					entityCodes = entityCodes +",'"+entityCode+"'";
				}
				
			}
			if(entityCodes != ""){
				initCondition = initCondition+" AND ENTITYCODE in("+entityCodes+")";
			}
			return initCondition;
		}
	}
}