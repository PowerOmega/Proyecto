package Servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServidorMenu {

	static Socket skCliente;
	private static DataOutputStream DOStream;
	private static DataInputStream DIStream;

	public ServidorMenu(Socket sk) {

		try {
			DOStream = new DataOutputStream(sk.getOutputStream());
			DIStream = new DataInputStream(sk.getInputStream());
			skCliente = sk;
			menu(skCliente);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void menu(Socket sk) {

		try {
			DOStream.writeUTF("Menu del Servidor: " + "\n\t1.Listar Ficheros" + "\n\t2.Descargar un fichero"
					+ "\n\t3.Desconexion");
			DOStream.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
