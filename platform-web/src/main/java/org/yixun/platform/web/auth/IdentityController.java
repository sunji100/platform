package org.yixun.platform.web.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yixun.platform.application.security.IdentityApplication;
import org.yixun.platform.application.security.dto.IdentityDTO;
import org.yixun.support.auth.util.SecurityMD5;

import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/identity")
public class IdentityController {
	
	@Inject
	private IdentityApplication identityApplication;
	
	@ResponseBody
	@RequestMapping("/pageQueryIdentity")
	public Map<String, Object> pageQueryIdentity(IdentityDTO identityDTO,@RequestParam("page")int pageNo,@RequestParam("pagesize")int pageSize) throws Exception{
		Page<IdentityDTO> page = identityApplication.pageQueryIdentity(identityDTO, pageNo, pageSize);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", page.getResult());
		result.put("Total", page.getTotalCount());
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/pageQueryIdentityByRoleId")
	public Map<String, Object> pageQueryIdentityByRoleId(IdentityDTO identityDTO,Long roleId,@RequestParam("page")int pageNo,@RequestParam("pagesize")int pageSize) throws Exception{
		Page<IdentityDTO> page = identityApplication.pageQueryIdentityByRoleId(identityDTO, roleId, pageNo, pageSize);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", page.getResult());
		result.put("Total", page.getTotalCount());
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/findNotAssignUserByRole")
	public Map<String, Object> findNotAssignUserByRole(IdentityDTO identityDTO,@RequestParam("roleId")Long roleId,@RequestParam("page")int pageNo,@RequestParam("pagesize")int pageSize) throws Exception{
		Page<IdentityDTO> page = identityApplication.findNotAssignUserByRole(identityDTO,roleId,pageNo,pageSize);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", page.getResult());
		result.put("Total", page.getTotalCount());
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/assignUserToRole")
	public Map<String, Object> assignUserToRole(Long roleId,String identityIds) throws Exception{
		String[] value = identityIds.split(",");
		Long[] identityIdArr = new Long[value.length];
		for(int i=0;i<value.length;i++){
			identityIdArr[i] = Long.parseLong(value[i]);
		}
		identityApplication.assignUserToRole(roleId, identityIdArr);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "sucess");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/removeUserForRole")
	public Map<String, Object> removeUserForRole(Long roleId,String identityIds) throws Exception{
		String[] value = identityIds.split(",");
		Long[] identityIdArr = new Long[value.length];
		for(int i=0;i<value.length;i++){
			identityIdArr[i] = Long.parseLong(value[i]);
		}
		identityApplication.removeUserForRole(roleId, identityIdArr);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "sucess");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/saveIdentity")
	public Map<String, Object> saveIdentity(IdentityDTO identityDTO) throws Exception{
		identityDTO.setUserPassword(SecurityMD5.encode(identityDTO.getUserPassword(), identityDTO.getUserAccount()));
		identityDTO = identityApplication.saveIdentity(identityDTO);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", identityDTO);
		result.put("result", "sucess");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/updateIdentity")
	public Map<String, Object> updateIdentity(IdentityDTO identityDTO) throws Exception{
		identityApplication.updateIdentity(identityDTO);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "sucess");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/findIdentityById/{id}")
	public Map<String, Object> findIdentityById(@PathVariable Long id) throws Exception{
		IdentityDTO identityDTO = identityApplication.findIdentityById(id);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", identityDTO);
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/removeIdentity")
	public Map<String, Object> removeIdentity(String ids) throws Exception{
		
		String[] value = ids.split(",");
		Long[] idArrs = new Long[value.length];
		for(int i=0;i<value.length;i++){
			idArrs[i] = Long.parseLong(value[i]);
		}
		
		identityApplication.removeIdentity(idArrs);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "sucess");
		return result;
	}
	
}
