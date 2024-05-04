package application.roomData;


public class WalkIn {

	private Integer roomNum;
	private String rtName;
	private String gName;
	private String arrivalDate;
	private String departureDate;
	
	public WalkIn (Integer roomNum, String rtName, String gName, String arrivalDate, String departureDate) {
		
		this.roomNum = roomNum;
		this.rtName = rtName;
		this.gName = gName;
		this.arrivalDate = arrivalDate;
		this.departureDate= departureDate;
	}
	
	public Integer getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(Integer roomNum) {
		this.roomNum = roomNum;
	}

	public String getRtName() {
		return rtName;
	}

	public void setRtName(String rtName) {
		this.rtName = rtName;
	}
	
	public String getGuestName() {
		return gName;
	}

	public void setGuestName(String gName) {
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

	
	
}
