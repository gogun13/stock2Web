<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	
	$(document).ready(function(){
		gp_initialPage("#frm");
	    
	    $("#cusName").focus();
		
		lp_ctrlIdType();
		if($("#pageMode").val()=="NEW"){
			$("#cusStatusDis").prop('disabled', true);
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
						$("#hidCusCode").val(jsonObj.cusCode);
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
		var la_validate   	= new Array( "cusName:ชื่อ"	
										, "cusSurname:นามสกุล"
										, "provinceName:จังหวัด"
										, "districtName:อำเภอ/เขต"
										, "subdistrictName:ตำบล/แขวง"
										, "postCode:รหัสไปรษณ๊ย์"
										, "cusStatusDis:สถานะ"
										, "startDate:วันที่สมัคร"
										, "cusGroupCode:กลุ่มลูกค้า");
		
	    var startDate				= "";
	    var expDate					= "";
	    
		try{
			
			if(!gp_validateEmptyObj(la_validate)){
				return false;
			}
			
			startDate		= gp_trim($("#startDate").val());
			expDate			= gp_trim($("#expDate").val());
			
			if(!isBlank(expDate)){
				if(gp_toDate(startDate) > gp_toDate(expDate)){
					gp_alert("วันที่หมดอายุต้องมากกว่าวันที่สมัคร");
	                return false;
                }
			}
			
			return true;
			
		}catch(e){
			console.error("lp_validate", e.stack);
			return false;
		}
	}
	
	function lp_ctrlIdType(){
		try{
			if($("#idType").val()=="0"){
				$("#idNumber").val('');
				$("#idNumber").prop("disabled", true);
			}else{
				$("#idNumber").prop("disabled", false);
			}
		}catch(e){
			alert("lp_ctrlIdType :: " + e);
		}
	}
	
	function lp_setCusStatus(){
		try{
			$("#cusStatus").val($("#cusStatusDis").val());
		}catch(e){
			alert("lp_setCompanyStatus :: " + e);
		}
	}
	
</script>
<s:form name="frm" id="frm" action="customerAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden name="pageMode" id="pageMode"/>
	<s:hidden name="hidCusCode" id="hidCusCode" />
	<s:hidden name="customerDetailsBean.cusStatus" id="cusStatus"/>
	<s:hidden name="customerDetailsBean.point" id="point" value="0"/>
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
						<label for="cusCode" class="control-label">
							รหัสลูกค้า :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="cusCode" name="customerDetailsBean.cusCode" cssClass="form-control" disabled="true" placeholder="รหัสลูกค้า" />
					</div>
				</div>
				<%--End Row 1 --%>
				<%--Begin Row 2 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="cusName" class="control-label">
							ชื่อ<span style="color: red;"><b>*</b></span>:
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="cusName" name="customerDetailsBean.cusName" cssClass="form-control" placeholder="ชื่อ" maxlength="100" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="cusSurname" class="control-label">
							นามสกุล<span style="color: red;"><b>*</b></span>:
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="cusSurname" name="customerDetailsBean.cusSurname" cssClass="form-control" placeholder="นามสกุล" maxlength="100" />
					</div>
				</div>
				<%--Begin Row 2 --%>
				<%--Begin Row 3 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="branchName" class="control-label">
							สาขา  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="branchName" name="customerDetailsBean.branchName" cssClass="form-control" placeholder="สาขา" maxlength="30" />
					</div>
				</div>
				<%--Begin Row 3 --%>
				<%--Begin Row 4 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="idType" class="control-label">
							ประเภทบัตร  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:select id="idType" name="customerDetailsBean.idType" cssClass="form-control"
		                      list="idTypeCombo" listKey="code" listValue="desc" onchange="lp_ctrlIdType();" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="idNumber" class="control-label">
							เลขที่บัตร  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="idNumber" name="customerDetailsBean.idNumber" cssClass="form-control tin" placeholder="เลขที่บัตร" />
					</div>
				</div>
				<%--Begin Row 4 --%>
				<%--Begin Row 5 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="sex" class="control-label">
							เพศ  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:select id="sex" name="customerDetailsBean.sex" cssClass="form-control"
		                      list="sexCombo" listKey="code" listValue="desc" />
					</div>
				</div>
				<%--Begin Row 5 --%>
				<%--Begin Row 6 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="birthDate" class="control-label">
							วันเกิด  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="birthDate" name="customerDetailsBean.birthDate" cssClass="form-control dateFormat" cssStyle="width:85%;display:-webkit-inline-box;" placeholder="วันเกิด" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="religion" class="control-label">
							ศาสนา  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="religion" name="customerDetailsBean.religion" cssClass="form-control" placeholder="ศาสนา" />
					</div>
				</div>
				<%--Begin Row 6 --%>
				<%--Begin Row 7 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="job" class="control-label">
							อาชีพ :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="job" name="customerDetailsBean.job" cssClass="form-control" placeholder="อาชีพ" />
					</div>
				</div>
				<%--Begin Row 7 --%>
				<%--Begin Row 8 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="houseNumber" class="control-label">
							บ้านเลขที่   :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="houseNumber" name="customerDetailsBean.houseNumber" cssClass="form-control" cssStyle="width: 100px;" placeholder="บ้านเลขที่" maxlength="30" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="mooNumber" class="control-label">
							หมู่ที่   :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="mooNumber" name="customerDetailsBean.mooNumber" cssClass="form-control" cssStyle="width: 80px;" placeholder="หมู่ที่" maxlength="10" />
					</div>
				</div>
				<%--Begin Row 8 --%>
				<%--Begin Row 9 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="buildingName" class="control-label">
							อาคาร  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="buildingName" name="customerDetailsBean.buildingName" cssClass="form-control" placeholder="อาคาร" maxlength="200" />
					</div>
					<div class="col-sm-6 text-left"></div>
				</div>
				<%--Begin Row 9 --%>
				<%--Begin Row 10 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="soiName" class="control-label">
							ตรอกซอย  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="soiName" name="customerDetailsBean.soiName" cssClass="form-control" placeholder="ตรอกซอย" maxlength="250" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="streetName" class="control-label">
							ถนน  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="streetName" name="customerDetailsBean.streetName" cssClass="form-control" placeholder="ถนน" maxlength="250" />
					</div>
				</div>
				<%--Begin Row 10 --%>
				<%--Begin Row 11 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="provinceName" class="control-label">
							จังหวัด <span style="color: red;"><b>*</b></span> :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="provinceName" name="customerDetailsBean.provinceName" cssClass="form-control auto-complete" placeholder="จังหวัด" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="districtName" class="control-label">
							อำเภอ/เขต <span style="color: red;"><b>*</b></span> :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="districtName" name="customerDetailsBean.districtName" cssClass="form-control auto-complete" placeholder="อำเภอ/เขต" />
					</div>
				</div>
				<%--Begin Row 11 --%>
				<%--Begin Row 12 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="subdistrictName" class="control-label">
							ตำบล/แขวง <span style="color: red;"><b>*</b></span> :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="subdistrictName" name="customerDetailsBean.subdistrictName" cssClass="form-control auto-complete" placeholder="ตำบล/แขวง" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="postCode" class="control-label">
							รหัสไปรษณ๊ย์ <span style="color: red;"><b>*</b></span> :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="postCode" name="customerDetailsBean.postCode" cssClass="form-control postCode" placeholder="รหัสไปรษณ๊ย์" maxlength="5" />
					</div>
				</div>
				<%--Begin Row 12 --%>
				<%--Begin Row 13 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="tel" class="control-label">
							เบอร์โทร :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="tel" name="customerDetailsBean.tel" cssClass="form-control" placeholder="เบอร์โทร" maxlength="50" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="fax" class="control-label">
							เบอร์ Fax :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="fax" name="customerDetailsBean.fax" cssClass="form-control" placeholder="เบอร์ Fax" maxlength="50" />
					</div>
				</div>
				<%--Begin Row 13 --%>
				<%--Begin Row 14 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="email" class="control-label">
							E-mail :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="email" name="customerDetailsBean.email" cssClass="form-control" placeholder="E-mail" maxlength="200" />
					</div>
					<div class="col-sm-6 text-left"></div>
				</div>
				<%--Begin Row 14 --%>
				<%--Begin Row 15 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="cusStatusDis" class="control-label">
							สถานะ <span style="color: red;"><b>*</b></span> :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:select id="cusStatusDis" name="cusStatusDis" cssClass="form-control"
	                      list="statusCombo" listKey="code" listValue="desc" onchange="lp_setCusStatus();" />
					</div>
					<div class="col-sm-6 text-left"></div>
				</div>
				<%--Begin Row 15 --%>
				<%--Begin Row 16 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="startDate" class="control-label">
							วันที่สมัคร <span style="color: red;"><b>*</b></span> :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="startDate" name="customerDetailsBean.startDate" cssClass="form-control dateFormat" cssStyle="width:85%;display:-webkit-inline-box;" placeholder="วันที่สมัคร" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="expDate" class="control-label">
							วันที่หมดอายุ :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="expDate" name="customerDetailsBean.expDate" cssClass="form-control dateFormat" cssStyle="width:85%;display:-webkit-inline-box;" placeholder="วันที่หมดอายุ " />
					</div>
				</div>
				<%--Begin Row 16 --%>
				<%--Begin Row 17 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="cusGroupCode" class="control-label">
							กลุ่มลูกค้า<span style="color: red;"><b>*</b></span> :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:select id="cusGroupCode" name="customerDetailsBean.cusGroupCode" cssClass="form-control"
	                      list="groupSalePriceCombo" listKey="code" listValue="desc" />
					</div>
					<div class="col-sm-6 text-left"></div>
				</div>
				<%--Begin Row 17 --%>
				<%--Begin Row 18 --%>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="remark" class="control-label">
							อื่นๆ :
						</label>
					</div>
					<div class="col-sm-5 text-left">
						<s:textarea id="remark" name="customerDetailsBean.remark" cols="60" rows="4" cssClass="form-control"></s:textarea>
					</div>
				</div>
				<%--Begin Row 18 --%>
				<%--Begin Row 19 --%>
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
				<%--Begin Row 19 --%>
			</div>
			
		</div>
	</div>
</s:form>