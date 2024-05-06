package user;

import java.util.HashMap;
import java.util.Map;

public class UserManager implements UserInterface{
	
	private HashMap<Long, User> users;

	public UserManager() {
        users = new HashMap<>();
    }

    public Map<Long, User> getUsers() {
        return this.users;
    }

	public void addUser(Long id, String username, String password) {
        User user = new User(id, username, password);
        users.put(id, user);
    }
    
    public void removeUser(Long id) {
        users.remove(id);
    }

    public boolean isValidLogin(String nickname, String password) {
        for (User user : users.values()) {
            if (user.getNickname().equals(nickname) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public void updateUserPassword(Long id, String newPassword) {
        User user = users.get(id);
        if (user != null) {
            user.setPassword(newPassword);
        }
    }
    
    public boolean isUsernameTaken(String username) {
        for (User user : users.values()) {
            if (user.getNickname().equals(username)) {
                return true;
            }
        }
        return false;
    }

	public HashMap<Long, User> setUsers(Map<Long, User> users) {
		return this.users;		
	}
}
