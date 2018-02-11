package com.enjoy.stock.services;

import java.util.ArrayList;

import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.stock.bean.InvoiceCreditDetailBean;
import com.enjoy.stock.bean.InvoiceCreditMasterBean;


public interface InvoiceCreditService {
	public void searchByCriteria(PaginatedListBean paginatedList,InvoiceCreditMasterBean invoiceCreditMasterBean) throws Exception;
	public InvoiceCreditMasterBean getInvoiceCreditMaster(String invoiceCode,String tin) throws Exception;
	public void insertInvoiceCreditMaster(InvoiceCreditMasterBean vo)throws Exception;
	public void updateInvoiceCreditMaster(final InvoiceCreditMasterBean vo)throws Exception;
	public void updateInvoiceCreditMasterStatus(final InvoiceCreditMasterBean vo)throws Exception;
	public ArrayList<InvoiceCreditDetailBean> getInvoiceCreditDetailList(String invoiceCode, String tin) throws Exception;
	public void insertInvoiceCreditDetail(InvoiceCreditDetailBean vo)throws Exception;
	public void deleteInvoiceCreditDetail(String invoiceCode, String tin) throws Exception;
	public String genId(String codeDisplay,String tin) throws Exception;
	public void searchByCriteriaForCredit(PaginatedListBean paginatedList,InvoiceCreditMasterBean invoiceCreditMasterBean) throws Exception;
	public ArrayList<InvoiceCreditMasterBean> searchForBillingReport(InvoiceCreditMasterBean invoiceCreditMasterBean) throws Exception;
	public InvoiceCreditMasterBean sumTotalForBillingReport(InvoiceCreditMasterBean invoiceCreditMasterBean) throws Exception;
}
