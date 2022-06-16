package com.html_to_pdf.html_to_pdf.services;

import java.util.Map;

public interface PdfGenerateService {
  void generatePdfFile(String templateName, Map<String, Object> data, String pdfFileName);
}
