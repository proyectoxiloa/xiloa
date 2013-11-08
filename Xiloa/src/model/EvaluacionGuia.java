package model;

import java.io.Serializable;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;


@Entity(name = "evaluacion_guia")
@AssociationOverrides({
	@AssociationOverride(name = "pk.evaluacion", joinColumns = @JoinColumn(name = "evaluacion_id")),
	@AssociationOverride(name = "pk.guia", joinColumns = @JoinColumn(name = "guia_id"))})
@NamedQueries ({
	@NamedQuery(name="EvaluacionGuia.findByEvaluacionId", query="select eg from evaluacion_guia eg where eg.pk.evaluacion.id=?1"),	
	@NamedQuery(name="EvaluacionGuia.findInstrumentoByEvaluacionId", query="select eg.pk.guia.instrumento.id from evaluacion_guia eg where eg.pk.evaluacion.id=?1 group by eg.pk.guia.instrumento.id")
			})
public class EvaluacionGuia implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private EvaluacionGuiaId pk;
	private Integer puntaje;
	
	public EvaluacionGuiaId getPk() {
		return pk;
	}
	
	public void setPk(EvaluacionGuiaId pk) {
		this.pk = pk;
	}
	
	public Integer getPuntaje() {
		return puntaje;
	}
	
	public void setPuntaje(Integer puntaje) {
		this.puntaje = puntaje;
	}
	
	@Transient
	public Evaluacion getEvaluacion(){
		return pk.getEvaluacion();
	}
	
	public void setEvaluacion(Evaluacion evaluacion){
		pk.setEvaluacion(evaluacion);
	}
	
	@Transient
	public Guia getGuia(){
		return pk.getGuia();
	}
	
	public void setGuia(Guia guia){
		pk.setGuia(guia);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof EvaluacionGuia))
			return false;
		EvaluacionGuia that = (EvaluacionGuia) obj;
		if (pk != null ? !pk.equals(that.getPk()) : that.getPk() != null)
			return false;
		return true;
	 }
	
	@Override
	public int hashCode() {
		return (pk != null ? pk.hashCode() : 0);
	}
}