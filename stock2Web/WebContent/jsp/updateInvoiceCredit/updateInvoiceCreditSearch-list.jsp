<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<table class="table table-hover table-bordered table-criteria">
    <thead>
      <tr>
        <th width="5%" >ลำดับ</th>
		<th width="15%">เลขที่ใบเสร็จ</th>
		<th width="15%">ประเภทการขาย</th>
		<th width="20%">ลูกค้า</th>
		<th width="15%">วันที่ขาย</th>
		<th width="15%">จำนวนเงิน</th>
		<th width="15%">หมายเหต</th>
      </tr>
    </thead>
    <tbody>
    	<s:if test="!paginatedList.list.isEmpty">
    		<s:iterator status="stat" value="paginatedList.list" var="item">
	    		<tr onclick="lp_gotoDetail('<s:property value="#item.invoiceCash"/>');">
					<td style="text-align:center"><s:property value="%{(#stat.index+1) + (paginatedList.pageSize * (paginatedList.index-1))}" /></td>
					<td><s:property value="#item.invoiceCode"/></td>
					<td><s:property value="#item.invoiceTypeDesc"/></td>
					<td><s:property value="#item.cusFullName"/></td>
					<td><s:property value="#item.invoiceDate"/></td>
					<td align="right"><s:property value="#item.invoiceTotal"/></td>
					<td><s:property value="#item.remark"/></td>
				</tr>
			</s:iterator>
    	</s:if>
    	<s:else>
    		<tr>
				<td colspan="7" style="text-align:center">ไม่พบข้อมูล</td>
			</tr>
    	</s:else>
	</tbody>
   	<tfoot>
		<tr height="26px;">
			<td colspan="7">
				<%@include file="../include/footer.jsp" %>
			</td>
		</tr>
	</tfoot>
 </table>