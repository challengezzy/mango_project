package smartx.framework.common.vo;

public class ClassFileVO implements Cloneable, java.io.Serializable {
    private String m_classFileName = null; // 类文件名称，不包含包名称

    private byte[] m_byteCodes = null;

    /**
     * ClassFileVO 构造子注解。
     */
    public ClassFileVO() {
        super();
    }

    public byte[] getByteCodes() {
        return m_byteCodes;
    }

    public String getClassFileName() {
        return m_classFileName;
    }

    public void setByteCodes(byte[] byteCodes) {
        m_byteCodes = byteCodes;
    }

    public void setClassFileName(String classFileName) {
        m_classFileName = classFileName;
    }
}
