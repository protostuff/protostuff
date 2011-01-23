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

import com.dyuproject.protostuff.ProtostuffPipeTest;
import com.dyuproject.protostuff.runtime.CollectionTest.Employee;
import com.dyuproject.protostuff.runtime.CollectionTest.Task;
import com.dyuproject.protostuff.runtime.MathObjectsTest.Payment;

/**
 * Test case for pipes generated at runtime.
 *
 * @author David Yu
 * @created Oct 9, 2010
 */
public class RuntimePipeTest extends ProtostuffPipeTest
{
    
    static <T> MappedSchema<T> getSchema(Class<T> typeClass)
    {
        return (MappedSchema<T>)RuntimeSchema.getSchema(typeClass);
    }
    
    public void testFoo() throws Exception
    {
        MappedSchema<Foo> schema = getSchema(Foo.class);
        
        Foo foo = SerializableObjects.foo;
        
        roundTrip(foo, schema, schema.getPipeSchema());
    }
    
    public void testBar() throws Exception
    {
        MappedSchema<Bar> schema = getSchema(Bar.class);
        
        Bar bar = SerializableObjects.bar;
        
        roundTrip(bar, schema, schema.getPipeSchema());
    }
    
    public void testBaz() throws Exception
    {
        MappedSchema<Baz> schema = getSchema(Baz.class);
        
        Baz baz = SerializableObjects.baz;
        
        roundTrip(baz, schema, schema.getPipeSchema());
    }
    
    public void testEmployee() throws Exception
    {
        MappedSchema<Employee> schema = getSchema(Employee.class);
        
        Employee emp = CollectionTest.filledEmployee();
        
        roundTrip(emp, schema, schema.getPipeSchema());
    }
    
    public void testTask() throws Exception
    {
        MappedSchema<Task> schema = getSchema(Task.class);
        
        Task task = CollectionTest.filledTask();
        
        roundTrip(task, schema, schema.getPipeSchema());
    }
    
    public void testPayment() throws Exception
    {
        MappedSchema<Payment> schema = getSchema(Payment.class);
        
        Payment payment = MathObjectsTest.filledPayment();
        
        roundTrip(payment, schema, schema.getPipeSchema());
    }
    
    public void testPojoWithArrayAndSet() throws Exception
    {
        MappedSchema<PojoWithArrayAndSet> schema = getSchema(PojoWithArrayAndSet.class);
        
        PojoWithArrayAndSet p = SerDeserTest.filledPojoWithArrayAndSet();
        
        roundTrip(p, schema, schema.getPipeSchema());
    }

}
