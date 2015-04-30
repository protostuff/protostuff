//========================================================================
//Copyright 2012 David Yu
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

package io.protostuff.compiler;

import static io.protostuff.compiler.Formatter.BUILTIN.PLURAL;
import static io.protostuff.compiler.Formatter.BUILTIN.SINGULAR;
import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link Formatter}.
 * 
 * @author David Yu
 * @author Kostiantyn Shchepanovskyi
 */
public class FomatterTest
{

    private void verify(Formatter f, String expect, String value)
    {
        assertEquals(expect, f.format(value));
    }

    @Test
    public void testCC()
    {
        final Formatter f = Formatter.BUILTIN.CC;

        verify(f, "someFoo", "some_foo");
        verify(f, "someFoo", "SomeFoo");

        // verify that it does not change anything
        verify(f, "someFoo", "someFoo");
    }

    @Test
    public void testCCU()
    {
        final Formatter f = Formatter.BUILTIN.CCU;

        verify(f, "someFoo_", "some_foo");
        verify(f, "someFoo_", "SomeFoo");
        verify(f, "someFoo_", "someFoo");
    }

    @Test
    public void testUC()
    {
        final Formatter f = Formatter.BUILTIN.UC;

        verify(f, "some_foo", "someFoo");
        verify(f, "some_foo", "SomeFoo");

        // verify that it does not change anything
        verify(f, "some_foo", "some_foo");
    }

    @Test
    public void testUCU()
    {
        final Formatter f = Formatter.BUILTIN.UCU;

        verify(f, "some_foo_", "someFoo");
        verify(f, "some_foo_", "SomeFoo");
        verify(f, "some_foo_", "some_foo");
    }

    @Test
    public void testUUC()
    {
        final Formatter f = Formatter.BUILTIN.UUC;

        verify(f, "SOME_FOO", "someFoo");
        verify(f, "SOME_FOO", "SomeFoo");
        verify(f, "SOME_FOO", "some_foo");

        // verify that it does not change anything
        verify(f, "SOME_FOO", "SOME_FOO");
    }

    @Test
    public void testPC()
    {
        final Formatter f = Formatter.BUILTIN.PC;

        verify(f, "SomeFoo", "someFoo");
        verify(f, "SomeFoo", "some_foo");

        // verify that it does not change anything
        verify(f, "SomeFoo", "SomeFoo");
    }

    @Test
    public void testPCS()
    {
        final Formatter f = Formatter.BUILTIN.PCS;

        verify(f, "Some Foo", "someFoo");
        verify(f, "Some Foo", "some_foo");
        verify(f, "Some Foo", "SomeFoo");

        // verify that it does not change anything
        verify(f, "Some Foo", "Some Foo");
    }

    @Test
    public void testPluralize(){
        assertEquals("octopi", PLURAL.format("octopus"));
        assertEquals("vertices", PLURAL.format("vertex"));
        assertEquals("oxen", PLURAL.format("ox"));
        assertEquals("books", PLURAL.format("book"));
        assertEquals("people", PLURAL.format("Person"));
        assertEquals("children", PLURAL.format("Child"));
        assertEquals("Addresses", PLURAL.format("Address"));
        assertEquals("money", PLURAL.format("money"));
        assertEquals("libraries", PLURAL.format("library"));
    }

    @Test
    public void testSingularize(){
        assertEquals("prognosis", SINGULAR.format("prognoses"));
        assertEquals("Analysis", SINGULAR.format("Analyses"));
        assertEquals("book", SINGULAR.format("books"));
        assertEquals("person", SINGULAR.format("people"));
        assertEquals("money", SINGULAR.format("money"));

        assertEquals("action", SINGULAR.format("actions"));
        assertEquals("availableBettingLimit", SINGULAR.format("availableBettingLimits"));
        assertEquals("availableExtendDuration", SINGULAR.format("availableExtendDurations"));
        assertEquals("betStat", SINGULAR.format("betStats"));
        assertEquals("bet", SINGULAR.format("bets"));
        assertEquals("brokenGame", SINGULAR.format("brokenGames"));
        assertEquals("capability", SINGULAR.format("capabilities"));
        assertEquals("card", SINGULAR.format("cards"));
        assertEquals("casinoClientProperty", SINGULAR.format("casinoClientProperties"));
        assertEquals("clientProperty", SINGULAR.format("clientProperties"));
        assertEquals("countRange", SINGULAR.format("countRanges"));
        assertEquals("deleted", SINGULAR.format("deleted"));
        assertEquals("delta", SINGULAR.format("delta"));
        assertEquals("gameType", SINGULAR.format("gameTypes"));
        assertEquals("history", SINGULAR.format("histories"));
        assertEquals("history", SINGULAR.format("history"));
        assertEquals("info", SINGULAR.format("infos"));
        assertEquals("limit", SINGULAR.format("limits"));
        assertEquals("maskUrl", SINGULAR.format("maskUrls"));
        assertEquals("metadata", SINGULAR.format("metadata"));
        assertEquals("offlineGame", SINGULAR.format("offlineGames"));
        assertEquals("playerInfo", SINGULAR.format("playerInfo"));
        assertEquals("playerInfo", SINGULAR.format("playerInfos"));
        assertEquals("position", SINGULAR.format("positions"));
        assertEquals("providerItem", SINGULAR.format("providerItems"));
        assertEquals("resolvedSideBet", SINGULAR.format("resolvedSideBets"));
        assertEquals("resultCount", SINGULAR.format("resultCounts"));
        assertEquals("result", SINGULAR.format("results"));
        assertEquals("serviceUrl", SINGULAR.format("serviceUrls"));
        assertEquals("setting", SINGULAR.format("settings"));
        assertEquals("statistic", SINGULAR.format("statistic"));
        assertEquals("stream", SINGULAR.format("streams"));
        assertEquals("suggestedBet", SINGULAR.format("suggestedBets"));
        assertEquals("supportedMessage", SINGULAR.format("supportedMessages"));
        assertEquals("tableHistory", SINGULAR.format("tableHistory"));
        assertEquals("tableStat", SINGULAR.format("tableStats"));
        assertEquals("table", SINGULAR.format("tables"));
        assertEquals("tag", SINGULAR.format("tags"));
        assertEquals("updated", SINGULAR.format("updated"));
        assertEquals("urlTypeList", SINGULAR.format("urlTypeList"));
        assertEquals("urlsList", SINGULAR.format("urlsList"));
        assertEquals("winnerListRange", SINGULAR.format("winnerListRanges"));
        assertEquals("winner", SINGULAR.format("winners"));
    }

    @Test
    public void testTrim() throws Exception {
        final Formatter f = Formatter.BUILTIN.TRIM;
        Assert.assertEquals("Some Foo", f.format("\n\n   Some Foo\n"));
    }

    @Test
    public void testCutL() throws Exception {
        final Formatter f = Formatter.BUILTIN.CUT_L;
        Assert.assertEquals("oo", f.format("foo"));
        Assert.assertEquals("", f.format(""));
    }

    @Test
    public void testCutR() throws Exception {
        final Formatter f = Formatter.BUILTIN.CUT_R;
        Assert.assertEquals("fo", f.format("foo"));
        Assert.assertEquals("", f.format(""));
    }
}
