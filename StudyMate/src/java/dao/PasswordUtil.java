package com.studymate.dao;
import javax.crypto.SecretKeyFactory; import javax.crypto.spec.PBEKeySpec;
import java.security.*; import java.util.Base64;
/** PBKDF2 password hashing using only the JDK. Stored as iterations:salt:hash (Base64). */
public class PasswordUtil {
 private static final int IT=65536;
 private static byte[] derive(char[] pw,byte[] salt,int it){
  try{ return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(new PBEKeySpec(pw,salt,it,256)).getEncoded(); }
  catch(GeneralSecurityException e){ throw new IllegalStateException(e); } }
 public static String hash(String pw){ byte[] s=new byte[16]; new SecureRandom().nextBytes(s); Base64.Encoder b=Base64.getEncoder();
  return IT+":"+b.encodeToString(s)+":"+b.encodeToString(derive(pw.toCharArray(),s,IT)); }
 public static boolean check(String pw,String stored){
  try{ String[] p=stored.split(":"); if(p.length!=3) return false;
   return MessageDigest.isEqual(Base64.getDecoder().decode(p[2]),derive(pw.toCharArray(),Base64.getDecoder().decode(p[1]),Integer.parseInt(p[0])));
  }catch(RuntimeException e){ return false; } }
}
