package scripts.markSeqsAug2015;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class PivotAllLevels
{
	private static final String[] LEVELS = { "otu", "k", "p", "c", "o", "f", "g" };
	public static final String UNCLASSIFIED = "UNCLASSIFIED";
	
	public static void main(String[] args) throws Exception
	{
		File inFile = new File(
				ConfigReader.getMarkAug2015Batch1Dir() + File.separator + "qiime18_Freads_cr.txt");
		
		List<String> sampleNames = getSampleNames(inFile);
		
		for( String s : LEVELS)
		{
			System.out.println(s);
			HashMap<String, List<Long>> map = getCounts(s, inFile);
		}
	}
	
	private static HashMap<String, List<Long>> getCounts(String level, File inFile) throws Exception
	{
		HashMap<String, List<Long>> map = new HashMap<String, List<Long>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		reader.readLine();
		
		for(String s= reader.readLine() ; s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String id = splits[0];
			
			if( ! level.equals("otu"))
				id = getTaxonomy(splits[splits.length-1], level);
			
			List<Long> innerList = map.get(id);
			
			if( innerList == null)
			{
				innerList = new ArrayList<Long>();
				
				for( int x=1; x < splits.length-1; x++)
					innerList.add(Long.parseLong( new StringTokenizer(splits[x], ".").nextToken()));
			}
			else
			{
				for( int x=1; x < splits.length-1; x++)
					innerList.set((x-1) , 
							innerList.get(x-1) + Long.parseLong(new StringTokenizer(splits[x], ".").nextToken()));
			}
		}
		
		reader.close();
		
		return map;
	}
	
	private static String getTaxonomy(String lastToken, String level) throws Exception
	{
		StringTokenizer sToken = new StringTokenizer(lastToken, ";");
		
		while( sToken.hasMoreTokens())
		{
			String nextToken = sToken.nextToken().trim();
			
			if( nextToken.startsWith(level))
			{
				String taxa = nextToken.substring(3).trim();
				
				if( taxa.length() == 0 )
					taxa = UNCLASSIFIED;
				
				return taxa;
			}
		}
		
		throw new Exception("Could not find " + level + " in " + lastToken);
	}
	
	private static List<String> getSampleNames(File inFile) throws Exception
	{
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
			
		List<String> list = new ArrayList<String>();
		
		String[] splits = reader.readLine().split("\t");
		
		for( int x=1; x < splits.length - 1; x++)
			list.add(splits[x]);
		
		reader.close();
		return list;
	}
}
