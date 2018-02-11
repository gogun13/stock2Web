package com.enjoy.stock.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.main.ConfigFile;
import com.enjoy.core.services.AbstractJdbcService;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.InvoiceCashDetailBean;
import com.enjoy.stock.bean.InvoiceCashMasterBean;

@Repository
public class InvoiceCashServiceImpl extends AbstractJdbcService implements InvoiceCashService{
	
	private static final Logger logger = Logger.getLogger(InvoiceCashServiceImpl.class);
	
	@Override
	public void searchByCriteria(PaginatedListBean paginatedList,InvoiceCashMasterBean 	invoiceCashMasterBean) throws Exception {
		logger.info("[searchByCriteria][Begin]");
		
		StringBuilder       				sql         			= new StringBuilder();
		ArrayList<InvoiceCashMasterBean>	resultList 				= new ArrayList<InvoiceCashMasterBean>();
		ArrayList<Object>					params					= new ArrayList<Object>();
		String 								invoiceDateForm			= null;
		String 								invoiceDateTo			= null;
		
		try{
			
			invoiceDateForm 	= EnjoyUtils.dateThaiToDb(invoiceCashMasterBean.getInvoiceDateForm());
			invoiceDateTo	= EnjoyUtils.dateThaiToDb(invoiceCashMasterBean.getInvoiceDateTo());
			
			sql.append("select a.*, CONCAT(b.cusName, ' ', b.cusSurname) as cusFullName");
			sql.append("\n	from invoicecashmaster a");
			sql.append("\n		LEFT JOIN customer b ON a.cusCode = b.cusCode and a.tin = b.tin");
			sql.append("\n	where a.tin	= ?");
			
			params.add(invoiceCashMasterBean.getTin());
			
			if(EnjoyUtils.chkNull(invoiceCashMasterBean.getInvoiceCode())){
				sql.append("\n and a.invoiceCode LIKE CONCAT(?, '%')");
				params.add(invoiceCashMasterBean.getInvoiceCode());
			}
			
			if(EnjoyUtils.chkNull(invoiceDateForm)){
				sql.append("\n and a.invoiceDate >= STR_TO_DATE(?, '%Y%m%d')");
				params.add(invoiceDateForm);
			}
			
			if(EnjoyUtils.chkNull(invoiceDateTo)){
				sql.append("\n and a.invoiceDate <= STR_TO_DATE(?, '%Y%m%d')");
				params.add(invoiceDateTo);
			}
			
			if(EnjoyUtils.chkNull(invoiceCashMasterBean.getCusFullName())){
				sql.append("\n and CONCAT(b.cusName, ' ', b.cusSurname) LIKE CONCAT('%', ?, '%')");
				params.add(invoiceCashMasterBean.getCusFullName());
			}
			
			if(EnjoyUtils.chkNull(invoiceCashMasterBean.getInvoiceStatus())){
				sql.append("\n and a.invoiceStatus = ?");
				params.add(invoiceCashMasterBean.getInvoiceStatus());
			}else{
				sql.append("\n and a.invoiceStatus not in ('W', 'I')");
			}
			
			if(EnjoyUtils.chkNull(invoiceCashMasterBean.getInvoiceType())){
				sql.append("\n and a.invoiceType = ?");
				params.add(invoiceCashMasterBean.getInvoiceType());
			}
			
			if(EnjoyUtils.chkNull(invoiceCashMasterBean.getInvoiceCredit())){
				sql.append("\n and a.invoiceCredit = ?");
				params.add(invoiceCashMasterBean.getInvoiceCredit());
			}
			
			sql.append("\n order by a.invoiceCode desc");
			
			logger.info("[searchByCriteria] sql :: " + sql);
			
			String rowNumSql = decorateRowNumSQL(sql.toString(), paginatedList.getIndex(), paginatedList.getPageSize());
			
			resultList = (ArrayList<InvoiceCashMasterBean>) jdbcTemplate.query(rowNumSql, params.toArray(), new RowMapper<InvoiceCashMasterBean>() {
	            @Override
	            public InvoiceCashMasterBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	InvoiceCashMasterBean item = new InvoiceCashMasterBean();
	            	
	            	String invoiceTypeDesc 		= EnjoyUtils.nullToStr(rs.getString("invoiceType")).equals("V")?"มี VAT":"ไม่มี VAT";
	            	String invoiceStatus		= EnjoyUtils.nullToStr(rs.getString("invoiceStatus"));
	            	String invoiceStatusDesc	= "";
					
					if(invoiceStatus.equals("A")){
						invoiceStatusDesc = "ใช้งานอยู่";
					}else if(invoiceStatus.equals("C")){
						invoiceStatusDesc = "ยกเลิกการใช้งาน";
					}else if(invoiceStatus.equals("W")){
						invoiceStatusDesc = "รอสร้างใบ Invoice";
					}
					
					item.setInvoiceCode			(EnjoyUtils.nullToStr(rs.getString("invoiceCode")));
					item.setInvoiceType			(EnjoyUtils.nullToStr(rs.getString("invoiceType")));
					item.setInvoiceTypeDesc		(invoiceTypeDesc);
					item.setCusFullName			(EnjoyUtils.nullToStr(rs.getString("cusFullName")));
					item.setInvoiceDate			(EnjoyUtils.dateToThaiDisplay(rs.getString("invoiceDate")));
					item.setInvoiceTotal		(EnjoyUtils.convertFloatToDisplay(rs.getString("invoiceTotal"), 2));
					item.setInvoiceStatus		(invoiceStatus);
					item.setInvoiceStatusDesc	(invoiceStatusDesc);
					item.setTin					(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setInvoiceCredit		(EnjoyUtils.nullToStr(rs.getString("invoiceCredit")));
	            	
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
	public InvoiceCashMasterBean getInvoiceCashMaster(String invoiceCode,String tin) throws Exception {
		logger.info("[getInvoiceCashMaster][Begin]");
		
		StringBuilder 						sql 					= new StringBuilder();
		InvoiceCashMasterBean 				invoiceCashMasterBean 	= null;
		ArrayList<InvoiceCashMasterBean> 	result 					= null;
		
		try{
		    
		    sql.append("select t.*, CONCAT(a.userName,' ',a.userSurname) saleName");
		    sql.append("\n	from invoicecashmaster t");
		    sql.append("\n		left join userdetails a on t.saleUniqueId = a.userUniqueId");
		    sql.append("\n	where t.invoiceCode = ?");
		    sql.append("\n		and t.tin		= ?");
		    
		    logger.info("[getInvoiceCashMaster] reciveNo 	:: " + invoiceCode);
		    logger.info("[getInvoiceCashMaster] tin 		:: " + tin);
		    logger.info("[getInvoiceCashMaster] sql 		:: " + sql.toString());
			
		    result = (ArrayList<InvoiceCashMasterBean>) jdbcTemplate.query(sql.toString(), new Object[]{invoiceCode,tin}, new RowMapper<InvoiceCashMasterBean>() {
	            @Override
	            public InvoiceCashMasterBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	InvoiceCashMasterBean item = new InvoiceCashMasterBean();
	            	
	            	item.setInvoiceCode				(EnjoyUtils.nullToStr(rs.getString("invoiceCode")));
					item.setInvoiceDate				(EnjoyUtils.dateToThaiDisplay(rs.getString("invoiceDate")));
					item.setInvoiceType				(EnjoyUtils.nullToStr(rs.getString("invoiceType")));
					item.setCusCode					(EnjoyUtils.nullToStr(rs.getString("cusCode")));
					item.setBranchName				(EnjoyUtils.nullToStr(rs.getString("branchName")));
					item.setSaleUniqueId			(EnjoyUtils.nullToStr(rs.getString("saleUniqueId")));
					item.setSaleCommission			(EnjoyUtils.convertFloatToDisplay(rs.getString("saleCommission"), 2));
					item.setInvoicePrice			(EnjoyUtils.convertFloatToDisplay(rs.getString("invoicePrice"), 2));
					item.setInvoicediscount			(EnjoyUtils.convertFloatToDisplay(rs.getString("invoicediscount"), 2));
					item.setInvoiceDeposit			(EnjoyUtils.convertFloatToDisplay(rs.getString("invoiceDeposit"), 2));
					item.setInvoiceVat				(EnjoyUtils.convertFloatToDisplay(rs.getString("invoiceVat"), 2));
					item.setInvoiceTotal			(EnjoyUtils.convertFloatToDisplay(rs.getString("invoiceTotal"), 2));
					item.setUserUniqueId			(EnjoyUtils.nullToStr(rs.getString("userUniqueId")));
					item.setInvoiceCredit			(EnjoyUtils.nullToStr(rs.getString("invoiceCredit")));
					item.setInvoiceStatus			(EnjoyUtils.nullToStr(rs.getString("invoiceStatus")));
					item.setTin						(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setRemark					(EnjoyUtils.nullToStr(rs.getString("remark")));
					item.setSaleName				(EnjoyUtils.nullToStr(rs.getString("saleName")));
	            	
	                return item;
	            }
	        });
		    
		    if(result != null && !result.isEmpty()){
		    	invoiceCashMasterBean = result.get(0);
		    }
			
			
		}catch(Exception e){
			logger.error("[getInvoiceCashMaster] :: ",e);
			throw e;
		}finally{
	        logger.info("[getInvoiceCashMaster][End]");
		}
		
		return invoiceCashMasterBean;
	}
	
	@Override
	public void insertInvoiceCashMaster(InvoiceCashMasterBean vo)throws Exception {
		logger.info("[insertInvoiceCashMaster][Begin]");
        
        StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
        
        try{
        	setColumnStr("invoiceCode", vo.getInvoiceCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("tin", vo.getTin(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("invoiceDate", EnjoyUtils.dateToThaiDB(vo.getInvoiceDate()), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("invoiceType", vo.getInvoiceType(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("cusCode", vo.getCusCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("branchName", vo.getBranchName(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("saleUniqueId", vo.getSaleUniqueId(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("saleCommission", vo.getSaleCommission(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("invoicePrice", vo.getInvoicePrice(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("invoicediscount", vo.getInvoicediscount(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("invoiceDeposit", vo.getInvoiceDeposit(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("invoiceVat", vo.getInvoiceVat(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("invoiceTotal", vo.getInvoiceTotal(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("userUniqueId", vo.getUserUniqueId(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("invoiceCredit", vo.getInvoiceCredit(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("invoiceStatus", vo.getInvoiceStatus(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("remark", vo.getRemark(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("created_id", vo.getUserUniqueId(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("created_dt", "", TYPE_SYS_DATE, columns, args, paramList, colTypeList);

            sql.append("INSERT INTO  invoicecashmaster ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertInvoiceCashMaster] sql :: " + sql.toString());
            logger.info("[insertInvoiceCashMaster] paramList :: " + paramList.size());
            logger.info("[insertInvoiceCashMaster] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertInvoiceCashMaster] row :: " + row);
            
        }catch(Exception e){
            logger.error("[insertInvoiceCashMaster] :: ",e);
            throw e;
        }finally{
            logger.info("[insertInvoiceCashMaster][End]");
        }
	}
	
	@Override
	public void updateInvoiceCashMaster(final InvoiceCashMasterBean vo)throws Exception {
		logger.info("[updateInvoiceCashMaster][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update invoicecashmaster");
			sql.append("\n	set invoiceDate 		= ?");
			sql.append("\n		, invoiceType 		= ?");
			sql.append("\n		, cusCode 			= ?");
			sql.append("\n		, branchName 		= ?");
			sql.append("\n		, saleUniqueId 		= ?");
			sql.append("\n		, saleCommission 	= ?");
			sql.append("\n		, invoicePrice 		= ?");
			sql.append("\n		, invoicediscount 	= ?");
			sql.append("\n		, invoiceDeposit 	= ?");
			sql.append("\n		, invoiceVat 		= ?");
			sql.append("\n		, invoiceTotal 		= ?");
			sql.append("\n		, remark 			= ?");
			sql.append("\n		, updated_id 		= ?");
			sql.append("\n		, updated_dt 		= sysdate()");
			sql.append("\n	where invoiceCode 	= ?");
			sql.append("\n		and tin 		= ?");
			
			int row = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	                int i = 1;
	                
	                ps.setString(i++, EnjoyUtils.dateToThaiDB(vo.getInvoiceDate()));
	                
	                ps.setString(i++, vo.getInvoiceType());
	                ps.setString(i++, vo.getCusCode());
	                ps.setString(i++, vo.getBranchName());
	                ps.setString(i++, vo.getSaleUniqueId());
	                ps.setString(i++, vo.getSaleCommission());
	                ps.setString(i++, vo.getInvoicePrice());
	                ps.setString(i++, vo.getInvoicediscount());
	                ps.setString(i++, vo.getInvoiceDeposit());
	                ps.setString(i++, vo.getInvoiceVat());
	                ps.setString(i++, vo.getInvoiceTotal());
	                ps.setString(i++, vo.getRemark());
	                ps.setString(i++, vo.getUserUniqueId());
	                ps.setString(i++, vo.getInvoiceCode());
	                ps.setString(i++, vo.getTin());
	            }
	        });
			
			logger.info("[updateInvoiceCashMaster] row :: " + row);
			
		}catch(Exception e){
			logger.error("[updateInvoiceCashMaster] :: ",e);
			throw e;
		}finally{
			logger.info("[updateInvoiceCashMaster][End]");
		}
	}
	
	@Override
	public void updateInvoiceCashMasterStatus(final InvoiceCashMasterBean vo)throws Exception {
		logger.info("[updateInvoiceCashMasterStatus][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update invoicecashmaster");
			sql.append("\n	set invoiceStatus 	= ?");
			sql.append("\n		, updated_id 	= ?");
			sql.append("\n		, updated_dt 	= sysdate()");
			sql.append("\n	where invoiceCode = ?");
			sql.append("\n		and tin 	= ?");
			
			int row = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	                int i = 1;
	                
	                ps.setString(i++, vo.getInvoiceStatus());
	                ps.setString(i++, vo.getUserUniqueId());
	                ps.setString(i++, vo.getInvoiceCode());
	                ps.setString(i++, vo.getTin());
	            }
	        });
			
			logger.info("[updateInvoiceCashMasterStatus] row :: " + row);
			
		}catch(Exception e){
			logger.error("[updateInvoiceCashMasterStatus] :: ",e);
			throw e;
		}finally{
			logger.info("[updateInvoiceCashMasterStatus][End]");
		}
	}
	
	@Override
	public void cancelInvoiceCashMasterByInvoiceCredit(final String invoiceCredit,final String tin)throws Exception {
		logger.info("[cancelInvoiceCashMasterByInvoiceCredit][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update invoicecashmaster");
			sql.append("\n	set invoiceStatus = 'I'");
			sql.append("\n	where invoiceCredit = ?");
			sql.append("\n		and tin 	= ?");
			
			int row = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	                int i = 1;
	                
	                ps.setString(i++, invoiceCredit);
	                ps.setString(i++, tin);
	            }
	        });
			
			logger.info("[cancelInvoiceCashMasterByInvoiceCredit] row :: " + row);
			
		}catch(Exception e){
			logger.error("[cancelInvoiceCashMasterByInvoiceCredit] :: ",e);
			throw e;
		}finally{
			logger.info("[cancelInvoiceCashMasterByInvoiceCredit][End]");
		}
	}
	
	@Override
	public ArrayList<InvoiceCashDetailBean> getInvoiceCashDetailList(String invoiceCode, String tin) throws Exception {
		logger.info("[getInvoiceCashDetailList][Begin]");
		
		StringBuilder       				sql         = new StringBuilder();
		ArrayList<InvoiceCashDetailBean>	resultList 	= new ArrayList<InvoiceCashDetailBean>();
		
		try{
			logger.info("[getInvoiceCashDetailList] invoiceCode :: " + invoiceCode);
			logger.info("[getInvoiceCashDetailList] tin 		:: " + tin);
			
			sql.append("select a.*, b.productName, e.quantity as inventory, b.unitCode, c.unitName");
			sql.append("\n	from invoicecashdetail a");
			sql.append("\n		INNER JOIN productmaster b on b.productCode = a.productCode and a.tin = b.tin");
			sql.append("\n		INNER JOIN unittype c on c.unitCode = b.unitCode and a.tin = c.tin");
			sql.append("\n		LEFT  JOIN invoicecashmaster d ON a.invoiceCode = d.invoiceCode and a.tin = d.tin");
			sql.append("\n		LEFT JOIN	productquantity e ON d.tin = e.tin and e.productCode = a.productCode");
			sql.append("\n	where a.invoiceCode 	= ?");
			sql.append("\n		and a.tin		= ?");
			sql.append("\n	order by a.seq asc");
			
			logger.info("[getInvoiceCashDetailList] sql :: " + sql);
			
			resultList = (ArrayList<InvoiceCashDetailBean>) jdbcTemplate.query(sql.toString(), new Object[]{invoiceCode,tin}, new RowMapper<InvoiceCashDetailBean>() {
	            @Override
	            public InvoiceCashDetailBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	InvoiceCashDetailBean item = new InvoiceCashDetailBean();
	            	
	            	item.setInvoiceCode			(EnjoyUtils.nullToStr(rs.getString("invoiceCode")));
					item.setSeqDb				(EnjoyUtils.nullToStr(rs.getString("seq")));
					item.setProductCode			(EnjoyUtils.nullToStr(rs.getString("productCode")));
					item.setProductName			(EnjoyUtils.nullToStr(rs.getString("productName")));
					item.setInventory			(EnjoyUtils.convertNumberToDisplay(rs.getString("inventory")));
					item.setQuantity			(EnjoyUtils.convertNumberToDisplay(rs.getString("quantity")));
					item.setPricePerUnit		(EnjoyUtils.convertFloatToDisplay(rs.getString("pricePerUnit"), 2));
					item.setDiscount			(EnjoyUtils.convertNumberToDisplay(rs.getString("discount")));
					item.setPrice				(EnjoyUtils.convertFloatToDisplay(rs.getString("price"), 2));
					item.setUnitCode			(EnjoyUtils.nullToStr(rs.getString("unitCode")));
					item.setUnitName			(EnjoyUtils.nullToStr(rs.getString("unitName")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getInvoiceCashDetailList][End]");
		}
		
		return resultList;
	}
	
	@Override
	public void insertInvoiceCashDetail(InvoiceCashDetailBean vo)throws Exception {
		logger.info("[insertInvoiceCashDetail][Begin]");
        
        StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
        
        try{
        	setColumnStr("invoiceCode", vo.getInvoiceCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("tin", vo.getTin(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("seq", vo.getSeqDb(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("productCode", vo.getProductCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("quantity", vo.getQuantity(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("pricePerUnit", vo.getPricePerUnit(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("discount", vo.getDiscount(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("price", vo.getPrice(), TYPE_STRING, columns, args, paramList, colTypeList);

            sql.append("INSERT INTO  invoicecashdetail ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertInvoiceCashDetail] sql :: " + sql.toString());
            logger.info("[insertInvoiceCashDetail] paramList :: " + paramList.size());
            logger.info("[insertInvoiceCashDetail] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertInvoiceCashDetail] row :: " + row);
            
        }catch(Exception e){
            logger.error("[insertInvoiceCashDetail] :: ",e);
            throw e;
        }finally{
            logger.info("[insertInvoiceCashDetail][End]");
        }
	}
	
	@Override
	public void deleteInvoiceCashDetail(String invoiceCode, String tin) throws Exception{
		logger.info("[deleteInvoiceCashDetail][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("delete from invoicecashdetail where invoiceCode = ? and tin = ?");
			
			logger.info("[deleteInvoiceCashDetail] invoiceCode 	:: " + invoiceCode);
			logger.info("[deleteInvoiceCashDetail] tin 			:: " + tin);
			logger.info("[deleteInvoiceCashDetail] sql 			:: " + sql.toString());
			
			int row = jdbcTemplate.update(sql.toString(), new Object[]{invoiceCode,tin});
			
			logger.info("[deleteInvoiceCashDetail] row :: " + row);
		}catch(Exception e){
			logger.error("deleteInvoiceCashDetail :: ", e);
			throw e;
		}finally{
			logger.info("[deleteInvoiceCashDetail][End]");
		}
	}
	
	@Override
	public String genId(String codeDisplay,String tin) throws Exception {
		logger.info("[genId][Begin]");
		
		StringBuilder 		sql 	= new StringBuilder();
		String 				newId 	= null;
		ArrayList<String> 	result 	= null;
		
		try{
		    
		    sql.append("SELECT (MAX(SUBSTRING_INDEX(invoiceCode, '-', -1)) + 1) AS newId");
		    sql.append("\n	FROM invoicecashmaster");
		    sql.append("\n	WHERE SUBSTRING_INDEX(invoiceCode, '-', 1) 	= ?");
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
		    	newId = codeDisplay.concat("-").concat(String.format(ConfigFile.getPadingInvoiceCode(),EnjoyUtils.parseInt(result.get(0))));
		    }else{
		    	newId = codeDisplay.concat("-").concat(String.format(ConfigFile.getPadingInvoiceCode(), 1));
		    }
			
			
		}catch(Exception e){
			logger.error("[genId] :: ",e);
			throw e;
		}finally{
	        logger.info("[genId][End]");
		}
		
		return newId;
	}
}
