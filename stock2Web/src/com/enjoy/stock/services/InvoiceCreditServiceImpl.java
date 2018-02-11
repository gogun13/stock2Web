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
import com.enjoy.stock.bean.InvoiceCreditDetailBean;
import com.enjoy.stock.bean.InvoiceCreditMasterBean;

@Repository
public class InvoiceCreditServiceImpl extends AbstractJdbcService implements InvoiceCreditService{
	
	private static final Logger logger = Logger.getLogger(InvoiceCreditServiceImpl.class);
	
	@Override
	public void searchByCriteria(PaginatedListBean paginatedList,InvoiceCreditMasterBean invoiceCreditMasterBean) throws Exception {
		logger.info("[searchByCriteria][Begin]");
		
		StringBuilder       				sql         			= new StringBuilder();
		ArrayList<InvoiceCreditMasterBean>	resultList 				= new ArrayList<InvoiceCreditMasterBean>();
		ArrayList<Object>					params					= new ArrayList<Object>();
		String 								invoiceDateForm			= null;
		String 								invoiceDateTo			= null;
		
		try{
			
			invoiceDateForm = EnjoyUtils.dateThaiToDb(invoiceCreditMasterBean.getInvoiceDateForm());
			invoiceDateTo	= EnjoyUtils.dateThaiToDb(invoiceCreditMasterBean.getInvoiceDateTo());
			
			sql.append("select a.*, CONCAT(b.cusName, ' ', b.cusSurname) as cusFullName");
			sql.append("\n	from invoicecreditmaster a");
			sql.append("\n		LEFT JOIN customer b ON a.cusCode = b.cusCode and a.tin = b.tin");
			sql.append("\n	where a.tin	= ?");
			
			params.add(invoiceCreditMasterBean.getTin());
			
			if(EnjoyUtils.chkNull(invoiceCreditMasterBean.getInvoiceCode())){
				sql.append("\n and a.invoiceCode LIKE CONCAT(?, '%')");
				params.add(invoiceCreditMasterBean.getInvoiceCode());
			}
			
			if(EnjoyUtils.chkNull(invoiceDateForm)){
				sql.append("\n and a.invoiceDate >= STR_TO_DATE(?, '%Y%m%d')");
				params.add(invoiceDateForm);
			}
			
			if(EnjoyUtils.chkNull(invoiceDateTo)){
				sql.append("\n and a.invoiceDate <= STR_TO_DATE(?, '%Y%m%d')");
				params.add(invoiceDateTo);
			}
			
			if(EnjoyUtils.chkNull(invoiceCreditMasterBean.getCusFullName())){
				sql.append("\n and CONCAT(b.cusName, ' ', b.cusSurname) LIKE CONCAT('%', ?, '%')");
				params.add(invoiceCreditMasterBean.getCusFullName());
			}
			
			if(EnjoyUtils.chkNull(invoiceCreditMasterBean.getInvoiceStatus())){
				sql.append("\n and a.invoiceStatus = ?");
				params.add(invoiceCreditMasterBean.getInvoiceStatus());
			}
			
			if(EnjoyUtils.chkNull(invoiceCreditMasterBean.getInvoiceType())){
				sql.append("\n and a.invoiceType = ?");
				params.add(invoiceCreditMasterBean.getInvoiceType());
			}
			
			sql.append("\n order by a.invoiceCode desc");
			
			logger.info("[searchByCriteria] sql :: " + sql);
			
			String rowNumSql = decorateRowNumSQL(sql.toString(), paginatedList.getIndex(), paginatedList.getPageSize());
			
			resultList = (ArrayList<InvoiceCreditMasterBean>) jdbcTemplate.query(rowNumSql, params.toArray(), new RowMapper<InvoiceCreditMasterBean>() {
	            @Override
	            public InvoiceCreditMasterBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	InvoiceCreditMasterBean item = new InvoiceCreditMasterBean();
	            	
	            	String invoiceTypeDesc 		= EnjoyUtils.nullToStr(rs.getString("invoiceType")).equals("V")?"มี VAT":"ไม่มี VAT";
	            	String invoiceStatus		= EnjoyUtils.nullToStr(rs.getString("invoiceStatus"));
	            	String invoiceStatusDesc	= "";
					
	            	if(invoiceStatus.equals("A")){
						invoiceStatusDesc = "ใช้งานอยู่";
					}else if(invoiceStatus.equals("C")){
						invoiceStatusDesc = "ยกเลิกการใช้งาน";
					}else if(invoiceStatus.equals("W")){
						invoiceStatusDesc = "รอสร้างใบ Invoice";
					}else if(invoiceStatus.equals("S")){
						invoiceStatusDesc = "รับเงินเรียบร้อยแล้ว";
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
	public InvoiceCreditMasterBean getInvoiceCreditMaster(String invoiceCode,String tin) throws Exception {
		logger.info("[getInvoiceCreditMaster][Begin]");
		
		StringBuilder 						sql 					= new StringBuilder();
		InvoiceCreditMasterBean 			invoiceCreditMasterBean = null;
		ArrayList<InvoiceCreditMasterBean> 	result 					= null;
		
		try{
		    
		    sql.append("select t.*, CONCAT(a.userName,' ',a.userSurname) saleName");
		    sql.append("\n	from invoicecreditmaster t");
		    sql.append("\n		left join userdetails a on t.saleUniqueId = a.userUniqueId");
		    sql.append("\n	where t.invoiceCode = ?");
		    sql.append("\n		and t.tin		= ?");
		    
		    logger.info("[getInvoiceCreditMaster] reciveNo 	:: " + invoiceCode);
		    logger.info("[getInvoiceCreditMaster] tin 		:: " + tin);
		    logger.info("[getInvoiceCreditMaster] sql 		:: " + sql.toString());
			
		    result = (ArrayList<InvoiceCreditMasterBean>) jdbcTemplate.query(sql.toString(), new Object[]{invoiceCode,tin}, new RowMapper<InvoiceCreditMasterBean>() {
	            @Override
	            public InvoiceCreditMasterBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	InvoiceCreditMasterBean item = new InvoiceCreditMasterBean();
	            	
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
					item.setInvoiceCash				(EnjoyUtils.nullToStr(rs.getString("invoiceCash")));
					item.setInvoiceStatus			(EnjoyUtils.nullToStr(rs.getString("invoiceStatus")));
					item.setTin						(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setRemark					(EnjoyUtils.nullToStr(rs.getString("remark")));
					item.setSaleName				(EnjoyUtils.nullToStr(rs.getString("saleName")));
	            	
	                return item;
	            }
	        });
		    
		    if(result != null && !result.isEmpty()){
		    	invoiceCreditMasterBean = result.get(0);
		    }
			
			
		}catch(Exception e){
			logger.error("[getInvoiceCreditMaster] :: ",e);
			throw e;
		}finally{
	        logger.info("[getInvoiceCreditMaster][End]");
		}
		
		return invoiceCreditMasterBean;
	}
	
	@Override
	public void insertInvoiceCreditMaster(InvoiceCreditMasterBean vo)throws Exception {
		logger.info("[insertInvoiceCreditMaster][Begin]");
        
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
        	setColumnStr("invoiceCash", vo.getInvoiceCash(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("invoiceStatus", vo.getInvoiceStatus(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("remark", vo.getRemark(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("created_id", vo.getUserUniqueId(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("created_dt", "", TYPE_SYS_DATE, columns, args, paramList, colTypeList);

            sql.append("INSERT INTO  invoicecreditmaster ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertInvoiceCreditMaster] sql :: " + sql.toString());
            logger.info("[insertInvoiceCreditMaster] paramList :: " + paramList.size());
            logger.info("[insertInvoiceCreditMaster] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertInvoiceCreditMaster] row :: " + row);
            
        }catch(Exception e){
            logger.error("[insertInvoiceCreditMaster] :: ",e);
            throw e;
        }finally{
            logger.info("[insertInvoiceCashMaster][End]");
        }
	}
	
	@Override
	public void updateInvoiceCreditMaster(final InvoiceCreditMasterBean vo)throws Exception {
		logger.info("[updateInvoiceCreditMaster][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update invoicecreditmaster");
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
			
			logger.info("[updateInvoiceCreditMaster] row :: " + row);
			
		}catch(Exception e){
			logger.error("[updateInvoiceCreditMaster] :: ",e);
			throw e;
		}finally{
			logger.info("[updateInvoiceCreditMaster][End]");
		}
	}
	
	@Override
	public void updateInvoiceCreditMasterStatus(final InvoiceCreditMasterBean vo)throws Exception {
		logger.info("[updateInvoiceCreditMasterStatus][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update invoicecreditmaster");
			sql.append("\n	set invoiceStatus = ?");
			sql.append("\n		, updated_id = ?");
			sql.append("\n		, updated_dt = sysdate()");
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
			
			logger.info("[updateInvoiceCreditMasterStatus] row :: " + row);
			
		}catch(Exception e){
			logger.error("[updateInvoiceCreditMasterStatus] :: ",e);
			throw e;
		}finally{
			logger.info("[updateInvoiceCreditMasterStatus][End]");
		}
	}
	
	@Override
	public ArrayList<InvoiceCreditDetailBean> getInvoiceCreditDetailList(String invoiceCode, String tin) throws Exception {
		logger.info("[getInvoiceCreditDetailList][Begin]");
		
		StringBuilder       				sql         = new StringBuilder();
		ArrayList<InvoiceCreditDetailBean>	resultList 	= new ArrayList<InvoiceCreditDetailBean>();
		
		try{
			logger.info("[getInvoiceCreditDetailList] invoiceCode :: " + invoiceCode);
			logger.info("[getInvoiceCreditDetailList] tin 		:: " + tin);
			
			sql.append("select a.*, b.productName, e.quantity as inventory, b.unitCode, c.unitName");
			sql.append("\n	from invoicecreditdetail a");
			sql.append("\n		INNER JOIN productmaster b on b.productCode = a.productCode and a.tin = b.tin");
			sql.append("\n		INNER JOIN unittype c on c.unitCode = b.unitCode and a.tin = c.tin");
			sql.append("\n		LEFT  JOIN invoicecreditmaster d ON a.invoiceCode = d.invoiceCode and a.tin = d.tin");
			sql.append("\n		LEFT JOIN	productquantity e ON d.tin = e.tin and e.productCode = a.productCode");
			sql.append("\n	where a.invoiceCode 	= ?");
			sql.append("\n		and a.tin		= ?");
			sql.append("\n	order by a.seq asc");
			
			logger.info("[getInvoiceCreditDetailList] sql :: " + sql);
			
			resultList = (ArrayList<InvoiceCreditDetailBean>) jdbcTemplate.query(sql.toString(), new Object[]{invoiceCode,tin}, new RowMapper<InvoiceCreditDetailBean>() {
	            @Override
	            public InvoiceCreditDetailBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	InvoiceCreditDetailBean item = new InvoiceCreditDetailBean();
	            	
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
			logger.info("[getInvoiceCreditDetailList][End]");
		}
		
		return resultList;
	}
	
	@Override
	public void insertInvoiceCreditDetail(InvoiceCreditDetailBean vo)throws Exception {
		logger.info("[insertInvoiceCreditDetail][Begin]");
        
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

            sql.append("INSERT INTO  invoicecreditdetail ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertInvoiceCreditDetail] sql :: " + sql.toString());
            logger.info("[insertInvoiceCreditDetail] paramList :: " + paramList.size());
            logger.info("[insertInvoiceCreditDetail] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertInvoiceCreditDetail] row :: " + row);
            
        }catch(Exception e){
            logger.error("[insertInvoiceCreditDetail] :: ",e);
            throw e;
        }finally{
            logger.info("[insertInvoiceCreditDetail][End]");
        }
	}
	
	@Override
	public void deleteInvoiceCreditDetail(String invoiceCode, String tin) throws Exception{
		logger.info("[deleteInvoiceCreditDetail][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("delete from invoicecreditdetail where invoiceCode = ? and tin = ?");
			
			logger.info("[deleteInvoiceCreditDetail] invoiceCode 	:: " + invoiceCode);
			logger.info("[deleteInvoiceCreditDetail] tin 			:: " + tin);
			logger.info("[deleteInvoiceCreditDetail] sql 			:: " + sql.toString());
			
			int row = jdbcTemplate.update(sql.toString(), new Object[]{invoiceCode,tin});
			
			logger.info("[deleteInvoiceCreditDetail] row :: " + row);
		}catch(Exception e){
			logger.error("deleteInvoiceCreditDetail :: ", e);
			throw e;
		}finally{
			logger.info("[deleteInvoiceCreditDetail][End]");
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
		    sql.append("\n	FROM invoicecreditmaster");
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
	
	@Override
	public void searchByCriteriaForCredit(PaginatedListBean paginatedList,InvoiceCreditMasterBean invoiceCreditMasterBean) throws Exception {
		logger.info("[searchByCriteriaForCredit][Begin]");
		
		StringBuilder       				sql         			= new StringBuilder();
		ArrayList<InvoiceCreditMasterBean>	resultList 				= new ArrayList<InvoiceCreditMasterBean>();
		ArrayList<Object>					params					= new ArrayList<Object>();
		String 								invoiceDateForm			= null;
		String 								invoiceDateTo			= null;
		
		try{
			
			invoiceDateForm = EnjoyUtils.dateThaiToDb(invoiceCreditMasterBean.getInvoiceDateForm());
			invoiceDateTo	= EnjoyUtils.dateThaiToDb(invoiceCreditMasterBean.getInvoiceDateTo());
			
			sql.append("select a.*, concat(b.cusName, ' ', b.cusSurname) cusFullName");
			sql.append("\n	from invoicecreditmaster a");
			sql.append("\n		LEFT JOIN customer b ON a.cusCode = b.cusCode and a.tin = b.tin");
			sql.append("\n	where a.invoiceStatus 	= 'A'");
			sql.append("\n		and a.tin	= ?");
			
			params.add(invoiceCreditMasterBean.getTin());
			
			if(EnjoyUtils.chkNull(invoiceCreditMasterBean.getInvoiceCode())){
				sql.append("\n and a.invoiceCode LIKE CONCAT(?, '%')");
				params.add(invoiceCreditMasterBean.getInvoiceCode());
			}
			
			if(EnjoyUtils.chkNull(invoiceDateForm)){
				sql.append("\n and a.invoiceDate >= STR_TO_DATE(?, '%Y%m%d')");
				params.add(invoiceDateForm);
			}
			
			if(EnjoyUtils.chkNull(invoiceDateTo)){
				sql.append("\n and a.invoiceDate <= STR_TO_DATE(?, '%Y%m%d')");
				params.add(invoiceDateTo);
			}
			
			if(EnjoyUtils.chkNull(invoiceCreditMasterBean.getCusFullName())){
				sql.append("\n and CONCAT(b.cusName, ' ', b.cusSurname) LIKE CONCAT('%', ?, '%')");
				params.add(invoiceCreditMasterBean.getCusFullName());
			}
			
			if(EnjoyUtils.chkNull(invoiceCreditMasterBean.getInvoiceType())){
				sql.append("\n and a.invoiceType = ?");
				params.add(invoiceCreditMasterBean.getInvoiceType());
			}
			
			sql.append("\n order by a.invoiceCode desc");
			
			logger.info("[searchByCriteriaForCredit] sql :: " + sql);
			
			String rowNumSql = decorateRowNumSQL(sql.toString(), paginatedList.getIndex(), paginatedList.getPageSize());
			
			resultList = (ArrayList<InvoiceCreditMasterBean>) jdbcTemplate.query(rowNumSql, params.toArray(), new RowMapper<InvoiceCreditMasterBean>() {
	            @Override
	            public InvoiceCreditMasterBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	InvoiceCreditMasterBean item = new InvoiceCreditMasterBean();
	            	
	            	String invoiceTypeDesc = EnjoyUtils.nullToStr(rs.getString("invoiceType")).equals("V")?"มี VAT":"ไม่มี VAT";
					
					item.setInvoiceCode			(EnjoyUtils.nullToStr(rs.getString("invoiceCode")));
					item.setInvoiceType			(EnjoyUtils.nullToStr(rs.getString("invoiceType")));
					item.setInvoiceTypeDesc		(invoiceTypeDesc);
					item.setCusFullName			(EnjoyUtils.nullToStr(rs.getString("cusFullName")));
					item.setInvoiceDate			(EnjoyUtils.dateToThaiDisplay(rs.getString("invoiceDate")));
					item.setInvoiceTotal		(EnjoyUtils.convertFloatToDisplay(rs.getString("invoiceTotal"), 2));
					item.setRemark				(EnjoyUtils.nullToStr(rs.getString("remark")));
					item.setInvoiceCash			(EnjoyUtils.nullToStr(rs.getString("invoiceCash")));
					item.setTin					(EnjoyUtils.nullToStr(rs.getString("tin")));
	            	
	                return item;
	            }
	        });
			
			paginatedList.setFullListSize(countTotal(sql.toString(),params.toArray()));
			paginatedList.setList(resultList);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[searchByCriteriaForCredit][End]");
		}
	}
	
	@Override
	public ArrayList<InvoiceCreditMasterBean> searchForBillingReport(InvoiceCreditMasterBean invoiceCreditMasterBean) throws Exception {
		logger.info("[searchForBillingReport][Begin]");
		
		StringBuilder       				sql         		= new StringBuilder();
		ArrayList<InvoiceCreditMasterBean>	resultList 			= new ArrayList<InvoiceCreditMasterBean>();
		ArrayList<Object>					params				= new ArrayList<Object>();
		String 								invoiceDateForm		= null;
		String 								invoiceDateTo		= null;
		
		try{
			
			invoiceDateForm 	= EnjoyUtils.dateThaiToDb(invoiceCreditMasterBean.getInvoiceDateForm());
			invoiceDateTo		= EnjoyUtils.dateThaiToDb(invoiceCreditMasterBean.getInvoiceDateTo());
			
			sql.append("select *");
			sql.append("\n	from invoicecreditmaster");
			sql.append("\n	where tin 	= ?");
			sql.append("\n		and invoiceStatus 	= 'A'");
			sql.append("\n		and cusCode 		= ?");
			sql.append("\n		and invoiceDate >= STR_TO_DATE(?, '%Y%m%d')");
			sql.append("\n		and invoiceDate <= STR_TO_DATE(?, '%Y%m%d')");
			
			params.add(invoiceCreditMasterBean.getTin());
			params.add(invoiceCreditMasterBean.getCusCode());
			params.add(invoiceDateForm);
			params.add(invoiceDateTo);
			
			logger.info("[searchForBillingReport] sql :: " + sql);
			
			resultList = (ArrayList<InvoiceCreditMasterBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<InvoiceCreditMasterBean>() {
	            @Override
	            public InvoiceCreditMasterBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	InvoiceCreditMasterBean item = new InvoiceCreditMasterBean();
	            	
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
					item.setInvoiceCash				(EnjoyUtils.nullToStr(rs.getString("invoiceCash")));
					item.setInvoiceStatus			(EnjoyUtils.nullToStr(rs.getString("invoiceStatus")));
					item.setTin						(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setRemark					(EnjoyUtils.nullToStr(rs.getString("remark")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[searchForBillingReport][End]");
		}
		
		return resultList;
	}
	
	@Override
	public InvoiceCreditMasterBean sumTotalForBillingReport(InvoiceCreditMasterBean invoiceCreditMasterBean) throws Exception{
		logger.info("[sumTotalForBillingReport][Begin]");
		
		StringBuilder       				sql         		= new StringBuilder();
		ArrayList<InvoiceCreditMasterBean>	resultList 			= new ArrayList<InvoiceCreditMasterBean>();
		ArrayList<Object>					params				= new ArrayList<Object>();
		String 								invoiceDateForm		= null;
		String 								invoiceDateTo		= null;
		InvoiceCreditMasterBean				bean				= null;
		
		try{	
			
			invoiceDateForm 	= EnjoyUtils.dateThaiToDb(invoiceCreditMasterBean.getInvoiceDateForm());
			invoiceDateTo		= EnjoyUtils.dateThaiToDb(invoiceCreditMasterBean.getInvoiceDateTo());
			
			sql.append("select SUM(invoicePrice) as invoicePrice");
			sql.append("\n		, SUM(invoicediscount) as invoicediscount ");
			sql.append("\n		, SUM(invoiceDeposit) as invoiceDeposit ");
			sql.append("\n		, SUM(invoiceVat) as invoiceVat ");
			sql.append("\n		, SUM(invoiceTotal) as invoiceTotal ");
			sql.append("\n	from invoicecreditmaster");
			sql.append("\n	where tin 				= ?");
			sql.append("\n		and invoiceStatus 	= 'A'");
			sql.append("\n		and cusCode 		= ?");
			sql.append("\n		and invoiceDate >= STR_TO_DATE(?, '%Y%m%d')");
			sql.append("\n		and invoiceDate <= STR_TO_DATE(?, '%Y%m%d')");
			
			params.add(invoiceCreditMasterBean.getTin());
			params.add(invoiceCreditMasterBean.getCusCode());
			params.add(invoiceDateForm);
			params.add(invoiceDateTo);
			
			if(EnjoyUtils.chkNull(invoiceCreditMasterBean.getInvoiceCode())){
				sql.append("\n and invoiceCode LIKE CONCAT(?, '%')");
				params.add(invoiceCreditMasterBean.getInvoiceCode());
			}
			
			if(EnjoyUtils.chkNull(invoiceCreditMasterBean.getInvoiceType())){
				sql.append("\n and invoiceType = ?");
				params.add(invoiceCreditMasterBean.getInvoiceType());
			}
			
			resultList = (ArrayList<InvoiceCreditMasterBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<InvoiceCreditMasterBean>() {
	            @Override
	            public InvoiceCreditMasterBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	InvoiceCreditMasterBean item = new InvoiceCreditMasterBean();
	            	
	            	item.setInvoicePrice	(EnjoyUtils.convertFloatToDisplay(rs.getString("invoicePrice"), 2));
					item.setInvoicediscount	(EnjoyUtils.convertFloatToDisplay(rs.getString("invoicediscount"), 2));
					item.setInvoiceDeposit	(EnjoyUtils.convertFloatToDisplay(rs.getString("invoiceDeposit"), 2));
					item.setInvoiceVat		(EnjoyUtils.convertFloatToDisplay(rs.getString("invoiceVat"), 2));
					item.setInvoiceTotal	(EnjoyUtils.convertFloatToDisplay(rs.getString("invoiceTotal"), 2));
	            	
	                return item;
	            }
	        });
			
			if(resultList!=null && !resultList.isEmpty()){
				bean = resultList.get(0);
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[searchForBillingReport][End]");
		}
		
		return bean;
		
	}
}
