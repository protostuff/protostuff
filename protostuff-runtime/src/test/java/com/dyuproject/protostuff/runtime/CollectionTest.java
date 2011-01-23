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

package com.dyuproject.protostuff.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import junit.framework.TestCase;

import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.Schema;

/**
 * Tests for abstract generic collection types.
 *
 * @author David Yu
 * @created Sep 11, 2010
 */
public class CollectionTest extends TestCase
{
    
    static
    {
        // this is necessary to be able to map interfaces to their respective 
        // implementations and to avoid including type metadata during serialization.
        RuntimeSchema.map(ITask.class, Task.class);
        RuntimeSchema.map(IEmployee.class, Employee.class);
    }
    
    public interface ITask
    {
        void setId(int id);
        int getId();
        
        String getDescription();
        void setDescription(String description);
        
        Collection<String> getTags();
        void setTags(Collection<String> tags);
    }
    
    public interface IEmployee
    {
        void setId(int id);
        int getId();
        
        Collection<String> getDepartments();
        void setDepartments(Collection<String> departments);
        
        Collection<ITask> getTasks();
        void setTasks(Collection<ITask> tasks);
        
    }
    
    public static class Task implements ITask
    {
        
        int id;
        String description;
        Collection<String> tags;
        Date dateCreated;
        
        public Task()
        {
            
        }

        /**
         * @return the id
         */
        public int getId()
        {
            return id;
        }

        /**
         * @param id the id to set
         */
        public void setId(int id)
        {
            this.id = id;
        }

        /**
         * @return the description
         */
        public String getDescription()
        {
            return description;
        }

        /**
         * @param description the description to set
         */
        public void setDescription(String description)
        {
            this.description = description;
        }

        /**
         * @return the tags
         */
        public Collection<String> getTags()
        {
            return tags;
        }

        /**
         * @param tags the tags to set
         */
        public void setTags(Collection<String> tags)
        {
            this.tags = tags;
        }

        /**
         * @return the dateCreated
         */
        public Date getDateCreated()
        {
            return dateCreated;
        }

        /**
         * @param dateCreated the dateCreated to set
         */
        public void setDateCreated(Date dateCreated)
        {
            this.dateCreated = dateCreated;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((dateCreated == null)?0:dateCreated.hashCode());
            result = prime * result + ((description == null)?0:description.hashCode());
            result = prime * result + id;
            result = prime * result + ((tags == null)?0:tags.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Task other = (Task)obj;
            if (dateCreated == null)
            {
                if (other.dateCreated != null)
                    return false;
            }
            else if (!dateCreated.equals(other.dateCreated))
                return false;
            if (description == null)
            {
                if (other.description != null)
                    return false;
            }
            else if (!description.equals(other.description))
                return false;
            if (id != other.id)
                return false;
            if (tags == null)
            {
                if (other.tags != null)
                    return false;
            }
            else if (!tags.equals(other.tags))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "Task [dateCreated=" + dateCreated + ", description=" + description + ", id=" + id + ", tags=" + tags + "]";
        }
        
        
        
        
    }

    
    public static class Employee implements IEmployee
    {
        
        int id;
        Collection<String> departments;
        Collection<ITask> tasks;
        
        public Employee()
        {
            
        }

        /**
         * @return the id
         */
        public int getId()
        {
            return id;
        }

        /**
         * @param id the id to set
         */
        public void setId(int id)
        {
            this.id = id;
        }

        /**
         * @return the departments
         */
        public Collection<String> getDepartments()
        {
            return departments;
        }

        /**
         * @param departments the departments to set
         */
        public void setDepartments(Collection<String> departments)
        {
            this.departments = departments;
        }

        /**
         * @return the tasks
         */
        public Collection<ITask> getTasks()
        {
            return tasks;
        }

        /**
         * @param tasks the tasks to set
         */
        public void setTasks(Collection<ITask> tasks)
        {
            this.tasks = tasks;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((departments == null) ? 0 : departments.hashCode());
            result = prime * result + id;
            result = prime * result + ((tasks == null) ? 0 : tasks.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Employee other = (Employee)obj;
            if (departments == null)
            {
                if (other.departments != null)
                    return false;
            }
            else if (!departments.equals(other.departments))
                return false;
            if (id != other.id)
                return false;
            if (tasks == null)
            {
                if (other.tasks != null)
                    return false;
            }
            else if (!tasks.equals(other.tasks))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "Employee [departments=" + departments + ", id=" + id + ", tasks=" + tasks + "]";
        }
        
        
        
    }
    
    static Task filledTask()
    {
        Collection<String> tags = new ArrayList<String>();
        tags.add("Urgent");
        tags.add("Priority");
        
        Task task = new Task();
        task.setId(1);
        task.setDescription("Complete that other task.");
        task.setTags(tags);
        task.setDateCreated(new Date(System.currentTimeMillis()));
        
        return task;
    }
    
    public void testSimpleTask() throws Exception
    {
        Schema<Task> schema = RuntimeSchema.getSchema(Task.class);
        
        Task p = filledTask();
        
        byte[] data = ProtostuffIOUtil.toByteArray(p, schema, LinkedBuffer.allocate(512));
        
        Task p2 = new Task();
        ProtostuffIOUtil.mergeFrom(data, p2, schema);
        System.err.println(p2);

        assertEquals(p, p2);
    }
    
    public void testITask() throws Exception
    {
        // Because we mapped ITask to Task, this is ok.
        Schema<ITask> schema = RuntimeSchema.getSchema(ITask.class);
        
        ITask p = filledTask();
        
        byte[] data = ProtostuffIOUtil.toByteArray(p, schema, LinkedBuffer.allocate(512));
        
        ITask p2 = new Task();
        ProtostuffIOUtil.mergeFrom(data, p2, schema);
        System.err.println(p2);

        assertEquals(p, p2);
    }
    
    static Employee filledEmployee()
    {
        Collection<String> departments = new ArrayList<String>();
        departments.add("Engineering");
        departments.add("IT");
        
        Collection<ITask> tasks = new ArrayList<ITask>();
        tasks.add(filledTask());
        
        Employee p = new Employee();

        p.setId(1);
        p.setDepartments(departments);
        p.setTasks(tasks);
        
        return p;
    }
    
    public void testEmployee() throws Exception
    {
        Schema<Employee> schema = RuntimeSchema.getSchema(Employee.class);
        
        Employee p = filledEmployee();

        byte[] data = ProtostuffIOUtil.toByteArray(p, schema, LinkedBuffer.allocate(512));
        
        Employee p2 = new Employee();
        ProtostuffIOUtil.mergeFrom(data, p2, schema);
        System.err.println(p2);
        
        assertEquals(p, p2);
    }
    
    public void testIEmployee() throws Exception
    {
        // Because we mapped IEmployee to Employee, this is ok.
        Schema<IEmployee> schema = RuntimeSchema.getSchema(IEmployee.class);
        
        Collection<String> departments = new ArrayList<String>();
        departments.add("Engineering");
        departments.add("IT");
        
        Collection<ITask> tasks = new ArrayList<ITask>();
        tasks.add(filledTask());
        
        IEmployee p = new Employee();

        p.setId(1);
        p.setDepartments(departments);
        p.setTasks(tasks);

        byte[] data = ProtostuffIOUtil.toByteArray(p, schema, LinkedBuffer.allocate(512));
        
        IEmployee p2 = new Employee();
        ProtostuffIOUtil.mergeFrom(data, p2, schema);
        System.err.println(p2);
        
        assertEquals(p, p2);
    }

}
