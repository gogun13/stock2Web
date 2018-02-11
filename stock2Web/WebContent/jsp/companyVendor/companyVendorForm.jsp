<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	
	$(document).ready(function(){
		gp_initialPage("#frm");
	    
	    $("#vendorName").focus();
		
		$('#btnReset').click(function(){
		    try{
				
				if($("#pageMode").val()=="NEW"){
					$("#command").val("success");
				}else{
					$("#command").val("getDetail");
				}
				
				gp_postAjaxRequest("#frm", "#page_main");
		    	
		    }catch(e){
		    	console.error("btnReset", e.stack);
		    }
		});
		
		$('#btnSave').click(function(){
		    try{
		    	if(!lp_validate())return;
		    	
		    	$("#command").val("onSave");
				gp_progressBarOn();
				gp_postAjaxRequest("#frm", "", function(data){
					var jsonObj = JSON.parse(data);
					
					gp_dialogSuccess("บันทึกเรียบร้อย", function() { 
						$("#hidVendorCode").val(jsonObj.vendorCode);
						$("#command").val("getDetail");
        				
        				gp_postAjaxRequest("#frm", "#page_main");
				    }, false);
				}, false);
		    	
		    }catch(e){
		    	console.error("btnSave", e.stack);
		    }
		});
		
		$('#btnBack').click(function(){
			try{
				$("#command").val("onBack");
				gp_postAjaxRequest("#frm", "#page_main");
				
			}catch(e){
				console.error("btnBack", e.stack);
			}
		});
		
		
		
	});
	
	function lp_validate(){
		var la_validate             = new Array("vendorName:บริษัท"	
									, "branchName:สาขา"
									, "provinceName:จังหวัด"
									, "districtName:อำเภอ/เขต"
									, "subdistrictName:ตำบล/แขวง"
									, "postCode:รหัสไปรษณีย์"
									, "tel:เบอร์โทร");
	    
		try{
			
			if(!gp_validateEmptyObj(la_validate)){
				return false;
			}
			
			return true;
			
		}catch(e){
			console.error("lp_validate", e.stack);
			return false;
		}
	}
	
