package com.enjoy.stock.services;

import java.util.ArrayList;

import com.enjoy.stock.bean.AlertLowProductReportBean;

public interface AlertLowProductReportService {
	public ArrayList<AlertLowProductReportBean> search(AlertLowProductReportBean vo) throws Exception;
	
}
