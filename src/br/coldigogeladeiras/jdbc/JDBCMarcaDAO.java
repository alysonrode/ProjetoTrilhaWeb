package br.coldigogeladeiras.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import br.coldigogeladeiras.modelo.Marca;
import java.util.List;

import java.util.ArrayList;

import br.coldigogeladeiras.jdbcinterface.MarcaDAO;

public class JDBCMarcaDAO implements MarcaDAO {
	// Cria um objeto de conexão como parâmetro
	private Connection conexao;

	// Isso funciona como um método construtor, que basicamente recebe a conexão dda
	// classe MarcaRest, e atribui a nossa classe.

	public JDBCMarcaDAO(Connection conexao) {
		this.conexao = conexao;
	}

	// Cria um método que debe retornar uma lista de marcas
	public List<Marca> buscar() {

		// Criação da instrução SQL para busca de todas as marcas
		String comando = "SELECT * FROM marcas";

		// Criação de uma lista para armazenar os dados que foram buscados do banco
		List<Marca> listMarcas = new ArrayList<Marca>();

		// Criação de um objeto marca com um valor nulo
		Marca marca = null;

		// Retorna a lista para quem chamou o método

//Abertura do try
		try {

			// Uso da conexão do banco para prepará-lo para uma instrução SQL
			Statement stmt = conexao.createStatement();

			// Execução da instrução criada previamente
			// e armazenamento do resultado no objeto rs
			ResultSet rs = stmt.executeQuery(comando);

			// Enquanto houver um proximo item no ResultSet
			while (rs.next()) {

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
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return listMarcas;
	}

	public boolean inserir(String marca) {
		String comando = "INSERT INTO marcas VALUES (null, ?)";

		PreparedStatement p;
		try {
			p = this.conexao.prepareStatement(comando);
			p.setString(1, marca);
			p.execute();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public List<Marca> buscarPorNome(String nome) {
		String comando = "SELECT * FROM marcas ";
		if (!nome.equals("")) {
			comando += "where nome like '%" + nome + "%'";
		}
		comando += " ORDER BY nome ASC";

		List<Marca> listaMarcas = new ArrayList<Marca>();
		Marca marca = null;

		try {
			Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando);

			while (rs.next()) {
				marca = new Marca();
				marca.setId(rs.getInt("id"));
				marca.setNome(rs.getString("nome"));

				listaMarcas.add(marca);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listaMarcas;

	}

	public boolean excluir(int id) {
		String comando = "DELETE from marcas where id = ?";
		PreparedStatement p;
		try {
			p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			p.execute();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public Marca buscarPorId(int id) {
		String comando = "select * from marcas WHERE ID = ?";
		PreparedStatement p;
		Marca marca = new Marca();
		try {

			p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			
			if(rs.next()){
				marca.setId(id);
				marca.setNome(rs.getString("nome"));
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	return marca;
	}
	public boolean editarMarca(Marca marca) {
		String comando = "update marcas set nome = ? where id = ?";
		PreparedStatement p;
		try {
			p = this.conexao.prepareStatement(comando);
			p.setString(1, marca.getNome());
			p.setInt(2, marca.getId());
			p.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	return true;
	}
}

	
