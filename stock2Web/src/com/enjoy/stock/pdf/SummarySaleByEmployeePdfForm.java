package com.enjoy.stock.pdf;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.enjoy.core.pdf.utils.EnjoyItext;
import com.enjoy.core.pdf.utils.PdfFormService;
import com.enjoy.stock.pdf.header.SummarySaleByEmployeeHeader;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class SummarySaleByEmployeePdfForm extends EnjoyItext implements PdfFormService {
	
	private PdfWriter 					writer;
	private JSONObject 					formDataObj;
	private SummarySaleByEmployeeHeader header;
	
	private void setWriter(PdfWriter writer) {
		this.writer = writer;
	}

	public void setJSONObject(PdfWriter writer, JSONObject jsonObject) {
		this.formDataObj  	= jsonObject;
		this.header			= new SummarySaleByEmployeeHeader(jsonObject);
		setWriter(writer);
		writer.setPageEvent(this.header);
		
	}
	
	public Document createForm(Document document) {
		System.out.println("[SummarySaleByEmployeePdfForm][createForm][Begin]");
		
		try{
			document.add(genHeader(formDataObj, "รายงานยอดขายของพนักงาน"));
			document.add(this.brLine());
			document.add(this.genHeader1());
			document.add(this.brLine());
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
			System.out.println("[SummarySaleByEmployeePdfForm][createForm][End]");
		}
		
		return document;
	}
	
//	private PdfPTable genHeader() throws DocumentException, MalformedURLException, IOException {
//		
//		PdfPTable 	table 			= new PdfPTable(1);
//		JSONObject 	jsonObjectMain  = this.formDataObj;
//		JSONObject  companyDetails	= (JSONObject) jsonObjectMain.get("companyDetails");
//		String		address			= "";
//		
//		address = " โทร." + getText(companyDetails, "tel") 
//				+ " Fax." + getText(companyDetails, "fax") 
//				+ " Email." + getText(companyDetails, "email");
//		
//		table.addCell(setCellWB(getText(companyDetails, "companyName"), getFont11Bold(), 1, Element.ALIGN_CENTER, 0));
//		table.addCell(setCellWB(getText(companyDetails, "address"), getFont8(), 1, Element.ALIGN_CENTER, 0));
//		table.addCell(setCellWB(address, getFont8(), 1, Element.ALIGN_CENTER, 0));
//		table.addCell(setCellWB("", getFont8(), 1, Element.ALIGN_CENTER, 0));
//		table.addCell(setCellWB("รายงานยอดขายของพนักงาน", getFont10Bold(), 1, Element.ALIGN_CENTER, 0));
//		
//		table.setWidthPercentage(100);
//	
//		return table;
//	}
	
	private PdfPTable genHeader1() throws DocumentException, MalformedURLException, IOException {
		
		float[] 	widths	 			= {50f,50f};
		PdfPTable 	table 				= new PdfPTable(widths);
		JSONObject 	jsonObjectMain  	= this.formDataObj;
		String		rangeReciveDate		= "";
		
		rangeReciveDate = getText(jsonObjectMain, "invoiceDateFrom") + " - " + getText(jsonObjectMain, "invoiceDateTo");
		
		table.addCell(setCellWB("ชื่อ :" + getText(jsonObjectMain, "saleName"), getFont9Bold(), 1, Element.ALIGN_LEFT, 0));
		table.addCell(setCellWB(rangeReciveDate, getFont9Bold(), 1, Element.ALIGN_RIGHT, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
	private PdfPTable genDetail() throws DocumentException, MalformedURLException, IOException,Exception {
		
		float[] 	widths	 			= {7f ,13f ,13f ,37f ,15f ,15f};
		PdfPTable 	table 				= new PdfPTable(widths);
		JSONObject 	jsonObjectMain  	= this.formDataObj;
		JSONArray 	resultList 			= (JSONArray) jsonObjectMain.get("resultList");
		JSONObject 	result  			= null;
		
		table.addCell(setCell("ลำดับ", getFont10Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("เลขที่ใบเสร็จ", getFont10Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("วันที่ขาย", getFont10Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("ชื่อลูกค้า", getFont10Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("จำนวนเงิน", getFont10Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("ค่าคอม", getFont10Bold(), 1, 1, Element.ALIGN_CENTER));
		
		if(resultList!=null && !resultList.isEmpty()){
			for(int i=0;i<resultList.size();i++){
				result = (JSONObject) resultList.get(i);
				table.addCell(setCell(String.valueOf((i+1)),   getFont8(), 1, 1, Element.ALIGN_CENTER));
				table.addCell(setCell(getText(result, "invoiceCode"),   getFont8(), 1, 1, Element.ALIGN_CENTER));
				table.addCell(setCell(getText(result, "invoiceDate"),   getFont8(), 1, 1, Element.ALIGN_CENTER));
				table.addCell(setCell(getText(result, "cusName"),   getFont8(), 1, 1, Element.ALIGN_LEFT));
				table.addCell(setCell(getText(result, "invoiceTotal"),   getFont8(), 1, 1, Element.ALIGN_RIGHT));
				table.addCell(setCell(getText(result, "saleCommission"),   getFont8(), 1, 1, Element.ALIGN_RIGHT));
			}
		}else{
			table.addCell(setCell("ไม่พบข้อมูล",   getFont8(), 6, 1, Element.ALIGN_CENTER));
		}
		
		table.setHeaderRows(1);
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
