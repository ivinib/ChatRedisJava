package appTrocaMensagem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.edu.opet.Reader;
import redis.clients.jedis.Jedis;

/**
 * @author vi_ni
 *
 */
public class MainAppTrocaMensagem {
	
	static Usuario usuario;
	static Mensagem mensagem;
	static Jedis jedis = new Jedis("localhost");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Variables declaration

		final String MENU_INICIO = "\nApp Troca de Mensagem Redis" 
								 + "\n1. Cadastro" 
								 + "\n2. Entrar no sistema"
								 + "\n3. Sair do sistema" 
								 + "\nEscolha uma opção: ";

		final String MENU_USUARIO = "\nMenu usuário" 
								  + "\n1. Ver perfil" 
								  + "\n2. Mandar mensagem"
								  + "\n3. Ver mensagens enviadas" 
								  + "\n4. Ver mensagens recebidas" 
								  + "\n5. Logout"
								  + "\nEscolha uma opção: ";

		boolean flagSistema = false;

		boolean flagUsuario = false;

		boolean login = false;

		int opcaoInicio = 0;

		int opcaoUsuario = 0;

		Character resposta = null;

		System.out.println("\nCoxexão: " + jedis.ping());

		do {

			flagSistema = true;

			opcaoInicio = Reader.readInt(MENU_INICIO);

			switch (opcaoInicio) {
			case 1:// Cadastrar usuário

				usuario = new Usuario();

				cadastrarUsuario();

				break;

			case 2:// Entrar no sistema

				usuario = new Usuario();

				login = loginUsuario();

				if (login == true) {

					do {
						flagUsuario = true;

						opcaoUsuario = Reader.readInt(MENU_USUARIO);

						switch (opcaoUsuario) {
						case 1: // Ver usuário

							System.out.println(usuario.toString());

							break;

						case 2: // Mandar Mensagem
							
							mensagem = new Mensagem();
							
							mandarMensagem();
							
							System.out.println("\nMensagem enviada com sucesso");
							
							break;

						case 3: // Ver mensagens enviadas
							
							mensagensEnviadas();

							break;

						case 4: // Ver mensagens recebidas
							
							Character respostaMensagem;
							
							mensagensRecebidas();
							
							respostaMensagem = Reader.readCharacter("\nDeseja responder alguma mensagem  (S/N)? ");
							
							respostaMensagem = Character.toUpperCase(respostaMensagem.charValue());
							
							if(respostaMensagem == 'S') {
								mensagem = new Mensagem();
								
								mandarMensagem();
								
							}

							break;

						case 5: // Logout
							usuario = null;
								
							mensagem = null;
								
							flagUsuario = false;
			
							break;
							
						default:
							
						System.out.println("\nOpção Inválida");
								
							break;
						}
					} while (flagUsuario);

				}

				break;

			case 3:// Sair do sistema

				resposta = Reader.readCharacter("Deseja sair do sistema (S/N)? ");

				resposta = Character.toUpperCase(resposta.charValue());

				if (resposta == 'S') {
					flagSistema = false;
				}

				break;
				
			default:
				
				System.out.println("\nOpção Inválida");
					
				break;
			}

		} while (flagSistema);

