package smartx.applet.vo;

//FIXME 与nova.framework.common.vo.ClassFileVO意义相同

public class ClassFileVO implements Cloneable, java.io.Serializable {
	
	private static final long serialVersionUID = 4279624538351895669L;
	private String _className = null;// 类文件名称，不包含包名称
	private byte[] _byteCodes = null;

	/**
	 * ClassFileVO 构造子注解。
	 */
	public ClassFileVO() {
		super();
	}
	
	
	/**
	 * 获得字节序列
	 * @return
	 */
	public byte[] getByteCodes() {
		return this._byteCodes;
	}
	/**
	 * 设置字节序列
	 * @param byteCodes
	 */
	public void setByteCodes(byte[] byteCodes) {
		this._byteCodes = byteCodes;
	}
	

	/**
	 * 获得文件名
	 * @return
	 */
	public String getClassFileName() {
		return this._className;
	}
	/**
	 * 设置文件名
	 * @param classFileName
	 */
	public void setClassFileName(String classFileName) {
		this._className = classFileName;
	}
	
	/**
	 * 返回文件长度
	 * @return
	 */
	public int getFileLength(){
		return (this._byteCodes==null)?0:this._byteCodes.length;
	}
	
}
