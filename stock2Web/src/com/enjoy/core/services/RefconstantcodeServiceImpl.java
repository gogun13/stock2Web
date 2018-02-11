package com.enjoy.core.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.enjoy.core.bean.RefconstantcodeBean;
import com.enjoy.core.utils.EnjoyUtils;

@Repository
public class RefconstantcodeServiceImpl extends AbstractJdbcService implements RefconstantcodeService{
	
	private static final Logger logger = Logger.getLogger(RefconstantcodeServiceImpl.class);
	
	@Override
	public ArrayList<RefconstantcodeBean> searchByCriteria(RefconstantcodeBean refconstantcodeBean) throws Exception{
		logger.info("[searchByCriteria][Begin]");
		
		StringBuilder 					sql 				= new StringBuilder();
		ArrayList<RefconstantcodeBean> 	result 				= new ArrayList<RefconstantcodeBean>();
		ArrayList<Object>				params				= new ArrayList<Object>();
		
		try{	
			sql.append("select * from refconstantcode where tin = ?");
			
			params.add(refconstantcodeBean.getTin());
			
			if(!"".equals(refconstantcodeBean.getTypeTB())){
				sql.append(" and typeTB = ?");
				params.add(refconstantcodeBean.getTypeTB());
			}
			
			logger.info("[searchByCriteria] sql :: " + sql.toString());
			
			result = (ArrayList<RefconstantcodeBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<RefconstantcodeBean>() {
	            @Override
	            public RefconstantcodeBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	RefconstantcodeBean item = new RefconstantcodeBean();
	            	
	            	item.setId					(EnjoyUtils.nullToStr(rs.getString("id")));
					item.setTin					(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setCodeDisplay			(EnjoyUtils.nullToStr(rs.getString("codeDisplay")));
					item.setCodeNameTH			(EnjoyUtils.nullToStr(rs.getString("codeNameTH")));
					item.setCodeNameEN			(EnjoyUtils.nullToStr(rs.getString("codeNameEN")));
					item.setFlagYear			(EnjoyUtils.nullToStr(rs.getString("flagYear")));
					
					if("Y".equals(item.getFlagYear())){
						item.setFlagYearBoolean(true);
					}else{
						item.setFlagYearBoolean(false);
					}
					
					item.setFlagEdit			(EnjoyUtils.nullToStr(rs.getString("flagEdit")));
					item.setTypeTB				(EnjoyUtils.nullToStr(rs.getString("typeTB")));
	            	
	                return item;
	            }
	        });
		    
		}catch(Exception e){
			logger.error("searchByCriteria] :: ",e);
			throw e;
		}finally{
			logger.info("[searchByCriteria][End]");
		}
		
		return result;
		
	}
	
