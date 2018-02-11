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
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.services.AbstractJdbcService;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.CompanyVendorBean;

@Repository
public class CompanyVendorServiceImpl extends AbstractJdbcService implements CompanyVendorService{
	
	private static final Logger logger = Logger.getLogger(CompanyVendorServiceImpl.class);

	@Override
	public void searchByCriteria(PaginatedListBean paginatedList,CompanyVendorBean vo) throws Exception {
		logger.info("[searchByCriteria][Begin]");
		
		StringBuilder       			sql         = new StringBuilder();
		ArrayList<CompanyVendorBean>	resultList 	= new ArrayList<CompanyVendorBean>();
		ArrayList<Object>				params		= new ArrayList<Object>();
		
		try{	
			
			sql.append("select * from companyvendor where tinCompany = ?");
			
			params.add(vo.getTinCompany());
			
			if(EnjoyUtils.chkNull(vo.getVendorName())){
				sql.append("\n and vendorName LIKE CONCAT('%',?, '%')");
				params.add(vo.getVendorName());
			}
			
			if(EnjoyUtils.chkNull(vo.getBranchName())){
				sql.append("\n and branchName LIKE CONCAT(?, '%')");
				params.add(vo.getBranchName());
			}
			
			if(EnjoyUtils.chkNull(vo.getTel())){
				sql.append("\n and REPLACE(tel,'-','') like CONCAT('%',?,'%')");
				params.add(vo.getTel());
			}
			
			logger.info("[searchByCriteria] sql :: " + sql);
			
			String rowNumSql = decorateRowNumSQL(sql.toString(), paginatedList.getIndex(), paginatedList.getPageSize());
			
			resultList = (ArrayList<CompanyVendorBean>) jdbcTemplate.query(rowNumSql, params.toArray(), new RowMapper<CompanyVendorBean>() {
	            @Override
	            public CompanyVendorBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	CompanyVendorBean item = new CompanyVendorBean();
	            	
	            	item.setVendorCode			(EnjoyUtils.nullToStr(rs.getString("vendorCode")));
					item.setTinCompany			(EnjoyUtils.nullToStr(rs.getString("tinCompany")));
					item.setTin					(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setVendorName			(EnjoyUtils.nullToStr(rs.getString("vendorName")));
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
	            	
	                return item;
	            }
	        });
			
			paginatedList.setFullListSize(countTotal(sql.toString(),params.toArray()));
			paginatedList.setList(resultList);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[searchByCriteria][End]");
		}
	}

	@Override
	public CompanyVendorBean getCompanyVendor(String vendorCode,String tinCompany) throws Exception {
		logger.info("[getCompanyVendor][Begin]");
		
		StringBuilder 					sql 				= new StringBuilder();
		CompanyVendorBean 				companyVendorBean 	= null;
		ArrayList<CompanyVendorBean> 	result 				= null;
		
		try{
		    
		    sql.append("select * from companyvendor where tinCompany = ? and vendorCode = ?");
		    
		    logger.info("[getCompanyVendor] tinCompany 	:: " + tinCompany);
		    logger.info("[getCompanyVendor] vendorCode 	:: " + vendorCode);
		    logger.info("[getCompanyVendor] sql 		:: " + sql.toString());
			
		    result = (ArrayList<CompanyVendorBean>) jdbcTemplate.query(sql.toString(), new Object[]{tinCompany,vendorCode}, new RowMapper<CompanyVendorBean>() {
	            @Override
	            public CompanyVendorBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	CompanyVendorBean item = new CompanyVendorBean();
	            	
	            	item.setVendorCode			(EnjoyUtils.nullToStr(rs.getString("vendorCode")));
					item.setTinCompany			(EnjoyUtils.nullToStr(rs.getString("tinCompany")));
					item.setTin					(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setVendorName			(EnjoyUtils.nullToStr(rs.getString("vendorName")));
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
	            	
	                return item;
	            }
	        });
		    
		    if(result != null && !result.isEmpty()){
		    	companyVendorBean = result.get(0);
		    }
			
			
		}catch(Exception e){
			logger.error("[getCompanyVendor] :: ",e);
			throw e;
		}finally{
	        logger.info("[getCompanyVendor][End]");
		}
		
		return companyVendorBean;
	}
	
