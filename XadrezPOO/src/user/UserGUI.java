package user;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import data.DataPersistence;
import main.GamePanel;

public class UserGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = -7652978378589697414L;

	private final JButton loginButton, registerButton;
	private final JTextField usernameField;
	private final JPasswordField passwordField;
	private final UserManager userManager;
	private Map<Long, User> users;

	public UserGUI() {
		
		super("Login de Usuário");
        userManager = new UserManager();
        loadUsers();
				
		setSize(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		  
		JPanel panel = new JPanel(new GridLayout(3, 2));

		JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Senha:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Entrar");
        registerButton = new JButton("Cadastrar");

        loginButton.addActionListener(this);
        registerButton.addActionListener(this);

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        add(panel);
        setVisible(true);
	}

	private Long generateUniqueId() {
		return System.currentTimeMillis();
	}

	public void actionPerformed(ActionEvent e) {
	    if (e.getSource() == loginButton) {
	        String username = usernameField.getText();
	        String password = new String(passwordField.getPassword());

	        if (userManager.isValidLogin(username, password)) {
	        	dispose();
	        	JFrame window = new JFrame("Simple Chess");
	        	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        	window.setResizable(false);

	        	// Add GamePanel to the window
	        	GamePanel gp = new GamePanel();
	        	window.add(gp);
	        	window.pack();

	        	window.setLocationRelativeTo(null);
	        	window.setVisible(true);

	        	gp.launchGame();


	        	
	        } else {
	            JOptionPane.showMessageDialog(this, "Useruário ou senha invalido.");
	        }

	    } else if (e.getSource() == registerButton || userManager == null) {
	        String username = usernameField.getText();
	        String password = new String(passwordField.getPassword());

	        if (!userManager.isUsernameTaken(username)) {
	            userManager.addUser(generateUniqueId(), username, password);
	            saveUsers();
	            JOptionPane.showMessageDialog(this, "Usuários registrados com sucesso!");
	        } else {
	            JOptionPane.showMessageDialog(this, "Username já existente.");
	        }
	    }	    
	}
	
	private void saveUsers() {
	    if (users != null) {
	        users.putAll(userManager.getUsers());
	        DataPersistence.saveUsers(users);
	    }
	}

	
	private void loadUsers() {
        users = DataPersistence.loadUsers();
        if (users == null) {
            users = new HashMap<>();
        } else {
            userManager.setUsers(users);
        }
    }
}