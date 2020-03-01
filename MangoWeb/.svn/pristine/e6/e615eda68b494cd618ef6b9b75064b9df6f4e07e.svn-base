package smartx.bam.flex.modules.entitymodel.listener
{
	import mx.containers.TitleWindow;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	
	import net.brandonmeyer.containers.SuperPanel;
	
	import smartx.bam.flex.modules.entitymodel.DQEntityModelDesigner;
	import smartx.bam.flex.modules.gis.GisMap;
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.StyleTemplate02;
	import smartx.flex.components.styletemplate.ifc.CardButtonListener;
	import smartx.flex.components.util.MemoryUtil;
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.vo.MetadataTemplet;
	
	public class DQEmDesignCardButtonListener implements CardButtonListener
	{
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var dataValue:Object; 
		
		private var entityModelDesigner:DQEntityModelDesigner;
		
		private var bcp:BillCardPanel;
		
		private var styleTemplet02:StyleTemplate02;
		
		public function DQEmDesignCardButtonListener(){}
		
		public function buttonClick(cardPanel:BillCardPanel):void{
			bcp = cardPanel;
//			test();
			dataValue = cardPanel.getDataValue();
			if(dataValue["DATASOURCE"] == null){
				SmartXMessage.show("请选择数据源!",SmartXMessage.MESSAGE_ERROR);
				return;
			}
			if(dataValue){
				entityModelDesigner = new DQEntityModelDesigner();
				entityModelDesigner.contentXml = dataValue["CONTENT"];
				entityModelDesigner.confirmFun = confirm;
				entityModelDesigner.saveFun = save;
				entityModelDesigner.saveNoAlertFun = saveNoAlert;
				entityModelDesigner.entityModelCode = dataValue["CODE"];
				entityModelDesigner.datasourceName = dataValue["DATASOURCE"].id;
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
			dataValue = bcp.getDataValue();
			if(entityModelDesigner)
				dataValue["CONTENT"] = entityModelDesigner.contentXml;
			bcp.isShowAlert =  true;
			bcp.setDataValue(dataValue);
			styleTemplet02?styleTemplet02.onSave():bcp.save();
		}
		
		private function saveNoAlert():void{
			dataValue = bcp.getDataValue();
			if(entityModelDesigner)
				dataValue["CONTENT"] = entityModelDesigner.contentXml;
			bcp.isShowAlert =  false;
			bcp.setDataValue(dataValue);
			styleTemplet02?styleTemplet02.onSave():bcp.save();
		}
		
		protected function test():void{
			
			var metadataTemplet:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet("MT_GIS_APP_test");
			
			if(metadataTemplet == null){
				SmartXMessage.show("无法打开GIS应用！");
				return;
			}
			
			var gisApp:XML = metadataTemplet.contentXML;
			
			var entityModelCode:String = gisApp.@entityModelCode;
			//查找领域实体模型元数据
			var entityModelMt:MetadataTemplet =  MetadataTempletUtil.getInstance()
				.findMetadataTemplet(BAMConst.ENTITY_MODEL_MT_PREFIX.concat(entityModelCode));
			if(entityModelMt == null){
				SmartXMessage.show("无法打开GIS应用！");
				return;
			}
			
			var gisMap:GisMap = new GisMap();
			gisMap.mapUrl = String(entityModelMt.contentXML.geoServer.@url);
			
			var basicLayers:Array = [];
			var entityLayers:Array = [];
			for each(var basicLayer:XML in gisApp.mapInfo.layers.basicLayers.layer){
				if(String(basicLayer.@isDisplay) == "true")
					basicLayers.push(basicLayer);
			}
			for each(var entityLayer:XML in gisApp.mapInfo.layers.entityLayers.layer){
				if(String(entityLayer.@isDisplay) == "true")
					entityLayers.push(entityLayer);
			}
			gisMap.basicLayers = basicLayers;
			gisMap.entityLayers = entityLayers;
			gisMap.centerPoint = String(gisApp.mapInfo.centerPoint);
			gisMap.zoom = String(gisApp.mapInfo.zoom);
			gisMap.srid = String(gisApp.@srid);
			gisMap.contentXml = gisApp;
			
			var superPanel:SuperPanel = new SuperPanel();
			superPanel.allowClose = true;
			superPanel.allowResize = true;
			superPanel.title = "GIS应用";
			superPanel.addEventListener(CloseEvent.CLOSE,function(event:CloseEvent):void{
				PopUpManager.removePopUp(superPanel);
			});
			
			PopUpManager.addPopUp(superPanel,bcp.root,true);
			
			superPanel.maximize();
			
			superPanel.addChild(gisMap);
		}
		
	}
}