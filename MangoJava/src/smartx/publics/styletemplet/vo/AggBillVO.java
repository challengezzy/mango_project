/**************************************************************************
 * $RCSfile: AggBillVO.java,v $  $Revision: 1.3 $  $Date: 2007/06/26 08:04:33 $
 **************************************************************************/
package smartx.publics.styletemplet.vo;

import java.io.*;
import java.util.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.vo.*;


public class AggBillVO implements Serializable {

    private static final long serialVersionUID = -3613852473457906627L;

    private BillVO parenVO = null; //主表的BillVO

    private VectorMap childVOMaps = null;

    public BillVO getParentVO() {
        return this.parenVO;
    }

    public void setParentVO(BillVO _vo) {
        this.parenVO = _vo;
    }

    /**
     * 根据页签的顺序取得新增VO
     * @param _index,页签位置是1,2,3,4,5
     * @return
     */
    public BillVO[] getChildAllVOs(int _index) {
        return (BillVO[]) childVOMaps.get(_index - 1); //取得所有数据
    }

    public void setChildAllVOs(int _index, BillVO[] vos) {
        childVOMaps.set(_index-1, vos);
    }

    /**
     * 根据页签的顺序取得新增VO
     * @param _index,页签位置是1,2,3,4,5
     * @return
     */
    public BillVO[] getChildInsertVOs(int _index) {
        BillVO[] billVOs = getChildAllVOs(_index);
        Vector v_tmp = new Vector(); //
        for (int i = 0; i < billVOs.length; i++) {
            if (billVOs[i].getEditType().equals(NovaConstants.BILLDATAEDITSTATE_INSERT)) {
                v_tmp.add(billVOs[i]);
            }
        }
        return (BillVO[]) v_tmp.toArray(new BillVO[0]); //
    }

    /**
     * 根据页签的顺序取得新增VO
     * @param _index,页签位置是1,2,3,4,5
     * @return
     */
    public BillVO[] getChildDeleteVOs(int _index) {
        BillVO[] billVOs = getChildAllVOs(_index);
        Vector v_tmp = new Vector(); //
        for (int i = 0; i < billVOs.length; i++) {
            if (billVOs[i].getEditType().equals(NovaConstants.BILLDATAEDITSTATE_DELETE)) {
                v_tmp.add(billVOs[i]);
            }
        }
        return (BillVO[]) v_tmp.toArray(new BillVO[0]); //
    }

    /**
     * 根据页签的顺序取得新增VO
     * @param _index,页签位置是1,2,3,4,5
     * @return
     */
    public BillVO[] getChildUpdateVOs(int _index) {
        BillVO[] billVOs = getChildAllVOs(_index);
        Vector v_tmp = new Vector(); //
        for (int i = 0; i < billVOs.length; i++) {
            if (billVOs[i].getEditType().equals(NovaConstants.BILLDATAEDITSTATE_UPDATE)) {
                v_tmp.add(billVOs[i]);
            }
        }
        return (BillVO[]) v_tmp.toArray(new BillVO[0]); //
    }

    public BillVO[] getChildInsertVOs() {
        return getChildInsertVOs(1);
    }

    public BillVO[] getChildDeleteVOs() {
        return getChildDeleteVOs(1);
    }

    public BillVO[] getChildUpdateVOs() {
        return getChildUpdateVOs(1);
    }

    //
    public void setChildVOMaps(VectorMap childVOMaps) {
        this.childVOMaps = childVOMaps;
    }

    //总共有几个页签!!
    public int getChildCount() {
        return childVOMaps.size(); //总共有几个页签!!
    }

    public String[] getChildCodes() {
        return childVOMaps.getKeysAsString(); //返回各页签的模板编码
    }
}
/**************************************************************************
 * $RCSfile: AggBillVO.java,v $  $Revision: 1.3 $  $Date: 2007/06/26 08:04:33 $
 *
 * $Log: AggBillVO.java,v $
 * Revision 1.3  2007/06/26 08:04:33  qilin
 * no message
 *
 * Revision 1.2  2007/05/31 07:39:05  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:46  qilin
 * no message
 *
 * Revision 1.4  2007/03/30 07:09:47  qilin
 * no message
 *
 * Revision 1.3  2007/02/27 06:57:20  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:23:54  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
