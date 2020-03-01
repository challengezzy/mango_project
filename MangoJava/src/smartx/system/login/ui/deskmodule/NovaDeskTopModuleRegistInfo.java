/**********************************************************************
 *$RCSfile: NovaDeskTopModuleRegistInfo.java,v $  $Revision: 1.1.2.1 $  $Date: 2008/10/29 09:31:45 $
 *********************************************************************/ 
package smartx.system.login.ui.deskmodule;
/**
 * <li>Title: ModuleRegistInfo.java</li>
 * <li>Description: 界面组件注册类，记录界面组件的数据响应设置，什么组件从启动开始延迟多久开始每隔多长时间激活一次。</li>
 * <li>Project: NOVA2_MAINBRANCH</li>
 * <li>Copyright: Copyright (c) 2008</li>
 * @Company: GXLU. All Rights Reserved.
 * @author James.W
 * @version 1.0
 */
public class NovaDeskTopModuleRegistInfo{
	public NovaDeskTopModuleRegistInfo(NovaDeskTopModuleIFC module,int delay,int interval){
		this.module=module;
		this.delay=delay;
		this.interval=interval;
	}
	public int getDelay(){
		return this.delay;
	}
	public int getInterval(){
		return this.interval;
	}
	public NovaDeskTopModuleIFC getModule(){
		return this.module;
	}
	
	private NovaDeskTopModuleIFC module=null;
	private int delay=-1;//-1表示立即
	private int interval=-1;//-1表示立即
}

/**********************************************************************
 *$RCSfile: NovaDeskTopModuleRegistInfo.java,v $  $Revision: 1.1.2.1 $  $Date: 2008/10/29 09:31:45 $
 *
 *$Log: NovaDeskTopModuleRegistInfo.java,v $
 *Revision 1.1.2.1  2008/10/29 09:31:45  wangqi
 **** empty log message ***
 *
 *********************************************************************/