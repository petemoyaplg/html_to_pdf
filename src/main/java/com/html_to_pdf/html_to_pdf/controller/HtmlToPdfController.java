package com.html_to_pdf.html_to_pdf.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.html_to_pdf.html_to_pdf.services.PdfGenerateService;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;

@Controller
public class HtmlToPdfController {

  @Autowired
  private PdfGenerateService pdfGenerateService;

  private final static String url = "jdbc:postgresql://localhost:5432/bd_assurre";
  private final static String user = "postgres";
  private final static String password = "12345";

  private static final String SQL = "SELECT * FROM public.assurree ORDER BY id";

  @GetMapping("/")
  public String index(Model model) {
    Map<String, Object> data = getData();
    Map<String, Object> series = new HashMap<>();
    series.put("key1", "data1");
    series.put("key2", "data2");
    series.put("key3", "data3");
    series.put("key4", "data4");
    series.put("key5", "data5");

    model.addAttribute("series", series);
    model.addAttribute("serie1", data);
    model.addAttribute("y", data.get("y"));
    model.addAttribute("x", data.get("x"));
    model.addAttribute("columnNames", data.get("columnNames"));
    model.addAttribute("dataList", data.get("dataList"));

    model.addAttribute("backgroundColor", data.get("backgroundColor"));
    model.addAttribute("borderColor", data.get("borderColor"));
    model.addAttribute("type", "line");
    model.addAttribute("nbRapport", 3);

    return "htmla4";
  }

  public Map<String, Object> getData() {
    Map<String, Object> data = new HashMap<>();
    List<Integer> y = new ArrayList<>();
    List<Object> x = new ArrayList<>();

    try (Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement ps = connection.prepareStatement(SQL);) {
      ResultSet rs = ps.executeQuery();

      ResultSetMetaData metaData = rs.getMetaData();
      List<String> columnNames = new ArrayList<>();
      List<String> columnTypes = new ArrayList<>();
      int columnCount = metaData.getColumnCount();
      for (int i = 1; i <= columnCount; i++) {
        columnNames.add(metaData.getColumnName(i));
        columnTypes.add(metaData.getColumnTypeName(i));
      }
      System.out.println(columnNames);
      System.out.println(columnTypes);

      List<List<Object>> dataList = new ArrayList<>();
      while (rs.next()) {
        List<Object> dataItem = new ArrayList<>();
        for (int i = 0; i < columnCount; i++) {
          String type = columnTypes.get(i);
          if (type.equals("serial") || type.equals("int4")) {
            dataItem.add(rs.getInt(i + 1));
          }
          if (type.equals("int8") || type.equals("bigserial") || type.equals("bigint") || type.equals("oid")) {
            dataItem.add(rs.getLong(i + 1));
          }
          if (type.equals("float4")) {
            dataItem.add(rs.getFloat(i + 1));
          }
          if (type.equals("float8") || type.equals("money")) {
            dataItem.add(rs.getDouble(i + 1));
          }
          if (type.equals("varchar") || type.equals("char") || type.equals("bpchar") || type.equals("text")
              || type.equals("name")) {
            dataItem.add(rs.getString(i + 1));
          }
          if (type.equals("bool") || type.equals("bit")) {
            dataItem.add(rs.getBoolean(i + 1));
          }
          if (type.equals("date")) {
            dataItem.add(rs.getDate(i + 1));
          }
          if (type.equals("time") || type.equals("timetz")) {
            dataItem.add(rs.getTime(i + 1));
          }
          if (type.equals("timestamp") || type.equals("timestamptz")) {
            dataItem.add(rs.getTimestamp(i + 1));
          }
        }
        y.add(rs.getInt("quantite"));
        x.add(rs.getString("date_create"));

        dataList.add(dataItem);
      }

      // generatePDF(columnNames, dataList);

      data.put("columnNames", columnNames);
      data.put("dataList", dataList);
      data.put("orientation", false);

      data.put("x", x);
      data.put("y", y);

      List<String> backgroundColor = new ArrayList<>();
      List<String> borderColor = new ArrayList<>();
      for (int i = 0; i < dataList.size(); i++) {
        String bgColor = "rgba(" + randInt(1, 255) + ", " + randInt(1, 255) + ", "
            + randInt(1, 255)
            + ", 0.7)";
        String bColor = "rgba(" + randInt(1, 255) + ", " + randInt(1, 255) + ", "
            + randInt(1, 255)
            + ", 1)";
        backgroundColor.add(bgColor);
        borderColor.add(bColor);
      }

      data.put("backgroundColor", backgroundColor);
      data.put("borderColor", borderColor);
      data.put("type", "line");

      // pdfGenerateService.generatePdfFile("quotation1", data, "quotation1.pdf");

    } catch (SQLException e) {
    }
    return data;
  }

  public static int randInt(int min, int max) {
    return new Random().nextInt((max - min) + 1) + min;
  }
}
