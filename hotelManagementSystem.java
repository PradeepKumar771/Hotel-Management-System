import java.sql.*;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded...");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            Connection conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant","root","root");
            System.out.println("connection established....");


            while (true){
                Scanner sc=new Scanner(System.in);
                System.out.println();
                System.out.println("Welcome to hilton hospitality ");
                System.out.println("press 1 for New Reservation");
                System.out.println("press 2 for view Reservation");
                System.out.println("press 3 for get Room No");
                System.out.println("press 4 for New update Reservation");
                System.out.println("press 5 for New Delete Resevation");
                System.out.println("press 0 for Exit");

                int choice= sc.nextInt();
                switch (choice){

                    case 1:
                        ReservRoom(conn, sc);
                        break;
                    case 2:
                        ViewReservatio(conn);
                        break;
                    case 3:
                        GetRoomNumber(conn,sc);
                        break;
                    case 4:
                        updateReservation(conn,sc);
                        break;
                    case 5:
                        deleteReservation(conn,sc);
                        break;
                    case 0:
                        exit();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    //reservation room method1
    public static void  ReservRoom(Connection conn, Scanner sc){
        System.out.println("Enter your guest name");
        String name=sc.next();
        sc.nextLine();
        System.out.println("Enter Room Number");
        int RoomNumber=sc.nextInt();
        System.out.println("Enter your contact number");
        String contact_number=sc.next();
        String query = "INSERT INTO reservation(guest_name, room_number, contact_number) VALUES ('" + name + "', " + RoomNumber + ", '" + contact_number + "')";

        try {
            Statement st= conn.createStatement();
            int rowAffected= st.executeUpdate(query);

            if(rowAffected > 0){
                System.out.println("row affected....");
            }
            else {
                System.out.println("row is not affected....");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //view reservation method 2
    public  static void ViewReservatio(Connection conn){
        String query1="select* from reservation";
        try {
            Statement vr= conn.createStatement();
            ResultSet rs= vr.executeQuery(query1);
            while (rs.next()){
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                int id=rs.getInt("reservation_id");
                String name= rs.getNString("guest_name");
                String roomNumber=rs.getNString("room_number");
                String number=rs.getNString("contact_number");
                Date timestamp=rs.getDate("reservation_date");

                System.out.println();
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~CONTACT DETAIL~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`");
                System.out.println("Id "+id);
                System.out.println("name "+name);
                System.out.println("roomno "+roomNumber);
                System.out.println("contact_No "+number);
                System.out.println("reservation_date " +timestamp);
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
//3 get room number
    public  static void GetRoomNumber(Connection conn, Scanner sc){
        System.out.println("Enter reservation id");
        int reservationId=sc.nextInt();
        System.out.println("Enter guest name");
        String guest_name=sc.next();

        String query2 = "SELECT room_number FROM reservation WHERE reservation_id = '" + reservationId + "' AND guest_name = '" + guest_name + "'";

            try {
                Statement st=conn.createStatement();
                ResultSet rs= st.executeQuery(query2);
                if(rs.next()){


                     String room=rs.getString("room_number");
                    System.out.println("Room Number for Reservation id ->" +reservationId +"And the Guest -> "+guest_name+" is :"+room);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

    }

    //4 update Reservation
    public  static void updateReservation(Connection conn ,Scanner sc) {
        System.out.println("Enter your reservation id to update");
        int reservationId = sc.nextInt();
        if(!reservationExist(conn, reservationId)){
            System.out.println("Reservation Not Found");
        }

        System.out.println("Enter new Guest name");
        String newGuestName = sc.next();

        System.out.println("Enter your Room Number");
        int newRoomNumber = sc.nextInt();

        System.out.println("Enter your New Contact Number");
        String newContactNumber = sc.next();



        String sql = "UPDATE reservation SET guest_name = ?, room_number = ?, contact_number = ? WHERE reservation_id = ?";

        try {
            PreparedStatement stmt  = conn.prepareStatement(sql);
            stmt.setString(1, newGuestName);
            stmt.setInt(2, newRoomNumber);
            stmt.setString(3, newContactNumber);
            stmt.setInt(4, reservationId);
            int result=stmt.executeUpdate();
            if(result>0){
                System.out.println("Update successfully....");
            }
            else {
                System.out.println("Not updated.....");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    public static boolean reservationExist (Connection conn, int reservationId){
            String query4 = "select room_number from reservation where reservation_id =" + reservationId;
            try {
                Statement st=conn.createStatement();
                ResultSet rs=st.executeQuery(query4);
               return rs.next();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        // 5 delete reservation
        public static void deleteReservation(Connection conn,Scanner sc){
            System.out.println("Enter your reservation ID to Delete");
            int ReservationId= sc.nextInt();
            if(!reservationExist(conn,ReservationId)){
                System.out.println("Reservation Id not found for a given id");
            }
            String query5 = "DELETE FROM reservation WHERE reservation_id = '" + ReservationId + "'";

            try {
                Statement st=conn.createStatement();
                int rowAffected=st.executeUpdate(query5);
                if(rowAffected>0){
                    System.out.println("Reservation delete successfully");
                }
                else {
                    System.out.println("Reservation not deleted....");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        //6 Exit method
    public  static void exit() throws InterruptedException {
        System.out.println();
        System.out.println("Exiting system");
        int i=10;
        while (i !=0){
            System.out.print(" . ");

            Thread.sleep(500);
            i--;
        }
        System.out.println();
        System.out.println("Thanks for Using Hilton Reservation...");
    }

}
