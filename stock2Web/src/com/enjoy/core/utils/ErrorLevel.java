/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.enjoy.core.utils;

import java.io.Serializable;

public enum ErrorLevel implements Serializable{
    SYSTEM,             /*System Error, have to terminate*/
    BUSINESS,           /*Business logic Error, depend on handling*/
    BUSINESS_UNHANDLE,  /*Business logic Error, have to terminate*/
    NO_PERMISSION,      /*Business logic Error, have to terminate*/
    INTERFACE,          /*Error from interfacing between distributed systems*/
    INVALID_PARAM;          /*Error from interfacing between distributed systems*/
}
