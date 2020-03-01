/**************************************************************************
 *
 * $RCSfile: ObjectProcess.java,v $  $Revision: 1.1 $  $Date: 2007/06/18 01:55:34 $
 *
 * $Log: ObjectProcess.java,v $
 * Revision 1.1  2007/06/18 01:55:34  qilin
 * no message
 *
 * Revision 1.2  2007/05/31 07:41:36  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:22:11  qilin
 * no message
 *
 * Revision 1.1  2007/02/27 07:20:48  yuhong
 * MR#:NMBF30-9999 2007/02/27
 * moved from afx
 *
 * Revision 1.1  2007/01/11 12:21:48  john_liu
 * 2006.01.11 by john_liu
 * MR#: BZM10-13
 *
 * Revision
 *
 * created by john_liu, 2007.01.10    for MR#: BZM10-13
 *
 ***************************************************************************/


package smartx.framework.common.utils;

import java.io.*;
import java.util.zip.*;

public class ObjectProcess extends Object {
    /**
     * transform an object to byte array.<BR>
     * Note: the object passed in must be serializable.
     *
     * @exception Exception when the object is transformed failure.
     * @param object an object.
     * @see java.io.Serializable
     * @retuen a byte array.
     */
    public static byte[] serializeObject(Object object) throws Exception {
        ByteArrayOutputStream bStream = null;

        try {
            bStream = new ByteArrayOutputStream();
            ObjectOutput s = new ObjectOutputStream(bStream);
            s.writeObject(object);
            s.flush();
        } catch (Exception e) {
            throw new Exception(e.toString());
        }

        return bStream.toByteArray();
    }

    /**
     * transform byte array to an object.
     *
     * @exception Exception when the object is transformed failure.
     * @param bytes the object serialized byte array.
     * @return an object.
     */
    public static Object externalizeObject(byte[] bytes) throws Exception {
        ByteArrayInputStream bStream = null;
        Object _object = null;

        try {
            bStream = new ByteArrayInputStream(bytes);
            ObjectInputStream s = new ObjectInputStream(bStream);
            _object = (Object) s.readObject();
        } catch (Exception e) {
            throw new Exception(e.toString());
        }

        return _object;
    }

    /**
     * transforms an object to a compressed byte array.<BR>
     * Note: the object passed in must be serializable.
     *
     * @exception Exception when the object is transformed failure.
     * @param object an object.
     * @see java.io.Serializable
     * @retuen a byte array.
     */
    public static byte[] serializeObjectWithZip(Object object) throws Exception {
        ByteArrayOutputStream bStream = null;

        try {
            bStream = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(bStream);
            ObjectOutput s = new ObjectOutputStream(gzip);
            s.writeObject(object);
            s.flush();
            gzip.close();
        } catch (Exception e) {
            throw new Exception(e.toString());
        }

        return bStream.toByteArray();
    }

    /**
     * transforms a compressed byte array to an object.
     *
     * @exception Exception when the object is transformed failure.
     * @param bytes the object serialized compressed byte array.
     * @return an object.
     */
    public static Object externalizeObjectWithZip(byte[] bytes) throws Exception {
        ByteArrayInputStream bStream = null;
        Object _object = null;

        try {
            bStream = new ByteArrayInputStream(bytes);
            GZIPInputStream gzip = new GZIPInputStream(bStream);
            ObjectInputStream s = new ObjectInputStream(gzip);
            _object = (Object) s.readObject();
            gzip.close();
        } catch (Exception e) {
            throw new Exception(e.toString());
        }

        return _object;
    }

    /**
     * converts a byte array to a string.
     *
     * @param bytes the byte array.
     * @return a byte array string.
     */
    public static String convertByteToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        String str = "";

        for (int i = 0; i < bytes.length; i++) {
            str += (char) bytes[i];
        }

        return str;
    }

    /**
     * converts a string to a byte array.
     *
     * @param str the string.
     * @return a byte array.
     */
    public static byte[] convertStringToByte(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }

        byte[] data = new byte[str.length()];

        for (int i = 0; i < str.length(); i++) {
            data[i] = (byte) str.charAt(i);
        }

        return data;
    }

    /**
     * make a clone of an Object
     * an shortcut for function serializeObject and then externalizeObject.
     *
     * @exception Exception when serilization is failure
     * @param o an Object
     * @return cloned Object
     * @see serializeObject
     * @see externalizeObject
     */
    public static Object cloneObject(Object o) throws Exception {

        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bOut);

        out.writeObject(o);

        ByteArrayInputStream bIn = new ByteArrayInputStream(bOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bIn);

        return (in.readObject());
    }

    /**
     * Compares two object by serializing them to byte array
     *
     * @exception Exception when serialization failed
     * @param obj1, obj2 two object
     * @return boolean
     */
    public static boolean isEqual(Object obj1, Object obj2) throws Exception {
        if ( (obj1 == null) && (obj2 == null)) {
            return true;
        }

        if ( (obj1 == null) || (obj2 == null)) {
            return false;
        }

        byte[] byteOfObj1 = serializeObject(obj1);
        byte[] byteOfObj2 = serializeObject(obj2);

        if (byteOfObj1.length != byteOfObj2.length) {
            return false;
        }

        for (int i = 0; i < byteOfObj1.length; i++) {
            if (byteOfObj1[i] != byteOfObj2[i]) {
                return false;
            }
        }

        return true;
    }

    // I do not want to explore the default constructor to the outside.
    /**          */
    private ObjectProcess() {
    }
}
