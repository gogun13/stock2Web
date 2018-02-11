package com.enjoy.stock.services;

import java.util.ArrayList;

import com.enjoy.stock.bean.HistoryPurchasedByDealerReportBean;
import com.enjoy.stock.bean.HistoryPurchasedByProductReportBean;

public interface HistoryPurchasedReportService {
	public ArrayList<HistoryPurchasedByDealerReportBean> searchByDealer(HistoryPurchasedByDealerReportBean vo) throws Exception;
	public ArrayList<HistoryPurchasedByProductReportBean> searchByProduct(HistoryPurchasedByProductReportBean vo) throws Exception;
	
}
