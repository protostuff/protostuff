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

package io.protostuff.runtime;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for abstract generic collection types.
 * 
 * @author David Yu
 * @created Sep 11, 2010
 */
public class CollectionTest
{

    static
    {
        // this is necessary to be able to map interfaces/abstract types to
        // their respective
        // implementations and to avoid including type metadata during
        // serialization.
        RuntimeSchema.map(ITask.class, Task.class);
        RuntimeSchema.map(AbstractEmployee.class, Employee.class);
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

    public static abstract class AbstractEmployee
    {
        public abstract void setId(int id);

        public abstract int getId();

        public abstract Collection<String> getDepartments();

        public abstract void setDepartments(Collection<String> departments);

        public abstract Collection<ITask> getTasks();

        public abstract void setTasks(Collection<ITask> tasks);

    }

    public static class Task implements ITask
    {

        private int id;
        private String description;
        private Collection<String> tags;
        private Date dateCreated;
        private BigInteger bigInteger;
        private BigDecimal bigDecimal;

        public Task()
        {

        }

        /**
         * @return the id
         */
        @Override
        public int getId()
        {
            return id;
        }

        /**
         * @param id
         *            the id to set
         */
        @Override
        public void setId(int id)
        {
            this.id = id;
        }

        /**
         * @return the description
         */
        @Override
        public String getDescription()
        {
            return description;
        }

        /**
         * @param description
         *            the description to set
         */
        @Override
        public void setDescription(String description)
        {
            this.description = description;
        }

        /**
         * @return the tags
         */
        @Override
        public Collection<String> getTags()
        {
            return tags;
        }

        /**
         * @param tags
         *            the tags to set
         */
        @Override
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
         * @param dateCreated
         *            the dateCreated to set
         */
        public void setDateCreated(Date dateCreated)
        {
            this.dateCreated = dateCreated;
        }

        /**
         * @return the bigInteger
         */
        public BigInteger getBigInteger()
        {
            return bigInteger;
        }

        /**
         * @param bigInteger
         *            the bigInteger to set
         */
        public void setBigInteger(BigInteger bigInteger)
        {
            this.bigInteger = bigInteger;
        }

        /**
         * @return the bigDecimal
         */
        public BigDecimal getBigDecimal()
        {
            return bigDecimal;
        }

        /**
         * @param bigDecimal
         *            the bigDecimal to set
         */
        public void setBigDecimal(BigDecimal bigDecimal)
        {
            this.bigDecimal = bigDecimal;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((bigDecimal == null) ? 0 : bigDecimal.hashCode());
            result = prime * result
                    + ((bigInteger == null) ? 0 : bigInteger.hashCode());
            result = prime * result
                    + ((dateCreated == null) ? 0 : dateCreated.hashCode());
            result = prime * result
                    + ((description == null) ? 0 : description.hashCode());
            result = prime * result + id;
            result = prime * result + ((tags == null) ? 0 : tags.hashCode());
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
            Task other = (Task) obj;
            if (bigDecimal == null)
            {
                if (other.bigDecimal != null)
                    return false;
            }
            else if (!bigDecimal.equals(other.bigDecimal))
                return false;
            if (bigInteger == null)
            {
                if (other.bigInteger != null)
                    return false;
            }
            else if (!bigInteger.equals(other.bigInteger))
                return false;
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
            return "Task [bigDecimal=" + bigDecimal + ", bigInteger="
                    + bigInteger + ", dateCreated=" + dateCreated
                    + ", description=" + description + ", id=" + id + ", tags="
                    + tags + "]";
        }

    }

    public static class Employee extends AbstractEmployee
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
        @Override
        public int getId()
        {
            return id;
        }

        /**
         * @param id
         *            the id to set
         */
        @Override
        public void setId(int id)
        {
            this.id = id;
        }

        /**
         * @return the departments
         */
        @Override
        public Collection<String> getDepartments()
        {
            return departments;
        }

        /**
         * @param departments
         *            the departments to set
         */
        @Override
        public void setDepartments(Collection<String> departments)
        {
            this.departments = departments;
        }

        /**
         * @return the tasks
         */
        @Override
        public Collection<ITask> getTasks()
        {
            return tasks;
        }

        /**
         * @param tasks
         *            the tasks to set
         */
        @Override
        public void setTasks(Collection<ITask> tasks)
        {
            this.tasks = tasks;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((departments == null) ? 0 : departments.hashCode());
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
            Employee other = (Employee) obj;
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
            return "Employee [departments=" + departments + ", id=" + id
                    + ", tasks=" + tasks + "]";
        }

    }

    static Task filledTask()
    {
        Collection<String> tags = new ArrayList<>();
        tags.add("Urgent");
        tags.add("Priority");

        Task task = new Task();
        task.setId(1);
        task.setDescription("Complete that other task.");
        task.setTags(tags);
        task.setDateCreated(new Date(System.currentTimeMillis()));
        task.setBigDecimal(new BigDecimal(564654.234234d));
        task.setBigInteger(BigInteger.valueOf(System.currentTimeMillis()));

        return task;
    }

