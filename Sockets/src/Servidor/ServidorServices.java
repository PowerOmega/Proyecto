package Servidor;

import java.io.*;
import java.net.Socket;

public class ServidorServices extends Thread {

	static final String nombreServidor = "SRodriguez";
	String sDirectorio = "src\\Servidor\\Ficheros\\";
	File fichero;
	static final int puertoServidor = 9999;
	static final String user = "Admin";
	static final String password = "notlogin";
	private static DataOutputStream DOStream;
	private static DataInputStream DIStream;
	static Socket skclient;
	static boolean sigue = true;

	// Inicializamos los Data
	public ServidorServices(Socket sk) {

		try {
			DOStream = new DataOutputStream(sk.getOutputStream());
			DIStream = new DataInputStream(sk.getInputStream());
			skclient = sk;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void run() {
		try {
			// Envia byte1
			DOStream.writeByte(1);
			// Envia nombre del servidor
			DOStream.writeUTF(nombreServidor);

			while (sigue) {

				// Espera a recibir un byte
				byte opcion = DIStream.readByte();
				switch (opcion) {
				case 2:
					DOStream.writeUTF("Comprobando su usuario... ");
					if (comprobarUsuario()) {
						// Enviamos byte1 en caso de login con exito
						DOStream.writeByte(1);
						// DOStream.flush();
						// Enviamos una bienvenida con su usuario ya logueado
						DOStream.writeUTF("Bienvenido : " + user);
						DOStream.flush();

					} else {
						// Enviamos byte0 en caso de login sin exito
						DOStream.writeInt(0);
						ServidorServices.sleep(5);
						DOStream.writeUTF("Usuario no existente! Se cerrara este servidor...");
						DOStream.flush();
						skclient.close();

					}
					break;

				case 10:
					System.out.println("Byte[" + opcion + "]");

					File f = new File(sDirectorio);
					File[] ficheros = f.listFiles();

					DOStream.writeInt(ficheros.length);
					DOStream.writeUTF("\tLista de ficheros");
					for (int x = 0; x < ficheros.length; x++) {
						DOStream.writeUTF(ficheros[x].getName());
						DOStream.writeLong(ficheros[x].length());

					}
					DOStream.flush();
					break;
				case 15:
					String nombreFichero;
					System.out.println("Byte[" + opcion + "]");
					nombreFichero = DIStream.readUTF();
					fichero = new File(sDirectorio + nombreFichero);
					// Busca el fichero con el nombre recibido
					if (fichero.exists()) {
						DOStream.writeByte(1);
						DOStream.writeUTF(fichero.getName());
						DOStream.writeLong(fichero.length());
						DOStream.flush();
					} else {
						DOStream.writeByte(0);
						System.out.println("Fichero no encontrado saliendo de la aplicacion...");
					}

					break;

				case 16:
					try {
						File fichero = new File(this.fichero.getAbsolutePath().toString());
						System.out.println("Enviará el fichero <" + fichero.getAbsolutePath() + ">");
						FileInputStream fileInput = new FileInputStream(fichero);
						BufferedInputStream bi = new BufferedInputStream(fileInput);

						long lfic = fichero.length();

						byte b[] = new byte[(int) lfic];
						bi.read(b); // lee el fichero completo en b
						DOStream.write(b); // enviamos todo el fichero
						DOStream.flush(); // vacia el buffer
						bi.close();
						DOStream.close();
						skclient.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					break;

				}

			}

		} catch (

		IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean comprobarUsuario() {

		try {
			DOStream.writeUTF("Iniciando sesion...");

			String login = DIStream.readUTF();
			String[] ArrayDatos = login.split(";");

			if (ArrayDatos[0].equals(user)) {
				if (ArrayDatos[1].equals(password)) {
					return true;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

}
