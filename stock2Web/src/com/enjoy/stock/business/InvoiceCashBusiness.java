package com.enjoy.stock.business;

import java.util.ArrayList;

import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.bean.UserDetailsBean;
import com.enjoy.stock.bean.InvoiceCashDetailBean;
import com.enjoy.stock.bean.InvoiceCashMasterBean;

public interface InvoiceCashBusiness {
	public void searchByCriteria(PaginatedListBean paginatedList,InvoiceCashMasterBean 	invoiceCashMasterBean) throws Exception;
	public InvoiceCashMasterBean getInvoiceCashMaster(String invoiceCode,String tin) throws Exception;
	public ArrayList<InvoiceCashDetailBean> getInvoiceCashDetailList(String invoiceCode, String tin) throws Exception;
	public String save(InvoiceCashMasterBean invoiceCashMasterBean, ArrayList<InvoiceCashDetailBean> invoiceCashDetailList, UserDetailsBean userData) throws Exception;
	public void updateInvoiceCashMasterStatus(final InvoiceCashMasterBean vo)throws Exception;
	public void cancel(String invoiceCode, UserDetailsBean userData, String invoiceCredit) throws Exception;
	public void onUpdateCredit(String invoiceCode, UserDetailsBean userData, String invoiceCredit) throws Exception;
	public void edit(InvoiceCashMasterBean invoiceCashMasterBean,ArrayList<InvoiceCashDetailBean> invoiceCashDetailList, UserDetailsBean userData, String invoiceCode) throws Exception;
}
