package com.html_to_pdf.html_to_pdf.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DataTable {
  private List<List<Object>> dataList;
  private List<String> columnNames;
  private List<String> columnTypes;

  {
    dataList = new ArrayList<>();
    columnNames = new ArrayList<>();
    columnTypes = new ArrayList<>();
  }
}
