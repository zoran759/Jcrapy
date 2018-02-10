package jcrapi.request;

import com.google.common.base.Preconditions;

import java.util.List;

/**
 * @author Michael Lieshoff
 */
public class ClanRequest extends Request {

    private final String tag;

    private ClanRequest(String tag, int limit, List<String> excludes, List<String> includes) {
        super(limit, excludes, includes);
        Preconditions.checkNotNull(tag, "tag");
        Preconditions.checkArgument(tag.length() > 0, "tag");
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public static ClanRequestBuilder builder() {
        return new ClanRequestBuilder();
    }

    public static class ClanRequestBuilder extends RequestBuilder<ClanRequest, ClanRequestBuilder> {

        private String tag;

        public ClanRequestBuilder tag(String tag) {
            this.tag = tag;
            return getThis();
        }

        @Override
        public ClanRequest build() {
            return new ClanRequest(tag, limit, excludes, keys);
        }

        @Override
        public ClanRequestBuilder getThis() {
            return this;
        }

    }

}
