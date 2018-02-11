<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<s:hidden id="userUniqueIdForDelete" name="userUniqueIdForDelete" />
<s:if test="relationUserAndCompanyList!=null">
	<table class="table table-striped table-bordered table-criteria">
	    <thead>
	      <tr>
	        <th width="5%">ลำดับ</th>
			<th width="20%">รหัสผู้ใช้งาน</th>
			<th width="40%">ชื่อ-นามสกุล</th> 
			<th width="20%">สถานะ</th> 
			<th width="15%">Action</th>
	      </tr>
	    </thead>
	    <tbody>
	    	<s:if test="!relationUserAndCompanyList.isEmpty">
	    		<s:set var="seqRow" value="0"/>
	    		<s:iterator status="stat" value="relationUserAndCompanyList" var="item">
	    			<tr>
						<td style="text-align:center">
							<s:set var="seqRow" value="%{#seqRow+1}"/>
							<s:property value="%{#seqRow}" />
						</td>
						<td>
							<s:property value="#item.userEmail"/>
							<s:hidden id="userEmail%{#stat.index}" name="relationUserAndCompanyList[%{#stat.index}].userEmail" />
						</td>
						<td>
							<s:property value="#item.userFullName"/>
							<s:hidden id="userFullName%{#stat.index}" name="relationUserAndCompanyList[%{#stat.index}].userFullName" />
						</td>
						<td>
							<s:property value="#item.userStatusName"/>
							<s:hidden id="userStatusName%{#stat.index}" name="relationUserAndCompanyList[%{#stat.index}].userStatusName" />
						</td>
						<td align="center">
							<img alt="ลบ" title="ลบ" src="<c:url value='/img/wrong.png'/>" width="24" height="24" border="0" style="cursor: pointer;" onclick="lp_deleteRecord('<s:property value="%{#stat.index}" />');" />
							<s:hidden id="seq%{#stat.index}" name="relationUserAndCompanyList[%{#stat.index}].seq" value="%{#stat.index}" />
							<s:hidden id="userUniqueId%{#stat.index}" name="relationUserAndCompanyList[%{#stat.index}].userUniqueId" />
							<s:hidden id="rowStatus%{#stat.index}" name="relationUserAndCompanyList[%{#stat.index}].rowStatus" />
						</td>
					</tr>
				</s:iterator>
	    	</s:if>
		</tbody>
	   	<tfoot>
			<tr>
				<td colspan="4">&nbsp;</td>
				<td align="center">
					<img alt="เลือกผู้ใช้งานระบบ" width="30px" height="30px" title="เลือกผู้ใช้งานระบบ" id="btnZoom" src="<c:url value='/img/zoom.png'/>" style="cursor: pointer;" border="0" onclick="lp_lookUp();" />
				</td>
			</tr>
		</tfoot>
	 </table>
	 <div class="row" >
		 <div class="col-sm-12 text-right">
         	<button type="button" name="btnSave" id="btnSave" onclick="lp_save();" class="btn btn-primary  btn-md">
		    	<span class="glyphicon glyphicon-floppy-save"></span> บันทึก
		    </button>
		    <button type="button" name="btnReset" id="btnReset" onclick="lp_reset();" class="btn btn-default  btn-md">
		    	<span class="glyphicon glyphicon-off"></span> เริ่มใหม่
		    </button>
		    <span style="width:20px;">&nbsp;</span>
		</div>
		<div class="col-sm-12 text-right" style="height:20px;"></div>
	</div>
 </s:if>