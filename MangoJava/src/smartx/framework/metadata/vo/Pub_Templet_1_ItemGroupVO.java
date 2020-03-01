package smartx.framework.metadata.vo;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 模板项分组
 * @author Administrator
 *
 */
public class Pub_Templet_1_ItemGroupVO implements Serializable{
	private static final long serialVersionUID = 5143645388144586229L;
	
	private String id;
	private String name;
	private Boolean isShow;
	private Boolean isExpand;
	private Pub_Templet_1VO parentTempletVO;
	private List<Pub_Templet_1_ItemVO> itemVOs=new ArrayList<Pub_Templet_1_ItemVO>();
	private Integer seq;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getIsShow() {
		return isShow;
	}
	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}
	public Boolean getIsExpand() {
		return isExpand;
	}
	public void setIsExpand(Boolean isExpand) {
		this.isExpand = isExpand;
	}
	public Pub_Templet_1VO getParentTempletVO() {
		return parentTempletVO;
	}
	public void setParentTempletVO(Pub_Templet_1VO parentTempletVO) {
		this.parentTempletVO = parentTempletVO;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public List<Pub_Templet_1_ItemVO> getItemVOs() {
		return itemVOs;
	}
	public void setItemVOs(List<Pub_Templet_1_ItemVO> itemVOs) {
		this.itemVOs = itemVOs;
	}
	
	
}
