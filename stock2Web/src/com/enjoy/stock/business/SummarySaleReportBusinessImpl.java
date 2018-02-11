package com.enjoy.stock.business;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.business.AbstractBusiness;
import com.enjoy.stock.bean.SummarySaleByCustomerReportBean;
import com.enjoy.stock.bean.SummarySaleByDayReportBean;
import com.enjoy.stock.bean.SummarySaleByEmployeeReportBean;
import com.enjoy.stock.bean.SummarySaleByMonthReportBean;
import com.enjoy.stock.bean.SummarySaleByProductReportBean;
import com.enjoy.stock.services.SummarySaleReportService;

@Service
public class SummarySaleReportBusinessImpl extends AbstractBusiness implements SummarySaleReportBusiness{
	private static final Logger logger = Logger.getLogger(SummarySaleReportBusinessImpl.class);
	
	@Autowired
	SummarySaleReportService summarySaleReportService;

	@Override
	public ArrayList<SummarySaleByCustomerReportBean> searchByCustomer(SummarySaleByCustomerReportBean vo) throws Exception {
		return summarySaleReportService.searchByCustomer(vo);
	}

	@Override
	public ArrayList<SummarySaleByDayReportBean> searchByDay(SummarySaleByDayReportBean vo) throws Exception {
		return summarySaleReportService.searchByDay(vo);
	}

	@Override
	public ArrayList<SummarySaleByEmployeeReportBean> searchByEmployee(SummarySaleByEmployeeReportBean vo) throws Exception {
		return summarySaleReportService.searchByEmployee(vo);
	}

	@Override
	public ArrayList<SummarySaleByMonthReportBean> searchByMonth(SummarySaleByMonthReportBean vo) throws Exception {
		return summarySaleReportService.searchByMonth(vo);
	}

	@Override
	public ArrayList<SummarySaleByProductReportBean> searchByProduct(SummarySaleByProductReportBean vo) throws Exception {
		return summarySaleReportService.searchByProduct(vo);
	}

	
}
