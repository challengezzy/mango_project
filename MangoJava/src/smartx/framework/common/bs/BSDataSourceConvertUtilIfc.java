/**************************************************************************
 * $RCSfile: BSDataSourceConvertUtilIfc.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:16 $
 *
 * $Log: BSDataSourceConvertUtilIfc.java,v $
 * Revision 1.2  2007/05/31 07:38:16  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:15  qilin
 * no message
 *
 * Revision 1.3  2007/02/27 08:01:06  shxch
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

/**
 * 数据源转换工具，即根据传入的对象，根据实际的业务转换对应的数据源!
 * 比如根据模板编码，区域编码两者算出对应的数据源!
 * @author user
 *
 */
public interface BSDataSourceConvertUtilIfc {

    /**
     * 将对象转换成数据源名称!
     * @param _obj
     * @return
     */
    public String convertOBJToDatasourceName(Object _obj); //

}
