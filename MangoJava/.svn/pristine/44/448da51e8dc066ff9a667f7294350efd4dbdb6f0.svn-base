
package smartx.publics.form.bs.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.ui.NovaClientEnvironment;
import smartx.framework.common.vo.HashVO;
import smartx.framework.metadata.bs.FrameWorkMetaDataServiceImpl;
import smartx.framework.metadata.vo.ItemVO;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.framework.metadata.vo.Pub_Templet_1_ItemVO;
import smartx.publics.form.bs.service.FormInterceptor;
import smartx.publics.form.bs.service.SmartXFormService;

public class TempletUtil
{
	/**
	 * 导出元原模板为脚本
	 * 
	 * @param templetCode
	 * @return
	 * @throws Exception
	 */
	public static String exportTempletScript( String templetCode ) throws Exception
	{
		CommDMO dmo = new CommDMO();
		try
		{
			String sql = "Select * From PUB_TEMPLET_1 Where templetcode ='" + templetCode + "'";
			HashVO[] hv_data = dmo.getHashVoArrayByDS( null, sql );
			if( hv_data.length == 0 )
			{
				throw new Exception( "要导出的元原模板不存在" );
			}
			sql = "Select t.*,(select name from PUB_TEMPLET_1_ITEM_GROUP g where t.TEMPLETITEMGROUPID=g.id) groupname From PUB_TEMPLET_1_ITEM t Where PK_PUB_TEMPLET_1 in "
					+ "(select pk_pub_templet_1 from pub_templet_1 where templetcode = '" + templetCode + "')";
			HashVO[] hv_item = dmo.getHashVoArrayByDS( null, sql );
			sql = "Select * From PUB_TEMPLET_1_ITEM_GROUP Where templetid in " + "(select pk_pub_templet_1 from pub_templet_1 where templetcode = '" + templetCode + "')";
			HashVO[] hv_group = dmo.getHashVoArrayByDS( null, sql );
			String[] str_keys = hv_data[0].getKeys();// 所有字段名
			StringBuffer result = new StringBuffer();
			result.append( "-- 模板生成脚本[" + templetCode + "]\n" );
			result.append( "-- ================start===========================================================\n" );
			result.append( "-- 删除模板明细数据\n" );
			result.append( "delete pub_templet_1_item where pk_pub_templet_1 in " + "(select pk_pub_templet_1 from pub_templet_1 where templetcode = '" + templetCode + "');\n" );
			result.append( "-- 删除模板分组信息\n" );
			result.append( "delete from PUB_TEMPLET_1_ITEM_GROUP where templetid in " + "(select pk_pub_templet_1 from pub_templet_1 where templetcode = '" + templetCode + "');\n" );
			result.append( "-- 判断是否已经存在模板\n" );
			result.append( "DECLARE TEMPL_COUNT NUMBER(6);\n" );
			result.append( "BEGIN\n" );
			result.append( "  SELECT COUNT(*) INTO TEMPL_COUNT FROM pub_templet_1 where templetcode ='" + templetCode + "';\n" );
			result.append( "  IF TEMPL_COUNT > 0 THEN\n" );
			result.append( "    -- 更新老的模板，ID没有变化\n" );
			result.append( "    UPDATE PUB_TEMPLET_1 SET " );
			String upd = "";
			for( int j = 1; j < str_keys.length; j++ )
			{
				upd += ", " + str_keys[j] + "=" + smartx.publics.form.bs.utils.StringUtil.strToSQLValue( hv_data[0].getStringValue( str_keys[j] ) );

			}
			upd += " WHERE templetcode = '" + templetCode + "';\n";
			result.append( upd.substring( 1 ) );
			result.append( "  ELSE\n" );
			result.append( "    -- 插入新模板，新生成ID\n" );
			result.append( "    Insert Into PUB_TEMPLET_1 (" );
			String str_temp = "";
			String str_value = "s_pub_templet_1.nextval";
			for( int j = 0; j < str_keys.length; j++ )
			{
				str_temp = str_temp + str_keys[j] + ", ";
				if( j > 0 )
				{
					str_value = str_value + ", " + smartx.publics.form.bs.utils.StringUtil.strToSQLValue( hv_data[0].getStringValue( str_keys[j] ) );
				}
			}
			result.append( str_temp.substring( 0, str_temp.length() - 2 ) + ")" );
			result.append( " values (" );
			result.append( str_value + ");" );
			result.append( "\n" );
			result.append( "  END IF;\n" );
			result.append( "  -- 插入模板分组\n" );
			for( int k = 0; k < hv_group.length; k++ )
			{
				result.append( "  Insert Into pub_templet_1_item_group (" );
				str_keys = hv_group[k].getKeys();
				str_temp = "";
				str_value = "";
				// str_value = "s_pub_templet_1_item_group.nextval,";
				// str_value = str_value
				// +
				// "(select PK_PUB_TEMPLET_1  From pub_templet_1 Where TEMPLETCODE = '"
				// + templetCode + "')";
				for( int j = 0; j < str_keys.length; j++ )
				{
					str_temp = str_temp + str_keys[j] + ", ";
					// if (j > 1) {
					if( "ID".equalsIgnoreCase( str_keys[j] ) )
					{
						str_value = str_value + "s_pub_templet_1_item_group.nextval";
					}
					else if( "templetid".equalsIgnoreCase( str_keys[j] ) )
					{
						str_value = str_value + ", " + "(select PK_PUB_TEMPLET_1  From pub_templet_1 Where TEMPLETCODE = '" + templetCode + "')";
					}
					else
						str_value = str_value + ", " + smartx.publics.form.bs.utils.StringUtil.strToSQLValue( hv_group[k].getStringValue( str_keys[j] ) );
					// }
				}
				result.append( str_temp.substring( 0, str_temp.length() - 2 ) + ")" );
				result.append( " values (" );
				result.append( str_value + ");\n" );
			}
			result.append( " -- 插入模板明细\n" );
			for( int k = 0; k < hv_item.length; k++ )
			{
				result.append( " Insert Into pub_templet_1_item (" );
				str_keys = hv_item[k].getKeys();
				str_temp = "";
				str_value = "";
				for( int j = 0; j < str_keys.length; j++ )
				{
					if( "groupname".equalsIgnoreCase( str_keys[j] ) )
						continue;
					str_temp = str_temp + str_keys[j] + ", ";
					// if (j > 1) {
					if( "PK_PUB_TEMPLET_1_ITEM".equalsIgnoreCase( str_keys[j] ) )
					{
						str_value = str_value + "s_pub_templet_1_item.nextval";
					}
					else if( "PK_PUB_TEMPLET_1".equalsIgnoreCase( str_keys[j] ) )
					{
						str_value = str_value + ", " + "(select PK_PUB_TEMPLET_1  From pub_templet_1 Where TEMPLETCODE = '" + templetCode + "')";
					}
					else if( "TEMPLETITEMGROUPID".equalsIgnoreCase( str_keys[j] ) )
					{
						if( hv_item[k].getStringValue( "TEMPLETITEMGROUPID" ) == null )
							str_value += ",null";
						else
							str_value = str_value + ", " + "(select id From pub_templet_1_item_group Where name = '" + hv_item[k].getStringValue( "groupname" ) + "' and TEMPLETID="
									+ "(select PK_PUB_TEMPLET_1  From pub_templet_1 Where TEMPLETCODE = '" + templetCode + "')" + ")";
					}
					else if( "groupname".equalsIgnoreCase( str_keys[j] ) )
						continue;
					else
						str_value = str_value + ", " + smartx.publics.form.bs.utils.StringUtil.strToSQLValue( hv_item[k].getStringValue( str_keys[j] ) );
					// }
				}
				result.append( str_temp.substring( 0, str_temp.length() - 2 ) + ")" );
				result.append( " values (" );
				result.append( str_value + ");\n" );
			}
			result.append( "  commit;\n" );
			result.append( "END;\n" );
			result.append( "/\n" );
			result.append( "-- ================end export of [" + templetCode + "]=============================================================\n" );
			return result.toString();
		}
		finally
		{
			dmo.releaseContext();
		}
	}

