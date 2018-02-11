package com.enjoy.stock.business;

import java.util.ArrayList;

import com.enjoy.stock.bean.AlertLowProductReportBean;

public interface AlertLowProductReportBusiness {
	public ArrayList<AlertLowProductReportBean> search(AlertLowProductReportBean vo) throws Exception;
}
