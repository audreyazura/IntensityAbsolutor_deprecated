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
package intensityabsolutor.calculator;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;

/**
 *
 * @author Alban Lafuente
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
            correctedSampleSpectra = new Spectra((Spectra.spectraFromWinSpec(m_fileMap.get("experiment")).divide(m_exposureMap.get("expintegration"))).substract(Spectra.spectraFromWinSpec(m_fileMap.get("bgexperiment")).divide(m_exposureMap.get("expbgintegration"))));
            correctedCallibrationSpectra = new Spectra((Spectra.spectraFromWinSpec(m_fileMap.get("callibration")).divide(m_exposureMap.get("callintegration"))).substract(Spectra.spectraFromWinSpec(m_fileMap.get("bgcallibration")).divide(m_exposureMap.get("callbgintegration"))));
            correctedWLNoSample = new Spectra((Spectra.spectraFromWinSpec(m_fileMap.get("whitelightnosample")).divide(m_exposureMap.get("wlnosampleintegration"))).substract(Spectra.spectraFromWinSpec(m_fileMap.get("bgwlnosample")).divide(m_exposureMap.get("wlnosamplebgintegration"))));
            correctedWLSample = new Spectra((Spectra.spectraFromWinSpec(m_fileMap.get("whitelightwithsample")).divide(m_exposureMap.get("wlsampleintegration"))).substract(Spectra.spectraFromWinSpec(m_fileMap.get("bgwlsample")).divide(m_exposureMap.get("wlsamplebgintegration"))));
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
            sampleRelativeIntensity = new Spectra(correctedSampleSpectra.divide(correctedCallibrationSpectra));
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
            whiteLightDivision = new Spectra(correctedWLNoSample.divide(correctedWLSample));
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
            (new Spectra(Spectra.callibrationAbsoluteIntensitySpectra(m_fileMap.get("lightintensity")).multiply(sampleRelativeIntensity).multiply(whiteLightDivision))).logToFile(m_fileMap.get("output"));
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
