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
 * @author Alban Lafuente
 */
public class WindowController
{
    @FXML private ChoiceBox callibrationlight;
    @FXML private Label lastlabel;
    @FXML private TextField experiment;
    @FXML private TextField bgexperiment;
    @FXML private TextField expintegration;
    @FXML private TextField expbgintegration;
    @FXML private TextField callibration;
    @FXML private TextField bgcallibration;
    @FXML private TextField callintegration;
    @FXML private TextField callbgintegration;
    @FXML private TextField whitelightnosample;
    @FXML private TextField bgwlnosample;
    @FXML private TextField wlnosampleintegration;
    @FXML private TextField wlnosamplebgintegration;
    @FXML private TextField whitelightwithsample;
    @FXML private TextField bgwlsample;
    @FXML private TextField wlsampleintegration;
    @FXML private TextField wlsamplebgintegration;
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
            m_exposureMap.put(expbgintegration.getId(), new BigDecimal(expbgintegration.getText()));
            m_exposureMap.put(callintegration.getId(), new BigDecimal(callintegration.getText()));
            m_exposureMap.put(callbgintegration.getId(), new BigDecimal(callbgintegration.getText()));
            m_exposureMap.put(wlnosampleintegration.getId(), new BigDecimal(wlnosampleintegration.getText()));
            m_exposureMap.put(wlnosamplebgintegration.getId(), new BigDecimal(wlnosamplebgintegration.getText()));
            m_exposureMap.put(wlsampleintegration.getId(), new BigDecimal(wlsampleintegration.getText()));
            m_exposureMap.put(wlsamplebgintegration.getId(), new BigDecimal(wlsamplebgintegration.getText()));
        }
        catch (NumberFormatException ex)
        {
            NumberFormatException passedEx = new NumberFormatException("Error in one of exposure time.");
            passedEx.setStackTrace(ex.getStackTrace());
            m_mainApp.sendException(passedEx);
            return;
        }
        
        try
        {
            m_fileMap.put(experiment.getId(), new File(experiment.getText()));
            m_fileMap.put(bgexperiment.getId(), new File(bgexperiment.getText()));
            m_fileMap.put(callibration.getId(), new File(callibration.getText()));
            m_fileMap.put(bgcallibration.getId(), new File(bgcallibration.getText()));
            m_fileMap.put(whitelightnosample.getId(), new File(whitelightnosample.getText()));
            m_fileMap.put(bgwlnosample.getId(), new File(bgwlnosample.getText()));
            m_fileMap.put(whitelightwithsample.getId(), new File(whitelightwithsample.getText()));
            m_fileMap.put(bgwlsample.getId(), new File(bgwlsample.getText()));
            
            m_fileMap.put(output.getId(), new File(output.getText()));
        }
        catch (NullPointerException ex)
        {
            NullPointerException passedEx = new NullPointerException("Error in the file addresses given.");
            passedEx.setStackTrace(ex.getStackTrace());
            m_mainApp.sendException(passedEx);
            return;
        }
        
        switch((String) callibrationlight.getValue())
        {
            case "Labsphere":
                m_fileMap.put("lightintensity", new File("ReferenceIntensities/Labsphere.intensity"));
                break;
            case "Ocean Opticts HL-3":
                m_fileMap.put("lightintensity", new File("ReferenceIntensities/OceanOpticsHL-3_PLUS-CAL-INT-EXT.intensity"));
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
        
        browser.setTitle(p_title);
        browser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text (*.txt)", "*.txt"), new FileChooser.ExtensionFilter("All files (*.*)", "*.*"));
	
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
        lastlabel.setVisible(false);
        lastlabel.setManaged(false);
        m_mainApp.getMainStage().sizeToScene();
    }
}
