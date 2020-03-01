/**************************************************************************
 * $RCSfile: FrameWorkMetaDataService.java,v $  $Revision: 1.5 $  $Date: 2008/01/08 00:46:22 $
 **************************************************************************/
package smartx.framework.metadata.ui;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.bs.MetaDataDMO;
import smartx.framework.metadata.vo.*;

/**
 * 获得元原模板数据
 * @author all
 *
 */
public interface FrameWorkMetaDataService extends NovaRemoteCallServiceIfc {

    /**
     * 取得元数据模板配置数据,永远从默认数据源取!!
     * @param _code
     * @param _clientEnv
     * @return
     * @throws Exception
     */
    public Pub_Templet_1VO getPub_Templet_1VO(String _code, NovaClientEnvironment _clientEnv) throws Exception;

    /**
     * 取得元数据模板配置数据,永远从默认数据源取!!
     * @param _parentVO
     * @param _childVOs
     * @param _clientEnv
     * @return
     * @throws Exception
     */
    public Pub_Templet_1VO getPub_Templet_1VO(HashVO _parentVO, HashVO[] _childVOs, NovaClientEnvironment _clientEnv) throws
        Exception;

    /**
     * 取得查询元数据模板中的配置信息,永远从默认数据源取!!
     * @param _code
     * @param _clientEnv
     * @return
     * @throws Exception
     */
    public Pub_QueryTempletVO getPub_QueryTempletVO(String _code, NovaClientEnvironment _clientEnv) throws Exception; //

    /**
     * 取得查询元数据模板中的配置信息,永远从默认数据源取!!
     * @param _parentVO
     * @param _childVOs
     * @param _clientEnv
     * @return
     * @throws Exception
     */
    public Pub_QueryTempletVO getPub_QueryTempletVO(HashVO _parentVO, HashVO[] _childVOs,
        NovaClientEnvironment _clientEnv) throws Exception;

    /**
     * 取得卡片中的数据,从指定数据源取!
     * @param _datasourcename
     * @param _sql
     * @param _templetVO
     * @param _env
     * @return
     * @throws Exception
     */
    public Object[] getBillCardDataByDS(String _datasourcename, String _sql, Pub_Templet_1VO _templetVO,
                                        NovaClientEnvironment _env) throws Exception;

    /**
     * 取得BillVO数组!!!
     * @param _datasourcename
     * @param _sql
     * @param _templetVO
     * @return
     * @throws Exception
     */
    public BillVO[] getBillVOsByDS(String _datasourcename, String _sql, Pub_Templet_1VO _templetVO) throws Exception; //
    /**
     * 取得BillVO数组!!!
     * @param templetcode
     * @param objs
     * @param clientenv
     * @return
     * @throws Exception
     */
    public BillVO[] getBillVOs(String templetcode, HashVO objs,NovaClientEnvironment clientenv) throws Exception; //
    /**
     * 取billvo
     * @param _templetVO
     * @param objs
     * @return
     * @throws Exception
     */
    public BillVO[] getBillVOs(Pub_Templet_1VO _templetVO,HashVO objs) throws Exception;
    
    /**
     * 取得列表中的数据行数,从指定数据源取!
     * @param _datasourcename
     * @param _sql
     * @param _templetVO
     * @param _clientEnv
     * @return
     * @throws Exception
     */
    public int getBillListRowCountByDS(String _datasourcename, String _sql, Pub_Templet_1VO _templetVO,
                                          NovaClientEnvironment _clientEnv) throws Exception;
    /**
     * 取得列表中的数据片段
     * @param _ds
     * @param _sql
     * @param sRowNum
     * @param eRowNum
     * @param _templetVO
     * @param _clientEnv
     * @return 
     * @throws Exception
     */
    public Object[][] getBillListSubDataByDS(String _ds, String _sql,int sRowNum,int eRowNum, 
    		Pub_Templet_1VO _templetVO,NovaClientEnvironment _clientEnv) throws Exception;
    
    /**
     * 取得列表中的数据,从指定数据源取!
     * @param _datasourcename
     * @param _sql
     * @param _templetVO
     * @param _clientEnv
     * @return
     * @throws Exception
     */
    public Object[][] getBillListDataByDS(String _datasourcename, String _sql, Pub_Templet_1VO _templetVO,
                                          NovaClientEnvironment _clientEnv) throws Exception;

    /**
     * 取得查询模板中的数据,从指定数据源取
     * @param _datasourcename
     * @param _sql
     * @param _templetVO
     * @param _env
     * @return
     * @throws Exception
     */
    public Object[][] getQueryDataByDS(String _datasourcename, String _sql, Pub_Templet_1VO _templetVO,
                                       NovaClientEnvironment _env) throws Exception;

    /**
     * 提交指定数据源中的BillVO!!
     * @param _dsName
     * @param _deleteVOs
     * @param _insertVOs
     * @param _updateVOs
     * @throws Exception
     */
    public void commitBillVOByDS(String _dsName, BillVO[] _deleteVOs, BillVO[] _insertVOs, BillVO[] _updateVOs) throws
        Exception;
    /**
     * 重设下拉框内容
     * @param itemVOS
     * @param _clientEnv
     * @return
     * @throws Exception
     */
	public ComBoxItemVO[] resetComBoxItemvo(Pub_Templet_1_ItemVO itemVOS,NovaClientEnvironment _clientEnv) throws Exception;

}

/**************************************************************************
 * $RCSfile: FrameWorkMetaDataService.java,v $  $Revision: 1.5 $  $Date: 2008/01/08 00:46:22 $
 *
 * $Log: FrameWorkMetaDataService.java,v $
 * Revision 1.5  2008/01/08 00:46:22  wangqi
 * 解决分页查询处理
 *
 * Revision 1.4  2007/07/05 12:14:39  sunxf
 * 独立出设置下拉框方法
 *
 * Revision 1.3  2007/07/05 07:10:04  sunxf
 * HASHVO 转换为 billvo
 *
 * Revision 1.2  2007/05/31 07:38:15  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:37  qilin
 * no message
 *
 * Revision 1.8  2007/03/30 10:00:08  shxch
 * *** empty log message ***
 *
 * Revision 1.7  2007/03/02 05:02:49  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/01 06:11:19  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/02/28 09:18:34  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/27 06:03:00  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/25 02:06:51  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/31 09:37:50  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/31 07:38:26  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:56:14  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
