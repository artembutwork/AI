import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;


public final class Runner extends Application {

    private List<Observation> trainingList;
    private HashMap<String, Perceptron> languageMap;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        setUp();

        TextArea textArea = new TextArea();

        Label bottomLabel = new Label("↑ Enter text for language recognition ↑");
        bottomLabel.setPrefSize(1500, 70);

        Button getAssumptionButton = new Button();
        getAssumptionButton.setText("ASSUMPTION FROM AI");
        getAssumptionButton.setPrefSize(1900,100);
        getAssumptionButton.setOnAction(event -> handleUsersText(textArea.getText()));

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(getAssumptionButton);
        borderPane.setCenter(textArea);
        borderPane.setBottom(bottomLabel);

        Scene scene = new Scene(borderPane, 1500, 550);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void setUp(){
        try {
            trainingList = getObservationsList("data/TrainingSet");
            languageMap = new HashMap<>();
            for (Observation observation : trainingList)
                languageMap.put(observation.getLanguage(), new Perceptron());
            training();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private List<Observation> getObservationsList(String directory) throws IOException {
        List<Observation> observations = new ArrayList<>();
        DirectoryStream<Path> dirs = null;
        try {
            Path directoryPath = Paths.get(directory);
            dirs = Files.newDirectoryStream(directoryPath);
            for (Path currentDirectory : dirs) {
                String lang = currentDirectory
                        .getFileName()
                        .toString();
                String text = getText(currentDirectory);
                Observation observation = new Observation(text, lang);
                observations.add(observation);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }finally {
            if(dirs != null)
                dirs.close();
        }
        return observations;
    }

    private String getText(Path currentDirectory) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (Files.isDirectory(currentDirectory)) {
            List<Path> files = Files.walk(currentDirectory)
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
            for (Path currentFile : files) {
                FileReader fileReader = new FileReader(currentFile.toFile());
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                while ((line = bufferedReader.readLine()) != null)
                    stringBuilder.append(line.toLowerCase());
            }
        }
        return stringBuilder.toString();
    }

    private void training(){
        for (int iteration = 0; iteration < 1000000; iteration++)
            for (Observation observation : trainingList)
                for (Map.Entry<String, Perceptron> langMapEntry : languageMap.entrySet())
                    langMapEntry.getValue()
                            .training(observation.getLanguage(), langMapEntry.getKey(), observation);
    }

    private void handleUsersText(String text){
        Observation observation = new Observation(text);
        for (Map.Entry<String, Perceptron> langMapEntry : languageMap.entrySet())
            if (langMapEntry.getValue().calculate(observation) == 1) {
                System.out.println("AI assumption: " + langMapEntry.getKey());
                break;
            }
    }

}