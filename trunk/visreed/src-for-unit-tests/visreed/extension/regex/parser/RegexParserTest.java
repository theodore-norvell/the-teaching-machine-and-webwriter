/**
 * RegexParserTest.java
 * 
 * @date: Jun 29, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.regex.parser;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

import tm.backtrack.BTTimeManager;
import visreed.extension.regex.model.RegexNode;
import visreed.model.VisreedNode;
import visreed.model.VisreedWholeGraph;

/**
 * @author Xiaoyu Guo
 *
 */
public class RegexParserTest {

    @Test
    public void testParseCase1() {
        String source = "abc";
        String expected = "abc";
        try {
            testParse(source, expected);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void testParseCase2() {
        String source = "abc+";
        String expected = "(abc)+";
        try {
            testParse(source, expected);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void testParseCase3() {
        String source = "a+b*c";
        String expected = "a+;b*;c";
        try {
            testParse(source, expected);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void testParseCase4() {
        String source = "a+|b*c";
        String expected = "(a+|b*;c)";
        try {
            testParse(source, expected);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void testParseCase5() {
        String source = "(ab+c*)|(abc)+|c";
        String expected = "((ab)+;c*|(abc)+|c)";
        try {
            testParse(source, expected);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
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
        BTTimeManager timeMan = new BTTimeManager();
        VisreedWholeGraph wg = new VisreedWholeGraph(timeMan);
        
        try {
            VisreedNode resultNode = RegexParser.parse(wg, reader);
            String result = resultNode.getPayload().format(resultNode);
            assertEquals(expected, result);
        } catch (ParseException e) {
            e.printStackTrace();
            throw(e);
        }
    }

}
