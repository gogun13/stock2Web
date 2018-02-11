package com.enjoy.core.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.utils.EnjoyUtils;

@Repository
public class AddressServiceImpl extends AbstractJdbcService implements AddressService{
	
	private static final Logger logger = Logger.getLogger(AddressServiceImpl.class);

	@Override
	public ArrayList<ComboBean>provinceList(String provinceName)throws Exception {
		logger.info("[provinceList][Begin]");
		
		StringBuilder       	sql         = new StringBuilder();
		ArrayList<ComboBean>	resultList 	= new ArrayList<ComboBean>();
		ArrayList<Object>		params		= new ArrayList<Object>();
		
		try{
			
			sql.append("select * from province where provinceId <> '00'");
			
			if(EnjoyUtils.chkNull(provinceName)){
				sql.append(" and provinceName LIKE CONCAT(?, '%')");
				params.add(provinceName);
			}
			
			sql.append(" order by provinceName asc limit 10");
			
			logger.info("[provinceList] sql :: " + sql);
			
			resultList = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ComboBean item = new ComboBean();
	            	
	            	item.setCode(EnjoyUtils.nullToStr(rs.getString("provinceId")));
	            	item.setDesc(EnjoyUtils.nullToStr(rs.getString("provinceName")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[provinceList][End]");
		}
		
		return resultList;
	}
	
	@Override
	public ArrayList<ComboBean>districtList(String provinceName, String districtName)throws Exception {
		logger.info("[districtList][Begin]");
		
		StringBuilder       	sql         = new StringBuilder();
		ArrayList<ComboBean>	resultList 	= new ArrayList<ComboBean>();
		
		try{
			
			sql.append("select a.*");
			sql.append("\n	from district a");
			sql.append("\n		inner join province b on b.provinceId = a.provinceId");
			sql.append("\n	where a.districtId <> '0000'");
			sql.append("\n		and a.provinceId <> '00'");
			sql.append("\n		and b.provinceName = ?");
			sql.append("\n		and a.districtName LIKE CONCAT(?, '%')");
			sql.append("\n	order by a.districtName asc limit 10");
			
			logger.info("[districtList] sql :: " + sql);
			
			resultList = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), new Object[]{provinceName,districtName}, new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ComboBean item = new ComboBean();
	            	
	            	item.setCode(EnjoyUtils.nullToStr(rs.getString("districtId")));
	            	item.setDesc(EnjoyUtils.nullToStr(rs.getString("districtName")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[districtList][End]");
		}
		
		return resultList;
	}
	
	@Override
	public ArrayList<ComboBean> subdistrictList(String provinceName, String districtName, String subdistrictName) throws Exception{
		logger.info("[districtList][Begin]");
		
		StringBuilder       	sql         = new StringBuilder();
		ArrayList<ComboBean>	resultList 	= new ArrayList<ComboBean>();
		
		try{
			
			sql.append("select a.* ");
			sql.append("\n	from subdistrict a");
			sql.append("\n		inner join province b on b.provinceId = a.provinceId");
			sql.append("\n		inner join district c on c.districtId = a.districtId");
			sql.append("\n	where a.subdistrictId <> '000000'");
			sql.append("\n		and a.provinceId <> '00'");
			sql.append("\n		and a.districtId <> '0000'");
			sql.append("\n		and SUBSTR(a.subdistrictId, 5, 2) <> '00'");
			sql.append("\n		and b.provinceName = ?");
			sql.append("\n		and c.districtName = ?");
			sql.append("\n		and a.subdistrictName LIKE CONCAT(?, '%')");
			sql.append("\n	order by a.subdistrictName asc limit 10");

			
			logger.info("[districtList] sql :: " + sql);
			
			resultList = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), new Object[]{provinceName,districtName,subdistrictName}, new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ComboBean item = new ComboBean();
	            	
	            	item.setCode(EnjoyUtils.nullToStr(rs.getString("subdistrictId")));
	            	item.setDesc(EnjoyUtils.nullToStr(rs.getString("subdistrictName")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[districtList][End]");
		}
		
		return resultList;
	}

	@Override
	public String getProvinceName(String provinceId) throws Exception {
		logger.info("[getProvinceName][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		String 				result		= "";
		ArrayList<String>	resultList 	= new ArrayList<String>();
		
		try{
			
			sql.append("select provinceName from province where provinceId = ?");
			
			logger.info("[getProvinceName] provinceId 	:: " + provinceId);
			logger.info("[getProvinceName] sql 			:: " + sql.toString());
			
			resultList = (ArrayList<String>) jdbcTemplate.query(sql.toString(), new Object[]{provinceId}, new RowMapper<String>() {
	            @Override
	            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return EnjoyUtils.nullToStr(rs.getString("provinceName"));
	            }
	        });
			
			if(resultList!=null && !resultList.isEmpty()){
				result = resultList.get(0);
			}
			
			logger.info("[getProvinceName] result 			:: " + result);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getProvinceName][End]");
		}
		
		return result;
	}

	@Override
	public String getDistrictName(String districtId) throws Exception {
		logger.info("[getDistrictName][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		String 				result		= "";
		ArrayList<String>	resultList 	= new ArrayList<String>();
		
		try{
			
			sql.append("select districtName from district where districtId = ?");
			
			logger.info("[getDistrictName] districtId 	:: " + districtId);
			logger.info("[getDistrictName] sql 			:: " + sql.toString());
			
			resultList = (ArrayList<String>) jdbcTemplate.query(sql.toString(), new Object[]{districtId}, new RowMapper<String>() {
	            @Override
	            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return EnjoyUtils.nullToStr(rs.getString("districtName"));
	            }
	        });
			
			if(resultList!=null && !resultList.isEmpty()){
				result = resultList.get(0);
			}
			
			logger.info("[getDistrictName] result 			:: " + result);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getDistrictName][End]");
		}
		
		return result;
	}

	@Override
	public String getSubdistrictName(String subdistrictId) throws Exception {
		logger.info("[getSubdistrictName][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		String 				result		= "";
		ArrayList<String>	resultList 	= new ArrayList<String>();
		
		try{
			
			sql.append("select subdistrictName from subdistrict where subdistrictId = ?");
			
			logger.info("[getSubdistrictName] subdistrictId :: " + subdistrictId);
			logger.info("[getSubdistrictName] sql 			:: " + sql.toString());
			
			resultList = (ArrayList<String>) jdbcTemplate.query(sql.toString(), new Object[]{subdistrictId}, new RowMapper<String>() {
	            @Override
	            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return EnjoyUtils.nullToStr(rs.getString("subdistrictName"));
	            }
	        });
			
			if(resultList!=null && !resultList.isEmpty()){
				result = resultList.get(0);
			}
			
			logger.info("[getSubdistrictName] result 			:: " + result);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getSubdistrictName][End]");
		}
		
		return result;
	}
	
	@Override
	public String getProvinceId(String provinceName) throws Exception {
		logger.info("[getProvinceId][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		String 				result		= "";
		ArrayList<String>	resultList 	= new ArrayList<String>();
		
		try{
			
			sql.append("select provinceId from province where provinceName = ? and provinceId <> '00'");
			
			logger.info("[getProvinceId] provinceName 	:: " + provinceName);
			logger.info("[getProvinceId] sql 			:: " + sql.toString());
			
			resultList = (ArrayList<String>) jdbcTemplate.query(sql.toString(), new Object[]{provinceName}, new RowMapper<String>() {
	            @Override
	            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return EnjoyUtils.nullToStr(rs.getString("provinceId"));
	            }
	        });
			
			if(resultList!=null && !resultList.isEmpty()){
				result = resultList.get(0);
			}
			
			logger.info("[getProvinceId] result 			:: " + result);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getProvinceId][End]");
		}
		
		return result;
	}

