
import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


   
  public class Client extends Application {
  // IO streams

  DataOutputStream toServer = null;
  DataInputStream fromServer = null;

  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
    // Panel p to hold the label and text field
    BorderPane paneForTextField = new BorderPane();
    paneForTextField.setPadding(new Insets(5, 5, 5, 5)); 
    paneForTextField.setStyle("-fx-border-color: green");
    paneForTextField.setLeft(new Label("Enter a number to check prime: "));
    
    TextField tf = new TextField();
    tf.setAlignment(Pos.BOTTOM_RIGHT);
    paneForTextField.setCenter(tf);
    
    BorderPane mainPane = new BorderPane();
    // Text outnumber to display contents
    TextArea ta = new TextArea();
    mainPane.setCenter(new ScrollPane(ta));
    mainPane.setTop(paneForTextField);
    
    // Create a scene and place it in the stage
    Scene scene = new Scene(mainPane, 450, 200);
    primaryStage.setTitle("Client"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage
    
    tf.setOnAction(e -> {
      try {
        // Get the innumber from the text field
        double innumber = Double.parseDouble(tf.getText().trim());
  
        // Send the innumber to the server
        toServer.writeDouble(innumber);
        toServer.flush();
  
        // Get number from the server
        double outnumber = fromServer.readDouble();
        
 
        // Display to the text outnumber
        //ta.appendText("Number is " + outnumber + "\n");
        //ta.appendText("The number received from the server "
        // + outnumber + '\n');
        		
        int temp;
      	boolean isPrime=true;
      			
          int i=2;
          while(i<= innumber/2)
          {
             if(innumber % i == 0)
             {
          	isPrime = false;
          	break;
             }
             i++;
          }
  
          if (isPrime)
          Platform.runLater(() -> {
            ta.appendText("Number is: " 
              + outnumber + '\n');
            ta.appendText("Number received is a Prime Number: " + outnumber + '\n'); 
          });
          else
        	  Platform.runLater(() -> {
                  ta.appendText("Number is: " 
                    + outnumber + '\n');
                  ta.appendText("Number received is not a Prime number: " + outnumber + '\n'); 
                });
  
      }
      catch (IOException ex) {
        System.err.println(ex);
      }
    });
  
    try {
      // Create a socket to connect to the server
      Socket socket = new Socket("localhost", 8000);
      // Socket socket = new Socket("130.254.204.36", 8000);
      // Socket socket = new Socket("drake.Armstrong.edu", 8000);

      // Create an input stream to receive data from the server
      fromServer = new DataInputStream(socket.getInputStream());

      // Create an output stream to send data to the server
      toServer = new DataOutputStream(socket.getOutputStream());
    }
    catch (IOException ex) {
      ta.appendText(ex.toString() + '\n');
    }
  }

  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
   launch(args);
  }
}
