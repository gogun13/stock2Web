package com.enjoy.stock.pdf;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.enjoy.core.pdf.utils.EnjoyItext;
import com.enjoy.core.pdf.utils.PdfFormService;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.pdf.header.FullSlipCashNoVatHeader;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class FullSlipCashNoVatPdfForm extends EnjoyItext implements PdfFormService {
	
	private PdfWriter 				writer;
	private JSONObject 				formDataObj;
	private FullSlipCashNoVatHeader header;
	
	private void setWriter(PdfWriter writer) {
		this.writer = writer;
	}

	public void setJSONObject(PdfWriter writer, JSONObject jsonObject) {
		this.formDataObj  	= jsonObject;
		this.header			= new FullSlipCashNoVatHeader(jsonObject);
		setWriter(writer);
		writer.setPageEvent(this.header);
		
	}
	
	public Document createForm(Document document) {
		System.out.println("[FullSlipCashNoVatPdfForm][createForm][Begin]");
		
		try{
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
			
		}
		catch(DocumentException de){
			de.printStackTrace();
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		finally{
			System.out.println("[FullSlipCashNoVatPdfForm][createForm][End]");
		}
		
		return document;
	}
	
	private PdfPTable genHeader1() throws DocumentException, MalformedURLException, IOException {
		
		float[] 	widths	 			= {22f,28f ,25f,25f};
		PdfPTable 	table 				= new PdfPTable(widths);
		JSONObject 	jsonObjectMain  	= this.formDataObj;
		JSONObject  invoiceCashMaster	= (JSONObject) jsonObjectMain.get("invoiceCashMaster");
		
		table.addCell(setCellWB("เงินสด", getFont12Bold(), 4, Element.ALIGN_CENTER, 0));
		
		table.addCell(setCellWB("เลขที่", getFont9Bold(), 3, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB(getText(invoiceCashMaster, "invoiceCode"), getFont9(), 1, Element.ALIGN_LEFT, 0));
		
		table.addCell(setCellWB("วันที่", getFont9Bold(), 3, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB(getText(invoiceCashMaster, "invoiceDate"), getFont9(), 1, Element.ALIGN_LEFT, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
	private PdfPTable genCustomerDetail(JSONObject customerDetails) throws DocumentException, MalformedURLException, IOException {
		
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
		JSONArray 	invoiceCashDetailList 	= (JSONArray) jsonObjectMain.get("invoiceCashDetailList");
		JSONObject 	invoiceCashDetail  		= null;
		
		table.addCell(setCell("ลำดับ", getFont8Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("สินค้า", getFont8Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("ปริมาณ", getFont8Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("ราคาต่อหน่วย", getFont8Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("ส่วนลด(%)", getFont8Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("ราคาขาย", getFont8Bold(), 1, 1, Element.ALIGN_CENTER));
		
		for(int i=0;i<invoiceCashDetailList.size();i++){
			invoiceCashDetail = (JSONObject) invoiceCashDetailList.get(i);
			table.addCell(setCellWB(String.valueOf((i+1)),   getFont8(), 1, Element.ALIGN_CENTER, 0));
			table.addCell(setCellWB(getText(invoiceCashDetail, "productName"),   getFont8(), 1, Element.ALIGN_LEFT, 0));
			table.addCell(setCellWB(getText(invoiceCashDetail, "quantity") + " " + getText(invoiceCashDetail, "unitName"),   getFont8(), 1, Element.ALIGN_CENTER, 0));
			table.addCell(setCellWB(getText(invoiceCashDetail, "pricePerUnit"),   getFont8(), 1, Element.ALIGN_RIGHT, 0));
			table.addCell(setCellWB(getText(invoiceCashDetail, "discount"),   getFont8(), 1, Element.ALIGN_RIGHT, 0));
			table.addCell(setCellWB(getText(invoiceCashDetail, "price"),   getFont8(), 1, Element.ALIGN_RIGHT, 0));
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
		JSONObject  invoiceCashMaster	= (JSONObject) jsonObjectMain.get("invoiceCashMaster");
		
		table.addCell(setCellWB("รวมจำรวนเงิน ", getFont9Bold(), 3, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB(getText(invoiceCashMaster, "invoicePrice"), getFont9(), 1, Element.ALIGN_RIGHT, 0));
		
		table.addCell(setCellWB("หักส่วนลด ", getFont9Bold(), 3, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB(getText(invoiceCashMaster, "invoicediscount"), getFont9(), 1, Element.ALIGN_RIGHT, 0));
		
		table.addCell(setCellWB("หักมัดจำ ", getFont9Bold(), 3, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB(getText(invoiceCashMaster, "invoiceDeposit"), getFont9(), 1, Element.ALIGN_RIGHT, 0));
		
		tableSub.addCell(setCell(EnjoyUtils.displayAmountThai(getText(invoiceCashMaster, "invoiceTotal")), getFont8(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCellWB(tableSub, 2, Element.ALIGN_LEFT, 0, false, false));
		table.addCell(setCellWB("จำนวนเงินรวมทั้งสิ้น ", getFont9Bold(), 1, Element.ALIGN_RIGHT, 0));
		table.addCell(setCellWB(getText(invoiceCashMaster, "invoiceTotal"), getFont9UnderLine(), 1, Element.ALIGN_RIGHT, 0));
		
		table.addCell(setCellWB("", getFont9Bold(), 4, Element.ALIGN_LEFT, 0));
		
		if(!"-".equals(getText(invoiceCashMaster, "remark"))){
			tableSubWRemark.addCell(setCellWB("หมายเหต", getFont8Bold(), 1, Element.ALIGN_LEFT, 0));
			tableSubWRemark.addCell(setCellWB(getText(invoiceCashMaster, "remark"), getFont8(), 3, Element.ALIGN_LEFT, 0));
			table.addCell(setCellWB(tableSubWRemark, 4, Element.ALIGN_LEFT, 0, false, false));
		}
		
//		table.addCell(setCellWB("หมายเหต ", getFont8Bold(), 1, Element.ALIGN_LEFT, 0));
//		table.addCell(setCellWB(getText(invoiceCashMaster, "remark"), getFont8(), 3, Element.ALIGN_LEFT, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
//	private PdfPTable genFooter() throws DocumentException, MalformedURLException, IOException {
//		float[] 	widths	 	= {75f, 25f};
//		PdfPTable 	table 		= new PdfPTable(widths);
//		
//		table.addCell(setCellWB("ผู้รับเงิน (ลายเซ็นต์)", getFont8(), 1, Element.ALIGN_RIGHT, 0));
//		table.addCell(setCellWB("ผู้รับสินค้า (ลายเซ็นต์)", getFont8(), 1, Element.ALIGN_RIGHT, 0));
//		
//		table.setWidthPercentage(100);
//	
//		return table;
//	}

	public PdfPTable brLine() throws DocumentException, MalformedURLException, IOException {
		
		PdfPTable 	table 			= new PdfPTable(1);
		
		table.addCell(setCellWB("", getFont12Bold(), 1, Element.ALIGN_LEFT, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
}
