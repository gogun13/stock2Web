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
import com.enjoy.core.main.ConfigFile;
import com.enjoy.core.services.AbstractJdbcService;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.CustomerDetailsBean;

@Repository
public class CustomerServiceImpl extends AbstractJdbcService implements CustomerService{
	
	private static final Logger logger = Logger.getLogger(CustomerServiceImpl.class);

	@Override
	public void getListCustomerDetails(PaginatedListBean paginatedList	,CustomerDetailsBean customerDetailsBean) throws Exception {
		logger.info("[getListCustomerDetails][Begin]");
		
		StringBuilder       			sql         = new StringBuilder();
		ArrayList<CustomerDetailsBean>	resultList 	= new ArrayList<CustomerDetailsBean>();
		ArrayList<Object>				params		= new ArrayList<Object>();
		
		try{	
			
			sql.append("select a.*, b.customerStatusName");
			sql.append("\n	from customer a, refcustomerstatus b");
			sql.append("\n	where b.customerStatusCode = a.cusStatus");
			sql.append("\n		and a.tin = ?");
			
			params.add(customerDetailsBean.getTin());
			
			if(EnjoyUtils.chkNull(customerDetailsBean.getCusCode())){
				sql.append("\n and a.cusCode LIKE CONCAT(?, '%')");
				params.add(customerDetailsBean.getCusCode());
			}
			
			if(EnjoyUtils.chkNull(customerDetailsBean.getFullName())){
				sql.append("\n and CONCAT(a.cusName, ' ', a.cusSurname) LIKE CONCAT('%',?, '%')");
				params.add(customerDetailsBean.getFullName());
			}
			
			if(EnjoyUtils.chkNull(customerDetailsBean.getTel())){
				sql.append("\n and REPLACE(a.tel,'-','') like CONCAT('%',?,'%')");
				params.add(customerDetailsBean.getTel());
			}
			
			if(EnjoyUtils.chkNull(customerDetailsBean.getCusStatus())){
				sql.append("\n and a.cusStatus = ?");
				params.add(customerDetailsBean.getCusStatus());
			}
			
			logger.info("[getListCustomerDetails] sql :: " + sql);
			
			String rowNumSql = decorateRowNumSQL(sql.toString(), paginatedList.getIndex(), paginatedList.getPageSize());
			
			resultList = (ArrayList<CustomerDetailsBean>) jdbcTemplate.query(rowNumSql, params.toArray(), new RowMapper<CustomerDetailsBean>() {
	            @Override
	            public CustomerDetailsBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	CustomerDetailsBean item = new CustomerDetailsBean();
	            	
	            	item.setCusCode				(EnjoyUtils.nullToStr(rs.getString("cusCode")));
					item.setTin					(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setCusName				(EnjoyUtils.nullToStr(rs.getString("cusName")));
					item.setCusSurname			(EnjoyUtils.nullToStr(rs.getString("cusSurname")));
					item.setSex					(EnjoyUtils.nullToStr(rs.getString("sex")));
					item.setIdType				(EnjoyUtils.nullToStr(rs.getString("idType")));
					item.setIdNumber			(EnjoyUtils.nullToStr(rs.getString("idNumber")));
					item.setBirthDate			(EnjoyUtils.dateToThaiDisplay(rs.getString("birthDate")));
					item.setReligion			(EnjoyUtils.nullToStr(rs.getString("religion")));
					item.setJob					(EnjoyUtils.nullToStr(rs.getString("job")));
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
					item.setCusStatus			(EnjoyUtils.nullToStr(rs.getString("cusStatus")));
					item.setStartDate			(EnjoyUtils.dateToThaiDisplay(rs.getString("startDate")));
					item.setExpDate				(EnjoyUtils.dateToThaiDisplay(rs.getString("expDate")));
					item.setPoint				(EnjoyUtils.nullToStr(rs.getString("point")));
					item.setRemark				(EnjoyUtils.nullToStr(rs.getString("remark")));
					item.setCustomerStatusName	(EnjoyUtils.nullToStr(rs.getString("customerStatusName")));
					String fullName	= EnjoyUtils.nullToStr(rs.getString("cusName")) + " " + EnjoyUtils.nullToStr(rs.getString("cusSurname"));
					item.setFullName			(fullName);
					item.setBranchName			(EnjoyUtils.nullToStr(rs.getString("branchName")));
					item.setCusGroupCode		(EnjoyUtils.nullToStr(rs.getString("cusGroupCode")));
	            	
	                return item;
	            }
	        });
			
			paginatedList.setFullListSize(countTotal(sql.toString(),params.toArray()));
			paginatedList.setList(resultList);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getListCustomerDetails][End]");
		}
	}

	@Override
	public CustomerDetailsBean getCustomerDetail(String cusCode,String tin) throws Exception {
		logger.info("[getCustomerDetail][Begin]");
		
		StringBuilder 					sql 				= new StringBuilder();
		CustomerDetailsBean 			customerDetailsBean = null;
		ArrayList<CustomerDetailsBean> 	result 				= null;
		
		try{
		    
		    sql.append("select a.*, b.groupSalePrice");
		    sql.append("\n	from customer a");
		    sql.append("\n		LEFT JOIN relationgroupcustomer b ON a.cusGroupCode = b.cusGroupCode and a.tin = b.tin");
		    sql.append("\n	where b.cusGroupStatus = 'A'");
		    sql.append("\n		and a.cusCode 	= ?");
		    sql.append("\n		and a.tin 		= ?");
		    
		    logger.info("[getCustomerDetail] cusCode 	:: " + cusCode);
		    logger.info("[getCustomerDetail] tin 		:: " + tin);
		    logger.info("[getCustomerDetail] sql 		:: " + sql.toString());
			
		    result = (ArrayList<CustomerDetailsBean>) jdbcTemplate.query(sql.toString(), new Object[]{cusCode,tin}, new RowMapper<CustomerDetailsBean>() {
	            @Override
	            public CustomerDetailsBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	CustomerDetailsBean item = new CustomerDetailsBean();
	            	
	            	item.setCusCode				(EnjoyUtils.nullToStr(rs.getString("cusCode")));
					item.setTin					(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setCusName				(EnjoyUtils.nullToStr(rs.getString("cusName")));
					item.setCusSurname			(EnjoyUtils.nullToStr(rs.getString("cusSurname")));
					item.setSex					(EnjoyUtils.nullToStr(rs.getString("sex")));
					item.setIdType				(EnjoyUtils.nullToStr(rs.getString("idType")));
					item.setIdNumber			(EnjoyUtils.nullToStr(rs.getString("idNumber")));
					item.setBirthDate			(EnjoyUtils.dateToThaiDisplay(rs.getString("birthDate")));
					item.setReligion			(EnjoyUtils.nullToStr(rs.getString("religion")));
					item.setJob					(EnjoyUtils.nullToStr(rs.getString("job")));
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
					item.setCusStatus			(EnjoyUtils.nullToStr(rs.getString("cusStatus")));
					item.setStartDate			(EnjoyUtils.dateToThaiDisplay(rs.getString("startDate")));
					item.setExpDate				(EnjoyUtils.dateToThaiDisplay(rs.getString("expDate")));
					item.setPoint				(EnjoyUtils.nullToStr(rs.getString("point")));
					item.setRemark				(EnjoyUtils.nullToStr(rs.getString("remark")));
					item.setBranchName			(EnjoyUtils.nullToStr(rs.getString("branchName")));
					item.setCusGroupCode		(EnjoyUtils.nullToStr(rs.getString("cusGroupCode")));
					item.setGroupSalePrice		(EnjoyUtils.nullToStr(rs.getString("groupSalePrice")));
					String fullName	= EnjoyUtils.nullToStr(rs.getString("cusName")) + " " + EnjoyUtils.nullToStr(rs.getString("cusSurname"));
					item.setFullName			(fullName);
	            	
	                return item;
	            }
	        });
		    
		    if(result != null && !result.isEmpty()){
		    	customerDetailsBean = result.get(0);
		    }
			
			
		}catch(Exception e){
			logger.error("[getCustomerDetail] :: ",e);
			throw e;
		}finally{
	        logger.info("[getCustomerDetail][End]");
		}
		
		return customerDetailsBean;
	}

	@Override
	public ArrayList<ComboBean> getStatusCombo() throws Exception {
		logger.info("[getStatusCombo][Begin]");
		
		StringBuilder       	sql         = new StringBuilder();
		ArrayList<ComboBean>	resultList 	= new ArrayList<ComboBean>();
		ArrayList<Object>		params		= new ArrayList<Object>();
		
		try{
			
			sql.append("select * from refcustomerstatus");
			
			logger.info("[getCompanystatusCombo] sql :: " + sql);
			
			resultList = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ComboBean item = new ComboBean();
	            	
	            	item.setCode(EnjoyUtils.nullToStr(rs.getString("customerStatusCode")));
	            	item.setDesc(EnjoyUtils.nullToStr(rs.getString("customerStatusName")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getStatusCombo][End]");
		}
		
		return resultList;
	}

	@Override
	public int checkDupCusName(String cusName, String cusSurname, String branchName, String cusCode, String tin) throws Exception {
		logger.info("[checkDupCusName][Begin]");
		
		StringBuilder 		sql 	= new StringBuilder();
		int 				result	= 0;
		ArrayList<Object>	params	= new ArrayList<Object>();
		
		try{
			logger.info("[checkDupCusName] tin :: " + tin);
			
			sql.append("select count(*) cou from customer where cusName = ? and cusSurname = ? and tin = ?");
			
			params.add(cusName);
			params.add(cusSurname);
			params.add(tin);
			
			if(EnjoyUtils.chkNull(branchName)){
				sql.append(" and branchName = ?");
				params.add(branchName);
			}
			
			if(EnjoyUtils.chkNull(cusCode)){
				sql.append(" and cusCode <> ?");
				params.add(cusCode);
			}
			
			logger.info("[checkDupCusName] sql :: " + sql.toString());
			
			result = jdbcTemplate.queryForObject(sql.toString() ,params.toArray(), Integer.class);
			
			logger.info("[checkDupCusName] result 			:: " + result);
			
			
			
		}catch(Exception e){
			logger.error("[checkDupCusName] :: ",e);
			throw e;
		}finally{
			logger.info("[checkDupCusName][End]");
		}
		
		return result;
	}

	@Override
	public ArrayList<ComboBean> getCusFullName(String fullName,String tin) throws Exception {
		logger.info("[getCusFullName][Begin]");
		
		StringBuilder       	sql         = new StringBuilder();
		ArrayList<ComboBean>	resultList 	= new ArrayList<ComboBean>();
		ArrayList<Object>		params		= new ArrayList<Object>();
		
		try{
			
			sql.append("select * from customer where cusStatus = 'A' and tin = ?");
			
			params.add(tin);
			
			if(EnjoyUtils.chkNull(fullName)){
				sql.append(" and CONCAT(cusName, ' ', cusSurname) LIKE CONCAT('%', ?, '%')");
				params.add(fullName);
			}
			
			sql.append(" order by CONCAT(cusName, ' ', cusSurname) asc limit 10");
			
			logger.info("[getCusFullName] sql :: " + sql);
			
			resultList = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ComboBean item = new ComboBean();
	            	String fullName	= EnjoyUtils.nullToStr(rs.getString("cusName")) + " " + EnjoyUtils.nullToStr(rs.getString("cusSurname"));
	            	
	            	item.setCode(EnjoyUtils.nullToStr(rs.getString("cusCode")));
	            	item.setDesc(fullName);
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getCusFullName][End]");
		}
		
		return resultList;
	}
	
	@Override
	public void insertCustomerDetails(CustomerDetailsBean vo)throws Exception {
		logger.info("[insertCustomerDetails][Begin]");
        
        StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
        
        try{
        	setColumnStr("cusCode", vo.getCusCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("tin", vo.getTin(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("cusName", vo.getCusName(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("cusSurname", vo.getCusSurname(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("branchName", vo.getBranchName(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("cusGroupCode", vo.getCusGroupCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("sex", vo.getSex(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("idType", vo.getIdType(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("idNumber", vo.getIdNumber(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("birthDate", EnjoyUtils.dateToThaiDB(vo.getBirthDate()), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("religion", vo.getReligion(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("job", vo.getJob(), TYPE_STRING, columns, args, paramList, colTypeList);
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
        	setColumnStr("cusStatus", vo.getCusStatus(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("startDate", EnjoyUtils.dateToThaiDB(vo.getStartDate()), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("expDate", EnjoyUtils.dateToThaiDB(vo.getExpDate()), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("point", vo.getPoint(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("remark", vo.getRemark(), TYPE_STRING, columns, args, paramList, colTypeList);
        	

            sql.append("INSERT INTO  customer ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertCustomerDetails] sql :: " + sql.toString());
            logger.info("[insertCustomerDetails] paramList :: " + paramList.size());
            logger.info("[insertCustomerDetails] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertCustomerDetails] row :: " + row);
            
        }catch(Exception e){
            logger.error("[insertCustomerDetails] :: ",e);
            throw e;
        }finally{
            logger.info("[insertCustomerDetails][End]");
        }
	}

	@Override
	public void updateCustomerDetail(final CustomerDetailsBean vo)throws Exception {
		logger.info("[updateCustomerDetail][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update customer");
			sql.append("\n	set cusName 			= ?");
			sql.append("\n		, cusSurname 		= ?");
			sql.append("\n		, branchName 		= ?");
			sql.append("\n		, cusGroupCode 		= ?");
			sql.append("\n		, sex 				= ?");
			sql.append("\n		, idType 			= ?");
			sql.append("\n		, idNumber 			= ?");
			sql.append("\n		, birthDate 		= ?");
			sql.append("\n		, religion 			= ?");
			sql.append("\n		, job 				= ?");
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
			sql.append("\n		, cusStatus 		= ?");
			sql.append("\n		, startDate 		= ?");
			sql.append("\n		, expDate 			= ?");
			sql.append("\n		, point 			= ?");
			sql.append("\n		, remark 			= ?");
			sql.append("\n	where cusCode = ?");
			sql.append("\n		and tin = ?");
			
			int row = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	                int i = 1;
	                
	                ps.setString(i++, vo.getCusName());
	                ps.setString(i++, vo.getCusSurname());
	                ps.setString(i++, vo.getBranchName());
	                ps.setString(i++, vo.getCusGroupCode());
	                ps.setString(i++, vo.getSex());
	                ps.setString(i++, vo.getIdType());
	                ps.setString(i++, vo.getIdNumber());
	                ps.setString(i++, EnjoyUtils.dateToThaiDB(vo.getBirthDate()));
	                ps.setString(i++, vo.getReligion());
	                ps.setString(i++, vo.getJob());
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
	                ps.setString(i++, vo.getCusStatus());
	                ps.setString(i++, EnjoyUtils.dateToThaiDB(vo.getStartDate()));
	                ps.setString(i++, EnjoyUtils.dateToThaiDB(vo.getExpDate()));
	                ps.setString(i++, vo.getPoint());
	                ps.setString(i++, vo.getRemark());
	                ps.setString(i++, vo.getCusCode());
	                ps.setString(i++, vo.getTin());
	            }
	        });
			
			logger.info("[updateCustomerDetail] row :: " + row);
			
		}catch(Exception e){
			logger.error("[updateCustomerDetail] :: ",e);
			throw e;
		}finally{
			logger.info("[updateCustomerDetail][End]");
		}
	}
	
	@Override
	public String genCusCode(String codeDisplay,String tin) throws Exception {
		logger.info("[genCusCode][Begin]");
		
		StringBuilder 		sql 	= new StringBuilder();
		String 				cusCode = null;
		ArrayList<String> 	result 	= null;
		
		try{
		    
		    sql.append("SELECT (MAX(SUBSTRING_INDEX(cusCode, '-', -1)) + 1) AS newId");
		    sql.append("\n	FROM customer");
		    sql.append("\n	WHERE SUBSTRING_INDEX(cusCode, '-', 1) 	= ?");
		    sql.append("\n		and tin 		= ?");
		    
		    logger.info("[genCusCode] codeDisplay 	:: " + codeDisplay);
		    logger.info("[genCusCode] tin 			:: " + tin);
		    logger.info("[genCusCode] sql 			:: " + sql.toString());
			
		    result = (ArrayList<String>) jdbcTemplate.query(sql.toString(), new Object[]{codeDisplay,tin}, new RowMapper<String>() {
	            @Override
	            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return EnjoyUtils.nullToStr(rs.getString("newId"));
	            }
	        });
		    
		    if(result != null && !result.isEmpty() && EnjoyUtils.chkNull(result.get(0))){
		    	
		    	logger.info("[genCusCode] result 			:: " + result.get(0));
		    	logger.info("[genCusCode] PadingCusCode 	:: " + ConfigFile.getPadingCusCode());
		    	
		    	cusCode = codeDisplay.concat("-").concat(String.format(ConfigFile.getPadingCusCode(),EnjoyUtils.parseInt(result.get(0))));
		    }else{
		    	cusCode = codeDisplay.concat("-").concat(String.format(ConfigFile.getPadingCusCode(), 1));
		    }
			
			
		}catch(Exception e){
			logger.error("[genCusCode] :: ",e);
			throw e;
		}finally{
	        logger.info("[genCusCode][End]");
		}
		
		return cusCode;
	}
	
	@Override
	public int countCusGroupCodeInCustomer(String cusGroupCode, String tin) throws Exception{
		logger.info("[countCusGroupCodeInCustomer][Begin]");
		
		StringBuilder       sql     = new StringBuilder();
		int 				result	= 0;
		ArrayList<Object>	params	= new ArrayList<Object>();
		
		try{
			logger.info("[countCusGroupCodeInCustomer] cusGroupCode :: " + cusGroupCode);
			logger.info("[countCusGroupCodeInCustomer] tin 			:: " + tin);
			
			sql.append("select count(*) as cou from customer where cusGroupCode = ? and tin = ? and cusStatus = 'A'");
			
			params.add(cusGroupCode);
			params.add(tin);
			
			logger.info("[countCusGroupCodeInCustomer] sql 			:: " + sql.toString());
			
			result = jdbcTemplate.queryForObject(sql.toString() ,params.toArray(), Integer.class);
			
			logger.info("[countCusGroupCodeInCustomer] result 			:: " + result);
			
		}catch(Exception e){
			logger.error("countCusGroupCodeInCustomer :: ", e);
			throw e;
		}finally{
			logger.info("[countCusGroupCodeInCustomer][End]");
		}
		
		return result;
	}
	
}
