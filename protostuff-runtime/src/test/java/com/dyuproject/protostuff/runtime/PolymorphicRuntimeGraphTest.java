//========================================================================
//Copyright 2007-2011 David Yu dyuproject@gmail.com
//------------------------------------------------------------------------
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at 
//http://www.apache.org/licenses/LICENSE-2.0
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//========================================================================

package com.dyuproject.protostuff.runtime;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dyuproject.protostuff.AbstractTest;
import com.dyuproject.protostuff.GraphTest;
import com.dyuproject.protostuff.Schema;

/**
 * Test ser/deser of polymorphic graph objects (references and cyclic dependencies).
 *
 * @author David Yu
 * @created Jan 19, 2011
 */
public class PolymorphicRuntimeGraphTest extends AbstractTest
{
    
    public static abstract class Node
    {
        ContainerNode parent;
        String tagName;
        String id;
        String name;
        String text;
        Map<String,String> attributes;
        Document root;
        
        public Node()
        {
            
        }
        
        public Node(ContainerNode parent, String tagName, String id, Document root)
        {
            this.parent = parent;
            this.tagName = tagName;
            this.id = id;
            this.root = root;
            
            if(parent != null)
                parent.addNode(this);
            
            if(id != null)
                root.addNodeById(this);
                
        }
        
        public void setAttribute(String key, String value)
        {
            if(attributes == null)
                attributes = new HashMap<String,String>();
            
            attributes.put(key, value);
        }
        
        public String getAttribute(String key)
        {
            return attributes == null ? null : attributes.get(key);
        }
        
        public String toString()
        {
            return getClass().getSimpleName();
        }
    }
    
    public static abstract class ContainerNode extends Node
    {

        List<Node> nodes = new ArrayList<Node>();
        
        public ContainerNode()
        {
            
        }

        public ContainerNode(ContainerNode parent, String name, String id, Document root)
        {
            super(parent, name, id, root);
        }
        
        public void addNode(Node node)
        {
            nodes.add(node);
            node.parent = this;
        }
        
        public Node getNode(int index)
        {
            return nodes.get(index);
        }
        
        public int getNodeCount()
        {
            return nodes.size();
        }
        
    }
    
    public static class Document extends ContainerNode
    {
        
        Map<String,Node> idMap;
        
        public Document()
        {
            
        }
        
        public Document(ContainerNode parent)
        {
            super(parent, "html", null, null);
        }
        
        public void addNodeById(Node node)
        {
            if(idMap == null)
                idMap = new HashMap<String,Node>();
            
            idMap.put(node.id, node);
        }
        
        public Node getNodeById(String id)
        {
            return idMap == null ? null : idMap.get(id);
        }
    }
    
    public static class Head extends ContainerNode
    {
        public Head()
        {
            
        }
        
        public Head(ContainerNode parent, Document root)
        {
            super(parent, "head", null, root);
        }
    }
    
    public static class Title extends Node
    {
        public Title()
        {
            
        }
        
        public Title(ContainerNode parent, Document root)
        {
            super(parent, "title", null, root);
        }
    }
    
    public static class Link extends Node
    {
        String rel;
        String type;
        String href;
        
        public Link()
        {
            
        }
        
        public Link(ContainerNode parent, Document root)
        {
            super(parent, "link", null, root);
        }
    }
    
    public static class Body extends ContainerNode
    {
        boolean hidden;
        
        public Body()
        {
            
        }
        
        public Body(ContainerNode parent, Document root)
        {
            super(parent, "body", null, root);
        }
    }
    
    public static class Div extends ContainerNode
    {
        public enum Display
        {
            INLINE, BLOCK;
        }
        
        // default is block
        Display display;
        
        public Div()
        {
            
        }
        
        public Div(ContainerNode parent, String id, Document root)
        {
            super(parent, "div", id, root);
        }
    }
    
    public static class TextArea extends Node
    {
        int rows;
        int cols;
        
        public TextArea()
        {
            
        }
        
        public TextArea(ContainerNode parent, String id, Document root)
        {
            super(parent, "textarea", id, root);
        }
    }
    
