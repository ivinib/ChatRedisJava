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
public class Mensagem {
	
	private Long idMensagem;
	
	private String remetente;
	
	private String destinatario;
	
	private Date dataEnvio;
	
	private Date horaEnvio;
	
	private long resposta;
	
	private String corpoMensagem;
	
	public Mensagem() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the idMensagem
	 */
	public Long getIdMensagem() {
		return idMensagem;
	}

	/**
	 * @param idMensagem the idMensagem to set
	 */
	public void setIdMensagem(Long idMensagem) {
		this.idMensagem = idMensagem;
	}

	/**
	 * @return the remetente
	 */
	public String getRemetente() {
		return remetente;
	}

	/**
	 * @param remetente the remetente to set
	 */
	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}

	/**
	 * @return the destino
	 */
	public String getDestinatario() {
		return destinatario;
	}

	/**
	 * @param destinatario the destino to set
	 */
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	/**
	 * @return the dataEnvio
	 */
	public Date getDataEnvio() {
		return dataEnvio;
	}

	/**
	 * @param dataEnvio the dataEnvio to set
	 */
	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	/**
	 * @return the horaEnvio
	 */
	public Date getHoraEnvio() {
		return horaEnvio;
	}

	/**
	 * @param horaEnvio the horaEnvio to set
	 */
	public void setHoraEnvio(Date horaEnvio) {
		this.horaEnvio = horaEnvio;
	}

	/**
	 * @return the resposta
	 */
	public long getResposta() {
		return resposta;
	}

	/**
	 * @param resposta the resposta to set
	 */
	public void setResposta(long resposta) {
		this.resposta = resposta;
	}

	/**
	 * @return the corpoMensagem
	 */
	public String getCorpoMensagem() {
		return corpoMensagem;
	}

	/**
	 * @param corpoMensagem the corpoMensagem to set
	 */
	public void setCorpoMensagem(String corpoMensagem) {
		this.corpoMensagem = corpoMensagem;
	}
	
	@Override
	public String toString() {
		
		String output = String.format("\nId Mensagem: " + this.idMensagem);
		
		output += String.format("\nRemetente: " + this.remetente);
		
		output += String.format("\nDestinatario: " + this.destinatario);
		
		output += String.format("\nData do envio: " + new SimpleDateFormat("dd/MM/yyyy").format(this.dataEnvio));
		
		output += String.format("\nData do envio: " + new SimpleDateFormat("hh:mm:ss").format(this.horaEnvio));
		
		output += String.format("\nMensagem: " + this.corpoMensagem);
		
		return output;
		
		
	}

}
