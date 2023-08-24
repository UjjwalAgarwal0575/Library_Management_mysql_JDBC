import java.util.*;
import java.sql.*;
import java.util.Scanner;

public class Jdbc_Library {
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://localhost/shop?allowPublicKeyRetrieval=true&useSSL=false";
   static final String USER = "root";
   static final String PASS = "admin";
   static Scanner sc = new Scanner(System.in);;
   static Statement stmt;

   public static void main(String[] args) {
      Connection conn = null;
      stmt = null;
      try {
         Class.forName(JDBC_DRIVER);
         conn = DriverManager.getConnection(DB_URL, USER, PASS);
         stmt = conn.createStatement();
         System.out.println("Shop Database Management System: ");
         main_menu();
         stmt.close();
         conn.close();
      } catch (SQLException se) {
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (stmt != null)
               stmt.close();
         } catch (SQLException se2) {
         }
         try {
            if (conn != null)
               conn.close();
         } catch (SQLException se) {
            se.printStackTrace();
         }
      }
   }

   static int valid_choice(int i) {
      int choice = -1;
      while (choice < 0 || choice >= i) {
         choice = sc.nextInt();
      }
      return choice;
   }

   static void main_menu() throws SQLException {
      while (true) {
         System.out.println("Select User Authorisation:");
         int i = 0;
         System.out.println((i++) + " Exit");
         System.out.println((i++) + " Customer");
         System.out.println((i++) + " Shop Staff");
         System.out.println((i++) + " Shop Owner");
         System.out.print("\nEnter here: ");
         int choice = valid_choice(i);
         switch (choice) {
            case 0:
               System.out.println("You Exited");
               return;
            case 1:
               Customer_menu();
               break;
            case 2:
               ShopStaff_menu();
               break;
            case 3:
               ShopOwner_menu();
               break;
         }
      }
   }

   static void Customer_menu() throws SQLException {
      while (true) {
         System.out.println("Select action: ");
         int i = 0;
         System.out.println((i++) + " Exit");
         System.out.println((i++) + " List items to add to cart");
         System.out.println((i++) + " Bill Amount");
         System.out.print("\nEnter Action : ");
         int choice = valid_choice(i);

         switch (choice) {
            case 0:
               return;
            case 1:
               list_of_items(true);
               break;
            case 2:
               compute_bill();
               break;
         }
      }
   }

   /* SHOP STAFF MENU */
   static void ShopStaff_menu() throws SQLException {
      while (true) {
         System.out.println("Select action: ");
         int i = 0;
         System.out.println((i++) + " Exit");
         System.out.println((i++) + " List of all items");
         System.out.println((i++) + " List of items presently in shop");
         System.out.println((i++) + " Sell a item");
         System.out.println((i++) + " Return a item");
         System.out.println((i++) + " Add a item");
         System.out.println((i++) + " Delete a item");
         System.out.print("\n\nEnter Action: ");
         int choice = valid_choice(i);

         switch (choice) {
            case 0:
               return;
            case 1:
               list_of_items(false);
               break;
            case 2:
               list_of_items(true);
               break;
            case 3:
               sell_item();
               break;
            case 4:
               return_item();
               break;
            case 5:
               add_item();
               break;
            case 6:
               delete_item();
               break;
         }
      }
   }

   /* OWNER MENU(SUPERADMIN) */
   static void ShopOwner_menu() throws SQLException {
      while (true) {
         System.out.println("Please select an appropriate option: ");
         int i = 0;
         System.out.println((i++) + " Exit");
         System.out.println((i++) + " List of Customers");
         System.out.println((i++) + " List of Shop staff members");
         System.out.println((i++) + " Add a Customer");
         System.out.println((i++) + " Remove a Customer");
         System.out.println((i++) + " Add a Shop staff");
         System.out.println((i++) + " Delete a Shop staff");
         System.out.print("\nEnter: ");
         int choice = valid_choice(i);
         switch (choice) {
            case 0:
               return;
            case 1:
               list_of_Customers();
               break;
            case 2:
               list_of_ShopStaffs();
               break;
            case 3:
               add_customer();
               break;
            case 4:
               delete_customer();
               break;
            case 5:
               add_ShopStaff();
               break;
            case 6:
               delete_ShopStaff();
               break;
         }
      }
   }

   /* GET LIST OF ALL itemS */
   static boolean list_of_items(boolean checkAvailable) throws SQLException {
      String sql = (checkAvailable) ? "select * from item where customer_id is null" : "select * from item";
      ResultSet rs = stmt.executeQuery(sql);
      boolean noitems = true;

      try {
         System.out.println("List of " + ((checkAvailable) ? "available" : "") + " items:\n");
         while (rs.next()) {
            String id = rs.getString("item_id");
            String name = rs.getString("item_name");
            Double price = rs.getDouble("item_price");
            String buyer = rs.getString("customer_id");

            System.out.println("item ID : " + id);
            System.out.println("item Name: " + name);
            System.out.println("item Price: " + price);
            if (!checkAvailable) {
               System.out.println("item Buyer : " + buyer);
            }
            System.out.println("");
            noitems = false;
         }

         if (noitems)
            System.out.println("No items Available");

         rs.close();
      } catch (SQLException e) {
         e.printStackTrace();
      }
      System.out.println("");
      return noitems;
   }

   static void sell_item() {
      try {
         boolean noitem = list_of_items(true);
         if (!noitem) {
            sc.nextLine();
            System.out.print("Enter item id : ");
            String item_id = sc.nextLine();
            System.out.print("Enter customer id : ");
            String customer_id = sc.nextLine();
            String sql = "UPDATE item SET customer_id = \'" + customer_id + "\' WHERE item_id = \'" + item_id
                  + "\'";
            int result = stmt.executeUpdate(sql);
            if (result != 0)
               System.out.println("Buyer Updated\n");
            else
               System.out.println("Buyer Update Failed\n");
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   static void return_item() {
      try {
         sc.nextLine();
         System.out.print("\nEnter item id : ");
         String item_id = sc.nextLine();
         String sql = "update item set customer_id = NULL where item_id = \'" + item_id
               + "\'";
         int result = stmt.executeUpdate(sql);
         if (result != 0)
            System.out.println("item has been returned.\n");
         else
            System.out.println("item return failed\n");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   static void add_item() {
      try {
         sc.nextLine();
         System.out.print("\nEnter item name : ");
         String item_name = sc.nextLine();

         System.out.print("\nEnter item price : ");
         Double item_price = sc.nextDouble();

         String sql = "INSERT INTO item(item_name,item_price) VALUES(\'" + item_name + "\', \'"
               + item_price
               + "\');";
         int result = stmt.executeUpdate(sql);

         if (result != 0)
            System.out.println("item has been added successfully!!\n");
         else
            System.out.println("item addition failed.\n");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   static void delete_item() {
      try {
         sc.nextLine();
         System.out.print("\nEnter item ID : ");
         String item_id = sc.nextLine();
         String sql = "DELETE FROM item where item_id = \'" + item_id + "\'";
         int result = stmt.executeUpdate(sql);

         if (result != 0)
            System.out.println("item has been deleted successfully!!\n");
         else
            System.out.println("item deletion failed\n");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   static void list_of_Customers() throws SQLException {
      String sql = "select * from customer";
      ResultSet rs = stmt.executeQuery(sql);

      try {
         System.out.println("List of customers:\n");
         while (rs.next()) {
            String customer_id = rs.getString("customer_id");
            String patient_name = rs.getString("customer_name");

            System.out.println("Customer Id : " + customer_id);
            System.out.println("Customer Name : " + patient_name);
            System.out.println("\n");
         }

         rs.close();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   static void list_of_ShopStaffs() throws SQLException {
      String sql = "select * from staff";
      ResultSet rs = stmt.executeQuery(sql);

      try {
         System.out.println("List of Shop Staff members:\n");

         while (rs.next()) {
            String staff_id = rs.getString("staff_id");
            String staff_name = rs.getString("staff_name");
            System.out.println("Staff Id : " + staff_id);
            System.out.println("Staff Name: " + staff_name);
            System.out.println("\n");
         }

         rs.close();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   static void add_customer() {
      try {
         sc.nextLine();
         System.out.print("Enter Customer name : ");
         String customer_name = sc.nextLine();
         String sql = "INSERT INTO customer (customer_name)VALUES(\'" + customer_name + "\')";
         int result = stmt.executeUpdate(sql);

         if (result != 0)
            System.out.println("Customer has been added successfully\n");
         else
            System.out.println("Customer addition failed\n");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   static void add_ShopStaff() {
      try {
         sc.nextLine();
         System.out.print("Enter Staff name : ");
         String staff_name = sc.nextLine();
         System.out.print("Enter Joining date of Staff member : ");
         String staff_join_date = sc.nextLine();
         String sql = "INSERT INTO staff(staff_name,shop_joining_date) VALUES( \'" + staff_name + "\', \'"
               + staff_join_date + "\')";
         int result = stmt.executeUpdate(sql);

         if (result != 0)
            System.out.println("Staff has been added successfully\n");
         else
            System.out.println("Staff addition failed\n");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   static void delete_customer() {
      try {
         sc.nextLine();
         System.out.print("Enter customer id : ");
         String customer_id = sc.nextLine();
         String sql = "update item set customer_id = NULL where customer_id = \'" + customer_id+ "\'";
         int result = stmt.executeUpdate(sql);
         sql = "DELETE FROM customer where customer_id = \'" + customer_id + "\'";
         result = stmt.executeUpdate(sql);

         if (result != 0)
            System.out.println("Customer has been deleted successfully\n");
         else
            System.out.println("Customer deletion successful\n");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   static void delete_ShopStaff() {
      try {
         sc.nextLine();
         System.out.print("Enter Staff id : ");
         String staff_id = sc.nextLine();
         String sql = "DELETE FROM staff where staff_id = \'" + staff_id + "\'";
         int result = stmt.executeUpdate(sql);

         if (result != 0)
            System.out.println("Staff has been deleted successfully\n");
         else
            System.out.println("Staff deletion failed\n");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   static void compute_bill() {
      String sql;
      System.out.println("Enter Customer Id: ");
      String custom_id = sc.nextLine();

      try {
         sql = "select item_name , item_price from item where customer_id = '" + custom_id + "';";
         ResultSet rs = stmt.executeQuery(sql);
         Double bill_sum = 0.0;
         while (rs.next()) {
            String name = rs.getString("item_name");
            Double price = rs.getDouble("item_price");
            bill_sum += price;
            System.out.println(name + ":" + price);
         }
         sql = String.format("UPDATE customer set customer_bill = '%f' WHERE customer_id = '%s", bill_sum, custom_id);
         System.out.println("Total : " + bill_sum);
      } catch (SQLException se) {
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}