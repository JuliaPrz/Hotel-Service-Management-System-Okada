package application.tableData;


public class Receptionist {

	private Integer empID;
	private String name;
	private String birthdate;
	private String contactNum;
	
	public Receptionist (Integer empID,  String name, String birthdate, String contactNum) {
		
		this.empID = empID;
		this.name = name;
		this.birthdate = birthdate;
		this.contactNum = contactNum;	
	}
	
	public Integer getEmpID() {
		return empID;
	}

	public void setEmpID(Integer empID) {
		this.empID = empID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getContactNum() {
		return contactNum;
	}

	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}
	


}
