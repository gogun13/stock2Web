<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#frm");
		
		$('#btnSearch').click(function(){ 
			var la_validate  = new Array( "productName:สินค้า");
			try{
				if(!gp_validateEmptyObj(la_validate)){
					return false;
				}
				
				$("#pageIndex").val("1");
				$("#command").val("search");
				gp_postAjaxRequest("#frm", "#adjustStockHistory-list");
			}catch(e){
				console.error("btnSearch", e.stack);
			}
			
		});
		
		$("#productName").keypress(function(e) {
		    if(e.which == 13) {
		    	$('#btnSearch').click();
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
		
	});
	
	function changePage(pageNum) {
		try{
			$("#pageIndex").val(pageNum);
	        $("#command").val("search");
	        gp_postAjaxRequest("#frm", "#adjustStockHistory-list");
		}catch(e){
			console.error("changePage", e.stack);
		}
    }
	
</script>
<s:form name="frm" id="frm" action="adjustStockHistoryAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden id="pageIndex" name="pageIndex" />
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>ประวัติการปรับยอดสต๊อกสินค้า</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="productName" class="control-label">
							สินค้า<span style="color: red;">*</span>  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="productName" name="adjustStockBean.productName" cssClass="form-control auto-complete" placeholder="สินค้า" />
						<script>$("#productName").focus();</script>
					</div>
					<div class="col-sm-4 text-left">
						<button type="button" id="btnSearch" name="btnSearch" class="btn btn-success">
					    	<span class="glyphicon glyphicon-search"></span> ค้นหา
					    </button>
					    <button type="button" name="btnReset" id="btnReset" class="btn btn-default  btn-md">
					    	<span class="glyphicon glyphicon-off"></span> เริ่มใหม่
					    </button>
					</div>
				</div>
			</div>
			<div class="row">
				<div id="adjustStockHistory-list" class="col-sm-12">
			    	<%@include file="adjustStockHistory-list.jsp" %>
				</div>
			</div>
		</div>
	</div>
</s:form>