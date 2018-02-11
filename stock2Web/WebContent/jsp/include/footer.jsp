<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<table width="100%" border="0">
	<tr height="26px;">
    	<td align="left" width="75%" style="border: 0px;">
        	<s:if test="!paginatedList.list.isEmpty && paginatedList.totalPage>1 ">
            	<div>
                	<s:if test="pageIndex>1">
						<button type="button" id="i_img_nvt_first" name="i_img_nvt_first" title="First" onclick="changePage('1');" class="btn btn-default">
					    	<span class="glyphicon glyphicon-fast-backward"></span>
					    </button>
					    <button type="button" id="i_img_nvt_prev"  name="i_img_nvt_prev" title="Previous" onclick="changePage('<s:property value="paginatedList.previousPage" />');" class="btn btn-default">
					    	<span class="glyphicon glyphicon-backward"></span>
					    </button>
                    </s:if>
                    <s:else>
                    	<button type="button" id="i_img_nvt_first" name="i_img_nvt_first" title="First" class="btn btn-default disabled">
					    	<span class="glyphicon glyphicon-fast-backward"></span>
					    </button>
					    <button type="button" id="i_img_nvt_prev"  name="i_img_nvt_prev" title="Previous" class="btn btn-default disabled">
					    	<span class="glyphicon glyphicon-backward"></span>
					    </button>
                    </s:else>
                    <s:iterator begin="paginatedList.beginPage" end="paginatedList.endPage" status="stat" step="1" var="pageNumber" >
                         <s:if test="#pageNumber == pageIndex">
                         	<button type="button" style="cursor:default;" class="btn btn-primary btn-sm disabled">
                         		<s:property value="%{(#pageNumber)}"/>
                         	</button>
                         </s:if>
                        <s:else>
                        	<button type="button" class="btn btn-primary btn-sm" onclick="changePage('<s:property value="%{(#pageNumber)}" />');">
                        		<s:property value="%{(#pageNumber)}"/>
                        	</button>
                        </s:else>
                    </s:iterator>
                    <s:if test="paginatedList.endPage>pageIndex">
						<button type="button" id="i_img_nvt_next"  name="i_img_nvt_next" title="Next" onclick="changePage('<s:property value="paginatedList.nextPage" />');" class="btn btn-default">
					    	<span class="glyphicon glyphicon-forward"></span>
					    </button>
					    <button type="button" id="i_img_nvt_last"  name="i_img_nvt_last" title="Last" onclick="changePage('<s:property value="paginatedList.totalPage" />');" class="btn btn-default">
					    	<span class="glyphicon glyphicon-fast-forward"></span>
					    </button>
                    </s:if>
                    <s:else>
                    	<button type="button" id="i_img_nvt_next"  name="i_img_nvt_next" title="Next" class="btn btn-default disabled">
					    	<span class="glyphicon glyphicon-forward"></span>
					    </button>
					    <button type="button" id="i_img_nvt_last"  name="i_img_nvt_last" title="Last" class="btn btn-default disabled">
					    	<span class="glyphicon glyphicon-fast-forward"></span>
					    </button>
                    </s:else>
               	</div>
               </s:if>
           </td>
           <td align="right" width="25%" style="border: 0px;">
               <s:if test="!paginatedList.list.isEmpty && paginatedList.totalPage>0 ">
               	<span style="top: -3px;">
               		จำนวน&nbsp;<s:property value="paginatedList.fullListSize" />
               	</span>
               	<span style="top: -3px;">&nbsp;รายการ&nbsp;&nbsp;</span>
               	<span class="c_field_label" style="top:-5px;">หน้า&nbsp;<s:property value="pageIndex" />/<s:property value="paginatedList.totalPage" /></span>
               </s:if>
           </td>
       </tr>
   </table>