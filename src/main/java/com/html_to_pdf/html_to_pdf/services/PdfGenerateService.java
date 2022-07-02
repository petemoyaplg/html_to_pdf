package com.html_to_pdf.html_to_pdf.services;

import java.util.Map;
import com.lowagie.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PdfGenerateService {
  private Logger logger = LoggerFactory.getLogger(PdfGenerateService.class);

  @Autowired
  private TemplateEngine templateEngine;

  @Value("${pdf.directory}")
  private String pdfDirectory;

  public void generatePdfFile(String templateName, Map<String, Object> data, String pdfFileName) {
    Context context = new Context();
    context.setVariables(data);
    System.out.println("====================================================");
    System.out.println(context.getVariableNames());
    System.out.println(pdfDirectory);

    String htmlContent = templateEngine.process(templateName, context);
    FileOutputStream fileOutputStream = null;
    try {
      fileOutputStream = new FileOutputStream(pdfDirectory + pdfFileName);
      ITextRenderer renderer = new ITextRenderer();
      renderer.setDocumentFromString(htmlContent);
      renderer.layout();
      renderer.createPDF(fileOutputStream, false);
      renderer.finishPDF();
    } catch (FileNotFoundException e) {
      logger.error(e.getMessage(), e);
    } catch (DocumentException e) {
      logger.error(e.getMessage(), e);
    } finally {
      if (fileOutputStream != null) {
        try {
          fileOutputStream.close();
        } catch (IOException e) {
          System.out.println(e.getMessage());
        }
      }
    }
  }
}
