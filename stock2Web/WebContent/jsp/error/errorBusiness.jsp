<!--#ERROR_BUSINESS-->
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script type="text/javascript">
	$(document).ready(function(){
		//gp_progressBarOnPopUp();
		
	    $("#dialogErr").dialog({
	        autoOpen: true,
	        bgiframe:true,
	        modal: true,
	        width: 600,
	        draggable: true,
	        show: {
		        effect: "slide",
		        direction: "up",
		        duration: 200
		    },
		    hide: {
		        effect: "slide",
		        direction: "up",
		        duration: 200
		    },
	        close: function(event, ui){
	            $(this).dialog('destroy').remove();
	        },
	        buttons: {
	            "ตกลง": function() {
	                $(this).dialog("close");
	                gp_progressBarOff();
	                gp_progressBarOffPopUp();
	            }
	        },
	        dialogClass: 'enjoyPopUp'
	    });
	
	    //$("#dialogErr").parents(".ui-dialog:first").find(".ui-dialog-titlebar").addClass("ui-state-error");
	});
</script>

<div id="dialogErr" title="การแจ้งเตือน" >
	<p>
		<%--<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>--%>
	    <s:if test="!errorMsgList.isEmpty">
	    	<ul>
	    		<s:iterator status="stat" var="item" value="errorMsgList">
		   			<li><s:property/></li>
		   		</s:iterator>
			</ul>
		</s:if>
    </p>
</div>
<span id="errMsgValErr" style="display:none"><s:actionmessage /><s:actionerror/></span>

