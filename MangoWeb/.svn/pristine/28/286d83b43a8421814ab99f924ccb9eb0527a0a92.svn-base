package smartx.bam.flex.modules.entitymodel.listener
{
	import mx.managers.PopUpManager;
	
	import smartx.bam.flex.modules.entitymodel.DQEntityRuleDesigner;
	import smartx.bam.flex.utils.BAMUtil;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.ifc.CardButtonListener;
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.vo.MetadataTemplet;
	import smartx.flex.components.vo.SimpleRefItemVO;
	
	public class DQEmRuleDesignCardButtonListener implements CardButtonListener
	{
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var endpoint:String = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		
		private var dataValue:Object; 
		
		private var bcp:BillCardPanel;
		
		private var dQEntityRuleDesigner:DQEntityRuleDesigner;
		
		public function DQEmRuleDesignCardButtonListener()
		{
		}
		
		public function buttonClick(cardPanel:BillCardPanel):void
		{
			bcp = cardPanel;
			dataValue = cardPanel.getDataValue();
			if(dataValue["ENTITYMODELCODE"] == null){
				SmartXMessage.show("请选选择一个模型!",SmartXMessage.MESSAGE_ERROR);
				return;
			}
			if(dataValue){
				
				dQEntityRuleDesigner = new DQEntityRuleDesigner();
				dQEntityRuleDesigner.modelCode = dataValue["ENTITYMODELCODE"].id;
				dQEntityRuleDesigner.entityCode = dataValue["ENTITYCODE"];
				dQEntityRuleDesigner.dataValue = dataValue;
				
				var mtCode:String = dataValue["MTCODE"];
//				if(!BAMUtil.isEmpty(mtCode) && mtCode.toLowerCase()!="null"){
//					var ruleTemplete:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet(mtCode);
//					if(ruleTemplete){
//						dQEntityRuleDesigner.ruleXml = ruleTemplete.contentXML;
//					}
//				}
				var ruleContent:String = dataValue["RULECONTENT"];
				if(ruleContent != null && ruleContent !="" && ruleContent.toLowerCase()!="null"){
					dQEntityRuleDesigner.ruleXml = new XML(ruleContent);
				}
				
				
				PopUpManager.addPopUp(dQEntityRuleDesigner,desktop,true);
				PopUpManager.centerPopUp(dQEntityRuleDesigner);
			}
		}
	}
}