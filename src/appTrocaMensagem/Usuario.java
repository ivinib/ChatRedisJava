/**
 * 
 */
package appTrocaMensagem;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @author vi_ni
 *
 */
public class Usuario {
	
	private String apelido;
	
	private String nome;
	
	private Date dataCadastro;
	
	public Usuario() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	/**
	 * @return the apelido
	 */
	public String getApelido() {
		return apelido;
	}



	/**
	 * @param apelido the apelido to set
	 */
	public void setApelido(String apelido) {
		this.apelido = apelido;
	}



	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}



	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}



	/**
	 * @return the dataCadastro
	 */
	public Date getDataCadastro() {
		return dataCadastro;
	}



	/**
	 * @param dataCadastro the dataCadastro to set
	 */
	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		
		String output = String.format("\nApelido: " + this.apelido);
		
		output += String.format("\nNome: " + this.nome);
		
		output += String.format("\nData do Cadastro: " + new SimpleDateFormat("dd/MM/yyyy").format(this.dataCadastro));
		
		return output;
		
	}

}
