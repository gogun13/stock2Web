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
        <th width="5%">ลำดับ</th>
		<th width="14%">ปริมาณที่ซื้อ</th>
		<th width="14%">ลดจำนวนเงิน(%)</th>
		<th width="18%">วันที่มีผล</th>
		<th width="18%">วันที่สิ้นสุด</th>
		<th width="16%">สำหรับการซื้อขาย</th>
		<th width="15%" style="text-align: center;">
			Action
		</th>
      </tr>
    </thead>
    <tbody>
   		<s:set var="seqRow" value="0"/>
   		<s:iterator status="stat" value="productdetailList" var="item">
   			<tr>
				<td style="text-align:center">
					<s:set var="seqRow" value="%{#seqRow+1}"/>
					<s:property value="%{#seqRow}" />
				</td>
				<td>
					<s:textfield id="quanDiscount%{#stat.index}" name="productdetailList[%{#stat.index}].quanDiscount" cssClass="form-control numberOnly" placeholder="ปริมาณที่ซื้อ" />
				</td>
				<td>
					<s:textfield id="discountRate%{#stat.index}" name="productdetailList[%{#stat.index}].discountRate" cssClass="form-control numberOnly" placeholder="ลดจำนวนเงิน(%)" />
				</td>
				<td>
					<s:textfield id="startDate%{#stat.index}" name="productdetailList[%{#stat.index}].startDate" cssClass="form-control dateFormat" cssStyle="width:85%;display:-webkit-inline-box;" placeholder="วันที่มีผล" />
				</td>
				<td>
					<s:textfield id="expDate%{#stat.index}" name="productdetailList[%{#stat.index}].expDate" cssClass="form-control dateFormat" cssStyle="width:85%;display:-webkit-inline-box;" placeholder="วันที่สิ้นสุด" />
				</td>
				<td>
					<s:select id="availPageFlag%{#stat.index}" name="productdetailList[%{#stat.index}].availPageFlag" cssClass="form-control"
		                      list="availPageFlagCombo" listKey="code" listValue="desc" />
				</td>
				<td align="center">
					<img alt="ลบ" title="ลบ" src="<c:url value='/img/wrong.png'/>" width="24" height="24" border="0" style="cursor: pointer;" onclick="lp_deleteRecord('<s:property value="%{#stat.index}" />');" />
					<s:hidden id="seq%{#stat.index}" name="productdetailList[%{#stat.index}].seq" value="%{#stat.index}" />
					<s:hidden id="rowStatus%{#stat.index}" name="productdetailList[%{#stat.index}].rowStatus" />
					<input type="hidden" id='rowIndex<s:property value="#stat.index"/>' name="rowIndex" value='<s:property value="#stat.index"/>'/>
				</td>
			</tr>
		</s:iterator>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="6">&nbsp;</td>
			<td align="center">
				<img alt="เพิ่ม" title="เพิ่ม" src="<c:url value='/img/Add.png'/>" width="24" height="24" border="0" style="cursor: pointer;" id="btnAdd" onclick="lp_addRowtable();" />
			</td>
		</tr>
	</tfoot>
 </table>
