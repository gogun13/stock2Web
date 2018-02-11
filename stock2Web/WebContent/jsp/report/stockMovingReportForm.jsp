<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#frm");
		
		$("#hisDateFrom").focus();
		
		$('#btnShowData').click(function(){ 
			var la_validate             = new Array( "hisDateFrom:วันที่เริ่มค้นหา", "hisDateTo:วันที่สิ้นสุดค้นหา");
			try{
				if(!gp_validateEmptyObj(la_validate)){
					return false;
				}
				
				$("#command").val("showData");
				gp_dialogPopUpPdf('<s:url action="stockMovingReportAction.action" />?' + $("#frm").serialize(), "รายงานเคลื่อนไหว Stock สินค้า");
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
		
		$("#hisDateFrom,#hisDateTo,#productTypeName,#productGroupName").keypress(function(e) {
		    if(e.which == 13) {
		    	$('#btnShowData').click();
		    }
		});
	});
	
</script>
<s:form name="frm" id="frm" action="stockMovingReportAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden id="pageIndex" name="pageIndex" />
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>รายงานเคลื่อนไหว Stock สินค้า</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="hisDateFrom" class="control-label">
							วันที่ขาย <span style="color: red;"><b>*</b></span>:
						</label>
					</div>
					<div class="col-sm-4 text-left">
						<s:textfield id="hisDateFrom" name="productQuanHistoryBean.hisDateFrom" cssClass="form-control dateFormat" cssStyle="width:40%;display:-webkit-inline-box;" placeholder="DD/MM/YYYY" />
						-
						<s:textfield id="hisDateTo" name="productQuanHistoryBean.hisDateTo" cssClass="form-control dateFormat" cssStyle="width:40%;display:-webkit-inline-box;" placeholder="DD/MM/YYYY" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="productTypeName" class="control-label">
							หมวดสินค้า :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="productTypeName" name="productQuanHistoryBean.productTypeName" cssClass="form-control auto-complete" maxlength="200" placeholder="หมวดสินค้า" />
					</div>
				</div>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="productGroupName" class="control-label">
							หมู่สินค้า :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="productGroupName" name="productQuanHistoryBean.productGroupName" cssClass="form-control auto-complete" maxlength="200" placeholder="หมู่สินค้า" />
					</div>
					<div class="col-sm-3 text-right">
						<label for="productName" class="control-label">
							ชื่อสินค้า :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="productName" name="productQuanHistoryBean.productName" cssClass="form-control auto-complete" maxlength="200" placeholder="ชื่อสินค้า" />
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