package org.lip6.jsf.crud;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;



public class DAOContact {
private final static String RESOURCE_JDBC = "java:comp/env/jdbc/dsMyDB";
    
    public static String addContact(ContactBean contact) {
		
    	int saveResult = 0;
		String navigationResult = "";
    	
		try {
            Context lContext = new InitialContext();
            DataSource lDataSource = (DataSource) lContext.lookup(RESOURCE_JDBC);
            Connection lConnection  = lDataSource.getConnection();
            
            // adding a new contact
            PreparedStatement lPreparedStatementCreation = 
            		lConnection.prepareStatement
            		("INSERT INTO contact(ID_CONTACT, FIRSTNAME, LASTNAME, EMAIL) VALUES(?, ?, ?, ?)");
            lPreparedStatementCreation.setInt(1, contact.getId());
            lPreparedStatementCreation.setString(2, contact.getFirstName());
            lPreparedStatementCreation.setString(3, contact.getLastName());
            lPreparedStatementCreation.setString(4, contact.getEmail());
            saveResult = lPreparedStatementCreation.executeUpdate();
            lConnection.close();//
        } 
		catch (SQLException e) {
        	e.printStackTrace();
        } 
		catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(saveResult !=0) {
			navigationResult = "contactList.xhtml?faces-redirect=true";
		} else {
			navigationResult = "add.xhtml?faces-redirect=true";
		}
		return navigationResult;
    }
    
    public static ArrayList <ContactBean> getContactsList(){
    	//connection
    	System.out.println("entre dans le DAO");
        
        ArrayList <ContactBean> contactsList = new ArrayList<ContactBean>();
		
        try {
            Context lContext = new InitialContext();
            DataSource lDataSource = (DataSource) lContext.lookup(RESOURCE_JDBC);
            Connection lConnection  = lDataSource.getConnection();
			ResultSet rs = lConnection.createStatement().executeQuery("SELECT * from contact");
			while (rs.next()) {
				ContactBean contact = new ContactBean();
				contact.setId(rs.getInt("ID_CONTACT"));
				contact.setFirstName(rs.getString("FIRSTNAME"));
				contact.setLastName(rs.getString("LASTNAME"));
				contact.setEmail(rs.getString("EMAIL"));
				contactsList.add(contact);
				System.out.println(contact);
			}
			System.out.println("Total Records Fetched : " + contactsList.size());
			lConnection.close();
		} 
        catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contactsList;
	}
    
    
    public static String deleteContactRecord(int contactId){
		System.out.println("deleteContactRecord() : Contact Id: " + contactId);
		try {
			Context lContext = new InitialContext();
            DataSource lDataSource = (DataSource) lContext.lookup(RESOURCE_JDBC);
            Connection lConnection  = lDataSource.getConnection();
            PreparedStatement pstmt;
            pstmt = lConnection.prepareStatement("DELETE FROM contact WHERE ID_CONTACT="+contactId); 
			pstmt.executeUpdate();  
			pstmt.executeUpdate();  
			lConnection.close();
		} catch(Exception sqlException){
			sqlException.printStackTrace();
		}
		return "/contactList.xhtml?faces-redirect=true";
	}
    /* Method Used To Edit Student Record In Database */
	public static String editContact(int contactId) {
		ContactBean editRecord = null;
		System.out.println("editContactRecord() : contact Id: " + contactId);

		/* Setting The Particular Student Details In Session */
		Map<String,Object> sessionMapObj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

		try {
			Context lContext = new InitialContext();
            DataSource lDataSource = (DataSource) lContext.lookup(RESOURCE_JDBC);
            Connection lConnection  = lDataSource.getConnection();
            PreparedStatement stmtObj = lConnection.prepareStatement("select * from contact where ID_CONTACT = "+contactId);   
            ResultSet resultSetObj = stmtObj.executeQuery();    
			if(resultSetObj != null) {
				resultSetObj.next();
				editRecord = new ContactBean(); 
				editRecord.setId(resultSetObj.getInt("ID_CONTACT"));
				editRecord.setFirstName(resultSetObj.getString("FIRSTNAME"));
				editRecord.setLastName(resultSetObj.getString("LASTNAME"));
				editRecord.setEmail(resultSetObj.getString("EMAIL"));
				
			}
			sessionMapObj.put("editRecordObj", editRecord);
			lConnection.close();
		} catch(Exception sqlException) {
			sqlException.printStackTrace();
		}
		return "/contactUpdate.xhtml?faces-redirect=true";
	}
    /* Method Used To Update Student Record In Database */
	public static String updateContact(ContactBean updateContactObj) {
		try {
			Context lContext = new InitialContext();
            DataSource lDataSource = (DataSource) lContext.lookup(RESOURCE_JDBC);
            Connection lConnection  = lDataSource.getConnection();
            PreparedStatement pstmt = lConnection.prepareStatement("update contact set FIRSTNAME=?, LASTNAME=?, EMAIL=? WHERE ID_contact=?");
            // updating a new contact
			    
			  
			pstmt.setString(1,updateContactObj.getFirstName());  
			pstmt.setString(2,updateContactObj.getLastName());  
			pstmt.setString(3,updateContactObj.getEmail());
			pstmt.setInt(4,updateContactObj.getId());
			 
			pstmt.executeUpdate();
			lConnection.close();			
		} catch(Exception sqlException) {
			sqlException.printStackTrace();
		}
		return "/contactList.xhtml?faces-redirect=true";
	}
     
	public static ArrayList<ContactBean> searchContact(String word) {
   	 
   	 System.out.println("Entre dans search contact DAO");
   	ArrayList <ContactBean> EDITcontacts = new ArrayList<ContactBean>();
   	
		/* Setting The Particular Contact Details In Session */
		Map<String,Object> sessionMapObj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		
   	//List<Contact>contacts = new ArrayList<Contact>();
		System.out.println("DAO avant TRY" + word);
		try {
	        Context lContext = new InitialContext();
	        DataSource lDataSource = (DataSource) lContext.lookup(RESOURCE_JDBC);
	        Connection lConnection  = lDataSource.getConnection();
			System.out.println("DAO apres TRY" + word);
			final PreparedStatement lPreparedStatementContact = lConnection.prepareStatement("SELECT ID_CONTACT, LASTNAME, FIRSTNAME, EMAIL FROM contact WHERE ID_CONTACT LIKE ? OR LASTNAME LIKE ? OR FIRSTNAME LIKE ? OR EMAIL LIKE ?");
			lPreparedStatementContact.setString(1, "%" + word + "%");
			lPreparedStatementContact.setString(2, "%" + word + "%");
			lPreparedStatementContact.setString(3, "%" + word + "%");
			lPreparedStatementContact.setString(4, "%" + word + "%");
			ResultSet rsContact = lPreparedStatementContact.executeQuery();
			
			System.out.println("DAO apres requete " + word);
			
			while (rsContact.next()) {

				ContactBean contact = new ContactBean();
				contact.setId(rsContact.getInt("ID_CONTACT"));
				contact.setFirstName(rsContact.getString("FIRSTNAME"));
				contact.setLastName(rsContact.getString("LASTNAME"));
				contact.setEmail(rsContact.getString("EMAIL"));
				
				System.out.println("FIRST NAME :" + contact.getFirstName());
				
				EDITcontacts.add(contact);
			}
			sessionMapObj.put("editRecordcontacts", EDITcontacts);
		} 
       catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EDITcontacts;
   	}
    
}
