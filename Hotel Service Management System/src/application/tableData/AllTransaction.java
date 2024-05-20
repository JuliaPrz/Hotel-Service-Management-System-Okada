package application.tableData;

// ENCAPSULATION
public class AllTransaction {

	private Integer transactNum;
	private String guestName;
	private String date;
	private String time;
	private String arrivalDate;
	private String departureDate;
	
	public AllTransaction (Integer transactNum, String guestName, String date, String time, String arrivalDate, String departureDate) {
		
		this.transactNum = transactNum;
		this.guestName = guestName;
		this.date = date;
		this.time = time;
		this.arrivalDate = arrivalDate;
		this.departureDate= departureDate;

	}
	
	public Integer getTransactNum() {
		return transactNum;
	}

	public void setTransactNum(Integer transactNum) {
		this.transactNum = transactNum;
	}

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}
	
}
	

