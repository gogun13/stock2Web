package com.enjoy.stock.pdf.header;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.simple.JSONObject;

import com.enjoy.core.pdf.utils.EnjoyItext;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class HistoryPurchasedByDealerHeader extends PdfPageEventHelper {
	
	private JSONObject 	formDataObj;
	private EnjoyItext	enjoyItext;
	
	public HistoryPurchasedByDealerHeader(JSONObject formDataObj) {
		enjoyItext 	= new EnjoyItext();
		setFormDataObj(formDataObj);
	}
	
	public void onStartPage(PdfWriter writer, Document document) {
		try{
			document.add(enjoyItext.genHeader(formDataObj, "รายงานประวัติการซื้อตามผู้จำหน่าย"));
			document.add(brLine());
			document.add(genHeader1());
			document.add(brLine());
			
		} catch (Exception e) {
	         throw new ExceptionConverter(e);
	    }
	}
	
//	private PdfPTable genHeader() throws DocumentException, MalformedURLException, IOException {
//		
//		PdfPTable 	table 			= new PdfPTable(1);
//		JSONObject 	jsonObjectMain  = this.formDataObj;
//		JSONObject  companyDetails	= (JSONObject) jsonObjectMain.get("companyDetails");
//		String		address			= "";
//		
//		address = " โทร." + enjoyItext.getText(companyDetails, "tel") 
//				+ " Fax." + enjoyItext.getText(companyDetails, "fax") 
//				+ " Email." + enjoyItext.getText(companyDetails, "email");
//		
//		table.addCell(enjoyItext.setCellWB(enjoyItext.getText(companyDetails, "companyName"), enjoyItext.getFont11Bold(), 1, Element.ALIGN_CENTER, 0));
//		table.addCell(enjoyItext.setCellWB(enjoyItext.getText(companyDetails, "address"), enjoyItext.getFont8(), 1, Element.ALIGN_CENTER, 0));
//		table.addCell(enjoyItext.setCellWB(address, enjoyItext.getFont8(), 1, Element.ALIGN_CENTER, 0));
//		table.addCell(enjoyItext.setCellWB("", enjoyItext.getFont8(), 1, Element.ALIGN_CENTER, 0));
//		table.addCell(enjoyItext.setCellWB("รายงานประวัติการซื้อตามผู้จำหน่าย", enjoyItext.getFont10Bold(), 1, Element.ALIGN_CENTER, 0));
//		
//		table.setWidthPercentage(100);
//	
//		return table;
//	}
	
	private PdfPTable genHeader1() throws DocumentException, MalformedURLException, IOException {
		
		PdfPTable 	table 							= new PdfPTable(1);
		JSONObject 	jsonObjectMain  				= this.formDataObj;
		String		rangeReciveDate					= "";
		
		rangeReciveDate = enjoyItext.getText(jsonObjectMain, "reciveDateFrom") + " - " + enjoyItext.getText(jsonObjectMain, "reciveDateTo");
		
		table.addCell(enjoyItext.setCellWB(rangeReciveDate, enjoyItext.getFont9Bold(), 1, Element.ALIGN_RIGHT, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
	public PdfPTable brLine() throws DocumentException, MalformedURLException, IOException {
		
		PdfPTable 	table 			= new PdfPTable(1);
		
		table.addCell(enjoyItext.setCellWB("", enjoyItext.getFont12Bold(), 1, Element.ALIGN_LEFT, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}

	public JSONObject getFormDataObj() {
		return formDataObj;
	}

	public void setFormDataObj(JSONObject formDataObj) {
		this.formDataObj = formDataObj;
	}
	
	

}
