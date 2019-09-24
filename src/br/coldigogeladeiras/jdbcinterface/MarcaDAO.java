package br.coldigogeladeiras.jdbcinterface;

import java.util.List;
import br.coldigogeladeiras.modelo.Marca;
public interface MarcaDAO {
	public List<Marca> buscar();
}
//Isso só vai servir como uma interface para a classe JDBCMarcaDAO.