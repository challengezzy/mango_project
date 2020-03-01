/**************************************************************************
 * $RCSfile: NovaServicePoolFactory.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:17 $
 **************************************************************************/
package smartx.framework.common.bs;

import java.util.*;

import org.apache.commons.pool.impl.*;

public class NovaServicePoolFactory {

    private static NovaServicePoolFactory factory = null;

    private static Hashtable ht_pool = null;

    private static Hashtable ht_implclass = null;

    private NovaServicePoolFactory() {
        ht_pool = new Hashtable(); //
        ht_implclass = new Hashtable();
    }

    public static NovaServicePoolFactory getInstance() {
        if (factory != null) {
            return factory;
        }

        factory = new NovaServicePoolFactory(); //
        return factory;
    }

    public GenericObjectPool getPool(String _servicename) {
        return (GenericObjectPool) ht_pool.get(_servicename); //
    }

    public void registPool(String _servicename, String _implClassName, GenericObjectPool _pool) {
        ht_pool.put(_servicename, _pool);
        ht_implclass.put(_servicename, _implClassName); //
    }

    public String getImplClassName(String _servicename) {
        return (String) ht_implclass.get(_servicename); //
    }

}

/**************************************************************************
 * $RCSfile: NovaServicePoolFactory.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:17 $
 *
 * $Log: NovaServicePoolFactory.java,v $
 * Revision 1.2  2007/05/31 07:38:17  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:15  qilin
 * no message
 *
 * Revision 1.2  2007/02/10 06:24:01  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/31 07:19:33  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 03:36:24  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