	/**
	 * 导出所有元原模板脚本
	 * 
	 * @param templetCode
	 * @return
	 * @throws Exception
	 */
	public static String exportAllTempletScript() throws Exception
	{
		CommDMO dmo = new CommDMO();
		try
		{
			String sql = "select templetcode from pub_templet_1";
			HashVO[] vos = dmo.getHashVoArrayByDS( null, sql );
			StringBuffer result = new StringBuffer();
			for( HashVO vo : vos )
			{
				result.append( exportTempletScript( vo.getStringValue( 0 ) ) );
			}
			return result.toString();
		}
		finally
		{
			dmo.releaseContext();
		}
	}

	private static void updateDataByTempletVOWithoutCommit( Pub_Templet_1VO templetVO, Map<String, Object> dataValue ) throws Exception
	{
		if( templetVO == null )
			throw new IllegalArgumentException( "templetVO不能是null" );
		CommDMO dmo = new CommDMO();
		String ds = templetVO.getDatasourcename();
		SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
		SimpleDateFormat longDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		
		String pk = templetVO.getPkname();
		Object pkValue = dataValue.get( pk );
		if( pkValue == null )
			throw new Exception( "主键数据不能为null" );
		String saveTable = templetVO.getSavedtablename();
		StringBuffer sql = new StringBuffer( "update " + saveTable + " set " );
		int count = 0;
		List<Object> params = new ArrayList<Object>();
		for( Pub_Templet_1_ItemVO templetItemVO : templetVO.getItemVos() )
		{
			if( templetItemVO.getIssave() )
			{
				if( "version".equalsIgnoreCase( templetItemVO.getItemkey() ) )
				{
					sql.append( "version=NVL(version,0) + 1" );
				}
				else if( "clob".equalsIgnoreCase( templetItemVO.getSavedcolumndatatype() ) )
				{
					if( dataValue.get( templetItemVO.getItemkey() ) != null )
						dmo.executeUpdateClobByDS( ds, templetItemVO.getItemkey(), saveTable, pk + "='" + pkValue + "'", (String) dataValue.get( templetItemVO.getItemkey() ) );
					continue;
				}
				else if( "blob".equalsIgnoreCase( templetItemVO.getSavedcolumndatatype() ) )
				{
					// 不支持 blob
					continue;
				}
				else
				{
					sql.append( templetItemVO.getItemkey() + "=?" );
					Object itemValue = dataValue.get( templetItemVO.getItemkey() );
					if( itemValue instanceof ItemVO )
					{
						itemValue = ((ItemVO) itemValue).getId();
					}
					if( "date".equalsIgnoreCase( templetItemVO.getSavedcolumndatatype() ) )
					{
						// 日期格式
						String strValue = (String) itemValue;
						if( strValue != null && strValue.length() > 10)
							itemValue = new Timestamp(longDateFormat.parse(strValue).getTime());
						else if( strValue != null)
							itemValue = new Timestamp(dateFormat.parse(strValue).getTime());
					}
					params.add( itemValue );
				}
				sql.append( "," );
				count++;
			}
		}
		if( count == 0 )// 没有要保存的
			return;
		sql = sql.deleteCharAt( sql.length() - 1 );// 去除最后一个逗号
		sql.append( " where " + pk + "=?" );
		params.add( pkValue );
		dmo.executeUpdateByDS( ds, sql.toString(), params.toArray() );

	}

