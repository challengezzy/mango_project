/**
 * 
 */
package smartx.publics.form.bs.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.FileUtil;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.form.vo.InitMetaDataConst;

/**
 * @author caohenghui
 *
 */
public class ImportInitMetaData {
	
	public void importXMLFromMDFile(String unZipDIR,String datasource) throws Exception{
		
		org.dom4j.Document doc = DocumentHelper.parseText(readContent(unZipDIR+"/metadata.meta"));
		
		Element rootElement = doc.getRootElement();
		CommDMO dmo = new CommDMO();
		try{
			importByRootXML(rootElement,datasource,unZipDIR,null);
			dmo.commit(null);
			
			File file = new File(unZipDIR);
			deleteFielOrDir(file);
			
		}
		catch(Exception e){
			dmo.rollback(null);
			throw e;
		}
		finally{
			dmo.releaseContext();
		}
	}
	
	private void importByRootXML(Element rootElement,String datasource,String unZipDIR,String pidSql) throws Exception{
		CommDMO dmo = new CommDMO();
		for (Iterator<?> i = rootElement.elementIterator(); i.hasNext();) {
			Element xmlObjectElement =  (Element) i.next();
			if((xmlObjectElement == null || xmlObjectElement.getName().trim().equalsIgnoreCase("clobColumns"))||
			   (xmlObjectElement == null || xmlObjectElement.getName().trim().equalsIgnoreCase("dateColumns"))){
				continue;
			}
			String tableName = xmlObjectElement.attributeValue("tableName");
			String pkName = xmlObjectElement.attributeValue("pkName");
			String visiblePkName = xmlObjectElement.attributeValue("visiblePkName");
			String ignoreWhenExists = xmlObjectElement.attributeValue("ignoreWhenExists");
			if(tableName == null || "".equals(tableName)
					|| pkName == null || "".equals(pkName)
					|| visiblePkName == null || "".equals(visiblePkName)
					)
				throw new Exception("非法的XML格式，缺少必填项\n"+xmlObjectElement);
			
			String fkType = xmlObjectElement.attributeValue("fkType");
			String fkName = xmlObjectElement.attributeValue("fkName");
			String fkTable = xmlObjectElement.attributeValue("fkTable");
			String idToParent = xmlObjectElement.attributeValue("idToParent");
			String idToChild = xmlObjectElement.attributeValue("idToChild");
			
			String[] visiblePkNames = visiblePkName.toLowerCase().split(",");
			
			for (Iterator<?> j = xmlObjectElement.elementIterator(); j.hasNext();) {
				Element dataElement = (Element) j.next();

				String parentIdSql = pidSql;
				String childIdSql = null;
				boolean isChild = (fkName != null && !"root".equalsIgnoreCase(rootElement.getName()));
				if(!isEmpty(fkType)&& fkType.trim().equalsIgnoreCase("RefTable")){
					isChild = (fkTable != null && !"root".equalsIgnoreCase(rootElement.getName()));
				}
				if(isChild){
					//rootElement是data节点
					Element parentXmlObjectElement = rootElement.getParent();//父对象的描述节点
					String parentTableName = parentXmlObjectElement.attributeValue("tableName");
					String parentPkName = parentXmlObjectElement.attributeValue("pkName");
					String parentVisiblePkName = parentXmlObjectElement.attributeValue("visiblePkName");
					parentIdSql = "(select "+parentPkName+" from "+parentTableName+" where 1=1 "+createCondition(rootElement,parentVisiblePkName.toLowerCase().split(","),parentIdSql)+")";
					if(!isEmpty(fkType)&&fkType.trim().equalsIgnoreCase("RefTable")){
						childIdSql ="(select "+pkName+" from "+tableName+" where 1=1 "+createCondition(dataElement,visiblePkNames,parentIdSql)+")";
					}
				}
				String sql = "select count(1) from "+tableName+" where 1=1 "+createCondition(dataElement,visiblePkNames,parentIdSql);
				HashVO[] vos = dmo.getHashVoArrayByDS(datasource, sql);
				HashVO vo = vos[0];
				int count = vo.getIntegerValue(0);
				if(count == 0){
					//不存在，insert
					if(!isContain(visiblePkNames,pkName)){
						sql = "insert into "+tableName+"(" +
								pkName;//主键
						for(Object o:dataElement.attributes()){
							Attribute attr = (Attribute)o;
							sql += ","+attr.getName();//各字段值
						}
						
						Element cLobColumns = dataElement.element("clobColumns");
						List list = null;
						if(cLobColumns != null){
							list = cLobColumns.elements();
						}
						if(list != null){
							for(Object obj : list){
								Element cLobColumn = (Element)obj;
								sql+=","+cLobColumn.attributeValue("name");
							}
						}
						
						Element dateColumns = dataElement.element("dateColumns");
						List datelist = null;
						if(dateColumns != null){
							datelist = dateColumns.elements();
						}
						if(datelist != null){
							for(Object obj : datelist){
								Element dateColumn = (Element)obj;
								sql+=","+dateColumn.attributeValue("name");
							}
						}
						//add by caohenghui --end
						
						//最后补上外键
						if(isChild && (this.isEmpty(fkType)||fkType.trim().equalsIgnoreCase("RefField"))){
							sql += ","+fkName;
						}
							
						
						sql += 	")" +
								"values(" +
								"s_"+tableName+".nextval";
						for(Object o:dataElement.attributes()){
							Attribute attr = (Attribute)o;
							sql += ","+StringUtil.strToSQLValue(attr.getValue());//各字段值
						}
						
						if(list != null){
							for(Object obj : list){
								Element cLobColumn = (Element)obj;
								sql += ",empty_clob()";
							}
						}
						
						if(datelist != null){
							for(Object obj : datelist){
								Element dateColumn = (Element)obj;
								String value = isEmpty(dateColumn.getText().trim())?"null":"'"+dateColumn.getText()+"'";
								String temp = ",to_date("+value+",'YYYY-MM-dd hh24:mi:ss')";
								sql+= (this.isEmpty(value)?",null":temp);
							}
						}
						
						if(isChild && (this.isEmpty(fkType)||fkType.trim().equalsIgnoreCase("RefField"))){
							sql += ","+parentIdSql;
						}
							
						sql += ")";
					}
					else{
						sql = "insert into " + tableName + "(";
						boolean isFirst = true;
						for (Object o : dataElement.attributes()) {
							Attribute attr = (Attribute) o;
							if(isFirst){
								sql += attr.getName();
								isFirst = false;
							}
							else
								sql += "," + attr.getName();// 各字段值
						}
						
						Element cLobColumns = dataElement.element("clobColumns");
						List list = null;
						if(cLobColumns != null){
							list = cLobColumns.elements();
						}
						if(list != null){
							for(Object obj : list){
								Element cLobColumn = (Element)obj;
								sql+=","+cLobColumn.attributeValue("name");
							}
						}
						
						Element dateColumns = dataElement.element("dateColumns");
						List datelist = null;
						if(dateColumns != null){
							datelist = dateColumns.elements();
						}
						if(datelist != null){
							for(Object obj : datelist){
								Element dateColumn = (Element)obj;
								sql+=","+dateColumn.attributeValue("name");
							}
						}
						
						// 最后补上外键
						if (isChild && (this.isEmpty(fkType)||fkType.trim().equalsIgnoreCase("RefField"))){
							sql += "," + fkName;
						}
							

						sql += ")" + "values(";
						isFirst = true;
						for (Object o : dataElement.attributes()) {
							Attribute attr = (Attribute) o;
							if(isFirst){
								sql += StringUtil.strToSQLValue(attr.getValue());
								isFirst = false;
							}
							else
								sql += ","
									+ StringUtil.strToSQLValue(attr.getValue());// 各字段值
						}
						
						if(list !=null){
							for(Object obj : list){
								Element cLobColumn = (Element)obj;
								sql += ",empty_clob()";
							}
						}
						
						if(datelist != null){
							for(Object obj : datelist){
								Element dateColumn = (Element)obj;
								String value = isEmpty(dateColumn.getText().trim())?"null":"'"+dateColumn.getText()+"'";
								String temp = ",to_date("+value+",'YYYY-MM-dd hh24:mi:ss')";
								sql += (this.isEmpty(value)?",null":temp);
							}
						}
						
						if (isChild && (this.isEmpty(fkType)||fkType.trim().equalsIgnoreCase("RefField"))){
							sql += "," + parentIdSql;
						}
							
						sql += ")";
					}
					NovaLogger.getLogger(ImportInitMetaData.class).debug("执行导入XMLSQL["+sql+"]");
					
					//add by caohenghui --start 更新clob字段
					
					try{
						
						dmo.executeUpdateByDS(datasource, sql);
						
						Element cLobColumns = dataElement.element("clobColumns");
						List list = null;
						if(cLobColumns != null){
							list = cLobColumns.elements();
						}
						if(list != null){
							for(Object obj : list){
								Element cLobColumn = (Element)obj;
								String columnName = cLobColumn.attributeValue("name");
								String content = randomAccessFile(unZipDIR,"clobcontent.clob", cLobColumn.getText().split(";"));
								if(!isEmpty(content)){
									dmo.executeUpdateClobByDS( datasource, columnName, tableName, " 1=1 "+createCondition(dataElement,visiblePkNames,parentIdSql),content);
								}
								
							}
						}
						
					}catch(Exception e){
						NovaLogger.getLogger(ImportInitMetaData.class).debug("", e);
					}
					
					//add by caohenghui --end 更新clob字段
					
				}
				else{
					//已存在，update
					
					//标示位提示不需要更新时,就直接跳过
					if(ignoreWhenExists != null && ignoreWhenExists.equalsIgnoreCase("true")){
						NovaLogger.getLogger(ImportInitMetaData.class).debug("ignoreWhenExists="+ignoreWhenExists+",放弃更新该条记录!Table:"+tableName);
						continue;
					}
					
					if(!isChild && dataElement.attributeCount() == 0)//没有需要update的字段
						continue;
					sql = "update "+tableName+" set ";
					boolean isFirst = true;
					for(Object o:dataElement.attributes()){
						Attribute attr = (Attribute)o;
						if(isFirst){
							sql += attr.getName()+"="+StringUtil.strToSQLValue(attr.getValue());
							isFirst = false;
						}
						else
							sql += ","+attr.getName()+"="+StringUtil.strToSQLValue(attr.getValue());
					}
					
					Element cLobColumns = dataElement.element("clobColumns");
					List list = null;
					if(cLobColumns != null){
						list = cLobColumns.elements();
					}
					if(list !=null ){
						for(Object obj : list){
							Element cLobColumn = (Element)obj;
							sql+=","+cLobColumn.attributeValue("name")+"=empty_clob()";
						}
					}
					
					Element dateColumns = dataElement.element("dateColumns");
					List datelist = null;
					if(dateColumns != null){
						datelist = dateColumns.elements();
					}
					if(datelist !=null ){
						for(Object obj : datelist){
							Element dateColumn = (Element)obj;
							String value = isEmpty(dateColumn.getText().trim())?"null":"'"+dateColumn.getText()+"'";
							String temp = "to_date("+value+",'YYYY-MM-dd hh24:mi:ss')";
							sql+=","+dateColumn.attributeValue("name")+"="+(this.isEmpty(value)?"null":temp);
						}
					}
					
					if(isChild && (this.isEmpty(fkType)||fkType.trim().equalsIgnoreCase("RefField"))){
						if(isFirst){
							sql += fkName+"="+parentIdSql;
							isFirst = false;
						}else{
							sql += ","+fkName+"="+parentIdSql;
						}
						
					}
					sql += " where 1=1 "+createCondition(dataElement,visiblePkNames,parentIdSql);
					NovaLogger.getLogger(ImportInitMetaData.class).debug("执行导入SQL["+sql+"]");
					
					//add by caohenghui --start 更新clob字段
					
					try{
						
						dmo.executeUpdateByDS(datasource, sql);
						
						Element cLobColumnsUP = dataElement.element("clobColumns");
						List listUP = null;
						if(cLobColumns != null){
							listUP = cLobColumnsUP.elements();
						}
						if(listUP != null){
							for(Object obj : listUP){
								Element cLobColumn = (Element)obj;
								String columnName = cLobColumn.attributeValue("name");
								String content = randomAccessFile(unZipDIR,"clobcontent.clob", cLobColumn.getText().split(";"));
								if(!isEmpty(content)){
									dmo.executeUpdateClobByDS( datasource, columnName, tableName, " 1=1 "+createCondition(dataElement,visiblePkNames,parentIdSql),content);
								}
							}
						}
						
					}catch(Exception e){
						NovaLogger.getLogger(ImportInitMetaData.class).debug("", e);
					}
					
					//add by caohenghui --end 更新clob字段
					
				}
				
				//add by caohenghui --start 更新关联关系表
				try{
					
					if(isChild && (!isEmpty(fkType)&&fkType.trim().equalsIgnoreCase("RefTable"))){
						//标示位提示不需要更新时,就直接跳过
						if(ignoreWhenExists != null && ignoreWhenExists.equalsIgnoreCase("true")){
							NovaLogger.getLogger(ImportInitMetaData.class).debug("ignoreWhenExists="+ignoreWhenExists+",放弃更新该条记录的关联关系表!Table:"+fkTable);
							continue;
						}
						String deleteSQL ="delete from "+fkTable+" where "+idToParent+" = "+parentIdSql+" and "+idToChild+"="+childIdSql;
						String insertSQL ="insert into "+fkTable+"(ID,"+idToParent+","+idToChild+") values(S_"+fkTable+".nextval,"+parentIdSql+","+childIdSql+")";
						dmo.executeBatchByDS(datasource, new String[]{deleteSQL,insertSQL});
					}
					dmo.commit(datasource);
					
				}catch(Exception e){
					NovaLogger.getLogger(ImportInitMetaData.class).debug("", e);
				}
				//add by caohenghui --end 更新关联关系表
				
				try{
					dmo.releaseContext(datasource);
				}catch(Exception e){
					NovaLogger.getLogger(ImportInitMetaData.class).debug("", e);
				}
				
				importByRootXML(dataElement, datasource,unZipDIR,parentIdSql);
			}
		}
	}
	
