package com.enjoy.stock.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.enjoy.core.services.AbstractJdbcService;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.ComparePriceBean;
import com.enjoy.stock.bean.ComparePriceRemarkBean;

@Repository
public class ComparePriceServiceImpl extends AbstractJdbcService implements ComparePriceService{
	
	private static final Logger logger = Logger.getLogger(ComparePriceServiceImpl.class);
	
	@Override
	public ArrayList<ComparePriceBean> searchByCriteria(String tin, String productCode) throws Exception{
		logger.info("[searchByCriteria][Begin]");
		
		StringBuilder       		sql         = new StringBuilder();
		ArrayList<ComparePriceBean>	resultList 	= new ArrayList<ComparePriceBean>();
		
		try{	
			sql.append("select a.productCode, b.productName, a.seq, a.vendorCode, c.vendorName, c.branchName, a.quantity, a.price, a.discountRate");
			sql.append("\n	from compareprice a");
			sql.append("\n		inner join productmaster b on b.productCode = a.productCode and b.tin = a.tin");
			sql.append("\n		inner join companyvendor c on c.vendorCode 	= a.vendorCode and c.tinCompany	= a.tin");
			sql.append("\n	where a.productCode 	= ?");
			sql.append("\n		and a.tin			= ?");
			sql.append("\n	order by a.seq asc");
			
			logger.info("[searchByCriteria] productCode :: " + productCode);
			logger.info("[searchByCriteria] tin 		:: " + tin);
			logger.info("[searchByCriteria] sql 		:: " + sql);
			
			resultList = (ArrayList<ComparePriceBean>) jdbcTemplate.query(sql.toString(), new Object[]{productCode,tin}, new RowMapper<ComparePriceBean>() {
	            @Override
	            public ComparePriceBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ComparePriceBean item = new ComparePriceBean();
	            	
	            	item.setProductCode	(EnjoyUtils.nullToStr(rs.getString("productCode")));
					item.setProductName	(EnjoyUtils.nullToStr(rs.getString("productName")));
					item.setVendorCode	(EnjoyUtils.nullToStr(rs.getString("vendorCode")));
					item.setVendorName	(EnjoyUtils.nullToStr(rs.getString("vendorName")));
					item.setBranchName	(EnjoyUtils.nullToStr(rs.getString("branchName")));
					item.setSeqDb		(EnjoyUtils.nullToStr(rs.getString("seq")));
					item.setQuantity	(EnjoyUtils.convertNumberToDisplay(rs.getString("quantity")));
					
					item.setPrice		(EnjoyUtils.convertFloatToDisplay(rs.getString("price"), 2));
					item.setDiscountRate(EnjoyUtils.nullToStr(rs.getString("discountRate")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			logger.error(e);
			throw e;
		}finally{
			logger.info("[searchByCriteria][End]");
		}
		
		return resultList;
		
	}
	
	@Override
	public void insertCompareprice(ComparePriceBean vo) throws Exception {
		logger.info("[insertCompareprice][Begin]");
        
        StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
        
        try{
        	setColumnStr("productCode", vo.getProductCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("tin", vo.getTin(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("seq", vo.getSeqDb(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("vendorCode", vo.getVendorCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("quantity", vo.getQuantity(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("price", vo.getPrice(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("discountRate", vo.getDiscountRate(), TYPE_STRING, columns, args, paramList, colTypeList);

            sql.append("INSERT INTO  compareprice ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertCompareprice] sql :: " + sql.toString());
            logger.info("[insertCompareprice] paramList :: " + paramList.size());
            logger.info("[insertCompareprice] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertCompareprice] row :: " + row);
            
        }catch(Exception e){
            logger.error("[insertCompareprice] :: ",e);
            throw e;
        }finally{
            logger.info("[insertCompareprice][End]");
        }
	}
	
	@Override
	public int couVenderInThisProduct(String productCode, String vendorCode, String tin) throws Exception{
		logger.info("[couVenderInThisProduct][Begin]");
		
		StringBuilder       sql     = new StringBuilder();
		int 				result	= 0;
		ArrayList<Object>	params	= new ArrayList<Object>();
		
		try{
			logger.info("[couVenderInThisProduct] productCode 	:: " + productCode);
			logger.info("[couVenderInThisProduct] vendorCode 	:: " + vendorCode);
			logger.info("[couVenderInThisProduct] tin 			:: " + tin);
			
			sql.append("select count(*) cou from compareprice where productCode = ? and vendorCode = ? and tin = ?");
			
			params.add(productCode);
			params.add(vendorCode);
			params.add(tin);
			
			logger.info("[couVenderInThisProduct] sql 			:: " + sql.toString());
			
			result = jdbcTemplate.queryForObject(sql.toString() ,params.toArray(), Integer.class);
			
			logger.info("[couVenderInThisProduct] result 			:: " + result);
			
		}catch(Exception e){
			logger.error("couVenderInThisProduct :: ", e);
			throw e;
		}finally{
			logger.info("[couVenderInThisProduct][End]");
		}
		
		return result;
	}
	
	@Override
	public int genId(String productCode, String tin) throws Exception {
		logger.info("[genId][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		int 				result		= 1;
		ArrayList<Integer>	resultList 	= new ArrayList<Integer>();
		
		try{
			
			sql.append("select (max(seq) + 1) newSeq from compareprice where productCode = ? and tin = ?");
			
			logger.info("[genId] productCode 	:: " + productCode);
			logger.info("[genId] tin 			:: " + tin);
			logger.info("[genId] sql 			:: " + sql.toString());
			
			resultList = (ArrayList<Integer>) jdbcTemplate.query(sql.toString(), new Object[]{productCode,tin}, new RowMapper<Integer>() {
	            @Override
	            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return rs.getInt("newSeq");
	            }
	        });
			
			if(resultList!=null && !resultList.isEmpty()){
				result = resultList.get(0);
			}
			
			logger.info("[genId] result 			:: " + result);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[genId][End]");
		}
		
		return result;
	}
	
	@Override
	public void deleteCompareprice(String productCode, String tin) throws Exception{
		logger.info("[deleteCompareprice][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("delete from compareprice where productCode = ? and tin = ?");
			
			logger.info("[deleteCompareprice] productCode :: " + productCode);
			logger.info("[deleteCompareprice] tin :: " + tin);
			logger.info("[deleteCompareprice] sql :: " + sql.toString());
			
			int row = jdbcTemplate.update(sql.toString(), new Object[]{productCode,tin});
			
			logger.info("[deleteCompareprice] row :: " + row);
		}catch(Exception e){
			logger.error("deleteCompareprice :: ", e);
			throw e;
		}finally{
			logger.info("[deleteCompareprice][End]");
		}
	}
	
	@Override
	public ComparePriceBean getPrice(ComparePriceBean vo) throws Exception {
		logger.info("[getPrice][Begin]");
		
		StringBuilder 					sql 				= new StringBuilder();
		ComparePriceBean 				comparePriceBean 	= null;
		ArrayList<ComparePriceBean> 	result 				= null;
		ArrayList<Object>				params				= new ArrayList<Object>();
		
		try{
		    
		    sql.append("select price, discountRate");
		    sql.append("\n	from compareprice");
		    sql.append("\n	where productCode 	= ?");
		    sql.append("\n		and tin			= ?");
		    sql.append("\n		and vendorCode 	= ?");
		    sql.append("\n		and quantity 	<= ?");
		    sql.append("\n	order by quantity DESC");
		    sql.append("\n	LIMIT 1");
		    
		    params.add(vo.getProductCode());
		    params.add(vo.getTin());
		    params.add(vo.getVendorCode());
		    params.add(vo.getQuantity());
			
		    result = (ArrayList<ComparePriceBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<ComparePriceBean>() {
	            @Override
	            public ComparePriceBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ComparePriceBean item = new ComparePriceBean();
	            	
	            	item.setPrice		(EnjoyUtils.nullToStr(rs.getString("price")));
					item.setDiscountRate(EnjoyUtils.nullToStr(rs.getString("discountRate")));
	            	
	                return item;
	            }
	        });
		    
		    if(result != null && !result.isEmpty()){
		    	comparePriceBean = result.get(0);
		    }
			
			
		}catch(Exception e){
			logger.error("[getPrice] :: ",e);
			throw e;
		}finally{
	        logger.info("[getPrice][End]");
		}
		
		return comparePriceBean;
	}
	
	@Override
	public void insertComparepriceRemark(ComparePriceRemarkBean vo) throws Exception {
		logger.info("[insertComparepriceRemark][Begin]");
        
        StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
        
        try{
        	setColumnStr("productCode", vo.getProductCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("tin", vo.getTin(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("remark", vo.getRemark(), TYPE_STRING, columns, args, paramList, colTypeList);

            sql.append("INSERT INTO  comparepriceremark ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertComparepriceRemark] sql :: " + sql.toString());
            logger.info("[insertComparepriceRemark] paramList :: " + paramList.size());
            logger.info("[insertComparepriceRemark] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertComparepriceRemark] row :: " + row);
            
        }catch(Exception e){
            logger.error("[insertComparepriceRemark] :: ",e);
            throw e;
        }finally{
            logger.info("[insertComparepriceRemark][End]");
        }
	}
	
	@Override
	public void updateComparepriceRemark(final ComparePriceRemarkBean vo)throws Exception {
		logger.info("[updateComparepriceRemark][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update comparepriceremark");
			sql.append("\n	set remark 		= ?");
			sql.append("\n	where productCode 	= ?");
			sql.append("\n		and tin 		= ?");
			
			int row = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	                int i = 1;
	                
	                ps.setString(i++, vo.getRemark());
	                ps.setString(i++, vo.getProductCode());
	                ps.setString(i++, vo.getTin());
	            }
	        });
			
			logger.info("[updateComparepriceRemark] row :: " + row);
			
		}catch(Exception e){
			logger.error("[updateComparepriceRemark] :: ",e);
			throw e;
		}finally{
			logger.info("[updateComparepriceRemark][End]");
		}
	}
	
	@Override
	public int checkForInsertRemark(String productCode, String tin) throws Exception{
		logger.info("[checkForInsertRemark][Begin]");
		
		StringBuilder       sql     = new StringBuilder();
		int 				result	= 0;
		ArrayList<Object>	params	= new ArrayList<Object>();
		
		try{
			logger.info("[checkForInsertRemark] productCode 	:: " + productCode);
			logger.info("[checkForInsertRemark] tin 			:: " + tin);
			
			sql.append("select count(*) cou from comparepriceremark where productCode = ? and tin = ?");
			
			params.add(productCode);
			params.add(tin);
			
			logger.info("[checkForInsertRemark] sql 			:: " + sql.toString());
			
			result = jdbcTemplate.queryForObject(sql.toString() ,params.toArray(), Integer.class);
			
			logger.info("[checkForInsertRemark] result 			:: " + result);
			
		}catch(Exception e){
			logger.error("checkForInsertRemark :: ", e);
			throw e;
		}finally{
			logger.info("[checkForInsertRemark][End]");
		}
		
		return result;
	}
	
	@Override
	public String getRemark(String productCode, String tin) throws Exception {
		logger.info("[getRemark][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		String 				result		= "";
		ArrayList<String>	resultList 	= new ArrayList<String>();
		ArrayList<Object>	params		= new ArrayList<Object>();
		
		try{
			
			sql.append("select remark from comparepriceremark where productCode = ? and tin = ?");
			
			logger.info("[getRemark] sql 			:: " + sql.toString());
			
			params.add(productCode);
			params.add(tin);
			
			resultList = (ArrayList<String>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<String>() {
	            @Override
	            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return EnjoyUtils.nullToStr(rs.getString("remark"));
	            }
	        });
			
			if(resultList!=null && !resultList.isEmpty()){
				result = resultList.get(0);
			}
			
			logger.info("[getRemark] result 			:: " + result);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getRemark][End]");
		}
		
		return result;
	}
	
}