	/**
	 * 根据元原模板更新数据
	 * 
	 * @param templetVO
	 * @param dataValue
	 * @throws Exception
	 */
	public static void updateDataByTempletVO( Pub_Templet_1VO templetVO, Map<String, Object> dataValue ) throws Exception
	{
		CommDMO dmo = new CommDMO();
		String ds = templetVO.getDatasourcename();
		try
		{
			updateDataByTempletVOWithoutCommit( templetVO, dataValue );
			dmo.commit( ds );
		}
		catch( Exception e )
		{
			dmo.rollback( ds );
			throw e;
		}
		finally
		{
			dmo.releaseContext();
		}

	}
	
	//add by caohenghui --start
	private static void doSomething(String ictClassName, Pub_Templet_1VO templetVO, List<Map<String,Object>> dataValueList) throws Exception
	{
		if( ictClassName != null && !"".equals( ictClassName ) )
		{
			Object formInterceptor = Class.forName( ictClassName ).newInstance();
			if( formInterceptor instanceof FormInterceptor )
				((FormInterceptor) formInterceptor).doSomething(templetVO,dataValueList);
		}
	}
	//add by caohenghui --end
	
	// add by zhangzz 20110402 for smartx-39为元原模板保存添加前置、后置拦截器 begin
	private static void doSomething(String ictClassName, Pub_Templet_1VO templetVO, Map<String, Object> dataValue) throws Exception
	{
		if( ictClassName != null && !"".equals( ictClassName ) )
		{
			Object formInterceptor = Class.forName( ictClassName ).newInstance();
			if( formInterceptor instanceof FormInterceptor )
				((FormInterceptor) formInterceptor).doSomething(templetVO,dataValue);
		}
	}
	
