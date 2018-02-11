<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#frm");
	});
</script>
<table class="table table-striped table-bordered table-criteria">
    <thead>
      <tr>
        <th width="5%" >ลำดับ</th>
		<th width="22%">สินค้า<span style="color: red;">*</span></th>
		<th width="12%">หน่วย</th>
		<th width="12%">เหลือในคลัง(เดิม)</th>
		<th width="12%">เพิ่ม/ลด สินค้า <span style="color: red;">*</span></th>
		<th width="12%">เหลือในคลัง(ใหม่)</th>
		<th width="16%">หมายเหตุ</th>
		<th width="9%">Action</th>
      </tr>
    </thead>
    <tbody>
   		<s:set var="seqRow" value="0"/>
   		<s:iterator status="stat" value="adjustStockList" var="item">
   			<tr>
				<td style="text-align:center">
					<s:set var="seqRow" value="%{#seqRow+1}"/>
					<s:property value="%{#seqRow}" />
				</td>
				<td>
					<s:textfield id="productName%{#stat.index}" name="adjustStockList[%{#stat.index}].productName" cssClass="form-control auto-complete" onblur="lp_getProductDetailByName('%{#stat.index}');" placeholder="สินค้า" />
					<script>$("#productName" + '<s:property value="%{#stat.index}" />').focus();</script>
				</td>
				<td>
					<s:textfield id="unitName%{#stat.index}" name="adjustStockList[%{#stat.index}].unitName" cssClass="form-control input-disabled" readonly="true" placeholder="หน่วย" />
				</td>
				<td>
					<s:textfield id="quanOld%{#stat.index}" name="adjustStockList[%{#stat.index}].quanOld" cssClass="form-control numberOnly input-disabled" readonly="true" placeholder="เหลือในคลัง(เดิม)" />
				</td>
				<td>
					<s:textfield id="quanNew%{#stat.index}" name="adjustStockList[%{#stat.index}].quanNew" cssClass="form-control numberOnly" onblur="lp_calQuantity('%{#stat.index}');" placeholder="เพิ่ม/ลด สินค้า" />
				</td>
				<td>
					<s:textfield id="quantity%{#stat.index}" name="adjustStockList[%{#stat.index}].quantity" cssClass="form-control numberOnly input-disabled" readonly="true" placeholder="เหลือในคลัง(ใหม่)" />
				</td>
				<td>
					<s:textfield id="remark%{#stat.index}" name="adjustStockList[%{#stat.index}].remark" cssClass="form-control" placeholder="หมายเหตุ" maxlength="500" />
				</td>
				<td align="center">
					<img alt="ลบ" title="ลบ" src="<c:url value='/img/wrong.png'/>" width="24" height="24" border="0" style="cursor: pointer;" onclick="lp_deleteRecord('<s:property value="%{#stat.index}" />');" />
					<s:hidden id="seq%{#stat.index}" name="adjustStockList[%{#stat.index}].seq" value="%{#stat.index}" />
					<s:hidden id="rowStatus%{#stat.index}" name="adjustStockList[%{#stat.index}].rowStatus" />
					<s:hidden id="productCode%{#stat.index}" name="adjustStockList[%{#stat.index}].productCode" />
					<input type="hidden" id='rowIndex<s:property value="#stat.index"/>' name="rowIndex" value='<s:property value="#stat.index"/>'/>
				</td>
			</tr>
		</s:iterator>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="7">&nbsp;</td>
			<td align="center">
				<img alt="เพิ่ม" title="เพิ่ม" src="<c:url value='/img/Add.png'/>" width="24" height="24" border="0" style="cursor: pointer;" id="btnAdd" onclick="lp_addRowtable();" />
				<img alt="รายการสินค้า" width="30px" height="30px" title="รายการสินค้า" id="btnZoom" src="<c:url value='/img/zoom.png'/>" border="0" style="cursor: pointer;" onclick="lp_openProductLoogUp();" />
			</td>
		</tr>
	</tfoot>
 </table>
