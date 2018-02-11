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
				$("#command").val("search");
				gp_postAjaxRequest("#frm", "#companyVendorSearch-list");
			}catch(e){
				console.error("btnSearch", e.stack);
			}
			
		});
		
		$('#btnReset').click(function(){ 
			try{
				$("#command").val("success");
				gp_postAjaxRequest("#frm", "#page_main");
			}catch(e){
				console.error("btnReset", e.stack);
			}
			
		});
		
		$("#vendorName,#branchName,#tel").keypress(function(e) {
		    if(e.which == 13) {
		    	$('#btnSearch').click();
		    }
		});
		
	});
	
	function changePage(pageNum) {
		try{
			$("#pageIndex").val(pageNum);
			$("#command").val("search");
			gp_postAjaxRequest("#frm", "#companyVendorSearch-list");
		}catch(e){
			console.error("changePage", e.stack);
		}
    }
	
	function lp_gotoDetail(av_vendorCode){
		try{
			$("#hidVendorCode").val(av_vendorCode);
			$("#command").val("gotoDetail");
			gp_postAjaxRequest("#frm", "#page_main");
		}catch(e){
			console.error("lp_gotoDetail", e.stack);
		}
	}
	
</script>
<s:form name="frm" id="frm" action="companyVendorSearchAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden id="pageIndex" name="pageIndex" />
	<s:hidden id="hidVendorCode" name="hidVendorCode" />
	<s:hidden id="companyVendorBean.tinCompany" name="companyVendorBean.tinCompany" />
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>ค้นหาบริษัทสั่งซื้อ</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<div class="col-sm-3 text-right">
					<label for="vendorName" class="control-label">
						ชื่อบริษัท  :
					</label>
				</div>
				<div class="col-sm-3 text-left">
					<s:textfield id="vendorName" name="companyVendorBean.vendorName" cssClass="form-control" placeholder="ชื่อบริษัท" />
					<script>$("#vendorName").focus();</script>
				</div>
				<div class="col-sm-2 text-right">
					<label for="branchName" class="control-label">
						สาขา :
					</label>
				</div>
				<div class="col-sm-3 text-left">
					<s:textfield id="branchName" name="companyVendorBean.branchName" cssClass="form-control" placeholder="สาขา" />
				</div>
				<div class="col-sm-1 text-left"></div>
				<div class="col-sm-3 text-right">
					<label for="tel" class="control-label">
						เบอร์โทรศัพท์  :
					</label>
				</div>
				<div class="col-sm-3 text-left">
					<s:textfield id="tel" name="companyVendorBean.tel" cssClass="form-control" placeholder="เบอร์โทรศัพท์" />
				</div>
				<div class="col-sm-6 text-right"></div>
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
				<div id="companyVendorSearch-list" class="col-sm-12">
			    	<%@include file="companyVendorSearch-list.jsp" %>
				</div>
			</div>
		</div>
	</div>
</s:form>