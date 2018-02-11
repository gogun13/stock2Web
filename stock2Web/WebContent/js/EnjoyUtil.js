$.datepicker.regional['th'] ={
    changeMonth: true,
    changeYear: true,
    //defaultDate: GetFxupdateDate(FxRateDateAndUpdate.d[0].Day),
    yearOffSet: 543,
    showOn: "button",
    buttonImage: '/stock2Web/img/Calendar.png',
    buttonImageOnly: true,
    dateFormat: 'dd/mm/yy',
    dayNames: ['อาทิตย์', 'จันทร์', 'อังคาร', 'พุธ', 'พฤหัสบดี', 'ศุกร์', 'เสาร์'],
    dayNamesMin: ['อา', 'จ', 'อ', 'พ', 'พฤ', 'ศ', 'ส'],
    monthNames: ['มกราคม', 'กุมภาพันธ์', 'มีนาคม', 'เมษายน', 'พฤษภาคม', 'มิถุนายน', 'กรกฎาคม', 'สิงหาคม', 'กันยายน', 'ตุลาคม', 'พฤศจิกายน', 'ธันวาคม'],
    monthNamesShort: ['ม.ค.', 'ก.พ.', 'มี.ค.', 'เม.ย.', 'พ.ค.', 'มิ.ย.', 'ก.ค.', 'ส.ค.', 'ก.ย.', 'ต.ค.', 'พ.ย.', 'ธ.ค.'],
    constrainInput: true,
   
    prevText: 'ก่อนหน้า',
    nextText: 'ถัดไป',
    yearRange: '-20:+20',
    buttonText: 'เลือก'
  
};

$.datepicker.regional['birthDateTH'] ={
    changeMonth: true,
    changeYear: true,
    yearOffSet: 543,
    showOn: "button",
    buttonImage: '/stock2Web/img/Calendar.png',
    buttonImageOnly: true,
    dateFormat: 'dd/mm/yy',
    dayNames: ['อาทิตย์', 'จันทร์', 'อังคาร', 'พุธ', 'พฤหัสบดี', 'ศุกร์', 'เสาร์'],
    dayNamesMin: ['อา', 'จ', 'อ', 'พ', 'พฤ', 'ศ', 'ส'],
    monthNames: ['มกราคม', 'กุมภาพันธ์', 'มีนาคม', 'เมษายน', 'พฤษภาคม', 'มิถุนายน', 'กรกฎาคม', 'สิงหาคม', 'กันยายน', 'ตุลาคม', 'พฤศจิกายน', 'ธันวาคม'],
    monthNamesShort: ['ม.ค.', 'ก.พ.', 'มี.ค.', 'เม.ย.', 'พ.ค.', 'มิ.ย.', 'ก.ค.', 'ส.ค.', 'ก.ย.', 'ต.ค.', 'พ.ย.', 'ธ.ค.'],
    constrainInput: true,
   
    prevText: 'ก่อนหน้า',
    nextText: 'ถัดไป',
    yearRange: '-100:+0',
    buttonText: 'เลือก'
  
};

$.datepicker.setDefaults($.datepicker.regional['th']);

function isBlank(objValue){
    if(objValue != null && objValue != undefined && gp_trim(objValue) != ''){
        return false;
    }else{
        return true;
    }
}

$(document).ready(function(){
	
	$('body').on('blur', '.moneyOnly',function(event){
		var element_id 	= $(this).attr("id");
		
	    if(!gp_format($("#" + element_id),2) || isBlank($("#" + element_id).val())){
	    	if(!isBlank($("#" + element_id).val())){
	    		gp_alert("ระบุจำนวนเงินผิด format", function(){
	    			$("#" + element_id).val('0.00');
		    	});
	    	}else{
	    		$("#" + element_id).val('0.00');
	    	}
	    	return false;
	    }
	    return true;
	});
	
	$('body').on('blur', '.numberOnly',function(event){
		
		var lv_val 		= gp_replaceComma($(this).val());
		var element_id 	= $(this).attr("id");
		
		if(!$.isNumeric(lv_val) || isBlank(lv_val)){
			if(!isBlank($("#" + element_id).val())){
				gp_alert("ระบุค่าตัวเลขผิด",function(){
					$("#" + element_id).val('0');
				});
			}else{
				$("#" + element_id).val('0');
			}
			return false;
	    }else{
	    	gp_number($("#" + element_id));
	    }
		return true;
	});
	
	$('body').on('blur', '.tin',function(event){
		var element_id 	= $(this).attr("id");
		
		if(!gp_validatePin(element_id) && !isBlank($("#" + element_id).val())){
			gp_alert("เลขประจำตัวผู้เสียภาษีผิด",function(){
				$("#" + element_id).val('');
			});
			return false;
		}
		return true;
	});
	
	$('body').on('blur', '.postCode',function(){
		var lv_val 		= gp_trim($(this).val());
		var la_num   	= ["0","1","2","3","4","5","6","7","8","9"];
		var element_id 	= $(this).attr("id");
		
		if(isBlank(lv_val)){
			return true;
		}
		
		if(lv_val.length!=5){
			gp_alert("ระบุรหัสไปรษณ๊ย์ผิด", function(){
				$("#" + element_id).val('');
			});
			return false;
		}
		
		for(var i=0;i<lv_val.length;i++){
			lv_char = lv_val.substr(i, 1);
			
			if($.inArray(lv_char, la_num) <= -1){
				gp_alert("ระบุรหัสไปรษณ๊ย์ผิด", function(){
					$("#" + element_id).val('');
				});
				return false;
			}
        }
		return true;
	});
	
	$('body').on('blur', '.dateFormat',function(event){
		var element_id 	= $(this).attr("id");
		
		return gp_checkDate("#" + element_id);
	});
	
	$('body').on('mouseover', 'input[type="text"]',function(){
    	$(this).attr('title', $(this).val());
	});
	
	$('body').on('focus', 'input[type="text"]',function(){
		$(this).select();
	});
	
	$('body').on('click', 'input[type="text"]',function(){
		$(this).select();
	});
    
    $( ".dateFormat" ).datepicker( $.datepicker.regional["th"] );
    $( ".dateFormat" ).datepicker( "option", "defaultDate", +0 );
    $( ".birthDateFormat" ).datepicker( $.datepicker.regional["birthDateTH"] );
    $( ".birthDateFormat" ).datepicker( "option", "defaultDate", +0 );
	    
});

