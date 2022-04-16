package tdd;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import tdd.dummies.NoTokenResource;
import tdd.dummies.OneTokenResource;
import tdd.dummies.TwoTokensResource;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(Parameterized.class)
public class BrowserTest {

    public static final String LOOKING_FOR_A = "a";
    public static final String LOOKING_FOR_B = "b";
    public static final String LOOKING_FOR_C = "c";
    public static final String GETTING_A = "a";
    public static final String GETTING_B = "b";
    public static final Object GETTING_NOTHING = null;

    @Parameterized.Parameter(0)
    public Resource<Page<String>> pageResource;

    @Parameterized.Parameter(1)
    public String target;

    @Parameterized.Parameter(2)
    public String expected;

    @BeforeClass
    public static void testOnePageResource() {
        final NoTokenResource noTokenResource = new NoTokenResource();
        var onePage = noTokenResource.nextPage(null);
        assertEquals(List.of("a"), onePage.getItems());
        assertNull(onePage.getNextPageToken());
    }

    @BeforeClass
    public static void testTwoTokensResource() {
        final OneTokenResource oneTokenResource = new OneTokenResource();

        var firstPage = oneTokenResource.nextPage(null);
        assertEquals(List.of("a"), firstPage.getItems());
        assertEquals("firstPage", firstPage.getNextPageToken());
        var secondPage = oneTokenResource.nextPage("firstPage");
        assertEquals(List.of("b"), secondPage.getItems());
        assertNull(secondPage.getNextPageToken());
    }

    @BeforeClass
    public static void testThreeTokensResource() {
        final TwoTokensResource twoTokensResource = new TwoTokensResource();

        var firstPage = twoTokensResource.nextPage(null);
        assertEquals(List.of("a"), firstPage.getItems());
        assertEquals("firstPage", firstPage.getNextPageToken());
        var secondPage = twoTokensResource.nextPage("firstPage");
        assertEquals(List.of("b"), secondPage.getItems());
        assertEquals("secondPage", secondPage.getNextPageToken());
        var thirdPage = twoTokensResource.nextPage("secondPage");
        assertNull(thirdPage.getItems());
        assertNull(thirdPage.getNextPageToken());
    }

    @Parameterized.Parameters
    public static Object[][] parameters() {
        return new Object[][]{
                {new NoTokenResource(), LOOKING_FOR_A, GETTING_A},
                {new NoTokenResource(), LOOKING_FOR_B, GETTING_NOTHING},

                {new OneTokenResource(), LOOKING_FOR_A, GETTING_A},
                {new OneTokenResource(), LOOKING_FOR_B, GETTING_B},
                {new OneTokenResource(), LOOKING_FOR_C, GETTING_NOTHING},

                {new TwoTokensResource(), LOOKING_FOR_A, GETTING_A},
                {new TwoTokensResource(), LOOKING_FOR_B, GETTING_B},
                {new TwoTokensResource(), LOOKING_FOR_C, GETTING_NOTHING},
        };
    }

    @Test
    public void parameterized() throws IOException {
        var stringService = new Browser<>(pageResource, String::equals);
        assertEquals(Optional.ofNullable(expected), stringService.search(target));
    }
}