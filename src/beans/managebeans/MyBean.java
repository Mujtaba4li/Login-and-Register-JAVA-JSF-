package beans.managebeans;

import beans.backingbeans.UserReg;
import db.DbConnection;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
public class MyBean {
    private UserReg user;
    private DbConnection connection;

    public MyBean() {
        user =new UserReg();
        connection =new DbConnection();
    }
//  Register user
    public String registerUser(){
        connection.createAccount(user.getUsername(),user.getFirstname(),user.getLastname(),user.getEmail(),user.getPassword(),user.getBirthday(),user.getGender());
        System.out.println("user data is:"+user.getUsername()+user.getFirstname()+user.getLastname()+user.getEmail()+user.getPassword()+user.getBirthday()+user.getGender());
        return null;
    }
//  Login user
    public String loginuser() {
        String Query = "Select*from jsf_assignment3.user where username='" + user.getUsername() + "'and password='" + user.getPassword() + "'";
        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gs_store?serverTimezone=UTC", "root", "");
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(Query);
            if (rs.next()) {
                HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
                session.setAttribute("uname", user.getUsername());
                session.setMaxInactiveInterval(15 * 60);
                return "welcome.xhtml?faces-redirect=true";
            }



        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
//        ResultSet resultSet= connection.loginAccount(user.getUsername(),user.getPassword());
//        if(resultSet==null){
//          return null;
//        }
//        HttpSession session=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
//        session.setAttribute("uname",user.getUsername());
//        session.setMaxInactiveInterval(15*60);
//        return "welcome.xhtml";
//    }
    }
    public boolean isloggedin(){
        HttpSession session=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if(session!=null)
        {
            String username= (String) session.getAttribute("uname");
            if(username!=null)
            {
                return true;
            }
        }
        return false;
    }
    public String logout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
//        session.invalidate();
        return "login.xhtml?faces-redirect=true";


    }




    public UserReg getUser() {
        return user;
    }

    public void setUser(UserReg user) {
        this.user = user;
    }

//    Display data in welcome page
public List<UserReg> getPersonList() throws SQLException {
    Connection c = null;
    try {
        Class.forName("com.mysql.jdbc.Driver");
        c = DriverManager.getConnection("jdbc:mysql://localhost:3306/jsf_assignment3", "root", "");
    } catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);
    }
    List<UserReg> list=new ArrayList<UserReg>();
    PreparedStatement pstmt=c.prepareStatement("select * from user");
    ResultSet rs=pstmt.executeQuery();

    while (rs.next())
    {
        UserReg user=new UserReg();
        user.id=rs.getInt("id");
        user.username=rs.getString("username");
        user.firstname=rs.getString("firstname");
        user.lastname=rs.getString("lastname");
        user.email=rs.getString("email");
        user.password=rs.getString("password");
        user.birthday=rs.getString("birthday");
        user.gender=rs.getString("gender");

        list.add(user);
    }
    return list;
}

    public String DeleteUser(UserReg user) throws SQLException {
        Connection c = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/jsf_assignment3", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        List<UserReg> list=new ArrayList<UserReg>();


        int id=user.id;
        String uname=user.username;
        String fname=user.firstname;
        String lname=user.lastname;
        String email=user.email;
        String birthday=user.password;
        String bday=user.birthday;
        String gender=user.gender;

        String sql="delete from user where id='"+id+"'"+"AND username='"+uname+"'";
        Statement stmt=c.createStatement();
        stmt.executeUpdate(sql);
        list.remove(user);
        System.out.print("Success!");
        return null;
    }
}
