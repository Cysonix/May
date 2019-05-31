package cn.edu.cuc.logindemo.http;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class DefaultAuthenticator extends Authenticator
{
  String kuser = "police2";

  String kpass = "police2";

  public DefaultAuthenticator(String username, String password) {
    this.kuser = username;
    this.kpass = password;
  }

  public PasswordAuthentication getPasswordAuthentication()
  {
    return new PasswordAuthentication(this.kuser, this.kpass.toCharArray());
  }
}
