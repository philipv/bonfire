package nio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import data.Person;

public class BlockDataPersistenceForPerson {

	private static final String[] firstNames = new String[]{"Zachariah", "Varughese", "Abraham", "Cherian", "Philipose"};
	private static final String[] lastNames = new String[]{"Philipose", "Cherian", "Abraham", "Varughese", "Zachariah"};
	private static final String[] addresses = new String[]{"Nagpur", "Saudi", "Korba", "Dubai", "Jedda"};
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int personsCount = 1;
		String fileName = "PersonsWithNIO.txt";
		List<Integer> sizes = new ArrayList<>(personsCount);
		try(
				FileChannel outFileChannel = new FileOutputStream(fileName).getChannel();
				FileChannel inFileChannel = new FileInputStream(fileName).getChannel()){
			List<Person> persons = new ArrayList<>(personsCount);
			for(int i=0;i<personsCount;i++){
				persons.add(createPerson(i));
			}
			
			long startTime = System.nanoTime();
			for(Person person:persons){
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				person.writeExternal(oos);
				oos.flush();
				ByteBuffer byteBuffer = ByteBuffer.wrap(baos.toByteArray());
				outFileChannel.write(byteBuffer);
				sizes.add(byteBuffer.limit());
				oos.close();
			}
			System.out.println("Time taken to write " + personsCount + " persons = " + (System.nanoTime() - startTime));
			
			int i = 0;
			for(Person person:persons){
				ByteBuffer buffer = ByteBuffer.allocate(sizes.get(i));
				inFileChannel.read(buffer);
				ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
				ObjectInputStream ois = new ObjectInputStream(bais);
				Person deserializedPerson = new Person();
				deserializedPerson.readExternal(ois);
				if(!person.equals(deserializedPerson)){
					System.out.println("The objects are not equal");
				}
				ois.close();
				i++;
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	private static Person createPerson(int i) {
		Person person = new Person();
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
