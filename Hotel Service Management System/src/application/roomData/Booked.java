package application.roomData;


public class Booked {

	private Integer transactNum;
	private String gName;
	private String arrivalDate;
	private String departureDate;
	private Integer roomNum;
	
	public Booked (Integer transactNum,  String gName, String arrivalDate, String departureDate, Integer roomNum) {
		
		this.transactNum = transactNum;
		this.gName = gName;
		this.arrivalDate = arrivalDate;
		this.departureDate= departureDate;
		this.roomNum = roomNum;
	}
	
	
	public Integer getTransactNum() {
		return transactNum;
	}

	public void setTransactNum(Integer transactNum) {
		this.transactNum = transactNum;
	}

	public String getgName() {
		return gName;
	}

	public void setgName(String gName) {
		this.gName = gName;
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

	public Integer getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(Integer roomNum) {
		this.roomNum = roomNum;
	}

}
