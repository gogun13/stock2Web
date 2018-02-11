<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		gp_initialPage("#frm");
		
		$('#btnSearch').click(function(){ 
			try{
				$("#pageIndex").val("1");
				$("#command").val("search");
				gp_postAjaxRequest("#frm", "#reciveStockSearch-list");
			}catch(e){
				console.error("btnSearch", e.stack);
			}
			
		});
		
		$('#btnReset').click(function(){ 
			try{
				$("#command").val("success");
				gp_postAjaxRequest("#frm", "#page_main");
			}catch(e){
				console.error("btnReset", e.stack);
			}
			
		});
		
		$("#reciveNo,#reciveDateFrom,#reciveDateTo,#vendorName,#branchName,#reciveStatus").keypress(function(e) {
		    if(e.which == 13) {
		    	$('#btnSearch').click();
		    }
		});
		
	});
	
	function changePage(pageNum) {
		try{
			$("#pageIndex").val(pageNum);
	        $("#command").val("search");
	        gp_postAjaxRequest("#frm", "#reciveStockSearch-list");
		}catch(e){
			console.error("changePage", e.stack);
		}
    }
	
	function lp_gotoDetail(av_reciveNo){
		try{
			$("#hidReciveNo").val(av_reciveNo);
			$("#command").val("gotoDetail");
			gp_postAjaxRequest("#frm", "#page_main");
		}catch(e){
			console.error("lp_gotoDetail", e.stack);
		}
	}
	
</script>
<s:form name="frm" id="frm" action="reciveStockSearchAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<s:hidden id="pageIndex" name="pageIndex" />
	<s:hidden id="hidReciveNo" name="hidReciveNo" />
	<s:hidden id="reciveOrderMasterBean.tin" name="reciveOrderMasterBean.tin" />
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>ค้นหาตามใบสั่งของ</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-body paddingForm">
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="reciveNo" class="control-label">
							เลขที่ใบสั่งของ  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="reciveNo" name="reciveOrderMasterBean.reciveNo" cssClass="form-control" placeholder="เลขที่ใบสั่งของ" />
						<script>$("#reciveNo").focus();</script>
					</div>
					<div class="col-sm-2 text-right">
						<label for="reciveDateFrom" class="control-label">
							วันที่สั่งของ:
						</label>
					</div>
					<div class="col-sm-4 text-left">
						<s:textfield id="reciveDateFrom" name="reciveOrderMasterBean.reciveDateFrom" cssClass="form-control dateFormat" cssStyle="width:40%;display:-webkit-inline-box;" placeholder="วันที่สั่งของ" />
						-
						<s:textfield id="reciveDateTo" name="reciveOrderMasterBean.reciveDateTo" cssClass="form-control dateFormat" cssStyle="width:40%;display:-webkit-inline-box;" placeholder="วันที่สั่งของ" />
					</div>
				</div>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="vendorName" class="control-label">
							บริษัทสั่งซื้อ  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="vendorName" name="reciveOrderMasterBean.vendorName" cssClass="form-control auto-complete" placeholder="บริษัทสั่งซื้อ" />
					</div>
					<div class="col-sm-2 text-right">
						<label for="branchName" class="control-label">
							สาขา:
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:textfield id="branchName" name="reciveOrderMasterBean.branchName" cssClass="form-control auto-complete" placeholder="สาขา" />
					</div>
				</div>
				<div class="col-sm-12">
					<div class="col-sm-2 text-right">
						<label for="reciveStatus" class="control-label">
							สถานะใบสั่งซื้อ  :
						</label>
					</div>
					<div class="col-sm-3 text-left">
						<s:select id="reciveStatus" name="reciveOrderMasterBean.reciveStatus" cssClass="form-control"
	                      list="statusCombo" listKey="code" listValue="desc" />
					</div>
				</div>
				<div class="col-sm-12">
					<div class="col-sm-11 text-right">
						<button type="button" id="btnSearch" name="btnSearch" class="btn btn-success">
					    	<span class="glyphicon glyphicon-search"></span> ค้นหา
					    </button>
						<button type="button" name="btnReset" id="btnReset" class="btn btn-default">
					    	<span class="glyphicon glyphicon-off"></span> เริ่มใหม่
					    </button>
					</div>
					<div class="col-sm-1 text-right"></div>
				</div>
			</div>
			<div class="row">
				<div id="reciveStockSearch-list" class="col-sm-12">
			    	<%@include file="reciveStockSearch-list.jsp" %>
				</div>
			</div>
		</div>
	</div>
</s:form>