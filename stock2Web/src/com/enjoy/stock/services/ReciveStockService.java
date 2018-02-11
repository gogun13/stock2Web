package com.enjoy.stock.services;

import java.util.ArrayList;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.stock.bean.ReciveOrdeDetailBean;
import com.enjoy.stock.bean.ReciveOrderMasterBean;


public interface ReciveStockService {
	public void searchByCriteria(PaginatedListBean paginatedList,ReciveOrderMasterBean reciveOrderMasterBean,String tin) throws Exception;
	public ReciveOrderMasterBean getReciveOrderMaster(String reciveNo,String tin) throws Exception;
	public void insertReciveordermaster(ReciveOrderMasterBean vo)throws Exception;
	public void updateReciveOrderMaster(final ReciveOrderMasterBean vo)throws Exception;
	public void updateReciveOrderMasterSpecial(final ReciveOrderMasterBean vo)throws Exception;
	public ArrayList<ReciveOrdeDetailBean> getReciveOrdeDetailList(String reciveNo, String tin) throws Exception;
	public void insertReciveOrdeDetail(ReciveOrdeDetailBean vo)throws Exception;
	public void deleteReciveordedetail(String reciveNo, String tin) throws Exception;
	public ArrayList<ComboBean> getRefReciveOrderStatusCombo() throws Exception;
	public String genReciveNo(String codeDisplay,String tin) throws Exception;
}
