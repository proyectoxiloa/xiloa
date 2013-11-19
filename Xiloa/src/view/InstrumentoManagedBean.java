package view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import model.Guia;
import model.Instrumento;
import model.Mantenedor;
import model.Unidad;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import service.IService;

@Component
@Scope(value="session")
public class InstrumentoManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private IService service;
	
	@Autowired
	private transient UtilitariosManagedBean util;

	private Long idCertificacion;
	private String nombreCertificacion;
	private Instrumento instrumento;
	private Instrumento selectedInstrumento;
	private Guia guia;
	private Guia selectedGuia;
	private List<Instrumento> instrumentos;	
	private Map<Integer,Mantenedor> catalogoTiposInstrumento;
	private Integer selectedTipoInstrumento;
	private Map<Long,Unidad> catalogoUnidades;
	private Long selectedUnidad;

	public InstrumentoManagedBean(){
		super();
		instrumento = new Instrumento();
		instrumento.setEstatus(true);
		selectedInstrumento = new Instrumento();
		selectedInstrumento.setEstatus(true);
		guia = new Guia();
		guia.setEstatus(true);
		selectedGuia = new Guia();
		selectedGuia.equals(true);
		catalogoTiposInstrumento = new HashMap<Integer, Mantenedor>();
		selectedTipoInstrumento = null;
		catalogoUnidades = new HashMap<Long, Unidad>();
		selectedUnidad = null;
	}
	
	@PostConstruct
	private void init(){
		catalogoTiposInstrumento = util.getCatalogoTiposInstrumento();
		//catalogoTiposInstrumento = service.getMapMantenedoresByTipo("6");
	}
	
	public Long getIdCertificacion(){
		return idCertificacion;
	}
	
	public void setIdCertificacion(Long id){
		this.idCertificacion = id;
		System.out.println("setIdCertificacion: "+id);
	}
	
	public String getNombreCertificacion() {
		return nombreCertificacion;
	}
	
	public void setNombreCertificacion(String nombre){
		this.nombreCertificacion = nombre;
		System.out.println("setNombreCertificacion: "+nombre);
	}

	public Instrumento getInstrumento() {
		return instrumento;
	}

	public void setInstrumento(Instrumento instrumento) {
		this.instrumento = instrumento;
	}

	public Instrumento getSelectedInstrumento() {
		return selectedInstrumento;
	}

	public void setSelectedInstrumento(Instrumento instrumento) {
		this.selectedInstrumento = instrumento;
		Object [] objs =  new Object [] {instrumento.getId()};
		this.selectedInstrumento.setGuias(service.getGuiaByParam("Guia.findByIdInstrumento", objs));				
	}
		
	public Guia getGuia() {
		return guia;
	}

	public void setGuia(Guia guia) {
		this.guia = guia;
	}
	
	public Guia getSelectedGuia() {
		return selectedGuia;
	}

	public void setSelectedGuia(Guia guia) {
		this.selectedGuia = guia;
	}

	public List<Mantenedor> getCatalogoTiposInstrumento() {
		return new ArrayList<Mantenedor>(catalogoTiposInstrumento.values());
	}

	public void setCatalogoTiposInstrumento(List<Mantenedor> catalogoTiposInstrumento) {
		for(int i=0; i<catalogoTiposInstrumento.size(); i++){
			this.catalogoTiposInstrumento.put(catalogoTiposInstrumento.get(i).getId(), catalogoTiposInstrumento.get(i));
		}
	}

	public Integer getSelectedTipoInstrumento() {
		return selectedTipoInstrumento;
	}

	public void setSelectedTipoInstrumento(Integer selectedTipoInstrumento) {
		this.selectedTipoInstrumento = selectedTipoInstrumento;
	}

	public List<Unidad> getCatalogoUnidades() {
		return new ArrayList<Unidad>(catalogoUnidades.values());
	}

	public Long getSelectedUnidad() {
		return selectedUnidad;
	}

	public void setSelectedUnidad(Long selectedUnidad) {
		this.selectedUnidad = selectedUnidad;
	}
	
	public String configurarInstrumento(Long idCertificacion, String nombreCertificacion){
		
		this.idCertificacion = idCertificacion;
		this.nombreCertificacion = nombreCertificacion;
		
		System.out.println("configurarInstrumento id: "+idCertificacion);
		System.out.println("configurarInstrumento nombre: "+nombreCertificacion);
		
		List<Unidad> unidades = service.getUnidadesByCertificacionId(idCertificacion);
		for(int i=0; i<unidades.size(); i++)
			this.catalogoUnidades.put(unidades.get(i).getId(), unidades.get(i));
		this.instrumentos = service.getInstrumentosByCertificacionId(idCertificacion);
		
		System.out.println("configurarInstrumento unidades: "+unidades.size());
		System.out.println("configurarInstrumento nombre: "+instrumentos.size());

		return "/modulos/planificacion/instrumentos?faces-redirect=true";
	}
	
	/*
	public String configurarInstrumento(ActionEvent event){
		this.idCertificacion = 2L;
		this.nombreCertificacion = FacesUtil.getActionAttribute(event, "nombreCertificacion");
		
		System.out.println("configurarInstrumento id: "+idCertificacion);
		System.out.println("configurarInstrumento nombre: "+nombreCertificacion);
		
		List<Unidad> unidades = service.getUnidadesByCertificacionId(idCertificacion);
		for(int i=0; i<unidades.size(); i++)
			this.catalogoUnidades.put(unidades.get(i).getId(), unidades.get(i));
		this.instrumentos = service.getInstrumentosByCertificacionId(idCertificacion);
		return "/modulos/planificacion/instrumentos?faces-redirect=true";
	}*/
	
	public void guardarInstrumento(Instrumento instrumento){
		instrumento.setDescripcion(instrumento.getNombre());
		instrumento.setTipo(catalogoTiposInstrumento.get(selectedTipoInstrumento));
		instrumento.setUnidad(catalogoUnidades.get(selectedUnidad));
		setSelectedInstrumento((Instrumento) service.guardar(instrumento));
		this.instrumentos = service.getInstrumentosByCertificacionId(idCertificacion);
	}
		
	public void nuevoInstrumento(){
		instrumento = new Instrumento();
		instrumento.setEstatus(true);
		selectedTipoInstrumento = null;
		selectedUnidad = null;
	}
	
	public void editarInstrumento(Instrumento instrumento){
		this.setInstrumento(instrumento);
		setSelectedTipoInstrumento(instrumento.getTipo().getId());
		setSelectedUnidad(instrumento.getUnidad().getId());
	}

	public String aceptar(){
		selectedInstrumento = new Instrumento();
		selectedTipoInstrumento = null;
		selectedUnidad = null;
		return "/modulos/planificacion/planificacion?faces-redirect=true";
	}
	
	public void nuevaGuia(){
		guia = new Guia();
		guia.setEstatus(true);
		guia.setInstrumento(selectedInstrumento);
	}
	
	public void editarGuia(Guia guia){
		this.guia = guia;
	}
	
	public void guardarGuia(Guia guia){
		service.guardar(guia);
		setSelectedInstrumento(selectedInstrumento);
	}
	
	public List<Instrumento> getInstrumentos() {
		return instrumentos;
	}

	public void setInstrumentos(List<Instrumento> instrumentos) {
		this.instrumentos = instrumentos;
	}
		
	public void onRowSelect(SelectEvent event) {
		setSelectedInstrumento((Instrumento) event.getObject());
		setSelectedTipoInstrumento(selectedInstrumento.getTipo().getId());
		setSelectedUnidad(selectedInstrumento.getUnidad().getId());
    }
  
    public void onRowUnselect(UnselectEvent event) {
    }
}