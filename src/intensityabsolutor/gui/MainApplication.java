/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package intensityabsolutor.gui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author audreyazura
 */
public class MainApplication extends Application
{
    private Stage m_mainStage;
    
    public void startApplication(String[] args)
    {
        launch(args);
    }
    
    @Override
    public void start(Stage stage)
    {
        m_mainStage = stage;
        
        FXMLLoader parameterWindowLoader = new FXMLLoader(MainApplication.class.getResource("FXMLApplicationWindow.fxml"));
        
        try
        {
            Parent windowFxml = parameterWindowLoader.load();
            m_mainStage.setScene(new Scene(windowFxml));
            m_mainStage.sizeToScene();
	    m_mainStage.show();
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Stage getMainStage()
    {
        return m_mainStage;
    }
}
