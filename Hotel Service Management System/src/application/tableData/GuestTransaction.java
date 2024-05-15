package application.tableData;


public class GuestTransaction {

	private Integer transactNum;
	private String arrivalDate;
	private String departureDate;
	private Double totalPayment;
	
	public GuestTransaction (Integer transactNum, String arrivalDate, String departureDate, Double totalPayment) {
		
		this.transactNum = transactNum;
		this.arrivalDate = arrivalDate;
		this.departureDate= departureDate;
		this.totalPayment = totalPayment;
	}
	
	
	public Integer getTransactNum() {
		return transactNum;
	}

	public void setTransactNum(Integer transactNum) {
		this.transactNum = transactNum;
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

	public Double getTotalPayment() {
		return totalPayment;
	}


	public void setTotalPayment(Double totalPayment) {
		this.totalPayment = totalPayment;
	}

}
