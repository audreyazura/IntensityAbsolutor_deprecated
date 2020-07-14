/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package intensityabsolutor.calculator;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

/**
 *
 * @author audreyazura
 */
public class IntensityAbsolutor implements Runnable
{
    private final Map<String, File> m_fileMap;
    private final Map<String, BigDecimal> m_exposureMap;
    private final GUIInterface m_app;
    private boolean m_properlyEnded = false;
    
    public IntensityAbsolutor(HashMap<String, File> p_fileMap, HashMap<String, BigDecimal> p_exposureMap, GUIInterface p_app)
    {
        m_fileMap = p_fileMap;
        m_exposureMap = p_exposureMap;
        m_app = p_app;
    }
    
    @Override
    public void run()
    {
        Spectra correctedSampleSpectra = new Spectra();
        Spectra correctedCallibrationSpectra = new Spectra();
        Spectra correctedWLNoSample = new Spectra();
        Spectra correctedWLSample = new Spectra();
        Spectra sampleRelativeIntensity = new Spectra();
        Spectra whiteLightDivision = new Spectra();

        try
        {
            correctedSampleSpectra = (Spectra) (Spectra.spectraFromWinSpec(m_fileMap.get("experiment")).substract(Spectra.spectraFromWinSpec(m_fileMap.get("bgexperiment")))).divide(m_exposureMap.get("expintegration"));
            correctedCallibrationSpectra = (Spectra) (Spectra.spectraFromWinSpec(m_fileMap.get("callibration")).substract(Spectra.spectraFromWinSpec(m_fileMap.get("bgcallibration")))).divide(m_exposureMap.get("callintegration"));
            correctedWLNoSample = (Spectra) (Spectra.spectraFromWinSpec(m_fileMap.get("whitelightnosample")).substract(Spectra.spectraFromWinSpec(m_fileMap.get("bgwlnosample")))).divide(m_exposureMap.get("wlnosampleintegration"));
            correctedWLSample = (Spectra) (Spectra.spectraFromWinSpec(m_fileMap.get("whitelightwithsample")).substract(Spectra.spectraFromWinSpec(m_fileMap.get("bgwlsample")))).divide(m_exposureMap.get("wlsampleintegration"));
        }
        catch (DataFormatException ex)
        {
            m_app.sendException(ex);
            return;
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            m_app.sendException(ex);
            return;
        }
        catch (IOException ex)
        {
            m_app.sendException(ex);
            return;
        }

        try
        {
            sampleRelativeIntensity = (Spectra) correctedSampleSpectra.divide(correctedCallibrationSpectra);
        }
        catch (ArithmeticException ex)
        {
            ArithmeticException exToSent = new ArithmeticException("Null value in the intensities of the callibration light");
            exToSent.setStackTrace(ex.getStackTrace());
            m_app.sendException(exToSent);
            return;
        }

        try
        {
            whiteLightDivision = (Spectra) correctedWLNoSample.divide(correctedWLSample);
        }
        catch (ArithmeticException ex)
        {
            ArithmeticException exToSent = new ArithmeticException("Null value in the intensities of the white light file with sample");
            exToSent.setStackTrace(ex.getStackTrace());
            m_app.sendException(exToSent);
            return;
        }

        try
        {
            ((Spectra) (Spectra.callibrationAbsoluteIntensitySpectra(m_fileMap.get("lightintensity")).multiply(sampleRelativeIntensity).multiply(whiteLightDivision))).logToFile(m_fileMap.get("output"));
            m_properlyEnded = true;
        }
        catch (DataFormatException ex)
        {
            m_app.sendException(ex);
            return;
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            m_app.sendException(ex);
            return;
        }
        catch (IOException ex)
        {
            m_app.sendException(ex);
            return;
        }
    }
    
    public boolean hasFinished()
    {
        return m_properlyEnded;
    }
}
