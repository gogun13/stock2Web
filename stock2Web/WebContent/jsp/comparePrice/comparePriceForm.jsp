<div id="dialogLookUp" title="เปรียบเทียบราคา">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#frmComparePrice");
		
		$('#btnResetNormal').click(function(){ 
			try{
				$("#commandComparePrice").val("success");
				gp_postAjaxRequest("#frmComparePrice", "#page_main");
			}catch(e){
				console.error("btnResetNormal", e.stack);
			}
		});
		
		/*$('#btnResetComparePrice').click(function(){ 
			try{
				$("#commandComparePrice").val("resetPopUp");
				gp_progressBarOnPopUp();
				gp_postAjaxRequest("#frmComparePrice", "#dialogLookUp",function(){
					gp_progressBarOffPopUp();
				},false);
			}catch(e){
				console.error("btnResetComparePrice", e.stack);
			}
		});*/
		
		$('#btnSaveNormal').click(function(){ 
			var lv_ret = true;
			
			try{
				$("input[name=rowIndexComparePrice]").each(function() {
					var rowIndex = $(this).val();
		            if(isBlank($("#vendorName" + rowIndex).val())){
		            	gp_alert("บริษัทห้ามเป็นค่าว่าง", function(){
		            		$("#vendorName" + rowIndex).focus();
		            	});
		            	lv_ret = false;
		            	return false;
		            }else if(isBlank($("#branchName" + rowIndex).val())){
		            	gp_alert("สาขาห้ามเป็นค่าว่าง", function(){
		            		$("#branchName" + rowIndex).focus();
		            	});
		            	lv_ret = false;
		            	return false;
		            }else if(isBlank($("#vendorCode" + rowIndex).val())){
		            	gp_alert("ระบุบริษัทหรือสาขาผิด", function(){
		            		$("#vendorName" + rowIndex).focus();
		            	});
		            	lv_ret = false;
		            	return false;
		            }
		            
		            lv_ret = true;
		        });
		    	
		    	if(!lv_ret)return;
		    	
		    	lp_setComparePriceRemark();
				
				$("#commandComparePrice").val("save");
				gp_progressBarOn();
				gp_postAjaxRequest("#frmComparePrice", "",function(){
					gp_dialogSuccess("บันทึกเรียบร้อย",function(){
						gp_progressBarOff();
					},false);
				},false);
			}catch(e){
				console.error("btnSaveNormal", e.stack);
			}
		});
		
		$('#btnSaveComparePrice').click(function(){ 
			var lv_ret = true;
			
			try{
				$("input[name=rowIndexComparePrice]").each(function() {
					var rowIndex = $(this).val();
		            if(isBlank($("#vendorName" + rowIndex).val())){
		            	gp_alert("บริษัทห้ามเป็นค่าว่าง", function(){
		            		$("#vendorName" + rowIndex).focus();
		            	});
		            	lv_ret = false;
		            	return false;
		            }else if(isBlank($("#branchName" + rowIndex).val())){
		            	gp_alert("สาขาห้ามเป็นค่าว่าง", function(){
		            		$("#branchName" + rowIndex).focus();
		            	});
		            	lv_ret = false;
		            	return false;
		            }else if(isBlank($("#vendorCode" + rowIndex).val())){
		            	gp_alert("ระบุบริษัทหรือสาขาผิด", function(){
		            		$("#vendorName" + rowIndex).focus();
		            	});
		            	lv_ret = false;
		            	return false;
		            }
		            
		            lv_ret = true;
		        });
		    	
		    	if(!lv_ret)return;
		    	
		    	lp_setComparePriceRemark();
				
				$("#commandComparePrice").val("save");
				gp_progressBarOnPopUp();
				gp_postAjaxRequest("#frmComparePrice", "",function(){
					gp_dialogSuccess("บันทึกเรียบร้อย",function(){
						gp_progressBarOffPopUp();
					},false);
				},false);
			}catch(e){
				console.error("btnSaveComparePrice", e.stack);
			}
		});
		
		$('#btnSearchComparePrice').click(function(){ 
			try{
				if(isBlank($("#productName").val())){
					gp_alert("กรุณาระบุสินค้า", function() { 
						$("#productName").focus();
	    		    });
	                return;
	            }
				
				$("#commandComparePrice").val("onSearch");
				gp_postAjaxRequest("#frmComparePrice", "#page_main");
			}catch(e){
				console.error("btnSearch", e.stack);
			}
		});
		
		$("#productName").keypress(function(e) {
		    if(e.which == 13) {
		    	$('#btnSearchComparePrice').click();
		    }
		});
		
		if($("#comparePriceFlagPopUp").val()=="Y" && $("#comparePriceFlagPopUpRepeat").val()=="N"){
			$("#divContainer").removeClass("container");
			
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
		        	CKEDITOR.instances['comparePriceTextEditor'].destroy();
		        	CKEDITOR.disableAutoInline = true;

		        	CKEDITOR.inline('comparePriceTextEditor');

		        	$("#comparePriceTextEditor").attr("contenteditable", "true");
		        	
		        	gp_progressBarOff();
		        	gp_initialPage("#frm");
		            $(this).dialog('destroy').remove();
		        },
		        buttons: null,
			    //dialogClass: 'zoom',
			    open: function(){
			    	$(this).focus();
		    	}
		    });
		}
		
		lp_initComparePriceTextEditor();
	});
	
	function lp_deleteComparePriceRecord(av_seq){
		try{
			$("#indexRowComparePrice").val(av_seq);
			
			$("#commandComparePrice").val("deleteRecord");
			gp_postAjaxRequest("#frmComparePrice", "#comparePrice-list",null,false);
		}catch(e){
			console.error("lp_deleteComparePriceRecord", e.stack);
		}
	}
	
	function lp_addComparePriceRowtable(){
		try{
			$("#commandComparePrice").val("newRecord");
			gp_postAjaxRequest("#frmComparePrice", "#comparePrice-list",null,false);
		}catch(e){
			console.error("lp_addComparePriceRowtable", e.stack);
		}
	}
	
	function lp_getCompanyVendorDetail(ao_obj, av_seq){
		try{
			if(isBlank(ao_obj.val())){
				return;
			}
			
			if(ao_obj.attr("id").indexOf("vendorName") > -1){
				$("#branchName" + av_seq).val("");
			}
			
			$("#indexRowComparePrice").val(av_seq);
			
			$("#commandComparePrice").val("getCompanyVendorDetail");
			gp_postAjaxRequest("#frmComparePrice", "",function(data){
				var jsonObj = null;
            	
            	try{
            		jsonObj = JSON.parse(data);
            		
            		$("#vendorCode" + av_seq).val(jsonObj.vendorCode);
        			$("#vendorName" + av_seq).val(jsonObj.vendorName);
        			$("#branchName" + av_seq).val(jsonObj.branchName);
        			
            	}catch(e){
            		console.error("After call getCompanyVendorDetail", e.stack);
            	}
			},false);
		}catch(e){
			console.error("lp_getCompanyVendorDetail", e.stack);
		}
	}
	
	function lp_initComparePriceTextEditor(){
		try{
			if ( CKEDITOR.env.ie && CKEDITOR.env.version < 9 )CKEDITOR.tools.enableHtml5Elements( document );

			CKEDITOR.config.height = 150;
			CKEDITOR.config.width = 'auto';
			CKEDITOR.config.startupFocus = true;

			var editorElement = CKEDITOR.document.getById( 'comparePriceTextEditor' );
			if(editorElement!=null){
				if(CKEDITOR.instances['comparePriceTextEditor']){
					CKEDITOR.instances['comparePriceTextEditor'].destroy();
				}
				
				editorElement.setHtml($("#comparePriceRemark").val());
				CKEDITOR.replace( 'comparePriceTextEditor' );
			}
			
		}catch(e){
			console.error("lp_initComparePriceTextEditor", e.stack);
		}
	}
	
	function lp_setComparePriceRemark(){
		try{
			$("#comparePriceRemark").val(CKEDITOR.instances['comparePriceTextEditor'].getData());
		}catch(e){
			console.error("lp_setComparePriceRemark", e.stack);
		}
	}
	