	/**
	 * 根据元原模板更新数据(拦截器)
	 * 
	 * @param templetVO
	 * @param dataValue
	 * @param beforeIct
	 *            前置拦截器类名
	 * @param afterIct
	 *            后拦截器类名
	 * @throws Exception
	 */
	public static void updateDataByTempletVOContainIct( Pub_Templet_1VO templetVO, Map<String, Object> dataValue, String beforeIct, String afterIct ) throws Exception
	{
		CommDMO dmo = new CommDMO();
		String ds = templetVO.getDatasourcename();
		try
		{
			doSomething(beforeIct,templetVO,dataValue);
			updateDataByTempletVOWithoutCommit( templetVO, dataValue );
			doSomething(afterIct,templetVO,dataValue);
			dmo.commit( ds );
		}
		catch( Exception e )
		{
			dmo.rollback( ds );
			throw e;
		}
		finally
		{
			dmo.releaseContext();
		}

	}
	
	/**
	 * 根据元原模板插入数据(拦截器)
	 * 
	 * @param templetVO
	 * @param dataValue
	 * @param beforeIct
	 *            前置拦截器类名
	 * @param afterIct
	 *            后拦截器类名
	 * @throws Exception
	 */
	public static void insertDataByTempletVOContainIct( Pub_Templet_1VO templetVO, Map<String, Object> dataValue, String beforeIct, String afterIct ) throws Exception
	{
		CommDMO dmo = new CommDMO();
		String ds = templetVO.getDatasourcename();
		try
		{
			doSomething(beforeIct,templetVO,dataValue);
			insertDataByTempletVOWithoutCommit( templetVO, dataValue );
			doSomething(afterIct,templetVO,dataValue);
			dmo.commit( ds );
		}
		catch( Exception e )
		{
			dmo.rollback( ds );
			throw e;
		}
		finally
		{
			dmo.releaseContext();
		}
	}
	
	/**
	 * 根据元原模板删除数据(有拦截器)
	 * 
	 * @param templetVO
	 * @param dataValue
	 * @throws Exception
	 */
	public static void deleteDataByTempletVOContainIct( Pub_Templet_1VO templetVO, Map<String, Object> dataValue ,String beforeIct, String afterIct) throws Exception
	{
		CommDMO dmo = new CommDMO();
		String ds = templetVO.getDatasourcename();
		try
		{
			doSomething(beforeIct,templetVO,dataValue);
			deleteDataByTempletVOWithoutCommit( templetVO, dataValue );
			doSomething(afterIct,templetVO,dataValue);
			dmo.commit( ds );
		}
		catch( Exception e )
		{
			dmo.rollback( ds );
			throw e;
		}
		finally
		{
			dmo.releaseContext();
		}
	}

