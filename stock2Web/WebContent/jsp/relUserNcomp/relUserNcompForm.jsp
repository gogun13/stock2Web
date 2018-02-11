<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#frm");
		
		$('#btnSearch').click(function(){ 
			try{
				if(isBlank($("#companyName").val())){
					gp_alert("กรุณาระบุชื่อบริษัท", function() { 
						$("#companyName").focus();
	    		    });
	                return;
	            }
				
				$("#command").val("onSearch");
				gp_postAjaxRequest("#frm", "#page_main");
			}catch(e){
				console.error("btnSearch", e.stack);
			}
		});
		
		$("#companyName").keypress(function(e) {
		    if(e.which == 13) {
		    	$('#btnSearch').click();
		    }
		});
		
	});
	
	function lp_lookUp(){
		try{
			$("#command").val("lookUp");
			gp_postAjaxRequest("#frm", "#dialog");
		}catch(e){
			console.error("lp_lookUp", e.stack);
		}
	}
	
	function lp_returnData(av_userUniqueId){
		try{
			$("#hidUserUniqueId").val(av_userUniqueId);
			
			$("#command").val("newRecord");
			gp_postAjaxRequest("#frm", "#relUserNcomp-list");
			
			$( "#dialogLookUp" ).dialog( "close" );
			
		}catch(e){
			console.error("lp_returnData", e.stack);
		}
	}
	
	function lp_deleteRecord(av_seq){
		try{
			$("#indexRow").val(av_seq);
			
			$("#command").val("deleteRecord");
			gp_postAjaxRequest("#frm", "#relUserNcomp-list");
		}catch(e){
			console.error("lp_deleteRecord", e.stack);
		}
	}
	
	function lp_reset(){
		try{
			$("#command").val("success");
			gp_postAjaxRequest("#frm", "#page_main");
		}catch(e){
			console.error("lp_reset", e.stack);
		}
	}
	
	function lp_save(){
		try{
			$("#command").val("save");
			gp_progressBarOn();
			gp_postAjaxRequest("#frm", "#relUserNcomp-list",function(){
				gp_dialogSuccess("บันทึกเรียบร้อย",function(){
					gp_progressBarOff();
				},false);
			},false);
		}catch(e){
			console.error("lp_save", e.stack);
		}
	}
	
</script>
<s:form name="frm" id="frm" action="relationUserAndCompanyAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden id="tin" name="tin" />
	<s:hidden id="hidUserUniqueId" name="hidUserUniqueId" />
	<s:hidden id="indexRow" name="indexRow" />
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>ผูกความสัมพันธ์ผู้ใช้กับบริษัท</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<div class="col-sm-2 text-right">
					<label for="companyName" class="control-label">
						ชื่อบริษัท  <span style="color: red;"><b>*</b></span>:
					</label>
				</div>
				<div class="col-sm-4 text-left">
					<s:if test="relationUserAndCompanyList!=null">
						<s:property value="companyName"/>
						<s:hidden name="companyName" id="companyName"/>
					</s:if>
					<s:else>
						<s:textfield id="companyName" name="companyName" cssClass="form-control auto-complete" placeholder="ชื่อบริษัท" maxlength="100"  />
						<script>$("#companyName").focus();</script>
					</s:else>
				</div>
				<div class="col-sm-5 text-left">
					<s:if test="relationUserAndCompanyList==null">
						<button type="button" id="btnSearch" name="btnSearch" class="btn btn-success">
					    	<span class="glyphicon glyphicon-search"></span> ค้นหา
					    </button>
				    </s:if>
				</div>
			</div>
			<div class="row">
				<div id="relUserNcomp-list" class="col-sm-12">
			    	<%@include file="relUserNcomp-list.jsp" %>
				</div>
			</div>
		</div>
	</div>
</s:form>