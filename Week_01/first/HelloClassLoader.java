package demo01;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HelloClassLoader extends ClassLoader {

    private final static int DECODE_BYTE = 255;
    private final static String FILE_NAME = "F:\\lwy\\workspace\\ByteCode\\src\\demo01\\Hello.xlass";

    public static void main(String[] args) {
        try {
            //获取Hello实例对象
            Object object = new HelloClassLoader().findClass("Hello").newInstance();
            Class<?> classType = object.getClass();

            //获取Hello实例对象中的hello()方法
            Method addMethod = classType.getMethod("hello");

            //执行Hello实例对象中的hello()方法
            addMethod.invoke(object);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        File file = new File(FILE_NAME);
        InputStream in = null;
        byte[] srcBytes = null;

        //读取hello.xlass文件到srcBytes数组中
        try {
            in = new FileInputStream(file);
            srcBytes = new byte[in.available()];
            in.read(srcBytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (null == srcBytes) {
            throw new ClassNotFoundException();
        }

        //通过255-x解码byte数组
        byte[] bytesDecode = decodeByte(srcBytes);

        return defineClass(name, bytesDecode, 0, bytesDecode.length);
    }

    private byte[] decodeByte(byte[] srcBytes) {
        byte[] desBytes = new byte[srcBytes.length];
        for (int i = 0; i < srcBytes.length; i++) {
            desBytes[i] = (byte) (DECODE_BYTE - srcBytes[i]);
        }

        return desBytes;
    }
}