	// add by zhangzz 20110402 for smartx-39为元原模板保存添加前置、后置拦截器 end

	private static void deleteDataByTempletVOWithoutCommit( Pub_Templet_1VO templetVO, Map<String, Object> dataValue ) throws Exception
	{
		if( templetVO == null )
			throw new IllegalArgumentException( "templetVO不能是null" );
		CommDMO dmo = new CommDMO();
		String ds = templetVO.getDatasourcename();
		String pk = templetVO.getPkname();
		Object pkValue = dataValue.get( pk );
		if( pkValue == null )
			throw new Exception( "主键数据不能为null" );
		List<String> listsql=new ArrayList<String>();
		SmartXFormService service=new SmartXFormService();
		for(Pub_Templet_1_ItemVO o : templetVO.getItemVos()){
			if(o.getItemtype().equals("列表框")){
				String[] pragarr=o.getClientrefdesc().split(",");
				String templetCode=pragarr[0];
				String forienKey=pragarr[1];
				String isselfbut=pragarr[2];
				String savetable=service.getTempletVO(templetCode).getSavedtablename();
				if(isselfbut.equals("false")){
					String sql="delete from " + savetable + " where " + forienKey + "="+dataValue.get(pk);
					listsql.add(sql);	
				}else {
					String sql="update " + savetable + " set " + forienKey + "=null  where " + forienKey + "="+dataValue.get(pk);
					listsql.add(sql);
				}
				
			}
		}
		String saveTable = templetVO.getSavedtablename();
		String sql = "delete from " + saveTable + " where " + pk + "="+pkValue;
		listsql.add(sql);
		dmo.executeBatchByDS(ds, listsql);
	}

	/**
	 * 根据元原模板删除数据
	 * 
	 * @param templetVO
	 * @param dataValue
	 * @throws Exception
	 */
	public static void deleteDataByTempletVO( Pub_Templet_1VO templetVO, Map<String, Object> dataValue ) throws Exception
	{
		CommDMO dmo = new CommDMO();
		String ds = templetVO.getDatasourcename();
		try
		{
			deleteDataByTempletVOWithoutCommit( templetVO, dataValue );
			dmo.commit( ds );
		}
		catch( Exception e )
		{
			dmo.rollback( ds );
			throw e;
		}
		finally
		{
			dmo.releaseContext();
		}
	}

