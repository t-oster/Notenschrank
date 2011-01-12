package com.t_oster.notenschrank.data;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

public class Sheet {
	
	public static final String FILEEXTENSION = "pdf";
	private File pdf_file;
	private PDFFile pdf_renderer_pdf;
	private PdfReader itext_reader;
	
	private PDFFile getPdfRendererPDF() throws IOException{
		if (pdf_renderer_pdf==null){
			//load a pdf from a byte buffer
	        RandomAccessFile raf = new RandomAccessFile(pdf_file, "r");
	        FileChannel channel = raf.getChannel();
	        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
	        pdf_renderer_pdf = new PDFFile(buf);
		}
		return pdf_renderer_pdf;
	}
	private PdfReader getItextReader() throws IOException{
		if (itext_reader==null){
			itext_reader = new PdfReader(pdf_file.getAbsolutePath());
		}
		return itext_reader;
	}
	
	public Sheet (File pdfFile) throws IOException{
		this.pdf_file = pdfFile;
		if (!this.pdf_file.exists()){
			throw new IOException("No such file!");
		}
	}
	
	/*
	 * Note: works only correct for pages rotated 0 or 180 degree
	 */
	public Image getPreview(Dimension percent, Dimension imagesize) throws IOException{
		if (percent==null){
			percent = new Dimension(100,100);
		}
		
		PDFFile pdffile = this.getPdfRendererPDF();
        // draw the first page to an image
        PDFPage page = pdffile.getPage(pdffile.getNumPages());
        
        //get the width and height for the doc at the default zoom
        
        int width = (int)page.getBBox().getWidth()*percent.width/100;
        int height =  (int)page.getBBox().getHeight()*percent.height/100;
        int posx =(int)page.getBBox().getWidth()-width;
        int posy =(int)page.getBBox().getHeight()-height;
        if (page.getRotation()==180){
        	posx=0;
        	posy=0;
        }
        Rectangle rect = new Rectangle(posx,posy,
               width,
               height);
        
        if (imagesize==null){
        	imagesize = new Dimension(width,height);
        }
        
        //generate the image
        Image img = page.getImage(
                imagesize.width, imagesize.height, //width & height
                rect, // clip rect
                null, // null for the ImageObserver
                true, // fill background with white
                true  // block until drawing is done
                );
        
        return img;
	}
	
	public void print(){
		throw new RuntimeException();
	}
	
	public void print(int[] pages){
		throw new RuntimeException();
	}
	
	public int numberOfPages() throws IOException{
		return this.getPdfRendererPDF().getNumPages();
	}

	public void rotatePage(int page, int quarters) throws IOException, DocumentException{
		File tmp = SettingsManager.getInstance().getTempFile();
		this.writeToFile(tmp);
		PdfReader reader = new PdfReader(tmp.getAbsolutePath());
		for (int k = 1; k <= reader.getNumberOfPages(); k++) {
			//TODO: Check Nullpointer Exception.
			if (k==page){
				PdfNumber n = (PdfNumber) reader.getPageN(k).get(PdfName.ROTATE);
				reader.getPageN(k).put(PdfName.ROTATE, new PdfNumber((n.intValue()+
						+90*quarters)%360));
			}
		}
		FileOutputStream stream =  new FileOutputStream(pdf_file);
		PdfStamper stp = new PdfStamper(reader,stream);
		stp.close();
		stream.flush();
		tmp.delete();
		this.reset();
	}
	
	private void reset(){
		this.pdf_renderer_pdf = null;
		this.itext_reader = null;
	}
	
	public void delete(){
		this.pdf_file.delete();
		this.pdf_file=null;
		this.itext_reader=null;
	}
	
	public void addPage(Sheet s) throws IOException, DocumentException{
		File temp = SettingsManager.getInstance().getTempFile();
		this.writeToFile(temp);
		PdfReader myReader = new PdfReader(temp.getAbsolutePath());
		PdfReader pdfReader = s.getItextReader();
		Document document = new Document();
		FileOutputStream stream = new FileOutputStream(pdf_file);
		PdfWriter writer = PdfWriter.getInstance(document, stream);
		document.open();
		PdfContentByte cb = writer.getDirectContent();
		for(int pageOfCurrentReaderPDF=1;pageOfCurrentReaderPDF<=myReader.getNumberOfPages();pageOfCurrentReaderPDF++){
			document.newPage();
			PdfImportedPage page1 = writer.getImportedPage(myReader, pageOfCurrentReaderPDF);
			cb.addTemplate(page1, 0, 0);
		}
		for(int pageOfCurrentReaderPDF=1;pageOfCurrentReaderPDF<=pdfReader.getNumberOfPages();pageOfCurrentReaderPDF++){
			document.newPage();
			PdfImportedPage page1 = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
			cb.addTemplate(page1, 0, 0);
		}
		stream.flush();
		document.close();
		temp.delete();
		this.reset();
	}

	public void writeToFile(File out) throws IOException, DocumentException{
		PdfReader rd = this.getItextReader();
		FileOutputStream stream = new FileOutputStream(out);
		PdfStamper stp = new PdfStamper(rd, stream);
		stp.close();
		stream.flush();
	}
}
