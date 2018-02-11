<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<style>
	.welcome-img{
		width:80%;
    	height: auto;
    	opacity:0.8;
	}
</style>
<script type="text/javascript">
	$(document).ready(function(){
		
		var refreshImage = setInterval(function() {
			$( "#divImage" ).show();
			$( "#divImage" ).effect( "bounce", "slow" );
			clearInterval(refreshImage);
			}, 1000);
		
	});
	
</script>
<div class="container">
	<div class="row">
			<div class="col-sm-12" style="height: 80px;"></div>
		</div>
	<div class="row">
		<div class="col-sm-1"></div>
		<div class="col-sm-10 text-center" id="divImage" style="display: none;">
			<img class="welcome-img" id="welcomeImg" src="<c:url value='/img/welcome.png'/>" alt="" />
		</div>
		<div class="col-sm-1"></div>
	</div>	
</div>