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
import com.enjoy.core.bean.PagesDetailBean;
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.bean.UserDetailsBean;
import com.enjoy.core.bean.UserPrivilegeBean;
import com.enjoy.core.main.Constants;
import com.enjoy.core.utils.EnjoyUtils;

@Repository
public class UserdetailsServiceImpl extends AbstractJdbcService implements UserdetailsService{
	
	private static final Logger logger = Logger.getLogger(UserdetailsServiceImpl.class);
	
	@Override
	public UserDetailsBean userSelect(String userEmail, String pass) throws Exception{
		logger.info("[userSelect][Begin]");
		
		StringBuilder 				sql 			= new StringBuilder();
		UserDetailsBean 			userDetailsBean = null;
		ArrayList<UserDetailsBean> 	result 			= null;
		
		try{
		    
		    sql.append("select * from userdetails where userEmail = ? and userPassword = ?");
			
		    result = (ArrayList<UserDetailsBean>) jdbcTemplate.query(sql.toString(), new Object[]{userEmail, pass}, new RowMapper<UserDetailsBean>() {
	            @Override
	            public UserDetailsBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	UserDetailsBean item = new UserDetailsBean();
	            	
	            	item.setUserUniqueId		(EnjoyUtils.parseInt(rs.getString("userUniqueId")));
	            	item.setUserEmail			(EnjoyUtils.nullToStr(rs.getString("userEmail")));
	            	item.setPwd					(EnjoyUtils.nullToStr(rs.getString("userPassword")));
	            	item.setUserName			(EnjoyUtils.nullToStr(rs.getString("userName")));
	            	item.setUserSurname			(EnjoyUtils.nullToStr(rs.getString("userSurname")));
					
	            	item.setUserFullName		(item.getUserName().concat(" ").concat(item.getUserSurname()));
					
	            	item.setUserPrivilege		(EnjoyUtils.nullToStr(rs.getString("userPrivilege")));
					item.setUserLevel			(EnjoyUtils.nullToStr(rs.getString("userLevel")));
					item.setUserStatus			(EnjoyUtils.nullToStr(rs.getString("userStatus")));
					item.setFlagChangePassword	(EnjoyUtils.chkBoxtoDb(rs.getString("flagChangePassword")));
					item.setCurrentDate			(EnjoyUtils.dateToThaiDisplay(EnjoyUtils.currDateThai()));
					item.setFlagAlertStock		(EnjoyUtils.chkBoxtoDb(rs.getString("flagAlertStock")));
					item.setFlagSalesman		(EnjoyUtils.chkBoxtoDb(rs.getString("flagSalesman")));
					item.setCommission			(EnjoyUtils.convertNumberToDisplay(rs.getString("commission")));
					item.setRemark			    (EnjoyUtils.nullToStr(rs.getString("remark")));
	            	
	                return item;
	            }
	        });
		    
		    if(result != null && !result.isEmpty()){
		    	userDetailsBean = result.get(0);
		    }
			
			
		}catch(Exception e){
			logger.error("[userSelect] :: ", e);
			throw e;
		}finally{
	        logger.info("[userSelect][End]");
		}
		
