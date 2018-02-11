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
			
			var lv_ret = true;
			
		    try{
		    	$("input[name=rowIndex]").each(function() {
		            if($("#codeDisplay" + $(this).val()).val().trim()==""){
		            	gp_alert("รหัสเอกสารห้ามเป็นค่าว่าง", function(){
		            		$("#codeDisplay" + $(this).val()).focus();
		            	});
		            	lv_ret = false;
		            	return false;
		            }
		            lv_ret = true;
		        });
		    	
		    	if(!lv_ret)return;
		    	
		    	$("#command").val("onSave");
				gp_progressBarOn();
				gp_postAjaxRequest("#frm", "#page_main", function(){
					gp_dialogSuccess("บันทึกเรียบร้อย",function(){
						gp_progressBarOff();
					});
				}, false);
		    	
		    }catch(e){
		    	console.error("btnSave", e.stack);
		    }
		});
		
	});
	
	function lp_clickCheckBox(av_id1,av_id2){
		try{
			if ($('#'+av_id1).is(':checked')) {
				$('#'+av_id2).val("Y");
			}else{
				$('#'+av_id2).val("N");
			}
		}catch(e){
	    	console.error("lp_clickCheckBox", e.stack);
	    }
	}
	
</script>
<s:form name="frm" id="frm" action="refconstantcodeAction" onsubmit="return false;">
	<s:hidden name="command" id="command"/>
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-left header">
                <h2>ตั้งค่าระบบ</h2>
            </div>
		</div>
		<div class="row">
			<div class="col-sm-12" style="height: 20px;"></div>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading">
		        <h3 class="panel-title">ตั้งค่ารหัสเอกสาร</h3>
			</div>
			<div class="row">
				<div class="col-sm-12">
					<table class="table table-striped table-bordered table-criteria">
					    <thead>
					      <tr>
					        <th width="5%">ลำดับ</th>
							<th width="10%">มีปีนำหน้า</th>
							<th width="20%">รหัสเอกสาร</th> 
							<th width="35%">ชื่อเอกสาร(ไทย)</th> 
							<th width="30%">ชื่อเอกสาร(อังกฤษ)</th>
					      </tr>
					    </thead>
					    <tbody>
					    	<s:if test="!section1List.isEmpty">
					    		<s:set var="seqRow" value="0"/>
					    		<s:iterator status="stat" value="section1List" var="item">
					    			<tr>
										<td style="text-align:center">
											<s:set var="seqRow" value="%{#seqRow+1}"/>
											<s:property value="%{#seqRow}" />
										</td>
										<td align="center">
											<s:if test='#item.flagEdit.equals("Y")'>
												<s:checkbox id="flagYearBoolean%{#stat.index}" 
									                		name="#item.flagYearBoolean" 
									                		onclick="lp_clickCheckBox('flagYearBoolean%{#stat.index}','flagYear%{#stat.index}');"
									                		cssStyle="width:15px;height:15px;"
									                		value="%{#item.flagYearBoolean}"
									                		fieldValue="true"/>
							                </s:if>
						                	<s:else>
						                		<s:checkbox id="flagYearBoolean%{#stat.index}" 
									                		name="#item.flagYearBoolean"
									                		cssStyle="width:15px;height:15px;"
									                		value="%{#item.flagYearBoolean}"
									                		disabled="true"
									                		fieldValue="true"/>
						                	</s:else>
											<s:hidden id="flagYear%{#stat.index}" name="section1List[%{#stat.index}].flagYear" />
											<s:hidden id="id%{#stat.index}" name="section1List[%{#stat.index}].id" />
											<s:hidden id="tin%{#stat.index}" name="section1List[%{#stat.index}].tin" />
											<input type="hidden" id='rowIndex<s:property value="#stat.index"/>' name="rowIndex" value='<s:property value="#stat.index"/>'/>
										</td>
										<td>
											<s:textfield id="codeDisplay%{#stat.index}" name="section1List[%{#stat.index}].codeDisplay" cssClass="form-control" maxlength="3" placeholder="รหัสเอกสาร" />
										</td>
										<td>
											<s:property value="#item.codeNameTH"/>
											<s:hidden id="codeNameTH%{#stat.index}" name="section1List[%{#stat.index}].codeNameTH" />
										</td>
										<td>
											<s:property value="#item.codeNameEN"/>
											<s:hidden id="codeNameEN%{#stat.index}" name="section1List[%{#stat.index}].codeNameEN" />
										</td>
									</tr>
								</s:iterator>
					    	</s:if>
						</tbody>
					 </table>
				</div>
			</div>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading">
		        <h3 class="panel-title">ตั้งค่ารายละเอียดโปรแกรม</h3>
			</div>
			<div class="row" style="height: 100px;">
				<div class="col-sm-12">
					<div class="col-sm-12" style="height: 20px;"></div>
					<s:iterator status="stat" value="section2List" var="item">
						<div class="col-sm-12">
							<s:if test='#item.flagEdit.equals("Y")'>
								<s:checkbox id="sendMailFlg%{#stat.index}" 
					                		name="sendMailFlg" 
					                		onclick="lp_clickCheckBox('sendMailFlg%{#stat.index}','section2CodeDisplay%{#stat.index}');"
					                		cssStyle="width:15px;height:15px;"
					                		value="%{sendMailFlg}"
					                		fieldValue="true"/>
				            </s:if>
				            <s:else>
					            <s:checkbox id="sendMailFlg%{#stat.index}" 
					                		name="sendMailFlg" 
					                		disabled="true"
					                		cssStyle="width:15px;height:15px;"
					                		value="%{sendMailFlg}"
					                		fieldValue="true"/>
				            </s:else>
				            <span>:<s:property value="#item.codeNameTH"/></span>
				            <s:hidden id="section2Id%{#stat.index}" name="section2List[%{#stat.index}].id" />
							<s:hidden id="section2Tin%{#stat.index}" name="section2List[%{#stat.index}].tin" />
							<s:hidden id="section2CodeDisplay%{#stat.index}" name="section2List[%{#stat.index}].codeDisplay" />
							<s:hidden id="section2List[%{#stat.index}].codeNameTH" name="section2List[%{#stat.index}].codeNameTH" />
							<s:hidden id="section2List[%{#stat.index}].codeNameEN" name="section2List[%{#stat.index}].codeNameEN" />
							<s:hidden id="section2List[%{#stat.index}].flagYear" name="section2List[%{#stat.index}].flagYear" />
							<s:hidden id="section2List[%{#stat.index}].flagEdit" name="section2List[%{#stat.index}].flagEdit" />
						</div>
					</s:iterator>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<div class="col-sm-12 text-right">
					<button type="button" name="btnSave" id="btnSave" class="btn btn-primary  btn-md">
				    	<span class="glyphicon glyphicon-floppy-save"></span> บันทึก
				    </button>
				    <button type="button" name="btnReset" id="btnReset" class="btn btn-default  btn-md">
				    	<span class="glyphicon glyphicon-off"></span> เริ่มใหม่
				    </button>
				</div>
			</div>
			<div class="col-sm-12" style="height: 30px;"></div>
		</div>
	</div>
</s:form>