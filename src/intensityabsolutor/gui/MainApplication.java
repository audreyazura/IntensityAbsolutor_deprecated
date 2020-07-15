/*
 * Copyright (C) 2020 Alban Lafuente
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package intensityabsolutor.gui;

import intensityabsolutor.calculator.GUIInterface;
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
 * @author Alban Lafuente
 */
public class MainApplication extends Application implements GUIInterface
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
            ((WindowController) parameterWindowLoader.getController()).initialize(this);
            m_mainStage.setScene(new Scene(windowFxml));
            m_mainStage.sizeToScene();
	    m_mainStage.show();
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void sendException(Exception p_exception)
    {
        Logger.getLogger(MainApplication.class.getName()).log(Level.SEVERE, p_exception.getMessage(), p_exception);
    }
    
    public Stage getMainStage()
    {
        return m_mainStage;
    }
}