</script>
<s:form name="frm" id="frm" action="companyVendorAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden name="pageMode" id="pageMode"/>
	<s:hidden name="hidVendorCode" id="hidVendorCode" />
	<s:hidden name="showBackFlag" id="showBackFlag"/>
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>เพิ่มลูกค้า/บริษัทสั่งซื้อ</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading">
		        <h3 class="panel-title"><s:property value="titlePage"/></h3>
			</div>
			<div class="panel-body paddingForm">
				<%--Begin Row 1 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="vendorName" class="control-label">
							บริษัท<span style="color: red;"><b>*</b></span> :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="vendorName" name="companyVendorBean.vendorName" cssClass="form-control" maxlength="100" placeholder="บริษัท" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="branchName" class="control-label">
							สาขา<span style="color: red;"><b>*</b></span>:
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="branchName" name="companyVendorBean.branchName" cssClass="form-control" placeholder="สาขา" maxlength="30" />
					</div>
				</div>
				<%--End Row 1 --%>
				<%--Begin Row 2 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="tin" class="control-label">
							เลขประจำตัวผู้เสียภาษี:
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="tin" name="companyVendorBean.tin" cssClass="form-control tin" placeholder="เลขประจำตัวผู้เสียภาษี" />
					</div>
				</div>
				<%--Begin Row 2 --%>
				<%--Begin Row 3 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="houseNumber" class="control-label">
							บ้านเลขที่   :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="houseNumber" name="companyVendorBean.houseNumber" cssClass="form-control" cssStyle="width: 100px;" placeholder="บ้านเลขที่" maxlength="30" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="mooNumber" class="control-label">
							หมู่ที่   :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="mooNumber" name="companyVendorBean.mooNumber" cssClass="form-control" cssStyle="width: 80px;" placeholder="หมู่ที่" maxlength="10" />
					</div>
				</div>
				<%--Begin Row 3 --%>
				<%--Begin Row 4 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="buildingName" class="control-label">
							อาคาร  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="buildingName" name="companyVendorBean.buildingName" cssClass="form-control" placeholder="อาคาร" maxlength="200" />
					</div>
					<div class="col-sm-6 text-left"></div>
				</div>
				<%--Begin Row 4 --%>
				<%--Begin Row 5 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="soiName" class="control-label">
							ตรอกซอย  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="soiName" name="companyVendorBean.soiName" cssClass="form-control" placeholder="ตรอกซอย" maxlength="250" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="streetName" class="control-label">
							ถนน  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="streetName" name="companyVendorBean.streetName" cssClass="form-control" placeholder="ถนน" maxlength="250" />
					</div>
				</div>
				<%--Begin Row 5 --%>
				<%--Begin Row 6 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="provinceName" class="control-label">
							จังหวัด <span style="color: red;"><b>*</b></span> :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="provinceName" name="companyVendorBean.provinceName" cssClass="form-control auto-complete" placeholder="จังหวัด" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="districtName" class="control-label">
							อำเภอ/เขต <span style="color: red;"><b>*</b></span> :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="districtName" name="companyVendorBean.districtName" cssClass="form-control auto-complete" placeholder="อำเภอ/เขต" />
					</div>
				</div>
				<%--Begin Row 6 --%>
				<%--Begin Row 7 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="subdistrictName" class="control-label">
							ตำบล/แขวง <span style="color: red;"><b>*</b></span> :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="subdistrictName" name="companyVendorBean.subdistrictName" cssClass="form-control auto-complete" placeholder="ตำบล/แขวง" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="postCode" class="control-label">
							รหัสไปรษณ๊ย์ <span style="color: red;"><b>*</b></span> :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="postCode" name="companyVendorBean.postCode" cssClass="form-control postCode" placeholder="รหัสไปรษณ๊ย์" maxlength="5" />
					</div>
				</div>
				<%--Begin Row 7 --%>
				<%--Begin Row 8 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="tel" class="control-label">
							เบอร์โทร <span style="color: red;"><b>*</b></span>:
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="tel" name="companyVendorBean.tel" cssClass="form-control" placeholder="เบอร์โทร" maxlength="50" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="fax" class="control-label">
							เบอร์ Fax :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="fax" name="companyVendorBean.fax" cssClass="form-control" placeholder="เบอร์ Fax" maxlength="50" />
					</div>
				</div>
				<%--Begin Row 8 --%>
				<%--Begin Row 9 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="email" class="control-label">
							E-mail :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="email" name="companyVendorBean.email" cssClass="form-control" placeholder="E-mail" maxlength="200" />
					</div>
					<div class="col-sm-6 text-left"></div>
				</div>
				<%--Begin Row 9 --%>
				<%--Begin Row 10 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="remark" class="control-label">
							อื่นๆ :
						</label>
					</div>
					<div class="col-sm-5 text-left">
						<s:textarea id="remark" name="companyVendorBean.remark" cols="60" rows="4" cssClass="form-control"></s:textarea>
					</div>
				</div>
				<%--Begin Row 10 --%>
				<%--Begin Row 11 --%>
				<div class="col-sm-12">
					<div class="col-sm-12 text-right">
						<s:if test='"Y".equals(showBackFlag)'>
	                		<button type="button" id="btnBack" name="btnBack" class="btn btn-default  btn-md">
						    	<span class="glyphicon glyphicon-hand-left"></span> กลับ
						    </button>
	                	</s:if>
	                	<button type="button" name="btnSave" id="btnSave" class="btn btn-primary  btn-md">
					    	<span class="glyphicon glyphicon-floppy-save"></span> บันทึก
					    </button>
					    <button type="button" name="btnReset" id="btnReset" class="btn btn-default  btn-md">
					    	<span class="glyphicon glyphicon-off"></span> เริ่มใหม่
					    </button>
					</div>
				</div>
				<%--Begin Row 11 --%>
			</div>
			
		</div>
	</div>
</s:form>