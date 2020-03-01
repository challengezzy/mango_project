package smartx.publics.cep.bs.configuration;

import java.util.List;
import java.util.Map;

import com.espertech.esper.client.Configuration;

/**
 * 引擎配置生成工厂
 * CEP服务通过此工厂初始化引擎
 * @author teddyxu
 *
 */
public interface CEPEngineConfigurationFactory {
	
	/**
	 * 获取需要初始化的容器名
	 * @return
	 */
	public List<String> getProviderNamesNeedToInit();
	/**
	 * 获取容器的通用配置，返回null则使用默认配置
	 * @return
	 */
	public Configuration getDefaultConfiguration();
	
	/**
	 * 获取指定容器的配置，如果返回null，则使用通用配置
	 * @param providerName
	 * @return
	 */
	public Configuration getConfigurationByProviderName(String providerName);
}
