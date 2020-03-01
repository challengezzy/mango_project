package smartx.publics.styletemplet.bs;

import java.util.*;

import smartx.framework.common.vo.*;
import smartx.framework.metadata.vo.*;
import smartx.publics.styletemplet.bs.templet01.*;
import smartx.publics.styletemplet.bs.templet02.*;
import smartx.publics.styletemplet.bs.templet03.*;
import smartx.publics.styletemplet.bs.templet04.*;
import smartx.publics.styletemplet.bs.templet05.*;
import smartx.publics.styletemplet.bs.templet06.*;
import smartx.publics.styletemplet.bs.templet07.*;
import smartx.publics.styletemplet.bs.templet08.*;
import smartx.publics.styletemplet.bs.templet09.*;
import smartx.publics.styletemplet.ui.*;
import smartx.publics.styletemplet.vo.*;


public class StyleTempletServiceImpl implements StyleTempletServiceIfc {

    //风格模板1
    public HashMap style01_dealCommit(String _dsName, String _bsInterceptName, BillVO[] _insertVOs, BillVO[] _deleteVOs,
                                      BillVO[] _updateVOs) throws Exception {
        return new DMO_01().dealCommit(_dsName, _bsInterceptName, _insertVOs, _deleteVOs, _updateVOs); //
    }

    //风格模板2
    public BillVO style02_dealInsert(String _dsName, String _bsInterceptName, BillVO _vo) throws Exception {
        return new DMO_02().dealInsert(_dsName, _bsInterceptName, _vo);
    }

    public void style02_dealDelete(String _dsName, String _bsInterceptName, BillVO _vo) throws Exception {
        new DMO_02().dealDelete(_dsName, _bsInterceptName, _vo);
    }

    public BillVO style02_dealUpdate(String _dsName, String _bsInterceptName, BillVO _vo) throws Exception {
        return new DMO_02().dealUpdate(_dsName, _bsInterceptName, _vo);
    }

    //
    //风格模板3
    public BillVO style03_dealInsert(String _dsName, String _bsInterceptName, BillVO _vo) throws Exception {
        return new DMO_03().dealInsert(_dsName, _bsInterceptName, _vo);
    }

    public void style03_dealDelete(String _dsName, String _bsInterceptName, BillVO _vo) throws Exception {
        new DMO_03().dealDelete(_dsName, _bsInterceptName, _vo);
    }

    //模板3的级联删除
    public void style03_dealCascadeDelete(String _dsName, String _bsInterceptName, HashVO[] _deleteobj,
                                          String tablename, String field) throws Exception {
        new DMO_03().dealDelete(_dsName, _bsInterceptName, _deleteobj, tablename, field);
    }

    public BillVO style03_dealUpdate(String _dsName, String _bsInterceptName, BillVO _vo) throws Exception {
        return new DMO_03().dealUpdate(_dsName, _bsInterceptName, _vo);
    }

    //
    //风格模板4
    public HashMap style04_dealCommit(String _dsName, String _bsInterceptName, BillVO[] _insertVOs, BillVO[] _deleteVOs,
                                      BillVO[] _updateVOs) throws Exception {
        return new DMO_04().dealCommit(_dsName, _bsInterceptName, _insertVOs, _deleteVOs, _updateVOs); //
    }

	public void style04_dealDelete(String _dsName, String _bsInterceptName, BillVO _vo) throws Exception{
		new DMO_04().dealDelete(_dsName, _bsInterceptName, _vo);
	}
    
    //
    //风格模板5
    public BillVO style05_dealInsert(String _dsName, String _bsInterceptName, BillVO _vo) throws Exception {
        return new DMO_05().dealInsert(_dsName, _bsInterceptName, _vo);
    }

    public void style05_dealDelete(String _dsName, String _bsInterceptName, BillVO _vo) throws Exception {
        new DMO_05().dealDelete(_dsName, _bsInterceptName, _vo);
    }

    public BillVO style05_dealUpdate(String _dsName, String _bsInterceptName, BillVO _vo) throws Exception {
        return new DMO_05().dealUpdate(_dsName, _bsInterceptName, _vo);
    }

    //
    //风格模板6
    public HashMap style06_dealCommit(String _dsName, String _bsInterceptName, BillVO[] _insertVOs, BillVO[] _deleteVOs,
                                      BillVO[] _updateVOs) throws Exception {
        return new DMO_06().dealCommit(_dsName, _bsInterceptName, _insertVOs, _deleteVOs, _updateVOs); //
    }

    //
    //风格模板7
    public BillVO style07_dealInsert(String _dsName, String _bsInterceptName, BillVO _vo) throws Exception {
        return new DMO_07().dealInsert(_dsName, _bsInterceptName, _vo);
    }

    public void style07_dealDelete(String _dsName, String _bsInterceptName, BillVO _vo) throws Exception {
        new DMO_07().dealDelete(_dsName, _bsInterceptName, _vo);
    }

    public BillVO style07_dealUpdate(String _dsName, String _bsInterceptName, BillVO _vo) throws Exception {
        return new DMO_07().dealUpdate(_dsName, _bsInterceptName, _vo);
    }

    //
    //风格模板8
    public AggBillVO style08_dealInsert(String _dsName, String _bsInterceptName, AggBillVO _aggVO) throws Exception {
        return new DMO_08().dealInsert(_dsName, _bsInterceptName, _aggVO); //
    }

    public void style08_dealDelete(String _dsName, String _bsInterceptName, AggBillVO _aggVO) throws Exception {
        new DMO_08().dealDelete(_dsName, _bsInterceptName, _aggVO); //
    }

    public AggBillVO style08_dealUpdate(String _dsName, String _bsInterceptName, AggBillVO _aggVO) throws Exception {
        return new DMO_08().dealUpdate(_dsName, _bsInterceptName, _aggVO); //
    }

    //
    //风格模板9
    public AggBillVO style09_dealInsert(String _dsName, String _bsInterceptName, AggBillVO _aggVO) throws Exception {
        return new DMO_09().dealInsert(_dsName, _bsInterceptName, _aggVO);
    }

    public void style09_dealDelete(String _dsName, String _bsInterceptName, AggBillVO _aggVO) throws Exception {
        new DMO_09().dealDelete(_dsName, _bsInterceptName, _aggVO);
    }

    public AggBillVO style09_dealUpdate(String _dsName, String _bsInterceptName, AggBillVO _aggVO) throws Exception {
        return new DMO_09().dealUpdate(_dsName, _bsInterceptName, _aggVO);
    }

}
