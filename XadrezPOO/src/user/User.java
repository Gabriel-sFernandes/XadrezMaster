package user;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable{
	
	private static final long serialVersionUID = -3130983940835193059L;
	private long id;
    private String nickname;
    private String password;
    
	public User(long id, String nickname, String password) {
		super();
		this.id = id;
		this.nickname = nickname;
		this.password = password;
	}
	
	public long getId() {
		return id;
	}

	public String getNickname() {
		return nickname;
	}
	
	public void setUsername(String nickname) {
		this.nickname = nickname;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

		
	@Override
	public int hashCode() {
		return Objects.hash(id, nickname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return id == other.id && Objects.equals(nickname, other.nickname);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", nickname=" + nickname + ", password=" + password + "]";
	}

	
}