	public String unZipPatchFile(String UpLoadFileName){
		
		String unZipDir = null;
		
		try{
			unZipDir = InitMetaDataConst.RootPath + InitMetaDataConst.InitDatas_DIR + "/" + UpLoadFileName;
			File dir = new File(unZipDir);
			dir.mkdir();
			
			ZipFile zipFile = new ZipFile(new File(InitMetaDataConst.RootPath + InitMetaDataConst.InitDatas_DIR + "/" + UpLoadFileName+".zip"));
			
            Enumeration<? extends ZipEntry> eu = zipFile.entries();
            byte[] buffer=new byte[1024];
            while (eu.hasMoreElements()) {
            	
            	ZipEntry ze = (ZipEntry)eu.nextElement();
            	String zeName = ze.getName();
            	
            	if(zeName.endsWith(".zip")){
            		
            		File file = new File(unZipDir+"/"+zeName);
            		if(!file.exists()){
            			file.createNewFile();
            		}
            		
            		FileOutputStream fos = new FileOutputStream(file);
            		BufferedOutputStream bos = new BufferedOutputStream(fos);
            		InputStream is = zipFile.getInputStream(ze);
            		int length=-1;
                    while(true){
                        length=is.read(buffer);
                        if(length==-1){
                        	break;
                        }
                        bos.write(buffer,0,length);
                    }
                    bos.close();
                    is.close();
            	}
            	
            }
			
		}catch(Exception e){
			NovaLogger.getLogger(ImportInitMetaData.class).debug("", e);
		}
		return unZipDir;
	}
	
