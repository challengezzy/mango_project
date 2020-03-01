package smartx.system.login.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;

import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.vo.*;
import smartx.system.login.vo.*;


public class TestFrame extends JFrame implements BillTreeSelectListener {

    private static final long serialVersionUID = -6062694913715594062L;

    BillTreePanel treePanel = null;

    BillCardPanel card = null;

    public TestFrame() {
        this.setTitle("测试树型面板"); //
        this.setSize(1000, 800); //
        initialize(); //
    }

    private void initialize() {
        this.getContentPane().setLayout(new BorderLayout()); //
        treePanel = new BillTreePanel(new PUB_MENU_CODE_1_VO(), "菜单树", "localname", "id", "PARENTMENUID", true); //创建树型面板!!
        //treePanel.setIconNames(new String[] { "refsearch.gif", "disableleft.gif", "provision_aggregateall.gif" }); //设置图标!!..
        treePanel.queryDataByCondition("1=1 order by seq"); //
        treePanel.addBillTreeSelectListener(this);

        JSplitPane splitPanel = new JSplitPane();
        splitPanel.setDividerLocation(300); //

        JPanel panel_tmp = new JPanel();
        panel_tmp.setLayout(new BorderLayout()); //
        JButton btn_1 = new JButton("测试");
        btn_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onClicked_1(); //
            }
        });

        panel_tmp.add(btn_1, BorderLayout.NORTH);
        panel_tmp.add(treePanel, BorderLayout.CENTER);

        splitPanel.add(panel_tmp, JSplitPane.LEFT); //

        card = new BillCardPanel(new PUB_MENU_CODE_1_VO());
        card.setEditable(true); //
        splitPanel.add(card, JSplitPane.RIGHT); //

        this.getContentPane().add(splitPanel);

        this.setVisible(true); //
    }

    private void onClicked_1() {
        //BillVO[] vo = treePanel.getSelectedVOs();
        //BillVO[] vos = treePanel.getSelectedParentPathVOs(); //
        //		BillVO[] vos = treePanel.getAllBillVOs(); //
        //		for (int i = 0; i < vos.length; i++) {
        //			System.out.println(vos[i].getObject("id") + "," + vos[i].getObject("name") + "," + vos[i].getObject("localname")); //
        //		}

        DefaultMutableTreeNode node = treePanel.findNodeByKey("22");
        treePanel.expandOneNode(node); //
        treePanel.getJTree().updateUI();
        //treePanel.myExpandAll();
    }

    public void selectChanged(BillTreeSelectionEvent _event) {
        BillVO vo = _event.getCurrSelectedVO(); //获得当前选中的VO
        card.setBillVO(vo);
    }

}
