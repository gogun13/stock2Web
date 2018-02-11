package com.enjoy.stock.pdf;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.enjoy.core.pdf.utils.EnjoyItext;
import com.enjoy.core.pdf.utils.PdfFormService;
import com.enjoy.core.utils.EnjoyUtils;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class FullSlipCashWithPatternPdfForm extends EnjoyItext implements PdfFormService {
	
	private PdfWriter 				writer;
	private JSONObject 				formDataObj;
	
	private void setWriter(PdfWriter writer) {
		this.writer = writer;
	}

	public void setJSONObject(PdfWriter writer, JSONObject jsonObject) {
		this.formDataObj  	= jsonObject;
		setWriter(writer);
		
	}
	
	public Document createForm(Document document) {
		System.out.println("[FullSlipCashWithPatternPdfForm][createForm][Begin]");
		
		try{
			
			JSONArray 	invoiceCashDetailList 	= (JSONArray) formDataObj.get("invoiceCashDetailList");
			
			for(int i=0;i<invoiceCashDetailList.size();i++){
				
				if(i > 0){
					document.newPage();
				}
				
				//พอดีกับแผ่นแรก
				document.add(brLine(86));
				document.add(genHeader1());
				document.add(brLine(57));
				
				JSONObject  customerDetails		= (JSONObject) this.formDataObj.get("customerDetails");
				document.add(genCustomerDetail(customerDetails));
				
				document.add(brLine(50));
				document.add(genDetail((JSONArray)invoiceCashDetailList.get(i),(i*12)));
				document.add(brLine(45));
			}
			
			document.add(genTotalCost());
			
			
		}
		catch(DocumentException de){
			de.printStackTrace();
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		finally{
			System.out.println("[FullSlipCashWithPatternPdfForm][createForm][End]");
		}
		
		return document;
	}
	
	private PdfPTable genHeader1() throws DocumentException, MalformedURLException, IOException {
		
		float[] 	widths	 			= {97f ,3f};
		PdfPTable 	table 				= new PdfPTable(widths);
		JSONObject 	jsonObjectMain  	= this.formDataObj;
		JSONObject  invoiceCashMaster	= (JSONObject) jsonObjectMain.get("invoiceCashMaster");
		
		table.addCell(setCellWB(getText(invoiceCashMaster, "invoiceCode"), getFont10(), 1, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB("", getFont9(), 1, Element.ALIGN_RIGHT, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
	private PdfPTable genCustomerDetail(JSONObject customerDetails) throws DocumentException, MalformedURLException, IOException {
		
		float[] 	widths	 			= {75f ,25f};
		PdfPTable 	table 				= new PdfPTable(widths);
		PdfPTable 	tableSub1 			= new PdfPTable(1);
		PdfPTable 	tableSub2 			= new PdfPTable(1);
		JSONObject  invoiceCashMaster	= (JSONObject) formDataObj.get("invoiceCashMaster");
		String		name 				= " ";
		String		address 			= " ";
		String 		idNumber 			= " ";
		
		if(customerDetails!=null){
			name 		= getText(customerDetails, "cusName") + " " + getText(customerDetails, "cusSurname");
			address 	= getText(customerDetails, "address");
			idNumber 	= getText(customerDetails, "idNumber");
		}
		
		tableSub1.addCell(setCellWB("                        " + name, getFont10(), 1, Element.ALIGN_LEFT, 0));
		
		tableSub1.addCell(setCellWB("", getFont9(), 1, Element.ALIGN_LEFT, 0));
		tableSub1.addCell(setCellWB("", getFont9(), 1, Element.ALIGN_LEFT, 0));
		tableSub1.addCell(setCellWB("", getFont9(), 1, Element.ALIGN_LEFT, 0));
		tableSub1.addCell(setCellWB("", getFont9(), 1, Element.ALIGN_LEFT, 0));
		tableSub1.addCell(setCellWB("", getFont9(), 1, Element.ALIGN_LEFT, 0));
		tableSub1.addCell(setCellWB("", getFont9(), 1, Element.ALIGN_LEFT, 0));
		
		tableSub1.addCell(setCellWBWithHeight("             " + address, getFont10(), 1, Element.ALIGN_LEFT, 0,25));
		
		tableSub1.addCell(setCellWB("", getFont9(), 1, Element.ALIGN_LEFT, 0));
		tableSub1.addCell(setCellWB("", getFont9(), 1, Element.ALIGN_LEFT, 0));
//		tableSub1.addCell(setCellWB("", getFont9(), 1, Element.ALIGN_LEFT, 0));
//		tableSub1.addCell(setCellWB("", getFont9(), 1, Element.ALIGN_LEFT, 0));
//		tableSub1.addCell(setCellWB("", getFont9(), 1, Element.ALIGN_LEFT, 0));
		
		tableSub1.addCell(setCellWB("                                   " + idNumber, getFont10(), 1, Element.ALIGN_LEFT, 0));
		
		table.addCell(setCellWB(tableSub1, 1, Element.ALIGN_LEFT, 0, false, false));
//		table.addCell(setCellWB("   ".concat(getText(invoiceCashMaster, "invoiceDate")), getFont10(), 1, Element.ALIGN_LEFT, 0));
		
		tableSub2.addCell(setCellWB("", getFont9(), 1, Element.ALIGN_LEFT, 0));
		tableSub2.addCell(setCellWB("", getFont9(), 1, Element.ALIGN_LEFT, 0));
		tableSub2.addCell(setCellWB("   ".concat(getText(invoiceCashMaster, "invoiceDate")), getFont10(), 1, Element.ALIGN_LEFT, 0));
		table.addCell(setCellWB(tableSub2, 1, Element.ALIGN_LEFT, 0, false, false));
		
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
	private PdfPTable genDetail(JSONArray list,int num) throws DocumentException, MalformedURLException, IOException {
		
//		float[] 	widths	 				= {6f ,54f ,9f ,11f ,16f,2f};
		float[] 	widths	 				= {5f ,56f ,10f ,11f ,16f};
		PdfPTable 	table 					= new PdfPTable(widths);
		JSONObject 	invoiceCashDetail  		= null;
		int			limitList				= 12;
		
		for(int i=0;i<list.size();i++){
			invoiceCashDetail = (JSONObject) list.get(i);
			
//			table.addCell(setCellWBWithHeight("", getFont10(), 1, Element.ALIGN_CENTER, 0,25));
			table.addCell(setCellWBWithHeight(String.valueOf((++num)), getFont10(), 1, Element.ALIGN_CENTER, 0,25));
			table.addCell(setCellWBWithHeight(" " + getText(invoiceCashDetail, "productName"), getFont10(), 1, Element.ALIGN_LEFT, 0,25));
			table.addCell(setCellWBWithHeight(getText(invoiceCashDetail, "quantity"), getFont10(), 1, Element.ALIGN_CENTER, 0,25));
			table.addCell(setCellWBWithHeight(getText(invoiceCashDetail, "pricePerUnit"), getFont10(), 1, Element.ALIGN_RIGHT, 0,25));
			table.addCell(setCellWBWithHeight(getText(invoiceCashDetail, "price"), getFont10(), 1, Element.ALIGN_RIGHT, 0,25));
//			table.addCell(setCellWBWithHeight("", getFont10(), 1, Element.ALIGN_CENTER, 0,25));
		}
		
		for(int i=0;i<(limitList-list.size());i++){
			
			table.addCell(setCellWBWithHeight(" ", getFont10(), 7, Element.ALIGN_CENTER, 0,25));
		}
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
	private PdfPTable genTotalCost() throws DocumentException, MalformedURLException, IOException {
		
//		float[] 	widths	 			= {3f, 64f ,14f ,15f,2f};
		float[] 	widths	 			= {3f, 66f ,14f ,15f};
		PdfPTable 	table 				= new PdfPTable(widths);
		JSONObject  invoiceCashMaster	= (JSONObject) formDataObj.get("invoiceCashMaster");
		
//		table.addCell(setCellWB(" ", getFont8(), 5, Element.ALIGN_CENTER, 0));
		
		table.addCell(setCellWB("", getFont8(), 1, Element.ALIGN_CENTER, 0));
		table.addCell(setCellWB(EnjoyUtils.displayAmountThai(getText(invoiceCashMaster, "invoiceTotal")), getFont10(), 1, Element.ALIGN_CENTER, 0));
		table.addCell(setCellWB("", getFont10(), 1, Element.ALIGN_CENTER, 0));
		table.addCell(setCellWB(getText(invoiceCashMaster, "invoicePrice"), getFont10(), 1, Element.ALIGN_RIGHT, 0));
//		table.addCell(setCellWB("", getFont10(), 1, Element.ALIGN_CENTER, 0));
		
		table.addCell(setCellWB(" ", getFont8(), 4, Element.ALIGN_CENTER, 0));
		
		table.addCell(setCellWB("", getFont10(), 3, Element.ALIGN_CENTER, 0));
		table.addCell(setCellWB(getText(invoiceCashMaster, "invoiceVat"), getFont10(), 1, Element.ALIGN_RIGHT, 0));
//		table.addCell(setCellWB("", getFont10(), 1, Element.ALIGN_CENTER, 0));
		
		table.addCell(setCellWB(" ", getFont6(), 4, Element.ALIGN_CENTER, 0));
		
		table.addCell(setCellWB("", getFont8(), 3, Element.ALIGN_CENTER, 0));
		table.addCell(setCellWB(getText(invoiceCashMaster, "invoiceTotal"), getFont10(), 1, Element.ALIGN_RIGHT, 0));
//		table.addCell(setCellWB("", getFont8(), 1, Element.ALIGN_CENTER, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
}
