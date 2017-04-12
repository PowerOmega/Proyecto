package Servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidormain {

	static final String nombreServidor = "SRodriguez";
	static final int puertoServidor = 9999;
	private static boolean sigue = true;
	private static Socket skClient;

	public static void main(String[] args) {

		ServerSocket skServidor;
		try {
			skServidor = new ServerSocket(puertoServidor);
			// Instanciamso un socket
			System.out.println("Abriendo el puerto : " + puertoServidor);

			while (sigue) {
				skClient = skServidor.accept(); // Aceptara la conexion en el socket, en caso de no estar lleno

				ServidorServices sc = new ServidorServices(skClient);
				sc.start();
				// skServidor.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
