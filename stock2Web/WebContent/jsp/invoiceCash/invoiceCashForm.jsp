<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#frm");
		
		if($("#pageMode").val()=="UPDATE"){
			lp_setModeEdit();
		}else if($("#pageMode").val()=="EDIT_PAGE"){
			$("#invoiceStatus").prop("disabled", true);
		}else{
			lp_setModeNew();
		}
		
		$('#btnReset').click(function(){ 
			try{
				$("#command").val("success");
				
				if($("#pageMode").val()=="UPDATE"){
					$("#command").val("getDetail");
				}
				
				gp_postAjaxRequest("#frm", "#page_main");
			}catch(e){
				console.error("btnReset", e.stack);
			}
		});
		
		$('#btnSave').click(function(){ 
			
			var la_validate     = new Array( "invoiceDate:วันที่ขาย");
			var lv_ret	 		= true;
			var lv_inventory	= 0.00;
			var lv_notEnough	= "";
		    
			try{
				if(!gp_validateEmptyObj(la_validate)){
					return false;
				}
				
				/*Begin รหัสลูกค้า*/
				if(isBlank($("#cusCode").val()) && !isBlank($("#cusCodeDis").val().trim())){ 
					gp_alert("ระบุรหัสลูกค้าผิด", function() { 
						$("#cusCodeDis").focus();
	    		    });
	                return false;
				}
				/*End รหัสลูกค้า*/
				
				/*Begin พนักงานขาย*/
				if(isBlank($("#userUniqueId").val()) && !isBlank($("#userFullName").val())){ 
					gp_alert("ระบุพนักงานขายผิด", function() { 
						$("#userFullName").focus();
	    		    });
	                return false;
				}
				/*End พนักงานขาย*/
				
				$("input[name=rowIndex]").each(function() {
					var rowIndex = $(this).val();
		            if(isBlank($("#productName" + rowIndex).val())){
		            	gp_alert("ชื่อสินค้าห้ามเป็นค่าว่าง", function(){
		            		$("#productName" + rowIndex).focus();
		            	});
		            	lv_ret = false;
		            	return false;
		            }else if(isBlank($("#productCode" + rowIndex).val())){
		            	gp_alert($("#productName" + rowIndex).val() + "ไม่มีอยู่จริงในระบบ", function(){
		            		$("#productName" + rowIndex).focus();
		            	});
		            	lv_ret = false;
		            	return false;
		            }else if(gp_parseFloat($("#quantity" + rowIndex).val()) < 1){
		            	gp_alert($("#productName" + rowIndex).val() + "ยังไมได้ระบุปริมาณ", function() { 
		            		$("#quantity" + rowIndex).focus();
		    		    });
		            	lv_ret = false;
		            	return false;
		            }
		            
		            lv_inventory = gp_parseFloat($("#inventory" + rowIndex).val()) - gp_parseFloat($("#quantity" + rowIndex).val());
					if(lv_inventory < 0){
						if(lv_notEnough==""){
							lv_notEnough += "-&nbsp;" + $("#productName" + rowIndex).val();
						}else{
							lv_notEnough += "<br/>-&nbsp;" + $("#productName" + rowIndex).val();
						}
					}
					
		            lv_ret = true;
		        });
				
				if(!lv_ret)return false;
				
				gp_progressBarOnPopUp();
				if(lv_notEnough!=""){
					gp_confirm("<b>สินค้าเหลือไม่เพียงพอจำหน่าย คุณต้องการทำรายการต่อ ?</b><br/>" + lv_notEnough
							, function(){lp_save();}
							, function(){gp_progressBarOffPopUp();}
							, false
							, 600
							, 300);
				}else{
					lp_save();
				}
				
			}catch(e){
				console.error("btnSave", e.stack);
			}
		});
		
		$('#btnBack').click(function(){
			try{
				$("#command").val("onBack");
				if($("#updateCredit").val()=="Y"){
					$("#command").val("onBackUpdateCredit");
				}
				gp_postAjaxRequest("#frm", "#page_main");
				
			}catch(e){
				console.error("btnBack", e.stack);
			}
		});
		
		$('#btnCancel').click(function(){
			try{
				gp_progressBarOnPopUp();
				gp_confirm("คุณแน่ใจว่าต้องการยกเลิกรายการขายนี้"
						, function(){lp_cancel();}
						, function(){gp_progressBarOffPopUp();}
						, false
						, 600
						, 200);
				
			}catch(e){
				console.error("btnCancel", e.stack);
			}
		});
		
		$('#btnSearch').click(function(){
			
			try{
				
				if(isBlank($("#productCodeDis").val())){
					$("#productCodeDis").focus();
					return;
				}
				
				if(!(gp_checkThaiLetter($("#productCodeDis").val()))){
					alert("รหัสสินค้าต้องเปนภาษาอังกฤษหรือตัวเลขเท่านั้น !!", function() { 
						$("#productCodeDis").val('');
	        			$("#productCodeDis").focus();
	    		    });
					return;
				}
				
				
				$("#command").val("getProductDetailByCodeDis");
				gp_postAjaxRequest("#frm", "#invoiceCash-list",function(){
					$("#productCodeDis").val('');
					$("#productCodeDis").focus();
				});
				
			}catch(err){
				console.error("btnSearch", e.stack);
			}
		});
		
		$("#productCodeDis").keypress(function(e) {
		    if(e.which == 13) {
		    	$('#btnSearch').click();
		    }
		});
		
		$('#btnPrint').click(function(){ 
			
			var h				= "";
			
			try{
				
				var lv_action = '<s:url action="invoiceCashAction.action" />';
				
				h = '<iframe name="printSection" '
					  + '		 id="printSection"'
					  + '		 src="'+lv_action+'?command=genPdf&hidInvoiceCode='+gp_sanitizeURLString($("#hidInvoiceCode").val()) + '"'
					  + '		 scrolling="yes"'
					  + '		 frameborder="0"'
					  + '		 width="0"'
					  + '		 height="0">'
					  + '</iframe>';
					
				$("#printDiv").html('');
				$("#printDiv").html(h);
				
				$('#printSection').on("load", function (e){
					var lo_pdf = document.getElementById("printSection");
					lo_pdf.focus();
					lo_pdf.contentWindow.print();
					return false;
				});
			}catch(e){
				console.error("btnPrint", e.stack);
			}
		});
		
		$('#btnEdit').click(function(){
			try{
				$("#command").val("getDetail");
				$("#pageMode").val("EDIT_PAGE");
				gp_postAjaxRequest("#frm", "#page_main");
				
			}catch(e){
				console.error("btnEdit", e.stack);
			}
		});
		
		$('#btnCancelEdit').click(function(){
			try{
				$("#command").val("getDetail");
				$("#pageMode").val("UPDATE");
				gp_postAjaxRequest("#frm", "#page_main");
				
			}catch(e){
				console.error("btnCancelEdit", e.stack);
			}
		});
		
		$('#btnUpdateCredit').click(function(){
			try{
				gp_progressBarOnPopUp();
				gp_confirm("คุณต้องการปรับปรุงงบการขายเงินเชื่อรายการนี้ใช่หรือไม่?"
						, function(){lp_updateCredit();}
						, function(){gp_progressBarOffPopUp();}
						, false
						, 600
						, 200);
				
			}catch(e){
				console.error("btnCancel", e.stack);
			}
		});
		
		$('body').on('focus', '#productCodeDis',function(event){
			$('html, body').animate({
		        scrollTop: parseInt($(this).offset().top) - 115
		    }, 800);
		    
		});
		
		$(document).keydown(function(e) {
			var x = e.which || e.keyCode;
			if(x==113){//F2
				$("#productCodeDis").val('');
				$("#productCodeDis").focus();
			}
		});
		
	});
	
	function lp_updateCredit(){
		try{
			$("#command").val("onUpdateCredit");
			gp_postAjaxRequest("#frm", "",function(data){
				var jsonObj = JSON.parse(data);
				
				gp_alert("ปรับปรุงงบการขายเงินเชื่อเรียบร้อย",function(){
					$("#command").val("getDetail");
					$("#hidInvoiceCode").val(jsonObj.invoiceCode);
					gp_postAjaxRequest("#frm","#page_main",function(){
						gp_progressBarOffPopUp();
					},false);
				},false);
			},false);
		}catch(e){
    		console.error("lp_updateCredit", e.stack);
    	}
	}
	
	function lp_cancel(){
		
		try{
    		$("#command").val("onCancel");
			gp_postAjaxRequest("#frm", "",function(data){
				var jsonObj = JSON.parse(data);
				
				gp_alert("ยกเลิกเรียบร้อย",function(){
					$("#command").val("getDetail");
					$("#hidInvoiceCode").val(jsonObj.invoiceCode);
					gp_postAjaxRequest("#frm","#page_main",function(){
						gp_progressBarOffPopUp();
					},false);
				},false);
			},false);
    	}catch(e){
    		console.error("lp_cancel", e.stack);
    	}
	}
	
	function lp_deleteRecord(av_seq){
		try{
			$("#indexRow").val(av_seq);
			
			$("#command").val("deleteRecord");
			gp_postAjaxRequest("#frm", "#invoiceCash-list",function(){
				lp_invoicePrice();
			});
		}catch(e){
			console.error("lp_deleteRecord", e.stack);
		}
	}
	
	function lp_addRowtable(){
		try{
			$("#command").val("newRecord");
			gp_postAjaxRequest("#frm", "#invoiceCash-list");
		}catch(e){
			console.error("lp_addRowtable", e.stack);
		}
	}
	
	function lp_save(){
		try{
			$("#command").val("onSave");
			gp_postAjaxRequest("#frm", "",function(data){
				var jsonObj = JSON.parse(data);
				
				gp_dialogSuccess("บันทึกเรียบร้อย",function(){
					$("#command").val("getDetail");
					$("#hidInvoiceCode").val(jsonObj.invoiceCode);
					gp_postAjaxRequest("#frm","#page_main",function(){
						gp_progressBarOffPopUp();
					},false);
				},false);
			},false);
		}catch(e){
			console.error("lp_save", e.stack);
		}
	}
	
	function lp_setModeEdit(){
		
		try{
			$("form").each(function(){
				$(this).find('input:visible').prop("disabled", true);
				$(this).find('textarea:visible').prop("disabled", true);
				$(this).find('select:visible').prop("disabled", true);
			    
			});
			
			if($("#updateCredit").val()=="Y"){
				$("#btnUpdateCredit").prop("disabled", false);
			}else{
				$("#btnPrint").prop("disabled", false);
				$("#btnCancel").prop("disabled", false);
			}
			
			$("#btnBack").prop("disabled", false);
			
			
		}catch(e){
			console.error("lp_setModeEdit", e.stack);
		}
	}
	
	function lp_setModeNew(){
		try{
			$("#invoiceStatus").prop("disabled", true);
			
			$("#cusCodeDis").focus();
			$("body").scrollTop(0);
		}catch(e){
			console.error("lp_setModeNew", e.stack);
		}
	}
	
	function lp_openProductLoogUp(){
		try{
			$("#command").val("openProductLoogUp");
			gp_postAjaxRequest("#frm", "#dialog");
		}catch(e){
			console.error("lp_openProductLoogUp", e.stack);
		}
	}
	
	function lp_returnProductForLookUp(av_val){
		try{
			$("#hidProductCodeSelect").val(av_val);
			
			$("#command").val("newRecordForLookUp");
			gp_postAjaxRequest("#frm", "#invoiceCash-list",function(){
				$( "#dialogLookUp" ).dialog( "close" );
			},false);
			
		}catch(e){
			console.error("lp_returnProductForLookUp", e.stack);
		}
	}
	
	function lp_openUserLoogUp(){
		try{
			$("#command").val("openUserLoogUp");
			gp_postAjaxRequest("#frm", "#dialog");
		}catch(e){
			console.error("lp_openUserLoogUp", e.stack);
		}
	}
	
	function lp_returnData(av_userUniqueId){
		try{
			$("#userUniqueId").val(av_userUniqueId);
			
			$("#command").val("getSaleNameDetailByCode");
			gp_postAjaxRequest("#frm", "",function(data){
				jsonObj = JSON.parse(data);
				$("#userUniqueId").val(jsonObj.userUniqueId);
				$("#flagSalesman").val(jsonObj.flagSalesman);
				$("#commission").val(jsonObj.commission);
				$("#userFullName").val(jsonObj.userFullName);
			},false);
			
			$( "#dialogLookUp" ).dialog( "close" );
			
		}catch(e){
			console.error("lp_returnData", e.stack);
		}
	}
	
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
			$("#cusCodeDis").val(av_cusCode);
			
			lp_getCustomerDetail();
			
			$( "#dialogLookUp" ).dialog( "close" );
		}catch(e){
			console.error("lp_returnCustomerData", e.stack);
		}
	}
	
	function lp_getCustomerDetail(){
		try{
			if(isBlank($("#cusCodeDis").val())){
				$("#cusCode").val('');
				$("#cusGroupCode").val('');
				$("#groupSalePrice").val('');
				$("#branchName").val('');
				$("#spanCusName").html('');
				return;
			}
			
			
			$("#command").val("getCustomerDetail");
			gp_postAjaxRequest("#frm", "",function(data){
				jsonObj = JSON.parse(data);
				if(jsonObj.cusCode!=""){
    				$("#cusCode").val(jsonObj.cusCode);
    				$("#cusGroupCode").val(jsonObj.cusGroupCode);
    				$("#groupSalePrice").val(jsonObj.groupSalePrice);
    				$("#branchName").val(jsonObj.branchName);
    				$("#spanCusName").html(jsonObj.fullName);
    				$('#spanCusName').attr('class', 'correct');
    			}else{
    				$("#cusCode").val('');
    				$("#cusGroupCode").val('');
    				$("#groupSalePrice").val('');
    				$("#branchName").val('');
    				$("#spanCusName").html("รหัสลูกค้าผิด");
    				$('#spanCusName').attr('class', 'wrong');
    			}
				
				lp_getMultiProductDetailFromList();
			},false);
		}catch(e){
			console.error("lp_getCustomerDetail", e.stack);
		}
	}
	
	function lp_getProductDetailByName(av_seq){
		
		try{
			$("#indexRow").val(av_seq);
			$("#command").val("getProductDetailByName");
			
			gp_postAjaxRequest("#frm", "",function(data){
				try{
					var jsonObj = JSON.parse(data);
					
					if(jsonObj.productCode==""){
        				$("#productCode" + av_seq).val('');
            			$("#pricePerUnit" + av_seq).val('0.00');
            			$("#inventory" + av_seq).val('');
            			$("#quantity" + av_seq).val('0.00');
            			$("#unitCode" + av_seq).val('');
            			$("#unitName" + av_seq).val('');
            			$("#price" + av_seq).val('0.00');
            			$("#discount" + av_seq).val('0.00');
        			}else{
        				$("#productCode" + av_seq).val(jsonObj.productCode);
            			$("#pricePerUnit" + av_seq).val(jsonObj.pricePerUnit);
            			$("#inventory" + av_seq).val(jsonObj.inventory);
            			$("#quantity" + av_seq).val(jsonObj.quantity);
            			$("#unitCode" + av_seq).val(jsonObj.unitCode);
            			$("#unitName" + av_seq).val(jsonObj.unitName);
            			$("#discount" + av_seq).val(jsonObj.discount);
        			}
        			
					lp_calAmount(av_seq);
				}catch(e){
					console.error("After call getProductDetailByName", e.stack);
				}
			},false);
			
		}catch(e){
			console.error("lp_getProductDetailByName", e.stack);
		}
	}
	
	function lp_getMultiProductDetailFromList(){
		
		try{
			$("#command").val("getMultiProductDetailFromList");
			gp_postAjaxRequest("#frm", "#invoiceCash-list",function(data){
				try{
					$("input[name=rowIndex]").each(function() {
						lp_calAmount($(this).val());
			        });
				}catch(e){
					console.error("After call getMultiProductDetailFromList", e.stack);
				}
			},false);
		}catch(e){
			console.error("lp_getMultiProductDetailFromList", e.stack);
		}
	}
	
	function lp_calAmount(av_seq){
		
		var lv_quantity 		= 0.00;
		var lv_pricePerUnit 	= 0.00;
		var lv_discount 		= 0.00;
		var lv_price 			= 0.00;
		
		try{
			lv_quantity 		= gp_parseFloat($("#quantity" + av_seq).val());
			lv_pricePerUnit		= gp_parseFloat($("#pricePerUnit" + av_seq).val());
			lv_discount 		= 100 - gp_parseFloat($("#discount" + av_seq).val());
			
			if(lv_quantity > 0){
				lv_price = ((lv_quantity * lv_pricePerUnit) * lv_discount) / 100;
			}
			
			
			$("#price" + av_seq).val(lv_price);
			gp_initialPage("#frm");
			
			lp_invoicePrice();
			
		}catch(e){
			console.error("lp_calAmount", e.stack);
		}
	}
	
	function lp_invoicePrice(){
		
		var lv_invoicePrice		= 0.00;
		
		try{
			
			$("input[name=rowIndex]").each(function() {
				var rowIndex = $(this).val();
				lv_invoicePrice += gp_parseFloat($("#price" + rowIndex).val());
	        });
			
			$("#invoicePrice").val(lv_invoicePrice);
			gp_initialPage("#frm");
			
			lp_calInvoiceVat();
			
		}catch(e){
			console.error("lp_invoicePrice", e.stack);
		}
	}
	
	function lp_calInvoiceVat(){
		
		var lv_invoiceVat		= 0.00;
		var lv_invoicePrice		= 0.00;
		var lv_invoicediscount	= 0.00;
		var lv_systemVat		= 0.00;
		
		try{
			
			$( 'input[name="invoiceCashMasterBean\\.invoiceType"]:checked' ).each(function( index ) {
				if($(this).val()=="V"){
					lv_systemVat		= gp_parseFloat($("#systemVat").val());
					lv_invoicePrice 	= gp_parseFloat($("#invoicePrice").val());
					lv_invoicediscount 	= gp_parseFloat($("#invoicediscount").val());
					
					lv_invoiceVat		= ((lv_invoicePrice - lv_invoicediscount) * lv_systemVat)/100;
					
				}
			});
			
			$("#invoiceVat").val(lv_invoiceVat);
			gp_initialPage("#frm");
			
			lp_InvoiceTotal();
			
		}catch(e){
			console.error("lp_calInvoiceVat", e.stack);
		}
	}
	
	function lp_InvoiceTotal(){
		
		var lv_invoiceVat		= 0.00;
		var lv_invoicePrice		= 0.00;
		var lv_invoicediscount	= 0.00;
		var lv_invoiceTotal		= 0.00;
		var lv_invoiceDeposit	= 0.00;
		
		try{
			
			lv_invoicePrice 	= gp_parseFloat($("#invoicePrice").val());
			lv_invoicediscount 	= gp_parseFloat($("#invoicediscount").val());
			lv_invoiceVat 		= gp_parseFloat($("#invoiceVat").val());
			lv_invoiceDeposit 	= gp_parseFloat($("#invoiceDeposit").val());
			lv_invoiceTotal		= (lv_invoicePrice - lv_invoicediscount - lv_invoiceDeposit) + lv_invoiceVat;
			
			$("#invoiceTotal").val(lv_invoiceTotal);
			gp_initialPage("#frm");
			
		}catch(e){
			console.error("lp_InvoiceTotal", e.stack);
		}
	}
	
	function lp_getDiscount(av_seq){
		try{
			$("#indexRow").val(av_seq);
			
			$("#command").val("getDiscount");
			gp_postAjaxRequest("#frm", "",function(data){
				jsonObj = JSON.parse(data);
				$("#discount" + av_seq).val(jsonObj.discount);
    			lp_calAmount(av_seq);
			},false);
		}catch(e){
			console.error("lp_getDiscount", e.stack);
		}
	}
	
	function lp_getSaleNameDetail(){
		
		try{
			
			$("#command").val("getSaleNameDetail");
			gp_postAjaxRequest("#frm", "",function(data){
				jsonObj = JSON.parse(data);
				$("#userUniqueId").val(jsonObj.userUniqueId);
				$("#flagSalesman").val(jsonObj.flagSalesman);
				$("#commission").val(jsonObj.commission);
			},false);
			
		}catch(e){
			console.error("lp_getSaleNameDetail", e.stack);
		}
	}
	
	function lp_setStatus(){
		try{
			$("#invoiceCashMasterBean\\.invoiceStatus").val($("#invoiceStatus").val());
		}catch(e){
			console.error("lp_setStatus", e.stack);
		}
	}
	
