package org.yixun.platform.application.security;

import java.util.List;

import org.yixun.platform.application.security.dto.IdentityDTO;
import org.yixun.platform.application.security.dto.OrgDTO;

public interface OrgApplication {
	public List<OrgDTO> findOrgTree() throws Exception;
	public List<IdentityDTO> findIdentityByOrgId(Long orgId) throws Exception;
	public List<OrgDTO> findOrgAndIdentityTree() throws Exception;
	public void assignRoleToOrg(Long orgId,Long[] roleIds) throws Exception;
	public void removeRoleForOrg(Long orgId,Long[] roleIds) throws Exception; 
	public OrgDTO findOrgById(Long id) throws Exception;
}
