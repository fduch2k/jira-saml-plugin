package com.bitium.jira.config;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.bitium.saml.X509Utils;

public class ConfigureAction extends JiraWebActionSupport {
	private static final long serialVersionUID = 1L;

	private String loginUrl;
	private String logoutUrl;
	private String entityId;
	private String x509Certificate;
	private String idpRequired;
	private String success = "";
	private String submitAction;
	
	private SAMLJiraConfig saml2Config;
	

	public void setSaml2Config(SAMLJiraConfig saml2Config) {
		this.saml2Config = saml2Config;
	}

	public ConfigureAction() {
	}
	
	public String getIdpRequired() {
		return idpRequired;
	}

	public void setIdpRequired(String idpRequired) {
		this.idpRequired = idpRequired;
	}

	public String getX509Certificate() {
		return x509Certificate;
	}

	public void setX509Certificate(String x509Certificate) {
		this.x509Certificate = x509Certificate;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	
	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}
	
	public String getSubmitAction() {
		return submitAction;
	}

	public void setSubmitAction(String submitAction) {
		this.submitAction = submitAction;
	}

	public void doValidation() {
		setSuccess("");
		if (getSubmitAction() == null || getSubmitAction().equals("")) {
			return;
		}
		if (!isSystemAdministrator()) {
			addErrorMessage(getText("saml2Plugin.admin.notAdministrator"));
		}
		if (StringUtils.isBlank(getLoginUrl())) {
			addErrorMessage(getText("saml2Plugin.admin.loginUrlEmpty"));
		} else {
			try {
				new URL(getLoginUrl());
			} catch (MalformedURLException e) {
				addErrorMessage(getText("saml2Plugin.admin.loginUrlInvalid"));
			}
		}
		if (StringUtils.isBlank(getLogoutUrl())) {
			//addActionError(getText("saml2Plugin.admin.logoutUrlEmpty"));
		} else {
			try {
				new URL(getLogoutUrl());
			} catch (MalformedURLException e) {
				addErrorMessage(getText("saml2Plugin.admin.logoutUrlInvalid"));
			}
		}
		if (StringUtils.isBlank(getEntityId())) {
			addErrorMessage(getText("saml2Plugin.admin.entityIdEmpty"));
		}
		if (StringUtils.isBlank(getX509Certificate())) {
			addErrorMessage(getText("saml2Plugin.admin.x509CertificateEmpty"));
		} else {
			try {
				X509Utils.generateX509Certificate(getX509Certificate());
			} catch (Exception e) {
				addErrorMessage(getText("saml2Plugin.admin.x509CertificateInvalid"));
			}
		}
		if (StringUtils.isBlank(getIdpRequired())) {
			setIdpRequired("false");
		} else {
			setIdpRequired("true");
		}
		
	}


	public String doExecute() throws Exception {
		if (getSubmitAction() == null || getSubmitAction().equals("")) {
			setLoginUrl(saml2Config.getLoginUrl());
			setLogoutUrl(saml2Config.getLogoutUrl());
			setEntityId(saml2Config.getIdpEntityId());
			setX509Certificate(saml2Config.getX509Certificate());
			String idpRequired = saml2Config.getIdpRequired();
			if (idpRequired != null) {
				setIdpRequired(idpRequired);
			} else {
				setIdpRequired("false");
			}
			return "success";
		}
		saml2Config.setLoginUrl(getLoginUrl());
		saml2Config.setLogoutUrl(getLogoutUrl());
		saml2Config.setEntityId(getEntityId());
		saml2Config.setX509Certificate(getX509Certificate());
		saml2Config.setIdpRequired(getIdpRequired());
		
		setSuccess("success");
		return "success";
	}

}