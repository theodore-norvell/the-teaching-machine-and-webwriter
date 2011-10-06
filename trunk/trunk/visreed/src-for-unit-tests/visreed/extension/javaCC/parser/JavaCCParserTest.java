/**
 * JavaCCParserTest.java
 * 
 * @date: Oct 3, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

import tm.backtrack.BTTimeManager;
import visreed.extension.javaCC.model.JavaCCWholeGraph;
import visreed.model.VisreedNode;

/**
 * @author Xiaoyu Guo
 *
 */
public class JavaCCParserTest {
	private JavaCCWholeGraph wg;
	private BTTimeManager timeMan;

	@Before
	public void setUp(){
		this.timeMan = new BTTimeManager();
		this.wg = new JavaCCWholeGraph(timeMan);
	}
	
    /**
     * @param source
     * @throws ParseException 
     */
    private void testParse(
        String source,
        String expected
    ) throws ParseException {
        StringReader reader = new StringReader(source);
        
        try {
            VisreedNode resultNode = JavaCCParser.parse(wg, reader);
            String result = resultNode.getPayload().format(resultNode);
            assertEquals(expected, result);
        } catch (ParseException e) {
            e.printStackTrace();
            throw(e);
        }
    }
	
    /**
     * @param source
     * @throws ParseException 
     */
    private void testParseFile(
        String sourceFileName,
        String expected
    ) throws ParseException {
		try {
			FileReader reader = new FileReader(sourceFileName);
            VisreedNode resultNode = JavaCCParser.parse(wg, reader);
            String result = resultNode.getPayload().format(resultNode);
            assertEquals(expected, result);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (ParseException e) {
	        e.printStackTrace();
	        throw(e);
	    }
    }
	
	/**
	 * Test method for {@link visreed.extension.javaCC.parser.JavaCCParser#parse(visreed.extension.javaCC.model.JavaCCWholeGraph, java.io.Reader)}.
	 */
	@Test
	public void testParse() {
		try {
			testParseFile("./bin/visreed/extension/javaCC/parser/testFiles/phone.jj", "");
		} catch (ParseException e) {
			fail();
		}
	}

}
