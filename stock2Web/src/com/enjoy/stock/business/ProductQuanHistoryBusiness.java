package com.enjoy.stock.business;

import java.util.ArrayList;

import com.enjoy.stock.bean.ProductQuanHistoryBean;


public interface ProductQuanHistoryBusiness {
	public ArrayList<ProductQuanHistoryBean> searchByCriteria(ProductQuanHistoryBean criteria) throws Exception;
}
