<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#changePass");
		alert(1);
		$('#btnSave').click(function(){
		    try{
		    	if(!lp_validate())return;
				
				$("#command").val("saveUpdData");
				gp_progressBarOn();
				gp_postAjaxRequest("#changePass", "#page_main", function(){
					gp_alert("เปลี่ยนรหัสผ่านเรียบร้อย กรุณาเข้าระบบใหม่", function() { 
						gp_progressBarOff();
						window.location.replace("<s:url action='logout.action' />"); 
	    		    }, false);
				}, false);
		    	
				
		    }catch(e){
		    	console.error("btnSave", e.stack);
		    }
		});
	});

	function lp_validate(){
		var la_validate = new Array( "oldPassword:รหัสผ่านเดิม"	
									, "newPassword:รหัสผ่านใหม่"
									, "confirmNewPassword:ยื่นยันรหัสผ่าน");
	    
		try{
			
			if(!gp_validateEmptyObj(la_validate)){
				return false;
			}
			
			if(gp_trim($("#newPassword").val()) != gp_trim($("#confirmNewPassword").val())){
				gp_alert("ยืนยันรหัสผ่านใหม่ไม่ตรงกับรหัสผ่านใหม่ กรุณาระบุใหม่อีกครั้ง", function() { 
					$("#confirmNewPassword").focus();
    		    });
				return false;
			}
			
		}catch(e){
			console.error("lp_validate", e.stack);
			return false;
		}
		
		return true;
	}

</script>
<s:form name="changePass" id="changePass" action="changePassAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>เปลี่ยนรหัสผ่าน</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-primary">
			<div class="panel-heading">
		        <h3 class="panel-title">เปลี่ยนรหัสผ่าน</h3>
			</div>
			<div class="contents form-horizontal panel-body">
				<div class="form-group" style="padding: 5px;">
	                <label for="oldPassword" class="col-sm-4 control-label" style="text-align: right;">
	                	รหัสผ่านเดิม<span style="color: red;"><b>*</b></span>
	                	</label>
	                <div class="col-sm-4">
	                    <s:password id="oldPassword" name="oldPassword" cssClass="form-control" placeholder="รหัสผ่านเดิม" />
	                </div>
	            </div>
	            <div class="form-group" style="padding: 5px;">
	                <label for="newPassword" class="col-sm-4 control-label" style="text-align: right;">
	                	รหัสผ่านใหม่<span style="color: red;"><b>*</b></span>
	                </label>
	                <div class="col-sm-4">
	                    <s:password id="newPassword" name="newPassword" cssClass="form-control" placeholder="รหัสผ่านใหม่" />
	                </div>
	            </div>
	            <div class="form-group" style="padding: 5px;">
	                <label for="confirmNewPassword" class="col-sm-4 control-label" style="text-align: right;">
	                	ยืนยันรหัสผ่านใหม่<span style="color: red;"><b>*</b></span>
	                </label>
	                <div class="col-sm-4">
	                    <s:password id="confirmNewPassword" name="confirmNewPassword" cssClass="form-control" placeholder="ยืนยันรหัสผ่านใหม่" />
	                </div>
	            </div>
	            <div class="form-group" style="padding: 25px;">
	                <div class="col-sm-10 pull-right" style="text-align: right;">
	                    <input type="button" name="btnSave" id="btnSave" class="btn btn-primary  btn-md" value="บันทึก">
	                    <input type="reset" name="btnReset" id="btnReset" class="btn btn-default  btn-md" value="เริ่มใหม่">
	                </div>
	            </div>
			</div>
		</div>
	</div>
</s:form>