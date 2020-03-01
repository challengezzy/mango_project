package smartx.system.login.ui.deskmodule;

import java.util.HashMap;

import javax.swing.JComponent;


/**
 * 桌面部件通用接口，被桌面管理器使用（DeskTopPanel）
 * 用于规范界面部件接口，主要目的是用来规范控件数据更新。
 * 范例：
 *   1、时钟，可见组件，每隔0.5秒刷新一次，显示当前时间
 *   2、消息板，可见组件，每隔30秒刷新一次，检索后台消息数据，并展现
 *   3、客户在线计时器，不可见组件，每隔0.5秒刷新一次，本地计数，当达到某值时向后台发送在线时长
 * 思路：
 *   1、桌面初始化时，组件注册在桌面管理器（DeskTopPanel），生成组件实例并注册执行逻辑
 *     1.1、启动后延迟某段时间后执行，如启动后4分钟执行，则逻辑为延迟480【4×60×2】
 *     1.2、启动后延迟某段时间后间隔执行，如启动后5分钟开始每隔3分钟执行，则逻辑为延迟600间隔360【5×60×2，3×60×2】
 *   2、桌面管理器有一个500毫秒的时钟计数，即每秒钟计两次数【也就是上面最后都要乘2的原因】，但间隔500毫秒不是绝对的，可能发生漂移。
 *   3、每当桌面计数点到的时候，桌面计数器加一，同时执行一个线程【TimerTask】，该线程遍历注册的实现本接口的组件【并非所有组件都实现本接口】，执行数据更新方法
 * 
 * @author James.W
 *
 */
public interface NovaDeskTopModuleIFC {
	
	/**
	 * 设置参数
	 * 默认有两个参数：uId和_vo，分别对应用户号和DeskTopVO
	 * @param param
	 */
	public void setParams(HashMap param);
	
	/**
	 * 加载数据接口
	 * @throws Exception
	 */
	public void exec() throws Exception;
	
	/**
	 * 获得部件
	 * @return
	 */
	public JComponent getModule()throws Exception;

}
/*******************************************************************************
 * $RCSfile: NovaDeskTopModuleIFC.java,v $ $Revision: 1.1.2.2 $ $Date: 2008/07/11 05:24:38 $
 *
 * $Log: NovaDeskTopModuleIFC.java,v $
 * Revision 1.1.2.2  2008/07/11 05:24:38  wangqi
 * *** empty log message ***
 *
 *
 *
 ******************************************************************************/