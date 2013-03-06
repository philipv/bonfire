package data;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import util.Externalizer;

public class ExternalizablePerson implements Externalizable{

	private transient String id;
	private transient int age;
	private transient String firstName;
	private transient String lastName;
	private transient float salary;
	private transient String address;
	private transient String contactNumber;
	private transient boolean married;
	
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
		if(obj==null || !(obj instanceof ExternalizablePerson))
			return false;
		ExternalizablePerson anotherPerson = (ExternalizablePerson)obj;
		
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
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		Externalizer.writeString(out, id);
		Externalizer.writeInteger(out, age);
		Externalizer.writeString(out, firstName);
		Externalizer.writeString(out, lastName);
		Externalizer.writeFloat(out, salary);
		Externalizer.writeString(out, address);
		Externalizer.writeString(out, contactNumber);
		Externalizer.writeBoolean(out, married);
	}
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		id = Externalizer.readString(in);
		age = Externalizer.readInteger(in);
		firstName = Externalizer.readString(in);
		lastName = Externalizer.readString(in);
		salary = Externalizer.readFloat(in);
		address = Externalizer.readString(in);
		contactNumber = Externalizer.readString(in);
		married = Externalizer.readBoolean(in);
	}
	
}
