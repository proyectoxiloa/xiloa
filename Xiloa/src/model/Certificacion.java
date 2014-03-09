package model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;

/**
 * 
 * @author Denis Chavez, Miriam Mart�nez
 * 
 * Entity Bean anotado con JPA para el manejo del mapeo Objeto/Relacional y la persistencia en BD
 * Esta clase es usada extensivamente por el servicio (paquete service)
 * Esta clase se corresponde con la tabla sccl.certificaciones 
 * 
 */

@Entity(name = "certificaciones")
@Table(name = "certificaciones", schema = "sccl")
@NamedQueries({
	@NamedQuery(name="Certificacion.findAll", query="select c from certificaciones c order by c.id desc"),
	@NamedQuery(name="Certificacion.findActivas", query="select c from certificaciones c where c.estatus.id=8 order by c.id desc"),
	@NamedQuery(name="Certificacion.findByIfpId", query="select c from certificaciones c where c.estatus.id!=9 and c.ifpId = case ?1 when 1000 then c.ifpId else ?1 end"),
	@NamedQuery(name="Certificacion.findById", query="select c from certificaciones c where c.id=?1"),
	@NamedQuery(name="Certificacion.findAllByNombre", query="select c from certificaciones c where c.estatus.id=8 and c.nombre like ?1 order by c.id desc"),
	@NamedQuery(name="Certificacion.findAllByCentro", query="select c from certificaciones c where c.estatus.id=8 and c.ifpNombre like ?1 order by c.id desc"),
	@NamedQuery(name="Certificacion.findItemsByIfpId", query="select new support.Item(c.id, c.nombre) from certificaciones c where c.estatus.id!=9 and c.ifpId = case ?1 when 1000 then c.ifpId else ?1 end"),
})
public class Certificacion implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "certificacion_id", nullable = false)
	private Long id;
	
	@Column(name = "certificacion_oferta_id", nullable = true)
	private Integer ofertaId;	
	
	@Column(name = "certificacion_estructura_id", nullable = false)
	private Integer estructuraId;	
	
	@Column(name = "certificacion_curso_id", nullable = false)
	private int cursoId;
	
	@Column(name = "certificacion_nombre", nullable = false)
	private String nombre;
	
	@Column(name = "certificacion_descripcion", nullable = false)
	private String descripcion;
		
	@Column(name = "certificacion_disponibilidad", nullable = true)
	private Integer disponibilidad;
	
	@Column(name = "certificacion_costo", nullable = true)
	private Float costo;
	
	@NotNull
	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "certificacion_fecha_registro", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date fechaRegistro;
	
	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "certificacion_fecha_actualiza", nullable = true)
	@Temporal(TemporalType.DATE)
	private Date fechaActualiza;
	
	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "certificacion_inicia", nullable = true)
	@Temporal(TemporalType.DATE)
	private Date inicia;
	
	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "certificacion_finaliza", nullable = true)
	@Temporal(TemporalType.DATE)
	private Date finaliza;
	
	@Column(name = "certificacion_ifp_id", nullable = false)
	private int ifpId;
	
	@Column(name = "certificacion_ifp_direccion", nullable = true)
	private String ifpDireccion;
	
	@Column(name = "certificacion_ifp_nombre", nullable = false)
	private String ifpNombre;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="certificacion_programador_id")	
	private Contacto programador;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="certificacion_creador_id")	
	private Contacto creador;
	
	@ManyToOne
	@JoinColumn(name="certificacion_actualiza_id")	
	private Contacto actualiza;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="certificacion_estatus")
	private Mantenedor estatus;
	
	@Column(name = "certificacion_referencial", nullable = true)
	private String referencial;
	
	@Column(name = "certificacion_nivel_competencia", nullable = true)
	private int nivelCompetencia;
	
	@NotNull
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
			name="sccl.certificacion_unidades",
	        joinColumns=@JoinColumn(name="certificacion_id"),
	        uniqueConstraints = @UniqueConstraint(columnNames = {"certificacion_id", "unidad_id"})
	)
	@Column(name="unidad_id", nullable = false)
	private Set<Long> unidades;
	
	@OneToMany(mappedBy = "certificacion",fetch = FetchType.EAGER)
	private List<Requisito> requisitos;
		
	@OneToMany(mappedBy = "certificacion",fetch = FetchType.EAGER)
	@MapKey(name = "indice")
	private Map<Integer,Actividad> actividades;	
		
	@OneToMany(mappedBy = "certificacion")
	private List<Solicitud> solicitudes;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable
	(
			name = "sccl.pinvolucrados",
			joinColumns = @JoinColumn(name = "certificacion_id", unique = false),
			inverseJoinColumns = @JoinColumn(name = "contacto_id", unique = false)
	)
	@MapKeyColumn(name="id_rol")
	private Map<Integer, Contacto> involucrados;
	
	public Integer getOfertaId() {
		return ofertaId;
	}

	public void setOfertaId(Integer ofertaId) {
		this.ofertaId = ofertaId;
	}

	public Contacto getCoordinador(){
		return involucrados.get(2);
	}
	
	public Contacto getVerificador(){
		return involucrados.get(3);
	}
	
	public Contacto getEvaluador(){
		return involucrados.get(7);
	}
	
	public Certificacion(){
		super();
		this.requisitos = new ArrayList<Requisito>();
		this.unidades = new HashSet<Long>();
		this.disponibilidad = null;
		this.actividades = new HashMap<Integer,Actividad>();
		this.solicitudes = new ArrayList<Solicitud>();
		this.involucrados = new HashMap<Integer, Contacto>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getEstructuraId() {
		return estructuraId;
	}

	public void setEstructuraId(Integer estructuraId) {
		this.estructuraId = estructuraId;
	}

	public int getCursoId() {
		return cursoId;
	}

	public void setCursoId(int cursoId) {
		this.cursoId = cursoId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getDisponibilidad() {
		return disponibilidad;
	}

	public void setDisponibilidad(Integer disponibilidad) {
		this.disponibilidad = disponibilidad;
	}

	public String getCostoFormateado() {
		if(costo == null)
			return "N/D";
		else
		{
			NumberFormat format = NumberFormat.getInstance(Locale.US);
			format.setMaximumFractionDigits(2);
			format.setMinimumFractionDigits(2);
			return format.format(costo);
		}
	}
	
	public Float getCosto() {
		return costo;
	}

	public void setCosto(Float costo) {
		this.costo = costo;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Date getFechaActualiza() {
		return fechaActualiza;
	}

	public void setFechaActualiza(Date fechaActualiza) {
		this.fechaActualiza = fechaActualiza;
	}

	public Date getInicia() {
		return inicia;
	}

	public void setInicia(Date inicia) {
		this.inicia = inicia;
	}

	public Date getFinaliza() {
		return finaliza;
	}

	public void setFinaliza(Date finaliza) {
		this.finaliza = finaliza;
	}

	public int getIfpId() {
		return ifpId;
	}

	public void setIfpId(int ifpId) {
		this.ifpId = ifpId;
	}

	public String getIfpDireccion() {
		return ifpDireccion;
	}

	public void setIfpDireccion(String ifpDireccion) {
		this.ifpDireccion = ifpDireccion;
	}

	public String getIfpNombre() {
		return ifpNombre;
	}

	public void setIfpNombre(String ifpNombre) {
		this.ifpNombre = ifpNombre;
	}

	public Contacto getProgramador() {
		return programador;
	}

	public void setProgramador(Contacto programador) {
		this.programador = programador;
	}

	public Date getDivulgacionInicia() {
		return actividades.get(0).getFechaInicial();
	}

	public Date getDivulgacionFinaliza() {
		return actividades.get(0).getFechaFinal();
	}

	public Date getConvocatoriaInicia() {
		return actividades.get(1).getFechaInicial();
	}
	
	public Date getConvocatoriaFinaliza() {
		return actividades.get(1).getFechaFinal();
	}

	public Date getEvaluacionInicia() {
		return actividades.get(2).getFechaInicial();
	}

	public Date getEvaluacionFinaliza() {
		return actividades.get(2).getFechaFinal();
	}
	
	public Date getVerificacionInicia() {
		return actividades.get(3).getFechaInicial();
	}

	public Date getVerificacionFinaliza() {
		return actividades.get(3).getFechaFinal();
	}

	public Contacto getCreador() {
		return creador;
	}

	public void setCreador(Contacto creador) {
		this.creador = creador;
	}

	public Contacto getActualiza() {
		return actualiza;
	}

	public void setActualiza(Contacto actualiza) {
		this.actualiza = actualiza;
	}

	public Mantenedor getEstatus() {
		return estatus;
	}

	public void setEstatus(Mantenedor estatus) {
		this.estatus = estatus;
	}

	public String getReferencial() {
		return referencial;
	}

	public void setReferencial(String referencial) {
		this.referencial = referencial;
	}

	public int getNivelCompetencia() {
		return nivelCompetencia;
	}

	public void setNivelCompetencia(int nivelCompetencia) {
		this.nivelCompetencia = nivelCompetencia;
	}

	public List<Requisito> getRequisitos() {
		return requisitos;
	}

	public void setRequisitos(List<Requisito> requisitos) {
		this.requisitos = requisitos;
	}

	public Set<Long> getUnidades() {
		return unidades;
	}

	public void setUnidades(Set<Long> unidades) {
		this.unidades = unidades;
	}
	
	public void addUnidad(Long unidad){
		this.unidades.add(unidad);
	}

	public List<Actividad> getActividades() {
		return new ArrayList<Actividad>(actividades.values());
	}
	
	public void addActividad(Actividad actividad){
		Integer indice;
		if(actividades.isEmpty())
			indice = 0;
		else
			indice = actividades.size();
		actividad.setIndice(indice);
		this.actividades.put(indice, actividad);
	}
	
	public List<Solicitud> getSolicitudes() {
		return solicitudes;
	}

	public void setSolicitudes(List<Solicitud> solicitudes) {
		this.solicitudes = solicitudes;
	}
	
	public List<Contacto> getInvolucrados() {
		return new ArrayList<Contacto>(involucrados.values());
	}	

	public void setInvolucrados(Contacto[] involucrados) {
		for(int i=0; i<involucrados.length; i++){
			this.involucrados.put(involucrados[i].getRol().getId(), involucrados[i]);
		}
	}

	public Certificacion(	Integer ofertaId,
							Integer estructuraId,
							int cursoId,
							String nombre, 
							String descripcion,
							int disponibilidad,
							Date inicia,
							Date finaliza, 
							int ifpId, 
							String ifpDireccion, 
							String ifpNombre,
							Contacto programador, 
							Contacto creador,
							Mantenedor estatus, 
							String referencial, 
							int nivelCompetencia,
							List<Requisito> requisitos, 
							Set<Long> unidades, 
							List<Solicitud> solicitudes,
							Map<Integer, Contacto> involucrados) {
		super();
		this.ofertaId = ofertaId;
		this.estructuraId = estructuraId;
		this.cursoId = cursoId;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.disponibilidad = disponibilidad;
		this.inicia = inicia;
		this.finaliza = finaliza;
		this.ifpId = ifpId;
		this.ifpDireccion = ifpDireccion;
		this.ifpNombre = ifpNombre;
		this.programador = programador;
		this.creador = creador;
		this.estatus = estatus;
		this.referencial = referencial;
		this.nivelCompetencia = nivelCompetencia;
		this.requisitos = requisitos;
		this.unidades = unidades;
		this.solicitudes = solicitudes;
		this.involucrados = involucrados;
	}
}