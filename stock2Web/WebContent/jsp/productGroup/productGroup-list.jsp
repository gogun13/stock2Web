<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<s:if test="productGroupList!=null">
	<s:hidden id="productGroupCodeForDelete" name="productGroupCodeForDelete" />
	<table class="table table-striped table-bordered table-criteria">
	    <thead>
	      <tr>
	        <th width="5%">ลำดับ</th>
			<th width="20%">รหัสหมู่สินค้า</th>
			<th width="60%">ชื่อหมู่สินค้า</th> 
			<th width="15%" style="text-align: center;">
				<img alt="เพิ่ม" title="เพิ่ม" src="<c:url value='/img/Add.png'/>" width="24" height="24" border="0" style="cursor: pointer;" id="btnAdd" onclick="lp_addRowtable();" />
			</th>
	      </tr>
	    </thead>
	    <tbody>
	    	<s:if test="!productGroupList.isEmpty">
	    		<s:set var="seqRow" value="0"/>
	    		<s:iterator status="stat" value="productGroupList" var="item">
		    		<tr>
						<td style="text-align:center">
							<s:set var="seqRow" value="%{#seqRow+1}"/>
							<s:property value="%{#seqRow}" />
						</td>
						<td>
							<s:if test='!"NEW".equals(#item.rowStatus)'>
								<s:property value="#item.productGroupCodeDis"/>
								<s:hidden id="productGroupCodeDis%{#stat.index}" name="productGroupList[%{#stat.index}].productGroupCodeDis" />
							</s:if>
							<s:else>
								<s:textfield id="productGroupCodeDis%{#stat.index}" name="productGroupList[%{#stat.index}].productGroupCodeDis" cssClass="form-control" maxlength="5" placeholder="รหัสหมู่สินค้า" />
							</s:else>
							<s:hidden id="productGroupCode%{#stat.index}" name="productGroupList[%{#stat.index}].productGroupCode" />
						</td>
						<td>
							<s:textfield id="productGroupName%{#stat.index}" name="productGroupList[%{#stat.index}].productGroupName" cssClass="form-control" maxlength="200" placeholder="ชื่อหมู่สินค้า" />
						</td>
						<td align="center">
							<img alt="ลบ" title="ลบ" src="<c:url value='/img/wrong.png'/>" width="24" height="24" border="0" style="cursor: pointer;" onclick="lp_deleteRecord('<s:property value="%{#stat.index}" />');" />
							<s:hidden id="seq%{#stat.index}" name="productGroupList[%{#stat.index}].seq" value="%{#stat.index}" />
							<s:hidden id="productGroupStatus%{#stat.index}" name="productGroupList[%{#stat.index}].productGroupStatus" />
							<s:hidden id="rowStatus%{#stat.index}" name="productGroupList[%{#stat.index}].rowStatus" />
							<input type="hidden" id='rowIndex<s:property value="#stat.index"/>' name="rowIndex" value='<s:property value="#stat.index"/>'/>
						</td>
					</tr>
				</s:iterator>
	    	</s:if>
		</tbody>
	 </table>
 </s:if>