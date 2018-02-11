package com.enjoy.stock.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.enjoy.core.services.AbstractJdbcService;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.AlertLowProductReportBean;

@Repository
public class AlertLowProductReportServiceImpl extends AbstractJdbcService implements AlertLowProductReportService{
	
	private static final Logger logger = Logger.getLogger(AlertLowProductReportServiceImpl.class);

	@Override
	public ArrayList<AlertLowProductReportBean> search(AlertLowProductReportBean vo) throws Exception {
		logger.info("[search][Begin]");
		
		StringBuilder       					sql         = new StringBuilder();
		ArrayList<AlertLowProductReportBean>	resultList 	= new ArrayList<AlertLowProductReportBean>();
		ArrayList<Object>						params		= new ArrayList<Object>();
		
		try{	
			
			sql.append("select t3. productTypeName, t4.productGroupName, t1.productname, t1.minQuan, t2.quantity");
			sql.append("\n	from productmaster t1");
			sql.append("\n		INNER JOIN productquantity t2 on t1.productCode = t2.productCode and t1.tin = t2.tin");
			sql.append("\n		INNER JOIN productype t3 ON t1.productType = t3.productTypeCode and t1.tin = t3.tin");
			sql.append("\n		INNER JOIN productgroup t4 on t1.productType = t4.productTypeCode and t1.productGroup = t4.productGroupCode and t1.tin = t4.tin");
			sql.append("\n	where t1.tin = ?");
			sql.append("\n		and t1.minQuan >= t2.quantity");
			sql.append("\n		and t3.productTypeStatus = 'A'");
			sql.append("\n		AND productGroupStatus = 'A'");
			
			params.add(vo.getTin());
			
			if(EnjoyUtils.chkNull(vo.getProductTypeName())){
				sql.append("\n 	and t3.productTypeName = ?");
				params.add(vo.getProductTypeName());
			}
			
			if(EnjoyUtils.chkNull(vo.getProductGroupName())){
				sql.append("\n 	and t4.productGroupName = ?");
				params.add(vo.getProductGroupName());
			}
			
			sql.append("\n order by t1.productType, t1.productGroup, t1.productCode");
			
			logger.info("[search] sql :: " + sql);
			
			resultList = (ArrayList<AlertLowProductReportBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<AlertLowProductReportBean>() {
	            @Override
	            public AlertLowProductReportBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	AlertLowProductReportBean item = new AlertLowProductReportBean();
	            	
	            	item.setProductTypeName	(EnjoyUtils.nullToStr(rs.getString("productTypeName")));
					item.setProductGroupName(EnjoyUtils.nullToStr(rs.getString("productGroupName")));
					item.setProductName		(EnjoyUtils.nullToStr(rs.getString("productName")));
					item.setMinQuan			(EnjoyUtils.convertNumberToDisplay(rs.getString("minQuan")));
					item.setQuantity		(EnjoyUtils.convertNumberToDisplay(rs.getString("quantity")));
	            	
	                return item;
	            }
	        });
			
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[search][End]");
		}
		return resultList;
	}
}
