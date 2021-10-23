package curso.rest.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import curso.rest.api.model.Profissao;
import curso.rest.api.model.Telefone;
import curso.rest.api.model.Usuario;

public class UsuarioDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String login;
	private String nome;
	private String cpf;
	private String senha;
	private String dataNascimento;
	private List<Telefone> telefones;
	private Profissao profissao;
	private BigDecimal salario;

	public UsuarioDTO(Usuario usuario) {
		this.id = usuario.getId();
		this.login = usuario.getLogin();
		this.nome = usuario.getNome();
		this.cpf = usuario.getCpf();
		this.senha = usuario.getSenha();
		this.telefones = usuario.getTelefones();
		this.profissao = usuario.getProfissao();
		this.salario = usuario.getSalario();
		this.dataNascimento = dataConversao(usuario.getDataNascimento());
	}

	private String dataConversao(Date usuarioData) {
		if (usuarioData != null) {
			Date dataUsuario = usuarioData;
			String data = new SimpleDateFormat("dd/MM/yyyy").format(dataUsuario);
			return data;

		}
		return null;
	}

	public BigDecimal getSalario() {
		return salario;
	}

	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}

	public Profissao getProfissao() {
		return profissao;
	}

	public void setProfissao(Profissao profissao) {
		this.profissao = profissao;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
