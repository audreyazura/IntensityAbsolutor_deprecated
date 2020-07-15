/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package intensityabsolutor.gui;

import intensityabsolutor.calculator.IntensityAbsolutor;
import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author audreyazura
 */
public class WindowController
{
    @FXML private ChoiceBox callibrationlight;
    @FXML private Label lastlabel;
    @FXML private TextField experiment;
    @FXML private TextField bgexperiment;
    @FXML private TextField expintegration;
    @FXML private TextField callibration;
    @FXML private TextField bgcallibration;
    @FXML private TextField callintegration;
    @FXML private TextField whitelightnosample;
    @FXML private TextField bgwlnosample;
    @FXML private TextField wlnosampleintegration;
    @FXML private TextField whitelightwithsample;
    @FXML private TextField bgwlsample;
    @FXML private TextField wlsampleintegration;
    @FXML private TextField output;
    
    private MainApplication m_mainApp;
    private Map<String, File> m_fileMap = new HashMap<>();
    private Map<String, BigDecimal> m_exposureMap = new HashMap<>();
    
    @FXML private void browseSample()
    {
        browse(experiment, "Chose the file with the sample spectra");
    }
    
    @FXML private void browseBGSample()
    {
        browse(bgexperiment, "Chose the file with the sample background");
    }
    
    @FXML private void browseCallibration()
    {
        browse(callibration, "Chose the file with the callibration spectra");
    }
    
    @FXML private void browseBGCallibration()
    {
        browse(bgcallibration, "Chose the file with the callibration background");
    }
    
    @FXML private void browseWhiteLightNoSample()
    {
        browse(whitelightnosample, "Chose the file with the white light spectra with no sample");
    }
    
    @FXML private void browseBGWLNoSample()
    {
        browse(bgwlnosample, "Chose the file with the white light background with no sample");
    }
    
    @FXML private void browseWhiteLightSample()
    {
        browse(whitelightwithsample, "Chose the file with the white light spectra with the sample");
    }
    
    @FXML private void browseBGWLSample()
    {
        browse(bgwlsample, "Chose the file with the white light background with the sample");
    }
    
    @FXML private void browseOutput()
    {
        browse(output, "Chose the output file");
    }
    
    @FXML private void calculate()
    {
        try
        {
            m_exposureMap.put(expintegration.getId(), new BigDecimal(expintegration.getText()));
            m_exposureMap.put(callintegration.getId(), new BigDecimal(callintegration.getText()));
            m_exposureMap.put(wlnosampleintegration.getId(), new BigDecimal(wlnosampleintegration.getText()));
            m_exposureMap.put(wlsampleintegration.getId(), new BigDecimal(wlsampleintegration.getText()));    
        }
        catch (NumberFormatException ex)
        {
            Logger.getLogger(WindowController.class.getName()).log(Level.SEVERE, "Error in one of exposure time.", ex);
        }
        
        switch((String) callibrationlight.getValue())
        {
            case "Labsphere":
                m_fileMap.put("output", new File("ReferenceIntensities/Labsphere.intensity"));
                break;
            case "Ocean Opticts HL-3":
                m_fileMap.put("output", new File("ReferenceIntensities/OceanOpticsHL-3_PLUS-CAL-INT-EXT.intensity"));
                break;
            default:
                m_mainApp.sendException(new IllegalArgumentException("Select a proper value in the callibration light list."));
                return;
        }
        
        if (!m_fileMap.containsValue(new File("")))
        {
            IntensityAbsolutor calculator = new IntensityAbsolutor((HashMap) m_fileMap, (HashMap) m_exposureMap, m_mainApp);
            Thread calculationThread = new Thread(calculator);
            calculationThread.start();
            
            try
            {
                calculationThread.join();
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(WindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(calculator.hasFinished())
            {
                lastlabel.setText(output.getText()+" created.");
                lastlabel.setManaged(true);
                lastlabel.setVisible(true);
                m_mainApp.getMainStage().sizeToScene();
            }
        }
    }
    
    private void browse(TextField p_outputField, String p_title)
    {
        FileChooser browser = new FileChooser();
        
        System.out.println(p_outputField.getId());
	
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
            m_fileMap.put(p_outputField.getId(), selectedFile);
        }
        else
        {
            p_outputField.setText("");
        }
    }
    
    public void initialize(MainApplication p_app)
    {
        m_mainApp = p_app;
        lastlabel.setVisible(false);
        lastlabel.setManaged(false);
        m_mainApp.getMainStage().sizeToScene();
    }
}
