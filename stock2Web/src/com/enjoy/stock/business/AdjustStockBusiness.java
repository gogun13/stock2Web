package com.enjoy.stock.business;

import java.util.ArrayList;

import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.stock.bean.AdjustStockBean;


public interface AdjustStockBusiness {
	public void getAdjustHistoryList(PaginatedListBean paginatedList,AdjustStockBean adjustStockBean) throws Exception;
	public void insertAdjustHistory(AdjustStockBean adjustStockBean) throws Exception;
	public void save(ArrayList<AdjustStockBean> adjustStockList,String tin) throws Exception;
}
