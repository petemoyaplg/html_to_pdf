package com.html_to_pdf.html_to_pdf.model;

import lombok.Data;

@Data
public class Serie {
  private String idSerie;
  private String sql;
  private String label;
  private String xLabel;
  private String yLabel;
  private CharteType charteType;
  private DataSourceConfig config;
  private Boolean chartIsVisible;
}
