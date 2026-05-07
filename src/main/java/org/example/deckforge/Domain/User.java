package org.example.deckforge.Domain;

public class User {
    private int userId;
    private String userName;
    private String email;
    private String password;
    private String roleType;
    private boolean currentLogin;

   public User(){
   }

   public User(int userId, String userName, String email, String password, String roleType, boolean currentLogin) {
       this.userId = userId;
       this.userName = userName;
       this.email = email;
       this.password = password;
       this.roleType = roleType;
       this.currentLogin = currentLogin;
   }

   public int getUserId() {
       return userId;
   }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getUserName() {
       return userName;
    }
    public void setUserName(String userName) {
       this.userName = userName;
    }
    public String getEmail() {
       return email;
    }
    public void setEmail(String email) {
       this.email = email;
    }
    public  String getPassword() {
       return password;
    }
    public void setPassword(String password) {
       this.password = password;
    }
    public String getRoleType() {
       return roleType;
    }
    public void setRoleType(String roleType) {
       this.roleType = roleType;
    }

    public boolean isCurrentLogin(){
        return currentLogin;
    }
    public void setCurrentLogin(boolean currentLogin){
        this.currentLogin = currentLogin;
    }
}
