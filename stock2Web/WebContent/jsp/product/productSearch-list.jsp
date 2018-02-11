<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<table class="table table-striped table-bordered table-criteria">
    <thead>
      <tr>
        <th width="5%">ลำดับ</th>
		<th width="15%">หมวดสินค้า</th>
		<th width="15%">หมู่สินค้า</th> 
		<th width="12%">รหัสสินค้า</th> 
		<th width="25%">ชื่อสินค้า</th>
		<th width="14%">ราคาขายสินค้า</th>
		<th width="7%" style="text-align: center;">
			แก้ไข
		</th>
		<th width="7%" style="text-align: center;">
			บาร์โค้ด
		</th>
      </tr>
    </thead>
    <tbody>
    	<s:if test="!paginatedList.list.isEmpty">
    		<s:iterator status="stat" value="paginatedList.list" var="item">
	    		<tr>
					<td style="text-align:center"><s:property value="%{(#stat.index+1) + (paginatedList.pageSize * (paginatedList.index-1))}" /></td>
					<td><s:property value="#item.productTypeName"/></td>
					<td><s:property value="#item.productGroupName"/></td>
					<td><s:property value="#item.productCodeDis"/></td>
					<td><s:property value="#item.productName"/></td>
					<td style="text-align: right;">
						<s:property value="#item.salePrice1"/>
					</td>
					<td style="padding: 0px;text-align: center;">
						<a href="javascript:void(0);" style="color: #504f4f;" title="แก้ไข" onclick="lp_gotoDetail('<s:property value="#item.productCode"/>');">
				          <span class="glyphicon glyphicon-edit btn-lg"></span>
				        </a>
					</td>
					<td style="padding: 0px;text-align: center;">
				        <a href="javascript:void(0);" style="color: #504f4f;" title="เพิ่มไปยังพิมพ์รหัสสินค้า" onclick="lp_setBarCodeForPrint('<s:property value="#item.productCode"/>');">
				          <span class="glyphicon glyphicon-barcode btn-lg"></span>
				        </a>
					</td>
				</tr>
			</s:iterator>
    	</s:if>
    	<s:else>
    		<tr>
				<td colspan="8" style="text-align:center">ไม่พบข้อมูล</td>
			</tr>
    	</s:else>
	</tbody>
   	<tfoot>
		<tr height="26px;">
			<td colspan="8">
				<%@include file="../include/footer.jsp" %>
			</td>
		</tr>
	</tfoot>
 </table>