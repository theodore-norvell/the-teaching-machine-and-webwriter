/**
 * JavaCCTagTest.java
 * 
 * @date: Aug 29, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import static org.junit.Assert.*;

import org.junit.Test;

import visreed.model.tag.VisreedTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class JavaCCTagTest {

    /**
     * Test method for {@link visreed.model.tag.VisreedTag#values()}.
     */
    @Test
    public void testValues() {
        VisreedTag[] result = JavaCCTag.values();
    }

}
