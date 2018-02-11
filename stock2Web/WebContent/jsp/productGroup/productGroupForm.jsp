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
		            if(isBlank($("#productGroupCodeDis" + rowIndex).val())){
		            	gp_alert("รหัสหมู่สินค้าห้ามเป็นค่าว่าง", function(){
		            		$("#productGroupCodeDis" + rowIndex).focus();
		            	});
		            	lv_ret = false;
		            	return false;
		            }else if(isBlank($("#productGroupName" + rowIndex).val())){
		            	gp_alert("ชื่อหมู่สินค้าห้ามเป็นค่าว่าง", function(){
		            		$("#productGroupName" + rowIndex).focus();
		            	});
		            	lv_ret = false;
		            	return false;
		            }
		            
		            lv_ret = true;
		        });
		    	
		    	if(!lv_ret)return;
				
				$("#command").val("save");
				gp_progressBarOn();
				gp_postAjaxRequest("#frm", "#productGroup-list",function(){
					gp_dialogSuccess("บันทึกเรียบร้อย",function(){
						gp_progressBarOff();
					},false);
				},false);
			}catch(e){
				console.error("btnSave", e.stack);
			}
		});
		
		$('#btnSearch').click(function(){ 
			try{
				if(isBlank($("#productTypeName").val())){
					gp_alert("กรุณาระบุหมวดสินค้า", function() { 
						$("#productTypeName").focus();
	    		    });
	                return;
	            }
				
				$("#command").val("onSearch");
				gp_postAjaxRequest("#frm", "#page_main");
			}catch(e){
				console.error("btnSearch", e.stack);
			}
		});
		
		$("#productTypeName").keypress(function(e) {
		    if(e.which == 13) {
		    	$('#btnSearch').click();
		    }
		});
		
		$('input[type="file"]').ajaxfileupload({
			'action': '<s:url action="productGroupAction.action?command=uploadExcelFileFile" />',           
		   	'onComplete': function(response) {
		   					$(":file").filestyle('clear');
		   					lp_newRecordFromExcelFile(response);
					     },
			'validate_extensions' : true,
			'valid_extensions' : [ 'xls', 'xlsx' ],
		    'submit_button' :  $('#hidBtnUpload')
		 });
		
		$(":file").filestyle({buttonText: "เลือกไฟล์",size: "md"});
		
		$('#btnUpload').click(function(){ 
			try{
				if (isBlank($("#uploadedFile").val()))return;
				
				gp_progressBarOn();
				$('#hidBtnUpload').click();
			}catch(e){
				console.error("btnUpload", e.stack);
			}
		});
		
	});
	
	function lp_newRecordFromExcelFile(av_json){
	    var status		= av_json.STATUS;
	    
		try{
			console.log(av_json);
			if(status!="SUCCESS"){
				var lv_msg = av_json.MSG;
				if(lv_msg == undefined ){
					lv_msg = "เกิดข้อผิดพลาดในการใช้ระบบ กรุณาติดต่อผู้ดูแลระบบ";
				}
				
				gp_alert(av_json.MSG,function(){
					gp_progressBarOff();
				},false);
				return;
			}
			
			$("#command").val("newRecordFromExcelFile");
			$("#hidProductGroupJsonList").val(JSON.stringify(av_json));
			gp_postAjaxRequest("#frm", "#productGroup-list",function(){
				gp_progressBarOff();
			},false);
			
		}catch(e){
			console.error("lp_newRecordFromExcelFile", e.stack);
		}
	}
	
	function lp_deleteRecord(av_seq){
		try{
			$("#indexRow").val(av_seq);
			
			$("#command").val("deleteRecord");
			gp_postAjaxRequest("#frm", "#productGroup-list");
		}catch(e){
			console.error("lp_deleteRecord", e.stack);
		}
	}
	
	function lp_addRowtable(){
		try{
			$("#command").val("newRecord");
			gp_postAjaxRequest("#frm", "#productGroup-list");
		}catch(e){
			console.error("lp_addRowtable", e.stack);
		}
	}
	
</script>
<s:form name="frm" id="frm" action="productGroupAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden id="indexRow" name="indexRow" />
	<s:hidden id="hidProductGroupJsonList" name="hidProductGroupJsonList" />
	<s:hidden id="productTypeCode" name="productTypeCode" />
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>เพิ่มหมู่สินค้า</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<div class="col-sm-2 text-right">
					<label for="productTypeName" class="control-label">
						หมวดสินค้า  <span style="color: red;"><b>*</b></span>:
					</label>
				</div>
				<div class="col-sm-4 text-left">
					<s:if test="productGroupList!=null">
						<s:property value="productTypeName"/>
						<s:hidden name="productTypeName" id="productTypeName"/>
					</s:if>
					<s:else>
						<s:textfield id="productTypeName" name="productTypeName" cssClass="form-control auto-complete" placeholder="หมวดสินค้า" maxlength="200"  />
						<script>$("#productTypeName").focus();</script>
					</s:else>
				</div>
				<div class="col-sm-5 text-left">
					<s:if test="productGroupList==null">
						<button type="button" id="btnSearch" name="btnSearch" class="btn btn-success">
					    	<span class="glyphicon glyphicon-search"></span> ค้นหา
					    </button>
				    </s:if>
				</div>
			</div>
			<s:if test="productGroupList!=null">
				<div class="row" style="padding-top: 15px;padding-bottom: 15px;">
					<div class="col-sm-1"></div>
					<div class="col-sm-4 text-left">
						<s:file name="uploadedFile" id="uploadedFile" />
					</div>
					<div class="col-sm-7 text-left">
						<button type="button" name="btnUpload" id="btnUpload" class="btn btn-default  btn-md">
					    	<span class="glyphicon glyphicon-upload"></span> อัพโหลด
					    </button>
					    <a href="<c:url value='/upload/UploadproductGroup.xlsx'/>">ตัวอย่างไฟล์อัพโหลด</a>
					    <input type="button" id="hidBtnUpload" name="hidBtnUpload" style="display: none;" />
					</div>
				</div>
			</s:if>
			<div class="row">
				<div id="productGroup-list" class="col-sm-12">
			    	<%@include file="productGroup-list.jsp" %>
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
	</div>
</s:form>