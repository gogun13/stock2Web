package com.enjoy.core.utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class EnjoyUtils {
	
	public static void main(String[] args) {
//		System.out.println(String.format("%09d", 1));
		
		try{
			System.out.println(convertNumberToDisplay("1.209"));
		}catch(Exception e){
			e.printStackTrace();
		}
		
//		System.out.println(getLastDayOfMonth("01/03/2559"));
		
//		currDateThai();
//		System.out.println(dateToStringThai(date));
//		genPassword();
//		increaseDate(new Date(), 15);

	}
	
	public static String nullToStr(Object str){
		
        return (str==null?"":str.toString().trim());
    }

	public static String nullToStr(Object str, String strRep){
        return (str==null?strRep:str.toString().trim());
    }
	
	public static String nullToStrUpperCase(Object str){
        return (str==null?"":str.toString().trim().toUpperCase());
    }
	
    public static String dateToString(Date dDate, String stFormat){
        String stDate = "";
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(stFormat,Locale.US);
            stDate = sdf.format(dDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
           
        return stDate;
    }
    
    public static String dateToStringThai(Date dDate){
        String 	stDate 	= "";
        int 	year 	= 0;
        int 	month 	= 0;
        int 	day 	= 0;
        try{
//            SimpleDateFormat 	sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            Calendar 			c 	= Calendar.getInstance(Locale.US);
            c.setTime(dDate);
            
            
            year 	= c.get(Calendar.YEAR);
			month 	= c.get(Calendar.MONTH) + 1;
			day 	= c.get(Calendar.DATE);
            
//            stDate = sdf.format(dDate);
            stDate = String.format("%02d/%02d/%04d", day, month, year+543);
        } catch (Exception e) {
            e.printStackTrace();
        }
           
        return stDate;
    }
    
    public static String increaseDate(Date dDate, int increaseDay){
    	Calendar 	c 		= null;
    	String 		stDate 	= "";
        int 		year 	= 0;
        int 		month 	= 0;
        int 		day 	= 0;
    	
    	try{
    		c = Calendar.getInstance(Locale.US); 
    		c.setTime(dDate);
    		c.add(Calendar.DATE, increaseDay);
    		
    		year 	= c.get(Calendar.YEAR);
			month 	= c.get(Calendar.MONTH) + 1;
			day 	= c.get(Calendar.DATE);
			
			stDate = String.format("%02d/%02d/%04d", day, month, year+543);
			
			System.out.println(stDate);
    	}catch (Exception e) {
            e.printStackTrace();
        }
    	return stDate;
    }
    
    public static String currDateThai(){
        String 	stDate 	= "";
        Date 	date	= new Date();
        
        try{
            java.text.SimpleDateFormat df= new java.text.SimpleDateFormat("yyyyMMdd", new Locale("th", "TH"));
            
//            df.applyPattern("yyyyMMdd");
            stDate = df.format(date);
            System.out.println("currDateThai :: " + stDate);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
           
        return stDate;
    }
    
//    public static String dateFormat(Object ao_obj, String av_currFormat, String av_toFormat){
//        System.out.println("[FormatUtil][dateFormat][Begin]");
//        
//        SimpleDateFormat    dt              = null;
//        Date                date            = null;
//        SimpleDateFormat    dt1             = null;
//        String              dateFormat      = null;
//        String				lv_val			= "";
//        
//        try{
//        	lv_val = ao_obj==null?"":ao_obj.toString().trim();
//        	
//        	System.out.println("[FormatUtil][dateFormat] lv_val 		:: " + lv_val);
//        	System.out.println("[FormatUtil][dateFormat] av_currFormat 	:: " + av_currFormat);
//        	System.out.println("[FormatUtil][dateFormat] av_toFormat 	:: " + av_toFormat);
//        	
//            if(lv_val==null || lv_val.equals("")){
//                dateFormat = "";
//            }else{
//                dt      = new SimpleDateFormat(av_currFormat); 
//                date    = dt.parse(lv_val); 
//                
//                System.out.println("[FormatUtil][dateFormat] date 	:: " + date);
//                
////                dt1     = new SimpleDateFormat(av_toFormat,Locale.US);// ค.ศ.
//                dt1     = new SimpleDateFormat(av_toFormat, new Locale("th", "TH"));//พ.ศ.
//                dateFormat = dt1.format(date);
//                
//                System.out.println("[FormatUtil][dateFormat] dateFormat 	:: " + dateFormat);
//            }
//        }catch(Exception e){
//                e.printStackTrace();
//        }finally{
//            System.out.println("[FormatUtil][dateFormat][End]");
//        }
//        
//        return dateFormat;
//    }
    
    public static String dateThaiToDb(Object ao_obj){
        System.out.println("[FormatUtil][dateFormat][Begin]");
        
        String              dateFormat      = "";
        String				lv_val			= "";
        String				day				= "";
        String				month			= "";
        String				year			= "";
        String[]			date			= null;
        
        try{
        	lv_val = ao_obj==null?"":ao_obj.toString().trim();
        	
        	System.out.println("[FormatUtil][dateThaiToDb] lv_val 		:: " + lv_val);
        	
            if(!lv_val.equals("")){
            	date = lv_val.split("/");
            	dateFormat = date[2] + date[1] + date[0];
            }
        }catch(Exception e){
                e.printStackTrace();
        }finally{
            System.out.println("[FormatUtil][dateThaiToDb][End]");
        }
        
        return dateFormat;
    }
    
    public static String  sanitizeURLString(String av_val){
        
        System.out.println("[FormatUtil][sanitizeURLString][Begin]");
        
        String[]    la_symbol   = {"%" , "<" , ">" , "#" , "{" , "}" , "|" , "\\" , "^" , "~" , "[" , "]" , "`" , ";" , "/" , "?" , ":" , "@" , "=" , "&" , "$"};
        String[]    la_replace  = {"%25", "%3C", "%3E", "%23", "%7B", "%7D", "%7C", "%5C", "%5E", "%7E", "%5B", "%5D", "%60", "%3B", "%2F", "%3F", "%3A", "%40", "%3D", "%26", "%24"};
        String      lv_return   = "";
        String      lv_char     = "";
        
        try{
            loop1:for(int i=0;i<av_val.length();i++){
                lv_char = av_val.substring(i, (i+1));
                loop2:for(int j=0;j<la_symbol.length;j++){
                    if(lv_char.indexOf(la_symbol[j]) > -1){
                        lv_char = lv_char.replaceAll(la_symbol[j], la_replace[j]);
                        break loop2;
                    }
                }
                lv_return = lv_return + lv_char;
            }
            
            System.out.println("[FormatUtil][sanitizeURLString] lv_return :: " + lv_return);
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            System.out.println("[FormatUtil][sanitizeURLString][End]");
        }
        
        return lv_return;
    }
    
	public static String convertFloatToDisplay(Object obj,int point){
		
		String stFloat = obj==null?"":obj.toString().trim();
		
		if (stFloat!=null&&!stFloat.equals("")){
			String strFormat = "##,##0";
			if (point > 0) { strFormat = strFormat + "."; }
			for(int i=0;i<point;i++){
				strFormat = strFormat + "0";	
			}		
			DecimalFormat df	= new DecimalFormat(strFormat);			
			stFloat 			= df.format(parseDouble(stFloat));	
		}else{
			stFloat = "0.00";
		}

		return stFloat;
	}
	
	public static String convertNumberToDisplay(Object obj){
		
		String stFloat 		= obj==null?"":obj.toString().trim();
		int point 			= 0;
		String strFormat 	= "##,##0";
		
		if (stFloat!=null&&!stFloat.equals("")){
			String[] la_dec = stFloat.split("\\.");
			
			if(la_dec.length > 1){
				if(EnjoyUtils.parseInt(la_dec[1]) > 0){
					if(la_dec[1].length() > 1){
						if(la_dec[1].length()==2 && la_dec[1].substring(1, 2).equals("0")){
							point = 1;
						}else{
							point = 2;
						}
					}else{
						point = 1;
					}
				}
			}
			
			if (point > 0) { strFormat = strFormat + "."; }
			for(int i=0;i<point;i++){
				strFormat = strFormat + "0";	
			}		
			DecimalFormat df	= new DecimalFormat(strFormat);			
			stFloat 			= df.format(parseDouble(stFloat));	
		}else{
			stFloat = "0";
		}

		return stFloat;
	}
	
	public static String tinFormat(Object obj) throws Exception{
		String stFloat;
		String lv_newPin 	= "";
		
		try{
			stFloat 	= obj==null?"":obj.toString().trim();
			lv_newPin 	= stFloat;
			
			if(chkNull(stFloat) && stFloat.length()==13){
				lv_newPin = "";
				
				for(int i=0;i<stFloat.length();i++){
					
					if(i==1 || i==5 || i==10 || i==12){
						lv_newPin += "-" + stFloat.substring(i, (i+1));
					}else{
						lv_newPin += stFloat.substring(i, (i+1));
					}
				}
			}
			
		}catch(Exception e){
			throw e;
		}
		
		return lv_newPin;
	}

	/**
	 * @param stData
	 * @return String
	 * convert Thai format
	 */
	public static String convertDataThai(String stData){
		String lv_strData 	 = nullToStr(stData);
		StringBuffer strTemp = new StringBuffer(lv_strData);		
		int maxLength 		 = lv_strData.length();
		int code;

		for( int i = 0; i < maxLength; i++){ 
			code = (int) strTemp.charAt(i); 
			if ( ( 0xA1 <= code ) && ( code <= 0xFB ) ){ 
				strTemp.setCharAt( i, (char) ( code + 0xD60 ) ); 
			} 
		} 
		return strTemp.toString(); 
	}
	/*
	public static String getCustNext(String code) {
    	StringBuffer codeReturn = new StringBuffer("CUS000");
    	String lv_strData 	 = nullToStr(code); 	
		int maxLength 		 = lv_strData.length();
		System.out.println("maxLength:"+maxLength);
		if(maxLength>=7){
			String lastRef       = lv_strData.substring(3);
	    	int codeInt  = Integer.parseInt(lastRef);
	    	codeInt++;
	    	System.out.println("codeInt:"+codeInt);
	    	codeReturn = codeReturn.append(String.valueOf(codeInt)); 
	    	System.out.println("codeReturn:"+codeReturn);
		}
        return codeReturn.toString();
    }*/
	
    public static String getCustNext(String code) {
    	String returnValue      = "";
    	String lv_strData 	    = nullToStr(code); 
    	int codeInt  = Integer.parseInt(lv_strData);
    	codeInt++;
    	System.out.println("codeInt:"+codeInt);
    	returnValue             = String.valueOf(codeInt); 
        return returnValue;
    }
    
    public static String displayAmountThai(String amt)
    {
        String CurText,Thai_CurText = "";
        char ch;
        String st_ch;
        int idx,Digit;

        String[] GetNum   = { "หนึ่ง","สอง","สาม","สี่","ห้า","หก","เจ็ด","แปด","เก้า","ยี่","เอ็ด" };
        String[] GetDigit = { "","สิบ","ร้อย","พัน","หมื่น","แสน","ล้าน"};

        CurText = amt!=null||!"".equals(amt)?amt.replaceAll(",", ""):"";
        Digit = 0;
        for ( idx = CurText.length() ; idx >= 1 ;idx-- )
        {
           if ( Digit == 6 ){
              Digit = 0;
              Thai_CurText = "ล้าน" + Thai_CurText;
           }

           ch      = CurText.substring(idx-1,idx).toCharArray()[0];
           st_ch   = CurText.substring(idx-1,idx);
           switch (ch){
              case '.' :  Digit = 0;
                          Thai_CurText = "บาท" + Thai_CurText;
                          break;
              case ',' :  break;

              case '0' :  Digit = Digit + 1;
                          break;
              case '1' :  Digit = Digit + 1;
                      switch (Digit){
                          case 1 : if ( st_ch.equals(CurText.substring(0,idx))) {
                                       Thai_CurText = GetNum[0] + Thai_CurText;
                                   }else if (! CurText.substring(idx-1,idx).equals("0") ){
                                	   if(CurText.substring(idx-2,idx-1).equals("0")){
                                		   Thai_CurText = GetNum[0] + Thai_CurText;
                                	   }else{
                                		   Thai_CurText = GetNum[10] + Thai_CurText;
                                	   }
                                   }else {
                                       Thai_CurText = GetNum[0] + Thai_CurText;
                                   }
                                   break;
                          case 2 : Thai_CurText = GetDigit[Digit-1] + Thai_CurText;
                                   break;
                          case 3 : Thai_CurText = GetNum[0] + GetDigit[Digit-1] + Thai_CurText;
                                   break;
                          case 4 : Thai_CurText = GetNum[0] + GetDigit[Digit-1] + Thai_CurText;
                                   break;
                          case 5 : Thai_CurText = GetNum[0] + GetDigit[Digit-1] + Thai_CurText;
                                   break;
                          case 6 : Thai_CurText = GetNum[0] + GetDigit[Digit-1] + Thai_CurText;
                                   break;
                          }
                          break;
              case '2' :  Digit = Digit + 1;
                          if ( Digit == 2 ) {
                              Thai_CurText = GetNum[9] + GetDigit[Digit-1] + Thai_CurText;
                          }else{
                              Thai_CurText = GetNum[1] + GetDigit[Digit-1] + Thai_CurText;
                          }
                          break;
              default  :  Digit = Digit + 1;
                          Thai_CurText = GetNum[Integer.parseInt(st_ch)-1] + GetDigit[Digit-1] + Thai_CurText;
                          break;

           }
        }

        int pos_dot = CurText.indexOf(".");
        if (Integer.parseInt(CurText.substring(pos_dot+1,CurText.length())) == 0 ){
        	if(amt.equals("0.00")){
        		Thai_CurText = "ศูนย์" + Thai_CurText + "ถ้วน";
        	}else{
        		Thai_CurText = Thai_CurText + "ถ้วน";
        	}
        }else{
        	Thai_CurText = Thai_CurText + "สตางค์";
        }
        return Thai_CurText;
    }
    
	public static String displayDateThai(String dateThai) {
        Hashtable tblMonth = new Hashtable();
        String    day      = "";
        int       month    = 0;
        int       year     = 0;
        String	  dateDisplay = "";
        try {
        	if (!dateThai.equals(""))
			{	
	            tblMonth.put("1", "มกราคม");
	            tblMonth.put("2", "กุมภาพันธ์");
	            tblMonth.put("3", "มีนาคม");
	            tblMonth.put("4", "เมษายน");
	            tblMonth.put("5", "พฤษภาคม");
	            tblMonth.put("6", "มิถุนายน");
	            tblMonth.put("7", "กรกฏาคม");
	            tblMonth.put("8", "สิงหาคม");
	            tblMonth.put("9", "กันยายน");
	            tblMonth.put("10","ตุลาคม");
	            tblMonth.put("11","พฤศจิกายน");
	            tblMonth.put("12","ธันวาคม");
	
	            day    = dateThai.substring(6, 8);
				month  = Integer.parseInt(dateThai.substring(4, 6));
		        year   = Integer.parseInt(dateThai.substring(0, 4));
		        dateDisplay = day + " " + tblMonth.get(String.valueOf(month)).toString() +" พ.ศ. " + year;
			} else {
				dateDisplay = "";			
			}
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            tblMonth = null;
        }
        return dateDisplay;
	}
	
	public static String replaceComma(String av_val) {
		
		String lv_ret = null;
		
        try {
        	if (av_val!=null&&!av_val.equals("")){	
        		lv_ret = av_val.replaceAll(",", "");
			} else {
				lv_ret = "";			
			}
        } catch (Exception e) {
        	e.printStackTrace();
        } 
        return lv_ret;
	}
	
	public static String chkBoxtoDb(Object av_val) {
		
		String lv_ret = null;
		
        try {
        	
        	lv_ret = av_val==null?"N":av_val.toString().trim();
        	
        	if (lv_ret.equals("")){	
        		lv_ret = "N";	
			}
        } catch (Exception e) {
        	e.printStackTrace();
        } 
        return lv_ret;
	}
	
	public static int parseInt(Object ao_obj) {
		
		int 	lv_ret = 0;
		String 	lv_val = "";
		
        try {
        	lv_val = ao_obj==null?"":ao_obj.toString().trim();
        	
        	
        	if (lv_val!=null&&!lv_val.equals("")) lv_ret = Integer.parseInt(lv_val.replaceAll(",", ""));
        } catch (Exception e) {
        	e.printStackTrace();
        } 
        return lv_ret;
	}
	
	public static Double parseDouble(Object ao_obj) {
		
		Double 	lv_ret = 0.00;
		String 	lv_val = "";
		
        try {
        	
        	lv_val = ao_obj==null?"":ao_obj.toString().trim();
        	
        	if (lv_val!=null&&!lv_val.equals("")){	
        		lv_ret = Double.parseDouble(lv_val.replaceAll(",", ""));
			} 
        } catch (Exception e) {
        	e.printStackTrace();
        } 
        return lv_ret;
	}
	
	public static BigDecimal parseBigDecimal(Object ao_obj) {
		
		BigDecimal 	lv_ret = new BigDecimal("0");
		String 		lv_val = "";
		
        try {
        	lv_val = ao_obj==null?"":ao_obj.toString().trim();
        	
        	System.out.println("lv_val :: " + lv_val);
        	
        	if (lv_val!=null&&!lv_val.equals("")){	
        		lv_ret = new BigDecimal(lv_val.replaceAll(",", ""));
			} 
        } catch (Exception e) {
        	e.printStackTrace();
        } 
        return lv_ret;
	}
	
	public static String genPassword(int charLength){
		Random r 			= new Random();
		String newPass		= "";
		String alphabet 	= "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		// Prints 50 random characters from alphabet
		for (int i = 0; i < charLength; i++){
			newPass = newPass.concat(String.valueOf(alphabet.charAt(r.nextInt(alphabet.length()))));
			
		}
		
		return newPass;
		
	}
	
	
	public static List<String> getListFromArr(String dataDelete){
		 List<String>  returnList = null;
		 String        lv_value   = null;
		   try {
	        	if(dataDelete != null && dataDelete != ""){	
	        		returnList = new ArrayList<String>();
	        		lv_value = dataDelete.replaceAll("\\s", ""); 
	        		String[] parts = lv_value.split(",");
	        		for(String s : parts){
	        		    returnList.add(s);
	        		}
				} 
	        } catch (Exception e) {
	        	e.printStackTrace();
	        } 
	        return returnList;
		
	}
	
	public static boolean checkRowName(String rowName,int rows){
		System.out.println("rowName:"+rowName);	
		System.out.println("rows:"+rows);	
		
		boolean flag = false;
		 String        lv_value   = null;
		   try {
	        	if(rowName != null && rowName != ""){	 
	        		lv_value = rowName.replaceAll("\\s", ""); 
	        		String[] parts = lv_value.split(",");
	        		System.out.println("parts:"+parts.length);	
    				
	        		if(parts.length != rows){
	        		   flag = true;
	        		}
				}else{
					 flag = true;
				}
	        } catch (Exception e) {
	        	e.printStackTrace();
	        } 
		   
	        return flag;
		
	}
	
	//ใช้สำหรับบแปลงเวลารูปแบบ 24 hr Ex av_time = "1725"
	public static String formatTime(String av_time) throws Exception{
		
		String t_formay		= "00:00";
		
		try{
			
			if(av_time!=null && !av_time.equals("") && av_time.length()==4){
				t_formay = av_time.substring(0, 2) + ":" + av_time.substring(2, 4);
			}
			
		}catch(Exception e){
			throw e;
		}
		
		return t_formay;
		
	}

	public static String dateToThaiDB(String dDate){
        String 	stDate 	= "";
        String  day     = "";
        String  month   = "";
        String  year    = "";
        String  dateDB  = "";
        try{  
        	if(chkNull(dDate)){
	        	dDate  = dDate.replaceAll("\\s", ""); 
	        	stDate = dDate.replaceAll("/", "");  
	        	day    = stDate.substring(0, 2);
				month  = stDate.substring(2, 4);
			    year   = stDate.substring(4, 8);
			    dateDB = year+month+day;
	        }
		    System.out.println("EnjoyUtils dateToThaiDB : "+ dateDB);
        } catch (Exception e) {
            e.printStackTrace();
        }
           
        return dateDB;
    }
    
	/*dd/MM/yyyy*/
    public static String dateToThaiDisplay(Object dDate){
        String day     	= "";
        String month   	= "";
        String year    	= "";
        String display  = "";
        String date		= "";
        
        try{ 
        	
        	date = dDate==null?"":dDate.toString().trim();
        	
        	if(!date.equals("")){
        		day    	= date.substring(6, 8);
           		month  	= date.substring(4, 6);
           		year   	= date.substring(0, 4);
           		display = day+"/"+month+"/"+year;
        	}
        	
		    System.out.println("EnjoyUtils dateToThaiDisplay : "+ display);
        } catch (Exception e) {
            e.printStackTrace();
        }
           
        return display;
    }
    
    public static String timeToDB(String dTime){
        String 	stDate 	= ""; 
        try{ 
        	dTime  = dTime.replaceAll("\\s", ""); 
        	stDate = dTime.replaceAll(":", "");  
        } catch (Exception e) {
            e.printStackTrace();
        }
           
        return stDate;
    }
    
    public static String timeToDisplay(String dTime){
        String 	display         = "";   
        String lv_time			= "";
        String lv_timeTmp		= "";
        int    lv_txtLength	    = 0;

    	try{ 
    		lv_time		=  dTime ;

    		if(lv_time != "") {
    			lv_txtLength = lv_time.length();
    			if(lv_txtLength==3 && lv_time.indexOf(":") < 0){
    				lv_timeTmp		= lv_time.substring(0, 1) + ":" + lv_time.substring(1, 3);
    				display	        = lv_timeTmp;
    			}else if(lv_txtLength==4 && lv_time.indexOf(":") < 0){
    				lv_timeTmp		= lv_time.substring(0, 2) + ":" + lv_time.substring(2, 4);
    				display	        = lv_timeTmp;
    			}else{
    				display         = lv_time;
    			}
 
    		} 

    	 } catch (Exception e) {
             e.printStackTrace();
         }
    	
        return display;
    }
    
    public static String formatPin(Object av_pin) throws Exception{
    	String lv_pin 		= "";
    	String lv_newPin 	= "";
    	
    	try{
    		if(av_pin!=null){
    			lv_pin = av_pin.toString().trim().replaceAll("-","");
    			if(!lv_pin.equals("") && lv_pin.length()==13){
    				for(int i=0;i<lv_pin.length();i++){
    					
    					if(i==1 || i==5 || i==10 || i==12){
    						lv_newPin += "-" + lv_pin.charAt(i);
    					}else{
    						lv_newPin += lv_pin.charAt(i);
    					}
    				}
    			}
    			
    		}
    		
    		
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		throw e;
    	}
    	return lv_newPin;
    }

    public static String getLastDateOfMonth(String sdate){
    	Calendar 			c 				= null;
    	SimpleDateFormat 	formatter 		= new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    	int 				lastDayOfMonth 	= 28;//Calendar.getInstance(Locale.US).getActualMaximum(Calendar.DAY_OF_MONTH);
    	String[]			sdateArray		= null;
    	String				sdateNew		= "";
    	Date 				date 			= null;
    	String				dateRet			= "";
    	
    	try{
    		sdateArray 	= sdate.split("/");
    		sdateNew	= sdateArray[0] + "/" + sdateArray[1] + "/" + (Integer.parseInt(sdateArray[2])-543);
    		date 		= formatter.parse(sdateNew);
    		
    		c = Calendar.getInstance(Locale.US); 
    		c.setTime(date);
    		
    		lastDayOfMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
    		
    		System.out.println(lastDayOfMonth);
    		
    		dateRet = lastDayOfMonth + "/" + sdateArray[1] + "/" + sdateArray[2];
    		
    	}catch (Exception e) {
            e.printStackTrace();
        }
    	
    	return dateRet;
    }
    
    public static Date dateTimeDisplayToDb(String dates) throws Exception{
        
        SimpleDateFormat    originalFormatter   = null; 
        SimpleDateFormat    newFormatter    	= null;
        ParsePosition       pos                 = new ParsePosition(0);
        Date                originalDate        = null;
        String              newFormatDate       = null;
        Date                dateForDisplay      = null;
        
        try{
            originalFormatter   = new SimpleDateFormat("dd/MM/yyyy HH:mm" , new Locale("th", "TH"));
            newFormatter    	= new SimpleDateFormat("yyyy-MM-dd HH:mm" , new Locale("en","EN"));
            
            originalDate    = originalFormatter.parse(dates, pos);
            newFormatDate   = newFormatter.format(originalDate);
            
           // logger.info("[dateTimeDisplayToDb] newFormatDate :: " + newFormatDate);
            
            dateForDisplay  = newFormatter.parse(newFormatDate);
            
        } catch (Exception e) {
            throw e;
        }
           
        return dateForDisplay;
    }
    
    public static Date getCurrentDateNoTime() {
        Calendar cal = Calendar.getInstance();
        cal.clear(Calendar.HOUR_OF_DAY);
        cal.clear(Calendar.AM_PM);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        return cal.getTime();
    }
    
    public static Timestamp toTimeStamp(java.util.Date date) {
        return date == null ? null : new Timestamp(date.getTime());
    }
    
    public static boolean chkNull(String obj) throws Exception{
        boolean flag = false;
        
        try{
            if(obj!=null&&!"".equals(obj))flag = true;
        }catch(Exception e){
        	throw e;
        }
        
        return flag;
    }
	
}
