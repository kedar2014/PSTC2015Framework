package com.pstc.project.models.data;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
@Entity
@Table(name = "GmailLoginPage")
public class GmailLoginPageData implements Serializable {
private static final long serialVersionUID = 1L;
@Id
@Column(name ="TestDataCode")
private String testDataCode;
@Column(name ="TestDataBuilderName")
private String testDataBuilderName;
@Column(name ="UserName")
private String userName;
@Column(name ="Password")
private String password;
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

}
