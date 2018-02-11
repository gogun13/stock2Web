<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#frm");
		
		$('#btnShowData').click(function(){ 
			var la_validate             = new Array( "productTypeName:หมวดสินค้า");
			try{
				if(!gp_validateEmptyObj(la_validate)){
					return false;
				}
				
				$("#command").val("showData");
				gp_dialogPopUpPdf('<s:url action="stockBalanceReportAction.action" />?' + $("#frm").serialize(), "รายงานยอด Stock คงเหลือ");
			}catch(e){
				console.error("btnShowData", e.stack);
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
		
		$("#productTypeName,#productGroupName").keypress(function(e) {
		    if(e.which == 13) {
		    	$('#btnShowData').click();
		    }
		});
	});
	
</script>
<s:form name="frm" id="frm" action="stockBalanceReportAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden id="pageIndex" name="pageIndex" />
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>รายงานยอด Stock คงเหลือ</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="productTypeName" class="control-label">
							หมวดสินค้า<span style="color: red;"><b>*</b></span> :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="productTypeName" name="stockBalanceReportBean.productTypeName" cssClass="form-control auto-complete" maxlength="200" placeholder="หมวดสินค้า" />
						<script>$("#productTypeName").focus();</script>
					</div>
					<div class="col-sm-2 text-right">
						<label for="productGroupName" class="control-label">
							หมู่สินค้า :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="productGroupName" name="stockBalanceReportBean.productGroupName" cssClass="form-control auto-complete" maxlength="200" placeholder="หมู่สินค้า" />
					</div>
				</div>
				<div class="col-sm-12">
					<div class="col-sm-11 text-right">
						<button type="button" id="btnShowData" name="btnShowData" class="btn btn-success">
					    	<span class="glyphicon glyphicon-list"></span> แสดงข้อมูล
					    </button>
						<button type="button" name="btnReset" id="btnReset" class="btn btn-default">
					    	<span class="glyphicon glyphicon-off"></span> เริ่มใหม่
					    </button>
					</div>
					<div class="col-sm-1 text-right"></div>
				</div>
			</div>
		</div>
	</div>
</s:form>