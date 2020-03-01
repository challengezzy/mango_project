/**
 * 
 */
package smartx.bam.bs.bvmanager;

import org.apache.log4j.Logger;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EPStatementStateListener;
import com.espertech.esper.core.EPServiceProviderImpl;
import com.espertech.esper.core.EPStatementImpl;
import com.espertech.esper.core.StatementType;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.cep.bs.listener.SmartXCepListener;
import smartx.publics.cep.vo.CEPConst;

/**
 * @author sky
 *
 */
public class BvSmartXCepListener implements SmartXCepListener {
	private Logger logger = NovaLogger.getLogger(this.getClass());

	private BvPersistentWriter bvPersistentWriter;
	/* (non-Javadoc)
	 * @see smartx.publics.cep.bs.listener.SmartXCepListener#process()
	 */
	@Override
	public void process() {
		initBvPersistent();
	}

	//初始化业务视图持久化层
	private void initBvPersistent(){
		logger.debug("初始化业务视图持久化层...");
		CommDMO dmo = new CommDMO();
		BusinessViewManager.bvTableMap.clear();
		String sql = "select code,streamwindowname from bam_businessview where ispersistviewdata = 1";
		try {
			HashVO[] vos = dmo.getHashVoArrayByDS(null, sql);
			for(HashVO vo : vos){
				BusinessViewManager.bvTableMap.put(vo.getStringValue("streamwindowname"), vo.getStringValue("code"));
			}
			bvPersistentWriter = new BvPersistentWriter();
			bvPersistentWriter.start();
			addBvUpdateListener();
		} catch (Exception e) {
			logger.error("初始化业务视图持久化层错误",e);
		}finally
		{
			dmo.releaseContext(null);
		}
		
	}
	
	/**
	 * 业务视图添加监听器
	 * @param providerName
	 * @param epl
	 */
	private void addBvUpdateListener()
	{
		final EPServiceProviderImpl provider = (EPServiceProviderImpl)EPServiceProviderManager.getProvider(CEPConst.DEFAULTPROVIDERNAME_CEP);
		logger.debug("为容器["+CEPConst.DEFAULTPROVIDERNAME_CEP+"]添加持久化所需的监听器");
		provider.addStatementStateListener(new EPStatementStateListener(){
			@Override
			public void onStatementCreate(EPServiceProvider provider,
					EPStatement statement) {
				EPStatementImpl epStatement = (EPStatementImpl)statement;
				if(epStatement.getStatementMetadata().getStatementType() == StatementType.CREATE_WINDOW){
					epStatement.addListener(new BvUpdateListener(bvPersistentWriter));
				}
			}

			@Override
			public void onStatementStateChange(EPServiceProvider arg0,
					EPStatement arg1) {
				
			}
			
		});
	}
}
