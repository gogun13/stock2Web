package com.enjoy.stock.pdf;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.enjoy.core.pdf.utils.EnjoyItext;
import com.enjoy.core.pdf.utils.PdfFormService;
import com.enjoy.stock.pdf.header.AlertLowProductHeader;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class AlertLowProductPdfForm extends EnjoyItext implements PdfFormService {
	
	private PdfWriter 				writer;
	private JSONObject 				formDataObj;
	private AlertLowProductHeader 	header;
	
	private void setWriter(PdfWriter writer) {
		this.writer = writer;
	}

	public void setJSONObject(PdfWriter writer, JSONObject jsonObject) {
		this.formDataObj  	= jsonObject;
		this.header			= new AlertLowProductHeader(jsonObject);
		setWriter(writer);
		writer.setPageEvent(this.header);
		
	}
	
	public Document createForm(Document document) {
		System.out.println("[AlertLowProductPdfForm][createForm][Begin]");
		
		try{
			document.add(genHeader(formDataObj, "รายงานแจ้งเตือน Stock สินค้าใกล้หมด"));
			document.add(this.brLine());
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
			System.out.println("[AlertLowProductPdfForm][createForm][End]");
		}
		
		return document;
	}
	
	private PdfPTable genDetail() throws DocumentException, MalformedURLException, IOException,Exception {
		
		float[] 	widths	 			= {5f ,20f ,20f ,25f ,15f ,15f};
		PdfPTable 	table 				= new PdfPTable(widths);
		JSONObject 	jsonObjectMain  	= this.formDataObj;
		JSONArray 	resultList 			= (JSONArray) jsonObjectMain.get("resultList");
		JSONObject 	result  			= null;
		
		table.addCell(setCell("ลำดับ", getFont9Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("หมวดสินค้า", getFont9Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("หมู่สินค้า", getFont9Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("รายการสินค้า", getFont9Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("ปริมาณที่แจ้งเตือน", getFont9Bold(), 1, 1, Element.ALIGN_CENTER));
		table.addCell(setCell("ปริมาณคงเหลือ", getFont9Bold(), 1, 1, Element.ALIGN_CENTER));
		
		if(resultList!=null && !resultList.isEmpty()){
			for(int i=0;i<resultList.size();i++){
				result = (JSONObject) resultList.get(i);
				table.addCell(setCell(String.valueOf((i+1)),   getFont8(), 1, 1, Element.ALIGN_CENTER));
				table.addCell(setCell(getText(result, "productTypeName"),   getFont8(), 1, 1, Element.ALIGN_CENTER));
				table.addCell(setCell(getText(result, "productGroupName"),   getFont8(), 1, 1, Element.ALIGN_CENTER));
				table.addCell(setCell(getText(result, "productName"),   getFont8(), 1, 1, Element.ALIGN_LEFT));
				table.addCell(setCell(getText(result, "minQuan"),   getFont8(), 1, 1, Element.ALIGN_CENTER));
				table.addCell(setCell(getText(result, "quantity"),   getFont8(), 1, 1, Element.ALIGN_CENTER));
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
