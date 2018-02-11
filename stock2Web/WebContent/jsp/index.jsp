<%@ page session="false" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
		<title>ระบบจัดการสินค้า</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<link rel="icon" href="<c:url value='/img/icon.ico'/>" type="image/x-icon">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>">
		<link rel="stylesheet" href="<c:url value='/css/jquery-ui.css'/>">
		<link rel="stylesheet" href="<c:url value='/css/Enjoy.css'/>">
		<script src="<c:url value='/js/jquery-3.2.1.js'/>"></script>
		<script src="<c:url value='/js/bootstrap.min.js'/>"></script>
		<script src="<c:url value='/js/jquery-ui.js'/>"></script>
		<script src="<c:url value='/js/EnjoyUtil.js'/>"></script>
		<style>
			body {
			  background-color:#f1f1f1;
			  -webkit-font-smoothing: antialiased;
			  font: normal 14px Roboto,arial,sans-serif;
			}
			
			.my-form{
				margin-top:12px;
				background-color:#EDEDED;
				-webkit-border-radius: 12px;
				-moz-border-radius: 12px;
				border-radius: 12px;
				padding: 12px 20px;
				box-shadow: 3px 3px 3px 3px #888887;
			}
			
			input[type=text], input[type=password] {
				width: 100%;
				padding: 12px 20px;
				margin: 8px 0;
				display: inline-block;
				border: 1px solid #ccc;
				box-sizing: border-box;
			}
			
			#btnLogin {
				background-color: #4CAF50;
				color: white;
				padding: 14px 20px;
				margin: 8px 0;
				border: none;
				cursor: pointer;
				width: 100%;
				-webkit-border-radius: 12px;
				-moz-border-radius: 12px;
				border-radius: 12px;
				font-size: 1.2em;
			}
			
			#btnLogin:hover {
				opacity: 0.8;
			}
			
			.imgcontainer {
				text-align: center;
				margin: 24px 0 12px 0;
			}
			
			span.psw {
				float: right;
				padding-top: 16px;
			}
			
			img.avatar {
				width: 60%;
			}
		</style>
		<script>
		$(document).ready(function(){if (gp_getCookie("username") != "") {
		    	$('#j_username').val(gp_getCookie("username"));
		    	$('#j_password').val("");
		    	$('#j_password').select();
		    }else{
		    	$('#j_username').select();
		    }
			
			$('#btnLogin').click(function(){
				var username	= null;
				var pass		= null;
				
				try{
					username 	= $('#j_username').val();
					pass 		= $('#j_password').val();						
					if (username == "") {
						gp_alert("กรุณาระบุรหัสผู้ใช้งานก่อนทำการเข้าสู่ระบบ", function() { 
							$('#j_username').focus();
		    		    },true);
						return false;
					}
					if (pass == "") {
						gp_alert("กรุณาระบุรหัสผ่านก่อนทำการเข้าสู่ระบบ", function() { 
							$('#j_password').focus();
		    		    },true);
						return false;
					}
					
					gp_progressBarOn();
					gp_postAjaxRequest("#loginForm", "", function(data){
						var jsonObj = null;
		            	
		            	try{
		            		jsonObj = JSON.parse(data);
		            		
		            		if(jsonObj.STATUS=="SUCCESS"){
			            		gp_setCookie("username", username, 3);
			            		
			            		if (jsonObj.flagChkCompany == "Y"){
			            			window.location.replace("<s:url action='index.action?command=success&pageAction=gotoCompanyDetails' />");
		            			}else if (jsonObj.FlagChange == "Y"){
		            				window.location.replace("<s:url action='index.action?command=success&pageAction=gotoChangePass' />");
		            			} else {
		            				countUserIncompany = parseInt(jsonObj.countUserIncompany);
		            				
		            				if(countUserIncompany==1){
		            					window.location.replace("<s:url action='index.action' />");
		            				}else{
		            					lp_chooseCompany(jsonObj.companyObjList);
		            				}
		            			}
		            		}else{
		            			gp_alert(jsonObj.MSG,function(){
		            				gp_progressBarOff();
		            			},false);
		            		}
		        			
		            	}catch(e){
		            		console.error("in btnLogin", e.stack);
		            	}
					},false);
					
				}catch(e){
					console.error("btnLogin", e.stack);
				}
			});
			
			$("#j_username,#j_password").keypress(function(e){
				try{
					var keycode =(window.event) ? event.keyCode : e.keyCode;
					if(keycode == 13) {
						$('#btnLogin').click();
					}
				}catch(e){
					console.error("keypress", e.stack);
				}
			});
			
		});
		
		function lp_chooseCompany(av_companyObjList){
			var lv_html = "";
			var lv_select = "";
			
			try{
				lv_select += '<select id="popUpTin" name="popUpTin" class="form-control" style="width:80%">';
				lv_select += '<option value="">กรุณาระบุ</option>';
				$.each(av_companyObjList, function(idx, obj) {
					lv_select+= '<option value="' + obj.code + '">' + obj.desc + '</option>';
				});
				lv_select += '</select>';
				
				lv_html = '<table border="0" width="100%">'
						 + '	<tr>'
						 + '		<td align="center">' + lv_select + '</td>'
						 + '	</tr>'
						 + '	<tr><td style="height:40px;"></td></tr>'
						 + '	<tr>'
						 + '		<td align="center" colspan="2">'
						 + '			<input type="button" onclick="lp_submit();" id="btnSubmit" class="btn btn-primary btn-md" name="btnSubmit" value="ตกลง" />'
						 + '			<input type="button" onclick="lp_cancel();" id="btnCancel" class="btn btn-primary btn-md" name="btnCancel" value="ยกเลิก" />'
						 + '		</td>'
						 + '	</tr>'
						 + '</table>';
				
				gp_dialogPopUpHtml(lv_html, "กรุณาระบุบริษัทที่ต้องการเข้าใช้งานระบบ",600, 200);
			}catch(e){
				console.error("lp_chooseCompany", e.stack);
			}
		}
		
		function lp_submit(){
			try{
				if($("#popUpTin").val()==""){
					$("#popUpTin").focus();
					return false;
				}
				
				$("#tin").val($("#popUpTin").val());
				
				$('#btnLogin').click();
			}catch(e){
				console.error("lp_submit", e.stack);
			}
		}
		
		function lp_cancel(){
			try{
				$("#tin").val("");
				$( "#dialog" ).dialog( "close" );
			}catch(e){
				console.error("lp_cancel", e.stack);
			}
		}
		
		</script>
	</head>
	<body>
		<form method="POST" name="loginForm" id="loginForm" action="<c:url value='/j_spring_security_check' />" onsubmit="return false;">
			<input type="hidden" id="tin" name="tin" value="" />
			<div class="container">
				<div class="row">
					<div class="col-sm-4">&nbsp;</div>
					<div class="col-sm-4 my-form">
						<div class="imgcontainer">
							<img src="<c:url value='/img/logo-login.png'/>" alt="Avatar" class="avatar">
						</div>
						<div>
							<label><b>รหัสผู้ใช้งาน</b></label>
							<input type="text" placeholder="รหัสผู้ใช้งาน" id="j_username" name="j_username" class="form-control" value="" />
	
							<label><b>รหัสผ่าน</b></label>
							<input type="password" placeholder="รหัสผ่าน" id="j_password" name="j_password" class="form-control" value="" />
							<button type="button" id="btnLogin" name="btnLogin" class="btn btn-success">
						    	Login <span class="glyphicon glyphicon-log-in"></span>
						    </button>
						 </div>
					</div>
					<div class="col-sm-4">&nbsp;</div>
				</div>
			</div>
			<div id="dialog" title="Look up"></div>
			<div id="biz_error_div" style="display:none"></div>
			<div align="center" class="FreezeScreen" style="display:none;">
	        	<img id="imgProgress" src="<c:url value='/img/loading36.gif'/>" alt="" />
	        	<span style="font-weight: bold;font-size: large;color: black;">Loading...</span>
	    	</div>
	    	<div align="center" class="FreezeScreenPopUp" style="display:none;">
	        	<img id="imgProgress" src="<c:url value='/img/loading36.gif'/>" alt="" />
	        	<span style="font-weight: bold;font-size: large;color: black;">Loading...</span>
	    	</div>
		</form>
	</body>
</html>