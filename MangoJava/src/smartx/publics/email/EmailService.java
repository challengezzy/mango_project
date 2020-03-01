package smartx.publics.email;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeUtility;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

import smartx.framework.common.vo.NovaLogger;

/**
 * 邮件服务类，封装发送和接收邮件的基础类
 * 基于apache的commons-email封装http://commons.apache.org/email/
 *@author zzy
 *@date Dec 22, 2011
 *
 **/
public class EmailService {
	
	private Logger logger = NovaLogger.getLogger(this.getClass());

	private String host; // 邮件服务器
	private String username; // 用户名
	private String password; // 密码
	private String from;
	private String charset = "UTF-8";
	private boolean isSessionDebug = false;//是否使用调试模式
	private HtmlEmail email = new HtmlEmail();
	/** 是否是HTML内容格式 */
	private boolean isMIMEBody = false;
	
	/**
	 * @param host 邮件服务器地址
	 * @param username 用户名
	 * @param password 密码
	 * @param from 邮件发送人
	 * @param isMINEBody 是否是html邮件
	 */
	public EmailService(String host,String username,String password,String from,boolean isMINEBody){
		this.host = host;
		this.username = username;
		this.password = password;
		this.from = from;
		this.isMIMEBody = isMINEBody;
		try{
			init();
		}catch (Exception e) {
			logger.error("邮件服务初始化失败！", e);
		}
	}
	
	private void init() throws Exception{
		email.setHostName(host);
		//email.setTLS(true);
		email.setAuthentication(username, password);
		email.setFrom(from);
		email.setDebug(isSessionDebug);
		email.setCharset(charset);
	}
	
	/**
	 * 发送带附件的邮件
	 * @param to
	 * @param subject
	 * @param messageBody
	 * @param attachment
	 * @throws Exception
	 */
	public void sendMail(String to,String subject,String messageBody,String attachment) throws Exception{
		ArrayList<String> toList = new ArrayList<String>();
		toList.add(to);
		
		ArrayList<String> attachmentList = new ArrayList<String>();
		if(attachment != null)
			attachmentList.add(attachment);
		
		this.sendMail(toList, subject, messageBody, attachmentList);
	}
	
	/**
	 * 发送不带附件的邮件
	 * @param to
	 * @param subject
	 * @param messageBody
	 * @throws Exception
	 */
	public void sendMail(String to,String subject,String messageBody) throws Exception{
		ArrayList<String> toList = new ArrayList<String>();
		toList.add(to);
		this.sendMail(toList, subject, messageBody, null);
	}

	/**
	 * 发送复杂内容邮件，邮件内容html格式，带附件
	 * @param toList 收件人列表
	 * @param subject 邮件主题
	 * @param messageBody 邮件内容
	 * @param attachmentList 附件文件
	 * @throws Exception
	 */
	public void sendMail(List<String> toList,String subject, String messageBody,List<String> attachmentList) throws Exception{
		email.setSubject(subject);//设置邮件主题
		email.setSentDate(new Date());//设置发送时间
		
		if(toList == null)
			throw new Exception("邮件收件人列表为空！！");
		//添加收件人列表
		for(int i=0;i<toList.size();i++){
			email.addTo(toList.get(i));
		}
		//设备邮件内容
		email.setTextMsg(messageBody);//文本内容
		if(isMIMEBody)
			email.setHtmlMsg(messageBody);
		
		//添加附件
		if(attachmentList != null){
			for(int i=0;i<attachmentList.size();i++){
				EmailAttachment mailAttach = new EmailAttachment();
				mailAttach.setPath(attachmentList.get(i));
				mailAttach.setDisposition(EmailAttachment.ATTACHMENT);
				
				File f = new File(attachmentList.get(i));
				//附件名称要使用MimeUtility.encodeText进行编码，否则会出现中文乱码
				mailAttach.setName(MimeUtility.encodeText(f.getName()));
				
				email.attach(mailAttach);
			}
		}
		
		email.send();
	}
	
	public static void main(String[] args) throws Exception{
		//String msg = FileUtil.readFileContent("D:\\ireport_src\\rulecheck_overview.html", "UTF-8");
		String msg = "<html><body>HTML邮件 + 附件测试</body></html>";
		
		//String attachmentFile = "E:\\lianzhi_docs\\电信现场\\华为PON网管\\TL1命令.txt";
		//String pdfFile = "E:\\lianzhi_docs\\各地IP地址.txt";//"D:\\ireport_src\\chart_bar_test.pdf";
		
		EmailService mailSrv = new EmailService("smtp.idccenter.net", "zhangzhongyou@lianzhisoft.com", "zhangzhongyou123", 
				"zhangzhongyou@lianzhisoft.com",false);
		//mailSrv.setSessionDebug(true);
		
		mailSrv.sendMail("zhangzhongyou@lianzhisoft.com", "邮件测试", msg);
			
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public boolean isSessionDebug() {
		return isSessionDebug;
	}

	public void setSessionDebug(boolean isSessionDebug) {
		this.isSessionDebug = isSessionDebug;
		if(email != null)
			email.setDebug(isSessionDebug);
	}

	public boolean isMINEBody() {
		return isMIMEBody;
	}

	public void setMINEBody(boolean isMINEBody) {
		this.isMIMEBody = isMINEBody;
	}
}
