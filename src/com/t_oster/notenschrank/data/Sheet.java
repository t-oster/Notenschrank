package com.t_oster.notenschrank.data;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.image.BufferedImage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;


public class Sheet {

	public static final String FILEEXTENSION = "pdf";
	public static final boolean USE_LOWAGIE_PDFLIB = true;

	private File pdf_file;
	private PDDocument pdf_renderer_pdf;
	private PdfReader itext_reader;

	private PDDocument getPdfRendererPDF() throws IOException {
		if (pdf_renderer_pdf == null) {
                    pdf_renderer_pdf = PDDocument.load(pdf_file.getAbsolutePath());
		}
		return pdf_renderer_pdf;
	}

	private PdfReader getItextReader() throws IOException {
		if (itext_reader == null) {
			itext_reader = new PdfReader(pdf_file.getAbsolutePath());
		}
		return itext_reader;
	}

	public Sheet(File pdfFile) throws IOException {
		this.pdf_file = pdfFile;
		if (!this.pdf_file.exists()) {
			throw new IOException("No such file!");
		}
	}

	/*
	 * Note: works only correct for pages rotated 0 or 180 degree
	 */
	public Image getPreview(Dimension relPosition, Dimension relSize,
			Dimension imagesize) throws IOException {

		if (relPosition.width + relSize.width > 100
				|| relPosition.height + relSize.height > 100) {
			throw new InvalidParameterException(
					"We have just 100%. Requested Size:" + relSize + " @ "
							+ relPosition);
		}

                PDDocument pdfdocument = this.getPdfRendererPDF();
                List<PDPage> pages = pdfdocument.getDocumentCatalog().getAllPages();
                
                PDPage page = null;
		if (SettingsManager.getInstance().isShowLastPage()) {
			page = pages.get(pages.size()-1);
		} else {
			page = pages.get(0);
		}

                BufferedImage buffImage = page.convertToImage();

		// get the width and height for the doc at the default zoom

		int docwidth = (int) buffImage.getWidth();
		int docheight = (int) buffImage.getHeight();

		int abswidth = docwidth * relSize.width / 100;
		int absheight = docheight * relSize.height / 100;
		int posx = relPosition.width * docwidth / 100;
		int posy = relPosition.height * docheight / 100;

		Rectangle rect = new Rectangle(posx, docheight - posy - absheight,
				abswidth, absheight);

		if (imagesize == null) {
			imagesize = new Dimension(abswidth, absheight);
		}
		// TODO: FixRotation
		// generate the image
		Image img = buffImage.getSubimage(posx, posy, abswidth, absheight);
		return img;
	}

	public int numberOfPages() throws IOException {
		return this.getPdfRendererPDF().getNumberOfPages();
	}

	public void rotatePage(int[] pages, int quarters) throws IOException,
			DocumentException {
		File tmp = SettingsManager.getInstance().getTempFile();
		this.writeToFile(tmp);
		PdfReader reader = new PdfReader(tmp.getAbsolutePath());
		if (pages != null) {
			for (int k : pages) {
				System.out.println("Document has " + reader.getNumberOfPages()
						+ " and we want " + k);
				PdfDictionary d = reader.getPageN(k);
				PdfNumber n = null;
				if (d != null) {
					n = (PdfNumber) reader.getPageN(k).get(PdfName.ROTATE);
				}
				int nn = n != null ? n.intValue() : 0;
				reader.getPageN(k).put(PdfName.ROTATE,
						new PdfNumber((nn + +90 * quarters) % 360));
			}
		} else {// rotate all pages
			for (int k = 1; k <= reader.getNumberOfPages(); k++) {
				PdfNumber n = (PdfNumber) reader.getPageN(k)
						.get(PdfName.ROTATE);
				int nn = n != null ? n.intValue() : 0;
				reader.getPageN(k).put(PdfName.ROTATE,
						new PdfNumber((nn + +90 * quarters) % 360));
			}
		}
		FileOutputStream stream = new FileOutputStream(pdf_file);
		PdfStamper stp = new PdfStamper(reader, stream);
		stp.close();
		stream.flush();
		tmp.delete();
		this.reset();
	}

