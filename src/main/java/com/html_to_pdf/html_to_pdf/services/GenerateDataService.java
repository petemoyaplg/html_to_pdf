package com.html_to_pdf.html_to_pdf.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.html_to_pdf.html_to_pdf.model.Serie;
import com.html_to_pdf.html_to_pdf.model.SerieRender;

@Service
public class GenerateDataService {

  public List<SerieRender> getData(List<Serie> series) {
    List<SerieRender> serieRenders = new ArrayList<>();
    List<Integer> y;
    List<Object> x;

    try {

      for (Serie serie : series) {
        Connection connection = DriverManager.getConnection(
            serie.getConfig().getUrl(),
            serie.getConfig().getUser(),
            serie.getConfig().getPwg());

        PreparedStatement ps = connection.prepareStatement(serie.getSql());
        ResultSet rs = ps.executeQuery();

        SerieRender serieRender = new SerieRender();
        serieRender.setIdSerie(serie.getIdSerie());
        serieRender.setLabel(serie.getLabel());
        serieRender.getGraphic().setCharteType(serie.getCharteType());
        y = new ArrayList<>();
        x = new ArrayList<>();

        ResultSetMetaData metaData = rs.getMetaData();
        List<String> columnNames = serieRender.getDataTable().getColumnNames();
        List<String> columnTypes = serieRender.getDataTable().getColumnTypes();

        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
          columnNames.add(metaData.getColumnName(i));
          columnTypes.add(metaData.getColumnTypeName(i));
        }
        System.out.println(serieRender.getDataTable().getColumnNames());
        System.out.println(serieRender.getDataTable().getColumnTypes());

        List<List<Object>> dataList = serieRender.getDataTable().getDataList();
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
            if (type.equals("float8") || type.equals("money") || type.equals("numeric")) {
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
          y.add(rs.getInt(serie.getYLabel()));
          x.add(rs.getString(serie.getXLabel()));

          dataList.add(dataItem);
        }

        serieRender.getGraphic().setX(x);
        serieRender.getGraphic().setY(y);

        serieRender = setGraphicStyle(serieRender, dataList.size());

        serieRenders.add(serieRender);
      }
    } catch (SQLException e) {
    }
    return serieRenders;
  }

  private SerieRender setGraphicStyle(SerieRender serieRender, int sizeDataListe) {

    List<String> backgroundColor = serieRender.getGraphic().getBackgroundColor();
    List<String> borderColor = serieRender.getGraphic().getBorderColor();
    for (int i = 0; i < sizeDataListe; i++) {
      String bgColor = "rgba(" + randInt(1, 255) + ", " + randInt(1, 255) + ", "
          + randInt(1, 255)
          + ", 0.7)";
      String bColor = "rgba(" + randInt(1, 255) + ", " + randInt(1, 255) + ", "
          + randInt(1, 255)
          + ", 1)";
      backgroundColor.add(bgColor);
      borderColor.add(bColor);
    }
    return serieRender;
  }

  private static int randInt(int min, int max) {
    return new Random().nextInt((max - min) + 1) + min;
  }
}
