package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import support.Departamento;
import support.Ifp;
import support.Municipio;
import support.USolicitud;
import support.UCompetencia;
import dao.IDao;
import dao.IDaoInatec;
import model.Actividad;
import model.Archivo;
import model.Bitacora;
import model.Certificacion;
import model.Contacto;
import model.Evaluacion;
import model.EvaluacionGuia;
import model.Guia;
import model.Instrumento;
import model.Laboral;
import model.Mantenedor;
import model.Perfil;
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
	//Inicio : SCCL || 22.10.2013 || Ing. Miriam Martinez Cano || Propiedades definidas para ser utilizados principalmente en el Modulo SOLICITUDES	
	@Autowired
	private IDao<Solicitud> solicitudDao;
	//Fin : SCCL || 22.10.2013 || Ing. Miriam Martinez Cano || Propiedades definidas para ser utilizados principalmente en el Modulo SOLICITUDES
	@Autowired
	private IDao<Mantenedor> mantenedorDao;
	@Autowired
	private IDao<Actividad> actividadDao;
	@Autowired	
	private IDao<Perfil> perfilDao;
	@Autowired	
	private IDao<Unidad> unidadDao;
	@Autowired	
	private IDao<Guia> guiaDao;
	@Autowired	
	private IDao<Instrumento> instrumentoDao;
	@Autowired	
	private IDaoInatec inatecDao;
	
	@Autowired	
	private IDao<Laboral> laboralDao;
	
	@Autowired	
	private IDao<Evaluacion> evaluacionDao;
	
	@Autowired	
	private IDao<EvaluacionGuia> evaluacionGuiaDao;
	
	@Autowired
	private IDao<Long> longDao;
	
	@Autowired
	private IDao<Bitacora> bitacoraDao;
	
	@Autowired
	private IDao<Integer> integerDao;
	
	@Autowired
	private IDao<Archivo> archivoDao;
	
	@Override
	public List<Certificacion> getCertificaciones(){
		return certificacionDao.findAllByNamedQuery("Certificacion.findAll");
	}
		
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Certificacion guardarCertificacion(Certificacion certificacion, List<Requisito> requisitos, List<Unidad> unidades) {
		
		certificacion = certificacionDao.save(certificacion);
		
		for(int i=0; i<requisitos.size(); i++){
			requisitos.get(i).setCertificacion(certificacion);
			requisitoDao.save(requisitos.get(i));
		}
		
		for(int i=0; i<unidades.size(); i++){
			unidades.get(i).setCertificacion(certificacion);
			unidadDao.save(unidades.get(i));
		}
		
		unidadDao.save(new Unidad(certificacion,"001","UC1",null,true));
		
		Mantenedor estado = getMapMantenedoresByTipo("4").get(10);				//estatus pendiente
		Map<Integer, Mantenedor> actividades = getMapMantenedoresByTipo("1");	//actualizar
		Usuario creador = getUsuarioLocal("admin");								//actualizar
		
		actividadDao.save(new Actividad(certificacion,0,actividades.get(1),"Divulgacion","A completar",null,null,null,new Date(),null,null,creador,null,null,null,estado));
		actividadDao.save(new Actividad(certificacion,1,actividades.get(4),"Convocatoria","A completar",null,null,null,new Date(),null,null,creador,null,null,null,estado));
		actividadDao.save(new Actividad(certificacion,2,actividades.get(3),"Evaluacion","A completar",null,null,null,new Date(),null,null,creador,null,null,null,estado));
		actividadDao.save(new Actividad(certificacion,3,actividades.get(2),"Verificacion","A completar",null,null,null,new Date(),null,null,creador,null,null,null,estado));
				
		return certificacionDao.save(certificacion);
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
	public List<UCompetencia> getUcompetenciaSinPlanificar() {
		return inatecDao.getCertificacionesSinPlanificar();
	}
	
	@Override
	public List<Contacto> getContactosInatec() {
		return contactoDao.findAllByQuery("Select c from contactos c where c.inatec='true' and c.rol.idRolInatec in (213,214,215,216)");
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
	public void RegistrarUsuarioOpenId(String login, String nombre, String apellido, String email, String rol) {
		Usuario usuario = new Usuario(null, //contacto 
				                      login, //usuarioAlias
				                      "", //usuarioPwd
				                      rolDao.findOneByQuery("Select r from roles r where r.estatus='true' and r.nombre="+"'"+rol+"'"), //rol 
				                      true // usuarioEstatus
				                      );		
		usuarioDao.save(usuario);
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
		Rol rol = rolDao.findOneByQuery("Select r from roles r where r.idRolInatec="+inatecDao.getIdRol(usuario));
		if(rol != null)
			contacto.setRol(rol);
		return contacto;
	}

	@Override
	public List<USolicitud> getUSolicitudes () {			
			
			List<Solicitud> Sols = solicitudDao.findAll(Solicitud.class);
			List<USolicitud> uSols = new ArrayList<USolicitud>();;
			
			for(int i = 0; i<Sols.size(); i++){
				
				USolicitud uSolicitud = new USolicitud (Sols.get(i), false);
				
				uSols.add(uSolicitud);			
			}
			
			return uSols;
			
	}
	
	@Override
	public List<Solicitud> getSolicitudes() {
		return solicitudDao.findAll(Solicitud.class);		
	}	
	
	@Override
	public Solicitud getSolicitudById(Long idSolicitud) {
		return solicitudDao.findById(Solicitud.class, idSolicitud.intValue());		
	}
	
	@Override
	public List<Solicitud> getSolicitudesByParam(HashMap<String, Object> param) {
		
		String sqlSolicitud;		
		
		String sqlWhere = null;		
		
		if (param.size() > 0 ) {
			
			String campo;
			Object valor;
			
			Iterator<String> claveSet = param.keySet().iterator();			
		    
		    while(claveSet.hasNext()){		      
		    	campo = claveSet.next();		    	
		    	if (param.get(campo) instanceof Integer || param.get(campo) instanceof Long) {
		    		valor = param.get(campo);
		    	} else {
		    		valor = "'" + param.get(campo) + "'";
		    	}		    	
		    	
		    	sqlWhere = (sqlWhere == null) ? "where " + campo + " = " + valor :sqlWhere + " and " + campo + " = " + valor;		        		        
		    }
		}		
		
		sqlSolicitud = "select s from solicitudes s " + ((sqlWhere == null) ? "" : sqlWhere) ;		
		
		return solicitudDao.findAllByQuery(sqlSolicitud);
	}
	
	@Override
	public Contacto getContactoByCedula(String cedula) {
		Contacto c = contactoDao.findOneByQuery("select c from contactos c where c.numeroIdentificacion = '" + cedula + "'");
		return c;
	}	

	@Override
	public Rol getRolById(int id) {
		return rolDao.findById(Rol.class, id);
	}

	@Override
	public List<Mantenedor> getMantenedorActividades() {
		return this.getMantenedoresByTipo(new Integer(1));
		//return mantenedorDao.findAllByQuery("Select m from mantenedores m where m.tipo='1' order by 1");
	}

	@Override
	public List<Mantenedor> getMantenedorEstatusCertificacion() {
		return this.getMantenedoresByTipo(new Integer(3));
		//return mantenedorDao.findAllByQuery("Select m from mantenedores m where m.tipo='3' order by 1");
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Object guardar (Object objeto) {
		if (objeto instanceof Solicitud) {
			return solicitudDao.save((Solicitud) objeto);
		}
		if (objeto instanceof Laboral) {
			return laboralDao.save((Laboral) objeto);
		}
		if (objeto instanceof Archivo) {
			return archivoDao.save((Archivo) objeto);
		}
		if (objeto instanceof Evaluacion) {
			return evaluacionDao.save((Evaluacion) objeto);
		}
		if (objeto instanceof EvaluacionGuia) {
			return evaluacionGuiaDao.save((EvaluacionGuia) objeto);
		}
		if(objeto instanceof Actividad){
			return actividadDao.save((Actividad)objeto);
		}
		if(objeto instanceof Mantenedor)
			return mantenedorDao.save((Mantenedor)objeto);
		if(objeto instanceof Perfil)
			return perfilDao.save((Perfil)objeto);
		if(objeto instanceof Rol)
			return rolDao.save((Rol)objeto);
		if(objeto instanceof Contacto){
			String nombreCompleto = ((Contacto) objeto).getPrimerNombre()+" "+((Contacto)objeto).getPrimerApellido();
			((Contacto)objeto).setNombreCompleto(nombreCompleto);
			return contactoDao.save((Contacto)objeto);
		}
		if(objeto instanceof Usuario)
			return usuarioDao.save((Usuario)objeto);
		if(objeto instanceof Certificacion)
			return certificacionDao.save((Certificacion)objeto);
		if(objeto instanceof Guia)
			return guiaDao.save((Guia)objeto);
		if(objeto instanceof Instrumento)
			return instrumentoDao.save((Instrumento)objeto);
		if(objeto instanceof Actividad)
			return actividadDao.save((Actividad)objeto);
		if(objeto instanceof Bitacora)
			return bitacoraDao.save((Bitacora)objeto);
		if(objeto instanceof Unidad)
			return unidadDao.save((Unidad)objeto);
		if(objeto instanceof Requisito)
			return requisitoDao.save((Requisito)objeto);
		return null;
	}
	
	@Override
	public List<Certificacion> getCertificacionesByIdIfp (Integer id) {
		if (id == null){
			return certificacionDao.findAll(Certificacion.class);					
		} else {
			Object [] objs =  new Object [] {id};
			return certificacionDao.findAllByNamedQueryParam("Certificacion.findByIfpId", objs);
		}		
	}
	
	@Override
	public Certificacion getCertificacionById(Long id) {
		Object [] objs =  new Object [] {id};
		return certificacionDao.findOneByNamedQueryParam("Certificacion.findById", objs);						
	}	
	
	@Override
	public List<Ifp> getIfpByInatec () {
		return inatecDao.getIfpInatec();
	}

	@Override
	public List<Actividad> getActividades(Long certificacionId) {		
		return actividadDao.findAllByQuery("Select a from actividades a where a.certificacion.id="+certificacionId);
		//return actividadDao.findAllByQuery("Select a from actividades a");
	}

	@Override
	public List<Mantenedor> getMantenedores() {
		return mantenedorDao.findAll(Mantenedor.class);		
	}
	
	@Override
	public List<Mantenedor> getMantenedoresByTipo(Integer tipo) {
		Object [] objs =  new Object [] {tipo.toString()};
		return mantenedorDao.findAllByNamedQueryParam("Mantenedor.findByTipo", objs);		
	}
	
	@Override
	public Map<Integer, Mantenedor> getMapMantenedoresByTipo(String tipo) {
		Object [] objs =  new Object [] {tipo};
		
		List<Mantenedor> l = mantenedorDao.findAllByNamedQueryParam("Mantenedor.findByTipo", objs);				
		Map<Integer, Mantenedor> m = new HashMap<Integer, Mantenedor>();
		if(l != null){
			for(int i=0; i<l.size(); i++){
				m.put(l.get(i).getId(), l.get(i));
			}			
		}
		return m;
	}
	
	@Override
	public List<Laboral> getListLaboralByTipo(Integer tipo, Contacto contacto) {
		Object [] objs =  new Object [] {tipo, contacto.getId()};
		return laboralDao.findAllByNamedQueryParam("Laboral.findAllByTipoAndContactoId", objs);					
	}
	
	@Override
	public Laboral getLaboralById(Long idLaboral) {
		Object [] objs =  new Object [] {idLaboral};
		return laboralDao.findOneByNamedQueryParam("Laboral.findById", objs);				
	}
	
	@Override
	public List<Evaluacion> getEvaluaciones(Solicitud solicitud) {
		Object [] objs =  new Object [] {solicitud.getId()};
		return evaluacionDao.findAllByNamedQueryParam("Evaluacion.findAllBySolicitudId", objs);		
	}
	
	@Override
	public Unidad getUnidadById(Long idUnidad){
		Object [] objs =  new Object [] {idUnidad};
		return unidadDao.findOneByNamedQueryParam("Unidad.findById", objs);
	}
	
	@Override
	public Instrumento getInstrumentoById(Long idInstrumento){
		Object [] objs =  new Object [] {idInstrumento};
		return instrumentoDao.findOneByNamedQueryParam("Instrumento.findById", objs);		
	}
	
	@Override
	public List<Instrumento> getInstrumentoByUnidad (Long idUnidad) {
		Object [] objs =  new Object [] {idUnidad};
		return instrumentoDao.findAllByNamedQueryParam("Instrumento.findAllByUnidadId", objs);				
	}	

	@Override
	public List<Unidad> getUnidadesByCertificacionId(Long certificacionId) {
		Object [] objs =  new Object [] {certificacionId};
		return unidadDao.findAllByNamedQueryParam("Certificacion.findUnidadesByCert", objs);		
	}

	@Override
	public List<Instrumento> getInstrumentosByCertificacionId(Long certificacionId) {
		String query = "select i from instrumentos i where i.unidad in (select u from unidades u where u.certificacion.id="+certificacionId+")";
		return instrumentoDao.findAllByQuery(query);
	}
	
	@Override
	public List<EvaluacionGuia> getEvaluacionGuiaByEvaluacionId(Long evaluacionId) {
		Object [] objs =  new Object [] {evaluacionId};
		return evaluacionGuiaDao.findAllByNamedQueryParam("EvaluacionGuia.findByEvaluacionId", objs);		
	}
	
	@Override
	public List<Instrumento> getIntrumentoByEvaluacion(Long evaluacionId){
		Object [] objs =  new Object [] {evaluacionId};
		List<Long> instrumentosId = longDao.findAllByNamedQueryParam("EvaluacionGuia.findInstrumentoByEvaluacionId", objs);
		List<Instrumento> listInstrumentos = new ArrayList<Instrumento> ();
		
		for (Long dato : instrumentosId) {
			listInstrumentos.add(this.getInstrumentoById(dato));
		}
		return listInstrumentos;
	}

	@Override
	public List<Bitacora> getBitacoras(Long actividadId) {
		Object [] objs =  new Object [] {actividadId};
		return bitacoraDao.findAllByNamedQueryParam("Bitacoras.findAllByActividadId", objs);
	}
	
	@Override
	public Integer getMantenedorMinByTipo(String tipo) {		
		Object [] objs =  new Object [] {tipo};
		return integerDao.findOneByNamedQueryParam("Mantenedor.findMinByTipo", objs);				
	}

	@Override
	public Mantenedor getMantenedorById(Integer idMantenedor) {
		return mantenedorDao.findById(Mantenedor.class, idMantenedor.intValue());	
	}
	
	@Override
	public Map<Integer, Departamento> getDepartamentosByInatec() {
		List<Departamento> lista = inatecDao.getDepartamentosInatec();
		
		Map<Integer, Departamento> m = new HashMap<Integer, Departamento>();
		
		for (Departamento d : lista){
			m.put(d.getDpto_id(), d);
		}
		
		return m;
	}

	@Override
	public Map<Integer, Municipio> getMunicipioDptoByInatec(Integer idDpto) {
		List<Municipio> lista = inatecDao.getMunicipioByDeptoInatec(idDpto);
		
		Map<Integer, Municipio> m = new HashMap<Integer, Municipio>();
		
		for(Municipio dato : lista) {
			m.put(dato.getMunicipio_id(), dato);
		} 
		return m;
	}	

	@Override
	public List<Guia> getGuiaByParam(String namedString, Object [] parametros){
		return guiaDao.findAllByNamedQueryParam(namedString, parametros);
	}		
	
	@Override
	public List<Archivo> getArchivoByParam (String namedString, Object [] parametros) {
		return archivoDao.findAllByNamedQueryParam(namedString, parametros);
	}
	
	@Override
	public List<Requisito> getRequisitos(int cursoId, int centroId){
		return inatecDao.getRequisitos(cursoId, centroId);
	}

	@Override
	public List<Unidad> getUnidades(int cursoId, int centroId) {
		return null;
	}
}