	public void rotateAllPages(int quarters) throws IOException,
			DocumentException {
		this.rotatePage(null, quarters);
	}

	/**
	 * Opens the current Sheet in a PDF -Reader Warning: OS-dependant Function.
	 * May not be implemented well for some OSes
	 * 
	 * Throws IOException if Document couldn't be found and
	 * UnsupportedOperationException if not implemented for specific OS
	 */
	public void openInReader() throws IOException {
		switch (Util.getOS()) {
		case WINDOWS: {
			if (USE_LOWAGIE_PDFLIB) {
				com.lowagie.tools.Executable.openDocument(pdf_file);
			} else {
				throw new RuntimeException(
						"Diese Funktion ist für ihr OS momentan nicht verfügbar.\n"
								+ "Bitte drucken Sie die Datei '"
								+ pdf_file.getAbsolutePath() + "' manuell.");
			}
			break;
		}
		case LINUX: {
			if (USE_LOWAGIE_PDFLIB) {
				Process p = com.lowagie.tools.Executable.openDocument(pdf_file);
				if (p == null) {
					System.err.println("Lowagie doesn't work... fallback.");
					p = Runtime.getRuntime().exec(
							new String[] { "acroread", "-openInNewInstance",
									"-tempFileTitle",
									"Notenschrank-Druckansicht",
									pdf_file.getAbsolutePath() });
					if (p == null) {
						throw new RuntimeException(
								"Diese Funktion ist für ihr OS momentan nicht verfügbar.\n"
										+ "Bitte drucken Sie die Datei '"
										+ pdf_file.getAbsolutePath()
										+ "' manuell.");
					}

				}
			} else {
				Runtime.getRuntime().exec(
						new String[] { "acroread", "-openInNewInstance",
								"-tempFileTitle", "Notenschrank-Druckansicht",
								pdf_file.getAbsolutePath() });
			}
			break;
		}
		default: {
			throw new UnsupportedOperationException(
					"Unter ihrem Betriebssystem können die Dokumente nicht direkt geöffnet werden."
							+ "\nBitte öffnen Sie manuell die Datei: '"
							+ pdf_file.getAbsolutePath() + "'");
		}
		}

	}

	/**
	 * Prints the current sheet on the default Printer. Warning: OS-dependant
	 * Function. May not be implemented well for some OSes
	 * 
	 * Throws IOException if Document couldn't be found and
	 * UnsupportedOperationException if not implemented for specific OS
	 * 
	 * @throws IOException
	 */
	public void print() throws IOException {
		switch (Util.getOS()) {
		case WINDOWS: {
			if (USE_LOWAGIE_PDFLIB) {
				com.lowagie.tools.Executable.printDocumentSilent(pdf_file);
			} else {
				throw new RuntimeException(
						"Diese Funktion ist für ihr OS momentan nicht verfügbar.\n"
								+ "Bitte drucken Sie die Datei '"
								+ pdf_file.getAbsolutePath() + "' manuell.");
			}
			break;
		}
		case LINUX: {
			// TODO: doesn't work
			try {
				Runtime.getRuntime()
						.exec(new String[] { "printpdf",
								pdf_file.getAbsolutePath() }).waitFor();
			} catch (InterruptedException e) {
				throw new UnsupportedOperationException(
						"Unter ihrem Betriebssystem können die Dokumente nicht direkt gedruckt werden."
								+ "\nBitte drucken Sie manuell die Datei: '"
								+ pdf_file.getAbsolutePath() + "'");
			}
			break;
		}
		default: {
			throw new UnsupportedOperationException(
					"Unter ihrem Betriebssystem können die Dokumente nicht direkt gedruckt werden."
							+ "\nBitte drucken Sie manuell die Datei: '"
							+ pdf_file.getAbsolutePath() + "'");
		}
		}

	}

