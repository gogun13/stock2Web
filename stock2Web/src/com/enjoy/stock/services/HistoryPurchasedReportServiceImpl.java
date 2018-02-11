package com.enjoy.stock.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.enjoy.core.services.AbstractJdbcService;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.HistoryPurchasedByDealerReportBean;
import com.enjoy.stock.bean.HistoryPurchasedByProductReportBean;

@Repository
public class HistoryPurchasedReportServiceImpl extends AbstractJdbcService implements HistoryPurchasedReportService{
	
	private static final Logger logger = Logger.getLogger(HistoryPurchasedReportServiceImpl.class);

	@Override
	public ArrayList<HistoryPurchasedByDealerReportBean> searchByDealer(HistoryPurchasedByDealerReportBean vo) throws Exception {
		logger.info("[searchByDealer][Begin]");
		
		StringBuilder       							sql         	= new StringBuilder();
		ArrayList<HistoryPurchasedByDealerReportBean>	resultList 		= new ArrayList<HistoryPurchasedByDealerReportBean>();
		ArrayList<Object>								params			= new ArrayList<Object>();
		String 											reciveDateFrom	= null;
		String 											reciveDateTo	= null;
		
		try{
			reciveDateFrom 		= EnjoyUtils.dateThaiToDb(vo.getReciveDateFrom());
			reciveDateTo		= EnjoyUtils.dateThaiToDb(vo.getReciveDateTo());
			
			sql.append("select CONCAT(b.vendorName, '(', b.branchName, ')') as vendorName , a.*");
			sql.append("\n	from reciveordermaster a");
			sql.append("\n		INNER JOIN companyvendor b on a.vendorCode = b.vendorCode and a.tin = b.tinCompany");
			sql.append("\n	where a.tin = ?");
			sql.append("\n		and a.reciveDate >= STR_TO_DATE(?	, '%Y%m%d')");
			sql.append("\n		and a.reciveDate <= STR_TO_DATE(?	, '%Y%m%d')");
			
			params.add(vo.getTin());
			params.add(reciveDateFrom);
			params.add(reciveDateTo);
			
			if(EnjoyUtils.chkNull(vo.getVendorName())){
				sql.append("\n 	and b.vendorName = ?");
				params.add(vo.getVendorName());
			}
			
			if(EnjoyUtils.chkNull(vo.getBranchName())){
				sql.append("\n 	and b.branchName = ?");
				params.add(vo.getBranchName());
			}
			
			sql.append("\n order by a.reciveDate, a.vendorCode");
			
			logger.info("[searchByDealer] sql :: " + sql);
			
			resultList = (ArrayList<HistoryPurchasedByDealerReportBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<HistoryPurchasedByDealerReportBean>() {
	            @Override
	            public HistoryPurchasedByDealerReportBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	HistoryPurchasedByDealerReportBean item = new HistoryPurchasedByDealerReportBean();
	            	
	            	item.setVendorName		(EnjoyUtils.nullToStr(rs.getString("vendorName")));
					item.setReciveNo		(EnjoyUtils.nullToStr(rs.getString("reciveNo")));
					item.setReciveDate		(EnjoyUtils.dateToThaiDisplay(rs.getString("reciveDate")));
					item.setReciveTotal		(EnjoyUtils.convertFloatToDisplay(rs.getString("reciveTotal"), 2));
					item.setReciveDiscount	(EnjoyUtils.convertFloatToDisplay(rs.getString("reciveDiscount"), 2));
	            	
	                return item;
	            }
	        });
			
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[searchByDealer][End]");
		}
		return resultList;
	}
	
	@Override
	public ArrayList<HistoryPurchasedByProductReportBean> searchByProduct(HistoryPurchasedByProductReportBean vo) throws Exception {
		logger.info("[searchByProduct][Begin]");
		
		StringBuilder       							sql         	= new StringBuilder();
		ArrayList<HistoryPurchasedByProductReportBean>	resultList 		= new ArrayList<HistoryPurchasedByProductReportBean>();
		ArrayList<Object>								params			= new ArrayList<Object>();
		String 											reciveDateFrom	= null;
		String 											reciveDateTo	= null;
		
		try{	
			reciveDateFrom 		= EnjoyUtils.dateThaiToDb(vo.getReciveDateFrom());
			reciveDateTo		= EnjoyUtils.dateThaiToDb(vo.getReciveDateTo());
			
			sql.append("select d.productName");
			sql.append("\n		, CONCAT(b.vendorName, '(', b.branchName, ')') as vendorName");
			sql.append("\n		, a.reciveNo");
			sql.append("\n		, a.reciveDate");
			sql.append("\n		, c.costPrice");
			sql.append("\n		, c.discountRate");
			sql.append("\n	from reciveordermaster a");
			sql.append("\n		inner join companyvendor b on b.vendorCode = a.vendorCode and b.tinCompany = a.tin");
			sql.append("\n		inner join reciveordedetail c on c.reciveNo = a.reciveNo and c.tin = a.tin");
			sql.append("\n		inner join productmaster d on d.productCode = c.productCode and d.tin = c.tin");
			sql.append("\n	where a.tin = ?");
			sql.append("\n		and a.reciveDate >= STR_TO_DATE(?	, '%Y%m%d')");
			sql.append("\n		and a.reciveDate <= STR_TO_DATE(?	, '%Y%m%d')");
			
			params.add(vo.getTin());
			params.add(reciveDateFrom);
			params.add(reciveDateTo);
			
			if(EnjoyUtils.chkNull(vo.getProductName())){
				sql.append("\n 	and d.productName = ?");
				params.add(vo.getProductName());
			}
			
			sql.append("\n order by c. productCode, a. vendorCode, a.reciveDate");
			
			logger.info("[searchByProduct] sql :: " + sql);
			
			resultList = (ArrayList<HistoryPurchasedByProductReportBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<HistoryPurchasedByProductReportBean>() {
	            @Override
	            public HistoryPurchasedByProductReportBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	HistoryPurchasedByProductReportBean item = new HistoryPurchasedByProductReportBean();
	            	
	            	item.setProductName		(EnjoyUtils.nullToStr(rs.getString("productName")));
					item.setVendorName		(EnjoyUtils.nullToStr(rs.getString("vendorName")));
					item.setReciveNo		(EnjoyUtils.nullToStr(rs.getString("reciveNo")));
					item.setReciveDate		(EnjoyUtils.dateToThaiDisplay(rs.getString("reciveDate")));
					item.setCostPrice		(EnjoyUtils.convertFloatToDisplay(rs.getString("costPrice"), 2));
					item.setDiscountRate	(EnjoyUtils.convertFloatToDisplay(rs.getString("discountRate"), 2));
	            	
	                return item;
	            }
	        });
			
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[searchByProduct][End]");
		}
		return resultList;
	}
}