	private static void insertDataByTempletVOWithoutCommit( Pub_Templet_1VO templetVO, Map<String, Object> dataValue ) throws Exception
	{
		if( templetVO == null )
			throw new IllegalArgumentException( "templetVO不能是null" );
		CommDMO dmo = new CommDMO();
		String ds = templetVO.getDatasourcename();
		String pk = templetVO.getPkname();
		Object pkValue = dataValue.get( pk );
		if( pkValue == null )
			throw new Exception( "主键数据不能为null" );
		SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
		SimpleDateFormat longDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		
		String saveTable = templetVO.getSavedtablename();
		StringBuffer sql = new StringBuffer( "insert into " + saveTable + "(" );
		int count = 0;
		List<Object> params = new ArrayList<Object>();
		for( Pub_Templet_1_ItemVO templetItemVO : templetVO.getItemVos() )
		{
			if( templetItemVO.getIssave() )
			{
				if( "clob".equalsIgnoreCase( templetItemVO.getSavedcolumndatatype() ) || "blob".equalsIgnoreCase( templetItemVO.getSavedcolumndatatype() ) )
				{
					continue;
				}
				sql.append( templetItemVO.getItemkey() + "," );
				count++;
			}
		}
		if( count == 0 )
			return;
		sql = sql.deleteCharAt( sql.length() - 1 );// 去除最后一个逗号
		sql.append( ") values (" );
		for( Pub_Templet_1_ItemVO templetItemVO : templetVO.getItemVos() )
		{
			if( templetItemVO.getIssave() )
			{
				if( "version".equalsIgnoreCase( templetItemVO.getItemkey() ) )
				{
					sql.append( "1" );
				}
				else if( "clob".equalsIgnoreCase( templetItemVO.getSavedcolumndatatype() ) || "blob".equalsIgnoreCase( templetItemVO.getSavedcolumndatatype() ) )
				{
					continue;
				}
				else
				{
					sql.append( "?" );
					Object itemValue = dataValue.get( templetItemVO.getItemkey() );
					if( itemValue instanceof ItemVO )
					{
						itemValue = ((ItemVO) itemValue).getId();
					}
					if( "date".equalsIgnoreCase( templetItemVO.getSavedcolumndatatype() ) )
					{
						// 日期格式
						String strValue = (String) itemValue;
						if( strValue != null && strValue.length() > 10)
							itemValue = new Timestamp(longDateFormat.parse(strValue).getTime());
						else if( strValue != null)
							itemValue = new Timestamp(dateFormat.parse(strValue).getTime());
							
					}
					params.add( itemValue );
				}
				sql.append( "," );
			}

		}
		sql = sql.deleteCharAt( sql.length() - 1 );// 去除最后一个逗号
		sql.append( ")" );
		dmo.executeUpdateByDS( ds, sql.toString(), params.toArray() );
		// 最后处理一下clob字段
		for( Pub_Templet_1_ItemVO templetItemVO : templetVO.getItemVos() )
		{
			if( templetItemVO.getIssave() )
			{
				if( "clob".equalsIgnoreCase( templetItemVO.getSavedcolumndatatype() ) )
				{
					if( dataValue.get( templetItemVO.getItemkey() ) != null )
						dmo.executeUpdateClobByDS( ds, templetItemVO.getItemkey(), saveTable, pk + "='" + pkValue + "'", (String) dataValue.get( templetItemVO.getItemkey() ) );
				}
			}
		}
	}

	/**
	 * 根据元原模板插入数据
	 * 
	 * @param templetVO
	 * @param dataValue
	 * @throws Exception
	 */
	public static void insertDataByTempletVO( Pub_Templet_1VO templetVO, Map<String, Object> dataValue ) throws Exception
	{
		CommDMO dmo = new CommDMO();
		String ds = templetVO.getDatasourcename();
		try
		{
			insertDataByTempletVOWithoutCommit( templetVO, dataValue );
			dmo.commit( ds );
		}
		catch( Exception e )
		{
			dmo.rollback( ds );
			throw e;
		}
		finally
		{
			dmo.releaseContext();
		}
	}
	
	/**
	 * 根据元原模板批量更新数据
	 * 
	 * @param templetVO
	 * @param dataValue
	 * @throws Exception
	 */
	public static void updateBatchDataByTempletVO( Pub_Templet_1VO templetVO, List<Map<String, Object>> dataValueList ) throws Exception
	{
		CommDMO dmo = new CommDMO();
		String ds = templetVO.getDatasourcename();
		try
		{
			for( Map<String, Object> dataValue : dataValueList )
			{
				String flag = (String) dataValue.get( SmartXFormService.KEYNAME_MODIFYFLAG );
				if( "insert".equalsIgnoreCase( flag ) )
				{
					insertDataByTempletVOWithoutCommit( templetVO, dataValue );
				}
				else if( "delete".equalsIgnoreCase( flag ) )
				{
					deleteDataByTempletVOWithoutCommit( templetVO, dataValue );
				}
				else
				{
					updateDataByTempletVOWithoutCommit( templetVO, dataValue );
				}
			}
			dmo.commit( ds );
		}
		catch( Exception e )
		{
			dmo.rollback( ds );
			throw e;
		}
		finally
		{
			dmo.releaseContext();
		}
	}
	
