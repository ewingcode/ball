package com.ewing.busi.ball.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.dom4j.DocumentException;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.pdf.BaseFont;

/**
 *
 * @author tansonlam
 * @create 2018年8月17日
 */
public class PDFTest {

	public static void main(String[] args) throws Exception {
		String htmlPath = "C:\\Users\\JIASHENGLIN\\Desktop\\template\\test.html";
		String pdfPath = "C:\\Users\\JIASHENGLIN\\Desktop\\template\\test.pdf";
		String content = FileUtils.readFileToString(new File(htmlPath));
		parsePdf(content, pdfPath);
	}

	/**
	 * HTML代码转PDF文档
	 * 
	 * @param content
	 *            待转换的HTML代码
	 * @param storagePath
	 *            保存为PDF文件的路径
	 * @throws com.itextpdf.text.DocumentException
	 */

	public static void parsePdf(String content, String storagePath)
			throws com.itextpdf.text.DocumentException {
		FileOutputStream os = null;
		try {
			File file = new File(storagePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			os = new FileOutputStream(file);

			ITextRenderer renderer = new ITextRenderer();
			// 解决中文支持问题
			ITextFontResolver resolver = renderer.getFontResolver();
		/*	resolver.addFont("/static/font/ARIALUNI.TTF", BaseFont.IDENTITY_H,
					BaseFont.NOT_EMBEDDED);*/
			renderer.setDocumentFromString(content);
			// 解决图片的相对路径问题,图片路径必须以file开头
			// renderer.getSharedContext().setBaseURL("file:/");
			renderer.layout();
			renderer.createPDF(os);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
