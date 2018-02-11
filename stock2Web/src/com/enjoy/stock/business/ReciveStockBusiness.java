package com.enjoy.stock.business;

import java.util.ArrayList;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.stock.bean.ReciveOrdeDetailBean;
import com.enjoy.stock.bean.ReciveOrderMasterBean;

public interface ReciveStockBusiness {
	public void searchByCriteria(PaginatedListBean paginatedList,ReciveOrderMasterBean reciveOrderMasterBean,String tin) throws Exception;
	public ReciveOrderMasterBean getReciveOrderMaster(String reciveNo,String tin) throws Exception;
	public ArrayList<ReciveOrdeDetailBean> getReciveOrdeDetailList(String reciveNo, String tin) throws Exception;
	public ArrayList<ComboBean> getRefReciveOrderStatusCombo() throws Exception;
	public String save(ReciveOrderMasterBean reciveOrderMasterBean, ArrayList<ReciveOrdeDetailBean> reciveOrdeDetailList, String pageMode, String currReciveStatus) throws Exception;
}