//Example gp_replaceComma(78,500.00);
function gp_replaceComma(av_val){
	
	var lv_ret = av_val;
	
	if(av_val!=null && av_val!=""){
		lv_ret = av_val.replace(/,/g,"");
	}

	return lv_ret;
}

function gp_format(ao_obj, decimals){
	return gp_number_format(ao_obj, decimals, '.', ',');
}

function gp_number(ao_obj){
	var lv_decimals = 0;
	var lv_val 		= ao_obj.val();
	
	if(lv_val.indexOf(".") > -1){
		var la_dec = lv_val.split(".");
		if(parseInt(la_dec[1]) > 0){
			if(la_dec[1].length > 1){
				if(la_dec[1].length==2 && la_dec[1].substring(1, 2)=="0"){
					lv_decimals = 1;
				}else{
					lv_decimals = 2;
				}
			}else{
				lv_decimals = 1;
			}
		}
	}
	
	return gp_number_format(ao_obj, lv_decimals, '.', ',');
}

//Example gp_number_format(av_obj, 2, ".", ",");
function gp_number_format (ao_obj, decimals, dec_point, thousands_sep) {
    
    var exponent    = "";
    var numberstr   = null;
    var eindex      = null;
    var temp        = null;
    var sign        = null;
    var integer     = null;
    var fractional  = null;
//    var number      = av_obj.value.replace(/,/g,"");
    var number      = gp_replaceComma(ao_obj.val());
    var msgVal      = "";

    if(gp_trim(number)==""){
        return true;
    }
    
    numberstr   = number.toString ();
    eindex      = numberstr.indexOf ("e");
    
    if (eindex > -1) {
        exponent = numberstr.substring (eindex);
        number = parseFloat (numberstr.substring (0, eindex));
    }
       
    if (decimals != null) {
        temp    = Math.pow (10, decimals);
        number  = Math.round (number * temp) / temp;
    }
    
    sign        = number < 0 ? "-" : "";
    integer     = (number > 0 ? Math.floor (number) : Math.abs (Math.ceil (number))).toString ();
    
    fractional  = number.toString ().substring (integer.length + sign.length);
    dec_point   = dec_point != null ? dec_point : ".";
    fractional  = decimals != null && decimals > 0 || fractional.length > 1 ? (dec_point + fractional.substring (1)) : "";
    
    if (decimals != null && decimals > 0) {
        for (i = fractional.length - 1, z = decimals; i < z; ++i) {
            fractional += "0";
        }
    }
    
    thousands_sep = (thousands_sep != dec_point || fractional.length == 0) ? thousands_sep : null;
    if (thousands_sep != null && thousands_sep != "") {
        for (var i = integer.length - 3; i > 0; i -= 3){
            integer = integer.substring (0 , i) + thousands_sep + integer.substring (i);
        }
    }
    
    msgVal = sign + integer + fractional + exponent;
    if(msgVal.indexOf("NaN") > -1){
        return false;
    }
    
    //av_obj.value = msgVal;
    ao_obj.val(msgVal);
    return true;
    
    //av_obj.value = sign + integer + fractional + exponent;
    //return sign + integer + fractional + exponent;
}

function gp_format_str(av_val, decimals){
	return gp_number_format_str(av_val, decimals, '.', ',');
}

