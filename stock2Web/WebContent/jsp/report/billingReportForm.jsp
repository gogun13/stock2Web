<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#frm");
		
		lp_onChangeInvoiceType();
		
		$('#btnShowData').click(function(){ 
			var la_validate             = new Array( "cusCode:ลูกค้า", "invoiceDateForm:วันที่เริ่มค้นหา", "invoiceDateTo:วันที่สิ้นสุดค้นหา");
			
			try{
				if(!gp_validateEmptyObj(la_validate)){
					return false;
				}
				
				$("#command").val("showData");
				gp_dialogPopUpPdf('<s:url action="billingReportAction.action" />?' + $("#frm").serialize(), "รายงานใบวางบิล");
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
		
		$("#invoiceCode,#invoiceDateForm,#invoiceDateTo,#invoiceType").keypress(function(e) {
		    if(e.which == 13) {
		    	$('#btnShowData').click();
		    }
		});
	});
	
	function lp_openCustomerLookUp(){
		try{
			$("#command").val("openCustomerLookUp");
			gp_postAjaxRequest("#frm", "#dialog");
		}catch(e){
			console.error("lp_openCustomerLookUp", e.stack);
		}
	}
	
	function lp_returnCustomerData(av_cusCode){
		try{
			$("#cusCode").val(av_cusCode);
			
			$( "#dialogLookUp" ).dialog( "close" );
		}catch(e){
			console.error("lp_returnCustomerData", e.stack);
		}
	}
	
	function lp_onChangeInvoiceType(){
		try{
			if($("#invoiceType").val()=="N"){
				$("#spanIncludeVat").show();
			}else{
				$( '#includeVat' ).prop('checked', false);
				$("#spanIncludeVat").hide();
			}
		}catch(e){
			console.error("lp_onChangeInvoiceType", e.stack);
		}
	}
	
</script>
<s:form name="frm" id="frm" action="billingReportAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden id="pageIndex" name="pageIndex" />
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>รายงานใบวางบิล</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="cusCode" class="control-label">
							ลูกค้า <span style="color: red;"><b>*</b></span>:
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="cusCode" name="invoiceCreditMasterBean.cusCode" cssClass="form-control input-disabled" readonly="true" placeholder="รหัสลูกค้า" cssStyle="width:80%;display:-webkit-inline-box;" />
						<img alt="ค้นหาลุกค้า" title="ค้นหาลุกค้า" src="<c:url value='/img/lookup.png'/>" width="24" height="24" border="0" style="cursor: pointer;" onclick="lp_openCustomerLookUp();" />
					</div>
					<div class="col-sm-3 text-right">
						<label for="invoiceCode" class="control-label">
							เลขที่บิล :
						</label>
					</div>
					<div class="col-sm-2 text-left">
						<s:textfield id="invoiceCode" name="invoiceCreditMasterBean.invoiceCode" cssClass="form-control" placeholder="เลขที่บิล" />
					</div>
				</div>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="invoiceDateForm" class="control-label">
							วันที่ขาย <span style="color: red;"><b>*</b></span>:
						</label>
					</div>
					<div class="col-sm-4 text-left">
						<s:textfield id="invoiceDateForm" name="invoiceCreditMasterBean.invoiceDateForm" cssClass="form-control dateFormat" cssStyle="width:40%;display:-webkit-inline-box;" placeholder="DD/MM/YYYY" />
						-
						<s:textfield id="invoiceDateTo" name="invoiceCreditMasterBean.invoiceDateTo" cssClass="form-control dateFormat" cssStyle="width:40%;display:-webkit-inline-box;" placeholder="DD/MM/YYYY" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="invoiceType" class="control-label">
							ประเภทบิล:
						</label>
					</div>
					<div class="col-sm-2 text-left">
						<s:select id="invoiceType" name="invoiceCreditMasterBean.invoiceType" cssClass="form-control"
	                      list="priceTypeList" listKey="code" listValue="desc" onchange="lp_onChangeInvoiceType();" />
					</div>
					<div id="spanIncludeVat" class="col-sm-2 text-left">
						<s:checkbox id="includeVat" 
				                		name="includeVat"
				                		cssStyle="width:15px;height:15px;"
				                		value="%{includeVat}"
				                		fieldValue="true"/>
				        	&nbsp;รวม Vat
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