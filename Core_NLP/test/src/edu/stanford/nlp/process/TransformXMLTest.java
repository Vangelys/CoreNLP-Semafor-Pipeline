package edu.stanford.nlp.process;

import edu.stanford.nlp.objectbank.IdentityFunction;
import edu.stanford.nlp.util.StringUtils;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.function.Function;


/** @author Christopher Manning */
public class TransformXMLTest extends TestCase {

  private String testCase =
          "<doc><el arg=\"funny&amp;'&gt;&quot;stuff\">yo! C&amp;C! </el></doc>";

  private String expectedAnswer =
          "<doc> <el arg=\"funny&amp;&apos;&gt;&quot;stuff\"> yo! C&amp;C! </el> </doc>";

  private String expectedAnswer2 =
          "<doc> <el arg=\"funny&amp;&apos;&gt;&quot;stuff\"> yo! C&amp;C!yo! C&amp;C! </el> </doc>";

  private Function<String,String> duplicate = (String in)->{ return in + in;};

  public void testTransformXML1() {
    TransformXML<String> tx = new TransformXML<>();
    StringWriter sw = new StringWriter();
    tx.transformXML(StringUtils.EMPTY_STRING_ARRAY, 
                    new IdentityFunction<String>(),
                    new ByteArrayInputStream(testCase.getBytes()),
                    sw);
    String answer = sw.toString().replaceAll("\\s+", " ").trim();
    assertEquals("Bad XML transform", expectedAnswer, answer);
    sw = new StringWriter();
    tx.transformXML(new String[] {"el"},
                    duplicate,
                    new ByteArrayInputStream(testCase.getBytes()),
                    sw);
    String answer2 = sw.toString().replaceAll("\\s+", " ").trim();
    assertEquals("Bad XML transform", expectedAnswer2, answer2);
  }

}