</script>
<s:form name="frmComparePrice" id="frmComparePrice" action="comparePriceAction" onsubmit="return false;">
	<s:hidden id="commandComparePrice" name="command" />
	<s:hidden id="indexRowComparePrice" name="indexRow" />
	<s:hidden id="productCode" name="productCode" />
	<s:hidden id="comparePriceFlagPopUp" name="comparePriceFlagPopUp" />
	<s:hidden id="comparePriceFlagPopUpRepeat" name="comparePriceFlagPopUpRepeat" />
	<div id="divContainer" class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>เปรียบเทียบราคา</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<div class="col-sm-2 text-right">
					<label for="productName" class="control-label">
						สินค้า  <span style="color: red;"><b>*</b></span>:
					</label>
				</div>
				<div class="col-sm-4 text-left">
					<s:if test="comparePriceList!=null">
						<s:property value="productName"/>
						<s:hidden name="productName" id="productName"/>
					</s:if>
					<s:else>
						<s:textfield id="productName" name="productName" cssClass="form-control auto-complete" placeholder="สินค้า" maxlength="255"  />
						<script>$("#productName").focus();</script>
					</s:else>
				</div>
				<div class="col-sm-5 text-left">
					<s:if test="comparePriceList==null">
						<button type="button" id="btnSearchComparePrice" name="btnSearchComparePrice" class="btn btn-success">
					    	<span class="glyphicon glyphicon-search"></span> ค้นหา
					    </button>
				    </s:if>
				</div>
			</div>
			<div class="row">
				<div id="comparePrice-list" class="col-sm-12">
			    	<%@include file="comparePrice-list.jsp" %>
				</div>
			</div>
			<s:if test="comparePriceList!=null">
				<div class="row">
					<div class="col-sm-2 text-right">
						<label for="comparePriceRemark" class="control-label">
							หมายเหต :
						</label>
					</div>
					<div class="col-sm-9 text-left">
						<s:textarea id="comparePriceRemark" name="comparePriceRemark" cssStyle="display:none;"></s:textarea>
						<div id="comparePriceTextEditor"></div>
					</div>
					<div class="col-sm-1"></div>
				</div>
				<div class="row">
					<div class="col-sm-12" style="height: 20px;"></div>
				</div>
				<s:if test='"Y".equals(comparePriceFlagPopUp)'>
					<div class="row" >
						 <div class="col-sm-12 text-right">
				         	<button type="button" name="btnSaveComparePrice" id="btnSaveComparePrice" class="btn btn-primary  btn-md">
						    	<span class="glyphicon glyphicon-floppy-save"></span> บันทึก
						    </button>
						    <%--
						    <button type="button" name="btnResetComparePrice" id="btnResetComparePrice" class="btn btn-default  btn-md">
						    	<span class="glyphicon glyphicon-off"></span> เริ่มใหม่
						    </button>
						     --%>
						    <span style="width:20px;">&nbsp;</span>
						</div>
						<div class="col-sm-12 text-right" style="height:20px;"></div>
					</div>
				</s:if>
				<s:else>
					<div class="row" >
						 <div class="col-sm-12 text-right">
				         	<button type="button" name="btnSaveNormal" id="btnSaveNormal" class="btn btn-primary  btn-md">
						    	<span class="glyphicon glyphicon-floppy-save"></span> บันทึก
						    </button>
						    <button type="button" name="btnResetNormal" id="btnResetNormal" class="btn btn-default  btn-md">
						    	<span class="glyphicon glyphicon-off"></span> เริ่มใหม่
						    </button>
						    <span style="width:20px;">&nbsp;</span>
						</div>
						<div class="col-sm-12 text-right" style="height:20px;"></div>
					</div>
				</s:else>
			</s:if>
		</div>
	</div>
</s:form>
</div>