function gp_number_format_str (av_val, decimals, dec_point, thousands_sep) {
    
    var exponent    = "";
    var numberstr   = null;
    var eindex      = null;
    var temp        = null;
    var sign        = null;
    var integer     = null;
    var fractional  = null;
    var number      = av_val.replace(/,/g,"");
    var msgVal      = "";

    if(gp_trim(number)==""){
        return true;
    }
    
    numberstr   = number.toString ();
    eindex      = numberstr.indexOf ("e");
    
    if (eindex > -1) {
        exponent = numberstr.substring (eindex);
        number = parseFloat (numberstr.substring (0, eindex));
    }
       
    if (decimals != null) {
        temp    = Math.pow (10, decimals);
        number  = Math.round (number * temp) / temp;
    }
    
    sign        = number < 0 ? "-" : "";
    integer     = (number > 0 ? Math.floor (number) : Math.abs (Math.ceil (number))).toString ();
    
    fractional  = number.toString ().substring (integer.length + sign.length);
    dec_point   = dec_point != null ? dec_point : ".";
    fractional  = decimals != null && decimals > 0 || fractional.length > 1 ? (dec_point + fractional.substring (1)) : "";
    
    if (decimals != null && decimals > 0) {
        for (i = fractional.length - 1, z = decimals; i < z; ++i) {
            fractional += "0";
        }
    }
    
    thousands_sep = (thousands_sep != dec_point || fractional.length == 0) ? thousands_sep : null;
    if (thousands_sep != null && thousands_sep != "") {
        for (var i = integer.length - 3; i > 0; i -= 3){
            integer = integer.substring (0 , i) + thousands_sep + integer.substring (i);
        }
    }
    
    msgVal = sign + integer + fractional + exponent;
    if(msgVal.indexOf("NaN") > -1){
        return false;
    }
    
    return msgVal;
}

function gp_trim(str) {
    return str.replace(/^\s+|\s+$/g,"");
}

function gp_validNumberKey(e){
//    var keyFormat = /^\d/g;
    var keyFormat = /[^\d\.]/g;
    var charVal = String.fromCharCode(e.keyCode);
    
    return !keyFormat.test(charVal);
}

function gp_validNumberKeyMinus(e){
//    var keyFormat = /^\d/g;
    var keyFormat = /[^\d\.\-]/g;
    var charVal = String.fromCharCode(e.keyCode);
    
    return !keyFormat.test(charVal);
}

function gp_clearMsg(ao_obj){
    ao_obj.value = "";
}

function gp_sanitizeURLString(av_val){
    var la_symbol   = ["%" , "<" , ">" , "#" , "{" , "}" , "|" , "\\" , "^" , "~" , "[" , "]" , "`" , ";" , "/" , "?" , ":" , "@" , "=" , "&" , "$"];
    var la_replace  = ["%25", "%3C", "%3E", "%23", "%7B", "%7D", "%7C", "%5C", "%5E", "%7E", "%5B", "%5D", "%60", "%3B", "%2F", "%3F", "%3A", "%40", "%3D", "%26", "%24"];
    var lv_return   = "";
    var lv_char		= null;

    loop1:for(var i=0;i<av_val.length;i++){
        lv_char = av_val.substr(i, 1);
        loop2:for(var j=0;j<la_symbol.length;j++){
            if(lv_char.indexOf(la_symbol[j]) > -1){
                lv_char = lv_char.split(la_symbol[j]).join(la_replace[j]);
                break loop2;
            }
        }
        lv_return = lv_return + lv_char;
    }
    
    return lv_return;
}

function gp_rowTableIndex(ao_obj){
    var lv_index            = 0;
    var lv_tagName          = "";
    var lo_obj              = ao_obj;
    
    lv_tagName  = lo_obj.tagName.toUpperCase();
    while (lv_tagName != "TR") {
        lo_obj      = lo_obj.parentNode;
        lv_tagName  = lo_obj.tagName.toUpperCase();
    }
    
    lv_index = lo_obj.rowIndex;
    
    return lv_index;
}

function gp_rowTableObj(ao_obj){
    var lv_tagName          = "";
    var lo_obj              = ao_obj;
    
    lv_tagName  = lo_obj.tagName.toUpperCase();
    while (lv_tagName != "TR") {
        lo_obj      = lo_obj.parentNode;
        lv_tagName  = lo_obj.tagName.toUpperCase();
    }
    
    return lo_obj;
}

//av_val ==> dd/MM/yyyy
function gp_toDate(av_val){
    var dateArray   = null;;
    var d           = null;
    
    try{
        dateArray   = av_val.split("/");
        d           = new Date(dateArray[2], parseInt(dateArray[1])-1, dateArray[0], 0, 0, 0, 0);
    }catch(e){
        d = null;
    }
    
    return d;
}

/*
function gp_checkAmtOnly(ao_obj, av_num){
	try{
		if(gp_trim(ao_obj.value)==""){
			ao_obj.value = "0.00";
		}
		
		if(gp_format(ao_obj, 2)==false){
			alert("กรุณาระบุตัวเลขเท่านั้น", function() { 
				ao_obj.value = "0.00";
				eval("$('#" + ao_obj.id + "').focus().select();");
		    });
//			ao_obj.value = "0.00";
//			//ao_obj.focus().select();
//			eval("$('#" + ao_obj.id + "').focus().select();");
			return false;
		}
		
		if(gp_replaceComma(ao_obj.value).length > av_num){
			
			alert("ระบุได้สูงสุด " + (av_num-1) + " ตัวอักษร", function() { 
				$("#" + ao_obj.id).focus().select();
				//eval("$('#" + ao_obj.id + "').focus().select();");
		    });
			
			return false;
		}
		
		return true;
		
	}catch(e){
		alert("gp_checkAmtOnly :: " + e);
	}
}
*/

