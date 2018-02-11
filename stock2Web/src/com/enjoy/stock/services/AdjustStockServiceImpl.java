package com.enjoy.stock.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.services.AbstractJdbcService;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.AdjustStockBean;

@Repository
public class AdjustStockServiceImpl extends AbstractJdbcService implements AdjustStockService{
	
	private static final Logger logger = Logger.getLogger(AdjustStockServiceImpl.class);
	
	@Override
	public void getAdjustHistoryList(PaginatedListBean paginatedList,AdjustStockBean adjustStockBean) throws Exception {
		logger.info("[getAdjustHistoryList][Begin]");
		
		StringBuilder       		sql         = new StringBuilder();
		ArrayList<AdjustStockBean>	resultList 	= new ArrayList<AdjustStockBean>();
		ArrayList<Object>			params		= new ArrayList<Object>();
		
		try{	
			
			sql.append("select *");
			sql.append("\n	from adjusthistory");
			sql.append("\n	where productCode 	= ?");
			sql.append("\n		and tin 		= ?");
			sql.append("\n	order by adjustDate desc, adjustNo desc");
			
			params.add(adjustStockBean.getProductCode());
			params.add(adjustStockBean.getTin());
			
			logger.info("[getAdjustHistoryList] sql :: " + sql);
			
			String rowNumSql = decorateRowNumSQL(sql.toString(), paginatedList.getIndex(), paginatedList.getPageSize());
			
			resultList = (ArrayList<AdjustStockBean>) jdbcTemplate.query(rowNumSql, params.toArray(), new RowMapper<AdjustStockBean>() {
	            @Override
	            public AdjustStockBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	AdjustStockBean item = new AdjustStockBean();
	            	
	            	item.setAdjustNo			(EnjoyUtils.nullToStr(rs.getString("adjustNo")));
					item.setAdjustDate			(EnjoyUtils.dateToThaiDisplay(rs.getString("adjustDate")));
					item.setProductCode			(EnjoyUtils.nullToStr(rs.getString("productCode")));
					item.setQuanOld				(EnjoyUtils.convertFloatToDisplay(rs.getString("quanOld"), 2));
					item.setQuanNew				(EnjoyUtils.convertFloatToDisplay(rs.getString("quanNew"), 2));
					item.setRemark				(EnjoyUtils.nullToStr(rs.getString("remark")));
	            	
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
	public void insertAdjustHistory(AdjustStockBean adjustStockBean) throws Exception {
		logger.info("[insertAdjustHistory][Begin]");
        
        StringBuilder       sql         = new StringBuilder();
        StringBuilder       columns     = new StringBuilder("");
        StringBuilder       args        = new StringBuilder("");
        ArrayList<Object>   paramList   = new ArrayList<Object>();
        ArrayList<Integer>  colTypeList = new ArrayList<Integer>();
        int                 row         = 0;
        
        try{
        	setColumnStr("adjustNo", "", TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("adjustDate", EnjoyUtils.currDateThai(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("productCode", adjustStockBean.getProductCode(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("quanOld", adjustStockBean.getQuanOld(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("quanNew", adjustStockBean.getQuanNew(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("remark", adjustStockBean.getRemark(), TYPE_STRING, columns, args, paramList, colTypeList);
        	setColumnStr("tin", adjustStockBean.getTin(), TYPE_STRING, columns, args, paramList, colTypeList);

            sql.append("INSERT INTO  adjusthistory ( ");
            sql.append(columns.toString());
            sql.append(" ) ");
            sql.append(" VALUES (");
            sql.append(args.toString());
            sql.append(") ");

            logger.info("[insertProducGroup] sql :: " + sql.toString());
            logger.info("[insertProducGroup] paramList :: " + paramList.size());
            logger.info("[insertProducGroup] colTypeList :: " + colTypeList.size());

            row = executeStatement(sql.toString(), paramList, colTypeList);
            
            logger.info("[insertProducGroup] row :: " + row);
            
        }catch(Exception e){
            logger.error("[insertProducGroup] :: ",e);
            throw e;
        }finally{
            logger.info("[insertProductype][End]");
        }
	}
}
