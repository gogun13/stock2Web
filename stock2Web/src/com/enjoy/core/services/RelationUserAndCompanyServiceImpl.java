package com.enjoy.core.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.RelationUserAndCompanyBean;
import com.enjoy.core.utils.EnjoyUtils;

@Repository
public class RelationUserAndCompanyServiceImpl extends AbstractJdbcService implements RelationUserAndCompanyService{
	
	private static final Logger logger = Logger.getLogger(RelationUserAndCompanyServiceImpl.class);
	
	@Override
	public ArrayList<RelationUserAndCompanyBean> searchByCriteria(RelationUserAndCompanyBean vo) throws Exception{
		logger.info("[searchByCriteria][Begin]");
		
		StringBuilder 							sql 				= new StringBuilder();
		ArrayList<RelationUserAndCompanyBean> 	resultList 			= new ArrayList<RelationUserAndCompanyBean>();
		ArrayList<Object>						params				= new ArrayList<Object>();
		int										seq					= 0;
		
		try{	
			sql.append("select a.*, b.userEmail, CONCAT(b.userName, ' ', b.userSurname) userFullName, b.userStatus, c.userStatusName");
			sql.append("\n	from relationuserncompany a");
			sql.append("\n		inner join userdetails b on b.userUniqueId = a.userUniqueId");
			sql.append("\n		inner join refuserstatus c on c.userStatusCode	= b.userStatus");
			sql.append("\n	where a.tin = ?");
			
			//Criteria
			params.add(vo.getTin());
			
//			if(!vo.getUserFullName().equals("")){
//				sql.append(" and CONCAT(b.userName, ' ', b.userSurname) LIKE CONCAT(?, '%')");
//				params.add(vo.getUserFullName());
//			}
			
			logger.info("[searchByCriteria] sql :: " + sql.toString());
			
			resultList = (ArrayList<RelationUserAndCompanyBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<RelationUserAndCompanyBean>() {
	            @Override
	            public RelationUserAndCompanyBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	RelationUserAndCompanyBean item = new RelationUserAndCompanyBean();
	            	
	            	item.setUserUniqueId	(EnjoyUtils.nullToStr(rs.getString("userUniqueId")));
					item.setTin				(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setUserEmail		(EnjoyUtils.nullToStr(rs.getString("userEmail")));
					item.setUserFullName	(EnjoyUtils.nullToStr(rs.getString("userFullName")));
					item.setUserStatus		(EnjoyUtils.nullToStr(rs.getString("userStatus")));
					item.setUserStatusName	(EnjoyUtils.nullToStr(rs.getString("userStatusName")));
	            	
	                return item;
	            }
	        });
			
//			for(RelationUserAndCompanyBean result:resultList){
//				result.setSeq(EnjoyUtils.nullToStr(seq++));
//			}
			
		}catch(Exception e){
			logger.error("searchByCriteria :: ", e);
			throw e;
		}finally{
			logger.info("[searchByCriteria][End]");
		}
		
		return resultList;
		
	}
	
