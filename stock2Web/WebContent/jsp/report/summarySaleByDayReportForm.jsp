<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#frm");
		
		$("#invoiceDateFrom").focus();
		
		$('#btnShowData').click(function(){ 
			var la_validate             = new Array( "invoiceDateFrom:วันที่เริ่มค้นหา", "invoiceDateTo:วันที่สิ้นสุดค้นหา");
			try{
				if(!gp_validateEmptyObj(la_validate)){
					return false;
				}
				
				$("#command").val("showData");
				gp_dialogPopUpPdf('<s:url action="summarySaleByDayReportAction.action" />?' + $("#frm").serialize(), "รายงานสรุปยอดขายประจำวัน");
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
		
		$("#invoiceDateFrom,#invoiceDateTo").keypress(function(e) {
		    if(e.which == 13) {
		    	$('#btnShowData').click();
		    }
		});
	});
	
</script>
<s:form name="frm" id="frm" action="summarySaleByDayReportAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden id="pageIndex" name="pageIndex" />
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>รายงานสรุปยอดขายประจำวัน</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="invoiceDateFrom" class="control-label">
							วันที่ขาย <span style="color: red;"><b>*</b></span>:
						</label>
					</div>
					<div class="col-sm-4 text-left">
						<s:textfield id="invoiceDateFrom" name="summarySaleByDayReportBean.invoiceDateFrom" cssClass="form-control dateFormat" cssStyle="width:40%;display:-webkit-inline-box;" placeholder="DD/MM/YYYY" />
						-
						<s:textfield id="invoiceDateTo" name="summarySaleByDayReportBean.invoiceDateTo" cssClass="form-control dateFormat" cssStyle="width:40%;display:-webkit-inline-box;" placeholder="DD/MM/YYYY" />
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