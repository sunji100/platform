package org.yixun.platform.application.security;

import java.util.List;

import org.yixun.platform.application.security.dto.IdentityDTO;
import org.yixun.platform.application.security.dto.OrgDTO;

public interface OrgApplication {
	/**
	 * 获得组织机构树
	 * @return
	 * @throws Exception
	 */
	public List<OrgDTO> findOrgTree() throws Exception;
	public List<IdentityDTO> findIdentityByOrgId(Long orgId) throws Exception;
	/**
	 * 组织机构及组织中用户树
	 * @return
	 * @throws Exception
	 */
	public List<OrgDTO> findOrgAndIdentityTree() throws Exception;
	/**
	 * 为组织分配角色
	 * @param orgId
	 * @param roleIds
	 * @throws Exception
	 */
	public void assignRoleToOrg(Long orgId,Long[] roleIds) throws Exception;
	/**
	 * 删除为组织分配的角色
	 * @param orgId
	 * @param roleIds
	 * @return
	 * @throws Exception
	 */
	public void removeRoleForOrg(Long orgId,Long[] roleIds) throws Exception; 
	/**
	 * 获得组织信息
	 * @param id 组织ID
	 * @return
	 * @throws Exception
	 */
	public OrgDTO findOrgById(Long id) throws Exception;
	public OrgDTO findOrgByIdentityId(Long userId) throws Exception;
}
