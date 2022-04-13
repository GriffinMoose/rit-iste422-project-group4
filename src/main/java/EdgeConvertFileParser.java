import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;
import javax.swing.*;

public abstract class EdgeConvertFileParser {

	public static Logger logger = LogManager.getLogger(EdgeConvertFileParser.class.getName());
	
   //private String filename = "test.edg";
   protected File parseFile;
   protected FileReader fr;
   protected BufferedReader br;
   protected String currentLine;
   protected ArrayList alTables, alFields, alConnectors;
   protected EdgeTable[] tables;
   protected EdgeField[] fields;
   protected EdgeField tempField;
   protected EdgeConnector[] connectors;
   protected String style;
   protected String text;
   protected String tableName;
   protected String fieldName;
   protected boolean isEntity, isAttribute, isUnderlined = false;
   protected int numFigure, numConnector, numFields, numTables, numNativeRelatedFields;
   protected int endPoint1, endPoint2;
   protected int numLine;
   protected String endStyle1, endStyle2;
   public static final String EDGE_ID = "EDGE Diagram File"; //first line of .edg files should be this
   public static final String SAVE_ID = "EdgeConvert Save File"; //first line of save files should be this
   public static final String DELIM = "|";
   
   public EdgeConvertFileParser(File constructorFile) {
      numFigure = 0;
      numConnector = 0;
      alTables = new ArrayList();
      alFields = new ArrayList();
      alConnectors = new ArrayList();
      isEntity = false;
      isAttribute = false;
      parseFile = constructorFile;
      numLine = 0;
      this.openFile(parseFile);
   }

		abstract void parseEdgeFile() throws IOException;
   
   abstract void resolveConnectors();
   
   abstract void parseSaveFile() throws IOException;

   private void makeArrays() { //convert ArrayList objects into arrays of the appropriate Class type
      if (alTables != null) {
         tables = (EdgeTable[])alTables.toArray(new EdgeTable[alTables.size()]);
      }
      if (alFields != null) {
         fields = (EdgeField[])alFields.toArray(new EdgeField[alFields.size()]);
      }
      if (alConnectors != null) {
         connectors = (EdgeConnector[])alConnectors.toArray(new EdgeConnector[alConnectors.size()]);
      }
   }
   
   protected boolean isTableDup(String testTableName) {
      for (int i = 0; i < alTables.size(); i++) {
         EdgeTable tempTable = (EdgeTable)alTables.get(i);
         if (tempTable.getName().equals(testTableName)) {
					 logger.debug("This Table is a duplicate");
            return true;
					 
         }
      }
      return false;
   }
   
   public EdgeTable[] getEdgeTables() {
      return tables;
   }
   
   public EdgeField[] getEdgeFields() {
      return fields;
   }
   
   public void openFile(File inputFile) {
		 
      try {
					logger.debug("Opening File: " + inputFile.getName());
         fr = new FileReader(inputFile);
         br = new BufferedReader(fr);
         //test for what kind of file we have
         currentLine = br.readLine().trim();
         numLine++;
         if (currentLine.startsWith(EDGE_ID)) { //the file chosen is an Edge Diagrammer file
            this.parseEdgeFile(); //parse the file
            br.close();
            this.makeArrays(); //convert ArrayList objects into arrays of the appropriate Class type
            this.resolveConnectors(); //Identify nature of Connector endpoints
         } else {
            if (currentLine.startsWith(SAVE_ID)) { //the file chosen is a Save file created by this application
               this.parseSaveFile(); //parse the file
               br.close();
               this.makeArrays(); //convert ArrayList objects into arrays of the appropriate Class type
							logger.debug("File opened ... Array Lists Created");
            } else { //the file chosen is something else
               JOptionPane.showMessageDialog(null, "Unrecognized file format");
            }
         }
      } // try
      catch (FileNotFoundException fnfe) {
				 logger.fatal("FileNotFoundException: " + fnfe);
         System.out.println("Cannot find \"" + inputFile.getName() + "\".");
         Runtime.getRuntime().halt(0);
      } // catch FileNotFoundException
      catch (IOException ioe) {
				 logger.fatal("IOException: " + ioe);
         System.out.println(ioe);
         Runtime.getRuntime().halt(0);
      } // catch IOException
   } // openFile()
} // EdgeConvertFileHandler
