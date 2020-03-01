/************************************************************************
 * $RCSfile: PrintWork.java,v $  $Revision: 1.6 $  $Date: 2007/10/15 13:49:49 $
 * $Log: PrintWork.java,v $
 * Revision 1.6  2007/10/15 13:49:49  john_liu
 * 2007.10.15 by john_liu
 * MR#: BIZM10-171
 * 代码错误，对打印宽度大时不够强壮
 *
 * Revision 1.5  2007/10/12 03:22:56  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2007/10/10 04:27:04  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/09/11 08:04:31  john_liu
 * no message
 *
 * Revision
 *
 * created by john_liu, 2007.09.11    for MR#: 0000, 迁移孙雪峰代码
 *
 ************************************************************************/


package smartx.publics.print.ui;


import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.JobAttributes;
import java.awt.PrintJob;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import smartx.publics.print.vo.Pub_PrintTempletItemVO;
import smartx.publics.print.vo.Pub_PrintTempletVO;

public class PrintWork
{
    int linxstart_x = 20;

    int linxstart_y = 25;

    int linxend_x = -1;

    int linxend_y = -1;

    int line_space = 25;

    int current_line = 0;

    int space_line_text = 15;

    int offset = 0; // 一个值显示为多行时,以下所有的线都要有的y偏移量.

    HashMap line_data = null; // 打印中一行数据中的多个分行结果.每个元素为一个arraylist

    private Font titleFont = null;

    private Font contentFont = null;

    JFrame frame = null;

    JobAttributes jobattribute = null;

    public PrintWork()
    {
        this.frame = new JFrame();
        frame.setSize( 300, 400 );
    }

    public static void printImage( Image image )
    {
        try
        {
            // 获得打印属性
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            pras.add( new Copies( 1 ) );
            // 获得打印设备
            PrintService pss[] = PrintServiceLookup.lookupPrintServices(
                DocFlavor.INPUT_STREAM.GIF, pras );
            if ( pss.length == 0 )
                throw new RuntimeException( "No printer services available." );

            for ( int i = 0; i < pss.length; i++ )
            {
                System.out.println( pss[i].getName() ); //
            }

            PrintService ps = pss[0];
            // // 获得打印工作
            DocPrintJob job = ps.createPrintJob();
            // FileInputStream fin = new FileInputStream(filename);
            Doc doc = new SimpleDoc( image, DocFlavor.INPUT_STREAM.JPEG, null );
            // // 开始打印
            job.print( doc, pras );
            // fin.close();
        }
        catch ( Exception ie )
        {
            ie.printStackTrace();
        }
    }

