package support;

/**
 * 
 * @author Denis Chavez
 *
 * Esta clase se utiliza para la creaci�n de cuentas de usuario externo desde la p�gina de inicio de la aplicaci�n.
 * Encapsula la informaci�n proporcionada por el usuario: usuario, nombre, apellido, email1 y email2.
 * 
 */
public class UsuarioExterno {

	private String usuario;
	private String nombre;
	private String apellido;
	private String email1;
	private String email2;
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getEmail1() {
		return email1;
	}
	public void setEmail1(String email1) {
		this.email1 = email1;
	}
	
	public String getEmail2() {
		return email2;
	}
	public void setEmail2(String email2) {
		this.email2 = email2;
	}
	
	public UsuarioExterno(){
		super();
	}
}