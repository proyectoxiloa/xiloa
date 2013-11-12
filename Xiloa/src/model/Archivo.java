package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;

@Entity(name="archivo")
@NamedQueries({
	@NamedQuery(name="Archivo.findByLaboralId", query="select a from archivo a where a.laboral.id=?1"),
	@NamedQuery(name="Archivo.findByContactoId", query="select a from archivo a where a.laboral.contacto.id=?1")
})
public class Archivo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String tipoMantenedorEstado = new String("8");

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "archivo_id", nullable = false)
	private Long id;
			
	@NotNull
	@ManyToOne
	@JoinColumn(name="laboral_id")
	private Laboral laboral;
			
	@NotNull
	@Column(name = "archivo_nombre", nullable = false)	
	private String nombre;

	@NotNull
	@Column(name = "archivo_descripcion", nullable = false)	
	private String descripcion;
	
	@NotNull
	@Column(name = "archivo_nombre_real", nullable = false)	
	private String nombreReal;
	
	@NotNull
	@Column(name = "archivo_ruta", nullable = false)	
	private String ruta;

	@NotNull
	@Column(name = "archivo_propietario", nullable = false)	
	private String propietario;

	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "archivo_fecha", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	@NotNull
	@Column(name = "archivo_tipo", nullable = false)	
	private String tipo;

	@NotNull
	@Column(name = "archivo_size", nullable = false)	
	private String size;

	@NotNull
	@Column(name = "archivo_version", nullable = false)	
	private String version;

	@NotNull
	@Column(name = "archivo_icono", nullable = false)	
	private String icono;

	@NotNull
	@Column(name = "archivo_categoria", nullable = false)	
	private String categoria;
	
	@NotNull
	@Column(name = "archivo_estado", nullable = false)	
	private String estado;
	
	@Column(name = "archivo_aprobado", nullable = true)	
	private String aprobado;
	
	/*
	@Lob()
	@Column(name="archivo_fisico", nullable=false)
	private byte[] archivoFisico;
*/
	
	public Long getId() {
		return id;
	}

	public String getTipoMantenedorEstado() {
		return tipoMantenedorEstado;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Laboral getLaboral() {
		return laboral;
	}

	public void setLaboral(Laboral laboral) {
		this.laboral = laboral;
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

	public String getNombreReal() {
		return nombreReal;
	}

	public void setNombreReal(String nombreReal) {
		this.nombreReal = nombreReal;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public String getPropietario() {
		return propietario;
	}

	public void setPropietario(String propietario) {
		this.propietario = propietario;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getIcono() {
		return icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	/*
	public byte[] getArchivoFisico() {
		return archivoFisico;
	}

	public void setArchivoFisico(byte[] archivoFisico) {
		this.archivoFisico = archivoFisico;
	}
	*/

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

		
	public String getAprobado() {
		return aprobado;
	}

	public void setAprobado(String aprobado) {
		this.aprobado = aprobado;
	}

	public Archivo(Laboral laboral, String nombre, String descripcion,
			String nombreReal, String ruta, String propietario, Date fecha,
			String tipo, String size, String version, String icono,
			String categoria, String estado) {
		super();
		this.laboral = laboral;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.nombreReal = nombreReal;
		this.ruta = ruta;
		this.propietario = propietario;
		this.fecha = fecha;
		this.tipo = tipo;
		this.size = size;
		this.version = version;
		this.icono = icono;
		this.categoria = categoria;
		this.estado = estado;
	}

	public Archivo() {
		super();		
	}
	
}