    public static class HtmlDocumentRequest
    {
        Document document;
    }
    
    /*
     * Scenario:
     * 
     * <html>
     *   <head>
     *     <title>Hello world from protostuff-runtime graph ser/deser!</title>
     *     <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
     *   </head>
     *   <body>
     *     <div id="firstDiv" name="firstDiv" foo="bar" display="inline">TextArea</div>
     *     <div id="secondDiv" name="secondDiv">
     *       <textarea id="ta" name="ta" rows="10" cols="20" />
     *     </div>
     *   </body>
     * </html>
     * 
     */
    public void testPolymorphicAndCyclic() throws Exception
    {
        Schema<HtmlDocumentRequest> schema = 
            RuntimeSchema.getSchema(HtmlDocumentRequest.class);

        Document document = new Document(null);

        attachHead(document);
        attachBody(document);
        
        HtmlDocumentRequest docRequest = new HtmlDocumentRequest();
        docRequest.document = document;
        
        verifyGraph(docRequest);
        byte[] data = GraphTest.toByteArray(docRequest, schema);
        
        HtmlDocumentRequest docRequestFromByteArray = new HtmlDocumentRequest();
        GraphTest.mergeFrom(data, 0, data.length, docRequestFromByteArray, schema);
        
        verifyGraph(docRequestFromByteArray);
        
        HtmlDocumentRequest docRequestFromStream = new HtmlDocumentRequest();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        GraphTest.mergeFrom(in, docRequestFromStream, schema);
        
        verifyGraph(docRequestFromStream);
    }
    
    static void attachHead(Document document)
    {
        Head head = new Head(document, document);
        
        Title title = new Title(head, document);
        title.text = "Hello world from protostuff-runtime graph ser/deser!";
        
        Link link = new Link(head, document);
        link.rel = "shortcut icon";
        link.href = "/favicon.ico";
        link.type = "image/x-icon";
    }
    
    static void attachBody(Document document)
    {
        Body body = new Body(document, document);
        Div div = new Div(body, "firstDiv", document);
        div.name = "firstDiv";
        div.text = "TextArea";
        div.setAttribute("foo", "bar");
        div.display = Div.Display.INLINE;
        
        Div anotherDiv = new Div(body, "secondDiv", document);
        anotherDiv.name = "secondDiv";
        anotherDiv.display = Div.Display.BLOCK;
        
        TextArea ta = new TextArea(anotherDiv, "ta", document);
        ta.name = "ta";
        ta.rows = 10;
        ta.cols = 20;
    }
    
