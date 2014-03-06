package org.yixun.platform.application.security;

import java.util.List;

import org.yixun.platform.application.security.dto.ResourceDTO;

public interface ResourceApplication {
	public List<ResourceDTO> findTopLevelMenuByUser(String userAccount);
	public List<ResourceDTO> findMenuByUser(String userAccount);
}
