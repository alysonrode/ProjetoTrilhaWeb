package br.coldigogeladeiras.rest;

import com.google.gson.Gson;

import br.coldigogeladeiras.bd.Conexao;
import br.coldigogeladeiras.jdbc.JDBCProdutoDAO;
import br.coldigogeladeiras.modelo.Produto;

import java.sql.Connection;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;


@Path("produto")
public class ProdutoRest extends UtilRest {
	
	@POST
	@Path("/inserir")
	@Consumes("application/*")
	public Response inserir(String produtoParam) {
		try {
			
			//Primeiramente criamos um objeto da classe Produto, e recebemos o objeto que veio do banco em jSon e re-convertemos, passando de parametro
			//do js e passando para classe java
			
			Produto produto = new Gson().fromJson(produtoParam, Produto.class);
			
			//como ja visto antes, ele cria uma conexão, e abre a mesma
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			
			//Aqui cria um objeto da classe JDBCProdutoDAO.
			JDBCProdutoDAO jdbcProduto = new JDBCProdutoDAO(conexao);
			
			//recebe o return da jdbc e coloca numa variável booleana.
			boolean retorno = jdbcProduto.inserir(produto);
			
			//cria uma string vazia
			String msg = "";
			
			//Se o retorno for = a true;
			if(retorno) {
				msg = "Produto cadastrado com sucesso";
			}else {
				msg = "Erro ao cadastrar produto";
			}
			
			//fecha a conexão com o banco
			
			conec.fecharConexao();
			
			//envia a mensagem para o HTML.
			return this.buildResponse(msg);
		}catch(Exception e){
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
}
