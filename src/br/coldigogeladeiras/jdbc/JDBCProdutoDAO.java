package br.coldigogeladeiras.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
