<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#lookUpFrm");
		$('#btnLookUpSearch').click(function(){ 
			try{
				$("#pageIndexLookUp").val("1");
				$("#commandLookUp").val("search");
				gp_progressBarOnPopUp();
				gp_postAjaxRequest("#lookUpFrm", "#productLookUp-list",function(){
					gp_progressBarOffPopUp();
				},false);
			}catch(e){
				console.error("btnLookUpSearch", e.stack);
			}
			
		});
		
		$('#btnLookUpReset').click(function(){ 
			try{
				$("#commandLookUp").val("success");
				gp_progressBarOnPopUp();
				gp_postAjaxRequest("#lookUpFrm", "#dialogLookUp",function(){
					gp_progressBarOffPopUp();
				},false);
			}catch(e){
				console.error("btnLookUpReset", e.stack);
			}
			
		});
		
		$('#btnLookUpSelect').click(function(){ 
			try{
				if(isBlank($("#hidProductCodeSelectLookUp").val())){
					gp_alert("ยังไม่มีสินค้าที่ถูกเลือก");
					return;
				}
				lp_returnProductForLookUp($("#hidProductCodeSelectLookUp").val());
			}catch(e){
				console.error("btnLookUpReset", e.stack);
			}
			
		});
		
		$("#productTypeNameLookUp,#productGroupNameLookUp,#productNameLookUp").keypress(function(e) {
		    if(e.which == 13) {
		    	$('#btnLookUpSearch').click();
		    }
		});
		
		$("#dialogLookUp").dialog({
	        autoOpen: true,
	        bgiframe:true,
	        modal: true,
	        height: 600,
		    width: 1050,
	        draggable: true,
	        show: {
		        effect: "clip",
		        duration: 500
		    },
		    hide: {
		        effect: "clip",
		        duration: 500
		    },
	        close: function(event, ui){
	        	gp_progressBarOff();
	        	gp_initialPage("#frm");
	            $(this).dialog('destroy').remove();
	        },
	        buttons: null,
		    dialogClass: 'zoom'
	    });
		
	});
	
	function changePageLookUp(pageNum) {
		try{
			$("#pageIndexLookUp").val(pageNum);
	        $("#commandLookUp").val("search");
	        gp_postAjaxRequest("#lookUpFrm", "#productLookUp-list","",false);
		}catch(e){
			console.error("changePage", e.stack);
		}
    }
	
	function lp_lookUpChkAll(){
		
		var lv_chk = false;
		
		try{
			if($("#lookUpChkAll").is(':checked')){
				lv_chk = true;
			}
			
			$("input[name=lookUpChkSelect]").each(function() {
				$(this).prop('checked', lv_chk);
	        });
			
			lp_lookUpChkSelect();
		}catch(e){
			console.error("lp_lookUpChkAll", e.stack);
		}
	}
	
	function lp_lookUpChkSelect(){
		
		var lv_val 		= "";
		var la_array 	= [];
		var removeItem 	= 0;
		
		try{
			if($("input[name=lookUpChkSelect]:checked").length != $("input[name=lookUpChkSelect]").length){
				$("#lookUpChkAll").prop('checked', false);
			}
			
			if(!isBlank($("#hidProductCodeSelectLookUp").val())){
				la_array 	= $("#hidProductCodeSelectLookUp").val().split("|");
			}
			
			$("input[name=lookUpChkSelect]").each(function() {
				if ( $( this ).is( ":checked" ) ){
					if($.inArray($(this).val(), la_array) == -1){
						la_array.push($(this).val());
					}
					
				}else{
					removeItem 	= $(this).val();
					la_array 	= $.grep(la_array, function(value) {
									  return value != removeItem;
									});
					
				}
	        });
			
			$.each(la_array, function(index, item) {
				if(isBlank(lv_val)){
					lv_val += item;
				}else{
					lv_val += "|" + item;
				}
			});
			
			$("#hidProductCodeSelectLookUp").val(lv_val);
			
			if(isBlank(lv_val)){
				$("#sp_chooseButton").html("");
			}else{
				$("#sp_chooseButton").html("(" + la_array.length + ")");
			}
			
		}catch(e){
			console.error("lp_lookUpChkSelect", e.stack);
		}
	}
	
	function lp_displayChkSelect(){
		
		var lv_val = "";
		var la_array;
		
		try{
			lv_val = $("#hidProductCodeSelectLookUp").val();
			if(!isBlank(lv_val)){
				la_array = lv_val.split("|");
				$("input[name=lookUpChkSelect]").each(function() {
					if($.inArray($(this).val(), la_array) > -1){
						$(this).prop('checked', true);
					}
		        });
			}
			
		}catch(e){
			console.error("lp_lookUpChkSelect", e.stack);
		}
	}
	
</script>
<div id="dialogLookUp" title="เลือกสินค้า">
<s:form name="lookUpFrm" id="lookUpFrm" action="productLookUpAction" onsubmit="return false;">
	<s:hidden name="command" id="commandLookUp"/>
	<s:hidden name="pageIndex" id="pageIndexLookUp"  />
	<s:hidden name="hidProductCodeSelectLookUp" id="hidProductCodeSelectLookUp"  />
	<div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<div class="col-sm-3 text-right">
					<label for="productTypeNameLookUp" class="control-label">
						หมวดสินค้า :
					</label>
				</div>
				<div class="col-sm-3 text-left">
					<s:textfield id="productTypeNameLookUp" name="beanForLookUp.productTypeName" cssClass="form-control auto-complete" maxlength="200" placeholder="หมวดสินค้า" />
					<script>$("#productTypeNameLookUp").focus();</script>
				</div>
				<div class="col-sm-2 text-right">
					<label for="productGroupNameLookUp" class="control-label">
						หมู่สินค้า :
					</label>
				</div>
				<div class="col-sm-3 text-left">
					<s:textfield id="productGroupNameLookUp" name="beanForLookUp.productGroupName" cssClass="form-control auto-complete" maxlength="200" placeholder="หมู่สินค้า" />
				</div>
				<div class="col-sm-1 text-left"></div>
				<div class="col-sm-3 text-right">
					<label for="productNameLookUp" class="control-label">
						ชื่อสินค้า  :
					</label>
				</div>
				<div class="col-sm-3 text-left">
					<s:textfield id="productNameLookUp" name="beanForLookUp.productName" cssClass="form-control auto-complete" maxlength="255" placeholder="ชื่อสินค้า" />
				</div>
				<div class="col-sm-6 text-left"></div>
				<div class="col-sm-11 text-right">
					<button type="button" id="btnLookUpSearch" name="btnLookUpSearch" class="btn btn-success">
				    	<span class="glyphicon glyphicon-search"></span> ค้นหา
				    </button>
					<button type="button" name="btnLookUpReset" id="btnLookUpReset" class="btn btn-default">
				    	<span class="glyphicon glyphicon-off"></span> เริ่มใหม่
				    </button>
				    <button type="button" name="btnLookUpSelect" id="btnLookUpSelect" class="btn btn-info">
				    	<span class="glyphicon glyphicon-ok"></span> เลือก<span id="sp_chooseButton"></span>
				    </button>
				</div>
				<div class="col-sm-1 text-right"></div>
			</div>
			<div class="row">
				<div id="productLookUp-list" class="col-sm-12">
			    	<%@include file="productLookUp-list.jsp" %>
				</div>
			</div>
		</div>
	</div>
</s:form>
</div>