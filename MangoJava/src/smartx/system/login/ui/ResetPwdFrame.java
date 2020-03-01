package smartx.system.login.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.ui.*;
import smartx.system.login.vo.*;


/**
 * 该类是实现修改密码的Dialog，可覆盖该类中的一些方法 来实现自己的修改密码的逻辑
 *
 * @author Administrator
 *
 */
public class ResetPwdFrame extends NovaDialog {

    /**
     *
     */
    private static final long serialVersionUID = -9186210344819389340L;

    private JLabel jlb_user = null;

    private JLabel jlb_pwd1 = null;

    private JLabel jlb_pwd2 = null;

    private JLabel jlb_pwd3 = null;

    private JTextField jtf_user = null;

    /**
     * 原始密码
     */
    protected JPasswordField jpf_pwd1 = null;

    /**
     * 新密码
     */
    protected JPasswordField jpf_pwd2 = null;

    /**
     * 重新输入的新密码
     */
    protected JPasswordField jpf_pwd3 = null;

    private JButton jbt_confirm = null;

    private JButton jbt_cancel = null;

    protected SystemLoginServiceIFC ifc = null;

    /**
     * 构造方法
     *
     * @param _parent:母板
     * @param _name:窗口要显示的Title
     */
    public ResetPwdFrame() {
        super("密码修改");
        this.setSize(400, 230);
        this.setLocation(250, 150);
        this.setResizable(false);
        initDialog();
    }

    /**
     * 构造方法
     *
     * @param _parent:母板
     * @param _name:窗口要显示的Title
     */
    public ResetPwdFrame(Container _parent, String _title) {
        super(_title);
        this.setSize(400, 230);
        this.setLocation(250, 150);
        initDialog();
    }

    /**
     * 初始化Dialog
     */
    private void initDialog() {
        jlb_user = getJlb("用户名:");
        jlb_pwd1 = getJlb("原始密码:");
        jlb_pwd2 = getJlb("输入新密码:");
        jlb_pwd3 = getJlb("重新输入新密码:");

        jtf_user = new JTextField( (String) NovaClientEnvironment.getInstance().get("SYS_LOGINUSER_CODE"));
        jtf_user.setPreferredSize(new Dimension(200, 20));
        jtf_user.setEditable(false);
        jpf_pwd1 = getJpf();
        jpf_pwd2 = getJpf();
        jpf_pwd3 = getJpf();
        jpf_pwd2.setToolTipText("请输入6-16位英文(A-Z,a-z)或数字(0-9)或_!@#&*等");
        jpf_pwd3.setToolTipText("请输入6-16位英文(A-Z,a-z)或数字(0-9)或_!@#&*等");

        JPanel jpn_con = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        jpn_con.add(getInfoPanel(jlb_user, jtf_user, new JLabel("")));
        jpn_con.add(getInfoPanel(jlb_pwd1, jpf_pwd1, new JLabel("")));
        jpn_con.add(getInfoPanel(jlb_pwd2, jpf_pwd2, new JLabel("")));
        jpn_con.add(getInfoPanel(jlb_pwd3, jpf_pwd3, new JLabel(""))); // 6-16位，可使用英文(A-Z,a-z)、数字(0-9)

        jbt_confirm = new JButton("确定");
        jbt_confirm.setPreferredSize(new Dimension(70, 20));
        jbt_confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onConfirm();
            }
        });
        jbt_cancel = new JButton("取消");
        jbt_cancel.setPreferredSize(new Dimension(70, 20));
        jbt_cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        JPanel jpn_btn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        jpn_btn.add(jbt_confirm);
        jpn_btn.add(jbt_cancel);
        jpf_pwd1.requestFocus();

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(jpn_con, BorderLayout.CENTER);
        this.getContentPane().add(jpn_btn, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    /**
     * 获得每条信息的面板
     *
     * @param _label
     * @param _field
     * @param _jlbintro
     * @return
     */
    private JPanel getInfoPanel(JLabel _label, JTextField _field, JLabel _jlbintro) {
        JPanel jpn_temp = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        jpn_temp.add(_label);
        jpn_temp.add(_field);
        jpn_temp.add(_jlbintro);
        return jpn_temp;
    }

    /**
     * 获得前面的框体说明
     *
     * @param _text
     * @return
     */
    private JLabel getJlb(String _text) {
        JLabel label = new JLabel(_text);
        label.setPreferredSize(new Dimension(100, 20));
        label.setHorizontalAlignment(JLabel.RIGHT);
        return label;
    }

    /**
     * 获得密码框
     *
     * @return
     */
    private JPasswordField getJpf() {
        JPasswordField field = new JPasswordField();
        field.setPreferredSize(new Dimension(200, 20));
        return field;
    }

    /**
     * 核查原始密码输入的正确性，可覆盖本方法，实现自己的核查逻辑
     *
     * @return,如果为false则说明该原始密码错误
     */
    protected boolean checkUserPwd() {

        LoginInfoVO loginInfo = null;
        try {
            ifc = (SystemLoginServiceIFC) NovaRemoteServiceFactory.getInstance().lookUpService(SystemLoginServiceIFC.class);
            loginInfo = ifc.login(jtf_user.getText(), new String(jpf_pwd1.getPassword()));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (loginInfo.getLoginStatus() == SystemLoginServiceIFC.USER_LOGINOK_TYPE) {
            return true;
        }

        return false;
    }

    /**
     * 在原始密码正确还有新密码无误的情况下，进行保存新密码， 可覆盖本方法，实现自己的保存新密码的逻辑
     */
    protected boolean onSaveNewPwd() throws Exception {
        return ifc.resetPwd(jtf_user.getText(), new String(jpf_pwd2.getPassword()));
    }

    /**
     * 核查填入的数据是否合乎要求
     *
     * @return
     */
    private boolean checkCondition() {
        if (new String(jpf_pwd1.getPassword()).equals("")) {
            JOptionPane.showMessageDialog(this, "请先输入原始密码！");
            return false;
        } else if (new String(jpf_pwd2.getPassword()).equals("")) {
            JOptionPane.showMessageDialog(this, "请输入新密码！");
            return false;
        } else if (new String(jpf_pwd3.getPassword()).equals("")) {
            JOptionPane.showMessageDialog(this, "请再次输入新密码！");
            return false;
        } else if (!new String(jpf_pwd2.getPassword()).equals(new String(jpf_pwd3.getPassword()))) {
            JOptionPane.showMessageDialog(this, "重新输入新密码框和新密码框中的密码不符，请重新核对！");
            jpf_pwd2.setText("");
            jpf_pwd3.setText("");
            jpf_pwd2.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * 处理确定按钮
     */
    private void onConfirm() {
        if (!checkCondition()) {
            return;
        }
        if (!checkUserPwd()) {
            JOptionPane.showMessageDialog(this, "原始密码输入错误，请重新输入！", "输入错误", JOptionPane.ERROR_MESSAGE);
            jpf_pwd1.setText("");
            return;
        }
        try {
            onSaveNewPwd();
            JOptionPane.showMessageDialog(this, "密码修改成功");
            this.dispose();
        } catch (Exception ex) {
            NovaMessage.showException(this, ex);
        }
    }

    /**
     * 处理取消按钮
     */
    private void onCancel() {
        this.dispose();
    }
}
