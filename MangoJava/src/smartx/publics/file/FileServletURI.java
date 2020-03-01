package smartx.publics.file;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import flex.messaging.FlexContext;
import flex.messaging.FlexSession;


public class FileServletURI extends HttpServlet implements Serializable{

    private static final long serialVersionUID = 2L;
    
    public HttpServletRequest request;
    public HttpServletResponse response;
    public FlexSession session;


    public FileServletURI() {
        request = FlexContext.getHttpRequest();
        session = FlexContext.getFlexSession();
        response = FlexContext.getHttpResponse();
    }
    
    public String  getDownLoadURI(String fileName,String reportName) throws IOException {

    	String uri = null;
    	try {
    	    HttpServletRequest req = FlexContext.getHttpRequest();
            String contextRoot = null;
            contextRoot = req.getContextPath();
            uri = contextRoot+"/fileDownloadServlet?fileName="+fileName+"&reportName="+reportName;

        } catch (Exception e) {

            e.printStackTrace();

		}
        return uri;
    }
    
    public String getUpLodaURI(){
    	
    	String uri = null;
    	try {
    	    HttpServletRequest req = FlexContext.getHttpRequest();
            String contextRoot = null;
            contextRoot = req.getContextPath();
            uri = contextRoot+"/fileUploadServlet";

        } catch (Exception e) {

            e.printStackTrace();

		}
        return uri;
    	
    }

}
