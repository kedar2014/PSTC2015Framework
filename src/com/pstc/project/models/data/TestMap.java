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
	@Column(name = "Page_Home")
	private String page_Home;
	@Column(name = "Page_Login")
	private String page_Login;

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public String getPage_Home() {
		return page_Home;
	}

	public void setPage_Home(String page_Home) {
		this.page_Home = page_Home;
	}

	public String getPage_Login() {
		return page_Login;
	}

	public void setPage_Login(String page_Login) {
		this.page_Login = page_Login;
	}

}
