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
				gp_postAjaxRequest("#lookUpFrm", "#customerLookUp-list",function(){
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
		
		$("#beanForLookUp\\.cusCode,#beanForLookUp\\.fullName,#beanForLookUp\\.tel").keypress(function(e) {
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
		
		$("#beanForLookUp\\.cusCode").focus();
		
	});
	
	function changePageLookUp(pageNum) {
		try{
			$("#pageIndexLookUp").val(pageNum);
	        $("#commandLookUp").val("search");
	        gp_progressBarOnPopUp();
	        gp_postAjaxRequest("#lookUpFrm", "#customerLookUp-list",function(){
	        	gp_progressBarOffPopUp();
	        },false);
		}catch(e){
			console.error("changePage", e.stack);
		}
    }
	
</script>
<div id="dialogLookUp" title="ค้นหาลุกค้า">
<s:form name="lookUpFrm" id="lookUpFrm" action="customerLookUpAction" onsubmit="return false;">
	<s:hidden name="command" id="commandLookUp"/>
	<s:hidden name="pageIndex" id="pageIndexLookUp"  />
	<s:hidden name="beanForLookUp.cusStatus" id="beanForLookUp.cusStatus"  />
	<div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="beanForLookUp.cusCode" class="control-label">
							รหัสลูกค้า  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="beanForLookUp.cusCode" name="beanForLookUp.cusCode" cssClass="form-control" placeholder="รหัสลูกค้า" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="beanForLookUp.fullName" class="control-label">
							ชื่อ-นามสกุล :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="beanForLookUp.fullName" name="beanForLookUp.fullName" cssClass="form-control auto-complete" placeholder="ชื่อ-นามสกุล" />
					</div>
				</div>
				<div class="col-sm-12">
					<div class="col-sm-3 text-right">
						<label for="beanForLookUp.tel" class="control-label">
							เบอร์โทรศัพท์   :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="beanForLookUp.tel" name="beanForLookUp.tel" cssClass="form-control" placeholder="เบอร์โทรศัพท์" />
					</div>
				</div>
				<div class="col-sm-12">
					<div class="col-sm-11 text-right">
						<button type="button" id="btnLookUpSearch" name="btnLookUpSearch" class="btn btn-success">
					    	<span class="glyphicon glyphicon-search"></span> ค้นหา
					    </button>
						<button type="button" name="btnLookUpReset" id="btnLookUpReset" class="btn btn-default">
					    	<span class="glyphicon glyphicon-off"></span> เริ่มใหม่
					    </button>
					</div>
				</div>
			</div>
			<div class="row">
				<div id="customerLookUp-list" class="col-sm-12">
			    	<%@include file="customerLookUp-list.jsp" %>
				</div>
			</div>
		</div>
	</div>
</s:form>
</div>