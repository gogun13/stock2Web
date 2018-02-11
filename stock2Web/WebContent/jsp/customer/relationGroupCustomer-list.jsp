<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<s:hidden id="cusGroupCodeForDelete" name="cusGroupCodeForDelete" />
<table class="table table-striped table-bordered table-criteria">
    <thead>
      <tr>
        <th width="5%">ลำดับ</th>
		<th width="40%">ชื่อกลุ่มลูกค้า<span style="color: red;"><b>*</b></span></th>
		<th width="40%">ใช้ราคาสินค้า<span style="color: red;"><b>*</b></span></th>
		<th width="15%" style="text-align: center;">
			<img alt="เพิ่ม" title="เพิ่ม" src="<c:url value='/img/Add.png'/>" width="24" height="24" border="0" style="cursor: pointer;" id="btnAdd" onclick="lp_addRowtable();" />
		</th>
      </tr>
    </thead>
    <tbody>
   		<s:set var="seqRow" value="0"/>
   		<s:iterator status="stat" value="relationGroupCustomerList" var="item">
   			<tr>
				<td style="text-align:center">
					<s:set var="seqRow" value="%{#seqRow+1}"/>
					<s:property value="%{#seqRow}" />
				</td>
				<td>
					<s:textfield id="cusGroupName%{#stat.index}" name="relationGroupCustomerList[%{#stat.index}].cusGroupName" cssClass="form-control" maxlength="250" placeholder="ชื่อกลุ่มลูกค้า" />
				</td>
				<td>
					<s:select id="groupSalePrice%{#stat.index}" name="relationGroupCustomerList[%{#stat.index}].groupSalePrice" cssClass="form-control"
		                      list="groupSalePriceCombo" listKey="code" listValue="desc" />
				</td>
				<td align="center">
					<img alt="ลบ" title="ลบ" src="<c:url value='/img/wrong.png'/>" width="24" height="24" border="0" style="cursor: pointer;" onclick="lp_deleteRecord('<s:property value="%{#stat.index}" />');" />
					<s:hidden id="seq%{#stat.index}" name="relationGroupCustomerList[%{#stat.index}].seq" value="%{#stat.index}" />
					<s:hidden id="cusGroupStatus%{#stat.index}" name="relationGroupCustomerList[%{#stat.index}].cusGroupStatus" />
					<s:hidden id="rowStatus%{#stat.index}" name="relationGroupCustomerList[%{#stat.index}].rowStatus" />
					<input type="hidden" id='rowIndex<s:property value="#stat.index"/>' name="rowIndex" value='<s:property value="#stat.index"/>'/>
					<s:hidden id="cusGroupCode%{#stat.index}" name="relationGroupCustomerList[%{#stat.index}].cusGroupCode" />
				</td>
			</tr>
		</s:iterator>
	</tbody>
 </table>
