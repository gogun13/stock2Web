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
				gp_postAjaxRequest("#frm", "#invoiceCashSearch-list");
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
		
		$("#invoiceCode,#invoiceDateForm,#invoiceDateTo,#cusFullName,#invoiceStatus,#invoiceType").keypress(function(e) {
		    if(e.which == 13) {
		    	$('#btnSearch').click();
		    }
		});
		
	});
	
	function changePage(pageNum) {
		try{
			$("#pageIndex").val(pageNum);
	        $("#command").val("search");
	        gp_postAjaxRequest("#frm", "#invoiceCashSearch-list");
		}catch(e){
			console.error("changePage", e.stack);
		}
    }
	
	function lp_gotoDetail(av_invoiceCode){
		try{
			$("#hidInvoiceCode").val(av_invoiceCode);
			$("#command").val("gotoDetail");
			gp_postAjaxRequest("#frm", "#page_main");
		}catch(e){
			console.error("lp_gotoDetail", e.stack);
		}
	}
	
</script>
<s:form name="frm" id="frm" action="invoiceCashSearchAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden id="pageIndex" name="pageIndex" />
	<s:hidden id="hidInvoiceCode" name="hidInvoiceCode" />
	<s:hidden id="invoiceCashMasterBean.tin" name="invoiceCashMasterBean.tin" />
	<s:hidden id="invoiceCredit" name="invoiceCashMasterBean.invoiceCredit" />
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>ค้นหารายละเอียดการขายเงินสด</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="invoiceCode" class="control-label">
							เลขที่ใบเสร็จ  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="invoiceCode" name="invoiceCashMasterBean.invoiceCode" cssClass="form-control" placeholder="เลขที่ใบเสร็จ" />
						<script>$("#invoiceCode").focus();</script>
					</div>
					<div class="col-sm-2 text-right">
						<label for="invoiceDateForm" class="control-label">
							วันที่ขาย:
						</label>
					</div>
					<div class="col-sm-4 text-left">
						<s:textfield id="invoiceDateForm" name="invoiceCashMasterBean.invoiceDateForm" cssClass="form-control dateFormat" cssStyle="width:40%;display:-webkit-inline-box;" placeholder="DD/MM/YYYY" />
						-
						<s:textfield id="invoiceDateTo" name="invoiceCashMasterBean.invoiceDateTo" cssClass="form-control dateFormat" cssStyle="width:40%;display:-webkit-inline-box;" placeholder="DD/MM/YYYY" />
					</div>
				</div>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="cusFullName" class="control-label">
							ชื่อลุกค้า  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="cusFullName" name="invoiceCashMasterBean.cusFullName" cssClass="form-control auto-complete" placeholder="ชื่อลุกค้า" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="invoiceStatus" class="control-label">
							สถานะ:
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:select id="invoiceStatus" name="invoiceCashMasterBean.invoiceStatus" cssClass="form-control"
	                      list="statusCombo" listKey="code" listValue="desc" />
					</div>
				</div>
				<div class="col-sm-12">
					<%--
					<div class="col-sm-2 text-right">
						<label for="invoiceCredit" class="control-label">
							เลขที่ใบวางบิล  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="invoiceCredit" name="invoiceCashMasterBean.invoiceCredit" cssClass="form-control" placeholder="เลขที่ใบวางบิล" />
					</div>
					--%>
					<div class="col-sm-2 text-right">
						<label for="invoiceType" class="control-label">
							ประเภทการขาย  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:select id="invoiceType" name="invoiceCashMasterBean.invoiceType" cssClass="form-control"
	                      list="priceTypeList" listKey="code" listValue="desc" />
					</div>
					<div class="col-sm-7"></div>
				</div>
				<div class="col-sm-12">
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
			</div>
			<div class="row">
				<div id="invoiceCashSearch-list" class="col-sm-12">
			    	<%@include file="invoiceCashSearch-list.jsp" %>
				</div>
			</div>
		</div>
	</div>
</s:form>