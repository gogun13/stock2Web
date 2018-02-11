<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#frm");
		
		$('#btnSearch').click(function(){ 
			try{
				$("#pageIndex").val("1");
				$("#command").val("searchUserDetail");
				gp_postAjaxRequest("#frm", "#userDetailsSearch-list");
			}catch(e){
				console.error("btnSearch", e.stack);
			}
			
		});
		
		$('#btnReset').click(function(){ 
			try{
				$("#command").val("success");
				gp_postAjaxRequest("#frm", "#page_main");
			}catch(e){
				console.error("btnSearch", e.stack);
			}
			
		});
		
		$("#userName,#userEmail").keypress(function(e) {
		    if(e.which == 13) {
		    	$('#btnSearch').click();
		    }
		});
		
	});
	
	function changePage(pageNum) {
		try{
			$("#pageIndex").val(pageNum);
	        $("#command").val("searchUserDetail");
	        gp_postAjaxRequest("#frm", "#userDetailsSearch-list");
		}catch(e){
			console.error("changePage", e.stack);
		}
    }
	
	function lp_gotoUserDetail(av_userUniqueId){
		try{
			$("#userUniqueId").val(av_userUniqueId);
			$("#command").val("gotoUserDetail");
			gp_postAjaxRequest("#frm", "#page_main");
		}catch(e){
			console.error("lp_gotoUserDetail", e.stack);
		}
	}
	
</script>
<s:form name="frm" id="frm" action="userDetailsSearchAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden id="pageIndex" name="pageIndex" />
	<s:hidden id="userUniqueId" name="userUniqueId" />
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>ค้นหาผู้ใช้งานระบบ</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<div class="col-sm-3 text-right">
					<label for="userName" class="control-label">
						ชื่อ-นามสกุล  :
					</label>
				</div>
				<div class="col-sm-3 text-left">
					<s:textfield id="userName" name="userDetailsBean.userName" cssClass="form-control auto-complete" placeholder="ชื่อ-นามสกุล" maxlength="150" />
					<script>$("#userName").focus();</script>
				</div>
				<div class="col-sm-1 text-right">
					<label for="userEmail" class="control-label">
						E-mail :
					</label>
				</div>
				<div class="col-sm-3 text-left">
					<s:textfield id="userEmail" name="userDetailsBean.userEmail" cssClass="form-control" placeholder="E-mail" maxlength="100" />
				</div>
				<div class="col-sm-2 text-left"></div>
				<div class="col-sm-3 text-right">
					<label for="userStatus" class="control-label">
						สถานะ  :
					</label>
				</div>
				<div class="col-sm-3 text-left">
					<s:select id="userStatus" name="userDetailsBean.userStatus" cssClass="form-control"
	                      list="statusCombo" listKey="code" listValue="desc" />
				</div>
				<div class="col-sm-6 text-left"></div>
				<div class="col-sm-10 text-right">
					<button type="button" id="btnSearch" name="btnSearch" class="btn btn-success">
				    	<span class="glyphicon glyphicon-search"></span> ค้นหา
				    </button>
					<button type="button" name="btnReset" id="btnReset" class="btn btn-default">
				    	<span class="glyphicon glyphicon-off"></span> เริ่มใหม่
				    </button>
				</div>
				<div class="col-sm-2 text-right"></div>
			</div>
			<div class="row">
				<div id="userDetailsSearch-list" class="col-sm-12">
			    	<%@include file="userDetailsSearch-list.jsp" %>
				</div>
			</div>
		</div>
	</div>
</s:form>