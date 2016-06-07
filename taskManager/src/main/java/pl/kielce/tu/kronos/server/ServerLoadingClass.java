package pl.kielce.tu.kronos.server;

import java.io.IOException;

public class ServerLoadingClass {

	public static void main(String[] args) {
		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		try {
            Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/k","java -cp server.jar pl.kielce.tu.kronos.server.Server"});
		} catch (IOException e1) {
			e1.printStackTrace();
		}

        System.out.println("end");
	}
}
