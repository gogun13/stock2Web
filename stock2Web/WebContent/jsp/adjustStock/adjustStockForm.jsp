<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#frm");
		
		$('#btnReset').click(function(){ 
			try{
				$("#command").val("success");
				gp_postAjaxRequest("#frm", "#page_main");
			}catch(e){
				console.error("btnReset", e.stack);
			}
		});
		
		$('#btnSave').click(function(){ 
			var lv_ret 		= true;
			
			try{
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
		            
					if(gp_parseFloat($("#quanNew" + rowIndex).val()) == 0){
						gp_alert($("#productName" + rowIndex).val() + "ไม่มีการเพิ่ม/ลด สินค้า", function() { 
							$("#quanNew" + rowIndex).focus();
		    		    });
						lv_ret = false;
		            	return false;
					}
		            
		            lv_ret = true;
		        });
		    	
		    	if(!lv_ret)return;
				
				$("#command").val("save");
				gp_progressBarOn();
				gp_postAjaxRequest("#frm", "",function(){
					gp_dialogSuccess("บันทึกเรียบร้อย",function(){
						$("#command").val("success");
						gp_postAjaxRequest("#frm", "#page_main",function(){
							gp_progressBarOff();
						},false);
					},false);
				},false);
			}catch(e){
				console.error("btnSave", e.stack);
			}
		});
		
	});
	
	function lp_deleteRecord(av_seq){
		try{
			$("#indexRow").val(av_seq);
			
			$("#command").val("deleteRecord");
			gp_postAjaxRequest("#frm", "#adjustStock-list");
		}catch(e){
			console.error("lp_deleteRecord", e.stack);
		}
	}
	
	function lp_addRowtable(){
		try{
			
			$("#command").val("newRecord");
			gp_postAjaxRequest("#frm", "#adjustStock-list");
		}catch(e){
			console.error("lp_addRowtable", e.stack);
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
            			$("#quanOld" + av_seq).val('0');
            			$("#quantity" + av_seq).val('0');
            			$("#unitName" + av_seq).val('');
        			}else{
        				$("#productCode" + av_seq).val(jsonObj.productCode);
            			$("#quanOld" + av_seq).val(jsonObj.quanOld);
            			$("#quantity" + av_seq).val(jsonObj.quantity);
            			$("#unitName" + av_seq).val(jsonObj.unitName);
        			}
					
					lp_calQuantity(av_seq);
        			
				}catch(e){
					console.error("After call getProductDetailByName", e.stack);
				}
			},false);
			
		}catch(e){
			console.error("lp_getProductDetailByName", e.stack);
		}
	}
	
	function lp_calQuantity(av_seq){
		
		var lv_quanOld 		= 0.00;
		var lv_quanNew 		= 0.00;
		var lv_quantity 	= 0.00;
		
		try{
			lv_quanOld 		= gp_parseFloat($("#quanOld" + av_seq).val());
			lv_quanNew 		= gp_parseFloat($("#quanNew" + av_seq).val());
			lv_quantity 	= lv_quanOld + lv_quanNew;
			
			$("#quantity" + av_seq).val(lv_quantity);
			
			gp_initialPage("#frm");
			
		}catch(e){
			console.error("lp_calQuantity", e.stack);
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
			gp_postAjaxRequest("#frm", "#adjustStock-list");
			
			$( "#dialogLookUp" ).dialog( "close" );
			
		}catch(e){
			console.error("lp_returnProductForLookUp", e.stack);
		}
	}
	
</script>
<s:form name="frm" id="frm" action="adjustStockAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden id="indexRow" name="indexRow" />
	<s:hidden id="hidProductCodeSelect" name="hidProductCodeSelect" />
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>ปรับยอดสต๊อกสินค้า (Adjust Stock)</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="row">
				<div id="adjustStock-list" class="col-sm-12">
			    	<%@include file="adjustStock-list.jsp" %>
				</div>
			</div>
		</div>
		<div class="row" >
			 <div class="col-sm-12 text-right">
	         	<button type="button" name="btnSave" id="btnSave" class="btn btn-primary  btn-md">
			    	<span class="glyphicon glyphicon-floppy-save"></span> บันทึก
			    </button>
			    <button type="button" name="btnReset" id="btnReset" class="btn btn-default  btn-md">
			    	<span class="glyphicon glyphicon-off"></span> เริ่มใหม่
			    </button>
			    <span style="width:20px;">&nbsp;</span>
			</div>
			<div class="col-sm-12 text-right" style="height:20px;"></div>
		</div>
	</div>
</s:form>