/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enjoy.core.exception;

import java.io.Serializable;
import java.util.HashMap;

import com.enjoy.core.utils.ErrorLevel;


public class SessionException extends Exception implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ErrorLevel errorLevel;
    private String subCode;
    private int sqlErrorCode;
    private String sqlErrorState;
    private String errorKey;
    private Object[] errorKeyParams = new Object[1];
    private boolean plainMessage;

    public ErrorLevel getErrorLevel() {
        return errorLevel;
    }

    public void setErrorLevel(ErrorLevel errorLevel) {
        this.errorLevel = errorLevel;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public int getSqlErrorCode() {
        return sqlErrorCode;
    }

    public void setSqlErrorCode(int sqlErrorCode) {
        this.sqlErrorCode = sqlErrorCode;
    }

    public String getSqlErrorState() {
        return sqlErrorState;
    }

    public void setSqlErrorState(String sqlErrorState) {
        this.sqlErrorState = sqlErrorState;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public void setErrorKey(String errorKey) {
        this.errorKey = errorKey;
    }

    public Object[] getErrorKeyParams() {
        return errorKeyParams;
    }

    public void setErrorKeyParams(Object[] errorKeyParams) {
        this.errorKeyParams = errorKeyParams;
    }

    public boolean isPlainMessage() {
        return plainMessage;
    }

    public void setPlainMessage(boolean plainMessage) {
        this.plainMessage = plainMessage;
    }


    // to handle additional info
    private HashMap<String, Object> info = new HashMap();
    
    public SessionException() {
    }
    
    public SessionException(String message) {
        super(message);
        setPlainMessage(true);
    }

    public HashMap<String, Object> getInfo() {
        return info;
    }

    public void setInfo(HashMap<String, Object> info) {
        this.info = info;
    }
    
}
