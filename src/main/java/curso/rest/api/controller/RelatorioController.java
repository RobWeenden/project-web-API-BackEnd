package curso.rest.api.controller;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.rest.api.model.UsuarioReport;
import curso.rest.api.service.RelatorioService;
import net.sf.jasperreports.engine.JRException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/usuario/v1")
public class RelatorioController {
	
	@Autowired
	private RelatorioService relatorioService;
	
	@GetMapping(value="/relatorio", produces= "application/text")
	public ResponseEntity<String> downloadRelatorio(HttpServletRequest request) throws SQLException, JRException{
				
		byte[] pdf = relatorioService.gerarRelatorio("relatorio-usuario", new HashMap<>(), request.getServletContext());
		String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);
		
		return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
		
	}
	
	@PostMapping(value="/relatorio/", produces= "application/text")
	public ResponseEntity<String> downloadRelatorioParam(HttpServletRequest request, @RequestBody UsuarioReport userReport) throws SQLException, JRException, ParseException{
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dateConvert = new SimpleDateFormat("yyyy-MM-dd");
		
		String dataInicio = dateConvert.format(dateFormat.parse(userReport.getDataInicio()));
		String dataFim = dateConvert.format(dateFormat.parse(userReport.getDataFim()));
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("DATA_INICIO", dataInicio);
		params.put("DATA_FIM", dataFim);
		
		byte[] pdf = relatorioService.gerarRelatorio("relatorio-usuario-param", params, request.getServletContext());
		String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);
		
		return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
		
	}

}
