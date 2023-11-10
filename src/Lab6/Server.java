package Lab6;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JTable;

public class Server {
	public static void main(String[] args) {
		try {
			Registry registry = LocateRegistry.createRegistry(1234);
			CRUD crud = new CRUDImpl();
			registry.bind("thaotaccsdl", crud);
			System.out.println("Server is running");
//			while (true) {
//				System.out.println(lab6_Skeleton.getData());
//				Thread.sleep(5000);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