	public void unZipFile(String fullFileName,String unZipDir){
		
		try{
			
			File dir = new File(unZipDir);
			dir.mkdir();
			
			ZipFile zipFile = new ZipFile(new File(fullFileName));
			
            Enumeration<? extends ZipEntry> eu = zipFile.entries();
            byte[] buffer=new byte[1024];
            while (eu.hasMoreElements()) {
            	
            	ZipEntry ze = (ZipEntry)eu.nextElement();
            	String zeName = ze.getName();
            	
            	if(zeName.endsWith(".meta")){
            		
            		File file = new File(unZipDir+"/metadata.meta");
            		if(!file.exists()){
            			file.createNewFile();
            		}
            		
            		FileOutputStream fos = new FileOutputStream(file);
            		BufferedOutputStream bos = new BufferedOutputStream(fos);
            		InputStream is = zipFile.getInputStream(ze);
            		int length=-1;
                    while(true){
                        length=is.read(buffer);
                        if(length==-1){
                        	break;
                        }
                        bos.write(buffer,0,length);
                    }
                    bos.close(); 
                    is.close();
            		
            	}else if(zeName.endsWith(".clob")){
            		
            		File file = new File(unZipDir+"/clobcontent.clob");
            		if(!file.exists()){
            			file.createNewFile();
            		}
            		
            		FileOutputStream fos = new FileOutputStream(file);
            		BufferedOutputStream bos = new BufferedOutputStream(fos);
            		InputStream is = zipFile.getInputStream(ze);
            		int length=-1;
                    while(true){
                        length=is.read(buffer);
                        if(length==-1){
                        	break;
                        }
                        bos.write(buffer,0,length);
                    }
                    bos.close();
                    is.close();
            		
            	}else if(zeName.endsWith(".key")){
            		
            		File file = new File(unZipDir+"/flag.key");
            		if(!file.exists()){
            			file.createNewFile();
            		}
            		
            		FileOutputStream fos = new FileOutputStream(file);
            		BufferedOutputStream bos = new BufferedOutputStream(fos);
            		InputStream is = zipFile.getInputStream(ze);
            		int length=-1;
                    while(true){
                        length=is.read(buffer);
                        if(length==-1){
                        	break;
                        }
                        bos.write(buffer,0,length);
                    }
                    bos.close();
                    is.close();
            	}
            	
            }
			
		}catch(Exception e){
			NovaLogger.getLogger(ImportInitMetaData.class).debug("", e);
		}
	}
	