    @Test
    public void testSimpleTask() throws Exception
    {
        Schema<Task> schema = RuntimeSchema.getSchema(Task.class);

        Task p = filledTask();

        byte[] data = ProtostuffIOUtil.toByteArray(p, schema,
                LinkedBuffer.allocate(512));

        Task p2 = new Task();
        ProtostuffIOUtil.mergeFrom(data, p2, schema);
        // System.err.println(p2);

        assertEquals(p, p2);
    }

    @Test
    public void testITask() throws Exception
    {
        // Because we mapped ITask to Task, this is ok.
        Schema<ITask> schema = RuntimeSchema.getSchema(ITask.class);

        ITask p = filledTask();

        byte[] data = ProtostuffIOUtil.toByteArray(p, schema,
                LinkedBuffer.allocate(512));

        ITask p2 = new Task();
        ProtostuffIOUtil.mergeFrom(data, p2, schema);
        // System.err.println(p2);

        assertEquals(p, p2);
    }

    static Employee filledEmployee()
    {
        Collection<String> departments = new ArrayList<>();
        departments.add("Engineering");
        departments.add("IT");

        Collection<ITask> tasks = new ArrayList<>();
        tasks.add(filledTask());

        Employee p = new Employee();

        p.setId(1);
        p.setDepartments(departments);
        p.setTasks(tasks);

        return p;
    }

    @Test
    public void testEmployee() throws Exception
    {
        Schema<Employee> schema = RuntimeSchema.getSchema(Employee.class);

        Employee p = filledEmployee();

        byte[] data = ProtostuffIOUtil.toByteArray(p, schema,
                LinkedBuffer.allocate(512));

        Employee p2 = new Employee();
        ProtostuffIOUtil.mergeFrom(data, p2, schema);
        // System.err.println(p2);

        assertEquals(p, p2);
    }

    @Test
    public void testIEmployee() throws Exception
    {
        // Because we mapped IEmployee to Employee, this is ok.
        Schema<AbstractEmployee> schema = RuntimeSchema
                .getSchema(AbstractEmployee.class);

        Collection<String> departments = new ArrayList<>();
        departments.add("Engineering");
        departments.add("IT");

        Collection<ITask> tasks = new ArrayList<>();
        tasks.add(filledTask());

        AbstractEmployee p = new Employee();

        p.setId(1);
        p.setDepartments(departments);
        p.setTasks(tasks);

        byte[] data = ProtostuffIOUtil.toByteArray(p, schema,
                LinkedBuffer.allocate(512));

        AbstractEmployee p2 = new Employee();
        ProtostuffIOUtil.mergeFrom(data, p2, schema);
        // System.err.println(p2);

        assertEquals(p, p2);
    }

    interface IFoo
    {

    }

    static abstract class AbstractFoo
    {

    }

    static class PojoWithMappedAbstractTypes
    {
        ITask task;
        AbstractEmployee employee;

        IFoo ifoo;
        AbstractFoo afoo;
    }

    @Test
    public void testPojoWithMappedAbstractTypes()
    {
        RuntimeSchema<PojoWithMappedAbstractTypes> schema = (RuntimeSchema<PojoWithMappedAbstractTypes>) RuntimeSchema
                .getSchema(PojoWithMappedAbstractTypes.class,
                        RuntimeEnv.ID_STRATEGY);

        assertTrue(schema.getFields().size() == 4);

        assertTrue(schema.getFields().get(0) instanceof RuntimeMessageField);
        assertTrue(schema.getFields().get(1) instanceof RuntimeMessageField);

        assertTrue(schema.getFields().get(2) instanceof RuntimeObjectField);
        assertTrue(schema.getFields().get(3) instanceof RuntimeDerivativeField);
    }

    static class Entry<T> {
        private Collection<T> collection = Collections.emptyList();
    }

    @Test
    public void testImmutableCollection() throws IOException {
        Schema<Entry> schema = RuntimeSchema
                .getSchema(Entry.class);

        Entry<String> originalObject = new Entry<>();
        Collection<String> collection = new HashSet<>();
        collection.add("hello");
        collection.add("world");

        originalObject.collection = collection;

        byte[] bytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ProtobufIOUtil.writeTo(bos, originalObject, schema, LinkedBuffer.allocate(1024));
            bytes = bos.toByteArray();
        }

        Entry deserializedObject = schema.newMessage();
        ProtobufIOUtil.mergeFrom(bytes, deserializedObject, schema);
        assertNotNull(deserializedObject);
        for (String word : originalObject.collection)
            assertTrue(deserializedObject.collection.contains(word));
    }

}
