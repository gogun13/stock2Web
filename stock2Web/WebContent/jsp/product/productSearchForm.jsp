<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#frm");
		lp_initialForPrint();
		
		$('#btnSearch').click(function(){ 
			try{
				$("#pageIndex").val("1");
				$("#command").val("searchProductDetails");
				gp_postAjaxRequest("#frm", "#productSearch-list");
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
		
		$("#productTypeName,#productGroupName,#productName").keypress(function(e) {
		    if(e.which == 13) {
		    	$('#btnSearch').click();
		    }
		});
		
		$('#btnPrintBarCode').click(function(){ 
			try{
				if(isBlank($("#hidProductCodeForBarCode").val())){
					gp_alert("ไม่พบรหัสสินค้าที่จะพิมพ์");
				}else{
					$("#command").val("gotoProductBarCode");
					gp_postAjaxRequest("#frm", "#dialog");
				}
			}catch(e){
				console.error("btnSearch", e.stack);
			}
			
		});
		
	});
	
	function changePage(pageNum) {
		try{
			
			$("#pageIndex").val(pageNum);
			$("#command").val("searchProductDetails");
			gp_postAjaxRequest("#frm", "#productSearch-list");
		}catch(e){
			console.error("changePage", e.stack);
		}
    }
	
	function lp_gotoDetail(av_productCode){
		try{
			$("#hidProductCode").val(av_productCode);
			$("#command").val("gotoDetail");
			gp_postAjaxRequest("#frm", "#page_main");
		}catch(e){
			console.error("lp_gotoDetail", e.stack);
		}
	}
	
	function lp_setBarCodeForPrint(av_productCode){
		
		var lv_val = "";
		var la_array;
		var lv_totalProduct = 1;
		
		try{
			
			if(isBlank($("#hidProductCodeForBarCode").val())){
				$("#hidProductCodeForBarCode").val(av_productCode);
				$("#sp_barcodeButton").html("(" + lv_totalProduct + ")");
			}else{
				la_array = $("#hidProductCodeForBarCode").val().split(",");
				
				if($.inArray( av_productCode, la_array ) < 0){
					lv_val = $("#hidProductCodeForBarCode").val() + "," + av_productCode;
					$("#hidProductCodeForBarCode").val(lv_val);
					lv_totalProduct = la_array.length + 1;
					$("#sp_barcodeButton").html("(" + lv_totalProduct + ")");
				}
			}
			
		}catch(e){
			console.error("lp_setBarCodeForPrint", e.stack);
		}
	}
	
	function lp_initialForPrint(){
		
		var la_array;
		
		try{
			
			if(!isBlank($("#hidProductCodeForBarCode").val())){
				la_array = $("#hidProductCodeForBarCode").val().split(",");
				$("#sp_barcodeButton").html("(" + la_array.length + ")");
			}
			
		}catch(e){
			console.error("lp_initialForPrint", e.stack);
		}
	}
	
</script>
<s:form name="frm" id="frm" action="productSearchAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden id="pageIndex" name="pageIndex" />
	<s:hidden id="hidProductCode" name="hidProductCode" />
	<s:hidden id="hidProductCodeForBarCode" name="hidProductCodeForBarCode" />
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>ค้นหารายละเอียดสินค้า</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<div class="col-sm-3 text-right">
					<label for="productTypeName" class="control-label">
						หมวดสินค้า :
					</label>
				</div>
				<div class="col-sm-3 text-left">
					<s:textfield id="productTypeName" name="productmasterBean.productTypeName" cssClass="form-control auto-complete" maxlength="200" placeholder="หมวดสินค้า" />
					<script>$("#productTypeName").focus();</script>
				</div>
				<div class="col-sm-2 text-right">
					<label for="productGroupName" class="control-label">
						หมู่สินค้า :
					</label>
				</div>
				<div class="col-sm-3 text-left">
					<s:textfield id="productGroupName" name="productmasterBean.productGroupName" cssClass="form-control auto-complete" maxlength="200" placeholder="หมู่สินค้า" />
				</div>
				<div class="col-sm-1 text-left"></div>
				<div class="col-sm-3 text-right">
					<label for="productName" class="control-label">
						ชื่อสินค้า  :
					</label>
				</div>
				<div class="col-sm-3 text-left">
					<s:textfield id="productName" name="productmasterBean.productName" cssClass="form-control auto-complete" maxlength="255" placeholder="ชื่อสินค้า" />
				</div>
				<div class="col-sm-6 text-left"></div>
				<div class="col-sm-11 text-right">
					<button type="button" id="btnSearch" name="btnSearch" class="btn btn-success">
				    	<span class="glyphicon glyphicon-search"></span> ค้นหา
				    </button>
					<button type="button" name="btnReset" id="btnReset" class="btn btn-default">
				    	<span class="glyphicon glyphicon-off"></span> เริ่มใหม่
				    </button>
				    <button type="button" name="btnPrintBarCode" id="btnPrintBarCode" class="btn btn-primary">
				    	<span class="glyphicon glyphicon-barcode"></span> พิมพ์รหัสสินค้า<span id="sp_barcodeButton"></span>
				    </button>
				</div>
				<div class="col-sm-1 text-right"></div>
			</div>
			<div class="row">
				<div id="productSearch-list" class="col-sm-12">
			    	<%@include file="productSearch-list.jsp" %>
				</div>
			</div>
		</div>
	</div>
</s:form>