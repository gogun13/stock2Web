package com.enjoy.stock.pdf;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.enjoy.core.pdf.utils.EnjoyItext;
import com.enjoy.core.pdf.utils.PdfFormService;
import com.enjoy.stock.pdf.header.HistoryPurchasedByDealerHeader;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class HistoryPurchasedByDealerPdfForm extends EnjoyItext implements PdfFormService {
	
	private PdfWriter 						writer;
	private JSONObject 						formDataObj;
	private HistoryPurchasedByDealerHeader 	header;
	
	private void setWriter(PdfWriter writer) {
		this.writer = writer;
	}

	public void setJSONObject(PdfWriter writer, JSONObject jsonObject) {
		this.formDataObj  	= jsonObject;
		this.header			= new HistoryPurchasedByDealerHeader(jsonObject);
		setWriter(writer);
		writer.setPageEvent(this.header);
		
	}
	
	public Document createForm(Document document) {
		System.out.println("[FullSlipCashPdfForm][createForm][Begin]");
		
		try{
			document.add(genHeader(formDataObj, "รายงานประวัติการซื้อตามผู้จำหน่าย"));
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
			System.out.println("[FullSlipCashPdfForm][createForm][End]");
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
//		table.addCell(setCellWB("รายงานประวัติการซื้อตามผู้จำหน่าย", getFont10Bold(), 1, Element.ALIGN_CENTER, 0));
//		
//		table.setWidthPercentage(100);
//	
//		return table;
//	}
	
	private PdfPTable genHeader1() throws DocumentException, MalformedURLException, IOException {
		
		PdfPTable 	table 							= new PdfPTable(1);
		JSONObject 	jsonObjectMain  				= this.formDataObj;
		String		rangeReciveDate					= "";
		
		rangeReciveDate = getText(jsonObjectMain, "reciveDateFrom") + " - " + getText(jsonObjectMain, "reciveDateTo");
		
		table.addCell(setCellWB(rangeReciveDate, getFont9Bold(), 1, Element.ALIGN_RIGHT, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
	private PdfPTable genDetail() throws DocumentException, MalformedURLException, IOException,Exception {
		
		float[] 	widths	 							= {7f ,34f ,13f ,13f ,15f, 15f};
		PdfPTable 	table 								= new PdfPTable(widths);
		JSONObject 	jsonObjectMain  					= this.formDataObj;
		JSONArray 	historyPurchasedByDealerReportList 	= (JSONArray) jsonObjectMain.get("historyPurchasedByDealerReportList");
		JSONObject 	historyPurchasedByDealerReport  	= null;
		
		table.addCell(setCell("ลำดับ", getFont10Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("บริษัท-สาขา", getFont10Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("เลขที่ใบสั่งของ", getFont10Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("วันที่สั่งของ", getFont10Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("ราคา", getFont10Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("ส่วนลด", getFont10Bold(), 1, 1, Element.ALIGN_CENTER));
		
		if(historyPurchasedByDealerReportList!=null && !historyPurchasedByDealerReportList.isEmpty()){
			for(int i=0;i<historyPurchasedByDealerReportList.size();i++){
	//		for(int i=0;i<100;i++){
				historyPurchasedByDealerReport = (JSONObject) historyPurchasedByDealerReportList.get(i);
				table.addCell(setCell(String.valueOf((i+1)),   getFont8(), 1, 1, Element.ALIGN_CENTER));
				table.addCell(setCell(getText(historyPurchasedByDealerReport, "vendorName"),   getFont8(), 1, 1, Element.ALIGN_LEFT));
				table.addCell(setCell(getText(historyPurchasedByDealerReport, "reciveNo"),   getFont8(), 1, 1, Element.ALIGN_CENTER));
				table.addCell(setCell(getText(historyPurchasedByDealerReport, "reciveDate"),   getFont8(), 1, 1, Element.ALIGN_CENTER));
				table.addCell(setCell(getText(historyPurchasedByDealerReport, "reciveTotal"),   getFont8(), 1, 1, Element.ALIGN_RIGHT));
				table.addCell(setCell(getText(historyPurchasedByDealerReport, "reciveDiscount"),   getFont8(), 1, 1, Element.ALIGN_RIGHT));
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
