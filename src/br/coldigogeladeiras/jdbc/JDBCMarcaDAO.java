package br.coldigogeladeiras.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import br.coldigogeladeiras.modelo.Marca;
import java.util.List;

import java.util.ArrayList;

import br.coldigogeladeiras.bd.Conexao;
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
		String comando = "SELECT * FROM marcas where status = 1";

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
				int status = rs.getInt("status");

//			Setando no objeto marca os valores desejados

				marca.setId(id);
				marca.setNome(nome);
				marca.setStatus(status);
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

		String comando = "INSERT INTO marcas VALUES (null, ?, 1)";

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
				marca.setStatus(rs.getInt("status"));

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

			if (rs.next()) {
				marca.setId(id);
				marca.setNome(rs.getString("nome"));
			}

		} catch (Exception e) {
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
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean verificaIntegridadeMarca(int id) {

		String comando = "SELECT COUNT(*) as count_produtos FROM produtos WHERE marcas_id = ?";
		PreparedStatement r;
		int contagem = 0;
		try {
			r = conexao.prepareStatement(comando);
			r.setInt(1, id);

			ResultSet rs = r.executeQuery();

			while (rs.next()) {
				contagem = rs.getInt("count_produtos");

				if (contagem > 0) {

					return true;

				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean verificaIntegridadeProduto(int id) {

		String comando = "SELECT COUNT(*) as count_produtos FROM marcas WHERE id = ?";
		PreparedStatement p;

		int contagem = 0;

		try {

			p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);

			ResultSet rs = p.executeQuery();

			while (rs.next()) {
				contagem = rs.getInt("count_produtos");
			}
			if (contagem > 0) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean onoff(Marca marca) {

		String comando = "UPDATE marcas set status = ? WHERE id = ? ";
		PreparedStatement p;

		try {

			p = this.conexao.prepareStatement(comando);
			p.setInt(1, marca.getStatus());
			p.setInt(2, marca.getId());

			p.execute();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean validaIguais(String elemento) {

		String segundoComando = "SELECT count(*) as count_marcas FROM marcas where nome like '" + elemento +"'";

		PreparedStatement r;
		int contagem = 0;
		try {
			r = this.conexao.prepareStatement(segundoComando);
			ResultSet rs = r.executeQuery();
			while (rs.next()) {
				contagem = rs.getInt("count_marcas");
			}
			if (contagem == 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