	private void reset() {
		this.pdf_renderer_pdf = null;
		this.itext_reader = null;
	}

	public void delete() {
		this.pdf_file.delete();
		this.pdf_file = null;
		this.itext_reader = null;
	}

	public void addPage(Sheet s) throws IOException, DocumentException {
		if (USE_LOWAGIE_PDFLIB) {
			File temp = null;
			if (pdf_file.length() != 0) {// File is not empty
				temp = SettingsManager.getInstance().getTempFile();
				com.lowagie.tools.ConcatPdf.main(new String[] {
						pdf_file.getAbsolutePath(), temp.getAbsolutePath() });
				com.lowagie.tools.ConcatPdf.main(new String[] {
						temp.getAbsolutePath(), s.pdf_file.getAbsolutePath(),
						this.pdf_file.getAbsolutePath() });
			} else {
				com.lowagie.tools.ConcatPdf.main(new String[] {
						s.pdf_file.getAbsolutePath(),
						this.pdf_file.getAbsolutePath() });
			}

		} else {
			File temp = null;
			PdfReader myReader = null;
			if (pdf_file.length() != 0) {// File is not empty
				temp = SettingsManager.getInstance().getTempFile();
				this.writeToFile(temp);
				myReader = new PdfReader(temp.getAbsolutePath());
			}

			PdfReader pdfReader = s.getItextReader();
			Document document = new Document();
			FileOutputStream stream = new FileOutputStream(pdf_file);
			PdfWriter writer = PdfWriter.getInstance(document, stream);
			document.open();
			PdfContentByte cb = writer.getDirectContent();
			if (myReader != null) {// add old contend if file was not empty
				for (int pageOfCurrentReaderPDF = 1; pageOfCurrentReaderPDF <= myReader
						.getNumberOfPages(); pageOfCurrentReaderPDF++) {
					document.newPage();
					PdfImportedPage page1 = writer.getImportedPage(myReader,
							pageOfCurrentReaderPDF);
					cb.addTemplate(page1, 0, 0);
				}
			}
			for (int pageOfCurrentReaderPDF = 1; pageOfCurrentReaderPDF <= pdfReader
					.getNumberOfPages(); pageOfCurrentReaderPDF++) {
				document.newPage();
				PdfImportedPage page1 = writer.getImportedPage(pdfReader,
						pageOfCurrentReaderPDF);
				cb.addTemplate(page1, 0, 0);
			}
			stream.flush();
			document.close();
			if (temp != null) {
				temp.delete();
			}
		}
		this.reset();
	}

	public void writeToFile(File out) throws IOException, DocumentException {
		FileChannel inChannel = new FileInputStream(pdf_file).getChannel();
		FileChannel outChannel = new FileOutputStream(out).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (IOException e) {
			throw e;
		} finally {
			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
		}
	}

	/**
	 * Creates a new PDF file for every page in the sheet and returns a List of
	 * Sheets containing those.
	 * 
	 * @return a list of Sheets containing PDF files for each page of this sheet
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public List<Sheet> tileInPages() throws IOException, DocumentException {
		List<Sheet> result = new LinkedList<Sheet>();
		PdfReader pdfReader = this.getItextReader();
		for (int page = 1; page <= pdfReader.getNumberOfPages(); page++) {
			//Generate Filename
			File file = this.pdf_file;
			int i=0;
			while (file.exists())
			{
				String parent = this.pdf_file.getParent();
				String name = this.pdf_file.getName();
				file = new File(parent+File.separator+(i++)+name);
			}
			Document document = new Document();
			FileOutputStream stream = new FileOutputStream(file);
			PdfWriter writer = PdfWriter.getInstance(document, stream);
			document.open();
			document.newPage();
			PdfImportedPage page1 = writer.getImportedPage(pdfReader,
					page);
			PdfContentByte cb = writer.getDirectContent();
			cb.addTemplate(page1, 0, 0);
			stream.flush();
			document.close();
			result.add(new Sheet(file));
		}
		return result;
	}
}
