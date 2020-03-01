package smartx.flex.modules.announcement
{
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.event.BasicEvent;
	import smartx.flex.components.event.BillCardPanelEvent;
	import smartx.flex.components.styletemplate.ifc.CardButtonListener;
	import smartx.flex.components.util.MemoryUtil;
	import smartx.flex.components.util.RichTextEditorWindow;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.util.StringUtils;
	import smartx.flex.components.vo.GlobalConst;
	
	/**
	 * @author zzy
	 * @date Jun 14, 2011
	 * 编辑公告信息
	 */
	public class EditAnnounceContentBtnListener implements CardButtonListener
	{
		
		private var cardData:Object;
		private var billCardPanel:BillCardPanel;
		private var taskId:String;
		private var editorWindow:RichTextEditorWindow;
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
	
		public function buttonClick(cardPanel:BillCardPanel):void{
			cardData = cardPanel.getDataValue();
			billCardPanel = cardPanel;
			
			editorWindow = new RichTextEditorWindow();
			editorWindow.text= cardData["CONTENT"];
			editorWindow.title = "公告内容编辑";
			editorWindow.showCloseButton = true;
			editorWindow.addEventListener(CloseEvent.CLOSE,function(event:CloseEvent):void{
				PopUpManager.removePopUp(editorWindow);
			});
			editorWindow.confirmFunc = editorWindowConfirmFun;
			
			PopUpManager.addPopUp(editorWindow,desktop,true);
			PopUpManager.centerPopUp(editorWindow);
		}
		
		private function editorWindowConfirmFun():void{
			cardData["CONTENT"] = editorWindow.textEditor.htmlText;
			billCardPanel.setDataValue(cardData);
			PopUpManager.removePopUp(editorWindow);
			
			editorWindow.confirmFunc = null;
			editorWindow = null;
			MemoryUtil.forceGC();
		}

		
	}
}