<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<s:if test="paginatedList!=null">
	<table class="table table-striped table-bordered table-criteria">
	    <thead>
	      <tr>
	        <th width="5%" >ลำดับ</th>
			<th width="15%">วันที่ปรับ</th>
			<th width="20%">ค่าเดิม</th>
			<th width="20%">ค่าใหม่</th>
			<th width="40%">หมายเหตุ</th>
	      </tr>
	    </thead>
	    <tbody>
	    	<s:if test="!paginatedList.list.isEmpty">
	    		<s:iterator status="stat" value="paginatedList.list" var="item">
		    		<tr>
						<td style="text-align:center"><s:property value="%{(#stat.index+1) + (paginatedList.pageSize * (paginatedList.index-1))}" /></td>
						<td><s:property value="#item.adjustDate"/></td>
						<td><s:property value="#item.quanOld"/></td>
						<td><s:property value="#item.quanNew"/></td>
						<td><s:property value="#item.remark"/></td>
					</tr>
				</s:iterator>
	    	</s:if>
	    	<s:else>
	    		<tr>
					<td colspan="5" style="text-align:center">ไม่พบข้อมูล</td>
				</tr>
	    	</s:else>
		</tbody>
	   	<tfoot>
			<tr height="26px;">
				<td colspan="5">
					<%@include file="../include/footer.jsp" %>
				</td>
			</tr>
		</tfoot>
	 </table>
 </s:if>