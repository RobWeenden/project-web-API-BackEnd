package curso.rest.api.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import curso.rest.api.config.ApiConfig;

@Service
public class EnvioEmailService {
	@Autowired
	private ApiConfig config;
	
	public void enviarEmail(String assunto, String emailDestino, String mensagem) throws Exception {
		
		Properties properties = new Properties();
		properties.put("mail.smtp.ssl.trust", "*");
		properties.put("mail.smtp.auth", "true");//Autorização
		properties.put("mail.smtp.starttls", "true");//Autenticação
		properties.put("mail.smtp.host", config.getSecretSmtp());//Servido do email
		properties.put("mail.smtp.port", "465");//porta do serviço
		properties.put("mail.smtp.socketFactory.port", "465");//porta para conectar o socket
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");//classe de conexao socket
		
		Session session = Session.getInstance(properties, new Authenticator() {
			
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(config.getSecretEmail(), config.getSecretPassword());
			}
			
		});
		
		InternetAddress[] toUser = InternetAddress.parse(emailDestino); 
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(config.getSecretEmail()));//quem está enviado, o responsaveis do projeto
		message.setRecipients(Message.RecipientType.TO, toUser);//Quem irá receber o email
		message.setSubject(assunto);//assunto do email
		message.setText(mensagem);//conteudo
		
		Transport.send(message);
		
	}
}
