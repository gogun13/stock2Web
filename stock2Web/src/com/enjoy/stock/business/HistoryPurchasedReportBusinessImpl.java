package com.enjoy.stock.business;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.business.AbstractBusiness;
import com.enjoy.stock.bean.HistoryPurchasedByDealerReportBean;
import com.enjoy.stock.bean.HistoryPurchasedByProductReportBean;
import com.enjoy.stock.services.HistoryPurchasedReportService;

@Service
public class HistoryPurchasedReportBusinessImpl extends AbstractBusiness implements HistoryPurchasedReportBusiness{
	private static final Logger logger = Logger.getLogger(HistoryPurchasedReportBusinessImpl.class);
	
	@Autowired
	HistoryPurchasedReportService historyPurchasedReportService;

	@Override
	public ArrayList<HistoryPurchasedByDealerReportBean> searchByDealer(HistoryPurchasedByDealerReportBean vo) throws Exception {
		return historyPurchasedReportService.searchByDealer(vo);
	}

	@Override
	public ArrayList<HistoryPurchasedByProductReportBean> searchByProduct(HistoryPurchasedByProductReportBean vo) throws Exception {
		return historyPurchasedReportService.searchByProduct(vo);
	}

	

	
}
