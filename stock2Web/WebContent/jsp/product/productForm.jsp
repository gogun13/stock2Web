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
				gp_progressBarOn();
				gp_postAjaxRequest("#frm", "",function(data){
					var jsonObj = JSON.parse(data);
					
					gp_dialogSuccess("บันทึกเรียบร้อย",function(){
						$("#command").val("getDetail");
						$("#hidProductCode").val(jsonObj.productCode);
						gp_postAjaxRequest("#frm","#page_main",function(){
							gp_progressBarOff();
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
		
		$('#btnCancel').click(function(){ 
			try{
				gp_progressBarOn();
				gp_confirm("คุณแน่ใจว่าต้องการยกเลิกสินค้ารายการนี้", function(){
					$("#command").val("cancel");
					gp_postAjaxRequest("#frm","",function(){
						gp_alert("ยกเลิกเรียบร้อย", function() {
							gp_getAjaxRequest('<s:url action="productSearchAction.action" />', "#page_main",function(){
        						gp_progressBarOff();
        					},false);
		    		    }, false);
					},false);
					
				}, function(){gp_progressBarOff();}, false);
				
			}catch(e){
				console.error("btnCancel", e.stack);
			}
		});
		
	});
	
	function lp_deleteRecord(av_seq){
		try{
			$("#indexRow").val(av_seq);
			
			$("#command").val("deleteRecord");
			gp_postAjaxRequest("#frm", "#product-list");
		}catch(e){
			console.error("lp_deleteRecord", e.stack);
		}
	}
	
	function lp_addRowtable(){
		try{
			$("#command").val("newRecord");
			gp_postAjaxRequest("#frm", "#product-list");
		}catch(e){
			console.error("lp_addRowtable", e.stack);
		}
	}
	
	function lp_validate(){
		var la_validate               = new Array("productCodeDis:รหัสสินค้า"	
												,"productName:ชื่อสินค้า"
												, "productTypeName:หมวดสินค้า"
												, "productGroupName:หมู่สินค้า"
												, "unitName:หน่วยสินค้า"
												, "minQuan:ยอดต่ำสุดที่ต้องแจ้งเตือน"
												, "costPrice:ราคาทุน"
												, "salePrice1:ราคาขาย 1");
		var lv_ret = true;
	    
		try{
			if(!gp_validateEmptyObj(la_validate)){
				return false;
			}
			
			if(!(gp_checkThaiLetter($("#productCodeDis").val()))){
				gp_alert("รหัสสินค้าต้องเปนภาษาอังกฤษหรือตัวเลขเท่านั้น !!", function() { 
					$("#productCodeDis").val('');
        			$("#productCodeDis").focus();
    		    });
				return;
			}
			
			$("input[name=rowIndex]").each(function() {
				var rowIndex = $(this).val();
	            if(isBlank($("#quanDiscount" + rowIndex).val())){
	            	gp_alert("ปริมาณที่ซื้อห้ามเป็นค่าว่าง", function(){
	            		$("#quanDiscount" + rowIndex).focus();
	            	});
	            	lv_ret = false;
	            	return false;
	            }else if(isBlank($("#discountRate" + rowIndex).val())){
	            	gp_alert("ลดจำนวนเงินห้ามเป็นค่าว่าง", function(){
	            		$("#discountRate" + rowIndex).focus();
	            	});
	            	lv_ret = false;
	            	return false;
	            }else if(isBlank($("#startDate" + rowIndex).val())){
	            	gp_alert("วันที่มีผลห้ามเป็นค่าว่าง", function(){
	            		$("#startDate" + rowIndex).focus();
	            	});
	            	lv_ret = false;
	            	return false;
	            }
				
	            if(!isBlank($("#expDate" + rowIndex).val())){
					if(gp_toDate($("#startDate" + rowIndex).val().trim()) > gp_toDate($("#expDate" + rowIndex).val().trim())){
						gp_alert("วันที่สิ้นสุดการใช้งานต้องมากกว่าวันที่มีผล", function() { 
							$("#expDate" + rowIndex).focus();
		    		    });
						lv_ret = false;
		                return false;
	                }
				}
	            
	            lv_ret = true;
	        });
			
			return lv_ret;
			
		}catch(e){
			console.error("lp_validate", e.stack);
			return false;
		}
	}
	
</script>
<s:form name="frm" id="frm" action="productAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden name="pageMode" id="pageMode"/>
	<s:hidden name="indexRow" id="indexRow"/>
	<s:hidden name="hidProductCode" id="hidProductCode"/>
	<s:hidden name="showBackFlag" id="showBackFlag" />
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>จัดการรายละเอียดสินค้า</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading">
		        <h3 class="panel-title"><s:property value="titlePage"/></h3>
			</div>
			<div class="panel-body paddingForm">
				<%--Begin Row 1 --%>
				<div class="col-sm-12">
					<div class="col-sm-5 text-right">
						<label for="productCodeDis" class="control-label">
							รหัสสินค้า <span style="color: red;"><b>*</b></span>  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="productCodeDis" name="productmasterBean.productCodeDis" cssClass="form-control" maxlength="17" placeholder="รหัสสินค้า" />
						<s:hidden id="productCode" name="productmasterBean.productCode" />
						<s:hidden id="productStatus" name="productmasterBean.productStatus" />
					</div>
				</div>
				<%--End Row 1 --%>
				<%--Begin Row 2 --%>
				<div class="col-sm-12">
					<div class="col-sm-5 text-right">
						<label for="productName" class="control-label">
							ชื่อสินค้า <span style="color: red;"><b>*</b></span>  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="productName" name="productmasterBean.productName" cssClass="form-control" maxlength="255" placeholder="ชื่อสินค้า" />
					</div>
				</div>
				<%--End Row 2 --%>
				<%--Begin Row 3 --%>
				<div class="col-sm-12">
					<div class="col-sm-5 text-right">
						<label for="productTypeName" class="control-label">
							หมวดสินค้า<span style="color: red;"><b>*</b></span>  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="productTypeName" name="productmasterBean.productTypeName" cssClass="form-control auto-complete" maxlength="200" placeholder="หมวดสินค้า" />
						<s:hidden name="productTypeCode" id="productmasterBean.productTypeCode"/>
					</div>
				</div>
				<%--End Row 3 --%>
				<%--Begin Row 4 --%>
				<div class="col-sm-12">
					<div class="col-sm-5 text-right">
						<label for="productGroupName" class="control-label">
							หมู่สินค้า<span style="color: red;"><b>*</b></span>  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="productGroupName" name="productmasterBean.productGroupName" cssClass="form-control auto-complete" maxlength="200" placeholder="หมู่สินค้า" />
						<s:hidden name="productGroupCode" id="productmasterBean.productGroupCode"/>
					</div>
				</div>
				<%--End Row 4 --%>
				<%--Begin Row 5 --%>
				<div class="col-sm-12">
					<div class="col-sm-5 text-right">
						<label for="unitName" class="control-label">
							หน่วยสินค้า <span style="color: red;"><b>*</b></span>  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="unitName" name="productmasterBean.unitName" cssClass="form-control auto-complete" maxlength="200" placeholder="หน่วยสินค้า " />
						<s:hidden name="unitCode" id="productmasterBean.unitCode"/>
					</div>
				</div>
				<%--End Row 5 --%>
				<%--Begin Row 6 --%>
				<div class="col-sm-12">
					<div class="col-sm-5 text-right">
						<label for="minQuan" class="control-label">
							ยอดต่ำสุดที่ต้องแจ้งเตือน <span style="color: red;"><b>*</b></span>  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="minQuan" name="productmasterBean.minQuan" cssClass="form-control numberOnly" placeholder="ยอดต่ำสุดที่ต้องแจ้งเตือน" />
					</div>
				</div>
				<%--End Row 6 --%>
				<%--Begin Row 7 --%>
				<div class="col-sm-12">
					<div class="col-sm-5 text-right">
						<label for="costPrice" class="control-label">
							ราคาทุน <span style="color: red;"><b>*</b></span>  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="costPrice" name="productmasterBean.costPrice" cssClass="form-control moneyOnly" placeholder="ราคาทุน" />
					</div>
				</div>
				<%--End Row 7 --%>
				<%--Begin Row 8 --%>
				<div class="col-sm-12">
					<div class="col-sm-5 text-right">
						<label for="salePrice1" class="control-label">
							ราคาขาย 1 <span style="color: red;"><b>*</b></span>  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="salePrice1" name="productmasterBean.salePrice1" cssClass="form-control moneyOnly" placeholder="ราคาขาย 1" />
					</div>
				</div>
				<%--End Row 8 --%>
				<%--Begin Row 9 --%>
				<div class="col-sm-12">
					<div class="col-sm-5 text-right">
						<label for="salePrice2" class="control-label">
							ราคาขาย 2  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="salePrice2" name="productmasterBean.salePrice2" cssClass="form-control moneyOnly" placeholder="ราคาขาย 2" />
					</div>
				</div>
				<%--End Row 9 --%>
				<%--Begin Row 10 --%>
				<div class="col-sm-12">
					<div class="col-sm-5 text-right">
						<label for="salePrice3" class="control-label">
							ราคาขาย 3  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="salePrice3" name="productmasterBean.salePrice3" cssClass="form-control moneyOnly" placeholder="ราคาขาย 3" />
					</div>
				</div>
				<%--End Row 10 --%>
				<%--Begin Row 11 --%>
				<div class="col-sm-12">
					<div class="col-sm-5 text-right">
						<label for="salePrice4" class="control-label">
							ราคาขาย 4  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="salePrice4" name="productmasterBean.salePrice4" cssClass="form-control moneyOnly" placeholder="ราคาขาย 4" />
					</div>
				</div>
				<%--End Row 11 --%>
				<%--Begin Row 12 --%>
				<div class="col-sm-12">
					<div class="col-sm-5 text-right">
						<label for="salePrice5" class="control-label">
							ราคาขาย 5  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="salePrice5" name="productmasterBean.salePrice5" cssClass="form-control moneyOnly" placeholder="ราคาขาย 5" />
					</div>
				</div>
				<%--End Row 12 --%>
				<div class="col-sm-12">
					<div id="product-list" class="col-sm-12">
				    	<%@include file="product-list.jsp" %>
					</div>
				</div>
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
					    <s:if test='"UPDATE".equals(pageMode)'>
	                		<button type="button" id="btnCancel" name="btnCancel" class="btn btn-danger  btn-md">
						    	<span class="glyphicon glyphicon-trash"></span> ยกเลิกสินค้า
						    </button>
	                	</s:if>
					    <button type="button" name="btnReset" id="btnReset" class="btn btn-default  btn-md">
					    	<span class="glyphicon glyphicon-off"></span> เริ่มใหม่
					    </button>
					</div>
				</div>
			</div>
		</div>
	</div>
</s:form>