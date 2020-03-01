package smartx.flex.modules.basic.system.mtdesigner
{
	import flash.utils.getDefinitionByName;
	
	import mx.controls.Alert;
	import mx.managers.PopUpManager;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.event.MTDesignerEvent;
	import smartx.flex.components.mtdesigner.MTDesigner;
	import smartx.flex.components.mtdesigner.MTDesignerUtils;
	import smartx.flex.components.styletemplate.ifc.CardButtonListener;
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.MetadataTemplet;
	
	public class DesignButtonListener implements CardButtonListener
	{
		private var thisCardPanel:BillCardPanel;
		
		private var deskTop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		public function DesignButtonListener()
		{
		}
		
		public function buttonClick(cardPanel:BillCardPanel):void 
		{
			try{
			thisCardPanel = cardPanel;
			var mttype:String = cardPanel.getDataValue()["TYPE"].code;
			var mtdesigner:MTDesigner = MTDesignerUtils.findMTDesigner(mttype);
			var content:String = cardPanel.getDataValue()["CONTENT"] as String;
			var contentXML:XML = XML(content);
			mtdesigner.sourceXML = contentXML;
			PopUpManager.addPopUp(mtdesigner,deskTop,true);
			PopUpManager.centerPopUp(mtdesigner);
			mtdesigner.initData();
			mtdesigner.addEventListener(MTDesignerEvent.DESIGN_END,function(event:MTDesignerEvent):void{
				thisCardPanel.getDataValue()["CONTENT"] = event.sourceXML.toXMLString();
				thisCardPanel.setDataValue(thisCardPanel.getDataValue());
			});
			}catch(e:TypeError)
			{
				//Alert.show(""+e.message);	
				SmartXMessage.show(e.message);
			}
		}
	}
}