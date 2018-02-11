<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#frm");
	});
</script>
<s:if test='"NEW".equals(pageMode) || "EDIT_PAGE".equals(pageMode)'>
	<table class="table table-striped table-bordered table-criteria">
	    <thead>
	      <tr>
			<th width="5%" >ลำดับ</th>
			<th width="16%">สินค้า<span style="color: red;">*</span></th>
			<th width="12%">เหลือในคลัง</th>
			<th width="12%">ปริมาณ<span style="color: red;">*</span></th>
			<th width="12%">หน่วย</th>
			<th width="12%">ราคาต่อหน่วย<span style="color: red;">*</span></th>
			<th width="12%">ส่วนลด(%)<span style="color: red;">*</span></th>
			<th width="12%">ราคาขาย<span style="color: red;">*</span></th>
			<th width="9%" style="text-align: center;">
				<img alt="เพิ่ม" title="เพิ่ม" src="<c:url value='/img/Add.png'/>" width="24" height="24" border="0" style="cursor: pointer;" id="btnAdd" onclick="lp_addRowtable();" />
			</th>
	      </tr>
	    </thead>
	    <tbody>
	   		<s:set var="seqRow" value="0"/>
	   		<s:iterator status="stat" value="invoiceCashDetailList" var="item">
	   			<tr>
					<td style="text-align:center">
						<s:set var="seqRow" value="%{#seqRow+1}"/>
						<s:property value="%{#seqRow}" />
					</td>
					<td>
						<s:textfield id="productName%{#stat.index}" name="invoiceCashDetailList[%{#stat.index}].productName" cssClass="form-control auto-complete" onblur="lp_getProductDetailByName('%{#stat.index}');" placeholder="สินค้า" />
						<script>$("#productName" + '<s:property value="%{#stat.index}" />').focus();</script>
					</td>
					<td>
						<s:textfield id="inventory%{#stat.index}" name="invoiceCashDetailList[%{#stat.index}].inventory" cssClass="form-control numberOnly input-disabled" readonly="true" placeholder="เหลือในคลัง" />
					</td>
					<td>
						<s:textfield id="quantity%{#stat.index}" name="invoiceCashDetailList[%{#stat.index}].quantity" cssClass="form-control numberOnly" onblur="lp_getDiscount('%{#stat.index}');" placeholder="ปริมาณ" />
					</td>
					<td>
						<s:textfield id="unitName%{#stat.index}" name="invoiceCashDetailList[%{#stat.index}].unitName" cssClass="form-control input-disabled" readonly="true" placeholder="หน่วย" />
					</td>
					<td>
						<s:textfield id="pricePerUnit%{#stat.index}" name="invoiceCashDetailList[%{#stat.index}].pricePerUnit" cssClass="form-control moneyOnly" onblur="lp_calAmount('%{#stat.index}');" placeholder="ราคาต่อหน่วย" />
					</td>
					<td>
						<s:textfield id="discount%{#stat.index}" name="invoiceCashDetailList[%{#stat.index}].discount" cssClass="form-control numberOnly" onblur="lp_calAmount('%{#stat.index}');" placeholder="ส่วนลด(%)" />
					</td>
					<td>
						<s:textfield id="price%{#stat.index}" name="invoiceCashDetailList[%{#stat.index}].price" cssClass="form-control moneyOnly" onblur="lp_invoicePrice();" placeholder="จำนวนเงิน" />
					</td>
					<td align="center">
						<img alt="ลบ" title="ลบ" src="<c:url value='/img/wrong.png'/>" width="24" height="24" border="0" style="cursor: pointer;" onclick="lp_deleteRecord('<s:property value="%{#stat.index}" />');" />
						<s:hidden id="seq%{#stat.index}" name="invoiceCashDetailList[%{#stat.index}].seq" value="%{#stat.index}" />
						<s:hidden id="rowStatus%{#stat.index}" name="invoiceCashDetailList[%{#stat.index}].rowStatus" />
						<s:hidden id="productCode%{#stat.index}" name="invoiceCashDetailList[%{#stat.index}].productCode" />
						<s:hidden id="unitCode%{#stat.index}" name="invoiceCashDetailList[%{#stat.index}].unitCode" />
						<input type="hidden" id='rowIndex<s:property value="#stat.index"/>' name="rowIndex" value='<s:property value="#stat.index"/>'/>
					</td>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="8">&nbsp;</td>
				<td align="center">
					<img alt="รายการสินค้า" width="30px" height="30px" title="รายการสินค้า" id="btnZoom" src="<c:url value='/img/zoom.png'/>" border="0" style="cursor: pointer;" onclick="lp_openProductLoogUp();" />
				</td>
			</tr>
		</tfoot>
	 </table>
</s:if>
<s:else>
	<table class="table table-striped table-bordered table-criteria">
	    <thead>
	      <tr>
			<th width="5%" >ลำดับ</th>
			<th width="30%">สินค้า</th>
			<th width="20%">ปริมาณ</th>
			<th width="15%">ราคาต่อหน่วย</th>
			<th width="15%">ส่วนลด(%)</th>
			<th width="15%">ราคาขาย</th>
	      </tr>
	    </thead>
	    <tbody>
	   		<s:set var="seqRow" value="0"/>
	   		<s:iterator status="stat" value="invoiceCashDetailList" var="item">
	   			<tr>
					<td style="text-align:center">
						<s:set var="seqRow" value="%{#seqRow+1}"/>
						<s:property value="%{#seqRow}" />
					</td>
					<td><s:property value="%{#item.productName}" /></td>
					<td style="text-align:right;"><s:property value="%{#item.quantity}" />&nbsp;<s:property value="%{#item.unitName}" /></td>
					<td style="text-align:right;"><s:property value="%{#item.pricePerUnit}" /></td>
					<td><s:property value="%{#item.discount}" /></td>
					<td style="text-align:right;"><s:property value="%{#item.price}" /></td>
				</tr>
			</s:iterator>
		</tbody>
	 </table>
</s:else>