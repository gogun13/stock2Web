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
				$("#command").val("searchCompanyDetails");
				gp_postAjaxRequest("#frm", "#companyDetailsSearch-list");
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
		
		$("#companyName,#tin").keypress(function(e) {
		    if(e.which == 13) {
		    	$('#btnSearch').click();
		    }
		});
		
	});
	
	function changePage(pageNum) {
		try{
			$("#pageIndex").val(pageNum);
	        $("#command").val("searchCompanyDetails");
	        gp_postAjaxRequest("#frm", "#companyDetailsSearch-list");
		}catch(e){
			console.error("changePage", e.stack);
		}
    }
	
	function lp_gotoDetail(av_tin){
		try{
			$("#hidTin").val(av_tin);
			$("#command").val("gotoDetail");
			gp_postAjaxRequest("#frm", "#page_main");
		}catch(e){
			console.error("lp_gotoDetail", e.stack);
		}
	}
	
</script>
<s:form name="frm" id="frm" action="companyDetailsSearchAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden id="pageIndex" name="pageIndex" />
	<s:hidden id="hidTin" name="hidTin" />
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>ค้นหารายละเอียดบริษัท</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<div class="col-sm-3 text-right">
					<label for="companyName" class="control-label">
						ชื่อบริษัท  :
					</label>
				</div>
				<div class="col-sm-3 text-left">
					<s:textfield id="companyName" name="companyDetailsBean.companyName" cssClass="form-control auto-complete" placeholder="ชื่อบริษัท" maxlength="100" />
					<script>$("#companyName").focus();</script>
				</div>
				<div class="col-sm-2 text-right">
					<label for="tin" class="control-label">
						เลขประจำตัวผู้เสียภาษี :
					</label>
				</div>
				<div class="col-sm-3 text-left">
					<s:textfield id="tin" name="companyDetailsBean.tin" cssClass="form-control auto-complete" placeholder="เลขประจำตัวผู้เสียภาษี" maxlength="13" />
				</div>
				<div class="col-sm-1 text-left"></div>
				<div class="col-sm-3 text-right">
					<label for="companyStatus" class="control-label">
						สถานะ  :
					</label>
				</div>
				<div class="col-sm-3 text-left">
					<s:select id="companyStatus" name="companyDetailsBean.companyStatus" cssClass="form-control"
	                      list="statusCombo" listKey="code" listValue="desc" />
				</div>
				<div class="col-sm-6 text-left"></div>
				<div class="col-sm-11 text-right">
					<button type="button" id="btnSearch" name="btnSearch" class="btn btn-success">
				    	<span class="glyphicon glyphicon-search"></span> ค้นหา
				    </button>
					<button type="button" name="btnReset" id="btnReset" class="btn btn-default">
				    	<span class="glyphicon glyphicon-off"></span> เริ่มใหม่
				    </button>
				</div>
				<div class="col-sm-1 text-right"></div>
			</div>
			<div class="row">
				<div id="companyDetailsSearch-list" class="col-sm-12">
			    	<%@include file="companyDetailsSearch-list.jsp" %>
				</div>
			</div>
		</div>
	</div>
</s:form>