    public void print( Pub_PrintTempletVO vo, HashMap data )
        throws Exception
    {
        if ( vo == null || data == null )
            return;
        setProperties( vo );
        if ( jobattribute == null )
            jobattribute = new JobAttributes();
        current_line = 0;
        PrintJob pj = frame.getToolkit().getPrintJob( frame,
            vo.getTitle() + "--打印", jobattribute, null ); // 建立一个打印任务,运行这行代码时会弹出打印设置窗口
        jobattribute.setDialog( JobAttributes.DialogType.NONE );
        if ( pj != null )
        {

            Graphics g = pj.getGraphics();
            g.setFont( titleFont );

            double graphicwidth = g.getClipBounds().getWidth();
            double graphicheigh = g.getClipBounds().getHeight();
            linxend_x = new Double( graphicwidth - linxstart_x ).intValue();
            linxend_y = new Double( graphicheigh - linxstart_y ).intValue();

            g.drawString( vo.getTitle(), 220, linxstart_y-20 );


            //绘制图标
            Image img = this.getImage( "images/logo/icon_print.gif" ).getImage();
            g.drawImage(img,15,5,frame);
            //

            //打印区域和时间 add by cuiyi 20070927
            g.setFont( contentFont );
            FontMetrics fontmetrics = g.getFontMetrics();
            double fontHight = fontmetrics.getStringBounds( vo.getTitle(), g ).getHeight( );
            double fontwidth = fontmetrics.getStringBounds( vo.getHeadStringRight( ), g ).getWidth();

            int rightStart = new Double(( graphicwidth - linxstart_x ) - fontwidth ).intValue()-10;
            //左对齐
            g.drawString( vo.getHeadStringLeft( ), linxstart_x+10, linxstart_y-5);
            //右对齐
            g.drawString( vo.getHeadStringRight( ), rightStart, linxstart_y-5);
            //打印区域和时间 add by cuiyi 20070927  end

            g.drawLine( linxstart_x, linxstart_y, linxend_x, linxstart_y );
            graphicwidth = linxend_x - linxstart_x; // 边框宽度
            Pub_PrintTempletItemVO[] allitemvos = vo.getItemvo();
            ArrayList printeditemvos = new ArrayList();
            int item_length = 0;
            for ( int j = 0; j < allitemvos.length; j++ )
            {
                //如果isshow为N,则跳过
                if ( allitemvos[j].getIsshow() != null && allitemvos[j].getIsshow().equalsIgnoreCase( "N" ) )
                {
                    System.out.println( allitemvos[j].getShowname() + " isshow为N,不打印直接返回" );
                    continue;
                }
                // 如果空时不显示,而值为空,则直接跳过.
                if ( ( data.get( allitemvos[j].getItemkey() ) == null ) && allitemvos[j].getIsshownull().equalsIgnoreCase( "N" ) )
                {
                    System.out.println( allitemvos[j].getShowname() + " 空时不显示,不打印直接返回" );
                    continue;
                }
                int namewidth = getNameWidth( allitemvos[j], new Double( graphicwidth ).intValue() );
                int valuewidth = getValueWidth( allitemvos[j], new Double( graphicwidth ).intValue() );
                //    modified by john_liu, 2007.10.15    for MR#: BIZM10-171    begin
//                if ( namewidth == -1 || valuewidth == -1 )
//                    return;
//                if ( namewidth + valuewidth > graphicwidth )
//                {
//                    System.out.println( allitemvos[j].getShowname() + "总长度超过一行.." );
//                    return;
//                }
                if ( namewidth == -1 || valuewidth == -1 )
                {
//                    return;
                    continue;
                }
                if ( namewidth + valuewidth > graphicwidth )
                {
                    System.out.println( allitemvos[j].getShowname() + "总长度超过一行.." );
//                    return;
                }
                //    modified by john_liu, 2007.10.15    for MR#: BIZM10-171    end
                if ( allitemvos[j].getIsonerow() != null && allitemvos[j].getIsonerow().equalsIgnoreCase( "Y" ) )
                { // 如果独占一行,则先打印前面所有的等待队列.
                    if ( printeditemvos.size() > 0 )
                        drawLineData( g, printeditemvos, data );
                    printeditemvos.clear(); // 清空arraylist
                    // 加入本行,打印.
                    printeditemvos.add( allitemvos[j] );
                    drawLineData( g, printeditemvos, data );
                    printeditemvos.clear(); // 清空arraylist
                    item_length = 0;
                    continue;
                }
                if ( item_length + namewidth + valuewidth < graphicwidth )
                { // 如果长度加和小于总宽度,则作一行处理.
                    printeditemvos.add( allitemvos[j] );
                    item_length += namewidth + valuewidth;
                }
                else
                {
                    drawLineData( g, printeditemvos, data );
                    printeditemvos.clear(); // 清空arraylist
                    item_length = 0; //打印出前面的项,本项加进去
                    printeditemvos.add( allitemvos[j] );
                    item_length += namewidth + valuewidth;
                }
                if ( j == allitemvos.length - 1 ) //如果是最后一个,则打印之
                {
                    drawLineData( g, printeditemvos, data );
                    printeditemvos.clear(); // 清空arraylist
                    item_length = 0;
                }
            }
            ////打印页脚 add by cuiyi 20070927
            //左对齐
//            g.drawString( vo.getTailStringLeft( ), linxstart_x+10, current_line * line_space + linxstart_y + space_line_text + line_space + 70);
            g.drawString( vo.getTailStringLeft( ), linxstart_x+10, ( current_line + 1 ) * line_space + linxstart_y + offset - 10);
            //右对齐
            g.drawString( vo.getTailStringRight( ), rightStart, ( current_line + 1 ) * line_space + linxstart_y + offset - 10);
            ////打印页脚 add by cuiyi 20070927 end
            pj.end();
        }
        offset = 0;
    }

