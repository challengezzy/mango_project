package smartx.framework.common.utils;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class FlexAntTaskHelper {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filename=".flexLibProperties";
		if(args.length > 0)
			filename = args[0];
		File file = new File(filename);
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(file);
			List<Element> list = doc.getRootElement().element("includeResources").elements();
			for(Element node : list){
				String temp = "<include-file ";
				String sourcePath = node.attributeValue("sourcePath");
				String sourceFileName = sourcePath.substring(sourcePath.lastIndexOf("/")+1,sourcePath.length());
				temp += "name=\""+sourceFileName+"\" path=\""+sourcePath+"\"/>";
				System.out.println(temp); 
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 	
	}

}
