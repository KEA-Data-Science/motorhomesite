package kea.motorhome.motorhomesite.models;

import java.util.Objects;

public class Employee {

	private int employeeID;
	private Person person;
	private int accountancyID;

	public Employee() {}

	public Employee(int employeeID, Person person, int accountancyID_bluff) {
		this.employeeID = employeeID;
		this.person = person;
		this.accountancyID = accountancyID_bluff;
	}

	public int getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public int getAccountancyID() {
		return accountancyID;
	}

	public void setAccountancyID(int accountancyID) {
		this.accountancyID = accountancyID;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Employee employee = (Employee) o;
		return employeeID == employee.employeeID &&
			   accountancyID == employee.accountancyID &&
			   Objects.equals(person, employee.person);
	}

	@Override
	public int hashCode() {
		return Objects.hash(employeeID, person, accountancyID);
	}

	@Override
	public String toString() {
		return "Employee{" +
			   "employeeID=" + employeeID +
			   ", person=" + person +
			   ", accountancyID_bluff=" + accountancyID +
			   '}';
	}
}