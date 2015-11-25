package com.pstc.project.models.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TestMap")
public class TestMap implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ScenarioName")
	private String scenarioName;
	
	@Column(name = "AmazonLoginPage")
	private String amazonLoginPage;
	@Column(name = "GmailLoginPage")
	private String gmailLoginPage;
	@Column(name = "GmailLandingPage")
	private String gmailLandingPage;
			


	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public String getAmazonLoginPage() {
		return amazonLoginPage;
	}

	public void setAmazonLoginPage(String amazonLoginPage) {
		this.amazonLoginPage = amazonLoginPage;
	}

	public String getGmailLoginPage() {
		return gmailLoginPage;
	}

	public void setGmailLoginPage(String gmailLoginPage) {
		this.gmailLoginPage = gmailLoginPage;
	}

	public String getGmailLandingPage() {
		return gmailLandingPage;
	}

	public void setGmailLandingPage(String gmailLandingPage) {
		this.gmailLandingPage = gmailLandingPage;
	}

	
}