	@Override
	public String getDistrictId(String provinceName, String districtName)throws Exception {
		logger.info("[getDistrictId][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		String 				result		= "";
		ArrayList<String>	resultList 	= new ArrayList<String>();
		
		try{
			
			sql.append("select a.districtId");
			sql.append("\n	from district a");
			sql.append("\n		inner JOIN province b on b.provinceId = a.provinceId");
			sql.append("\n	where a.provinceId <> '00'");
			sql.append("\n		and a.districtId <> '0000'");
			sql.append("\n		and b.provinceName = ?");
			sql.append("\n		and a.districtName = ?");
			
			logger.info("[getDistrictId] provinceName 	:: " + provinceName);
			logger.info("[getDistrictId] districtName 	:: " + districtName);
			logger.info("[getDistrictId] sql 			:: " + sql.toString());
			
			resultList = (ArrayList<String>) jdbcTemplate.query(sql.toString(), new Object[]{provinceName,districtName}, new RowMapper<String>() {
	            @Override
	            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return EnjoyUtils.nullToStr(rs.getString("districtId"));
	            }
	        });
			
			if(resultList!=null && !resultList.isEmpty()){
				result = resultList.get(0);
			}
			
			logger.info("[getDistrictId] result 			:: " + result);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getDistrictId][End]");
		}
		
		return result;
	}

	@Override
	public String getSubdistrictId(String provinceName, String districtName,String subdistrictName) throws Exception {
		logger.info("[getSubdistrictId][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		String 				result		= "";
		ArrayList<String>	resultList 	= new ArrayList<String>();
		
		try{
			
			sql.append("select a.subdistrictId");
			sql.append("\n	from subdistrict a");
			sql.append("\n		inner JOIN province b on b.provinceId = a.provinceId");
			sql.append("\n		inner JOIN district c on c.districtId = a.districtId and c.provinceId = a.provinceId");
			sql.append("\n	where a.provinceId <> '00'");
			sql.append("\n		and a.districtId <> '0000'");
			sql.append("\n		and a.subdistrictId <> '000000'");
			sql.append("\n		and b.provinceName = ?");
			sql.append("\n		and c.districtName = ?");
			sql.append("\n		and a.subdistrictName = ?");
			
			logger.info("[getSubdistrictId] provinceName 	:: " + provinceName);
			logger.info("[getSubdistrictId] districtName 	:: " + districtName);
			logger.info("[getSubdistrictId] subdistrictName :: " + subdistrictName);
			logger.info("[getSubdistrictId] sql 			:: " + sql.toString());
			
			resultList = (ArrayList<String>) jdbcTemplate.query(sql.toString(), new Object[]{provinceName,districtName,subdistrictName}, new RowMapper<String>() {
	            @Override
	            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return EnjoyUtils.nullToStr(rs.getString("subdistrictId"));
	            }
	        });
			
			if(resultList!=null && !resultList.isEmpty()){
				result = resultList.get(0);
			}
			
			logger.info("[getSubdistrictId] result 			:: " + result);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getSubdistrictId][End]");
		}
		
		return result;
	}
	
}
