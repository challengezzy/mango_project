package smartx.bam.flex.modules.task.listener
{
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.ifc.CardButtonListener;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.util.StringUtils;
	import smartx.flex.components.util.TextAreaWindow;
	import smartx.flex.components.vo.GlobalConst;

	/**
	 * @author zzy
	 * @date Jun 14, 2011
	 * 添加任务备注信息
	 */
	public class AddTaskCommentButtonListener implements CardButtonListener
	{
		protected  var remoteObj:RemoteObject;
		
		private var textWindow:TextAreaWindow;
		
		private var comment:String;
		private var cardData:Object;
		private var billCardPanel:BillCardPanel;
		private var taskId:String;
		
		public function AddTaskCommentButtonListener()
		{
			remoteObj = new RemoteObject(BAMConst.BAM_Service);
			remoteObj.endpoint = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
			remoteObj.updateTaskComment.addEventListener(ResultEvent.RESULT,function(event:ResultEvent):void{
				PopUpManager.removePopUp(textWindow);
				//Alert.show("添加任务备注成功");
				cardData["MEMO"] = comment;
				//billCardPanel.refresh("id = " + taskId  );//数据刷新
				billCardPanel.setDataValue(cardData);
			});
			remoteObj.updateTaskComment.addEventListener(FaultEvent.FAULT,function(event:FaultEvent):void{
				SmartXMessage.show("添加任务备注信息错误!",SmartXMessage.MESSAGE_ERROR,event.fault.faultString);
			});
		}
		
		public function buttonClick(cardPanel:BillCardPanel):void{
			cardData = cardPanel.getDataValue();
			billCardPanel = cardPanel;
			if(cardData != null && cardData[GlobalConst.KEYNAME_MODIFYFLAG] != "insert")
			{
				comment = cardData["MEMO"];
				//填出窗口让用户添加备注信息
				textWindow = new TextAreaWindow();
				textWindow.confirmFunc = addCommentConfirm;
				textWindow.width = 400;
				textWindow.height = 300;
				textWindow.title = "添加任务备注信息";
				
				PopUpManager.addPopUp(textWindow,cardPanel,true);
				PopUpManager.centerPopUp(textWindow);

			}else
				SmartXMessage.show("新增操作时不能添加任务备注信息！",SmartXMessage.MESSAGE_WARN);
			
		}
		
		public function addCommentConfirm():void{
			if(textWindow.text == null || "" == textWindow.text )
			{
				SmartXMessage.show("备注信息不能为空！",SmartXMessage.MESSAGE_WARN);
				return;
			}
			var newComment:String = textWindow.text;
			taskId = cardData["ID"];
			var userName:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_LOGIN_USER_LOGINNAME));
			var dataStr:String = StringUtils.convertDateToString(new Date(),1);
			
			if(comment == null)
				comment = "";
			
			comment = comment + "\n----------------------------------------------\n" + userName + " [" + dataStr + "] \n" + newComment;
			
			remoteObj.updateTaskComment(null, comment,taskId);
			
			textWindow.confirmButton.enabled = false;
			//textWindow.cancelButton.enabled = false;
			//cardData["MEMO"] = comment;
			
		}
		
	}
}