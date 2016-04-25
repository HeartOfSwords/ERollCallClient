package com.epoint.tools;

import java.io.IOException;

/**
 * 
 * @author lipengpeng
 *
 */
public class LoginTools {
	
	private static String result;

	//�÷�������URL��Method�����ȡ��¼���
	public static String GetLoginResult(String url,String method) {
		try {
			result = NetTools.GetStringFromService(url, method);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
