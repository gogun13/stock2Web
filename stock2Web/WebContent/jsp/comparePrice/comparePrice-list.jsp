<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#frmComparePrice");
	});
</script>
<s:if test="comparePriceList!=null">
	<table class="table table-striped table-bordered table-criteria">
	    <thead>
	      <tr>
	        <th	width="5%" >ลำดับ</th>
			<th width="22%">บริษัท</th>
			<th width="17%">สาขา</th>
			<th width="17%">ปริมาณ</th>
			<th width="17%">ราคาที่ซื้อ</th>
			<th width="17%">ส่วนลด(%)</th>
			<th width="5%" style="text-align: center;">
				<img alt="เพิ่ม" title="เพิ่ม" src="<c:url value='/img/Add.png'/>" width="24" height="24" border="0" style="cursor: pointer;" id="btnAdd" onclick="lp_addComparePriceRowtable();" />
			</th>
	      </tr>
	    </thead>
	    <tbody>
	    	<s:if test="!comparePriceList.isEmpty">
	    		<s:set var="seqRow" value="0"/>
	    		<s:iterator status="stat" value="comparePriceList" var="item">
		    		<tr>
						<td style="text-align:center">
							<s:set var="seqRow" value="%{#seqRow+1}"/>
							<s:property value="%{#seqRow}" />
						</td>
						<td>
							<s:textfield id="vendorName%{#stat.index}" name="comparePriceList[%{#stat.index}].vendorName" cssClass="form-control auto-complete" onblur="lp_getCompanyVendorDetail($(this),%{#stat.index});" maxlength="100" placeholder="บริษัท" />
							<s:hidden id="vendorCode%{#stat.index}" name="comparePriceList[%{#stat.index}].vendorCode" />
						</td>
						<td>
							<s:textfield id="branchName%{#stat.index}" name="comparePriceList[%{#stat.index}].branchName" cssClass="form-control auto-complete" onblur="lp_getCompanyVendorDetail($(this),%{#stat.index});" maxlength="30" placeholder="สาขา" />
						</td>
						<td>
							<s:textfield id="quantity%{#stat.index}" name="comparePriceList[%{#stat.index}].quantity" cssClass="form-control numberOnly" placeholder="ปริมาณ" />
						</td>
						<td>
							<s:textfield id="price%{#stat.index}" name="comparePriceList[%{#stat.index}].price" cssClass="form-control moneyOnly" placeholder="ราคาที่ซื้อ" />
						</td>
						<td>
							<s:textfield id="discountRate%{#stat.index}" name="comparePriceList[%{#stat.index}].discountRate" cssClass="form-control" placeholder="ส่วนลด(%)" />
						</td>
						<td align="center">
							<img alt="ลบ" title="ลบ" src="<c:url value='/img/wrong.png'/>" width="24" height="24" border="0" style="cursor: pointer;" onclick="lp_deleteComparePriceRecord('<s:property value="%{#stat.index}" />');" />
							<s:hidden id="seq%{#stat.index}" name="comparePriceList[%{#stat.index}].seq" value="%{#stat.index}" />
							<s:hidden id="tin%{#stat.index}" name="comparePriceList[%{#stat.index}].tin" />
							<s:hidden id="productCode%{#stat.index}" name="comparePriceList[%{#stat.index}].productCode" />
							<s:hidden id="rowStatus%{#stat.index}" name="comparePriceList[%{#stat.index}].rowStatus" />
							<input type="hidden" id='rowIndexComparePrice<s:property value="#stat.index"/>' name="rowIndexComparePrice" value='<s:property value="#stat.index"/>'/>
						</td>
					</tr>
				</s:iterator>
	    	</s:if>
		</tbody>
	 </table>
 </s:if>