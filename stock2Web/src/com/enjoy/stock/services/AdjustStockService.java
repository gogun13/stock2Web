package com.enjoy.stock.services;

import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.stock.bean.AdjustStockBean;

public interface AdjustStockService {
	public void getAdjustHistoryList(PaginatedListBean paginatedList,AdjustStockBean adjustStockBean) throws Exception;
	public void insertAdjustHistory(AdjustStockBean adjustStockBean) throws Exception;
}
