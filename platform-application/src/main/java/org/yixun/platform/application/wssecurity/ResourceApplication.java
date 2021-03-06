package org.yixun.platform.application.wssecurity;

import java.util.List;
import java.util.Map;

import org.yixun.platform.application.wssecurity.dto.ResourceDTO;

import com.dayatang.querychannel.support.Page;

public interface ResourceApplication {
	/**
	 * 查询所有资源
	 * @param queryDTO
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	public Page<ResourceDTO> pageQueryResource(ResourceDTO queryDTO,int page,int pagesize) throws Exception;
	/**
	 * 增加资源
	 * @param resourceDTO
	 * @return
	 * @throws Exception
	 */
	public void saveResource(ResourceDTO resourceDTO) throws Exception;
	/**
	 * 获得指定资源详细信息
	 * @param id 资源ID
	 * @return
	 * @throws Exception
	 */
	public ResourceDTO findResourceById(Long id) throws Exception;
	/**
	 * 更新资源信息
	 * @param resourceDTO
	 * @return
	 * @throws Exception
	 */
	public void updateResource(ResourceDTO resourceDTO) throws Exception;
	/**
	 * 删除资源
	 * @param ids 资源ID数组
	 * @return
	 * @throws Exception
	 */
	public void removeResource(Long[] ids) throws Exception;
	/**
	 * 获得角色所拥有的资源
	 * @param roleId 角色ID
	 * @return
	 * @throws Exception
	 */
	public Page<ResourceDTO> pageQueryResourceByRoleId(ResourceDTO queryDTO, Long roleId, int page, int pageSize) throws Exception;
	/**
	 * 没有分配到角色的资源
	 * @param queryDTO
	 * @param roleId 角色ID
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	public Page<ResourceDTO> findResourceNotAssignToRole(ResourceDTO queryDTO,Long roleId,int page,int pagesize) throws Exception;
	/**
	 * 获得资源和角色对照表
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<String>> getAllResourceAndRoles() throws Exception;
}
