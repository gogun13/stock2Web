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
				$("#command").val("searchCustomerDetails");
				gp_postAjaxRequest("#frm", "#customerSearch-list");
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
		
		$("#cusCode,#fullName,#tel,#cusStatus").keypress(function(e) {
		    if(e.which == 13) {
		    	$('#btnSearch').click();
		    }
		});
		
	});
	
	function changePage(pageNum) {
		try{
			$("#pageIndex").val(pageNum);
	        $("#command").val("searchCustomerDetails");
	        gp_postAjaxRequest("#frm", "#customerSearch-list");
		}catch(e){
			console.error("changePage", e.stack);
		}
    }
	
	function lp_gotoDetail(av_cusCode){
		try{
			$("#hidCusCode").val(av_cusCode);
			$("#command").val("gotoDetail");
			gp_postAjaxRequest("#frm", "#page_main");
		}catch(e){
			console.error("lp_gotoDetail", e.stack);
		}
	}
	
</script>
<s:form name="frm" id="frm" action="customerSearchAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden id="pageIndex" name="pageIndex" />
	<s:hidden id="hidCusCode" name="hidCusCode" />
	<s:hidden id="customerDetailsBean.tin" name="customerDetailsBean.tin" />
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>ค้นหารายละเอียดลูกค้า</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<div class="col-sm-3 text-right">
					<label for="cusCode" class="control-label">
						รหัสลูกค้า  :
					</label>
				</div>
				<div class="col-sm-3 text-left">
					<s:textfield id="cusCode" name="customerDetailsBean.cusCode" cssClass="form-control" placeholder="รหัสลูกค้า" />
					<script>$("#cusCode").focus();</script>
				</div>
				<div class="col-sm-2 text-right">
					<label for="fullName" class="control-label">
						ชื่อ-นามสกุล :
					</label>
				</div>
				<div class="col-sm-3 text-left">
					<s:textfield id="fullName" name="customerDetailsBean.fullName" cssClass="form-control auto-complete" placeholder="ชื่อ-นามสกุล" />
				</div>
				<div class="col-sm-1 text-left"></div>
				<div class="col-sm-3 text-right">
					<label for="cusStatus" class="control-label">
						สถานะ  :
					</label>
				</div>
				<div class="col-sm-3 text-left">
					<s:select id="cusStatus" name="customerDetailsBean.cusStatus" cssClass="form-control"
	                      list="statusCombo" listKey="code" listValue="desc" />
				</div>
				<div class="col-sm-2 text-right">
					<label for="tel" class="control-label">
						เบอร์โทรศัพท์ :
					</label>
				</div>
				<div class="col-sm-3 text-left">
					<s:textfield id="tel" name="customerDetailsBean.tel" cssClass="form-control" placeholder="เบอร์โทรศัพท์" />
				</div>
				<div class="col-sm-1 text-right"></div>
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
				<div id="customerSearch-list" class="col-sm-12">
			    	<%@include file="customerSearch-list.jsp" %>
				</div>
			</div>
		</div>
	</div>
</s:form>