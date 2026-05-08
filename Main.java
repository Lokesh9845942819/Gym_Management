import java.sql.*;
import java.util.Scanner;

public class Main {

    static String url = "jdbc:mysql://localhost:3306/gymdb";
    static String user = "root";
    static String password = "your_password";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
            Connection con = DriverManager.getConnection(url, user, password);

            while (true) {
                System.out.println("\n--- GYM MANAGEMENT ---");
                System.out.println("1. Add Member");
                System.out.println("2. View Members");
                System.out.println("3. Delete Member");
                System.out.println("4. Update Member");
                System.out.println("5. Search Member");
                System.out.println("6. Check Membership Status");
                System.out.println("7. Renew Membership Status");

                System.out.println("8. Exit");

                System.out.print("Enter choice: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {

                    case 1:
                        addMember(con, sc);
                        break;

                    case 2:
                        viewMembers(con);
                        break;

                    case 3:
                        deleteMember(con, sc);
                        break;

                    case 4:
                        updateMember(con, sc);
                        break;

                    case 5:
                        searchMember(con, sc);
                        break;

                    case 6:
                        checkMembership(con);
                        break;

                   
                    case 7:
                        renewMembership(con, sc);
                        break;
                        
                    case 8:
                        System.out.println("Membership renewed");
                        return;

                    default:
                        System.out.println("Invalid choice");
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // 🔹 ADD MEMBER WITH PLAN + ELIGIBILITY
    public static void addMember(Connection con, Scanner sc) throws Exception {

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Age: ");
        int age = sc.nextInt();
        sc.nextLine();

        if(age < 18){
            System.out.println("Not eligible");
            return;
        }

        System.out.print("Enter Phone: ");
        String phone = sc.nextLine();

        System.out.print("Enter Fee Paid: ");
        int fee = sc.nextInt();
        sc.nextLine();

        System.out.println("Select Membership Plan:");
        System.out.println("1. 1 Month");
        System.out.println("2. 6 Months");
        System.out.println("3. 12 Months");

        int plan = sc.nextInt();
        sc.nextLine();

        int months = 0;

        switch(plan){
            case 1: months = 1; break;
            case 2: months = 6; break;
            case 3: months = 12; break;
            default:
                System.out.println("Invalid plan");
                return;
        }

        String query = "INSERT INTO members (name, age, phone, fee_paid, start_date, end_date) VALUES (?, ?, ?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL ? MONTH))";

        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, name);
        pst.setInt(2, age);
        pst.setString(3, phone);
        pst.setInt(4, fee);
        pst.setInt(5, months);

        pst.executeUpdate();

        System.out.println("Member Added!");
    }

    // 🔹 VIEW MEMBERS
    public static void viewMembers(Connection con) throws Exception {
        String query = "SELECT * FROM members";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        System.out.println("\n--- MEMBERS LIST ---");

        while (rs.next()) {
            System.out.println(
                rs.getInt("member_id") + " | " +
                rs.getString("name") + " | " +
                rs.getInt("age") + " | " +
                rs.getString("phone") + " | " +
                rs.getInt("fee_paid") + " | " +
                rs.getDate("start_date") + " | " +
                rs.getDate("end_date")
            );
        }
    }

    // 🔹 DELETE MEMBER
    public static void deleteMember(Connection con, Scanner sc) throws Exception {
        System.out.print("Enter Member ID to delete: ");
        int id = sc.nextInt();

        String query = "DELETE FROM members WHERE member_id = ?";

        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, id);

        int rows = pst.executeUpdate();

        System.out.println(rows > 0 ? "Deleted!" : "Not found!");
    }

    // 🔹 UPDATE MEMBER
    public static void updateMember(Connection con, Scanner sc) throws Exception {
        System.out.print("Enter Member ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter new name: ");
        String name = sc.nextLine();

        System.out.print("Enter new age: ");
        int age = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter new phone: ");
        String phone = sc.nextLine();

        String q = "UPDATE members SET name=?, age=?, phone=? WHERE member_id=?";

        PreparedStatement pst = con.prepareStatement(q);
        pst.setString(1, name);
        pst.setInt(2, age);
        pst.setString(3, phone);
        pst.setInt(4, id);

        int rows = pst.executeUpdate();
        System.out.println(rows > 0 ? "Updated!" : "Not found!");
    }

    // 🔹 SEARCH MEMBER
    public static void searchMember(Connection con, Scanner sc) throws Exception {
        System.out.print("Enter name to search: ");
        String name = sc.nextLine();

        String q = "SELECT * FROM members WHERE name LIKE ?";
        PreparedStatement pst = con.prepareStatement(q);
        pst.setString(1, "%" + name + "%");

        ResultSet rs = pst.executeQuery();

        boolean found = false;

        while (rs.next()) {
            found = true;

            System.out.println(
                rs.getInt("member_id") + " | " +
                rs.getString("name") + " | " +
                rs.getInt("age")
            );
        }

        if (!found) {
            System.out.println("Member not found!");
        }
    }

    // 🔹 CHECK MEMBERSHIP STATUS
    public static void checkMembership(Connection con) throws Exception {

        String q = "SELECT name, end_date FROM members";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(q);

        System.out.println("\n--- MEMBERSHIP STATUS ---");

        while(rs.next()){
            String name = rs.getString("name");
            Date end = rs.getDate("end_date");

            if(end.before(new java.util.Date())){
                System.out.println(name + " → Expired");
            } else {
                System.out.println(name + " → Active");
            }
        }
    }
    public static void renewMembership(Connection con, Scanner sc) throws Exception {

    System.out.print("Enter Member ID: ");
    int id = sc.nextInt();

    System.out.println("Select Plan to Renew:");
    System.out.println("1. 1 Month");
    System.out.println("2. 6 Months");
    System.out.println("3. 12 Months");

    int plan = sc.nextInt();

    int months = 0;

    switch(plan){
        case 1: months = 1; break;
        case 2: months = 6; break;
        case 3: months = 12; break;
        default:
            System.out.println("Invalid plan");
            return;
    }

    String q = "UPDATE members SET end_date = DATE_ADD(end_date, INTERVAL ? MONTH) WHERE member_id = ?";

    PreparedStatement pst = con.prepareStatement(q);
    pst.setInt(1, months);
    pst.setInt(2, id);

    int rows = pst.executeUpdate();

    if(rows > 0){
        System.out.println("Membership renewed successfully!");
    } else {
        System.out.println("Member not found!");
    }
}
}