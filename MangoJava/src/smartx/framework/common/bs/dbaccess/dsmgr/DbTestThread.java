package smartx.framework.common.bs.dbaccess.dsmgr;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;

public class DbTestThread extends Thread {
	private Logger logger = NovaLogger.getLogger(this.getClass());
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS"); 
	
	private String tbname=null;
	private int timecount=100;
	private String ds="datasource_default";
	private boolean stop=false;
	
	/**
	 * 当前线程的序号
	 * @param tbname
	 * @param idx
	 * @param times
	 */
	public DbTestThread(String tbname, int times, String ds){
		this.tbname=tbname;
		this.timecount=times;
		this.ds=(ds==null||ds.equals(""))?"datasource_default":ds;
	}
	
	public void close(){
		this.stop=true;
	}
	
	public void run(){
		logger.info(tbname+"：开始准备测试数据库信息...");
		if(hasEnv()){
			clearEnv();
		}
		buildEnv();
		initEnv();
		logger.info(tbname+"：准备测试数据库信息完成");
		
			
		logger.info(tbname+"开始测试数据库...");
		int times=0;
		while(times<timecount&&!stop){
			times++;
			logger.info(tbname+"第"+times+"次调用...");
			logger.info(tbname+"调用查询数据库时间：");
			getDBDate(times);
			logger.info(tbname+"模拟业务更新操作：");
			doDBTest(times);
			logger.info(tbname+"第"+times+"次调用结束，暂停1秒。");
			try{sleep(1000);}catch(Exception ex){}
		}		
		logger.info(tbname+"结束测试数据库。");
		
		
		logger.info(tbname+"：开始清理测试数据库信息...");
		clearEnv();
		logger.info(tbname+"：清理测试数据库信息完成");
	}
	
	private boolean hasEnv(){
		CommDMO dmo=new CommDMO();	
		HashVO[] vos=null;
		//必须创建成功，因此使用死循环处理
		while(true){
			try{
				String sql="select * from tab where tname='"+tbname+"'";
				logger.info(tbname+"：判断表是否存在...["+sql+"]");
				vos=dmo.getHashVoArrayByDS(ds, sql);
				logger.info(tbname+"：判断表是否存在。");
				return vos!=null&&vos.length>0;
			}catch(Exception e){
				logger.error(tbname+"发生SQL错误！",e);				
			}finally{
				dmo.releaseContext();
			}
			//如果能执行到这里，一定是执行出错误的。
			try{sleep(300);}catch(Exception e){}
		}
	}
	
	
	private void buildEnv(){
		CommDMO dmo=new CommDMO();	
		//必须创建成功，因此使用死循环处理
		while(true){
			try{
				String sql="create table "+tbname+" (userid varchar2(32) PRIMARY KEY,version number(15))";
				logger.info(tbname+"：创建表...["+sql+"]");
				dmo.executeUpdateByDS(ds, sql);
				logger.info(tbname+"：创建表。");
				
				return;
			}catch(Exception e){
				logger.error(tbname+"创建表发生SQL错误！",e);						
			}finally{
				dmo.releaseContext();
			}
			//如果能执行到这里，一定是执行出错误的。
			try{sleep(300);}catch(Exception e){}
		}
	}
	
	private void initEnv(){
		CommDMO dmo=new CommDMO();	
		//必须创建成功，因此使用死循环处理
		while(true){
			try{
				logger.info(tbname+"：创建模拟记录500条...");
				String[] sqls=new String[500];
				long inow=System.currentTimeMillis();
				for(int i=0;i<500;i++){
					sqls[i]="insert into "+tbname+"(userid,version) values('u"+(inow+i)+"',0)";
				}
				// 批量
				//dmo.executeBatchByDS(ds, sqls);
				// 按语句
				for(int i=0;i<sqls.length;i++){
					dmo.executeUpdateByDS(ds, sqls[i]);
				}
				logger.info(tbname+"：创建模拟记录500条。");
				
				dmo.commit(ds);	
				return;
			}catch(Exception e){
				logger.error(tbname+"创建模拟记录500条发生SQL错误！",e);
				try{dmo.rollback(ds);}catch(Exception ex){}				
			}finally{
				dmo.releaseContext();
			}
			//如果能执行到这里，一定是执行出错误的。
			try{sleep(300);}catch(Exception e){}
		}
	}
	
	
	private void clearEnv(){
		CommDMO dmo=new CommDMO();			
		//必须创建成功，因此使用死循环处理
		while(true){
			try{
				logger.info(tbname+"：删除表...");
				dmo.executeUpdateByDS(ds, "drop table "+tbname+" ");
				logger.info(tbname+"：删除表。");			
				return;
			}catch(Exception e){
				logger.error(tbname+"删除表发生SQL错误！",e);			
			}finally{
				dmo.releaseContext();
			}
			//如果能执行到这里，一定是执行出错误的。
			try{sleep(300);}catch(Exception e){}
		}
	}
	
	private void getDBDate(int times){
		CommDMO dmo=new CommDMO();	
		HashVO[] vos=null;
		//必须创建成功，因此使用死循环处理
		while(true){
			try{
				logger.info(tbname+"_"+times+"开始调用获得数据库日期...");
				//获得新数据
				vos=dmo.getHashVoArrayByDS(ds, "SELECT SYSDATE DBDATE FROM DUAL");
				logger.info(tbname+"_"+times+"当前数据库日期："+sdf.format(vos[0].getDateValue(0)));		
				return;
			}catch(Exception e){
				logger.error(tbname+"_"+times+"发生SQL错误！",e);						
			}finally{
				dmo.releaseContext(ds);
			}
			//如果能执行到这里，一定是执行出错误的。
			try{sleep(300);}catch(Exception e){}
		}
	}
	
	private void doDBTest(int times){
		CommDMO dmo=new CommDMO();	
		HashVO[] vos=null;
		//必须创建成功，因此使用死循环处理
		while(true){
			try{
				logger.info(tbname+"_"+times+"开始调用模拟数据操作...");
				//获得新数据
				String sql="select * from "+tbname+"";
				logger.debug(tbname+"_"+times+"执行sql："+sql);
				vos=dmo.getHashVoArrayByDS(ds, sql);
				logger.info(tbname+"_"+times+"当前库共有"+vos.length+"条记录。");
				for(int i=0;i<vos.length;i++){
					sql="update "+tbname+" set version=version+1 where userid='"+vos[i].getStringValue("userid")+"'";
					logger.debug(tbname+"_"+times+"执行sql："+sql);
					dmo.executeUpdateByDS(ds, sql);				
				}
				dmo.commit(ds);
				logger.info(tbname+"_"+times+"调用模拟数据操作结束。");
				return;
			}catch(Exception e){
				try {
					dmo.rollback(ds);
				} catch (Exception ex) {
					
				}
				logger.error(tbname+"_"+times+"发生SQL错误！",e);						
			}finally{
				dmo.releaseContext(ds);
			}
			//如果能执行到这里，一定是执行出错误的。
			try{sleep(300);}catch(Exception e){}
		}		
	}
	
	
}
