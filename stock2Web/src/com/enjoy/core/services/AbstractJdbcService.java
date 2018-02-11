package com.enjoy.core.services;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.enjoy.core.utils.EnjoyUtils;

public abstract class AbstractJdbcService{
	@Autowired
    public JdbcTemplate jdbcTemplate;
	private static final Logger log = Logger.getLogger(AbstractJdbcService.class);
	
	public static final int TYPE_STRING    = 1;
	public static final int TYPE_INT       = 2;
	public static final int TYPE_DATE      = 3;
	public static final int TYPE_SYS_DATE  = 4;
	public static final int TYPE_DOUBLE  	= 5;
    private static final String PRE_COUNT_SQL = "select count(*) from (";
    private static final String SUF_COUNT_SQL = ") c ";
	
	public int executeStatement(final String sql, final ArrayList<Object>   paramList, final ArrayList<Integer>  colTypeList){
        int row = jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int i = 1;
                int j = 0;
                
                try{
                	for(Object obj:paramList){
                        if(colTypeList.get(j)==TYPE_STRING){
                            ps.setString(i++, obj.toString());
                        }else if(colTypeList.get(j)==TYPE_DATE){
                            ps.setTimestamp(i++, EnjoyUtils.toTimeStamp(EnjoyUtils.dateTimeDisplayToDb(obj.toString())));
                        }else if(colTypeList.get(j)==TYPE_INT){
                            ps.setInt(i++, EnjoyUtils.parseInt(obj.toString()));
                        }else if(colTypeList.get(j)==TYPE_DOUBLE){
                            ps.setDouble(i++, EnjoyUtils.parseDouble(obj.toString()));
                        }
//                        else if(colTypeList.get(j)==TYPE_SYS_DATE){
//                            ps.setTimestamp(i++, EnjoyUtils.toTimeStamp(EnjoyUtils.getCurrentDateNoTime()));
//                        }
                        
                        j++;
                    }
                }catch(SQLException e){
                	log.error("[executeStatement] :: ",e);
                	throw e;
                }catch(Exception e){
                	log.error("[executeStatement] :: ",e);
                	throw new SQLException("Other Exception !!");
                }
           }
        });
        
        return row;
    }
	
	public void setColumnStr(String colName, String colData, int columnType, StringBuilder columnStr, StringBuilder args, ArrayList<Object> paramList, ArrayList<Integer> colTypeList){
        
        try{
            log.info("[setColumnStr] " + colName + " :: " + colData);
            
            if(EnjoyUtils.chkNull(colData) || columnType==TYPE_SYS_DATE){
            	
            	if(columnStr.toString().equals("")){
                    columnStr.append(colName);
                }else{
                    columnStr.append(", ").append(colName);
                }
            	
            	if(columnType==TYPE_SYS_DATE){
            		if(args.toString().equals("")){
	                    args.append("sysdate()");
	                }else{
	                    args.append(",sysdate()");
	                }
            	}else{
	                if(args.toString().equals("")){
	                    args.append("?");
	                }else{
	                    args.append(",?");
	                }
            	}
            	
                paramList.add(colData);
                colTypeList.add(columnType);
            }
            
        }catch(Exception e){
        	log.error("[setColumnStr] :: ",e);
        }
    }
	
	public String decorateRowNumSQL(String sql, int pageIndex, int pageSize) {
        int startRow 	= getStartRow(pageIndex, pageSize);
        int endRow 		= (pageIndex) * pageSize;

//        For DB2
        StringBuilder bf = new StringBuilder();
        bf.append("select t1.*");
        bf.append("\n	from(");
        bf.append("\n		select @rownum := @rownum + 1 rowNum,t.*");
        bf.append("\n			FROM(");
        bf.append("\n				").append(sql);
        bf.append("\n					) t, (SELECT @rownum := 0) r");
        bf.append("\n		) t1");
        bf.append("\n	where t1.rowNum BETWEEN ").append(startRow).append(" and ").append(endRow);
        
        
        log.info("sql:" + sql.toString());
        
        return bf.toString();
    }
	
	public int getStartRow(int pageIndex, int pageSize) {
        return ((pageIndex - 1) * pageSize) + 1;
    }
	
	public int countTotal(String sql) {
        int count = jdbcTemplate.queryForObject(PRE_COUNT_SQL + sql + SUF_COUNT_SQL, Integer.class);

        log.info("count:" + count);
        
        return count;
    }

    public int countTotal(String sql, Object[] params) {
        int count = 0;
        if (params != null && params.length > 0) {
            count = jdbcTemplate.queryForObject(PRE_COUNT_SQL + sql + SUF_COUNT_SQL, params, Integer.class);
        } else {
            count = countTotal(sql);
        }
        
        log.info("count:" + count);
        
        return count;
    }
}
