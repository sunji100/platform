package org.yixun.platform.application.wssecurity.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.application.wssecurity.RoleApplication;
import org.yixun.platform.application.wssecurity.dto.RoleDTO;
import org.yixun.platform.application.wssecurity.util.RoleBeanUtil;
import org.yixun.platform.core.wssecurity.WsRole;

import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

@Named
@Transactional
public class RoleApplicationImpl implements RoleApplication {
	
	private QueryChannelService queryChannelService;
	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public Page<RoleDTO> pageQueryRole(RoleDTO queryDTO, int page, int pagesize) throws Exception {
		StringBuilder jpql = new StringBuilder("select _role from WsRole _role where 1=1");
		List<Object> conditionVals = new ArrayList<Object>();
		
		if(!StringUtils.isBlank(queryDTO.getName())){
			jpql.append(" and _role.name like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getName()));
		}
		
		Page<WsRole> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pagesize);
		
		List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
		RoleDTO roleDTO = null;
		for(WsRole role:pages.getResult()){
			roleDTO = new RoleDTO();
			RoleBeanUtil.domainToDTO(roleDTO, role);
			roleDTOs.add(roleDTO);
		}
		return new Page(pages.getCurrentPageNo(),pages.getTotalCount(),pages.getPageSize(),roleDTOs);
	}

	@Override
	public RoleDTO findRoleById(Long id) throws Exception {
		WsRole role = WsRole.load(WsRole.class, id);
		RoleDTO roleDTO = new RoleDTO();
		RoleBeanUtil.domainToDTO(roleDTO, role);
		return roleDTO;
	}

	@Override
	public void updateRole(RoleDTO roleDTO) throws Exception {
		WsRole role = WsRole.load(WsRole.class, roleDTO.getId());
		RoleBeanUtil.dtoToDomain(role,roleDTO);
		
	}

	@Override
	public void removeRole(Long[] ids) throws Exception {
		for (Long id : ids) {
			WsRole role = WsRole.load(WsRole.class, id);
			role.remove();
		}
		
	}

	@Override
	public RoleDTO saveRole(RoleDTO roleDTO) throws Exception {
		WsRole role = new WsRole();
		RoleBeanUtil.dtoToDomain(role,roleDTO);
		role.setId(null);
		role.save();
		
		roleDTO.setId(role.getId());
		return roleDTO;
	}

}
