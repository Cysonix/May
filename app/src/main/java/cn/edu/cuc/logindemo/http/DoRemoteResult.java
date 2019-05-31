package cn.edu.cuc.logindemo.http;

public class DoRemoteResult {

	public static String[] doResult(String result) {
		
		String[] resultStrings  = null;
		String[] tempStrings1 = null;
		String[] tempStrings2 = null;
		if(result !=null){
			if(result.indexOf("||") >0){
				tempStrings1 =result.split("\\|\\|");
				
				if(tempStrings1[1].indexOf("^") >0){
					tempStrings2 = tempStrings1[1].split("\\^");
					resultStrings = new String[tempStrings2.length + 1];
					resultStrings[0] = tempStrings1[0];
					
					for (int i = 0; i < tempStrings2.length; i++) {
						resultStrings[i + 1] = tempStrings2[i];
					}					
				}else{
					resultStrings = tempStrings1;
				}
			}else{
				resultStrings= new String[] { result };
			}
		}
		
		return resultStrings;
	}
}
