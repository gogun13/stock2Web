package com.enjoy.stock.pdf;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.enjoy.core.pdf.utils.EnjoyItext;
import com.enjoy.core.pdf.utils.PdfFormService;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.pdf.header.FullSlipCreditHeader;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class FullSlipCreditPdfForm extends EnjoyItext implements PdfFormService {
	
	private PdfWriter 				writer;
	private JSONObject 				formDataObj;
	private FullSlipCreditHeader 	header;
	
	private void setWriter(PdfWriter writer) {
		this.writer = writer;
	}

	public void setJSONObject(PdfWriter writer, JSONObject jsonObject) {
		this.formDataObj  	= jsonObject;
		this.header			= new FullSlipCreditHeader(jsonObject);
		setWriter(writer);
		writer.setPageEvent(this.header);
		
	}
	
	public Document createForm(Document document) {
		System.out.println("[FullSlipCreditPdfForm][createForm][Begin]");
		
		try{
			document.add(this.genHeader());
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
	
	private PdfPTable genHeader() throws DocumentException, MalformedURLException, IOException {
		
		float[] 	widths	 		= {1};
		PdfPTable 	table 			= new PdfPTable(widths);
		JSONObject 	jsonObjectMain  = this.formDataObj;
		JSONObject  companyDetails	= (JSONObject) jsonObjectMain.get("companyDetails");
		String		address			= "";
		
		address = " โทร." + getText(companyDetails, "tel") 
				+ " Fax." + getText(companyDetails, "fax") 
				+ " Email." + getText(companyDetails, "email");
		
		table.addCell(setCellWB("ใบส่งของชั่วคราว", getFont14Bold(), 1, Element.ALIGN_CENTER, 0));
		table.addCell(setCellWB(getText(companyDetails, "companyName"), getFont12Bold(), 1, Element.ALIGN_CENTER, 0));
		table.addCell(setCellWB(getText(companyDetails, "address"), getFont9(), 1, Element.ALIGN_CENTER, 0));
		table.addCell(setCellWB(address, getFont9(), 1, Element.ALIGN_CENTER, 0));
//		table.addCell(setCellWB("เลขประจำตัวผู้เสียภาษี:" + getText(companyDetails, "tin"), getFont9(), 1, Element.ALIGN_CENTER, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
	private PdfPTable genHeader1() throws DocumentException, MalformedURLException, IOException {
		
		float[] 	widths	 			= {75f ,25f};
		PdfPTable 	table 				= new PdfPTable(widths);
		JSONObject 	jsonObjectMain  	= this.formDataObj;
		JSONObject  invoiceCreditMaster	= (JSONObject) jsonObjectMain.get("invoiceCreditMaster");
		
		table.addCell(setCellWB("เลขที่", getFont9Bold(), 1, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB(getText(invoiceCreditMaster, "invoiceCode"), getFont9(), 1, Element.ALIGN_LEFT, 0));
		
		table.addCell(setCellWB("วันที่", getFont9Bold(), 1, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB(getText(invoiceCreditMaster, "invoiceDate"), getFont9(), 1, Element.ALIGN_LEFT, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
	private PdfPTable genCustomerDetail(JSONObject customerDetails) throws DocumentException, MalformedURLException, IOException {
		
//		float[] 	widths	 			= {12f,88f};
		PdfPTable 	table 				= new PdfPTable(1);
		
		table.addCell(setCellWB("ลูกค้า : " + getText(customerDetails, "cusName") + " " + getText(customerDetails, "cusSurname"), getFont9(), 1, Element.ALIGN_LEFT, 0));
//		table.addCell(setCellWB("ที่อยู่  : " + getText(customerDetails, "address"), getFont9(), 1, Element.ALIGN_LEFT, 0));
		table.addCell(setCellWB("เลขประจำตัวผู้เสียภาษี : " + getText(customerDetails, "idNumber"), getFont9(), 1, Element.ALIGN_LEFT, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
	private PdfPTable genDetail() throws DocumentException, MalformedURLException, IOException {
		
		float[] 	widths	 				= {7f ,28f ,17f ,16f ,16f ,16f};
		PdfPTable 	table 					= new PdfPTable(widths);
		JSONObject 	jsonObjectMain  		= this.formDataObj;
		JSONArray 	invoiceCreditDetailList = (JSONArray) jsonObjectMain.get("invoiceCreditDetailList");
		JSONObject 	invoiceCreditDetail  	= null;
		
		table.addCell(setCell("ลำดับ", getFont8Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("สินค้า", getFont8Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("ปริมาณ", getFont8Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("ราคาต่อหน่วย", getFont8Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("ส่วนลด(%)", getFont8Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("ราคาขาย", getFont8Bold(), 1, 1, Element.ALIGN_CENTER));
		
		for(int i=0;i<invoiceCreditDetailList.size();i++){
			invoiceCreditDetail = (JSONObject) invoiceCreditDetailList.get(i);
			table.addCell(setCellWB(String.valueOf((i+1)),   getFont8(), 1, Element.ALIGN_CENTER, 0));
			table.addCell(setCellWB(getText(invoiceCreditDetail, "productName"),   getFont8(), 1, Element.ALIGN_LEFT, 0));
			table.addCell(setCellWB(getText(invoiceCreditDetail, "quantity") + " " + getText(invoiceCreditDetail, "unitName"),   getFont8(), 1, Element.ALIGN_CENTER, 0));
			table.addCell(setCellWB(getText(invoiceCreditDetail, "pricePerUnit"),   getFont8(), 1, Element.ALIGN_RIGHT, 0));
			table.addCell(setCellWB(getText(invoiceCreditDetail, "discount"),   getFont8(), 1, Element.ALIGN_RIGHT, 0));
			table.addCell(setCellWB(getText(invoiceCreditDetail, "price"),   getFont8(), 1, Element.ALIGN_RIGHT, 0));
		}
		
		table.setHeaderRows(1);
		table.setWidthPercentage(100);
	
		return table;
	}
	
	private PdfPTable genTotalCost() throws DocumentException, MalformedURLException, IOException {
		
		float[] 	widths	 			= {10f ,50f ,24f ,21f};
		PdfPTable 	table 				= new PdfPTable(widths);
		float[] 	sub_w	 			= {1};
		PdfPTable 	tableSub 			= new PdfPTable(sub_w);
		float[] 	subWRemark	 		= {12f, 88f};
		PdfPTable 	tableSubWRemark 	= new PdfPTable(subWRemark);
		JSONObject 	jsonObjectMain  	= this.formDataObj;
		JSONObject  invoiceCreditMaster	= (JSONObject) jsonObjectMain.get("invoiceCreditMaster");
		
		table.addCell(setCellWB("รวมจำรวนเงิน ", getFont9Bold(), 3, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB(getText(invoiceCreditMaster, "invoicePrice"), getFont9(), 1, Element.ALIGN_RIGHT, 0));
		
//		table.addCell(setCellWB("หักส่วนลด ", getFont9Bold(), 3, Element.ALIGN_RIGHT, 0));
//		table.addCell(setCellWB(getText(invoiceCreditMaster, "invoicediscount"), getFont9(), 1, Element.ALIGN_RIGHT, 0));
		
//		table.addCell(setCellWB("ภาษีมูลค่าเพิ่ม " + getText(invoiceCreditMaster, "vatDis"), getFont9Bold(), 3, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB("VAT", getFont9Bold(), 3, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB(getText(invoiceCreditMaster, "invoiceVat"), getFont9(), 1, Element.ALIGN_RIGHT, 0));
		
//		table.addCell(setCellWB("หักมัดจำ ", getFont9Bold(), 3, Element.ALIGN_RIGHT, 0));
//		table.addCell(setCellWB(getText(invoiceCreditMaster, "invoiceDeposit"), getFont9(), 1, Element.ALIGN_RIGHT, 0));
		
		tableSub.addCell(setCell(EnjoyUtils.displayAmountThai(getText(invoiceCreditMaster, "invoiceTotal")), getFont8(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCellWB(tableSub, 2, Element.ALIGN_LEFT, 0, false, false));
		table.addCell(setCellWB("จำนวนเงินรวมทั้งสิ้น ", getFont9Bold(), 1, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB(getText(invoiceCreditMaster, "invoiceTotal"), getFont9UnderLine(), 1, Element.ALIGN_RIGHT, 0));
		
		table.addCell(setCellWB("", getFont9Bold(), 4, Element.ALIGN_LEFT, 0));
		
		tableSubWRemark.addCell(setCellWB("หมายเหต", getFont8Bold(), 1, Element.ALIGN_LEFT, 0));
		tableSubWRemark.addCell(setCellWB(getText(invoiceCreditMaster, "remark"), getFont8(), 3, Element.ALIGN_LEFT, 0));
		table.addCell(setCellWB(tableSubWRemark, 4, Element.ALIGN_LEFT, 0, false, false));
		
//		table.addCell(setCellWB("หมายเหต ", getFont8Bold(), 1, Element.ALIGN_LEFT, 0));
//		table.addCell(setCellWB(getText(invoiceCreditMaster, "remark"), getFont8(), 3, Element.ALIGN_LEFT, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
	private PdfPTable genFooter() throws DocumentException, MalformedURLException, IOException {
		float[] 	widths	 	= {60f, 40f};
		PdfPTable 	table 		= new PdfPTable(widths);
		
		table.addCell(setCellWB("ผู้ส่งสินค้า...............................", getFont9(), 1, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB("ผู้รับสินค้า...............................", getFont9(), 1, Element.ALIGN_RIGHT, 0));
		
//		table.addCell(setCellWB("ผู้ส่งสินค้า (ลายเซ็นต์)", getFont9(), 1, Element.ALIGN_RIGHT, 0));
//		table.addCell(setCellWB("ผู้รับสินค้า (ลายเซ็นต์)", getFont9(), 1, Element.ALIGN_RIGHT, 0));
		
		table.addCell(setCellWB("", getFont9(), 2, Element.ALIGN_RIGHT, 0));
		
		table.addCell(setCellWB("วันที่ (...............................)", getFont9(), 1, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB("      วันที่ (...............................)", getFont9(), 1, Element.ALIGN_RIGHT, 0));
		
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
