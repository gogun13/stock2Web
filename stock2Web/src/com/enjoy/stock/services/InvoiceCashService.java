package com.enjoy.stock.services;

import java.util.ArrayList;

import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.stock.bean.InvoiceCashDetailBean;
import com.enjoy.stock.bean.InvoiceCashMasterBean;


public interface InvoiceCashService {
	public void searchByCriteria(PaginatedListBean paginatedList,InvoiceCashMasterBean 	invoiceCashMasterBean) throws Exception;
	public InvoiceCashMasterBean getInvoiceCashMaster(String invoiceCode,String tin) throws Exception;
	public void insertInvoiceCashMaster(InvoiceCashMasterBean vo)throws Exception;
	public void updateInvoiceCashMasterStatus(final InvoiceCashMasterBean vo)throws Exception;
	public ArrayList<InvoiceCashDetailBean> getInvoiceCashDetailList(String invoiceCode, String tin) throws Exception;
	public void insertInvoiceCashDetail(InvoiceCashDetailBean vo)throws Exception;
	public void deleteInvoiceCashDetail(String invoiceCode, String tin) throws Exception;
	public String genId(String codeDisplay,String tin) throws Exception;
	public void cancelInvoiceCashMasterByInvoiceCredit(final String invoiceCode,final String tin)throws Exception;
	public void updateInvoiceCashMaster(final InvoiceCashMasterBean vo)throws Exception;
}
