<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#frm");
		
		$("#invoiceMonth").focus();
		
		$('#btnShowData').click(function(){ 
			var la_validate             = new Array( "invoiceMonth:เดือน/ปี");
			
			try{
				if(!gp_validateEmptyObj(la_validate)){
					return false;
				}
				
				$("#command").val("showData");
				gp_dialogPopUpPdf('<s:url action="summarySaleByMonthReportAction.action" />?' + $("#frm").serialize(), "รายงานสรุปยอดขายประจำเดือน");
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
		
		$("#invoiceMonth").keypress(function(e) {
		    if(e.which == 13) {
		    	$('#btnShowData').click();
		    }
		});
	});
	
</script>
<s:form name="frm" id="frm" action="summarySaleByMonthReportAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden id="pageIndex" name="pageIndex" />
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>รายงานสรุปยอดขายประจำเดือน</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="invoiceMonth" class="control-label">
							เดือน/ปี <span style="color: red;"><b>*</b></span>:
						</label>
					</div>
					<div class="col-sm-2 text-left">
						<s:textfield id="invoiceMonth" name="summarySaleByMonthReportBean.invoiceMonth" onblur="gp_checkMonth(this);" cssClass="form-control" placeholder="MM/YYYY" />
					</div>
					<div class="col-sm-6 text-left">
						<button type="button" id="btnShowData" name="btnShowData" class="btn btn-success">
					    	<span class="glyphicon glyphicon-list"></span> แสดงข้อมูล
					    </button>
						<button type="button" name="btnReset" id="btnReset" class="btn btn-default">
					    	<span class="glyphicon glyphicon-off"></span> เริ่มใหม่
					    </button>
					</div>
				</div>
			</div>
		</div>
	</div>
</s:form>