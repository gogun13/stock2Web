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
        <th width="5%"> ลำดับ</th>
		<th width="19%">สินค้า<span style="color: red;">*</span></th>
		<th width="11%">เหลือในคลัง</th>
		<th width="11%">ปริมาณ<span style="color: red;">*</span></th>
		<th width="10%">หน่วย</th>
		<th width="11%">ราคาที่ซื้อ<span style="color: red;">*</span></th>
		<th width="12%">ส่วนลด(%)<span style="color: red;">*</span></th>
		<th width="12%">จำนวนเงิน<span style="color: red;">*</span></th>
		<th width="9%">Action</th>
      </tr>
    </thead>
    <tbody>
   		<s:set var="seqRow" value="0"/>
   		<s:iterator status="stat" value="reciveOrdeDetailList" var="item">
   			<tr>
				<td style="text-align:center">
					<s:set var="seqRow" value="%{#seqRow+1}"/>
					<s:property value="%{#seqRow}" />
				</td>
				<td>
					<s:textfield id="productName%{#stat.index}" name="reciveOrdeDetailList[%{#stat.index}].productName" cssClass="form-control auto-complete" onblur="lp_getProductDetailByName('%{#stat.index}');" cssStyle="width:85%;display:-webkit-inline-box;" placeholder="สินค้า" />
					<a href="javascript:void(0);" title="เปรียบเทียบราคา" style="color: #504f4f;" onclick="lp_comparePrice('<s:property value="%{#stat.index}" />');">
			          <span class="glyphicon glyphicon-list-alt btn-lg" style="padding: 0px 0px"></span>
			        </a>
					<script>$("#productName" + '<s:property value="%{#stat.index}" />').focus();</script>
				</td>
				<td>
					<s:textfield id="inventory%{#stat.index}" name="reciveOrdeDetailList[%{#stat.index}].inventory" cssClass="form-control numberOnly input-disabled" readonly="true" placeholder="เหลือในคลัง" />
				</td>
				<td>
					<s:textfield id="quantity%{#stat.index}" name="reciveOrdeDetailList[%{#stat.index}].quantity" cssClass="form-control numberOnly" onblur="lp_getPrice('%{#stat.index}');" placeholder="ปริมาณ" />
				</td>
				<td>
					<s:textfield id="unitName%{#stat.index}" name="reciveOrdeDetailList[%{#stat.index}].unitName" cssClass="form-control input-disabled" readonly="true" placeholder="หน่วย" />
				</td>
				<td>
					<s:textfield id="price%{#stat.index}" name="reciveOrdeDetailList[%{#stat.index}].price" cssClass="form-control moneyOnly" onblur="lp_calAmount('%{#stat.index}');" placeholder="ราคาที่ซื้อ" />
				</td>
				<td>
					<s:textfield id="discountRate%{#stat.index}" name="reciveOrdeDetailList[%{#stat.index}].discountRate" cssClass="form-control" onblur="lp_calAmount('%{#stat.index}');" placeholder="ส่วนลด(%)" />
				</td>
				<td>
					<s:textfield id="costPrice%{#stat.index}" name="reciveOrdeDetailList[%{#stat.index}].costPrice" cssClass="form-control moneyOnly" onblur="lp_calReciveAmount();" placeholder="จำนวนเงิน" />
				</td>
				<td align="center">
					<s:if test='"1".equals(currReciveStatus) || "2".equals(currReciveStatus)'>
						<img alt="ลบ" title="ลบ" src="<c:url value='/img/wrong.png'/>" width="24" height="24" border="0" style="cursor: pointer;" onclick="lp_deleteRecord('<s:property value="%{#stat.index}" />');" />
					</s:if>
					<s:else>
						<span class="glyphicon glyphicon-minus btn-sm"></span>
					</s:else>
					<s:hidden id="seq%{#stat.index}" name="reciveOrdeDetailList[%{#stat.index}].seq" value="%{#stat.index}" />
					<s:hidden id="rowStatus%{#stat.index}" name="reciveOrdeDetailList[%{#stat.index}].rowStatus" />
					<s:hidden id="productCode%{#stat.index}" name="reciveOrdeDetailList[%{#stat.index}].productCode" />
					<s:hidden id="unitCode%{#stat.index}" name="reciveOrdeDetailList[%{#stat.index}].unitCode" />
					<input type="hidden" id='rowIndex<s:property value="#stat.index"/>' name="rowIndex" value='<s:property value="#stat.index"/>'/>
				</td>
			</tr>
		</s:iterator>
	</tbody>
	<s:if test='"1".equals(currReciveStatus) || "2".equals(currReciveStatus)'>
		<tfoot>
			<tr>
				<td colspan="8">&nbsp;</td>
				<td align="center">
					<img alt="เพิ่ม" title="เพิ่ม" src="<c:url value='/img/Add.png'/>" width="24" height="24" border="0" style="cursor: pointer;" id="btnAdd" onclick="lp_addRowtable();" />
					<img alt="รายการสินค้า" width="30px" height="30px" title="รายการสินค้า" id="btnZoom" src="<c:url value='/img/zoom.png'/>" border="0" style="cursor: pointer;" onclick="lp_openProductLoogUp();" />
				</td>
			</tr>
		</tfoot>
	</s:if>
 </table>
