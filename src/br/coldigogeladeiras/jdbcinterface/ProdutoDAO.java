package br.coldigogeladeiras.jdbcinterface;

import br.coldigogeladeiras.modelo.Produto;
import java.util.List;
import com.google.gson.JsonObject;

public interface ProdutoDAO {
	
	public boolean inserir(Produto produto);
	public List<JsonObject> buscarPorNome(String nome);
	public boolean deletar(int id);
	public Produto buscarPorId(int id);
	
}
