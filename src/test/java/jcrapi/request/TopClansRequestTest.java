package jcrapi.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Michael Lieshoff
 */
public class TopClansRequestTest {

    @Test
    public void shouldCreateBuilder() {
        assertNotNull(TopClansRequest.builder());
    }

    @Test
    public void shouldCreateBuilderWithCorrectClass() {
        assertEquals(TopClansRequest.TopClansRequestBuilder.class, TopClansRequest.builder().getClass());
    }

    @Test
    public void shouldBeWithLocationKey() {
        assertEquals("abc", TopClansRequest.builder().locationKey("abc").build().getLocationKey());
    }

}