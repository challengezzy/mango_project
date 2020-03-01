/**
 * 
 */
package smartx.publics.file;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * @author caohenghui
 * 
 */
public class FileUploadServlet extends HttpServlet {
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		DiskFileItemFactory factory = new DiskFileItemFactory();

		ServletFileUpload fileUpload = new ServletFileUpload(factory);
		fileUpload.setSizeMax(1024 * 1024 * 1024);

		try {
			List items = fileUpload.parseRequest(request);
			Iterator iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();

				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString();
				} else {
					String fieldName = item.getFieldName();
					String fileName = item.getName();
					String contentType = item.getContentType();
					boolean isInMemory = item.isInMemory();
					long sizeInBytes = item.getSize();
					String path = getServletContext().getRealPath("/");
					File uploadedFile = new File(path+new Random().nextInt(Integer.MAX_VALUE) + "_"+ fileName);
					item.write(uploadedFile);
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(req, resp);
	}
	
	
	
}
