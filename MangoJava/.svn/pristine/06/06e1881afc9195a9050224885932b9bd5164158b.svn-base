package smartx.bam.bs.task;

import java.util.List;
import java.util.Map;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.cep.vo.CEPConst;
import smartx.publics.form.bs.service.FormInterceptor;
import smartx.publics.form.bs.service.SmartXFormService;

public class TaskDelBfInterceptor implements FormInterceptor {

	@Override
	public void doSomething(Pub_Templet_1VO templetVO, Map<String, Object> dataValue) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSomething(Pub_Templet_1VO templetVO, List<Map<String, Object>> dataValueList) throws Exception {
		CommDMO dmo = new CommDMO();
		//任务删除前，先删除任务观察者
		for (Map<String, Object> dataValue : dataValueList) {
			String flag = (String) dataValue.get(SmartXFormService.KEYNAME_MODIFYFLAG);
			String id = (String)dataValue.get("ID");
			if ("delete".equalsIgnoreCase(flag.trim())){
				String sql = "delete bam_taskwatcher w where w.taskid = ?";
				dmo.executeUpdateByDS(null, sql, id);
			}
		}
	}

	@Override
	public void doSomething(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSomething(List<Map<String, Object>> dataValueList) throws Exception {
		// TODO Auto-generated method stub

	}

}
