package com.enjoy.core.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.CompanyDetailsBean;
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.utils.EnjoyUtils;

@Repository
public class CompanyDetailsServiceImpl extends AbstractJdbcService implements CompanyDetailsService{
	
	private static final Logger logger = Logger.getLogger(CompanyDetailsServiceImpl.class);

	@Override
	public void getListCompanyDetails(PaginatedListBean paginatedList	,CompanyDetailsBean companyDetailsBean) throws Exception {
		logger.info("[getListCompanyDetails][Begin]");
		
		StringBuilder       			sql         = new StringBuilder();
		ArrayList<CompanyDetailsBean>	resultList 	= new ArrayList<CompanyDetailsBean>();
		ArrayList<Object>				params		= new ArrayList<Object>();
		
		try{	
			
			sql.append("select a.*, b.companyStatusName ");
			sql.append("\n	from company a, refcompanystatus b");
			sql.append("\n	where b.companyStatusCode = a.companyStatus");
			sql.append("\n		and a.tin <> '9999999999999'");
			
			if(EnjoyUtils.chkNull(companyDetailsBean.getCompanyName())){
				sql.append("\n and a.companyName LIKE CONCAT(?, '%')");
				params.add(companyDetailsBean.getCompanyName());
			}
			
			if(EnjoyUtils.chkNull(companyDetailsBean.getTin())){
				sql.append("\n and a.tin LIKE CONCAT(?, '%')");
				params.add(companyDetailsBean.getTin());
			}
			
			if(EnjoyUtils.chkNull(companyDetailsBean.getCompanyStatus())){
				sql.append("\n and a.companyStatus = ?");
				params.add(companyDetailsBean.getCompanyStatus());
			}
			
			logger.info("[getListCompanyDetails] sql :: " + sql);
			
			String rowNumSql = decorateRowNumSQL(sql.toString(), paginatedList.getIndex(), paginatedList.getPageSize());
			
			resultList = (ArrayList<CompanyDetailsBean>) jdbcTemplate.query(rowNumSql, params.toArray(), new RowMapper<CompanyDetailsBean>() {
	            @Override
	            public CompanyDetailsBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	CompanyDetailsBean item = new CompanyDetailsBean();
	            	
	            	item.setTin					(EnjoyUtils.nullToStr(rs.getString("tin")));
	            	item.setCompanyName			(EnjoyUtils.nullToStr(rs.getString("companyName")));
	            	item.setBranchName			(EnjoyUtils.nullToStr(rs.getString("branchName")));
	            	item.setBuildingName		(EnjoyUtils.nullToStr(rs.getString("buildingName")));
	            	item.setHouseNumber			(EnjoyUtils.nullToStr(rs.getString("houseNumber")));
	            	item.setMooNumber			(EnjoyUtils.nullToStr(rs.getString("mooNumber")));
	            	item.setSoiName				(EnjoyUtils.nullToStr(rs.getString("soiName")));
	            	item.setStreetName			(EnjoyUtils.nullToStr(rs.getString("streetName")));
	            	item.setProvinceCode		(EnjoyUtils.nullToStr(rs.getString("provinceCode")));
	            	item.setDistrictCode		(EnjoyUtils.nullToStr(rs.getString("districtCode")));
	            	item.setSubdistrictCode		(EnjoyUtils.nullToStr(rs.getString("subdistrictCode")));
	            	
	            	item.setPostCode			(EnjoyUtils.nullToStr(rs.getString("postCode")));
	            	item.setTel					(EnjoyUtils.nullToStr(rs.getString("tel")));
	            	item.setFax					(EnjoyUtils.nullToStr(rs.getString("fax")));
	            	item.setEmail				(EnjoyUtils.nullToStr(rs.getString("email")));
	            	item.setRemark				(EnjoyUtils.nullToStr(rs.getString("remark")));
	            	item.setCompanyStatus		(EnjoyUtils.nullToStr(rs.getString("companyStatus")));
	            	item.setCompanyStatusName	(EnjoyUtils.nullToStr(rs.getString("companyStatusName")));
	            	
	                return item;
	            }
	        });
			
			paginatedList.setFullListSize(countTotal(sql.toString(),params.toArray()));
			paginatedList.setList(resultList);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getListCompanyDetails][End]");
		}
		
	}

	@Override
	public CompanyDetailsBean getCompanyDetails(String tin) throws Exception {
		logger.info("[getCompanyDetails][Begin]");
		
		StringBuilder 					sql 				= new StringBuilder();
		CompanyDetailsBean 				companyDetailsBean = null;
		ArrayList<CompanyDetailsBean> 	result 				= null;
		
		try{
		    
		    sql.append("select * from company where tin = ?");
		    
		    logger.info("[getCompanyDetails] tin :: " + tin);
		    logger.info("[getCompanyDetails] sql :: " + sql.toString());
			
		    result = (ArrayList<CompanyDetailsBean>) jdbcTemplate.query(sql.toString(), new Object[]{tin}, new RowMapper<CompanyDetailsBean>() {
	            @Override
	            public CompanyDetailsBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	CompanyDetailsBean item = new CompanyDetailsBean();
	            	
	            	item.setTin					(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setCompanyName			(EnjoyUtils.nullToStr(rs.getString("companyName")));
					item.setBranchName			(EnjoyUtils.nullToStr(rs.getString("branchName")));
					item.setBuildingName		(EnjoyUtils.nullToStr(rs.getString("buildingName")));
					item.setHouseNumber			(EnjoyUtils.nullToStr(rs.getString("houseNumber")));
					item.setMooNumber			(EnjoyUtils.nullToStr(rs.getString("mooNumber")));
					item.setSoiName				(EnjoyUtils.nullToStr(rs.getString("soiName")));
					item.setStreetName			(EnjoyUtils.nullToStr(rs.getString("streetName")));
					item.setProvinceCode		(EnjoyUtils.nullToStr(rs.getString("provinceCode")));
					item.setDistrictCode		(EnjoyUtils.nullToStr(rs.getString("districtCode")));
					item.setSubdistrictCode		(EnjoyUtils.nullToStr(rs.getString("subdistrictCode")));
					item.setPostCode			(EnjoyUtils.nullToStr(rs.getString("postCode")));
					item.setTel					(EnjoyUtils.nullToStr(rs.getString("tel")));
					item.setFax					(EnjoyUtils.nullToStr(rs.getString("fax")));
					item.setEmail				(EnjoyUtils.nullToStr(rs.getString("email")));
					item.setRemark				(EnjoyUtils.nullToStr(rs.getString("remark")));
					item.setCompanyStatus		(EnjoyUtils.nullToStr(rs.getString("companyStatus")));
	            	
	                return item;
	            }
	        });
		    
		    if(result != null && !result.isEmpty()){
		    	companyDetailsBean = result.get(0);
		    }
			
			
		}catch(Exception e){
			logger.error("[getCompanyDetails] :: ",e);
			throw e;
		}finally{
	        logger.info("[getCompanyDetails][End]");
		}
		
		return companyDetailsBean;
	}

	@Override
	public ArrayList<ComboBean> getCompanystatusCombo() throws Exception {
		logger.info("[getCompanystatusCombo][Begin]");
		
		StringBuilder       	sql         = new StringBuilder();
		ArrayList<ComboBean>	resultList 	= new ArrayList<ComboBean>();
		ArrayList<Object>		params		= new ArrayList<Object>();
		
		try{
			
			sql.append("select * from refcompanystatus");
			
			logger.info("[getCompanystatusCombo] sql :: " + sql);
			
			resultList = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ComboBean item = new ComboBean();
	            	
	            	item.setCode(EnjoyUtils.nullToStr(rs.getString("companyStatusCode")));
	            	item.setDesc(EnjoyUtils.nullToStr(rs.getString("companyStatusName")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getCompanystatusCombo][End]");
		}
		
		return resultList;
	}

	@Override
	public int checkDupTin(String tin) throws Exception {
		logger.info("[checkDupTin][Begin]");
		
		StringBuilder 		sql 	= new StringBuilder();
		int 				result	= 0;
		ArrayList<Object>	params	= new ArrayList<Object>();
		
		try{
			logger.info("[checkDupTin] tin :: " + tin);
			
			sql.append("select count(*) cou from company where tin = ?");
			
			params.add(tin);
			
			logger.info("[checkDupTin] sql :: " + sql.toString());
			
			result = jdbcTemplate.queryForObject(sql.toString() ,params.toArray(), Integer.class);
			
			logger.info("[checkDupTin] result 			:: " + result);
			
			
			
		}catch(Exception e){
			logger.error("[checkDupTin] :: ",e);
			throw e;
		}finally{
			logger.info("[checkDupTin][End]");
		}
		
		return result;
	}

	@Override
	public void insertCompanyDetail(CompanyDetailsBean vo)throws Exception {
		logger.info("[insertCompanyDetail][Begin]");
        
        StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
        
        try{
        	setColumnStr("tin", vo.getTin(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("companyName", vo.getCompanyName(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("branchName", vo.getBranchName(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("buildingName", vo.getBuildingName(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("houseNumber", vo.getHouseNumber(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("mooNumber", vo.getMooNumber(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("soiName", vo.getSoiName(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("streetName", vo.getStreetName(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("provinceCode", vo.getProvinceCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("districtCode", vo.getDistrictCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("subdistrictCode", vo.getSubdistrictCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("postCode", vo.getPostCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("tel", vo.getTel(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("fax", vo.getFax(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("email", vo.getEmail(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("remark", vo.getRemark(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("companyStatus", vo.getCompanyStatus(), TYPE_STRING, columns, args, paramList, colTypeList);

            sql.append("INSERT INTO  company ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertCompanyDetail] sql :: " + sql.toString());
            logger.info("[insertCompanyDetail] paramList :: " + paramList.size());
            logger.info("[insertCompanyDetail] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertCompanyDetail] row :: " + row);
            
        }catch(Exception e){
            logger.error("[insertCompanyDetail] :: ",e);
            throw e;
        }finally{
            logger.info("[insertCompanyDetail][End]");
        }
	}

	@Override
	public void updateCompanyDetail(final CompanyDetailsBean vo)throws Exception {
		logger.info("[updateCompanyDetail][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update company");
			sql.append("\n	set companyName 		= ?");
			sql.append("\n		, branchName 		= ?");
			sql.append("\n		, buildingName		= ?");
			sql.append("\n		, houseNumber		= ?");
			sql.append("\n		, mooNumber			= ?");
			sql.append("\n		, soiName			= ?");
			sql.append("\n		, streetName 		= ?");
			sql.append("\n		, provinceCode 		= ?");
			sql.append("\n		, districtCode 		= ?");
			sql.append("\n		, subdistrictCode 	= ?");
			sql.append("\n		, postCode 			= ?");
			sql.append("\n		, tel 				= ?");
			sql.append("\n		, fax 				= ?");
			sql.append("\n		, email 			= ?");
			sql.append("\n		, remark 			= ?");
			sql.append("\n		, companyStatus 	= ?");
			sql.append("\n	where tin = ?");
			
			int row = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	                int i = 1;
	                
	                ps.setString(i++, vo.getCompanyName());
	                ps.setString(i++, vo.getBranchName());
	                ps.setString(i++, vo.getBuildingName());
	                ps.setString(i++, vo.getHouseNumber());
	                ps.setString(i++, vo.getMooNumber());
	                ps.setString(i++, vo.getSoiName());
	                ps.setString(i++, vo.getStreetName());
	                ps.setString(i++, vo.getProvinceCode());
	                ps.setString(i++, vo.getDistrictCode());
	                ps.setString(i++, vo.getSubdistrictCode());
	                ps.setString(i++, vo.getPostCode());
	                ps.setString(i++, vo.getTel());
	                ps.setString(i++, vo.getFax());
	                ps.setString(i++, vo.getEmail());
	                ps.setString(i++, vo.getRemark());
	                ps.setString(i++, vo.getCompanyStatus());
	                ps.setString(i++, vo.getTin());
	            }
	        });
			
			logger.info("[updateCompanyDetail] row :: " + row);
			
		}catch(Exception e){
			logger.error("[updateCompanyDetail] :: ",e);
			throw e;
		}finally{
			logger.info("[updateCompanyDetail][End]");
		}
	}

	@Override
	public ArrayList<ComboBean> companyNameList(String companyName,int userUniqueId) throws Exception {
		logger.info("[companyNameList][Begin]");
		
		StringBuilder       	sql         = new StringBuilder();
		ArrayList<ComboBean>	resultList 	= new ArrayList<ComboBean>();
		ArrayList<Object>		params		= new ArrayList<Object>();
		
		try{
			
			if(userUniqueId==1){
				sql.append("select tin, companyName ");
				sql.append("\n	from company ");
				sql.append("\n	where companyName LIKE CONCAT(?, '%')");
				sql.append("\n		and companyStatus = 'A'");
				sql.append("\n		and tin <> '9999999999999'");
				sql.append("\n	order by companyName asc limit 10 ");
				
				params.add(companyName);
			}else{
				sql.append("select a.tin, b.companyName");
				sql.append("\n	from relationuserncompany a");
				sql.append("\n		inner join company b on b.tin = a.tin");
				sql.append("\n	where a.userUniqueId = ?");
				sql.append("\n		and b.companyName LIKE CONCAT(?, '%')");
				sql.append("\n		and a.tin <> '9999999999999'");
				
				params.add(userUniqueId);
				params.add(companyName);
			}
			
			logger.info("[companyNameList] sql :: " + sql);
			
			resultList = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ComboBean item = new ComboBean();
	            	
	            	item.setCode(EnjoyUtils.nullToStr(rs.getString("tin")));
	            	item.setDesc(EnjoyUtils.nullToStr(rs.getString("companyName")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[companyNameList][End]");
		}
		
		return resultList;
	}

	@Override
	public String getTin(String companyName) throws Exception {
		logger.info("[getTin][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		String 				result		= "";
		ArrayList<String>	resultList 	= new ArrayList<String>();
		
		try{
			
			sql.append("select tin ");
			sql.append("\n	from company ");
			sql.append("\n	where companyStatus = 'A'");
			sql.append("\n		and tin <> '9999999999999'");
			sql.append("\n		and companyName = ?");
			
			logger.info("[getTin] companyName 	:: " + companyName);
			logger.info("[getTin] sql 			:: " + sql.toString());
			
			resultList = (ArrayList<String>) jdbcTemplate.query(sql.toString(), new Object[]{companyName}, new RowMapper<String>() {
	            @Override
	            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return EnjoyUtils.nullToStr(rs.getString("tin"));
	            }
	        });
			
			if(resultList!=null && !resultList.isEmpty()){
				result = resultList.get(0);
			}
			
			logger.info("[getTin] result 			:: " + result);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getTin][End]");
		}
		
		return result;
	}

	@Override
	public ArrayList<ComboBean> getCompanyCombo() throws Exception {
		logger.info("[getCompanyCombo][Begin]");
		
		StringBuilder       	sql         = new StringBuilder();
		ArrayList<ComboBean>	resultList 	= new ArrayList<ComboBean>();
		
		try{
			
			sql.append("select tin,companyName from company where companyStatus = 'A' and tin <> '9999999999999'");
			
			logger.info("[getCompanyCombo] sql :: " + sql);
			
			resultList = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ComboBean item = new ComboBean();
	            	
	            	item.setCode(EnjoyUtils.nullToStr(rs.getString("tin")));
	            	item.setDesc(EnjoyUtils.nullToStr(rs.getString("companyName")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getCompanyCombo][End]");
		}
		
		return resultList;
	}

	@Override
	public ArrayList<ComboBean> tinListForAutoComplete(String tin) throws Exception {
		logger.info("[tinListForAutoComplete][Begin]");
		
		StringBuilder       	sql         = new StringBuilder();
		ArrayList<ComboBean>	resultList 	= new ArrayList<ComboBean>();
		ArrayList<Object>		params		= new ArrayList<Object>();
		
		try{
			
			logger.info("[tinListForAutoComplete] tin :: " + tin);
			
			sql.append("select tin from company where tin <> '9999999999999'");
			
			if(EnjoyUtils.chkNull(tin)){
				sql.append(" and tin LIKE CONCAT(?, '%')");
				params.add(tin);
			}
			
			sql.append(" order by tin asc limit 10");
			
			logger.info("[tinListForAutoComplete] sql :: " + sql);
			
			resultList = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ComboBean item = new ComboBean();
	            	
	            	item.setCode(EnjoyUtils.nullToStr(rs.getString("tin")));
	            	item.setDesc(EnjoyUtils.nullToStr(rs.getString("tin")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[tinListForAutoComplete][End]");
		}
		
		return resultList;
	}

	@Override
	public ArrayList<ComboBean> companyNameListForAutoComplete(String companyName) throws Exception {
		logger.info("[companyNameListForAutoComplete][Begin]");
		
		StringBuilder       	sql         = new StringBuilder();
		ArrayList<ComboBean>	resultList 	= new ArrayList<ComboBean>();
		ArrayList<Object>		params		= new ArrayList<Object>();
		
		try{
			
			logger.info("[companyNameListForAutoComplete] companyName :: " + companyName);
			
			sql.append("select companyName from company where tin <> '9999999999999'");
			
			if(EnjoyUtils.chkNull(companyName)){
				sql.append(" and companyName LIKE CONCAT(?, '%')");
				params.add(companyName);
			}
			
			sql.append(" order by companyName asc limit 10");
			
			logger.info("[companyNameListForAutoComplete] sql :: " + sql);
			
			resultList = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ComboBean item = new ComboBean();
	            	
	            	item.setCode(EnjoyUtils.nullToStr(rs.getString("companyName")));
	            	item.setDesc(EnjoyUtils.nullToStr(rs.getString("companyName")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[companyNameListForAutoComplete][End]");
		}
		
		return resultList;
	}
	
	@Override
	public String getCompanyName(String tin) throws Exception {
		logger.info("[getCompanyName][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		String 				result		= "";
		ArrayList<String>	resultList 	= new ArrayList<String>();
		
		try{
			
			sql.append("select companyName from company where tin = ?");
			
			logger.info("[getCompanyName] tin 	:: " + tin);
			logger.info("[getCompanyName] sql 	:: " + sql.toString());
			
			resultList = (ArrayList<String>) jdbcTemplate.query(sql.toString(), new Object[]{tin}, new RowMapper<String>() {
	            @Override
	            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return EnjoyUtils.nullToStr(rs.getString("companyName"));
	            }
	        });
			
			if(resultList!=null && !resultList.isEmpty()){
				result = resultList.get(0);
			}
			
			logger.info("[getCompanyName] result 			:: " + result);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getCompanyName][End]");
		}
		
		return result;
	}
	
	
}
