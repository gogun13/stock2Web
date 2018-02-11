package com.enjoy.stock.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.services.AbstractJdbcService;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.ManageProductTypeBean;

@Repository
public class ProductTypeServiceImpl extends AbstractJdbcService implements ProductTypeService{
	
	private static final Logger logger = Logger.getLogger(ProductTypeServiceImpl.class);
	
	@Override
	public ArrayList<ManageProductTypeBean> getProductTypeList(String tin) throws Exception{
		logger.info("[getProductTypeList][Begin]");
		
		StringBuilder       				sql         = new StringBuilder();
		ArrayList<ManageProductTypeBean>	resultList 	= new ArrayList<ManageProductTypeBean>();
		
		try{	
			sql.append("select * ");
			sql.append("\n	from productype");
			sql.append("\n	where productTypeStatus = 'A'");
			sql.append("\n		and tin				= ?");
			sql.append("\n	order by productTypeCodeDis asc");
			
			logger.info("[getProductTypeList] sql :: " + sql);
			
			resultList = (ArrayList<ManageProductTypeBean>) jdbcTemplate.query(sql.toString(), new Object[]{tin}, new RowMapper<ManageProductTypeBean>() {
	            @Override
	            public ManageProductTypeBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ManageProductTypeBean item = new ManageProductTypeBean();
	            	
	            	item.setProductTypeCode		(EnjoyUtils.nullToStr(rs.getString("productTypeCode")));
	            	item.setTin					(EnjoyUtils.nullToStr(rs.getString("tin")));
	            	item.setProductTypeCodeDis	(EnjoyUtils.nullToStr(rs.getString("productTypeCodeDis")));
	            	item.setProductTypeName		(EnjoyUtils.nullToStr(rs.getString("productTypeName")));
	            	item.setProductTypeStatus	(EnjoyUtils.nullToStr(rs.getString("productTypeStatus")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			logger.error(e);
			throw e;
		}finally{
			logger.info("[getProductTypeList][End]");
		}
		
		return resultList;
		
	}
	
	@Override
	public void insertProductype(ManageProductTypeBean manageProductTypeBean) throws Exception {
		logger.info("[insertProductype][Begin]");
        
        StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
        
        try{
        	setColumnStr("productTypeCode", manageProductTypeBean.getProductTypeCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("tin", manageProductTypeBean.getTin(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("productTypeCodeDis", manageProductTypeBean.getProductTypeCodeDis(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("productTypeName", manageProductTypeBean.getProductTypeName(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("productTypeStatus", manageProductTypeBean.getProductTypeStatus(), TYPE_STRING, columns, args, paramList, colTypeList);

            sql.append("INSERT INTO  productype ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertProductype] sql :: " + sql.toString());
            logger.info("[insertProductype] paramList :: " + paramList.size());
            logger.info("[insertProductype] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertProductype] row :: " + row);
            
        }catch(Exception e){
            logger.error("[insertProductype] :: ",e);
            throw e;
        }finally{
            logger.info("[insertProductype][End]");
        }
	}
	
	@Override
	public void updateProductype(final ManageProductTypeBean vo)throws Exception {
		logger.info("[updateProductype][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update productype");
			sql.append("\n	set productTypeName 		= ?");
			sql.append("\n		, productTypeStatus 	= ?");
			sql.append("\n	where productTypeCode 	= ?");
			sql.append("\n		and tin 			= ?");
			
			int row = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	                int i = 1;
	                
	                ps.setString(i++, vo.getProductTypeName());
	                ps.setString(i++, vo.getProductTypeStatus());
	                ps.setString(i++, vo.getProductTypeCode());
	                ps.setString(i++, vo.getTin());
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
	public void producTypeSuspended(String productTypeCode,String tin)throws Exception {
		logger.info("[producTypeSuspended][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		ArrayList<Object>	params		= new ArrayList<Object>();
		
		try{
			sql.append("update productype");
			sql.append("\n	set productTypeStatus 	= 'R'");
			sql.append("\n	where productTypeCode 	= ?");
			sql.append("\n		and tin 			= ?");
			
			params.add(productTypeCode);
			params.add(tin);
			
			int row = jdbcTemplate.update(sql.toString(),params.toArray());
			
			logger.info("[producTypeSuspended] row :: " + row);
			
		}catch(Exception e){
			logger.error("[producTypeSuspended] :: ",e);
			throw e;
		}finally{
			logger.info("[producTypeSuspended][End]");
		}
	}
	
	@Override
	public ArrayList<ComboBean> productTypeNameList(String productTypeName, String tin) throws Exception {
		logger.info("[productTypeNameList][Begin]");
		
		StringBuilder       	sql         = new StringBuilder();
		ArrayList<ComboBean>	resultList 	= new ArrayList<ComboBean>();
		ArrayList<Object>		params		= new ArrayList<Object>();
		
		try{
			
			sql.append("select productTypeCode, productTypeName");
			sql.append("\n	from productype");
			sql.append("\n	where productTypeName LIKE CONCAT(?, '%')");
			sql.append("\n		and	tin	= ?");
			sql.append("\n		and productTypeStatus = 'A'");
			sql.append("\n	order by productTypeName asc limit 10 ");
			
			params.add(productTypeName);
			params.add(tin);
			
			logger.info("[productTypeNameList] sql :: " + sql);
			
			resultList = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ComboBean item = new ComboBean();
	            	
	            	item.setCode(EnjoyUtils.nullToStr(rs.getString("productTypeCode")));
	            	item.setDesc(EnjoyUtils.nullToStr(rs.getString("productTypeName")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[productTypeNameList][End]");
		}
		
		return resultList;
	}
	
	@Override
	public String getProductTypeCode(String productTypeName, String tin) throws Exception {
		logger.info("[getProductTypeCode][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		String 				result		= "";
		ArrayList<String>	resultList 	= new ArrayList<String>();
		
		try{
			
			sql.append("select productTypeCode");
			sql.append("\n	from productype");
			sql.append("\n	where productTypeStatus = 'A'");
			sql.append("\n		and productTypeName = ?");
			sql.append("\n		and tin				= ?");
			
			logger.info("[getProductTypeCode] sql 	:: " + sql.toString());
			
			resultList = (ArrayList<String>) jdbcTemplate.query(sql.toString(), new Object[]{productTypeName,tin}, new RowMapper<String>() {
	            @Override
	            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return EnjoyUtils.nullToStr(rs.getString("productTypeCode"));
	            }
	        });
			
			if(resultList!=null && !resultList.isEmpty()){
				result = resultList.get(0);
			}
			
			logger.info("[getProductTypeCode] result 			:: " + result);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getProductTypeCode][End]");
		}
		
		return result;
	}
	
	@Override
	public int genId(String tin) throws Exception {
		logger.info("[genId][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		int 				result		= 1;
		ArrayList<Integer>	resultList 	= new ArrayList<Integer>();
		
		try{
			
			sql.append("select (max(productTypeCode) + 1) newId from productype where tin = ?");
			
			logger.info("[genId] sql 			:: " + sql.toString());
			
			resultList = (ArrayList<Integer>) jdbcTemplate.query(sql.toString(), new Object[]{tin}, new RowMapper<Integer>() {
	            @Override
	            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return rs.getInt("newId");
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
}