	@Override
	public void insertRelationUserAndCompany(RelationUserAndCompanyBean vo) throws Exception{
		logger.info("[insertRelationUserAndCompany][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
		
		try{
			setColumnStr("userUniqueId", vo.getUserUniqueId(), TYPE_INT, columns, args, paramList, colTypeList);
			setColumnStr("tin", vo.getTin(), TYPE_STRING, columns, args, paramList, colTypeList);
			
			sql.append("INSERT INTO  relationuserncompany ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertRelationUserAndCompany] sql :: " + sql.toString());
            logger.info("[insertRelationUserAndCompany] paramList :: " + paramList.size());
            logger.info("[insertRelationUserAndCompany] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertRelationUserAndCompany] row :: " + row);
			
		}catch(Exception e){
			logger.error("insertRelationUserAndCompany :: ", e);
			throw e;
		}finally{
			logger.info("[insertRelationUserAndCompany][End]");
		}
	}
	
	@Override
	public void deleteRelationUserAndCompany(String tin) throws Exception{
		logger.info("[deleteRelationUserAndCompany][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("delete relationuserncompany where tin = ?");
			
			logger.info("[deleteRelationUserAndCompany] tin :: " + tin);
			logger.info("[deleteRelationUserAndCompany] sql :: " + sql.toString());
			
			int row = jdbcTemplate.update(sql.toString(), new Object[]{tin});
			
			logger.info("[deleteRelationUserAndCompany] row :: " + row);
		}catch(Exception e){
			logger.error("deleteRelationUserAndCompany :: ", e);
			throw e;
		}finally{
			logger.info("[deleteRelationUserAndCompany][End]");
		}
	}
	
	@Override
	public void deleteRelationUserAndCompany(String userUniqueId, String tin) throws Exception{
		logger.info("[deleteRelationUserAndCompany][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("delete from relationuserncompany where userUniqueId = ? and tin = ?");
			
			logger.info("[deleteRelationUserAndCompany] userUniqueId :: " + userUniqueId);
			logger.info("[deleteRelationUserAndCompany] tin :: " + tin);
			logger.info("[deleteRelationUserAndCompany] sql :: " + sql.toString());
			
			int row = jdbcTemplate.update(sql.toString(), new Object[]{userUniqueId ,tin});
			
			logger.info("[deleteRelationUserAndCompany] row :: " + row);
			
		}catch(Exception e){
			logger.error("deleteRelationUserAndCompany :: ", e);
			throw e;
		}finally{
			logger.info("[deleteRelationUserAndCompany][End]");
		}
	}
	
	@Override
	public int countForCheckLogin(int userUniqueId) throws Exception{
		logger.info("[countForCheckLogin][Begin]");
		
		StringBuilder       sql     = new StringBuilder();
		int 				result	= 0;
		
		try{
			sql.append("select count(*) cou from relationuserncompany where userUniqueId = ?");
			
			logger.info("[countForCheckLogin] userUniqueId 	:: " + userUniqueId);
			logger.info("[countForCheckLogin] sql 			:: " + sql.toString());
			
			result = jdbcTemplate.queryForObject(sql.toString() ,new Object[]{userUniqueId}, Integer.class);
			
			logger.info("[countForCheckLogin] result 			:: " + result);
			
		}catch(Exception e){
			logger.error("countForCheckLogin :: ", e);
			throw e;
		}finally{
			logger.info("[countForCheckLogin][End]");
		}
		
		return result;
	}
	
	@Override
	public ArrayList<ComboBean> getCompanyList(int userUniqueId) throws Exception{
		logger.info("[getCompanyList][Begin]");
		
		StringBuilder       	sql     	= new StringBuilder();
		ArrayList<ComboBean> 	resultList 	= new ArrayList<ComboBean>();
		ArrayList<Object>		params		= new ArrayList<Object>();
		
		try{
			
			if(userUniqueId==1){
				sql.append("select tin, companyName from company where companyStatus = 'A'");
			}else{
				sql.append("select a.tin, b.companyName");
				sql.append("\n	from relationuserncompany a");
				sql.append("\n		INNER JOIN company b on a.tin = b.tin");
				sql.append("\n	where b.companyStatus = 'A'");
				sql.append("\n		and a.userUniqueId = ?");
				
				params.add(userUniqueId);
			}
			
			logger.info("[getCompanyList] sql :: " + sql.toString());
			
			resultList = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return new ComboBean(EnjoyUtils.nullToStr(rs.getString("tin")), EnjoyUtils.nullToStr(rs.getString("companyName")));
	            }
	        });
			
			
		}catch(Exception e){
			logger.error("getCompanyList :: ", e);
			throw e;
		}finally{
			logger.info("[getCompanyList][End]");
		}
		
		return resultList;
	}
	
	@Override
	public ComboBean getCompanyForAdminEnjoy(int userUniqueId) throws Exception{
		logger.info("[getCompanyForAdminEnjoy][Begin]");
		
		StringBuilder       	sql     	= new StringBuilder();
		ArrayList<ComboBean> 	resultList 	= new ArrayList<ComboBean>();
		ComboBean				comboBean   = null;
		
		try{
			
			sql.append("select a.tin, b.companyName");
			sql.append("\n	from relationuserncompany a");
			sql.append("\n		INNER JOIN company b on a.tin = b.tin");
			sql.append("\n	where a.userUniqueId = ?");
			
			resultList = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), new Object[]{userUniqueId}, new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return new ComboBean(EnjoyUtils.nullToStr(rs.getString("tin")), EnjoyUtils.nullToStr(rs.getString("companyName")));
	            }
	        });
			
			if(resultList!=null && !resultList.isEmpty()){
				comboBean = resultList.get(0);
			}
			
		}catch(Exception e){
			logger.error("getCompanyForAdminEnjoy :: ", e);
			throw e;
		}finally{
			logger.info("[getCompanyForAdminEnjoy][End]");
		}
		
		return comboBean;
	}
}
