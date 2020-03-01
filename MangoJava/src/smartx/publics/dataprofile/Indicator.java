package smartx.publics.dataprofile;

/**
 *@author zzy
 *@date Feb 10, 2012
 *@description 指标模板实体
 **/
public class Indicator {
	
	private String id;
	private String code;
	private String name;
	private String comments;
	private String definitionTemplate;//指标模板定义
	private String type;
	private String creator;
	private String extattribute1;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getDefinitionTemplate() {
		return definitionTemplate;
	}
	public void setDefinitionTemplate(String definitionTemplate) {
		this.definitionTemplate = definitionTemplate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getExtattribute1() {
		return extattribute1;
	}
	public void setExtattribute1(String extattribute1) {
		this.extattribute1 = extattribute1;
	}
	
	
}