	private String readContent(String filePath){
		StringBuffer content = new StringBuffer();
		try{
			
//			File file = new File(filePath);
//			
////			FileReader fr = new FileReader(file);
//			
//			FileInputStream fis = new FileInputStream(file);
//			InputStreamReader isr = new InputStreamReader(fis,"UTF-8"); 
//			BufferedReader br = new BufferedReader(isr);
			
			String tempContent = FileUtil.readFileContent(filePath, "UTF-8");
			content.append(tempContent);

			
		}catch(Exception e){
			NovaLogger.getLogger(ImportInitMetaData.class).debug("", e);
		}
		return content.toString();
	}
	
	private String randomAccessFile(String filePath,String fileName,String[] index){
		StringBuffer content = new StringBuffer();
		try{
			
			RandomAccessFile raf = new RandomAccessFile(filePath+"/"+fileName,"rw");
			
			byte[] buffer = new byte[Integer.parseInt(index[1].trim())];
			
			raf.seek(Long.parseLong(index[0].trim()));
			
			raf.read(buffer,0,Integer.parseInt(index[1].trim()));
			
			content.append(new String(buffer,"UTF-8"));
			
			raf.close();
			
			
		}catch(Exception e){
			NovaLogger.getLogger(ImportInitMetaData.class).debug("", e);
		}
		return content.toString();
	}
	
