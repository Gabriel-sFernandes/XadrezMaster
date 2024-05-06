package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import user.User;

public class DataPersistence {

	private static final String FILE_PATH = "/userData/users.dat";

	public static void saveUsers(Map<Long, User> users) {
		try {
			File file = new File(FILE_PATH);
			File parentDir = file.getParentFile();
			if (!parentDir.exists()) {
				parentDir.mkdirs(); // Cria os diretórios se não existirem
			}

			FileOutputStream fos = new FileOutputStream(file, false);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(users);
			oos.close();
			fos.close();
			System.out.println("Usuários salvos com successo!");
		} catch (IOException e) {
			System.err.println("Erro ao salvar usuários: " + e.getMessage());
		}

	}

	public static Map<Long, User> loadUsers() {
		 Map<Long, User> users = new HashMap<>();
		    try (FileInputStream fis = new FileInputStream(FILE_PATH); ObjectInputStream ois = new ObjectInputStream(fis)) {

		        // Lê o objeto do arquivo
		        Object obj = ois.readObject();

		        // Verifica se o objeto lido é um HashMap
		        if (obj instanceof HashMap) {
		            // Se for, faz o cast para HashMap e adiciona os usuários ao mapa local
		            Map<Long, User> loadedUsers = (HashMap<Long, User>) obj;
		            users.putAll(loadedUsers);
		            System.out.println("Usuários carregados com sucesso!");
		        } else {
		            // Caso contrário, exibe uma mensagem de erro
		            System.err.println("Formato de arquivo inválido.");
		        }

		    } catch (IOException | ClassNotFoundException e) {
		        System.out.println("Erro ao carregar usuários: " + e.getMessage());
		        e.printStackTrace();
		    }
		    return users;
	}

}