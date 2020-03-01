/**************************************************************************
 * $RCSfile: Pub_QueryTempletVO.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:20 $
 **************************************************************************/
package smartx.framework.metadata.vo;

import java.io.*;

public class Pub_QueryTempletVO implements Serializable {
    private static final long serialVersionUID = -4450634920623950116L;

    private String datasourcename = null;

    private String templetcode = null;

    private String templetname = null;

    private String sql = null;

    private String[] itemKeys;

    private Pub_QueryTemplet_ItemVO[] itemVOs = null;

    public Pub_QueryTemplet_ItemVO[] getItemVOs() {
        return itemVOs;
    }

    public void setItemVOs(Pub_QueryTemplet_ItemVO[] itemVOs) {
        this.itemVOs = itemVOs;
    }

    public String[] getItemKeys() {
        return itemKeys;
    }

    public void setItemKeys(String[] itemKeys) {
        this.itemKeys = itemKeys;
    }

    public String getTempletcode() {
        return templetcode;
    }

    public void setTempletcode(String templetcode) {
        this.templetcode = templetcode;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getTempletname() {
        return templetname;
    }

    public void setTempletname(String templetname) {
        this.templetname = templetname;
    }

    public Pub_QueryTemplet_ItemVO getQueryTempletItemVObyKey(String _key) {
        if (_key == null || _key.equals("")) {
            return null;
        }
        for (int i = 0; i < itemVOs.length; i++) {
            if (itemVOs[i].getItemKey().equalsIgnoreCase(_key)) {
                return itemVOs[i];
            }
        }
        return null;
    }

    public String getDataSourceName() {
        return datasourcename;
    }

    public void setDataSourceName(String _datasourcename) {
        this.datasourcename = _datasourcename;
    }
}
/**************************************************************************
 * $RCSfile: Pub_QueryTempletVO.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:20 $
 *
 * $Log: Pub_QueryTempletVO.java,v $
 * Revision 1.2  2007/05/31 07:38:20  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:03:16  qilin
 * no message
 *
 * Revision 1.3  2007/03/02 05:02:51  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:16:43  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
