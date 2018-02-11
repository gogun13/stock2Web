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

public class FullSlipCreditNoVatHeader extends PdfPageEventHelper {
	
	private JSONObject 	formDataObj;
	private EnjoyItext	enjoyItext;
	
	public FullSlipCreditNoVatHeader(JSONObject formDataObj) {
		enjoyItext 	= new EnjoyItext();
		setFormDataObj(formDataObj);
	}
	
	public void onStartPage(PdfWriter writer, Document document) {
		try{
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
	
	private PdfPTable genHeader1() throws DocumentException, MalformedURLException, IOException {
		
		float[] 	widths	 			= {22f,28f ,25f,25f};
		PdfPTable 	table 				= new PdfPTable(widths);
		JSONObject 	jsonObjectMain  	= this.formDataObj;
		JSONObject  invoiceCreditMaster	= (JSONObject) jsonObjectMain.get("invoiceCreditMaster");
		
		table.addCell(enjoyItext.setCellWB("ใบกำกับภาษี/ใบเสร็จรับเงิน", enjoyItext.getFont10Bold(), 4, Element.ALIGN_CENTER, 0));
		
		table.addCell(enjoyItext.setCellWB("เลขที่ใบเสร็จ", enjoyItext.getFont8Bold(), 3, Element.ALIGN_RIGHT, 0));
		table.addCell(enjoyItext.setCellWB(enjoyItext.getText(invoiceCreditMaster, "invoiceCode"), enjoyItext.getFont8(), 1, Element.ALIGN_LEFT, 0));
		
		table.addCell(enjoyItext.setCellWB("วันที่ใบเสร็จ", enjoyItext.getFont8Bold(), 3, Element.ALIGN_RIGHT, 0));
		table.addCell(enjoyItext.setCellWB(enjoyItext.getText(invoiceCreditMaster, "invoiceDate"), enjoyItext.getFont8(), 1, Element.ALIGN_LEFT, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
	private PdfPTable genCustomerDetail(JSONObject customerDetails) throws DocumentException, MalformedURLException, IOException {
		
		float[] 	widths	 			= {12f,88f};
		PdfPTable 	table 				= new PdfPTable(widths);
		
		table.addCell(enjoyItext.setCellWB("ชื่อลูกค้า    : ", enjoyItext.getFont8Bold(), 1, Element.ALIGN_LEFT, 0));
		table.addCell(enjoyItext.setCellWB(enjoyItext.getText(customerDetails, "cusName") + " " + enjoyItext.getText(customerDetails, "cusSurname"), enjoyItext.getFont8(), 1, Element.ALIGN_LEFT, 0));
		table.addCell(enjoyItext.setCellWB("ที่อยู่ลูกค้า  : ", enjoyItext.getFont8Bold(), 1, Element.ALIGN_LEFT, 0));
		table.addCell(enjoyItext.setCellWB(enjoyItext.getText(customerDetails, "address"), enjoyItext.getFont8(), 1, Element.ALIGN_LEFT, 0));
		
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
