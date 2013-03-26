package serializer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import data.CustomSerializable;

public class CustomSerializer {
	private static final Map<String, Integer> classToValue = new HashMap<>();
	public CustomSerializable deserialize(byte[] binary, DataInput dataInput){
		
		return null;
	}
	
	public byte[] serialize(CustomSerializable customSerializable, DataOutput dataOutput){
		try {
			Class<? extends CustomSerializable> objectClass = customSerializable.getClass();
			dataOutput.writeShort(classToValue.get(objectClass.getName()));
			
			for(Field field : objectClass.getFields()){
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private enum Keys{
		DATA_TYPE, LENGTH, FIELD_NAME, DATA;
	}
}
