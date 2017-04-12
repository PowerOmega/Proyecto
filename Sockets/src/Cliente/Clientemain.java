package Cliente;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Clientemain {

	static final String HOST = "localhost";
	static final int PUERTO = 9999;
	protected static Socket socket;
	protected DataOutputStream DOStream;
	protected DataInputStream DIStream;
	protected Scanner leer;
	String nombreFichero;
	Long tamañoFichero;

	public static void main(String[] args) {

		Clientemain cli = new Clientemain();
		cli.Cliente1();
	}

	public void Cliente1() {
		try {
			socket = new Socket(HOST, PUERTO);
			DOStream = new DataOutputStream(socket.getOutputStream());
			DIStream = new DataInputStream(socket.getInputStream());
			conectarServer();
			ListadoFiles();
			// DIStream.close();
			// DOStream.close();
			// socket.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void conectarServer() {
		try {
			// Recibe byte1
			System.out.println("Byte[" + DIStream.readByte() + "]:");
			// Recibe nombre del servidor
			System.out.print("Conectando con el server : " + DIStream.readUTF());
			timingadd();
			iniciarSesionServer();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void iniciarSesionServer() {
		try {
			// Envia byte2
			DOStream.writeByte(2);
			// Envia el String con usuario;contraseña
			DOStream.writeUTF("Admin;notlogin");
			DOStream.flush();

			// Mostramos los datos
			System.out.println(DIStream.readUTF());
			System.out.println(DIStream.readUTF());
			System.out.print("Byte[" + DIStream.readByte() + "]");
			System.out.println("\t" + DIStream.readUTF());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void ListadoFiles() {
		try {
			DOStream.writeByte(10);
			int numeroFicheros = DIStream.readInt();
			System.out.println(DIStream.readUTF() + " : " + numeroFicheros);
			System.out.println("    Nombre \t\t Tamaño");
			for (int i = 1; i < numeroFicheros + 1; i++) {
				System.out.println(i + ".  " + DIStream.readUTF() + "\t " + DIStream.readLong());
			}
			DOStream.flush();
			descargaFichero();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void descargaFichero() {
		leer = new Scanner(System.in);
		String opcion = "";
		System.out.print("Desea descargar algun fichero de la lista S/N : ");
		opcion = leer.next();

		if (opcion.equals("S")) {
			try {
				System.out.println("Escribe el nombre del fichero a descargar");
				opcion = leer.next();
				DOStream.writeByte(15);
				DOStream.writeUTF(opcion);
				comprueboFichero();
				System.out.println("Confirmar la descarga? S/N");
				opcion = leer.next();
				if (opcion.equals("S")) {
					DOStream.writeByte(16);
					System.out.println("Descargando...");
					reciboFichero();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void comprueboFichero() {
		try {
			// Si el fichero existe
			byte reciboByte = DIStream.readByte();
			this.nombreFichero = DIStream.readUTF();
			this.tamañoFichero = DIStream.readLong();
			if (reciboByte == 1) {
				System.out.println("Byte[" + reciboByte + "] : " + nombreFichero + "\t " + tamañoFichero);

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void reciboFichero() {
		try {

			File fichero = new File(
					"C:/Users/el_19/Documents/Proyectos/Eclipse/Sockets/src/Cliente/Ficheros/" + nombreFichero);
			long lfic = tamañoFichero;

			FileOutputStream fileOutput = new FileOutputStream(fichero);
			BufferedOutputStream bo = new BufferedOutputStream(fileOutput);

			byte b[] = new byte[(int) lfic];

			
			DIStream.read();
			bo.write(b);
			bo.close();
			System.out.println("Finalizado!");
		} catch (Exception e) {

		}
	}

	public void timingadd() {

		try {
			for (int i = 0; i < 3; i++) {
				System.out.print(".");
				Thread.sleep(1000);
			}
		} catch (Exception e) {

		}

	}

}