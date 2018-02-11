<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<table class="table table-hover table-bordered table-criteria">
    <thead>
      <tr>
        <th width="5%">ลำดับ</th>
		<th width="15%">ชื่อ-นามสกุล</th>
		<th width="35%">ที่อยู่</th> 
		<th width="15%">เบอร์โทร</th> 
		<th width="15%">สถานะ</th>
		<th width="15%">หมายเหตุ</th>
      </tr>
    </thead>
    <tbody>
    	<s:if test="!paginatedList.list.isEmpty">
    		<s:iterator status="stat" value="paginatedList.list" var="item">
	    		<tr onclick="lp_gotoDetail('<s:property value="#item.cusCode"/>');">
					<td style="text-align:center"><s:property value="%{(#stat.index+1) + (paginatedList.pageSize * (paginatedList.index-1))}" /></td>
					<td><s:property value="#item.fullName"/></td>
					<td><s:property value="#item.address"/></td>
					<td><s:property value="#item.tel"/></td>
					<td><s:property value="#item.customerStatusName"/></td>
					<td><s:property value="#item.remark"/></td>
				</tr>
			</s:iterator>
    	</s:if>
    	<s:else>
    		<tr>
				<td colspan="6" style="text-align:center">ไม่พบข้อมูล</td>
			</tr>
    	</s:else>
	</tbody>
   	<tfoot>
		<tr height="26px;">
			<td colspan="6">
				<%@include file="../include/footer.jsp" %>
			</td>
		</tr>
	</tfoot>
 </table>