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
import com.enjoy.stock.bean.ProductquantityBean;

@Repository
public class ProductquantityServiceImpl extends AbstractJdbcService implements ProductquantityService{
	
	private static final Logger logger = Logger.getLogger(ProductquantityServiceImpl.class);
	
	@Override
	public void insertProductquantity(ProductquantityBean productquantityBean) throws Exception {
		logger.info("[insertProductquantity][Begin]");
        
        StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
        
        try{
        	setColumnStr("productCode", productquantityBean.getProductCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("tin", productquantityBean.getTin(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("quantity", productquantityBean.getQuantity(), TYPE_STRING, columns, args, paramList, colTypeList);

            sql.append("INSERT INTO  productquantity ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertProductquantity] sql :: " + sql.toString());
            logger.info("[insertProductquantity] paramList :: " + paramList.size());
            logger.info("[insertProductquantity] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertProductquantity] row :: " + row);
            
        }catch(Exception e){
            logger.error("[insertProductquantity] :: ",e);
            throw e;
        }finally{
            logger.info("[insertProductquantity][End]");
        }
	}
	
	@Override
	public void updateProductquantity(final ProductquantityBean productquantityBean)throws Exception {
		logger.info("[updateProductquantity][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update productquantity");
			sql.append("\n	set quantity 		= ?");
			sql.append("\n	where productCode 	= ?");
			sql.append("\n		and tin 			= ?");
			
			int row = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	                int i = 1;
	                
	                ps.setString(i++, productquantityBean.getQuantity());
	                ps.setString(i++, productquantityBean.getProductCode());
	                ps.setString(i++, productquantityBean.getTin());
	            }
	        });
			
			logger.info("[updateProductype] row :: " + row);
			
		}catch(Exception e){
			logger.error("[updateProductype] :: ",e);
			throw e;
		}finally{
			logger.info("[updateProductype][End]");
		}
	}
	
	@Override
	public String getProductquantity(String productCode, String tin) throws Exception {
		logger.info("[getProductquantity][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		String 				result		= "";
		ArrayList<String>	resultList 	= new ArrayList<String>();
		
		try{
			
			logger.info("[getProductTypeCode] productCode 	:: " + productCode);
			logger.info("[getProductTypeCode] tin 			:: " + tin);
			
			sql.append("select quantity");
			sql.append("\n	from productquantity");
			sql.append("\n	where productCode = ?");
			sql.append("\n		and tin		= ?");
			
			logger.info("[getProductTypeCode] sql 	:: " + sql.toString());
			
			resultList = (ArrayList<String>) jdbcTemplate.query(sql.toString(), new Object[]{productCode,tin}, new RowMapper<String>() {
	            @Override
	            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return EnjoyUtils.convertNumberToDisplay(rs.getString("quantity"));
	            }
	        });
			
			if(resultList!=null && !resultList.isEmpty()){
				result = resultList.get(0);
			}
			
			logger.info("[getProductquantity] result 			:: " + result);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getProductquantity][End]");
		}
		
		return result;
	}
}
