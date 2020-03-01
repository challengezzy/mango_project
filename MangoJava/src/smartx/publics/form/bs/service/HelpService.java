/**
 * 
 */
package smartx.publics.form.bs.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.common.vo.SimpleHashVO;

/**
 * @author caohenghui
 * Jun 29, 2012
 */
public class HelpService {
	
	protected Logger logger = NovaLogger.getLogger(this.getClass());
	
	public Map<String,Object> getHelpInfo(String id){
		Map<String,Object> map = new HashMap<String,Object>();
		CommDMO dmo = new CommDMO();
		try{
			
			if(StringUtil.isEmpty(id)){
				
				String sql = "select t.*,e.code eventcode from pub_helptopic t,pub_helpevent e where t.id=e.helptopicid(+) and t.parentid is null order by t.name asc";
				
				HashVO[] vos = dmo.getHashVoArrayByDS(null, sql);
				if(vos != null && vos.length>0){
					
					SimpleHashVO[] simpleVos = new SimpleHashVO[vos.length];
					for (int i = 0; i < vos.length; i++) {
						SimpleHashVO vo = new SimpleHashVO(vos[i]);
						simpleVos[i] = vo;
					}
					map.put("id", "");
					map.put("parentid", "");
					map.put("name", "帮助教程首页");
					map.put("content", "请访问以下的主题浏览感兴趣的内容");
					map.put("imageurl", "images/helpHomePage.png");
					map.put("videourl", "");
					map.put("webpageurl", "");
					map.put("eventcode", "");
					map.put("children", simpleVos);
				}
				
			}else{
				
				String sql = "select t.*,e.code eventcode from pub_helptopic t,pub_helpevent e where t.id=e.helptopicid(+) and t.id=? order by t.name asc";
				
				HashVO[] vos = dmo.getHashVoArrayByDS(null, sql,id);
				if(vos != null && vos.length>0){
					
					map.put("id", vos[0].getStringValue("id"));
					map.put("parentid", vos[0].getStringValue("parentid"));
					map.put("name", vos[0].getStringValue("name"));
					map.put("content", vos[0].getStringValue("content"));
					map.put("imageurl", vos[0].getStringValue("imageurl"));
					map.put("videourl", vos[0].getStringValue("videourl"));
					map.put("webpageurl", vos[0].getStringValue("webpageurl"));
					map.put("eventcode", vos[0].getStringValue("eventcode"));
				}
				
				String sql2 = "select t.*,e.code eventcode from pub_helptopic t,pub_helpevent e where t.id=e.helptopicid(+) and t.parentid=? order by t.name asc";
				
				vos = dmo.getHashVoArrayByDS(null, sql2,id);
				if(vos != null && vos.length>0){
					
					SimpleHashVO[] simpleVos = new SimpleHashVO[vos.length];
					for (int i = 0; i < vos.length; i++) {
						SimpleHashVO vo = new SimpleHashVO(vos[i]);
						simpleVos[i] = vo;
					}
					map.put("children", simpleVos);
				}
				
			}
			
		}catch(Exception e){
			logger.debug("",e);
		}
		
		return map;
	}
	
	public SimpleHashVO[] searchHelpInfo(String name){
		
		SimpleHashVO[] shvos = null;
		CommDMO dmo = new CommDMO();
		try{
			String sql = "select t.*,e.code eventcode from pub_helptopic t,pub_helpevent e where t.id=e.helptopicid(+) and t.name like '%"+name+"%' and rownum<=20 order by t.name asc";
			HashVO[] vos = dmo.getHashVoArrayByDS(null, sql);
			if(vos != null && vos.length>0){
				
				shvos = new SimpleHashVO[vos.length];
				for (int i = 0; i < vos.length; i++) {
					SimpleHashVO vo = new SimpleHashVO(vos[i]);
					shvos[i] = vo;
				}
			}
		}catch(Exception e){
			logger.debug("",e);
		}
		return shvos;
	}

}
