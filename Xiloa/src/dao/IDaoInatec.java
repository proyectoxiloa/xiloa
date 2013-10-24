package dao;

import java.util.List;

import model.Contacto;
import model.Usuario;
import support.Ifp;
import support.UCompetencia;

public interface IDaoInatec {

	public int getIdRol(String usuario);
	public void agregarRol();
	public List<UCompetencia> getCertificacionesSinPlanificar();
	public Usuario getUsuario(String username);
	public List<Contacto> getContactosInatec();
	public Contacto generarContacto(String usuario);	
	public List<Ifp> getIfpInatec();
	
}