    // 从第一条横线下开始的部分.
    private void drawLineData( Graphics g, ArrayList itemvos, HashMap data )
    {
        if ( itemvos == null || itemvos.size() == 0 )
            return;
        int oldoffset = offset;
        offset += getOffset( g, itemvos, data );
        g.drawLine( linxstart_x, current_line * line_space + linxstart_y + oldoffset,
                    linxstart_x, current_line * line_space + linxstart_y
                    + line_space + offset ); // 左竖线;
        g
            .drawLine( linxend_x, current_line * line_space + linxstart_y + oldoffset,
                       linxend_x, current_line * line_space + linxstart_y + offset
                       + line_space ); // 右竖线;
        int current_width = 0;
        for ( int i = 0; i < itemvos.size(); i++ )
        {
            Pub_PrintTempletItemVO vo = ( Pub_PrintTempletItemVO ) itemvos.get( i );
            if ( i != 0 )
            {
                g.drawLine( linxstart_x + current_width + 5, current_line
                            * line_space + linxstart_y + oldoffset,
                            linxstart_x + current_width + 5, ( current_line + 1 )
                            * line_space + linxstart_y + offset ); // 竖线;
            }
            g.drawString( vo.getShowname(), current_width + linxstart_x + 10,
                          current_line * line_space + linxstart_y + space_line_text + oldoffset ); // 标题
            current_width += Integer.parseInt( vo.getNamewidthabsolute() );
            g.drawLine( linxstart_x + current_width, current_line * line_space + oldoffset
                        + linxstart_y, linxstart_x + current_width,
                        ( current_line + 1 ) * line_space + linxstart_y + offset ); // 竖线;

            String value = "";
            if ( data.get( vo.getItemkey() ) != null )
            {
                // 分行打印值
                ArrayList list = ( ArrayList ) line_data.get( vo.getItemkey() );
                //获取对齐方式时的偏移量
                int line_off = getLineOffset( g, vo, list );
                for ( int j = 0; j < list.size(); j++ )
                {
                    value = ( String ) list.get( j );
                    g.drawString( value, line_off + current_width + linxstart_x + 5, current_line
                                  * line_space + linxstart_y + space_line_text + j * line_space + oldoffset ); // 值
                }
            }
            // 如果不存在此值,则显示为空格.
            else
            {
                g.drawString( value, current_width + linxstart_x + 5, current_line
                              * line_space + linxstart_y + space_line_text + line_space + oldoffset ); // 值
            }

            current_width += Integer.parseInt( vo.getValuewidthabsolute() );
        }
        g.drawLine( linxstart_x, ( current_line + 1 ) * line_space + linxstart_y + offset,
                    linxend_x, ( current_line + 1 ) * line_space + linxstart_y + offset ); // 底线
        current_line++;
    }

    // 左对齐,右对齐,还是居中
    private int getLineOffset( Graphics g, Pub_PrintTempletItemVO vo, ArrayList data )
    {
        if ( vo == null )
            return 0;
        int result = 0;
        FontMetrics fontmetrics = g.getFontMetrics();
        // 获取对齐方式,0,左对齐 1, 居中 2,右对齐
        String align_value = vo.getAligntype();
        if ( align_value == null || align_value.equals( "" ) )
            align_value = "0";
        // 只有一行时设置偏移量
        if ( data.size() == 1 )
        {
            String data_value = ( String ) data.get( 0 );
            if ( align_value.equalsIgnoreCase( "0" ) )
                result = 0;
            else if ( align_value.equalsIgnoreCase( "1" ) )
            {
                double fontwidth = fontmetrics.getStringBounds( data_value, g ).getWidth();
                result = new Double( ( Double.parseDouble( vo.getValuewidthabsolute() ) - fontwidth ) / 2 ).intValue() - 15;
            }
            else if ( align_value.equalsIgnoreCase( "2" ) )
            {
                double fontwidth = fontmetrics.getStringBounds( data_value, g ).getWidth();
                result = new Double( Double.parseDouble( vo.getValuewidthabsolute() ) - fontwidth ).intValue() - 15;
            }
        }
        //多行时说明已经撑满,不用设置对齐方式.
        else
            result = 0;
        return result;
    }

