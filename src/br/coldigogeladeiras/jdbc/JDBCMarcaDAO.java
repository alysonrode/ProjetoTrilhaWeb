package br.coldigogeladeiras.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import br.coldigogeladeiras.modelo.Marca;
import java.util.List;
import java.util.ArrayList;

import br.coldigogeladeiras.jdbcinterface.MarcaDAO;

public class JDBCMarcaDAO implements MarcaDAO {
	//Cria um objeto de conexão como parâmetro
	private Connection conexao;
	//Isso funciona como um método construtor, que basicamente recebe a conexão dda classe MarcaRest, e atribui a nossa classe.
	public JDBCMarcaDAO(Connection conexao) {
		this.conexao = conexao; 
	}
	//Cria um método que debe retornar uma lista de marcas
	public List<Marca> buscar(){
		
//Criação da instrução SQL para busca de todas as marcas
	String comando = "SELECT * FROM marcas";
//Criação de uma lista para armazenar os dados que foram buscados do banco
	List<Marca> listMarcas = new ArrayList<Marca>();
//Criação de um objeto marca com um valor nulo
	Marca marca = null;
//Retorna a lista para quem chamou o método 

//Abertura do try
	try {
//Uso da conexão do banco para prepará-lo para uma instrução SQL
		Statement stmt = conexao.createStatement();
//Execução da instrução criada previamente
//e armazenamento do resultado no objeto rs
		ResultSet rs = stmt.executeQuery(comando);
//Enquanto houver um proximo item no ResultSet
		while(rs.next()) {
			
//			criação de uma nova instancia de marca
			
			marca = new Marca();

//			Recebimento dos dados pelos métodos get
			
			int id = rs.getInt("id");
			String nome = rs.getString("nome");

//			Setando no objeto marca os valores desejados
			
			marca.setId(id);
			marca.setNome(nome);
			
//			Adição da instância contida no objeto Marca na lista de Marcas
			
			listMarcas.add(marca);
		}
		
//abaixo, caso algo de errado ocorres, o catch irá trata-lo
	}catch (Exception ex) {
		ex.printStackTrace();
	}
	return listMarcas;
	}
}