</script>
<s:form name="frm" id="frm" action="invoiceCashAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden name="pageMode" id="pageMode"/>
	<s:hidden name="indexRow" id="indexRow"/>
	<s:hidden name="hidInvoiceCode" id="hidInvoiceCode"/>
	<s:hidden name="updateCredit" id="updateCredit"/>
	<s:hidden name="showBackFlag" id="showBackFlag" />
	<s:hidden name="systemVat" id="systemVat" />
	<s:hidden name="hidProductCodeSelect" id="hidProductCodeSelect"/>
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>บันทึกการขายต่างๆ</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-info">
			<div class="panel-heading">
		        <h3 class="panel-title"><s:property value="titlePage"/></h3>
			</div>
			<div class="panel-body">
				<div class="panel panel-primary" style="border: none;box-shadow: none;">
				  <div class="panel-body paddingForm">
				  	<%--Begin Row 1 --%>
					<div class="col-sm-12">
						<div class="col-sm-2 text-right">
							<label for="invoiceCode" class="control-label">
								เลขที่ใบเสร็จ  :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:textfield id="invoiceCode" name="invoiceCashMasterBean.invoiceCode" cssClass="form-control input-disabled" readonly="true" placeholder="เลขที่ใบเสร็จ"  />
						</div>
						<div class="col-sm-2 text-right">
							<label for="invoiceDate" class="control-label">
								วันที่ขาย<span style="color: red;"><b>*</b></span>  :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:if test='"NEW".equals(pageMode) || "EDIT_PAGE".equals(pageMode)'>
								<s:textfield id="invoiceDate" name="invoiceCashMasterBean.invoiceDate" onchange="lp_getMultiProductDetailFromList();" cssClass="form-control input-disabled dateFormat" cssStyle="width:85%;display:-webkit-inline-box;" readonly="true" placeholder="วันที่ขาย" />
							</s:if>
							<s:else>
								<s:textfield id="invoiceDate" name="invoiceCashMasterBean.invoiceDate" cssClass="form-control input-disabled" readonly="true" placeholder="วันที่ขาย" />
							</s:else>
						</div>
					</div>
					<%--End Row 1 --%>
					<%--Begin Row 2 --%>
					<s:if test='!"".equals(invoiceCashMasterBean.invoiceCredit)'>
						<div class="col-sm-12">
							<div class="col-sm-2 text-right">
								<label for="invoiceCreditDis" class="control-label">
									เลขที่ใบวางบิล :
								</label>
							</div>
							<div class="col-sm-3 text-left">
								<s:textfield id="invoiceCreditDis" name="invoiceCreditDis" cssClass="form-control input-disabled" readonly="true" value="%{invoiceCashMasterBean.invoiceCredit}" placeholder="เลขที่ใบวางบิล"  />
								<s:hidden id="invoiceCredit" name="invoiceCashMasterBean.invoiceCredit" />
							</div>
						</div>
					</s:if>
					<%--End Row 2 --%>
				  </div>
				</div>
				<div class="panel panel-primary" style="border: none;box-shadow: none;">
				  <div class="panel-heading"><h3 class="panel-title">รายละเอียดใบขาย</h3></div>
				  <div class="panel-body paddingForm">
				  	<%--Begin Row 1 --%>
					<div class="col-sm-12">
						<div class="col-sm-2 text-right">
							<label for="cusCodeDis" class="control-label">
								รหัสลูกค้า  :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:textfield id="cusCodeDis" name="cusCodeDis" cssClass="form-control" placeholder="รหัสลูกค้า" onblur="lp_getCustomerDetail();" cssStyle="width:80%;display:-webkit-inline-box;" />
							<s:hidden id="cusCode" name="customerDetailsBean.cusCode" />
							<s:hidden id="cusGroupCode" name="customerDetailsBean.cusGroupCode" />
							<s:hidden id="groupSalePrice" name="customerDetailsBean.groupSalePrice" />
							<s:hidden id="branchName" name="customerDetailsBean.branchName" />
							<s:if test='"NEW".equals(pageMode)'>
								<img alt="ค้นหาลุกค้า" title="ค้นหาลุกค้า" src="<c:url value='/img/lookup.png'/>" width="24" height="24" border="0" style="cursor: pointer;" onclick="lp_openCustomerLookUp();" />
							</s:if>
						</div>
						<div class="col-sm-3 text-left">
							<span id="spanCusName" class="correct"><s:property value="%{customerDetailsBean.fullName}" /></span>
						</div>
					</div>
					<%--End Row 1 --%>
					<%--Begin Row 2 --%>
					<div class="col-sm-12">
						<div class="col-sm-2 text-right">
							<label for="saleName" class="control-label">
								พนักงานขาย  :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:textfield id="userFullName" name="userDetailsBean.userFullName" onblur="lp_getSaleNameDetail();" cssClass="form-control auto-complete" placeholder="พนักงานขาย" cssStyle="width:80%;display:-webkit-inline-box;" />
					        <s:hidden id="flagSalesman" name="userDetailsBean.flagSalesman" />
					        <s:hidden id="commission" name="userDetailsBean.commission" cssClass="numberOnly" />
					        <s:hidden id="userUniqueId" name="userDetailsBean.userUniqueId" />
					        <s:if test='"NEW".equals(pageMode)'>
					        	<img alt="เลือกพนักงานขาย" title="เลือกพนักงานขาย" src="<c:url value='/img/lookup.png'/>" width="24" height="24" border="0" style="cursor: pointer;" onclick="lp_openUserLoogUp();" />
					        </s:if>
						</div>
					</div>
					<%--End Row 2 --%>
					<%--Begin Row 3 --%>
					<div class="col-sm-12">
						<div class="col-sm-2 text-right">
							<label for="invoiceCashMasterBean.invoiceTypeV" class="control-label">
								ประเภทราคา:
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:radio name="invoiceCashMasterBean.invoiceType" list="priceTypeList"
      							listKey="code" listValue="desc" onclick="lp_calInvoiceVat();" />
						</div>
						<div class="col-sm-3 text-right">
							<label for="reciveDate" class="control-label">
								สถานะใบสั่งซื้อ<span style="color: red;"><b>*</b></span>  :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:select id="invoiceStatus" name="invoiceStatus" cssClass="form-control"
		                      list="statusCombo" listKey="code" listValue="desc" value="%{invoiceCashMasterBean.invoiceStatus}" 
		                      onchange="lp_setStatus();" />
		                    <s:hidden name="invoiceCashMasterBean.invoiceStatus" id="invoiceCashMasterBean.invoiceStatus"/>
						</div>
					</div>
					<%--End Row 3 --%>
				  </div>
				</div>
				<div class="panel panel-primary" style="border: none;box-shadow: none;">
					  <div class="panel-heading"><h3 class="panel-title">รายการสินค้า</h3></div>
					  <s:if test='"NEW".equals(pageMode)'>
						  <div class="panel-body paddingForm">
							<div class="col-sm-12">
								<div class="col-sm-2 text-right">
									<label for="productCodeDis" class="control-label">
										รหัสสินค้า :
									</label>
								</div>
								<div class="col-sm-3 text-left">
									<s:textfield id="productCodeDis" name="productCodeDis" cssClass="form-control" placeholder="รหัสสินค้า" />
								</div>
								<div class="col-sm-4 text-left">
									<button type="button" id="btnSearch" name="btnSearch" class="btn btn-success">
								    	<span class="glyphicon glyphicon-search"></span> ตกลง
								    </button>
								</div>
							</div>
						</div>
					</s:if>
					<div class="panel-body paddingForm" id="invoiceCash-list">
						<%@include file="invoiceCash-list.jsp" %>
					</div>
				</div>
				<div class="panel" style="border: none;box-shadow: none;">
				  <div class="panel-body paddingForm">
				  	<%--Begin Row 1 --%>
					<div class="col-sm-12">
						<div class="col-sm-9 text-right">
							<label for="invoicePrice" class="control-label">
								รวมจำนวนเงิน<span style="color: red;"><b>*</b></span> :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:textfield id="invoicePrice" name="invoiceCashMasterBean.invoicePrice" cssClass="form-control moneyOnly" onblur="lp_calInvoiceVat();" placeholder="รวมจำนวนเงิน" />
						</div>
					</div>
					<%--End Row 1 --%>
					<%--Begin Row 2 --%>
					<div class="col-sm-12">
						<div class="col-sm-9 text-right">
							<label for="invoicediscount" class="control-label">
								หักส่วนลด<span style="color: red;"><b>*</b></span> :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:textfield id="invoicediscount" name="invoiceCashMasterBean.invoicediscount" cssClass="form-control moneyOnly" onblur="lp_calInvoiceVat();" placeholder="หักส่วนลด" />
						</div>
					</div>
					<%--End Row 2 --%>
					<%--Begin Row 3 --%>
					<div class="col-sm-12">
						<div class="col-sm-9 text-right">
							<label for="invoiceVat" class="control-label">
								VAT<span style="color: red;"><b>*</b></span> :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:textfield id="invoiceVat" name="invoiceCashMasterBean.invoiceVat" cssClass="form-control moneyOnly" onblur="lp_InvoiceTotal();" placeholder="VAT" />
						</div>
					</div>
					<%--End Row 3 --%>
					<%--Begin Row 4 --%>
					<div class="col-sm-12">
						<div class="col-sm-9 text-right">
							<label for="invoiceDeposit" class="control-label">
								หักมัดจำ<span style="color: red;"><b>*</b></span> :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:textfield id="invoiceDeposit" name="invoiceCashMasterBean.invoiceDeposit" cssClass="form-control moneyOnly" onblur="lp_InvoiceTotal();" placeholder="หักมัดจำ" />
						</div>
					</div>
					<%--End Row 4 --%>
					<%--Begin Row 5 --%>
					<div class="col-sm-12">
						<div class="col-sm-9 text-right">
							<label for="invoiceTotal" class="control-label">
								รวมทั้งสิ้น<span style="color: red;"><b>*</b></span> :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:textfield id="invoiceTotal" name="invoiceCashMasterBean.invoiceTotal" cssClass="form-control moneyOnly" placeholder="รวมทั้งสิ้น" />
						</div>
					</div>
					<%--End Row 5 --%>
					<%--Begin Row 6 --%>
					<div class="col-sm-12 text-left">
						<label for="remark" class="control-label">
							หมายเหต :
						</label>
					</div>
					<%--End Row 6 --%>
					<%--Begin Row 7 --%>
					<div class="col-sm-12">
						<s:textarea id="remark" name="invoiceCashMasterBean.remark" cols="60" rows="4" cssClass="form-control"></s:textarea>
					</div>
					<%--End Row 7 --%>
					<%--Begin Row 8 --%>
					<div class="col-sm-12">
						<div class="col-sm-12 text-right">
							<s:if test='"Y".equals(showBackFlag) && !"EDIT_PAGE".equals(pageMode)'>
		                		<button type="button" id="btnBack" name="btnBack" class="btn btn-default  btn-md">
							    	<span class="glyphicon glyphicon-hand-left"></span> กลับ
							    </button>
		                	</s:if>
		                	<s:if test='"NEW".equals(pageMode)'>
			                	<button type="button" name="btnSave" id="btnSave" class="btn btn-primary  btn-md">
							    	<span class="glyphicon glyphicon-floppy-save"></span> บันทึก
							    </button>
							    <button type="button" name="btnReset" id="btnReset" class="btn btn-default  btn-md">
							    	<span class="glyphicon glyphicon-off"></span> เริ่มใหม่
							    </button>
						    </s:if>
						    <s:elseif test='"EDIT_PAGE".equals(pageMode)'>
			                	<button type="button" name="btnSave" id="btnSave" class="btn btn-primary  btn-md">
							    	<span class="glyphicon glyphicon-floppy-save"></span> บันทึก
							    </button>
							    <button type="button" name="btnCancelEdit" id="btnCancelEdit" class="btn btn-default  btn-md">
							    	<span class="glyphicon glyphicon-off"></span> ยกเลิกแก้ไข
							    </button>
						    </s:elseif>
						    <s:else>
						    	<s:if test='"Y".equals(updateCredit) && "W".equals(invoiceCashMasterBean.invoiceStatus)'>
						    		<button type="button" name="btnUpdateCredit" id="btnUpdateCredit" class="btn btn-primary  btn-md">
								    	<span class="glyphicon glyphicon-floppy-save"></span> ปรับปรุงสถานะ
								    </button>
						    	</s:if>
						    	<s:if test='!"C".equals(invoiceCashMasterBean.invoiceStatus) && !"W".equals(invoiceCashMasterBean.invoiceStatus)'>
						    		<button type="button" name="btnEdit" id="btnEdit" class="btn btn-primary">
								    	<span class="glyphicon glyphicon-edit"></span> แก้ไข
								    </button>
						    		<button type="button" name="btnPrint" id="btnPrint" class="btn btn-primary">
								    	<span class="glyphicon glyphicon-print"></span> พิมพ์
								    </button>
								    <button type="button" name="btnCancel" id="btnCancel" class="btn btn-danger">
								    	<span class="glyphicon glyphicon-floppy-remove"></span> ยกเลิก
								    </button>
						    	</s:if>
						    </s:else>
						</div>
					</div>
					<%--Begin Row 8 --%>
				  </div>
				</div>
			</div>
		</div>
	</div>
</s:form>