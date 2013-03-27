package bonfire.serializer;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import bonfire.serializer.data.CustomSerializable;


public class CustomSerializer {
	private static final Map<String, Integer> classToValue = new HashMap<>();
	private static final Map<String, List<Method>> classToGetters = new HashMap<>();
	private static final Map<String, List<Method>> classToSetters = new HashMap<>();
	public CustomSerializable deserialize(byte[] binary, DataInput dataInput){
		return null;
	}
	
	public byte[] serialize(CustomSerializable customSerializable){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutput dataOutput = new ObjectOutputStream(baos);
			Class<? extends CustomSerializable> objectClass = customSerializable.getClass();
			dataOutput.writeShort(classToValue.get(objectClass.getName()));
			
			for(Field field : objectClass.getFields()){
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private enum Keys{
		DATA_TYPE, LENGTH, FIELD_NAME, DATA;
	}
	
	static{
		try{
			ClassPathScanningCandidateComponentProvider scanningProvider = new ClassPathScanningCandidateComponentProvider(false);
			scanningProvider.addIncludeFilter(new AssignableTypeFilter(CustomSerializable.class));
			Set<BeanDefinition> components = scanningProvider.findCandidateComponents("bonfire/serializer");
			
			for(BeanDefinition component:components){
				String className = component.getBeanClassName();
				Class<?> customClass = Class.forName(className);
				
				for(Method method:customClass.getMethods()){
					if(method.getName().startsWith("get") || method.getName().startsWith("is")){
						
					}
						
				}
				
			}
		}catch(ClassNotFoundException cnfe){
			cnfe.printStackTrace();
		}
		
	}
}
