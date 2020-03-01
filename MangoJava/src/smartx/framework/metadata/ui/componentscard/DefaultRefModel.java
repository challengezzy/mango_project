/**************************************************************************
 * $RCSfile: DefaultRefModel.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:17 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import smartx.framework.common.ui.*;

public class DefaultRefModel extends AbstractRefModel {

    private static final long serialVersionUID = -805985643150338617L;

    private String str_sql = null;

    private String str_datasourcename = null;

    private DefaultRefModel() {
    }

    public DefaultRefModel(String _sql) {
        this.str_sql = _sql;
        this.str_datasourcename = NovaClientEnvironment.getInstance().getDefaultDatasourceName(); //数据源用默认数据源!!
    }

    public DefaultRefModel(String _datasourcename, String _sql) {
        this.str_datasourcename = _datasourcename;
        this.str_sql = _sql;
    }

    public String getDataSourceName() {
        return str_datasourcename;
    }

    public String getSQL() {
        return str_sql;
    }

}
/**************************************************************************
 * $RCSfile: DefaultRefModel.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:17 $
 *
 * $Log: DefaultRefModel.java,v $
 * Revision 1.2  2007/05/31 07:38:17  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:07  qilin
 * no message
 *
 * Revision 1.2  2007/01/30 05:14:29  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/