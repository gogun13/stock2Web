package com.enjoy.stock.business;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.business.AbstractBusiness;
import com.enjoy.stock.bean.ProductQuanHistoryBean;
import com.enjoy.stock.services.ProductQuanHistoryService;

@Service
public class ProductQuanHistoryBusinessImpl extends AbstractBusiness implements ProductQuanHistoryBusiness{
	private static final Logger logger = Logger.getLogger(ProductQuanHistoryBusinessImpl.class);
	
	@Autowired
	ProductQuanHistoryService productQuanHistoryService;

	@Override
	public ArrayList<ProductQuanHistoryBean> searchByCriteria(ProductQuanHistoryBean criteria) throws Exception {
		return productQuanHistoryService.searchByCriteria(criteria);
	}

	

}
