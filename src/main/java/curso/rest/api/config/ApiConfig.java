package curso.rest.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("apiconfig")
public class ApiConfig {

	private String secret;
	private String secretEmail;
	private String secretPassword;
	private String secretSmtp;

	public String getSecretSmtp() {
		return secretSmtp;
	}

	public void setSecretSmtp(String secretSmtp) {
		this.secretSmtp = secretSmtp;
	}

	public String getSecretEmail() {
		return secretEmail;
	}

	public void setSecretEmail(String secretEmail) {
		this.secretEmail = secretEmail;
	}

	public String getSecretPassword() {
		return secretPassword;
	}

	public void setSecretPassword(String secretPassword) {
		this.secretPassword = secretPassword;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

}
