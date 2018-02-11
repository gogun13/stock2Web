<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#frm");
		
		$("#reciveDateFrom").focus();
		
		$('#btnShowData').click(function(){ 
			var la_validate             = new Array( "reciveDateFrom:วันที่เริ่มค้นหา", "reciveDateTo:วันที่สิ้นสุดค้นหา");
			try{
				if(!gp_validateEmptyObj(la_validate)){
					return false;
				}
				
				$("#command").val("showData");
				gp_dialogPopUpPdf('<s:url action="historyPurchasedByDealerReportAction.action" />?' + $("#frm").serialize(), "รายงานประวัติการซื้อตามผู้จำหน่าย");
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
		
		$("#reciveDateFrom,#reciveDateTo,#vendorName,#branchName").keypress(function(e) {
		    if(e.which == 13) {
		    	$('#btnShowData').click();
		    }
		});
	});
	
</script>
<s:form name="frm" id="frm" action="historyPurchasedByDealerReportAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden id="pageIndex" name="pageIndex" />
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>รายงานประวัติการซื้อตามผู้จำหน่าย</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="reciveDateFrom" class="control-label">
							วันที่สั่งของ <span style="color: red;"><b>*</b></span>:
						</label>
					</div>
					<div class="col-sm-4 text-left">
						<s:textfield id="reciveDateFrom" name="historyPurchasedByDealerReportBean.reciveDateFrom" cssClass="form-control dateFormat" cssStyle="width:40%;display:-webkit-inline-box;" placeholder="DD/MM/YYYY" />
						-
						<s:textfield id="reciveDateTo" name="historyPurchasedByDealerReportBean.reciveDateTo" cssClass="form-control dateFormat" cssStyle="width:40%;display:-webkit-inline-box;" placeholder="DD/MM/YYYY" />
					</div>
				</div>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="vendorName" class="control-label">
							บริษัทที่สั่งซื้อ :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="vendorName" name="historyPurchasedByDealerReportBean.vendorName" cssClass="form-control auto-complete" placeholder="บริษัทที่สั่งซื้อ" />
					</div>
					<div class="col-sm-3 text-right">
						<label for="branchName" class="control-label">
							สาขา :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="branchName" name="historyPurchasedByDealerReportBean.branchName" cssClass="form-control auto-complete" placeholder="สาขา" />
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