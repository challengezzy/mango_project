/**
 * 
 */
package smartx.bam.bs.bvmanager;

import java.util.List;

/**
 * @author sky
 *
 */
public class BvPersitentTask {
	private String windowName;
	
	private List<String> fieldLists;
	
	private List<String> values;

	public String getWindowName() {
		return windowName;
	}

	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}

	public List<String> getFieldLists() {
		return fieldLists;
	}

	public void setFieldLists(List<String> fieldLists) {
		this.fieldLists = fieldLists;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}
}
