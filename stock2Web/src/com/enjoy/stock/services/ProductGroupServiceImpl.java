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
import com.enjoy.stock.bean.ManageProductGroupBean;

@Repository
public class ProductGroupServiceImpl extends AbstractJdbcService implements ProductGroupService{
	
	private static final Logger logger = Logger.getLogger(ProductGroupServiceImpl.class);
	
	@Override
	public ArrayList<ManageProductGroupBean> getProductGroupList(String tin, String productTypeCode) throws Exception{
		logger.info("[getProductGroupList][Begin]");
		
		StringBuilder       				sql         = new StringBuilder();
		ArrayList<ManageProductGroupBean>	resultList 	= new ArrayList<ManageProductGroupBean>();
		
		try{	
			sql.append("select * ");
			sql.append("\n	from productgroup");
			sql.append("\n	where productGroupStatus 	= 'A'");
			sql.append("\n		and tin					= ?");
			sql.append("\n		and productTypeCode		= ?");
			sql.append("\n	order by productGroupCode asc");
			
			logger.info("[getProductGroupList] sql :: " + sql);
			
			resultList = (ArrayList<ManageProductGroupBean>) jdbcTemplate.query(sql.toString(), new Object[]{tin,productTypeCode}, new RowMapper<ManageProductGroupBean>() {
	            @Override
	            public ManageProductGroupBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ManageProductGroupBean item = new ManageProductGroupBean();
	            	
	            	item.setProductTypeCode		(EnjoyUtils.nullToStr(rs.getString("productTypeCode")));
	            	item.setProductGroupCode	(EnjoyUtils.nullToStr(rs.getString("productGroupCode")));
	            	item.setTin					(EnjoyUtils.nullToStr(rs.getString("tin")));
	            	item.setProductGroupCodeDis	(EnjoyUtils.nullToStr(rs.getString("productGroupCodeDis")));
	            	item.setProductGroupName	(EnjoyUtils.nullToStr(rs.getString("productGroupName")));
	            	item.setProductGroupStatus	(EnjoyUtils.nullToStr(rs.getString("productGroupStatus")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			logger.error(e);
			throw e;
		}finally{
			logger.info("[getProductGroupList][End]");
		}
		
		return resultList;
		
	}
	
	@Override
	public void insertProducGroup(ManageProductGroupBean manageProductGroupBean) throws Exception {
		logger.info("[insertProducGroup][Begin]");
        
        StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
        
        try{
        	setColumnStr("productGroupCode", manageProductGroupBean.getProductGroupCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("productTypeCode", manageProductGroupBean.getProductTypeCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("tin", manageProductGroupBean.getTin(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("productGroupCodeDis", manageProductGroupBean.getProductGroupCodeDis(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("productGroupName", manageProductGroupBean.getProductGroupName(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("productGroupStatus", manageProductGroupBean.getProductGroupStatus(), TYPE_STRING, columns, args, paramList, colTypeList);

            sql.append("INSERT INTO  productgroup ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertProducGroup] sql :: " + sql.toString());
            logger.info("[insertProducGroup] paramList :: " + paramList.size());
            logger.info("[insertProducGroup] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertProducGroup] row :: " + row);
            
        }catch(Exception e){
            logger.error("[insertProducGroup] :: ",e);
            throw e;
        }finally{
            logger.info("[insertProductype][End]");
        }
	}
	
	@Override
	public void updateProductgroup( ManageProductGroupBean vo)throws Exception {
		logger.info("[updateProductgroup][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		ArrayList<Object>	params		= new ArrayList<Object>();
		
		try{
			sql.append("update productgroup");
			sql.append("\n	set productGroupName 		= ?");
			sql.append("\n		, productGroupStatus 	= ?");
			sql.append("\n	where productTypeCode 		= ?");
			sql.append("\n		and productGroupCode 	= ?");
			sql.append("\n		and tin 				= ?");
			
			logger.info("[updateProductgroup] productGroupName 		:: " + vo.getProductGroupName());
			logger.info("[updateProductgroup] productGroupStatus 	:: " + vo.getProductGroupStatus());
			logger.info("[updateProductgroup] productTypeCode 		:: " + vo.getProductTypeCode());
			logger.info("[updateProductgroup] productGroupCode 		:: " + vo.getProductGroupCode());
			logger.info("[updateProductgroup] tin 					:: " + vo.getTin());
			
			params.add(vo.getProductGroupName());
			params.add(vo.getProductGroupStatus());
			params.add(vo.getProductTypeCode());
			params.add(vo.getProductGroupCode());
			params.add(vo.getTin());
			
			int row = jdbcTemplate.update(sql.toString(), params.toArray());
			
			logger.info("[updateProductgroup] row :: " + row);
			
		}catch(Exception e){
			logger.error("[updateProductgroup] :: ",e);
			throw e;
		}finally{
			logger.info("[updateProductgroup][End]");
		}
	}
	
	@Override
	public void productGroupSuspended(String tin ,String productTypeCode ,String productGroupCode)throws Exception {
		logger.info("[productGroupSuspended][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		ArrayList<Object>	params		= new ArrayList<Object>();
		
		try{
			sql.append("update productgroup");
			sql.append("\n	set productGroupStatus 	= 'R'");
			sql.append("\n	where productTypeCode 		= ?");
			sql.append("\n		and productGroupCode 	= ?");
			sql.append("\n		and tin 				= ?");
			
			params.add(productTypeCode);
			params.add(productGroupCode);
			params.add(tin);
			
			int row = jdbcTemplate.update(sql.toString(),params.toArray());
			
			logger.info("[productGroupSuspended] row :: " + row);
			
		}catch(Exception e){
			logger.error("[productGroupSuspended] :: ",e);
			throw e;
		}finally{
			logger.info("[productGroupSuspended][End]");
		}
	}
	
	@Override
	public ArrayList<ComboBean> productGroupNameList(String productTypeName, String productGroupName, String tin, boolean flag) throws Exception {
		logger.info("[productGroupNameList][Begin]");
		
		StringBuilder       	sql         = new StringBuilder();
		ArrayList<ComboBean>	resultList 	= new ArrayList<ComboBean>();
		ArrayList<Object>		params		= new ArrayList<Object>();
		
		try{
			
			if(flag==true){
				if(EnjoyUtils.chkNull(productTypeName)){
					sql.append("select a.productGroupCode, a.productGroupName");
					sql.append("\n	from productgroup a");
					sql.append("\n		inner join productype b on b.productTypeCode = a.productTypeCode and b.tin = a.tin");
					sql.append("\n	where a.productGroupStatus 	= 'A'");
					sql.append("\n		and b.productTypeStatus = 'A'");
					sql.append("\n		and b.productTypeName = ?");
					sql.append("\n		and	a.tin	= ?");
					sql.append("\n		and a.productGroupName LIKE CONCAT(?, '%')");
					sql.append("\n	order by productGroupName asc limit 10 ");
					
					params.add(productTypeName);
					params.add(tin);
					params.add(productGroupName);
				}else{
					sql.append("select productGroupCode, productGroupName");
					sql.append("\n	from productgroup");
					sql.append("\n	where productGroupName LIKE CONCAT(?, '%')");
					sql.append("\n		and tin	= ?");
					sql.append("\n		and productGroupStatus 	= 'A'");
					sql.append("\n	order by productGroupName asc limit 10 ");
					
					params.add(productGroupName);
					params.add(tin);
				}
			}else{
				sql.append("select a.productGroupCode, a.productGroupName");
				sql.append("\n	from productgroup a");
				sql.append("\n		inner join productype b on b.productTypeCode = a.productTypeCode and b.tin = a.tin");
				sql.append("\n	where a.productGroupStatus 	= 'A'");
				sql.append("\n		and b.productTypeStatus = 'A'");
				sql.append("\n		and b.productTypeName = ?");
				sql.append("\n		and	a.tin	= ?");
				sql.append("\n		and a.productGroupName LIKE CONCAT(?, '%')");
				sql.append("\n	order by productGroupName asc limit 10 ");
				
				params.add(productTypeName);
				params.add(tin);
				params.add(productGroupName);
			}
			
			logger.info("[productGroupNameList] sql :: " + sql);
			
			resultList = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ComboBean item = new ComboBean();
	            	
	            	item.setCode(EnjoyUtils.nullToStr(rs.getString("productGroupCode")));
	            	item.setDesc(EnjoyUtils.nullToStr(rs.getString("productGroupName")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[productGroupNameList][End]");
		}
		
		return resultList;
	}
	
	@Override
	public String getProductGroupCode(String productTypeCode, String productGroupName, String tin) throws Exception {
		logger.info("[getProductGroupCode][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		String 				result		= "";
		ArrayList<String>	resultList 	= new ArrayList<String>();
		
		try{
			
			sql.append("select productGroupCode");
			sql.append("\n	from productgroup");
			sql.append("\n	where productGroupStatus 	= 'A'");
			sql.append("\n		and productGroupName 	= ?");
			sql.append("\n		and productTypeCode		= ?");
			sql.append("\n		and tin					= ?");
			
			logger.info("[getProductGroupCode] sql 	:: " + sql.toString());
			
			resultList = (ArrayList<String>) jdbcTemplate.query(sql.toString(), new Object[]{productGroupName,productTypeCode,tin}, new RowMapper<String>() {
	            @Override
	            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return EnjoyUtils.nullToStr(rs.getString("productGroupCode"));
	            }
	        });
			
			if(resultList!=null && !resultList.isEmpty()){
				result = resultList.get(0);
			}
			
			logger.info("[getProductGroupCode] result 			:: " + result);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getProductGroupCode][End]");
		}
		
		return result;
	}
	
	@Override
	public int genId(String tin, String productTypeCode) throws Exception {
		logger.info("[genId][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		int 				result		= 1;
		ArrayList<Integer>	resultList 	= new ArrayList<Integer>();
		
		try{
			
			sql.append("select (max(productGroupCode) + 1) newId from productgroup where tin = ? and productTypeCode = ?");
			
			logger.info("[genId] sql 			:: " + sql.toString());
			
			resultList = (ArrayList<Integer>) jdbcTemplate.query(sql.toString(), new Object[]{tin,productTypeCode}, new RowMapper<Integer>() {
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
	
	@Override
	public void cancelProductgroupByProductTypeCode(final String productTypeCode, final String tin)throws Exception {
		logger.info("[cancelProductgroupByProductTypeCode][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update productgroup");
			sql.append("\n	set productGroupStatus 		= 'R'");
			sql.append("\n	where productTypeCode 		= ?");
			sql.append("\n		and tin 				= ?");
			
			int row = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	                int i = 1;
	                
	                ps.setString(i++, productTypeCode);
	                ps.setString(i++, tin);
	            }
	        });
			
			logger.info("[cancelProductgroupByProductTypeCode] row :: " + row);
			
		}catch(Exception e){
			logger.error("[cancelProductgroupByProductTypeCode] :: ",e);
			throw e;
		}finally{
			logger.info("[cancelProductgroupByProductTypeCode][End]");
		}
	}
}
