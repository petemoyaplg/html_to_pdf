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

	private static final String SQL = "SELECT * FROM public.assurree ORDER BY id";

	public static void main(String[] args) {
		SpringApplication.run(HtmlToPdfApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		getAllUsers();
		// generateCertificat();
	}

	public void getAllUsers() {
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

			// Step 4: Process the ResultSet object.
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

	private void generateCertificat() {
		pdfGenerateService.generatePdfFile("certificat-assurance", null, "certificat.pdf");
	}

}
