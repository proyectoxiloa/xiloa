package model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;

/**
 * 
 * @author Denis Chavez, Miriam Mart�nez
 * 
 * Entity Bean anotado con JPA para el manejo del mapeo Objeto/Relacional y la persistencia en BD
 * Esta clase es usada extensivamente por el servicio (paquete service)
 * Esta clase se corresponde con la tabla sccl.roles 
 * 
 */

@Entity(name = "roles")
@Table(name = "roles", schema = "sccl")
@NamedQueries({
	@NamedQuery(name="Rol.findByNombre", query="select r from roles r where r.estatus='true' and r.nombre=?1")
})
public class Rol implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "rol_id", nullable = false)
	private Integer id;
	
	@Column(name = "rol_nombre", nullable = false)	
	private String nombre;
	
	@Column(name = "rol_descripcion", nullable = false)
	private String descripcion;
	
	@Column(name = "id_rol_inatec", nullable = true)
	private Integer idRolInatec;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable
	(
			name = "sccl.perfiles_roles",
			joinColumns = @JoinColumn(name = "rol_id", unique = false),
			inverseJoinColumns = @JoinColumn(name = "perfil_id", unique = false),
			uniqueConstraints = @UniqueConstraint(columnNames = {"rol_id", "perfil_id"})
	)
	private List<Perfil> perfiles;
	
	@NotNull
	@Column(name = "rol_estatus", nullable = false)
	private boolean estatus;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public int getIdRolInatec() {
		return idRolInatec;
	}

	public void setIdRolInatec(int idRolInatec) {
		this.idRolInatec = idRolInatec;
	}

	public List<Perfil> getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(List<Perfil> perfiles) {
		this.perfiles = perfiles;
	}

	public boolean isEstatus() {
		return estatus;
	}

	public void setEstatus(boolean estatus) {
		this.estatus = estatus;
	}

	public Rol(String nombre, String descripcion, Integer idRolInatec, List<Perfil> perfiles, boolean estatus) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.idRolInatec = idRolInatec;
		this.perfiles = perfiles;
		this.estatus = estatus;
	}
	
	public Rol() {
		super();		
	}
}