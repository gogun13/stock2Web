<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<table class="table table-hover table-bordered table-criteria">
    <thead>
      <tr>
        <th width="5%" >ลำดับ</th>
		<th width="15%">เลขที่ใบสั่งของ</th>
		<th width="35%">บริษัทสั่งซื้อ</th>
		<th width="10%">วันที่สั่งของ</th>
		<th width="20%">ผู้บันทึก</th>
		<th width="15%">สถานะใบสั่งซื้อ</th> 
      </tr>
    </thead>
    <tbody>
    	<s:if test="!paginatedList.list.isEmpty">
    		<s:iterator status="stat" value="paginatedList.list" var="item">
	    		<tr onclick="lp_gotoDetail('<s:property value="#item.reciveNo"/>');">
					<td style="text-align:center"><s:property value="%{(#stat.index+1) + (paginatedList.pageSize * (paginatedList.index-1))}" /></td>
					<td><s:property value="#item.reciveNo"/></td>
					<td><s:property value="#item.vendorName"/></td>
					<td><s:property value="#item.reciveDate"/></td>
					<td><s:property value="#item.usrName"/></td>
					<td><s:property value="#item.reciveStatusDesc"/></td>
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