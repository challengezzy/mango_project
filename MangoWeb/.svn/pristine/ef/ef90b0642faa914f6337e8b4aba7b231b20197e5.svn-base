package smartx.bam.flex.modules.dataanalyze
{
	import flash.display.DisplayObject;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import mx.controls.LinkButton;
	import mx.events.FlexEvent;
	import mx.modules.ModuleLoader;
	
	import smartx.flex.components.assets.AssetsFileLib;
	import smartx.flex.components.core.BillListItemPanel;
	import smartx.flex.components.styletemplate.ifc.ListItemButtonListenerIFC;
	
	public class DboViewListener implements ListItemButtonListenerIFC
	{
		private var linkBtn:LinkButton;
		public function DboViewListener()
		{
		}
		
		public function handler(dataValue:Object, listItemPanel:BillListItemPanel):void
		{
			var dbCode:String = dataValue["DASHBOARDCODE"];
			if(dbCode != null && dbCode != "" ){
				
				var mtCode:String = "MT_LAYOUT_"+dbCode;
				
				var loader:ModuleLoader = new ModuleLoader();
				loader.percentHeight = 100;
				loader.percentWidth = 100;
				loader.url = "smartx/bam/flex/modules/dashboard/Dashboard.swf?dbcode="+dbCode;
				
				var parentDisplay:DataAnalyzeDBPanel = this.getDataAnalyzeDBPanel(listItemPanel);
				
				if(parentDisplay.canvas.getChildren().length>0){
					parentDisplay.canvas.removeAllChildren();
				}
				
				parentDisplay.canvas.addChild(loader);
				
				linkBtn = new LinkButton();
				linkBtn.toolTip = "返回";
				linkBtn.setStyle("icon",AssetsFileLib.returnIcon);
				linkBtn.addEventListener(MouseEvent.CLICK,function(event:MouseEvent):void{
					parentDisplay.mainStack.selectedChild = parentDisplay.listVbox;
				});
				
				loader.addEventListener(FlexEvent.CREATION_COMPLETE,function(event:FlexEvent):void{
					linkBtn.x = loader.width-linkBtn.width;
					linkBtn.y = 2;
				});
				
				parentDisplay.canvas.addChild(linkBtn);
				
				parentDisplay.mainStack.selectedChild = parentDisplay.canvas;
				
			}
		}
		
		private function getDataAnalyzeDBPanel(display:DisplayObject):DataAnalyzeDBPanel{
			if(display is DataAnalyzeDBPanel){
				return display as DataAnalyzeDBPanel;
			}else{
				return getDataAnalyzeDBPanel(display.parent);
			}
		}
	}
}