package smartx.publics.cep.bs.persistent;

public class PersistentTask {
	public static final String TYPE_PersistentTask_ADD = "ADD";
	public static final String TYPE_PersistentTask_DEL = "DEL";
	private String type = TYPE_PersistentTask_ADD;
	
	private String eventCode;
	private Long activateTime;
	private String eventType;
	private Object realObject;
	private String providerName;
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEventCode() {
		return eventCode;
	}
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	public Long getActivateTime() {
		return activateTime;
	}
	public void setActivateTime(Long activateTime) {
		this.activateTime = activateTime;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public Object getRealObject() {
		return realObject;
	}
	public void setRealObject(Object realObject) {
		this.realObject = realObject;
	}
	
	
}
