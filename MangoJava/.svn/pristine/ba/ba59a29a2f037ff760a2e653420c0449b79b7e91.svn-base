package smartx.framework.metadata.bs;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.ui.NovaConstants;
import smartx.framework.metadata.intercept.BSCardInterceptIFC;
import smartx.framework.metadata.intercept.DefaultBSCardIntercept;
import smartx.framework.metadata.ui.BillOperateServiceIFC;
import smartx.framework.metadata.vo.BillVO;

/**
 * Card面板操作处理服务
 * @author James.W
 *
 */
public class BillOperateServiceImpl implements BillOperateServiceIFC {

	public int doCardSave(String ds, String bsIntercept, BillVO vo)
			throws Exception {
		//操作前处理
		getCardBSInterceptImpl(bsIntercept).dealCommitBefore(ds, vo);
		
		//处理更新
		String oper=vo.getRowNumberItemVO().getState();
		String sql=null;
		if(oper.equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_INSERT)){
			sql=vo.getInsertSQL();
		}else if(oper.equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_UPDATE)){
			sql=vo.getUpdateSQL();
		}else if(oper.equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_DELETE)){
			sql=vo.getDeleteSQL();
		}else if(oper.equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_INIT)){
			;//vo是init状态，不需要更新
		}else {
			throw new Exception("不能执行无需更新的操作！");
		}
		int rt=0;
		if(sql!=null){
			rt=(new CommDMO()).executeUpdateByDS(ds, sql);
		}
		
		//操作后处理
		getCardBSInterceptImpl(bsIntercept).dealCommitAfter(ds, vo);
		
		return rt;
	}
	
	/**
	 * 获得Card操作的BS端拦截器
	 * @param bsIntercept 拦截器实现类名
	 * @return BS端拦截器，不会返回空
	 * @throws Exception
	 */
	private BSCardInterceptIFC getCardBSInterceptImpl(String bsIntercept)throws Exception{
		
    		if(bsIntercept==null){
    			//没有设置uiIntercept，启用默认处理
    			return new DefaultBSCardIntercept();        		
        	}
    		
    		try {
				return (BSCardInterceptIFC)Class.forName(bsIntercept).newInstance();
			} catch (InstantiationException e) {
				throw new Exception("不能创建指定的拦截器！");
			} catch (IllegalAccessException e) {
				throw new Exception("不能创建指定的拦截器！");
			} catch (ClassNotFoundException e) {
				throw new Exception("拦截器指定的类不存在！");
			}
	}
	

}
