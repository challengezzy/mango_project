/**************************************************************************
 * $RCSfile: AbstractTempletVO.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:20 $
 **************************************************************************/
package smartx.framework.metadata.vo.templetvo;

import java.io.*;

import smartx.framework.common.vo.*;


/**
 * 所有模板数据VO的抽象类!!!!
 * @author user
 *
 */
public abstract class AbstractTempletVO implements Serializable {

    /**
     * 主表VO
     * @return
     */
    public abstract HashVO getPub_templet_1Data();

    /**
     * 子表VO
     * @return
     */
    public abstract HashVO[] getPub_templet_1_itemData();

}
/**************************************************************************
 * $RCSfile: AbstractTempletVO.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:20 $
 *
 * $Log: AbstractTempletVO.java,v $
 * Revision 1.2  2007/05/31 07:38:20  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:03:52  qilin
 * no message
 *
 * Revision 1.3  2007/02/27 06:03:01  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:12:30  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/