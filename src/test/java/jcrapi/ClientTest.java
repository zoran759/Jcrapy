/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jcrapi;

import com.google.common.collect.ImmutableMap;
import jcrapi.model.ClanSearch;
import jcrapi.request.ClanBattlesRequest;
import jcrapi.request.ClanSearchRequest;
import jcrapi.request.ProfileRequest;
import jcrapi.request.ProfilesRequest;
import jcrapi.request.TopClansRequest;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author Michael Lieshoff
 */
public class ClientTest {

    private CrawlerFactory crawlerFactory;

    private Crawler crawler;

    @Before
    public void setUp() {
        crawlerFactory = Mockito.mock(CrawlerFactory.class);
        crawler = Mockito.mock(Crawler.class);
        when(crawlerFactory.createCrawler()).thenReturn(crawler);
    }

    @Test(expected = NullPointerException.class)
    public void failCreateBecauseNullUrl() {
        new Client(null, "abc", crawlerFactory);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failCreateBecauseEmptyUrl() {
        new Client("", "abc", crawlerFactory);
    }

    @Test(expected = NullPointerException.class)
    public void failCreateBecauseNullCrawlerFactory() {
        new Client("abc", "abc", null);
    }

    @Test
    public void shouldGetVersion() throws IOException {
        when(crawler.get("lala/version", createHeaders())).thenReturn("1.0");
        assertEquals("1.0", createClient().getVersion());
    }

    private Map<String,String> createHeaders() {
        return ImmutableMap.<String, String>builder().put("auth", "abc").build();
    }

    private Client createClient() {
        return new Client("lala/", "abc", crawlerFactory);
    }

    @Test(expected = NullPointerException.class)
    public void failGetProfileBecauseNullTag() throws IOException {
        createClient().getProfile((String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failGetProfileBecauseEmptyTag() throws IOException {
        createClient().getProfile("");
    }

    @Test
    public void shouldGetProfile() throws IOException {
        when(crawler.get("lala/player/xyz", createHeaders())).thenReturn("{}");
        assertNotNull(createClient().getProfile("xyz"));
    }

    @Test(expected = NullPointerException.class)
    public void failGetProfileBecauseNullRequest() throws IOException {
        createClient().getProfile((ProfileRequest) null);
    }

    @Test
    public void shouldGetProfileFromRequest() throws IOException {
        when(crawler.get("lala/player/xyz?limit=15&keys=a,b&excludes=x,y", createHeaders())).thenReturn("{}");
        assertNotNull(createClient().getProfile(ProfileRequest.builder()
                .limit(15)
                .keys(Arrays.asList("a", "b"))
                .excludes(Arrays.asList("x", "y"))
                .tag("xyz")
                .build()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failGetProfilesBecauseNullTag() throws IOException {
        createClient().getProfiles((List<String>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failGetProfilesBecauseEmptyTag() throws IOException {
        createClient().getProfiles(Collections.<String>emptyList());
    }

    @Test
    public void shouldGetProfiles() throws IOException {
        List<String> tags = createTags();
        when(crawler.get("lala/player/" + StringUtils.join(tags, ','), createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getProfiles(tags));
    }

    private List<String> createTags() {
        List<String> tags = new ArrayList<>();
        tags.add("xyz");
        tags.add("def");
        return tags;
    }

    @Test(expected = NullPointerException.class)
    public void failGetProfilesBecauseNullRequest() throws IOException {
        createClient().getProfiles((ProfilesRequest) null);
    }

    @Test
    public void shouldGetProfilesFromRequest() throws IOException {
        ProfilesRequest profilesRequest = ProfilesRequest.builder()
                .limit(15)
                .keys(Arrays.asList("a", "b"))
                .excludes(Arrays.asList("x", "y"))
                .tags(createTags())
                .build();
        when(crawler.get("lala/player/xyz,def?limit=15&keys=a,b&excludes=x,y", createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getProfiles(profilesRequest));
    }

    @Test
    public void shouldGetTopClans() throws IOException {
        when(crawler.get("lala/top/clans", createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getTopClans((String) null));
    }

    @Test
    public void shouldGetTopClansWithLocation() throws IOException {
        when(crawler.get("lala/top/clans/EU", createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getTopClans("EU"));
    }

    @Test
    public void shouldGetTopClansFromRequest() throws IOException {
        when(crawler.get("lala/top/clans", createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getTopClans(TopClansRequest.builder().build()));
    }

    @Test
    public void shouldGetTopClansWithLocationFromRequest() throws IOException {
        when(crawler.get("lala/top/clans/EU", createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getTopClans(TopClansRequest.builder().locationKey("EU").build()));
    }

    @Test(expected = NullPointerException.class)
    public void failGetClanBecauseNullTag() throws IOException {
        createClient().getClan((String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failGetClanBecauseEmptyTag() throws IOException {
        createClient().getClan("");
    }

    @Test
    public void shouldGetClan() throws IOException {
        when(crawler.get("lala/clan/xyz", createHeaders())).thenReturn("{}");
        assertNotNull(createClient().getClan("xyz"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failGetClansBecauseNullTag() throws IOException {
        createClient().getClans(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failGetClansBecauseEmptyTag() throws IOException {
        createClient().getClans(Collections.<String>emptyList());
    }

    @Test
    public void shouldGetClans() throws IOException {
        List<String> tags = createTags();
        when(crawler.get("lala/clan/" + StringUtils.join(tags, ','), createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getClans(tags));
    }

    @Test
    public void shouldGetClanSearch() throws IOException {
        when(crawler.get("lala/clan/search", createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getClanSearch((ClanSearch) null));
    }

    @Test
    public void shouldGetClanSearchWithParameters() throws IOException {
        ClanSearch clanSearch = new ClanSearch();
        clanSearch.setName("abc");
        clanSearch.setScore(2000);
        clanSearch.setMinMembers(20);
        clanSearch.setMaxMembers(50);
        when(crawler.get("lala/clan/search?name=abc&score=2000&minMembers=20&maxMembers=50", createHeaders()))
                .thenReturn("[{}]");
        assertNotNull(createClient().getClanSearch(clanSearch));
    }

    @Test
    public void shouldGetClanSearchWithEncodedParameters() throws IOException {
        ClanSearch clanSearch = new ClanSearch();
        clanSearch.setName("reddit+alpha");
        clanSearch.setScore(2000);
        clanSearch.setMinMembers(20);
        clanSearch.setMaxMembers(50);
        when(crawler.get("lala/clan/search?name=reddit%2Balpha&score=2000&minMembers=20&maxMembers=50", createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getClanSearch(clanSearch));
    }

    @Test
    public void shouldGetClanSearchFromRequest() throws IOException {
        when(crawler.get("lala/clan/search", createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getClanSearch(ClanSearchRequest.builder().build()));
    }

    @Test
    public void shouldGetClanSearchWithParametersFromRequest() throws IOException {
        ClanSearchRequest clanSearchRequest = ClanSearchRequest.builder()
                .name("abc")
                .score(2000)
                .minMembers(20)
                .maxMembers(50)
                .build();
        when(crawler.get("lala/clan/search?name=abc&score=2000&minMembers=20&maxMembers=50", createHeaders()))
                .thenReturn("[{}]");
        assertNotNull(createClient().getClanSearch(clanSearchRequest));
    }

    @Test
    public void shouldGetClanSearchWithEncodedParametersFromRequest() throws IOException {
        ClanSearchRequest clanSearchRequest = ClanSearchRequest.builder()
                .name("reddit+alpha")
                .score(2000)
                .minMembers(20)
                .maxMembers(50)
                .build();
        when(crawler.get("lala/clan/search?name=reddit%2Balpha&score=2000&minMembers=20&maxMembers=50", createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getClanSearch(clanSearchRequest));
    }

    @Test
    public void shouldGetTopPlayers() throws IOException {
        when(crawler.get("lala/top/players", createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getTopPlayers((String) null));
    }

    @Test
    public void shouldGetTopPlayersWithLocation() throws IOException {
        when(crawler.get("lala/top/players/EU", createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getTopPlayers("EU"));
    }

    @Test
    public void shouldGetTournaments() throws IOException {
        when(crawler.get("lala/tournaments/abc", createHeaders())).thenReturn("{}");
        assertNotNull(createClient().getTournaments("abc"));
    }

    @Test
    public void shouldGetConstants() throws IOException {
        when(crawler.get("lala/constants", createHeaders())).thenReturn("{}");
        assertNotNull(createClient().getConstants());
    }

    @Test
    public void shouldGetAllianceConstants() throws IOException {
        when(crawler.get("lala/constants/alliance/", createHeaders())).thenReturn("{}");
        assertNotNull(createClient().getAllianceConstants());
    }

    @Test
    public void shouldGetArenasConstants() throws IOException {
        when(crawler.get("lala/constants/arenas/", createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getArenasConstants());
    }

    @Test
    public void shouldGetBadgesConstants() throws IOException {
        when(crawler.get("lala/constants/badges/", createHeaders())).thenReturn("{}");
        assertNotNull(createClient().getBadgesConstants());
    }

    @Test
    public void shouldGetChestCycleConstants() throws IOException {
        when(crawler.get("lala/constants/chestCycle/", createHeaders())).thenReturn("{}");
        assertNotNull(createClient().getChestCycleConstants());
    }

    @Test
    public void shouldGetCountryCodesConstants() throws IOException {
        when(crawler.get("lala/constants/countryCodes/", createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getCountryCodesConstants());
    }

    @Test
    public void shouldGetRaritiesConstants() throws IOException {
        when(crawler.get("lala/constants/rarities/", createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getRaritiesConstants());
    }

    @Test
    public void shouldGetCardConstants() throws IOException {
        when(crawler.get("lala/constants/cards/", createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getCardsConstants());
    }

    @Test
    public void shouldGetEndpoint() throws IOException {
        when(crawler.get("lala/endpoints", createHeaders())).thenReturn("[]");
        assertNotNull(createClient().getEndpoints());
    }

    @Test
    public void shouldGetPopularClans() throws IOException {
        when(crawler.get("lala/popular/clans", createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getPopularClans());
    }

    @Test
    public void shouldGetPopularPlayers() throws IOException {
        when(crawler.get("lala/popular/players", createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getPopularPlayers());
    }

    @Test
    public void shouldGetPopularTournaments() throws IOException {
        when(crawler.get("lala/popular/tournaments", createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getPopularTournaments());
    }

    @Test
    public void shouldGetClanBattles() throws IOException {
        when(crawler.get("lala/clan/xyz/battles", createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getClanBattles("xyz"));
    }

    @Test
    public void shouldGetClanBattlesFromRequest() throws IOException {
        when(crawler.get("lala/clan/xyz/battles", createHeaders())).thenReturn("[{}]");
        assertNotNull(createClient().getClanBattles(ClanBattlesRequest.builder().tag("xyz").build()));
    }

    @Test
    public void shouldGetClanHistory() throws IOException {
        when(crawler.get("lala/clan/xyz/history", createHeaders())).thenReturn("{}");
        assertNotNull(createClient().getClanHistory("xyz"));
    }

}