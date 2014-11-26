package org.sagemath.droid.models.gson;

import android.net.Uri;
import org.sagemath.droid.utils.UrlUtils;

/**
 * Response from server containing the permalink paths.
 *
 * @author Nikhil Peter Raj
 */
public class PermalinkResponse extends BaseResponse {

    private String query;
    private String zip;

    public String getQueryID() {
        return query;
    }

    public String getZip() {
        return zip;
    }

    public String getQueryURL() {
        Uri queryUri = Uri.parse(UrlUtils.getPermalinkURL())
                .buildUpon()
                .appendQueryParameter("q", getQueryID())
                .build();

        return queryUri.toString();
    }

    public Uri getZipURL(Uri serverUri) {
        return serverUri.buildUpon()
                .appendQueryParameter("z", getQueryID())
                .build();
    }
}
