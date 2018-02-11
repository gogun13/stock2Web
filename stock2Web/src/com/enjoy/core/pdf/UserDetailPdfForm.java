package com.enjoy.core.pdf;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.simple.JSONObject;

import com.enjoy.core.pdf.utils.EnjoyItext;
import com.enjoy.core.pdf.utils.PdfFormService;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class UserDetailPdfForm extends EnjoyItext implements PdfFormService {
	
	private PdfWriter 						writer;
	private JSONObject 						formDataObj;
	
	private void setWriter(PdfWriter writer) {
		this.writer = writer;
	}

	public void setJSONObject(PdfWriter writer, JSONObject jsonObject) {
		this.formDataObj  	= jsonObject;
		setWriter(writer);
	}
	
	public Document createForm(Document document) {
		System.out.println("[UserDetailPdfForm][createForm][Begin]");
		
		try{
			document.add(this.genDetail());
		}
		catch(DocumentException de){
			de.printStackTrace();
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			System.out.println("[UserDetailPdfForm][createForm][End]");
		}
		
		return document;
	}
	
	private PdfPTable genDetail() throws DocumentException, MalformedURLException, IOException {
		
		PdfPTable 	table 			= new PdfPTable(1);
		JSONObject 	jsonObjectMain  = this.formDataObj;
		
		table.addCell(setCellWB("เรียนคุณ " + getText(jsonObjectMain, "fullName"), getFont8(), 1, Element.ALIGN_LEFT, 0));
		table.addCell(setCellWB("User : " + getText(jsonObjectMain, "userEmail"), getFont8(), 1, Element.ALIGN_LEFT, 0));
		table.addCell(setCellWB("Password : " + getText(jsonObjectMain, "pwd"), getFont8(), 1, Element.ALIGN_LEFT, 0));
		table.addCell(setCellWB("", getFont8(), 1, Element.ALIGN_LEFT, 0));
		table.addCell(setCellWB("***กรุณาเก็บ user และ password เอาไว้สำหรับใช้ในการเข้าระบบในครั้งต่อไป", getFont8(), 1, Element.ALIGN_LEFT, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
	public PdfPTable brLine() throws DocumentException, MalformedURLException, IOException {
		
		PdfPTable 	table 			= new PdfPTable(1);
		
		table.addCell(setCellWB("", getFont12Bold(), 1, Element.ALIGN_LEFT, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
}
