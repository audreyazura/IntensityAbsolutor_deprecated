/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package intensityabsolutor.gui;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author audreyazura
 */
public class WindowController
{
    @FXML private TextField experiment;
    @FXML private TextField callibration;
    @FXML private TextField whitelightnosample;
    @FXML private TextField whitelightwithsample;
    @FXML private TextField output;
    
    MainApplication m_mainApp;
    
    @FXML private void browseSample()
    {
        browse(experiment, "Chose the file with the sample spectra");
    }
    
    @FXML private void browseCallibration()
    {
        browse(callibration, "Chose the file with the callibration spectra");
    }
    
    @FXML private void browseWhiteLightNoSample()
    {
        browse(whitelightnosample, "Chose the file with the white light spectra with no sample");
    }
    
    @FXML private void browseWhiteLightSample()
    {
        browse(whitelightwithsample, "Chose the file with the white light spectra with the sample");
    }
    
    @FXML private void browseOutput()
    {
        browse(output, "Chose the output file");
    }
    
    @FXML private void calculate()
    {
        
    }
    
    private void browse(TextField p_outputField, String p_title)
    {
        FileChooser browser = new FileChooser();
	
	browser.setTitle(p_title);
	
	try
        {
            String fieldText = p_outputField.getText();
            browser.setInitialDirectory(new File((new File(fieldText)).getParent()));
        }
        catch (NullPointerException ex)
        {
            browser.setInitialDirectory(new File(System.getProperty("user.home")));
        }
        
        File selectedFile = new File("");
        if (p_outputField.equals(output))
        {
            selectedFile = browser.showSaveDialog(new Stage());
        }
        else
        {
            selectedFile = browser.showOpenDialog(new Stage());
        }
        if (selectedFile != null)
        {
            p_outputField.setText(selectedFile.getAbsolutePath());
        }
        else
        {
            p_outputField.setText("");
        }
    }
    
    public void initialize(MainApplication p_app)
    {
        m_mainApp = p_app;
    }
}
