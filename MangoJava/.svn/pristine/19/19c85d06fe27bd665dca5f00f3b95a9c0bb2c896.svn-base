/**********************************************************************
 *$RCSfile: SimpleHashVO.java,v $  $Revision: 1.1 $  $Date: 2009/12/24 01:52:33 $
 *********************************************************************/ 
package smartx.framework.common.vo;


import java.util.HashMap;
import java.util.Map;

/**
 * <li>Title: SimpleHashVO.java</li>
 * <li>Description: 简介</li>
 * <li>Project: FlexMap</li>
 * <li>Copyright: Copyright (c) 2009</li>
 * @Company: GXLU. All Rights Reserved.
 * @author xuzhil Of VAS2.Dept
 * @version 1.0
 */
public class SimpleHashVO
{
	private Map tableNameMap = new HashMap();
	private Map dataMap = new HashMap();
	
	public SimpleHashVO(HashVO hashVO){
		if(hashVO == null)
			throw new IllegalArgumentException("hashVO不能为null");
		String[] keys = hashVO.getKeys();
		for(int i=0;i<keys.length;i++){
			String key = keys[i];
			tableNameMap.put(key, hashVO.getTableName(key));
			dataMap.put(key, hashVO.getObjectValue(key));
		}
	}
	
	
	public Map getTableNameMap()
	{
	
		return tableNameMap;
	}


	public void setTableNameMap(Map tableNameMap)
	{
	
		this.tableNameMap = tableNameMap;
	}


	public Map getDataMap()
	{
	
		return dataMap;
	}
	public void setDataMap(Map dataMap)
	{
	
		this.dataMap = dataMap;
	}
	
}

/**********************************************************************
 *$RCSfile: SimpleHashVO.java,v $  $Revision: 1.1 $  $Date: 2009/12/24 01:52:33 $
 *
 *$Log: SimpleHashVO.java,v $
 *Revision 1.1  2009/12/24 01:52:33  xuzhil
 **** empty log message ***
 *
 *Revision 1.1  2009/11/02 06:18:54  xuzhil
 **** empty log message ***
 *
 *********************************************************************/