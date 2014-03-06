package org.yixun.platform.core.crud;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.dayatang.domain.AbstractEntity;

@Entity
@Table(name="t_product")
public class Product extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2758703898631335697L;
	private String product_name;
	private Date product_date;

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public Date getProduct_date() {
		return product_date;
	}

	public void setProduct_date(Date product_date) {
		this.product_date = product_date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((product_date == null) ? 0 : product_date.hashCode());
		result = prime * result
				+ ((product_name == null) ? 0 : product_name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (product_date == null) {
			if (other.product_date != null)
				return false;
		} else if (!product_date.equals(other.product_date))
			return false;
		if (product_name == null) {
			if (other.product_name != null)
				return false;
		} else if (!product_name.equals(other.product_name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Product [product_name=" + product_name + ", product_date="
				+ product_date + "]";
	}

}
