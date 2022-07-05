package com.html_to_pdf.html_to_pdf.model;

import lombok.Data;

@Data
public class Serie {
  private String sql;
  private CharteType charteType;
  private DataSourceConfig config;
  private Boolean chartIsVisible;
}
