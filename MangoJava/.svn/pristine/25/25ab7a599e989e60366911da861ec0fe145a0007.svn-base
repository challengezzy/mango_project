package smartx.framework.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 *@author zzy
 *@date Aug 30, 2011
 **/
public class SmartXMLUtil {
	
	private static Serializer serializer;
	
	/**
	 * read the contents of the XML
    * document from the provided source and convert it into an object
    * of the specified type. If the XML source cannot be deserialized
    * or there is a problem building the object graph an exception
    * is thrown. The instance deserialized is returned
	 * @param <T>
	 * @param type this is the class type to be deserialized from XML
     * @param source this provides the source of the XML document
     * @param strict this determines whether to read in strict mode 
	 * @throws Exception
	 */
	public static <T> T getObjectByXml(Class<? extends T> type, String sourceXml, boolean strict) throws Exception{
		//把XML文件内容转换成对象
		if(serializer == null)
			serializer = new Persister();
			
		
		
//		InputStream in = SmartXMLUtil.class.getResourceAsStream("/smartx/dq/bs/compare/compareEntity.xml");
//		java.io.InputStreamReader reader=new java.io.InputStreamReader(in,"utf-8");
//		char[] buf=new char[1024];
//		StringWriter writer = new StringWriter();
//		int pos;
//		while((pos=reader.read(buf))!=-1){
//			writer.write(buf,0,pos);
//		}
//		reader.close();
//		String str=writer.toString();
//		System.out.println(str);
		
		//ComparisonVo cmpVo = serializer.read(type, sourceXml,strict);
		
		return serializer.read(type, sourceXml,strict);
	}
	
	/**
	 * 把XML对象转换成字符串
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public static String getXmlByObject(Object source) throws Exception{
		if(serializer == null)
			serializer = new Persister();
		
		//再把对象转换成XML字符串
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		serializer.write( source, outputStream);
		
		String returnStr = outputStream.toString("utf-8");
		outputStream.close();
		//System.out.println(returnStr);
		
		return returnStr;
	}

}
