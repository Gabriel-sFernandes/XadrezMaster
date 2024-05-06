package user;

public interface UserInterface {
	public void addUser(Long id, String username, String password);
	public void removeUser(Long id);
	public boolean isValidLogin(String nickname, String password);
	public void updateUserPassword(Long id, String newPassword);
}
