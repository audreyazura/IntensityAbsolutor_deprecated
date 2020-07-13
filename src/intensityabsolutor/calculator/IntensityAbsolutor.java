/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package intensityabsolutor.calculator;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

/**
 *
 * @author audreyazura
 */
public class IntensityAbsolutor
{
    static public void calculator(File sampleSpectraFile, BigDecimal sampleExposure, File callibrationSpectraFile, BigDecimal callibrationExposure, File whiteLightNoSampleFile, BigDecimal noSampleExposure, File whiteLightWithSampleFile, BigDecimal withSampleExposure, File lightIntensityFile, File outputFile)
    {
        Spectra sampleRelativeIntensity = new Spectra();
        Spectra whiteLightDivision = new Spectra();
        
        try
        {
            sampleRelativeIntensity = (Spectra) Spectra.spectraFromWinSpec(sampleSpectraFile).multiply(sampleExposure).divide(Spectra.spectraFromWinSpec(callibrationSpectraFile).multiply(callibrationExposure));
        }
        catch (DataFormatException ex)
        {
            Logger.getLogger(IntensityAbsolutor.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            Logger.getLogger(IntensityAbsolutor.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(IntensityAbsolutor.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ArithmeticException ex)
        {
            Logger.getLogger(IntensityAbsolutor.class.getName()).log(Level.SEVERE, "Null value in the intensities of the callibration light", ex);
        }
        
        try
        {
            whiteLightDivision = (Spectra) Spectra.spectraFromWinSpec(whiteLightNoSampleFile).multiply(noSampleExposure).divide(Spectra.spectraFromWinSpec(whiteLightWithSampleFile).multiply(withSampleExposure));
        }
        catch (DataFormatException ex)
        {
            Logger.getLogger(IntensityAbsolutor.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            Logger.getLogger(IntensityAbsolutor.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(IntensityAbsolutor.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ArithmeticException ex)
        {
            Logger.getLogger(IntensityAbsolutor.class.getName()).log(Level.SEVERE, "Null value in the intensities of the white light file with sample", ex);
        }
        
        try
        {
            ((Spectra) (Spectra.callibrationAbsoluteIntensitySpectra(lightIntensityFile).multiply(sampleRelativeIntensity).multiply(whiteLightDivision))).logToFile(outputFile);
        }
        catch (DataFormatException ex)
        {
            Logger.getLogger(IntensityAbsolutor.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            Logger.getLogger(IntensityAbsolutor.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(IntensityAbsolutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