	@Override
	public void insertCompanyVendor(CompanyVendorBean vo)throws Exception {
		logger.info("[insertCompanyVendor][Begin]");
        
        StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
        
        try{
        	setColumnStr("vendorCode", vo.getVendorCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("tinCompany", vo.getTinCompany(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("vendorName", vo.getVendorName(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("tin", vo.getTin(), TYPE_STRING, columns, args, paramList, colTypeList);
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
        	

            sql.append("INSERT INTO  companyvendor ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertCompanyVendor] sql :: " + sql.toString());
            logger.info("[insertCompanyVendor] paramList :: " + paramList.size());
            logger.info("[insertCompanyVendor] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertCompanyVendor] row :: " + row);
            
        }catch(Exception e){
            logger.error("[insertCompanyVendor] :: ",e);
            throw e;
        }finally{
            logger.info("[insertCustomerDetails][End]");
        }
	}

	@Override
	public void updateCompanyvendor(final CompanyVendorBean vo)throws Exception {
		logger.info("[updateCompanyvendor][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update companyvendor");
			sql.append("\n	set vendorName 			= ?");
			sql.append("\n		, tin 				= ?");
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
			sql.append("\n	where vendorCode = ?");
			sql.append("\n		and tinCompany = ?");
			
			int row = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	                int i = 1;
	                
	                ps.setString(i++, vo.getVendorName());
	                ps.setString(i++, vo.getTin());
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
	                ps.setString(i++, vo.getVendorCode());
	                ps.setString(i++, vo.getTinCompany());
	            }
	        });
			
			logger.info("[updateCompanyvendor] row :: " + row);
			
		}catch(Exception e){
			logger.error("[updateCompanyvendor] :: ",e);
			throw e;
		}finally{
			logger.info("[updateCompanyvendor][End]");
		}
	}
	
	@Override
	public int genId(String tinCompany) throws Exception {
		logger.info("[genId][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		int 				result		= 1;
		ArrayList<Integer>	resultList 	= new ArrayList<Integer>();
		
		try{
		    
		    sql.append("select (max(vendorCode) + 1) newId from companyvendor where tinCompany = ?");
		    
		    logger.info("[genId] tinCompany :: " + tinCompany);
		    logger.info("[genId] sql 		:: " + sql.toString());
			
		    resultList = (ArrayList<Integer>) jdbcTemplate.query(sql.toString(), new Object[]{tinCompany}, new RowMapper<Integer>() {
	            @Override
	            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return rs.getInt("newId");
	            }
	        });
			
			if(resultList!=null && !resultList.isEmpty()){
				result = resultList.get(0);
			}
			
			logger.info("[genId] result :: " + result);
			
			
		}catch(Exception e){
			logger.error("[genId] :: ",e);
			throw e;
		}finally{
	        logger.info("[genId][End]");
		}
		
		return result;
	}

	@Override
	public int checkDupVendorName(String vendorName, String branchName, String vendorCode, String tinCompany) throws Exception {
		logger.info("[checkDupVendorName][Begin]");
		
		StringBuilder 		sql 	= new StringBuilder();
		int 				result	= 0;
		ArrayList<Object>	params	= new ArrayList<Object>();
		
		try{
			sql.append("select count(*) cou from companyvendor where vendorName = ? and branchName = ? and tinCompany = ?");
			
			params.add(vendorName);
			params.add(branchName);
			params.add(tinCompany);
			
			if(EnjoyUtils.chkNull(vendorCode)){
				sql.append(" and vendorCode <> ?");
				params.add(vendorCode);
			}
			
			logger.info("[checkDupVendorName] sql :: " + sql.toString());
			
			result = jdbcTemplate.queryForObject(sql.toString() ,params.toArray(), Integer.class);
			
			logger.info("[checkDupVendorName] result 			:: " + result);
			
			
			
		}catch(Exception e){
			logger.error("[checkDupVendorName] :: ",e);
			throw e;
		}finally{
			logger.info("[checkDupVendorName][End]");
		}
		
		return result;
	}

	@Override
	public ArrayList<ComboBean> vendorNameList(String vendorName, String tinCompany) throws Exception {
		logger.info("[vendorNameList][Begin]");
		
		StringBuilder       	sql         = new StringBuilder();
		ArrayList<ComboBean>	resultList 	= new ArrayList<ComboBean>();
		ArrayList<Object>		params		= new ArrayList<Object>();
		
		try{
			
			sql.append("select * from companyvendor where tinCompany = ?");
			
			params.add(tinCompany);
			
			if(EnjoyUtils.chkNull(vendorName)){
				sql.append(" and vendorName LIKE CONCAT('%',?, '%')");
				params.add(vendorName);
			}
			
			sql.append(" order by vendorName asc limit 10");
			
			logger.info("[vendorNameList] sql :: " + sql);
			
			resultList = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ComboBean item = new ComboBean();
	            	
	            	item.setCode(EnjoyUtils.nullToStr(rs.getString("vendorCode")));
	            	item.setDesc(EnjoyUtils.nullToStr(rs.getString("vendorName")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[vendorNameList][End]");
		}
		
		return resultList;
	}
	
	@Override
	public ArrayList<ComboBean> branchNameList(String vendorName, String branchName, String tinCompany) throws Exception {
		logger.info("[branchNameList][Begin]");
		
		StringBuilder       	sql         = new StringBuilder();
		ArrayList<ComboBean>	resultList 	= new ArrayList<ComboBean>();
		ArrayList<Object>		params		= new ArrayList<Object>();
		
		try{
			
			sql.append("select * from companyvendor where tinCompany = ?");
			
			params.add(tinCompany);
			
			if(EnjoyUtils.chkNull(vendorName)){
				sql.append(" and vendorName = ?");
				params.add(vendorName);
			}
			
			if(EnjoyUtils.chkNull(branchName)){
				sql.append(" and branchName LIKE CONCAT('%',?, '%')");
				params.add(branchName);
			}
			
			sql.append(" order by branchName asc limit 10");
			
			logger.info("[vendorNameList] sql :: " + sql);
			
			resultList = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ComboBean item = new ComboBean();
	            	
	            	item.setCode(EnjoyUtils.nullToStr(rs.getString("vendorCode")));
	            	item.setDesc(EnjoyUtils.nullToStr(rs.getString("branchName")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[vendorNameList][End]");
		}
		
		return resultList;
	}
	
	@Override
	public CompanyVendorBean getCompanyVendorByName(String vendorName, String branchName, String tinCompany) throws Exception {
		logger.info("[getCompanyVendorByName][Begin]");
		
		StringBuilder 					sql 				= new StringBuilder();
		CompanyVendorBean 				companyVendorBean 	= null;
		ArrayList<CompanyVendorBean> 	result 				= null;
		ArrayList<Object>				params				= new ArrayList<Object>();
		
		try{
		    logger.info("[getCompanyVendorByName] tinCompany 	:: " + tinCompany);
		    logger.info("[getCompanyVendorByName] vendorName 	:: " + vendorName);
		    logger.info("[getCompanyVendorByName] branchName 	:: " + branchName);
		    
		    sql.append("select * from companyvendor where tinCompany = ? and vendorName = ?");
		    
		    params.add(tinCompany);
		    params.add(vendorName);
		    
		    if(EnjoyUtils.chkNull(branchName)){
				sql.append(" and branchName = ?");
				params.add(branchName);
			}
		    
		    logger.info("[getCompanyVendorByName] sql 		:: " + sql.toString());
			
		    result = (ArrayList<CompanyVendorBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<CompanyVendorBean>() {
	            @Override
	            public CompanyVendorBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	CompanyVendorBean item = new CompanyVendorBean();
	            	
	            	item.setVendorCode			(EnjoyUtils.nullToStr(rs.getString("vendorCode")));
					item.setTinCompany			(EnjoyUtils.nullToStr(rs.getString("tinCompany")));
					item.setTin					(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setVendorName			(EnjoyUtils.nullToStr(rs.getString("vendorName")));
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
	            	
	                return item;
	            }
	        });
		    
		    if(result != null && !result.isEmpty() && result.size()==1){
		    	companyVendorBean = result.get(0);
		    }
			
			
		}catch(Exception e){
			logger.error("[getCompanyVendorByName] :: ",e);
			throw e;
		}finally{
	        logger.info("[getCompanyVendorByName][End]");
		}
		
		return companyVendorBean;
	}
	
}
