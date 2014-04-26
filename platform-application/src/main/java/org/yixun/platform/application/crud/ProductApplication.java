package org.yixun.platform.application.crud;

import java.util.List;

import javax.jws.WebService;

import org.yixun.platform.application.crud.dto.ProductDTO;

import com.dayatang.querychannel.support.Page;

@WebService()
public interface ProductApplication {
	public ProductDTO saveProduct(ProductDTO productDTO);
	public void updateProduct(ProductDTO productDTO); 
	public void removeProduct(Long id);
	public void removeProducts(Long[] ids);
	public List<ProductDTO> findAllProduct();
	public List<ProductDTO> queryProduct(ProductDTO queryVo,String sortname,String sortorder);
	public Page<ProductDTO> pageQueryProduct(ProductDTO queryVo, int currentPage, int pageSize);
	public ProductDTO getProduct(Long id);
}
