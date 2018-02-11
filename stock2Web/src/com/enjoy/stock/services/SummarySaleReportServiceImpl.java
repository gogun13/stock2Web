package com.enjoy.stock.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.enjoy.core.services.AbstractJdbcService;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.SummarySaleByCustomerReportBean;
import com.enjoy.stock.bean.SummarySaleByDayReportBean;
import com.enjoy.stock.bean.SummarySaleByEmployeeReportBean;
import com.enjoy.stock.bean.SummarySaleByMonthReportBean;
import com.enjoy.stock.bean.SummarySaleByProductReportBean;

@Repository
public class SummarySaleReportServiceImpl extends AbstractJdbcService implements SummarySaleReportService{
	
	private static final Logger logger = Logger.getLogger(SummarySaleReportServiceImpl.class);

	@Override
	public ArrayList<SummarySaleByCustomerReportBean> searchByCustomer(SummarySaleByCustomerReportBean vo) throws Exception {
		logger.info("[searchByCustomer][Begin]");
		
		StringBuilder       						sql         	= new StringBuilder();
		ArrayList<SummarySaleByCustomerReportBean>	resultList 		= new ArrayList<SummarySaleByCustomerReportBean>();
		ArrayList<Object>							params			= new ArrayList<Object>();
		String 										invoiceDateFrom	= null;
		String 										invoiceDateTo	= null;
		
		try{
			invoiceDateFrom 	= EnjoyUtils.dateThaiToDb(vo.getInvoiceDateFrom());
			invoiceDateTo		= EnjoyUtils.dateThaiToDb(vo.getInvoiceDateTo());
			
			sql.append("select d.productName, a.invoiceDate, c.quantity,  c.price, c.discount");
			sql.append("\n	from invoicecashmaster a");
			sql.append("\n		inner join customer b on b.cusCode 	= a.cusCode and b.tin	= a.tin");
			sql.append("\n		inner join invoicecashdetail c on c.invoiceCode 	= a.invoiceCode and c.tin	= a.tin");
			sql.append("\n		inner join productmaster d  on d.productCode 	= c.productCode and d.tin	= c.tin");
			sql.append("\n	where a.cusCode = ?");
			sql.append("\n		and a.tin = ?");
			sql.append("\n		and a.invoiceDate >= STR_TO_DATE(?	, '%Y%m%d')");
			sql.append("\n		and a.invoiceDate <= STR_TO_DATE(?	, '%Y%m%d')");
			
			params.add(vo.getCusCode());
			params.add(vo.getTin());
			params.add(invoiceDateFrom);
			params.add(invoiceDateTo);
			
			if(EnjoyUtils.chkNull(vo.getProductName())){
				sql.append("\n 	and d.productName = ?");
				params.add(vo.getProductName());
			}
			
			sql.append("\n order by a.invoiceDate, a.invoiceCode");
			
			logger.info("[searchByCustomer] sql :: " + sql);
			
			resultList = (ArrayList<SummarySaleByCustomerReportBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<SummarySaleByCustomerReportBean>() {
	            @Override
	            public SummarySaleByCustomerReportBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	SummarySaleByCustomerReportBean item = new SummarySaleByCustomerReportBean();
	            	
	            	item.setProductName	(EnjoyUtils.nullToStr(rs.getString("productName")));
					item.setInvoiceDate	(EnjoyUtils.dateToThaiDisplay(rs.getString("invoiceDate")));
					item.setQuantity	(EnjoyUtils.convertNumberToDisplay(rs.getString("quantity")));
					item.setPrice		(EnjoyUtils.convertFloatToDisplay(rs.getString("price"), 2));
					item.setDiscount	(EnjoyUtils.convertFloatToDisplay(rs.getString("discount"), 2));
	            	
	                return item;
	            }
	        });
			
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[searchByCustomer][End]");
		}
		return resultList;
	}
	
	@Override
	public ArrayList<SummarySaleByDayReportBean> searchByDay(SummarySaleByDayReportBean vo) throws Exception {
		logger.info("[searchByDay][Begin]");
		
		StringBuilder       					sql         	= new StringBuilder();
		ArrayList<SummarySaleByDayReportBean>	resultList 		= new ArrayList<SummarySaleByDayReportBean>();
		ArrayList<Object>						params			= new ArrayList<Object>();
		String 									invoiceDateFrom	= null;
		String 									invoiceDateTo	= null;
		
		try{
			invoiceDateFrom 	= EnjoyUtils.dateThaiToDb(vo.getInvoiceDateFrom());
			invoiceDateTo		= EnjoyUtils.dateThaiToDb(vo.getInvoiceDateTo());
			
			sql.append("select a.invoiceCode");
			sql.append("\n		, CONCAT(b.cusName, ' ', b.cusSurname) as cusName");
			sql.append("\n		, b.branchName");
			sql.append("\n		, a.invoiceDate");
			sql.append("\n		, a.invoiceTotal");
			sql.append("\n		, a.remark");
			sql.append("\n	from invoicecashmaster a");
			sql.append("\n		inner join customer b on b.cusCode 	= a.cusCode and b.tin	= a.tin");
			sql.append("\n	where a.tin = ?");
			sql.append("\n		and a.invoiceDate >= STR_TO_DATE(?	, '%Y%m%d')");
			sql.append("\n		and a.invoiceDate <= STR_TO_DATE(?	, '%Y%m%d')");
			
			params.add(vo.getTin());
			params.add(invoiceDateFrom);
			params.add(invoiceDateTo);
			
			sql.append("\n order by a.invoiceDate, a.invoiceCode");
			
			logger.info("[searchByDay] sql :: " + sql);
			
			resultList = (ArrayList<SummarySaleByDayReportBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<SummarySaleByDayReportBean>() {
	            @Override
	            public SummarySaleByDayReportBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	SummarySaleByDayReportBean 	item = new SummarySaleByDayReportBean();
	            	String						cusName	= EnjoyUtils.nullToStr(rs.getString("cusName"));
	            	
					try {
						if(EnjoyUtils.chkNull(rs.getString("branchName")) && !"-".equals(rs.getString("branchName"))){
							cusName += "(" + EnjoyUtils.nullToStr(rs.getString("branchName")) + ")";
						}
						
						item.setInvoiceCode	(EnjoyUtils.nullToStr(rs.getString("invoiceCode")));
						item.setCusName		(cusName);
						item.setInvoiceDate	(EnjoyUtils.dateToThaiDisplay(rs.getString("invoiceDate")));
						item.setInvoiceTotal(EnjoyUtils.convertFloatToDisplay(rs.getString("invoiceTotal"), 2));
						item.setRemark		(EnjoyUtils.nullToStr(rs.getString("remark")));
					} catch (Exception e) {
						logger.error("",e);
					}
					
	                return item;
	            }
	        });
			
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[searchByDay][End]");
		}
		return resultList;
	}
	
	@Override
	public ArrayList<SummarySaleByEmployeeReportBean> searchByEmployee(SummarySaleByEmployeeReportBean vo) throws Exception {
		logger.info("[searchByEmployee][Begin]");
		
		StringBuilder       						sql         	= new StringBuilder();
		ArrayList<SummarySaleByEmployeeReportBean>	resultList 		= new ArrayList<SummarySaleByEmployeeReportBean>();
		ArrayList<Object>							params			= new ArrayList<Object>();
		String 										invoiceDateFrom	= null;
		String 										invoiceDateTo	= null;
		
		try{
			invoiceDateFrom 	= EnjoyUtils.dateThaiToDb(vo.getInvoiceDateFrom());
			invoiceDateTo		= EnjoyUtils.dateThaiToDb(vo.getInvoiceDateTo());
			
			sql.append("select a.invoiceCode");
			sql.append("\n		, a. invoiceDate");
			sql.append("\n		, CONCAT(b.cusName, ' ', b.cusSurname) as cusName");
			sql.append("\n		, b.branchName");
			sql.append("\n		, a.invoiceTotal");
			sql.append("\n		, a.saleCommission");
			sql.append("\n	from invoicecashmaster a");
			sql.append("\n		left join customer b on a.cusCode = b.cusCode and a.tin = b.tin");
			sql.append("\n		inner join userdetails c on c.userUniqueId = a.saleUniqueId");
			sql.append("\n	where a.tin = ?");
			sql.append("\n		and a.invoiceDate >= STR_TO_DATE(?	, '%Y%m%d')");
			sql.append("\n		and a.invoiceDate <= STR_TO_DATE(?	, '%Y%m%d')");
			
			params.add(vo.getTin());
			params.add(invoiceDateFrom);
			params.add(invoiceDateTo);
			
			if(EnjoyUtils.chkNull(vo.getSaleName())){
				sql.append("\n 	and CONCAT(c.userName, ' ', c.userSurname) = ?");
				params.add(vo.getSaleName());
			}
			
			sql.append("\n order by a.invoiceDate, a.invoiceCode");
			
			logger.info("[searchByEmployee] sql :: " + sql);
			
			resultList = (ArrayList<SummarySaleByEmployeeReportBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<SummarySaleByEmployeeReportBean>() {
	            @Override
	            public SummarySaleByEmployeeReportBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	SummarySaleByEmployeeReportBean item 	= new SummarySaleByEmployeeReportBean();
	            	String							cusName	= EnjoyUtils.nullToStr(rs.getString("cusName"));
	            	
					try {
						if(EnjoyUtils.chkNull(rs.getString("branchName")) && !"-".equals(rs.getString("branchName"))){
							cusName += "(" + EnjoyUtils.nullToStr(rs.getString("branchName")) + ")";
						}
						
						item.setInvoiceCode		(EnjoyUtils.nullToStr(rs.getString("invoiceCode")));
						item.setInvoiceDate		(EnjoyUtils.dateToThaiDisplay(rs.getString("invoiceDate")));
						item.setCusName			(cusName);
						item.setInvoiceTotal	(EnjoyUtils.convertFloatToDisplay(rs.getString("invoiceTotal"), 2));
						item.setSaleCommission	(EnjoyUtils.convertFloatToDisplay(rs.getString("saleCommission"), 2));
					} catch (Exception e) {
						logger.error("",e);
					}
	            	
	                return item;
	            }
	        });
			
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[searchByEmployee][End]");
		}
		return resultList;
	}
	
	@Override
	public ArrayList<SummarySaleByMonthReportBean> searchByMonth(SummarySaleByMonthReportBean vo) throws Exception {
		logger.info("[searchByMonth][Begin]");
		
		StringBuilder       					sql         	= new StringBuilder();
		ArrayList<SummarySaleByMonthReportBean>	resultList 		= new ArrayList<SummarySaleByMonthReportBean>();
		ArrayList<Object>						params			= new ArrayList<Object>();
		String 									invoiceDateFrom	= null;
		String 									invoiceDateTo	= null;
		
		try{
			invoiceDateFrom 	= EnjoyUtils.dateThaiToDb(vo.getInvoiceDateFrom());
			invoiceDateTo		= EnjoyUtils.dateThaiToDb(vo.getInvoiceDateTo());
			
			sql.append("select CONCAT(b.cusName, ' ', b.cusSurname) as cusName");
			sql.append("\n		, b.branchName");
			sql.append("\n		, d.productName");
			sql.append("\n		, sum(c.quantity) as quantity");
			sql.append("\n		, SUM(c.price) - SUM(c.discount) as price");
			sql.append("\n	from invoicecashmaster a");
			sql.append("\n		inner join customer b on b.cusCode = a.cusCode and b.tin = a.tin");
			sql.append("\n		inner join invoicecashdetail c on c.invoiceCode = a.invoiceCode and c.tin = a.tin");
			sql.append("\n		inner join productmaster d on d.productCode = c.productCode and d.tin = c.tin");
			sql.append("\n	where a.tin = ?");
			sql.append("\n		and a.invoiceDate >= STR_TO_DATE(?	, '%Y%m%d')");
			sql.append("\n		and a.invoiceDate <= STR_TO_DATE(?	, '%Y%m%d')");
			sql.append("\n	GROUP BY CONCAT(b.cusName, ' ', b.cusSurname, ' (', b.branchName, ')'),d.productName");
			sql.append("\n	order by a.invoiceDate, a.invoiceCode");
			
			params.add(vo.getTin());
			params.add(invoiceDateFrom);
			params.add(invoiceDateTo);
			
			logger.info("[searchByMonth] sql :: " + sql);
			
			resultList = (ArrayList<SummarySaleByMonthReportBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<SummarySaleByMonthReportBean>() {
	            @Override
	            public SummarySaleByMonthReportBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	SummarySaleByMonthReportBean 	item 	= new SummarySaleByMonthReportBean();
	            	String							cusName	= EnjoyUtils.nullToStr(rs.getString("cusName"));
	            	
					try {
						if(EnjoyUtils.chkNull(rs.getString("branchName")) && !"-".equals(rs.getString("branchName"))){
							cusName += "(" + EnjoyUtils.nullToStr(rs.getString("branchName")) + ")";
						}
						
						item.setCusName		(cusName);
						item.setProductName	(EnjoyUtils.nullToStr(rs.getString("productName")));
						item.setQuantity	(EnjoyUtils.convertNumberToDisplay(rs.getString("quantity")));
						item.setPrice		(EnjoyUtils.convertFloatToDisplay(rs.getString("price"), 2));
					} catch (Exception e) {
						logger.error("",e);
					}
	            	
	                return item;
	            }
	        });
			
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[searchByMonth][End]");
		}
		return resultList;
	}
	
	@Override
	public ArrayList<SummarySaleByProductReportBean> searchByProduct(SummarySaleByProductReportBean vo) throws Exception {
		logger.info("[searchByProduct][Begin]");
		
		StringBuilder       						sql         	= new StringBuilder();
		ArrayList<SummarySaleByProductReportBean>	resultList 		= new ArrayList<SummarySaleByProductReportBean>();
		ArrayList<Object>							params			= new ArrayList<Object>();
		String 										invoiceDateFrom	= null;
		String 										invoiceDateTo	= null;
		
		try{
			invoiceDateFrom 	= EnjoyUtils.dateThaiToDb(vo.getInvoiceDateFrom());
			invoiceDateTo		= EnjoyUtils.dateThaiToDb(vo.getInvoiceDateTo());
			
			sql.append("select a.invoiceDate");
			sql.append("\n		, CONCAT(b.cusName, ' ', b.cusSurname) as cusName");
			sql.append("\n		, b.branchName");
			sql.append("\n		, d.productName");
			sql.append("\n		, c.quantity");
			sql.append("\n		, c.price");
			sql.append("\n	from invoicecashmaster a");
			sql.append("\n		inner join customer b on b.cusCode = a.cusCode and b.tin = a.tin");
			sql.append("\n		inner join invoicecashdetail c on c.invoiceCode = a.invoiceCode and c.tin = a.tin");
			sql.append("\n		inner join productmaster d on d.productCode = c.productCode and d.tin = c.tin");
			sql.append("\n	where a.tin = ?");
			sql.append("\n		and a.invoiceDate >= STR_TO_DATE(?	, '%Y%m%d')");
			sql.append("\n		and a.invoiceDate <= STR_TO_DATE(?	, '%Y%m%d')");
			
			params.add(vo.getTin());
			params.add(invoiceDateFrom);
			params.add(invoiceDateTo);
			
			if(EnjoyUtils.chkNull(vo.getProductName())){
				sql.append("\n 	and d.productName = ?");
				params.add(vo.getProductName());
			}
			
			logger.info("[searchByProduct] sql :: " + sql);
			
			resultList = (ArrayList<SummarySaleByProductReportBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<SummarySaleByProductReportBean>() {
	            @Override
	            public SummarySaleByProductReportBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	SummarySaleByProductReportBean 	item 	= new SummarySaleByProductReportBean();
	            	String							cusName	= EnjoyUtils.nullToStr(rs.getString("cusName"));
	            	
					try {
						if(EnjoyUtils.chkNull(rs.getString("branchName")) && !"-".equals(rs.getString("branchName"))){
							cusName += "(" + EnjoyUtils.nullToStr(rs.getString("branchName")) + ")";
						}
						
						item.setInvoiceDate	(EnjoyUtils.dateToThaiDisplay(rs.getString("invoiceDate")));
						item.setCusName		(cusName);
						item.setProductName	(EnjoyUtils.nullToStr(rs.getString("productName")));
						item.setQuantity	(EnjoyUtils.convertNumberToDisplay(rs.getString("quantity")));
						item.setPrice		(EnjoyUtils.convertFloatToDisplay(rs.getString("price"), 2));
						
					} catch (Exception e) {
						logger.error("",e);
					}
	            	
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
