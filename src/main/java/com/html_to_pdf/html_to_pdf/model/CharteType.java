package com.html_to_pdf.html_to_pdf.model;

public enum CharteType {
  line("line"), area("area"), bar("bar");

  private String value;

  CharteType(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
