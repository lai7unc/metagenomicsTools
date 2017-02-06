package scripts.topeOneAtATime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.NumberFormat;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class CountSignificantHits
{
	private static final double THRESHOLD = 0.05;
	
	public static void main(String[] args) throws Exception
	{
		//addSignifiant("caseControl", 19, 6);
		//addSignifiant("tic", 21, 14);
		addSignifiant("sex", 24, 12,11);
		
	}
	
	private static void addSignifiant(String text, int sigColumn, int rSquaredColumn, int pValueUncorrected)
		throws Exception
	{
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(4);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getTopeOneAtATimeDir() 
				+ File.separator + text + "sigHits.txt")));
		
		writer.write("level\ttaxa\tpValue\trSquared\tcorrectedPvalue\totuClassificaiton\n");
		
		for(int x=1; x < NewRDPParserFileLine.TAXA_ARRAY_PLUS_OTU.length; x++)
		{
			String fileName = "metapValuesFor_otu_read1_WithTaxa.txt";
						
			String taxa = NewRDPParserFileLine.TAXA_ARRAY_PLUS_OTU[x];
			
			if( ! taxa.equals("otu"))
			{
				fileName = "metapValuesFor_" + taxa + "_read1_.txt";
			}
			
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					ConfigReader.getTopeOneAtATimeDir() + File.separator + 
						"merged" + File.separator + fileName)));
			
			reader.readLine();
			
			for(String s= reader.readLine(); s != null; s = reader.readLine())
			{
				String[] splits = s.split("\t");
				
				Double pValue = Double.parseDouble(splits[sigColumn]);
				
				if( pValue <= THRESHOLD)
				{
					System.out.println( text + " " + taxa + " "+  splits[0] +  " " + pValue + " " + Double.parseDouble(splits[rSquaredColumn]) );
					
					
					writer.write(taxa + "\t" + splits[0] + "\t" + 
					nf.format(Double.parseDouble(splits[pValueUncorrected]))  + "\t" + 
										nf.format(Double.parseDouble(splits[rSquaredColumn]))
									+ "\t" + nf.format(Double.parseDouble(splits[sigColumn])));
					
					if( taxa.equals("otu") )
						writer.write("\t" + splits[splits.length -1]);
					
					writer.write("\n");
				}
			}
			
			reader.close();
		}

		writer.flush();  writer.close();
	}
}
