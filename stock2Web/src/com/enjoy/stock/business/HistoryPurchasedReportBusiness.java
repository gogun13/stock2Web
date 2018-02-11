package com.enjoy.stock.business;

import java.util.ArrayList;

import com.enjoy.stock.bean.AlertLowProductReportBean;
import com.enjoy.stock.bean.HistoryPurchasedByDealerReportBean;
import com.enjoy.stock.bean.HistoryPurchasedByProductReportBean;

public interface HistoryPurchasedReportBusiness {
	public ArrayList<HistoryPurchasedByDealerReportBean> searchByDealer(HistoryPurchasedByDealerReportBean vo) throws Exception;
	public ArrayList<HistoryPurchasedByProductReportBean> searchByProduct(HistoryPurchasedByProductReportBean vo) throws Exception;
}