    // 获取值应该打印的行数,顺便获取该行中所有的数据分析结果.
    private int getOffset( Graphics g, ArrayList itemvos, HashMap data )
    {
        if ( itemvos == null || itemvos.size() == 0 )
            return 0;
        if ( line_data == null )
            line_data = new HashMap();
        else
            line_data.clear();
        int[] nums = new int[itemvos.size()];
        for ( int i = 0; i < itemvos.size(); i++ )
        {
            Pub_PrintTempletItemVO vo = ( Pub_PrintTempletItemVO ) itemvos.get( i );
            if ( data.get( vo.getItemkey() ) != null )
            {
                String value = ( String ) data.get( vo.getItemkey() );
                // 取得打印值
                ArrayList sub_value = getLineDatas( g, value, Integer.parseInt( vo.getValuewidthabsolute() ) );
                nums[i] = sub_value.size();
                line_data.put( vo.getItemkey(), sub_value );
            }
        }
        // 取得最大的分行数.
        int max = 1;
        for ( int i = 0; i < nums.length; i++ )
        {
            if ( nums[i] > max )
                max = nums[i];
        }
        // 返回结果
        return ( max - 1 ) * line_space;
    }

    // 截取适合宽度的数据子集.
    private ArrayList getLineDatas( Graphics g, String data, int maxwidth )
    {
        if ( data == null || data.equals( "" ) || g == null )
            return new ArrayList();
        if ( maxwidth <= 0 )
        {
            System.out.println( "最大宽度设置错误" );
            System.exit( 1 );
        }
        ArrayList str_list = new ArrayList();
        FontMetrics fontmetrics = g.getFontMetrics();
        String subString = data;
        String alreadySplit_Str = "";
        do
        {
            alreadySplit_Str = "";
            do
            {
                if ( fontmetrics.getStringBounds( subString, g ).getWidth() <= maxwidth ) // 如果字符串已经小于要求的宽度,直接返回
                {
                    str_list.add( subString );
                    break;
                }
                else
                {
                    subString = subString.substring( 0, subString.length() - 1 ); // 截去最后一位,再比较
                }
            }
            while ( true );

            for ( int i = 0; i < str_list.size(); i++ )
            {
                alreadySplit_Str += str_list.get( i );
            }
            subString = data.substring( alreadySplit_Str.length() );
        }
        while ( !alreadySplit_Str.equals( data ) );
        return str_list;
    }

    // 获取字段的宽度,结果存入绝对宽度中..
    private int getNameWidth( Pub_PrintTempletItemVO itemvo, int graphicwidth )
    {
        int namewidth = -1;
        if ( itemvo.getNamewidthabsolute() == null || itemvo.getNamewidthabsolute().equals( "" ) )
        {
            String percentwidth = itemvo.getNamewidthpercent();
            if ( percentwidth != null && !percentwidth.equals( "" ) )
            {
                if ( Integer.parseInt( percentwidth ) >= 100 )
                {
                    System.out.println( itemvo.getItemkey() + "打印宽度设置有误." );
                    return -1;
                }
                namewidth = new Double( Integer.parseInt( percentwidth ) / 100 * graphicwidth ).intValue();
            }
        }
        else
            namewidth = Integer.parseInt( itemvo.getNamewidthabsolute() );
        itemvo.setNamewidthabsolute( namewidth + "" );
        return namewidth;
    }

// 获取字段值的宽度,结果存入绝对宽度中..
    private int getValueWidth( Pub_PrintTempletItemVO itemvo, int graphicwidth )
    {
        int valuewidth = -1;
        if ( itemvo.getValuewidthabsolute() == null || itemvo.getValuewidthabsolute().equals( "" ) )
        {
            String percentwidth = itemvo.getValuewidthabsolute();
            if ( percentwidth != null && !percentwidth.equals( "" ) )
            {
                if ( Integer.parseInt( percentwidth ) >= 100 )
                {
                    System.out.println( itemvo.getItemkey() + "打印宽度设置有误." );
                    return -1;
                }
                valuewidth = new Double( Integer.parseInt( percentwidth ) / 100 * graphicwidth ).intValue();
            }
        }
        else
            valuewidth = Integer.parseInt( itemvo.getValuewidthabsolute() );
        itemvo.setValuewidthabsolute( valuewidth + "" );
        return valuewidth;
    }

