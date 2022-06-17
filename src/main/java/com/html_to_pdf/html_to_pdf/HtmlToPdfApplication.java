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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import com.html_to_pdf.html_to_pdf.model.Assurree;
import com.html_to_pdf.html_to_pdf.services.PdfGenerateService;

@SpringBootApplication
public class HtmlToPdfApplication implements CommandLineRunner {

	@Autowired
	private JdbcTemplate jdbcTemplate;

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
		// String sql = "SELECT * FROM public.assurree";
		// SqlRowSet queryForRowSet = jdbcTemplate.queryForRowSet(sql);
		// SqlRowSetMetaData metaData = queryForRowSet.getMetaData();
		// String[] columnNames = metaData.getColumnNames();
		// System.out
		// .println("=================================================================================================");
		// for (String columnName : columnNames) {
		// System.out.println(columnName);
		// }
		// System.out
		// .println("=================================================================================================");
		// int x = 1;
		// while (queryForRowSet.next()) {
		// System.out.println("OK");
		// x++;
		// }
		// System.out.println(x);
		// List<Assurree> assurrees = jdbcTemplate.query(sql, (rs, rowNum) -> {
		// return new Assurree(rs.getInt(1), rs.getString(2), rs.getString(3),
		// rs.getString(4));
		// });

		// for (Assurree assurree : assurrees) {
		// System.out.println(assurree);
		// }
		// List<Map<Object, Object>> values = new ArrayList<>();
		// for (Assurree assurree : assurrees) {
		// Map<Object, Object> assurreeMap = new HashMap<>();
		// assurreeMap.put(columnNames[0], assurree.getId());
		// assurreeMap.put(columnNames[1], assurree.getEmail());
		// assurreeMap.put(columnNames[2], assurree.getName());
		// assurreeMap.put(columnNames[3], assurree.getUsername());

		// values.add(assurreeMap);
		// }
		// for (Map<Object, Object> map : values) {
		// System.out.println(map);
		// }
		getAllUsers();
	}

	public void getAllUsers() {
		try (Connection connection = DriverManager.getConnection(url, user, password);

				PreparedStatement ps = connection.prepareStatement(SELECT_ALL);) {
			// System.out.println(ps);
			// Step 3: Execute the query or update query
			ResultSet rs = ps.executeQuery();

			ResultSetMetaData metaData = rs.getMetaData();
			List<String> columnNames = new ArrayList<>();
			List<String> columnTypes = new ArrayList<>();
			int columnCount = metaData.getColumnCount();
			System.out.print(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				columnNames.add(metaData.getColumnName(i));
				columnTypes.add(metaData.getColumnTypeName(i));
			}
			System.out.print(columnNames);
			System.out.print(columnTypes);

			// Step 4: Process the ResultSet object.
			List<Map<String, Object>> datas = new ArrayList<>();
			while (rs.next()) {

				Map<String, Object> data = new HashMap<>();
				for (int i = 0; i < columnCount; i++) {
					String type = columnTypes.get(i);
					if (type.equals("serial")) {
						data.put(columnNames.get(i), rs.getInt(i + 1));
					}
					if (type.equals("varchar")) {
						data.put(columnNames.get(i), rs.getString(i + 1));
					}

				}
				datas.add(data);
			}
			for (Map<String, Object> map : datas) {
				System.out.println(map);
			}
			generatePDF(columnNames, datas);
		} catch (SQLException e) {
		}
	}

	private void generatePDF(List<String> columnNames, List<Map<String, Object>> datas) {
		Map<String, Object> data = new HashMap<>();
		data.put("columns", columnNames);
		data.put("assurrees", data);

		pdfGenerateService.generatePdfFile("quotation1", data, "quotation1.pdf");
	}

}
