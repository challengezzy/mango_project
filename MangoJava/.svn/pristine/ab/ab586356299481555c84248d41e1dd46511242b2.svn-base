/**
 * 
 */
package smartx.bam.bs.alertmessage;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import smartx.bam.vo.DatabaseConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.form.bs.service.FormInterceptor;
import smartx.publics.form.bs.service.SmartXFormService;

/**
 * @author caohenghui
 *
 */
public class AlertMessageDeleteInterceptor implements FormInterceptor {
	
	private Logger logger = NovaLogger.getLogger(this.getClass());
	
	private CommDMO dmo = null;
	
	private CommDMO getCommDMO() {
		if (dmo == null)
			dmo = new CommDMO();
		return dmo;
	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.Map)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			Map<String, Object> dataValue) throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.List)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			List<Map<String, Object>> dataValueList){
		// TODO Auto-generated method stub
		

		// TODO Auto-generated method stub

		try{
			
			for (Map<String, Object> dataValue : dataValueList) {
				String flag = (String) dataValue.get(SmartXFormService.KEYNAME_MODIFYFLAG);
				String ID = (String)dataValue.get("ID");
				if ("delete".equalsIgnoreCase(flag.trim())){
					
					String deleteSQL ="delete from BAM_MESSAGESUBSCRIBER WHERE ALERTMESSAGEID=?";
					
					this.getCommDMO().executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, deleteSQL, ID);
					
					this.getCommDMO().commit(DatabaseConst.DATASOURCE_DEFAULT);

				}

			}
			
		}catch(Exception e){
			logger.debug("执行SQL语句出现异常!",e);
			try {
				this.getCommDMO().rollback(DatabaseConst.DATASOURCE_DEFAULT);
			} catch (Exception e1) {

				logger.debug("回滚数据库出现异常!",e1);
			}
		}finally{
		
			this.getCommDMO().releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(java.util.Map)
	 */
	@Override
	public void doSomething(Map<String, Object> arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(java.util.List)
	 */
	@Override
	public void doSomething(List<Map<String, Object>> arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
