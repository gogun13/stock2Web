<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#lookUpFrm");
		
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
		
		$('#btnPrint').click(function(){ 
			
			var h				= "";
			
			try{
				
				if($('input[name="radPrint"]:checked').length<=0){
					gp_alert("กรุณาระบุประเภทในการพิมพ์");
					return;
                }
				
				console.log("radPrint :: " + $('input[name="radPrint"]:checked').val());
				
				if(isBlank($("#hidProductCodeForBarCode").val())){
					gp_alert("ไม่พบรหัสสินค้าที่จะพิมพ์");
				}else{
					var lv_action = '<s:url action="productSearchAction.action" />';
					
					h = '<iframe name="printSection" '
						  + '		 id="printSection"'
						  + '		 src="'+lv_action+'?command=genPdf&hidProductCodeForBarCode='+$("#hidProductCodeForBarCode").val()+'&radPrint='+$('input[name="radPrint"]:checked').val()+'"'
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
				}
			}catch(e){
				console.error("btnPrint", e.stack);
			}
			
		});
		
	});
	
	function lp_deleteRecord(av_seq){
		
		try{
			$("#indexRow").val(av_seq);
			$("#commandLookUp").val("deleteRecord");
			
			gp_postAjaxRequest("#lookUpFrm", "#productBarCode-list",function(){
				var lv_val = "";
				var la_array;
				
				$("input[name=rowIndex]").each(function() {
					var rowIndex = $(this).val();
					
					if(isBlank(lv_val)){
						lv_val = $("#productCode" + rowIndex).val();
					}else{
						lv_val += "," + $("#productCode" + rowIndex).val();
					}
		        });
				
				console.log("lv_val :: " + lv_val);
				
				if(isBlank(lv_val)){
					$("#hidProductCodeForBarCode").val(lv_val);
					$("#sp_barcodeButton").html("");
				}else{
					la_array = lv_val.split(",");
					
					$("#sp_barcodeButton").html("(" + la_array.length + ")");
					$("#hidProductCodeForBarCode").val(lv_val);
				}
				
			},false);
		}catch(e){
			console.error("lp_deleteRecord", e.stack);
		}
	}
	
</script>
<div id="dialogLookUp" title="รายการสินค้าที่จะพิมพ์">
<s:form name="lookUpFrm" id="lookUpFrm" action="productSearchAction" onsubmit="return false;">
	<s:hidden name="command" id="commandLookUp"/>
	<s:hidden name="indexRow" id="indexRow"/>
	<div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="row">
				<div id="productBarCode-list" class="col-sm-12">
			    	<%@include file="productBarCode-list.jsp" %>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12 text-right">
				<input type="radio" name="radPrint" id="radPrint1" value="A" />พิมพ์ซ้ำรายการเดิม
				&nbsp;&nbsp;
				<input type="radio" name="radPrint" id="radPrint2" value="B" />พิมพ์รายการต่อเนื่อง
				<button type="button" name="btnPrint" id="btnPrint" class="btn btn-primary">
			    	<span class="glyphicon glyphicon-print"></span> พิมพ์
			    </button>
			</div>
		</div>
	</div>
</s:form>
</div>