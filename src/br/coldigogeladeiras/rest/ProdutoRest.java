package br.coldigogeladeiras.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.coldigogeladeiras.bd.Conexao;
import br.coldigogeladeiras.jdbc.JDBCMarcaDAO;
import br.coldigogeladeiras.jdbc.JDBCProdutoDAO;
import br.coldigogeladeiras.modelo.Produto;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
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
			
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			boolean integridade = jdbcMarca.verificaIntegridadeProduto(conexao, produto.getMarcaId());
			
			if (integridade) {
			
				//Aqui cria um objeto da classe JDBCProdutoDAO.
				JDBCProdutoDAO jdbcProduto = new JDBCProdutoDAO(conexao);
				
				//recebe o return da jdbc e coloca numa variável booleana.
				boolean retorno = jdbcProduto.inserir(produto);
				
				//cria uma string vazia
				String msg = "";
				
				//Se o retorno for = a true;
				conec.fecharConexao();
				if(retorno) {
					msg = "Produto cadastrado com sucesso";
					return this.buildResponse(msg);
				}else {
					msg = "Erro ao cadastrar produto";
					return this.buildResponse(msg);
				}
			}else {
				conec.fecharConexao();
				return this.buildErrorResponse("A marca que você tentou colocar não existe queridão");
			}
			//fecha a conexão com o banco
			
		
			
			//envia a mensagem para o HTML.
		}catch(Exception e){
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
		@GET
		@Path("/buscar")
		@Consumes("application/*")
		@Produces(MediaType.APPLICATION_JSON)
		public Response buscarPorNome(@QueryParam("valorBusca") String nome) {
		
			try {
				
				List<JsonObject> listaProdutos = new ArrayList<JsonObject>();
				
				Conexao conec = new Conexao();
				Connection conexao = conec.abrirConexao();
				JDBCProdutoDAO jdbcProduto = new JDBCProdutoDAO(conexao);
				listaProdutos = jdbcProduto.buscarPorNome(nome);
				conec.fecharConexao();
				
				return this.buildResponse(listaProdutos);
				
			}catch (Exception e){
				e.printStackTrace();
				return this.buildErrorResponse(e.getMessage());
			}
			
		}
		@DELETE
		@Path("/excluir/{id}")
		@Consumes("application/*")
		public Response excluir(@PathParam("id") int id) {
			try {
				Conexao conec = new Conexao();
				Connection conexao = conec.abrirConexao();
				JDBCProdutoDAO jdbcProduto = new JDBCProdutoDAO(conexao);
				
				boolean retorno = jdbcProduto.deletar(id);
				conec.fecharConexao();
				if(retorno) {
					return this.buildResponse("Produto excluído com sucesso");
				}else {
					return this.buildErrorResponse("Erro ao excluir produto");
				}
				
			}catch(Exception e) {
				e.printStackTrace();
				return this.buildErrorResponse(e.getMessage());
			}
		}
		@GET
		@Path("/buscarPorId")
		@Consumes("application/*")
		@Produces(MediaType.APPLICATION_JSON)
		public Response buscarPorId(@QueryParam("id") int id) {
			
			try {
				Produto produto = new Produto();
				Conexao conec = new Conexao();
				Connection conexao = conec.abrirConexao();
				JDBCProdutoDAO jdbcProduto = new JDBCProdutoDAO(conexao);
			
				produto = jdbcProduto.buscarPorId(id);
				
				conec.fecharConexao();
				return this.buildResponse(produto);
			
			}catch(Exception e) {
				e.printStackTrace();
				return this.buildErrorResponse(e.getMessage());
			}
			
			
		}
		@PUT
		@Path ("/alterar")
		@Consumes("application/*")
		public Response alterar(String produtoParam) {
			try {
				
				Produto produto = new Gson().fromJson(produtoParam, Produto.class);
				Conexao conec = new Conexao();
				Connection conexao = conec.abrirConexao();
				JDBCProdutoDAO jdbcProduto = new JDBCProdutoDAO(conexao);
				
				boolean retorno = jdbcProduto.alterar(produto);
				conec.fecharConexao();
				String msg = "";
				if(retorno) {
					msg = "Produto alterado com sucesso!";
					return this.buildResponse(msg);
				}else {
					msg = "Erro ao alterar produto";
					return this.buildErrorResponse(msg);
				}
			}catch(Exception e) {
				e.printStackTrace();
				return this.buildErrorResponse(e.getMessage());
			}
		}
}
