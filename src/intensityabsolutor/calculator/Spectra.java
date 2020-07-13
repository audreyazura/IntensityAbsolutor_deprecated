/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package intensityabsolutor.calculator;

import commonutils.ContinuousFunction;
import commonutils.PhysicsTools;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
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
    
    private Spectra (File p_inputFile, BigDecimal p_abscissaUnitMultiplier, BigDecimal p_valuesUnitMultiplier, String p_expectedExtension, int p_ncolumn, int[] p_columnToExtract) throws FileNotFoundException, DataFormatException, ArrayIndexOutOfBoundsException, IOException
    {
        super(p_inputFile, p_abscissaUnitMultiplier, p_valuesUnitMultiplier, p_expectedExtension, p_ncolumn, p_columnToExtract);
    }
    
    public Spectra()
    {
        super();
    }
    
    public void logToFile(File outputFile)
    {
        
    }
}
