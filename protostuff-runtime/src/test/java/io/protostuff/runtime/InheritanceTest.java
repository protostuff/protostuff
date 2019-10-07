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

package io.protostuff.runtime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;

/**
 * Test ser/deser for subclasses that inherit from non-abstract base types.
 * <p>
 * Run this standalone to execute the actual tests.
 * 
 * @author David Yu
 * @created Jan 21, 2011
 */
public class InheritanceTest extends AbstractTest
{
    final DefaultIdStrategy strategy = new DefaultIdStrategy(IdStrategy.DEFAULT_FLAGS | 
            IdStrategy.MORPH_NON_FINAL_POJOS);
    
    @Override
    protected IdStrategy newIdStrategy()
    {
        return strategy;
    }

    public static class InputDevice
    {
        private String name;

        public InputDevice()
        {
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            final InputDevice other = (InputDevice) obj;
            if ((this.name == null) ? (other.name != null) : !this.name
                    .equals(other.name))
            {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = 47 * hash + (this.name != null ? this.name.hashCode() : 0);
            return hash;
        }

    }

    public static class Mouse extends InputDevice
    {
        private int numberOfButtons;

        public Mouse()
        {
        }

        public int getNumberOfButtons()
        {
            return numberOfButtons;
        }

        public void setNumberOfButtons(int numberOfButtons)
        {
            this.numberOfButtons = numberOfButtons;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            final Mouse other = (Mouse) obj;
            if (this.numberOfButtons != other.numberOfButtons)
            {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = 83 * hash + this.numberOfButtons;
            return hash;
        }

    }

    public static class KeyBoard extends InputDevice
    {
        private int numberOfKeys;

        public KeyBoard()
        {
        }

        public int getNumberOfKeys()
        {
            return numberOfKeys;
        }

        public void setNumberOfKeys(int numberOfKeys)
        {
            this.numberOfKeys = numberOfKeys;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            final KeyBoard other = (KeyBoard) obj;
            if (this.numberOfKeys != other.numberOfKeys)
            {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode()
        {
            int hash = 5;
            hash = 67 * hash + this.numberOfKeys;
            return hash;
        }

    }

    public static class InputSystem
    {
        List<InputDevice> inputDevices;

        public InputSystem()
        {
        }

        public List<InputDevice> getInputDevices()
        {
            return inputDevices;
        }

        public void setInputDevices(List<InputDevice> inputDevices)
        {
            this.inputDevices = inputDevices;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            final InputSystem other = (InputSystem) obj;
            if (this.inputDevices != other.inputDevices
                    && (this.inputDevices == null || !this.inputDevices
                            .equals(other.inputDevices)))
            {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode()
        {
            int hash = 3;
            hash = 67
                    * hash
                    + (this.inputDevices != null ? this.inputDevices.hashCode()
                            : 0);
            return hash;
        }

    }

    public void testInheritance() throws IOException
    {
        Schema<InputSystem> schema = getSchema(InputSystem.class);
        InputSystem sys = new InputSystem();
        KeyBoard kb = new KeyBoard();
        Mouse ms = new Mouse();
        kb.setName("Test");
        kb.setNumberOfKeys(10);
        ms.setName("Test1");
        ms.setNumberOfButtons(2);
        List<InputDevice> devices = new ArrayList<InputDevice>();
        devices.add(ms);
        devices.add(kb);
        sys.setInputDevices(devices);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProtostuffIOUtil.writeTo(out, sys, schema, buf());
        byte[] listData = out.toByteArray();
        InputSystem deserSystem = new InputSystem();
        ByteArrayInputStream in = new ByteArrayInputStream(listData);
        ProtostuffIOUtil.mergeFrom(in, deserSystem, schema, buf());

        assertEquals(sys, deserSystem);
    }

    public void testInheritanceProtobuf() throws IOException
    {
        Schema<InputSystem> schema = getSchema(InputSystem.class);
        InputSystem sys = new InputSystem();
        KeyBoard kb = new KeyBoard();
        Mouse ms = new Mouse();
        kb.setName("Test");
        kb.setNumberOfKeys(10);
        ms.setName("Test1");
        ms.setNumberOfButtons(2);
        List<InputDevice> devices = new ArrayList<InputDevice>();
        devices.add(ms);
        devices.add(kb);
        sys.setInputDevices(devices);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProtobufIOUtil.writeTo(out, sys, schema, buf());
        byte[] listData = out.toByteArray();
        InputSystem deserSystem = new InputSystem();
        ByteArrayInputStream in = new ByteArrayInputStream(listData);
        ProtobufIOUtil.mergeFrom(in, deserSystem, schema, buf());

        assertEquals(sys, deserSystem);
    }

}
