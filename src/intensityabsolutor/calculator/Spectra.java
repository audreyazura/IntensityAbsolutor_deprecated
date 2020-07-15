/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package intensityabsolutor.calculator;

import commonutils.ContinuousFunction;
import commonutils.PhysicsTools;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.DataFormatException;

/**
 *
 * @author audreyazura
 */
public class Spectra extends ContinuousFunction
{
    static public Spectra spectraFromWinSpec(File p_winSpecFile) throws FileNotFoundException, DataFormatException, ArrayIndexOutOfBoundsException, IOException
    {
        return new Spectra(p_winSpecFile, PhysicsTools.UnitsPrefix.NANO.getMultiplier(), PhysicsTools.UnitsPrefix.UNITY.getMultiplier(), "txt", 3, new int[] {0,2});
    }
    static public Spectra callibrationAbsoluteIntensitySpectra(File p_passedFile) throws FileNotFoundException, DataFormatException, ArrayIndexOutOfBoundsException, IOException
    {
        return new Spectra(p_passedFile, PhysicsTools.UnitsPrefix.NANO.getMultiplier(), PhysicsTools.UnitsPrefix.UNITY.getMultiplier().divide(PhysicsTools.UnitsPrefix.NANO.getMultiplier()), "intensity", 2, new int[] {0,1});
    }
    
    public Spectra()
    {
        super();
    }
    
    private Spectra (File p_inputFile, BigDecimal p_abscissaUnitMultiplier, BigDecimal p_valuesUnitMultiplier, String p_expectedExtension, int p_ncolumn, int[] p_columnToExtract) throws FileNotFoundException, DataFormatException, ArrayIndexOutOfBoundsException, IOException
    {
        super(p_inputFile, p_abscissaUnitMultiplier, p_valuesUnitMultiplier, p_expectedExtension, p_ncolumn, p_columnToExtract);
    }
    
    public void logToFile(File outputFile) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        Set<BigDecimal> abscissa = new TreeSet(m_values.keySet());
        
        writer.write("Wavelength (nm)\tAbsolute intensity (μW/nm)");
        for(BigDecimal currentAbscissa: abscissa)
        {
            BigDecimal convertedIntensity = currentAbscissa.multiply(PhysicsTools.UnitsPrefix.NANO.getMultiplier()).divide(PhysicsTools.UnitsPrefix.MICRO.getMultiplier());
            
            writer.newLine();
            writer.write(currentAbscissa.divide(PhysicsTools.UnitsPrefix.NANO.getMultiplier())+"\t"+convertedIntensity);
        }
        writer.flush();
        writer.close();
    }
}
