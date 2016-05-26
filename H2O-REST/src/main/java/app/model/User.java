package app.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

public class User {

  @Id
  private String id;
  private String firstName;
  private String lastName;
  private String email;
  private String contact;
  private boolean isMale;
  
  @DateTimeFormat(pattern = "dd/MM/yyyy")
  private Date dob;

 
public User(){}

  public User(String firstName, String lastName, String email, String contact, boolean isMale, Date dob){
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.contact = contact;
    this.isMale = isMale;
    this.dob = dob;
  }

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  
  public String getFirstName() {
	return firstName;
}

public void setFirstName(String firstName) {
	this.firstName = firstName;
}

public String getLastName() {
	return lastName;
}

public void setLastName(String lastName) {
	this.lastName = lastName;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getContact() {
	return contact;
}

public void setContact(String contact) {
	this.contact = contact;
}

public boolean isMale() {
	return isMale;
}

public void setMale(boolean isMale) {
	this.isMale = isMale;
}

public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

}
