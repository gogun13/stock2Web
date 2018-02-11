<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<table class="table table-striped table-bordered table-criteria">
    <thead>
      <tr>
        <th width="5%">ลำดับ</th>
		<th width="20%">รหัสสินค้า</th>
		<th width="65%">ชื่อสินค้า</th>
		<th width="10%">Action</th>
      </tr>
    </thead>
    <tbody>
    	<s:if test="!productListForBarCode.isEmpty">
    		<s:iterator status="stat" value="productListForBarCode" var="item">
	    		<tr>
					<td style="text-align:center"><s:property value="%{#stat.index+1}" /></td>
					<td>
						<s:property value="#item.productCodeDis"/>
						<s:hidden id="productCodeDis%{#stat.index}" name="productListForBarCode[%{#stat.index}].productCodeDis" />
						<s:hidden id="productCode%{#stat.index}" name="productListForBarCode[%{#stat.index}].productCode" />
					</td>
					<td>
						<s:property value="#item.productName"/>
						<s:hidden id="productName%{#stat.index}" name="productListForBarCode[%{#stat.index}].productName" />
					</td>
					<td align="center">
						<img alt="ลบ" title="ลบ" src="<c:url value='/img/wrong.png'/>" width="24" height="24" border="0" style="cursor: pointer;" onclick="lp_deleteRecord('<s:property value="%{#stat.index}" />');" />
						<s:hidden id="seq%{#stat.index}" name="productListForBarCode[%{#stat.index}].seq" value="%{#stat.index}" />
						<input type="hidden" id='rowIndex<s:property value="#stat.index"/>' name="rowIndex" value='<s:property value="#stat.index"/>'/>
					</td>
				</tr>
			</s:iterator>
    	</s:if>
    	<s:else>
    		<tr>
				<td colspan="4" style="text-align:center">ไม่พบข้อมูล</td>
			</tr>
    	</s:else>
	</tbody>
 </table>