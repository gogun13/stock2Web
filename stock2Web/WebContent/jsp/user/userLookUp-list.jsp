<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<table class="table table-hover table-bordered table-criteria">
    <thead>
      <tr>
        <th width="5%">ลำดับ</th>
		<th width="50%">ชื่อ-นามสกุล</th>
		<th width="45%">E-mail</th>
      </tr>
    </thead>
    <tbody>
    	<s:if test="!paginatedListForLookUp.list.isEmpty">
    		<s:iterator status="stat" value="paginatedListForLookUp.list" var="item">
	    		<tr onclick="lp_returnData('<s:property value="#item.userUniqueId"/>');">
					<td style="text-align:center"><s:property value="%{(#stat.index+1) + (paginatedListForLookUp.pageSize * (paginatedListForLookUp.index-1))}" /></td>
					<td><s:property value="#item.userName"/></td>
					<td><s:property value="#item.userEmail"/></td>
				</tr>
			</s:iterator>
    	</s:if>
    	<s:else>
    		<tr>
				<td colspan="3" style="text-align:center">ไม่พบข้อมูล</td>
			</tr>
    	</s:else>
	</tbody>
   	<tfoot>
		<tr height="26px;">
			<td colspan="3">
				<%@include file="../include/footerLookUp.jsp" %>
			</td>
		</tr>
	</tfoot>
 </table>