package com.pstc.project.models.data;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
@Entity
@Table(name = "GmailLandingPage")
public class GmailLandingPageData implements Serializable {
private static final long serialVersionUID = 1L;
@Id
@Column(name ="TestDataCode")
private String testDataCode;
@Column(name ="TestDataBuilderName")
private String testDataBuilderName;
@Column(name ="Search")
private String search;
@Column(name ="CountToVerify")
private String countToVerify;
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
public String getSearch() {
	return search;
}
public void setSearch(String search) {
	this.search = search;
}
public String getCountToVerify() {
	return countToVerify;
}
public void setCountToVerify(String countToVerify) {
	this.countToVerify = countToVerify;
}

}
