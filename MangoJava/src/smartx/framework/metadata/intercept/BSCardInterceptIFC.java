package smartx.framework.metadata.intercept;

import smartx.framework.metadata.vo.BillVO;

/**
 * 针对BillCardPanel增、删、改操作BS端操作拦截器
 * @author James.W
 */
public interface BSCardInterceptIFC {
	
	/**
	 * 提交记录前拦截处理
	 * @param ds
	 * @param vo
	 * @throws Exception
	 */
    public void dealCommitBefore(String ds, BillVO vo) throws Exception;

    /**
	 * 提交记录后拦截处理
	 * @param ds
	 * @param vo
	 * @throws Exception
	 */
    public void dealCommitAfter(String ds, BillVO vo) throws Exception;

    
}
