package nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import data.ExternalizablePerson;

public class ExternalizablePersistenceForPerson {

	private static final String[] firstNames = new String[]{"Zachariah", "Varughese", "Abraham", "Cherian", "Philipose"};
	private static final String[] lastNames = new String[]{"Philipose", "Cherian", "Abraham", "Varughese", "Zachariah"};
	private static final String[] addresses = new String[]{"Nagpur", "Saudi", "Korba", "Dubai", "Jedda"};
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int personsCount = 1;
		String fileName = "PersonsWithExternalizableNormalIO.txt";
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))){
			List<ExternalizablePerson> persons = new ArrayList<>(personsCount);
			for(int i=0;i<personsCount;i++){
				persons.add(createPerson(i));
			}
			
			long startTime = System.nanoTime();
			for(ExternalizablePerson person:persons){
				oos.writeObject(person);
			}
			oos.flush();
			System.out.println("Time taken to write " + personsCount + " persons = " + (System.nanoTime() - startTime));
			
			for(ExternalizablePerson person:persons){
				ExternalizablePerson deserializedPerson = (ExternalizablePerson)ois.readObject();
				if(!person.equals(deserializedPerson)){
					System.out.println("The objects are not equal");
				}
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static ExternalizablePerson createPerson(int i) {
		ExternalizablePerson person = new ExternalizablePerson();
		person.setId("Person-" + i);
		person.setAge(i);
		person.setFirstName(firstNames[i%firstNames.length] + i);
		person.setLastName(lastNames[i%lastNames.length] + i);
		person.setSalary(i * 1000.0f);
		person.setAddress(addresses[i%addresses.length] + i);
		person.setContactNumber((i%2==0?"2345678901":"1234567890") + i);
		person.setMarried(true);
		return person;
	}

}