		return userDetailsBean;
	}
	
	@Override
	public ArrayList<UserPrivilegeBean> userPrivilegeListSelect(String userPrivilege) throws Exception{
		logger.info("[userPrivilegeListSelect][Begin]");
		
		StringBuilder 					sql 				= new StringBuilder();
		ArrayList<UserPrivilegeBean> 	result 				= new ArrayList<UserPrivilegeBean>();
		String[]						privilegeCode		= null;
		ArrayList<Object>				params				= new ArrayList<Object>();
		StringBuilder 					criteria 			= new StringBuilder("");
		
		try{
			privilegeCode	= userPrivilege.split("\\,");
			
			for(int i=0;i<privilegeCode.length;i++){
				if(criteria.toString().equals("")){
					criteria.append("?");
				}else{
					criteria.append(",?");
				}
				params.add(privilegeCode[i]);
			}
			
			sql.append("select * from userprivilege where privilegeCode in (").append(criteria.toString()).append(")");
			
			logger.info("[userPrivilegeListSelect] sql 		:: " + sql.toString());
			logger.info("[userPrivilegeListSelect] criteria :: " + criteria.toString());
			
			result = (ArrayList<UserPrivilegeBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<UserPrivilegeBean>() {
	            @Override
	            public UserPrivilegeBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	UserPrivilegeBean item = new UserPrivilegeBean();
	            	
	            	item.setPrivilegeCode(EnjoyUtils.nullToStr(rs.getString("privilegeCode")));
	            	item.setPrivilegeName(EnjoyUtils.nullToStr(rs.getString("privilegeName")));
	            	item.setPagesCode(EnjoyUtils.nullToStr(rs.getString("pagesCode")));
	            	
	                return item;
	            }
	        });	
			
		}catch(Exception e){
			logger.error("[userPrivilegeListSelect] :: ", e);
			throw e;
		}finally{
			logger.info("[userPrivilegeListSelect][End]");
		}
		
		return result;
	}
	
	@Override
	public ArrayList<PagesDetailBean> pagesDetailListSelect(String pagesCode) throws Exception{
		logger.info("[pagesDetailListSelect][Begin]");
		
		StringBuilder 				sql 				= new StringBuilder();
		ArrayList<PagesDetailBean> 	result 				= new ArrayList<PagesDetailBean>();
		String[]					pagesCodeArr		= null;
		ArrayList<Object>			params				= new ArrayList<Object>();
		StringBuilder 				criteria 			= new StringBuilder("");
		
		try{
			pagesCodeArr	= pagesCode.split("\\,");
			
			for(int i=0;i<pagesCodeArr.length;i++){
				if(criteria.toString().equals("")){
					criteria.append("?");
				}else{
					criteria.append(",?");
				}
				params.add(pagesCodeArr[i]);
			}
			sql.append("select * from refpagedetails where pageCodes in (").append(criteria.toString()).append(")");
			
			result = (ArrayList<PagesDetailBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<PagesDetailBean>() {
	            @Override
	            public PagesDetailBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	PagesDetailBean item = new PagesDetailBean();
	            	
	            	item.setPageCodes(EnjoyUtils.nullToStr(rs.getString("pageCodes")));
	            	item.setPageNames(EnjoyUtils.nullToStr(rs.getString("pageNames")));
	            	item.setPathPages(EnjoyUtils.nullToStr(rs.getString("pathPages")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			logger.error("[pagesDetailListSelect] :: ",e);
			throw e;
		}finally{
			logger.info("[pagesDetailListSelect][End]");
		}
		
		return result;
	}
	
	@Override
	public int updateUserPassword(UserDetailsBean userDetailsBean) throws Exception{
		logger.info("[updateUserPassword][Begin]");
		
		StringBuilder 	sql 			= new StringBuilder();
		int row = 0;
		
		try{
			sql.append("update userdetails set userPassword = ? ,flagChangePassword = 'N' where userUniqueId = ?");
			
			row = jdbcTemplate.update(sql.toString(), new Object[]{userDetailsBean.getPwd(), userDetailsBean.getUserUniqueId()});
            if (logger.isInfoEnabled()) {
                logger.info("[updateUserPassword] row :: " + row);
            }
		}catch(Exception e){
			logger.error("[updateUserPassword] :: ",e);
			throw e;
		}finally{
			logger.info("[updateUserPassword][End]");
		}
		
		return row;
	}
	
	@Override
	public ArrayList<ComboBean> getRefuserstatusCombo() throws Exception{
		logger.info("[getRefuserstatusCombo][Begin]");
		
		StringBuilder 			sql 	= new StringBuilder();
		ArrayList<ComboBean> 	result 	= new ArrayList<ComboBean>();
		
		try{
			sql.append("select * from refuserstatus");
			
			logger.info("[getRefuserstatusCombo] sql :: " + sql.toString());
			
			result = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	String userStatusCode = EnjoyUtils.nullToStr(rs.getString("userStatusCode"));
	            	String userStatusName = EnjoyUtils.nullToStr(rs.getString("userStatusName"));
	            	
	            	ComboBean item = new ComboBean(userStatusCode ,userStatusName);
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			logger.error("[getRefuserstatusCombo] :: ", e);
			throw e;
		}finally{
			logger.info("[getRefuserstatusCombo][End]");
		}
		
		return result;
		
	}
	
	@Override
	public ArrayList<UserPrivilegeBean> getUserprivilege() throws Exception{
		logger.info("[getUserprivilege][Begin]");
		
		StringBuilder 					sql 	= new StringBuilder();
		ArrayList<UserPrivilegeBean> 	result 	= new ArrayList<UserPrivilegeBean>();
		
		try{
			sql.append("select * from userprivilege where flagDispaly = 'Y'");
			
			logger.info("[getUserprivilege] sql :: " + sql.toString());
			
			result = (ArrayList<UserPrivilegeBean>) jdbcTemplate.query(sql.toString(), new RowMapper<UserPrivilegeBean>() {
	            @Override
	            public UserPrivilegeBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	UserPrivilegeBean item = new UserPrivilegeBean();
	            	
	            	item.setPrivilegeCode(EnjoyUtils.nullToStr(rs.getString("privilegeCode")));
	            	item.setPrivilegeName(EnjoyUtils.nullToStr(rs.getString("privilegeName")));
	            	item.setPagesCode(EnjoyUtils.nullToStr(rs.getString("pagesCode")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			logger.error("[getUserprivilege] :: "	,e);
			throw e;
		}finally{
			logger.info("[getUserprivilege][End]");
		}
		
		return result;
		
	}
	
	@Override
	public UserDetailsBean getUserdetail(int userUniqueId) throws Exception{
		logger.info("[getUserdetail][Begin]");
		
		StringBuilder 				sql 			= new StringBuilder();
		UserDetailsBean 			userDetailsBean = null;
		ArrayList<UserDetailsBean> 	result 			= null;
		
		try{
		    
		    sql.append("select a.*,b.userStatusName");
		    sql.append("	from userdetails a");
		    sql.append("		INNER JOIN refuserstatus b on b.userStatusCode = a.userStatus");
		    sql.append("	where a.userUniqueId = ?");
		    
		    logger.info("[getUserdetail] sql :: " + sql.toString());
			
		    result = (ArrayList<UserDetailsBean>) jdbcTemplate.query(sql.toString(), new Object[]{userUniqueId}, new RowMapper<UserDetailsBean>() {
	            @Override
	            public UserDetailsBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	UserDetailsBean item = new UserDetailsBean();
	            	
	            	item.setUserUniqueId		(EnjoyUtils.parseInt(rs.getString("userUniqueId")));
	            	item.setUserEmail			(EnjoyUtils.nullToStr(rs.getString("userEmail")));
	            	item.setPwd					(EnjoyUtils.nullToStr(rs.getString("userPassword")));
	            	item.setUserName			(EnjoyUtils.nullToStr(rs.getString("userName")));
					item.setUserSurname			(EnjoyUtils.nullToStr(rs.getString("userSurname")));
					item.setUserPrivilege		(EnjoyUtils.nullToStr(rs.getString("userPrivilege")));
					item.setUserLevel			(EnjoyUtils.nullToStr(rs.getString("userLevel")));
					item.setUserStatus			(EnjoyUtils.nullToStr(rs.getString("userStatus")));
					item.setFlagChangePassword	(EnjoyUtils.nullToStr(rs.getString("flagChangePassword")));
					item.setFlagAlertStock		(EnjoyUtils.nullToStr(rs.getString("flagAlertStock")));
					item.setFlagSalesman		(EnjoyUtils.chkBoxtoDb(rs.getString("flagSalesman")));
					item.setCommission			(EnjoyUtils.convertNumberToDisplay(rs.getString("commission")));
					item.setRemark			    (EnjoyUtils.nullToStr(rs.getString("remark")));
					item.setUserStatusName		(EnjoyUtils.nullToStr(rs.getString("userStatusName")));
					
					item.setUserFullName		(item.getUserName().concat(" ").concat(item.getUserSurname()));
	            	
	                return item;
	            }
	        });
		    
		    if(result != null && !result.isEmpty()){
		    	userDetailsBean = result.get(0);
		    }
			
			
		}catch(Exception e){
			logger.error("[getUserdetail] :: ",e);
			throw e;
		}finally{
	        logger.info("[getUserdetail][End]");
		}
		
		return userDetailsBean;
	}
	
	@Override
	public int checkDupUserEmail(String userEmail, String pageMode, int userUniqueId) throws Exception{
		logger.info("[checkDupUserId][Begin]");
		
		StringBuilder 		sql 	= new StringBuilder();
		int 				result	= 0;
		ArrayList<Object>	params	= new ArrayList<Object>();
		
		try{
			logger.info("[checkDupUserId] pageMode :: " + pageMode);
			
			sql.append("select count(userEmail) cou from userdetails where userEmail = ?");
			
			params.add(userEmail);
			
			if(pageMode.equals(Constants.UPDATE)){
				sql.append(" and userUniqueId <> ?");
				params.add(userUniqueId);
			}
			
			logger.info("[checkDupUserId] sql :: " + sql.toString());
			
			result = jdbcTemplate.queryForObject(sql.toString() ,params.toArray(), Integer.class);
			
			logger.info("[checkDupUserId] result 			:: " + result);
			
			
			
		}catch(Exception e){
			logger.error("[checkDupUserId] :: ",e);
			throw e;
		}finally{
			logger.info("[checkDupUserId][End]");
		}
		
		return result;
	}
	
	@Override
    public void saveUserDetails(UserDetailsBean vo) throws Exception {
        logger.info("[saveUserDetails][Begin]");
        
        StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
        
        try{
            setColumnStr("userEmail", vo.getUserEmail(), TYPE_STRING, columns, args, paramList, colTypeList);
            setColumnStr("userPassword", vo.getPwd(), TYPE_STRING, columns, args, paramList, colTypeList);
            setColumnStr("userName", vo.getUserName(), TYPE_STRING, columns, args, paramList, colTypeList);
            setColumnStr("userSurname", vo.getUserSurname(), TYPE_STRING, columns, args, paramList, colTypeList);
            setColumnStr("userPrivilege", vo.getUserPrivilege(), TYPE_STRING, columns, args, paramList, colTypeList);
            setColumnStr("userLevel", vo.getUserLevel(), TYPE_STRING, columns, args, paramList, colTypeList);
            setColumnStr("userStatus", vo.getUserStatus(), TYPE_STRING, columns, args, paramList, colTypeList);
            setColumnStr("flagChangePassword", vo.getFlagChangePassword(), TYPE_STRING, columns, args, paramList, colTypeList);
            setColumnStr("flagAlertStock", vo.getFlagAlertStock(), TYPE_STRING, columns, args, paramList, colTypeList);
            setColumnStr("flagSalesman", vo.getFlagSalesman(), TYPE_STRING, columns, args, paramList, colTypeList);
            setColumnStr("commission", vo.getCommission(), TYPE_STRING, columns, args, paramList, colTypeList);
            setColumnStr("remark", vo.getRemark(), TYPE_STRING, columns, args, paramList, colTypeList);
            
            sql.append("INSERT INTO  userdetails ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[saveUserDetails] sql :: " + sql.toString());
            logger.info("[saveUserDetails] paramList :: " + paramList.size());
            logger.info("[saveUserDetails] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[saveUserDetails] row :: " + row);
            
        }catch(Exception e){
            logger.error("[saveUserDetails] :: ",e);
            throw e;
        }finally{
            logger.info("[saveUserDetails][End]");
        }
    }
	
	@Override
	public void updateUserDetail(final UserDetailsBean vo) throws Exception{
		logger.info("[updateUserDetail][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update userdetails");
			sql.append("\n	set userEmail 				= ?");
			sql.append("\n		,userName 				= ?");
			sql.append("\n		, userSurname			= ?");
			sql.append("\n		, userPrivilege			= ?");
			sql.append("\n		, userLevel				= ?");
			sql.append("\n		, userStatus			= ?");
			sql.append("\n		, flagChangePassword 	= ?");
			sql.append("\n		, flagAlertStock 		= ?");
			sql.append("\n		, flagSalesman 			= ?");
			sql.append("\n		, commission 			= ?");
			sql.append("\n		, remark 				= ?");
			sql.append("\n	where userUniqueId = ?");
			
			int row = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	                int i = 1;
	                
	                ps.setString(i++, vo.getUserEmail());
	                ps.setString(i++, vo.getUserName());
		   			ps.setString(i++, vo.getUserSurname());
		   			ps.setString(i++, vo.getUserPrivilege());
		   			ps.setString(i++, vo.getUserLevel());
		   			ps.setString(i++, vo.getUserStatus());
		   			ps.setString(i++, vo.getFlagChangePassword());
		   			ps.setString(i++, vo.getFlagAlertStock());
		   			ps.setString(i++, vo.getFlagSalesman());
		   			ps.setDouble(i++, EnjoyUtils.parseDouble(vo.getCommission()));
		   			ps.setString(i++, vo.getRemark());
		   			ps.setInt(i++, vo.getUserUniqueId());
	            }

				
	        });
			
			logger.info("[updateUserDetail] row :: " + row);
			
		}catch(Exception e){
			logger.error("[updateUserDetail] :: ",e);
			throw e;
		}finally{
			logger.info("[updateUserDetail][End]");
		}
	}
	
	@Override
	public int lastId() throws Exception{
		logger.info("[lastId][Begin]");
		
		StringBuilder 		sql 	= new StringBuilder();
		int 				result	= 0;
		
		try{
			
			sql.append("select max(userUniqueId) lastId from userdetails");
			
			logger.info("[lastId] sql :: " + sql.toString());
			
			result = jdbcTemplate.queryForObject(sql.toString(), Integer.class);
			
			logger.info("[lastId] result 			:: " + result);
			
		}catch(Exception e){
			logger.error("[lastId] :: ",e);
			throw e;
		}finally{
			logger.info("[lastId][End]");
		}
		
		return result;
	}
	
	@Override
	public void changePassword(final String newPassword, final int userUniqueId) throws Exception{
		logger.info("[changePassword][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update userdetails set userPassword = ?");
			sql.append("\n	where userUniqueId = ?");
			
			logger.info("[changePassword] sql :: " + sql.toString());
			
			int row = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	                int i = 1;
	                
	                ps.setString(i++, newPassword);
	                ps.setInt(i++, userUniqueId);
	            }

				
	        });
			
			logger.info("[changePassword] row :: " + row);
			
		}catch(Exception e){
			logger.error("[changePassword] :: " ,e);
            throw e;
		}finally{
			logger.info("[changePassword][End]");
		}
	}
	
	@Override
	public ArrayList<ComboBean> userFullNameList(String userFullName, String tin, int userUniqueId)throws Exception{
		logger.info("[userFullNameList][Begin]");
		
		StringBuilder       	sql         = new StringBuilder();
		ArrayList<ComboBean>	resultList 	= new ArrayList<ComboBean>();
		ArrayList<Object>		params		= new ArrayList<Object>();
		
		try{
			logger.info("[userFullNameList] userFullName :: " + userFullName);
			logger.info("[userFullNameList] tin 		 :: " + tin);
			logger.info("[userFullNameList] userUniqueId :: " + userUniqueId);
			
			if(userUniqueId==1){
				sql.append("select t.userUniqueId, CONCAT(t.userName, ' ', t.userSurname) userFullName");
				sql.append("\n	from userdetails t");
				sql.append("\n	where t.userStatus 		= 'A'");
				sql.append("\n		and t.userUniqueId 	<> 1");
				
			}else{
				sql.append("select t.userUniqueId, CONCAT(t.userName, ' ', t.userSurname) userFullName");
				sql.append("\n	from userdetails t");
				sql.append("\n		inner join relationuserncompany a on a.userUniqueId = t.userUniqueId and a.tin = ?");
				sql.append("\n	where t.userStatus 	= 'A'");
				sql.append("\n		and t.userUniqueId 	<> 1");
				
				params.add(tin);
			}
			
			if(EnjoyUtils.chkNull(userFullName)){
				sql.append("\n and CONCAT(t.userName, ' ', t.userSurname) LIKE CONCAT(?, '%')");
				params.add(userFullName);
			}
			
			sql.append("\n order by CONCAT(t.userName, ' ', t.userSurname) asc limit 10");
			
			logger.info("[userFullNameList] sql :: " + sql);
			
			resultList = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ComboBean item = new ComboBean();
	            	
	            	item.setCode(EnjoyUtils.nullToStr(rs.getString("userUniqueId")));
	            	item.setDesc(EnjoyUtils.nullToStr(rs.getString("userFullName")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[userFullNameList][End]");
		}
		
		return resultList;
	}

	@Override
	public void getListUserdetail(PaginatedListBean paginatedList,UserDetailsBean userDetailsBean) throws Exception{
		logger.info("[getListUserdetail][Begin]");
		
		StringBuilder       		sql         = new StringBuilder();
		ArrayList<UserDetailsBean>	resultList 	= new ArrayList<UserDetailsBean>();
		ArrayList<Object>			params		= new ArrayList<Object>();
		
		try{	
			
			if(userDetailsBean.getUserUniqueId()==1){
				sql.append("select a.*");
				sql.append("\n	, b.userStatusName");
				sql.append("\n	from userdetails a");
				sql.append("\n		inner join refuserstatus b on b.userStatusCode = a.userStatus");
				sql.append("\n	where a.userUniqueId <> 1");
			}else{
				sql.append("select a.*");
				sql.append("\n	, b.userStatusName");
				sql.append("\n	from userdetails a");
				sql.append("\n		inner join refuserstatus b on b.userStatusCode = a.userStatus");
				sql.append("\n		inner join relationuserncompany c on c.userUniqueId = a.userUniqueId");
				sql.append("\n	where a.userUniqueId <> 1");
				sql.append("\n		and c.tin = ?");
				
				params.add(userDetailsBean.getTin());
			}
			
			if(EnjoyUtils.chkNull(userDetailsBean.getUserName())){
				sql.append("\n and CONCAT(a.userName, ' ', a.userSurname) LIKE CONCAT('%', ?, '%')");
				params.add(userDetailsBean.getUserName());
			}
			
			if(EnjoyUtils.chkNull(userDetailsBean.getUserEmail())){
				sql.append("\n and a.userEmail LIKE CONCAT('%',?, '%')");
				params.add(userDetailsBean.getUserEmail());
			}
			
			if(EnjoyUtils.chkNull(userDetailsBean.getUserStatus())){
				sql.append("\n and a.userStatus = ?");
				params.add(userDetailsBean.getUserStatus());
			}
			
			logger.info("[getListUserdetail] sql :: " + sql);
			
			String rowNumSql = decorateRowNumSQL(sql.toString(), paginatedList.getIndex(), paginatedList.getPageSize());
			
			resultList = (ArrayList<UserDetailsBean>) jdbcTemplate.query(rowNumSql, params.toArray(), new RowMapper<UserDetailsBean>() {
	            @Override
	            public UserDetailsBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	UserDetailsBean item = new UserDetailsBean();
	            	
	            	item.setUserUniqueId		(rs.getInt("userUniqueId"));
	            	item.setUserEmail			(EnjoyUtils.nullToStr(rs.getString("userEmail")));
	            	item.setUserName			(EnjoyUtils.nullToStr(rs.getString("userName")) + "  " + EnjoyUtils.nullToStr(rs.getString("userSurname")));
	            	item.setUserPrivilege		(EnjoyUtils.nullToStr(rs.getString("userPrivilege")));
	            	item.setUserLevel			(EnjoyUtils.nullToStr(rs.getString("userLevel")));
	            	item.setUserStatus			(EnjoyUtils.nullToStr(rs.getString("userStatus")));
	            	item.setFlagChangePassword	(EnjoyUtils.chkBoxtoDb(rs.getString("flagChangePassword")));
	            	item.setFlagAlertStock		(EnjoyUtils.chkBoxtoDb(rs.getString("flagAlertStock")));
	            	item.setFlagSalesman		(EnjoyUtils.chkBoxtoDb(rs.getString("flagSalesman")));
	            	item.setCommission			(EnjoyUtils.convertNumberToDisplay(rs.getString("commission")));
	            	item.setRemark			    (EnjoyUtils.nullToStr(rs.getString("remark")));
	            	item.setUserStatusName		(EnjoyUtils.nullToStr(rs.getString("userStatusName")));
	            	
	                return item;
	            }
	        });
			
			paginatedList.setFullListSize(countTotal(sql.toString(),params.toArray()));
			paginatedList.setList(resultList);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getListUserdetail][End]");
		}
		
	}	
	
	@Override
	public void getListUserdetailForLookUp(PaginatedListBean paginatedList,UserDetailsBean userDetailsBean) throws Exception{
		logger.info("[getListUserdetailForLookUp][Begin]");
		
		StringBuilder       		sql         = new StringBuilder();
		ArrayList<UserDetailsBean>	resultList 	= new ArrayList<UserDetailsBean>();
		ArrayList<Object>			params		= new ArrayList<Object>();
		
		try{	
			
			if(userDetailsBean.getUserUniqueId()==1){
				sql.append("select a.*");
				sql.append("\n	, b.userStatusName");
				sql.append("\n	from userdetails a");
				sql.append("\n		inner join refuserstatus b on b.userStatusCode = a.userStatus");
				sql.append("\n	where a.userUniqueId <> 1");
				sql.append("\n		and a.userStatus = 'A'");
			}else{
				sql.append("select a.*");
				sql.append("\n	, b.userStatusName");
				sql.append("\n	from userdetails a");
				sql.append("\n		inner join refuserstatus b on b.userStatusCode = a.userStatus");
				sql.append("\n		inner join relationuserncompany c on c.userUniqueId = a.userUniqueId and c.tin = ?");
				sql.append("\n	where a.userUniqueId <> 1");
				sql.append("\n		and a.userStatus = 'A'");
				
				params.add(userDetailsBean.getTin());
			}
			
			if(EnjoyUtils.chkNull(userDetailsBean.getUserName())){
				sql.append("\n and CONCAT(a.userName, ' ', a.userSurname) LIKE CONCAT('%', ?, '%')");
				params.add(userDetailsBean.getUserName());
			}
			
			if(EnjoyUtils.chkNull(userDetailsBean.getUserEmail())){
				sql.append("\n and a.userEmail LIKE CONCAT('%', ?, '%')");
				params.add(userDetailsBean.getUserEmail());
			}
			
			logger.info("[getListUserdetailForLookUp] sql :: " + sql);
			
			String rowNumSql = decorateRowNumSQL(sql.toString(), paginatedList.getIndex(), paginatedList.getPageSize());
			
			resultList = (ArrayList<UserDetailsBean>) jdbcTemplate.query(rowNumSql, params.toArray(), new RowMapper<UserDetailsBean>() {
	            @Override
	            public UserDetailsBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	UserDetailsBean item = new UserDetailsBean();
	            	
	            	item.setUserUniqueId		(rs.getInt("userUniqueId"));
	            	item.setUserEmail			(EnjoyUtils.nullToStr(rs.getString("userEmail")));
	            	item.setUserName			(EnjoyUtils.nullToStr(rs.getString("userName")) + "  " + EnjoyUtils.nullToStr(rs.getString("userSurname")));
	            	item.setUserPrivilege		(EnjoyUtils.nullToStr(rs.getString("userPrivilege")));
	            	item.setUserLevel			(EnjoyUtils.nullToStr(rs.getString("userLevel")));
	            	item.setUserStatus			(EnjoyUtils.nullToStr(rs.getString("userStatus")));
	            	item.setFlagChangePassword	(EnjoyUtils.chkBoxtoDb(rs.getString("flagChangePassword")));
	            	item.setFlagAlertStock		(EnjoyUtils.chkBoxtoDb(rs.getString("flagAlertStock")));
	            	item.setFlagSalesman		(EnjoyUtils.chkBoxtoDb(rs.getString("flagSalesman")));
	            	item.setCommission			(EnjoyUtils.convertNumberToDisplay(rs.getString("commission")));
	            	item.setRemark			    (EnjoyUtils.nullToStr(rs.getString("remark")));
	            	item.setUserStatusName		(EnjoyUtils.nullToStr(rs.getString("userStatusName")));
	            	
	                return item;
	            }
	        });
			
			paginatedList.setFullListSize(countTotal(sql.toString(),params.toArray()));
			paginatedList.setList(resultList);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getListUserdetailForLookUp][End]");
		}
		
	}	
	
	@Override
	public void updateUserStatusByUserEmail(final String userEmail, final String userStatus) throws Exception{
		logger.info("[updateUserStatusByUserEmail][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update userdetails");
			sql.append("\n	set userStatus 	= ?");
			sql.append("\n	where userEmail = ?");
			
			int row = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	                int i = 1;
	                
	                ps.setString(i++, userStatus);
	                ps.setString(i++, userEmail);
	            }
	        });
			
			logger.info("[updateUserDetail] row :: " + row);
			
		}catch(Exception e){
			logger.error("[updateUserDetail] :: ",e);
			throw e;
		}finally{
			logger.info("[updateUserDetail][End]");
		}
	}
	
	@Override
	public UserDetailsBean getUserdetailByTin(String fullName ,String tin) throws Exception{
		logger.info("[getUserdetailByTin][Begin]");
		
		StringBuilder 				sql 			= new StringBuilder();
		UserDetailsBean 			userDetailsBean = null;
		ArrayList<UserDetailsBean> 	result 			= null;
		
		try{			
			sql.append("select a.*, b.userStatusName");
			sql.append("\n	from userdetails a");
			sql.append("\n		inner join refuserstatus b on b.userStatusCode = a.userStatus");
			sql.append("\n		inner join relationuserncompany c on c.userUniqueId = a.userUniqueId");
			sql.append("\n	where a.userUniqueId <> 1");
			sql.append("\n		and a.userStatus = 'A'");
			sql.append("\n		and c.tin = ?");
			sql.append("\n		and CONCAT(a.userName, ' ', a.userSurname) = ?");
			
			result = (ArrayList<UserDetailsBean>) jdbcTemplate.query(sql.toString(), new Object[]{tin,fullName}, new RowMapper<UserDetailsBean>() {
	            @Override
	            public UserDetailsBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	UserDetailsBean item = new UserDetailsBean();
	            	
	            	item.setUserUniqueId		(EnjoyUtils.parseInt(rs.getString("userUniqueId")));
	            	item.setUserEmail			(EnjoyUtils.nullToStr(rs.getString("userEmail")));
	            	item.setUserName			(EnjoyUtils.nullToStr(rs.getString("userName")));
					item.setUserSurname			(EnjoyUtils.nullToStr(rs.getString("userSurname")));
					item.setUserPrivilege		(EnjoyUtils.nullToStr(rs.getString("userPrivilege")));
					item.setUserLevel			(EnjoyUtils.nullToStr(rs.getString("userLevel")));
					item.setUserStatus			(EnjoyUtils.nullToStr(rs.getString("userStatus")));
					item.setFlagChangePassword	(EnjoyUtils.nullToStr(rs.getString("flagChangePassword")));
					item.setFlagAlertStock		(EnjoyUtils.nullToStr(rs.getString("flagAlertStock")));
					item.setFlagSalesman		(EnjoyUtils.chkBoxtoDb(rs.getString("flagSalesman")));
					item.setCommission			(EnjoyUtils.convertNumberToDisplay(rs.getString("commission")));
					item.setRemark			    (EnjoyUtils.nullToStr(rs.getString("remark")));
					item.setUserStatusName		(EnjoyUtils.nullToStr(rs.getString("userStatusName")));
					
					item.setUserFullName(item.getUserName().concat(" ").concat(item.getUserSurname()));
	            	
	                return item;
	            }
	        });
		    
		    if(result != null && result.size()==1){
		    	userDetailsBean = result.get(0);
		    }
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getUserdetailByTin][End]");
		}
		
		return userDetailsBean;
		
	}
}
