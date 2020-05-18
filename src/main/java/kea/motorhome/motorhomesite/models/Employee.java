package kea.motorhome.motorhomesite.models;

import java.util.Objects;

public class Employee {

	private int employeeID;
	private Person person;
	private int accountancyID_bluff;

	public Employee() {}

	public Employee(int employeeID, Person person, int accountancyID_bluff) {
		this.employeeID = employeeID;
		this.person = person;
		this.accountancyID_bluff = accountancyID_bluff;
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

	public int getAccountancyID_bluff() {
		return accountancyID_bluff;
	}

	public void setAccountancyID_bluff(int accountancyID_bluff) {
		this.accountancyID_bluff = accountancyID_bluff;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Employee employee = (Employee) o;
		return employeeID == employee.employeeID &&
				accountancyID_bluff == employee.accountancyID_bluff &&
				Objects.equals(person, employee.person);
	}

	@Override
	public int hashCode() {
		return Objects.hash(employeeID, person, accountancyID_bluff);
	}

	@Override
	public String toString() {
		return "Employee{" +
				"employeeID=" + employeeID +
				", person=" + person +
				", accountancyID_bluff=" + accountancyID_bluff +
				'}';
	}
}