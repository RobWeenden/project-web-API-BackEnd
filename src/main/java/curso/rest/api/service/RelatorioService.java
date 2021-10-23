package curso.rest.api.service;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class RelatorioService implements Serializable {

	private static final long serialVersionUID = 1L;
	@Autowired
	private JdbcTemplate jdbc;

	@SuppressWarnings("unchecked")
	public byte[] gerarRelatorio(String nomeRelatorio, Map<String, Object> params, ServletContext context) throws SQLException, JRException {
		Connection connection = jdbc.getDataSource().getConnection();
		String pathJasper = context.getRealPath("relatorios") + File.separator + nomeRelatorio + ".jasper";
		JasperPrint print = JasperFillManager.fillReport(pathJasper, params, connection);
		byte[] exportPdf = JasperExportManager.exportReportToPdf(print);
		connection.close();
		
		return exportPdf;
	}
}
