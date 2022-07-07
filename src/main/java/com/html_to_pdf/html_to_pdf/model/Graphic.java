package com.html_to_pdf.html_to_pdf.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Graphic {
  private List<Integer> y = new ArrayList<>();
  private List<Object> x = new ArrayList<>();
  private List<String> backgroundColor;
  private List<String> borderColor;
  private CharteType charteType;

  {
    y = new ArrayList<>();
    x = new ArrayList<>();
    backgroundColor = new ArrayList<>();
    borderColor = new ArrayList<>();
  }
}
