package com.aptana.plist;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import ch.randelshofer.quaqua.util.BinaryPListParser;

import com.aptana.plist.xml.XMLPListParser;

public class PListParserFactory
{

	public static Map<String, Object> parse(File file) throws IOException
	{
		try
		{
			// Try binary format. This should fail quickly with IOException if it's not binary!
			BinaryPListParser parser = new BinaryPListParser();
			return parser.parse(file);
		}
		catch (IOException e)
		{
			// Assume XML now...
			XMLPListParser parser = new XMLPListParser();
			return parser.parse(file);
		}
	}
}
