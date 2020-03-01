package smartx.publics.form.vo;

public class XMLExportObject {
	private String tableName;
	private String pkName;
	private String fkName;
	private String visiblePkName;
	private String datasource;
	private String fetchSql;
	private String fkType;
	private String fkTable;
	private String idToParent;
	private String idToChild;
	private XMLExportObject childObject;
	private XMLExportObject[] childObjects;
	
	private boolean ignoreWhenExists = false;

	public boolean isIgnoreWhenExists() {
		return ignoreWhenExists;
	}
	public void setIgnoreWhenExists(boolean ignoreWhenExists) {
		this.ignoreWhenExists = ignoreWhenExists;
	}
	public XMLExportObject[] getChildObjects() {
		return childObjects;
	}
	public void setChildObjects(XMLExportObject[] childObjects) {
		this.childObjects = childObjects;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getPkName() {
		return pkName;
	}
	public void setPkName(String pkName) {
		this.pkName = pkName;
	}
	public String getFkName() {
		return fkName;
	}
	public void setFkName(String fkName) {
		this.fkName = fkName;
	}
	public String getFetchSql() {
		return fetchSql;
	}
	public void setFetchSql(String fetchSql) {
		this.fetchSql = fetchSql;
	}
	public XMLExportObject getChildObject() {
		return childObject;
	}
	public void setChildObject(XMLExportObject childObject) {
		this.childObject = childObject;
	}
	public String getDatasource() {
		return datasource;
	}
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}
	public String getVisiblePkName() {
		return visiblePkName;
	}
	public void setVisiblePkName(String visiblePkName) {
		this.visiblePkName = visiblePkName;
	}
	public String getFkType() {
		return fkType;
	}
	public void setFkType(String fkType) {
		this.fkType = fkType;
	}
	public String getFkTable() {
		return fkTable;
	}
	public void setFkTable(String fkTable) {
		this.fkTable = fkTable;
	}
	public String getIdToParent() {
		return idToParent;
	}
	public void setIdToParent(String idToParent) {
		this.idToParent = idToParent;
	}
	public String getIdToChild() {
		return idToChild;
	}
	public void setIdToChild(String idToChild) {
		this.idToChild = idToChild;
	}
}
