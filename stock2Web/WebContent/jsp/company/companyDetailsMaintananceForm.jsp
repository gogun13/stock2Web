<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	var gv_checkDupTin 		= false;
	
	$(document).ready(function(){
		gp_initialPage("#frm");
		
		if($("#pageMode").val()=="UPDATE"){
			$("#tin").prop('disabled', true);
			$("#inValidSpan").css("color", "green");
			$("#inValidSpan").html("เลขประจำตัวผู้เสียภาษีนี้ใช้งานได้");
			
			gv_checkDupTin = true;
		}else{
			$("#companyStatusDis").prop('disabled', true);
		}
		
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
						
						if (jsonObj.flagChkCompany == "Y" && jsonObj.FlagChange == "Y"){
            				lp_menuLink('<s:url action="changePassAction.action" />');
            			} else {
            				if($("#pageMode").val()=="NEW"){
            					$("#command").val("success");
            				}else{
            					$("#command").val("getDetail");
            				}
            				
            				gp_postAjaxRequest("#frm", "#page_main");
            			}
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
		
		$('body').on('blur', '#tin',function(event){
			var lv_tin 		= null;
			
			try{
				console.log("event.result :: " + event.result);
				if(!event.result)return;
				
				lv_tin = gp_trim($("#tin").val());
				
				$("#inValidSpan").html("");
				
				if(isBlank(lv_tin)){
					return false;
				}
				
				$("#hidTin").val(lv_tin);
				
				$("#command").val("checkDupTin");
				gp_postAjaxRequest("#frm", "", function(data){
					var jsonObj = JSON.parse(data);
					var cou 	= parseInt(jsonObj.COU);
					
					if(cou > 0){
	    				$("#inValidSpan").css("color", "red");
	    				$("#inValidSpan").html("มีเลขประจำตัวผู้เสียภาษีนี้ในระบบแล้ว");
	    				
	    				gv_checkDupTin = false;
	    				
	    			}else{
	    				$("#inValidSpan").css("color", "green");
	    				$("#inValidSpan").html("เลขประจำตัวผู้เสียภาษีนี้ใช้งานได้");
	    				
	    				gv_checkDupTin = true;
	    			}
				}, false);
				
			}catch(e){
				console.error("tin", e.stack);
			}
		});
		
	});
	
	function lp_validate(){
		var la_validate             = new Array( "tin:เลขประจำตัวผู้เสียภาษี"	
												, "companyName:ชื่อบริษัท"
												, "branchName:สาขาที่"
												, "provinceName:จังหวัด"
												, "districtName:อำเภอ/เขต"
												, "subdistrictName:ตำบล/แขวง"
												, "postCode:รหัสไปรษณ๊ย์");
		
		
	    var lv_return				= true;
	    
		try{
			
			if(!gp_validateEmptyObj(la_validate)){
				return false;
			}
			
			if(gv_checkDupTin==false){
				alert("มีเลขประจำตัวผู้เสียภาษีนี้ในระบบแล้ว", function() { 
					$("#tin").focus();
    		    });
				return false;
			}
			
			if(!gp_checkemail($("#email").val())){
				return false;
			}
			
		}catch(e){
			console.error("lp_validate", e.stack);
			return false;
		}
		
		return lv_return;
	}
	
	function lp_setCompanyStatus(){
		try{
			$("#companyStatus").val($("#companyStatusDis").val());
		}catch(e){
			alert("lp_setCompanyStatus :: " + e);
		}
	}
	
