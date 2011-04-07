//========================================================================
//Copyright 2007-2010 David Yu dyuproject@gmail.com
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

package com.dyuproject.protostuff.me;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Test ser/deser of graph objects (references and cyclic dependencies).
 *
 * @author David Yu
 * @created Dec 10, 2010
 */
public class GraphTest extends AbstractTest
{
    
    static final int INITIAL_CAPACITY = 32;
    
    public static <T> byte[] toByteArray(T message, Schema schema) throws IOException
    {
        return GraphIOUtil.toByteArray(message, schema, buf());
    }
    
    public static <T> void mergeFrom(byte[] data, int offset, int length, 
            T message, Schema schema) throws IOException
    {
        GraphIOUtil.mergeFrom(data, offset, length, message, schema);
    }
    
    public static <T> void mergeFrom(InputStream in, T message, Schema schema) 
    throws IOException
    {
        GraphIOUtil.mergeFrom(in, message, schema);
    }
    
    public void testReference() throws Exception
    {
        Foo fooCompare = newFoo();
        
        // verify
        checkReference(fooCompare);
        
        byte[] data = toByteArray(fooCompare, Foo.getSchema());
        
        
        // from byte array
        
        Foo fooFromByteArray = new Foo();
        
        mergeFrom(data, 0, data.length, fooFromByteArray, Foo.getSchema());
        
        SerializableObjects.assertEquals(fooCompare, fooFromByteArray);
        
        // verify parsed message
        checkReference(fooFromByteArray);
        
        
        // from inputstream
        
        Foo fooFromStream = new Foo();
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        
        mergeFrom(in, fooFromStream, Foo.getSchema());
        
        SerializableObjects.assertEquals(fooCompare, fooFromStream);
        
        // verify parsed message
        checkReference(fooFromStream);
    }
    
    
    static Foo newFoo()
    {
        // one-instance of Baz.
        
        Baz baz = new Baz();
        baz.setId(100);
        
        Bar bar1 = new Bar();
        bar1.setSomeInt(1);
        bar1.setSomeBaz(baz);
        
        Bar bar2 = new Bar();
        bar2.setSomeInt(2);
        bar2.setSomeBaz(baz);
        
        ArrayList<Bar> bars = new ArrayList<Bar>();
        bars.add(bar1);
        bars.add(bar2);
        
        
        Foo foo = new Foo();
        foo.setSomeBar(bars);
        
        return foo;
    }
    
    static void checkReference(Foo foo)
    {
        Bar bar1 = foo.getSomeBar().get(0);
        assertNotNull(bar1);
        assert(bar1.getSomeInt() == 1);
        
        Bar bar2 = foo.getSomeBar().get(1);
        assertNotNull(bar2);
        assert(bar2.getSomeInt() == 2);
        
        // check if its same instance
        assertTrue(bar1.getSomeBaz() == bar2.getSomeBaz());
    }
    
    /*
        message ClubFounder {
            optional string name = 1;
            optional Club club = 2;
        }
        
        message Club {
            optional string name = 1;
            repeated Student student = 2;
            repeated Club partner_club = 3;
        }

        message Student {
            optional string name = 1;
            repeated Club club = 2;
        }
        
        ClubFounders:
          some_glee_club_founder
          
        Clubs:
          glee
          private_club_of_jake_partner_of_glee
        
        Students:
          jake
          john
          jane
          
        Links:
          (clubfounder-club)
          some_glee_club_founder -> glee
            
          (student-club)
          jake <- glee
          
          jake <-> private_club_of_jake_partner_of_glee
          
          john <-> glee
          
          jane <-> glee
          
          (club-club)
          glee <-> private_club_of_jake_partner_of_glee
        
     */
    public void testCyclic() throws Exception
    {
        ClubFounder founder = new ClubFounder();
        founder.setName("some_glee_club_founder");
        
        Club gleeClub = new Club();
        gleeClub.setName("glee");
        
        founder.setClub(gleeClub);

        addPartnerStudentTo(gleeClub, "jake");
        
        // cyclic
        Student john = new Student();
        john.setName("john");
        john.addClub(gleeClub);
        gleeClub.addStudent(john);
        
        Student jane = new Student();
        jane.setName("jane");
        jane.addClub(gleeClub);
        gleeClub.addStudent(jane);
        
        // check that our scenario is coded correctly.
        checkLinks(founder);
        
        byte[] data = toByteArray(founder, ClubFounder.getSchema());
        
        
        // from byte array
        
        ClubFounder founderFromByteArray = new ClubFounder();
        mergeFrom(data, 0, data.length, founderFromByteArray, ClubFounder.getSchema());
        
        // verify parsed message
        checkLinks(founderFromByteArray);
        
        
        // from inputstream
        
        ClubFounder founderFromStream = new ClubFounder();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, founderFromStream, ClubFounder.getSchema());
        
        // verify parsed message
        checkLinks(founderFromStream);
    }
    
    static void addPartnerStudentTo(Club club, String studentName)
    {
        Student student = new Student();
        student.setName(studentName);
        // non-cyclic
        club.addStudent(student);
        
        Club privateClub = new Club();
        privateClub.setName("private_club_of_" + studentName + 
                "_partner_of_" + club.getName());
        
        // cyclic
        student.addClub(privateClub);
        privateClub.addStudent(student);
        
        // cyclic
        privateClub.addPartnerClub(club);
        club.addPartnerClub(privateClub);
    }
    
    static void checkLinks(ClubFounder founder)
    {
        assertNotNull(founder);
        
        assertEquals("some_glee_club_founder", founder.getName());
        
        Club glee = founder.getClub();
        
        assertNotNull(glee);
        
        assertTrue(glee.getStudentCount() == 3);
        
        assertTrue(glee.getPartnerClubCount() == 1);
        
        Student jake = glee.getStudent(0);
        Student john = glee.getStudent(1);
        Student jane = glee.getStudent(2);
        
        assertEquals("jake", jake.getName());
        assertEquals("john", john.getName());
        assertEquals("jane", jane.getName());
        
        assertTrue(jake.getClubCount() == 1);
        assertTrue(john.getClubCount() == 1);
        assertTrue(jane.getClubCount() == 1);
        
        // john and jane are cyclic to their club
        assertTrue(john.getClub(0) == glee);
        assertTrue(jane.getClub(0) == glee);
        
        // jake's club is linked to its private club only
        assertTrue(jake.getClub(0) != glee);
        
        Club privateClubOfJake = jake.getClub(0);
        assertNotNull(privateClubOfJake);
        
        assertTrue(privateClubOfJake.getStudentCount() == 1);
        assertTrue(privateClubOfJake.getStudent(0) == jake);
        
        // club-club cyclic link
        assertTrue(glee.getPartnerClub(0) == privateClubOfJake);
    }

}
