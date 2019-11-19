package br.coldigogeladeiras.jdbcinterface;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import br.coldigogeladeiras.modelo.Marca;
public interface MarcaDAO {
	public List<Marca> buscar();
	public boolean inserir(String marca);
	public List<Marca> buscarPorNome(String nome);
	public Marca buscarPorId(int id);
	public boolean editarMarca(Marca marca);
}
//Isso só vai servir como uma interface para a classe JDBCMarcaDAO.