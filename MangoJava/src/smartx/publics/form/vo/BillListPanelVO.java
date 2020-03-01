/**********************************************************************
 *$RCSfile: BillListPanelVO.java,v $  $Revision: 1.1 $  $Date: 2009/12/24 01:52:33 $
 *********************************************************************/ 
package smartx.publics.form.vo;

import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.framework.metadata.vo.Pub_Templet_1_ItemVO;

import java.util.List;

/**
 * <li>Title: BillListPanelVO.java</li>
 * <li>Description: 简介</li>
 * <li>Project: FlexMap</li>
 * <li>Copyright: Copyright (c) 2009</li>
 * @Company: GXLU. All Rights Reserved.
 * @author xuzhil Of VAS2.Dept
 * @version 1.0
 */
public class BillListPanelVO
{
	private Pub_Templet_1VO templetVO;
	private Pub_Templet_1_ItemVO[] templetItemVOs;
	
	private int tableDataRowCount=0;
	private int tableDataPageCount=0;
	private int rowCountPerPage=40;
	private int currentPage=0;
	private List tableDataValues;
	private String realSQL;

	public int getTableDataRowCount()
	{
	
		return tableDataRowCount;
	}

	public void setTableDataRowCount(int tableDataRowCount)
	{
	
		this.tableDataRowCount = tableDataRowCount;
	}

	public int getTableDataPageCount()
	{
	
		return tableDataPageCount;
	}

	public void setTableDataPageCount(int tableDataPageCount)
	{
	
		this.tableDataPageCount = tableDataPageCount;
	}

	public List getTableDataValues()
	{
	
		return tableDataValues;
	}

	public void setTableDataValues(List tableDataValues)
	{
	
		this.tableDataValues = tableDataValues;
	}



	public int getCurrentPage()
	{
	
		return currentPage;
	}

	public void setCurrentPage(int currentPage)
	{
	
		this.currentPage = currentPage;
	}

	public int getRowCountPerPage()
	{
	
		return rowCountPerPage;
	}

	public void setRowCountPerPage(int rowCountPerPage)
	{
	
		this.rowCountPerPage = rowCountPerPage;
	}

	public Pub_Templet_1VO getTempletVO()
	{
	
		return templetVO;
	}

	public void setTempletVO(Pub_Templet_1VO templetVO)
	{
	
		this.templetVO = templetVO;
	}

	public Pub_Templet_1_ItemVO[] getTempletItemVOs()
	{
	
		return templetItemVOs;
	}

	public void setTempletItemVOs(Pub_Templet_1_ItemVO[] templetItemVOs)
	{
	
		this.templetItemVOs = templetItemVOs;
	}

	public String getRealSQL()
	{
	
		return realSQL;
	}

	public void setRealSQL(String realSQL)
	{
	
		this.realSQL = realSQL;
	}


	
	
}

/**********************************************************************
 *$RCSfile: BillListPanelVO.java,v $  $Revision: 1.1 $  $Date: 2009/12/24 01:52:33 $
 *
 *$Log: BillListPanelVO.java,v $
 *Revision 1.1  2009/12/24 01:52:33  xuzhil
 **** empty log message ***
 *
 *Revision 1.4  2009/11/29 06:27:54  xuzhil
 **** empty log message ***
 *
 *Revision 1.3  2009/11/02 06:18:54  xuzhil
 **** empty log message ***
 *
 *Revision 1.2  2009/10/31 03:53:44  xuzhil
 **** empty log message ***
 *
 *Revision 1.1  2009/10/29 11:51:20  xuzhil
 **** empty log message ***
 *
 *********************************************************************/