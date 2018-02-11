package com.enjoy.stock.pdf;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.enjoy.core.pdf.utils.EnjoyItext;
import com.enjoy.core.pdf.utils.PdfFormService;
import com.enjoy.stock.pdf.header.BillingHeader;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class BillingPdfForm extends EnjoyItext implements PdfFormService {
	
	private PdfWriter 				writer;
	private JSONObject 				formDataObj;
	private BillingHeader 			header;
	
	private void setWriter(PdfWriter writer) {
		this.writer = writer;
	}

	public void setJSONObject(PdfWriter writer, JSONObject jsonObject) {
		this.formDataObj  	= jsonObject;
		this.header			= new BillingHeader(jsonObject);
		setWriter(writer);
		writer.setPageEvent(this.header);
		
	}
	
	public Document createForm(Document document) {
		System.out.println("[FullSlipCreditPdfForm][createForm][Begin]");
		
		try{
			document.add(genHeader(formDataObj, "ใบวางบิล"));
			document.add(this.brLine());
			document.add(this.genHeader1());
			
			JSONObject  customerDetails		= (JSONObject) this.formDataObj.get("customerDetails");
			if(null!=customerDetails){
				document.add(this.genCustomerDetail(customerDetails));
			}
			document.add(this.brLine());
			document.add(this.genDetail());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.genTotalCost());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.brLine());
			document.add(this.genFooter());
			
		}
		catch(DocumentException de){
			de.printStackTrace();
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		finally{
			System.out.println("[FullSlipCreditPdfForm][createForm][End]");
		}
		
		return document;
	}
	
