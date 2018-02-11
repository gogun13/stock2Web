<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#frm");
	});
</script>
<s:if test="productList!=null">
	<s:hidden id="productCodeForDelete" name="productCodeForDelete" />
	<table class="table table-striped table-bordered table-criteria">
	    <thead>
	      <tr>
	        <th width="5%" >ลำดับ</th>
			<th width="9%">รหัสสินค้า</th>
			<th width="9%">ชื่อสินค้า</th>
			<th width="9%">ยอดแจ้งเตือน</th>
			<th width="9%">ราคาทุน</th>
			<th width="9%">ราคาขาย 1</th>
			<th width="9%">ราคาขาย 2</th>
			<th width="9%">ราคาขาย 3</th>
			<th width="9%">ราคาขาย 4</th>
			<th width="9%">ราคาขาย 5</th>
			<th width="9%">จำนวน</th>
			<th width="5%" style="text-align: center;">
				<img alt="เพิ่ม" title="เพิ่ม" src="<c:url value='/img/Add.png'/>" width="24" height="24" border="0" style="cursor: pointer;" id="btnAdd" onclick="lp_addRowtable();" />
			</th>
	      </tr>
	    </thead>
	    <tbody>
	    	<s:if test="!productList.isEmpty">
	    		<s:set var="seqRow" value="0"/>
	    		<s:iterator status="stat" value="productList" var="item">
		    		<tr>
						<td style="text-align:center">
							<s:set var="seqRow" value="%{#seqRow+1}"/>
							<s:property value="%{#seqRow}" />
						</td>
						<td>
							<s:if test='"NEW".equals(#item.rowStatus)'>
								<s:textfield id="productCodeDis%{#stat.index}" name="productList[%{#stat.index}].productCodeDis" cssClass="form-control" maxlength="17" placeholder="รหัสสินค้า" />
								<script>$("#productCodeDis" + '<s:property value="%{#stat.index}" />').focus();</script>
							</s:if>
							<s:else>
								<s:property value="%{#item.productCodeDis}" />
								<s:hidden id="productCodeDis%{#stat.index}" name="productList[%{#stat.index}].productCodeDis" />
							</s:else>
							<s:hidden id="productCode%{#stat.index}" name="productList[%{#stat.index}].productCode" />
						</td>
						<td>
							<s:if test='"NEW".equals(#item.rowStatus)'>
								<s:textfield id="productName%{#stat.index}" name="productList[%{#stat.index}].productName" cssClass="form-control" maxlength="255" placeholder="ชื่อสินค้า" />
							</s:if>
							<s:else>
								<s:property value="%{#item.productName}" />
								<s:hidden id="productName%{#stat.index}" name="productList[%{#stat.index}].productName" />
							</s:else>
						</td>
						<td>
							<s:textfield id="minQuan%{#stat.index}" name="productList[%{#stat.index}].minQuan" cssClass="form-control numberOnly" placeholder="ยอดแจ้งเตือน" />
						</td>
						<td>
							<s:textfield id="costPrice%{#stat.index}" name="productList[%{#stat.index}].costPrice" cssClass="form-control moneyOnly" placeholder="ราคาทุน" />
						</td>
						<td>
							<s:textfield id="salePrice1%{#stat.index}" name="productList[%{#stat.index}].salePrice1" cssClass="form-control moneyOnly" placeholder="ราคาขาย 1" />
						</td>
						<td>
							<s:textfield id="salePrice2%{#stat.index}" name="productList[%{#stat.index}].salePrice2" cssClass="form-control moneyOnly" placeholder="ราคาขาย 2" />
						</td>
						<td>
							<s:textfield id="salePrice3%{#stat.index}" name="productList[%{#stat.index}].salePrice3" cssClass="form-control moneyOnly" placeholder="ราคาขาย 3" />
						</td>
						<td>
							<s:textfield id="salePrice4%{#stat.index}" name="productList[%{#stat.index}].salePrice4" cssClass="form-control moneyOnly" placeholder="ราคาขาย 4" />
						</td>
						<td>
							<s:textfield id="salePrice5%{#stat.index}" name="productList[%{#stat.index}].salePrice5" cssClass="form-control moneyOnly" placeholder="ราคาขาย 5" />
						</td>
						<td>
							<s:textfield id="quantity%{#stat.index}" name="productList[%{#stat.index}].quantity" cssClass="form-control numberOnly" placeholder="จำนวน" />
						</td>
						<td align="center">
							<s:if test='"NEW".equals(#item.rowStatus)'>
								<img alt="ลบ" title="ลบ" src="<c:url value='/img/wrong.png'/>" width="24" height="24" border="0" style="cursor: pointer;" onclick="lp_deleteRecord('<s:property value="%{#stat.index}" />');" />
							</s:if>
							<s:else>
								<span class="glyphicon glyphicon-minus btn-sm"></span>
							</s:else>
							<s:hidden id="seq%{#stat.index}" name="productList[%{#stat.index}].seq" value="%{#stat.index}" />
							<s:hidden id="productStatus%{#stat.index}" name="productList[%{#stat.index}].productStatus" />
							<s:hidden id="rowStatus%{#stat.index}" name="productList[%{#stat.index}].rowStatus" />
							<input type="hidden" id='rowIndex<s:property value="#stat.index"/>' name="rowIndex" value='<s:property value="#stat.index"/>'/>
						</td>
					</tr>
				</s:iterator>
	    	</s:if>
		</tbody>
	 </table>
 </s:if>