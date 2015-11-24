package com.pstc.project.models.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Page_Login")
public class Login implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "TestDataCode")
	private String testDataCode;
	@Column(name = "TestDataBuilderName")
	private String testDataBuilderName;
	@Column(name = "UserName")
	private String userName;
	@Column(name = "Password")
	private String password;
	@Column(name = "RememberMe")
	private String rememberMe;
	@Column(name = "Table1")
	private String table1;

	public String getTestDataCode() {
		return testDataCode;
	}

	public void setTestDataCode(String testDataCode) {
		this.testDataCode = testDataCode;
	}

	public String getTestDataBuilderName() {
		return testDataBuilderName;
	}

	public void setTestDataBuilderName(String testDataBuilderName) {
		this.testDataBuilderName = testDataBuilderName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(String rememberMe) {
		this.rememberMe = rememberMe;
	}

	public String getTable1() {
		return table1;
	}

	public void setTable1(String table1) {
		this.table1 = table1;
	}

}