	public void deleteFielOrDir(File oldPath) {
		if (oldPath.isDirectory()) {
			File[] files = oldPath.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isFile()) {
						file.delete();
					} else {
						deleteFielOrDir(file);
						file.delete();
					}
				}
			}
		}
		oldPath.delete();
	}
	
	public void deleteFile(File oldPath){

		if (oldPath.isDirectory()) {
			File[] files = oldPath.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isFile()) {
						file.delete();
					} else {
						deleteFile(file);
					}
				}
			}
		}else{
			oldPath.delete();
		}
		
	}
	
	private String createCondition(Element dataElement,String[] visiblePkNames,String parentIdSql ){
		StringBuffer condition = new StringBuffer();
		try{

			for(int i=0; i<visiblePkNames.length ; i++){
				String tempPkName = visiblePkNames[i].toLowerCase();
				if(tempPkName.indexOf("{")==0 && tempPkName.indexOf("}")==tempPkName.length()-1){
					if(!isEmpty(parentIdSql)){
						tempPkName = tempPkName.replaceAll("\\{", "");
						tempPkName = tempPkName.replaceAll("\\}", "");
						condition.append(" and "+tempPkName+" = "+parentIdSql);
					}
				}else{
					condition.append(" and "+tempPkName+" = "+StringUtil.strToSQLValue(dataElement.attributeValue(tempPkName)));
				}
			}
			
		}catch(Exception e){
			NovaLogger.getLogger(ImportInitMetaData.class).debug("", e);
		}
		return condition.toString();
	}
	
	private boolean isContain(String[] visiblePkNames ,String pkName){
		boolean flag = false;
		try{
			if(visiblePkNames != null ){
				for(String vp : visiblePkNames){
					if(pkName != null){
						if(vp.trim().equalsIgnoreCase(pkName.trim())){
							flag = true;
							break;
						}else{
							flag = false;
						}
					}
				}
			}
		}catch(Exception e){
			NovaLogger.getLogger(ImportInitMetaData.class).debug("", e);
		}
		return flag;
	}
	
	private boolean isEmpty(String str){
		boolean flag = false;
		try{
			if(str == null || str.trim().equals("") || str.trim().equalsIgnoreCase("null")){
				flag = true;
			}
		}catch(Exception e){
			NovaLogger.getLogger(ImportInitMetaData.class).debug("", e);
			flag = true;
		}
		return flag;
	}
	
	public void writeFlagToXML(){
		try{
			
			File file = new File(InitMetaDataConst.SMARTXCONFIG);
			if(file.exists()){
				
				String content = FileUtil.readFileContent(InitMetaDataConst.SMARTXCONFIG, "UTF-8");
				
				Document doc = DocumentHelper.parseText(content.toString());
				Element root = doc.getRootElement();
				Element initMetadataFiles = root.element("init-metadata-files");
				initMetadataFiles.addAttribute("initialize","false");
				
				writeXMLToFile(doc,InitMetaDataConst.SMARTXCONFIG,"UTF-8");
				
			}
		}catch(Exception e){
			NovaLogger.getLogger(ImportInitMetaData.class).debug("", e);
		}
		
	}
	
	private void writeXMLToFile(Document doc,String fullFileName,String charEncode){
		
		try{
			
			File file = new File(fullFileName);
			
			StringWriter sw = new StringWriter();
	        OutputFormat format = OutputFormat.createPrettyPrint();
	        format.setEncoding(charEncode);
	        XMLWriter xmlwriter = new XMLWriter(sw,format);
	        xmlwriter.write(doc);
	        
			Writer writer = new OutputStreamWriter(new FileOutputStream(file), charEncode); 
	        writer.write(sw.toString());
	        writer.flush();
	        writer.close();
			
//	        FileWriter fw = new FileWriter(file);
//	        fw.write(writer.toString());
//	        fw.close();
			
		}catch(Exception e){
			NovaLogger.getLogger(ImportInitMetaData.class).debug("", e);
		}
		
	}

}
