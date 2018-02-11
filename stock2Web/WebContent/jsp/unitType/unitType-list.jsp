<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<s:hidden id="unitCodeForDelete" name="unitCodeForDelete" />
<table class="table table-striped table-bordered table-criteria">
    <thead>
      <tr>
        <th width="5%">ลำดับ</th>
		<th width="80%">ชื่อหน่วยสินค้า</th>
		<th width="15%" style="text-align: center;">
			<img alt="เพิ่ม" title="เพิ่ม" src="<c:url value='/img/Add.png'/>" width="24" height="24" border="0" style="cursor: pointer;" id="btnAdd" onclick="lp_addRow();" />
		</th>
      </tr>
    </thead>
    <tbody>
    	<s:if test="!unitTypeList.isEmpty">
    		<s:set var="seqRow" value="0"/>
    		<s:iterator status="stat" value="unitTypeList" var="item">
    			<tr>
					<td style="text-align:center">
						<s:set var="seqRow" value="%{#seqRow+1}"/>
						<s:property value="%{#seqRow}" />
					</td>
					<td>
						<s:textfield id="unitName%{#stat.index}" name="unitTypeList[%{#stat.index}].unitName" cssClass="form-control" maxlength="200" placeholder="ชื่อหน่วยสินค้า" />
					</td>
					<td align="center">
						<img alt="ลบ" title="ลบ" src="<c:url value='/img/wrong.png'/>" width="24" height="24" border="0" style="cursor: pointer;" onclick="lp_deleteRecord('<s:property value="%{#stat.index}" />');" />
						<s:hidden id="seq%{#stat.index}" name="unitTypeList[%{#stat.index}].seq" value="%{#stat.index}" />
						<s:hidden id="unitCode%{#stat.index}" name="unitTypeList[%{#stat.index}].unitCode" />
						<s:hidden id="unitStatus%{#stat.index}" name="unitTypeList[%{#stat.index}].unitStatus" />
						<s:hidden id="rowStatus%{#stat.index}" name="unitTypeList[%{#stat.index}].rowStatus" />
						<input type="hidden" id='rowIndex<s:property value="#stat.index"/>' name="rowIndex" value='<s:property value="#stat.index"/>'/>
					</td>
				</tr>
			</s:iterator>
    	</s:if>
	</tbody>
 </table>
