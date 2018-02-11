<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
	$(document).ready(function(){
		lp_displayChkSelect();
	});
</script>
<table class="table table-striped table-bordered table-criteria">
    <thead>
      <tr>
        <th width="5%" style="text-align: center;">
        	<input type="checkbox" id="lookUpChkAll" name="lookUpChkAll" value="" onclick="lp_lookUpChkAll();" />
        </th>
		<th width="20%">รหัสสินค้า</th> 
		<th width="45%">ชื่อสินค้า</th>
		<th width="13%">ราคาขายสินค้า</th>
		<th width="17%">ปริมาณสินค้าคงเหลือ</th>
      </tr>
    </thead>
    <tbody>
    	<s:if test="!paginatedListForLookUp.list.isEmpty">
    		<s:iterator status="stat" value="paginatedListForLookUp.list" var="item">
	    		<tr>
					<td style="text-align:center">
						<input type="checkbox" id="lookUpChkSelect<s:property value="#stat.index"/>" name="lookUpChkSelect" onclick="lp_lookUpChkSelect();" value='<s:property value="#item.productCode"/>' />
					</td>
					<td style="text-align: center;"><s:property value="#item.productCodeDis"/></td>
					<td style="text-align: left;"><s:property value="#item.productName"/></td>
					<td style="text-align: right;"><s:property value="#item.salePrice1"/></td>
					<td style="text-align: right;"><s:property value="#item.quantity"/>&nbsp;<s:property value="#item.unitName"/></td>
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
				<%@include file="../include/footerLookUp.jsp" %>
			</td>
		</tr>
	</tfoot>
 </table>