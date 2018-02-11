package com.enjoy.stock.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.enjoy.core.services.AbstractJdbcService;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.StockBalanceReportBean;

@Repository
public class StockBalanceReportServiceImpl extends AbstractJdbcService implements StockBalanceReportService{
	
	private static final Logger logger = Logger.getLogger(StockBalanceReportServiceImpl.class);

	@Override
	public ArrayList<StockBalanceReportBean> search(StockBalanceReportBean vo) throws Exception {
		logger.info("[search][Begin]");
		
		StringBuilder       				sql         = new StringBuilder();
		ArrayList<StockBalanceReportBean>	resultList 	= new ArrayList<StockBalanceReportBean>();
		ArrayList<Object>					params		= new ArrayList<Object>();
		
		try{	
			
			sql.append("select c.productTypeName, d.productGroupName, a.productName, b.quantity");
			sql.append("\n	from productmaster a");
			sql.append("\n		inner join productquantity b on b.productCode = a.productCode and b.tin = a.tin");
			sql.append("\n		inner join productype c on c.productTypeCode = a.productType and c.tin = a.tin");
			sql.append("\n		inner join productgroup d on d.productTypeCode = a.productType and d.productGroupCode = a.productGroup and d.tin and a.tin");
			sql.append("\n	where a.tin = ?");
			
			params.add(vo.getTin());
			
			if(EnjoyUtils.chkNull(vo.getProductTypeName())){
				sql.append("\n 	and c.productTypeName = ?");
				params.add(vo.getProductTypeName());
			}
			
			if(EnjoyUtils.chkNull(vo.getProductGroupName())){
				sql.append("\n 	and d.productGroupName = ?");
				params.add(vo.getProductGroupName());
			}
			
			sql.append("\n order by a.productType, a.productGroup, a.productCode");
			
			logger.info("[search] sql :: " + sql);
			
			resultList = (ArrayList<StockBalanceReportBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<StockBalanceReportBean>() {
	            @Override
	            public StockBalanceReportBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	StockBalanceReportBean item = new StockBalanceReportBean();
	            	
	            	item.setProductTypeName	(EnjoyUtils.nullToStr(rs.getString("productTypeName")));
					item.setProductGroupName(EnjoyUtils.nullToStr(rs.getString("productGroupName")));
					item.setProductName		(EnjoyUtils.nullToStr(rs.getString("productName")));
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
