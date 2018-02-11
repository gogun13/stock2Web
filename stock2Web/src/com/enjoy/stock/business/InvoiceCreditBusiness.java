package com.enjoy.stock.business;

import java.util.ArrayList;

import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.bean.UserDetailsBean;
import com.enjoy.stock.bean.InvoiceCreditDetailBean;
import com.enjoy.stock.bean.InvoiceCreditMasterBean;

public interface InvoiceCreditBusiness {
	public void searchByCriteria(PaginatedListBean paginatedList,InvoiceCreditMasterBean invoiceCreditMasterBean) throws Exception;
	public InvoiceCreditMasterBean getInvoiceCreditMaster(String invoiceCode,String tin) throws Exception;
	public ArrayList<InvoiceCreditDetailBean> getInvoiceCreditDetailList(String invoiceCode, String tin) throws Exception;
	public String save(InvoiceCreditMasterBean invoiceCreditMasterBean, ArrayList<InvoiceCreditDetailBean> invoiceCreditDetailList, UserDetailsBean userData) throws Exception;
	public void updateInvoiceCreditMasterStatus(final InvoiceCreditMasterBean vo)throws Exception;
	public void cancel(String invoiceCode, UserDetailsBean userData) throws Exception;
	public void searchByCriteriaForCredit(PaginatedListBean paginatedList,InvoiceCreditMasterBean invoiceCreditMasterBean) throws Exception;
	public ArrayList<InvoiceCreditMasterBean> searchForBillingReport(InvoiceCreditMasterBean invoiceCreditMasterBean) throws Exception;
	public InvoiceCreditMasterBean sumTotalForBillingReport(InvoiceCreditMasterBean invoiceCreditMasterBean) throws Exception;
	public void edit(InvoiceCreditMasterBean invoiceCreditMasterBean, ArrayList<InvoiceCreditDetailBean> invoiceCreditDetailList, UserDetailsBean userData,String invoiceCode) throws Exception;
}
