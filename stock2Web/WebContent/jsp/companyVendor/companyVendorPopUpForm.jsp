<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#lookUpFrm");
		
		$("#dialogLookUp").dialog({
	        autoOpen: true,
	        bgiframe:true,
	        modal: true,
	        height: 600,
		    width: 1050,
	        draggable: true,
	        show: {
		        effect: "clip",
		        duration: 500
		    },
		    hide: {
		        effect: "clip",
		        duration: 500
		    },
	        close: function(event, ui){
	        	gp_progressBarOff();
	        	gp_initialPage("#frm");
	            $(this).dialog('destroy').remove();
	        },
	        buttons: null,
		    dialogClass: 'zoom'
	    });
		
	});
	
</script>
<div id="dialogLookUp" title="รายละเอียดผู้จำหน่าย">
<s:form name="lookUpFrm" id="lookUpFrm" action="companyVendorPopUpAction" onsubmit="return false;">
	<s:hidden name="command" id="commandLookUp"/>
	<s:hidden name="pageIndex" id="pageIndexLookUp"  />
	<s:hidden name="hidVendorCodePopUp" id="hidVendorCodePopUp"  />
	<div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<%--Begin Row 1 --%>
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="vendorName" class="control-label">
							บริษัท :
						</label>
					</div>
					<div class="col-sm-4 text-left" id="vendorName">
						<s:property value="%{companyVendorBean.vendorName}" />
					</div>
					<div class="col-sm-1 text-right">
						<label for="branchName" class="control-label">
							สาขา:
						</label>
					</div>
					<div class="col-sm-3 text-left" id="branchName">
						<s:property value="%{companyVendorBean.branchName}" />
					</div>
				</div>
				<%--End Row 1 --%>
				<%--Begin Row 2 --%>
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="tin" class="control-label">
							เลขประจำตัวผู้เสียภาษี:
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:property value="%{companyVendorBean.tin}" />
					</div>
				</div>
				<%--Begin Row 2 --%>
				<%--Begin Row 3 --%>
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="houseNumber" class="control-label">
							บ้านเลขที่   :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:property value="%{companyVendorBean.houseNumber}" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="mooNumber" class="control-label">
							หมู่ที่   :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:property value="%{companyVendorBean.mooNumber}" />
					</div>
				</div>
				<%--Begin Row 3 --%>
				<%--Begin Row 4 --%>
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="buildingName" class="control-label">
							อาคาร  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:property value="%{companyVendorBean.buildingName}" />
					</div>
					<div class="col-sm-6 text-left"></div>
				</div>
				<%--Begin Row 4 --%>
				<%--Begin Row 5 --%>
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="soiName" class="control-label">
							ตรอกซอย  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:property value="%{companyVendorBean.soiName}" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="streetName" class="control-label">
							ถนน  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:property value="%{companyVendorBean.streetName}" />
					</div>
				</div>
				<%--Begin Row 5 --%>
				<%--Begin Row 6 --%>
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="provinceName" class="control-label">
							จังหวัด :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:property value="%{companyVendorBean.provinceName}" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="districtName" class="control-label">
							อำเภอ/เขต :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:property value="%{companyVendorBean.districtName}" />
					</div>
				</div>
				<%--Begin Row 6 --%>
				<%--Begin Row 7 --%>
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="subdistrictName" class="control-label">
							ตำบล/แขวง :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:property value="%{companyVendorBean.subdistrictName}" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="postCode" class="control-label">
							รหัสไปรษณ๊ย์ :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:property value="%{companyVendorBean.postCode}" />
					</div>
				</div>
				<%--Begin Row 7 --%>
				<%--Begin Row 8 --%>
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="tel" class="control-label">
							เบอร์โทร :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:property value="%{companyVendorBean.tel}" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="fax" class="control-label">
							เบอร์ Fax :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:property value="%{companyVendorBean.fax}" />
					</div>
				</div>
				<%--Begin Row 8 --%>
				<%--Begin Row 9 --%>
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="email" class="control-label">
							E-mail :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:property value="%{companyVendorBean.email}" />
					</div>
					<div class="col-sm-6 text-left"></div>
				</div>
				<%--Begin Row 9 --%>
				<%--Begin Row 10 --%>
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="remark" class="control-label">
							อื่นๆ :
						</label>
					</div>
					<div class="col-sm-5 text-left">
						<s:property value="companyVendorBean.remark" escapeHtml="false" />
					</div>
				</div>
				<%--Begin Row 10 --%>
			</div>
		</div>
	</div>
</s:form>
</div>