    static void verifyGraph(HtmlDocumentRequest docRequest)
    {
        assertNotNull(docRequest);
        
        Document document = docRequest.document;
        
        assertNotNull(document);
        
        assertTrue(document.getNodeCount() == 2);
        assertTrue(document.getNode(0) instanceof Head);
        assertTrue(document.getNode(1) instanceof Body);
        
        Head head = (Head)document.getNode(0);
        assertTrue(head.parent == document);
        assertTrue(head.root == document);
        assertEquals(head.tagName, "head");
        
        assertTrue(head.getNodeCount() == 2);
        assertTrue(head.getNode(0) instanceof Title);
        assertTrue(head.getNode(1) instanceof Link);
        
        Title title = (Title)head.getNode(0);
        assertTrue(title.parent == head);
        assertTrue(title.root == document);
        assertEquals(title.tagName, "title");
        assertEquals(title.text, "Hello world from protostuff-runtime graph ser/deser!");
        
        Link link = (Link)head.getNode(1);
        assertTrue(link.parent == head);
        assertTrue(link.root == document);
        assertEquals(link.tagName, "link");
        assertEquals(link.rel, "shortcut icon");
        assertEquals(link.href, "/favicon.ico");
        assertEquals(link.type, "image/x-icon");
        
        Body body = (Body)document.getNode(1);
        assertTrue(body.parent == document);
        assertTrue(body.root == document);
        assertEquals(body.tagName, "body");
        
        assertTrue(body.getNodeCount() == 2);
        assertTrue(body.getNode(0) instanceof Div);
        assertTrue(body.getNode(1) instanceof Div);
        
        Div div = (Div)body.getNode(0);
        assertTrue(div.parent == body);
        assertTrue(div.root == document);
        assertEquals(div.tagName, "div");
        assertEquals(div.id, "firstDiv");
        assertEquals(div.name, "firstDiv");
        assertEquals(div.text, "TextArea");
        assertEquals("bar", div.getAttribute("foo"));
        assertTrue(div.display == Div.Display.INLINE);
        assertTrue(div.getNodeCount() == 0);
        
        Div anotherDiv = (Div)body.getNode(1);
        assertTrue(anotherDiv.parent == body);
        assertTrue(anotherDiv.root == document);
        assertEquals(anotherDiv.tagName, "div");
        assertEquals(anotherDiv.id, "secondDiv");
        assertEquals(anotherDiv.name, "secondDiv");
        assertTrue(anotherDiv.display == Div.Display.BLOCK);
        assertNull(anotherDiv.text);
        
        assertTrue(anotherDiv.getNodeCount() == 1);
        assertTrue(anotherDiv.getNode(0) instanceof TextArea);
        
        TextArea ta = (TextArea)anotherDiv.getNode(0);
        assertTrue(ta.parent == anotherDiv);
        assertTrue(ta.root == document);
        assertEquals(ta.tagName, "textarea");
        assertEquals(ta.id, "ta");
        assertEquals(ta.name, "ta");
        assertTrue(ta.rows == 10);
        assertTrue(ta.cols == 20);
        
        assertTrue(document.getNodeById("firstDiv") == div);
        assertTrue(document.getNodeById("secondDiv") == anotherDiv);
        assertTrue(document.getNodeById("ta") == ta);
    }
    
    public static abstract class LinkedElement
    {
        String name;
        LinkedElement next;
        
        public LinkedElement()
        {
            
        }
        
        public LinkedElement(String name, LinkedElement previous)
        {
            this.name = name;
            if(previous != null)
                previous.next = this;
        }
    }
    
    public static class HotElement extends LinkedElement
    {
        
        public HotElement()
        {
            
        }

        public HotElement(LinkedElement previous)
        {
            super("hot", previous);
        }
        
    }
    
    public static class ColdElement extends LinkedElement
    {
        
        public ColdElement()
        {
            
        }
        
        public ColdElement(LinkedElement previous)
        {
            super("cold", previous);
        }
    }
    
    public static class LinkedElementContainer
    {
        LinkedElement element;
    }
    
    public void testPolymorphicSelfReference() throws Exception
    {
        Schema<LinkedElementContainer> schema = 
            RuntimeSchema.getSchema(LinkedElementContainer.class);
        
        HotElement hot = new HotElement(null);
        ColdElement cold = new ColdElement(hot);
        // self reference
        cold.next = cold;
        
        LinkedElementContainer container = new LinkedElementContainer();
        container.element = hot;
        
        verifyGraph(container);
        
        byte[] data = GraphTest.toByteArray(container, schema);
        
        LinkedElementContainer containerFromByteArray = new LinkedElementContainer();
        GraphTest.mergeFrom(data, 0, data.length, containerFromByteArray, schema);
        
        verifyGraph(containerFromByteArray);
        
        LinkedElementContainer containerFromStream = new LinkedElementContainer();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        GraphTest.mergeFrom(in, containerFromStream, schema);
        
        verifyGraph(containerFromStream);
    }
    
    static void verifyGraph(LinkedElementContainer container)
    {
        assertNotNull(container);
        
        LinkedElement element = container.element;
        assertTrue(element instanceof HotElement);
        
        HotElement hot = (HotElement)element;
        assertEquals(hot.name, "hot");
        
        assertTrue(hot.next instanceof ColdElement);
        
        ColdElement cold = (ColdElement)hot.next;
        assertEquals(cold.name, "cold");
        
        assertTrue(cold == cold.next);
    }

}
