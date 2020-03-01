package smartx.publics.cep.bs;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;

/**
 * 流应用管理
 * @author gxlx
 *
 */
public class StreamAppManager {
	private Logger logger = NovaLogger.getLogger(this);
	
	public void saveStreamApp(String streamAppXML, String operator) throws Exception{
		if(streamAppXML == null)
			throw new IllegalArgumentException("streamAppXML不能是null");
		Document doc = DocumentHelper.parseText(streamAppXML);
		String name = doc.getRootElement().attributeValue("name");
		if(name == null)
			throw new IllegalArgumentException("streamAppXML的名称不能是null");
		String code = doc.getRootElement().attributeValue("code");
		if(code == null)
			throw new IllegalArgumentException("streamAppXML的编码不能是null");
		logger.info("保存流应用[code="+code+",operator="+operator+"]");
		logger.debug(streamAppXML);
		CommDMO dmo = new CommDMO();
		try {
			String sql = "select 1 from PUB_CEP_STREAMAPP where code=?";
			HashVO[] vos = dmo.getHashVoArrayByDS(null, sql, code);
			if(vos.length == 0){
				logger.debug("新建流应用[code="+code+"]");
				sql = "insert into PUB_CEP_STREAMAPP(id,code,name,sourcexml,creator,modifytime)" +
						"values(s_PUB_CEP_STREAMAPP.nextval,?,?,empty_clob(),?,sysdate)";
				dmo.executeUpdateByDS(null, sql, code,name,operator);
				dmo.executeUpdateClobByDS(null, "sourcexml","PUB_CEP_STREAMAPP","code='"+code+"'",streamAppXML);
			}
			else{
				logger.debug("修改流应用[code="+code+"]");
				sql = "update PUB_CEP_STREAMAPP set name=?,creator=?,modifytime=sysdate where code=?";
				dmo.executeUpdateByDS(null, sql, name, operator, code);
				sql = "select sourcexml from PUB_CEP_STREAMAPP where code='"+code+"' for update";
				dmo.executeUpdateClobByDS(null, "sourcexml","PUB_CEP_STREAMAPP","code='"+code+"'",streamAppXML);
			}
			dmo.commit(null);
		} catch (Exception e) {
			dmo.rollback(null);
			throw e;
		} finally{
			dmo.releaseContext(null);
		}
		logger.info("保存流应用[code="+code+"]完毕");
	}
	
	public String loadStreamAppXML(String code) throws Exception{
		if(code == null)
			throw new IllegalArgumentException("code不能为null");
		logger.info("读取流应用XML[code="+code+"]");
		CommDMO dmo = new CommDMO();
		String xml = null;
		try{
			String sql = "select sourcexml from PUB_CEP_STREAMAPP where code='"+code+"'";
			xml = dmo.readClobDataByDS(null, sql);
		}finally{
			dmo.releaseContext(null);
		}
		logger.info("读取流应用XML[code="+code+"]完毕");
		return xml;
	}
}