//	private PdfPTable genHeader() throws DocumentException, MalformedURLException, IOException {
//		
//		float[] 	widths	 		= {1};
//		PdfPTable 	table 			= new PdfPTable(widths);
//		JSONObject 	jsonObjectMain  = this.formDataObj;
//		JSONObject  companyDetails	= (JSONObject) jsonObjectMain.get("companyDetails");
//		String		address			= "";
//		
//		address = " โทร." + getText(companyDetails, "tel") 
//				+ " Fax." + getText(companyDetails, "fax") 
//				+ " Email." + getText(companyDetails, "email");
//		
//		table.addCell(setCellWB("ใบวางบิล", getFont18Bold(), 1, Element.ALIGN_CENTER, 0));
//		table.addCell(setCellWB(getText(companyDetails, "companyName"), getFont14Bold(), 1, Element.ALIGN_CENTER, 0));
//		table.addCell(setCellWB(getText(companyDetails, "address"), getFont10(), 1, Element.ALIGN_CENTER, 0));
//		table.addCell(setCellWB(address, getFont10(), 1, Element.ALIGN_CENTER, 0));
//		table.addCell(setCellWB("เลขประจำตัวผู้เสียภาษี:" + getText(companyDetails, "tin"), getFont10(), 1, Element.ALIGN_CENTER, 0));
//		
//		table.setWidthPercentage(100);
//	
//		return table;
//	}
	
	private PdfPTable genHeader1() throws DocumentException, MalformedURLException, IOException {
		
		float[] 	widths	 			= {89f ,11f};
		PdfPTable 	table 				= new PdfPTable(widths);
		JSONObject 	jsonObjectMain  	= this.formDataObj;
		JSONObject  billingDetail		= (JSONObject) jsonObjectMain.get("billingDetail");
		
//		table.addCell(setCellWB("ใบวางบิล", getFont12Bold(), 2, Element.ALIGN_CENTER, 0));
		
//		table.addCell(setCellWB("เลขประจำตัวผู้เสียภาษี", getFont8Bold(), 1, Element.ALIGN_RIGHT, 0));
//		table.addCell(setCellWB(getText(billingDetail, "tin"), getFont8(), 1, Element.ALIGN_LEFT, 0));
		table.addCell(setCellWB("วันที่", getFont10Bold(), 1, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB(getText(billingDetail, "bullingDate"), getFont10(), 1, Element.ALIGN_RIGHT, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
	private PdfPTable genCustomerDetail(JSONObject customerDetails) throws DocumentException, MalformedURLException, IOException {
		
		PdfPTable 	table 				= new PdfPTable(1);
		
		table.addCell(setCellWB("ลูกค้า : " + getText(customerDetails, "cusName") + " " + getText(customerDetails, "cusSurname"), getFont10(), 1, Element.ALIGN_LEFT, 0));
		table.addCell(setCellWB("ที่อยู่  : " + getText(customerDetails, "address"), getFont10(), 1, Element.ALIGN_LEFT, 0));
		table.addCell(setCellWB("เลขประจำตัวผู้เสียภาษี : " + getText(customerDetails, "idNumber"), getFont10(), 1, Element.ALIGN_LEFT, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
	private PdfPTable genDetail() throws DocumentException, MalformedURLException, IOException {
		
		float[] 	widths	 				= {10f ,27f ,26f ,27f};
		PdfPTable 	table 					= new PdfPTable(widths);
		JSONObject 	jsonObjectMain  		= this.formDataObj;
		JSONArray 	invoiceCreditMasterList = (JSONArray) jsonObjectMain.get("invoiceCreditMasterList");
		JSONObject 	invoiceCreditMaster  	= null;
//		JSONObject  billingDetail			= (JSONObject) jsonObjectMain.get("billingDetail");
		
		table.addCell(setCell("ลำดับ", getFont9Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("เลขที่บิล", getFont9Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("วันที่", getFont9Bold(), 1, 1, Element.ALIGN_CENTER));
//		table.addCell(setCell("จำนวนเงิน", getFont9Bold(), 1, 1, Element.ALIGN_CENTER));
//		table.addCell(setCell("ส่วนลด", getFont9Bold(), 1, 1, Element.ALIGN_CENTER));
//		table.addCell(setCell("มัดจำ", getFont9Bold(), 1, 1, Element.ALIGN_CENTER));
//		table.addCell(setCell("ภาษีมูลค่าเพิ่ม " + getText(billingDetail, "vatDis"), getFont9Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("รวมจำนวนเงิน", getFont9Bold(), 1, 1, Element.ALIGN_CENTER));
		
		if(invoiceCreditMasterList!=null && !invoiceCreditMasterList.isEmpty()){
			for(int i=0;i<invoiceCreditMasterList.size();i++){
				invoiceCreditMaster = (JSONObject) invoiceCreditMasterList.get(i);
				table.addCell(setCellWB(String.valueOf((i+1)),   getFont9(), 1, Element.ALIGN_CENTER, 0));
				table.addCell(setCellWB(getText(invoiceCreditMaster, "invoiceCode"),   getFont9(), 1, Element.ALIGN_CENTER, 0));
				table.addCell(setCellWB(getText(invoiceCreditMaster, "invoiceDate"),   getFont9(), 1, Element.ALIGN_CENTER, 0));
//				table.addCell(setCell(getText(invoiceCreditMaster, "invoicePrice"),   getFont8(), 1, 1, Element.ALIGN_RIGHT));
//				table.addCell(setCell(getText(invoiceCreditMaster, "invoicediscount"),   getFont8(), 1, 1, Element.ALIGN_RIGHT));
//				table.addCell(setCell(getText(invoiceCreditMaster, "invoiceDeposit"),   getFont8(), 1, 1, Element.ALIGN_RIGHT));
//				table.addCell(setCell(getText(invoiceCreditMaster, "invoiceVat"),   getFont8(), 1, 1, Element.ALIGN_RIGHT));
				table.addCell(setCellWB(getText(invoiceCreditMaster, "invoiceTotal"),   getFont9(), 1, Element.ALIGN_RIGHT, 0));
			}
		}else{
			table.addCell(setCell("ไม่พบข้อมูล",   getFont9(), 5, 1, Element.ALIGN_CENTER));
		}
		
		table.setHeaderRows(1);
		table.setWidthPercentage(100);
	
		return table;
	}
	
	private PdfPTable genTotalCost() throws DocumentException, MalformedURLException, IOException {
		
		float[] 	widths	 			= {10f ,55f ,24f ,16f};
		PdfPTable 	table 				= new PdfPTable(widths);
		JSONObject 	jsonObjectMain  	= this.formDataObj;
		JSONObject  billingDetail		= (JSONObject) jsonObjectMain.get("billingDetail");
		
//		table.addCell(setCellWB("รวมบิล ", getFont8Bold(), 3, Element.ALIGN_RIGHT, 0));
//		table.addCell(setCellWB(getText(billingDetail, "totalRecord") + " ฉบับ", getFont8(), 1, Element.ALIGN_RIGHT, 0));
		
		table.addCell(setCellWB("จำนวนเงินสุทธิ ", getFont10Bold(), 3, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB(getText(billingDetail, "sumInvoicePrice"), getFont10(), 1, Element.ALIGN_RIGHT, 0));
		
//		table.addCell(setCellWB("ส่วนลดสุทธิ", getFont10Bold(), 3, Element.ALIGN_RIGHT, 0));
//		table.addCell(setCellWB(getText(billingDetail, "sumInvoicediscount"), getFont10(), 1, Element.ALIGN_RIGHT, 0));
		
//		table.addCell(setCellWB("มัดจำสุทธิ ", getFont10Bold(), 3, Element.ALIGN_RIGHT, 0));
//		table.addCell(setCellWB(getText(billingDetail, "sumInvoiceDeposit"), getFont10(), 1, Element.ALIGN_RIGHT, 0));
		
		table.addCell(setCellWB("ภาษีมูลค่าเพิ่ม " + getText(billingDetail, "vatDis") + "สุทธิ", getFont10Bold(), 3, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB(getText(billingDetail, "sumInvoiceVat"), getFont10(), 1, Element.ALIGN_RIGHT, 0));
		
		table.addCell(setCellWB("รวมจำนวนเงินสุทธิ ", getFont10Bold(), 3, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB(getText(billingDetail, "sumInvoiceTotal"), getFont10UnderLine(), 1, Element.ALIGN_RIGHT, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
	private PdfPTable genFooter() throws DocumentException, MalformedURLException, IOException {
		float[] 	widths	 	= {65f, 35f};
		PdfPTable 	table 		= new PdfPTable(widths);
		
		table.addCell(setCellWB("ผู้รับวางบิล..................................", getFont10(), 1, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB("ผู้วางบิล..................................", getFont10(), 1, Element.ALIGN_RIGHT, 0));
		
//		table.addCell(setCellWB("ผู้รับวางบิล                                                                              ", getFont10(), 1, Element.ALIGN_RIGHT, 0));
//		table.addCell(setCellWB("ผู้วางบิล                                                                         ", getFont10(), 1, Element.ALIGN_RIGHT, 0));
		
		table.addCell(setCellWB("", getFont10(), 2, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB("", getFont10(), 2, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB("", getFont10(), 2, Element.ALIGN_RIGHT, 0));
		
		table.addCell(setCellWB("วันที่ (...............................)", getFont10(), 1, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB("วันที่ (...............................)", getFont10(), 1, Element.ALIGN_RIGHT, 0));
		
		table.addCell(setCellWB("", getFont10(), 2, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB("", getFont10(), 2, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB("", getFont10(), 2, Element.ALIGN_RIGHT, 0));
		
		table.addCell(setCellWB("วันนัดชำระ (...............................)", getFont10(), 1, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB("", getFont10(), 1, Element.ALIGN_LEFT, 0));
		
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
