package br.coldigogeladeiras.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import br.coldigogeladeiras.jdbcinterface.ProdutoDAO;
import br.coldigogeladeiras.modelo.Produto;


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
			//Caso aconteça algo de errado, será retornado um false, caso não, será retornado um true =)
		}catch (SQLException e){
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
			comando += "where modelo like '%'" + nome + "%";
				
		}
		//finaliza o comando ordenando alfebiticamente por 
		//categoria, marca e depois modelo.
		
		comando += "ORDER BY categoria ASC, marcas.nome ASC, modelo ASC";
		
		List<JsonObject> listaProdutos = new ArrayList<JsonObject>();
		JsonObject produto = null;
		
		try {
			Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando);
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String categoria = rs.getString("categoria");
				String modelo = rs.getString("modelo");
				int capacidade = rs.getInt("capacidade");
				float valor = rs.getFloat("valor");
				String marcaNome = rs.getString("marca");
				
				if(categoria.equals("1")) {
					categoria = "Geladeira";
				}else if (categoria.equals("2")) {
					categoria = "Freezar";
				}
				produto = new JsonObject();
				produto.addProperty("id", id);
				produto.addProperty("categoria", categoria);
				produto.addProperty("modelo", modelo);
				produto.addProperty("capacidade", capacidade);
				produto.addProperty("valor", valor);
				produto.addProperty("marcaNome", marcaNome);
			
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
	
}