	@Override
	public void insertRefconstantcode(RefconstantcodeBean vo) throws Exception{
		logger.info("[insertRefconstantcode][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
		
		try{
			setColumnStr("id", vo.getId(), TYPE_INT, columns, args, paramList, colTypeList);
			setColumnStr("tin", vo.getTin(), TYPE_STRING, columns, args, paramList, colTypeList);
			setColumnStr("codeDisplay", vo.getCodeDisplay(), TYPE_STRING, columns, args, paramList, colTypeList);
			setColumnStr("codeNameTH", vo.getCodeNameTH(), TYPE_STRING, columns, args, paramList, colTypeList);
			setColumnStr("codeNameEN", vo.getCodeNameEN(), TYPE_STRING, columns, args, paramList, colTypeList);
			setColumnStr("flagYear", vo.getFlagYear(), TYPE_STRING, columns, args, paramList, colTypeList);
			setColumnStr("flagEdit", vo.getFlagEdit(), TYPE_STRING, columns, args, paramList, colTypeList);
			setColumnStr("typeTB", vo.getTypeTB(), TYPE_STRING, columns, args, paramList, colTypeList);
			
			sql.append("INSERT INTO  refconstantcode ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertRefconstantcode] sql :: " + sql.toString());
            logger.info("[insertRefconstantcode] paramList :: " + paramList.size());
            logger.info("[insertRefconstantcode] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertRefconstantcode] row :: " + row);
			
		}catch(Exception e){
			logger.error("insertRefconstantcode] :: ",e);
			throw e;
		}finally{
			logger.info("[insertRefconstantcode][End]");
		}
	}
	
	@Override
	public void updateRefconstantcode(final RefconstantcodeBean vo) throws Exception{
		logger.info("[updateRefconstantcode][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update refconstantcode");
			sql.append("\n	set codeDisplay 	= ?");
			sql.append("\n		, codeNameTH	= ?");
			sql.append("\n		, codeNameEN	= ?");
			sql.append("\n		, flagYear		= ?");
			sql.append("\n		, flagEdit		= ?");
			sql.append("\n	 where id 	= ?");
			sql.append("\n		and tin = ?");
			
			int row = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	                int i = 1;
	                
	                ps.setString(i++, 	vo.getCodeDisplay());
	                ps.setString(i++, 	vo.getCodeNameTH());
		   			ps.setString(i++, 	vo.getCodeNameEN());
		   			ps.setString(i++, 	vo.getFlagYear());
		   			ps.setString(i++, 	vo.getFlagEdit());
		   			ps.setInt(i++, 		EnjoyUtils.parseInt(vo.getId()));
		   			ps.setString(i++, 	vo.getTin());
	            }

	        });
			
			logger.info("[updateRefconstantcode] row :: " + row);
			
		}catch(Exception e){
			logger.error("updateRefconstantcode] :: ",e);
			throw e;
		}finally{
			logger.info("[updateRefconstantcode][End]");
		}
	}
	
	@Override
	public void updateCodeDisplay(final RefconstantcodeBean vo) throws Exception{
		logger.info("[updateCodeDisplay][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update refconstantcode");
			sql.append("\n	set codeDisplay 	= ?");
			sql.append("\n		, flagYear		= ?");
			sql.append("\n	 where id 	= ?");
			sql.append("\n		and tin = ?");
			
			
			int row = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	                int i = 1;
	                
	                ps.setString(i++, 	vo.getCodeDisplay());
		   			ps.setString(i++, 	vo.getFlagYear());
		   			ps.setInt(i++, 		EnjoyUtils.parseInt(vo.getId()));
		   			ps.setString(i++, 	vo.getTin());
	            }

	        });
			
			logger.info("[updateCodeDisplay] row :: " + row);
			
		}catch(Exception e){
			logger.error("updateCodeDisplay] :: ",e);
			throw e;
		}finally{
			logger.info("[updateCodeDisplay][End]");
		}
	}
	
	@Override
	public String getCodeDisplay(String id, String tin) throws Exception{
		logger.info("[getCodeDisplay][Begin]");
		
		StringBuilder 	sql 		= new StringBuilder();
		String			result 		= null;
		String			currDate	= "";
		String			year		= "";
		
		try{
			currDate		= EnjoyUtils.currDateThai();
			year			= currDate.substring(2, 4);
			
			sql.append("select case");
			sql.append("\n			WHEN flagYear = 'Y' THEN");
			sql.append("\n				CONCAT(codeDisplay, ?)");
			sql.append("\n			ELSE");
			sql.append("\n				codeDisplay");
			sql.append("\n		  END as codeDisplay");
			sql.append("\n	from refconstantcode");
			sql.append("\n	where id 	= ?");
			sql.append("\n		and tin = ?");
			
			logger.info("[getCodeDisplay] year 	:: " + year);
			logger.info("[getCodeDisplay] id 	:: " + id);
			logger.info("[getCodeDisplay] tin 	:: " + tin);
			logger.info("[getCodeDisplay] sql 	:: " + sql.toString());
			
			result = (String) jdbcTemplate.queryForObject(sql.toString(), new Object[]{year, id, tin}, String.class);
			
			logger.info("[getCodeDisplay] result 			:: " + result);
			
			
			
		}catch(Exception e){
			logger.error("getCodeDisplay] :: ",e);
			throw e;
		}finally{
			logger.info("[getCodeDisplay][End]");
		}
		
		return result;
	}
	
	@Override
	public ArrayList<RefconstantcodeBean> templateDefaultList() throws Exception{
		logger.info("[templateDefaultList][Begin]");
		
		StringBuilder 					sql 		= new StringBuilder();
		ArrayList<RefconstantcodeBean>	result 		= null;
		
		try{	
			sql.append("select * from templateconstantcode");
			
			logger.info("[templateDefaultList] sql 	:: " + sql.toString());
			
			result = (ArrayList<RefconstantcodeBean>) jdbcTemplate.query(sql.toString(), new RowMapper<RefconstantcodeBean>() {
	            @Override
	            public RefconstantcodeBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	RefconstantcodeBean item = new RefconstantcodeBean();
	            	
	            	item.setId					(EnjoyUtils.nullToStr(rs.getString("id")));
					item.setCodeDisplay			(EnjoyUtils.nullToStr(rs.getString("codeDisplay")));
					item.setCodeNameTH			(EnjoyUtils.nullToStr(rs.getString("codeNameTH")));
					item.setCodeNameEN			(EnjoyUtils.nullToStr(rs.getString("codeNameEN")));
					item.setFlagYear			(EnjoyUtils.nullToStr(rs.getString("flagYear")));
					item.setFlagEdit			(EnjoyUtils.nullToStr(rs.getString("flagEdit")));
					item.setTypeTB				(EnjoyUtils.nullToStr(rs.getString("typeTB")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			logger.error("templateDefaultList] :: ",e);
			throw e;
		}finally{
			logger.info("[templateDefaultList][End]");
		}
		
		return result;
		
	}
	
	@Override
	public RefconstantcodeBean getDetail(String id, String tin) throws Exception{
		logger.info("[getDetail][Begin]");
		
		StringBuilder 					sql 		= new StringBuilder();
		ArrayList<RefconstantcodeBean>	resultList 	= null;
		RefconstantcodeBean				result		= null;
		
		try{	
			sql.append("select * from refconstantcode where id = ? and tin = ?");
			
			logger.info("[getDetail] id 	:: " + id);
			logger.info("[getDetail] tin 	:: " + tin);
			logger.info("[getDetail] sql 	:: " + sql.toString());
			
			resultList = (ArrayList<RefconstantcodeBean>) jdbcTemplate.query(sql.toString(), new Object[]{id, tin},new RowMapper<RefconstantcodeBean>() {
	            @Override
	            public RefconstantcodeBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	RefconstantcodeBean item = new RefconstantcodeBean();
	            	
	            	item.setId					(EnjoyUtils.nullToStr(rs.getString("id")));
	            	item.setTin					(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setCodeDisplay			(EnjoyUtils.nullToStr(rs.getString("codeDisplay")));
					item.setCodeNameTH			(EnjoyUtils.nullToStr(rs.getString("codeNameTH")));
					item.setCodeNameEN			(EnjoyUtils.nullToStr(rs.getString("codeNameEN")));
					item.setFlagYear			(EnjoyUtils.nullToStr(rs.getString("flagYear")));
					
					if("Y".equals(item.getFlagYear())){
						item.setFlagYearBoolean(true);
					}else{
						item.setFlagYearBoolean(false);
					}
					
					item.setFlagEdit			(EnjoyUtils.nullToStr(rs.getString("flagEdit")));
					item.setTypeTB				(EnjoyUtils.nullToStr(rs.getString("typeTB")));
	            	
	                return item;
	            }
	        });
			
			if(!resultList.isEmpty() && resultList.size()==1){
				result = resultList.get(0);
			}
			
		}catch(Exception e){
			logger.error("getDetail] :: ",e);
			throw e;
		}finally{
			logger.info("[getDetail][End]");
		}
		
		return result;
		
	}
	
}
