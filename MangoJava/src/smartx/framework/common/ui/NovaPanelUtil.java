package smartx.framework.common.ui;

import javax.swing.JPanel;

/**
 * 面板工具类
 * 
 * @author Administrator
 *
 */
public class NovaPanelUtil {
	
	private NovaPanelUtil(){
		
	}
	
	/**
     * 当前面板为FlowLayout(LEFT)的时候自动根据宽度计算需要的面板高度
     * 动态计算行数和计算面板高度。
     * 计算逻辑：
     * 0、准备参照值：当前行号、当前行高、总行高、可用宽度
     * 1、循环所有控件，临时变量当前行已用宽度
     * 2、  判断已用宽度是否0，
     *       是表示新的一行，行号增加，行高初始化，已用宽度为当前控件宽度
     *       否表示已有行向右边增加控件，
     *         通过已有宽度判断新增的控件是否需要独立
     *           是，把当前行高加入总行高，行增加，新行高等于当前控件高，已用宽度等于当前控件宽度
     *           否，把当前控件宽度加入已用宽度，并把当前控件高度和当前控件高度取大赋给当前行高
     * @param panel  条件所在面板
     * @parem vs      控件序列
     * @param vgap   行的垂直间距 flayout.setVgap(10) 那么设置应该为10
     */ 
    public static int getPanelHeight(int bwidth,JPanel[] ps,int vgap){
    	int row=0;                         //当前行号
    	int rowheight=0;                   //当前行高
    	int bheight=0;                     //总行高    	
    	//int bwidth=pwidth;                 //可用宽度
    	
        for(int i=0,uwidth=0;i<ps.length;i++){//uwidth 上一控件宽度
        	JPanel p=ps[i];
        	int pwidth=(new Double(p.getPreferredSize().getWidth())).intValue();
        	int pheight=(new Double(p.getPreferredSize().getHeight())).intValue();
        	//int pwidth=p.getWidth();
        	//int pheight=p.getHeight();
        	
        	
        	if(uwidth==0){
        		row++;
        		rowheight=pheight;
        		uwidth=pwidth;
        	}else{        		
        		if((uwidth+pwidth)>bwidth){
        			//后一个控件加上本行前面控件大于行宽度，那么需要换行：
        			//  1、把当前行高加到总高度，2）行数加一，3）当前控件的高作为新的行高，当前控件的宽作为新行的已有控件宽度
        			bheight+=rowheight;
        			row++;
            		rowheight=pheight;
            		uwidth=pwidth;
        		}else{  
        			//后一个控件不需要换行，再判断后面控件的高度是不是大于当前控件的高度？是就把当前控件的高度作为当前行的高度，否则维持不变
        		    if(pheight>rowheight) rowheight=pheight;
        		    uwidth+=pwidth;
        		}
        	}        	
        } 
        bheight+=rowheight;
        bheight+=row*vgap;
    	return bheight;
    	
    }
	

}
