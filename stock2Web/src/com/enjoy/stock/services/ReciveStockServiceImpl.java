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
import com.enjoy.stock.bean.ReciveOrdeDetailBean;
import com.enjoy.stock.bean.ReciveOrderMasterBean;

@Repository
public class ReciveStockServiceImpl extends AbstractJdbcService implements ReciveStockService{
	
	private static final Logger logger = Logger.getLogger(ReciveStockServiceImpl.class);
	
	@Override
	public void searchByCriteria(PaginatedListBean paginatedList,ReciveOrderMasterBean reciveOrderMasterBean,String tin) throws Exception {
		logger.info("[searchByCriteria][Begin]");
		
		StringBuilder       				sql         	= new StringBuilder();
		ArrayList<ReciveOrderMasterBean>	resultList 		= new ArrayList<ReciveOrderMasterBean>();
		ArrayList<Object>					params			= new ArrayList<Object>();
		String 								reciveDateFrom	= null;
		String 								reciveDateTo	= null;
		
		try{
			
			reciveDateFrom 	= EnjoyUtils.dateThaiToDb(reciveOrderMasterBean.getReciveDateFrom());
			reciveDateTo	= EnjoyUtils.dateThaiToDb(reciveOrderMasterBean.getReciveDateTo());
			
			sql.append("select a.*, CONCAT(b.username, ' ', b.userSurname) as usrName, c.reciveStatusName, CONCAT(d.vendorName ,' (' ,d.branchName ,')') as vendorName");
			sql.append("\n	from reciveordermaster a");
			sql.append("\n		inner join userdetails b on b.userUniqueId = a.userUniqueId");
			sql.append("\n		inner join refreciveorderstatus c on c.reciveStatusCode = a.reciveStatus");
			sql.append("\n		inner join companyvendor d on d.vendorCode = a.vendorCode and d.tinCompany = a.tin");
			sql.append("\n	where a.tin			= ?");
			
			params.add(tin);
			
			if(EnjoyUtils.chkNull(reciveOrderMasterBean.getReciveNo())){
				sql.append("\n and a.reciveNo LIKE CONCAT(?, '%')");
				params.add(reciveOrderMasterBean.getReciveNo());
			}
			
			if(EnjoyUtils.chkNull(reciveDateFrom)){
				sql.append("\n and a.reciveDate >= STR_TO_DATE(?, '%Y%m%d')");
				params.add(reciveDateFrom);
			}
			
			if(EnjoyUtils.chkNull(reciveDateTo)){
				sql.append("\n and a.reciveDate <= STR_TO_DATE(?, '%Y%m%d')");
				params.add(reciveDateTo);
			}
			
			if(EnjoyUtils.chkNull(reciveOrderMasterBean.getReciveStatus())){
				sql.append("\n and a.reciveStatus = ?");
				params.add(reciveOrderMasterBean.getReciveStatus());
			}
			
			if(EnjoyUtils.chkNull(reciveOrderMasterBean.getVendorName())){
				sql.append("\n and d.vendorName LIKE CONCAT(?, '%')");
				params.add(reciveOrderMasterBean.getVendorName());
			}
			
			if(EnjoyUtils.chkNull(reciveOrderMasterBean.getBranchName())){
				sql.append("\n and a.branchName LIKE CONCAT(?, '%')");
				params.add(reciveOrderMasterBean.getBranchName());
			}
			
			sql.append("\n order by a.reciveNo asc");
			
			logger.info("[searchByCriteria] sql :: " + sql);
			
			String rowNumSql = decorateRowNumSQL(sql.toString(), paginatedList.getIndex(), paginatedList.getPageSize());
			
			resultList = (ArrayList<ReciveOrderMasterBean>) jdbcTemplate.query(rowNumSql, params.toArray(), new RowMapper<ReciveOrderMasterBean>() {
	            @Override
	            public ReciveOrderMasterBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ReciveOrderMasterBean item = new ReciveOrderMasterBean();
	            	
	            	item.setReciveNo			(EnjoyUtils.nullToStr(rs.getString("reciveNo")));
					item.setReciveDate			(EnjoyUtils.dateToThaiDisplay(rs.getString("reciveDate")));
					item.setUsrName				(EnjoyUtils.nullToStr(rs.getString("usrName")));
					item.setReciveStatus		(EnjoyUtils.nullToStr(rs.getString("reciveStatus")));
					item.setReciveStatusDesc	(EnjoyUtils.nullToStr(rs.getString("reciveStatusName")));
					item.setTin					(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setVendorName			(EnjoyUtils.nullToStr(rs.getString("vendorName")));
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
	public ReciveOrderMasterBean getReciveOrderMaster(String reciveNo,String tin) throws Exception {
		logger.info("[getReciveOrderMaster][Begin]");
		
		StringBuilder 						sql 					= new StringBuilder();
		ReciveOrderMasterBean 				reciveOrderMasterBean 	= null;
		ArrayList<ReciveOrderMasterBean> 	result 					= null;
		
		try{
		    
		    sql.append("select * from reciveordermaster where reciveNo 	= ? and tin = ?");
		    
		    logger.info("[getReciveOrderMaster] reciveNo 	:: " + reciveNo);
		    logger.info("[getReciveOrderMaster] tin 		:: " + tin);
		    logger.info("[getReciveOrderMaster] sql 		:: " + sql.toString());
			
		    result = (ArrayList<ReciveOrderMasterBean>) jdbcTemplate.query(sql.toString(), new Object[]{reciveNo,tin}, new RowMapper<ReciveOrderMasterBean>() {
	            @Override
	            public ReciveOrderMasterBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ReciveOrderMasterBean item = new ReciveOrderMasterBean();
	            	
	            	item.setReciveNo				(EnjoyUtils.nullToStr(rs.getString("reciveNo")));
					item.setReciveDate				(EnjoyUtils.dateToThaiDisplay(rs.getString("reciveDate")));
					item.setReciveType				(EnjoyUtils.nullToStr(rs.getString("reciveType")));
					item.setCreditDay				(EnjoyUtils.nullToStr(rs.getString("creditDay")));
					item.setCreditExpire			(EnjoyUtils.dateToThaiDisplay(rs.getString("creditExpire")));
					item.setVendorCode				(EnjoyUtils.nullToStr(rs.getString("vendorCode")));
					item.setBranchName				(EnjoyUtils.nullToStr(rs.getString("branchName")));
					item.setBillNo					(EnjoyUtils.nullToStr(rs.getString("billNo")));
					item.setPriceType				(EnjoyUtils.nullToStr(rs.getString("priceType")));
					item.setReciveStatus			(EnjoyUtils.nullToStr(rs.getString("reciveStatus")));
					item.setUserUniqueId			(EnjoyUtils.nullToStr(rs.getString("userUniqueId")));
					item.setReciveAmount			(EnjoyUtils.convertFloatToDisplay(rs.getString("reciveAmount"), 2));
					item.setReciveDiscount			(EnjoyUtils.convertFloatToDisplay(rs.getString("reciveDiscount"), 2));
					item.setReciveVat				(rs.getString("reciveVat"));
					item.setReciveTotal				(EnjoyUtils.convertFloatToDisplay(rs.getString("reciveTotal"), 2));
					item.setTin						(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setRemark					(EnjoyUtils.nullToStr(rs.getString("remark")));
	            	
	                return item;
	            }
	        });
		    
		    if(result != null && !result.isEmpty()){
		    	reciveOrderMasterBean = result.get(0);
		    }
			
			
		}catch(Exception e){
			logger.error("[getReciveOrderMaster] :: ",e);
			throw e;
		}finally{
	        logger.info("[getReciveOrderMaster][End]");
		}
		
		return reciveOrderMasterBean;
	}
	
	@Override
	public void insertReciveordermaster(ReciveOrderMasterBean vo)throws Exception {
		logger.info("[insertReciveordermaster][Begin]");
        
        StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
        
        try{
        	setColumnStr("reciveNo", vo.getReciveNo(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("tin", vo.getTin(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("reciveDate", EnjoyUtils.dateToThaiDB(vo.getReciveDate()), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("reciveType", vo.getReciveType(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("creditDay", vo.getCreditDay(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("creditExpire", EnjoyUtils.dateToThaiDB(vo.getCreditExpire()), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("vendorCode", vo.getVendorCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("branchName", vo.getBranchName(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("billNo", vo.getBillNo(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("priceType", vo.getPriceType(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("reciveStatus", vo.getReciveStatus(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("userUniqueId", vo.getUserUniqueId(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("reciveAmount", vo.getReciveAmount(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("reciveDiscount", vo.getReciveDiscount(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("reciveVat", vo.getReciveVat(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("reciveTotal", vo.getReciveTotal(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("remark", vo.getRemark(), TYPE_STRING, columns, args, paramList, colTypeList);

            sql.append("INSERT INTO  reciveordermaster ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertReciveordermaster] sql :: " + sql.toString());
            logger.info("[insertReciveordermaster] paramList :: " + paramList.size());
            logger.info("[insertReciveordermaster] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertReciveordermaster] row :: " + row);
            
        }catch(Exception e){
            logger.error("[insertReciveordermaster] :: ",e);
            throw e;
        }finally{
            logger.info("[insertReciveordermaster][End]");
        }
	}
	
	@Override
	public void updateReciveOrderMaster(final ReciveOrderMasterBean vo)throws Exception {
		logger.info("[updateReciveOrderMaster][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update reciveordermaster");
			sql.append("\n	set reciveDate 			= ?");
			sql.append("\n		, reciveType 		= ?");
			sql.append("\n		, creditDay			= ?");
			sql.append("\n		, creditExpire		= ?");
			sql.append("\n		, vendorCode		= ?");
			sql.append("\n		, branchName		= ?");
			sql.append("\n		, billNo 			= ?");
			sql.append("\n		, priceType 		= ?");
			sql.append("\n		, reciveStatus 		= ?");
			sql.append("\n		, userUniqueId 		= ?");
			sql.append("\n		, reciveAmount 		= ?");
			sql.append("\n		, reciveDiscount 	= ?");
			sql.append("\n		, reciveVat 		= ?");
			sql.append("\n		, reciveTotal 		= ?");
			sql.append("\n		, remark 			= ?");
			sql.append("\n	where reciveNo 	= ?");
			sql.append("\n		and tin 	= ?");
			
			int row = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	                int i = 1;
	                
	                ps.setString(i++, EnjoyUtils.dateToThaiDB(vo.getReciveDate()));
	                ps.setString(i++, vo.getReciveType());
	                ps.setString(i++, vo.getCreditDay());
	                ps.setString(i++, EnjoyUtils.dateToThaiDB(vo.getCreditExpire()));
	                ps.setString(i++, vo.getVendorCode());
	                ps.setString(i++, vo.getBranchName());
	                ps.setString(i++, vo.getBillNo());
	                ps.setString(i++, vo.getPriceType());
	                ps.setString(i++, vo.getReciveStatus());
	                ps.setString(i++, vo.getUserUniqueId());
	                ps.setString(i++, vo.getReciveAmount());
	                ps.setString(i++, vo.getReciveDiscount());
	                ps.setString(i++, vo.getReciveVat());
	                ps.setString(i++, vo.getReciveTotal());
	                ps.setString(i++, vo.getRemark());
	                ps.setString(i++, vo.getReciveNo());
	                ps.setString(i++, vo.getTin());
	            }
	        });
			
			logger.info("[updateReciveOrderMaster] row :: " + row);
			
		}catch(Exception e){
			logger.error("[updateReciveOrderMaster] :: ",e);
			throw e;
		}finally{
			logger.info("[updateReciveOrderMaster][End]");
		}
	}
	
	@Override
	public void updateReciveOrderMasterSpecial(final ReciveOrderMasterBean vo)throws Exception {
		logger.info("[updateReciveOrderMasterSpecial][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update reciveordermaster");
			sql.append("\n	set reciveDate 			= ?");
			sql.append("\n		, reciveStatus 		= ?");
			sql.append("\n		, userUniqueId		= ?");
			sql.append("\n	where reciveNo 	= ?");
			sql.append("\n		and tin 	= ?");
			
			int row = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	                int i = 1;
	                
	                ps.setString(i++, EnjoyUtils.dateToThaiDB(vo.getReciveDate()));
	                ps.setString(i++, vo.getReciveStatus());
	                ps.setString(i++, vo.getUserUniqueId());
	                ps.setString(i++, vo.getReciveNo());
	                ps.setString(i++, vo.getTin());
	            }
	        });
			
			logger.info("[updateReciveOrderMasterSpecial] row :: " + row);
			
		}catch(Exception e){
			logger.error("[updateReciveOrderMasterSpecial] :: ",e);
			throw e;
		}finally{
			logger.info("[updateReciveOrderMasterSpecial][End]");
		}
	}
	
	@Override
	public ArrayList<ReciveOrdeDetailBean> getReciveOrdeDetailList(String reciveNo, String tin) throws Exception {
		logger.info("[getReciveOrdeDetailList][Begin]");
		
		StringBuilder       			sql         = new StringBuilder();
		ArrayList<ReciveOrdeDetailBean>	resultList 	= new ArrayList<ReciveOrdeDetailBean>();
		
		try{
			logger.info("[getReciveOrdeDetailList] reciveNo :: " + reciveNo);
			logger.info("[getReciveOrdeDetailList] tin 		:: " + tin);
			
			sql.append("select a.*,b.productName,e.quantity as inventory, b.unitCode, c.unitName");
			sql.append("\n	from reciveordedetail a");
			sql.append("\n		inner JOIN productmaster b on b.productCode = a.productCode and a.tin = b.tin");
			sql.append("\n		inner JOIN unittype c on  b.unitCode = c.unitCode and a.tin = c.tin");
			sql.append("\n		LEFT JOIN reciveordermaster d on a.reciveNo = d.reciveNo and a.tin = d.tin");
			sql.append("\n		LEFT JOIN productquantity e on a.productCode = e.productCode AND d.tin = e.tin");
			sql.append("\n	where a.reciveNo 	= ?");
			sql.append("\n		and a.tin		= ?");
			sql.append("\n	order by a.seq asc");
			
			logger.info("[getReciveOrdeDetailList] sql :: " + sql);
			
			resultList = (ArrayList<ReciveOrdeDetailBean>) jdbcTemplate.query(sql.toString(), new Object[]{reciveNo,tin}, new RowMapper<ReciveOrdeDetailBean>() {
	            @Override
	            public ReciveOrdeDetailBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ReciveOrdeDetailBean item = new ReciveOrdeDetailBean();
	            	
	            	item.setReciveNo			(EnjoyUtils.nullToStr(rs.getString("reciveNo")));
					item.setSeqDb				(EnjoyUtils.nullToStr(rs.getString("seq")));
					item.setProductCode			(EnjoyUtils.nullToStr(rs.getString("productCode")));
					item.setProductName			(EnjoyUtils.nullToStr(rs.getString("productName")));
					item.setInventory			(EnjoyUtils.nullToStr(rs.getString("inventory")));
					item.setQuantity			(EnjoyUtils.convertNumberToDisplay(rs.getString("quantity")));
					item.setUnitCode			(EnjoyUtils.nullToStr(rs.getString("unitCode")));
					item.setUnitName			(EnjoyUtils.nullToStr(rs.getString("unitName")));
					item.setPrice				(EnjoyUtils.convertFloatToDisplay(rs.getString("price"), 2));
					item.setDiscountRate		(EnjoyUtils.nullToStr(rs.getString("discountRate")));
					item.setCostPrice			(EnjoyUtils.convertFloatToDisplay(rs.getString("costPrice"), 2));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getReciveOrdeDetailList][End]");
		}
		
		return resultList;
	}
	
	@Override
	public void insertReciveOrdeDetail(ReciveOrdeDetailBean vo)throws Exception {
		logger.info("[insertReciveOrdeDetail][Begin]");
        
        StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
        
        try{
        	setColumnStr("reciveNo", vo.getReciveNo(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("tin", vo.getTin(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("seq", vo.getSeqDb(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("productCode", vo.getProductCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("quantity", vo.getQuantity(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("price", vo.getPrice(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("discountRate", vo.getDiscountRate(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("costPrice", vo.getCostPrice(), TYPE_STRING, columns, args, paramList, colTypeList);

            sql.append("INSERT INTO  reciveordedetail ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertReciveOrdeDetail] sql :: " + sql.toString());
            logger.info("[insertReciveOrdeDetail] paramList :: " + paramList.size());
            logger.info("[insertReciveOrdeDetail] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertReciveOrdeDetail] row :: " + row);
            
        }catch(Exception e){
            logger.error("[insertReciveOrdeDetail] :: ",e);
            throw e;
        }finally{
            logger.info("[insertReciveOrdeDetail][End]");
        }
	}
	
	@Override
	public void deleteReciveordedetail(String reciveNo, String tin) throws Exception{
		logger.info("[deleteReciveordedetail][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("delete from reciveordedetail where reciveNo = ? and tin = ?");
			
			logger.info("[deleteReciveordedetail] reciveNo 	:: " + reciveNo);
			logger.info("[deleteReciveordedetail] tin 		:: " + tin);
			logger.info("[deleteReciveordedetail] sql 		:: " + sql.toString());
			
			int row = jdbcTemplate.update(sql.toString(), new Object[]{reciveNo,tin});
			
			logger.info("[deleteReciveordedetail] row :: " + row);
		}catch(Exception e){
			logger.error("deleteReciveordedetail :: ", e);
			throw e;
		}finally{
			logger.info("[deleteReciveordedetail][End]");
		}
	}
	
	@Override
	public ArrayList<ComboBean> getRefReciveOrderStatusCombo() throws Exception {
		logger.info("[getRefReciveOrderStatusCombo][Begin]");
		
		StringBuilder       	sql         = new StringBuilder();
		ArrayList<ComboBean>	resultList 	= new ArrayList<ComboBean>();
		ArrayList<Object>		params		= new ArrayList<Object>();
		
		try{
			
			sql.append("select * from refreciveorderstatus order by reciveStatusCode asc");
			
			logger.info("[getRefReciveOrderStatusCombo] sql :: " + sql);
			
			resultList = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ComboBean item = new ComboBean();
	            	
	            	item.setCode(EnjoyUtils.nullToStr(rs.getString("reciveStatusCode")));
	            	item.setDesc(EnjoyUtils.nullToStr(rs.getString("reciveStatusName")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getRefReciveOrderStatusCombo][End]");
		}
		
		return resultList;
	}
	
	@Override
	public String genReciveNo(String codeDisplay,String tin) throws Exception {
		logger.info("[genReciveNo][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		String 				reciveNo 	= null;
		ArrayList<String> 	result 		= null;
		
		try{
		    
		    sql.append("SELECT (MAX(SUBSTRING_INDEX(reciveNo, '-', -1)) + 1) AS newId");
		    sql.append("\n	FROM reciveordermaster");
		    sql.append("\n	WHERE SUBSTRING_INDEX(reciveNo, '-', 1) 	= ?");
		    sql.append("\n		and tin 		= ?");
		    
		    logger.info("[genReciveNo] codeDisplay 	:: " + codeDisplay);
		    logger.info("[genReciveNo] tin 			:: " + tin);
		    logger.info("[genReciveNo] sql 			:: " + sql.toString());
			
		    result = (ArrayList<String>) jdbcTemplate.query(sql.toString(), new Object[]{codeDisplay,tin}, new RowMapper<String>() {
	            @Override
	            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return EnjoyUtils.nullToStr(rs.getString("newId"));
	            }
	        });
		    
		    if(result != null && !result.isEmpty() && EnjoyUtils.chkNull(result.get(0))){
		    	reciveNo = codeDisplay.concat("-").concat(String.format(ConfigFile.getPadingReciveNo(),EnjoyUtils.parseInt(result.get(0))));
		    }else{
		    	reciveNo = codeDisplay.concat("-").concat(String.format(ConfigFile.getPadingReciveNo(), 1));
		    }
			
			
		}catch(Exception e){
			logger.error("[genReciveNo] :: ",e);
			throw e;
		}finally{
	        logger.info("[genReciveNo][End]");
		}
		
		return reciveNo;
	}
}
