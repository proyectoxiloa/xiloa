package view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import model.Contacto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import service.IService;

@Component
@Scope("session")  //@ViewScoped
public class CertificacionManagedBean {
	
	@Autowired
	private IService service;
	private String idCurso;
	private String nombreCertificacion;
	private String descripcionCertificacion;
	private float costo;
	private List<Contacto> contactos = new ArrayList<Contacto>();
	private Contacto[] selectedContactos;
	private Date fechaIniciaDivulgacion;
	private Date fechaFinalizaInscripcion;
	private Date fechaIniciaConvocatoria;
	private String idCentro;
	private String nombreCentro;
	private String direccionCentro;
	private Date fechaIniciaEvaluacion;
	private String estatus;
	
	public String nuevaCertificacion(){
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		setIdCurso(params.get("idCompetencia"));
		setNombreCertificacion(params.get("nombreCurso"));
		setCosto(Float.valueOf(params.get("costoCurso")));
		setDescripcionCertificacion(params.get("nombreCurso"));
		setIdCentro(params.get("idCentro"));
		setNombreCentro(params.get("nombreCentro"));
		setDireccionCentro(params.get("direccionCentro"));
		return "/modulos/planificacion/edicion_planificacion?faces-redirect=true";
	}
	public IService getService() {
		return service;
	}
	public void setService(IService service) {
		this.service = service;
	}
	public String getIdCurso() {
		return idCurso;
	}
	public void setIdCurso(String idCurso) {
		this.idCurso = idCurso;
	}
	public String getNombreCertificacion() {
		return nombreCertificacion;
	}
	public void setNombreCertificacion(String nombreCertificacion) {
		this.nombreCertificacion = nombreCertificacion;
	}
	public String getDescripcionCertificacion() {
		return descripcionCertificacion;
	}
	public void setDescripcionCertificacion(String descripcionCertificacion) {
		this.descripcionCertificacion = descripcionCertificacion;
	}
	public float getCosto() {
		return costo;
	}
	public void setCosto(float costo) {
		this.costo = costo;
	}
	public List<Contacto> getContactos() {
		contactos = service.getContactosInatec();
		return contactos;
	}
	public Contacto[] getSelectedContactos() {
		return selectedContactos;
	}
	public void setSelectedContactos(Contacto[] selectedContactos) {
		this.selectedContactos = selectedContactos;
	}
	public Date getFechaIniciaDivulgacion() {
		return fechaIniciaDivulgacion;
	}
	public void setFechaIniciaDivulgacion(Date fechaIniciaDivulgacion) {
		this.fechaIniciaDivulgacion = fechaIniciaDivulgacion;
	}
	public Date getFechaFinalizaInscripcion() {
		return fechaFinalizaInscripcion;
	}
	public void setFechaFinalizaInscripcion(Date fechaFinalizaInscripcion) {
		this.fechaFinalizaInscripcion = fechaFinalizaInscripcion;
	}
	public Date getFechaIniciaConvocatoria() {
		return fechaIniciaConvocatoria;
	}
	public void setFechaIniciaConvocatoria(Date fechaIniciaConvocatoria) {
		this.fechaIniciaConvocatoria = fechaIniciaConvocatoria;
	}
	public String getIdCentro() {
		return idCentro;
	}
	public void setIdCentro(String idCentro) {
		this.idCentro = idCentro;
	}
	public String getNombreCentro() {
		return nombreCentro;
	}
	public void setNombreCentro(String nombreCentro) {
		this.nombreCentro = nombreCentro;
	}
	public String getDireccionCentro() {
		return direccionCentro;
	}
	public void setDireccionCentro(String direccionCentro) {
		this.direccionCentro = direccionCentro;
	}
	public Date getFechaIniciaEvaluacion() {
		return fechaIniciaEvaluacion;
	}
	public void setFechaIniciaEvaluacion(Date fechaIniciaEvaluacion) {
		this.fechaIniciaEvaluacion = fechaIniciaEvaluacion;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public void guardar(){
		service.guardarCertificacion(
				getNombreCertificacion(),
				getDescripcionCertificacion(), 
				null, //new Date(), //fechaInicia,
				null, //new Date(), //fechaFinaliza,
				Integer.valueOf(getIdCentro()),
				getNombreCentro(),
				getDireccionCentro(), 
				null, //programador,
				new Date(), //getFechaIniciaDivulgacion(), 
				new Date(), //getFechaFinalizaDivulgacion(), 
				new Date(), //getFechaFinalizaInscripcion(),
				new Date(), //getFechaIniciaConvocatoria(),
				new Date(), //getFechaIniciaEvaluacion(),
				null, //creador,
				"N/D", //referencia, 
				0, //nivelCompetencia, 
				null, //requisitos, 
				null, //unidades, 
				null, //actividades, 
				null, //solicitudes,
				contactos,
				estatus);
	}
}