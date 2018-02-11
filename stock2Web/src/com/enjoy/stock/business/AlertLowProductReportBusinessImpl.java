package com.enjoy.stock.business;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.business.AbstractBusiness;
import com.enjoy.stock.bean.AlertLowProductReportBean;
import com.enjoy.stock.services.AlertLowProductReportService;

@Service
public class AlertLowProductReportBusinessImpl extends AbstractBusiness implements AlertLowProductReportBusiness{
	private static final Logger logger = Logger.getLogger(AlertLowProductReportBusinessImpl.class);
	
	@Autowired
	AlertLowProductReportService alertLowProductReportService;

	@Override
	public ArrayList<AlertLowProductReportBean> search(AlertLowProductReportBean vo) throws Exception {
		return alertLowProductReportService.search(vo);
	}

	
}