</script>
<s:form name="frm" id="frm" action="companyDetailsMaintananceAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden name="pageMode" id="pageMode"/>
	<s:hidden name="hidTin" id="hidTin" cssClass="tin"/>
	<s:hidden name="companyDetailsBean.companyStatus" id="companyStatus"/>
	<s:hidden name="userUniqueId" id="userUniqueId" />
	<s:hidden name="showBackFlag" id="showBackFlag"/>
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>ผู้ดูแลระบบ/จัดการผู้ใช้งาน</h2>
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
					<div class="col-sm-3 text-right">
						<label for="tin" class="control-label">
							เลขประจำตัวผู้เสียภาษี <span style="color: red;"><b>*</b></span>  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="tin" name="companyDetailsBean.tin" cssClass="form-control tin" placeholder="เลขประจำตัวผู้เสียภาษี" />
					</div>
					<div class="col-sm-6 text-left">
						<span id="inValidSpan"></span>
					</div>
				</div>
				<%--End Row 1 --%>
				<%--Begin Row 2 --%>
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="companyName" class="control-label">
							ชื่อบริษัท <span style="color: red;"><b>*</b></span>  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:if test='userUniqueId.equals("1")'>
							<s:textfield id="companyName" name="companyDetailsBean.companyName" cssClass="form-control" placeholder="ชื่อบริษัท" maxlength="100" />
						</s:if>
						<s:else>
							<s:textfield id="companyName" name="companyDetailsBean.companyName" cssClass="form-control input-disabled" readonly="true" placeholder="ชื่อบริษัท" />
						</s:else>
					</div>
					<div class="col-sm-6 text-left"></div>
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
						<s:textfield id="houseNumber" name="companyDetailsBean.houseNumber" cssClass="form-control" cssStyle="width: 80px;" placeholder="บ้านเลขที่" maxlength="30" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="mooNumber" class="control-label">
							หมู่ที่   :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="mooNumber" name="companyDetailsBean.mooNumber" cssClass="form-control" cssStyle="width: 80px;" placeholder="หมู่ที่" maxlength="10" />
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
						<s:textfield id="buildingName" name="companyDetailsBean.buildingName" cssClass="form-control" placeholder="อาคาร" maxlength="200" />
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
						<s:textfield id="soiName" name="companyDetailsBean.soiName" cssClass="form-control" placeholder="ตรอกซอย" maxlength="250" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="streetName" class="control-label">
							ถนน  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="streetName" name="companyDetailsBean.streetName" cssClass="form-control" placeholder="ถนน" maxlength="250" />
					</div>
				</div>
				<%--Begin Row 5 --%>
				<%--Begin Row 6 --%>
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="provinceName" class="control-label">
							จังหวัด <span style="color: red;"><b>*</b></span> :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="provinceName" name="companyDetailsBean.provinceName" cssClass="form-control auto-complete" placeholder="จังหวัด" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="districtName" class="control-label">
							อำเภอ/เขต <span style="color: red;"><b>*</b></span> :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="districtName" name="companyDetailsBean.districtName" cssClass="form-control auto-complete" placeholder="อำเภอ/เขต" />
					</div>
				</div>
				<%--Begin Row 6 --%>
				<%--Begin Row 7 --%>
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="subdistrictName" class="control-label">
							ตำบล/แขวง <span style="color: red;"><b>*</b></span> :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="subdistrictName" name="companyDetailsBean.subdistrictName" cssClass="form-control auto-complete" placeholder="ตำบล/แขวง" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="postCode" class="control-label">
							รหัสไปรษณ๊ย์ <span style="color: red;"><b>*</b></span> :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="postCode" name="companyDetailsBean.postCode" cssClass="form-control postCode" placeholder="รหัสไปรษณ๊ย์" maxlength="5" />
					</div>
				</div>
				<%--Begin Row 7 --%>
				<%--Begin Row 8 --%>
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="branchName" class="control-label">
							สาขาที่ <span style="color: red;"><b>*</b></span> :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="branchName" name="companyDetailsBean.branchName" cssClass="form-control" placeholder="สาขาที่" maxlength="30" />
					</div>
					<div class="col-sm-6 text-left"></div>
				</div>
				<%--Begin Row 8 --%>
				<%--Begin Row 9 --%>
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="tel" class="control-label">
							เบอร์โทร :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="tel" name="companyDetailsBean.tel" cssClass="form-control" placeholder="เบอร์โทร" maxlength="50" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="fax" class="control-label">
							เบอร์ Fax :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="fax" name="companyDetailsBean.fax" cssClass="form-control" placeholder="เบอร์ Fax" maxlength="50" />
					</div>
				</div>
				<%--Begin Row 9 --%>
				<%--Begin Row 10 --%>
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="email" class="control-label">
							E-mail :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="email" name="companyDetailsBean.email" cssClass="form-control" placeholder="E-mail" maxlength="200" />
					</div>
					<div class="col-sm-6 text-left"></div>
				</div>
				<%--Begin Row 10 --%>
				<%--Begin Row 11 --%>
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="remark" class="control-label">
							อื่นๆ :
						</label>
					</div>
					<div class="col-sm-5 text-left">
						<s:textarea id="remark" name="companyDetailsBean.remark" cols="60" rows="4" cssClass="form-control"></s:textarea>
					</div>
				</div>
				<%--Begin Row 11 --%>
				<%--Begin Row 12 --%>
				<s:if test='userUniqueId.equals("1")'>
					<div class="col-sm-12">
						<div class="col-sm-3 text-right">
							<label for="companyStatusDis" class="control-label">
								สถานะ <span style="color: red;"><b>*</b></span> :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:select id="companyStatusDis" name="companyStatusDis" cssClass="form-control"
		                      list="statusCombo" listKey="code" listValue="desc" onchange="lp_setCompanyStatus();" />
						</div>
						<div class="col-sm-6 text-left"></div>
					</div>
				</s:if>
				<%--Begin Row 12 --%>
				<%--Begin Row 13 --%>
				<div class="col-sm-12">
					<div class="col-sm-12 text-right">
						<s:if test='userUniqueId.equals("1")'>
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
						</s:if>
						<s:else>
							<button type="button" name="btnSave" id="btnSave" class="btn btn-primary  btn-md">
						    	<span class="glyphicon glyphicon-floppy-save"></span> บันทึก
						    </button>
						</s:else>
					</div>
				</div>
				<%--Begin Row 13 --%>
			</div>
		</div>
	</div>
</s:form>