package smartx.framework.metadata.ui.componentscard;

import smartx.framework.metadata.vo.Pub_Templet_1_ItemVO;

public class QueryUIRefPanel extends UIRefPanel {
	private static final long serialVersionUID = -5132534959388660148L;

	/**
     * 根据vo模板创建参照框组件
     * @param _templetVO
     */
    public QueryUIRefPanel(Pub_Templet_1_ItemVO _templetVO) {
        this._vo = _templetVO; //

        this.key = this._vo.getItemkey(); //
        this.name = this._vo.getItemname(); //
        this.refDesc = this._vo.getConditionRefDesc(); //原始定义

        this.FIELD_WIDTH = this._vo.getCardwidth().intValue(); //默认宽度
        this.UREF_WIDTH = this.FIELD_WIDTH - REF_WIDTH- CLEAR_WIDTH;
        super.initRefDesc();
        super.init();
    }
}
