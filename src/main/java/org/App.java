package org;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    private static volatile Injector injector;

    public static void main(String[] args) {
        launch(args);
    }

    private Stage mainStage;



    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        StackPane mainView = getLoader("/fxml/view/app_view.fxml").load();

        Scene scene = new Scene(mainView);

        primaryStage.setMinWidth(650);
        primaryStage.setMinHeight(400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static FXMLLoader getLoader(String location) {
        if (injector == null) {
            synchronized (App.class) {
                if (injector == null) {
                    injector = Guice.createInjector(new AppModule());
                }
            }
        }

        FXMLLoader loader = new FXMLLoader(App.class.getResource(location));
        loader.setControllerFactory(injector::getInstance);

        return loader;
    }


    public Stage getMainStage() {
        return mainStage;
    }
}
