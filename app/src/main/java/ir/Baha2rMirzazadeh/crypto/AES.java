package ir.Baha2rMirzazadeh.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.util.Base64;

import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;

public abstract class AES {

    private final static String ALGORITHM = "AES";
    private final static String MOD = "AES/CBC/PKCS5PADDING";
    private final static byte MUL = -1;
    private final static byte PLUS = 51;

    private AES() {
    }

    private static String readKey(Context context) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(context.getAssets().open("k"));
        final int size = ois.available();
        byte[] bytes = new byte[size];
        byte[] buf = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = ois.readByte();
        }
        for (int i = 0; i < size; i++) {
            buf[i] = (byte) ((bytes[i] * MUL) + PLUS);
        }
        return new String(buf);
    }

    private static String readInitVector(Context context) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(context.getAssets().open("iv"));
        final int size = ois.available();
        byte[] bytes = new byte[size];
        byte[] buf = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = ois.readByte();
        }
        for (int i = 0; i < size; i++) {
            buf[i] = (byte) ((bytes[i] * MUL) + PLUS);
        }
        return new String(buf);
    }

    public static String encrypt(String value, final String key, Context context) {
        try {
            IvParameterSpec iv = new IvParameterSpec(readInitVector(context).getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(MOD);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String encrypted, final String key, Context context) {
        try {
            IvParameterSpec iv = new IvParameterSpec(readInitVector(context).getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(MOD);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));
            return new String(original);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String defaultEncrypt(String value, Context context) {
        try {
            IvParameterSpec iv = new IvParameterSpec(readInitVector(context).getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(readKey(context).getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(MOD);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String defaultDecrypt(String encrypted, Context context) {
        try {
            IvParameterSpec iv = new IvParameterSpec(readInitVector(context).getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(readKey(context).getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(MOD);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));
            return new String(original);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}