package org.yixun.platform.application.workflow;

import java.util.List;
import java.util.Map;

public interface BpmApplication {
	public List<Map<String, Object>> listProcessDefinitions() throws Exception;
}
