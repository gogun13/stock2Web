<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#frm");
		
		if($("#pageMode").val()=="UPDATE"){
			lp_setModeEdit();
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
			}catch(err){
				console.error("btnReset", e.stack);
			}
		});
		
		$('#btnSave').click(function(){ 
			
			try{
				if(!lp_validate()){
					return;
				}
				
				$("#command").val("onSave");
				gp_progressBarOnPopUp();
				gp_postAjaxRequest("#frm", "",function(data){
					var jsonObj = JSON.parse(data);
					
					gp_dialogSuccess("บันทึกเรียบร้อย",function(){
						$("#command").val("getDetail");
						$("#hidReciveNo").val(jsonObj.reciveNo);
						gp_postAjaxRequest("#frm","#page_main",function(){
							gp_progressBarOffPopUp();
						},false);
					},false);
				},false);
			}catch(e){
				console.error("btnSave", e.stack);
			}
		});
		
		$('#btnBack').click(function(){
			try{
				$("#command").val("onBack");
				gp_postAjaxRequest("#frm", "#page_main");
				
			}catch(e){
				console.error("btnBack", e.stack);
			}
		});
		
		$('body').on('blur', '#creditDay',function(event){
			try{
				//console.log("event.result :: " + event.result);
				if(!event.result)return;
				
				if(isBlank($("#creditDay").val()) || $('#creditDay').is('[readonly]')){
					return;
				}
				
				$("#command").val("ctrlCreditDay");
				gp_postAjaxRequest("#frm", "",function(data){
					try{
						var jsonObj = JSON.parse(data);
						$("#creditExpire").val(jsonObj.creditExpire);
					}catch(e){
						console.error("After call ctrlCreditDay", e.stack);
					}
				},false);
				
			}catch(e){
				console.error("creditDay", e.stack);
			}
		});
		
		$('body').on('blur', '#vendorName,#branchName',function(event){
			try{
				
				if(isBlank($(this).val())){
					return;
				}
				
				if($(this).attr("id")=="vendorName"){
					$("#branchName").val("");
				}
				
				$("#command").val("getCompanyVendorDetail");
				gp_postAjaxRequest("#frm", "",function(data){
					var jsonObj = null;
	            	
	            	try{
	            		jsonObj = JSON.parse(data);
	            		
	            		$("#vendorCode").val(jsonObj.vendorCode);
	        			$("#vendorName").val(jsonObj.vendorName);
	        			$("#branchName").val(jsonObj.branchName);
	        			
	        			$("input[name=rowIndex]").each(function() {
	        				lp_getPrice($( this ).val());
	        	        });
	        			
	            	}catch(e){
	            		console.error("After call getCompanyVendorDetail", e.stack);
	            	}
				},false);
				
			}catch(e){
				console.error("#vendorName,#branchName", e.stack);
			}
		});
		
	});
	
	function lp_deleteRecord(av_seq){
		try{
			$("#indexRow").val(av_seq);
			
			$("#command").val("deleteRecord");
			gp_postAjaxRequest("#frm", "#reciveStock-list",function(){
				lp_calReciveAmount();
			});
		}catch(e){
			console.error("lp_deleteRecord", e.stack);
		}
	}
	
	function lp_addRowtable(){
		try{
			$("#command").val("newRecord");
			gp_postAjaxRequest("#frm", "#reciveStock-list");
		}catch(e){
			console.error("lp_addRowtable", e.stack);
		}
	}
	
	function lp_validate(){
		var la_validate = new Array( "vendorName:บริษัท"	
									, "branchName:สาขา"
									, "reciveDate:วันที่สั่งซื้อ");
		var lv_ret = true;
		var lv_currReciveStatus 	= "";
	    
		try{
			if(!gp_validateEmptyObj(la_validate)){
				return false;
			}
			
			lv_currReciveStatus = $("#currReciveStatus").val().trim();
			if(lv_currReciveStatus=="3" || lv_currReciveStatus=="4"){
				return true;
			}
			
			$( 'input[name="reciveOrderMasterBean\\.reciveType"]:checked' ).each(function( index ) {
				if($(this).val()=="C" && (isBlank($("#creditDay").val()) || isBlank($("#creditExpire").val()))){
					gp_alert("กรุณาระบุจำนวนวันและวันครบกำหนด");
					return false;
				}
			});
			
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
	            }
				
	            lv_ret = true;
	        });
			
			return lv_ret;
			
		}catch(e){
			console.error("lp_validate", e.stack);
			return false;
		}
	}
	
	function lp_setModeEdit(){
		var lv_currReciveStatus = "";
		
		try{
			
			lv_currReciveStatus = $("#currReciveStatus").val();
			
			if(lv_currReciveStatus!="1" && lv_currReciveStatus!="2"){
				$("form").each(function(){
					//$(this).find('input:visible').prop("disabled", true);
					$(this).find('input:visible').prop("readonly", true).addClass("input-disabled");
					$(this).find('input[type=\'radio\']:visible').prop("disabled", true);
				    
				});
				
				$("#remark").prop("disabled", true);
				
				if(lv_currReciveStatus!="4"){
					$("#reciveStatus").prop("disabled", false);
					$("#btnSave").prop("disabled", false);
				}
				 
				$("#btnBack").prop("disabled", false);
				 
			}
			
			if(lv_currReciveStatus=="2"){
				$("#reciveStatus option[value='1']").remove();
			}else if(lv_currReciveStatus=="3"){
				$("#reciveStatus option[value='1']").remove();
				$("#reciveStatus option[value='2']").remove();
			}else if(lv_currReciveStatus=="4"){
				$("#reciveStatus option[value='1']").remove();
				$("#reciveStatus option[value='2']").remove();
				$("#reciveStatus option[value='3']").remove();
			}
			
			lp_ctrllReciveType();
			
		}catch(e){
			console.error("lp_setModeEdit", e.stack);
		}
	}
	
	function lp_setModeNew(){
		try{
			$("#reciveStatus").prop("disabled", true);
			lp_ctrllReciveType();
			lp_calReciveVat();
		}catch(e){
			console.error("lp_setModeNew", e.stack);
		}
	}
	
	function lp_ctrllReciveType(){
		try{
			
			if($("#currReciveStatus").val()=="" || $("#currReciveStatus").val()=="1" || $("#currReciveStatus").val()=="2"){
				$( 'input[name="reciveOrderMasterBean\\.reciveType"]:checked' ).each(function( index ) {
					$('#creditExpire').next("img").remove();
					
					if($(this).val()=="M"){
						$('#creditDay').val('');
						$('#creditExpire').val('');
						
						$('#creditDay').attr('readonly', true);
						$('#creditExpire').attr('readonly', true);
						
						$("#creditDay").attr('class', 'form-control col-sm-1 numberOnly input-disabled');
						$("#creditExpire").attr('class', 'form-control input-disabled');
					}else{
						$('#creditDay').attr('readonly', false);
						$('#creditExpire').attr('readonly', false);
						
						$("#creditDay").attr('class', 'form-control col-sm-1 numberOnly');
						$("#creditExpire").attr('class', 'form-control dateFormat');
						
						//$('#creditExpire').next("img").show();
					}
				});
				
				gp_initialPage("#frm");
			}
		}catch(e){
			console.error("lp_ctrllReciveType", e.stack);
		}
	}
	
	function lp_comparePrice(av_seq){
		
		try{
			
			if(isBlank($("#productName" + av_seq).val())){
				gp_alert("กรุณาระบุสินค้า",function(){
					$("#productName" + av_seq).focus();
				});
				return;
			}
			
			if(isBlank($("#productCode" + av_seq).val())){
				gp_alert($("#productName" + av_seq).val() + "ไม่มีอยู่จริงในระบบ",function(){
					$("#productName" + av_seq).focus();
				});
				return;
			}
			
			$("#command").val("comparePricePopUp");
			$("#indexRow").val(av_seq);
			gp_postAjaxRequest("#frm", "#dialog");
			
		}catch(e){
			console.error("lp_comparePrice", e.stack);
		}
	}
	
	function lp_showCompVenDetail(){
		try{
			$("#command").val("vendorDetailPopUp");
			gp_postAjaxRequest("#frm", "#dialog");
		}catch(e){
			console.error("lp_showCompVenDetail", e.stack);
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
			gp_postAjaxRequest("#frm", "#reciveStock-list",function(){
				$( "#dialogLookUp" ).dialog( "close" );
			},false);
			
		}catch(e){
			console.error("lp_returnProductForLookUp", e.stack);
		}
	}
	
	function lp_calReciveAmount(){
		
		var lv_reciveAmount		= 0.00;
		
		try{
			$("input[name=rowIndex]").each(function() {
				var rowIndex = $(this).val();
				lv_reciveAmount += gp_parseFloat($("#costPrice" + rowIndex).val());
	        });
			
			$("#reciveAmount").val(lv_reciveAmount);
			gp_format($("#reciveAmount"), 2);
			
			lp_calReciveVat();
			
		}catch(e){
			console.error("lp_calReciveAmount", e.stack);
		}
	}
	
	function lp_calReciveVat(){
		
		var lv_reciveVat		= 0.00;
		var lv_reciveAmount		= 0.00;
		var lv_reciveDiscount	= 0.00;
		var lv_systemVat		= 0.00;
		
		try{
			$( 'input[name="reciveOrderMasterBean\\.priceType"]:checked' ).each(function( index ) {
				if($(this).val()=="V"){
					lv_systemVat		= gp_parseFloat($("#systemVat").val());
					lv_reciveAmount 	= gp_parseFloat($("#reciveAmount").val());
					lv_reciveDiscount 	= gp_parseFloat($("#reciveDiscount").val());
					
					lv_reciveVat		= ((lv_reciveAmount - lv_reciveDiscount) * lv_systemVat)/100;
				}
			});
			
			$("#reciveVat").val(lv_reciveVat);
			gp_format($("#reciveVat"),2);
			
			lp_calReciveTotal();
		}catch(e){
			console.error("lp_calReciveVat", e.stack);
		}
	}
	
	function lp_calReciveTotal(){
		
		var lv_reciveVat		= 0.00;
		var lv_reciveAmount		= 0.00;
		var lv_reciveDiscount	= 0.00;
		var lv_reciveTotal		= 0.00;
		
		try{
			
			lv_reciveAmount 	= gp_parseFloat($("#reciveAmount").val());
			lv_reciveDiscount 	= gp_parseFloat($("#reciveDiscount").val());
			lv_reciveVat 		= gp_parseFloat($("#reciveVat").val());
			lv_reciveTotal		= (lv_reciveAmount - lv_reciveDiscount) + lv_reciveVat;
			
			$("#reciveTotal").val(lv_reciveTotal);
			gp_format($("#reciveTotal"), 2);
			
		}catch(e){
			console.error("lp_calReciveTotal", e.stack);
		}
	}
	
	function lp_setStatus(){
		try{
			$("#reciveOrderMasterBean\\.reciveStatus").val($("#reciveStatus").val());
		}catch(e){
			console.error("lp_setStatus", e.stack);
		}
	}
	
	function lp_getPrice(av_seq){
		
		try{
			
			if(isBlank($("#productCode" + av_seq).val())){
				return;
			}
			
			$("#indexRow").val(av_seq);
			$("#command").val("getPrice");
			
			gp_postAjaxRequest("#frm", "",function(data){
				try{
					var jsonObj = JSON.parse(data);
					
					$("#price" + av_seq).val(jsonObj.price);
        			$("#discountRate" + av_seq).val(jsonObj.discountRate);
        			lp_calAmount(av_seq);
				}catch(e){
					console.error("After call getPrice", e.stack);
				}
			},false);
			
		}catch(e){
			console.error("lp_getPrice", e.stack);
		}
	}
	
	function lp_calAmount(av_seq){
		
		var lv_quantity 		= 0.00;
		var lv_price 			= 0.00;
		var lv_costPrice 		= 0.00;
		var lv_discountRate 	= "";
		var la_discountRate 	= null;
		var lv_discount 		= 0.00;
		
		try{
			lv_quantity 	= gp_parseFloat($("#quantity" + av_seq).val());
			lv_price		= gp_parseFloat($("#price" + av_seq).val());
			lv_discountRate = $("#discountRate" + av_seq).val();
			
			if(lv_discountRate!="" && lv_quantity > 0){
				lv_costPrice 	= lv_quantity * lv_price;
				lv_discountRate	= lv_discountRate==""?"0":lv_discountRate;
				
				if(lv_discountRate.indexOf("%") > -1){
					la_discountRate = lv_discountRate.split("%");
					for(var i=0;i<la_discountRate.length;i++){
						if($.isNumeric(la_discountRate[i])){
							lv_discount 	= gp_parseFloat(la_discountRate[i]);
							lv_costPrice 	= (lv_costPrice * (100-lv_discount))/100;
						}
					}
				}else{
					if($.isNumeric(lv_discountRate)){
						lv_discount 	= gp_parseFloat(lv_discountRate);
						lv_costPrice 	= (lv_costPrice * (100-lv_discount))/100;
					}
				}
			}
			
			
			$("#discountRate" + av_seq).val(lv_discountRate);
			$("#costPrice" + av_seq).val(lv_costPrice);
			gp_format($("#costPrice" + av_seq), 2);
			
			lp_calReciveAmount();
			
		}catch(e){
			console.error("lp_calAmount", e.stack);
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
            			$("#price" + av_seq).val('0.00');
            			$("#discountRate" + av_seq).val('0');
            			$("#inventory" + av_seq).val('');
            			$("#unitCode" + av_seq).val('');
            			$("#unitName" + av_seq).val('');
        			}else{
        				$("#productCode" + av_seq).val(jsonObj.productCode);
            			$("#inventory" + av_seq).val(jsonObj.inventory);
            			$("#unitCode" + av_seq).val(jsonObj.unitCode);
            			$("#unitName" + av_seq).val(jsonObj.unitName);
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
	
</script>
<s:form name="frm" id="frm" action="reciveStockAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden name="pageMode" id="pageMode"/>
	<s:hidden name="indexRow" id="indexRow"/>
	<s:hidden name="hidReciveNo" id="hidReciveNo"/>
	<s:hidden name="currReciveStatus" id="currReciveStatus"/>
	<s:hidden name="showBackFlag" id="showBackFlag" />
	<s:hidden name="systemVat" id="systemVat" />
	<s:hidden name="companyVendorBean.vendorCode" id="vendorCode"/>
	<s:hidden name="hidProductCodeSelect" id="hidProductCodeSelect"/>
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>ดูแลจัดการ Stock สินค้า</h2>
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
				  <div class="panel-heading"><h3 class="panel-title">ผู้จำหน่าย</h3></div>
				  <div class="panel-body paddingForm">
				  	<%--Begin Row 1 --%>
					<div class="col-sm-12">
						<div class="col-sm-2 text-right">
							<label for="vendorName" class="control-label">
								บริษัท<span style="color: red;"><b>*</b></span>  :
							</label>
						</div>
						<div class="col-sm-4 text-left">
							<s:textfield id="vendorName" name="companyVendorBean.vendorName" cssClass="form-control auto-complete" maxlength="100" placeholder="บริษัท" cssStyle="width:80%;display:-webkit-inline-box;" />
							<a href="javascript:void(0);" title="รายละเอียดผู้จำหน่าย" style="color: #504f4f;" onclick="lp_showCompVenDetail();">
					          <span class="glyphicon glyphicon-info-sign btn-lg" style="padding: 0px 0px"></span>
					        </a>
						</div>
						<div class="col-sm-2 text-right">
							<label for="branchName" class="control-label">
								สาขา<span style="color: red;"><b>*</b></span>  :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:textfield id="branchName" name="companyVendorBean.branchName" cssClass="form-control auto-complete" maxlength="30" placeholder="สาขา" />
						</div>
					</div>
					<%--End Row 1 --%>
					<%--Begin Row 2 --%>
					<div class="col-sm-12">
						<div class="col-sm-2 text-right">
							<label for="billNo" class="control-label">
								เลขที่บิล :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:textfield id="billNo" name="reciveOrderMasterBean.billNo" cssClass="form-control" maxlength="50" placeholder="เลขที่บิล" />
						</div>
					</div>
					<%--End Row 2 --%>
				  </div>
				</div>
				<div class="panel panel-primary" style="border: none;box-shadow: none;">
				  <div class="panel-heading"><h3 class="panel-title">รายละเอียดใบสั่งซื้อ</h3></div>
				  <div class="panel-body paddingForm">
				  	<%--Begin Row 1 --%>
					<div class="col-sm-12">
						<div class="col-sm-2 text-right">
							<label for="reciveNo" class="control-label">
								เลขที่ใบสั่งซื้อ <span style="color: red;"><b>*</b></span>  :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:textfield id="reciveNo" name="reciveOrderMasterBean.reciveNo" cssClass="form-control input-disabled" maxlength="50" readonly="true" placeholder="เลขที่ใบสั่งซื้อ" />
						</div>
						<div class="col-sm-3 text-right">
							<label for="reciveDate" class="control-label">
								วันที่สั่งซื้อ<span style="color: red;"><b>*</b></span>  :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:if test='!"1".equals(currReciveStatus) && !"2".equals(currReciveStatus)'>
								<s:property value="reciveOrderMasterBean.reciveDate" />
								<s:hidden name="reciveOrderMasterBean.reciveDate" id="reciveDate"/>
							</s:if>
							<s:else>
								<s:textfield id="reciveDate" name="reciveOrderMasterBean.reciveDate" cssClass="form-control dateFormat" cssStyle="width:85%;display:-webkit-inline-box;" placeholder="วันที่สั่งซื้อ" />
							</s:else>
						</div>
					</div>
					<%--End Row 1 --%>
					<%--Begin Row 2 --%>
					<div class="col-sm-12">
						<div class="col-sm-2 text-right">
							<s:radio name="reciveOrderMasterBean.reciveType" list="reciveTypeList"
      							listKey="code" listValue="desc" onclick="lp_ctrllReciveType();" />
						</div>
						<div class="col-sm-1 text-right">
							<label for="creditDay" class="control-label">
								วัน  :
							</label>
						</div>
						<div class="col-sm-2 text-left">
							<s:textfield id="creditDay" name="reciveOrderMasterBean.creditDay" cssClass="form-control col-sm-1 numberOnly" placeholder="วัน" cssStyle="width: 80px;" />
						</div>
						<div class="col-sm-3 text-right">
							<label for="creditExpire" class="control-label">
								วันครบกำหนด  :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:if test='!"1".equals(currReciveStatus) && !"2".equals(currReciveStatus)'>
								<s:property value="reciveOrderMasterBean.creditExpire" />
								<s:hidden name="reciveOrderMasterBean.creditExpire" id="creditExpire"/>
							</s:if>
							<s:else>
								<s:textfield id="creditExpire" name="reciveOrderMasterBean.creditExpire" cssClass="form-control dateFormat" cssStyle="width:85%;display:-webkit-inline-box;" placeholder="วันครบกำหนด" />
							</s:else>
						</div>
					</div>
					<%--End Row 2 --%>
					<%--Begin Row 3 --%>
					<div class="col-sm-12">
						<div class="col-sm-2 text-right">
							<label for="reciveOrderMasterBean.priceTypeV" class="control-label">
								ประเภทราคา:
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:radio name="reciveOrderMasterBean.priceType" list="priceTypeList"
      							listKey="code" listValue="desc" onclick="lp_calReciveVat();" />
						</div>
						<div class="col-sm-3 text-right">
							<label for="reciveDate" class="control-label">
								สถานะใบสั่งซื้อ<span style="color: red;"><b>*</b></span>  :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:select id="reciveStatus" name="reciveStatus" cssClass="form-control"
		                      list="statusCombo" listKey="code" listValue="desc" value="%{reciveOrderMasterBean.reciveStatus}" 
		                      onchange="lp_setStatus();" />
		                    <s:hidden name="reciveOrderMasterBean.reciveStatus" id="reciveOrderMasterBean.reciveStatus"/>
						</div>
					</div>
					<%--End Row 3 --%>
				  </div>
				</div>
				<div class="panel panel-primary" style="border: none;box-shadow: none;">
				  <div class="panel-heading"><h3 class="panel-title">รายการสินค้า</h3></div>
				  <div class="panel-body paddingForm" id="reciveStock-list">
				  	<%@include file="reciveStock-list.jsp" %>
				  </div>
				</div>
				<div class="panel" style="border: none;box-shadow: none;">
				  <div class="panel-body paddingForm">
				  	<%--Begin Row 1 --%>
					<div class="col-sm-12">
						<div class="col-sm-9 text-right">
							<label for="reciveAmount" class="control-label">
								รวมจำนวนเงิน<span style="color: red;"><b>*</b></span> :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:textfield id="reciveAmount" name="reciveOrderMasterBean.reciveAmount" cssClass="form-control moneyOnly" onblur="lp_calReciveVat();" placeholder="รวมจำนวนเงิน" />
						</div>
					</div>
					<%--End Row 1 --%>
					<%--Begin Row 2 --%>
					<div class="col-sm-12">
						<div class="col-sm-9 text-right">
							<label for="reciveDiscount" class="control-label">
								หักเงินมัดจำ<span style="color: red;"><b>*</b></span> :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:textfield id="reciveDiscount" name="reciveOrderMasterBean.reciveDiscount" cssClass="form-control moneyOnly" onblur="lp_calReciveVat();" placeholder="หักเงินมัดจำ" />
						</div>
					</div>
					<%--End Row 2 --%>
					<%--Begin Row 3 --%>
					<div class="col-sm-12">
						<div class="col-sm-9 text-right">
							<label for="reciveVat" class="control-label">
								VAT<span style="color: red;"><b>*</b></span> :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:textfield id="reciveVat" name="reciveOrderMasterBean.reciveVat" cssClass="form-control moneyOnly" onblur="lp_calReciveTotal();" placeholder="VAT" />
						</div>
					</div>
					<%--End Row 3 --%>
					<%--Begin Row 4 --%>
					<div class="col-sm-12">
						<div class="col-sm-9 text-right">
							<label for="reciveTotal" class="control-label">
								รวมทั้งสิ้น<span style="color: red;"><b>*</b></span> :
							</label>
						</div>
						<div class="col-sm-3 text-left">
							<s:textfield id="reciveTotal" name="reciveOrderMasterBean.reciveTotal" cssClass="form-control moneyOnly" placeholder="รวมทั้งสิ้น" />
						</div>
					</div>
					<%--End Row 4 --%>
					<%--Begin Row 5 --%>
					<div class="col-sm-12 text-left">
						<label for="remark" class="control-label">
							หมายเหต :
						</label>
					</div>
					<%--End Row 5 --%>
					<%--Begin Row 6 --%>
					<div class="col-sm-12">
						<s:textarea id="remark" name="reciveOrderMasterBean.remark" cols="60" rows="4" cssClass="form-control"></s:textarea>
					</div>
					<%--End Row 6 --%>
					<%--Begin Row 7 --%>
					<div class="col-sm-12">
						<div class="col-sm-12 text-right">
							<s:if test='"Y".equals(showBackFlag)'>
		                		<button type="button" id="btnBack" name="btnBack" class="btn btn-default  btn-md">
							    	<span class="glyphicon glyphicon-hand-left"></span> กลับ
							    </button>
		                	</s:if>
		                	<button type="button" name="btnSave" id="btnSave" class="btn btn-primary  btn-md">
						    	<span class="glyphicon glyphicon-floppy-save"></span> บันทึก
						    </button>
						    <button type="button" name="btnReset" id="btnReset" class="btn btn-default  btn-md">
						    	<span class="glyphicon glyphicon-off"></span> เริ่มใหม่
						    </button>
						</div>
					</div>
					<%--Begin Row 7 --%>
				  </div>
				</div>
			</div>
		</div>
	</div>
</s:form>