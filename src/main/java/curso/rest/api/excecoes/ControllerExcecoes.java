package curso.rest.api.excecoes;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@ControllerAdvice
public class ControllerExcecoes extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ Exception.class, RuntimeException.class, Throwable.class })
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String msgError = "";
		if (ex instanceof MethodArgumentNotValidException) {
			List<ObjectError> listError = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
			for (ObjectError objectError : listError) {
				msgError += objectError.getDefaultMessage() + "\n";
			}
		} else {
			msgError = ex.getMessage();
		}
		ObjetoErro objErro = new ObjetoErro();
		objErro.setError(msgError);
		objErro.setCode(status.value() + " ==> " + status.getReasonPhrase());

		return new ResponseEntity<>(objErro, headers, status);
	}

	@ExceptionHandler({ DataIntegrityViolationException.class, ConstraintViolationException.class, PSQLException.class,
			SQLException.class })
	protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex) {
		String msgEx = "";
		if (ex instanceof DataIntegrityViolationException) {
			msgEx = ((DataIntegrityViolationException) ex).getCause().getCause().getMessage();
		} else if (ex instanceof ConstraintViolationException) {
			msgEx = ((ConstraintViolationException) ex).getCause().getCause().getMessage();
		} else if (ex instanceof PSQLException) {
			msgEx = ((PSQLException) ex).getCause().getCause().getMessage();
		} else if (ex instanceof SQLException) {
			msgEx = ((SQLException) ex).getCause().getCause().getMessage();
		} else {
			msgEx = ex.getMessage();
		}
		ObjetoErro objtErro = new ObjetoErro();
		objtErro.setError(msgEx);
		objtErro.setCode(
				HttpStatus.INTERNAL_SERVER_ERROR + " ==> " + HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		return new ResponseEntity<>(objtErro, HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
