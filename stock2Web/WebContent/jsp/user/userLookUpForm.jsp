<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#lookUpFrm");
		$('#btnLookUpSearch').click(function(){ 
			try{
				$("#pageIndexLookUp").val("1");
				$("#commandLookUp").val("searchUserDetail");
				gp_progressBarOnPopUp();
				gp_postAjaxRequest("#lookUpFrm", "#userLookUp-list",function(){
					gp_progressBarOffPopUp();
				},false);
			}catch(e){
				console.error("btnLookUpSearch", e.stack);
			}
			
		});
		
		$('#btnLookUpReset').click(function(){ 
			try{
				$("#commandLookUp").val("success");
				gp_progressBarOnPopUp();
				gp_postAjaxRequest("#lookUpFrm", "#dialogLookUp",function(){
					gp_progressBarOffPopUp();
				},false);
			}catch(e){
				console.error("btnLookUpReset", e.stack);
			}
			
		});
		
		$("#beanForLookUp\\.userName,#beanForLookUp\\.userEmail").keypress(function(e) {
		    if(e.which == 13) {
		    	$('#btnLookUpSearch').click();
		    }
		});
		
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
	
	function changePageLookUp(pageNum) {
		try{
			$("#pageIndexLookUp").val(pageNum);
	        $("#commandLookUp").val("searchUserDetail");
	        gp_progressBarOnPopUp();
	        gp_postAjaxRequest("#lookUpFrm", "#userLookUp-list",function(){
	        	gp_progressBarOffPopUp();
	        },false);
		}catch(e){
			console.error("changePage", e.stack);
		}
    }
	
</script>
<div id="dialogLookUp" title="ค้นหาผู้ใช้งานระบบ">
<s:form name="lookUpFrm" id="lookUpFrm" action="userLookUpAction" onsubmit="return false;">
	<s:hidden name="command" id="commandLookUp"/>
	<s:hidden name="pageIndex" id="pageIndexLookUp"  />
	<div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="beanForLookUp.userName" class="control-label">
							ชื่อ-นามสกุล  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="beanForLookUp.userName" name="beanForLookUp.userName" cssClass="form-control auto-complete" placeholder="ชื่อ-นามสกุล" maxlength="150" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="beanForLookUp.userEmail" class="control-label">
							E-mail :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="beanForLookUp.userEmail" name="beanForLookUp.userEmail" cssClass="form-control" placeholder="E-mail" maxlength="100" />
					</div>
				</div>
				<div class="col-sm-12">
					<div class="col-sm-11 text-right">
						<button type="button" id="btnLookUpSearch" name="btnLookUpSearch" class="btn btn-success">
					    	<span class="glyphicon glyphicon-search"></span> ค้นหา
					    </button>
						<button type="button" name="btnLookUpReset" id="btnLookUpReset" class="btn btn-default">
					    	<span class="glyphicon glyphicon-off"></span> เริ่มใหม่
					    </button>
					</div>
				</div>
			</div>
			<div class="row">
				<div id="userLookUp-list" class="col-sm-12">
			    	<%@include file="userLookUp-list.jsp" %>
				</div>
			</div>
		</div>
	</div>
</s:form>
</div>