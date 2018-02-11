<!--#LOGIN SUCCESS-->
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<title>ระบบจัดการสินค้า</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<link rel="icon" href="<c:url value='/img/icon.ico'/>" type="image/x-icon">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="<c:url value='/css/bootstrap.css'/>">
		<link rel="stylesheet" href="<c:url value='/css/jquery-ui.css'/>">
		<link rel="stylesheet" href="<c:url value='/css/Enjoy.css'/>">
		<script src="<c:url value='/js/jquery-3.2.1.js'/>"></script>
		<script src="<c:url value='/js/bootstrap.js'/>"></script>
		<script src="<c:url value='/js/jquery-ui.js'/>"></script>
		<script src="<c:url value='/js/jquery.AjaxFileUpload.js'/>"></script>
		<script src="<c:url value='/js/bootstrap-filestyle.js'/>"></script>
		<script src="<c:url value='/ckeditor/ckeditor.js'/>"></script>
		<script src="<c:url value='/js/EnjoyUtil.js'/>"></script>
		<script>
		$(document).ready(function(){
			$('ul.nav li.dropdown').hover(function() {
			  $(this).find('.dropdown-menu').stop(true, true).delay(200).fadeIn(100);
			}, function() {
			  $(this).find('.dropdown-menu').stop(true, true).delay(200).fadeOut(100);
			});
			
			$('#logOut').click(function(){ 
				try{
					gp_confirm("ต้องการออกจากระบบ?", function(){
						window.location.replace('<s:url action="logout.action" />');
					});
				}catch(err){
					console.log("logOut :: " + err);
				}
				
			});
			
			var gv_pageAction = '<s:property value="pageAction"/>';
			if(gv_pageAction!=null && gv_pageAction!=""){
				lp_menuLink('<s:url action="index.action?command=" />' + gv_pageAction);
			}
			
			var gv_timeOut = 0;
		    $(document).mousemove(function(event){
		    	gv_timeOut = 0;
		    });
		    
		    $(document).keypress(function(){
		    	gv_timeOut = 0;
		    });
		    
		    setInterval(function(){ 
		    	gv_timeOut++;
		    	
		    	if(gv_timeOut==45){
		    		window.location.replace('<s:url action="logout.action?command=clearInterval" />');
		    	}
		    }, 60000);
			
		});
		
		function lp_menuLink(gv_url){
			try{
				gp_getAjaxRequest(gv_url, "#page_main");
			}catch(e){
				console.log("lp_menuLink :: " + e);
			}
		}
		
		</script>
	</head>
	<body>
		<nav class="navbar navbar-inverse navbar-fixed-top">
		  <div class="container-fluid">
		    <div class="navbar-header pull-left">
		      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span>                        
		      </button>
		    </div>
		    <div class="collapse navbar-collapse" id="myNavbar">
		      <ul class="nav navbar-nav">
		      	<s:iterator status="stat" value="currentUser.userPrivilegeList" var="item">
			        <li class="dropdown">
			          <a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0);"><s:property value="privilegeName"/> <span class="caret"></span></a>
			          <ul class="dropdown-menu">
			          	<s:iterator status="stat1" value="#item.pagesDetail" var="item1">
			          		<li ><a href="javascript:void(0);" onclick="lp_menuLink('<s:url action="%{#item1.pathPages}" />')"><s:property value="#item1.pageNames"/></a></li>
			          	</s:iterator>
			          </ul>
			        </li>
		        </s:iterator>
		      </ul>
		      <ul class="nav navbar-nav navbar-right">
		        <li class="dropdown">
		        	<a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0);" onclick="lp_menuLink('<s:url action="index" />?command=welcome')">
		        		<s:property value="currentUser.userEmail" /> <span class="glyphicon glyphicon-user"></span> <span class="caret"></span>
		        	</a>
		        	<ul class="dropdown-menu">
		            <li><a href="javascript:void(0);" onclick="lp_menuLink('<s:url action="changePassAction.action" />')">เปลี่ยนรหัสผ่าน</a></li>
		            <li><a id="logOut" href="javascript:void(0);">ออกจากระบบ</a></li>
		          </ul>
		        </li>
		      </ul>
		    </div>
		  </div>
		</nav>
		<div style="height: 50px;"></div>
		<div id="page_main"><%@include file="welcome.jsp" %></div>
		<div id="biz_error_div" style="display:none"></div>
		<div id="printDiv" style="display: none;"></div>
		<div id="dialog" title="Look up"></div>
		<div align="center" class="FreezeScreen" style="display:none;">
        	<img id="imgProgress" src="<c:url value='/img/loading36.gif'/>" alt="" />
        	<span style="font-weight: bold;font-size: large;color: black;">Loading...</span>
    	</div>
    	<div align="center" class="FreezeScreenPopUp" style="display:none;">
        	<img id="imgProgress" src="<c:url value='/img/loading36.gif'/>" alt="" />
        	<span style="font-weight: bold;font-size: large;color: black;">Loading...</span>
    	</div>
	</body>
</html>