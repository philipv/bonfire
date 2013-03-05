package util;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

public class Externalizer {
public static void writeString(ObjectOutput out, String string) throws IOException{
        
        if(string!=null){
            byte[] b = string.getBytes();
            out.writeInt(b.length);
            out.write(b);
            //System.out.println("writeString length "+b.length+" and string "+string);
        }else{
            out.writeInt(-1);
        }
    }
   
    public static String readString(ObjectInput in) throws IOException, ClassNotFoundException{
        int len = in.readInt();
        if(len>=0){
            //long t1 = System.nanoTime();
            byte[] b = new byte[len];
            int read = 0;//DATS-4813
            while(read!=len){//the read is restricted to 995 bytes
                read += in.read(b, read, len-read);
            }
            //System.out.println("readString length "+len+" in "+ (System.nanoTime()-t1)/1000L +" us => data["+new String(b)+"]");
            return (new String(b));
        }
        return null;
    }
    
    public static void writeDate(ObjectOutput out, Date date) throws IOException{
        out.writeLong(((date==null)?Long.MIN_VALUE: date.getTime()));
    }
    
    public static Date readDate(ObjectInput in) throws IOException, ClassNotFoundException{
        Long longVal = in.readLong();
        return (longVal.equals(Long.MIN_VALUE)? null : new Date(longVal));
    }
    
    public static void writeInteger(ObjectOutput out, Integer i) throws IOException{
        out.writeInt(((i==null)?Integer.MIN_VALUE : i));
    }
    
    public static Integer readInteger(ObjectInput in) throws IOException, ClassNotFoundException{
        Integer intVal = in.readInt();
        return (intVal.equals(Integer.MIN_VALUE) ? null : intVal);
    }  

    public static void writeLong(ObjectOutput out, Long l) throws IOException{
        out.writeLong(((l==null)? Long.MIN_VALUE : l));
    }
    
    public static Long readLong(ObjectInput in) throws IOException, ClassNotFoundException{
        Long longVal = in.readLong();
        return ((longVal.equals(Long.MIN_VALUE)?null : longVal));
    }  
    
    public static void writeDouble(ObjectOutput out, Double d) throws IOException{
        out.writeDouble(((d==null)? Double.NaN : d));
    }
    
    public static Double readDouble(ObjectInput in) throws IOException, ClassNotFoundException{
        Double dVal = in.readDouble();
        return dVal.equals(Double.NaN) ? null : dVal;
    }
    
    public static void writeFloat(ObjectOutput out, Float f) throws IOException{
    	out.writeFloat(((f==null)? Float.NaN : f));
    }
    
    public static Float readFloat(ObjectInput in) throws IOException, ClassNotFoundException{
    	Float fVal = in.readFloat();
    	return fVal.equals(Float.NaN) ? null : fVal;
    }
    
    public static void writeBoolean(ObjectOutput out, Boolean b) throws IOException{
        out.writeInt(((b==null)? -1 : ((b.equals(Boolean.TRUE))? 1 : 0)));
    }
    
    public static Boolean readBoolean(ObjectInput in) throws IOException, ClassNotFoundException{
        Integer intVal = in.readInt();
        return (intVal.equals(-1)? null : (intVal.equals(1)?Boolean.TRUE:Boolean.FALSE));
    }
}