    public static void print( String args[][] )
    {
        if ( args == null || args.length == 0 )
            return;
        JobAttributes jobattribute = null;

        for ( int i = 0; i < args.length; i++ )
        {
            JFrame f1 = new JFrame( "tet" );
            f1.setSize( 300, 600 );
            String[] data = args[i];
            if ( jobattribute == null )
                jobattribute = new JobAttributes();
            PrintJob pj = f1.getToolkit().getPrintJob( f1, "print1",
                jobattribute, null ); // 建立一个打印任务,运行这行代码时会弹出打印设置窗口
            jobattribute.setDialog( JobAttributes.DialogType.NONE );
            if ( pj != null )
            {
                int linxstart_x = 15;
                int linxstart_y = 50;
                int linxend_x = -1;
                int linxend_y = -1;
                int line_space = 30;
                int v_line_num = data.length - 1;
                int v_line_space = -1;

                Graphics g = pj.getGraphics(); // 关键,获取打印图面,然后在这里面画图,画字!!!!!!!!!!!
                // Graphics g = f1.getGraphics();
                Font oldfont = g.getFont();
                Font newfont = new Font( "Arial", Font.BOLD, 13 );
                g.setFont( newfont );
                double graphicwidth = g.getClipBounds().getWidth();
                double graphicheigh = g.getClipBounds().getHeight();
                g.drawString( "打印测试   (200,30)", 200, 30 );
                g.setFont( oldfont );
                linxend_x = new Double( graphicwidth - linxstart_x ).intValue();
                linxend_y = new Double( graphicheigh - linxstart_y ).intValue();
                int rectWidth = new Double( graphicwidth - 2 * linxstart_x )
                    .intValue();
                int rectHeight = new Double( graphicheigh - 2 * linxstart_y )
                    .intValue();
                v_line_space = rectWidth / data.length;
                g.drawRect( linxstart_x, linxstart_y, rectWidth, rectHeight );
                while ( linxstart_y < linxend_y )
                {
                    g
                        .drawLine( linxstart_x, linxstart_y, linxend_x,
                                   linxstart_y );
                    int v_line_x = linxstart_x;
                    for ( int j = 0; j <= v_line_num; j++ )
                    {
                        g.drawString( data[j], v_line_x + 5, linxstart_y + 10 );
                        g
                            .drawLine(
                                v_line_x + v_line_space,
                                linxstart_y,
                                v_line_x + v_line_space,
                                ( linxstart_y + line_space ) < linxend_y ? ( linxstart_y + line_space )
                                : linxend_y );
                        v_line_x += v_line_space;
                    }
                    // g.drawString("X:"+(linxstart_x+10)+" Y:"+linxstart_y,
                    // linxstart_x+10, linxstart_y);
                    linxstart_y += line_space;
                }
                pj.end();
            }
        }
        System.exit( 0 );
    }

    private void setProperties( Pub_PrintTempletVO vo )
    {
        linxstart_x = Integer.parseInt( vo.getLeftspace() );

        linxstart_y = Integer.parseInt( vo.getTitleheight() );

        linxend_x = Integer.parseInt( vo.getRightspace() );

        linxend_y = Integer.parseInt( vo.getBottomspace() );

        line_space = Integer.parseInt( vo.getLinespace() );
        int title_size = 10;
        if ( vo.getTitlesize() != null )
            title_size = Integer.parseInt( vo.getTitlesize() );
        titleFont = new Font( "宋体", 0, title_size );
        int content_size = 8;
        if ( vo.getContentsize() != null )
            content_size = Integer.parseInt( vo.getContentsize() );
        contentFont = new Font( "宋体", 0, content_size );
    }

    public static ImageIcon getImage(String name)
    {
        URL urlImage = PrintWork.class.getClassLoader().getResource(name);
        if(urlImage == null)
           return null;
        return new ImageIcon(urlImage);
    }

    public static void main( String[] args )
    {
    }
}
