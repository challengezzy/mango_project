package smartx.flex.modules.basic.system.usermgmt
{
	import mx.controls.Alert;
	import mx.formatters.DateFormatter;
	
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.core.ext.DataValidator;
	import smartx.flex.components.util.TempletDataUtil;

	public class UsermgmtDataValidator implements DataValidator
	{
		
 
        private var dataValue:Object;	
        	
		public function validateData(cardPanel:BillCardPanel):Boolean
		{
			if(!cardPanel.insertMode)//非insert模式不用输密码
				return true;
			this.dataValue = cardPanel.getDataValue();
			if(dataValue["PWD"] != dataValue["PWDCONFIRM"]){
				Alert.show("两次输入的密码不一致","错误");
				return false;
			}
			if(dataValue["ADMINPWD"] != dataValue["ADMINPWDCONFIRM"]){
				Alert.show("两次输入的管理密码不一致","错误");
				return false;
			}
			if(cardPanel.insertMode){
				//密码编码
				if(dataValue["PWD"] != null)
					dataValue["PWD"] = TempletDataUtil.generatePassword(dataValue["LOGINNAME"],dataValue["PWD"]);
				else
					dataValue["PWD"] = TempletDataUtil.generatePassword(dataValue["LOGINNAME"],"");
						
				if(dataValue["ADMINPWD"] != null)
					dataValue["ADMINPWD"] = TempletDataUtil.generatePassword(dataValue["LOGINNAME"],dataValue["ADMINPWD"]);
				else
					dataValue["ADMINPWD"] = TempletDataUtil.generatePassword(dataValue["LOGINNAME"],"");
			}
			var now:Date = new Date();
			var formatter:DateFormatter = new DateFormatter();
			var formatString:String = "YYYY-MM-DD JJ:NN:SS";
			formatter.formatString = formatString;
			dataValue["CREATEDATE"] = formatter.format(now);
			return true;
		}
		

	}
}