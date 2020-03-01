package smartx.flex.components.itemcomponent
{
	import flash.events.Event;
	import flash.events.FocusEvent;
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Button;
	import mx.events.DataGridEvent;
	import mx.events.FlexEvent;
	import mx.managers.PopUpManager;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.flex.components.assets.AssetsFileLib;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.event.UIComponentEvent;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.TempletItemVO;

	public class ItemListPanel extends ItemUIComponent
	{
		private var childPanel:BillListPanel=new BillListPanel;
		private var refdesc:String;
		[Bindable]
		public var forienKey:String;
		[Bindable]
		private var mainTableKey:String;
		[Bindable]
		public var pkValue:String="-1";
		private var childTempletCode:String;
		private var poppanel:ItemListPopPanel;
		private var selfheight:String="400";
		private var dataValueList:Array=new Array;
		private var ishowsefbt:String="false";
		private var deskTop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		public function ItemListPanel(templetItemVO:TempletItemVO,destination:String, endpoint:String=null,showLabel:Boolean=true) 
		{
			super(templetItemVO, childPanel, showLabel);
			if(templetItemVO.clientrefdesc != null && templetItemVO.clientrefdesc != ""){
				mainTableKey=templetItemVO.pub_Templet_1VO.pkname;
				refdesc = templetItemVO.clientrefdesc;//childTempletCode,forienKey,isshowbtcon_bt_del,height
				var descarr:Array=refdesc.split(",");
				childTempletCode=descarr[0];
				forienKey=descarr[1];
				if(descarr.length>=2){
					ishowsefbt=descarr[2];
				}
				 if(descarr.length>=3){
					 selfheight=descarr[3];
				 }
				
				 this.height=parseInt(selfheight);
				if(childTempletCode == null)
					throw new Error("没有设置子表的模板编码");
				
				childPanel.templetCode = childTempletCode;
				childPanel.destination = destination;
				childPanel.endpoint = endpoint;
				childPanel.editable = true;
				childPanel.showQuickQueryPanel = false;
				childPanel.showEditBox = true;
				childPanel.showInitingWindow = false;
				childPanel.showLoadingWindow = false;
				childPanel.showSaveButton = false;
				childPanel.rowCountPerPage=100;
				childPanel.height=parseInt(selfheight);
				childPanel.width=this.width;
				childPanel.addEventListener("queryEnd",function(e:Event):void{
					dispatchEvent(new UIComponentEvent(UIComponentEvent.REAL_VALUE_CHANGE));
				});
				childPanel.dataGrid.addEventListener(DataGridEvent.ITEM_EDIT_END,function(e:Event):void{
					dispatchEvent(new UIComponentEvent(UIComponentEvent.REAL_VALUE_CHANGE));
				});
				childPanel.dataGrid.addEventListener(FlexEvent.VALUE_COMMIT,function(e:Event):void{
					dispatchEvent(new UIComponentEvent(UIComponentEvent.REAL_VALUE_CHANGE));
				});
				if(ishowsefbt=="true"){
					childPanel.editBox.removeAllChildren()
					var bt_con:Button=new Button;
					bt_con.toolTip="引用";
					bt_con.width=20;
					bt_con.height=20;
					bt_con.setStyle("icon",AssetsFileLib.filterIcon);
					bt_con.addEventListener(MouseEvent.CLICK,bt_con_clickHandler);
					childPanel.editBox.addChildAt(bt_con,0);//自定义按钮  引用关联数据   删除关联
					
					var bt_del:Button=new Button;
					bt_del.toolTip="删除关联";
					bt_del.width=20;
					bt_del.height=20;
					bt_del.setStyle("icon",AssetsFileLib.delete2Icon);
					bt_del.addEventListener(MouseEvent.CLICK,bt_del_clickHandler);
					childPanel.editBox.addChildAt(bt_del,1);//自定义按钮  引用关联数据   删除关联
				}
				
				
			}
			this.hbox2.removeChild(this.clearButton);
		}
		
		protected function bt_con_clickHandler(event:MouseEvent):void
		{
			poppanel=new ItemListPopPanel;
			poppanel.templetCode=childTempletCode;
			poppanel.forienKey=this.forienKey;
			poppanel.pkValue=this.pkValue==null?"-1":this.pkValue;
			poppanel.addEventListener("CONOK",conok);
			PopUpManager.addPopUp(poppanel,deskTop);
			PopUpManager.centerPopUp(poppanel);
			
			
		}
		
		public function getbilllist():BillListPanel{
			return childPanel;
		}
		
		public function getdataValueList():Array{
			return dataValueList;
		}
		private function conok(event:Event):void{
			var arr:Array=poppanel.arro;
			if(arr!=null&&arr.length>0){
				var degd:ArrayCollection=this.childPanel.dataGrid.dataProvider as ArrayCollection;
				for each(var o:Object in arr){
					o[GlobalConst.KEYNAME_MODIFYFLAG] = "update";
					o[GlobalConst.KEYNAME_TEMPLETCODE] = childTempletCode;
					o[forienKey]=pkValue;
					dataValueList.push(o);
					degd.addItem(o);
				}
			}
			var i:int = 1;
			for each(var temp:Object in degd){
				temp.rownum = i++;
			}
			this.childPanel.refreshByData(degd);
		}
		
		protected function bt_del_clickHandler(event:MouseEvent):void
		{
			var selectedIndex:int=childPanel.dataGrid.selectedIndex;
			var deleteData:Object = childPanel.getCurrentData().getItemAt(selectedIndex);
			var insertIndex:int = dataValueList.indexOf(deleteData);
			if(insertIndex>=0)//是新插入的记录，则直接去掉
				dataValueList.splice(insertIndex,1);
			else{
				deleteData[GlobalConst.KEYNAME_MODIFYFLAG] = "update";
				deleteData[GlobalConst.KEYNAME_TEMPLETCODE] = childTempletCode;
				deleteData[forienKey]=null;
				dataValueList.push(deleteData);
			}
				
			childPanel.getCurrentData().removeItemAt(selectedIndex);
		}
		
		//重新初始化
		public function reInit(dataValue:Object=null):void{
			if(dataValue!=null){
				pkValue = dataValue[mainTableKey]==null?"-1":dataValue[mainTableKey];
				childPanel.initQueryCondition = forienKey+"='"+pkValue+"'";
				childPanel.query();
			}		
			
		}
		
		public override function set editable(editable:Boolean):void{
			childPanel.editable = editable;
			childPanel.showEditBox = editable;
			
		}
		
		public override function get realValue():Object{
			if(childPanel.getCurrentData()!=null&&childPanel.getCurrentData().length>0)
				return "";
			return null;
		}
		
	}
}