package com.enjoy.stock.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.enjoy.core.services.AbstractJdbcService;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.ProductQuanHistoryBean;

@Repository
public class ProductQuanHistoryServiceImpl extends AbstractJdbcService implements ProductQuanHistoryService{
	
	private static final Logger logger = Logger.getLogger(ProductQuanHistoryServiceImpl.class);
	
	@Override
	public ArrayList<ProductQuanHistoryBean> searchByCriteria(ProductQuanHistoryBean criteria) throws Exception{
		logger.info("[searchByCriteria][Begin]");
		
		StringBuilder       				sql         = new StringBuilder();
		ArrayList<ProductQuanHistoryBean>	resultList 	= new ArrayList<ProductQuanHistoryBean>();
		ArrayList<Object>					params		= new ArrayList<Object>();
		
		try{	
			sql.append("select  a.formRef, a.hisDate, b.productName, a.quantityPlus, a.quantityMinus, a.quantityTotal");
			sql.append("\n	from productquanhistory a");
			sql.append("\n		inner JOIN productmaster b on a.productCode = b. productCode and a.tin = b.tin");
			sql.append("\n		inner JOIN productgroup c on c.productTypeCode = a.productType and a.productGroup = c.productGroupCode and a.tin = c.tin");
			sql.append("\n		inner JOIN productype d on a.productType = d.productTypeCode and a.tin = d.tin");
			sql.append("\n	where a.tin = ?");
			sql.append("\n		and a.hisDate >= STR_TO_DATE(?	, '%Y%m%d')");
			sql.append("\n		and a.hisDate <= STR_TO_DATE(?	, '%Y%m%d')");
			
			params.add(criteria.getTin());
			params.add(EnjoyUtils.dateThaiToDb(criteria.getHisDateFrom()));
			params.add(EnjoyUtils.dateThaiToDb(criteria.getHisDateTo()));
			
			if(EnjoyUtils.chkNull(criteria.getProductName())){
				sql.append("\n		and b.productName = ?");
				params.add(criteria.getProductName());
			}
			
			if(EnjoyUtils.chkNull(criteria.getProductGroupName())){
				sql.append("\n		and c.productGroupName = ?");
				params.add(criteria.getProductGroupName());
			}
			
			if(EnjoyUtils.chkNull(criteria.getProductTypeName())){
				sql.append("\n		and d.productTypeName = ?");
				params.add(criteria.getProductTypeName());
			}
			
			logger.info("[searchByCriteria] sql :: " + sql);
			
			resultList = (ArrayList<ProductQuanHistoryBean>) jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<ProductQuanHistoryBean>() {
	            @Override
	            public ProductQuanHistoryBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	ProductQuanHistoryBean item = new ProductQuanHistoryBean();
	            	
	            	item.setFormRef			(EnjoyUtils.nullToStr(rs.getString("formRef")));
	            	item.setHisDate			(EnjoyUtils.dateToThaiDisplay(rs.getString("hisDate")));
	            	item.setProductName		(EnjoyUtils.nullToStr(rs.getString("productName")));
	            	item.setQuantityPlus	(EnjoyUtils.convertNumberToDisplay(rs.getString("quantityPlus")));
	            	item.setQuantityMinus	(EnjoyUtils.convertNumberToDisplay(rs.getString("quantityMinus")));
	            	item.setQuantityTotal	(EnjoyUtils.convertNumberToDisplay(rs.getString("quantityTotal")));
	            	
	                return item;
	            }
	        });
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[searchByCriteria][End]");
		}
		
		return resultList;
		
	}
	
	@Override
	public void insert(ProductQuanHistoryBean bean, int hisCode) throws Exception {
		logger.info("[insert][Begin]");
        
        StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
        
        try{
        	setColumnStr("hisCode", EnjoyUtils.nullToStr(hisCode), TYPE_INT, columns, args, paramList, colTypeList);
        	setColumnStr("tin", bean.getTin(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("formRef", bean.getFormRef(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("hisDate", EnjoyUtils.currDateThai(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("productType", bean.getProductType(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("productGroup", bean.getProductGroup(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("productCode", bean.getProductCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("quantityPlus", bean.getQuantityPlus(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("quantityMinus", bean.getQuantityMinus(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("quantityTotal", bean.getQuantityTotal(), TYPE_STRING, columns, args, paramList, colTypeList);

            sql.append("INSERT INTO  productquanhistory ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insert] sql :: " + sql.toString());
            logger.info("[insert] paramList :: " + paramList.size());
            logger.info("[insert] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insert] row :: " + row);
            
        }catch(Exception e){
            logger.error("[insert] :: ",e);
            throw e;
        }finally{
            logger.info("[insert][End]");
        }
	}
	
	@Override
	public int genId(String tin) throws Exception {
		logger.info("[genId][Begin]");
		
		StringBuilder 		sql 		= new StringBuilder();
		int 				result		= 1;
		ArrayList<Integer>	resultList 	= new ArrayList<Integer>();
		
		try{
			
			sql.append("select (max(hisCode) + 1) newId from productquanhistory where tin = ?");
			
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
}