function gp_progressBarOn(){
	var h 		= $(document).height();
	var x 		= $(document).scrollLeft();
	var y 		= $(document).scrollTop();
	
	$('.FreezeScreen').css('height', h);
    $(".FreezeScreen").fadeIn();
    
    $("body").bind("mousewheel", function() {
        return false;
    });
	
	$("body").bind("keypress", function(evt) {
		var keyCode = evt.keyCode ? evt.keyCode : evt.which;
		if(keyCode>=37 && keyCode<=40){
			return false;
		}
		return true;
    });
	
	$("body").bind("keydown", function(evt) {
		var keyCode = evt.keyCode ? evt.keyCode : evt.which;
		if(keyCode>=37 && keyCode<=40){
			return false;
		}
		return true;
    });
	
	$("body").bind("keyup", function(evt) {
		var keyCode = evt.keyCode ? evt.keyCode : evt.which;
		if(keyCode>=37 && keyCode<=40){
			return false;
		}
		return true;
    });
	
	$(window).scroll(function(event){
		window.scrollTo(x, y);
    });
	
}

function gp_progressBarOff(){
	$(".FreezeScreen").fadeOut("slow");
	$("body").unbind("mousewheel");
	$("body").unbind("keypress");
	$("body").unbind("keydown");
	$("body").unbind("keyup");
	$(window).unbind("scroll");
}

function gp_progressBarOnPopUp(){
	var h 		= $(document).height();
	var x 		= $(document).scrollLeft();
	var y 		= $(document).scrollTop();
	
	$('.FreezeScreenPopUp').css('height', h);
    $(".FreezeScreenPopUp").fadeIn();
    
    $("body").bind("mousewheel", function() {
        return false;
    });
	
	$("body").bind("keypress", function(evt) {
		var keyCode = evt.keyCode ? evt.keyCode : evt.which;
		if(keyCode>=37 && keyCode<=40){
			return false;
		}
		return true;
    });
	
	$("body").bind("keydown", function(evt) {
		var keyCode = evt.keyCode ? evt.keyCode : evt.which;
		if(keyCode>=37 && keyCode<=40){
			return false;
		}
		return true;
    });
	
	$("body").bind("keyup", function(evt) {
		var keyCode = evt.keyCode ? evt.keyCode : evt.which;
		if(keyCode>=37 && keyCode<=40){
			return false;
		}
		return true;
    });
	
	$(window).scroll(function(event){
		window.scrollTo(x, y);
    });
	
}

function gp_progressBarOffPopUp(){
	$(".FreezeScreenPopUp").fadeOut("slow");
	$("body").unbind("mousewheel");
	$("body").unbind("keypress");
	$("body").unbind("keydown");
	$("body").unbind("keyup");
	$(window).unbind("scroll");
}

function gp_checkDate(av_id){
    var minYear 	= 2200;
    //var maxYear 	= (new Date()).getFullYear();
    var errorMsg 	= "";
    var field 		= gp_trim($(av_id).val());

    // regular expression to match required date format
    re = /^(\d{1,2})\/(\d{1,2})\/(\d{4})$/;
	
    try{
	    if(field != '') {
			
	    	if(field.length==8){
	    		field 			= field.substring(0, 2) + "/" + field.substring(2, 4) + "/" + field.substring(4, 8);
	    		$(av_id).val(field);
	    	}
	    	
	      if(regs = field.match(re)) {
	        if(regs[1] < 1 || regs[1] > 31) {
	          errorMsg = "กรอกวันที่ผิด: " + regs[1];
	        } else if(regs[2] < 1 || regs[2] > 12) {
	          errorMsg = "กรอกเดือนผิด: " + regs[2];
	        } else if(regs[3] < minYear) {
	          errorMsg = "กรอกปีผิด: " + regs[3] + " ต้องเป็น พ.ศ. เท่านั้น";
	        }
	      } else {
	        errorMsg = "กรอกรูปแบบวันที่ผิด";
	      }
	    }

	    if(errorMsg != "") {
	    	
	    	gp_alert(errorMsg, function() { 
	    		$(av_id).val("");
	    		$(av_id).select();
		    });
	    	

	      return false;
	    }
    }catch(e){
    	console.error("checkDate", e.stack);
    	return false;
    }

    return true;
  }

function showCalendar() {
    
    /*
    	p_month : 0-11 for Jan-Dec; 12 for All Months.
    	p_year	: 4-digit year
    	p_format: Date format (mm/dd/yyyy, dd/mm/yy, ...)
    	p_item	: Return Item.
    */

    p_item = arguments[0];

    if (arguments[1] == null || arguments[1] == ""){
        p_month = new String(gNow.getMonth());
    }else{
        p_month = arguments[1];
    }
    
    if (arguments[2] == "" || arguments[2] == null){
        p_year = new String(gNow.getFullYear().toString());
    }else{
    	p_year = arguments[2];
    }
	
    if(arguments[3] == "1"){
    	p_format = "DD/MM/YYYY";
    }else if(arguments[3] == "2"){
    	p_format = "MM/DD/YYYY";
    }else{
        p_format="YYYY/MM/DD";
    }
    
    //vWinCal = window.open("", "calendar", "width=150,height=180,status=no,resizable=no,toolbar=no,top=250,left=450");
    // Set position for click as easier [surachai].
    p_top = arguments[4];
    p_left = arguments[5];
    
    vWinCal = window.open("", "calendar", "width=230,height=170,status=no,resizable=no,toolbar=no,top="+(p_top+70)+",left="+p_left+",screenY=400");
    vWinCal.opener = self;
    ggWinCal = vWinCal;
    vWinCal.focus();
    build(p_item, p_month, p_year, p_format);
}

