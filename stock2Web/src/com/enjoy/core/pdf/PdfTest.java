package com.enjoy.core.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.enjoy.core.pdf.utils.PdfFormService;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

public class PdfTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static void writePDF(String formName, String filePath, String pdfPath) throws Exception{
	    String 				formClass					= null;
	    JSONParser 			parser 						= null;
		Document 			document					= null;
		File 				f 							= null;
		FileOutputStream 	fos 						= null;
		PdfWriter 			writer 						= null;
		PdfFormService 		pdfForm 					= null;
		Object 				obj 						= null;
		JSONObject 			jsonObject 					= null;
		
		try{
			System.out.println("formName :: " + formName);
			
			formClass					= "com.enjoy.core.pdf."+formName;
			document 					= new Document(PageSize.A4);
			parser 						= new JSONParser();
			f 							= new File(pdfPath);
			fos            				= new FileOutputStream(f.getAbsolutePath());			
			writer 						= PdfWriter.getInstance( document,fos  );
	
			document.addTitle("Ticket Form");
			System.out.println(formClass);
	
			Class c 					= 	Class.forName(formClass);
			pdfForm 	        		= 	(PdfFormService) c.newInstance();
		
			document.open();
			
			obj 						= parser.parse(new FileReader(filePath));
			jsonObject 					= (JSONObject) obj;
			
			pdfForm.setJSONObject(writer, jsonObject);
			pdfForm.createForm(document);
	
			document.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
	}

}
