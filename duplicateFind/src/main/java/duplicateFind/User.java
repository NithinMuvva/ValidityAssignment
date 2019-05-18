package duplicateFind;

public class User {
	private String id;
	private String first_name;
	private String last_name;
	private String company;
	private String email;
	private String address1;
	private String address2;
	private String zip;
	private String city;
	private String state_long;
	private String state;
	private String phone;
	
	public User() {
		
	}
	public User(String id, String first_name, String last_name, String company, String email, String address1,
			String address2, String zip, String city, String state_long, String state, String phone) {
		//super();
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.company = company;
		this.email = email;
		this.address1 = address1;
		this.address2 = address2;
		this.zip = zip;
		this.city = city;
		this.state_long = state_long;
		this.state = state;
		this.phone = phone;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState_long() {
		return state_long;
	}
	public void setState_long(String state_long) {
		this.state_long = state_long;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFullName() {
		return ""+ this.first_name + " "+ this.last_name +"";
	}
	public String getAddress() {
		return ""+ this.address1 + " "+ this.address2 +"";
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + first_name + ", lastName=" + last_name + ", company=" + company
				+ ", email=" + email + ", address1=" + address1 + ", address2=" + address2 + ", zip=" + zip + ", city="
				+ city + ", state_long=" + state_long + ", state=" + state + ", phone=" + phone + "]";
	}
} 
