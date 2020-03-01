package smartx.system.login.ui;

import smartx.publics.styletemplet.ui.templet08.*;

/**
 * 角色管理界面
 *
 * @author Administrator
 *
 */
public class RoleConfigFrame extends AbstractTempletFrame08 {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String getChildTableFK() {
        return "ROLEID";
    }

    protected String getTempletTitle() {
        return "角色管理";
    }

    public String getChildTablePK() {
        return "ID";
    }

    public String getChildTableTempletcode() {
        return "PUB_ROLE_NMGOPERATOR_CODE1";
    }

    public String getParentTablePK() {
        return "ID";
    }

    public String getParentTableTempletcode() {
        return "PUB_ROLE_CODE1";
    }

}
