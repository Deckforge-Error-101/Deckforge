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
      if (userId > 0) {
         this.userId = userId;
      }
    }
    public String getUserName() {
       return userName;
    }
    public void setUserName(String userName) throws Exception {
      if (userName != null) {
         this.userName = userName;
      } throw new Exception("Bruger skal have et navn");
    }
    public String getEmail() {
       return email;
    }
    public void setEmail(String email) throws Exception {
      if (email != null) {
         this.email = email;
      } throw new Exception("Der skal være en mail");
    }
    public  String getPassword() {
       return password;
    }
    public void setPassword(String password) throws Exception {
      if (password != null) {
         this.password = password;
      } throw new Exception("Der skal være et password");
    }
    public String getRoleType() {
       return roleType;
    }
    public void setRoleType(String roleType) throws Exception {
      if (roleType != null) {
         this.roleType = roleType;
      } throw new Exception("Brugeren skal have en rolle");
    }

    public boolean isCurrentLogin(){
        return currentLogin;
    }
    public void setCurrentLogin(boolean currentLogin){
        this.currentLogin = currentLogin;
    }
}