function removeComma(obj) {
    var val = obj.value;
    val = val.replace(/,/g, '');
    obj.value = val;
}

function formatNumber(obj) {
    var val = obj.value;
    val = $.trim(val);
    if (val != '') {
        val += '';
        x = val.split('.');
        x1 = x[0];
        x2 = x.length > 1 ? '.' + x[1] : '.00';
        var rgx = /(\d+)(\d{3})/;
        while (rgx.test(x1)) {
            x1 = x1.replace(rgx, '$1' + ',' + '$2');
        }
        obj.value = x1 + x2;
    }
}

function gp_validateTime(ao_obj){
	var errorMsg		= "";
	var re				= null;
	var lv_time			= "";
	var lv_timeTmp		= "";
	var lv_txtLength	= 0;

	try{
		re			= /^(\d{1,2}):(\d{2})(:00)?([ap]m)?$/;
		lv_time		= gp_trim(ao_obj.value);

		if(lv_time != "") {
			lv_txtLength = lv_time.length;
			if(lv_txtLength==3 && lv_time.indexOf(":") < 0){
				lv_timeTmp		= lv_time.substring(0, 1) + ":" + lv_time.substring(1, 3);
				ao_obj.value	= lv_timeTmp;
			}else if(lv_txtLength==4 && lv_time.indexOf(":") < 0){
				lv_timeTmp		= lv_time.substring(0, 2) + ":" + lv_time.substring(2, 4);
				ao_obj.value	= lv_timeTmp;
			}else{
				lv_timeTmp = lv_time;
			}

			if(regs = lv_timeTmp.match(re)) {
				if(!regs[4]) {
				  // 24-hour time format
				  if(regs[1] > 23) {
					errorMsg = "Invalid value for hours: " + regs[1];
				  }
				}else{
					errorMsg = "Invalid time format: " + lv_timeTmp;
				}

				if(!errorMsg && regs[2] > 59) {
				  errorMsg = "Invalid value for minutes: " + regs[2];
				}

			} else {
				errorMsg = "Invalid time format: " + lv_timeTmp;
			}
		}

		if(errorMsg != "") {
			gp_alert(errorMsg, function() { 
				ao_obj.focus();
		    });
		
//		  alert(errorMsg);
//		  ao_obj.focus();
		  return false;
		}

		return true;

	}catch(e){
		console.error("gp_validateTime", e.stack);
		return false;
	}
  }

//สำหรับ Validate format E-mail
function gp_checkemail(av_val){
   var Email = null;
   
   try{
	   
	   Email=/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	   if(av_val!=""){
		   if(!av_val.match(Email)){
		     gp_alert('รูปแบบ Email ไม่ถูกต้อง');
		     return false;
		   }else{
			 return true;
		   }
	   }
	   
	   return true;
	   
   }catch(e){
	   console.error("gp_checkemail", e.stack);
	   return false;
   }
}

// Verify PIN
function gp_validatePin(av_id){
	/*
		1. Get the first digit multiples with 13 and the next digit multiples with 12 .. and go on .. the twelve digit will minus with 2
		2. Sum the result from (1)
		3. Get the result from (2) MOD with 11
		4. Get the result from (3) (after modulus) minus with 11
		5. The result from (4) is checked digit for compare with the last digit of PIN.
		**ps.- if the remainder of mod is 0 -- digi =1
		          - if the remainder of mod is 1-- digi =0
	*/
	var sum_unit_pin = 0;
	var tempMod = 0;
	var check_digit 	= "";
	var lv_pin 			= $("#" + av_id).val().trim().replace(/-/g,"");
	var unit_pin 		= new Array(lv_pin.length);
	var multiple_pin 	= new Array(13,12,11,10,9,8,7,6,5,4,3,2);
	
	if(lv_pin.length!=13){
		return false;
	}
	
	
	for (var i=0;i<lv_pin.length;i++){
		unit_pin[i] = lv_pin.substring(i,i+1);
		if(i != (lv_pin.length-1)){
			unit_pin[i] = unit_pin[i] * multiple_pin[i];
			sum_unit_pin = sum_unit_pin+unit_pin[i];
		}
	}
	tempMod = sum_unit_pin % 11 ;

	switch(tempMod){
		case 0	:	check_digit = 1;
							break;
		case 1	:	check_digit = 0;
							break;
 	    default	:	check_digit = 11- tempMod ;
							break;
	}

	if(check_digit == unit_pin[lv_pin.length-1]){
		gp_setFormatPin(av_id);
		return true;
	}else{
		return false;
	}
}

