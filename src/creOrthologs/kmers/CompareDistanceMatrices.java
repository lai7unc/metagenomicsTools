package creOrthologs.kmers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class CompareDistanceMatrices
{
	private static class Holder
	{
		double dist1;
		double dist2;
	}
	
	private static void addToHolder(File file, List<Holder> list, boolean first ) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		int num = Integer.parseInt(reader.readLine());
		
		int index =0;
		for( int x=0; x < num ; x++)
		{
			String s= reader.readLine();
			StringTokenizer sToken = new StringTokenizer(s);
			
			for( int y=0; y < num ; y++)
			{
				if( first)
				{
					Holder h = new Holder();
					list.add(h);
					h.dist1 = Double.parseDouble(sToken.nextToken());
				}
				else
				{
					Holder h = list.get(index);
					h.dist2 = Double.parseDouble(sToken.nextToken());
					index++;
				}
			}
			
			if( sToken.hasMoreTokens())
				throw new Exception("no");
		}
		
		
		reader.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		File file1 = new File(ConfigReader.getCREOrthologsDir() + File.separator + 
				"gatheredKmerMatrices" + File.separator + "allDist.txt");
		
		File file2 = new File(ConfigReader.getCREOrthologsDir() + File.separator + 
				"gatheredKmerMatrices" + File.separator + "sub.txt");
	
		List<Holder> list = new ArrayList<CompareDistanceMatrices.Holder>();
		
		addToHolder(file1, list, true);
		addToHolder(file2, list, false);
		
		writeResults(list);
	}
	
	private static void writeResults(List<Holder> list) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				new File(ConfigReader.getCREOrthologsDir() + File.separator + 
				"gatheredKmerMatrices" + File.separator + "allDist.txt")));
		
		writer.write("allDist\tsubDist\n");
		
		for( Holder h : list)
			writer.write( h.dist1  + "\t" + h.dist2 + "\n");
		
		writer.flush();  writer.close();
		
	}
}
