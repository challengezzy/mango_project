package smartx.bam.flex.modules.entitymodel.listener
{
	import mx.managers.PopUpManager;
	
	import smartx.bam.flex.modules.entitymodel.EntityModelDesigner;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.ifc.CardButtonListener;
	import smartx.flex.components.util.MemoryUtil;
	import smartx.flex.components.util.SmartXMessage;
	
	public class EmDesignCardButtonListener implements CardButtonListener
	{
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var dataValue:Object; 
		
		private var entityModelDesigner:EntityModelDesigner;
		
		private var bcp:BillCardPanel;
		
		public function EmDesignCardButtonListener(){}
		
		public function buttonClick(cardPanel:BillCardPanel):void{
			bcp = cardPanel;
			dataValue = cardPanel.getDataValue();
			if(dataValue["DATASOURCE"] == null){
				SmartXMessage.show("请选择数据源!",SmartXMessage.MESSAGE_ERROR);
				return;
			}
			if(dataValue["DWDS"] == null ){
				SmartXMessage.show("请选择维度数据源!",SmartXMessage.MESSAGE_ERROR);
				return;
			}
			if(dataValue){
				entityModelDesigner = new EntityModelDesigner();
				entityModelDesigner.contentXml = dataValue["CONTENT"];
				entityModelDesigner.confirmFun = confirm;
				entityModelDesigner.saveFun = save;
				entityModelDesigner.saveNoAlertFun = saveNoAlert;
				entityModelDesigner.entityModelCode = dataValue["CODE"];
				entityModelDesigner.datasourceName = dataValue["DATASOURCE"].id;
				entityModelDesigner.dwDsName = dataValue["DWDS"].id;
				entityModelDesigner.insertMode = bcp.insertMode;
				PopUpManager.addPopUp(entityModelDesigner,desktop,true);
				PopUpManager.centerPopUp(entityModelDesigner);
				entityModelDesigner.maximize();
			}
		}
		
		private function confirm():void{
			dataValue["CONTENT"] = entityModelDesigner.contentXml;
			bcp.setDataValue(dataValue);
			PopUpManager.removePopUp(entityModelDesigner);
			entityModelDesigner.confirmFun = null;
			entityModelDesigner = null;
			MemoryUtil.forceGC();
		}
		
		private function save():void{
			dataValue["CONTENT"] = entityModelDesigner.contentXml;
			bcp.isShowAlert =  true;
			bcp.setDataValue(dataValue);
			bcp.save();
		}
		
		private function saveNoAlert():void{
			dataValue["CONTENT"] = entityModelDesigner.contentXml;
			bcp.isShowAlert =  false;
			bcp.setDataValue(dataValue);
			bcp.save();
		}
		
	}
}