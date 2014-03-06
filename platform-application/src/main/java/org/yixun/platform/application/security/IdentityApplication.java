package org.yixun.platform.application.security;

import java.util.List;

import org.yixun.platform.application.crud.dto.ProductDTO;
import org.yixun.platform.application.security.dto.IdentityDTO;

import com.dayatang.querychannel.support.Page;

public interface IdentityApplication {
	public IdentityDTO getIdentity(String userAccount);
	public List<IdentityDTO> findAllIdentity();
	public Page<IdentityDTO> findNotAssignUserByRole(IdentityDTO identityDTO,Long roleId,int page,int pageSize) throws Exception; 
	public Page<IdentityDTO> pageQueryIdentity(IdentityDTO identityDTO,int page,int pageSize);
	public Page<IdentityDTO> pageQueryIdentityByRoleId(IdentityDTO identityDTO,Long roleId,int page,int pageSize) throws Exception;
	public IdentityDTO saveIdentity(IdentityDTO identityDTO) throws Exception;
	public void updateIdentity(IdentityDTO identityDTO);
	public IdentityDTO findIdentityById(Long id);
	public void removeIdentity(Long[] ids);
	public void assignUserToRole(Long roleId,Long[] identityIds) throws Exception;
	public void removeUserForRole(Long roleId,Long[] identityIds) throws Exception;
}
