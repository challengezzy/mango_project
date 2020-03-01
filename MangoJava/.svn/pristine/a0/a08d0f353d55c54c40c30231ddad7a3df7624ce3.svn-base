/**************************************************************************
 * $RCSfile: FrameWorkMetaDataServiceImpl.java,v $  $Revision: 1.5 $  $Date: 2008/01/08 00:45:51 $
 **************************************************************************/
package smartx.framework.metadata.bs;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.vo.*;

/**
 * 元数据的主要方法,包括取元数据模板配置数据,取得BillCardPanel与BillListPanel中的数据等,注意多数据源的问题!!
 * @author user
 *
 */
public class FrameWorkMetaDataServiceImpl implements FrameWorkMetaDataService {

    public FrameWorkMetaDataServiceImpl() {
    }

    public Pub_Templet_1VO getPub_Templet_1VO(String _code, NovaClientEnvironment _clientEnv) throws Exception {
        MetaDataDMO metaDMO = new MetaDataDMO();
        return metaDMO.getPub_Templet_1VO(_code, _clientEnv); //
    }

    public Pub_Templet_1VO getPub_Templet_1VO(HashVO _parentVO, HashVO[] _childVOs, NovaClientEnvironment _clientEnv) throws
        Exception {
        MetaDataDMO metaDMO = new MetaDataDMO();
        return metaDMO.getPub_Templet_1VO(_parentVO, _childVOs, new HashVO[]{},_clientEnv); //
    }

    public Pub_QueryTempletVO getPub_QueryTempletVO(String _code, NovaClientEnvironment _clientEnv) throws Exception {
        MetaDataDMO metaDMO = new MetaDataDMO();
        return metaDMO.getPub_QueryTempletVO(_code, _clientEnv); //
    }

    public Pub_QueryTempletVO getPub_QueryTempletVO(HashVO _parentVO, HashVO[] _childVOs,
        NovaClientEnvironment _clientEnv) throws Exception {
        MetaDataDMO metaDMO = new MetaDataDMO();
        return metaDMO.getPub_QueryTempletVO(_parentVO, _childVOs, _clientEnv); //
    }

    public Object[] getBillCardDataByDS(String _datasourcename, String _sql, Pub_Templet_1VO _templetVO,
                                        NovaClientEnvironment _clientEnv) throws Exception {
        if (_datasourcename == null) {
            _datasourcename = NovaServerEnvironment.getInstance().getDefaultDataSourceName();
        }
        MetaDataDMO metaDMO = new MetaDataDMO();
        return metaDMO.getBillCardDataByDS(_datasourcename, _sql, _templetVO, _clientEnv);
    }

    public BillVO[] getBillVOsByDS(String _datasourcename, String _sql, Pub_Templet_1VO _templetVO) throws Exception {
        if (_datasourcename == null) {
            _datasourcename = NovaServerEnvironment.getInstance().getDefaultDataSourceName();
        }
        return new MetaDataDMO().getBillVOsByDS(_datasourcename, _sql, _templetVO); //取得BillVO数组!!!
    }
    
    /**
     * 获得数据行数
     */
    public int getBillListRowCountByDS(String _datasourcename, String _sql, Pub_Templet_1VO _templetVO,
        NovaClientEnvironment _clientEnv) throws Exception {
    	MetaDataDMO metaDMO = new MetaDataDMO();
    	return metaDMO.getBillListRowCountByDS(_datasourcename, _sql, _templetVO, _clientEnv);
    }
    
    public Object[][] getBillListSubDataByDS(String _ds, String _sql,int sRowNum,int eRowNum,Pub_Templet_1VO _templetVO,
        NovaClientEnvironment _clientEnv) throws Exception {
    	MetaDataDMO metaDMO = new MetaDataDMO();
    	return metaDMO.getBillListSubDataByDS(_ds, _sql,sRowNum,eRowNum,_templetVO, _clientEnv);
    }

    public Object[][] getBillListDataByDS(String _datasourcename, String _sql, Pub_Templet_1VO _templetVO,
                                          NovaClientEnvironment _clientEnv) throws Exception {
        if (_datasourcename == null) {
            _datasourcename = NovaServerEnvironment.getInstance().getDefaultDataSourceName();
        }
        MetaDataDMO metaDMO = new MetaDataDMO();
        return metaDMO.getBillListDataByDS(_datasourcename, _sql, _templetVO, _clientEnv);
    }

    public Object[][] getQueryDataByDS(String _datasourcename, String _sql, Pub_Templet_1VO _templetVO,
                                       NovaClientEnvironment _env) throws Exception {
        if (_datasourcename == null) {
            _datasourcename = NovaServerEnvironment.getInstance().getDefaultDataSourceName();
        }
        MetaDataDMO metaDMO = new MetaDataDMO();
        return metaDMO.getQueryDataByDS(_datasourcename, _sql, _templetVO, _env); //
    }

    public void commitBillVOByDS(String _datasourcename, BillVO[] _deleteVOs, BillVO[] _insertVOs, BillVO[] _updateVOs) throws
        Exception {
        if (_datasourcename == null) {
            _datasourcename = NovaServerEnvironment.getInstance().getDefaultDataSourceName();
        }
        MetaDataDMO metaDMO = new MetaDataDMO();
        metaDMO.commitBillVO(_datasourcename, _deleteVOs, _insertVOs, _updateVOs); //
    }

	public BillVO[] getBillVOs(String templetcode, HashVO objs, NovaClientEnvironment clientenv) throws Exception {
		MetaDataDMO metaDMO = new MetaDataDMO();
        return metaDMO.getBillVOs(templetcode, objs, clientenv);
	}

	public BillVO[] getBillVOs(Pub_Templet_1VO _templetVO, HashVO objs) throws Exception {
		MetaDataDMO metaDMO = new MetaDataDMO();
        return metaDMO.getBillVOs(_templetVO, objs);
	}
	public ComBoxItemVO[] resetComBoxItemvo(Pub_Templet_1_ItemVO itemVOS,NovaClientEnvironment _clientEnv) throws Exception{
		MetaDataDMO metaDMO = new MetaDataDMO();
        return metaDMO.resetComBoxItemvo(itemVOS, _clientEnv);
	}
}

/**************************************************************************
 * $RCSfile: FrameWorkMetaDataServiceImpl.java,v $  $Revision: 1.5 $  $Date: 2008/01/08 00:45:51 $
 *
 * $Log: FrameWorkMetaDataServiceImpl.java,v $
 * Revision 1.5  2008/01/08 00:45:51  wangqi
 * 解决分页查询处理
 *
 * Revision 1.4  2007/07/05 12:14:39  sunxf
 * 独立出设置下拉框方法
 *
 * Revision 1.3  2007/07/05 07:10:04  sunxf
 * HASHVO 转换为 billvo
 *
 * Revision 1.2  2007/05/31 07:38:21  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:00:41  qilin
 * no message
 *
 * Revision 1.7  2007/03/30 10:00:07  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/14 06:57:54  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/02 05:02:49  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/28 09:18:08  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/27 06:03:01  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/02/25 02:06:51  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/31 07:38:27  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 03:44:56  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
