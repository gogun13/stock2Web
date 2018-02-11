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
				console.error("btnAdd", e.stack);
			}
		});
		
		$('#btnSave').click(function(){ 
			var lv_ret = true;
			
			try{
				$("input[name=rowIndex]").each(function() {
					var rowIndex = $(this).val();
		            if(isBlank($("#unitName" + rowIndex).val())){
		            	gp_alert("ชื่อหน่วยสินค้าห้ามเป็นค่าว่าง", function(){
		            		$("#unitName" + rowIndex).focus();
		            	});
		            	lv_ret = false;
		            	return false;
		            }
		            
		            lv_ret = true;
		        });
		    	
		    	if(!lv_ret)return;
				
				$("#command").val("save");
				gp_progressBarOn();
				gp_postAjaxRequest("#frm", "#unitType-list",function(){
					gp_dialogSuccess("บันทึกเรียบร้อย",function(){
						gp_progressBarOff();
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
			gp_postAjaxRequest("#frm", "#unitType-list");
		}catch(e){
			console.error("lp_deleteRecord", e.stack);
		}
	}
	
	function lp_addRow(){
		try{
			$("#command").val("newRecord");
			gp_postAjaxRequest("#frm", "#unitType-list");
		}catch(e){
			console.error("lp_addRow", e.stack);
		}
	}
	
</script>
<s:form name="frm" id="frm" action="unitTypeAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden id="indexRow" name="indexRow" />
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>บันทึกหน่วยสินค้า</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="row">
				<div id="unitType-list" class="col-sm-12">
			    	<%@include file="unitType-list.jsp" %>
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