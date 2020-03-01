package smartx.publics.excelexport.vo;

/**
 *@author zzy
 *@date Apr 2, 2012
 **/
public class ExcelItemVo {
	
	private int seq;//列序号
	
	private String column;//对应数据中的英文字段名
	
	private String title;//列标题
	
	private String type;//类型
	
	private int width;//列宽度
	

	public ExcelItemVo(int seq,String column, String title, String type, int width) {
		super();
		this.seq = seq;
		this.column = column;
		this.title = title;
		this.type = type;
		this.width = width;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

}
