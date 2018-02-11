package com.enjoy.stock.business;

import java.util.ArrayList;

import com.enjoy.stock.bean.StockBalanceReportBean;

public interface StockBalanceReportBusiness {
	public ArrayList<StockBalanceReportBean> search(StockBalanceReportBean vo) throws Exception;
}
