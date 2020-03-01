/**************************************************************************
 * $RCSfile: DataSourceFactory.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:16 $
 *
 * $Log: DataSourceFactory.java,v $
 * Revision 1.2  2007/05/31 07:38:16  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:15  qilin
 * no message
 *
 * Revision 1.3  2007/03/01 08:52:15  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/02/27 08:00:38  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/27 07:50:58  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/27 07:44:29  shxch
 * *** empty log message ***
 *
 **************************************************************************/

package smartx.framework.common.bs;

import smartx.framework.metadata.bs.*;

/**
 * 取得数据源
 * @author user
 *
 */
public class DataSourceFactory {

    private static DataSourceFactory factory = new DataSourceFactory();

    private DataSourceFactory() {
    }

    public static DataSourceFactory getInstance() {
        return factory;
    }

    /**
     * 取得数据源名称,如果为空，则返回默认数据源,如果是String类型，则认为是数据源名称，直接返回!!!!
     * @param _obj
     * @return 数据源名称，比如"datasource_gis_default"等
     */
    public synchronized String getDataSourceName(Object _obj) {
        if (_obj == null) { //如果对象为空则直接返回默认数据源!
            return NovaServerEnvironment.getInstance().getDefaultDataSourceName(); //直接返回默认数据源名称!
        }

        if (_obj instanceof String) { //如果对象类型是String,则直接返回之，即认为这本身就是数据源名称!
            String name = (String) _obj;
            if (isOneOfDataSourceName(name)) { //如果其就是一个数据源名称,则优先返回之!!!
                return name; //
            } else { //如果是不是数据源名称,则返回
                String str_bsdsconvertClass = System.getProperty("BSDataSourceConvertUtil"); //看是否定义了转换器
                if (str_bsdsconvertClass == null || str_bsdsconvertClass.trim().equals("")) { //如果没有定义,则返回默认数据源!
                    return NovaServerEnvironment.getInstance().getDefaultDataSourceName(); //直接返回默认数据源名称!
                } else { //如果定义了!
                    try {
                        BSDataSourceConvertUtilIfc convert = (BSDataSourceConvertUtilIfc) Class.forName(
                            str_bsdsconvertClass).newInstance();
                        return convert.convertOBJToDatasourceName(_obj); //返回转换器转换后的数据源名称!
                    } catch (Exception e) {
                        System.out.println("转换数据源名称时发生错误!!原因:");
                        e.printStackTrace();
                        return _obj.toString();
                    }
                }
            }
        } else { //如果不是字符串，是一个Java对象，比如Vector,Properties,HashMap或一个自定义的Java对象，则反射调定义的转换器,取得转换后的数据源名称!
            String str_bsdsconvertClass = System.getProperty("BSDataSourceConvertUtil"); //看是否定义了转换器
            if (str_bsdsconvertClass == null || str_bsdsconvertClass.trim().equals("")) { //如果没有定义,则返回默认数据源!!!
                return NovaServerEnvironment.getInstance().getDefaultDataSourceName(); //直接返回默认数据源名称!
            } else {
                try {
                    BSDataSourceConvertUtilIfc convert = (BSDataSourceConvertUtilIfc) Class.forName(
                        str_bsdsconvertClass).newInstance();
                    return convert.convertOBJToDatasourceName(_obj); //返回转换器转换后的数据源名称!
                } catch (Exception e) {
                    System.out.println("转换数据源名称时发生错误!!原因:");
                    e.printStackTrace();
                    return _obj.toString();
                }
            }
        }
    }

    /**
     * 判断该名称是不是一个数据源的名称!!!!
     * @param _name
     * @return
     */
    private boolean isOneOfDataSourceName(String _name) {
        String[] all_dsNames = NovaServerEnvironment.getInstance().getAllDataSourceNames();
        for (int i = 0; i < all_dsNames.length; i++) {
            if (_name.equals(all_dsNames[i])) {
                return true;
            }
        }
        return false;
    }

}
