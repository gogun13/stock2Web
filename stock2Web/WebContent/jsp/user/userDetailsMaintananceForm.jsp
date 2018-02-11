<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	var gv_checkDupUserEmail 	= false;
	
	$(document).ready(function(){
		gp_initialPage("#userDetailsMaintanancesForm");
		
		if($("#pageMode").val()=="UPDATE"){
			lp_checkDupUserEmail();
			lp_initialUserPrivilege();
		}else{
			$("#userStatusDis").prop('disabled', true);
		}
		
		$('#btnReset').click(function(){
		    try{
				
				if($("#pageMode").val()=="NEW"){
					$("#command").val("success");
				}else{
					$("#command").val("onGetUserDetail");
				}
				
				gp_postAjaxRequest("#userDetailsMaintanancesForm", "#page_main");
		    	
		    }catch(e){
		    	console.error("btnReset", e.stack);
		    }
		});
		
		$('#btnSave').click(function(){
		    try{
		    	if(!lp_validate())return;
				if($("#pageMode").val()=="NEW"){
					if($("#sendMailFlag").val()=="Y"){
						gp_confirm("Password จะถูกส่งไปที่ E-mail ที่คุณกรอก คุณกรอก E-mail ถูกต้องแล้วใช่หรือไม่ ?", function(){
							onSave();
						},function(){
							$('#userEmail').focus();
							return;
						});
					}else{
						onSave();
					}
				}else if ($("#pageMode").val()=="UPDATE"){
					onSave();
				}
		    	
		    }catch(e){
		    	console.error("btnSave", e.stack);
		    }
		});
		
		$('#btnResetPass').click(function(){
			try{
				gp_progressBarOn();
				if($("#sendMailFlag").val()=="Y"){
					gp_confirm("Password จะถูกส่งไปที่ E-mail ที่คุณกรอก คุณกรอก E-mail ถูกต้องแล้วใช่หรือไม่ ?", function(){
						lp_ajaxResetPass();
					}, function(){
						gp_progressBarOff();
						$('#userEmail').focus();
						return;
					},false);
				}else{
					lp_ajaxResetPass();
				}
				
			}catch(e){
				console.error("btnResetPass", e.stack);
			}
		});
		
		$('#btnBack').click(function(){
			try{
				$("#command").val("onBack");
				gp_postAjaxRequest("#userDetailsMaintanancesForm", "#page_main");
				
			}catch(e){
				console.error("btnBack", e.stack);
			}
		});
		
		lp_ctrlCommission();
	});
	
	function lp_ajaxResetPass(){
		
		try{
			$("#command").val("resetPass");
			gp_postAjaxRequest("#userDetailsMaintanancesForm", "", function(data){
				try{
					var jsonObj = JSON.parse(data);
					
					if($("#sendMailFlag").val()=="Y"){
	    				gp_alert("Password ถูกส่งไปที่ E-mail แล้ว", function(){
	    					gp_progressBarOff();
	    				},false);
	    			}else{
	    				params 	= "userUniqueId="+$("#userUniqueId").val()+"&pwd=" + jsonObj.pwd;
	    				gp_dialogPopUpPdf('<s:url action="userDetailsMaintanancesAction.action" />?command=genPdf&' + params, "แจ้งรหัสผู้ใช้งานและรหัสผ่าน", function(){
	    					gp_progressBarOff();
	    				},false);
	    			}
					
				}catch(e){
					console.error("return lp_ajaxResetPass", e.stack);
				}
			}, false);
		}catch(e){
			console.error("lp_ajaxResetPass", e.stack);
		}
	}
	
	function lp_setUserPrivilege(){
		
		var lv_userPrivilege = "";
		
		try{
			$( 'input[name="chkUserPrivilege"]:checked' ).each(function( index ) {
				lv_userPrivilege += lv_userPrivilege==""?$(this).val():","+$(this).val();
			});
			
			$("#hidUserPrivilege").val(lv_userPrivilege);
			
		}catch(e){
			console.error("lp_setUserPrivilege", e.stack);
		}
		
	}
	
	function onSave(){
		
		try{
			$("#command").val("onSave");
			gp_progressBarOn();
			gp_postAjaxRequest("#userDetailsMaintanancesForm", "", function(data){
				
				var jsonObj = JSON.parse(data);
				
				if($("#sendMailFlag").val()=="N" && $("#pageMode").val()=="NEW"){
					var params 	= "userUniqueId="+jsonObj.userUniqueId+"&pwd=" + jsonObj.pwd;
    				gp_dialogPopUpPdf('<s:url action="userDetailsMaintanancesAction.action" />?command=genPdf&' + params, "แจ้งรหัสผู้ใช้งานและรหัสผ่าน", function(){
    					
    					$("#userUniqueId").val(jsonObj.userUniqueId);
    					$("#command").val("onGetUserDetail");
    					gp_postAjaxRequest("#userDetailsMaintanancesForm", "#page_main");
    				},false);
				}else{
					gp_dialogSuccess("บันทึกเรียบร้อย", function() { 
						gp_progressBarOff();
				    }, false);
				}
			}, false);
		}catch(e){
	    	console.error("onSave", e.stack);
	    }
	}
	
	function lp_ctrlCommission(){
		
		try{
			if($("#flagSalesman:checked").length > 0){
				$('#commission').prop('readonly', false);
				$("#commission").attr('class', 'form-control col-sm-1 numberOnly');
			}else{
				$('#commission').prop('readonly', true);
				$("#commission").attr('class', 'form-control col-sm-1 numberOnly input-disabled');
				$('#commission').val("0");
			}
			
		}catch(e){
			console.error("lp_ctrlCommission", e.stack);
		}
	}
	
	function lp_checkDupUserEmail(){
		
		var lv_userEmail 	= null;
		
		try{
			lv_userEmail = gp_trim($("#userEmail").val());
			
			$("#inValidSpan").html("");
			
			if(lv_userEmail==""){
				return;
			}
			
			$("#userEmail").val(lv_userEmail);
			$("#command").val("checkDupUserEmail");
			
			gp_postAjaxRequest("#userDetailsMaintanancesForm", "", function(data){
				var jsonObj = null;
            	var cou		= 0;
            	
            	try{
            		jsonObj = JSON.parse(data);
            		status	= jsonObj.status;
            		
            		cou = parseInt(jsonObj.COU);
        			
        			if(cou > 0){
        				$("#inValidSpan").css("color", "red");
        				$("#inValidSpan").html("มี E-mail นี้ในระบบแล้ว");
        				
        				gv_checkDupUserEmail = false;
        				
        			}else{
        				$("#inValidSpan").css("color", "green");
        				$("#inValidSpan").html("E-mail นี้ใช้งานได้");
        				
        				gv_checkDupUserEmail = true;
        			}
        			
            	}catch(e){
            		console.error("in lp_checkDupUserEmail", e.stack);
            	}
			},false);
			
		}catch(e){
			console.error("lp_checkDupUserEmail", e.stack);
		}
	}
	
	function lp_validate(){
	    var la_validate   = new Array( "userName:ชื่อ"	
									, "userSurname:นามสกุล"
									, "userEmail:E-mail");
	    
		try{
			
			if(!gp_validateEmptyObj(la_validate)){
				return false;
			}
			
			if(gv_checkDupUserEmail==false){
				gp_alert("มี E-mail นี้ในระบบแล้ว", function() { 
					$("#userEmail").focus();
    		    });
				return false;
			}
			
			if($('#flagSalesman').is(':checked') && $("#commission").val() <= 0){
				gp_alert("กรุณาระบุค่าคอม", function() { 
					$("#commission").focus();
    		    });
				return false;
			}
			
			if($('input[name="chkUserPrivilege"]:checked').length<=0){
				gp_alert("กรุณาเลือกสิทธิ์การใช้ระบบอย่างน้อย 1 รายการ");
                return false;
            }
			
		}catch(e){
			console.error("lp_validate", e.stack);
			return false;
		}
		
		return true;
	}
	
	function lp_initialUserPrivilege(){
		
		var lv_userPrivilege = "";
		var la_userPrivilege = "";
		
		try{
			lv_userPrivilege = $("#hidUserPrivilege").val();
			
			if(!isBlank(lv_userPrivilege)){
				la_userPrivilege = lv_userPrivilege.split(",");
				la_userPrivilege.forEach(function(item, i) {
					$( 'input[name="chkUserPrivilege"]' ).each(function( index ) {
						if(item==$(this).val()){
							$(this).prop('checked', true);
						}
					});
				});
			}
			
		}catch(e){
			console.error("lp_initialUserPrivilege", e.stack);
		}
	}
	
	function lp_changeUserStatusDis(){
		try{
			$("#userStatus").val($("#userStatusDis").val());
		}catch(e){
			console.error("lp_changeUserStatusDis", e.stack);
		}
	}
	
