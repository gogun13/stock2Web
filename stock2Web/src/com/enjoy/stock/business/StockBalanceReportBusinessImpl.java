package com.enjoy.stock.business;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.business.AbstractBusiness;
import com.enjoy.stock.bean.StockBalanceReportBean;
import com.enjoy.stock.services.StockBalanceReportService;

@Service
public class StockBalanceReportBusinessImpl extends AbstractBusiness implements StockBalanceReportBusiness{
	private static final Logger logger = Logger.getLogger(StockBalanceReportBusinessImpl.class);
	
	@Autowired
	StockBalanceReportService stockBalanceReportService;

	@Override
	public ArrayList<StockBalanceReportBean> search(StockBalanceReportBean vo) throws Exception {
		return stockBalanceReportService.search(vo);
	}

	
}