function gp_setFormatPin(av_id){
	var lv_pin 		= "";
	var lv_newPin 	= "";
	
	try{
		lv_pin = $("#" + av_id).val().trim().replace(/-/g,"");
		
		if(lv_pin!="" && lv_pin.length==13){
			for(var i=0;i<lv_pin.length;i++){
				
				if(i==1 || i==5 || i==10 || i==12){
					lv_newPin += "-" + lv_pin[i];
				}else{
					lv_newPin += lv_pin[i];
				}
			}
			//alert(lv_newPin);
			$("#" + av_id).val(lv_newPin);
		}
		
	}catch(e){
		console.error("gp_setFormatPin", e.stack);
	}
}

function gp_parseFloat(av_val){
	
	var av_ret = av_val!=null && av_val.trim()!=""?parseFloat(gp_replaceComma(av_val)):0.00;
	
	return av_ret;
}



function gp_setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}

function gp_getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
    }
    return "";
}

function gp_validateEmptyObj(ga_array){
	var la_temp = null;
	var lo_obj 	= null;
	
	try{
		for(var i=0;i<ga_array.length;i++){
			la_temp			= ga_array[i].split(":");
            lo_obj          = eval('$("#' + la_temp[0] + '")');
            
            if(gp_trim(lo_obj.val())==""){
            	gp_alert("กรุณาระบุ " + la_temp[1], function() { 
            		lo_obj.focus();
    		    });
            	
                return false;
            }
        }
		
		return true;
	}catch(e){
		console.error("lp_validateEmptyObj", e.stack);
		return false;
	}
}

function gp_alert(message, successCallback, autoIndicator){
	
	if(autoIndicator == undefined ){
        autoIndicator = true;
    }
	
    if(autoIndicator){
    	gp_progressBarOnPopUp();
    }
	
	$(document.createElement('div'))
	    .html("<p>" + message + "</p>")
	    .dialog({
	      autoOpen: true,
	      //height: 175,
	      width: 600,
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
	      close: function() {
	    	  if(autoIndicator)gp_progressBarOffPopUp();
	    	  
	    	  if(successCallback!=null && successCallback != 'undefined'){
	    		  successCallback();
	    	  }
	    	  $( this ).removeClass( "enjoyPopUp" );
	        },
	      title: "การแจ้งเตือน",
	      //draggable: false,
        buttons: {
          "ตกลง": function() {
        	$( this ).dialog( "close" );
            
            return;
          }
        },
	      dialogClass: 'enjoyPopUp'
	    });
};

function gp_dialogSuccess(message, successCallback, autoIndicator){
	
	if(autoIndicator == undefined ){
        autoIndicator = true;
    }
	
    if(autoIndicator){
    	gp_progressBarOnPopUp();
    }
	
	$(document.createElement('div'))
	    .html("<p style=\"color:#1ea710;font-size:1.2em;\"><strong><span class=\"glyphicon glyphicon-saved btn-lg\"></span></strong>" + message + "</p>")
	    .dialog({
	      autoOpen: true,
	      //height: 175,
	      width: 300,
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
	      close: function() {
	    	  if(autoIndicator)gp_progressBarOffPopUp();
	    	  
	    	  if(successCallback!=null && successCallback != 'undefined'){
	    		  successCallback();
	    	  }
	    	  $( this ).removeClass( "enjoyPopUp" );
	        },
	      //title: "การแจ้งเตือน",
	      //draggable: false,
	    modal: true,
        buttons: {
          "ตกลง": function() {
        	$( this ).dialog( "close" );
            
            return;
          }
        },
	      dialogClass: 'enjoyPopUp'
	    }).prev(".ui-dialog-titlebar").css({"background":"#88f07e"});
};

function gp_confirm(message, successCallback, nonSuccessCallback, autoIndicator, w, h){
	
	if(autoIndicator == undefined )autoIndicator = true;
	if(w == undefined )w = 600;
    if(h == undefined )h = 175;
	
    if(autoIndicator){
    	gp_progressBarOnPopUp();
    }
	
	$(document.createElement('div'))
	    .html("<p>" + message + "</p>")
	    .dialog({
	      autoOpen: true,
	      height: h,
	      width: w,
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
	      close: function() {
	    	  if(autoIndicator)gp_progressBarOffPopUp();
	    	  $( this ).removeClass( "enjoyPopUp" );
	        },
	      title: "การแจ้งเตือน",
	      draggable: false,
        buttons: {
          ตกลง: function() {
	    	  if(successCallback!=null && successCallback != 'undefined'){
	    		  successCallback();
	    	  }
	    	  $( this ).dialog( "close" );
	    	  return;
          },
          ยกเลิก: function() {
        	  if(nonSuccessCallback!=null && nonSuccessCallback != 'undefined'){
        		  nonSuccessCallback();
	    	  }
	    	  $( this ).dialog( "close" );
	    	  return;
          }
        },
	      dialogClass: 'enjoyPopUp'
	    });
};

