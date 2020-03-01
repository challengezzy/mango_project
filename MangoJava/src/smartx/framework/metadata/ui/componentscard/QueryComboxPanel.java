package smartx.framework.metadata.ui.componentscard;

import smartx.framework.metadata.vo.ComBoxItemVO;
import smartx.framework.metadata.vo.Pub_Templet_1_ItemVO;

/**
 * 为条件列创建所定义的下拉框组件，与其父类下拉框组件的行为一致，但取定义的来源不同
 * 如果不想创建本对象，也可以直接调用new ComboxPanel(key,name, ComBoxItemVOs)
 * @author Administrator
 *
 */
public class QueryComboxPanel extends ComBoxPanel {
	private static final long serialVersionUID = -840334275276382992L;

	/**
     * 根据vo模板创建下拉框组件
     * @param _templetVO
     */
	public QueryComboxPanel(Pub_Templet_1_ItemVO _templetVO) {
        this._vo = _templetVO;

        this.key = this._vo.getItemkey();
        this.name = this._vo.getItemname();
        this.itemVOs = this._vo.getConditionComBoxItemVos();
        this.FIELD_WIDTH=this._vo.getCardwidth().intValue();
        this.COMBOX_WIDTH = this.FIELD_WIDTH - CLEAR_WIDTH; //
        super.init();
    }	
}
