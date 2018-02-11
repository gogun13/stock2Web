package com.enjoy.core.pdf.utils;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.simple.JSONObject;

import com.enjoy.core.main.ConfigFile;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class EnjoyItext {
	private static EnjoyItext instance = null;
	private BaseFont bfComic;
	
	private Font font3;
	private Font font6;
	private Font font7;
	private Font font8;
	private Font font9;
	private Font font10;
	private Font font11;
	private Font font12;
	private Font font14;
	private Font font16;
	private Font font18;
	
	private Font font3Bold;
	private Font font6Bold;
	private Font font7Bold;
	private Font font8Bold;
	private Font font9Bold;
	private Font font10Bold;
	private Font font11Bold;
	private Font font12Bold;
	private Font font14Bold;
	private Font font16Bold;
	private Font font18Bold;
    
	private Font font3UnderLine;
	private Font font6UnderLine;
	private Font font7UnderLine;
	private Font font8UnderLine;
	private Font font9UnderLine;
	private Font font10UnderLine;
	private Font font11UnderLine;
	private Font font12UnderLine;
	private Font font14UnderLine;
	private Font font16UnderLine;
	private Font font18UnderLine;
	
	public EnjoyItext( ) {
		String fontName 	= 	ConfigFile.getPDF_FONT();
		
		try{
			bfComic 		= 	BaseFont.createFont(fontName, BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
			font3 			= 	new Font(bfComic, 3);
			font6 			= 	new Font(bfComic, 6);
			font7 			= 	new Font(bfComic, 7);
			font8 			= 	new Font(bfComic, 8);
			font9 			= 	new Font(bfComic, 9);
			font10 			= 	new Font(bfComic, 10);
			font11 			= 	new Font(bfComic, 11);
			font12 			= 	new Font(bfComic, 12);
			font14 			= 	new Font(bfComic, 14);
			font16 			= 	new Font(bfComic, 16);
			font18 			= 	new Font(bfComic, 18);
			
			font3Bold 		= 	new Font(bfComic, 3, Font.BOLD);
			font6Bold 		= 	new Font(bfComic, 6, Font.BOLD);
			font7Bold 		= 	new Font(bfComic, 7, Font.BOLD);
			font8Bold 		= 	new Font(bfComic, 8, Font.BOLD);
			font9Bold 		= 	new Font(bfComic, 9, Font.BOLD);
			font10Bold 		= 	new Font(bfComic, 10, Font.BOLD);
			font11Bold 		= 	new Font(bfComic, 11, Font.BOLD);
			font12Bold 		= 	new Font(bfComic, 12, Font.BOLD);
			font14Bold 		= 	new Font(bfComic, 14, Font.BOLD);
			font16Bold 		= 	new Font(bfComic, 16, Font.BOLD);
			font18Bold 		= 	new Font(bfComic, 18, Font.BOLD);
			
			font3UnderLine = 	FontFactory.getFont(fontName, 3,Font.UNDERLINE);
			font6UnderLine = 	FontFactory.getFont(fontName, 6,Font.UNDERLINE);
			font7UnderLine = 	FontFactory.getFont(fontName, 7,Font.UNDERLINE);
			font8UnderLine = 	FontFactory.getFont(fontName, 8,Font.UNDERLINE);
			font9UnderLine = 	FontFactory.getFont(fontName, 9,Font.UNDERLINE);
			font10UnderLine = 	FontFactory.getFont(fontName, 10,Font.UNDERLINE);
			font11UnderLine = 	FontFactory.getFont(fontName, 11,Font.UNDERLINE);
			font12UnderLine = 	FontFactory.getFont(fontName, 12,Font.UNDERLINE);
			font14UnderLine = 	FontFactory.getFont(fontName, 14,Font.UNDERLINE);
			font16UnderLine = 	FontFactory.getFont(fontName, 16,Font.UNDERLINE);
			font18UnderLine = 	FontFactory.getFont(fontName, 18,Font.UNDERLINE);
	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	// ================================== Cell Management Function	========================================================
	public PdfPCell setCell(String text, Font font, int colSpans, int align) {
		Paragraph paragraph = new Paragraph(text, font);
		return setCell (paragraph,colSpans,align,false) ; 
	}

	public PdfPCell setCell(String text, Font font, int colSpans, int rowSpans, int align) {
		Paragraph paragraph = new Paragraph(text, font);
		return setCell(paragraph, colSpans, rowSpans, align, false) ; 
	}
	
	public PdfPCell setCell(String text, Font font, int colSpans, int align, boolean noWarp) {
		Paragraph paragraph = new Paragraph(text, font);
		return setCell (paragraph,colSpans,align,noWarp) ; 
	}
	
	public PdfPCell setCell(Paragraph paragraph, int colSpans, int align) {
		return setCell (paragraph,colSpans,align,false) ;  
	}
	
	public PdfPCell setCell(Paragraph paragraph, int colSpans, int align, boolean noWarp) {
		PdfPCell cell = new PdfPCell(paragraph);

		cell.setColspan(colSpans);
		cell.setHorizontalAlignment(align);
		cell.setPaddingBottom(5);
		cell.setNoWrap(noWarp);
		return cell;
	}
	
	public PdfPCell setCell(Paragraph paragraph, int colSpans, int rowSpans, int align, boolean noWarp){
		PdfPCell cell = new PdfPCell(paragraph);

		cell.setColspan(colSpans);
		cell.setRowspan(rowSpans);
		cell.setHorizontalAlignment(align);
		cell.setPaddingBottom(5);
		cell.setNoWrap(noWarp);
		return cell;
	}
	
	public PdfPCell setCell(PdfPTable table, int colSpans) {
		PdfPCell cell = new PdfPCell(table);
		cell.setColspan(colSpans);
		return cell;
	}
	
	public PdfPCell setCell(PdfPTable table, int colSpans, int border) {
		PdfPCell cell = new PdfPCell(table);
		cell.setColspan(colSpans);
		if (border == 0) {
			cell.setBorder(border);
		} else {
			cell.setBorder(border + 1);
		}
		return cell;
	}
	
	public PdfPCell setCell(PdfPTable table, int colSpans, int align, boolean noWarp) {
		PdfPCell cell = new PdfPCell(table);

		cell.setColspan(colSpans);
		cell.setHorizontalAlignment(align);
		cell.setPaddingBottom(5);
		cell.setNoWrap(noWarp);
		return cell;
	}
	
	public PdfPCell setCell(Image image, int colSpans, int rowSpans, int align, boolean noWarp){
		PdfPCell cell = new PdfPCell(image);

		cell.setColspan(colSpans);
		cell.setRowspan(rowSpans);
		cell.setHorizontalAlignment(align);
		cell.setPaddingBottom(5);
		cell.setNoWrap(noWarp);
		return cell;
	}
	
	public PdfPCell setCellWithBorder(Phrase myPhrase, int colSpans, int align, int border) {
		PdfPCell cell = new PdfPCell(myPhrase);

		cell.setColspan(colSpans);
		cell.setHorizontalAlignment(align);
		cell.setUseBorderPadding(true);
		// cell.setPaddingBottom(3); //��ͧ��ҧ��ҹ...
		if (border == 0) {
			cell.setBorder(border);
		} else {
			cell.setBorder(border + 1);
		}
		return cell;
	}
	
	
	public PdfPCell setCellWB(Paragraph paragraph, int colSpans, int align, int border) {
		return setCellWB(paragraph,colSpans,align, border ,false,false);
	}
	public PdfPCell setCellWB(String text, Font font, int colSpans,int align, int border) {
		Paragraph p = new Paragraph(text, font);
		return setCellWB(p,colSpans,align, border ,false,false);
	}
	public PdfPCell setCellWB(String text, Font font, int colSpans, int rowSpans, int align, int border) {
		Paragraph p = new Paragraph(text, font);
		return setCellWB(p, colSpans, rowSpans, align, border, false, false);
	}
	public PdfPCell setCellWB(Paragraph paragraph, int colSpans,int align, int border, boolean space) {
		return setCellWB(paragraph,colSpans,align, border, space, false);
	}
	public PdfPCell setCellWB(Paragraph Paragraph, int colSpans,int align, int border, boolean space ,boolean noWrap) {
		PdfPCell cell = new PdfPCell(Paragraph);
		cell.setColspan(colSpans);
		cell.setHorizontalAlignment(align);
		cell.setUseBorderPadding(true);
		cell.setNoWrap(noWrap);
		if (space) {
			cell.setPaddingBottom(5);
		}
		
		if (border == 0) {
			cell.setBorder(border);
		} else {
			cell.setBorder(border + 1);
		}
		return cell;
	}
	
	public PdfPCell setCellWB(PdfPTable table, int colSpans, int align, int border, boolean space, boolean noWrap) {
		PdfPCell cell = new PdfPCell(table);
		cell.setColspan(colSpans);
		cell.setHorizontalAlignment(align);
		cell.setUseBorderPadding(true);
		cell.setNoWrap(noWrap);
		if (space) {
			cell.setPaddingBottom(5); // ��ͧ��ҧ��ҹ...
		}
		if (border == 0) {
			cell.setBorder(border);
		} else {
			cell.setBorder(border + 1);
		}
		return cell;
	}
	
	public PdfPCell setCellWB(Image image, int colSpans,int align, int border, boolean space ,boolean noWrap) {
		PdfPCell cell = new PdfPCell(image);
		cell.setColspan(colSpans);
		cell.setHorizontalAlignment(align);
		cell.setUseBorderPadding(true);
		cell.setNoWrap(noWrap);
		if (space) {
			cell.setPaddingBottom(5);
		}
		if (border == 0) {
			cell.setBorder(border);
		} else {
			cell.setBorder(border + 1);
		}
		return cell;
	}
	
	public PdfPCell setCellWB(Paragraph Paragraph, int colSpans, int rowSpans, int align, int border, boolean space, boolean noWrap){
		PdfPCell cell = new PdfPCell(Paragraph);
		cell.setColspan(colSpans);
		cell.setRowspan(rowSpans);
		cell.setHorizontalAlignment(align);
		cell.setUseBorderPadding(true);
		cell.setNoWrap(noWrap);
		
		if(space){
			cell.setPaddingBottom(5); // ��ͧ��ҧ��ҹ...
		}
		if(border == 0){
			cell.setBorder(border);
		}else{
			cell.setBorder(border + 1);
		}
		return cell;
	}
	
	public PdfPCell setCellWBWithHeight(String text, Font font, int colSpans,int align, int border,int h) {
		Paragraph p = new Paragraph(text, font);
		return setCellWBWithHeight(p,colSpans,align, border ,false,false,h);
	}
	
	public PdfPCell setCellWBWithHeight(Paragraph Paragraph, int colSpans,int align, int border, boolean space ,boolean noWrap,int h) {
		PdfPCell cell = new PdfPCell(Paragraph);
		cell.setColspan(colSpans);
		cell.setHorizontalAlignment(align);
		cell.setUseBorderPadding(true);
		cell.setNoWrap(noWrap);
		if (space) {
			cell.setPaddingBottom(5);
		}
		
		cell.setFixedHeight(h);
		
		if (border == 0) {
			cell.setBorder(border);
		} else {
			cell.setBorder(border + 1);
		}
		return cell;
	}
	
	public String getText(JSONObject obj, String field){
		String ret = "-";
		String txt = (String) obj.get(field);
		
		if(txt!=null && !txt.equals("")){
			ret = txt;
		}
		
		return ret;
		
	}
	
	public static EnjoyItext getInstance() {
		return instance;
	}
	public static void setInstance(EnjoyItext instance) {
		EnjoyItext.instance = instance;
	}
	public BaseFont getBfComic() {
		return bfComic;
	}
	public void setBfComic(BaseFont bfComic) {
		this.bfComic = bfComic;
	}
	public Font getFont6() {
		return font6;
	}
	public void setFont6(Font font6) {
		this.font6 = font6;
	}
	public Font getFont7() {
		return font7;
	}
	public void setFont7(Font font7) {
		this.font7 = font7;
	}
	public Font getFont8() {
		return font8;
	}
	public void setFont8(Font font8) {
		this.font8 = font8;
	}
	public Font getFont9() {
		return font9;
	}
	public void setFont9(Font font9) {
		this.font9 = font9;
	}
	public Font getFont10() {
		return font10;
	}
	public void setFont10(Font font10) {
		this.font10 = font10;
	}
	public Font getFont11() {
		return font11;
	}
	public void setFont11(Font font11) {
		this.font11 = font11;
	}
	public Font getFont12() {
		return font12;
	}
	public void setFont12(Font font12) {
		this.font12 = font12;
	}
	public Font getFont14() {
		return font14;
	}
	public void setFont14(Font font14) {
		this.font14 = font14;
	}
	public Font getFont16() {
		return font16;
	}
	public void setFont16(Font font16) {
		this.font16 = font16;
	}
	public Font getFont18() {
		return font18;
	}
	public void setFont18(Font font18) {
		this.font18 = font18;
	}
	public Font getFont6Bold() {
		return font6Bold;
	}
	public void setFont6Bold(Font font6Bold) {
		this.font6Bold = font6Bold;
	}
	public Font getFont7Bold() {
		return font7Bold;
	}
	public void setFont7Bold(Font font7Bold) {
		this.font7Bold = font7Bold;
	}
	public Font getFont8Bold() {
		return font8Bold;
	}
	public void setFont8Bold(Font font8Bold) {
		this.font8Bold = font8Bold;
	}
	public Font getFont9Bold() {
		return font9Bold;
	}
	public void setFont9Bold(Font font9Bold) {
		this.font9Bold = font9Bold;
	}
	public Font getFont10Bold() {
		return font10Bold;
	}
	public void setFont10Bold(Font font10Bold) {
		this.font10Bold = font10Bold;
	}
	public Font getFont11Bold() {
		return font11Bold;
	}
	public void setFont11Bold(Font font11Bold) {
		this.font11Bold = font11Bold;
	}
	public Font getFont12Bold() {
		return font12Bold;
	}
	public void setFont12Bold(Font font12Bold) {
		this.font12Bold = font12Bold;
	}
	public Font getFont14Bold() {
		return font14Bold;
	}
	public void setFont14Bold(Font font14Bold) {
		this.font14Bold = font14Bold;
	}
	public Font getFont16Bold() {
		return font16Bold;
	}
	public void setFont16Bold(Font font16Bold) {
		this.font16Bold = font16Bold;
	}
	public Font getFont18Bold() {
		return font18Bold;
	}
	public void setFont18Bold(Font font18Bold) {
		this.font18Bold = font18Bold;
	}
	public Font getFont6UnderLine() {
		return font6UnderLine;
	}
	public void setFont6UnderLine(Font font6UnderLine) {
		this.font6UnderLine = font6UnderLine;
	}
	public Font getFont7UnderLine() {
		return font7UnderLine;
	}
	public void setFont7UnderLine(Font font7UnderLine) {
		this.font7UnderLine = font7UnderLine;
	}
	public Font getFont8UnderLine() {
		return font8UnderLine;
	}
	public void setFont8UnderLine(Font font8UnderLine) {
		this.font8UnderLine = font8UnderLine;
	}
	public Font getFont9UnderLine() {
		return font9UnderLine;
	}
	public void setFont9UnderLine(Font font9UnderLine) {
		this.font9UnderLine = font9UnderLine;
	}
	public Font getFont10UnderLine() {
		return font10UnderLine;
	}
	public void setFont10UnderLine(Font font10UnderLine) {
		this.font10UnderLine = font10UnderLine;
	}
	public Font getFont11UnderLine() {
		return font11UnderLine;
	}
	public void setFont11UnderLine(Font font11UnderLine) {
		this.font11UnderLine = font11UnderLine;
	}
	public Font getFont12UnderLine() {
		return font12UnderLine;
	}
	public void setFont12UnderLine(Font font12UnderLine) {
		this.font12UnderLine = font12UnderLine;
	}
	public Font getFont14UnderLine() {
		return font14UnderLine;
	}
	public void setFont14UnderLine(Font font14UnderLine) {
		this.font14UnderLine = font14UnderLine;
	}
	public Font getFont16UnderLine() {
		return font16UnderLine;
	}
	public void setFont16UnderLine(Font font16UnderLine) {
		this.font16UnderLine = font16UnderLine;
	}
	public Font getFont18UnderLine() {
		return font18UnderLine;
	}
	public void setFont18UnderLine(Font font18UnderLine) {
		this.font18UnderLine = font18UnderLine;
	}
	public Font getFont3() {
		return font3;
	}
	public void setFont3(Font font3) {
		this.font3 = font3;
	}
	public Font getFont3Bold() {
		return font3Bold;
	}
	public void setFont3Bold(Font font3Bold) {
		this.font3Bold = font3Bold;
	}
	public Font getFont3UnderLine() {
		return font3UnderLine;
	}
	public void setFont3UnderLine(Font font3UnderLine) {
		this.font3UnderLine = font3UnderLine;
	}
	
	public PdfPTable genHeader(JSONObject jsonObjectMain, String reportName) throws DocumentException, MalformedURLException, IOException {
		
		PdfPTable 	table 			= new PdfPTable(1);
		JSONObject  companyDetails	= (JSONObject) jsonObjectMain.get("companyDetails");
		String		address			= "";
		
		address = " โทร." + getText(companyDetails, "tel") 
				+ " Fax." + getText(companyDetails, "fax") 
				+ " Email." + getText(companyDetails, "email");
		
		table.addCell(setCellWB(reportName, getFont18Bold(), 1, Element.ALIGN_CENTER, 0));
		table.addCell(setCellWB(getText(companyDetails, "companyName"), getFont14Bold(), 1, Element.ALIGN_CENTER, 0));
		table.addCell(setCellWB(getText(companyDetails, "address"), getFont10(), 1, Element.ALIGN_CENTER, 0));
		table.addCell(setCellWB(address, getFont10(), 1, Element.ALIGN_CENTER, 0));
		table.addCell(setCellWB("", getFont10(), 1, Element.ALIGN_CENTER, 0));
		table.addCell(setCellWB("เลขประจำตัวผู้เสียภาษี:" + getText(companyDetails, "tin"), getFont10(), 1, Element.ALIGN_CENTER, 0));
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
	public PdfPTable brLine(int padding) throws DocumentException, MalformedURLException, IOException {
		
		PdfPTable	table 	= new PdfPTable(1);
		PdfPCell 	cell 	= new PdfPCell();
		
		cell.setPaddingBottom(padding);
		cell.setBorder(0);
		
		table.addCell(cell);
		
		table.setWidthPercentage(100);
	
		return table;
	}
	
}
