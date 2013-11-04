package model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;

@Entity(name="unidades")
public class Unidad {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "unidad_id", nullable = false)
	private Long id;
	
	@NotNull
	@Column(name = "competencia_codigo", nullable = false)
	private String competenciaCodigo;

	@NotNull
	@Column(name = "competencia_descripcion", nullable = false)	
	private String competenciaDescripcion;
	
	@OneToMany//(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="unidad_id", referencedColumnName="unidad_id")
	private List<Instrumento> instrumentos;
	
	@NotNull
	@Column(name = "competencia_estatus", nullable = false)	
	private boolean estatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompetenciaCodigo() {
		return competenciaCodigo;
	}

	public void setCompetenciaCodigo(String competenciaCodigo) {
		this.competenciaCodigo = competenciaCodigo;
	}

	public String getCompetenciaDescripcion() {
		return competenciaDescripcion;
	}

	public void setCompetenciaDescripcion(String competenciaDescripcion) {
		this.competenciaDescripcion = competenciaDescripcion;
	}

	public List<Instrumento> getInstrumentos() {
		return instrumentos;
	}

	public void setInstrumentos(List<Instrumento> instrumentos) {
		this.instrumentos = instrumentos;
	}

	public Unidad(String competenciaCodigo, String competenciaDescripcion, boolean estatus) {
		super();
		this.competenciaCodigo = competenciaCodigo;
		this.competenciaDescripcion = competenciaDescripcion;
		this.estatus = estatus;
	}
	
	public Unidad() {
		super();		
	}

	public boolean isEstatus() {
		return estatus;
	}

	public void setEstatus(boolean estatus) {
		this.estatus = estatus;
	}
}