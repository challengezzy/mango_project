/**
 * 
 */
package smartx.publics.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.NovaLogger;

/**
 * @author caohenghui
 * 文件下载Servlet，支持中文文件名下载
 */
public class FileDownloadServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");		
		Logger logger = NovaLogger.getLogger(this.getClass());
		
		
		String rootPath = (String) Sys.getInfo("NOVA2_SYS_ROOTPATH");		
		OutputStream out = response.getOutputStream();	
		FileInputStream fis = null;
		File file = null;
		
		String reportName = request.getParameter("reportName");
		String fileName = request.getParameter("fileName");
		
		logger.debug("requestAddr = " + request.getRemoteAddr()+"  " + request.getRemoteHost() + "   " + request.getRemoteUser());
		logger.debug("reportname  = " + reportName + ", fileName= "+ fileName);
		try {
			//*********对于下载的中文文件名，编码必须为iso-8859-1,否则乱码***********
			//reportName = new String(reportName.getBytes(),"ISO-8859-1");
			//fileName = new String(fileName.getBytes(),"ISO-8859-1");
			//modify by zhangzy conf/server.xml中，配置URIEncoding="UTF-8", 不进行转换
			reportName = URLEncoder.encode(reportName, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		try {
			String filePath = rootPath + FileConstant.DOWNLOAD_DIR + "/" + fileName;
			file = new File(filePath);
			fis = new FileInputStream(file);

			response.setContentType("application/download");
			response.setHeader("content-disposition", "attachment; filename=" + reportName);
//			int readIndex = -1;
//			while ((readIndex = fis.read()) != -1) {
//				out.write(readIndex);
//			}
			//一次读取1024个字节
			byte[] buffer = new byte[1024];
			int i = -1;
			while ((i = fis.read(buffer)) != -1) {
				out.write(buffer,0,i);
			}
			
		} catch (IOException e) {
			logger.error("文件下载出错", e);
			throw e;
		}finally{
			out.flush();
			out.close();

			fis.close();
			//取消文件删除，360浏览器会死命发下载请求
			//file.delete();// 删除下载后的文件,一定要放在代码的最后一行
		}
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
}
