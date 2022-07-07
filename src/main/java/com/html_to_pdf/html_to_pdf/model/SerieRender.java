package com.html_to_pdf.html_to_pdf.model;

import lombok.Data;

@Data
public class SerieRender {
  private String idSerie;
  private String label;
  private DataTable dataTable;
  private Graphic graphic;

  {
    dataTable = new DataTable();
    graphic = new Graphic();
  }
}
