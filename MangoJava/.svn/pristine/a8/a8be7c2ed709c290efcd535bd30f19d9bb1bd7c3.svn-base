/**
 * 
 */
package smartx.bam.bs.systemsetting;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;

/**
 * @author sky
 * Description 
 */
public class SysSettingManager
{
	private Logger logger = Logger.getLogger( this.getClass() );
	
	private static SysSettingManager instance = new SysSettingManager();
	
	private Map<String,String> sysSettingCache = new HashMap<String, String>();
	
	private SysSettingManager(){}
	
	public static SysSettingManager getInstance(){
		if(instance ==null)
			instance = new SysSettingManager();
		return instance;
	}
	
	/**
	 * 通过系统参数查询对应的值
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public String findSysSettingByKey(String key) throws Exception
	{
		logger.debug( "通过系统参数["+key+"]查询对应的值" );
		if(sysSettingCache.containsKey( key ))
			return sysSettingCache.get( key );
		else{
			CommDMO comm = new CommDMO();
			String value = null;
			HashVO[] vos;
			try{
				String sql = "select b.value from bam_systemsettings b where b.key=?";
				vos = comm.getHashVoArrayByDS( null, sql ,key);
				if(vos.length >0){
					value = vos[0].getStringValue( "value" );
					sysSettingCache.put( key,  value);
				}
			}catch(Exception e){
				throw e;
			}finally{
				comm.releaseContext( null );
			}
			return value;
		}
	}
	
	/**
	 * 刷新系统参数
	 */
	public void refreshCache()
	{
		logger.debug( "刷新系统参数" );
		sysSettingCache.clear();
	}
}
