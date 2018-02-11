package com.enjoy.stock.business;

import java.util.ArrayList;

import com.enjoy.stock.bean.SummarySaleByCustomerReportBean;
import com.enjoy.stock.bean.SummarySaleByDayReportBean;
import com.enjoy.stock.bean.SummarySaleByEmployeeReportBean;
import com.enjoy.stock.bean.SummarySaleByMonthReportBean;
import com.enjoy.stock.bean.SummarySaleByProductReportBean;

public interface SummarySaleReportBusiness {
	public ArrayList<SummarySaleByCustomerReportBean> searchByCustomer(SummarySaleByCustomerReportBean vo) throws Exception;
	public ArrayList<SummarySaleByDayReportBean> searchByDay(SummarySaleByDayReportBean vo) throws Exception;
	public ArrayList<SummarySaleByEmployeeReportBean> searchByEmployee(SummarySaleByEmployeeReportBean vo) throws Exception;
	public ArrayList<SummarySaleByMonthReportBean> searchByMonth(SummarySaleByMonthReportBean vo) throws Exception;
	public ArrayList<SummarySaleByProductReportBean> searchByProduct(SummarySaleByProductReportBean vo) throws Exception;
}
