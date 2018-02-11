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

public class FullSlipCashHeader extends PdfPageEventHelper {
	
	private JSONObject 	formDataObj;
	private EnjoyItext	enjoyItext;
	
	public FullSlipCashHeader(JSONObject formDataObj) {
		enjoyItext 	= new EnjoyItext();
		setFormDataObj(formDataObj);
	}
	
	public void onStartPage(PdfWriter writer, Document document) {
		try{
			document.add(this.genHeader());
			document.add(this.brLine());
			document.add(this.genHeader1());
			
			JSONObject  customerDetails		= (JSONObject) this.formDataObj.get("customerDetails");
			if(null!=customerDetails){
				document.add(this.genCustomerDetail(customerDetails));
			}
			document.add(this.brLine());
		} catch (Exception e) {
	         throw new ExceptionConverter(e);
	    }
	}
	
	private PdfPTable genHeader() throws DocumentException, MalformedURLException, IOException {
		
		float[] 	widths	 		= {1};
		PdfPTable 	table 			= new PdfPTable(widths);
		JSONObject 	jsonObjectMain  = this.formDataObj;
		JSONObject  companyDetails	= (JSONObject) jsonObjectMain.get("companyDetails");
		String		address			= "";
		
		address = " โทร." + enjoyItext.getText(companyDetails, "tel") 
				+ " Fax." + enjoyItext.getText(companyDetails, "fax") 
				+ " Email." + enjoyItext.getText(companyDetails, "email");
		
		table.addCell(enjoyItext.setCellWB(enjoyItext.getText(companyDetails, "companyName"), enjoyItext.getFont12Bold(), 1, Element.ALIGN_CENTER, 0));
		table.addCell(enjoyItext.setCellWB(enjoyItext.getText(companyDetails, "address"), enjoyItext.getFont9(), 1, Element.ALIGN_CENTER, 0));
		table.addCell(enjoyItext.setCellWB(address, enjoyItext.getFont9(), 1, Element.ALIGN_CENTER, 0));
		table.addCell(enjoyItext.setCellWB("เลขประจำตัวผู้เสียภาษี:" + enjoyItext.getText(companyDetails, "tin"), enjoyItext.getFont9(), 1, Element.ALIGN_CENTER, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
	private PdfPTable genHeader1() throws DocumentException, MalformedURLException, IOException {
		
		float[] 	widths	 			= {75f ,25f};
		PdfPTable 	table 				= new PdfPTable(widths);
		JSONObject 	jsonObjectMain  	= this.formDataObj;
		JSONObject  invoiceCashMaster	= (JSONObject) jsonObjectMain.get("invoiceCashMaster");
		
		table.addCell(enjoyItext.setCellWB("เลขที่ใบเสร็จ", enjoyItext.getFont9Bold(), 1, Element.ALIGN_RIGHT, 0));
		table.addCell(enjoyItext.setCellWB(enjoyItext.getText(invoiceCashMaster, "invoiceCode"), enjoyItext.getFont9(), 1, Element.ALIGN_LEFT, 0));
		
		table.addCell(enjoyItext.setCellWB("วันที่ใบเสร็จ", enjoyItext.getFont9Bold(), 1, Element.ALIGN_RIGHT, 0));
		table.addCell(enjoyItext.setCellWB(enjoyItext.getText(invoiceCashMaster, "invoiceDate"), enjoyItext.getFont9(), 1, Element.ALIGN_LEFT, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
	private PdfPTable genCustomerDetail(JSONObject customerDetails) throws DocumentException, MalformedURLException, IOException {
		
		PdfPTable 	table 				= new PdfPTable(1);
		
		table.addCell(enjoyItext.setCellWB("ลูกค้า : " + enjoyItext.getText(customerDetails, "cusName") + " " + enjoyItext.getText(customerDetails, "cusSurname"), enjoyItext.getFont9(), 1, Element.ALIGN_LEFT, 0));
		table.addCell(enjoyItext.setCellWB("ที่อยู่  : " + enjoyItext.getText(customerDetails, "address"), enjoyItext.getFont9(), 1, Element.ALIGN_LEFT, 0));
		table.addCell(enjoyItext.setCellWB("เลขประจำตัวผู้เสียภาษี : " + enjoyItext.getText(customerDetails, "idNumber"), enjoyItext.getFont9(), 1, Element.ALIGN_LEFT, 0));
		
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
