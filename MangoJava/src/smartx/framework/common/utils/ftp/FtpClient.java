package smartx.framework.common.utils.ftp;

public class FtpClient extends sun.net.ftp.FtpClient {
	public FtpClient(){
		super();
	}
	
	public FtpClient(String encode){
		super();
		encoding=encode;
	}

}