function gp_dialogPopUpPdf(av_path, av_title, callFunction, autoIndicator){
	
	var lo_iframe = null;
	
	try{
		$( "#dialog" ).empty();
		$( "#dialog" ).css("overflow-y", "hidden");
		
		lo_iframe	= $("<object />").attr("data", av_path)
									 .attr("width", "100%")
									 .attr("height", "100%")
									 .attr("type", "application/pdf");
		
		if(autoIndicator == undefined )autoIndicator = true;
		if(autoIndicator){
	    	gp_progressBarOn();
	    }
		
		$( "#dialog" )
		    .html(lo_iframe)
		    .dialog({
	      autoOpen: true,
	      height: 600,
	      width: 1050,
	      show: {
	        effect: "clip",
	        duration: 500
	      },
	      hide: {
	        effect: "clip",
	        duration: 500
	      },
	      close: function() {
	    	  if(autoIndicator)gp_progressBarOff();
	    	  $( "#dialog" ).removeClass( "zoom" );
	    	  if(callFunction!=null && callFunction != 'undefined'){
	    		  callFunction();
	    	  }
	        },
	      title: av_title,
	      buttons: null,
	      draggable: false,
	      dialogClass: 'zoom'
	    });
		
	}catch(e){
		console.error("gp_dialogPopUpPdf", e.stack);
	}
}

function gp_dialogPopUpHtml(av_html, av_title, w, h){
	
	try{
		w = w==null || w==""?1050:w;
		h = h==null || h==""?600:h;
		
		
		$( "#dialog" ).empty();
		
		gp_progressBarOn();
		
		$( "#dialog" )
		    .html(av_html)
		    .dialog({
	      autoOpen: true,
	      height: h,
	      width: w,
	      show: {
	        effect: "clip",
	        duration: 300
	      },
	      hide: {
	        effect: "clip",
	        duration: 300
	      },
	      close: function() {
	    	  gp_progressBarOff();
	    	  $( "#dialog" ).removeClass( "zoom" );
	        },
	      title: av_title,
	      buttons: null,
	      draggable: false,
	      dialogClass: 'zoom'
	    });
		
	}catch(e){
		console.error("gp_dialogPopUpHtml", e.stack);
	}
}

