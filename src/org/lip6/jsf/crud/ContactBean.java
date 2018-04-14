package org.lip6.jsf.crud;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

@ManagedBean @RequestScoped
public class ContactBean {
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String word=null;
    public ArrayList <ContactBean> contactListSearch; 
    public ArrayList <ContactBean> contactList;
      
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String checkAccesslogin() {
		if(username.equals("admin")&& password.equals("admin"))
				return("accueil-admin");
		else if((isMissing(username))||isMissing(password)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("erreur"));	
				return("accueil");
			}
		return("accueil-utilisateur");
	}
	
	public String checkAccessPage(boolean test) {
		if(test==true)
			return("contactList.xhtml");
		else return("search.xhtml");
	}
	
	//Contrôle que les champs sont non-vides
	private boolean isMissing(String value) {
	    return((value == null) || (value.trim().isEmpty()));
	  }
	
	@PostConstruct
	public void init() {
		contactList = DAOContact.getContactsList();
	}
	
	public String searchContact() throws NamingException, Exception {
		System.out.println("ENTRE DANS SEARCH CONTACT" + word);
		contactListSearch=DAOContact.searchContact(word);
		if(!contactListSearch.isEmpty()) 
		{
			return("searchResultSuccess");
		}
		else 
		{
			return("searchResultFailed");
		}
	}
	
	public ArrayList<ContactBean> contactList() {
		return contactList;
	}
	
	public String addContact(ContactBean contact) {
		int cpt=0;
		Object o=email;
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"; 
		Pattern EMAIL_COMPILED_PATTERN = Pattern.compile(EMAIL_PATTERN);
		if ((id==0)||isMissing(firstName)||isMissing(lastName)||isMissing(email)) {
			  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Tous les champs doivent être complétés"));	
		      return("add");
		 } 
		ArrayList<ContactBean>contacts=contactList();
		for(int i=0;i<contacts.size();i++)
		{
			if((id==contacts.get(i).id)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Id déjà existant"));	
				return("add");
			}
			else if (email.equals(contacts.get(i).email)){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Email déjà existant"));	
				return("add");
			}
		}
		        Matcher matcher = EMAIL_COMPILED_PATTERN.matcher((String)o);
		         
		        if (!matcher.matches()) {
		        	cpt=1;
		            FacesMessage msg = new FacesMessage(null,"Format de mail invalide");
		            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		        }
		        if(cpt==1) return ("add");
		return DAOContact.addContact(contact);
	}
	public String deleteContactRecord(int contactId) {
		return DAOContact.deleteContactRecord(contactId);
	}
	public String editContactRecord(int contactId) {
		return DAOContact.editContact(contactId);
	}
	public String updateContact(ContactBean updateContactObj) {
		return DAOContact.updateContact(updateContactObj);
	}
}