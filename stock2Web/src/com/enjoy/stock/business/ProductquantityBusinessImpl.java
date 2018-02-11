package com.enjoy.stock.business;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.business.AbstractBusiness;
import com.enjoy.stock.services.ProductquantityService;

@Service
public class ProductquantityBusinessImpl extends AbstractBusiness implements ProductquantityBusiness{
	private static final Logger logger = Logger.getLogger(ProductquantityBusinessImpl.class);
	
	@Autowired
	ProductquantityService productquantityService;

	@Override
	public String getProductquantity(String productCode, String tin)throws Exception {
		return productquantityService.getProductquantity(productCode, tin);
	}

	

}
