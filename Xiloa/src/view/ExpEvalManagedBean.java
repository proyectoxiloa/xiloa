package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import model.Archivo;
import model.Contacto;
import model.Evaluacion;
import model.Instrumento;
import model.Laboral;
import model.Mantenedor;
import model.Pais;
import model.Solicitud;
import model.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;

import service.IService;
import support.BeanEvaluacion;
import support.Departamento;
import support.FacesUtil;
import support.Municipio;

@Component
@Scope(value="request")
public class ExpEvalManagedBean implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private IService service;	
	
	@Autowired
	private UtilitariosManagedBean utilitarios;
	
	private Solicitud solicitudExp;
	private Contacto contactoExp;
	
	private List<Laboral> listDatosLaborales;
	private List<Laboral> listDatosEstudios;
	private List<Laboral> listDatosCalificacion;
	private List<Laboral> listDatosCertificaciones;
	private List<Evaluacion> listEvaluaciones;
	private List<BeanEvaluacion> listBeanEval;
	private List<BeanEvaluacion> listBeanEvalFormacion;	
	
	private String telefonoInstitucion;	
	private String descripcionCargo;
	private Pais paisInstitucion;
	private Long paisIdLaboral;
	private String nombreInstitucion;
	private String nombreCargo;
	private Date fechaDesde;
	private Date fechaHasta;
	private String institucionDireccion;
	private Integer tipoLaboral;
	
	private Mantenedor estadoActual;
	private Mantenedor estadoSiguiente;	
	
	private Long idSeletedLaboral;
	private List<SelectItem> listTipoDatosLaborales;
	private List<SelectItem> listEstadosPortafolio;
	private Laboral selectedLaboral;
	private Laboral nuevoLaboral;
	
	private Evaluacion seletedEvaluacion;
	private BeanEvaluacion selectedBeanEvaluacion;
	
	private UploadedFile file;
	
	private Archivo archivoExp;
	private Long evalIdByArchivoExp;
	private List<Archivo> listPortafolio;
	private List<Archivo> listPortafolioContacto;
	
	private boolean disableSolicitarCertificacion;
	private boolean disablePortafolio;
	private boolean disabledUploadFile;
	private boolean indicaCVFull;
	private boolean disabledBtnActualizaContacto;
	private boolean disabledBtnAgregaLaborales;
	private boolean disabledBtnAgregaEvaluacion;
	private boolean disabledConcluido;
	
	private List<SelectItem> listEvalBySolicitud;	

	private Map<Integer, Municipio> catalogoMunicipiosByDepto;
		
	private List<SelectItem> listDeptos;
	private List<SelectItem> listMunicipioByDptos;
	private List<SelectItem> listPaises;
	private List<SelectItem> listGenero;
	
	private List<Archivo> listPortafolioLaboral;
	
	private Long selectedArchivoId;
	
	private String municipioIdSelected;
	
	private Integer departamentoIdSelected;
	
	private String nombreArchivoExp;
	private String versionArchivoExp;
	private String tipoArchivoExp;
	private String descripcionArchivoExp;
	private String nombreRealArchivoExp;
	private String sizeArchivoExp;
	
	private boolean disabledConvocar;
	private boolean disabledAsesorar;
	private boolean disabledCerrar;
	
	private Mantenedor generoContacto;
	
	private Municipio municipioContacto;
	
	private Departamento deptoContacto;
	
	private String tipoEstadosPortafolio;
	
	public ExpEvalManagedBean() {
		super();
		
		this.setDisableSolicitarCertificacion(true);
		this.setDisablePortafolio(true);
		this.setDisabledUploadFile(true);
		this.setIndicaCVFull(false);
		this.setDisabledBtnActualizaContacto(false);
		this.setDisabledBtnAgregaLaborales(false);
		this.setDisabledBtnAgregaEvaluacion(true);	
		this.setDisabledConcluido(false);
		
		listDatosLaborales = new ArrayList<Laboral> ();
		listDatosEstudios = new ArrayList<Laboral> ();
		listDatosCalificacion = new ArrayList<Laboral> ();
		listDatosCertificaciones = new ArrayList<Laboral> ();
		listEvaluaciones = new ArrayList<Evaluacion> ();
		listBeanEval = new ArrayList<BeanEvaluacion> ();
		listBeanEvalFormacion = new ArrayList<BeanEvaluacion> ();
		listPortafolioContacto = new ArrayList<Archivo> ();
		listPortafolioLaboral = new ArrayList<Archivo> ();
		
		listTipoDatosLaborales = new ArrayList<SelectItem> ();
		listEstadosPortafolio  = new ArrayList<SelectItem> ();
		
		listEvalBySolicitud = new ArrayList<SelectItem> ();	
					
		catalogoMunicipiosByDepto = new HashMap<Integer, Municipio>();
				
		listDeptos = new ArrayList<SelectItem> ();
		listMunicipioByDptos = new ArrayList<SelectItem> ();
		listPaises = new ArrayList<SelectItem> ();
		listGenero = new ArrayList<SelectItem> ();
				
		nuevoLaboral = new Laboral();		
		
	}	
	
	public boolean isDisabledConcluido() {
		return disabledConcluido;
	}

	public void setDisabledConcluido(boolean disabledConcluido) {
		this.disabledConcluido = disabledConcluido;
	}

	public String getTipoEstadosPortafolio() {
		return tipoEstadosPortafolio;
	}


	public void setTipoEstadosPortafolio(String tipoEstadosPortafolio) {
		this.tipoEstadosPortafolio = tipoEstadosPortafolio;
	}


	public Municipio getMunicipioContacto() {
		return municipioContacto;
	}


	public void setMunicipioContacto(Municipio municipioContacto) {
		this.municipioContacto = municipioContacto;
	}


	public Departamento getDeptoContacto() {
		return deptoContacto;
	}


	public void setDeptoContacto(Departamento deptoContacto) {
		this.deptoContacto = deptoContacto;
	}


	public Mantenedor getGeneroContacto() {
		return generoContacto;
	}


	public void setGeneroContacto(Mantenedor generoContacto) {
		this.generoContacto = generoContacto;
	}


	public boolean isDisabledConvocar() {
		return disabledConvocar;
	}


	public void setDisabledConvocar(boolean disabledConvocar) {
		this.disabledConvocar = disabledConvocar;
	}


	public boolean isDisabledAsesorar() {
		return disabledAsesorar;
	}


	public void setDisabledAsesorar(boolean disabledAsesorar) {
		this.disabledAsesorar = disabledAsesorar;
	}


	public boolean isDisabledCerrar() {
		return disabledCerrar;
	}


	public void setDisabledCerrar(boolean disabledCerrar) {
		this.disabledCerrar = disabledCerrar;
	}


	public String getNombreRealArchivoExp() {
		return nombreRealArchivoExp;
	}

	public void setNombreRealArchivoExp(String nombreRealArchivoExp) {
		this.nombreRealArchivoExp = nombreRealArchivoExp;
	}

	public String getSizeArchivoExp() {
		return sizeArchivoExp;
	}

	public void setSizeArchivoExp(String sizeArchivoExp) {
		this.sizeArchivoExp = sizeArchivoExp;
	}

	public String getNombreArchivoExp() {
		return nombreArchivoExp;
	}

	public void setNombreArchivoExp(String nombreArchivoExp) {
		this.nombreArchivoExp = nombreArchivoExp;
	}

	public String getVersionArchivoExp() {
		return versionArchivoExp;
	}

	public void setVersionArchivoExp(String versionArchivoExp) {
		this.versionArchivoExp = versionArchivoExp;
	}

	public String getTipoArchivoExp() {
		return tipoArchivoExp;
	}

	public void setTipoArchivoExp(String tipoArchivoExp) {
		this.tipoArchivoExp = tipoArchivoExp;
	}

	public String getDescripcionArchivoExp() {
		return descripcionArchivoExp;
	}

	public void setDescripcionArchivoExp(String descripcionArchivoExp) {
		this.descripcionArchivoExp = descripcionArchivoExp;
	}

	public List<SelectItem> getListGenero() {
		return listGenero;
	}

	public void setListGenero(List<SelectItem> listGenero) {
		this.listGenero = listGenero;
	}

	public Long getPaisIdLaboral() {
		return paisIdLaboral;
	}

	public void setPaisIdLaboral(Long paisIdLaboral) {
		this.paisIdLaboral = paisIdLaboral;
	}

	public List<SelectItem> getListPaises() {
		return listPaises;
	}

	public void setListPaises(List<SelectItem> listPaises) {
		this.listPaises = listPaises;
	}

	public String getMunicipioIdSelected() {
		return municipioIdSelected;
	}

	public void setMunicipioIdSelected(String municipioIdSelected) {
		this.municipioIdSelected = municipioIdSelected;
	}

	public Integer getDepartamentoIdSelected() {
		return departamentoIdSelected;
	}


	public void setDepartamentoIdSelected(Integer departamentoIdSelected) {
		this.departamentoIdSelected = departamentoIdSelected;
	}


	public Contacto getContactoExp() {
		return contactoExp;
	}


	public void setContactoExp(Contacto contactoExp) {
		this.contactoExp = contactoExp;
	}


	public List<SelectItem> getListEstadosPortafolio() {
		return listEstadosPortafolio;
	}

	public void setListEstadosPortafolio(List<SelectItem> listEstadosPortafolio) {
		this.listEstadosPortafolio = listEstadosPortafolio;
	}

	public boolean isDisabledBtnActualizaContacto() {		
		return disabledBtnActualizaContacto;
	}


	public void setDisabledBtnActualizaContacto(boolean disabledBtnActualizaContacto) {
		this.disabledBtnActualizaContacto = disabledBtnActualizaContacto;
	}


	public boolean isDisabledBtnAgregaLaborales() {		
		return disabledBtnAgregaLaborales;
	}


	public void setDisabledBtnAgregaLaborales(boolean disabledBtnAgregaLaborales) {
		this.disabledBtnAgregaLaborales = disabledBtnAgregaLaborales;
	}


	public boolean isDisabledBtnAgregaEvaluacion() {		
		return disabledBtnAgregaEvaluacion;
	}


	public void setDisabledBtnAgregaEvaluacion(boolean disabledBtnAgregaEvaluacion) {
		this.disabledBtnAgregaEvaluacion = disabledBtnAgregaEvaluacion;
	}


	public boolean isIndicaCVFull() {
		return indicaCVFull;
	}

	public void setIndicaCVFull(boolean indicaCVFull) {
		this.indicaCVFull = indicaCVFull;
	}

	public boolean isDisabledUploadFile() {
		return disabledUploadFile;
	}



	public void setDisabledUploadFile(boolean disabledUploadFile) {
		this.disabledUploadFile = disabledUploadFile;
	}



	public boolean isDisablePortafolio() {
		return disablePortafolio;
	}



	public void setDisablePortafolio(boolean disablePortafolio) {
		this.disablePortafolio = disablePortafolio;
	}


	public List<BeanEvaluacion> getListBeanEvalFormacion() {
		if (this.solicitudExp != null){			
			listBeanEvalFormacion = getListadoEvaluacionesByParam(this.solicitudExp, false);
		}
		return listBeanEvalFormacion;
	}

	public void setListBeanEvalFormacion(List<BeanEvaluacion> listBeanEvalFormacion) {
		this.listBeanEvalFormacion = listBeanEvalFormacion;
	}

	public List<Archivo> getListPortafolioContacto() {
		actualizaListaPortafolio (new Integer(2));		
		return listPortafolioContacto;
	}


	public void setListPortafolioContacto(List<Archivo> listPortafolioContacto) {
		this.listPortafolioContacto = listPortafolioContacto;
	}



	public Long getSelectedArchivoId() {
		return selectedArchivoId;
	}



	public void setSelectedArchivoId(Long selectedArchivoId) {
		this.selectedArchivoId = selectedArchivoId;
	}



	public List<Archivo> getListPortafolioLaboral() {		
		return listPortafolioLaboral;
	}



	public void setListPortafolioLaboral(List<Archivo> listPortafolioLaboral) {
		this.listPortafolioLaboral = listPortafolioLaboral;
	}



	public String getTelefonoInstitucion() {
		return telefonoInstitucion;
	}



	public void setTelefonoInstitucion(String telefonoInstitucion) {
		this.telefonoInstitucion = telefonoInstitucion;
	}



	public String getDescripcionCargo() {
		return descripcionCargo;
	}



	public void setDescripcionCargo(String descripcionCargo) {
		this.descripcionCargo = descripcionCargo;
	}



	public Pais getPaisInstitucion() {
		return paisInstitucion;
	}



	public void setPaisInstitucion(Pais paisInstitucion) {
		this.paisInstitucion = paisInstitucion;
	}

	public Laboral getNuevoLaboral() {
		return nuevoLaboral;
	}

	public void setNuevoLaboral(Laboral nuevoLaboral) {
		this.nuevoLaboral = nuevoLaboral;
	}

	public List<SelectItem> getListDeptos() {
		
		if (utilitarios.getCatalogoEstadoSolicitud().size() > 0){
			listDeptos.add(new SelectItem(null, "Seleccion un Departamento"));
			for (Departamento d : utilitarios.getCatalogoDepartamentos().values()){
				this.listDeptos.add(new SelectItem(d.getDpto_id(), d.getDpto_nombre()));
			}
		}
		
		return listDeptos;
	}


	public void setListDeptos(List<SelectItem> listDeptos) {
		this.listDeptos = listDeptos;
	}


	public List<SelectItem> getListMunicipioByDptos() {
		return listMunicipioByDptos;
	}


	public void setListMunicipioByDptos(List<SelectItem> listMunicipioByDptos) {
		this.listMunicipioByDptos = listMunicipioByDptos;
	}

	public Map<Integer, Municipio> getCatalogoMunicipiosByDepto() {
		return catalogoMunicipiosByDepto;
	}

	public void setCatalogoMunicipiosByDepto(Map<Integer, Municipio> catalogoMunicipiosByDepto) {
		this.catalogoMunicipiosByDepto = catalogoMunicipiosByDepto;
	}



	public boolean isDisableSolicitarCertificacion() {
		return disableSolicitarCertificacion;
	}

	public void setDisableSolicitarCertificacion(
			boolean disableSolicitarCertificacion) {
		this.disableSolicitarCertificacion = disableSolicitarCertificacion;
	}

	public Solicitud getSolicitudExp() {		
		return solicitudExp;
	}

	public void setSolicitudExp(Solicitud solicitudExp) {
		this.solicitudExp = solicitudExp;
	}

	public List<Laboral> getListDatosLaborales() {
		if (this.solicitudExp != null){
			this.listDatosLaborales = service.getListLaboralByTipo(new Integer(13), this.solicitudExp.getContacto());
		}
		return this.listDatosLaborales;
	}

	public void setListDatosLaborales(List<Laboral> listDatosLaborales) {
		this.listDatosLaborales = listDatosLaborales;
	}

	public List<Laboral> getListDatosEstudios() {
		if (this.solicitudExp != null){
			this.listDatosEstudios = service.getListLaboralByTipo(new Integer(14), this.solicitudExp.getContacto());
		}
		return listDatosEstudios;
	}

	public void setListDatosEstudios(List<Laboral> listDatosEstudios) {
		this.listDatosEstudios = listDatosEstudios;
	}

	public List<Laboral> getListDatosCalificacion() {
		if (this.solicitudExp != null){
			this.listDatosCalificacion = service.getListLaboralByTipo(new Integer(15), this.solicitudExp.getContacto());
		}
		return listDatosCalificacion;
	}

	public void setListDatosCalificacion(List<Laboral> listDatosCalificacion) {
		this.listDatosCalificacion = listDatosCalificacion;
	}

	public List<Laboral> getListDatosCertificaciones() {
		if (this.solicitudExp != null){
			this.listDatosCertificaciones = service.getListLaboralByTipo(new Integer(16), this.solicitudExp.getContacto());
		}
		return listDatosCertificaciones;
	}

	public void setListDatosCertificaciones(List<Laboral> listDatosCertificaciones) {
		this.listDatosCertificaciones = listDatosCertificaciones;
	}

	public List<Evaluacion> getListEvaluaciones() {
		if (this.solicitudExp != null){
			listEvaluaciones = service.getEvaluaciones(this.solicitudExp);
		}
		return listEvaluaciones;
	}

	public void setListEvaluaciones(List<Evaluacion> listEvaluaciones) {
		this.listEvaluaciones = listEvaluaciones;
	}

	public List<BeanEvaluacion> getListBeanEval() {
		if (this.solicitudExp != null){
			listBeanEval = getListadoEvaluacionesByParam(this.solicitudExp, true);
		}
		return listBeanEval;
	}		

	public void setListBeanEval(List<BeanEvaluacion> listBeanEval) {
		this.listBeanEval = listBeanEval;
	}

	public String getNombreInstitucion() {
		return nombreInstitucion;
	}

	public void setNombreInstitucion(String nombreInstitucion) {
		this.nombreInstitucion = nombreInstitucion;
	}

	public String getNombreCargo() {
		return nombreCargo;
	}

	public void setNombreCargo(String nombreCargo) {
		this.nombreCargo = nombreCargo;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public String getInstitucionDireccion() {
		return institucionDireccion;
	}

	public void setInstitucionDireccion(String institucionDireccion) {
		this.institucionDireccion = institucionDireccion;
	}

	public Integer getTipoLaboral() {
		return tipoLaboral;
	}

	public void setTipoLaboral(Integer tipoLaboral) {
		this.tipoLaboral = tipoLaboral;
	}		
		
	public Mantenedor getEstadoActual() {
		if (this.solicitudExp != null){				
			estadoActual = this.solicitudExp.getEstatus();
		}
		return estadoActual;
	}

	public void setEstadoActual(Mantenedor estadoActual) {
		this.estadoActual = estadoActual;
	}

	public Mantenedor getEstadoSiguiente() {
		Integer    proxKey = null;
		Mantenedor actualEstado = null;
		Mantenedor proximoEstado = null;
				
		if (this.solicitudExp != null){
			
			actualEstado = this.getSolicitudExp().getEstatus();
			
			proxKey = Integer.valueOf(actualEstado.getProximo());
			
			if (proxKey != null){				
				proximoEstado = utilitarios.getCatalogoEstadoSolicitud().get(proxKey);
									
				estadoSiguiente = (proximoEstado == null) ? actualEstado : proximoEstado;								
			} else {
				estadoSiguiente = actualEstado;
			}				
		}
		return estadoSiguiente;
	}

	public void setEstadoSiguiente(Mantenedor estadoSiguiente) {
		this.estadoSiguiente = estadoSiguiente;
	}
	
	public Long getIdSeletedLaboral() {
		return idSeletedLaboral;
	}

	public void setIdSeletedLaboral(Long idSeletedLaboral) {
		this.idSeletedLaboral = idSeletedLaboral;
	}

	public List<SelectItem> getListTipoDatosLaborales() {
		return listTipoDatosLaborales;
	}

	public void setListTipoDatosLaborales(List<SelectItem> listTipoDatosLaborales) {
		this.listTipoDatosLaborales = listTipoDatosLaborales;
	}
	
	public Laboral getSelectedLaboral() {
		return selectedLaboral;
	}

	public void setSelectedLaboral(Laboral selectedLaboral) {
		this.selectedLaboral = selectedLaboral;
	}
	
	public Evaluacion getSeletedEvaluacion() {
		return seletedEvaluacion;
	}

	public void setSeletedEvaluacion(Evaluacion seletedEvaluacion) {
		this.seletedEvaluacion = seletedEvaluacion;
	}

	public BeanEvaluacion getSelectedBeanEvaluacion() {
		return selectedBeanEvaluacion;
	}

	public void setSelectedBeanEvaluacion(BeanEvaluacion selectedBeanEvaluacion) {
		this.selectedBeanEvaluacion = selectedBeanEvaluacion;
	}
	
	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public Archivo getArchivoExp() {
		return archivoExp;
	}

	public void setArchivoExp(Archivo archivoExp) {
		this.archivoExp = archivoExp;
	}

	public List<Archivo> getListPortafolio() {
		return listPortafolio;
	}

	public void setListPortafolio(List<Archivo> listPortafolio) {
		this.listPortafolio = listPortafolio;
	}

	public List<SelectItem> getListEvalBySolicitud() {		
		return listEvalBySolicitud;
	}

	public void setListEvalBySolicitud(List<SelectItem> listEvalBySolicitud) {
		this.listEvalBySolicitud = listEvalBySolicitud;
	}	

	public Long getEvalIdByArchivoExp() {
		return evalIdByArchivoExp;
	}

	public void setEvalIdByArchivoExp(Long evalIdByArchivoExp) {
		this.evalIdByArchivoExp = evalIdByArchivoExp;
	}

	@PostConstruct
	private void initBean(){        
							
		for (Mantenedor dato : utilitarios.getCatalogoTiposDatosLaborales().values()) {			
			this.listTipoDatosLaborales.add(new SelectItem(dato.getId(), dato.getValor()));			
		}	
						
		listEstadosPortafolio = new ArrayList<SelectItem> ();
		for (Mantenedor dato : utilitarios.getCatalogoPortafolio().values()) {
			this.listEstadosPortafolio.add(new SelectItem(dato.getId(), dato.getValor()));
		}
				
		List<Pais> paises = service.getPaises();
		this.listPaises = new ArrayList<SelectItem> ();
		this.listPaises.add(new SelectItem(null, "Seleccion un pais"));
		for (Pais p : paises){
			this.listPaises.add(new SelectItem(p.getId(), p.getNombre()));
		}
						
		listGenero.add(new SelectItem(null, "Indique el Genero"));
							
		for (Mantenedor dato : utilitarios.getCatalogoGenero().values()){
			listGenero.add(new SelectItem(dato.getId(), dato.getValor()));			
		}		
				
		archivoExp = new Archivo();
				
	}
	
	@Autowired
	public void iniciaBeanExpEval (){
		
		Solicitud  solicitud = (Solicitud)FacesUtil.getParametroSession("dbSolicitudesBean");				
				
		if (solicitud != null){		
						
			this.solicitudExp = solicitud;
									
			this.contactoExp = this.solicitudExp.getContacto();
			
			if (this.contactoExp != null){
				
				if (this.contactoExp.getSexo() != null) 
					this.generoContacto = utilitarios.getCatalogoGenero().get(this.contactoExp.getSexo());					
				else 
					this.generoContacto = null; 
				 
				if (this.contactoExp.getDepartamentoId() != null){
					this.departamentoIdSelected = this.contactoExp.getDepartamentoId();					
					handleMunicipios();					
					this.setDeptoContacto(utilitarios.getCatalogoDepartamentos().get(this.contactoExp.getDepartamentoId()));							
				} else{
					this.setDeptoContacto(null);
					
					this.departamentoIdSelected = null;					
					this.listMunicipioByDptos = new ArrayList<SelectItem>();
					this.listMunicipioByDptos.add(new SelectItem(null, "Seleccion un Municipio"));
				}
				
				if (this.contactoExp.getMunicipioId() != null){
					this.municipioIdSelected = String.valueOf(this.contactoExp.getMunicipioId());
					this.setMunicipioContacto(this.catalogoMunicipiosByDepto.get(this.contactoExp.getMunicipioId()));					
				} else {
					this.setMunicipioContacto(null);
					this.municipioIdSelected = null;					
				}
								
			}
														
			//Estado Actual
			this.setEstadoActual(this.solicitudExp.getEstatus());
			
			enabledDisableButton(1);
			enabledDisableButton(2);
			enabledDisableButton(3);
			enabledDisableButton(4);
			
		}		
		
		//((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession(false).setAttribute("dbSolicitudesBean",null);
	}
	
	public List<BeanEvaluacion> getListadoEvaluacionesByParam(Solicitud sol, boolean todos) {
		
		List<BeanEvaluacion> listBeanEv = new ArrayList<BeanEvaluacion> ();		
		
		List<Evaluacion> listEval = service.getEvaluaciones(sol);
		
		Mantenedor estadoEval;
		String     unidadDescripcion = "";
		
		for (Evaluacion e : listEval) {
			
			estadoEval = e.getEstado(); 
			
			unidadDescripcion = this.utilitarios.getCompetenciaDescripcion(e.getUnidad());
					
			List<Instrumento> listInstrumento = service.getIntrumentoByEvaluacion(e.getId());
			for (Instrumento inst : listInstrumento) {				
				BeanEvaluacion bean = new BeanEvaluacion (sol, //Solicitud, 
						  								  e, //	Evaluacion
						  								  inst,// Instrumento
						  								  estadoEval, // EstadoEvaluacion
						  								  unidadDescripcion // UnidadCompentenciaDescripcion
						  								  );
				
				if (todos == true) {
					listBeanEv.add(bean);
				} else {
					if (e.isAprobado() == false){
						listBeanEv.add(bean);
					} 
				}	
			}
		}			
		
		return listBeanEv;
	}
	
	public void handleActivaUpload(){
		if (archivoExp != null) {
			if ((archivoExp.getNombre() != null) && (archivoExp.getTipo() != null) && (archivoExp.getVersion() != null) && (archivoExp.getDescripcion() != null)){
				this.setDisabledUploadFile(false);
			}
		}
	}
	
	public void handleMunicipios() {
		System.out.println("Entra a handleMunicipio");
		Contacto cExp;
		this.municipioIdSelected = "1";
		
		if (this.contactoExp == null)
			cExp = this.getSolicitudExp().getContacto();
		else
			cExp = this.contactoExp;
		
		if (cExp != null) {
						
			this.catalogoMunicipiosByDepto = service.getMunicipioDptoByInatec(cExp.getDepartamentoId());			
			
			if (this.catalogoMunicipiosByDepto.size() > 0 ) {
				
				Integer idValor;
				Municipio valor;
				
				Iterator<Integer> claveSet = this.catalogoMunicipiosByDepto.keySet().iterator();
				
				this.listMunicipioByDptos = new ArrayList<SelectItem> ();
				
				this.listMunicipioByDptos.add(new SelectItem(null, "Seleccione un Municipio"));
				
			    while(claveSet.hasNext()){
			    	System.out.println("Crea la lista de Municipios");			    	
			    	idValor = claveSet.next();
			    	valor = this.catalogoMunicipiosByDepto.get(idValor);
			    	System.out.println("IdMunicipio: " + valor.getMunicipio_id());
			    	System.out.println("Descripcion " + valor.getMunicipio_nombre());
			    	this.listMunicipioByDptos.add(new SelectItem(valor.getMunicipio_id(), valor.getMunicipio_nombre()));			    			    			        		        
			    }
			}
			
			
		}
	}
	
	public void editarLaboral(){
				
		this.setNombreInstitucion(this.selectedLaboral.getInstitucion());
		this.setInstitucionDireccion(this.selectedLaboral.getInstitucionDireccion());
		this.setTipoLaboral(this.selectedLaboral.getTipo());
		this.setNombreCargo(this.selectedLaboral.getCargo());
		this.setFechaDesde(this.selectedLaboral.getFechaInicia());
		this.setFechaHasta(this.selectedLaboral.getFechaFinaliza());
		this.setIdSeletedLaboral(this.selectedLaboral.getId());
		this.setTelefonoInstitucion(this.selectedLaboral.getInstitucionTelefono());
		this.setPaisInstitucion(this.selectedLaboral.getPais());
		this.setDescripcionCargo(this.selectedLaboral.getDescripcion());
		if (this.selectedLaboral.getPais() != null)
			this.setPaisIdLaboral(this.selectedLaboral.getPais().getId());
		else
			this.setPaisIdLaboral(null);

		FacesUtil.setParamBySession("idSelectedLaboral", this.selectedLaboral.getId());		
		
		//enabledDisableButton(2);
		
		actualizaListaPortafolio (new Integer(1));		
				
	}
		
	public void nuevoLaboral() {		
		limpiarCampos();		
	}
	
	public void rowEditListenerPortafolio(RowEditEvent event){
		System.out.println("El estado seleccionado es: " + (String)event.getObject());
		System.out.println("Archivo Seleccionado " + selectedArchivoId);
		
	}
	
	public String editarEvaluacion(BeanEvaluacion beanEval) {		
		Evaluacion eval = null;
		
		this.setSelectedBeanEvaluacion(beanEval);
		
		FacesUtil.setParamBySession("solicitudEval", this.selectedBeanEvaluacion.getSolicitudBeanEval());		
		FacesUtil.setParamBySession("selectedInstrumento", this.selectedBeanEvaluacion.getInstrumento());
		
		eval = this.selectedBeanEvaluacion.getEvaluacion();
		
		if (eval != null){
			FacesUtil.setParamBySession("selectedEvaluacion", eval);					
			FacesUtil.setParamBySession("selectedUnidad", eval.getUnidad());
		} else {
			FacesUtil.setParamBySession("selectedEvaluacion", null);
			FacesUtil.setParamBySession("selectedUnidad", null);
		}
		
		return "/modulos/solicitudes/registro_evaluacion?faces-redirect=true";		
	}	
	
	public String registrarEvaluacion() {		
				
		FacesUtil.setParamBySession("solicitudEval", this.solicitudExp);		
		FacesUtil.setParamBySession("selectedInstrumento", null);		
		FacesUtil.setParamBySession("selectedEvaluacion", null);
		FacesUtil.setParamBySession("selectedUnidad", null);
				
		return "/modulos/solicitudes/registro_evaluacion?faces-redirect=true";		
	}	
	
	public void solicitarCertificacion (){
		
		//Integer estadoActual = new Integer(this.getSolicitudExp().getEstatus().getId());
		Mantenedor estadoActual = this.getSolicitudExp().getEstatus();
		Integer proxEstado = Integer.valueOf(estadoActual.getProximo()); //this.catalogoEstadosSolicitud.get(estadoActual).getProximo();
		
		Mantenedor proximoEstado = utilitarios.getCatalogoEstadoSolicitud().get(proxEstado);
						
		if (proxEstado != null)
			this.solicitudExp.setEstatus(proximoEstado);		
		
		Solicitud sol = (Solicitud) service.guardar(this.solicitudExp);
		
		if (sol != null){
			this.setSolicitudExp(sol);
			// habilita y desabilita los botones
			enabledDisableButton(1);
			enabledDisableButton(3);
			enabledDisableButton(4);
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "SCCL - Mensaje: ", "La solicitud a sido registrada exitosamente !!"));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SCCL - Mensaje: ", "Error al registrar la solicitud. Favor revisar..."));
		}
		
		
	}
				
	public void actualizarContacto() {
		System.out.println("correo " + solicitudExp.getContacto().getCorreo1());
		
		if (solicitudExp.getContacto().getDireccionActual() == null) {
			solicitudExp.getContacto().setDireccionActual("");			
		}
		if (solicitudExp.getContacto().getCorreo1() == null) {
			solicitudExp.getContacto().setCorreo1("");			
		}		
		if (solicitudExp.getContacto().getTelefono1() == null) {
			solicitudExp.getContacto().setTelefono1("");			
		}
		if (solicitudExp.getContacto().getTelefono2() == null) {
			solicitudExp.getContacto().setTelefono2("");			
		}
		
		Contacto contactoExp = (Contacto)service.guardar(solicitudExp.getContacto());
		
		if (contactoExp != null)
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "SCCL - Mensaje: ", "Los cambios ha sido aplicados exitosamente !!"));
		else
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SCCL - Mensaje: ", "Error al actualizar los datos del contacto..."));
	      
	}

	public void guardarDatosLaborales() {
		
		System.out.println("Entra al metodo guardarDatosLaborales");
		
		String nombreCargoLaboral;
		
		this.idSeletedLaboral = (Long)FacesUtil.getParametroSession("idSelectedLaboral");				
		
		FacesUtil.setParamBySession("idSelectedLaboral", null);
		
		nombreCargoLaboral = this.nombreCargo.toUpperCase() + " /  " + this.nombreInstitucion.toUpperCase();
		
		if (this.idSeletedLaboral == null) {
			
			if (this.paisIdLaboral != null){
				Object [] objs =  new Object [] {this.paisIdLaboral};
				paisInstitucion = service.getPaisByNQParam("Pais.findById", objs);
			} 
				
						
			this.selectedLaboral = new Laboral (this.solicitudExp.getContacto(), // contacto, 
									   this.tipoLaboral, // tipo, 
									   nombreCargoLaboral.toUpperCase(), // nombre,
									   this.descripcionCargo, // descripcion, 
									   this.nombreInstitucion.toUpperCase(), // institucion, 
									   this.paisInstitucion, // pais,
									   this.fechaDesde, // fechaInicia, 
									   this.fechaHasta, // fechaFinaliza, 
									   this.institucionDireccion.toUpperCase(), // institucionDireccion,
									   this.telefonoInstitucion, // institucionTelefono, 
									   this.nombreCargo.toUpperCase(), // cargo,
									   null // archivo
										);		
			
		} else {
			this.selectedLaboral = service.getLaboralById(this.idSeletedLaboral);
						
			this.selectedLaboral.setInstitucion(this.nombreInstitucion.toUpperCase());
			this.selectedLaboral.setInstitucionDireccion(this.institucionDireccion.toUpperCase());		
			this.selectedLaboral.setCargo(this.nombreCargo.toUpperCase());		
			this.selectedLaboral.setFechaInicia(this.fechaDesde);
			this.selectedLaboral.setFechaFinaliza(this.fechaHasta);	
			this.selectedLaboral.setDescripcion(this.descripcionCargo.toUpperCase());
			this.selectedLaboral.setNombre(nombreCargoLaboral.toUpperCase());
			this.selectedLaboral.setInstitucionTelefono(this.telefonoInstitucion.toUpperCase());
			this.selectedLaboral.setPais(this.paisInstitucion);			
		}
		this.selectedLaboral = (Laboral)service.guardar(this.selectedLaboral);
		
		if (this.selectedLaboral != null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "SCCL - Mensaje: ", "Los cambios ha sido aplicados exitosamente !!"));			
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SCCL - Mensaje: ", "Se genero un error al grabar los datos laborales / academicos. Favor revisar..."));
		}
		
		enabledDisableButton(2);		
	}
	
	public void enabledDisableButton(int opcion) {
		
		Solicitud sol = this.getSolicitudExp();
		
		Integer inicialKey, convocaKey, asesoraKey, siguienteKey, evaluarKey;
		
		Mantenedor inicialEstado, convocaEstado, asesoraEstado, siguienteEstado, evaluarEstado;				
		
		if (sol != null){		
			
			inicialEstado = service.getMantenedorMinByTipo(sol.getTipomantenedorestado());
			inicialKey = inicialEstado.getId();
			
			convocaKey = Integer.valueOf(inicialEstado.getProximo());
			
			convocaEstado = utilitarios.getCatalogoEstadoSolicitud().get(convocaKey);
								
			asesoraKey = Integer.valueOf(convocaEstado.getProximo());
			
			asesoraEstado = utilitarios.getCatalogoEstadoSolicitud().get(asesoraKey); 
								
			evaluarEstado = sol.getEstatus();
			
			evaluarKey = evaluarEstado.getId();
			
			siguienteKey = Integer.valueOf(evaluarEstado.getProximo());
			
			siguienteEstado = (siguienteKey == null) ? null : utilitarios.getCatalogoEstadoSolicitud().get(siguienteKey);
							
		} else{
			inicialEstado = null;
			inicialKey = null;			
			convocaKey = null;			
			convocaEstado = null;			
			asesoraKey = null;			
			asesoraEstado = null;			
			evaluarEstado = null;			
			evaluarKey = null;			
			siguienteKey = null;			
			siguienteEstado = null;		
		}
				
		switch(opcion) {
		   //Boton Convocar
			case 1:	{				
				if ((evaluarKey == convocaKey) && (evaluarKey != null))
					this.setDisabledConvocar(false);
				else
					this.setDisabledConvocar(true);
				break;	
			}
			//Boton Asesorar
			case 2: {
				if ((evaluarKey == asesoraKey) && (evaluarKey != null))
					this.setDisabledAsesorar(false);
				else
					this.setDisabledAsesorar(true);
				break;
			}
			//Boton Inscripcion
			case 3: {
				if ((evaluarKey == Integer.valueOf(asesoraEstado.getProximo())) && (evaluarKey != null))
					this.setDisabledCerrar(false);
				else
					this.setDisabledCerrar(true);				
				break;
			}
			//Boton Agregar Evaluacion
			case 4: {
				if ((evaluarKey == Integer.valueOf(asesoraEstado.getProximo())) && (evaluarKey != null))
					this.setDisabledBtnAgregaEvaluacion(false);
				else{
					if (service.validaProcesoConcluido(sol, false))
						this.setDisabledBtnAgregaEvaluacion(false);
					else
						this.setDisabledBtnAgregaEvaluacion(true);
				}
					
				break;
			}
			//Boton Proceso Concluido
			case 5: {
				if (service.validaProcesoConcluido(sol, false))
					this.setDisabledConcluido(false);
				else
					this.setDisabledConcluido(true);
					
			}
			default: 
				break;			
		}
			
	}
	
	public void limpiarCampos (){
				
		this.setSelectedLaboral(null);
		this.setNombreCargo(null);	
		this.setNombreInstitucion(null);
		this.setFechaDesde(null);
		this.setFechaHasta(null);
		this.setInstitucionDireccion(null);
		this.setIdSeletedLaboral(null);	
		this.setDescripcionCargo(null);
		this.setPaisInstitucion(null);
		this.setTelefonoInstitucion(null);
		this.listPortafolioLaboral = new ArrayList<Archivo> ();
		
		FacesUtil.setParamBySession("idSelectedLaboral", null);
					
	}	
	
	
	public void nuevoArchivo() {
		archivoExp = new Archivo();
		
		listEvalBySolicitud = new ArrayList<SelectItem> ();
		List<Evaluacion> listE = this.getListEvaluaciones();
		
		System.out.println("Obtiene el listado de las evaluaciones");
		listEvalBySolicitud.add(new SelectItem(null, "Seleccione la evaluacion"));
		for (Evaluacion dato : listE) {
			listEvalBySolicitud.add(new SelectItem(dato.getUnidad(), utilitarios.getCompetenciaDescripcion(dato.getUnidad())));
			//listEvalBySolicitud.add(new SelectItem(dato.getId(), dato.getUnidad().getCompetenciaDescripcion()));
		}
	}

	public void nuevoPortafolio (){
		System.out.println("Agrega archivo de evidencia al dato laboral/academico: ");
		
		if (this.selectedLaboral == null)			
			this.idSeletedLaboral = (Long)FacesUtil.getParametroSession("idSelectedLaboral");			
		else
			this.idSeletedLaboral = this.selectedLaboral.getId();		
			
		if (this.idSeletedLaboral != null) {
			
			if (this.selectedLaboral == null)
				
				this.selectedLaboral = service.getLaboralById(this.idSeletedLaboral);
				FacesUtil.setParamBySession("selectedLaboral", this.selectedLaboral);				
			
		}
		 
		
	}
	
	public void guardarArchivo() {
		
		this.archivoExp = (Archivo)FacesUtil.getParametroSession("archivoExp");			
				
		if (archivoExp != null) {
			//Asignando valores de pantalla
			
			archivoExp.setNombre(this.getNombreArchivoExp());
			archivoExp.setVersion(this.getVersionArchivoExp());
			archivoExp.setDescripcion(this.getDescripcionArchivoExp());
						
			archivoExp = (Archivo) service.guardar(archivoExp);
			
			if (archivoExp != null){
				FacesUtil.getMensaje("SCCL - Mensaje: ", "Los cambios ha sido aplicados exitosamente !!", false);
				FacesUtil.setParamBySession("archivoExp", null);
				FacesUtil.setParamBySession("selectedLaboral", null);
				FacesUtil.setParamBySession("idSelectedLaboral", null);		
			}else{
				FacesUtil.getMensaje("SCCL - Mensaje: ", "Error al grabar el archivo. Favor revisar...", true);				
			}
		}		
		
	}
	
	public void cancelaPortafolio (){
		
		this.setNombreArchivoExp(null);
		this.setDescripcionArchivoExp(null);
		this.setVersionArchivoExp(null);
		this.setTipoArchivoExp(null);
		this.setNombreRealArchivoExp(null);
		this.setSizeArchivoExp(null);
		
		FacesUtil.setParamBySession("archivoExp", null);
		FacesUtil.setParamBySession("selectedLaboral", null);
		FacesUtil.setParamBySession("idSelectedLaboral", null);		
		
	}
	
	public void editarPortafolio(){		
		Object [] objs;
		if (selectedArchivoId != null){			
			objs =  new Object [] {selectedArchivoId};
			this.archivoExp = service.getArchivoOneByParam("Archivo.findById", objs);
			
			this.selectedLaboral = archivoExp.getLaboral();
		}
	}
	
	public void aprobarPortafolio(){
		System.out.println("selectedLaboral " + selectedLaboral);
		System.out.println("archivoExp "+ archivoExp);
		System.out.println("Indica Aprobado: "+ archivoExp.getAprobado());
		
		Mantenedor estadoArchivo = archivoExp.getEstado();
		
		Integer  proxEstado = Integer.valueOf(estadoArchivo.getProximo());
		
		if (proxEstado != null){
			archivoExp.setEstado(utilitarios.getCatalogoPortafolio().get(proxEstado));
				
		}
				
		archivoExp = (Archivo) service.guardar(archivoExp);
		
		if (archivoExp != null){
			
		}
	}
	
	public void actualizaListaPortafolio (Integer tipo){
		Object [] objs;
		if (tipo == 1) {
			System.out.println("Consulta el listado de Archivos por datos laborales");			
			if (selectedLaboral != null) {			
				System.out.println("Datos laborales " + selectedLaboral.getId());
				 objs =  new Object [] {selectedLaboral.getId()};
				 System.out.println("Ejecuta la consulta");
				 this.listPortafolioLaboral = service.getArchivoByParam ("Archivo.findByLaboralId", objs);				
			}
		} else {		
				if (this.solicitudExp != null){
					Contacto c = solicitudExp.getContacto();
					objs =  new Object [] {c.getId()};
					this.listPortafolioContacto = service.getArchivoByParam ("Archivo.findByContactoId", objs);
				}
		}
	}
	
	public void uploadFile(FileUploadEvent event){
		
		Date fechaAhora = new Date();
		String tituloMsg = "";
		String mensaje = "";
		boolean isError = false;
					    
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		
	    String directorio = ec.getRealPath("/portafolio");	    
	    Usuario u = service.getUsuarioLocal(SecurityContextHolder.getContext().getAuthentication().getName());
	    String nombreFile;
	    String nombrePropietario;
	    Long   sizeArchivo;
	    int    pos;
	    String extension;
	    	    	    
		try {
		
			this.file = event.getFile();			
	
			nombreFile = file.getFileName().trim();
			
			this.nombreRealArchivoExp = nombreFile;
			
			pos=nombreFile.indexOf(".");
			
			extension = nombreFile.substring(pos); 
			
			if ((this.nombreRealArchivoExp.toLowerCase().endsWith(".pdf")) || (this.nombreRealArchivoExp.toLowerCase().endsWith(".png"))){
							
				nombrePropietario = u.getUsuarioAlias();
				
				sizeArchivo = (file.getSize()) / 1024; // El tama�o del archivo en KB
				
				this.sizeArchivoExp = String.valueOf(sizeArchivo) + " KB";
				
				archivoExp.setNombre(nombreFile);
				archivoExp.setSize(this.sizeArchivoExp);			
				archivoExp.setFecha(fechaAhora);					
				archivoExp.setNombreReal(this.nombreRealArchivoExp);
				archivoExp.setRuta(String.format(directorio+"/%s",nombreFile));
				archivoExp.setPropietario(nombrePropietario);
				archivoExp.setTipo(extension);
				archivoExp.setCategoria(extension);
				archivoExp.setIcono(nombreFile);
				archivoExp.setVersion("1");
				archivoExp.setDescripcion(this.nombreRealArchivoExp);
																	
				FileOutputStream fos = new FileOutputStream(String.format(directorio+"/%s",file.getFileName()));			
	            fos.write(file.getContents());
	            fos.flush();
	            fos.close();            
	            
	            String tipoMantenedorEstado = archivoExp.getTipoMantenedorEstado();
				
				Mantenedor primerEstado = service.getMantenedorMinByTipo(tipoMantenedorEstado);
				
				archivoExp.setEstado(primerEstado);
				
				this.selectedLaboral = (Laboral)FacesUtil.getParametroSession("selectedLaboral");
										
				archivoExp.setLaboral(this.selectedLaboral);
				
				archivoExp = (Archivo) service.guardar(archivoExp);
				
				if (archivoExp != null){		            
					mensaje = nombreFile + " ha sido cargado al servidor.";
					isError = false;
					FacesUtil.setParamBySession("archivoExp", this.archivoExp);					
				}else {
					isError = true;
				}
				
				
	            
			} else {
				 mensaje = nombreFile + "Unicamente se permite subir archivo PDF y PNG. Favor revisar...." + nombreFile.toLowerCase();
				 isError = true;
			}
	
		} catch (IOException e) {
			isError = true;
			e.printStackTrace();
		}		
		
	
		if (isError){
			tituloMsg = "Proceso Incompleto: ";
			mensaje = (mensaje.isEmpty()) ? "Se genero un error al subir el archivo. Favor revisar...." : mensaje;
		} else {
			tituloMsg = "Proceso Exitoso !!!";
		}
		
		FacesUtil.getMensaje(tituloMsg, mensaje, isError);
			        			
	}
	
	public void validaRegistroCV (){
		String       textMsg = "";
		String       titulo = "";
		boolean      isError = false;
		
		Contacto solicitante = this.solicitudExp.getContacto();
		
		if (this.isIndicaCVFull()) {
			this.setDisableSolicitarCertificacion(false);		
			
			if (solicitante.getTelefono1() == null) {
				textMsg = "Debe indicar el numero de telefono";					    
				this.setDisableSolicitarCertificacion(true);
			}
			
			if (solicitante.getDireccionActual() == null) {
				textMsg = (textMsg.isEmpty()) ? "Debe indicar la direccion actual" : textMsg + ", la direccion actual";					    
				this.setDisableSolicitarCertificacion(true);
			} 
			
			if (solicitante.getDepartamentoId() == null) {
				textMsg = (textMsg.isEmpty()) ? "Debe indicar el departamento" : textMsg + ", el departamento";			
				this.setDisableSolicitarCertificacion(true);
			}
			
			if (solicitante.getMunicipioId() == null) {
				textMsg = (textMsg.isEmpty()) ? "Debe indicar el municipio." : textMsg + " y el municipio.";		
				this.setDisableSolicitarCertificacion(true);
			}
			
			if (this.getListDatosLaborales().size() == 0) {
				textMsg = (textMsg.isEmpty()) ? "Debe indicar los datos Laborales / Academicos." : ". Debe indicar los datos Laborales / Academicos.";
				this.setDisableSolicitarCertificacion(true);
			}
			
			if (this.isDisableSolicitarCertificacion()){
				titulo = "Informacion incompleta: ";
				isError = true;
			}else{
				isError = false;
				titulo = "Informacion: ";
				textMsg = "Puede proceder a registrar la solicitud";
			}
						
			FacesUtil.getMensaje(titulo, textMsg, isError);
		} else
		{
			this.setDisableSolicitarCertificacion(true);
		}
	}	
	
	public String inicializaParametros (ActionEvent event){
		//Recibiendo el parametro del IdSolicitud que se va a editar en el Expediente.
		System.out.println("ExpedienteManagedBean. Obtiendo los parametros ");
		
		System.out.println("IdSolicitud = " + FacesUtil.getActionAttribute(event, "idSolicitudExp"));
		
		Long idSolicitud = Long.valueOf(FacesUtil.getActionAttribute(event, "idSolicitudExp"));
	
		System.out.println("Ya convierte a Long " + idSolicitud);		
		
		this.solicitudExp = service.getSolicitudById(idSolicitud);
		
		System.out.println("Obtiene el objeto solicitud " + solicitudExp.getId());
	
		return "/modulos/solicitudes/expediente?faces-redirect=true";
		
	}
	
	public void convocarCertificacion(){
		Contacto solicitante = null;
		boolean pasaConvocatoria = false;
		String  titulo = "";
		String  textoMsg = "";
		boolean isError = false;
		
		if (this.solicitudExp != null){
			solicitante = this.solicitudExp.getContacto();		
			
			pasaConvocatoria = service.portafolioVerificado(solicitante, new String("8"));			
		}
		
		System.out.println("Se cambia el estado");
		
		if (pasaConvocatoria == true){
			System.out.println("Nuevo estado " + this.getEstadoSiguiente().getValor());
			
			this.solicitudExp.setEstatus(this.getEstadoSiguiente());
			
			this.solicitudExp = (Solicitud) service.guardar(this.solicitudExp);
			
			if (this.solicitudExp != null){
				titulo = "SCCL - Proceso exitoso: ";
				textoMsg = "La solicitud ha pasado a convocatoria.";
				isError = false;
			} else {
				titulo = "SCCL - Error: ";
				textoMsg = "Se gener� un error al aplicar los cambios. Favor comun�quese al Departamento de Tecnolog�a del INATEC.";
				isError = true;
			}				
		} else {
			titulo = "SCCL - Error: ";
			textoMsg = "No puede ser convocado por que existen archivos del portafolio que deben ser revisado.";
			isError = true;
		}
				
		FacesUtil.getMensaje(titulo, textoMsg, isError);	  
		
	}
	
	
	public void asesorarCertificacion (){
	
		String  textoMsg = "";	
		boolean indicaAsesorado = false;
			
		solicitudExp = avanzaProceso (solicitudExp, "listo para inscripci�n", indicaAsesorado, textoMsg);
			
	}	
	
	public void inscripcionCertificacion (){
		boolean listoInscripcion = false;	
		String textoMsg = "La solicitud no cumple con las condiciones para proceder con la inscripcion. Favor revisar...";
		
		if (solicitudExp != null) {
			listoInscripcion = service.validaListoInscripcion(solicitudExp);			
			solicitudExp = avanzaProceso (solicitudExp, "listo para inscripci�n", listoInscripcion, textoMsg);						
		}
	}
	public void concluirCertificacion(){
		boolean concluir = false;
		String textoMsg = "La solicitud no cumple con las condiciones para concluir el proceso de certificacion. Favor revisar...";
		if (solicitudExp != null){
			concluir = service.validaProcesoConcluido(solicitudExp, false);
			solicitudExp = avanzaProceso(solicitudExp, " Proceso Concluido", concluir, textoMsg );
		}
	}
	public Solicitud avanzaProceso (Solicitud sol, String nombreEstado, boolean pasa, String msgNoPasa){
		String 	titulo = "";
		String 	textoMsg = "";
		boolean isError = false;
				
		if (sol == null){
			textoMsg = "Se gener� un error al obtener los datos del proceso de certificacion. Favor comun�quese al Departamento de Tecnolog�a del INATEC.";
			isError = true;
		} else {
			if (!pasa){
				textoMsg = msgNoPasa;
				isError = true;
			}else {
				sol.setEstatus(this.getEstadoSiguiente());		
				sol = (Solicitud) service.guardar(sol);
				
				if (sol != null)					
					textoMsg = "La solicitud se encuentra en estatus " + nombreEstado;					
				else {					
					textoMsg = "Se gener� un error al intentar aplicar los cambios. Favor comun�quese al Departamento de Tecnolog�a del INATEC.";
					isError = true;
				}
			}			
			
		}
		
		titulo = (isError) ? "SCCL - Error: " : "SCCL - Proceso exitoso: ";
		
		FacesUtil.getMensaje(titulo, textoMsg, isError);
		
		return sol;		
		
	}

	
}