		System.out.println("\n FIM DO PROGRAMA");

	}

	public static void cadastrarUsuario() {

		Long count = 0L;

		usuario.setApelido(Reader.readString("Digite um apelido: "));

		count = jedis.sadd("apelidos", usuario.getApelido());
		
		while (count != 1) {
			System.out.println("\nNão foi possivel cadastrar. O apelido informado já existe");
			
			usuario.setApelido(Reader.readString("Digite um apelido: "));

			count = jedis.sadd("apelidos", usuario.getApelido());
			
		}

		usuario.setNome(Reader.readString("Digite seu nome: "));

		jedis.hset(usuario.getApelido(), "nome", usuario.getNome());

		usuario.setDataCadastro(new Date());

		jedis.hset(usuario.getApelido(), "data_cadastro", new SimpleDateFormat("dd/MM/yyyy").format(usuario.getDataCadastro()));

		System.out.println("\nUsuario cadastrado com sucesso");
	}

	public static boolean loginUsuario() {

		boolean flagLogin = false;

		String apelidoLogin = null;

		apelidoLogin = Reader.readString("Digite seu apelido: ");

		if (jedis.sismember("apelidos", apelidoLogin)) {

				usuario.setApelido(apelidoLogin);

				usuario.setNome(jedis.hget(usuario.getApelido(), "nome"));

				try {

					usuario.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse(jedis.hget(usuario.getApelido(), "data_cadastro")));

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				flagLogin = true;
		}
		else {
			System.out.println("\nApelido não encontrado");
		}

		return flagLogin;
	}
	
	public static void mandarMensagem() {

		String destinatarios = null;

		String[] listaDestinatarios = null;

		mensagem.setIdMensagem(jedis.incr("id_mensagem"));
		
		mensagem.setCorpoMensagem(Reader.readString("Digite a mensagem que vai enviar: "));

		destinatarios = Reader.readString("Digite os destinatários da mensagem separados por ',' : ");

		mensagem.setRemetente(usuario.getApelido());

		mensagem.setDataEnvio(new Date());

		mensagem.setHoraEnvio(new Date());

		listaDestinatarios = destinatarios.split(",");

		for (int i = 0; i < listaDestinatarios.length; i++) {
			 /*System.out.println("\n p: " + listaDestinatarios[i]);*/

			mensagem.setDestinatario(listaDestinatarios[i]);

			jedis.sadd("mensagem_" + mensagem.getIdMensagem() + "_destinatario", mensagem.getDestinatario());

			jedis.rpush("mensagens_recebidas_" + mensagem.getDestinatario(), "mensagem_" + mensagem.getIdMensagem());

			jedis.rpush("mensagens_enviadas_" + usuario.getApelido(), "mensagem_" + mensagem.getIdMensagem());
			
			/*System.out.println(mensagem.toString());*/
		}

		jedis.hset("mensagem_" + mensagem.getIdMensagem(), "remetente", mensagem.getRemetente());

		jedis.hset("mensagem_" + mensagem.getIdMensagem(), "data_envio",new SimpleDateFormat("dd/MM/yyyy").format(mensagem.getDataEnvio()));

		jedis.hset("mensagem_" + mensagem.getIdMensagem(), "hora_envio",new SimpleDateFormat("hh:mm:ss").format(mensagem.getHoraEnvio()));

		jedis.hset("mensagem_" + mensagem.getIdMensagem(), "corpo_mensagem",mensagem.getCorpoMensagem());

	}
	
	public static void mensagensEnviadas() {
		
		int count = 0;
		
		long numeroMensagensEnviadas = 0L;
		
		List<String> mensagensEnviadas;
		
		numeroMensagensEnviadas = jedis.llen("mensagens_enviadas_" + usuario.getApelido());
		
		mensagensEnviadas = jedis.lrange("mensagens_enviadas_" + usuario.getApelido(), 0, numeroMensagensEnviadas);
		
		for(String mensagemEnviada: mensagensEnviadas){
			
			System.out.println("\nId: " + jedis.lrange("mensagens_enviadas_" + usuario.getApelido(), count, count));
			
			System.out.println("Destinatario: " + jedis.sinter(mensagemEnviada+"_destinatario"));
			
			System.out.println("Data de envio: " + jedis.hget(mensagemEnviada, "data_envio"));
			
			System.out.println("Hora de envio: " + jedis.hget(mensagemEnviada, "hora_envio"));
			
			System.out.println("Mensagem: " + jedis.hget(mensagemEnviada, "corpo_mensagem"));
			
			count++;
		}		
	}
	
	public static void mensagensRecebidas() {
		
		long numeroMensagensRecebidas = 0L;
		
		int count = 0;
		
		List<String> mensagensRecebidas;
		
		numeroMensagensRecebidas = jedis.llen("mensagens_recebidas_" + usuario.getApelido());
		
		mensagensRecebidas = jedis.lrange("mensagens_recebidas_" + usuario.getApelido(), 0, numeroMensagensRecebidas);
		
		for(String mensagemRecebida: mensagensRecebidas){
			
			System.out.println("\nId: " + jedis.lrange("mensagens_recebidas_" + usuario.getApelido(), count, count));
			
			System.out.println("Remetente: " + jedis.hget(mensagemRecebida, "remetente"));
			
			System.out.println("Data de envio: " + jedis.hget(mensagemRecebida, "data_envio"));
			
			System.out.println("Hora de envio: " + jedis.hget(mensagemRecebida, "hora_envio"));
			
			System.out.println("Mensagem: " + jedis.hget(mensagemRecebida, "corpo_mensagem"));
			
			count++;
			
		}		
	}
}
