<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<s:hidden id="productTypeCodeForDelete" name="productTypeCodeForDelete" />
<table class="table table-striped table-bordered table-criteria">
    <thead>
      <tr>
        <th width="5%">ลำดับ</th>
		<th width="20%">รหัสหมวดสินค้า</th>
		<th width="60%">ชื่อหมวดสินค้า</th>
		<th width="15%" style="text-align: center;">
			<img alt="เพิ่ม" title="เพิ่ม" src="<c:url value='/img/Add.png'/>" width="24" height="24" border="0" style="cursor: pointer;" id="btnAdd" onclick="lp_addRowtable();" />
		</th>
      </tr>
    </thead>
    <tbody>
   		<s:set var="seqRow" value="0"/>
   		<s:iterator status="stat" value="productTypeList" var="item">
   			<tr>
				<td style="text-align:center">
					<s:set var="seqRow" value="%{#seqRow+1}"/>
					<s:property value="%{#seqRow}" />
				</td>
				<td>
					<s:if test='!"NEW".equals(#item.rowStatus)'>
						<s:property value="#item.productTypeCodeDis"/>
						<s:hidden id="productTypeCodeDis%{#stat.index}" name="productTypeList[%{#stat.index}].productTypeCodeDis" />
					</s:if>
					<s:else>
						<s:textfield id="productTypeCodeDis%{#stat.index}" name="productTypeList[%{#stat.index}].productTypeCodeDis" cssClass="form-control" maxlength="5" placeholder="รหัสหมวดสินค้า" />
					</s:else>
					<s:hidden id="productTypeCode%{#stat.index}" name="productTypeList[%{#stat.index}].productTypeCode" />
				</td>
				<td>
					<s:textfield id="productTypeName%{#stat.index}" name="productTypeList[%{#stat.index}].productTypeName" cssClass="form-control" maxlength="200" placeholder="ชื่อหมวดสินค้า" />
				</td>
				<td align="center">
					<img alt="ลบ" title="ลบ" src="<c:url value='/img/wrong.png'/>" width="24" height="24" border="0" style="cursor: pointer;" onclick="lp_deleteRecord('<s:property value="%{#stat.index}" />');" />
					<s:hidden id="seq%{#stat.index}" name="productTypeList[%{#stat.index}].seq" value="%{#stat.index}" />
					<s:hidden id="productTypeStatus%{#stat.index}" name="productTypeList[%{#stat.index}].productTypeStatus" />
					<s:hidden id="rowStatus%{#stat.index}" name="productTypeList[%{#stat.index}].rowStatus" />
					<input type="hidden" id='rowIndex<s:property value="#stat.index"/>' name="rowIndex" value='<s:property value="#stat.index"/>'/>
				</td>
			</tr>
		</s:iterator>
	</tbody>
 </table>
