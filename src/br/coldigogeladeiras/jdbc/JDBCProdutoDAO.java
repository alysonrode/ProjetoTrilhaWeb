package br.coldigogeladeiras.jdbc;

import br.coldigogeladeiras.jdbcinterface.ProdutoDAO;
import br.coldigogeladeiras.modelo.Produto;
import com.google.gson.JsonObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JDBCProdutoDAO implements ProdutoDAO {

	private Connection conexao;

	public JDBCProdutoDAO(Connection conexao) {
		this.conexao = conexao;
	}
	
	public boolean inserir(Produto produto) {
		
		//Cria o comando no sql que será enviado pelo p.execute, colocando todos os parâmetros dentro de um objeto de p
		//puxando do produto.
		
		String comando = "INSERT INTO produtos (id, categoria, modelo, capacidade, valor, marcas_id) " 
		+ "VALUES (?,?,?,?,?,?)";
		
			PreparedStatement p;
		
			try {
				p = this.conexao.prepareStatement(comando);
			
				p.setInt(1, produto.getId());
				p.setString(2, produto.getCategoria());
				p.setString(3, produto.getModelo());
				p.setInt(4, produto.getCapacidade());
				p.setFloat(5, produto.getValor());
				p.setInt(6, produto.getMarcaId());
			
				p.execute();
			}
			//Caso aconteça algo de errado, será retornado um false, caso não, será retornado um true =)
		catch (SQLException e){
			e.printStackTrace();
			return false;
		}
		
		return true;
		}
	
	public List<JsonObject> buscarPorNome(String nome){
		
		//INICIA CRIAÇÃO DO COMANDO SQL
		String comando = "SELECT produtos.*, marcas.nome as marca FROM produtos " + "INNER JOIN marcas ON produtos.marcas_id = marcas.id ";
		//SE O NOME NÃO ESTIVER VAZIO
		if (!nome.equals("")) {
			
			//CONTATENA NO COMANDO O WHERE BUSCANDO NO NOME DO PRODUTO
			//O TEXTO DA VÁRIAVEL NOME
			comando += "where modelo like '%" + nome + "%'";
				
		}
		//finaliza o comando ordenando alfebiticamente por 
		//categoria, marca e depois modelo.
		
		comando += " ORDER BY categoria ASC, marcas.nome ASC, modelo ASC";
		
		List<JsonObject> listaProdutos = new ArrayList<JsonObject>();
		JsonObject produto = null;
		
		try {
			Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando);
			
			while(rs.next()) {
				produto = new JsonObject();
				produto.addProperty("id", rs.getInt("id"));
				produto.addProperty("modelo", rs.getString("modelo"));
				produto.addProperty("capacidade", rs.getInt("capacidade"));
				produto.addProperty("valor", rs.getFloat("valor"));
				produto.addProperty("marcaNome", rs.getString("marca"));
				
				if(rs.getString("categoria").equals("1")) {
					produto.addProperty("categoria", "Geladeira");
				}else if (rs.getString("categoria").equals("2")) {
					produto.addProperty("categoria", "Freezar");
				}
			
				listaProdutos.add(produto);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return listaProdutos;
	}
	
	public boolean deletar(int id) {
		
		String comando = "DELETE from produtos where id = ?";		
		PreparedStatement p;
		
		try {
			p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			p.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
		
		
	}
	
	public Produto buscarPorId(int id) {
		
		String comando = "SELECT * FROM produtos where id = ?";
		Produto produto = new Produto();
		try {
			PreparedStatement p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {
				String categoria = rs.getString("categoria");
				String modelo = rs.getString("modelo");
				int capacidade = rs.getInt("capacidade");
				float valor = rs.getFloat("valor");
				int marcaId = rs.getInt("marcas_id");
				
				produto.setId(id);
				produto.setCategoria(categoria);
				produto.setMarcaId(marcaId);
				produto.setModelo(modelo);
				produto.setValor(valor);
				produto.setCapacidade(capacidade);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return produto;
	}
	public boolean alterar(Produto produto) {
		String comando = "UPDATE produtos " + "SET categoria=?, modelo=?, capacidade=?, valor=?, marcas_id=?" + " WHERE id=?";
		PreparedStatement p;
		try {
			p = this.conexao.prepareStatement(comando);
			p.setString(1, produto.getCategoria());
			p.setString(2, produto.getModelo());
			p.setInt(3, produto.getCapacidade());
			p.setFloat(4, produto.getValor());
			p.setInt(5, produto.getMarcaId());
			p.setInt(6, produto.getId());
			p.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	return true;
	}
	public boolean validaIguais(String elemento) {

		String segundoComando = "SELECT count(*) as count_modelo FROM produtos where modelo like '" + elemento + "'";

		PreparedStatement r;
		int contagem = 0;
		try {
			r = this.conexao.prepareStatement(segundoComando);
			ResultSet rs = r.executeQuery();
			while (rs.next()) {
				contagem = rs.getInt("count_modelo");
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