</script>
<s:form name="userDetailsMaintanancesForm" id="userDetailsMaintanancesForm" action="userDetailsMaintanancesAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden name="pageMode" id="pageMode"/>
	<s:hidden name="sendMailFlag" id="sendMailFlag"/>
	<s:hidden name="userUniqueId" id="userUniqueId"/>
	<s:hidden name="showBackFlag" id="showBackFlag"/>
	<s:hidden id="hidUserPrivilege" name="userDetailsBean.userPrivilege" />
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
			<div class="contents form-horizontal panel-body">
				<div class="form-group" style="padding: 5px;">
	                <label for="userName" class="col-sm-4 control-label" style="text-align: right;">
	                	ชื่อ <span style="color: red;"><b>*</b></span>
	                </label>
	                <div class="col-sm-4">
	                    <s:textfield id="userName" name="userDetailsBean.userName" cssClass="form-control" placeholder="ชื่อ" maxlength="50" />
	                </div>
	            </div>
	            <div class="form-group" style="padding: 5px;">
	                <label for="userSurname" class="col-sm-4 control-label" style="text-align: right;">
	                	นามสกุล <span style="color: red;"><b>*</b></span>
	                </label>
	                <div class="col-sm-4">
	                    <s:textfield id="userSurname" name="userDetailsBean.userSurname" cssClass="form-control" placeholder="นามสกุล" maxlength="100" />
	                </div>
	            </div>
	            <div class="form-group" style="padding: 5px;">
	                <label for="userEmail" class="col-sm-4 control-label" style="text-align: right;">
	                	E-mail<span style="color: red;"><b>*</b></span>
	                </label>
	                <div class="col-sm-4">
	                    <s:textfield id="userEmail" name="userDetailsBean.userEmail" onchange="lp_checkDupUserEmail();" cssClass="form-control" placeholder="E-mail" maxlength="100" />
	                </div>
	                <div class="col-sm-2 text-left" style="padding-left:0px;">
	                	<span id="inValidSpan"></span>
	                </div>
	            </div>
	            <div class="form-group" style="padding: 5px;">
	                <label for="userStatusDis" class="col-sm-4 control-label" style="text-align: right;">
	                	สถานะ <span style="color: red;"><b>*</b></span>
	                </label>
	                <div class="col-sm-4">
	                	<s:select id="userStatusDis" name="userStatusDis" cssClass="form-control"
	                      list="statusCombo" listKey="code" listValue="desc" onchange="lp_changeUserStatusDis();" />
	                    <s:hidden id="userStatus" name="userDetailsBean.userStatus" />
	                </div>
	            </div>
	            <div class="form-group" style="padding: 5px;">
	                <label for="remark" class="col-sm-4 control-label" style="text-align: right;">
	                	หมายเหตุ
	                </label>
	                <div class="col-sm-6">
	                	<s:textarea rows="4" id="remark" name="userDetailsBean.remark" cssClass="form-control" />
	                </div>
	            </div>
	            <div class="form-group" style="padding: 5px;">
	                <div class="col-sm-3"></div>
	                <div class="col-sm-6">
	                	<table style="border: 0px;padding: 5px;width:100%;">
	                		<tr>
			        			<td align="center" style="width: 10%;">
					                <s:checkbox id="flagSalesman" 
				                		name="flagSalesman" 
				                		onclick="lp_ctrlCommission();"
				                		cssStyle="width:15px;height:15px;"
				                		value="%{flagSalesman}" 
				                		fieldValue="true"/>
			        			</td>
			        			<td align="left" style="width: 30%;">
			        				<label for="flagSalesman">เป็นพนักงานขาย </label>
			        			</td>
			        			<td align="left">
			        				<s:textfield id="commission" name="userDetailsBean.commission" cssClass="form-control col-sm-1 numberOnly" placeholder="ค่าคอม" cssStyle="width: 80px;" />&nbsp;<label>%</label>
			        			</td>
			        		</tr>
	                		<tr>
	                			<td align="center">
	                				<s:checkbox id="flagChangePassword" 
				                		name="flagChangePassword" 
				                		cssStyle="width:15px;height:15px;"
				                		value="%{flagChangePassword}" 
				                		fieldValue="true"/>
	                			</td>
			        			<td align="left" colspan="3">
				                	<label for="flagChangePassword">
					                	ต้องการเปลี่ยนรหัสผ่านเมื่อ Login ครั้งแรก
					                </label>
			        			</td>
			        		</tr>
	                	</table>
	                </div>
	            </div>
	            <div class="form-group" style="padding: 5px;">
	                <div class="col-sm-3"></div>
	                <div class="col-sm-6">
	                	<table style="border: 0px;padding: 5px;width:100%;">
	                		<tr>
			        			<td align="left" colspan="4">
			        				<label>
					                	สิทธิ์การใช้ระบบ<span style="color: red;"><b>*</b></span>
					                </label>
			        			</td>
			        		</tr>
	                		<s:iterator status="stat" value="userprivilegeList" var="item">
	                			<s:if test="%{(#stat.index+1)%2 != 0}"><tr style="height:30px;"></s:if>
	                			<td align="right">
			        				<input type="checkbox" id="chkUserPrivilege<s:property value="#item.privilegeCode"/>" name="chkUserPrivilege" onclick="lp_setUserPrivilege();" value="<s:property value="#item.privilegeCode"/>" /> :&nbsp;
			        			</td>
			        			<td align="left">
			        				<label style="font-weight: normal !important;" for="chkUserPrivilege<s:property value="#item.privilegeCode"/>">
			        					<s:property value="#item.privilegeName"/>
			        				</label>
			        			</td>
			        			<s:if test="%{(#stat.index+1)%2 == 0}"></tr></s:if>
	                		</s:iterator>
	                	</table>
	                </div>
	            </div>
	            <div class="form-group" style="padding: 25px;">
	                <div class="col-sm-10 pull-right" style="text-align: right;">
	                	<s:if test='"UPDATE".equals(pageMode)'>
	                		<s:if test='"Y".equals(showBackFlag)'>
		                		<button type="button" id="btnBack" name="btnBack" class="btn btn-default  btn-md">
							    	<span class="glyphicon glyphicon-hand-left"></span> กลับ
							    </button>
						    </s:if>
						    <button type="button" id="btnResetPass" name="btnResetPass" class="btn btn-default btn-md">
						    	Reset Password
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
			</div>
		</div>
	</div>
</s:form>