	//add by caohenghui --start
	public static void updateBatchDataByTempletVO( Pub_Templet_1VO templetVO, List<Map<String, Object>> dataValueList,String beforeIct, String afterIct ) throws Exception{
		
		CommDMO dmo = new CommDMO();
		String ds = templetVO.getDatasourcename();
		try
		{
			doSomething(beforeIct,templetVO,dataValueList);
			updateBatchDataByTempletVO( templetVO, dataValueList );
			doSomething(afterIct,templetVO,dataValueList);
			dmo.commit( ds );
		}
		catch( Exception e )
		{
			dmo.rollback( ds );
			throw e;
		}
		finally
		{
			dmo.releaseContext();
		}
	}
	//add by caohenghui --end

	/**
	 * 批量更新数据(隐含了模板编码)
	 * 
	 * @param templetVO
	 * @param dataValue
	 * @throws Exception
	 */
	public static void updateBatchData( List<Map<String, Object>> dataValueList ) throws Exception
	{
		CommDMO dmo = new CommDMO();
		try
		{
			SmartXFormService formService = new SmartXFormService();
			for( Map<String, Object> dataValue : dataValueList )
			{
				String flag = (String) dataValue.get( SmartXFormService.KEYNAME_MODIFYFLAG );
				String templetCode = (String) dataValue.get( SmartXFormService.KEYNAME_TEMPLETCODE );
				Pub_Templet_1VO templetVO = getTempleteVOByCode(templetCode,formService);
				if(templetVO == null)
					new FrameWorkMetaDataServiceImpl().getPub_Templet_1VO(templetCode,NovaClientEnvironment.getInstance());
				if( "insert".equalsIgnoreCase( flag ) )
				{
					insertDataByTempletVOWithoutCommit( templetVO, dataValue );
				}
				else if( "delete".equalsIgnoreCase( flag ) )
				{
					deleteDataByTempletVOWithoutCommit( templetVO, dataValue );
				}
				else
				{
					updateDataByTempletVOWithoutCommit( templetVO, dataValue );
				}
			}
			dmo.commitAll();
		}
		catch( Exception e )
		{
			dmo.rollbackAll();
			throw e;
		}
		finally
		{
			dmo.releaseContext();
		}
	}
	
	private static Pub_Templet_1VO getTempleteVOByCode(String templetCode,SmartXFormService formService){
		Pub_Templet_1VO templetVO = null;
		try{
				FrameWorkMetaDataServiceImpl service = new FrameWorkMetaDataServiceImpl();
				templetVO = service.getPub_Templet_1VO(templetCode,NovaClientEnvironment.getInstance());
		}catch(Exception e){
		}
		return templetVO;
	}
	
	/**
	 * 根据多个元原模板更新数据
	 * 
	 * @param templetVO
	 * @param dataValue
	 * @throws Exception
	 */
	public static void updateDataByTempletVOs( Pub_Templet_1VO[] templetVOs, Map<String, Object>[] dataValues ) throws Exception
	{
		CommDMO dmo = new CommDMO();
		String ds = null;
		try
		{
			for(int i=0;i<templetVOs.length;i++){
				ds = templetVOs[i].getDatasourcename();
				String flag = (String) dataValues[i].get( SmartXFormService.KEYNAME_MODIFYFLAG );
				if( "insert".equalsIgnoreCase( flag ) )
					insertDataByTempletVOWithoutCommit( templetVOs[i], dataValues[i] );
				else if( "delete".equalsIgnoreCase( flag ) )
					deleteDataByTempletVOWithoutCommit( templetVOs[i], dataValues[i] );
				else
					updateDataByTempletVOWithoutCommit( templetVOs[i], dataValues[i] );
			}
			dmo.commit( ds );
		}
		catch( Exception e )
		{
			dmo.rollback( ds );
			throw e;
		}
		finally
		{
			dmo.releaseContext();
		}

	}
}
