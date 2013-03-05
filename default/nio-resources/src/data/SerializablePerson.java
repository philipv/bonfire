package data;

import java.io.Serializable;

public class SerializablePerson implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String id;
	private int age;
	private String firstName;
	private String lastName;
	private float salary;
	private String address;
	private String contactNumber;
	private boolean married;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
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
	public float getSalary() {
		return salary;
	}
	public void setSalary(float salary) {
		this.salary = salary;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public boolean isMarried() {
		return married;
	}
	public void setMarried(boolean married) {
		this.married = married;
	}
	@Override
	public int hashCode() {
		
		return 31 * ((id != null?id.hashCode():0) + age + (firstName != null?firstName.hashCode():0)
				+ (lastName != null?lastName.hashCode():0) + Float.floatToIntBits(salary)
				+ (address != null?address.hashCode():0) + (contactNumber != null?contactNumber.hashCode():0)
				+ Boolean.valueOf(married).hashCode());
	}
	@Override
	public boolean equals(Object obj) {
		if(obj==null || !(obj instanceof SerializablePerson))
			return false;
		SerializablePerson anotherPerson = (SerializablePerson)obj;
		
		if((id != null && anotherPerson.getId() == null) || !id.equals(anotherPerson.getId()))
			return false;
		else if(age != anotherPerson.getAge())
			return false;
		else if((firstName != null && anotherPerson.getFirstName() == null) || !firstName.equals(anotherPerson.getFirstName()))
			return false;
		else if((lastName != null && anotherPerson.getLastName() == null) || !lastName.equals(anotherPerson.getLastName()))
			return false;
		else if(Float.floatToIntBits(salary) != Float.floatToIntBits(anotherPerson.getSalary()))
			return false;
		else if((address != null && anotherPerson.getAddress() == null) || !address.equals(anotherPerson.getAddress()))
			return false;
		else if((contactNumber != null && anotherPerson.getContactNumber() == null) || !contactNumber.equals(anotherPerson.getContactNumber()))
			return false;
		
		return married == anotherPerson.isMarried();
	}
	
}
