package view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import model.Certificacion;
import model.Contacto;
import model.Requisito;
import model.Solicitud;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import controller.LoginController;
import service.IService;
import support.Departamento;
import support.FacesUtil;
import support.Ifp;
import support.Municipio;
import util.ValidatorUtil;

@Component
@Scope(value = "view")
public class CertificacionesManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private IService service;
	@Autowired
	private LoginController login;
	private List<Certificacion> certificaciones;
	private Certificacion selectedCertificacion;
	private Contacto solicitante;
	private Solicitud solicitud;
	private Map<Integer, Municipio> municipios;
	private Map<Integer, Departamento> departamentos;
	private Integer selectedDepartamento;
	private Integer selectedMunicipio;

	public CertificacionesManagedBean() {
		super();
		solicitante = new Contacto();
		solicitud = new Solicitud();
		municipios = new HashMap<Integer, Municipio>();
		departamentos = new HashMap<Integer, Departamento>();
	}
	
	@PostConstruct
	private void init() {		
		certificaciones = service.getCertificaciones(login.getEntidadUsuario());
		departamentos = service.getDepartamentosByInatec();
	}
	
	public List<Departamento> getDepartamentos(){
		return new ArrayList<Departamento>(departamentos.values());
	}
	
	public Integer getSelectedDepartamento(){
		return selectedDepartamento;
	}

	public void setSelectedDepartamento(Integer departamento){
		this.selectedDepartamento = departamento;
	}
	
	public Integer getSelectedMunicipio(){
		return selectedMunicipio;
	}
	
	public void setSelectedMunicipio(Integer municipio){
		this.selectedMunicipio = municipio;
	}
	
	public void handleDepartamentoChange(){
		municipios = service.getMunicipioDptoByInatec(selectedDepartamento);
	}

	public List<Municipio> getMunicipios(){
		return new ArrayList<Municipio>(municipios.values());
	}
	
	public List<Certificacion> getCertificaciones(){
		return certificaciones;
	}
	
	public Certificacion getSelectedCertificacion(){
		return selectedCertificacion;
	}
	
	public void setSelectedCertificacion(Certificacion certificacion){
		this.selectedCertificacion = certificacion;
	}
	
	public Solicitud getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(Solicitud solicitud) {
		this.solicitud = solicitud;
	}

	public Contacto getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Contacto solicitante) {
		this.solicitante = solicitante;
	}
	
	public List<Requisito> getRequisitos(Long certificacionId){
		return service.getRequisitos(certificacionId);
	}

	public void onRowSelect(SelectEvent event) {
		setSelectedCertificacion((Certificacion) event.getObject());
    }
  
    public void onRowUnselect(UnselectEvent event) {
    }
    
	public SelectItem[] getListaCentros(){
		List<Ifp> centros = service.getIfpByInatec(login.getEntidadUsuario());
	
		SelectItem[] opciones = new SelectItem[centros.size()+1];
		opciones[0] = new SelectItem("","Seleccione");
		for(int i=0; i<centros.size(); i++)
			opciones[i+1] = new SelectItem(centros.get(i).getIfpNombre(),centros.get(i).getIfpNombre());
		return opciones;
	}
    
    public void nuevaSolicitud(){
		solicitante = new Contacto();
		solicitud = new Solicitud();
    }
	
	public String registrarSolicitud(Solicitud solicitud, Contacto solicitante){
		
		//validar si tiene solicitudes pendientes
		
		if(service.tieneSolicitudesPendientes(solicitante.getNumeroIdentificacion(), selectedCertificacion.getId())){
			FacesUtil.getMensaje("SCCL - Mensaje: ", "El candidato ya tiene una solicitud en proceso.", true);
			return null;
		}
		
		//validar la cedula del candidato
		
		if(ValidatorUtil.validateCedula(solicitante.getNumeroIdentificacion())){
			FacesUtil.getMensaje("SCCL - Mensaje: ", "La cedula es invalida.", true);
			return null;			
		}
		
		//validar fecha de nacimiento
		Date fecha = ValidatorUtil.obtenerFechaNacimientoDeCedula(solicitante.getNumeroIdentificacion());
		
		if(fecha != solicitante.getFechaNacimiento()){
			FacesUtil.getMensaje("SCCL - Mensaje: ", "La fecha de nacimiento es incorrecta.", true);
			return null;						
		}
		
		solicitud.setCertificacion(selectedCertificacion);
		solicitante.setDepartamentoId(selectedDepartamento);
		solicitante.setMunicipioId(selectedMunicipio);
		service.registrarSolicitud(solicitud, solicitante);
		return "/modulos/solicitudes/solicitudes?faces-redirect=true";
	}
}