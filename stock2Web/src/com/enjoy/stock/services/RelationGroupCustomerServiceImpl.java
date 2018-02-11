package com.enjoy.stock.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.enjoy.core.services.AbstractJdbcService;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.RelationGroupCustomerBean;

@Repository
public class RelationGroupCustomerServiceImpl extends AbstractJdbcService implements RelationGroupCustomerService{
	
	private static final Logger logger = Logger.getLogger(RelationGroupCustomerServiceImpl.class);
	
	@Override
	public ArrayList<RelationGroupCustomerBean> searchByCriteria(String tin) throws Exception{
		logger.info("[searchByCriteria][Begin]");
		
		StringBuilder       					sql         = new StringBuilder();
		ArrayList<RelationGroupCustomerBean>	resultList 	= new ArrayList<RelationGroupCustomerBean>();
		
		try{	
			sql.append("select * ");
			sql.append("\n	from relationgroupcustomer");
			sql.append("\n	where cusGroupStatus 	= 'A'");
			sql.append("\n		and tin				= ?");
			sql.append("\n	order by cusGroupCode asc");
			
			logger.info("[searchByCriteria] sql :: " + sql);
			
			resultList = (ArrayList<RelationGroupCustomerBean>) jdbcTemplate.query(sql.toString(), new Object[]{tin}, new RowMapper<RelationGroupCustomerBean>() {
	            @Override
	            public RelationGroupCustomerBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	RelationGroupCustomerBean item = new RelationGroupCustomerBean();
	            	
	            	item.setCusGroupCode			(EnjoyUtils.nullToStr(rs.getString("cusGroupCode")));
					item.setTin						(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setCusGroupName			(EnjoyUtils.nullToStr(rs.getString("cusGroupName")));
					item.setGroupSalePrice			(EnjoyUtils.nullToStr(rs.getString("groupSalePrice")));
					item.setCusGroupStatus			(EnjoyUtils.nullToStr(rs.getString("cusGroupStatus")));
	            	
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
	public void insertRelationGroupCustomer(RelationGroupCustomerBean relationGroupCustomerBean) throws Exception {
		logger.info("[insertRelationGroupCustomer][Begin]");
        
        StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
        
        try{
        	setColumnStr("cusGroupCode", relationGroupCustomerBean.getCusGroupCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("tin", relationGroupCustomerBean.getTin(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("cusGroupName", relationGroupCustomerBean.getCusGroupName(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("groupSalePrice", relationGroupCustomerBean.getGroupSalePrice(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("cusGroupStatus", relationGroupCustomerBean.getCusGroupStatus(), TYPE_STRING, columns, args, paramList, colTypeList);

            sql.append("INSERT INTO  relationgroupcustomer ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertRelationGroupCustomer] sql :: " + sql.toString());
            logger.info("[insertRelationGroupCustomer] paramList :: " + paramList.size());
            logger.info("[insertRelationGroupCustomer] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertRelationGroupCustomer] row :: " + row);
            
        }catch(Exception e){
            logger.error("[insertRelationGroupCustomer] :: ",e);
            throw e;
        }finally{
            logger.info("[insertRelationGroupCustomer][End]");
        }
	}
	
	@Override
	public int genId(String tin) throws Exception {
		logger.info("[genId][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		int 				result		= 1;
		ArrayList<Integer>	resultList 	= new ArrayList<Integer>();
		
		try{
			
			sql.append("select (max(cusGroupCode) + 1) newId from relationgroupcustomer where tin = ?");
			
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
	
	@Override
	public void updateRelationGroupCustomer(RelationGroupCustomerBean vo) throws Exception {
		logger.info("[updateRelationGroupCustomer][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		ArrayList<Object>	params		= new ArrayList<Object>();
		
		try{
			sql.append("update relationgroupcustomer");
			sql.append("\n	set cusGroupName 		= ?");
			sql.append("\n		, groupSalePrice 	= ?");
			sql.append("\n	where cusGroupCode 	= ?");
			sql.append("\n		and tin 		= ?");
			
			logger.info("[updateProductgroup] cusGroupName 		:: " + vo.getCusGroupName());
			logger.info("[updateProductgroup] groupSalePrice 	:: " + vo.getGroupSalePrice());
			logger.info("[updateProductgroup] cusGroupCode 		:: " + vo.getCusGroupCode());
			logger.info("[updateProductgroup] tin 				:: " + vo.getTin());
			
			params.add(vo.getCusGroupName());
			params.add(vo.getGroupSalePrice());
			params.add(vo.getCusGroupCode());
			params.add(vo.getTin());
			
			int row = jdbcTemplate.update(sql.toString(), params.toArray());
			
			logger.info("[updateRelationGroupCustomer] row :: " + row);
			
		}catch(Exception e){
			logger.error("[updateRelationGroupCustomer] :: ",e);
			throw e;
		}finally{
			logger.info("[updateProductgroup][End]");
		}
	}
	
	@Override
	public void rejectRelationGroupCustomer(String cusGroupCode ,String tin)throws Exception {
		logger.info("[rejectRelationGroupCustomer][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		ArrayList<Object>	params		= new ArrayList<Object>();
		
		try{
			sql.append("update relationgroupcustomer");
			sql.append("\n	set cusGroupStatus 	= 'R'");
			sql.append("\n	where cusGroupCode 	= ?");
			sql.append("\n		and tin 		= ?");
			
			params.add(cusGroupCode);
			params.add(tin);
			
			int row = jdbcTemplate.update(sql.toString(),params.toArray());
			
			logger.info("[rejectRelationGroupCustomer] row :: " + row);
			
		}catch(Exception e){
			logger.error("[rejectRelationGroupCustomer] :: ",e);
			throw e;
		}finally{
			logger.info("[rejectRelationGroupCustomer][End]");
		}
	}
}
