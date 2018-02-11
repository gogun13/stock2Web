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
import com.enjoy.stock.bean.ManageUnitTypeBean;

@Repository
public class UnitTypeServiceImpl extends AbstractJdbcService implements UnitTypeService{
	
	private static final Logger logger = Logger.getLogger(UnitTypeServiceImpl.class);
	
	@Override
	public ArrayList<ManageUnitTypeBean> getUnitTypeList(String tin) throws Exception{
		logger.info("[getUnitTypeList][Begin]");
		
		StringBuilder       			sql         = new StringBuilder();
		ArrayList<ManageUnitTypeBean>	resultList 	= new ArrayList<ManageUnitTypeBean>();
		
		try{	
			sql.append("select * ");
			sql.append("\n	from unittype");
			sql.append("\n	where unitStatus 	= 'A'");
			sql.append("\n		and tin				= ?");
			sql.append("\n	order by unitCode asc");
			
			logger.info("[getUnitTypeList] sql :: " + sql);
			
			resultList = (ArrayList<ManageUnitTypeBean>) jdbcTemplate.query(sql.toString(), new Object[]{tin}, new RowMapper<ManageUnitTypeBean>() {
	            @Override
	            public ManageUnitTypeBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ManageUnitTypeBean item = new ManageUnitTypeBean();
	            	
	            	item.setUnitCode	(EnjoyUtils.nullToStr(rs.getString("unitCode")));
	            	item.setTin			(EnjoyUtils.nullToStr(rs.getString("tin")));
	            	item.setUnitName	(EnjoyUtils.nullToStr(rs.getString("unitName")));
	            	item.setUnitStatus	(EnjoyUtils.nullToStr(rs.getString("unitStatus")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			logger.error(e);
			throw e;
		}finally{
			logger.info("[getUnitTypeList][End]");
		}
		
		return resultList;
		
	}
	
	@Override
	public void insertUnitType(ManageUnitTypeBean manageUnitTypeBean) throws Exception {
		logger.info("[insertUnitType][Begin]");
        
        StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
        
        try{
        	setColumnStr("unitCode", manageUnitTypeBean.getUnitCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("tin", manageUnitTypeBean.getTin(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("unitName", manageUnitTypeBean.getUnitName(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("unitStatus", manageUnitTypeBean.getUnitStatus(), TYPE_STRING, columns, args, paramList, colTypeList);

            sql.append("INSERT INTO  unittype ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertUnitType] sql :: " + sql.toString());
            logger.info("[insertUnitType] paramList :: " + paramList.size());
            logger.info("[insertUnitType] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertUnitType] row :: " + row);
            
        }catch(Exception e){
            logger.error("[insertUnitType] :: ",e);
            throw e;
        }finally{
            logger.info("[insertUnitType][End]");
        }
	}
	
	@Override
	public void updateUnitType(final ManageUnitTypeBean manageUnitTypeBean)throws Exception {
		logger.info("[updateUnitType][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update unittype");
			sql.append("\n	set unitName 		= ?");
			sql.append("\n		, unitStatus 	= ?");
			sql.append("\n	where unitCode 	= ?");
			sql.append("\n		and tin 	= ?");
			
			int row = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	                int i = 1;
	                
	                ps.setString(i++, manageUnitTypeBean.getUnitName());
	                ps.setString(i++, manageUnitTypeBean.getUnitStatus());
	                ps.setString(i++, manageUnitTypeBean.getUnitCode());
	                ps.setString(i++, manageUnitTypeBean.getTin());
	            }
	        });
			
			logger.info("[updateUnitType] row :: " + row);
			
		}catch(Exception e){
			logger.error("[updateUnitType] :: ",e);
			throw e;
		}finally{
			logger.info("[updateUnitType][End]");
		}
	}
	
	@Override
	public void unitTypeSuspended(String unitCode,String tin)throws Exception {
		logger.info("[unitTypeSuspended][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		ArrayList<Object>	params		= new ArrayList<Object>();
		
		try{
			sql.append("update unittype");
			sql.append("\n	set unitStatus 	= 'R'");
			sql.append("\n	where unitCode 	= ?");
			sql.append("\n		and tin 	= ?");
			
			params.add(unitCode);
			params.add(tin);
			
			int row = jdbcTemplate.update(sql.toString(), params.toArray());
			
			logger.info("[unitTypeSuspended] row :: " + row);
			
		}catch(Exception e){
			logger.error("[unitTypeSuspended] :: ",e);
			throw e;
		}finally{
			logger.info("[unitTypeSuspended][End]");
		}
	}
	
	@Override
	public ArrayList<ComboBean> unitNameList(String unitName, String tin) throws Exception {
		logger.info("[unitNameList][Begin]");
		
		StringBuilder       	sql         = new StringBuilder();
		ArrayList<ComboBean>	resultList 	= new ArrayList<ComboBean>();
		ArrayList<Object>		params		= new ArrayList<Object>();
		
		try{
			
			sql.append("select unitCode, unitName");
			sql.append("\n	from unittype");
			sql.append("\n	where unitName LIKE CONCAT(?, '%')");
			sql.append("\n		and	tin	= ?");
			sql.append("\n		and unitStatus  = 'A'");
			sql.append("\n	order by unitName asc limit 10 ");
			
			params.add(unitName);
			params.add(tin);
			
			logger.info("[unitNameList] sql :: " + sql);
			
			resultList = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ComboBean item = new ComboBean();
	            	
	            	item.setCode(EnjoyUtils.nullToStr(rs.getString("unitCode")));
	            	item.setDesc(EnjoyUtils.nullToStr(rs.getString("unitName")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[unitNameList][End]");
		}
		
		return resultList;
	}
	
	@Override
	public String getUnitCode(String unitName, String tin) throws Exception {
		logger.info("[getUnitCode][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		String 				result		= "";
		ArrayList<String>	resultList 	= new ArrayList<String>();
		
		try{
			
			sql.append("select unitCode");
			sql.append("\n	from unittype");
			sql.append("\n	where unitStatus 	= 'A'");
			sql.append("\n		and unitName = ?");
			sql.append("\n		and tin		= ?");
			
			logger.info("[getUnitCode] sql 	:: " + sql.toString());
			
			resultList = (ArrayList<String>) jdbcTemplate.query(sql.toString(), new Object[]{unitName,tin}, new RowMapper<String>() {
	            @Override
	            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return EnjoyUtils.nullToStr(rs.getString("unitCode"));
	            }
	        });
			
			if(resultList!=null && !resultList.isEmpty()){
				result = resultList.get(0);
			}
			
			logger.info("[getUnitCode] result 			:: " + result);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getUnitCode][End]");
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
			
			sql.append("select (max(unitCode) + 1) newId from unittype where tin = ?");
			
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
