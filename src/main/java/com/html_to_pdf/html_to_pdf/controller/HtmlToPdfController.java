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
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.html_to_pdf.html_to_pdf.model.CharteType;
import com.html_to_pdf.html_to_pdf.model.DataSourceConfig;
import com.html_to_pdf.html_to_pdf.model.Serie;
import com.html_to_pdf.html_to_pdf.model.SerieRender;
import com.html_to_pdf.html_to_pdf.services.GenerateDataService;

@Controller
public class HtmlToPdfController {

  @Autowired
  private GenerateDataService dataService;

  private static final Logger logger = Logger.getLogger(HtmlToPdfController.class.getName());

  @GetMapping("/")
  public String index(Model model) {
    List<Serie> series = new ArrayList<>();
    Serie serie = new Serie();
    serie.setIdSerie(UUID.randomUUID().toString());
    serie.setSql("SELECT * FROM public.assurree ORDER BY id");
    serie.setCharteType(CharteType.line);
    serie.setLabel("Assurrées");
    serie.setXLabel("date_create");
    serie.setYLabel("quantite");

    DataSourceConfig config = new DataSourceConfig();
    config.setUrl("jdbc:postgresql://localhost:5432/bd_assurre");
    config.setUser("postgres");
    config.setPwg("12345");

    serie.setConfig(config);
    serie.setChartIsVisible(true);
    series.add(serie);
    //
    serie = new Serie();
    serie.setIdSerie(UUID.randomUUID().toString());
    serie.setSql("SELECT * FROM public.product ORDER BY id");
    serie.setCharteType(CharteType.bar);
    serie.setLabel("Produit");
    serie.setXLabel("name");
    serie.setYLabel("qte");

    config = new DataSourceConfig();
    config.setUrl("jdbc:postgresql://localhost:5432/bd_assurre");
    config.setUser("postgres");
    config.setPwg("12345");

    serie.setConfig(config);
    serie.setChartIsVisible(true);
    series.add(serie);

    List<SerieRender> serieRenders = this.dataService.getData(series);

    model.addAttribute("series", serieRenders);

    return "htmla4";
  }
  
}
