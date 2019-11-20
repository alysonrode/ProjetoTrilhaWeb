package br.coldigogeladeiras.rest;

import com.google.gson.Gson;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import br.coldigogeladeiras.modelo.Marca;
import br.coldigogeladeiras.bd.Conexao;
import br.coldigogeladeiras.jdbc.JDBCMarcaDAO;

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

//o path é quando você precisa que se referencie a classe abaixo pela url, sempre que acionada o "marca" na url, ela será executada
@Path("marca")
public class MarcaRest extends UtilRest {
	@GET
	@Path("/buscar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscar() {
		try {
			// COLOCA UMA LISTA DE MARCAS COMO ATRIBUTO DA CLASS.
			List<Marca> listaMarcas = new ArrayList<Marca>();
			// CRIA UM OBJETO CONEC DA CLASSE CONEXÃO
			Conexao conec = new Conexao();
			// executa o método para abrir conexão
			Connection conexao = conec.abrirConexao();
			// cria um objeto da jdbcMarcaDAO
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			// faz uma busca nas marcas do banco de dados
			listaMarcas = jdbcMarca.buscar();
			// fecha conexão com o banco
			conec.fecharConexao();
			// cria resposta em json e retorna ela
			return this.buildResponse(listaMarcas);
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}

	@POST
	@Path("/inserir")
	@Consumes("application/*")
	public Response inserir(String marca) {
		marca = new Gson().fromJson(marca, String.class);
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);

		boolean igualdade = jdbcMarca.validaIguais(marca);
		if (igualdade) {
			boolean retorno = jdbcMarca.inserir(marca);
			if (retorno) {
				return this.buildResponse("Marca cadastrada com sucesso");
			} else {
				return this.buildErrorResponse("Erro ao cadastrar marca!");
			}
		} else {
			return this.buildErrorResponse("Marca já existente");
		}
	}

	@GET
	@Path("/buscarPorNome")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorNome(@QueryParam("valorBusca") String nome) {

		try {

			List<Marca> listaMarcas = new ArrayList<Marca>();

			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			listaMarcas = jdbcMarca.buscarPorNome(nome);
			conec.fecharConexao();

			return this.buildResponse(listaMarcas);

		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}

	}

	@DELETE
	@Path("/deletar/{id}")
	@Consumes("application/*")
	public Response deletar(@PathParam("id") int id) {
		try {
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();

			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			boolean integridade = jdbcMarca.verificaIntegridadeMarca(id);
			if (integridade == false) {
				boolean resposta = jdbcMarca.excluir(id);

				conec.fecharConexao();

				if (resposta) {
					return this.buildResponse("Marca excluída com sucesso!");
				} else {
					return this.buildErrorResponse("Falha ao excluir marca!");
				}
			} else {
				return this.buildErrorResponse("Há um produto registrado com essa marca.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}

	}

	@GET
	@Path("/buscarPorId/{id}")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorId(@PathParam("id") int id) {
		try {
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			Marca marca = new Marca();

			marca = jdbcMarca.buscarPorId(id);
			conec.fecharConexao();
			return this.buildResponse(marca);
		} catch (Exception e) {

			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());

		}
	}

	@PUT
	@Path("/editar")
	@Consumes("application/*")
	public Response editarMarca(String marcaParam) {
		Marca marca = new Gson().fromJson(marcaParam, Marca.class);
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		try {
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);

			boolean retorno = jdbcMarca.editarMarca(marca);
			conec.fecharConexao();
			if (retorno) {
				return this.buildResponse("Marca alterado com sucesso!");
			} else {
				return this.buildErrorResponse("Erro alterar a marcaAAAAAAAAAAAAH");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}

	@PUT
	@Path("/onoff")
	@Consumes("application/*")
	public Response onoff(String marcaStatus) {

		Marca marca = new Gson().fromJson(marcaStatus, Marca.class);

		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);

		try {

			String textStatus = "";
			if (marca.getStatus() == 0) {
				textStatus = "desativada";
			} else {
				textStatus = "ativada";
			}

			boolean integridade = jdbcMarca.verificaIntegridadeMarca(marca.getId());

			if (integridade == false) {
				boolean retorno = jdbcMarca.onoff(marca);
				if (retorno) {
					conec.fecharConexao();
					return this.buildResponse("Marca " + textStatus + " com sucesso!");

				} else {
					conec.fecharConexao();
					return this.buildErrorResponse("Não foi possível alterar o status da marca!");
				}
			} else {
				conec.fecharConexao();
				return this.buildErrorResponse("Há um produto cadastrado com essa marca, impossível inativá-la");
			}
		} catch (Exception e) {
			return this.buildErrorResponse(e.getMessage());
		}
	}

}
