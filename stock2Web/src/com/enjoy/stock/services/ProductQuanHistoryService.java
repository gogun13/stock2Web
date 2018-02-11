package com.enjoy.stock.services;

import java.util.ArrayList;

import com.enjoy.stock.bean.ProductQuanHistoryBean;

public interface ProductQuanHistoryService {
	public ArrayList<ProductQuanHistoryBean> searchByCriteria(ProductQuanHistoryBean criteria) throws Exception;
	public void insert(ProductQuanHistoryBean bean, int hisCode) throws Exception;
	public int genId(String tin) throws Exception;
}
