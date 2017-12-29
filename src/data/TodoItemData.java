package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class TodoItemData {

    private ObservableList<TodoItem> todoItems;
    private ObservableList<TodoItem> trashTodoItems;
    private  static String fileName;
    private  static String fileName1;
    private static TodoItemData instance=new TodoItemData();
    private DateTimeFormatter formatter;

    private TodoItemData(){
        fileName="TODODATA.txt";
        fileName1="TODODATATRASH.txt";
        formatter=DateTimeFormatter.ofPattern("MMMM dd yyyy");
    }

    public static TodoItemData getInstance() {
        return instance;
    }

    public ObservableList<TodoItem> getTrashTodoItems() {
        return trashTodoItems;
    }

    public ObservableList<TodoItem> getTodoItems() {
        return todoItems;
    }

    public void addTodoItem (TodoItem item){
        todoItems.add(item);
    }

    public void deleteTodoItem (TodoItem item){
        todoItems.remove(item);
        trashTodoItems.add(item);
    }

    public void perDeleteTodoItem(TodoItem item){
        todoItems.remove(item);
    }

    public void deleteTrashTodoItem(TodoItem item){
        trashTodoItems.remove(item);
    }

    public void restoreTrashTodoItem(TodoItem item){
        trashTodoItems.remove(item);
        todoItems.add(item);
    }


    public void storeDataToFile()throws IOException{

        Path path= Paths.get(fileName);
        BufferedWriter bw= Files.newBufferedWriter(path);
        Iterator<TodoItem> iterator=todoItems.iterator();

        try{
            while (iterator.hasNext()){
                TodoItem item=iterator.next();
                bw.write(String.format("%s\t%s\t%s",item.getTitle(),item.getDetails(),(item.getDueDate().format(formatter))));
                bw.newLine();
            }
        }finally {
            if (bw!=null)
                bw.close();
        }
    }

    public void loadDataFromFile()throws IOException{
        todoItems= FXCollections.observableArrayList();
        Path path=Paths.get(fileName);
        BufferedReader br=Files.newBufferedReader(path);
        String data;
        try{
            while((data=br.readLine())!=null){
                String[] dataFormatted=data.split("\t");
                TodoItem item=new TodoItem(dataFormatted[0],dataFormatted[1],LocalDate.parse(dataFormatted[2],formatter));
                todoItems.add(item);
            }
        }finally {
            if(br!=null)
                br.close();
        }
    }

    public void loadDataFromTrash () throws IOException {
        trashTodoItems=FXCollections.observableArrayList();
        Path path=Paths.get(fileName1);
        BufferedReader br=Files.newBufferedReader(path);
        String data;
        try{
            while ((data=br.readLine())!=null){
                String[] dataFormatted=data.split("\t");
                TodoItem item=new TodoItem(dataFormatted[0],dataFormatted[1],LocalDate.parse(dataFormatted[2],formatter));
                trashTodoItems.add(item);
            }
        } finally {
            if (br!=null)
                br.close();
        }
    }

    public void storeDataToTrash () throws IOException{
        Path path=Paths.get(fileName1);
        BufferedWriter bw=Files.newBufferedWriter(path);
        Iterator<TodoItem> iterator=trashTodoItems.iterator();
        try {
            while (iterator.hasNext()) {
                TodoItem item = iterator.next();
                bw.write(String.format(String.format("%s\t%s\t%s", item.getTitle(), item.getDetails(), item.getDueDate().format(formatter))));
                bw.newLine();
            }
        } finally {
            if (bw!=null)
                bw.close();
        }
    }


}