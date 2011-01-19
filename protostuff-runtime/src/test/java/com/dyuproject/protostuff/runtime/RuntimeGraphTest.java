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
import java.util.List;

import com.dyuproject.protostuff.AbstractTest;
import com.dyuproject.protostuff.GraphTest;
import com.dyuproject.protostuff.Schema;

/**
 * Test ser/deser of graph objects (references and cyclic dependencies) with runtime 
 * schemas.
 *
 * @author David Yu
 * @created Jan 19, 2011
 */
public class RuntimeGraphTest extends AbstractTest
{
    
    public static class ClubFounder
    {
        String name;
        Club club;
        
        // getters and setters

        // name

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        // club

        public Club getClub()
        {
            return club;
        }

        public void setClub(Club club)
        {
            this.club = club;
        }
        
        
    }
    
    public static class Club
    {
        String name;
        List<Student> student;
        List<Club> partnerClub;

        // getters and setters

        // name

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        // student

        public List<Student> getStudentList()
        {
            return student;
        }

        public void setStudentList(List<Student> student)
        {
            this.student = student;
        }

        public Student getStudent(int index)
        {
            return student == null ? null : student.get(index);
        }

        public int getStudentCount()
        {
            return student == null ? 0 : student.size();
        }

        public void addStudent(Student student)
        {
            if(this.student == null)
                this.student = new ArrayList<Student>();
            this.student.add(student);
        }

        // partnerClub

        public List<Club> getPartnerClubList()
        {
            return partnerClub;
        }

        public void setPartnerClubList(List<Club> partnerClub)
        {
            this.partnerClub = partnerClub;
        }

        public Club getPartnerClub(int index)
        {
            return partnerClub == null ? null : partnerClub.get(index);
        }

        public int getPartnerClubCount()
        {
            return partnerClub == null ? 0 : partnerClub.size();
        }

        public void addPartnerClub(Club partnerClub)
        {
            if(this.partnerClub == null)
                this.partnerClub = new ArrayList<Club>();
            this.partnerClub.add(partnerClub);
        }
    }
    
    public static class Student
    {
        String name;
        List<Club> club;
        
        // getters and setters

        // name

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        // club

        public List<Club> getClubList()
        {
            return club;
        }

        public void setClubList(List<Club> club)
        {
            this.club = club;
        }

        public Club getClub(int index)
        {
            return club == null ? null : club.get(index);
        }

        public int getClubCount()
        {
            return club == null ? 0 : club.size();
        }

        public void addClub(Club club)
        {
            if(this.club == null)
                this.club = new ArrayList<Club>();
            this.club.add(club);
        }
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
        Schema<ClubFounder> schema = RuntimeSchema.getSchema(ClubFounder.class);
        
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

        byte[] data = GraphTest.toByteArray(founder, schema);

        // from byte array

        ClubFounder founderFromByteArray = new ClubFounder();
        GraphTest.mergeFrom(data, 0, data.length, founderFromByteArray, schema);

        // verify parsed message
        checkLinks(founderFromByteArray);

        // from inputstream

        ClubFounder founderFromStream = new ClubFounder();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        GraphTest.mergeFrom(in, founderFromStream, schema);

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
        privateClub.setName("private_club_of_" + studentName + "_partner_of_" + club.getName());

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
    
    
    public static class LinkedNode
    {
        String name;
        LinkedNode next;
        
        public LinkedNode()
        {
            
        }
        
        public LinkedNode(String name, LinkedNode previous)
        {
            this.name = name;
            if(previous != null)
                previous.next = this;
        }
    }
    
    public static class LinkedNodeContainer
    {
        LinkedNode node;
    }
    
    public void testSelfReference() throws Exception
    {
        Schema<LinkedNodeContainer> schema = 
            RuntimeSchema.getSchema(LinkedNodeContainer.class);
        
        LinkedNode first = new LinkedNode("first", null);
        LinkedNode second = new LinkedNode("second", first);
        LinkedNode third = new LinkedNode("third", second);
        // self reference
        third.next = third;
        
        LinkedNodeContainer container = new LinkedNodeContainer();
        container.node = first;
        
        verifyGraph(container);
        
        byte[] data = GraphTest.toByteArray(container, schema);
        
        LinkedNodeContainer containerFromByteArray = new LinkedNodeContainer();
        GraphTest.mergeFrom(data, 0, data.length, containerFromByteArray, schema);
        
        verifyGraph(containerFromByteArray);
        
        LinkedNodeContainer containerFromStream = new LinkedNodeContainer();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        GraphTest.mergeFrom(in, containerFromStream, schema);
        
        verifyGraph(containerFromStream);
    }
    
    static void verifyGraph(LinkedNodeContainer container)
    {
        assertNotNull(container);
        
        LinkedNode first = container.node;
        assertNotNull(first);
        assertEquals(first.name, "first");
        
        LinkedNode second = first.next;
        assertNotNull(second);
        assertEquals(second.name, "second");
        
        LinkedNode third = second.next;
        assertNotNull(third);
        assertEquals(third.name, "third");
        
        assertTrue(third == third.next);
    }

}
