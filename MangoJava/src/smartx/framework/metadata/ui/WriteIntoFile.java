/**************************************************************************
 * $RCSfile: WriteIntoFile.java,v $  $Revision: 1.3.8.1 $  $Date: 2009/02/02 16:12:54 $
 **************************************************************************/
package smartx.framework.metadata.ui;

import java.io.*;

import java.awt.*;
import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.*;


public class WriteIntoFile {
    private final Container con_parent;

    public WriteIntoFile(Container _parent) {
        con_parent = _parent;
    }

    public void writeToCodeVO(String _templetcode, String _codeid, String file_path) {
        String file_name = file_path + "\\" + _templetcode + "_VO.java";

        File out_file = new File(file_name);

        if (out_file.exists()) {
            int re_return = JOptionPane.showConfirmDialog(con_parent, "你要保存的" + _templetcode + "VO.java文件已经存在，要覆盖原文件吗？",
                "文件操作提示", JOptionPane.OK_CANCEL_OPTION);
            if (re_return == 1) {
                return;
            }
        } else {
            try {
                out_file.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(con_parent, "创建" + _templetcode + "VO.java 文件出现异常！");
                e.printStackTrace();
                return;
            }
        }

        try {
            String str_sql = "Select * From Pub_templet_1 where TEMPLETCODE='" + _templetcode + "'";
            HashVO[] hvs = null;
            try {
                hvs = UIUtil.getHashVoArrayByDS(null, str_sql);
            } catch (NovaRemoteException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FileWriter resultFile = new FileWriter(out_file);
            PrintWriter myFile = new PrintWriter(resultFile);

            myFile.println("package gxlu.pluto.vo.framework.templetvos;");
            myFile.println("import java.util.Vector;"); //
            myFile.println("import smartx.framework.metadata.vo.HashVO;");

            myFile.println("");
            myFile.println("public class " + _templetcode + "_VO extends AbstractTempletVO {");
            myFile.println("\tprivate static final long serialVersionUID = 8057184541083294474L;"); //
            myFile.println("");
            myFile.println("\tpublic HashVO getPub_templet_1Data() {");
            myFile.println("\t\tHashVO vo = new HashVO(); //");
            myFile.println("\t\tvo.setAttributeValue(\"templetcode\", " +
                           getTypeValue(hvs[0].getStringValue("TEMPLETCODE")) + "); //模板编码，请勿随便修改");
            myFile.println("\t\tvo.setAttributeValue(\"templetname\", " +
                           getTypeValue(hvs[0].getStringValue("TEMPLETNAME")) + "); //模板名称");
            myFile.println("\t\tvo.setAttributeValue(\"tablename\", " + getTypeValue(hvs[0].getStringValue("TABLENAME")) +
                           "); //查询数据的表(视图)名");
            myFile.println("\t\tvo.setAttributeValue(\"pkname\", " + getTypeValue(hvs[0].getStringValue("PKNAME")) +
                           ");  //主键名");
            myFile.println("\t\tvo.setAttributeValue(\"pksequencename\", " +
                           getTypeValue(hvs[0].getStringValue("PKSEQUENCENAME")) + ");  //序列名");
            myFile.println("\t\tvo.setAttributeValue(\"savedtablename\", " +
                           getTypeValue(hvs[0].getStringValue("SAVEDTABLENAME")) + ");  //保存数据的表名"); //
            myFile.println("\t\tvo.setAttributeValue(\"listcustpanel\"," +
                           getTypeValue(hvs[0].getStringValue("LISTCUSTPANEL")) + ");  //列表自定义面板");
            myFile.println("\t\tvo.setAttributeValue(\"cardcustpanel\"," +
                           getTypeValue(hvs[0].getStringValue("CARDCUSTPANEL")) + ");  //卡片自定义面板");
            myFile.println("\t\treturn vo;");
            myFile.println("\t}");

            String str_item_sql = "Select * From Pub_templet_1_item where PK_PUB_TEMPLET_1='" + _codeid +
                "' order by showorder asc";
            HashVO[] hvs_item = null;
            try {
                hvs_item = UIUtil.getHashVoArrayByDS(null, str_item_sql);
            } catch (NovaRemoteException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            myFile.println("");
            myFile.println("\tpublic HashVO[] getPub_templet_1_itemData() {");
            myFile.println("\t\tVector vector = new Vector();"); //
            myFile.println("\t\tHashVO itemVO = null;");

            for (int i = 0; i < hvs_item.length; i++) {
                myFile.println("");
                myFile.println("\t\titemVO = new HashVO();");
                myFile.println("\t\titemVO.setAttributeValue(\"itemkey\", " +
                               getTypeValue(hvs_item[i].getStringValue("itemkey")) + "); //唯一标识,用于取数与保存");
                myFile.println("\t\titemVO.setAttributeValue(\"itemname\", " +
                               getTypeValue(hvs_item[i].getStringValue("itemname")) + "); //显示名称");
                myFile.println("\t\titemVO.setAttributeValue(\"itemtype\", " +
                               getTypeValue(hvs_item[i].getStringValue("itemtype")) + "); //控件类型");
                myFile.println("\t\titemVO.setAttributeValue(\"comboxdesc\"," +
                               getTypeValue(hvs_item[i].getStringValue("comboxdesc")) + "); //下拉框定义");
                myFile.println("\t\titemVO.setAttributeValue(\"refdesc\"," +
                               getTypeValue(hvs_item[i].getStringValue("refdesc")) + "); //参照定义");
                myFile.println("\t\titemVO.setAttributeValue(\"issave\", " +
                               getTypeValue(hvs_item[i].getStringValue("issave")) + "); //是否参与保存(Y,N)");
                myFile.println("\t\titemVO.setAttributeValue(\"isdefaultquery\", " +
                               getTypeValue(hvs_item[i].getStringValue("isdefaultquery")) +
                               "); //1-快速查询;2-通用查询;3-不参与查询");
                myFile.println("\t\titemVO.setAttributeValue(\"ismustinput\", " +
                               getTypeValue(hvs_item[i].getStringValue("ismustinput")) + "); //是否必输项(Y,N)");
                myFile.println("\t\titemVO.setAttributeValue(\"loadformula\"," +
                               getTypeValue(hvs_item[i].getStringValue("loadformula")) + "); //加载公式");
                myFile.println("\t\titemVO.setAttributeValue(\"editformula\", " +
                               getTypeValue(hvs_item[i].getStringValue("editformula")) + "); //编辑公式");
                myFile.println("\t\titemVO.setAttributeValue(\"defaultvalueformula\", " +
                               getTypeValue(hvs_item[i].getStringValue("defaultvalueformula")) + "); //默认值公式");
                myFile.println("\t\titemVO.setAttributeValue(\"colorformula\", " +
                               getTypeValue(hvs_item[i].getStringValue("colorformula")) + "); //颜色公式");
                myFile.println("\t\titemVO.setAttributeValue(\"listwidth\", " +
                               getTypeValue(hvs_item[i].getStringValue("listwidth")) + "); //列表是宽度");
                myFile.println("\t\titemVO.setAttributeValue(\"cardwidth\",  " +
                               getTypeValue(hvs_item[i].getStringValue("cardwidth")) + "); //卡片时宽度");
                myFile.println("\t\titemVO.setAttributeValue(\"listisshowable\", " +
                               getTypeValue(hvs_item[i].getStringValue("listisshowable")) + "); //列表时是否显示(Y,N)");
                myFile.println("\t\titemVO.setAttributeValue(\"listiseditable\", " +
                               getTypeValue(hvs_item[i].getStringValue("listiseditable")) +
                               "); //列表时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用");
                myFile.println("\t\titemVO.setAttributeValue(\"cardisshowable\", " +
                               getTypeValue(hvs_item[i].getStringValue("cardisshowable")) + "); //卡片时是否显示(Y,N)");
                myFile.println("\t\titemVO.setAttributeValue(\"cardiseditable\", " +
                               getTypeValue(hvs_item[i].getStringValue("cardiseditable")) +
                               "); //卡片时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用");
                myFile.println("\t\tvector.add(itemVO);");
                myFile.println("");
            }

            myFile.println("\t\treturn (HashVO[])vector.toArray(new HashVO[0]);");
            myFile.println("\t}");
            myFile.println("}");
            myFile.close();
            resultFile.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(con_parent, "写入" + _templetcode + "VO.java 文件出现异常！", "操作提示",
                                          JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
        }

    }

    private String getTypeValue(String _value) {
        if (_value == null || _value.trim().equals("")) {
            return "null";
        } else {
            _value = StringUtil.replaceAll(_value, "\"", "\\\"");
            return "\"" + _value + "\"";
        }

    }

    public void writeToCodeVO(String _tablename, String _templetcode, String _templetname, String file_path) {

        String file_name = file_path + "\\" + _templetcode + "_VO.java";

        File out_file = new File(file_name);

        if (out_file.exists()) {
            int re_return = JOptionPane.showConfirmDialog(con_parent, "你要保存的" + _templetcode + "VO.java文件已经存在，要覆盖原文件吗？",
                "文件操作提示", JOptionPane.OK_CANCEL_OPTION);
            if (re_return == 1) {
                return;
            }
        } else {
            try {
                out_file.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(con_parent, "创建" + _templetcode + "VO.java 文件出现异常！");
                e.printStackTrace();
                return;
            }
        }
        try {
            String str_sql = "Select COLUMN_NAME,DATA_TYPE,DATA_LENGTH From cols where TABLE_NAME='" + _tablename +
                "' Order by COLUMN_ID";
            String[][] str_table_columns = null;
            try {
                str_table_columns = UIUtil.getStringArrayByDS(null, str_sql);
            } catch (NovaRemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            FileWriter resultFile = new FileWriter(out_file);
            PrintWriter myFile = new PrintWriter(resultFile);

            myFile.println("package gxlu.pluto.vo.framework.templetvos;");
            myFile.println("import smartx.framework.metadata.vo.HashVO;");

            myFile.println("");
            myFile.println("public class " + _templetcode + "_VO extends AbstractTempletVO {");

            myFile.println("");
            myFile.println("\tpublic HashVO getPub_templet_1Data() {");
            myFile.println("\t\tHashVO vo = new HashVO(); //");
            myFile.println("\t\tvo.setAttributeValue(\"templetcode\", \"" + _templetcode + "\"); //模板编码，请勿随便修改");
            myFile.println("\t\tvo.setAttributeValue(\"templetname\", \"" + _templetname + "\"); //在此修改模板名称");
            myFile.println("\t\tvo.setAttributeValue(\"tablename\", \"" + _tablename + "\"); //原始表名");
            myFile.println("\t\tvo.setAttributeValue(\"pkname\", \"pk_" + _tablename + "\");");
            myFile.println("\t\tvo.setAttributeValue(\"pksequencename\", \"s_" + _tablename + "\");");
            myFile.println("\t\tvo.setAttributeValue(\"savedtablename\", \"" + _tablename + "\");");
            myFile.println("\t\tvo.setAttributeValue(\"ui_eventlistener\"," + " null);");
            myFile.println("\t\treturn vo;");
            myFile.println("\t}");

            myFile.println("");
            myFile.println("\tpublic HashVO[] getPub_templet_1_itemData() {");
            myFile.println("\t\tHashVO[] vos = new HashVO[" + str_table_columns.length + "]; //");
            myFile.println("\t\tint li_index = 0;");
            myFile.println("");
            for (int i = 0; i < str_table_columns.length; i++) {
                myFile.println("\t\tvos[li_index] = new HashVO();");
                myFile.println("\t\tvos[li_index].setAttributeValue(\"itemkey\", \"" + str_table_columns[i][0] +
                               "\"); //");
                myFile.println("\t\tvos[li_index].setAttributeValue(\"itemname\", \"" + str_table_columns[i][0] +
                               "\"); //");

                String str_type = this.getType(str_table_columns[i][1], Integer.parseInt(str_table_columns[i][2]));
                myFile.println("\t\tvos[li_index].setAttributeValue(\"itemtype\", \"" + str_type + "\"); //");
                myFile.println("\t\tvos[li_index].setAttributeValue(\"comboxdesc\", null); //");
                myFile.println("\t\tvos[li_index].setAttributeValue(\"refdesc\",null); //");
                myFile.println("\t\tvos[li_index].setAttributeValue(\"issave\", \"" + "Y\"); //");
                myFile.println("\t\tvos[li_index].setAttributeValue(\"isdefaultquery\", \"" + "N\"); //");
                myFile.println("\t\tvos[li_index].setAttributeValue(\"ismustinput\", \"" + "N\"); //");
                myFile.println("\t\tvos[li_index].setAttributeValue(\"loadformula\", null); //");
                myFile.println("\t\tvos[li_index].setAttributeValue(\"editformula\", null); //");
                myFile.println("\t\tvos[li_index].setAttributeValue(\"defaultvalueformula\",null); //");
                myFile.println("\t\tvos[li_index].setAttributeValue(\"colorformula\",null); //"); //
                myFile.println("\t\tvos[li_index].setAttributeValue(\"listwidth\", \"" + "150\"); //"); //
                myFile.println("\t\tvos[li_index].setAttributeValue(\"cardwidth\", \"" + "150\"); //");
                myFile.println("\t\tvos[li_index].setAttributeValue(\"listisshowable\", \"" + "Y\"); //");
                myFile.println("\t\tvos[li_index].setAttributeValue(\"listiseditable\", \"" + "Y\"); //");
                myFile.println("\t\tvos[li_index].setAttributeValue(\"cardisshowable\", \"" + "Y\"); //");
                myFile.println("\t\tvos[li_index].setAttributeValue(\"cardiseditable\", \"" + "Y\"); //");

                myFile.println("\t\tli_index ++;");
                myFile.println("");
            }

            myFile.println("\t\treturn vos;");
            myFile.println("\t}");
            myFile.println("}");
            myFile.close();
            resultFile.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(con_parent, "写入" + _templetcode + "VO.java 文件出现异常！", "操作提示",
                                          JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
        }
    }

    private String getType(String _type, int _length) {

        if (_type.equalsIgnoreCase("VARCHAR2")) {
            return "文本框";
        }
        if (_type.equalsIgnoreCase("NUMBER")) {
            return "数字框";
        }
        if (_type.equalsIgnoreCase("CHAR")) {
            if (_length == 19) {
                return "时间";
            } else if (_length == 10) {
                return "日期";
            } else if (_length == 1) {
                return "勾选框";
            }
        }
        return "文本框";
    }
}
/**************************************************************************
 * $RCSfile: WriteIntoFile.java,v $  $Revision: 1.3.8.1 $  $Date: 2009/02/02 16:12:54 $
 *
 * $Log: WriteIntoFile.java,v $
 * Revision 1.3.8.1  2009/02/02 16:12:54  wangqi
 * *** empty log message ***
 *
 * Revision 1.3  2007/07/02 00:30:26  sunxb
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:16  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:36  qilin
 * no message
 *
 * Revision 1.6  2007/03/07 02:01:55  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/02 05:02:48  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/27 06:03:00  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/27 05:15:54  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:56:13  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
