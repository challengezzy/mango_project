/**************************************************************************
 * $RCSfile: QuickSearchDialog.java,v $  $Revision: 1.3 $  $Date: 2007/06/19 03:42:31 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import smartx.framework.metadata.ui.*;


public class QuickSearchDialog extends NovaDialog {

    private static final long serialVersionUID = 1L;

    private JTextField filed = null;

    private String[] column;

    private int selectedRow = 0;

    private String name = null;

    private JCheckBox checkbox1 = null;

    private JCheckBox checkbox2 = null;

    private BillListPanel parent = null;

    private ArrayList columnList = null;

    private int count = -1;

    public QuickSearchDialog(BillListPanel _parent, String _name, int _selectedRow, String[] _column) {
        super(_parent, "定位:" + _name, 330, 120);
        this.parent = _parent;
        this.name = _name;
        this.column = _column;
        this.selectedRow = _selectedRow;
        this.setModal(true);
        init();
    }

    private void init() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());
        JLabel namelabel = new JLabel();
        namelabel.setText(name + ":");
        filed = new JTextField(15);
        filed.setBackground(Color.WHITE);
        northPanel.add(namelabel);
        northPanel.add(filed);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout());
        checkbox1 = new JCheckBox();
        checkbox1.setSelected(true);
        checkbox1.setToolTipText("勾选为模糊查询");
        checkbox1.setBackground(Color.WHITE);
        JLabel label1 = new JLabel();
        label1.setText("是否模糊查询");
        checkbox2 = new JCheckBox();
        checkbox2.setBackground(Color.WHITE);
        checkbox2.setToolTipText("勾选为区分大小写");
        JLabel label2 = new JLabel();
        label2.setText("是否区分大小写");
        centerPanel.add(checkbox1);
        centerPanel.add(label1);
        centerPanel.add(checkbox2);
        centerPanel.add(label2);
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        JButton confirmButton = new JButton("确定");
        confirmButton.setPreferredSize(new Dimension(70, 20));
        JButton preButton = new JButton("上一条");
        preButton.setPreferredSize(new Dimension(70, 20));
        JButton nextButton = new JButton("下一条");
        nextButton.setPreferredSize(new Dimension(70, 20));
        JButton allButton = new JButton("所有");
        allButton.setPreferredSize(new Dimension(70, 20));
        southPanel.add(confirmButton);
        southPanel.add(preButton);
        southPanel.add(nextButton);
        southPanel.add(allButton);
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        this.getContentPane().add(mainPanel);
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onConfirm(true, false, false, 0, false);
            }
        });
        preButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onPre();
            }
        });
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onNext();
            }
        });
        allButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onConfirm(false, true, false, 0, false);
            }
        });
        filed.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
                String key = new String(e.getKeyChar() + "");
                if (key.equals("\n")) {
                    onConfirm(true, false, false, 0, false);
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    onCancel();
                }
            }

            public void keyPressed(KeyEvent arg0) {
                // TODO Auto-generated method stub

            }

            public void keyReleased(KeyEvent arg0) {
                onKeyReleased();
            }
        });

        this.addWindowListener(new WindowListener() {

            public void windowActivated(WindowEvent arg0) {
                // TODO Auto-generated method stub

            }

            public void windowClosed(WindowEvent arg0) {
                parent.setSearchcolumn(null);

            }

            public void windowClosing(WindowEvent arg0) {
                // TODO Auto-generated method stub

            }

            public void windowDeactivated(WindowEvent arg0) {
                // TODO Auto-generated method stub

            }

            public void windowDeiconified(WindowEvent arg0) {
                // TODO Auto-generated method stub

            }

            public void windowIconified(WindowEvent arg0) {
                // TODO Auto-generated method stub

            }

            public void windowOpened(WindowEvent arg0) {
                // TODO Auto-generated method stub

            }

        });
        this.setVisible(true);
        filed.requestFocus();
    }

    private void onKeyReleased() {
        onConfirm(false, true, false, 0, true);
    }

    private void onCancel() {
        this.dispose();
    }

    private void onConfirm(boolean confirm, boolean allconfirm, boolean next, int _count, boolean keyEvent) {
        if (columnList != null) {
            columnList.clear();
        } else {
            columnList = new ArrayList();
        }
        if (checkbox1.isSelected() && !checkbox2.isSelected()) {
            for (int i = selectedRow; i < column.length; i++) {
                if (column[i].toLowerCase().indexOf(this.filed.getText().trim().toLowerCase()) >= 0) {
                    Object obcolumn = new Integer(i);
                    columnList.add(obcolumn);
                    if (confirm == true) {
                        break;
                    } else {
                        continue;
                    }
                }
            }
        } else if (!checkbox1.isSelected() && !checkbox2.isSelected()) {
            for (int i = selectedRow; i < column.length; i++) {
                if (column[i].toLowerCase().equalsIgnoreCase(this.filed.getText().trim().toLowerCase())) {
                    Object obcolumn = new Integer(i);
                    columnList.add(obcolumn);
                    if (confirm == true) {
                        break;
                    } else {
                        continue;
                    }
                }
            }
        } else if (!checkbox1.isSelected() && checkbox2.isSelected()) {
            for (int i = selectedRow; i < column.length; i++) {
                if (column[i].equals(this.filed.getText().trim())) {
                    Object obcolumn = new Integer(i);
                    columnList.add(obcolumn);
                    if (confirm == true) {
                        break;
                    } else {
                        continue;
                    }
                }
            }
        } else if (checkbox1.isSelected() && checkbox2.isSelected()) {
            for (int i = selectedRow; i < column.length; i++) {
                if (column[i].indexOf(this.filed.getText().trim()) >= 0) {
                    Object obcolumn = new Integer(i);
                    columnList.add(obcolumn);
                    if (confirm == true) {
                        break;
                    } else {
                        continue;
                    }
                }
            }
        }
        Object[] obColumn = columnList.toArray();
        int[] intColumn = new int[obColumn.length];
        for (int j = 0; j < obColumn.length; j++) {
            intColumn[j] = ( (Integer) obColumn[j]).intValue();
        }
        parent.setSearchcolumn(intColumn);
        if (parent.getTable().getSelectedRowCount() > 0) {
            parent.getTable().clearSelection();
        }
        if (next != true && keyEvent != true) {
            this.dispose();
        }
        if (parent.getSearchcolumn() != null && parent.getSearchcolumn().length > 0) {
            if (next != true) {
                for (int i = 0; i < parent.getSearchcolumn().length; i++) {
                    Rectangle rect = parent.getTable().getCellRect(parent.getSearchcolumn()[i], 0, true);
                    parent.getTable().scrollRectToVisible(rect);
                    parent.getTable().addRowSelectionInterval(parent.getSearchcolumn()[i], parent.getSearchcolumn()[i]);
                }
            } else {
                if (_count < parent.getSearchcolumn().length && _count >= 0) {
                    Rectangle rect = parent.getTable().getCellRect(parent.getSearchcolumn()[_count], 0, true);
                    parent.getTable().scrollRectToVisible(rect);
                    parent.getTable().setRowSelectionInterval(parent.getSearchcolumn()[_count],
                        parent.getSearchcolumn()[_count]);
                } else {
                    if (_count < 0) {
                        count = 0;
                        Rectangle rect = parent.getTable().getCellRect(parent.getSearchcolumn()[0], 0, true);
                        parent.getTable().scrollRectToVisible(rect);
                        parent.getTable().setRowSelectionInterval(parent.getSearchcolumn()[0],
                            parent.getSearchcolumn()[0]);
                        JOptionPane.showMessageDialog(QuickSearchDialog.this, "已经到达首记录!");
                    } else {
                        count = parent.getSearchcolumn().length - 1;
                        Rectangle rect = parent.getTable().getCellRect(parent.getSearchcolumn()[parent.getSearchcolumn().
                            length - 1], 0, true);
                        parent.getTable().scrollRectToVisible(rect);
                        parent.getTable().setRowSelectionInterval(parent.getSearchcolumn()[parent.getSearchcolumn().
                            length - 1], parent.getSearchcolumn()[parent.getSearchcolumn().length - 1]);
                        JOptionPane.showMessageDialog(QuickSearchDialog.this, "已经到达末记录!");
                    }
                }
            }
        }
    }

    private void onNext() {
        count++;
        onConfirm(false, false, true, count, false);
    }

    private void onPre() {
        count--;
        onConfirm(false, false, true, count, false);
    }
}
/**************************************************************************
 * $RCSfile: QuickSearchDialog.java,v $  $Revision: 1.3 $  $Date: 2007/06/19 03:42:31 $
 *
 * $Log: QuickSearchDialog.java,v $
 * Revision 1.3  2007/06/19 03:42:31  qilin
 * MR#:BZM10-84
 *
 * Revision 1.2  2007/05/31 07:38:18  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:07  qilin
 * no message
 *
 * Revision 1.3  2007/02/10 08:51:57  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:14:31  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
