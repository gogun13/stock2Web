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
import com.enjoy.core.main.Constants;
import com.enjoy.core.services.AbstractJdbcService;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.ProductdetailBean;
import com.enjoy.stock.bean.ProductmasterBean;

@Repository
public class ProductServiceImpl extends AbstractJdbcService implements ProductService{
	
	private static final Logger logger = Logger.getLogger(ProductServiceImpl.class);
	
	@Override
	public void searchByCriteria(PaginatedListBean paginatedList,ProductmasterBean productmasterBean,String tin) throws Exception {
		logger.info("[searchByCriteria][Begin]");
		
		StringBuilder       			sql         = new StringBuilder();
		ArrayList<ProductmasterBean>	resultList 	= new ArrayList<ProductmasterBean>();
		ArrayList<Object>				params		= new ArrayList<Object>();
		
		try{	
			
			sql.append("select a.*, b.productTypeName, c.productGroupName, d.unitName");
			sql.append("\n	from productmaster a");
			sql.append("\n		inner join productype b on b.productTypeCode = a.productType and b.tin = a.tin and b.productTypeStatus = 'A'");
			sql.append("\n		inner join productgroup c on c.productTypeCode 	= a.productType and c.productGroupCode = a.productGroup and c.tin = a.tin and c.productGroupStatus = 'A'");
			sql.append("\n		inner join unittype d on d.unitCode = a.unitCode and d.tin = a.tin and d.unitStatus = 'A'");
			sql.append("\n	where a.tin	= ?");
			sql.append("\n		and a.productStatus = 'A'");
			
			params.add(tin);
			
			if(EnjoyUtils.chkNull(productmasterBean.getProductTypeName())){
				sql.append("\n and b.productTypeName LIKE CONCAT(?, '%')");
				params.add(productmasterBean.getProductTypeName());
			}
			
			if(EnjoyUtils.chkNull(productmasterBean.getProductGroupName())){
				sql.append("\n and c.productGroupName LIKE CONCAT(?, '%')");
				params.add(productmasterBean.getProductGroupName());
			}
			
			if(EnjoyUtils.chkNull(productmasterBean.getProductName())){
				sql.append("\n and a.productName LIKE CONCAT(?, '%')");
				params.add(productmasterBean.getProductName());
			}
			
			sql.append("\n order by a.productCodeDis asc");
			
			logger.info("[searchByCriteria] sql :: " + sql);
			
			String rowNumSql = decorateRowNumSQL(sql.toString(), paginatedList.getIndex(), paginatedList.getPageSize());
			
			resultList = (ArrayList<ProductmasterBean>) jdbcTemplate.query(rowNumSql, params.toArray(), new RowMapper<ProductmasterBean>() {
	            @Override
	            public ProductmasterBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ProductmasterBean item = new ProductmasterBean();
	            	
	            	item.setProductCode				(EnjoyUtils.nullToStr(rs.getString("productCode")));
					item.setTin						(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setProductCodeDis			(EnjoyUtils.nullToStr(rs.getString("productCodeDis")));
					item.setProductTypeCode			(EnjoyUtils.nullToStr(rs.getString("productType")));
					item.setProductGroupCode		(EnjoyUtils.nullToStr(rs.getString("productGroup")));
					item.setProductName				(EnjoyUtils.nullToStr(rs.getString("productName")));
					item.setUnitCode				(EnjoyUtils.nullToStr(rs.getString("unitCode")));
					item.setMinQuan					(rs.getString("minQuan"));
					item.setCostPrice				(EnjoyUtils.convertFloatToDisplay(rs.getString("costPrice"), 2));
					item.setSalePrice1				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice1"), 2));
					item.setSalePrice2				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice2"), 2));
					item.setSalePrice3				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice3"), 2));
					item.setSalePrice4				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice4"), 2));
					item.setSalePrice5				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice5"), 2));
					item.setProductTypeName			(EnjoyUtils.nullToStr(rs.getString("productTypeName")));
					item.setProductGroupName		(EnjoyUtils.nullToStr(rs.getString("productGroupName")));
					item.setProductStatus			(EnjoyUtils.nullToStr(rs.getString("productStatus")));
					item.setUnitName				(EnjoyUtils.nullToStr(rs.getString("unitName")));
	            	
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
	public ProductmasterBean getProductDetail(String productCode,String tin) throws Exception {
		logger.info("[getProductDetail][Begin]");
		
		StringBuilder 					sql 				= new StringBuilder();
		ProductmasterBean 				productmasterBean 	= null;
		ArrayList<ProductmasterBean> 	result 				= null;
		
		try{
		    
		    sql.append("select a.*, b.productTypeName, c.productGroupName, d.unitName, e.quantity");
		    sql.append("\n	from productmaster a");
		    sql.append("\n		inner join productype b on b.productTypeCode = a.productType and b.tin = a.tin and b.productTypeStatus = 'A'");
		    sql.append("\n		inner join productgroup c on c.productTypeCode 	= a.productType and c.productGroupCode = a.productGroup and c.tin = a.tin and c.productGroupStatus = 'A'");
		    sql.append("\n		inner join unittype d on d.unitCode = a.unitCode and d.tin = a.tin and d.unitStatus = 'A'");
		    sql.append("\n		left join productquantity e on e.productCode = a.productCode and e.tin = a.tin");
		    sql.append("\n	where a.productCode		= ?");
		    sql.append("\n		and a.tin			= ?");
		    sql.append("\n		and a.productStatus	= 'A'");
		    
		    logger.info("[getProductDetail] productCode :: " + productCode);
		    logger.info("[getProductDetail] tin :: " + tin);
		    logger.info("[getProductDetail] sql :: " + sql.toString());
			
		    result = (ArrayList<ProductmasterBean>) jdbcTemplate.query(sql.toString(), new Object[]{productCode,tin}, new RowMapper<ProductmasterBean>() {
	            @Override
	            public ProductmasterBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ProductmasterBean item = new ProductmasterBean();
	            	
	            	item.setProductCode				(EnjoyUtils.nullToStr(rs.getString("productCode")));
					item.setTin						(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setProductCodeDis			(EnjoyUtils.nullToStr(rs.getString("productCodeDis")));
					item.setProductTypeCode			(EnjoyUtils.nullToStr(rs.getString("productType")));
					item.setProductGroupCode		(EnjoyUtils.nullToStr(rs.getString("productGroup")));
					item.setProductName				(EnjoyUtils.nullToStr(rs.getString("productName")));
					item.setUnitCode				(EnjoyUtils.nullToStr(rs.getString("unitCode")));
					item.setMinQuan					(rs.getString("minQuan"));
					item.setCostPrice				(EnjoyUtils.convertFloatToDisplay(rs.getString("costPrice"), 2));
					item.setSalePrice1				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice1"), 2));
					item.setSalePrice2				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice2"), 2));
					item.setSalePrice3				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice3"), 2));
					item.setSalePrice4				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice4"), 2));
					item.setSalePrice5				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice5"), 2));
					item.setProductTypeName			(EnjoyUtils.nullToStr(rs.getString("productTypeName")));
					item.setProductGroupName		(EnjoyUtils.nullToStr(rs.getString("productGroupName")));
					item.setUnitName				(EnjoyUtils.nullToStr(rs.getString("unitName")));
					item.setQuantity				(EnjoyUtils.convertNumberToDisplay(rs.getString("quantity")));
					item.setProductStatus			(EnjoyUtils.nullToStr(rs.getString("productStatus")));
	            	
	                return item;
	            }
	        });
		    
		    if(result != null && !result.isEmpty()){
		    	productmasterBean = result.get(0);
		    }
			
			
		}catch(Exception e){
			logger.error("[getProductDetail] :: ",e);
			throw e;
		}finally{
	        logger.info("[getProductDetail][End]");
		}
		
		return productmasterBean;
	}
	
	@Override
	public void insertProductmaster(ProductmasterBean vo)throws Exception {
		logger.info("[insertProductmaster][Begin]");
        
        StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
        
        try{
        	setColumnStr("productCode", vo.getProductCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("tin", vo.getTin(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("productCodeDis", vo.getProductCodeDis(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("productType", vo.getProductTypeCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("productGroup", vo.getProductGroupCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("productName", vo.getProductName(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("unitCode", vo.getUnitCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("minQuan", vo.getMinQuan(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("costPrice", vo.getCostPrice(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("salePrice1", vo.getSalePrice1(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("salePrice2", vo.getSalePrice2(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("salePrice3", vo.getSalePrice3(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("salePrice4", vo.getSalePrice4(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("salePrice5", vo.getSalePrice5(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("productStatus", vo.getProductStatus(), TYPE_STRING, columns, args, paramList, colTypeList);

            sql.append("INSERT INTO  productmaster ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertProductmaster] sql :: " + sql.toString());
            logger.info("[insertProductmaster] paramList :: " + paramList.size());
            logger.info("[insertProductmaster] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertProductmaster] row :: " + row);
            
        }catch(Exception e){
            logger.error("[insertProductmaster] :: ",e);
            throw e;
        }finally{
            logger.info("[insertProductmaster][End]");
        }
	}
	
	@Override
	public void updateProductmaster(final ProductmasterBean vo)throws Exception {
		logger.info("[updateProductmaster][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("update productmaster");
			sql.append("\n	set productCodeDis 		= ?");
			sql.append("\n		, productType 		= ?");
			sql.append("\n		, productGroup		= ?");
			sql.append("\n		, productName		= ?");
			sql.append("\n		, unitCode			= ?");
			sql.append("\n		, minQuan			= ?");
			sql.append("\n		, costPrice 		= ?");
			sql.append("\n		, salePrice1 		= ?");
			sql.append("\n		, salePrice2 		= ?");
			sql.append("\n		, salePrice3 		= ?");
			sql.append("\n		, salePrice4 		= ?");
			sql.append("\n		, salePrice5 		= ?");
			sql.append("\n	where productCode 	= ?");
			sql.append("\n		and tin 		= ?");
			
			int row = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	                int i = 1;
	                
	                ps.setString(i++, vo.getProductCodeDis());
	                ps.setString(i++, vo.getProductTypeCode());
	                ps.setString(i++, vo.getProductGroupCode());
	                ps.setString(i++, vo.getProductName());
	                ps.setString(i++, vo.getUnitCode());
	                ps.setString(i++, vo.getMinQuan());
	                ps.setString(i++, vo.getCostPrice());
	                ps.setString(i++, vo.getSalePrice1());
	                ps.setString(i++, vo.getSalePrice2());
	                ps.setString(i++, vo.getSalePrice3());
	                ps.setString(i++, vo.getSalePrice4());
	                ps.setString(i++, vo.getSalePrice5());
	                ps.setString(i++, vo.getProductCode());
	                ps.setString(i++, vo.getTin());
	            }
	        });
			
			logger.info("[updateProductmaster] row :: " + row);
			
		}catch(Exception e){
			logger.error("[updateProductmaster] :: ",e);
			throw e;
		}finally{
			logger.info("[updateProductmaster][End]");
		}
	}
	
	@Override
	public int checkDupProductCode(String productCodeDis, String tin, String productCode, String pageMode) throws Exception{
		logger.info("[checkDupProductCode][Begin]");
		
		StringBuilder       sql     = new StringBuilder();
		int 				result	= 0;
		ArrayList<Object>	params	= new ArrayList<Object>();
		
		try{
			logger.info("[checkDupProductCode] productCodeDis 	:: " + productCodeDis);
			logger.info("[checkDupProductCode] tin 				:: " + tin);
			logger.info("[checkDupProductCode] productCode 		:: " + productCode);
			logger.info("[checkDupProductCode] pageMode 		:: " + pageMode);
			
			sql.append("select count(*) cou from productmaster where productCodeDis = ? and tin = ? and productStatus	= 'A'");
			
			params.add(productCodeDis);
			params.add(tin);
			
			if(!Constants.NEW.equals(pageMode)){
				sql.append(" and productCode <> ?");
				params.add(productCode);
			}
			
			logger.info("[checkDupProductCode] sql 			:: " + sql.toString());
			
			result = jdbcTemplate.queryForObject(sql.toString() ,params.toArray(), Integer.class);
			
			logger.info("[checkDupProductCode] result 			:: " + result);
			
		}catch(Exception e){
			logger.error("checkDupProductCode :: ", e);
			throw e;
		}finally{
			logger.info("[checkDupProductCode][End]");
		}
		
		return result;
	}
	
	@Override
	public int checkDupProductName(String productName, String productCode, String tin, String pageMode) throws Exception{
		logger.info("[checkDupProductName][Begin]");
		
		StringBuilder       sql     = new StringBuilder();
		int 				result	= 0;
		ArrayList<Object>	params	= new ArrayList<Object>();
		
		try{
			logger.info("[checkDupProductCode] productName 		:: " + productName);
			logger.info("[checkDupProductCode] tin 				:: " + tin);
			logger.info("[checkDupProductCode] productCode 		:: " + productCode);
			logger.info("[checkDupProductCode] pageMode 		:: " + pageMode);
			
			sql.append("select count(*) cou from productmaster where productName = ? and tin = ? and productStatus	= 'A'");
			
			params.add(productName);
			params.add(tin);
			
			if(!Constants.NEW.equals(pageMode)){
				sql.append(" and productCode <> ?");
				params.add(productCode);
			}
			
			logger.info("[checkDupProductName] sql 			:: " + sql.toString());
			
			result = jdbcTemplate.queryForObject(sql.toString() ,params.toArray(), Integer.class);
			
			logger.info("[checkDupProductName] result 			:: " + result);
			
		}catch(Exception e){
			logger.error("checkDupProductName :: ", e);
			throw e;
		}finally{
			logger.info("[checkDupProductName][End]");
		}
		
		return result;
	}
	
	@Override
	public ArrayList<ComboBean> productNameList(String productName, String productTypeName, String productGroupName, String tin, boolean flag) throws Exception {
		logger.info("[productNameList][Begin]");
		
		StringBuilder       	sql         = new StringBuilder();
		ArrayList<ComboBean>	resultList 	= new ArrayList<ComboBean>();
		ArrayList<Object>		params		= new ArrayList<Object>();
		
		try{
			logger.info("[productNameList] productName 		:: " + productName);
			logger.info("[productNameList] productTypeName 	:: " + productTypeName);
			logger.info("[productNameList] productGroupName :: " + productGroupName);
			logger.info("[productNameList] tin 				:: " + tin);
			logger.info("[productNameList] flag 			:: " + flag);
			
			if(flag==true){
				sql.append("select productCode, productName");
				sql.append("\n	from productmaster a");
				sql.append("\n		inner join productype b on b.productTypeCode = a.productType and b.tin = a.tin");
				sql.append("\n		inner JOIN productgroup c on c.productGroupCode = a.productGroup and c.productTypeCode = b.productTypeCode and c.tin = a.tin");
				sql.append("\n	WHERE	a.productStatus = 'A'");
				sql.append("\n		and b.productTypeStatus = 'A'");
				sql.append("\n		and c.productGroupStatus = 'A'");
				sql.append("\n		and a.tin = ?");
				
				params.add(tin);
				
				if(EnjoyUtils.chkNull(productTypeName)){
					sql.append("\n 		and b.productTypeName = ?");
	    			params.add(productTypeName);
	    		}
	    		if(EnjoyUtils.chkNull(productGroupName)){
	    			sql.append("\n		and c.productGroupName = ?");
	    			params.add(productGroupName);
	    		}
				
			}else{
				sql.append("select productCode, productName");
				sql.append("\n	from productmaster a");
				sql.append("\n		inner join productype b on b.productTypeCode = a.productType and b.tin = a.tin");
				sql.append("\n		inner JOIN productgroup c on c.productGroupCode = a.productGroup and c.productTypeCode = b.productTypeCode and c.tin = a.tin");
				sql.append("\n	WHERE	a.productStatus = 'A'");
				sql.append("\n		and b.productTypeStatus = 'A'");
				sql.append("\n		and c.productGroupStatus = 'A'");
				sql.append("\n		and a.tin = ?");
				sql.append("\n		and b.productTypeName = ?");
				sql.append("\n		and c.productGroupName = ?");
				
				params.add(tin);
				params.add(productTypeName);
				params.add(productGroupName);
			}
			
			if(EnjoyUtils.chkNull(productName)){
				sql.append("\n and a.productName LIKE CONCAT(?, '%')");
				params.add(productName);
			}
			
			sql.append("\n order by a.productName asc limit 10");
			
			logger.info("[productNameList] sql :: " + sql);
			
			resultList = (ArrayList<ComboBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<ComboBean>() {
	            @Override
	            public ComboBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ComboBean item = new ComboBean();
	            	
	            	item.setCode(EnjoyUtils.nullToStr(rs.getString("productCode")));
	            	item.setDesc(EnjoyUtils.nullToStr(rs.getString("productName")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[productNameList][End]");
		}
		
		return resultList;
	}
	
	@Override
	public ArrayList<ProductdetailBean> getProductdetailList(String productCode, String tin) throws Exception {
		logger.info("[getProductdetailList][Begin]");
		
		StringBuilder       			sql         = new StringBuilder();
		ArrayList<ProductdetailBean>	resultList 	= new ArrayList<ProductdetailBean>();
		
		try{
			logger.info("[getProductdetailList] productCode :: " + productCode);
			logger.info("[getProductdetailList] tin 		:: " + tin);
			
			sql.append("select * from productdetail where productCode = ? and tin = ? order by seq asc");
			
			logger.info("[getProductdetailList] sql :: " + sql);
			
			resultList = (ArrayList<ProductdetailBean>) jdbcTemplate.query(sql.toString(), new Object[]{productCode,tin}, new RowMapper<ProductdetailBean>() {
	            @Override
	            public ProductdetailBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ProductdetailBean item = new ProductdetailBean();
	            	
	            	item.setProductCode			(EnjoyUtils.nullToStr(rs.getString("productCode")));
					item.setSeqDb				(EnjoyUtils.nullToStr(rs.getString("seq")));
					item.setQuanDiscount		(EnjoyUtils.convertFloatToDisplay(rs.getString("quanDiscount"), 2));
					item.setDiscountRate		(EnjoyUtils.convertFloatToDisplay(rs.getString("discountRate"), 2));
					item.setStartDate			(EnjoyUtils.dateToThaiDisplay(rs.getString("startDate")));
					item.setExpDate				(EnjoyUtils.dateToThaiDisplay(rs.getString("expDate")));
					item.setAvailPageFlag		(EnjoyUtils.nullToStr(rs.getString("availPageFlag")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getProductdetailList][End]");
		}
		
		return resultList;
	}
	
	@Override
	public void insertProductdetail(ProductdetailBean vo)throws Exception {
		logger.info("[insertProductdetail][Begin]");
        
        StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
        
        try{
        	setColumnStr("productCode", vo.getProductCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("tin", vo.getTin(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("seq", vo.getSeqDb(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("quanDiscount", vo.getQuanDiscount(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("discountRate", vo.getDiscountRate(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("startDate", EnjoyUtils.dateToThaiDB(vo.getStartDate()), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("expDate", EnjoyUtils.dateToThaiDB(vo.getExpDate()), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("availPageFlag", vo.getAvailPageFlag(), TYPE_STRING, columns, args, paramList, colTypeList);

            sql.append("INSERT INTO  productdetail ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertProductdetail] sql :: " + sql.toString());
            logger.info("[insertProductdetail] paramList :: " + paramList.size());
            logger.info("[insertProductdetail] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertProductdetail] row :: " + row);
            
        }catch(Exception e){
            logger.error("[insertProductdetail] :: ",e);
            throw e;
        }finally{
            logger.info("[insertProductdetail][End]");
        }
	}
	
	@Override
	public void deleteProductdetail(String productCode, String tin) throws Exception{
		logger.info("[deleteProductdetail][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		
		try{
			sql.append("delete from productdetail where productCode = ? and tin = ?");
			
			logger.info("[deleteProductdetail] productCode :: " + productCode);
			logger.info("[deleteProductdetail] tin :: " + tin);
			logger.info("[deleteProductdetail] sql :: " + sql.toString());
			
			int row = jdbcTemplate.update(sql.toString(), new Object[]{productCode,tin});
			
			logger.info("[deleteProductdetail] row :: " + row);
		}catch(Exception e){
			logger.error("deleteProductdetail :: ", e);
			throw e;
		}finally{
			logger.info("[deleteProductdetail][End]");
		}
	}
	
	@Override
	public ProductmasterBean getProductDetailByName(String 	productName, String tin) throws Exception {
		logger.info("[getProductDetailByName][Begin]");
		
		StringBuilder 					sql 				= new StringBuilder();
		ProductmasterBean 				productmasterBean 	= null;
		ArrayList<ProductmasterBean> 	result 				= null;
		
		try{
		    
		    sql.append("select a.*, b.productTypeName, c.productGroupName, d.unitName, e.quantity");
		    sql.append("\n	from productmaster a");
		    sql.append("\n		inner join productype b on b.productTypeCode = a.productType and b.tin = a.tin and b.productTypeStatus = 'A'");
		    sql.append("\n		inner join productgroup c on c.productTypeCode 	= a.productType and c.productGroupCode = a.productGroup and c.tin = a.tin and c.productGroupStatus = 'A'");
		    sql.append("\n		inner join unittype d on d.unitCode = a.unitCode and d.tin = a.tin and d.unitStatus = 'A'");
		    sql.append("\n		left join productquantity e on e.productCode = a.productCode and e.tin = a.tin");
		    sql.append("\n	where a.productName		= ?");
		    sql.append("\n		and a.tin			= ?");
		    sql.append("\n		and a.productStatus	= 'A'");
		    
		    logger.info("[getProductDetailByName] productName :: " + productName);
		    logger.info("[getProductDetailByName] tin :: " + tin);
		    logger.info("[getProductDetailByName] sql :: " + sql.toString());
			
		    result = (ArrayList<ProductmasterBean>) jdbcTemplate.query(sql.toString(), new Object[]{productName,tin}, new RowMapper<ProductmasterBean>() {
	            @Override
	            public ProductmasterBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ProductmasterBean item = new ProductmasterBean();
	            	
	            	item.setProductCode				(EnjoyUtils.nullToStr(rs.getString("productCode")));
					item.setTin						(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setProductCodeDis			(EnjoyUtils.nullToStr(rs.getString("productCodeDis")));
					item.setProductTypeCode			(EnjoyUtils.nullToStr(rs.getString("productType")));
					item.setProductGroupCode		(EnjoyUtils.nullToStr(rs.getString("productGroup")));
					item.setProductName				(EnjoyUtils.nullToStr(rs.getString("productName")));
					item.setUnitCode				(EnjoyUtils.nullToStr(rs.getString("unitCode")));
					item.setMinQuan					(EnjoyUtils.convertFloatToDisplay(rs.getString("minQuan"), 2));
					item.setCostPrice				(EnjoyUtils.convertFloatToDisplay(rs.getString("costPrice"), 2));
					item.setSalePrice1				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice1"), 2));
					item.setSalePrice2				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice2"), 2));
					item.setSalePrice3				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice3"), 2));
					item.setSalePrice4				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice4"), 2));
					item.setSalePrice5				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice5"), 2));
					item.setProductTypeName			(EnjoyUtils.nullToStr(rs.getString("productTypeName")));
					item.setProductGroupName		(EnjoyUtils.nullToStr(rs.getString("productGroupName")));
					item.setUnitName				(EnjoyUtils.nullToStr(rs.getString("unitName")));
					item.setQuantity				(EnjoyUtils.convertNumberToDisplay(rs.getString("quantity")));
					item.setProductStatus			(EnjoyUtils.nullToStr(rs.getString("productStatus")));
	            	
	                return item;
	            }
	        });
		    
		    if(result != null && !result.isEmpty()){
		    	productmasterBean = result.get(0);
		    }
			
			
		}catch(Exception e){
			logger.error("[getProductDetailByName] :: ",e);
			throw e;
		}finally{
	        logger.info("[getProductDetailByName][End]");
		}
		
		return productmasterBean;
	}
	
	@Override
	public String getQuanDiscount(String productCode, String quantity, String invoiceDate, String tin, String availPageFlag) throws Exception {
		logger.info("[getQuanDiscount][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		String 				result		= "";
		ArrayList<String>	resultList 	= new ArrayList<String>();
		ArrayList<Object>	params		= new ArrayList<Object>();
		
		try{
			
			logger.info("[getQuanDiscount] productCode :: " + productCode);
			logger.info("[getQuanDiscount] quantity :: " + quantity);
			logger.info("[getQuanDiscount] invoiceDate :: " + EnjoyUtils.dateThaiToDb(invoiceDate));
			logger.info("[getQuanDiscount] tin :: " + tin);
			logger.info("[getQuanDiscount] availPageFlag :: " + availPageFlag);
			
			sql.append("select discountRate");
			sql.append("\n	from productdetail");
			sql.append("\n	where productCode = ?");
			sql.append("\n		and quanDiscount 	<= ?");
			sql.append("\n		and startDate 		<= ?");
			sql.append("\n		and (expDate is null or expDate = '' or expDate >= ?)");
			sql.append("\n		and tin = ?");
			sql.append("\n		and availPageFlag	in ('AL', ?)");
			sql.append("\n	order by quanDiscount DESC, startDate DESC");
			sql.append("\n	LIMIT 1");
			
			logger.info("[getQuanDiscount] sql 			:: " + sql.toString());
			
			params.add(productCode);
			params.add(quantity);
			params.add(EnjoyUtils.dateThaiToDb(invoiceDate));
			params.add(EnjoyUtils.dateThaiToDb(invoiceDate));
			params.add(tin);
			params.add(availPageFlag);
			
			resultList = (ArrayList<String>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<String>() {
	            @Override
	            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return EnjoyUtils.convertNumberToDisplay(rs.getString("discountRate"));
	            }
	        });
			
			if(resultList!=null && !resultList.isEmpty()){
				result = resultList.get(0);
			}
			
			logger.info("[getQuanDiscount] result 			:: " + result);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getQuanDiscount][End]");
		}
		
		return result;
	}
	
	@Override
	public ArrayList<ProductmasterBean> getMultiManageProduct(ProductmasterBean vo) throws Exception {
		logger.info("[getMultiManageProduct][Begin]");
		
		StringBuilder       			sql         = new StringBuilder();
		ArrayList<ProductmasterBean>	resultList 	= new ArrayList<ProductmasterBean>();
		ArrayList<Object>				params		= new ArrayList<Object>();
		
		try{
			
			sql.append("select a.*, b.productTypeName, c.productGroupName, d.unitName, e.quantity");
			sql.append("\n	from productmaster a");
			sql.append("\n		inner join productype b on b.productTypeCode = a.productType and b.tin = a.tin");
			sql.append("\n		inner JOIN productgroup c on c.productGroupCode = a.productGroup and c.productTypeCode = b.productTypeCode and c.tin = a.tin");
			sql.append("\n		inner join unittype d on d.unitCode = a.unitCode and d.tin = a.tin");
			sql.append("\n		left join productquantity e on e.productCode = a.productCode and e.tin = a.tin");
			sql.append("\n	WHERE	a.productStatus = 'A'");
			sql.append("\n		and b.productTypeStatus = 'A'");
			sql.append("\n		and c.productGroupStatus = 'A'");
			sql.append("\n		and d.unitStatus = 'A'");
			sql.append("\n		and a.tin = ?");
			sql.append("\n		and b.productTypeCode = ?");
			sql.append("\n		and c.productGroupCode = ?");
			sql.append("\n		and d.unitCode			= ?");
			
			params.add(vo.getTin());
			params.add(vo.getProductTypeCode());
			params.add(vo.getProductGroupCode());
			params.add(vo.getUnitCode());
			
			logger.info("[getMultiManageProduct] sql :: " + sql);
			
			resultList = (ArrayList<ProductmasterBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<ProductmasterBean>() {
	            @Override
	            public ProductmasterBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ProductmasterBean item = new ProductmasterBean();
	            	
	            	item.setProductCode				(EnjoyUtils.nullToStr(rs.getString("productCode")));
					item.setProductCodeDis			(EnjoyUtils.nullToStr(rs.getString("productCodeDis")));
					item.setTin						(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setProductTypeCode			(EnjoyUtils.nullToStr(rs.getString("productType")));
					item.setProductGroupCode		(EnjoyUtils.nullToStr(rs.getString("productGroup")));
					item.setProductName				(EnjoyUtils.nullToStr(rs.getString("productName")));
					item.setUnitCode				(EnjoyUtils.nullToStr(rs.getString("unitCode")));
					item.setMinQuan					(EnjoyUtils.convertFloatToDisplay(rs.getString("minQuan"), 2));
					item.setCostPrice				(EnjoyUtils.convertFloatToDisplay(rs.getString("costPrice"), 2));
					item.setSalePrice1				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice1"), 2));
					item.setSalePrice2				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice2"), 2));
					item.setSalePrice3				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice3"), 2));
					item.setSalePrice4				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice4"), 2));
					item.setSalePrice5				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice5"), 2));
					item.setProductTypeName			(EnjoyUtils.nullToStr(rs.getString("productTypeName")));
					item.setProductGroupName		(EnjoyUtils.nullToStr(rs.getString("productGroupName")));
					item.setQuantity				(EnjoyUtils.convertNumberToDisplay(rs.getString("quantity")));
					item.setProductStatus			(EnjoyUtils.nullToStr(rs.getString("productStatus")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getMultiManageProduct][End]");
		}
		
		return resultList;
	}
	
	@Override
	public int genId(String tin) throws Exception {
		logger.info("[genId][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		int 				result		= 1;
		ArrayList<Integer>	resultList 	= new ArrayList<Integer>();
		
		try{
			
			sql.append("select (max(productCode) + 1) newId from productmaster where tin = ?");
			
			logger.info("[genId] sql 			:: " + sql.toString());
			
			resultList = (ArrayList<Integer>) jdbcTemplate.query(sql.toString(), new Object[]{tin}, new RowMapper<Integer>() {
	            @Override
	            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	
	                return rs.getInt("newId");
	            }
	        });
			
			if(resultList!=null && !resultList.isEmpty()){
				result = resultList.get(0);
			}
			
			logger.info("[genId] result 			:: " + result);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[genId][End]");
		}
		
		return result;
	}
	
	@Override
	public void cancelProductmaster(String tin, String productType, String productGroup, String productCode)throws Exception {
		logger.info("[cancelProductgroupByProductTypeCode][Begin]");
		
		StringBuilder       sql         = new StringBuilder();
		ArrayList<Object>	params		= new ArrayList<Object>();
		
		try{
			sql.append("update  productmaster");
			sql.append("\n	set productStatus 		= 'R'");
			sql.append("\n	where tin 		= ?");
			
			params.add(tin);
			
			if(EnjoyUtils.chkNull(productType)){
				sql.append("\n		and productType = ?");
				params.add(productType);
			}
			
			if(EnjoyUtils.chkNull(productGroup)){
				sql.append("\n		and productGroup = ?");
				params.add(productGroup);
			}
			
			if(EnjoyUtils.chkNull(productCode)){
				sql.append("\n		and productCode = ?");
				params.add(productCode);
			}
			
			logger.info("[cancelProductgroupByProductTypeCode] hql :: " + sql.toString());
			
			int row = jdbcTemplate.update(sql.toString(), params.toArray());
			
			logger.info("[cancelProductgroupByProductTypeCode] row :: " + row);
			
		}catch(Exception e){
			logger.error("[cancelProductgroupByProductTypeCode] :: ",e);
			throw e;
		}finally{
			logger.info("[cancelProductgroupByProductTypeCode][End]");
		}
	}
	
	@Override
	public ProductmasterBean getProductDetailByProductCodeDis(String tin, String productCodeDis) throws Exception {
		logger.info("[getProductDetailByProductCodeDis][Begin]");
		
		StringBuilder 					sql 				= new StringBuilder();
		ProductmasterBean 				productmasterBean 	= null;
		ArrayList<ProductmasterBean> 	result 				= null;
		
		try{
		    
		    sql.append("select a.*, b.productTypeName, c.productGroupName, d.unitName, e.quantity");
		    sql.append("\n	from productmaster a");
		    sql.append("\n		inner join productype b on b.productTypeCode = a.productType and b.tin = a.tin and b.productTypeStatus = 'A'");
		    sql.append("\n		inner join productgroup c on c.productTypeCode 	= a.productType and c.productGroupCode = a.productGroup and c.tin = a.tin and c.productGroupStatus = 'A'");
		    sql.append("\n		inner join unittype d on d.unitCode = a.unitCode and d.tin = a.tin and d.unitStatus = 'A'");
		    sql.append("\n		left join productquantity e on e.productCode = a.productCode and e.tin = a.tin");
		    sql.append("\n	where a.productCodeDis		= ?");
		    sql.append("\n		and a.tin			= ?");
		    sql.append("\n		and a.productStatus	= 'A'");
		    
		    logger.info("[getProductDetailByProductCodeDis] productCodeDis :: " + productCodeDis);
		    logger.info("[getProductDetailByProductCodeDis] tin :: " + tin);
		    logger.info("[getProductDetailByProductCodeDis] sql :: " + sql.toString());
			
		    result = (ArrayList<ProductmasterBean>) jdbcTemplate.query(sql.toString(), new Object[]{productCodeDis,tin}, new RowMapper<ProductmasterBean>() {
	            @Override
	            public ProductmasterBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ProductmasterBean item = new ProductmasterBean();
	            	
	            	item.setProductCode				(EnjoyUtils.nullToStr(rs.getString("productCode")));
					item.setTin						(EnjoyUtils.nullToStr(rs.getString("tin")));
					item.setProductCodeDis			(EnjoyUtils.nullToStr(rs.getString("productCodeDis")));
					item.setProductTypeCode			(EnjoyUtils.nullToStr(rs.getString("productType")));
					item.setProductGroupCode		(EnjoyUtils.nullToStr(rs.getString("productGroup")));
					item.setProductName				(EnjoyUtils.nullToStr(rs.getString("productName")));
					item.setUnitCode				(EnjoyUtils.nullToStr(rs.getString("unitCode")));
					item.setMinQuan					(EnjoyUtils.convertFloatToDisplay(rs.getString("minQuan"), 2));
					item.setCostPrice				(EnjoyUtils.convertFloatToDisplay(rs.getString("costPrice"), 2));
					item.setSalePrice1				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice1"), 2));
					item.setSalePrice2				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice2"), 2));
					item.setSalePrice3				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice3"), 2));
					item.setSalePrice4				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice4"), 2));
					item.setSalePrice5				(EnjoyUtils.convertFloatToDisplay(rs.getString("salePrice5"), 2));
					item.setProductTypeName			(EnjoyUtils.nullToStr(rs.getString("productTypeName")));
					item.setProductGroupName		(EnjoyUtils.nullToStr(rs.getString("productGroupName")));
					item.setUnitName				(EnjoyUtils.nullToStr(rs.getString("unitName")));
					item.setQuantity				(EnjoyUtils.convertNumberToDisplay(rs.getString("quantity")));
					item.setProductStatus			(EnjoyUtils.nullToStr(rs.getString("productStatus")));
	            	
	                return item;
	            }
	        });
		    
		    if(result != null && !result.isEmpty()){
		    	productmasterBean = result.get(0);
		    }
			
			
		}catch(Exception e){
			logger.error("[getProductDetailByProductCodeDis] :: ",e);
			throw e;
		}finally{
	        logger.info("[getProductDetailByProductCodeDis][End]");
		}
		
		return productmasterBean;
	}
	
	@Override
	public int countUnitCodeInProduct(String unitCode, String tin) throws Exception{
		logger.info("[countUnitCodeInProduct][Begin]");
		
		StringBuilder       sql     = new StringBuilder();
		int 				result	= 0;
		ArrayList<Object>	params	= new ArrayList<Object>();
		
		try{
			logger.info("[countUnitCodeInProduct] unitCode 		:: " + unitCode);
			logger.info("[countUnitCodeInProduct] tin 			:: " + tin);
			
			sql.append("select count(*) as cou from productmaster where unitCode = ? and tin = ? and productStatus = 'A'");
			
			params.add(unitCode);
			params.add(tin);
			
			logger.info("[countUnitCodeInProduct] sql 			:: " + sql.toString());
			
			result = jdbcTemplate.queryForObject(sql.toString() ,params.toArray(), Integer.class);
			
			logger.info("[countUnitCodeInProduct] result 			:: " + result);
			
		}catch(Exception e){
			logger.error("countUnitCodeInProduct :: ", e);
			throw e;
		}finally{
			logger.info("[countUnitCodeInProduct][End]");
		}
		
		return result;
	}
	
	@Override
	public ArrayList<ProductmasterBean> getProductmasterForBarCode(String productCode, String tin) throws Exception {
		logger.info("[getProductmasterForBarCode][Begin]");
		
		StringBuilder       			sql         		= new StringBuilder();
		ArrayList<ProductmasterBean>	resultList 			= new ArrayList<ProductmasterBean>();
		ArrayList<Object>				params				= new ArrayList<Object>();
		String[]						productCodeArray 	= null;
		String							whereCause			= "";
		
		try{
			logger.info("[getProductmasterForBarCode] productCode 	:: " + productCode);
			logger.info("[getProductmasterForBarCode] tin 			:: " + tin);
			
			productCodeArray = productCode.split(",");
			for(String p:productCodeArray){
				if("".equals(whereCause)){
					whereCause = "?";
				}else{
					whereCause += ",?";
				}
				params.add(p);
			}
			
			params.add(tin);
			
			sql.append("select * from productmaster");
			sql.append("\n	where productCode in (").append(whereCause).append(")");
			sql.append("\n		and tin = ?");
			sql.append("\n		and productStatus = 'A'");
			
			logger.info("[getProductmasterForBarCode] sql :: " + sql);
			
			resultList = (ArrayList<ProductmasterBean>) jdbcTemplate.query(sql.toString(),params.toArray(), new RowMapper<ProductmasterBean>() {
	            @Override
	            public ProductmasterBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ProductmasterBean item = new ProductmasterBean();
	            	
	            	item.setProductCode			(EnjoyUtils.nullToStr(rs.getString("productCode")));
	            	item.setProductCodeDis		(EnjoyUtils.nullToStr(rs.getString("productCodeDis")));
					item.setProductName			(EnjoyUtils.nullToStr(rs.getString("productName")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getProductmasterForBarCode][End]");
		}
		
		return resultList;
	}
	
}
