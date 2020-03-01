package smartx.bam.flex.modules.dataanalyze
{
	import flash.events.MouseEvent;
	
	import mx.containers.TitleWindow;
	import mx.controls.Alert;
	import mx.controls.LinkButton;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.bam.flex.modules.dashboard.DashboardLayoutPanel;
	import smartx.bam.flex.utils.BAMUtil;
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.assets.AssetsFileLib;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.event.BillCardPanelEvent;
	import smartx.flex.components.event.StyleTemplateEvent;
	import smartx.flex.components.styletemplate.ifc.CardButtonListener;
	import smartx.flex.components.util.MemoryUtil;
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.util.OperatingTipUtil;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.MetadataTemplet;
	
	public class DataAnalyzeDesigner implements CardButtonListener
	{
		
		private var endpoint:String = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var metaService:RemoteObject;
		
		private var layoutManager:DashboardLayoutPanel;
		
		private var mtCode:String;
		
		public function DataAnalyzeDesigner()
		{
			metaService = new RemoteObject(GlobalConst.SERVICE_METADATATEMPLET);
			metaService.endpoint = endpoint;
			metaService.findMetadataTempletNoCache.addEventListener(ResultEvent.RESULT,findMetadataTempletNoCacheHandler);
			metaService.findMetadataTempletNoCache.addEventListener(FaultEvent.FAULT,faultHander);
			
			metaService.updateMetadataTempletContent.addEventListener(ResultEvent.RESULT,updateMetadataTempletContentHandler);
			metaService.updateMetadataTempletContent.addEventListener(FaultEvent.FAULT,faultHander);
			
			
		}
		
		public function buttonClick(cardPanel:BillCardPanel):void
		{
			if(!cardPanel.insertMode){
				
				var dataObject:Object = cardPanel.getDataValue();
				if(dataObject != null ){
					var dbCode:String = dataObject["DASHBOARDCODE"];
					if(!BAMUtil.isEmpty(dbCode)){
						mtCode = "MT_LAYOUT_"+dbCode;
						OperatingTipUtil.startOperat("正在初始化...");
						metaService.findMetadataTempletNoCache(mtCode);
					}else{
						SmartXMessage.show("仪表盘不能为空!");
					}
				}
			}else{
				SmartXMessage.show("需要先保存!");
			}
		}
		
		private function findMetadataTempletNoCacheHandler(event:ResultEvent):void{
			OperatingTipUtil.endOperat();
			var template:MetadataTemplet = event.result as MetadataTemplet;
			if(template != null ){
				
				var contentXml:XML = template.contentXML;
				
				layoutManager = new DashboardLayoutPanel();
				layoutManager.contentXml = contentXml;
				layoutManager.confirmFunc = confirmBtnClick;
				layoutManager.queryDboSql = "select v.code as id#,v.code 编码#,v.name 名称 from v_BAM_DASHBOARDOBJECT v ";//where v.folderid in (select id from bam_folder f start with f.code='DQ_DATAVISUAL_DBO' connect by prior f.id=f.parentid)";
				layoutManager.queryDbSql = "select v.code as id#,v.code 编码,v.name 名称 from bam_dashboard v where v.folderid in (select id from bam_folder f start with f.code='DQ_DATAVISUAL' connect by prior f.id=f.parentid)";
				
				PopUpManager.addPopUp(layoutManager,desktop,true);
				PopUpManager.centerPopUp(layoutManager);
				
				//modify by zhangzy 以下代码迁移到DashboardLayoutPanel中实现。
//				var addLinkBtn:LinkButton = new LinkButton();
//				addLinkBtn.toolTip = "新增仪表盘对像";
//				addLinkBtn.setStyle("icon",AssetsFileLib.addaddIcon);
//				addLinkBtn.addEventListener(MouseEvent.CLICK,addClickHandler);
//				
//				var editLinkBtn:LinkButton = new LinkButton();
//				editLinkBtn.toolTip = "编辑仪表盘对像";
//				editLinkBtn.setStyle("icon",AssetsFileLib.editIcon);
//				editLinkBtn.addEventListener(MouseEvent.CLICK,function(event:MouseEvent):void{
//					var item:Object = layoutManager.dataGrid.selectedItem;
//					if(item != null ){
//						var itemXml:XML = item as XML;
//						var dboCode:String = String(itemXml.@dbocode);
//						
//						var tw:TitleWindow = new TitleWindow();
//						tw.width = 650;
//						tw.height = 450;
//						tw.title = "编辑仪表盘对像";
//						tw.showCloseButton = true;
//						tw.addEventListener(CloseEvent.CLOSE,function(event:CloseEvent):void{
//							PopUpManager.removePopUp(tw);
//						});
//						
//						var tempPanel:DboBillCardPanel = new DboBillCardPanel();
//						tempPanel.templetCode = "T_BAM_DASHBOARDOBJECT2";
//						tempPanel.initQueryCondition = " code='"+dboCode+"'";
//						tempPanel.insertbfIncCard = "smartx.bam.bs.dataanalyze.DboBfFormInterceptor";
//						tempPanel.insertafIncCard = "smartx.bam.bs.dataanalyze.DboAfFormInterceptor";
//						tempPanel.updatebfIncCard = "smartx.bam.bs.dataanalyze.DboBfFormInterceptor";
//						tempPanel.updateafIncCard = "smartx.bam.bs.dataanalyze.DboAfFormInterceptor";
//						tempPanel.deleteafIncCard = "smartx.bam.bs.dataanalyze.DboAfFormInterceptor";
////						tempPanel.clientInsertBfIncCard = "smartx.bam.flex.modules.dataanalyze.DboInsertClientInterceptor";
//						tempPanel.addEventListener("initComplete",function(event:BillCardPanelEvent):void{
//							tempPanel.cardPanel.setDataValueByQuery();
//						});
//						tempPanel.addEventListener("deleteSuccessful",function(event:BillCardPanelEvent):void{
//							layoutManager.deleteClickHandler(null);
//							PopUpManager.removePopUp(tw);
//						});
//						tempPanel.addEventListener("saveSuccessful",function(event:StyleTemplateEvent):void{
//							if(event.saveObject != null ){
//								var tempMtCode:String = event.saveObject["MTCODE"];
//								MetadataTempletUtil.getInstance().flushMetadataTempletByMtcode(tempMtCode,endpoint);
//								PopUpManager.removePopUp(tw);
//							}
//						});
//						
//						
//						tw.addChild(tempPanel);
//						
//						PopUpManager.addPopUp(tw,desktop,true);
//						PopUpManager.centerPopUp(tw);
//					}
//				});
//				
//				layoutManager.podsBtnBox.addChild(addLinkBtn);
//				layoutManager.podsBtnBox.addChild(editLinkBtn);
				
			}
		}
		
		private function updateMetadataTempletContentHandler(event:ResultEvent):void{
			OperatingTipUtil.endOperat();
			PopUpManager.removePopUp(layoutManager);
			layoutManager.confirmFunc = null;
			layoutManager = null;
			MemoryUtil.forceGC();
			MetadataTempletUtil.getInstance().flushMetadataTempletByMtcode(mtCode,endpoint);
		}
		
		private function faultHander(event:FaultEvent):void{
			OperatingTipUtil.endOperat();
			SmartXMessage.show("ERROR!",SmartXMessage.MESSAGE_ERROR,event.fault.faultDetail);
		}
		
		private function confirmBtnClick():void{
			var contentStr:String = layoutManager.getContentXml();
			OperatingTipUtil.startOperat("正在保存元数据...");
			metaService.updateMetadataTempletContent(mtCode,contentStr);
		}
		
		private function addClickHandler(event:MouseEvent):void{
			
			var tw:TitleWindow = new TitleWindow();
			tw.width = 650;
			tw.height = 450;
			tw.title = "新建仪表盘对像";
			tw.showCloseButton = true;
			tw.addEventListener(CloseEvent.CLOSE,function(event:CloseEvent):void{
				PopUpManager.removePopUp(tw);
			});
			
			var tempPanel:DboBillCardPanel = new DboBillCardPanel();
			tempPanel.templetCode = "T_BAM_DASHBOARDOBJECT2";
			tempPanel.insertbfIncCard = "smartx.bam.bs.dataanalyze.DboBfFormInterceptor";
			tempPanel.insertafIncCard = "smartx.bam.bs.dataanalyze.DboAfFormInterceptor";
			tempPanel.updatebfIncCard = "smartx.bam.bs.dataanalyze.DboBfFormInterceptor";
			tempPanel.updateafIncCard = "smartx.bam.bs.dataanalyze.DboAfFormInterceptor";
			tempPanel.deleteafIncCard = "smartx.bam.bs.dataanalyze.DboAfFormInterceptor";
			tempPanel.clientInsertBfIncCard = "smartx.bam.flex.modules.dataanalyze.DboInsertClientInterceptor";
			tempPanel.addEventListener("initComplete",function(event:BillCardPanelEvent):void{
				tempPanel.cardPanel.enterInsertMode();
			});
			tempPanel.addEventListener("deleteSuccessful",function(event:BillCardPanelEvent):void{
				layoutManager.deleteClickHandler(null);
				PopUpManager.removePopUp(tw);
			});
			tempPanel.addEventListener("saveSuccessful",function(event:StyleTemplateEvent):void{
				if(event.saveObject != null ){
					var tempMtCode:String = event.saveObject["MTCODE"];
					var newPod:XML = new XML("<pod/>");
					newPod.@dbocode = event.saveObject["CODE"];
					newPod.@dboname =  event.saveObject["NAME"];
					newPod.@code = layoutManager.getNextPodCode();
					layoutManager.contentXml.pods.appendChild(newPod);
					if(layoutManager.contentXml.layout.@type == BAMConst.LAYOUTTYPE_MDIWINDOW){
						var newPodRef:XML = new XML("<podref/>");
						newPodRef.@code = newPod.@code;
						layoutManager.selectedLayoutXml.sequence.appendChild(newPodRef);
					}
					layoutManager.dataGrid.invalidateList();
					MetadataTempletUtil.getInstance().flushMetadataTempletByMtcode(tempMtCode,endpoint);
					
					PopUpManager.removePopUp(tw);
				}
			});
			
			tw.addChild(tempPanel);
			
			PopUpManager.addPopUp(tw,desktop,true);
			PopUpManager.centerPopUp(tw);
			
		}
	}
}