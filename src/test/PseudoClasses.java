package test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import cz.vutbr.web.css.CSSFactory;
import cz.vutbr.web.css.NodeData;
import cz.vutbr.web.css.Selector.PseudoDeclaration;
import cz.vutbr.web.css.TermColor;
import cz.vutbr.web.css.TermFactory;
import cz.vutbr.web.csskit.MatchConditionOnElements;
import cz.vutbr.web.domassign.StyleMap;

public class PseudoClasses {
	private static final Logger log = LoggerFactory.getLogger(PseudoClasses.class);
	
	private static TermFactory tf = CSSFactory.getTermFactory();
	
	@BeforeClass
	public static void init() throws SAXException, IOException {
		log.info("\n\n\n == DOMAssign test at {} == \n\n\n", new Date());
	}
	
    @Test
    public void pseudoClassMap() throws SAXException, IOException {  
        
        DOMSource ds = new DOMSource(new FileInputStream("data/simple/pseudo.html"));
        Document doc = ds.parse();
        ElementMap elements = new ElementMap(doc);
        
        MatchConditionOnElements cond = new MatchConditionOnElements(doc.getElementById("l2"), PseudoDeclaration.HOVER);
        cond.addMatch(doc.getElementById("l3"), PseudoDeclaration.VISITED);
        CSSFactory.registerDefaultMatchCondition(cond);
        
        StyleMap decl = CSSFactory.assignDOM(doc, null, createBaseFromFilename("data/simple/pseudo.html"),"screen", true);
        
        NodeData l1 = getStyleById(elements, decl, "l1");
        NodeData l2 = getStyleById(elements, decl, "l2");
        NodeData l3 = getStyleById(elements, decl, "l3");
        
        assertThat(l1.getValue(TermColor.class, "color"), is(tf.createColor(0,255,0)));
        assertThat(l2.getValue(TermColor.class, "color"), is(tf.createColor(0,255,255)));
        assertThat(l3.getValue(TermColor.class, "color"), is(tf.createColor(0,0,170)));
        
    }
    
    
    private NodeData getStyleById(ElementMap elements, StyleMap decl, String id)
    {
        NodeData data = decl.get(elements.getElementById(id));
        assertNotNull("Data for #" + id + " exists", data);
        return data;
    }
    
	private static URL createBaseFromFilename(String filename) {
		try {
			File f = new File(filename);
			return f.toURI().toURL();
		} catch (MalformedURLException e) {
			return null;
		}
	}
}