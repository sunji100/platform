package org.yixun.platform.application.security;

import java.util.List;
import java.util.Map;

import org.yixun.platform.application.crud.dto.UserDetails;


public interface AuthDataService {
	public UserDetails loadUserByUseraccount(String useraccount) throws Exception;
	public Map<String, List<String>> getAllResourceAndRoles() throws Exception;
}
