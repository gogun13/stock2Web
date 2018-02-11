package com.enjoy.stock.services;

import java.util.ArrayList;

import com.enjoy.stock.bean.StockBalanceReportBean;

public interface StockBalanceReportService {
	public ArrayList<StockBalanceReportBean> search(StockBalanceReportBean vo) throws Exception;
	
}
