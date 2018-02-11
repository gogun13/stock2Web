package com.enjoy.stock.services;

import com.enjoy.stock.bean.ProductquantityBean;

public interface ProductquantityService {
	public void insertProductquantity(ProductquantityBean productquantityBean) throws Exception;
	public void updateProductquantity(final ProductquantityBean productquantityBean)throws Exception;
	public String getProductquantity(String productCode, String tin) throws Exception;
}
