package smartx.flex.components.styletemplate.ext
{
	import flash.events.MouseEvent;
	import flash.utils.getDefinitionByName;
	
	import mx.containers.Canvas;
	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.events.FlexEvent;
	
	import smartx.flex.components.assets.AssetsFileLib;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.Hashtable;
	import smartx.flex.components.util.IconUtility;
	import smartx.flex.components.util.SmartXMessage;
	
	import spark.layouts.HorizontalAlign;
	
	public class ListEmbedButtonGroupComponent extends Canvas
	{
		private var listenerMap:Hashtable = new Hashtable();
		
		private var iconComponentArray:Array = [];
		
		public var contentXml:XML;
		
		public var billListPanel:BillListPanel;
		
		public function ListEmbedButtonGroupComponent(){
			super();
			this.addEventListener(FlexEvent.CREATION_COMPLETE,function(event:FlexEvent):void{
				buildEmbedButton();
			});
		}
		
		private function buildEmbedButton():void{
			var hbox:HBox = new HBox();
			hbox.percentWidth = 100;
			hbox.setStyle("horizontalAlign",HorizontalAlign.CENTER);
			
			for each(var embedBtnXML:XML in contentXml.embedButton.button){
				var embedBtn:Button = new Button();
				embedBtn.id = embedBtnXML.@id;
				embedBtn.label = embedBtnXML.@label;
				embedBtn.toolTip = embedBtnXML.@toolTip;
				embedBtn.height = 20;
				var iconUrl:String = embedBtnXML.@icon;
				if(iconUrl != null && iconUrl != ""){
					var embedBtnIcon:Class = IconUtility.getClass(embedBtn,iconUrl,16,16);
					embedBtn.setStyle("icon",embedBtnIcon);
					iconComponentArray.push(embedBtn);
				}
				var embedBtnListeners:Array = [];
				for each(var cardListener:XML in embedBtnXML.listeners.listener){
					var embedBtnClassReference:Class = getDefinitionByName(cardListener) as Class;
					var cardListenerClass:ListButtonListener = new embedBtnClassReference() as ListButtonListener;
					embedBtnListeners.push(cardListenerClass);
				}
				listenerMap.add(embedBtn.id,embedBtnListeners);
				embedBtn.addEventListener(MouseEvent.CLICK,function(event:MouseEvent):void{
					var array:Array = listenerMap.find(event.target.id) as Array;
					if(array != null){
						for each(var temp:ListButtonListener in array){
							temp.buttonClick(billListPanel);
						}
					}	
				});
				hbox.addChild(embedBtn);
			}
			
			/*var b1:Button = new Button();
			b1.label = "test1";
			b1.height = 20;
			b1.setStyle("icon",AssetsFileLib.designIcon);
			b1.addEventListener(MouseEvent.CLICK,function(event:MouseEvent):void{
			SmartXMessage.show("test1 click...");
			});
			hbox.addChild(b1);
			
			var b2:Button = new Button();
			b2.label = "test2";
			b2.addEventListener(MouseEvent.CLICK,function(event:MouseEvent):void{
			SmartXMessage.show("test2 click...");
			});
			hbox.addChild(b2);
			
			var b3:Button = new Button();
			b3.label = "test3";
			b3.addEventListener(MouseEvent.CLICK,function(event:MouseEvent):void{
			SmartXMessage.show("test3 click...");
			});
			hbox.addChild(b3);*/
			
			this.addChild(hbox);
		}
		
		public function getIconComponentArray():Array{
			return iconComponentArray;
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void{
			super.updateDisplayList(unscaledWidth,unscaledHeight);
		}
		
		
	}
}