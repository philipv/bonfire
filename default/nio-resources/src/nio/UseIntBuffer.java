package nio;
// $Id$

import java.nio.*;

public class UseIntBuffer
{
  static public void main( String args[] ) throws Exception {
	  IntBuffer buffer = IntBuffer.allocate( 10 );

    for (int i=0; i<buffer.capacity(); ++i) {
    	System.out.println("Putting " + i +" into buffer");
    	buffer.put( i );
    }

    buffer.flip();

    while (buffer.hasRemaining()) {
      int i = buffer.get();
      System.out.println( i );
    }
  }
}