function gp_checkMonth(ao_obj){
    var minYear 	= 2200;
    var errorMsg 	= "";
    var field 		= gp_trim(ao_obj.value);
    var la_str		= null;

    try{
	    if(field != '') {
	    	
	    	if(field.length!=6 && field.length!=7){
	    		errorMsg = "กรอกรูปแบบเดือน/ปีผิด";
	    	}else{
	    		if(field.length==6 && field.indexOf("/") < 0){
		    		field 			= field.substring(0, 2) + "/" + field.substring(2, 6);
		    		ao_obj.value 	= field;
		    		la_str			= field.split("/");
		    	}else{
		    		if(field.length==7 && field.indexOf("/") >= 0){
		    			la_str			= field.split("/");
		    		}else{
		    			errorMsg = "กรอกรูปแบบเดือน/ปีผิด";
		    		}
		    	}
	    	}
	    	
	    	if(la_str!=null){
	    		if(la_str[0] < 1 || la_str[0] > 12) {
	  	          errorMsg = "กรอกเดือนผิด: " + la_str[0];
	  	        }else if(la_str[1] < minYear) {
	  	          errorMsg = "กรอกปีผิด: " + la_str[1] + " ต้องเป็น พ.ศ. เท่านั้น";
		        }
	    	}
	    }
			
	    if(errorMsg != "") {
	    	
	    	gp_alert(errorMsg, function() { 
	    		ao_obj.value = "";
		    	ao_obj.focus();
		    });
	    	
	    }
    }catch(e){
    	console.error("gp_checkMonth", e.stack);
    	return false;
    }

    return true;
}

	function gp_checkThaiLetter(av_txtChk){
		var letters = /^[u0E01-u0E5B]+$/;
		
		try{
			if(!(letters.test(av_txtChk))) return false;
			
			return true;
		}catch(e){
			console.error("gp_checkThaiLetter", e.stack);
	    	return false;
		}
	}
	
	function gp_getAjaxRequest(url, divId, afterGet, autoIndicator){
	    if(autoIndicator == undefined )autoIndicator = true;

	    if(autoIndicator){
	    	gp_progressBarOn();
	    }
	    
	    gp_beforesendToServer();
	    
	    $.ajax({
	    	async:true,
            type: "GET",
            url: url,
            success: function(data){
            	gp_initialPage();
            	displayDiv(data, divId, afterGet, autoIndicator);
            }
        });

	    /*$.get(url, function(data) {
	    	displayDiv(data, divId, afterGet, autoIndicator);
	    });*/
	}
	
	function gp_postAjaxRequest(form, divId, afterPost, autoIndicator){
		if(autoIndicator == undefined )autoIndicator = true;
		console.log(autoIndicator);
	    if(autoIndicator){
	    	gp_progressBarOn();
	    }
	    
	    gp_beforesendToServer();

	    var queryString = $(form).serialize();

	    var actionUrl = $(form).attr('action');
	    if(isBlank(actionUrl)){
	    	if(autoIndicator)gp_progressBarOff();
	        gp_alert("ไม่ได้ระบุ action ใน form");
	        return;
	    }
	    
	    $.ajax({
	    	async:true,
            type: "POST",
            url: actionUrl,
            data: queryString,
            success: function(data){
            	gp_initialPage();
            	displayDiv(data, divId, afterPost, autoIndicator);
            }
        });

	    /*$.post(actionUrl ,queryString, function(data) {
	    	displayDiv(data, divId, afterPost, autoIndicator);
	    });*/
	}
	
	function displayDiv(data, divId, afterPost, autoIndicator){
		//console.log(data);
		if(data.indexOf("<!--#ERROR-->",0) == 0){
	        $("#biz_error_div").html(data);
	    }else if(data.indexOf("<!--#ERROR_BUSINESS-->",0) == 0){
	        $("#biz_error_div").html(data);
	    }else if(data.indexOf("<!--#SESSION FAIL LOGIN-->",0) == 0){
	    	window.location.replace('<c:url value="/jsp/error/loginFail.jsp"/>'); 
	    }else{
	    	if (!isBlank(divId))$(divId).html(data);
	    	if($.isFunction(afterPost))afterPost(data);
	    	if(autoIndicator)gp_progressBarOff();
	    }
	}
	
	function gp_initialPage(av_formId){
		try{
			$( ".moneyOnly" ).each(function( index ) {
				if(!gp_format($(this),2) || isBlank($(this).val())){
			    	$(this).val('0.00');
			    }
			});
			
			$( ".numberOnly" ).each(function( index ) {
				var lv_val = gp_replaceComma($(this).val());
				
				if(!$.isNumeric(lv_val) || isBlank(lv_val)){
			    	$(this).val('0');
			    }else{
			    	gp_number($(this));
			    }
			});
			
			$( ".tin" ).each(function( index ) {
				
				var element_id 	= $(this).attr("id");
				
				console.log(element_id);
				
				if(!gp_validatePin(element_id)){
					$(this).val('');
				}
			});
			
			if(av_formId != undefined ){
				gp_autoComplete(av_formId);
				$( ".dateFormat" ).datepicker( $.datepicker.regional["th"] );
			    $( ".dateFormat" ).datepicker( "option", "defaultDate", +0 );
			}
			
			
		}catch(e){
			console.error("gp_initialPage", e.stack);
		}
	}
	
	function gp_beforesendToServer(){
		try{
			$( ".moneyOnly" ).each(function( index ) {
				if(!gp_format($(this),2) || isBlank($(this).val())){
			    	$(this).val('0.00');
			    }else{
			    	$(this).val(gp_replaceComma($(this).val()));
			    }
			});
			
			$( ".numberOnly" ).each(function( index ) {
				var lv_val = gp_replaceComma($(this).val());
				
				if(!$.isNumeric(lv_val) || isBlank(lv_val)){
			    	$(this).val('0');
			    }else{
			    	$(this).val(lv_val);
			    }
			});
			
			$( ".tin" ).each(function( index ) {
				
				var lv_val = $(this).val();
				
				if(!isBlank(lv_val)){
					lv_val = lv_val.replace(/-/g,"");
				}else{
					lv_val = "";
				}
				
				$(this).val(lv_val);
			});
			
			
			
		}catch(e){
			console.error("gp_beforesendToServer", e.stack);
		}
	}
	
	function gp_autoComplete(av_form,av_fnSuccess ,av_fnOpen ,av_fnClose ,av_fnFocus ,av_fnSelect){
		try{
			
		    var actionUrl 	= $(av_form).attr('action');
		    
		    if(isBlank(actionUrl)){
		        gp_alert("ไม่ได้ระบุ action ใน form");
		        return;
		    }
			
			$(".auto-complete").autocomplete({
				 source: function(request, response) {
					var autoCompleteName 	= $(this.element).attr("name");
					
					$(av_form + " input[name='command']").val("autoComplete");
					
					var queryString 		= $(av_form).serialize();
					
		            $.ajax({
		            	async:false,
			            type: "POST",
		                url: actionUrl,
		                dataType: "json",
		                data: "autoCompleteName=" + autoCompleteName + "&autoCompParamter=" + gp_trim(request.term) + "&" + queryString,
		                success: function( data, textStatus, jqXHR) {
		                    var items = data;
		                    
		                    response(items);
		                    if($.isFunction(av_fnSuccess))av_fnSuccess(data, textStatus, jqXHR);
		                },
		                error: function(jqXHR, textStatus, errorThrown){
		                     gp_alert( "เกิดข้อผิดพลาดในการใช้ระบบ กรุณาติดต่อผู้ดูแลระบบ");
		                     console.error("gp_autoComplete", errorThrown.stack);
		                }
		            });
		          },
			      minLength: 0,//กี่ตัวอักษรถึงทำงาน
			      open: function() {
			    	  $(".ui-autocomplete").css("z-index", "2147483647");
			    	  if($.isFunction(av_fnOpen))av_fnOpen();
			      },
			      close: function() {
			    	  if($.isFunction(av_fnClose))av_fnClose();
			      },
			      focus:function(event,ui) {
			    	  if($.isFunction(av_fnFocus))av_fnFocus();
			      },
			      select: function( event, ui ) {
			    	  if($.isFunction(av_fnSelect))av_fnSelect();
			      }
			});
			
		}catch(e){
			console.error("gp_autoComplete", e.stack);
		}
	}






















