package smartx.flex.components.styletemplate.ext
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.containers.Canvas;
	import mx.containers.TitleWindow;
	import mx.containers.VBox;
	import mx.controls.Button;
	import mx.events.CloseEvent;
	import mx.events.FlexEvent;
	import mx.managers.PopUpManager;
	
	import smartx.flex.components.assets.AssetsFileLib;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.mtchart.MTCardChartPanel;
	import smartx.flex.components.itemcomponent.ext.MTCardForRefPanel;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.MetadataTemplet;

	public class DebugUtil
	{
		public static function generateDebugButton(sytletemlate:Canvas,templateId:Number):void{
			var vbox:VBox = new VBox();
			vbox.width = 20;
			vbox.height = 20;
			var debugButton:Button = new Button();
			vbox.addChild(debugButton);
			debugButton.setStyle("icon",AssetsFileLib.submitIcon);
			debugButton.width = 20;
			debugButton.height = 20;
			debugButton.toolTip = "点击修改对应的元数据模板";
			debugButton.visible = false;
			//鼠标移动到按钮上则显示
			vbox.addEventListener(MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{
				debugButton.visible = true;
			});
			vbox.addEventListener(MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{
				debugButton.visible = false;
			});
			
			debugButton.addEventListener(MouseEvent.CLICK,function(e:MouseEvent):void{
				var panel:MTCardForRefPanel = new MTCardForRefPanel();
				var metadataEditTemplet:MetadataTemplet = new MetadataTemplet();
				metadataEditTemplet.contentXML = GlobalConst.STYLETEMPLATE_EDIT_METADATAXML;
				panel.endpoint = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
				panel.metadataTemplet = metadataEditTemplet;
				panel.percentHeight = 100;
				panel.percentWidth = 100;
				panel.insertMode = false;
				panel.initQueryCondition = "id="+templateId;
				var editWindow:TitleWindow = new TitleWindow();
				editWindow.width = 800;
				editWindow.height = 450;
				editWindow.title = "编辑元数据模板";
				editWindow.showCloseButton = true;
				editWindow.addEventListener(CloseEvent.CLOSE,function(e:CloseEvent):void{
					PopUpManager.removePopUp(e.target as TitleWindow);
				});
				editWindow.addChild(panel);
				PopUpManager.addPopUp(editWindow,sytletemlate.root,true);
				PopUpManager.centerPopUp(editWindow);
				panel.addEventListener("cancel",function(e:Event):void{
					var temp:MTCardForRefPanel = e.target as MTCardForRefPanel;
					PopUpManager.removePopUp(temp.parent as TitleWindow);
				});
				panel.addEventListener("confirm",function(e:Event):void{
					var temp:MTCardForRefPanel = e.target as MTCardForRefPanel;
					PopUpManager.removePopUp(temp.parent as TitleWindow);
				});
			}
			);
			
			sytletemlate.addChild(vbox);
		}
		
	}
}