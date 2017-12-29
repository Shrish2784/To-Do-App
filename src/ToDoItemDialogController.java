import data.TodoItem;
import data.TodoItemData;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;


public class ToDoItemDialogController {

    @FXML
    private TextField titleField;
    @FXML
    private TextArea detailsArea;
    @FXML
    private DatePicker datePicker;
    @FXML
    private DialogPane addtodoitemDialogPane;
    @FXML
    private BorderPane mainWindowBorderpane;

    @FXML
    public TodoItem addtodoitem() {
        String title = titleField.getText();
        String Details = detailsArea.getText();
        LocalDate duedate = datePicker.getValue();

        TodoItem item = new TodoItem(title, Details, duedate);

        if ( title.trim().isEmpty() || Details.trim().isEmpty() || duedate==null ) {

            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("Enter the valid inputs.");
            alert.setContentText("All the input fields should be filled\n" +
                    "        with some inputs.");

            Optional<ButtonType> result= alert.showAndWait();

            if (result.isPresent()&&(result.get().equals(ButtonType.OK))){
                alert.close();
//                Controller controller= new FXMLLoader().getController();
//                controller.addToDoItemDialog();
            }
            return null;

//            Dialog<ButtonType> dialog = new Dialog<>();
//            dialog.setHeaderText("Enter the valid inputs.");
//
//            FXMLLoader fxmlLoader = new FXMLLoader();
//
//            fxmlLoader.setLocation(getClass().getResource("error.fxml"));
////            dialog.initOwner(mainWindowBorderpane.getScene().getWindow());//NulLPointer
//            try {
//                dialog.getDialogPane().setContent(fxmlLoader.load());
//            } catch (Exception e) {
//                System.out.println("Error");
//            }
//
//            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
////            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
//
//            Optional<ButtonType> result = dialog.showAndWait();
        } else {
            TodoItemData.getInstance().addTodoItem(item);
            try {
                TodoItemData.getInstance().storeDataToFile();
            } catch (IOException e) {
                System.out.println("cant store the data.");
                e.getMessage();
            }
            return item;
        }
    }
}