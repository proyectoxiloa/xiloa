package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import support.Involucrado;
import support.Planificacion;
import support.USolicitud;

import support.UCompetencia;
import dao.IDao;
import dao.IDaoInatec;
import model.Actividad;
import model.Certificacion;
import model.Contacto;
import model.Requisito;
import model.Rol;
import model.Solicitud;
import model.Unidad;
import model.Usuario;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ServiceImp implements IService {
	
	@Autowired
	private IDao<Certificacion> certificacionDao;
	@Autowired
	private IDao<Requisito> requisitoDao;
	@Autowired
	private IDao<Usuario> usuarioDao;
	@Autowired
	private IDao<Contacto> contactoDao;
	@Autowired
	private IDao<Rol> rolDao;
	@Autowired
	private IDaoInatec inatecDao;

	//Inicio : SCCL || 22.10.2013 || Ing. Miriam Martinez Cano || Propiedades definidas para ser utilizados principalmente en el Modulo SOLICITUDES	
	@Autowired
	private IDao<Solicitud> solicitudDao;
	//Fin : SCCL || 22.10.2013 || Ing. Miriam Martinez Cano || Propiedades definidas para ser utilizados principalmente en el Modulo SOLICITUDES	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void guardarCertificacion(	String nombre, 
										String descripcion,
										Date fechaInicia, 
										Date fechaFinaliza, 
										int ifp, 
										String ifpNombre,
										String ifpDireccion, 
										Usuario programador,
										Date fechaIniciaDivulgacion, 
										Date fechaFinalizaDivulgacion,
										Date fechaFinalizaInscripcion, 
										Date fechaIniciaConvocatoria,
										Date fechaIniciaEvaluacion, 
										Usuario creador, 
										String referencial,
										int nivelCompetencia, 
										List<Requisito> requisitos,
										List<Unidad> unidades, 
										List<Actividad> actividades,
										List<Solicitud> solicitudes, 
										List<Contacto> involucrados,
										String estatus) {
		Contacto contacto = contactoDao.findOneByQuery("select c from contactos c where c.id=1");
		List<Contacto> contactos = new ArrayList<Contacto>();
		contactos.add(contacto);
		Usuario usuario = usuarioDao.findOneByQuery("select u from usuarios u where u.id=3");
		Certificacion certificacion = new Certificacion();
		certificacion.setNombre(nombre);
		certificacion.setDescripcion(descripcion);
		certificacion.setInicia(fechaInicia);
		certificacion.setFinaliza(fechaFinaliza);
		certificacion.setIfpId(ifp);
		certificacion.setIfpNombre(ifpNombre);
		certificacion.setIfpDireccion(ifpDireccion);
		certificacion.setProgramador(usuario);	//programador
		certificacion.setDivulgacionInicia(fechaIniciaDivulgacion);
		certificacion.setDivulgacionFinaliza(fechaFinalizaDivulgacion);
		certificacion.setInscripcionFinaliza(fechaFinalizaInscripcion);
		certificacion.setConvocatoriaInicia(fechaIniciaConvocatoria);
		certificacion.setEvaluacionInicia(fechaIniciaEvaluacion);
		certificacion.setCreador(usuario);		//creador
		certificacion.setReferencial(referencial);
		certificacion.setNivelCompetencia(nivelCompetencia);
		certificacion.setUnidades(unidades);
		certificacion.setActividades(actividades);
		certificacion.setSolicitudes(solicitudes);
		certificacion.setInvolucrados(contactos);
		certificacion.setEstatus(1);
		
		certificacionDao.save(certificacion);
	}
	
	@Override
	public List<Requisito> getRequisitos(int certificacionId) {
		return requisitoDao.findAll(Requisito.class);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void updateRequisito(Requisito requisito) {
		requisitoDao.save(requisito);
	}

	@Override
	public List<Usuario> getUsuarios() {
		return usuarioDao.findAll(Usuario.class);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)	
	public void updateUsuario(Usuario usuario) {
		usuarioDao.save(usuario);
	}

	@Override
	public List<Planificacion> getPlanificacion() {
		List<Planificacion> planificaciones = new ArrayList<Planificacion>();
		Contacto evalua;
		Contacto coordina;

		for(int i = 0; i<5; i++){
			Planificacion p = new Planificacion();
			p.setIdCentro(i+1);
			p.setNombreCentro("Centro "+i+1);
			p.setUnidadCompetencia("Unidad "+i+1);
			p.setDisponibilidad(i+1);
			p.setEstatus("Convocatoria");
			evalua = contactoDao.findById(Contacto.class, 2);
			coordina = contactoDao.findById(Contacto.class, 3);
			p.setEvalua(evalua.getPrimerNombre()+" "+evalua.getPrimerApellido());
			p.setCoordina(coordina.getPrimerNombre()+" "+coordina.getPrimerApellido());
			p.setRegistrado("11-10-2013");
			planificaciones.add(p);
		}
		return planificaciones;
	}

	@Override
	public List<UCompetencia> getUcompetenciaSinPlanificar() {
		return inatecDao.getCertificacionesSinPlanificar();
	}

	@Override
	public List<Involucrado> getContactos() {
		List<Contacto> contactos = new ArrayList<Contacto>();
		List<Involucrado> involucrados = new ArrayList<Involucrado>();
		contactos = contactoDao.findAll(Contacto.class);
		for(int i = 0; i<contactos.size(); i++){
			Involucrado involucrado = new Involucrado();
			involucrado.setIdContacto(contactos.get(i).getId());
			involucrado.setNombre(contactos.get(i).getPrimerNombre()+" "+contactos.get(i).getPrimerApellido());
			involucrado.setIdFuncion(i);
			if(i==0) involucrado.setFuncion("Supervisor");
			if(i==1) involucrado.setFuncion("Evaluador");
			if(i==2) involucrado.setFuncion("Tecnico docente");
			if(i==3) involucrado.setFuncion("Registro Academico");
			involucrado.setCorreo(contactos.get(i).getCorreo1());
			involucrados.add(involucrado);
		}
		return involucrados;
	}

	@Override
	public Usuario getUsuarioLocal(String usuario) {
		return usuarioDao.findOneByQuery("Select u from usuarios u where u.usuarioEstatus='true' and u.usuarioAlias="+"'"+usuario+"'");
	}
	
	@Override
	public Usuario getUsuarioInatec(String usuario) {
		return inatecDao.getUsuario(usuario);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void RegistrarUsuario(Usuario usuario) {
		usuarioDao.save(usuario);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void RegistrarUsuarioOpenId(String login, String nombre, String apellido, String email, String rol) {
		Usuario usuario = new Usuario();
		usuario.setUsuarioAlias(login);
		usuario.setUsuarioPwd("");
		usuario.setUsuarioEstatus(true);
		usuario.setRol(rolDao.findOneByQuery("Select r from roles r where r.estatus='true' and r.nombre="+"'"+rol+"'"));
		usuarioDao.save(usuario);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void guardarContacto(Contacto contacto) {			
			contactoDao.save(contacto);
	}

	@Override
	public boolean isNuevoContactoInatec(String usuario) {
		Contacto contacto = contactoDao.findOneByQuery("select c from contactos c where c.inatec=true and c.usuarioInatec="+"'"+usuario+"'");
		if(contacto == null)
			return true;
		else
			return false;
	}

	@Override
	public Contacto generarNuevoContactoInatec(String usuario) {
		Contacto contacto = inatecDao.generarContacto(usuario);
		return contacto;
	}
	
	//Inicio : SCCL || 22.10.2013 || Ing. Miriam Martinez Cano || Metodos definidos para ser utilizados principalmente en el Modulo SOLICITUDES
	public List<USolicitud> getUSolicitudes () {
			System.out.println("Desde el metodo getSolicitudes del ServiceImp");
			
			List<Solicitud> Sols = solicitudDao.findAll(Solicitud.class);
			List<USolicitud> uSols = new ArrayList<USolicitud>();;
			
			for(int i = 0; i<Sols.size(); i++){
				
				USolicitud uSolicitud = new USolicitud();
								
				uSolicitud.setCentroEvaluador(Sols.get(i).getCertificacion().getIfpNombre());
				uSolicitud.setNombreCertificacion(Sols.get(i).getCertificacion().getNombre());			
				
				uSolicitud.setNombreCandidato(Sols.get(i).getContacto().getPrimerNombre() + " " +
											  Sols.get(i).getContacto().getSegundoNombre()  + " " +
											  Sols.get(i).getContacto().getPrimerApellido() + " " +
											  Sols.get(i).getContacto().getSegundoApellido());			
							
				uSolicitud.setNombreEvaluador(Sols.get(i).getCertificacion().getInvolucrados().get(0).getCorreo1());
									
				uSolicitud.setEstatus(Sols.get(i).getEstatus());
									
				uSolicitud.setFechaRegistro(Sols.get(i).getFechaRegistro());
						
				uSols.add(uSolicitud);			
			}
			
			return uSols;
			
	};
	//Fin : SCCL || 22.10.2013 || Ing. Miriam Martinez Cano || Metodos definidos para ser utilizados principalmente en el Modulo SOLICITUDES
	
	
}