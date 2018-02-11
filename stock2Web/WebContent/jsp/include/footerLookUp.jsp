<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<table width="100%" border="0">
	<tr height="26px;">
    	<td align="left" width="70%" style="border: 0px;">
        	<s:if test="!paginatedListForLookUp.list.isEmpty && paginatedListForLookUp.totalPage>1 ">
            	<div style="font-size: 0.75em !important;">
                	<s:if test="pageIndex>1">
						<button type="button" id="i_img_nvt_first" name="i_img_nvt_first" title="First" onclick="changePageLookUp('1');" class="btn sm btn-default">
					    	<span class="glyphicon glyphicon-fast-backward"></span>
					    </button>
					    <button type="button" id="i_img_nvt_prev"  name="i_img_nvt_prev" title="Previous" onclick="changePageLookUp('<s:property value="paginatedListForLookUp.previousPage" />');" class="btn btn-default">
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
                    <s:iterator begin="paginatedListForLookUp.beginPage" end="paginatedListForLookUp.endPage" status="stat" step="1" var="pageNumber" >
                         <s:if test="#pageNumber == pageIndex">
                         	<button type="button" style="cursor:default;" class="btn btn-primary btn-sm disabled">
                         		<s:property value="%{(#pageNumber)}"/>
                         	</button>
                         </s:if>
                        <s:else>
                        	<button type="button" class="btn btn-primary btn-sm" onclick="changePageLookUp('<s:property value="%{(#pageNumber)}" />');">
                        		<s:property value="%{(#pageNumber)}"/>
                        	</button>
                        </s:else>
                    </s:iterator>
                    <s:if test="paginatedListForLookUp.endPage>pageIndex">
						<button type="button" id="i_img_nvt_next"  name="i_img_nvt_next" title="Next" onclick="changePageLookUp('<s:property value="paginatedListForLookUp.nextPage" />');" class="btn btn-default">
					    	<span class="glyphicon glyphicon-forward"></span>
					    </button>
					    <button type="button" id="i_img_nvt_last"  name="i_img_nvt_last" title="Last" onclick="changePageLookUp('<s:property value="paginatedListForLookUp.totalPage" />');" class="btn btn-default">
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
           <td align="right" width="30%" style="border: 0px;">
               <s:if test="!paginatedListForLookUp.list.isEmpty && paginatedListForLookUp.totalPage>0 ">
               	<span style="top: -3px;">
               		จำนวน&nbsp;<s:property value="paginatedListForLookUp.fullListSize" />
               	</span>
               	<span style="top: -3px;">&nbsp;รายการ&nbsp;&nbsp;</span>
               	<span class="c_field_label" style="top:-5px;">หน้า&nbsp;<s:property value="pageIndex" />/<s:property value="paginatedListForLookUp.totalPage" /></span>
               </s:if>
           </td>
       </tr>
   </table>