package com.html_to_pdf.html_to_pdf;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.html_to_pdf.html_to_pdf.services.PdfGenerateService;

@SpringBootApplication
public class HtmlToPdfApplication implements CommandLineRunner {

	@Autowired
	private PdfGenerateService pdfGenerateService;

	private final static String url = "jdbc:postgresql://localhost:5432/bd_assurre";
	private final static String user = "postgres";
	private final static String password = "12345";

	private static final String SELECT_ALL = "SELECT * FROM public.assurree";

	public static void main(String[] args) {
		SpringApplication.run(HtmlToPdfApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		getAllUsers();
	}

	public void getAllUsers() {
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = connection.prepareStatement(SELECT_ALL);) {
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

			// Step 4: Process the ResultSet object.
			List<List<Object>> dataList = new ArrayList<>();
			while (rs.next()) {
				List<Object> dataItem = new ArrayList<>();
				for (int i = 0; i < columnCount; i++) {
					String type = columnTypes.get(i);
					if (type.equals("serial")) {
						dataItem.add(rs.getInt(i + 1));
					}
					if (type.equals("varchar")) {
						dataItem.add(rs.getString(i + 1));
					}
				}
				dataList.add(dataItem);
			}
			generatePDF(columnNames, dataList);
		} catch (SQLException e) {
		}
	}

	private void generatePDF(List<String> columnNames, List<List<Object>> dataList) {
		Map<String, Object> data = new HashMap<>();
		data.put("columns", columnNames);
		data.put("assurrees", dataList);
		data.put("orientation", false);
		pdfGenerateService.generatePdfFile("quotation1", data, "quotation1.pdf");
	}

}
