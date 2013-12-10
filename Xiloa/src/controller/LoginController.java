package controller;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;

import security.CustomUsernamePasswordAuthenticationToken;

@Controller
@Scope("request")
public class LoginController implements PhaseListener {

	private static final long serialVersionUID = 1L;
	
	protected final Log logger = LogFactory.getLog(getClass());
	private String username;
	private String password;
	private boolean inatec = false;
	private String loggedUser;
	
	@Autowired
	private AuthenticationManager authenticationManager;	
	
	public String getLoggedUser(){
		if(loggedUser==null){
			loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
		}
		logger.info("Usuario conectado: "+loggedUser);
		return loggedUser;
	}
	
    public AuthenticationManager getAuthenticationManager() {
    	return authenticationManager;
    }
 
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    	this.authenticationManager = authenticationManager;
    }

	public String login() {
		CustomUsernamePasswordAuthenticationToken token = new CustomUsernamePasswordAuthenticationToken(username, password, inatec);
		try{
			Authentication authentication = authenticationManager.authenticate(token);
			SecurityContext sContext = SecurityContextHolder.getContext();
			sContext.setAuthentication(authentication);
			password = "";
			return "/modulos/solicitudes/solicitudes?faces-redirect=true";
		} catch(AuthenticationException loginError){
			FacesContext fContext = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Autenticación fallida: " + loginError.getMessage(), null);
			fContext.addMessage(null, message);
			return "/modulos/usuario/index.xhtml?error=1";
		}
	}
	
	public String doLogin() throws ServletException, IOException {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();		
		RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/j_spring_security_check");
		dispatcher.forward((ServletRequest) context.getRequest(),(ServletResponse) context.getResponse());
		FacesContext.getCurrentInstance().responseComplete();
		return null;
	}
	
	public String openIdAuth() throws ServletException, IOException {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/j_spring_openid_security_check");
		dispatcher.forward((ServletRequest) context.getRequest(),(ServletResponse)context.getResponse());
	    FacesContext.getCurrentInstance().responseComplete();	    
	    return null;
	}
	
	public void logout() throws ServletException, IOException {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/j_spring_security_logout");
		dispatcher.forward((ServletRequest) context.getRequest(),(ServletResponse) context.getResponse());
    	SecurityContextHolder.getContext().setAuthentication(null);
		FacesContext.getCurrentInstance().responseComplete();
	}
	
	public void updateMessages(boolean update) throws Exception {
		Exception ex = (Exception)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(WebAttributes.AUTHENTICATION_EXCEPTION);
		if (ex != null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage()));
			ex = null;
		}
	}
	
	public void afterPhase(PhaseEvent event) {
	}

	public void beforePhase(PhaseEvent event) {
		Exception e = (Exception) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(WebAttributes.AUTHENTICATION_EXCEPTION);
 
        if (e instanceof Exception) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(WebAttributes.AUTHENTICATION_EXCEPTION, null);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Autenticación fallida: " + e.getMessage(), null));
        }
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}
	
	public String getUsername(){
		return username;
	}
	 
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword(){
		return password;
	}
	 	 
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getInatec() {
		return inatec;
	}

	public void setInatec(boolean inatec) {
		this.inatec = inatec;
	}
}